package com.lxzl.erp.core.service.statement.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderDO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
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
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createOrderStatement(String orderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
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
        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        Date rentStartTime = orderDO.getRentStartTime();
        Integer buyerCustomerId = orderDO.getBuyerCustomerId();
        Integer orderId = orderDO.getId();
        Integer statementDays;
        DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
        if (dataDictionaryDO == null) {
            statementDays = 31;
        } else {
            statementDays = Integer.parseInt(dataDictionaryDO.getDataName());
        }
        // 其他费用，包括运费、等费用
        BigDecimal otherAmount = orderDO.getLogisticsAmount();

        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        // 商品生成结算单
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPaymentCycle(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductAmount(), orderProductDO.getProductCount(), orderProductDO.getInsuranceAmount(), orderProductDO.getRentDepositAmount(), otherAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUser.getUserId());
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(statementDays, orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), orderProductDO.getInsuranceAmount(), orderProductDO.getRentDepositAmount(), orderProductDO.getRentTimeLength(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                            lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            BigDecimal itemAllAmount = BigDecimalUtil.add(orderProductDO.getProductAmount(), orderProductDO.getRentDepositAmount());
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, rentStartTime, orderProductDO.getPayMode(), orderProductDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(statementOrderDetailDO);
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, orderProductDO.getPaymentCycle(), orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), statementDays, orderProductDO.getPayMode(), currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(statementOrderDetailDO);
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
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialAmount(), orderMaterialDO.getMaterialCount(), orderMaterialDO.getInsuranceAmount(), orderMaterialDO.getRentDepositAmount(), otherAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUser.getUserId());
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(statementDays, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), orderMaterialDO.getInsuranceAmount(), orderMaterialDO.getRentDepositAmount(), orderMaterialDO.getRentTimeLength(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                            lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            BigDecimal itemAllAmount = BigDecimalUtil.add(orderMaterialDO.getMaterialAmount(), orderMaterialDO.getRentDepositAmount());
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, rentStartTime, orderMaterialDO.getPayMode(), orderMaterialDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(statementOrderDetailDO);
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), statementDays, orderMaterialDO.getPayMode(), currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                            lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                        }
                    }
                }
            }
        }

        saveStatementOrder(addStatementOrderDetailDOList, buyerCustomerId, currentTime, loginUser.getUserId());

        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            StatementOrderDetailDO statementOrderDetailDO = addStatementOrderDetailDOList.get(0);
            // 核算本次应该交多少钱
            if (DateUtil.isSameDay(rentStartTime, statementOrderDetailDO.getStatementExpectPayTime())) {
                thisNeedPayAmount = BigDecimalUtil.add(thisNeedPayAmount, statementOrderDetailDO.getStatementDetailAmount());
            }
        }

        result.setResult(thisNeedPayAmount);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Boolean> payStatementOrder(String statementOrderNo) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        StatementOrderDO statementOrderDO = statementOrderMapper.findByNo(statementOrderNo);
        if (statementOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus())) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_STATUS_ERROR);
            return result;
        }
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        CustomerDO customerDO = customerMapper.findById(statementOrderDO.getCustomerId());
        BigDecimal payRentAmount = BigDecimalUtil.add(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDO.getStatementRentPaidAmount()), statementOrderDO.getStatementOverdueAmount());
        BigDecimal payRentDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDO.getStatementRentDepositPaidAmount());
        ServiceResult<String, Boolean> paymentResult = paymentService.balancePay(customerDO.getCustomerNo(), statementOrderDO.getStatementOrderNo(), statementOrderDO.getRemark(), null, payRentAmount, payRentDepositAmount);
        if (!ErrorCode.SUCCESS.equals(paymentResult.getErrorCode()) || !paymentResult.getResult()) {
            result.setErrorCode(paymentResult.getErrorCode());
            return result;
        }

        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentPaidAmount(), payRentAmount));
        statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositPaidAmount(), payRentDepositAmount));
        statementOrderDO.setStatementPaidTime(currentTime);
        statementOrderDO.setUpdateTime(currentTime);
        statementOrderDO.setUpdateUser(loginUser.getUserId().toString());
        statementOrderMapper.update(statementOrderDO);
        Set<Integer> orderIdSet = new HashSet<>();
        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
            if (!StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
                BigDecimal needStatementDetailRentPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(), statementOrderDetailDO.getStatementDetailRentPaidAmount());
                statementOrderDetailDO.setStatementDetailRentPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), needStatementDetailRentPayAmount));
                BigDecimal needStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                statementOrderDetailDO.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), needStatementDetailRentDepositPayAmount));
                statementOrderDetailDO.setStatementDetailPaidTime(currentTime);
                statementOrderDetailDO.setUpdateTime(currentTime);
                statementOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
                statementOrderDetailMapper.update(statementOrderDetailDO);
                orderIdSet.add(statementOrderDetailDO.getOrderId());
            }
        }

        for (Integer orderId : orderIdSet) {
            OrderDO orderDO = orderMapper.findByOrderId(orderId);
            if (!PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())) {
                orderDO.setPayStatus(PayStatus.PAY_STATUS_PAID);
                orderDO.setPayTime(currentTime);
                orderMapper.update(orderDO);
            }
        }

        result.setResult(true);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<StatementOrder>> queryStatementOrder(StatementOrderQueryParam statementOrderQueryParam) {
        ServiceResult<String, Page<StatementOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(statementOrderQueryParam.getPageNo(), statementOrderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("statementOrderQueryParam", statementOrderQueryParam);
        Integer totalCount = statementOrderMapper.listCount(maps);
        List<StatementOrderDO> statementOrderDOList = statementOrderMapper.listPage(maps);
        List<StatementOrder> statementOrderList = ConverterUtil.convertList(statementOrderDOList, StatementOrder.class);
        Page<StatementOrder> page = new Page<>(statementOrderList, totalCount, statementOrderQueryParam.getPageNo(), statementOrderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, BigDecimal> createReturnOrderStatement(String returnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrderNo);
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Integer buyerCustomerId = returnOrderDO.getCustomerId();
        Date statementDetailStartTime = currentTime;
        Date statementDetailEndTime = null;
        // 其他的费用
        BigDecimal otherAmount = BigDecimalUtil.add(returnOrderDO.getServiceCost(), returnOrderDO.getDamageCost());
        List<ReturnOrderProductEquipmentDO> returnOrderProductEquipmentDOList = returnOrderProductEquipmentMapper.findByReturnOrderNo(returnOrderNo);
        if (CollectionUtil.isNotEmpty(returnOrderProductEquipmentDOList)) {
            for (ReturnOrderProductEquipmentDO returnOrderProductEquipmentDO : returnOrderProductEquipmentDOList) {
                OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderNoAndEquipmentNo(returnOrderProductEquipmentDO.getOrderNo(), returnOrderProductEquipmentDO.getEquipmentNo());
                OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    BigDecimal payReturnAmount = BigDecimal.ZERO;
                    for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                            // 如果最后一期都交完了，那么就设定一个本期的最后一个时间点
                            if (i == (statementOrderDetailDOList.size() - 1)) {
                                statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                            }
                            continue;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                        if (i == (statementOrderDetailDOList.size())) {
                            // 最后一期
                            int diffDays = DateUtil.daysBetween(statementDetailEndTime, statementDetailStartTime);
                            int thisMonthDays = DateUtil.getActualMaximum(statementDetailStartTime);
                            payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.mul(BigDecimalUtil.div(orderProductEquipmentDO.getProductEquipmentUnitAmount(), new BigDecimal(thisMonthDays), 2), new BigDecimal(diffDays)));
                        } else {
                            payReturnAmount = orderProductEquipmentDO.getProductEquipmentUnitAmount();
                        }

                        StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderProductEquipmentDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, returnOrderProductEquipmentDO.getReturnOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, currentTime, loginUser.getUserId());
                        addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                    }

                    if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                        // 如果没有处理完差价，统一当天交钱
                        StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderProductEquipmentDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, returnOrderProductEquipmentDO.getReturnOrderProductId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, otherAmount, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                        addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                    }
                }
            }
        }

        List<ReturnOrderMaterialBulkDO> returnOrderMaterialBulkDOList = returnOrderMaterialBulkMapper.findByReturnOrderNo(returnOrderNo);
        if (CollectionUtil.isNotEmpty(returnOrderMaterialBulkDOList)) {
            for (ReturnOrderMaterialBulkDO returnOrderMaterialBulkDO : returnOrderMaterialBulkDOList) {
                OrderMaterialBulkDO orderNoAndBulkMaterialNo = orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo(returnOrderMaterialBulkDO.getOrderNo(), returnOrderMaterialBulkDO.getBulkMaterialNo());
                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderNoAndBulkMaterialNo.getOrderMaterialId());
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    BigDecimal payReturnAmount = BigDecimal.ZERO;
                    BigDecimal materialBulkUnitAmount = orderNoAndBulkMaterialNo.getMaterialBulkUnitAmount();
                    for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                            // 如果最后一期都交完了，那么就设定一个本期的最后一个时间点
                            if (i == (statementOrderDetailDOList.size() - 1)) {
                                statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                            }
                            continue;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                        if (i == (statementOrderDetailDOList.size())) {
                            // 最后一期
                            int diffDays = DateUtil.daysBetween(statementDetailEndTime, statementDetailStartTime);
                            int thisMonthDays = DateUtil.getActualMaximum(statementDetailStartTime);
                            payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.mul(BigDecimalUtil.div(orderNoAndBulkMaterialNo.getMaterialBulkUnitAmount(), new BigDecimal(thisMonthDays), 2), new BigDecimal(diffDays)));
                        } else {
                            payReturnAmount = materialBulkUnitAmount;
                        }

                        StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderMaterialBulkDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, returnOrderMaterialBulkDO.getReturnOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, currentTime, loginUser.getUserId());
                        addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                    }

                    if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                        // 如果没有处理完差价，统一当天交钱
                        StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderMaterialBulkDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, returnOrderMaterialBulkDO.getReturnOrderMaterialId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, otherAmount, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                        addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                    }
                }
            }
        }
        saveStatementOrder(addStatementOrderDetailDOList, buyerCustomerId, currentTime, loginUser.getUserId());

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, BigDecimal> createChangeOrderStatement(String changeOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        Calendar currentTimeCalendar = Calendar.getInstance();
        currentTimeCalendar.setTime(currentTime);
        ChangeOrderDO changeOrderDO = changeOrderMapper.findByNo(changeOrderNo);
        List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(changeOrderNo);
        Integer buyerCustomerId = changeOrderDO.getCustomerId();
        BigDecimal otherAmount = BigDecimalUtil.add(changeOrderDO.getServiceCost(), changeOrderDO.getDamageCost());
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Date statementDetailStartTime = currentTime;
        Date statementDetailEndTime = null;
        if (CollectionUtil.isNotEmpty(changeOrderProductEquipmentDOList)) {
            for (ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList) {
                BigDecimal productEquipmentDiffAmount = changeOrderProductEquipmentDO.getPriceDiff();
                if (productEquipmentDiffAmount == null || BigDecimalUtil.compare(productEquipmentDiffAmount, BigDecimal.ZERO) <= 0) {
                    continue;
                }
                OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderNoAndEquipmentNo(changeOrderProductEquipmentDO.getOrderNo(), changeOrderProductEquipmentDO.getSrcEquipmentNo());
                OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());

                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    statementDetailEndTime = orderProductEquipmentDO.getExpectReturnTime();
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, productEquipmentDiffAmount, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                    addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                    List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
                    if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                        BigDecimal payDiffAmount = BigDecimal.ZERO;
                        for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                            StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                                // 如果最后一期都交完了，那么就设定一个本期的最后一个时间点
                                if (i == (statementOrderDetailDOList.size() - 1)) {
                                    statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                                }
                                continue;
                            }
                            statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                            // 第一月差价
                            if (BigDecimalUtil.compare(payDiffAmount, BigDecimal.ZERO) == 0) {
                                int diffDays = DateUtil.daysBetween(statementDetailEndTime, statementDetailStartTime);
                                int thisMonthDays = DateUtil.getActualMaximum(statementDetailStartTime);
                                payDiffAmount = BigDecimalUtil.mul(BigDecimalUtil.div(productEquipmentDiffAmount, new BigDecimal(thisMonthDays), 2), new BigDecimal(diffDays));
                            } else if (i == (statementOrderDetailDOList.size() - 1)) {
                                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                                int diffDays = DateUtil.daysBetween(statementDetailEndTime, statementDetailStartTime);
                                int thisMonthDays = DateUtil.getActualMaximum(statementDetailStartTime);
                                payDiffAmount = BigDecimalUtil.mul(BigDecimalUtil.div(productEquipmentDiffAmount, new BigDecimal(thisMonthDays), 2), new BigDecimal(diffDays));
                            } else {
                                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                                payDiffAmount = productEquipmentDiffAmount;
                            }

                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, payDiffAmount, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                        }

                        if (BigDecimalUtil.compare(payDiffAmount, BigDecimal.ZERO) == 0 || BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) == 0) {
                            BigDecimal thisPayAmount = BigDecimalUtil.compare(payDiffAmount, BigDecimal.ZERO) == 0 ? BigDecimalUtil.add(productEquipmentDiffAmount, otherAmount) : otherAmount;
                            // 如果没有处理完差价，统一当天交钱
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, thisPayAmount, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                        }
                    }
                }
            }
        }
        List<ChangeOrderMaterialBulkDO> changeOrderMaterialBulkDOList = changeOrderMaterialBulkMapper.findByChangeOrderNo(changeOrderNo);
        if (CollectionUtil.isNotEmpty(changeOrderMaterialBulkDOList)) {
            for (ChangeOrderMaterialBulkDO changeOrderMaterialBulkDO : changeOrderMaterialBulkDOList) {
                if (changeOrderMaterialBulkDO.getPriceDiff() == null || BigDecimalUtil.compare(changeOrderMaterialBulkDO.getPriceDiff(), BigDecimal.ZERO) <= 0) {
                    continue;
                }
                BigDecimal materialBulkDiffAmount = changeOrderMaterialBulkDO.getPriceDiff();
                OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo(changeOrderMaterialBulkDO.getOrderNo(), changeOrderMaterialBulkDO.getSrcBulkMaterialNo());
                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderMaterialBulkDO.getOrderMaterialId());
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                    statementDetailEndTime = orderMaterialBulkDO.getExpectReturnTime();
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, materialBulkDiffAmount, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                    addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                } else if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                    List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
                    if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                        BigDecimal payDiffAmount = BigDecimal.ZERO;
                        for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                            StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                                // 如果最后一期都交完了，那么就设定一个本期的最后一个时间点
                                if (i == (statementOrderDetailDOList.size() - 1)) {
                                    statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                                }
                                continue;
                            }
                            statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                            // 第一月差价
                            if (i == 0) {
                                int diffDays = DateUtil.daysBetween(statementDetailEndTime, statementDetailStartTime);
                                int thisMonthDays = DateUtil.getActualMaximum(statementDetailStartTime);
                                payDiffAmount = BigDecimalUtil.mul(BigDecimalUtil.div(materialBulkDiffAmount, new BigDecimal(thisMonthDays), 2), new BigDecimal(diffDays));
                            } else if (i == (statementOrderDetailDOList.size() - 1)) {
                                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                                payDiffAmount = BigDecimalUtil.sub(materialBulkDiffAmount, payDiffAmount);
                            } else {
                                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                                payDiffAmount = materialBulkDiffAmount;
                            }

                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(statementOrderDetailDO.getCustomerId(), OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, payDiffAmount, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                        }

                        if (BigDecimalUtil.compare(payDiffAmount, BigDecimal.ZERO) == 0 || BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) == 0) {
                            BigDecimal thisPayAmount = BigDecimalUtil.compare(payDiffAmount, BigDecimal.ZERO) == 0 ? BigDecimalUtil.add(materialBulkDiffAmount, otherAmount) : otherAmount;
                            // 如果没有处理完差价，统一当天交钱
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, thisPayAmount, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                        }
                    }
                }
            }
        }

        saveStatementOrder(addStatementOrderDetailDOList, buyerCustomerId, currentTime, loginUser.getUserId());

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    void saveStatementOrder(List<StatementOrderDetailDO> addStatementOrderDetailDOList, Integer buyerCustomerId, Date currentTime, Integer loginUserId) {

        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            // 同一个时间的做归集
            Map<Date, StatementOrderDO> statementOrderDOMap = new HashMap<>();
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                Date dateKey = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementExpectPayTime());
                StatementOrderDO statementOrderDO = statementOrderMapper.findByCustomerAndPayTime(buyerCustomerId, dateKey);
                if (statementOrderDO != null) {
                    statementOrderDOMap.put(dateKey, statementOrderDO);
                }

                if (!statementOrderDOMap.containsKey(dateKey)) {
                    statementOrderDO = new StatementOrderDO();
                    statementOrderDO.setStatementOrderNo(GenerateNoUtil.generateStatementNo(currentTime));
                    statementOrderDO.setCustomerId(buyerCustomerId);
                    statementOrderDO.setStatementExpectPayTime(dateKey);
                    statementOrderDO.setStatementAmount(statementOrderDetailDO.getStatementDetailAmount());
                    statementOrderDO.setStatementRentDepositAmount(statementOrderDetailDO.getStatementDetailRentDepositAmount());
                    statementOrderDO.setStatementRentAmount(statementOrderDetailDO.getStatementDetailRentAmount());
                    statementOrderDO.setStatementStartTime(statementOrderDetailDO.getStatementStartTime());
                    statementOrderDO.setStatementEndTime(statementOrderDetailDO.getStatementEndTime());
                    statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    statementOrderDO.setCreateUser(loginUserId.toString());
                    statementOrderDO.setUpdateUser(loginUserId.toString());
                    statementOrderDO.setCreateTime(currentTime);
                    statementOrderDO.setUpdateTime(currentTime);
                    statementOrderMapper.save(statementOrderDO);
                } else {
                    statementOrderDO = statementOrderDOMap.get(dateKey);
                    statementOrderDO.setStatementAmount(BigDecimalUtil.add(statementOrderDO.getStatementAmount(), statementOrderDetailDO.getStatementDetailAmount()));
                    statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositAmount()));
                    statementOrderDO.setStatementRentAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentAmount(), statementOrderDetailDO.getStatementDetailRentAmount()));
                    if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus())) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
                    }
                    if (statementOrderDetailDO.getStatementStartTime().getTime() < statementOrderDO.getStatementStartTime().getTime()) {
                        statementOrderDO.setStatementStartTime(statementOrderDetailDO.getStatementStartTime());
                    }
                    if (statementOrderDetailDO.getStatementEndTime().getTime() > statementOrderDO.getStatementEndTime().getTime()) {
                        statementOrderDO.setStatementEndTime(statementOrderDetailDO.getStatementEndTime());
                    }
                    statementOrderMapper.update(statementOrderDO);
                }
                statementOrderDOMap.put(dateKey, statementOrderDO);
                statementOrderDetailDO.setStatementOrderId(statementOrderDO.getId());
            }
            statementOrderDetailMapper.saveList(addStatementOrderDetailDOList);
        }
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
    StatementOrderDetailDO calculateOneStatementOrderDetail(Integer rentType, Integer rentTimeLength, Integer payMode, Date rentStartTime, BigDecimal statementDetailAmount, Integer itemCount, BigDecimal insuranceAmount, BigDecimal totalRentDepositAmount, BigDecimal otherAmount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId) {
        Calendar rentStartTimeCalendar = Calendar.getInstance();
        rentStartTimeCalendar.setTime(rentStartTime);
        Date statementEndTime = null, statementExpectPayTime = null;
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, rentTimeLength);
            if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)) {
                statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, rentTimeLength);
            }
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
            if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)) {
                statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
            }
        }

        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
        }
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
        } else if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)) {
            statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
        }

        // 保险金额
        BigDecimal totalInsuranceAmount = BigDecimal.ZERO;
        if (BigDecimalUtil.compare(insuranceAmount, BigDecimal.ZERO) > 0) {
            totalInsuranceAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(insuranceAmount, new BigDecimal(rentTimeLength)), new BigDecimal(itemCount));
        }
        BigDecimal thisPhaseAmount = BigDecimalUtil.add(BigDecimalUtil.add(statementDetailAmount, totalInsuranceAmount), otherAmount);

        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, thisPhaseAmount, totalRentDepositAmount, currentTime, loginUserId);
    }

    /**
     * 计算多期中的第一期明细
     */
    StatementOrderDetailDO calculateFirstStatementOrderDetail(Integer statementDays, Integer paymentCycle, Integer payMode, Date rentStartTime, BigDecimal unitAmount, Integer itemCount, BigDecimal insuranceAmount, BigDecimal totalRentDepositAmount, Integer rentTimeLength, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId) {
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
        // 保险金额
        BigDecimal totalInsuranceAmount = BigDecimal.ZERO;
        if (BigDecimalUtil.compare(insuranceAmount, BigDecimal.ZERO) > 0) {
            totalInsuranceAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(insuranceAmount, new BigDecimal(rentTimeLength)), new BigDecimal(itemCount));
        }
        // 第一期总金额
        BigDecimal firstPhaseAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(thisMonthAmount, surplusMonthAmount), lastMonthAmount), totalInsuranceAmount);
        Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, actualMaximumDays);
        statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(statementEndTime, surplusMonth);
        statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, lastMonthSurplusDays);
        Date statementExpectPayTime;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
        } else {
            statementExpectPayTime = statementEndTime;
        }
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, firstPhaseAmount, totalRentDepositAmount, currentTime, loginUserId);
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
        Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(lastPhaseStartTime, surplusDays - 1);
        Date statementExpectPayTime;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = lastPhaseStartTime;
        } else {
            statementExpectPayTime = statementEndTime;
        }
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, lastPhaseStartTime, statementEndTime, lastPhaseAmount, BigDecimal.ZERO, currentTime, loginUserId);
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
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, thisPhaseStartTime, statementEndTime, middlePhaseAmount, BigDecimal.ZERO, currentTime, loginUserId);
    }

    StatementOrderDetailDO buildStatementOrderDetailDO(Integer customerId, Integer orderType, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date statementExpectPayTime, Date startTime, Date endTime, BigDecimal statementDetailRentAmount, BigDecimal statementDetailRentDepositAmount, Date currentTime, Integer loginUserId) {
        StatementOrderDetailDO statementOrderDetailDO = new StatementOrderDetailDO();
        statementOrderDetailDO.setCustomerId(customerId);
        statementOrderDetailDO.setOrderType(orderType);
        statementOrderDetailDO.setOrderId(orderId);
        statementOrderDetailDO.setOrderItemType(orderItemType);
        statementOrderDetailDO.setOrderItemReferId(orderItemReferId);
        statementOrderDetailDO.setStatementExpectPayTime(statementExpectPayTime);
        statementOrderDetailDO.setStatementStartTime(startTime);
        statementOrderDetailDO.setStatementEndTime(endTime);
        statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(statementDetailRentDepositAmount, statementDetailRentAmount));
        statementOrderDetailDO.setStatementDetailRentDepositAmount(statementDetailRentDepositAmount);
        statementOrderDetailDO.setStatementDetailRentAmount(statementDetailRentAmount);
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

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;

    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private ChangeOrderMapper changeOrderMapper;

    @Autowired
    private ChangeOrderMaterialBulkMapper changeOrderMaterialBulkMapper;

    @Autowired
    private ChangeOrderProductEquipmentMapper changeOrderProductEquipmentMapper;

    @Autowired
    private ReturnOrderMapper returnOrderMapper;

    @Autowired
    private ReturnOrderProductEquipmentMapper returnOrderProductEquipmentMapper;

    @Autowired
    private ReturnOrderMaterialBulkMapper returnOrderMaterialBulkMapper;
}
