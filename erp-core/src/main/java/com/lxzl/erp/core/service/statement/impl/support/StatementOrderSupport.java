package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.*;
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
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerPersonMapper;
import com.lxzl.erp.dataaccess.dao.mysql.dingdingGroupMessageConfig.DingdingGroupMessageConfigMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderStatementDateChangeLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.dingdingGroupMessageConfig.DingdingGroupMessageConfigDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderStatementDateChangeLogDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDetailDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private UserMapper userMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private CustomerPersonMapper customerPersonMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private StatisticsService statisticsService;

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
                    returnStatementAmount = BigDecimalUtil.add(returnStatementAmount, sod.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE);
                    returnStatementRentAmount = BigDecimalUtil.add(returnStatementRentAmount, sod.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE);
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
     *
     * @param orderNo
     * @param changeTime
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
        int oldTimeLength = DateUtil.daysBetween(orderDO.getRentStartTime(), expectReturnTime)+1;
        int timeLength = DateUtil.daysBetween(orderDO.getRentStartTime(), stopTime)+1;
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
                    if(amountNeedReturn!=null){

                        needReturnRentAmount = BigDecimalUtil.add(needReturnRentAmount, amountNeedReturn.getRentPaidAmount());
                        needReturnRentDepositAmount=BigDecimalUtil.add(needReturnRentDepositAmount,amountNeedReturn.getRentDepositPaidAmount());
                        needReturnDepositAmount=BigDecimalUtil.add(needReturnDepositAmount,amountNeedReturn.getDepositPaidAmount());
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
            if (BigDecimalUtil.compare(needReturnRentAmount, BigDecimal.ZERO) > 0) {
                String payResultCode = paymentService.returnDepositExpand(customerDO.getCustomerNo(), needReturnRentAmount, BigDecimal.ZERO, needReturnRentDepositAmount,needReturnDepositAmount, "测试机转单退款");
                if (!ErrorCode.SUCCESS.equals(payResultCode)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return payResultCode;
                }
            }
        }
        return ErrorCode.SUCCESS;
    }

    private AmountNeedReturn modifyStatementItem(BigDecimal percent, Map<Integer, StatementOrderDO> statementOrderDOMap, List<StatementOrderDetailDO> needUpdateDetailDOList, StatementOrderDetailDO statementOrderDetailDO, String userId, Date updateTime, Date changeTime) {
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

            statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(),subRentDepositAmount));

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

            statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(),subDepositAmount));
            statementOrderDO.setStatementDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), subDepositAmount));
        }


        if (isRentNeedModify) {
            BigDecimal oldRentPaidAmount = statementOrderDetailDO.getStatementDetailRentPaidAmount();
            BigDecimal rentAmount = getMul(percent, oldRentAmount);

            BigDecimal rentAmountSub = BigDecimalUtil.sub(oldRentAmount, rentAmount);
            statementOrderDetailDO.setStatementDetailRentAmount(rentAmount);
            statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(), rentAmountSub, BigDecimalUtil.STANDARD_SCALE));

            if (BigDecimalUtil.compare(oldRentPaidAmount, rentAmount) > 0) {
                statementOrderDetailDO.setStatementDetailRentPaidAmount(rentAmount);
                rentPaidAmountSub = BigDecimalUtil.sub(oldRentPaidAmount, statementOrderDetailDO.getStatementDetailRentPaidAmount());
                statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentPaidAmount(), rentPaidAmountSub));
            }

            statementOrderDO.setStatementRentAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), rentAmountSub));
            statementOrderDO.setStatementAmount(BigDecimalUtil.sub(statementOrderDO.getStatementAmount(), rentAmountSub));
        }

        if (DateUtil.daysBetween(changeTime, statementOrderDetailDO.getStatementEndTime()) > 0) {
            statementOrderDetailDO.setStatementEndTime(changeTime);
        }
        //兼容部分特殊情况
        if (DateUtil.daysBetween(changeTime, statementOrderDetailDO.getStatementStartTime()) > 0) {
            statementOrderDetailDO.setStatementStartTime(changeTime);
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
