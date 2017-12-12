package com.lxzl.erp.core.service.statement.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
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

    @Override
    public ServiceResult<String, String> createNewStatementOrder(String orderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
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
        // 先确定订单需要结算几期
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                Integer statementMonthCount = calculateStatementMonthCount(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPaymentCycle(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    Date statementEndTime = null, statementExpectPayTime = null;
                    if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                        statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, orderProductDO.getRentTimeLength());
                    } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                        statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, orderProductDO.getRentTimeLength());
                    }
                    if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProductDO.getPayMode())) {
                        statementExpectPayTime = rentStartTime;
                    } else {
                        statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, orderProductDO.getRentTimeLength());
                    }
                    // 当前月份的
                    int thisMonth = rentStartTimeCalendar.get(Calendar.MONTH) + 1;
                    StatementOrderDetailDO statementOrderDetailDO = buildStatementOrderDetailDO(orderDO.getBuyerCustomerId(), orderDO.getId(), thisMonth, statementExpectPayTime, rentStartTime, statementEndTime, orderProductDO.getProductAmount(), currentTime, loginUser.getUserId());
                    statementOrderDetailDOList.add(statementOrderDetailDO);
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            // 本月天数
                            int thisMonthDays = DateUtil.getActualMaximum(rentStartTime);
                            // 计算开始日期距离月底的天数
                            int actualMaximumDays = DateUtil.betweenActualMaximumDays(rentStartTime);
                            // 本月每天金额
                            BigDecimal oneDayAmount = BigDecimalUtil.div(orderProductDO.getProductUnitAmount(), new BigDecimal(thisMonthDays), 4);
                            // 本月的金额（开始日到本月底）
                            BigDecimal thisMonthAmount = BigDecimalUtil.mul(new BigDecimal(actualMaximumDays), BigDecimalUtil.mul(oneDayAmount, new BigDecimal(orderProductDO.getProductCount())));
                            // 本期未结算剩余整月份
                            int surplusMonth = orderProductDO.getPaymentCycle() - 1;
                            // 本期未计算整月的金额
                            BigDecimal surplusMonthAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(surplusMonth));
                            // 本期未结算剩余月数
                            Date lastMonth = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, orderProductDO.getPaymentCycle());
                            // 计算最后一月的天数
                            int lastMonthSurplusDays = DateUtil.betweenAppointDays(lastMonth, statementDays);
                            // 后一月的总天数
                            int lastMonthDays = DateUtil.getActualMaximum(lastMonth);
                            // 最后一月每天金额
                            BigDecimal lastMonthOneDayAmount = BigDecimalUtil.div(orderProductDO.getProductUnitAmount(), new BigDecimal(lastMonthDays), 2);
                            // 最后一月的金额（开始日到本月底）
                            BigDecimal lastMonthAmount = BigDecimalUtil.mul(new BigDecimal(lastMonthSurplusDays), BigDecimalUtil.mul(lastMonthOneDayAmount, new BigDecimal(orderProductDO.getProductCount())));
                            // 第一期总金额
                            BigDecimal firstPhaseAmount = BigDecimalUtil.add(BigDecimalUtil.add(thisMonthAmount, surplusMonthAmount), lastMonthAmount);
                            Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, actualMaximumDays);
                            statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(statementEndTime, surplusMonth);
                            statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, lastMonthSurplusDays);
                            Date statementExpectPayTime;
                            if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProductDO.getPayMode())) {
                                statementExpectPayTime = rentStartTime;
                            } else {
                                statementExpectPayTime = statementEndTime;
                            }
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(rentStartTime);
                            int thisMonth = calendar.get(Calendar.MONTH) + 1;
                            StatementOrderDetailDO statementOrderDetailDO = buildStatementOrderDetailDO(orderDO.getBuyerCustomerId(), orderDO.getId(), thisMonth, statementExpectPayTime, rentStartTime, statementEndTime, firstPhaseAmount, currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, firstPhaseAmount);
                            // 计算到哪天
                            Calendar lastDate = Calendar.getInstance();
                            lastDate.setTime(com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, orderProductDO.getPaymentCycle()));
                            lastDate.set(Calendar.DAY_OF_MONTH, lastMonthSurplusDays);
                            lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(lastDate.getTime());
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            Date lastPhaseStartTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(lastCalculateDate, 1);
                            Date lastPhaseDate = com.lxzl.se.common.util.date.DateUtil.monthInterval(lastCalculateDate, orderProductDO.getPaymentCycle());
                            // 订单的最后日期
                            Date orderLastDate = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, orderProductDO.getRentTimeLength());
                            Calendar orderLastPhaseCalendar = Calendar.getInstance();
                            orderLastPhaseCalendar.setTime(orderLastDate);
                            int surplusDays = DateUtil.daysBetween(lastPhaseStartTime, orderLastDate);

                            // 最后一期总金额 注释行为按天计算的方式，现采用暴力减法的方式
                            /*int surplusMonth = 0;
                            for (int j = 0; j < orderProductDO.getPaymentCycle(); j++) {
                                Date nextMonth = com.lxzl.se.common.util.date.DateUtil.monthInterval(lastPhaseDate, (i + 1));
                                if (nextMonth.getTime() > orderLastDate.getTime()) {
                                    break;
                                }
                                surplusMonth++;
                            }
                            // 本月天数
                            lastPhaseDate = com.lxzl.se.common.util.date.DateUtil.monthInterval(lastPhaseDate, surplusMonth);
                            int thisMonthDays = DateUtil.getActualMaximum(lastPhaseDate);
                            BigDecimal oneDayAmount = BigDecimalUtil.div(orderProductDO.getProductUnitAmount(), new BigDecimal(thisMonthDays), 2);
                            BigDecimal lastPhaseAmount = BigDecimalUtil.add(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(surplusMonth)), BigDecimalUtil.mul(BigDecimalUtil.mul(oneDayAmount, new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(surplusDays)));*/
                            BigDecimal lastPhaseAmount = BigDecimalUtil.sub(orderProductDO.getProductAmount(), alreadyPaidAmount);

                            Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(lastPhaseStartTime, surplusDays);
                            Date statementExpectPayTime;
                            if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProductDO.getPayMode())) {
                                statementExpectPayTime = lastPhaseStartTime;
                            } else {
                                statementExpectPayTime = statementEndTime;
                            }
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(lastPhaseStartTime);
                            int thisMonth = calendar.get(Calendar.MONTH) + 1;
                            StatementOrderDetailDO statementOrderDetailDO = buildStatementOrderDetailDO(orderDO.getBuyerCustomerId(), orderDO.getId(), thisMonth, statementExpectPayTime, lastPhaseStartTime, statementEndTime, lastPhaseAmount, currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                        } else {
                            // 中间期数
                            Date thisPhaseStartTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(lastCalculateDate, 1);
                            Date thisPhaseDate = com.lxzl.se.common.util.date.DateUtil.monthInterval(lastCalculateDate, orderProductDO.getPaymentCycle());
                            // 本期总金额
                            BigDecimal middlePhaseAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(orderProductDO.getPaymentCycle()));
                            Date statementEndTime = thisPhaseDate;
                            int lastMonthSurplusDays = DateUtil.betweenAppointDays(statementEndTime, statementDays);
                            Calendar statementEndTimeCalendar = Calendar.getInstance();
                            statementEndTimeCalendar.setTime(statementEndTime);
                            if (statementEndTimeCalendar.get(Calendar.DAY_OF_MONTH) < lastMonthSurplusDays) {
                                statementEndTimeCalendar.set(Calendar.DAY_OF_MONTH, lastMonthSurplusDays);
                                statementEndTime = statementEndTimeCalendar.getTime();
                            }
                            Date statementExpectPayTime;
                            if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProductDO.getPayMode())) {
                                statementExpectPayTime = thisPhaseStartTime;
                            } else {
                                statementExpectPayTime = statementEndTime;
                            }
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(thisPhaseStartTime);
                            int thisMonth = calendar.get(Calendar.MONTH) + 1;
                            StatementOrderDetailDO statementOrderDetailDO = buildStatementOrderDetailDO(orderDO.getBuyerCustomerId(), orderDO.getId(), thisMonth, statementExpectPayTime, thisPhaseStartTime, statementEndTime, middlePhaseAmount, currentTime, loginUser.getUserId());
                            statementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, middlePhaseAmount);
                            lastCalculateDate = statementEndTime;
                        }
                    }
                }
            }
        }

        statementOrderDetailMapper.saveList(statementOrderDetailDOList);

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                Integer statementMonthCount = calculateStatementMonthCount(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPaymentCycle(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
            }
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

    StatementOrderDetailDO buildStatementOrderDetailDO(Integer customerId, Integer orderId, Integer statementMonth, Date statementExpectPayTime, Date startTime, Date endTime, BigDecimal statementDetailAmount, Date currentTime, Integer loginUserId) {
        StatementOrderDetailDO statementOrderDetailDO = new StatementOrderDetailDO();
        statementOrderDetailDO.setCustomerId(customerId);
        statementOrderDetailDO.setOrderId(orderId);
        statementOrderDetailDO.setStatementOrderId(1);
        statementOrderDetailDO.setStatementMonth(statementMonth);
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

    void calculateFirstAmount(Integer rentType, Integer rentTimeLength, Integer payMode, Date rentStartTime, BigDecimal productAmount, Integer customerId, Integer orderId) {
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
        } else {
            statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
        }
        // 当前月份的
        int thisMonth = rentStartTimeCalendar.get(Calendar.MONTH) + 1;
        StatementOrderDetailDO statementOrderDetailDO = buildStatementOrderDetailDO(customerId, orderId, thisMonth, statementExpectPayTime, rentStartTime, statementEndTime, productAmount, null, null);
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
