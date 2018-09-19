package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.DingDingCommonMsg;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
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
                statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositReturnAmount(),statementOrderDetailDO.getStatementDetailDepositReturnAmount()));
                //处理已退租金押金
                statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositReturnAmount(),statementOrderDetailDO.getStatementDetailRentDepositReturnAmount()));
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
        Set<Integer> orderIds=new HashSet<>();
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
        StringBuilder sb=null;
        //目前只会涉及一个订单
        for(Integer id:orderIds){
            OrderDO orderDO =orderMapper.findByOrderId(id);
            if(orderDO!=null&&orderDO.getOrderSellerId()!=null){
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
     * @param statementOrderDetailDO
     * @return
     */
    public AmountHasReturn getStatementItemHasReturn(StatementOrderDetailDO statementOrderDetailDO){
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
        return new AmountHasReturn(returnStatementAmount,returnStatementRentAmount,returnStatementDepositAmount,returnStatementRentDepositAmount);
    }


    /**
     * 修正单个结算单开始结束时间，状态等
     *
     * @author ZhaoZiXuan
     * @date 2018/8/17 10:25
     * @param
     * @return
     */
    public void fixOneStatementOrderStatementTime(StatementOrderDO statementOrderDO){
        if (statementOrderDO == null){
            return;
        }
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByStatementOrderId(statementOrderDO.getId());

        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)){
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
            if(DateUtil.daysBetween(minStartTime,statementOrderDO.getStatementStartTime())!=0||DateUtil.daysBetween(maxEndTime,statementOrderDO.getStatementEndTime())!=0){
                statementOrderDO.setStatementStartTime(minStartTime);
                statementOrderDO.setStatementEndTime(maxEndTime);
            }
        }

        if(BigDecimalUtil.compare(BigDecimal.ZERO,statementOrderDO.getStatementAmount())==0){
            if(CollectionUtil.isEmpty(statementOrderDetailDOList)){
                statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            }else{
                statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
            }
        }
        else if(BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(),statementOrderDO.getStatementAmount())==0){
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        }else if(BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(),BigDecimal.ZERO)>0){
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
        }else{
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
        }
        statementOrderMapper.update(statementOrderDO);
    }

    /**
     * 清除结算信息
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

}
