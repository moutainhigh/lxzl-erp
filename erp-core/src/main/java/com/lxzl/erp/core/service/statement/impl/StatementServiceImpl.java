package com.lxzl.erp.core.service.statement.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
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
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
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
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductAmount(), orderProductDO.getProductCount(), orderProductDO.getInsuranceAmount(), orderProductDO.getRentDepositAmount(), orderProductDO.getDepositAmount(), otherAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUser.getUserId());
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(statementDays, orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), orderProductDO.getInsuranceAmount(), orderProductDO.getRentDepositAmount(), orderProductDO.getDepositAmount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUser.getUserId());
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
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialAmount(), orderMaterialDO.getMaterialCount(), orderMaterialDO.getInsuranceAmount(), orderMaterialDO.getRentDepositAmount(), orderMaterialDO.getDepositAmount(), otherAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUser.getUserId());
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(statementDays, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), orderMaterialDO.getInsuranceAmount(), orderMaterialDO.getRentDepositAmount(), orderMaterialDO.getDepositAmount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUser.getUserId());
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
        BigDecimal payDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDO.getStatementDepositPaidAmount());
        ServiceResult<String, Boolean> paymentResult = paymentService.balancePay(customerDO.getCustomerNo(), statementOrderDO.getStatementOrderNo(), statementOrderDO.getRemark(), null, payRentAmount, payRentDepositAmount, payDepositAmount);
        if (!ErrorCode.SUCCESS.equals(paymentResult.getErrorCode()) || !paymentResult.getResult()) {
            result.setErrorCode(paymentResult.getErrorCode());
            return result;
        }

        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentPaidAmount(), payRentAmount));
        statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositPaidAmount(), payRentDepositAmount));
        statementOrderDO.setStatementDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositPaidAmount(), payDepositAmount));
        statementOrderDO.setStatementPaidTime(currentTime);
        statementOrderDO.setUpdateTime(currentTime);
        statementOrderDO.setUpdateUser(loginUser.getUserId().toString());
        statementOrderMapper.update(statementOrderDO);
        Map<Integer, BigDecimal> orderPaidMap = new HashMap<>();
        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
                BigDecimal needStatementDetailRentPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(), statementOrderDetailDO.getStatementDetailRentPaidAmount());
                statementOrderDetailDO.setStatementDetailRentPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), needStatementDetailRentPayAmount));
                BigDecimal needStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                BigDecimal needStatementDetailDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositAmount(), statementOrderDetailDO.getStatementDetailDepositPaidAmount());
                statementOrderDetailDO.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), needStatementDetailRentDepositPayAmount));
                statementOrderDetailDO.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), needStatementDetailDepositPayAmount));
                statementOrderDetailDO.setStatementDetailPaidTime(currentTime);
                statementOrderDetailDO.setUpdateTime(currentTime);
                statementOrderDetailDO.setUpdateUser(loginUser.getUserId().toString());
                statementOrderDetailMapper.update(statementOrderDetailDO);
                orderPaidMap.put(statementOrderDetailDO.getOrderId(), BigDecimalUtil.add(orderPaidMap.get(statementOrderDetailDO.getOrderId()), needStatementDetailRentPayAmount));
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : orderPaidMap.entrySet()) {
            Integer orderId = entry.getKey();
            BigDecimal paidAmount = entry.getValue();
            OrderDO orderDO = orderMapper.findByOrderId(orderId);
            if (!PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())) {
                orderDO.setPayStatus(PayStatus.PAY_STATUS_PAID);
            }

            orderDO.setTotalPaidOrderAmount(BigDecimalUtil.add(orderDO.getTotalPaidOrderAmount(), paidAmount));
            orderDO.setPayTime(currentTime);
            orderMapper.update(orderDO);
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
    public ServiceResult<String, StatementOrder> queryStatementOrderDetail(String statementOrderNo) {
        ServiceResult<String, StatementOrder> result = new ServiceResult<>();
        StatementOrderDO statementOrderDO = statementOrderMapper.findByNo(statementOrderNo);
        if (statementOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        result.setResult(ConverterUtil.convert(statementOrderDO, StatementOrder.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, BigDecimal> createReturnOrderStatement(String returnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrderNo);
        if (returnOrderDO == null) {
            result.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return result;
        }
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Integer buyerCustomerId = returnOrderDO.getCustomerId();
        Date statementDetailStartTime;
        Date statementDetailEndTime;
        // 其他的费用
        BigDecimal otherAmount = BigDecimalUtil.add(returnOrderDO.getServiceCost(), returnOrderDO.getDamageCost());

        // TODO 租金押金和设备押金
        BigDecimal totalReturnRentDepositAmount = BigDecimal.ZERO, totalReturnDepositAmount = BigDecimal.ZERO;

        List<ReturnOrderProductEquipmentDO> returnOrderProductEquipmentDOList = returnOrderProductEquipmentMapper.findByReturnOrderNo(returnOrderNo);
        if (CollectionUtil.isNotEmpty(returnOrderProductEquipmentDOList)) {
            for (ReturnOrderProductEquipmentDO returnOrderProductEquipmentDO : returnOrderProductEquipmentDOList) {
                OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderNoAndEquipmentNo(returnOrderProductEquipmentDO.getOrderNo(), returnOrderProductEquipmentDO.getEquipmentNo());
                if (orderProductEquipmentDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_EXISTS);
                    return result;
                }
                OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
                if (orderProductDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_NOT_EXISTS);
                    return result;
                }
                totalReturnRentDepositAmount = BigDecimalUtil.add(totalReturnRentDepositAmount, BigDecimalUtil.div(orderProductDO.getRentDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), 2));
                totalReturnDepositAmount = BigDecimalUtil.add(totalReturnDepositAmount, BigDecimalUtil.div(orderProductDO.getDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), 2));

                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                        BigDecimal payReturnAmount = BigDecimal.ZERO;
                        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                            continue;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                        BigDecimal needPayAmount = BigDecimal.ZERO;
                        // 如果开始时间在当前时间之前，证明先用后付，要计费。
                        if (currentTime.getTime() > statementDetailStartTime.getTime()) {
                            needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, currentTime, orderProductEquipmentDO.getProductEquipmentUnitAmount());
                        }
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            payReturnAmount = BigDecimalUtil.add(payReturnAmount, otherAmount);
                            otherAmount = BigDecimal.ZERO;
                        }
                        // 正常全额退
                        payReturnAmount = BigDecimalUtil.add(payReturnAmount, statementOrderDetailDO.getStatementDetailAmount());
                        // 退款金额，除了租金以外，保险金额也不能收了
                        payReturnAmount = BigDecimalUtil.add(payReturnAmount, amountSupport.calculateRentAmount(statementDetailStartTime, statementDetailEndTime, orderProductDO.getInsuranceAmount()));
                        // 退款金额，扣除未交款的部分
                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);
                        StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderProductEquipmentDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, returnOrderProductEquipmentDO.getReturnOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                        addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);

                        // 本项退货后，计算需要支付的金额
                    }
                }
            }
        }

        List<ReturnOrderMaterialBulkDO> returnOrderMaterialBulkDOList = returnOrderMaterialBulkMapper.findByReturnOrderNo(returnOrderNo);
        if (CollectionUtil.isNotEmpty(returnOrderMaterialBulkDOList)) {
            for (ReturnOrderMaterialBulkDO returnOrderMaterialBulkDO : returnOrderMaterialBulkDOList) {
                OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo(returnOrderMaterialBulkDO.getOrderNo(), returnOrderMaterialBulkDO.getBulkMaterialNo());
                if (orderMaterialBulkDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_BULK_NOT_EXISTS);
                    return result;
                }
                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderMaterialBulkDO.getOrderMaterialId());
                if (orderMaterialDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_MATERIAL_NOT_EXISTS);
                    return result;
                }
                totalReturnRentDepositAmount = BigDecimalUtil.add(totalReturnRentDepositAmount, BigDecimalUtil.div(orderMaterialDO.getRentDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), 2));
                totalReturnDepositAmount = BigDecimalUtil.add(totalReturnDepositAmount, BigDecimalUtil.div(orderMaterialDO.getDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), 2));

                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                        BigDecimal payReturnAmount = BigDecimal.ZERO;
                        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                            continue;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                        BigDecimal needPayAmount = BigDecimal.ZERO;
                        // 如果开始时间在当前时间之前，证明先用后付，要计费。
                        if (currentTime.getTime() > statementDetailStartTime.getTime()) {
                            needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, currentTime, orderMaterialBulkDO.getMaterialBulkUnitAmount());
                        }

                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            payReturnAmount = BigDecimalUtil.add(payReturnAmount, otherAmount);
                            otherAmount = BigDecimal.ZERO;
                        }
                        payReturnAmount = BigDecimalUtil.add(payReturnAmount, statementOrderDetailDO.getStatementDetailAmount());
                        // 退款金额，除了租金以外，保险金额也不能收了
                        payReturnAmount = BigDecimalUtil.add(payReturnAmount, amountSupport.calculateRentAmount(statementDetailStartTime, statementDetailEndTime, orderMaterialDO.getInsuranceAmount()));
                        // 退款金额，扣除未交款的部分
                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderMaterialBulkDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, returnOrderMaterialBulkDO.getReturnOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                        addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                    }
                }
            }
        }
        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
            // 如果没有处理完差价，统一当天交钱
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER, BigInteger.ZERO.intValue(), currentTime, currentTime, currentTime, otherAmount, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
        }
        saveStatementOrder(addStatementOrderDetailDOList, buyerCustomerId, currentTime, loginUser.getUserId());

        // TODO 退货完成后要退还租金押金和设备押金   totalReturnRentDepositAmount,totalReturnDepositAmount

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
        if (changeOrderDO == null) {
            result.setErrorCode(ErrorCode.CHANGE_ORDER_NOT_EXISTS);
            return result;
        }
        List<ChangeOrderProductEquipmentDO> changeOrderProductEquipmentDOList = changeOrderProductEquipmentMapper.findByChangeOrderNo(changeOrderNo);
        Integer buyerCustomerId = changeOrderDO.getCustomerId();
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        BigDecimal otherAmount = BigDecimalUtil.add(changeOrderDO.getServiceCost(), changeOrderDO.getDamageCost());
        Date statementDetailStartTime = currentTime;
        Date statementDetailEndTime;
        if (CollectionUtil.isNotEmpty(changeOrderProductEquipmentDOList)) {
            for (ChangeOrderProductEquipmentDO changeOrderProductEquipmentDO : changeOrderProductEquipmentDOList) {
                BigDecimal productEquipmentDiffAmount = changeOrderProductEquipmentDO.getPriceDiff();
                if (productEquipmentDiffAmount == null || BigDecimalUtil.compare(productEquipmentDiffAmount, BigDecimal.ZERO) <= 0) {
                    continue;
                }
                if (changeOrderProductEquipmentDO.getSrcEquipmentNo() == null || changeOrderProductEquipmentDO.getDestEquipmentNo() == null) {
                    continue;
                }
                OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderNoAndEquipmentNo(changeOrderProductEquipmentDO.getOrderNo(), changeOrderProductEquipmentDO.getSrcEquipmentNo());
                if (orderProductEquipmentDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_EXISTS);
                    return result;
                }
                OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
                if (orderProductDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_NOT_EXISTS);
                    return result;
                }

                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    statementDetailEndTime = orderProductEquipmentDO.getExpectReturnTime();
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, productEquipmentDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                    addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                    List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
                    if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                        BigDecimal payDiffAmount = BigDecimal.ZERO;
                        for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                            StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                                continue;
                            }
                            statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                            // 第一月差价,其他费用跟着下期一起
                            if (BigDecimalUtil.compare(payDiffAmount, BigDecimal.ZERO) == 0) {
                                if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                                    payDiffAmount = BigDecimalUtil.add(payDiffAmount, otherAmount);
                                    otherAmount = BigDecimal.ZERO;
                                }
                                payDiffAmount = BigDecimalUtil.add(payDiffAmount, amountSupport.calculateRentAmount(statementDetailStartTime, statementDetailEndTime, productEquipmentDiffAmount));
                            } else if (i == (statementOrderDetailDOList.size() - 1)) {
                                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                                payDiffAmount = amountSupport.calculateRentAmount(statementDetailStartTime, statementDetailEndTime, productEquipmentDiffAmount);
                            } else {
                                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                                payDiffAmount = productEquipmentDiffAmount;
                            }

                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, payDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
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
                if (changeOrderMaterialBulkDO.getSrcBulkMaterialNo() == null || changeOrderMaterialBulkDO.getDestBulkMaterialNo() == null) {
                    continue;
                }
                BigDecimal materialBulkDiffAmount = changeOrderMaterialBulkDO.getPriceDiff();
                OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderNoAndBulkMaterialNo(changeOrderMaterialBulkDO.getOrderNo(), changeOrderMaterialBulkDO.getSrcBulkMaterialNo());
                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderMaterialBulkDO.getOrderMaterialId());
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                    statementDetailEndTime = orderMaterialBulkDO.getExpectReturnTime();
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, materialBulkDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                    addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                } else if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                    List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
                    if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                        BigDecimal payDiffAmount = BigDecimal.ZERO;
                        for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                            StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                                continue;
                            }
                            statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                            // 第一月差价
                            if (BigDecimalUtil.compare(payDiffAmount, BigDecimal.ZERO) == 0) {
                                if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                                    payDiffAmount = BigDecimalUtil.add(payDiffAmount, otherAmount);
                                    otherAmount = BigDecimal.ZERO;
                                }
                                payDiffAmount = BigDecimalUtil.add(payDiffAmount, amountSupport.calculateRentAmount(statementDetailStartTime, statementDetailEndTime, materialBulkDiffAmount));
                            } else if (i == (statementOrderDetailDOList.size() - 1)) {
                                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                                payDiffAmount = BigDecimalUtil.sub(materialBulkDiffAmount, payDiffAmount);
                            } else {
                                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                                payDiffAmount = materialBulkDiffAmount;
                            }
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(statementOrderDetailDO.getCustomerId(), OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, payDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                        }
                    }
                }
            }
        }
        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
            // 如果没有处理完差价，统一当天交钱
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, changeOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER, BigInteger.ZERO.intValue(), currentTime, currentTime, currentTime, otherAmount, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
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
                    statementOrderDO.setStatementDepositAmount(statementOrderDetailDO.getStatementDetailDepositAmount());
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
                    statementOrderDO.setStatementDepositAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositAmount(), statementOrderDetailDO.getStatementDetailDepositAmount()));
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
            if (paymentCycle == null || paymentCycle >= rentTimeLength || paymentCycle == 0) {
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
    StatementOrderDetailDO calculateOneStatementOrderDetail(Integer rentType, Integer rentTimeLength, Integer payMode, Date rentStartTime, BigDecimal statementDetailAmount, Integer itemCount, BigDecimal insuranceAmount, BigDecimal totalRentDepositAmount, BigDecimal totalDepositAmount, BigDecimal otherAmount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId) {
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

        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, thisPhaseAmount, totalRentDepositAmount, totalDepositAmount, currentTime, loginUserId);
    }

    /**
     * 计算多期中的第一期明细
     */
    StatementOrderDetailDO calculateFirstStatementOrderDetail(Integer statementDays, Integer paymentCycle, Integer payMode, Date rentStartTime, BigDecimal unitAmount, Integer itemCount, BigDecimal insuranceAmount, BigDecimal totalRentDepositAmount, BigDecimal totalDepositAmount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId) {

        Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, paymentCycle);
        Calendar statementEndTimeCalendar = Calendar.getInstance();
        statementEndTimeCalendar.setTime(statementEndTime);
        statementEndTimeCalendar.set(Calendar.DAY_OF_MONTH, statementDays);
        statementEndTime = statementEndTimeCalendar.getTime();


        Date statementExpectPayTime;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
        } else {
            statementExpectPayTime = statementEndTime;
        }

        BigDecimal firstPhaseAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, BigDecimalUtil.mul(unitAmount, new BigDecimal(itemCount)));
        BigDecimal insuranceTotalAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, BigDecimalUtil.mul(insuranceAmount, new BigDecimal(itemCount)));
        firstPhaseAmount = BigDecimalUtil.add(firstPhaseAmount, insuranceTotalAmount);
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, firstPhaseAmount, totalRentDepositAmount, totalDepositAmount, currentTime, loginUserId);
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
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, lastPhaseStartTime, statementEndTime, lastPhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId);
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
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, thisPhaseStartTime, statementEndTime, middlePhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId);
    }

    StatementOrderDetailDO buildStatementOrderDetailDO(Integer customerId, Integer orderType, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date statementExpectPayTime, Date startTime, Date endTime, BigDecimal statementDetailRentAmount, BigDecimal statementDetailRentDepositAmount, BigDecimal statementDetailDepositAmount, Date currentTime, Integer loginUserId) {
        StatementOrderDetailDO statementOrderDetailDO = new StatementOrderDetailDO();
        statementOrderDetailDO.setCustomerId(customerId);
        statementOrderDetailDO.setOrderType(orderType);
        statementOrderDetailDO.setOrderId(orderId);
        statementOrderDetailDO.setOrderItemType(orderItemType);
        statementOrderDetailDO.setOrderItemReferId(orderItemReferId);
        statementOrderDetailDO.setStatementExpectPayTime(statementExpectPayTime);
        statementOrderDetailDO.setStatementStartTime(startTime);
        statementOrderDetailDO.setStatementEndTime(endTime);
        statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailRentDepositAmount, statementDetailRentAmount), statementDetailDepositAmount));
        statementOrderDetailDO.setStatementDetailRentDepositAmount(statementDetailRentDepositAmount);
        statementOrderDetailDO.setStatementDetailDepositAmount(statementDetailDepositAmount);
        statementOrderDetailDO.setStatementDetailRentAmount(statementDetailRentAmount);
        if (BigDecimalUtil.compare(statementDetailRentDepositAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(statementDetailDepositAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(statementDetailRentAmount, BigDecimal.ZERO) > 0) {
            statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
        } else {
            statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
        }
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
    private AmountSupport amountSupport;

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
