package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.order.impl.support.OrderSupport;
import com.lxzl.erp.core.service.statement.impl.StatementServiceImpl;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderProductDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * 换货单结算
 *
 * @author: huanglong
 * @since: 2018/9/10/010
 */
@Component
public class StatementReplaceOrderSupport {

    /**
     * 结算换货单（调用失败时，需要外部处理事务回滚）
     *
     * @param replaceOrderNo
     * @return
     */
    public String createStatement(String replaceOrderNo) {
        Assert.notNull(replaceOrderNo, "换货单号为空，不能进行结算");
        //增加换货退货记录，修改结算单总金额
        ReplaceOrderDO replaceOrderDO = replaceOrderMapper.findByOrderNo(replaceOrderNo);
        if (replaceOrderDO == null) {
            return ErrorCode.CHANGE_ORDER_NOT_EXISTS;
        }
        OrderDO orderDO = orderMapper.findByOrderIdSimple(replaceOrderDO.getOrderId());
        if (orderDO == null) {
            return ErrorCode.ORDER_NOT_EXISTS;
        }
        //初始化订单项缓存
        Map<Integer, OrderProductDO> orderProductDOMap = new HashMap<>();
        Map<Integer, OrderMaterialDO> orderMaterialDOMap = new HashMap<>();
        initOrderItemCatch(orderDO, orderProductDOMap, orderMaterialDOMap);
        //获取时间轴
        Date startTime, endTime;
        if (CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderDO.getIsReletOrderReplace())) {
            ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(replaceOrderDO.getReletOrderNo());
            startTime = reletOrderDO.getRentStartTime();
            endTime = reletOrderDO.getExpectReturnTime();
        } else {
            startTime = orderDO.getRentStartTime();
            endTime = orderSupport.generateExpectReturnTime(orderDO);
        }

