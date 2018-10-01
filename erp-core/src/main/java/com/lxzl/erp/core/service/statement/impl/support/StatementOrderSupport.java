package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.DingDingCommonMsg;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannel;
import com.lxzl.erp.common.domain.statement.AmountHasReturn;
import com.lxzl.erp.common.domain.statement.AmountNeedReturn;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.messagethirdchannel.MessageThirdChannelService;
import com.lxzl.erp.core.service.order.impl.support.OrderSupport;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.dingdingGroupMessageConfig.DingdingGroupMessageConfigMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderStatementDateChangeLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.dingdingGroupMessageConfig.DingdingGroupMessageConfigDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.order.OrderStatementDateChangeLogDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDetailDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.se.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-06 10:06
 */
@Component
public class StatementOrderSupport {

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private CustomerMapper customerMapper;


    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    public List<StatementOrderDO> getOverdueStatementOrderList(Integer customerId) {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        param.setIsOverdue(CommonConstant.COMMON_CONSTANT_YES);
        param.setStatementOrderCustomerId(customerId);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderQueryParam", param);
        return statementOrderMapper.listPage(maps);
    }

    public BigDecimal getShortRentReceivable(Integer customerId) {
        StatementOrderDetailQueryParam param = new StatementOrderDetailQueryParam();
        param.setCustomerId(customerId);
        param.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderDetailQueryParam", param);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listPage(maps);
        BigDecimal totalShortRentReceivable = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                totalShortRentReceivable = BigDecimalUtil.add(totalShortRentReceivable, statementOrderDetailDO.getStatementDetailAmount());
            }
        }
        return totalShortRentReceivable;
    }


    public BigDecimal getSubCompanyShortRentReceivable(Integer subCompanyId) {
        StatementOrderDetailQueryParam param = new StatementOrderDetailQueryParam();
        param.setSubCompanyId(subCompanyId);
        param.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderDetailQueryParam", param);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listPage(maps);
        BigDecimal totalShortRentReceivable = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                totalShortRentReceivable = BigDecimalUtil.add(totalShortRentReceivable, statementOrderDetailDO.getStatementDetailAmount());
            }
        }
        return totalShortRentReceivable;
    }

    /**
     * 计算结算时间
     *
     * @param statementDate
     * @param rentStartTime
     * @return
     */
    public Integer getCustomerStatementDate(Integer statementDate, Date rentStartTime) {
        if (statementDate == null) {
            DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
            statementDate = dataDictionaryDO == null ? StatementMode.STATEMENT_MONTH_END : Integer.parseInt(dataDictionaryDO.getDataName());
        }
        if (StatementMode.STATEMENT_MONTH_NATURAL.equals(statementDate)) {
            // 如果结算日为按月结算，那么就要自然日来结算
            Calendar rentStartTimeCalendar = Calendar.getInstance();
            rentStartTimeCalendar.setTime(rentStartTime);
            //如果是当月第一天
            if (DateUtil.isSameDay(rentStartTimeCalendar.getTime(), DateUtil.getStartMonthDate(rentStartTimeCalendar.getTime()))) {
                statementDate = StatementMode.STATEMENT_MONTH_END;
            } else {
                rentStartTimeCalendar.add(Calendar.DAY_OF_MONTH, -1);
                statementDate = rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH);
            }
        }
        return statementDate;
    }

    /**
     * 恢复结算单
     *
     * @param currentTime
     * @param statementOrderDetailDOList
     */
    public void reStatement(Date currentTime, Map<Integer, StatementOrderDO> statementCache, List<StatementOrderDetailDO> statementOrderDetailDOList) {
        //处理结算单
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                StatementOrderDO statementOrderDO = statementCache.get(statementOrderDetailDO.getStatementOrderId());
                //处理结算单总金额
                statementOrderDO.setStatementAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDO.getStatementAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE)));
                //处理结算租金押金金额
                statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositAmount()));
                //处理结算押金金额
                statementOrderDO.setStatementDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDetailDO.getStatementDetailDepositAmount()));
                //处理结算租金金额
                statementOrderDO.setStatementRentAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDetailDO.getStatementDetailRentAmount()));
                //处理结算单逾期金额
                statementOrderDO.setStatementOverdueAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOverdueAmount(), statementOrderDetailDO.getStatementDetailOverdueAmount()));
                //处理其他费用
                statementOrderDO.setStatementOtherAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOtherAmount(), statementOrderDetailDO.getStatementDetailOtherAmount()));
                //处理冲正金额
                statementOrderDO.setStatementCorrectAmount(BigDecimalUtil.sub(statementOrderDO.getStatementCorrectAmount(), statementOrderDetailDO.getStatementDetailCorrectAmount()));
                //处理已退设备
                statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositReturnAmount(), statementOrderDetailDO.getStatementDetailDepositReturnAmount()));
                //处理已退租金押金
                statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositReturnAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount()));
                statementOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                statementOrderDetailDO.setUpdateTime(currentTime);
                // K3退货回调时没有登录用户，设为superUser
                String updateUser = userSupport.getCurrentUser() == null ? CommonConstant.SUPER_USER_ID.toString() : userSupport.getCurrentUserId().toString();
                statementOrderDetailDO.setUpdateUser(updateUser);
                statementOrderDetailMapper.update(statementOrderDetailDO);
            }
            for (Integer key : statementCache.keySet()) {
                StatementOrderDO statementOrderDO = statementCache.get(key);
                if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), statementOrderDO.getStatementPaidAmount()) <= 0) {
                    statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
                }
                statementOrderDO.setUpdateTime(currentTime);
                // K3退货回调时没有登录用户，设为superUser
                String updateUser = userSupport.getCurrentUser() == null ? CommonConstant.SUPER_USER_ID.toString() : userSupport.getCurrentUserId().toString();
                statementOrderDO.setUpdateUser(updateUser);
                statementOrderMapper.update(statementOrderDO);
            }
        }
    }

    /**
     * 恢复结算单已支付金额
     */
    public void reStatementPaid(Map<Integer, StatementOrderDO> statementOrderDOMap, List<StatementOrderDetailDO> statementOrderDetailDOList) {
        //处理结算单
        //缓存查询到的结算单
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                StatementOrderDO statementOrderDO = statementOrderDOMap.get(statementOrderDetailDO.getStatementOrderId());
                BigDecimal statementDetailOtherPaidAmount = statementOrderDetailDO.getStatementDetailOtherPaidAmount();
                BigDecimal statementDetailRentDepositPaidAmount = statementOrderDetailDO.getStatementDetailRentDepositPaidAmount();
                BigDecimal statementDetailDepositPaidAmount = statementOrderDetailDO.getStatementDetailDepositPaidAmount();
                BigDecimal statementDetailRentPaidAmount = statementOrderDetailDO.getStatementDetailRentPaidAmount();
                BigDecimal statementDetailOverduePaidAmount = statementOrderDetailDO.getStatementDetailOverduePaidAmount();

                //处理结算单已支付租金押金金额
                statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositPaidAmount(), statementDetailRentDepositPaidAmount));
                //处理结算单已支付押金金额
                statementOrderDO.setStatementDepositPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositPaidAmount(), statementDetailDepositPaidAmount));
                //处理结算单已支付租金金额
                statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentPaidAmount(), statementDetailRentPaidAmount));
                //处理结算单已支付逾期金额
                statementOrderDO.setStatementOverduePaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOverduePaidAmount(), statementDetailOverduePaidAmount));
                //处理结算单已支付其他费用
                statementOrderDO.setStatementOtherPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOtherPaidAmount(), statementDetailOtherPaidAmount));
            }
            for (Integer key : statementOrderDOMap.keySet()) {
                StatementOrderDO statementOrderDO = statementOrderDOMap.get(key);
                statementOrderMapper.update(statementOrderDO);
            }
        }
    }

    public Map<Integer, StatementOrderDO> getStatementOrderByDetails(List<StatementOrderDetailDO> statementOrderDetailDOList) {
        //缓存查询到的结算单
        Map<Integer, StatementOrderDO> statementCache = new HashMap<>();
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                StatementOrderDO statementOrderDO = statementCache.get(statementOrderDetailDO.getStatementOrderId());
                if (statementOrderDO == null) {
                    statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
                    statementCache.put(statementOrderDO.getId(), statementOrderDO);
                }
            }
        }
        return statementCache;
    }

    public void recordStatementDateLog(String orderNo, Integer statementDate) {
        if (statementDate == null) {
            DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
            statementDate = dataDictionaryDO == null ? StatementMode.STATEMENT_MONTH_END : Integer.parseInt(dataDictionaryDO.getDataName());
        }
        Date now = new Date();
        OrderStatementDateChangeLogDO orderStatementDateChangeLogDO = new OrderStatementDateChangeLogDO();
        orderStatementDateChangeLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderStatementDateChangeLogDO.setStatementDate(statementDate);
        orderStatementDateChangeLogDO.setOrderNo(orderNo);
        orderStatementDateChangeLogDO.setCreateTime(now);
        orderStatementDateChangeLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        orderStatementDateChangeLogDO.setUpdateTime(now);
        orderStatementDateChangeLogDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        orderStatementDateChangeLogMapper.save(orderStatementDateChangeLogDO);
    }

    /**
     * 订单重算成功钉钉消息
     *
     * @param orderDO
     */
    public void sendOrderRestatementSuccess(OrderDO orderDO) {
        if (orderDO == null) return;
        sendReStatementDdMsg(orderDO.getOrderSellerName(), orderDO.getOrderSubCompanyName(), orderDO.getBuyerCustomerName(), orderDO.getOrderNo(), orderDO.getOrderSubCompanyId());

    }

    /**
     * 续租单重算成功
     *
     * @param reletOrderDO
     */
    public void sendReletOrderRestatementSuccess(ReletOrderDO reletOrderDO) {
        if (reletOrderDO == null) return;
        sendReStatementDdMsg(reletOrderDO.getOrderSellerName(), reletOrderDO.getOrderSellerName(), reletOrderDO.getBuyerCustomerName(), reletOrderDO.getOrderNo(), reletOrderDO.getOrderSubCompanyId());
    }

    private void sendReStatementDdMsg(String salemanName, String subCompanyName, String customerName, String orderNo, Integer subCompanyId) {
        List<DingdingGroupMessageConfigDO> dingdingGroupMessageConfigDOS = dingdingGroupMessageConfigMapper.findBySendTypeAndSubCompanyId(DingDingGroupMessageType.SEND_TYPE_STATEMENT_RECALCU, subCompanyId);
        if (CollectionUtil.isEmpty(dingdingGroupMessageConfigDOS)) return;
        for (DingdingGroupMessageConfigDO dingdingGroupMessageConfigDO : dingdingGroupMessageConfigDOS) {
            DingDingCommonMsg dingDingCommonMsg = new DingDingCommonMsg();
            dingDingCommonMsg.setUserGroupUrl(dingdingGroupMessageConfigDO.getDingdingGroupUrl());
            dingDingCommonMsg.setContent(dingdingGroupMessageConfigDO.getMessageContent());
            dingDingSupport.dingDingSendMessage(dingDingCommonMsg, dingdingGroupMessageConfigDO.getMessageTitle(), subCompanyName, salemanName, customerName, orderNo);
        }
    }

    /**
     * 清除结算详情关联冲正
     *
     * @param list
     */
    public void clearStatementRefCorrect(List<StatementOrderDetailDO> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        //数据准备
        List<Integer> ids = new ArrayList<>();
        Set<Integer> orderIds = new HashSet<>();
        for (StatementOrderDetailDO orderDetailDO : list) {
            ids.add(orderDetailDO.getId());
            orderIds.add(orderDetailDO.getOrderId());
        }
        List<StatementOrderCorrectDetailDO> statementOrderCorrectDetailDOS = statementOrderCorrectDetailMapper.findByStatementDetailIds(ids);
        if (CollectionUtil.isEmpty(statementOrderCorrectDetailDOS)) {
            return;
        }
        List<Integer> statementOrderCorrectIds = new ArrayList<>();
        for (StatementOrderCorrectDetailDO statementOrderCorrectDetailDO : statementOrderCorrectDetailDOS) {
            statementOrderCorrectIds.add(statementOrderCorrectDetailDO.getStatementOrderCorrectId());
        }
        //删除冲正单和关联明细
        statementOrderCorrectMapper.deleteByIds(statementOrderCorrectIds);
        statementOrderCorrectDetailMapper.deleteByIds(ids);
        StringBuilder sb = null;
        //目前只会涉及一个订单
        for (Integer id : orderIds) {
            OrderDO orderDO = orderMapper.findByOrderId(id);
            if (orderDO != null && orderDO.getOrderSellerId() != null) {
                sb = new StringBuilder();
                sb.append("您的客户[").append(orderDO.getBuyerCustomerName()).append("]所下租赁订单（订单号：").append(orderDO.getOrderNo()).append("）由于重算冲正信息已被清除，请重新冲正！");
                MessageThirdChannel messageThirdChannel = new MessageThirdChannel();
                messageThirdChannel.setMessageContent(sb.toString());
                messageThirdChannel.setReceiverUserId(orderDO.getOrderSellerId());
                messageThirdChannelService.sendMessage(messageThirdChannel);
            }
        }
    }

    /**
     * 计算结算单项已退金额
     *
     * @param statementOrderDetailDO
     * @return
     */
    public AmountHasReturn getStatementItemHasReturn(StatementOrderDetailDO statementOrderDetailDO) {
        Integer returnType = null;
        if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
            returnType = StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT;
        } else if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
            returnType = StatementDetailType.STATEMENT_DETAIL_TYPE_RETURN_DEPOSIT;
        }
        //计算已退金额
        BigDecimal returnStatementAmount = BigDecimal.ZERO, returnStatementRentAmount = BigDecimal.ZERO, returnStatementDepositAmount = BigDecimal.ZERO, returnStatementRentDepositAmount = BigDecimal.ZERO;
        if (returnType != null) {
            List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByReturnReferIdAndStatementType(statementOrderDetailDO.getId(), returnType);
            if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                for (StatementOrderDetailDO sod : statementOrderDetailDOList) {
                    returnStatementRentAmount = BigDecimalUtil.add(returnStatementRentAmount, sod.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE);
                    returnStatementAmount = BigDecimalUtil.add(returnStatementAmount, sod.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE);
                    returnStatementDepositAmount = BigDecimalUtil.add(returnStatementDepositAmount, sod.getStatementDetailDepositAmount(), BigDecimalUtil.STANDARD_SCALE);
                    returnStatementRentDepositAmount = BigDecimalUtil.add(returnStatementRentDepositAmount, sod.getStatementDetailRentDepositAmount(), BigDecimalUtil.STANDARD_SCALE);
                }
            }
        }
        return new AmountHasReturn(returnStatementAmount, returnStatementRentAmount, returnStatementDepositAmount, returnStatementRentDepositAmount);
    }


    /**
     * 修正单个结算单开始结束时间，状态等
     *
     * @param
     * @return
     * @author ZhaoZiXuan
     * @date 2018/8/17 10:25
     */
    public void fixOneStatementOrderStatementTime(StatementOrderDO statementOrderDO) {
        if (statementOrderDO == null) {
            return;
        }
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByStatementOrderId(statementOrderDO.getId());

        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            Date minStartTime = null, maxEndTime = null;
            for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                StatementOrderDetailDO orderDetailDO = statementOrderDetailDOList.get(i);
                if (i == 0) {
                    minStartTime = orderDetailDO.getStatementStartTime();
                    maxEndTime = orderDetailDO.getStatementEndTime();
                } else {
                    if (minStartTime.compareTo(orderDetailDO.getStatementStartTime()) > 0)
                        minStartTime = orderDetailDO.getStatementStartTime();
                    if (maxEndTime.compareTo(orderDetailDO.getStatementEndTime()) < 0)
                        maxEndTime = orderDetailDO.getStatementEndTime();
                }
            }
            if (DateUtil.daysBetween(minStartTime, statementOrderDO.getStatementStartTime()) != 0 || DateUtil.daysBetween(maxEndTime, statementOrderDO.getStatementEndTime()) != 0) {
                statementOrderDO.setStatementStartTime(minStartTime);
                statementOrderDO.setStatementEndTime(maxEndTime);
            }
        }

        if (BigDecimalUtil.compare(BigDecimal.ZERO, statementOrderDO.getStatementAmount()) == 0) {
            if (CollectionUtil.isEmpty(statementOrderDetailDOList)) {
                statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            } else {
                statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
            }
        } else if (BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(), statementOrderDO.getStatementAmount()) == 0) {
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        } else if (BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(), BigDecimal.ZERO) > 0) {
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
        } else {
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
        }
        statementOrderMapper.update(statementOrderDO);
    }

    /**
     * stop the test machine order in a special day
     * change the end time of the order,update the statement orders and return part of amount
     * old order`s rentType is date,has never return
     *
     * @param orderNo
     * @param changeTime
     * @return
     */
    public String stopTestMachineOrder(String orderNo, Date changeTime) {
        Assert.notNull(orderNo, "订单号不能为空!");
        Assert.notNull(changeTime, "转单时间不能为空!");
        OrderDO orderDO = orderMapper.findByOrderNoSimple(orderNo);
        if (orderDO == null) {
            return ErrorCode.ORDER_NOT_EXISTS;
        }
        CustomerDO customerDO = customerMapper.findById(orderDO.getBuyerCustomerId());
        if (customerDO == null) {
            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
                customerDO = customerMapper.findByName(orderDO.getBuyerCustomerName());
                if (customerDO == null) {
                    return ErrorCode.CUSTOMER_NOT_EXISTS;
                }
                orderDO.setBuyerCustomerId(customerDO.getId());
                orderDO.setBuyerCustomerNo(customerDO.getCustomerNo());
            } else {
                return ErrorCode.CUSTOMER_NOT_EXISTS;
            }
        }
        boolean isTestMachineOrder = isTestMachineOrder(orderDO);
        if (!isTestMachineOrder) {
            return ErrorCode.IS_NOT_TEST_MECHANINE_ORDER;
        }
        Date stopTime = DateUtil.getDayByOffset(changeTime, -1);

        //考虑到续租会覆盖原订单的归还时间
        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);
        boolean isStopTimeInOrderLifcycle = DateUtil.daysBetween(orderDO.getRentStartTime(), stopTime) >= 0 && DateUtil.daysBetween(stopTime, expectReturnTime) >= 0;
        if (!isStopTimeInOrderLifcycle) {
            return ErrorCode.TEST_MECHANINE_ORDER_CHANGE_TIME_ERROR;
        }
        int oldTimeLength = DateUtil.daysBetween(orderDO.getRentStartTime(), expectReturnTime) + 1;
        int timeLength = DateUtil.daysBetween(orderDO.getRentStartTime(), stopTime) + 1;
        if (oldTimeLength == 0) {
            return ErrorCode.ORDER_RENT_TIME_LENGTH_IS_ZERO_OR_IS_NULL;
        }
        BigDecimal percent = BigDecimalUtil.div(new BigDecimal(timeLength), new BigDecimal(oldTimeLength), BigDecimalUtil.SCALE);

        //是否测试机正常结束转单
        boolean isLastDayChangeOrder = DateUtil.daysBetween(stopTime, expectReturnTime) == 0;
        if (!isLastDayChangeOrder) {
            String userId = userSupport.getCurrentUserId() == null ? null : userSupport.getCurrentUserId().toString();
            Date updateTime = new Date();
            List<StatementOrderDetailDO> statementOrderDetailDOS = statementOrderDetailMapper.findByOrderId(orderDO.getId());
            List<StatementOrderDetailDO> needUpdateDetailDOList = new ArrayList<>();
            Map<Integer, StatementOrderDO> statementOrderDOMap = new HashMap<>();
            BigDecimal needReturnRentAmount = BigDecimal.ZERO;
            BigDecimal needReturnRentDepositAmount = BigDecimal.ZERO;
            BigDecimal needReturnDepositAmount = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(statementOrderDetailDOS)) {
                for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOS) {
                    if (statementOrderDetailDO.getReletOrderItemReferId() != null) {
                        return ErrorCode.TEST_MECHANINE_ORDER_HAS_RELET;
                    }
                    AmountNeedReturn amountNeedReturn = modifyStatementItem(percent, statementOrderDOMap, needUpdateDetailDOList, statementOrderDetailDO, userId, updateTime, stopTime);
                    if (amountNeedReturn != null) {

                        needReturnRentAmount = BigDecimalUtil.add(needReturnRentAmount, amountNeedReturn.getRentPaidAmount());
                        needReturnRentDepositAmount = BigDecimalUtil.add(needReturnRentDepositAmount, amountNeedReturn.getRentDepositPaidAmount());
                        needReturnDepositAmount = BigDecimalUtil.add(needReturnDepositAmount, amountNeedReturn.getDepositPaidAmount());
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(needUpdateDetailDOList)) {
                statementOrderDetailMapper.batchUpdate(needUpdateDetailDOList);
            }
            Collection<StatementOrderDO> needUpdateStatementOrderDOS = statementOrderDOMap.values();
            if (CollectionUtil.isNotEmpty(needUpdateStatementOrderDOS)) {
                for (StatementOrderDO statementOrderDO : needUpdateStatementOrderDOS) {
                    statementOrderMapper.update(statementOrderDO);
                }
            }

            //退款
            if (BigDecimalUtil.compare(BigDecimalUtil.add(needReturnRentAmount, needReturnRentDepositAmount, needReturnDepositAmount), BigDecimal.ZERO) > 0) {
                String payResultCode = paymentService.returnDepositExpand(customerDO.getCustomerNo(), needReturnRentAmount, BigDecimal.ZERO, needReturnRentDepositAmount, needReturnDepositAmount, "测试机转单退款");
                if (!ErrorCode.SUCCESS.equals(payResultCode)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return payResultCode;
                }
            }
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 清除结算信息
     *
     * @param paid
     * @param buyerCustomerNo
     * @param statementOrderDetailDOList
     * @return 返回需退还账户的金额
     */
    public ServiceResult<String, AmountNeedReturn> clearStatement(boolean paid, String buyerCustomerNo, List<StatementOrderDetailDO> statementOrderDetailDOList) {
        ServiceResult<String, AmountNeedReturn> result = new ServiceResult<>();
        Map<Integer, StatementOrderDO> statementOrderDOMap = getStatementOrderByDetails(statementOrderDetailDOList);
        reStatement(new Date(), statementOrderDOMap, statementOrderDetailDOList);
        //删除相关冲正单
        clearStatementRefCorrect(statementOrderDetailDOList);
        if (paid) {
            //已付设备押金
            BigDecimal depositPaidAmount = BigDecimal.ZERO;
            //已付其他费用
            BigDecimal otherPaidAmount = BigDecimal.ZERO;
            // 已付租金
            BigDecimal rentPaidAmount = BigDecimal.ZERO;
            //已付逾期费用
            BigDecimal overduePaidAmount = BigDecimal.ZERO;
            //已付违约金
            BigDecimal penaltyPaidAmount = BigDecimal.ZERO;
            //已付租金押金
            BigDecimal rentDepositPaidAmount = BigDecimal.ZERO;
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                //计算所有已支付金额,由于付款是在冲正后做的，所以此时无需考虑冲正金额
                depositPaidAmount = BigDecimalUtil.add(depositPaidAmount, BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementOrderDetailDO.getStatementDetailDepositReturnAmount()));
                otherPaidAmount = BigDecimalUtil.add(otherPaidAmount, statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                rentPaidAmount = BigDecimalUtil.add(rentPaidAmount, statementOrderDetailDO.getStatementDetailRentPaidAmount());
                overduePaidAmount = BigDecimalUtil.add(overduePaidAmount, statementOrderDetailDO.getStatementDetailOverduePaidAmount());
                penaltyPaidAmount = BigDecimalUtil.add(penaltyPaidAmount, statementOrderDetailDO.getStatementDetailPenaltyPaidAmount());
                rentDepositPaidAmount = BigDecimalUtil.add(rentDepositPaidAmount, BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount()));
            }
            //处理结算单总状态及已支付金额
            reStatementPaid(statementOrderDOMap, statementOrderDetailDOList);
            AmountNeedReturn amountNeedReturn = new AmountNeedReturn();
            amountNeedReturn.setDepositPaidAmount(depositPaidAmount);
            amountNeedReturn.setRentDepositPaidAmount(rentDepositPaidAmount);
            amountNeedReturn.setOtherPaidAmount(otherPaidAmount);
            amountNeedReturn.setOverduePaidAmount(overduePaidAmount);
            amountNeedReturn.setPenaltyPaidAmount(penaltyPaidAmount);
            amountNeedReturn.setRentPaidAmount(rentPaidAmount);
//            if(BigDecimalUtil.compare(rentPaidAmount,BigDecimal.ZERO)!=0||BigDecimalUtil.compare(rentDepositPaidAmount,BigDecimal.ZERO)!=0||BigDecimalUtil.compare(depositPaidAmount,BigDecimal.ZERO)!=0||BigDecimalUtil.compare(BigDecimalUtil.addAll(otherPaidAmount, overduePaidAmount, penaltyPaidAmount),BigDecimal.ZERO)==0){
//                String returnCode = paymentService.returnDepositExpand(buyerCustomerNo, rentPaidAmount, BigDecimalUtil.addAll(otherPaidAmount, overduePaidAmount, penaltyPaidAmount)
//                        , rentDepositPaidAmount, depositPaidAmount, "重算结算单，已支付金额退还到客户余额");
//                if (!ErrorCode.SUCCESS.equals(returnCode)) {
//                    result.setErrorCode(returnCode);
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
//                    return result;
//                }
//            }
            result.setResult(amountNeedReturn);
        } else {
            //如果未支付，物理删除结算单详情，结算单
            if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                statementOrderDetailMapper.realDeleteStatementOrderDetailList(statementOrderDetailDOList);
            }
            List<StatementOrderDO> deleteStatementOrderDOList = new ArrayList<>();
            for (Integer key : statementOrderDOMap.keySet()) {
                StatementOrderDO statementOrderDO = statementOrderDOMap.get(key);
                if (CommonConstant.DATA_STATUS_DELETE.equals(statementOrderDO.getDataStatus())) {
                    deleteStatementOrderDOList.add(statementOrderDO);
                }
            }
            if (CollectionUtil.isNotEmpty(deleteStatementOrderDOList)) {
                statementOrderMapper.realDeleteStatementOrderList(deleteStatementOrderDOList);
            }

        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    private AmountNeedReturn modifyStatementItem(BigDecimal percent, Map<Integer, StatementOrderDO> statementOrderDOMap, List<StatementOrderDetailDO> needUpdateDetailDOList, StatementOrderDetailDO statementOrderDetailDO, String userId, Date updateTime, Date changeTime) {
        if (BigDecimalUtil.compare(statementOrderDetailDO.getStatementDetailCorrectAmount(), BigDecimal.ZERO) > 0) {
            throw new BusinessException(ErrorCode.EXIT_CORRECT_ORDER_NOT_ALLOW_CHANGE_ORDER,ErrorCode.getMessage(ErrorCode.EXIT_CORRECT_ORDER_NOT_ALLOW_CHANGE_ORDER));
        }
        BigDecimal rentDepositNeedReturn = BigDecimal.ZERO;
        BigDecimal depositNeedReturn = BigDecimal.ZERO;
        BigDecimal rentPaidAmountSub = BigDecimal.ZERO;

        BigDecimal oldRentAmount = statementOrderDetailDO.getStatementDetailRentAmount();

        BigDecimal oldRentDepositAmount = statementOrderDetailDO.getStatementDetailRentDepositAmount();
        BigDecimal oldRentDepositPaiAmount = statementOrderDetailDO.getStatementDetailRentDepositPaidAmount();
        BigDecimal oldRentDepositReturnAmount = statementOrderDetailDO.getStatementDetailRentDepositReturnAmount();
        boolean isRentDepositNeedModify = BigDecimalUtil.compare(oldRentDepositAmount, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(oldRentDepositAmount, oldRentDepositReturnAmount) > 0;


        BigDecimal oldDepositAmount = statementOrderDetailDO.getStatementDetailDepositAmount();
        BigDecimal oldDepositPaidAmount = statementOrderDetailDO.getStatementDetailDepositPaidAmount();
        BigDecimal oldDepositReturnAmount = statementOrderDetailDO.getStatementDetailDepositReturnAmount();
        boolean isDepositNeedModify = BigDecimalUtil.compare(oldDepositAmount, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(oldDepositAmount, oldDepositReturnAmount) > 0;
        boolean isRentNeedModify = BigDecimalUtil.compare(oldRentAmount, BigDecimal.ZERO) != 0;

        if (!isRentNeedModify && !isDepositNeedModify && !isRentDepositNeedModify) return null;

        Integer statementOrderId = statementOrderDetailDO.getStatementOrderId();
        StatementOrderDO statementOrderDO = getStatementOrderDO(statementOrderDOMap, statementOrderId);

        if (isRentDepositNeedModify) {
            //已交押金全退
            if (BigDecimalUtil.compare(oldRentDepositPaiAmount, oldRentDepositReturnAmount) > 0) {
                statementOrderDetailDO.setStatementDetailRentDepositReturnAmount(oldRentDepositPaiAmount);
                BigDecimal subReturnRentDeposit = BigDecimalUtil.sub(oldRentDepositPaiAmount, oldRentDepositReturnAmount);

                statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositReturnAmount(), subReturnRentDeposit));

                rentDepositNeedReturn = BigDecimalUtil.add(rentDepositNeedReturn, subReturnRentDeposit);
            }
            //未缴的押金无需再缴（将需付押金同步为已付押金）
            statementOrderDetailDO.setStatementDetailRentDepositAmount(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
            BigDecimal subRentDepositAmount = BigDecimalUtil.sub(oldRentDepositAmount, statementOrderDetailDO.getStatementDetailRentDepositAmount());

            statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(), subRentDepositAmount));

            statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), subRentDepositAmount));
        }

        if (isDepositNeedModify) {
            //已交设备押金全退
            if (BigDecimalUtil.compare(oldDepositPaidAmount, oldDepositReturnAmount) > 0) {
                statementOrderDetailDO.setStatementDetailDepositReturnAmount(oldDepositPaidAmount);
                BigDecimal subReturnDeposit = BigDecimalUtil.sub(oldDepositPaidAmount, oldDepositReturnAmount);

                statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositReturnAmount(), subReturnDeposit));

                depositNeedReturn = BigDecimalUtil.add(depositNeedReturn, subReturnDeposit);
            }
            //未缴的押金无需再缴（将需付押金同步为已付押金）
            statementOrderDetailDO.setStatementDetailDepositAmount(statementOrderDetailDO.getStatementDetailDepositPaidAmount());
            BigDecimal subDepositAmount = BigDecimalUtil.sub(oldDepositAmount, statementOrderDetailDO.getStatementDetailDepositAmount());

            statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(), subDepositAmount));
            statementOrderDO.setStatementDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), subDepositAmount));
        }

        if(isRentDepositNeedModify||isDepositNeedModify){
            if(BigDecimalUtil.compare(statementOrderDetailDO.getStatementDetailAmount(),BigDecimal.ZERO)==0){
                statementOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            }
        }

        //租金是否需要截断（占时间轴百分百则不需要截断）
        boolean thisPhaseAllRemove = BigDecimalUtil.compare(percent, BigDecimal.ZERO) == 0;
        if (isRentNeedModify) {
            //获取最新已退金额，如果需截断则为截断后的已退金额
            BigDecimal nowAllReturnRentAmount = modifyReturnRentStatementItem(needUpdateDetailDOList, statementOrderDetailDO, userId, updateTime, changeTime, statementOrderDO, thisPhaseAllRemove);

            BigDecimal rentAmount = BigDecimal.ZERO;
            BigDecimal rentAmountSub = BigDecimal.ZERO;
            BigDecimal oldRentPaidAmount = statementOrderDetailDO.getStatementDetailRentPaidAmount();
            if (thisPhaseAllRemove) {
                rentAmountSub = oldRentAmount;
                statementOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            } else {
                rentAmount = getMul(percent, oldRentAmount);

                rentAmountSub = BigDecimalUtil.sub(oldRentAmount, rentAmount);
                statementOrderDetailDO.setStatementDetailRentAmount(rentAmount);
                statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(), rentAmountSub, BigDecimalUtil.STANDARD_SCALE));
            }

            BigDecimal nowNeedPayRent = BigDecimalUtil.add(rentAmount, nowAllReturnRentAmount);
            if (BigDecimalUtil.compare(oldRentPaidAmount, nowNeedPayRent) > 0) {
                statementOrderDetailDO.setStatementDetailRentPaidAmount(nowNeedPayRent);
                rentPaidAmountSub = BigDecimalUtil.sub(oldRentPaidAmount, statementOrderDetailDO.getStatementDetailRentPaidAmount());
                statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentPaidAmount(), rentPaidAmountSub));
            }

            statementOrderDO.setStatementRentAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), rentAmountSub));
            statementOrderDO.setStatementAmount(BigDecimalUtil.sub(statementOrderDO.getStatementAmount(), rentAmountSub));
        }

        if (!thisPhaseAllRemove) {
            if (DateUtil.daysBetween(changeTime, statementOrderDetailDO.getStatementEndTime()) > 0) {
                statementOrderDetailDO.setStatementEndTime(changeTime);
            }
            //兼容部分特殊情况
            if (DateUtil.daysBetween(changeTime, statementOrderDetailDO.getStatementStartTime()) > 0) {
                statementOrderDetailDO.setStatementStartTime(changeTime);
            }
        }

        statementOrderDetailDO.setUpdateTime(updateTime);
        statementOrderDetailDO.setUpdateUser(userId);
        needUpdateDetailDOList.add(statementOrderDetailDO);


        statementOrderDO.setUpdateTime(updateTime);
        statementOrderDO.setUpdateUser(userId);

        AmountNeedReturn amountNeedReturn = new AmountNeedReturn();
        amountNeedReturn.setRentPaidAmount(rentPaidAmountSub);
        amountNeedReturn.setDepositPaidAmount(depositNeedReturn);
        amountNeedReturn.setRentDepositPaidAmount(rentDepositNeedReturn);
        return amountNeedReturn;
    }

    private BigDecimal modifyReturnRentStatementItem(List<StatementOrderDetailDO> needUpdateDetailDOList, StatementOrderDetailDO statementOrderDetailDO, String userId, Date updateTime, Date changeTime, StatementOrderDO statementOrderDO, boolean isThisPhaseAllRemove) {
        List<StatementOrderDetailDO> returnRentDetails = statementOrderDetailMapper.findByReturnReferIdAndStatementType(statementOrderDetailDO.getId(), StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
        if (CollectionUtil.isNotEmpty(returnRentDetails)) {
            BigDecimal returnRentAmount = BigDecimal.ZERO;
            for (StatementOrderDetailDO returnRentDetail : returnRentDetails) {
                if (isThisPhaseAllRemove) {
                    returnRentDetail.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                    returnRentAmount = BigDecimalUtil.add(returnRentAmount, returnRentDetail.getStatementDetailRentAmount());
                } else {
                    if (isThisPhaseNotNeedModify(changeTime, statementOrderDetailDO)) continue;
                    if (isThisPhaseNeedInterupt(changeTime, returnRentDetail)) {
                        BigDecimal percent = getRentLengthPercent(statementOrderDetailDO.getStatementStartTime(), changeTime, statementOrderDetailDO.getStatementEndTime());
                        BigDecimal oldReturnRentAmount = returnRentDetail.getStatementDetailRentAmount();
                        BigDecimal newReturnRentAmount = BigDecimalUtil.mul(oldReturnRentAmount, percent, BigDecimalUtil.STANDARD_SCALE);
                        BigDecimal returnRentGap = BigDecimalUtil.sub(oldReturnRentAmount, newReturnRentAmount);
                        returnRentDetail.setStatementDetailRentAmount(newReturnRentAmount);
                        returnRentDetail.setStatementDetailAmount(BigDecimalUtil.sub(returnRentDetail.getStatementDetailAmount(), returnRentGap));
                        returnRentDetail.setStatementEndTime(changeTime);
                        returnRentAmount = BigDecimalUtil.add(returnRentAmount, returnRentGap);
                    } else {
                        returnRentDetail.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                        returnRentAmount = BigDecimalUtil.add(returnRentAmount, returnRentDetail.getStatementDetailRentAmount());
                    }
                }
                returnRentDetail.setUpdateUser(userId);
                returnRentDetail.setUpdateTime(updateTime);
                needUpdateDetailDOList.add(returnRentDetail);
            }
            statementOrderDO.setStatementRentAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), returnRentAmount));

            BigDecimal nowAllReturnRentAmount = BigDecimal.ZERO;
            for (StatementOrderDetailDO returnRentDetail : returnRentDetails) {
                if (!CommonConstant.DATA_STATUS_ENABLE.equals(returnRentDetail.getDataStatus())) {
                    continue;
                }
                nowAllReturnRentAmount = BigDecimalUtil.add(nowAllReturnRentAmount, returnRentDetail.getStatementDetailRentAmount());
            }
            return nowAllReturnRentAmount;
        }
        return BigDecimal.ZERO;
    }

    private StatementOrderDO getStatementOrderDO(Map<Integer, StatementOrderDO> statementOrderDOMap, Integer statementOrderId) {
        if (!statementOrderDOMap.containsKey(statementOrderId)) {
            statementOrderDOMap.put(statementOrderId, statementOrderMapper.findById(statementOrderId));
        }
        return statementOrderDOMap.get(statementOrderId);
    }

    private BigDecimal getMul(BigDecimal amount, BigDecimal percent) {
        return BigDecimalUtil.mul(amount, percent, BigDecimalUtil.STANDARD_SCALE);
    }

    private boolean isTestMachineOrder(OrderDO orderDO) {
        return OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType()) && orderDO.getRentTimeLength() < 30;
    }

    /**
     * stop old order which is rentType of month
     *
     * @param orderNo
     * @param changeTime
     * @return
     */
    public Map<String, AmountNeedReturn> stopOldMonthRentOrder(String orderNo, Date changeTime) {
        Assert.notNull(orderNo, "订单号不能为空！");
        Assert.notNull(changeTime, "换单时间不能为空！");
        OrderDO orderDO = orderMapper.findByOrderNoSimple(orderNo);
        if (orderDO == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_EXISTS,ErrorCode.getMessage(ErrorCode.ORDER_NOT_EXISTS));
        }
        if (!OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
            throw new BusinessException(ErrorCode.ONLY_MONTH_RENT_ALLOW_CHANGE_ORDER,ErrorCode.getMessage(ErrorCode.ONLY_MONTH_RENT_ALLOW_CHANGE_ORDER));
        }
        Date stopTime = DateUtil.getDayByOffset(changeTime, -1);
        Map<Integer, StatementOrderDO> statementOrderDOMap = new HashMap<>();
        List<StatementOrderDetailDO> needUpdateDetailDOList = new ArrayList<>();
        Date currentTime = new Date();
        Integer userId = userSupport.getCurrentUserId();
        Map<String, AmountNeedReturn> returnAmountMap = new HashMap<>();
        //商品项处理
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                stopOrderItemMonthStatement(stopTime, statementOrderDOMap, needUpdateDetailDOList, currentTime, userId, returnAmountMap, orderProductDO.getOrderId(), OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
            }
        }
        //处理物料
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                stopOrderItemMonthStatement(stopTime, statementOrderDOMap, needUpdateDetailDOList, currentTime, userId, returnAmountMap, orderMaterialDO.getOrderId(), OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
            }
        }
        //更新结算详情
        if (CollectionUtil.isNotEmpty(needUpdateDetailDOList)) {
            statementOrderDetailMapper.batchUpdate(needUpdateDetailDOList);
        }
        //更新结算单
        if (statementOrderDOMap.size() > 0) {
            for (StatementOrderDO statementOrderDO : statementOrderDOMap.values()) {
                statementOrderMapper.update(statementOrderDO);
            }
        }
        return returnAmountMap;
    }

    private void stopOrderItemMonthStatement(Date stopTime, Map<Integer, StatementOrderDO> statementOrderDOMap, List<StatementOrderDetailDO> needUpdateDetailDOList, Date currentTime, Integer userId, Map<String, AmountNeedReturn> returnAmountMap, Integer orderId, Integer orderItemType, Integer orderItemId) {
        String uniqueKey = orderId + "_" + orderItemType + "_" + orderItemId;
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndIdIngnoreOrderType(orderItemType, orderItemId);
        if (CollectionUtil.isEmpty(statementOrderDetailDOList)) return;
        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
            //押金全退
            BigDecimal percent;
            if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                percent = new BigDecimal(CommonConstant.COMMON_ZERO);
            } else {
                //保留完整期
                if (isThisPhaseNotNeedModify(stopTime, statementOrderDetailDO)) continue;
                //需保留百分比，默认无需保留
                percent = new BigDecimal(CommonConstant.COMMON_ZERO);
                //如果是当期 则被截断，截断租金，截断关联退货
                if (isThisPhaseNeedInterupt(stopTime, statementOrderDetailDO)) {
                    percent = getRentLengthPercent(statementOrderDetailDO.getStatementStartTime(), stopTime, statementOrderDetailDO.getStatementEndTime());
                }
            }
            AmountNeedReturn amountNeedReturn = modifyStatementItem(percent, statementOrderDOMap, needUpdateDetailDOList, statementOrderDetailDO, userId == null ? CommonConstant.SUPER_USER_ID.toString() : userId.toString(), currentTime, stopTime);
            if (amountNeedReturn != null&&BigDecimalUtil.compare(amountNeedReturn.getTotalAmount(),BigDecimal.ZERO)>0) {
                if (!returnAmountMap.containsKey(uniqueKey)) {
                    returnAmountMap.put(uniqueKey, amountNeedReturn);
                } else {
                    returnAmountMap.get(uniqueKey).add(amountNeedReturn);
                }
            }
        }
    }

    private BigDecimal getRentLengthPercent(Date startTime, Date stopTime, Date endTime) {
        int oldTimeLength = DateUtil.daysBetween(startTime, endTime) + 1;
        int timeLength = DateUtil.daysBetween(startTime, stopTime) + 1;
        if (oldTimeLength == 0) {
            throw new BusinessException(ErrorCode.ORDER_RENT_TIME_LENGTH_IS_ZERO_OR_IS_NULL,ErrorCode.getMessage(ErrorCode.ORDER_RENT_TIME_LENGTH_IS_ZERO_OR_IS_NULL));
        }
        return BigDecimalUtil.div(new BigDecimal(timeLength), new BigDecimal(oldTimeLength), BigDecimalUtil.SCALE);
    }

    private boolean isThisPhaseNotNeedModify(Date stopTime, StatementOrderDetailDO statementOrderDetailDO) {
        return DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(), stopTime) >= 0;
    }

    private boolean isThisPhaseNeedInterupt(Date stopTime, StatementOrderDetailDO statementOrderDetailDO) {
        return DateUtil.daysBetween(statementOrderDetailDO.getStatementStartTime(), stopTime) >= 0 && DateUtil.daysBetween(stopTime, statementOrderDetailDO.getStatementEndTime()) > 0;
    }

    /**
     * 将租金押金补到新订单
     *
     * @param newOrderNo               新订单编号
     * @param oldAmountMap             老订单截断后需退还的金额
     * @param orderItemUnionKeyMapping 新订单商品索引与旧订单商品索引的映射关系 （key= orderId + "_" + orderItemType +"_"+orderItemId）
     * @return
     */
    public String fillOldOrderAmountToNew(String newOrderNo, Map<String, AmountNeedReturn> oldAmountMap, Map<String, String> orderItemUnionKeyMapping) {
        if (oldAmountMap == null || oldAmountMap.size() == 0) return ErrorCode.SUCCESS;

        OrderDO orderDO = orderMapper.findByOrderNo(newOrderNo);
        if (orderDO == null) {
            return ErrorCode.ORDER_NOT_EXISTS;
        }
        CustomerDO customerDO = customerMapper.findById(orderDO.getBuyerCustomerId());
        if (customerDO == null) {
            return ErrorCode.CUSTOMER_NOT_EXISTS;
        }
        //获取新订单的结算单，并将老订单的押金进行冲正新订单
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            Date nowTime = new Date();
            Map<Integer, StatementOrderDO> statementOrderDOMap = new HashMap<>();
            List<StatementOrderDetailDO> needUpdate = new ArrayList<>();
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                if (!StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType()))
                    continue;
                String uniqueKey = statementOrderDetailDO.getOrderId() + "_" + statementOrderDetailDO.getOrderItemType() + "_" + statementOrderDetailDO.getOrderItemReferId();
                String oldUnionKey = orderItemUnionKeyMapping.get(uniqueKey);
                if (oldUnionKey == null) continue;
                if (oldAmountMap.containsKey(oldUnionKey)) {
                    AmountNeedReturn amountNeedReturn = oldAmountMap.get(oldUnionKey);
                    //处理押金
                    BigDecimal newDeposit = statementOrderDetailDO.getStatementDetailDepositAmount();
                    if (BigDecimalUtil.compare(newDeposit, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(amountNeedReturn.getDepositPaidAmount(), BigDecimal.ZERO) > 0) {
                        //获取可冲正的金额
                        BigDecimal fillAmount = BigDecimalUtil.compare(newDeposit, amountNeedReturn.getDepositPaidAmount()) >= 0 ? amountNeedReturn.getDepositPaidAmount() : newDeposit;
                        statementOrderDetailDO.setStatementDetailDepositPaidAmount(fillAmount);
                        statementOrderDetailDO.setStatementDetailPaidTime(nowTime);
                        amountNeedReturn.setDepositPaidAmount(BigDecimalUtil.sub(amountNeedReturn.getDepositPaidAmount(), fillAmount));
                        StatementOrderDO statementOrderDO = getStatementOrderDO(statementOrderDOMap, statementOrderDetailDO.getStatementOrderId());
                        statementOrderDO.setStatementDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositPaidAmount(), fillAmount));
                        statementOrderDO.setStatementPaidTime(nowTime);
                        //根据已付金额修改结算状态
                        fixStatementStatus(statementOrderDetailDO, statementOrderDO);
                        if (!needUpdate.contains(statementOrderDetailDO)) {
                            needUpdate.add(statementOrderDetailDO);
                        }
                    }
                    //处理租金押金
                    BigDecimal newRentDeposit = statementOrderDetailDO.getStatementDetailRentDepositAmount();
                    if (BigDecimalUtil.compare(newRentDeposit, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(amountNeedReturn.getRentDepositPaidAmount(), BigDecimal.ZERO) > 0) {
                        //获取可冲正的金额
                        BigDecimal fillAmount = BigDecimalUtil.compare(newRentDeposit, amountNeedReturn.getRentDepositPaidAmount()) >= 0 ? amountNeedReturn.getRentDepositPaidAmount() : newRentDeposit;
                        statementOrderDetailDO.setStatementDetailRentDepositPaidAmount(fillAmount);
                        statementOrderDetailDO.setStatementDetailPaidTime(nowTime);
                        amountNeedReturn.setRentDepositPaidAmount(BigDecimalUtil.sub(amountNeedReturn.getRentDepositPaidAmount(), fillAmount));
                        StatementOrderDO statementOrderDO = getStatementOrderDO(statementOrderDOMap, statementOrderDetailDO.getStatementOrderId());
                        statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositPaidAmount(), fillAmount));
                        statementOrderDO.setStatementPaidTime(nowTime);
                        fixStatementStatus(statementOrderDetailDO, statementOrderDO);
                        if (!needUpdate.contains(statementOrderDetailDO)) {
                            needUpdate.add(statementOrderDetailDO);
                        }
                    }
                }
            }
            //更新结算详情
            if (CollectionUtil.isNotEmpty(needUpdate)) {
                statementOrderDetailMapper.batchUpdate(needUpdate);
            }
            //更新结算单
            if (statementOrderDOMap.size() > 0) {
                for (StatementOrderDO statementOrderDO : statementOrderDOMap.values()) {
                    statementOrderMapper.update(statementOrderDO);
                }
            }
        }
        //退剩余金额到账户余额
        AmountNeedReturn allAmountNeedReturn = new AmountNeedReturn();
        for (AmountNeedReturn amountNeedReturn : oldAmountMap.values()) {
            allAmountNeedReturn.add(amountNeedReturn);
        }
        //退款
        if (BigDecimalUtil.compare(allAmountNeedReturn.getTotalAmount(), BigDecimal.ZERO) > 0) {
            String payResultCode = paymentService.returnDepositExpand(customerDO.getCustomerNo(), allAmountNeedReturn.getRentPaidAmount(), BigDecimal.ZERO, allAmountNeedReturn.getRentDepositPaidAmount(), allAmountNeedReturn.getDepositPaidAmount(), "换单差价退款转单退款");
            if (!ErrorCode.SUCCESS.equals(payResultCode)) {
                return payResultCode;
            }
        }
        return ErrorCode.SUCCESS;
    }

    private void fixStatementStatus(StatementOrderDetailDO statementOrderDetailDO, StatementOrderDO statementOrderDO) {
        if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), statementOrderDO.getStatementPaidAmount()) > 0) {
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
        } else {
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        }
        //总押金大于已付押金,改变支付转态
        if (BigDecimalUtil.compare(statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.addAll(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount())) > 0) {
            statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
        } else {
            statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        }
    }
      /**
     * 结算日偏移到最近结算日(近供月租使用)
     * @param statementOrderDetailDO
     * @param rentType
     * @param statementDays
     * @param payMode
     */
    public void updateRealPayTimeIfNeed(StatementOrderDetailDO statementOrderDetailDO,int rentType,int statementDays,int payMode){
        if(statementOrderDetailDO==null)return;
        //统一修改结算日到具体结算日
        if(OrderRentType.RENT_TYPE_MONTH.equals(rentType)){
            Date realExpectPayDate=getRealExpectPayTime(statementOrderDetailDO.getStatementStartTime(),statementOrderDetailDO.getStatementEndTime(),statementDays,payMode);
            if(realExpectPayDate!=null)statementOrderDetailDO.setStatementExpectPayTime(realExpectPayDate);
        }
    }

    private Date getRealExpectPayTime(Date startTime, Date endTime, int statementDays, int payMode) {
        Date lastEndTime=null;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            lastEndTime= DateUtil.getDayByOffset(startTime,-1);
        } else if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)) {
            lastEndTime=endTime;
        }
        if(lastEndTime==null)return null;
        return DateUtil.getDayByOffset(getNextStatementDay(lastEndTime, statementDays),1);
    }

    private Date getNextStatementDay(Date startTime, int statementDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int nowDays = calendar.get(Calendar.DAY_OF_MONTH);
        if (statementDays == nowDays || (StatementMode.STATEMENT_MONTH_END.equals(statementDays) && nowDays == calendar.getActualMaximum(Calendar.DAY_OF_MONTH))) {
            return startTime;
        }
        calendar.set(Calendar.DAY_OF_MONTH, StatementMode.STATEMENT_MONTH_END.equals(statementDays)?calendar.getActualMaximum(Calendar.DAY_OF_MONTH):statementDays);
        if (DateUtil.daysBetween(calendar.getTime(), startTime) > 0) {
            calendar.add(Calendar.MONTH, 1);
            if(StatementMode.STATEMENT_MONTH_END.equals(statementDays)){
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        }
        return calendar.getTime();
    }

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private OrderStatementDateChangeLogMapper orderStatementDateChangeLogMapper;
    @Autowired
    private DingdingGroupMessageConfigMapper dingdingGroupMessageConfigMapper;
    @Autowired
    private DingDingSupport dingDingSupport;
    @Autowired
    private StatementOrderCorrectDetailMapper statementOrderCorrectDetailMapper;
    @Autowired
    private StatementOrderCorrectMapper statementOrderCorrectMapper;
    @Autowired
    private MessageThirdChannelService messageThirdChannelService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderSupport orderSupport;
    @Autowired
    private PaymentService paymentService;

}
