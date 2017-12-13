package com.lxzl.erp.core.service.statement.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: 结算单服务类
 *
 * @author gaochao
 * @date 2017-12-06 15:34
 */
@Service("statementService")
public class StatementServiceImpl implements StatementService {

    private static final Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);

    @Override
    public ServiceResult<String, String> createStatementOrder(Date startDate, Date endDate) {
        ServiceResult<String, String> result = new ServiceResult<>();
        // TODO 查询在这期间范围内，欠款的用户的需要支付的订单。

        // 根据用户

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private void updateStatementOrder(String orderNo) {
        Date currentTime = new Date();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            return;
        }
        List<StatementOrderDetailDO> dbStatementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
        // 根据订单的变化，去掉退货的部分，增加差价的部分。就是剩余的每月的金额

        // 查询订单项下的所有设备及物料，如果归还了，就不再算钱了，  如果没归还，就继续算钱
    }

    @Override
    public ServiceResult<String, String> createNewStatementOrder(String orderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        List<StatementOrderDetailDO> dbStatementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
        if (CollectionUtil.isNotEmpty(dbStatementOrderDetailDOList)) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_CREATE_ERROR);
            return result;
        }

        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        Date rentStartTime = orderDO.getRentStartTime();
        String statementDay;
        // 根据订单信息，生成订单的结算单
        DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
        if (dataDictionaryDO == null) {
            statementDay = "31";
        } else {
            statementDay = dataDictionaryDO.getDataName();
        }
        Integer statementDays = Integer.parseInt(statementDay);

        List<StatementOrderDetailDO> statementOrderDetailDOList = new ArrayList<>();
        // 商品生成结算单
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPaymentCycle(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPayMode(), orderDO.getRentStartTime(), orderProductDO.getProductAmount(), orderDO.getBuyerCustomerId(), orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUser.getUserId());
                    statementOrderDetailDOList.add(statementOrderDetailDO);
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(statementDays, orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), orderDO.getBuyerCustomerId(), orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                            lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(orderDO.getBuyerCustomerId(), orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, rentStartTime, orderProductDO.getPayMode(), orderProductDO.getRentTimeLength(), orderProductDO.getProductAmount(), alreadyPaidAmount, currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderDO.getBuyerCustomerId(), orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, orderProductDO.getPaymentCycle(), orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), statementDays, orderProductDO.getPayMode(), currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                            lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                        }
                    }
                }
            }
        }

        // 物料生成结算单
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPaymentCycle(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPayMode(), orderDO.getRentStartTime(), orderMaterialDO.getMaterialAmount(), orderDO.getBuyerCustomerId(), orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUser.getUserId());
                    statementOrderDetailDOList.add(statementOrderDetailDO);
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(statementDays, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), orderDO.getBuyerCustomerId(), orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                            lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(orderDO.getBuyerCustomerId(), orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, rentStartTime, orderMaterialDO.getPayMode(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getMaterialAmount(), alreadyPaidAmount, currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderDO.getBuyerCustomerId(), orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), statementDays, orderMaterialDO.getPayMode(), currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                            lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                        }
                    }
                }
            }
        }

        // 同一个时间的做归集
        Map<Date, StatementOrderDO> statementOrderDOMap = new HashMap<>();
        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
            Date dateKey = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementExpectPayTime());
            StatementOrderDO statementOrderDO = statementOrderMapper.findByCustomerAndPayTime(orderDO.getBuyerCustomerId(), dateKey);
            if (statementOrderDO != null) {
                statementOrderDOMap.put(dateKey, statementOrderDO);
            }

            if (!statementOrderDOMap.containsKey(dateKey)) {
                statementOrderDO = new StatementOrderDO();
                statementOrderDO.setStatementOrderNo(GenerateNoUtil.generateStatementNo(currentTime));
                statementOrderDO.setCustomerId(orderDO.getBuyerCustomerId());
                statementOrderDO.setStatementExpectPayTime(dateKey);
                statementOrderDO.setStatementAmount(statementOrderDetailDO.getStatementDetailAmount());
                statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                statementOrderDO.setCreateUser(loginUser.getUserId().toString());
                statementOrderDO.setUpdateUser(loginUser.getUserId().toString());
                statementOrderDO.setCreateTime(currentTime);
                statementOrderDO.setUpdateTime(currentTime);
                statementOrderMapper.save(statementOrderDO);
            } else {
                statementOrderDO = statementOrderDOMap.get(dateKey);
                statementOrderDO.setStatementAmount(BigDecimalUtil.add(statementOrderDO.getStatementAmount(), statementOrderDetailDO.getStatementDetailAmount()));
                statementOrderMapper.update(statementOrderDO);
            }
            statementOrderDOMap.put(dateKey, statementOrderDO);
            statementOrderDetailDO.setStatementOrderId(statementOrderDO.getId());
        }
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            statementOrderDetailMapper.saveList(statementOrderDetailDOList);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    Integer calculateStatementMonthCount(Integer rentType, Integer rentTimeLength, Integer paymentCycle, int startDay, int statementDay) {
        Integer statementMonthCount = 1;
        // 如果租赁类型为次和天的，那么需要一次性付清，如果为月的，分期付
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return statementMonthCount;
        } else {
            if (paymentCycle == null || paymentCycle >= rentTimeLength) {
                return statementMonthCount;
            }
            statementMonthCount = rentTimeLength / paymentCycle;
            if (rentTimeLength % paymentCycle > 0) {
                statementMonthCount++;
            }
            if (startDay > statementDay) {
                statementMonthCount++;
            }
            return statementMonthCount;
        }
    }

    /**
     * 一次性付款时计算结算单明细
     */
    StatementOrderDetailDO calculateOneStatementOrderDetail(Integer rentType, Integer rentTimeLength, Integer payMode, Date rentStartTime, BigDecimal statementDetailAmount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId) {
        Calendar rentStartTimeCalendar = Calendar.getInstance();
        rentStartTimeCalendar.setTime(rentStartTime);
        Date statementEndTime = null, statementExpectPayTime = null;
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, rentTimeLength);
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
        }
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
        } else if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)) {
            statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
        }
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, statementDetailAmount, currentTime, loginUserId);
    }

    /**
     * 计算多期中的第一期明细
     */
    StatementOrderDetailDO calculateFirstStatementOrderDetail(Integer statementDays, Integer paymentCycle, Integer payMode, Date rentStartTime, BigDecimal unitAmount, Integer itemCount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId) {
        // 本月天数
        int thisMonthDays = DateUtil.getActualMaximum(rentStartTime);
        // 计算开始日期距离月底的天数
        int actualMaximumDays = DateUtil.betweenActualMaximumDays(rentStartTime);
        // 本月每天金额
        BigDecimal oneDayAmount = BigDecimalUtil.div(unitAmount, new BigDecimal(thisMonthDays), 4);
        // 本月的金额（开始日到本月底）
        BigDecimal thisMonthAmount = BigDecimalUtil.mul(new BigDecimal(actualMaximumDays), BigDecimalUtil.mul(oneDayAmount, new BigDecimal(itemCount)));
        // 本期未结算剩余整月份
        int surplusMonth = paymentCycle - 1;
        // 本期未计算整月的金额
        BigDecimal surplusMonthAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(unitAmount, new BigDecimal(itemCount)), new BigDecimal(surplusMonth));
        // 本期未结算剩余月数
        Date lastMonth = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, paymentCycle);
        // 计算最后一月的天数
        int lastMonthSurplusDays = DateUtil.betweenAppointDays(lastMonth, statementDays);
        // 后一月的总天数
        int lastMonthDays = DateUtil.getActualMaximum(lastMonth);
        // 最后一月每天金额
        BigDecimal lastMonthOneDayAmount = BigDecimalUtil.div(unitAmount, new BigDecimal(lastMonthDays), 2);
        // 最后一月的金额（开始日到本月底）
        BigDecimal lastMonthAmount = BigDecimalUtil.mul(new BigDecimal(lastMonthSurplusDays), BigDecimalUtil.mul(lastMonthOneDayAmount, new BigDecimal(itemCount)));
        // 第一期总金额
        BigDecimal firstPhaseAmount = BigDecimalUtil.add(BigDecimalUtil.add(thisMonthAmount, surplusMonthAmount), lastMonthAmount);
        Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, actualMaximumDays);
        statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(statementEndTime, surplusMonth);
        statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, lastMonthSurplusDays);
        Date statementExpectPayTime;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
        } else {
            statementExpectPayTime = statementEndTime;
        }
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, firstPhaseAmount, currentTime, loginUserId);
    }

    /**
     * 计算多期中的最后一期明细
     */
    StatementOrderDetailDO calculateLastStatementOrderDetail(Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date lastCalculateDate, Date rentStartTime, Integer payMode, Integer rentTimeLength, BigDecimal itemAllAmount, BigDecimal alreadyPaidAmount, Date currentTime, Integer loginUserId) {
        // 最后一期
        Date lastPhaseStartTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(lastCalculateDate, 1);
        // 订单的最后日期
        Date orderLastDate = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
        // 最后一期的金额
        BigDecimal lastPhaseAmount = BigDecimalUtil.sub(itemAllAmount, alreadyPaidAmount);
        // 剩余的天数
        int surplusDays = DateUtil.daysBetween(lastPhaseStartTime, orderLastDate);
        // 最后一期的结束时间
        Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(lastPhaseStartTime, surplusDays);
        Date statementExpectPayTime;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = lastPhaseStartTime;
        } else {
            statementExpectPayTime = statementEndTime;
        }
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, lastPhaseStartTime, statementEndTime, lastPhaseAmount, currentTime, loginUserId);
    }

    /**
     * 计算多期中的中间期明细
     */
    StatementOrderDetailDO calculateMiddleStatementOrderDetail(Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date lastCalculateDate, Integer paymentCycle, BigDecimal itemUnitAmount, Integer itemCount, Integer statementDays, Integer payMode, Date currentTime, Integer loginUserId) {
        // 中间期数
        Date thisPhaseStartTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(lastCalculateDate, 1);
        Date thisPhaseDate = com.lxzl.se.common.util.date.DateUtil.monthInterval(lastCalculateDate, paymentCycle);
        // 本期总金额
        BigDecimal middlePhaseAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(itemUnitAmount, new BigDecimal(itemCount)), new BigDecimal(paymentCycle));
        Date statementEndTime = thisPhaseDate;
        int lastMonthSurplusDays = DateUtil.betweenAppointDays(statementEndTime, statementDays);
        Calendar statementEndTimeCalendar = Calendar.getInstance();
        statementEndTimeCalendar.setTime(statementEndTime);
        if (statementEndTimeCalendar.get(Calendar.DAY_OF_MONTH) < lastMonthSurplusDays) {
            statementEndTimeCalendar.set(Calendar.DAY_OF_MONTH, lastMonthSurplusDays);
            statementEndTime = statementEndTimeCalendar.getTime();
        }
        Date statementExpectPayTime;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = thisPhaseStartTime;
        } else {
            statementExpectPayTime = statementEndTime;
        }
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, thisPhaseStartTime, statementEndTime, middlePhaseAmount, currentTime, loginUserId);
    }

    StatementOrderDetailDO buildStatementOrderDetailDO(Integer customerId, Integer orderType, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date statementExpectPayTime, Date startTime, Date endTime, BigDecimal statementDetailAmount, Date currentTime, Integer loginUserId) {
        StatementOrderDetailDO statementOrderDetailDO = new StatementOrderDetailDO();
        statementOrderDetailDO.setCustomerId(customerId);
        statementOrderDetailDO.setOrderType(orderType);
        statementOrderDetailDO.setOrderId(orderId);
        statementOrderDetailDO.setOrderItemType(orderItemType);
        statementOrderDetailDO.setOrderItemReferId(orderItemReferId);
        statementOrderDetailDO.setStatementOrderId(1);
        statementOrderDetailDO.setStatementExpectPayTime(statementExpectPayTime);
        statementOrderDetailDO.setStatementStartTime(startTime);
        statementOrderDetailDO.setStatementEndTime(endTime);
        statementOrderDetailDO.setStatementDetailAmount(statementDetailAmount);
        statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
        statementOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        statementOrderDetailDO.setCreateTime(currentTime);
        statementOrderDetailDO.setUpdateTime(currentTime);
        statementOrderDetailDO.setCreateUser(loginUserId.toString());
        statementOrderDetailDO.setUpdateUser(loginUserId.toString());
        return statementOrderDetailDO;
    }


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;
}