        //todo 获取换货单所属订单或者续租单生命周期
        Date currentTime = new Date();
        Integer loginUserId = userSupport.getCurrentUserId();
        Date changeTime = replaceOrderDO.getReplaceTime();
        Integer buyerCustomerId = replaceOrderDO.getCustomerId();
        //换货前一天原单结束
        Date endTimeAfterChange = DateUtil.getDayByOffset(changeTime, -1);
        Integer changeOrderId = replaceOrderDO.getId();
        List<StatementOrderDetailDO> needUpdateStatementOrderDetailList = new ArrayList<>();
        //新增的结算单项，可能不存在结算单项需最后统一处理
        List<StatementOrderDetailDO> needUpdateOrderBeforeList = new ArrayList<>();
        Map<Integer, StatementOrderDO> needUpdateStatementOrderMap = new HashMap<>();
        BigDecimal totalReturnRentToAccount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(replaceOrderDO.getReplaceOrderProductDOList())) {
            for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderDO.getReplaceOrderProductDOList()) {
                Integer changeCount = replaceOrderProductDO.getProductCount();
                if (changeCount <= 0) continue;
                OrderProductDO oldProduct = orderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
                OrderProductDO newProduct = orderProductDOMap.get(replaceOrderProductDO.getNewOrderProductId());
                //需退押金
                BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(oldProduct.getRentDepositAmount(), new BigDecimal(oldProduct.getProductCount()), BigDecimalUtil.SCALE), new BigDecimal(changeCount));
                BigDecimal thisReturnDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(oldProduct.getDepositAmount(), new BigDecimal(oldProduct.getProductCount()), BigDecimalUtil.SCALE), new BigDecimal(changeCount));

                //原订单项关联结算
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, replaceOrderProductDO.getOldOrderProductId());

                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    BigDecimal hasSettleRentAmount = BigDecimal.ZERO;
                    BigDecimal totalAmount = newProduct.getProductAmount();
                    List<StatementOrderDetailDO> generateStatementDetailList = new ArrayList<>();
                    //处理押金
                    for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                        if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                            //押金未支付不允许换货
                            if (!StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                                return ErrorCode.DEPOSIT_NOT_PAID_BEFORE_REPLACE_ORDER;
                            }

                            StatementOrderDO statementOrderDO = getStatementOrderFromCatchOrDB(needUpdateStatementOrderMap, statementOrderDetailDO.getStatementOrderId());

                            //保存退押金记录
                            BigDecimal realReturnDeposit = saveReturnDepositRecord(currentTime, thisReturnDepositAmount, statementOrderDetailDO, statementOrderDO, OrderType.ORDER_TYPE_REPLACE, changeOrderId, replaceOrderProductDO.getId(), loginUserId, changeTime);
                            BigDecimal realReturnRentDeposit = saveReturnRentDepositRecord(currentTime, thisReturnRentDepositAmount, statementOrderDetailDO, statementOrderDO, OrderType.ORDER_TYPE_REPLACE, changeOrderId, replaceOrderProductDO.getId(), loginUserId, changeTime);
                            generateStatementDetailList.add(statementOrderDetailDO);
                            //todo 新增新商品项押金结算
                        }
                    }
                    //处理租金
                    //原被换商品有结算单，则新换货商品与之对齐，否则新商品单独结算
                    if (BigDecimalUtil.compare(oldProduct.getProductAmount(), BigDecimal.ZERO) > 0) {
                        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                            //只处理订单租金（根据订单租金生成相应换货结算）
                            if (!OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetailDO.getOrderType())) continue;
                            //在换货时间轴外
                            if (DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(), endTimeAfterChange) <= 0) {
                                continue;
                            }
                            //订单或续租单时间轴之外跳过
                            if (DateUtil.daysBetween(endTime, statementOrderDetailDO.getStatementStartTime()) > 0 || DateUtil.daysBetween(statementOrderDetailDO.getStatementStartTime(), startTime) > 0)
                                continue;
                            //截断期
                            if (DateUtil.daysBetween(statementOrderDetailDO.getStatementStartTime(), endTimeAfterChange) >= 0) {
                                Integer oldDays = DateUtil.daysBetween(statementOrderDetailDO.getStatementStartTime(), statementOrderDetailDO.getStatementEndTime()) + 1;
                                Integer nowDays = DateUtil.daysBetween(changeTime, statementOrderDetailDO.getStatementEndTime()) + 1;
                                BigDecimal timePercent = BigDecimalUtil.div(new BigDecimal(nowDays), new BigDecimal(oldDays), BigDecimalUtil.SCALE);
                                GenerateRentStatementResult rentStatementResult = generateRentStatement(oldProduct, newProduct, currentTime, loginUserId, changeTime, buyerCustomerId, generateStatementDetailList, needUpdateStatementOrderMap, replaceOrderProductDO, statementOrderDetailDO, timePercent, true);
                                BigDecimal newRentAmount = rentStatementResult.getNewRentAmount();
                                hasSettleRentAmount = BigDecimalUtil.add(hasSettleRentAmount, newRentAmount);
                            }
                            //完整期
                            else {
                                GenerateRentStatementResult rentStatementResult = generateRentStatement(oldProduct, newProduct, currentTime, loginUserId, changeTime, buyerCustomerId, generateStatementDetailList, needUpdateStatementOrderMap, replaceOrderProductDO, statementOrderDetailDO, BigDecimal.ONE, false);
                                BigDecimal newRentAmount = rentStatementResult.getNewRentAmount();
                                BigDecimal needReturnRentToAccount = rentStatementResult.getNeedReturnRentToAccount();
                                totalReturnRentToAccount = BigDecimalUtil.add(totalReturnRentToAccount, needReturnRentToAccount);
                                hasSettleRentAmount = BigDecimalUtil.add(hasSettleRentAmount, newRentAmount);
                            }
                        }
                        //存在的误差归到在最后一期
                        fillGapAmountToLastPhase(needUpdateStatementOrderMap, hasSettleRentAmount, totalAmount, generateStatementDetailList);
                        needUpdateStatementOrderDetailList.addAll(generateStatementDetailList);
                    } else {
                        Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderDO.getStatementDate(), orderDO.getRentStartTime());
                        List<StatementOrderDetailDO> statementDetailDOList = new StatementServiceImpl().generateOrderProductStatement(orderDO.getRentTimeLength(), orderDO.getStatementDate(), currentTime, statementDays, loginUserId, changeTime, buyerCustomerId, newProduct.getOrderId(), newProduct, newProduct.getProductAmount(), false);
                        if (CollectionUtil.isNotEmpty(statementDetailDOList)) {
                            needUpdateOrderBeforeList.addAll(statementDetailDOList);
                        }
                    }

                }


            }
        }
        //增加换货下单记录，修改结算单总额
        //支付了的直接冲正换货下单记录
        //第一期多的钱不退
        //押金（增加退押金记录，增加新商品项需缴押金；计算差价）

        //必须在缓存中结算单更新之后处理新生成的结算单
        new StatementServiceImpl().saveStatementOrder(needUpdateOrderBeforeList, currentTime, loginUserId);
        //todo 退款
        return null;
    }

    private void fillGapAmountToLastPhase(Map<Integer, StatementOrderDO> needUpdateStatementOrderMap, BigDecimal hasSettleRentAmount, BigDecimal totalAmount, List<StatementOrderDetailDO> generateStatementDetailList) {
        if (CollectionUtil.isNotEmpty(generateStatementDetailList)) {
            BigDecimal totalAmountGap = BigDecimalUtil.sub(totalAmount, hasSettleRentAmount);
            if (BigDecimalUtil.compare(totalAmountGap, BigDecimal.ZERO) != 0) {
                for (int i = generateStatementDetailList.size() - 1; i >= 0; i--) {
                    StatementOrderDetailDO statementOrderDetailDO = generateStatementDetailList.get(i);
                    if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                        statementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentAmount(), totalAmountGap));
                        statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailAmount(), totalAmountGap));
                        StatementOrderDO statementOrderDO = getStatementOrderFromCatchOrDB(needUpdateStatementOrderMap, statementOrderDetailDO.getStatementOrderId());
                        statementOrderDO.setStatementAmount(BigDecimalUtil.add(statementOrderDO.getStatementAmount(), totalAmountGap));
                        statementOrderDO.setStatementRentAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentAmount(), totalAmountGap));
                        break;
                    }
                }
            }
        }
    }

    private GenerateRentStatementResult generateRentStatement(OrderProductDO oldProduct, OrderProductDO newProduct, Date currentTime, Integer loginUserId, Date changeTime, Integer buyerCustomerId, List<StatementOrderDetailDO> needUpdateStatementOrderDetailList, Map<Integer, StatementOrderDO> needUpdateStatementOrderMap, ReplaceOrderProductDO replaceOrderProductDO, StatementOrderDetailDO statementOrderDetailDO, BigDecimal timePercent, boolean isFirstChangePhase) {
        Assert.isTrue(BigDecimalUtil.compare(oldProduct.getProductAmount(), BigDecimal.ZERO) != 0, "此方法只默认与原订单结算对齐，原订单项租金必须大于零");
        BigDecimal returnRentToAccount = BigDecimal.ZERO;
        //退货租金
        BigDecimal returnRentAmount = BigDecimalUtil.mul(statementOrderDetailDO.getStatementDetailRentAmount(), timePercent, BigDecimalUtil.STANDARD_SCALE);

        //todo 结算单新增换货单列（处理换货单再换货，部分结换货结算单不能退）
        BigDecimal amountPercent = BigDecimalUtil.div(newProduct.getProductUnitAmount(), oldProduct.getProductUnitAmount(), BigDecimalUtil.SCALE);
        BigDecimal newRentAmount = BigDecimalUtil.mul(returnRentAmount, amountPercent, BigDecimalUtil.STANDARD_SCALE);
        //新订单商品项结算
        StatementOrderDetailDO newStatementOrderDetailDO = statementCommonSupport.buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_REPLACE, oldProduct.getOrderId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, oldProduct.getId(), statementOrderDetailDO.getStatementExpectPayTime(), changeTime, statementOrderDetailDO.getStatementEndTime(), newRentAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, null);
        if (newStatementOrderDetailDO != null) {
            newStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
            newStatementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
        }

        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus()) && BigDecimalUtil.compare(returnRentAmount, newRentAmount) > 0) {
            //支付了当期不退（将退的钱改成与教的钱相等）
            if (isFirstChangePhase) {
                returnRentAmount = newRentAmount;
            } else {
                returnRentToAccount = BigDecimalUtil.sub(returnRentAmount, newRentAmount);
                statementOrderDetailDO.setStatementDetailRentPaidAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentPaidAmount(), returnRentToAccount));
                StatementOrderDO statementOrderDO = getStatementOrderFromCatchOrDB(needUpdateStatementOrderMap, statementOrderDetailDO.getStatementOrderId());
                statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentPaidAmount(), returnRentToAccount));
                statementOrderDO.setStatementPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementPaidAmount(), returnRentToAccount));
            }
        }

        //换货单退货结算
        StatementOrderDetailDO oldStatementOrderDetailDO = statementCommonSupport.buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_REPLACE, oldProduct.getOrderId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, oldProduct.getId(), statementOrderDetailDO.getStatementExpectPayTime(), changeTime, statementOrderDetailDO.getStatementEndTime(), BigDecimalUtil.mul(returnRentAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, statementOrderDetailDO.getReletOrderItemReferId());
        if (oldStatementOrderDetailDO != null) {
            oldStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
            oldStatementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
        }
        needUpdateStatementOrderDetailList.add(oldStatementOrderDetailDO);
        needUpdateStatementOrderDetailList.add(newStatementOrderDetailDO);

        //新增租金
        BigDecimal increaseRentAmount = BigDecimalUtil.sub(newRentAmount, returnRentAmount);
        //修改结算单
        if (BigDecimalUtil.compare(increaseRentAmount, BigDecimal.ZERO) != 0) {
            StatementOrderDO statementOrderDO = getStatementOrderFromCatchOrDB(needUpdateStatementOrderMap, statementOrderDetailDO.getStatementOrderId());
            statementOrderDO.setStatementRentAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentAmount(), increaseRentAmount));
            statementOrderDO.setStatementAmount(BigDecimalUtil.add(statementOrderDO.getStatementAmount(), increaseRentAmount));
            if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) <= 0) {
                statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
            } else if (BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(), BigDecimal.ZERO) == 0) {
                statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
            } else if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), statementOrderDO.getStatementPaidAmount()) > 0) {
                statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
            } else if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), statementOrderDO.getStatementPaidAmount()) == 0) {
                statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
            }
        }
        return new GenerateRentStatementResult(returnRentAmount, newRentAmount, returnRentToAccount);
        //todo 补差价（差价金额很蛋疼）
        //todo 根据借宿时间判断最后一期（差价补全）
    }


    private BigDecimal saveReturnRentDepositRecord(Date currentTime, BigDecimal thisReturnRentDepositAmount, StatementOrderDetailDO statementOrderDetailDO, StatementOrderDO statementOrderDO, Integer orderType, Integer orderId, Integer orderItemId, Integer loginUserId, Date returnTime) {
        boolean isRentDepositCanReturn = BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount()), thisReturnRentDepositAmount) >= 0;
        if (isRentDepositCanReturn) {
            statementOrderDetailDO.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositReturnAmount(), thisReturnRentDepositAmount));
            statementOrderDetailDO.setStatementDetailRentDepositReturnTime(currentTime);
            statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositReturnAmount(), thisReturnRentDepositAmount));
            statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), StatementReturnType.RETURN_TYPE_RENT_DEPOSIT, statementOrderDetailDO.getOrderItemReferId(), thisReturnRentDepositAmount, returnTime, loginUserId, currentTime, orderType, orderId, orderItemId);
            return thisReturnRentDepositAmount;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal saveReturnDepositRecord(Date currentTime, BigDecimal thisReturnDepositAmount, StatementOrderDetailDO statementOrderDetailDO, StatementOrderDO statementOrderDO, Integer orderType, Integer orderId, Integer orderItemId, Integer loginUserId, Date returnTime) {
        boolean isDepositCanReturn = BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementOrderDetailDO.getStatementDetailDepositReturnAmount()), thisReturnDepositAmount) >= 0;
        if (isDepositCanReturn) {
            statementOrderDetailDO.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositReturnAmount(), thisReturnDepositAmount));
            statementOrderDetailDO.setStatementDetailDepositReturnTime(currentTime);
            statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositReturnAmount(), thisReturnDepositAmount));
            statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnDepositAmount, returnTime, loginUserId, currentTime, orderType, orderId, orderItemId);
            return thisReturnDepositAmount;
        }
        return BigDecimal.ZERO;
    }

    private StatementOrderDO getStatementOrderFromCatchOrDB(Map<Integer, StatementOrderDO> needUpdateStatementOrderMap, Integer statementOrderId) {
        if (!needUpdateStatementOrderMap.containsKey(statementOrderId)) {
            StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderId);
            needUpdateStatementOrderMap.put(statementOrderId, statementOrderDO);
        }
        return needUpdateStatementOrderMap.get(statementOrderId);
    }

    private void initOrderItemCatch(OrderDO orderDO, Map<Integer, OrderProductDO> orderProductDOMap, Map<Integer, OrderMaterialDO> orderMaterialDOMap) {
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                orderProductDOMap.put(orderProductDO.getId(), orderProductDO);
            }
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                orderMaterialDOMap.put(orderMaterialDO.getId(), orderMaterialDO);
            }
        }
    }

    class GenerateRentStatementResult {
        BigDecimal oldRentAmount;
        BigDecimal newRentAmount;
        BigDecimal needReturnRentToAccount;

        public GenerateRentStatementResult(BigDecimal oldRentAmount, BigDecimal newRentAmount, BigDecimal needReturnRentToAccount) {
            this.oldRentAmount = oldRentAmount;
            this.newRentAmount = newRentAmount;
            this.needReturnRentToAccount = needReturnRentToAccount;
        }

        public BigDecimal getOldRentAmount() {
            return oldRentAmount;
        }

        public void setOldRentAmount(BigDecimal oldRentAmount) {
            this.oldRentAmount = oldRentAmount;
        }

        public BigDecimal getNewRentAmount() {
            return newRentAmount;
        }

        public void setNewRentAmount(BigDecimal newRentAmount) {
            this.newRentAmount = newRentAmount;
        }

        public BigDecimal getNeedReturnRentToAccount() {
            return needReturnRentToAccount;
        }

        public void setNeedReturnRentToAccount(BigDecimal needReturnRentToAccount) {
            this.needReturnRentToAccount = needReturnRentToAccount;
        }
    }

    @Autowired
    private ReplaceOrderMapper replaceOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementReturnSupport statementReturnSupport;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private StatementCommonSupport statementCommonSupport;

    @Autowired
    private StatementOrderSupport statementOrderSupport;

    @Autowired
    private ReletOrderMapper reletOrderMapper;

    @Autowired
    private OrderSupport orderSupport;
}
