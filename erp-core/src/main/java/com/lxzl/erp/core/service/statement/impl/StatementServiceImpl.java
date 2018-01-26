package com.lxzl.erp.core.service.statement.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.callback.WeixinPayCallbackParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementPaySupport;
import com.lxzl.erp.core.service.statement.impl.support.StatementReturnSupport;
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
import com.lxzl.erp.dataaccess.domain.changeOrder.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.returnOrder.*;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementPayOrderDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    public ServiceResult<String, BigDecimal> calculateOrderFirstNeedPayAmount(String orderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        Date rentStartTime = orderDO.getRentStartTime();

        List<StatementOrderDetailDO> addStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId());

        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                // 核算本次应该交多少钱
                if (DateUtil.isSameDay(rentStartTime, statementOrderDetailDO.getStatementExpectPayTime())) {
                    thisNeedPayAmount = BigDecimalUtil.add(thisNeedPayAmount, statementOrderDetailDO.getStatementDetailAmount());
                }
            }
        }

        result.setResult(thisNeedPayAmount);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
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
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        Date rentStartTime = orderDO.getRentStartTime();

        List<StatementOrderDetailDO> addStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, loginUser.getUserId());
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUser.getUserId());

        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                // 核算本次应该交多少钱
                if (DateUtil.isSameDay(rentStartTime, statementOrderDetailDO.getStatementExpectPayTime())) {
                    thisNeedPayAmount = BigDecimalUtil.add(thisNeedPayAmount, statementOrderDetailDO.getStatementDetailAmount());
                }
            }
        }

        result.setResult(thisNeedPayAmount);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private List<StatementOrderDetailDO> generateStatementDetailList(OrderDO orderDO, Date currentTime, Integer loginUserId) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Date rentStartTime = orderDO.getRentStartTime();
        Integer buyerCustomerId = orderDO.getBuyerCustomerId();
        CustomerDO customerDO = customerMapper.findById(buyerCustomerId);
        if (customerDO == null) {
            return null;
        }
        Integer orderId = orderDO.getId();
        Integer statementDays;
        DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
        if (dataDictionaryDO == null) {
            statementDays = CommonConstant.SYSTEM_STATEMENT_DATE;
        } else {
            statementDays = Integer.parseInt(dataDictionaryDO.getDataName());
        }
        if (customerDO.getStatementDate() != null) {
            statementDays = customerDO.getStatementDate();
        }
        // 其他费用，包括运费、等费用
        BigDecimal otherAmount = orderDO.getLogisticsAmount();

        // 商品生成结算单
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                // 无论什么时候交租金，押金必须当天缴纳
                StatementOrderDetailDO depositDetail = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, orderProductDO.getRentDepositAmount(), orderProductDO.getDepositAmount(), otherAmount, currentTime, loginUserId);
                if (depositDetail != null) {
                    depositDetail.setStatementDetailPhase(0);
                    depositDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
                    addStatementOrderDetailDOList.add(depositDetail);
                    otherAmount = BigDecimal.ZERO;
                }
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductAmount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId);
                    if (statementOrderDetailDO != null) {
                        statementOrderDetailDO.setStatementDetailPhase(statementMonthCount);
                        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    }
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    BigDecimal itemAllAmount = orderProductDO.getProductAmount();
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), statementDays, orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                            }
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, rentStartTime, orderProductDO.getPayMode(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            }
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderProductDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, orderProductDO.getPaymentCycle(), orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), statementDays, orderProductDO.getPayMode(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                            }
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
                // 无论什么时候交租金，押金必须当天缴纳
                StatementOrderDetailDO depositDetail = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, orderMaterialDO.getRentDepositAmount(), orderMaterialDO.getDepositAmount(), otherAmount, currentTime, loginUserId);
                if (depositDetail != null) {
                    depositDetail.setStatementDetailPhase(0);
                    depositDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
                    addStatementOrderDetailDOList.add(depositDetail);
                    otherAmount = BigDecimal.ZERO;
                }
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialAmount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId);
                    if (statementOrderDetailDO != null) {
                        statementOrderDetailDO.setStatementDetailPhase(statementMonthCount);
                        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    }
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    BigDecimal itemAllAmount = orderMaterialDO.getMaterialAmount();
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), statementDays, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                            }
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, rentStartTime, orderMaterialDO.getPayMode(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            }
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderMaterialDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), statementDays, orderMaterialDO.getPayMode(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                            }
                        }
                    }
                }
            }
        }
        return addStatementOrderDetailDOList;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> weixinPayStatementOrder(String statementOrderNo, String openId, String ip) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();
        Date currentTime = new Date();
        StatementOrderDO statementOrderDO = statementOrderMapper.findByNo(statementOrderNo);
        if (statementOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        String payVerifyResult = payVerify(statementOrderDO);
        if (!ErrorCode.SUCCESS.equals(payVerifyResult)) {
            result.setErrorCode(payVerifyResult);
            return result;
        }

        CustomerDO customerDO = customerMapper.findById(statementOrderDO.getCustomerId());
        BigDecimal payRentAmount = BigDecimalUtil.add(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDO.getStatementRentPaidAmount()), statementOrderDO.getStatementOverdueAmount());
        BigDecimal payRentDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDO.getStatementRentDepositPaidAmount());
        BigDecimal payDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDO.getStatementDepositPaidAmount());
        BigDecimal otherAmount = BigDecimalUtil.sub(statementOrderDO.getStatementOtherAmount(), statementOrderDO.getStatementOtherPaidAmount());
        BigDecimal totalAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(payRentAmount, payRentDepositAmount), payDepositAmount), otherAmount);

        boolean haveOldRecord = false;
        StatementPayOrderDO statementPayOrderLastRecord = statementPaySupport.getLastRecord(statementOrderDO.getId());
        if (statementPayOrderLastRecord != null && PayStatus.PAY_STATUS_PAYING.equals(statementPayOrderLastRecord.getPayStatus())) {
            // 如果本次支付和上一次支付价格不同
            if ((statementPayOrderLastRecord.getCreateTime().getTime() + (90 * 60 * 1000)) <= currentTime.getTime()) {
                // 查询时间过去多久了，如果超过90分钟，即为失效，重新下单
                boolean updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderLastRecord.getId(), PayStatus.PAY_STATUS_TIME_OUT, null, loginUserId, currentTime);
                if (!updatePayOrderResult) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    result.setErrorCode(ErrorCode.STATEMENT_PAY_FAILED);
                    return result;
                }
            } else if (BigDecimalUtil.compare(statementPayOrderLastRecord.getPayAmount(), totalAmount) != 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(ErrorCode.STATEMENT_PAY_AMOUNT_DIFF_ERROR);
                return result;
            } else {
                haveOldRecord = true;
            }
        }
        StatementPayOrderDO statementPayOrderDO;
        if (haveOldRecord) {
            statementPayOrderDO = statementPayOrderLastRecord;
        } else {
            statementPayOrderDO = statementPaySupport.saveStatementPayOrder(statementOrderDO.getId(), totalAmount, payRentAmount, payRentDepositAmount, payDepositAmount, otherAmount, StatementOrderPayType.PAY_TYPE_WEIXIN, loginUserId, currentTime);
        }

        if (statementPayOrderDO == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.AMOUNT_MAST_MORE_THEN_ZERO);
            return result;
        }

        logger.info("wechat pay customer: {} , orderNo: {} , payRentAmount: {} , payRentDepositAmount: {} , payDepositAmount: {} , otherAmount: {} , totalAmount: {} , ",
                customerDO.getCustomerNo(), statementPayOrderDO.getPaymentOrderNo(), payRentAmount, payDepositAmount, otherAmount, totalAmount);
        ServiceResult<String, String> wechatPayResult = paymentService.wechatPay(customerDO.getCustomerNo(), statementPayOrderDO.getPaymentOrderNo(), statementOrderDO.getRemark(), totalAmount, openId, ip, loginUserId);
        if (!ErrorCode.SUCCESS.equals(wechatPayResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(wechatPayResult.getErrorCode());
            return result;
        }
        result.setResult(wechatPayResult.getResult());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> weixinPayCallback(WeixinPayCallbackParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();

        Date currentTime = new Date();
        Integer loginUserId = CommonConstant.SUPER_USER_ID;
        if (param == null
                || (!WeixinPayCallbackParam.NOTIFY_STATUS_SUCCESS.equals(param.getNotifyStatus()) && !WeixinPayCallbackParam.NOTIFY_STATUS_FAIL.equals(param.getNotifyStatus()))) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        StatementPayOrderDO statementPayOrderDO = statementPaySupport.findByNo(param.getBusinessOrderNo());
        if (statementPayOrderDO == null
                || PayStatus.PAY_STATUS_PAID.equals(statementPayOrderDO.getPayStatus())
                || PayStatus.PAY_STATUS_FAILED.equals(statementPayOrderDO.getPayStatus())) {
            result.setErrorCode(ErrorCode.SYSTEM_EXCEPTION);
            return result;
        }

        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementPayOrderDO.getStatementOrderId());
        updateStatementOrderResult(statementOrderDO, statementPayOrderDO.getOtherAmount(), statementPayOrderDO.getPayRentAmount(), statementPayOrderDO.getPayRentDepositAmount(), statementPayOrderDO.getPayDepositAmount(), currentTime, loginUserId);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    String payVerify(StatementOrderDO statementOrderDO) {
        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus())
                || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDO.getStatementStatus())) {
            return ErrorCode.STATEMENT_ORDER_STATUS_ERROR;
        }
        List<StatementOrderDO> statementOrderDOList = statementOrderMapper.findByCustomerId(statementOrderDO.getCustomerId());
        for (StatementOrderDO dbStatementOrderDO : statementOrderDOList) {
            if (!StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(dbStatementOrderDO.getStatementStatus())
                    && !StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(dbStatementOrderDO.getStatementStatus())) {
                if (dbStatementOrderDO.getStatementOrderNo().equals(statementOrderDO.getStatementOrderNo())) {
                    break;
                }
                return ErrorCode.STATEMENT_ORDER_CAN_NOT_PAID_THIS;
            }
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Boolean> payStatementOrder(String statementOrderNo) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();

        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        StatementOrderDO statementOrderDO = statementOrderMapper.findByNo(statementOrderNo);
        if (statementOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        String payVerifyResult = payVerify(statementOrderDO);
        if (!ErrorCode.SUCCESS.equals(payVerifyResult)) {
            result.setErrorCode(payVerifyResult);
            return result;
        }
        CustomerDO customerDO = customerMapper.findById(statementOrderDO.getCustomerId());
        BigDecimal payRentAmount = BigDecimalUtil.add(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDO.getStatementRentPaidAmount()), statementOrderDO.getStatementOverdueAmount());
        BigDecimal payRentDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDO.getStatementRentDepositPaidAmount());
        BigDecimal payDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDO.getStatementDepositPaidAmount());
        BigDecimal otherAmount = BigDecimalUtil.sub(statementOrderDO.getStatementOtherAmount(), statementOrderDO.getStatementOtherPaidAmount());
        BigDecimal totalAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(payRentAmount, payRentDepositAmount), payDepositAmount), otherAmount);
        StatementPayOrderDO statementPayOrderDO = statementPaySupport.saveStatementPayOrder(statementOrderDO.getId(), totalAmount, payRentAmount, payRentDepositAmount, payDepositAmount, otherAmount, StatementOrderPayType.PAY_TYPE_BALANCE, loginUser.getUserId(), currentTime);
        if (statementPayOrderDO == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.AMOUNT_MAST_MORE_THEN_ZERO);
            return result;
        }
        ServiceResult<String, Boolean> paymentResult = paymentService.balancePay(customerDO.getCustomerNo(), statementPayOrderDO.getPaymentOrderNo(), statementOrderDO.getRemark(), payRentAmount, payRentDepositAmount, payDepositAmount, otherAmount);
        if (!ErrorCode.SUCCESS.equals(paymentResult.getErrorCode()) || !paymentResult.getResult()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(paymentResult.getErrorCode());
            return result;
        }
        boolean updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderDO.getId(), PayStatus.PAY_STATUS_PAID, null, loginUser.getUserId(), currentTime);
        if (!updatePayOrderResult) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.STATEMENT_PAY_FAILED);
            return result;
        }

        updateStatementOrderResult(statementOrderDO, otherAmount, payRentAmount, payRentDepositAmount, payDepositAmount, currentTime, loginUser.getUserId());
        result.setResult(true);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    void updateStatementOrderResult(StatementOrderDO statementOrderDO, BigDecimal otherAmount, BigDecimal rentAmount, BigDecimal rentDepositAmount, BigDecimal depositAmount, Date currentTime, Integer loginUserId) {

        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        statementOrderDO.setStatementOtherPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementOtherPaidAmount(), otherAmount));
        statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentPaidAmount(), rentAmount));
        statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositPaidAmount(), rentDepositAmount));
        statementOrderDO.setStatementDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositPaidAmount(), depositAmount));
        statementOrderDO.setStatementPaidTime(currentTime);
        statementOrderDO.setUpdateTime(currentTime);
        statementOrderDO.setUpdateUser(loginUserId.toString());
        statementOrderMapper.update(statementOrderDO);
        Map<Integer, BigDecimal> orderPaidMap = new HashMap<>();
        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
                BigDecimal needStatementDetailRentPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(), statementOrderDetailDO.getStatementDetailRentPaidAmount());
                statementOrderDetailDO.setStatementDetailRentPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), needStatementDetailRentPayAmount));
                BigDecimal needStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                statementOrderDetailDO.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), needStatementDetailRentDepositPayAmount));
                BigDecimal needStatementDetailDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositAmount(), statementOrderDetailDO.getStatementDetailDepositPaidAmount());
                statementOrderDetailDO.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), needStatementDetailDepositPayAmount));
                BigDecimal needStatementDetailOtherPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOtherAmount(), statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                statementOrderDetailDO.setStatementDetailOtherPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), needStatementDetailOtherPayAmount));
                statementOrderDetailDO.setStatementDetailPaidTime(currentTime);
                statementOrderDetailDO.setUpdateTime(currentTime);
                statementOrderDetailDO.setUpdateUser(loginUserId.toString());
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
            orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), OrderStatus.ORDER_STATUS_PAID, null, currentTime, loginUserId);
        }
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
        StatementOrder statementOrder = ConverterUtil.convert(statementOrderDO, StatementOrder.class);
        if (statementOrder != null && CollectionUtil.isNotEmpty(statementOrder.getStatementOrderDetailList())) {
            for (StatementOrderDetail statementOrderDetail : statementOrder.getStatementOrderDetailList()) {
                convertStatementOrderDetailOtherInfo(statementOrderDetail);
            }
        }

        result.setResult(statementOrder);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, StatementOrder> queryStatementOrderDetailByOrderId(String orderNo) {
        ServiceResult<String, StatementOrder> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        StatementOrder statementOrder = new StatementOrder();
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
        List<StatementOrderDetail> statementOrderDetailList = ConverterUtil.convertList(statementOrderDetailDOList, StatementOrderDetail.class);

        Integer customerId = null;
        if (CollectionUtil.isNotEmpty(statementOrderDetailList)) {
            for (StatementOrderDetail statementOrderDetail : statementOrderDetailList) {
                if (customerId == null) {
                    customerId = statementOrderDetail.getCustomerId();
                }
                convertStatementOrderDetailOtherInfo(statementOrderDetail);

                statementOrder.setStatementAmount(BigDecimalUtil.add(statementOrder.getStatementAmount(), statementOrderDetail.getStatementDetailAmount()));
                statementOrder.setStatementPaidAmount(BigDecimalUtil.add(statementOrder.getStatementPaidAmount(), statementOrderDetail.getStatementDetailPaidAmount()));
                statementOrder.setStatementRentDepositAmount(BigDecimalUtil.add(statementOrder.getStatementRentDepositAmount(), statementOrderDetail.getStatementDetailRentDepositAmount()));
                statementOrder.setStatementRentDepositPaidAmount(BigDecimalUtil.add(statementOrder.getStatementRentDepositPaidAmount(), statementOrderDetail.getStatementDetailRentDepositPaidAmount()));
                statementOrder.setStatementRentDepositReturnAmount(BigDecimalUtil.add(statementOrder.getStatementRentDepositReturnAmount(), statementOrderDetail.getStatementDetailRentDepositReturnAmount()));
                statementOrder.setStatementDepositAmount(BigDecimalUtil.add(statementOrder.getStatementDepositAmount(), statementOrderDetail.getStatementDetailDepositAmount()));
                statementOrder.setStatementDepositPaidAmount(BigDecimalUtil.add(statementOrder.getStatementDepositPaidAmount(), statementOrderDetail.getStatementDetailDepositPaidAmount()));
                statementOrder.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrder.getStatementDepositReturnAmount(), statementOrderDetail.getStatementDetailDepositReturnAmount()));
                statementOrder.setStatementRentAmount(BigDecimalUtil.add(statementOrder.getStatementRentAmount(), statementOrderDetail.getStatementDetailRentAmount()));
                statementOrder.setStatementRentPaidAmount(BigDecimalUtil.add(statementOrder.getStatementRentPaidAmount(), statementOrderDetail.getStatementDetailRentPaidAmount()));
                statementOrder.setStatementOverdueAmount(BigDecimalUtil.add(statementOrder.getStatementOverdueAmount(), statementOrderDetail.getStatementDetailOverdueAmount()));
            }
        }
        statementOrder.setStatementOrderDetailList(statementOrderDetailList);

        CustomerDO customerDO = customerMapper.findById(customerId);
        if (customerDO != null) {
            statementOrder.setCustomerId(customerId);
            statementOrder.setCustomerName(customerDO.getCustomerName());
        }

        result.setResult(statementOrder);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private void convertStatementOrderDetailOtherInfo(StatementOrderDetail statementOrderDetail) {
        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
            OrderDO orderDO = orderMapper.findByOrderId(statementOrderDetail.getOrderId());
            statementOrderDetail.setOrderNo(orderDO.getOrderNo());
            if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
                for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(orderProductDO.getId())) {
                        statementOrderDetail.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                        statementOrderDetail.setItemCount(orderProductDO.getProductCount());
                        statementOrderDetail.setUnitAmount(orderProductDO.getProductUnitAmount());
                        statementOrderDetail.setItemRentType(orderProductDO.getRentType());
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
                for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                    if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(orderMaterialDO.getId())) {
                        statementOrderDetail.setItemName(orderMaterialDO.getMaterialName());
                        statementOrderDetail.setItemCount(orderMaterialDO.getMaterialCount());
                        statementOrderDetail.setUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                        statementOrderDetail.setItemRentType(orderMaterialDO.getRentType());
                    }
                }
            }
        }
        if (OrderType.ORDER_TYPE_RETURN.equals(statementOrderDetail.getOrderType())) {
            ReturnOrderDO returnOrderDO = returnOrderMapper.findById(statementOrderDetail.getOrderId());
            statementOrderDetail.setOrderNo(returnOrderDO.getReturnOrderNo());
            if (CollectionUtil.isNotEmpty(returnOrderDO.getReturnOrderProductDOList())) {
                for (ReturnOrderProductDO returnOrderProductDO : returnOrderDO.getReturnOrderProductDOList()) {
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(returnOrderProductDO.getId())) {
                        Product product = FastJsonUtil.toBean(returnOrderProductDO.getReturnProductSkuSnapshot(), Product.class);
                        if (CollectionUtil.isNotEmpty(product.getProductSkuList())) {
                            statementOrderDetail.setItemName(product.getProductName() + product.getProductSkuList().get(0).getSkuName());
                        }
                        statementOrderDetail.setItemCount(returnOrderProductDO.getRealReturnProductSkuCount());
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(returnOrderDO.getReturnOrderMaterialDOList())) {
                for (ReturnOrderMaterialDO returnOrderMaterialDO : returnOrderDO.getReturnOrderMaterialDOList()) {
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(returnOrderMaterialDO.getId())) {
                        Material material = FastJsonUtil.toBean(returnOrderMaterialDO.getReturnMaterialSnapshot(), Material.class);
                        statementOrderDetail.setItemName(material.getMaterialName());
                        statementOrderDetail.setItemCount(returnOrderMaterialDO.getRealReturnMaterialCount());
                    }
                }
            }
        }
        if (OrderType.ORDER_TYPE_CHANGE.equals(statementOrderDetail.getOrderType())) {
            ChangeOrderDO changeOrderDO = changeOrderMapper.findById(statementOrderDetail.getOrderId());
            statementOrderDetail.setOrderNo(changeOrderDO.getChangeOrderNo());
            if (CollectionUtil.isNotEmpty(changeOrderDO.getChangeOrderProductDOList())) {
                for (ChangeOrderProductDO changeOrderProductDO : changeOrderDO.getChangeOrderProductDOList()) {
                    if (OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(changeOrderDO.getId())) {
                        Product product = FastJsonUtil.toBean(changeOrderProductDO.getSrcChangeProductSkuSnapshot(), Product.class);
                        if (CollectionUtil.isNotEmpty(product.getProductSkuList())) {
                            statementOrderDetail.setItemName(product.getProductName() + product.getProductSkuList().get(0).getSkuName());
                        }
                        statementOrderDetail.setItemCount(changeOrderProductDO.getRealChangeProductSkuCount());
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(changeOrderDO.getChangeOrderMaterialDOList())) {
                for (ChangeOrderMaterialDO changeOrderMaterialDO : changeOrderDO.getChangeOrderMaterialDOList()) {
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(changeOrderMaterialDO.getId())) {
                        Material material = FastJsonUtil.toBean(changeOrderMaterialDO.getSrcChangeMaterialSnapshot(), Material.class);
                        statementOrderDetail.setItemName(material.getMaterialName());
                        statementOrderDetail.setItemCount(changeOrderMaterialDO.getRealChangeMaterialCount());
                    }
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createReturnOrderStatement(String returnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        ReturnOrderDO returnOrderDO = returnOrderMapper.findByNo(returnOrderNo);
        if (returnOrderDO == null) {
            result.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return result;
        }

        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        Map<String, StatementOrderDetailDO> addStatementOrderDetailDOMap = new HashMap<>();
        Integer buyerCustomerId = returnOrderDO.getCustomerId();
        Date statementDetailStartTime;
        Date statementDetailEndTime;
        // 其他的费用 包括服务费等费用
        BigDecimal otherAmount = BigDecimalUtil.add(returnOrderDO.getServiceCost(), returnOrderDO.getDamageCost());

        // 租金押金和设备押金
        BigDecimal totalReturnRentDepositAmount = BigDecimal.ZERO, totalReturnDepositAmount = BigDecimal.ZERO;
        Date returnTime = returnOrderDO.getReturnTime();

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

                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
                BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.div(orderProductDO.getRentDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE);
                BigDecimal thisReturnDepositAmount = BigDecimalUtil.div(orderProductDO.getDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE);
                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                            // 第一期如果支付了押金，就要退押金，否则不退了
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                                StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
                                if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementOrderDetailDO.getStatementDetailDepositReturnAmount()), thisReturnDepositAmount) >= 0) {
                                    totalReturnDepositAmount = BigDecimalUtil.add(totalReturnDepositAmount, thisReturnDepositAmount);
                                    statementOrderDetailDO.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositReturnAmount(), thisReturnDepositAmount));
                                    statementOrderDetailDO.setStatementDetailDepositReturnTime(currentTime);
                                    statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositReturnAmount(), thisReturnDepositAmount));
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnRentDepositAmount, returnTime, loginUser.getUserId(), currentTime);
                                }
                                if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount()), thisReturnRentDepositAmount) >= 0) {
                                    totalReturnRentDepositAmount = BigDecimalUtil.add(totalReturnRentDepositAmount, thisReturnRentDepositAmount);
                                    statementOrderDetailDO.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositReturnAmount(), thisReturnRentDepositAmount));
                                    statementOrderDetailDO.setStatementDetailRentDepositReturnTime(currentTime);
                                    statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositReturnAmount(), thisReturnRentDepositAmount));
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_RENT_DEPOSIT, thisReturnRentDepositAmount, returnTime, loginUser.getUserId(), currentTime);
                                }
                                statementOrderDetailMapper.update(statementOrderDetailDO);
                                statementOrderMapper.update(statementOrderDO);
                            }
                            continue;
                        }

                        // 如果有押金还没交，不让退货
                        if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            result.setErrorCode(ErrorCode.STATEMENT_ORDER_CREATE_ERROR);
                            return result;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                        // 如果退货时间在结束时间之后，证明本期都没缴，不用处理，等待交
                        if (returnTime.getTime() > statementDetailEndTime.getTime()) {
                            continue;
                        }
                        BigDecimal needPayAmount = BigDecimal.ZERO;
                        if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
                            if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (!DateUtil.isSameDay(returnTime, statementDetailStartTime) && returnTime.getTime() > statementDetailStartTime.getTime()) {
                                    needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, returnTime, orderProductEquipmentDO.getProductEquipmentUnitAmount());
                                }
                            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderProductDO.getPayMode())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (!DateUtil.isSameDay(returnTime, statementDetailStartTime) && returnTime.getTime() > statementDetailStartTime.getTime()) {
                                    BigDecimal firstAmount = BigDecimalUtil.div(BigDecimalUtil.mul(orderProductDO.getProductAmount(), new BigDecimal(0.3)), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE);
                                    // 30%期
                                    if (statementOrderDetailDO.getStatementDetailPhase() == 1) {
                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(orderProductEquipmentDO.getProductEquipmentUnitAmount(), new BigDecimal(surplusDays)) : needPayAmount;
                                        needPayAmount = BigDecimalUtil.compare(needPayAmount, firstAmount) > 0 ? firstAmount : needPayAmount;
                                    } else if (statementOrderDetailDO.getStatementDetailPhase() == 2) {
                                        // 70% 期
                                        // 加2的原因是算上前面30%的那一天
                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 2;
                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(orderProductEquipmentDO.getProductEquipmentUnitAmount(), new BigDecimal(surplusDays)) : needPayAmount;
                                        // 减掉上期的30%
                                        needPayAmount = BigDecimalUtil.sub(needPayAmount, firstAmount);
                                    }
                                }
                            } else {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (!DateUtil.isSameDay(returnTime, statementDetailStartTime) && returnTime.getTime() > statementDetailStartTime.getTime()) {
                                    int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                    needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(orderProductEquipmentDO.getProductEquipmentUnitAmount(), new BigDecimal(surplusDays)) : needPayAmount;
                                }
                            }
                        }
                        // 正常全额退
                        BigDecimal payReturnAmount = BigDecimalUtil.div(statementOrderDetailDO.getStatementDetailAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE);
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            payReturnAmount = BigDecimalUtil.sub(payReturnAmount, otherAmount);
                            otherAmount = BigDecimal.ZERO;
                        }
                        // 退款金额，扣除未交款的部分
                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT + "-" + orderProductDO.getId() + "-" + orderProductDO.getProductSkuId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();;
                        if (addStatementOrderDetailDOMap.get(key) != null) {
                            StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                            thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount,new BigDecimal(-1))));
                            thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount,new BigDecimal(-1))));
                            addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                        } else {
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderProductEquipmentDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, returnOrderProductEquipmentDO.getReturnOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            if (thisStatementOrderDetailDO != null) {
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                                addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                            }
                        }
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

                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
                BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.div(orderMaterialDO.getRentDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE);
                BigDecimal thisReturnDepositAmount = BigDecimalUtil.div(orderMaterialDO.getDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE);
                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                            // 第一期如果支付了押金，就要退押金，否则不退了
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                                StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
                                if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementOrderDetailDO.getStatementDetailDepositReturnAmount()), thisReturnDepositAmount) >= 0) {
                                    totalReturnDepositAmount = BigDecimalUtil.add(totalReturnDepositAmount, thisReturnDepositAmount);
                                    statementOrderDetailDO.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositReturnAmount(), thisReturnDepositAmount));
                                    statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositReturnAmount(), thisReturnDepositAmount));
                                }
                                if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount()), thisReturnRentDepositAmount) >= 0) {
                                    totalReturnRentDepositAmount = BigDecimalUtil.add(totalReturnRentDepositAmount, thisReturnRentDepositAmount);
                                    statementOrderDetailDO.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositReturnAmount(), thisReturnRentDepositAmount));
                                    statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositReturnAmount(), thisReturnRentDepositAmount));
                                }
                                statementOrderDetailMapper.update(statementOrderDetailDO);
                                statementOrderMapper.update(statementOrderDO);
                            }
                            continue;
                        }

                        // 如果有押金还没交，不让退货
                        if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            result.setErrorCode(ErrorCode.STATEMENT_ORDER_CREATE_ERROR);
                            return result;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                        // 如果退货时间在结束时间之后，证明本期都没缴，不用处理，等待交
                        if (returnTime.getTime() > statementDetailEndTime.getTime()) {
                            continue;
                        }

                        BigDecimal needPayAmount = BigDecimal.ZERO;

                        if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
                            if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (!DateUtil.isSameDay(returnTime, statementDetailStartTime) && returnTime.getTime() > statementDetailStartTime.getTime()) {
                                    needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, returnTime, orderMaterialBulkDO.getMaterialBulkUnitAmount());
                                }
                            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderMaterialDO.getPayMode())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (!DateUtil.isSameDay(returnTime, statementDetailStartTime) && returnTime.getTime() > statementDetailStartTime.getTime()) {
                                    BigDecimal firstAmount = BigDecimalUtil.div(BigDecimalUtil.mul(orderMaterialDO.getMaterialAmount(), new BigDecimal(0.3)), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE);
                                    // 30%期
                                    if (statementOrderDetailDO.getStatementDetailPhase() == 1) {
                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(orderMaterialBulkDO.getMaterialBulkUnitAmount(), new BigDecimal(surplusDays)) : needPayAmount;
                                        needPayAmount = BigDecimalUtil.compare(needPayAmount, firstAmount) > 0 ? firstAmount : needPayAmount;
                                    } else if (statementOrderDetailDO.getStatementDetailPhase() == 2) {
                                        // 70% 期
                                        // 加2的原因是算上前面30%的那一天
                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 2;
                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(orderMaterialBulkDO.getMaterialBulkUnitAmount(), new BigDecimal(surplusDays)) : needPayAmount;
                                        // 减掉上期的30%
                                        needPayAmount = BigDecimalUtil.sub(needPayAmount, firstAmount);
                                    }
                                }
                            } else {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (!DateUtil.isSameDay(returnTime, statementDetailStartTime) && returnTime.getTime() > statementDetailStartTime.getTime()) {
                                    int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                    needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(orderMaterialBulkDO.getMaterialBulkUnitAmount(), new BigDecimal(surplusDays)) : needPayAmount;
                                }
                            }
                        }

                        BigDecimal payReturnAmount = BigDecimalUtil.div(statementOrderDetailDO.getStatementDetailAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE);
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            payReturnAmount = BigDecimalUtil.sub(payReturnAmount, otherAmount);
                            otherAmount = BigDecimal.ZERO;
                        }
                        // 退款金额，扣除未交款的部分
                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL + "-" +  orderMaterialDO.getId() + "-" + orderMaterialDO.getMaterialId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
                        if (addStatementOrderDetailDOMap.get(key) != null) {
                            StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                            thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount,new BigDecimal(-1))));
                            thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount,new BigDecimal(-1))));
                            addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                        } else {
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderMaterialBulkDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, returnOrderMaterialBulkDO.getReturnOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            if (thisStatementOrderDetailDO != null) {
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                                addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                            }
                        }
                    }
                }
            }
        }
        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
            String key = "";
            // 如果没有处理完差价，统一当天交钱
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER, BigInteger.ZERO.intValue(), currentTime, currentTime, currentTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUser.getUserId());
            if (thisStatementOrderDetailDO != null) {
                thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OTHER);
                addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
            }
        }

        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        if (!addStatementOrderDetailDOMap.isEmpty()) {
            for (Map.Entry<String, StatementOrderDetailDO> entry : addStatementOrderDetailDOMap.entrySet()) {
                addStatementOrderDetailDOList.add(entry.getValue());
            }
        }
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUser.getUserId());

        // TODO 退货完成后要退还租金押金和设备押金   totalReturnRentDepositAmount,totalReturnDepositAmount
        if (BigDecimalUtil.compare(totalReturnRentDepositAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(totalReturnDepositAmount, BigDecimal.ZERO) > 0) {
            ServiceResult<String, Boolean> returnDepositResult = paymentService.returnDeposit(returnOrderDO.getCustomerNo(), totalReturnRentDepositAmount, totalReturnDepositAmount);
            if (!ErrorCode.SUCCESS.equals(returnDepositResult.getErrorCode()) || !returnDepositResult.getResult()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(returnDepositResult.getErrorCode());
                return result;
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, productEquipmentDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
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

                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, payDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            if (thisStatementOrderDetailDO != null) {
                                addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                            }
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
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, materialBulkDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
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
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(statementOrderDetailDO.getCustomerId(), OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, payDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            if (thisStatementOrderDetailDO != null) {
                                addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                            }
                        }
                    }
                }
            }
        }
        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
            // 如果没有处理完差价，统一当天交钱
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, changeOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER, BigInteger.ZERO.intValue(), currentTime, currentTime, currentTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUser.getUserId());
            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
        }
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUser.getUserId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    void saveStatementOrder(List<StatementOrderDetailDO> addStatementOrderDetailDOList, Date currentTime, Integer loginUserId) {

        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            // 同一个时间的做归集
            Map<Date, StatementOrderDO> statementOrderDOMap = new HashMap<>();
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                if (statementOrderDetailDO == null) {
                    continue;
                }
                Date dateKey = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementExpectPayTime());
                StatementOrderDO statementOrderDO = statementOrderMapper.findByCustomerAndPayTime(statementOrderDetailDO.getCustomerId(), dateKey);
                if (statementOrderDO != null) {
                    statementOrderDOMap.put(dateKey, statementOrderDO);
                }

                if (!statementOrderDOMap.containsKey(dateKey)) {
                    statementOrderDO = new StatementOrderDO();
                    statementOrderDO.setStatementOrderNo(generateNoSupport.generateStatementOrderNo(dateKey, statementOrderDetailDO.getCustomerId()));
                    statementOrderDO.setCustomerId(statementOrderDetailDO.getCustomerId());
                    statementOrderDO.setStatementExpectPayTime(dateKey);
                    statementOrderDO.setStatementAmount(statementOrderDetailDO.getStatementDetailAmount());
                    statementOrderDO.setStatementRentDepositAmount(statementOrderDetailDO.getStatementDetailRentDepositAmount());
                    statementOrderDO.setStatementDepositAmount(statementOrderDetailDO.getStatementDetailDepositAmount());
                    statementOrderDO.setStatementRentAmount(statementOrderDetailDO.getStatementDetailRentAmount());
                    statementOrderDO.setStatementOtherAmount(statementOrderDetailDO.getStatementDetailOtherAmount());
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
                    // 结算单不能为负数
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) < 0) {
                        BigDecimal diffAmount = BigDecimalUtil.sub(BigDecimal.ZERO, statementOrderDO.getStatementAmount());
                        statementOrderDO.setStatementAmount(BigDecimal.ZERO);
                        statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(), diffAmount));
                    }
                    statementOrderDO.setStatementDepositAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositAmount(), statementOrderDetailDO.getStatementDetailDepositAmount()));
                    statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositAmount()));
                    statementOrderDO.setStatementRentAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentAmount(), statementOrderDetailDO.getStatementDetailRentAmount()));
                    statementOrderDO.setStatementOtherAmount(BigDecimalUtil.add(statementOrderDO.getStatementOtherAmount(), statementOrderDetailDO.getStatementDetailOtherAmount()));
                    if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus())) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
                    }
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) == 0) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
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
            if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
                statementOrderDetailMapper.saveList(addStatementOrderDetailDOList);
            }
        }
    }

    Integer calculateStatementMonthCount(Integer rentType, Integer rentTimeLength, Integer paymentCycle, Integer payMode, int startDay, int statementDay) {
        Integer statementMonthCount = 1;
        // 如果租赁类型为次和天的，那么需要一次性付清，如果为月的，分期付
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(payMode)) {
            return ++statementMonthCount;
        } else if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return statementMonthCount;
        } else {
            if (paymentCycle == null || paymentCycle >= rentTimeLength || paymentCycle == 0) {
                return statementMonthCount;
            }
            statementMonthCount = rentTimeLength / paymentCycle;
            if ((rentTimeLength % paymentCycle > 0) || (startDay > (statementDay + 1))) {
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
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, rentTimeLength - 1);
            if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
                statementExpectPayTime = rentStartTime;
            } else if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)) {
                statementExpectPayTime = statementEndTime;
            }
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, -1);
            if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
                statementExpectPayTime = rentStartTime;
            } else if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(payMode)) {
                statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
            }
        }

        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, statementDetailAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId);
    }

    /**
     * 计算多期中的第一期明细
     */
    StatementOrderDetailDO calculateFirstStatementOrderDetail(Integer rentType, Integer rentLength, Integer statementDays, Integer paymentCycle, Integer payMode, Date rentStartTime, BigDecimal unitAmount, Integer itemCount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId) {

        Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, paymentCycle);
        Calendar statementEndTimeCalendar = Calendar.getInstance();
        statementEndTimeCalendar.setTime(statementEndTime);
        int statementEndMonthDays = DateUtil.getActualMaximum(statementEndTime);
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            // 不做任何动作，只是当天
        } else if (statementDays > statementEndMonthDays) {
            statementEndTimeCalendar.set(Calendar.DAY_OF_MONTH, statementEndMonthDays);
            statementEndTime = statementEndTimeCalendar.getTime();
        } else {
            statementEndTimeCalendar.set(Calendar.DAY_OF_MONTH, statementDays);
            statementEndTime = statementEndTimeCalendar.getTime();
        }

        Date statementExpectPayTime = null;
        BigDecimal firstPhaseAmount;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
            firstPhaseAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, BigDecimalUtil.mul(unitAmount, new BigDecimal(itemCount)));
        } else if (OrderRentType.RENT_TYPE_DAY.equals(rentType) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
            firstPhaseAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(BigDecimalUtil.mul(unitAmount, new BigDecimal(itemCount)), new BigDecimal(rentLength)), new BigDecimal(0.3));
        } else {
            // 如果按天的，本期结束就要还款，如果是按月的，本期结束第二天还款
            if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
                statementExpectPayTime = statementEndTime;
            } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
                statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, 1);
            }
            firstPhaseAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, BigDecimalUtil.mul(unitAmount, new BigDecimal(itemCount)));
        }
        // 先不考虑保险
        /*BigDecimal insuranceTotalAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, BigDecimalUtil.mul(insuranceAmount, new BigDecimal(itemCount)));
        firstPhaseAmount = BigDecimalUtil.add(firstPhaseAmount, insuranceTotalAmount);*/
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, firstPhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId);
    }

    /**
     * 计算多期中的最后一期明细
     */
    StatementOrderDetailDO calculateLastStatementOrderDetail(Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date lastCalculateDate, Date rentStartTime, Integer payMode, Integer rentType, Integer rentTimeLength, BigDecimal itemAllAmount, BigDecimal alreadyPaidAmount, Date currentTime, Integer loginUserId) {
        // 最后一期
        Date lastPhaseStartTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(lastCalculateDate, 1);
        // 最后一期的结束时间
        Date statementEndTime = null;
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(rentStartTime, rentTimeLength - 1);
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, rentTimeLength);
            statementEndTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, -1);
        }

        lastPhaseStartTime = lastPhaseStartTime.getTime() > statementEndTime.getTime() ? statementEndTime : lastPhaseStartTime;
        Date statementExpectPayTime = null;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = lastPhaseStartTime;
        } else {
            // 如果按天的，本期结束就要还款，如果是按月的，本期结束第二天还款
            if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
                statementExpectPayTime = statementEndTime;
            } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
                statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, 1);
            }
        }
        // 最后一期的金额
        BigDecimal lastPhaseAmount = BigDecimalUtil.sub(itemAllAmount, alreadyPaidAmount);
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, lastPhaseStartTime, statementEndTime, lastPhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId);
    }

    /**
     * 计算多期中的中间期明细
     */
    StatementOrderDetailDO calculateMiddleStatementOrderDetail(Integer rentType, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date lastCalculateDate, Integer paymentCycle, BigDecimal itemUnitAmount, Integer itemCount, Integer statementDays, Integer payMode, Date currentTime, Integer loginUserId) {
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
        Date statementExpectPayTime = null;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = thisPhaseStartTime;
        } else {
            // 如果按天的，本期结束就要还款，如果是按月的，本期结束第二天还款
            if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
                statementExpectPayTime = statementEndTime;
            } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
                statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, 1);
            }
        }
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, thisPhaseStartTime, statementEndTime, middlePhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId);
    }

    StatementOrderDetailDO buildStatementOrderDetailDO(Integer customerId, Integer orderType, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date statementExpectPayTime, Date startTime, Date endTime, BigDecimal statementDetailRentAmount, BigDecimal statementDetailRentDepositAmount, BigDecimal statementDetailDepositAmount, BigDecimal otherAmount, Date currentTime, Integer loginUserId) {
        StatementOrderDetailDO statementOrderDetailDO = new StatementOrderDetailDO();
        statementOrderDetailDO.setCustomerId(customerId);
        statementOrderDetailDO.setOrderType(orderType);
        statementOrderDetailDO.setOrderId(orderId);
        statementOrderDetailDO.setOrderItemType(orderItemType);
        statementOrderDetailDO.setOrderItemReferId(orderItemReferId);
        statementOrderDetailDO.setStatementExpectPayTime(statementExpectPayTime);
        statementOrderDetailDO.setStatementStartTime(startTime);
        statementOrderDetailDO.setStatementEndTime(endTime);
        statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailRentDepositAmount, statementDetailDepositAmount), statementDetailRentAmount), otherAmount));
        statementOrderDetailDO.setStatementDetailRentDepositAmount(statementDetailRentDepositAmount);
        statementOrderDetailDO.setStatementDetailDepositAmount(statementDetailDepositAmount);
        statementOrderDetailDO.setStatementDetailRentAmount(statementDetailRentAmount);
        statementOrderDetailDO.setStatementDetailOtherAmount(otherAmount);
        if (BigDecimalUtil.compare(statementDetailRentDepositAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(statementDetailDepositAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(statementDetailRentAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
            statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
        } else if (BigDecimalUtil.compare(statementDetailRentDepositAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(statementDetailDepositAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(statementDetailRentAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) < 0) {
            statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
        } else {
            return null;
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

    @Autowired
    private OrderTimeAxisSupport orderTimeAxisSupport;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private StatementPaySupport statementPaySupport;

    @Autowired
    private StatementReturnSupport statementReturnSupport;
}
