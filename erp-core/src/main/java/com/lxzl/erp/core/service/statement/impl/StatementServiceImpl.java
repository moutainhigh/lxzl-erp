package com.lxzl.erp.core.service.statement.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.callback.WeixinPayCallbackParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.domain.payment.account.pojo.PayResult;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderPayParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.StatementPayOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.coupon.impl.support.CouponSupport;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.order.impl.support.PenaltySupport;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.core.service.statement.impl.support.StatementPaySupport;
import com.lxzl.erp.core.service.statement.impl.support.StatementReturnSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ChangeOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ChangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementPayOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.domain.changeOrder.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3ChangeOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.K3ChangeOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderProductDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.*;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementPayOrderDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDetailDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.ibatis.mapping.StatementType;
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
    public ServiceResult<String, Map<String, BigDecimal>> calculateOrderFirstNeedPayAmount(OrderDO orderDO) {
        ServiceResult<String, Map<String, BigDecimal>> result = new ServiceResult<>();

        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        Date rentStartTime = orderDO.getRentStartTime();


        CustomerDO customerDO = customerMapper.findById(orderDO.getBuyerCustomerId());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return null;
        }
        Integer statementDays;
        DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
        if (dataDictionaryDO == null) {
            statementDays = StatementMode.STATEMENT_MONTH_END;
        } else {
            statementDays = Integer.parseInt(dataDictionaryDO.getDataName());
        }
        if (customerDO.getStatementDate() != null) {
            if (StatementMode.STATEMENT_MONTH_NATURAL.equals(customerDO.getStatementDate())) {
                // 如果结算日为按月结算，那么就要自然日来结算
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                rentStartTimeCalendar.add(Calendar.DAY_OF_MONTH, -1);
                statementDays = rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH);
            } else {
                statementDays = customerDO.getStatementDate();
            }
        }
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, statementDays, loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId());

        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        Map<String, BigDecimal> map = new HashMap();
        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                // 核算本次应该交多少钱
                if (DateUtil.isSameDay(rentStartTime, statementOrderDetailDO.getStatementExpectPayTime())) {
                    thisNeedPayAmount = BigDecimalUtil.add(thisNeedPayAmount, statementOrderDetailDO.getStatementDetailAmount().setScale(BigDecimalUtil.STANDARD_SCALE, BigDecimal.ROUND_HALF_UP));

                    if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                        String key = statementOrderDetailDO.getItemName() + "-" + statementOrderDetailDO.getItemIsNew() + "-" + statementOrderDetailDO.getOrderItemReferId() + "-" + statementOrderDetailDO.getOrderId();
                        map.put(key, BigDecimalUtil.add(map.get(key), statementOrderDetailDO.getStatementDetailAmount().setScale(BigDecimalUtil.STANDARD_SCALE, BigDecimal.ROUND_HALF_UP)));
                    }
                }
            }
            map.put("thisNeedPayAmount,ALL", thisNeedPayAmount);
        }

        result.setResult(map);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createOrderStatement(String orderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
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


        CustomerDO customerDO = customerMapper.findById(orderDO.getBuyerCustomerId());
        if (customerDO == null) {
            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
                customerDO = customerMapper.findByName(orderDO.getBuyerCustomerName());
                if (customerDO == null) {
                    result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                    return result;
                }
                orderDO.setBuyerCustomerId(customerDO.getId());
                orderDO.setBuyerCustomerNo(customerDO.getCustomerNo());
            } else {

                result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                return result;
            }
        }
        //统一拿订单结算日
        Integer statementDays = orderDO.getStatementDate();
        if (statementDays == null) {
            statementDays = statementOrderSupport.getCustomerStatementDate(customerDO.getStatementDate(), rentStartTime);
        }
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, statementDays, loginUser.getUserId());
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUser.getUserId());

        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                // 核算本次应该交多少钱
                if (DateUtil.isSameDay(rentStartTime, statementOrderDetailDO.getStatementExpectPayTime())) {
                    thisNeedPayAmount = BigDecimalUtil.add(thisNeedPayAmount, statementOrderDetailDO.getStatementDetailAmount().setScale(BigDecimalUtil.STANDARD_SCALE, BigDecimal.ROUND_HALF_UP));
                }
            }
        }

        result.setResult(thisNeedPayAmount);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createK3OrderStatement(Order order) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
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
        CustomerDO customerDO = customerMapper.findById(orderDO.getBuyerCustomerId());
        if (customerDO == null) {
            customerDO = customerMapper.findByName(orderDO.getBuyerCustomerName());
            if (customerDO == null) {
                result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                return result;
            }
            orderDO.setBuyerCustomerId(customerDO.getId());
            orderDO.setBuyerCustomerNo(customerDO.getCustomerNo());
        }
        //统一拿订单结算日
        Integer statementDays = orderDO.getStatementDate();
        if (statementDays == null) {
            statementDays = statementOrderSupport.getCustomerStatementDate(customerDO.getStatementDate(), rentStartTime);
        }
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, statementDays, loginUser.getUserId());
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUser.getUserId());

        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                // 核算本次应该交多少钱
                if (DateUtil.isSameDay(rentStartTime, statementOrderDetailDO.getStatementExpectPayTime())) {
                    thisNeedPayAmount = BigDecimalUtil.add(thisNeedPayAmount, statementOrderDetailDO.getStatementDetailAmount().setScale(BigDecimalUtil.STANDARD_SCALE, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
        result.setResult(thisNeedPayAmount);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> reCreateOrderStatement(String orderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        if (StringUtil.isEmpty(orderNo)) {
            result.setErrorCode(ErrorCode.ORDER_NO_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        //目前仅允许未支付和支付失败订单重新结算
        if (!(PayStatus.PAY_STATUS_INIT.equals(orderDO.getPayStatus()) || PayStatus.PAY_STATUS_FAILED.equals(orderDO.getPayStatus()))) {
            result.setErrorCode(ErrorCode.ORDER_PAY_STATUS_CAN_NOT_RESETTLE);
            return result;
        }
        //有退货单不允许重算
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByOrderNo(orderDO.getOrderNo());
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDetailDOList)) {
            result.setErrorCode(ErrorCode.HAS_RETURN_ORDER);
            return result;
        }

//        ServiceResult<String, String> clearResult = clearStatementOrderDetail(orderDO);
        ServiceResult<String, String> clearResult = clearStatementOrder(orderDO);
        if (!ErrorCode.SUCCESS.equals(clearResult.getErrorCode())) {
            result.setErrorCode(clearResult.getErrorCode());
            return result;
        }
        ServiceResult<String, BigDecimal> createResult = createOrderStatement(orderNo);
        //创建失败回滚
        if (!ErrorCode.SUCCESS.equals(createResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
        }
        return createResult;
    }

    private List<StatementOrderDetailDO> generateStatementDetailList(OrderDO orderDO, Date currentTime, Integer statementDays, Integer loginUserId) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Date rentStartTime = orderDO.getRentStartTime();
        Integer buyerCustomerId = orderDO.getBuyerCustomerId();
        Integer orderId = orderDO.getId();

        // 商品生成结算单
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {

                BigDecimal itemAllAmount = orderProductDO.getProductAmount();
                // 如果是K3订单，那么数量就要为在租数
                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
                    orderProductDO.setProductCount(orderProductDO.getRentingProductCount());
                    itemAllAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(orderProductDO.getRentingProductCount()), orderProductDO.getProductUnitAmount(), BigDecimalUtil.STANDARD_SCALE), new BigDecimal(orderProductDO.getRentTimeLength()), BigDecimalUtil.STANDARD_SCALE);
                }

                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                // 无论什么时候交租金，押金必须当天缴纳
                StatementOrderDetailDO depositDetail = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, orderProductDO.getRentDepositAmount(), orderProductDO.getDepositAmount(), BigDecimal.ZERO, currentTime, loginUserId);
                if (depositDetail != null) {
                    depositDetail.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                    depositDetail.setItemIsNew(orderProductDO.getIsNewProduct());
                    depositDetail.setStatementDetailPhase(0);
                    depositDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
                    addStatementOrderDetailDOList.add(depositDetail);
                }
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), orderProductDO.getPayMode(), rentStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId);
                    if (statementOrderDetailDO != null) {
                        statementOrderDetailDO.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                        statementOrderDetailDO.setItemIsNew(orderProductDO.getIsNewProduct());
                        statementOrderDetailDO.setStatementDetailPhase(statementMonthCount);
                        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                        //添加优惠券抵扣金额
                        ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
                        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                            statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
                        }
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    }
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;

                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), statementDays, orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                                statementOrderDetailDO.setItemIsNew(orderProductDO.getIsNewProduct());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                //添加优惠券抵扣金额
                                ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
                                if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                                    statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
                                }
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                            }
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, rentStartTime, orderProductDO.getPayMode(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                                statementOrderDetailDO.setItemIsNew(orderProductDO.getIsNewProduct());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                //添加优惠券抵扣金额
                                ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
                                if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                                    statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
                                }
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            }
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderProductDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, orderProductDO.getPaymentCycle(), orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), statementDays, orderProductDO.getPayMode(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                                statementOrderDetailDO.setItemIsNew(orderProductDO.getIsNewProduct());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                //添加优惠券抵扣金额
                                ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
                                if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                                    statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
                                }
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
                // 如果是K3订单，那么数量就要为在租数
                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
                    orderMaterialDO.setMaterialCount(orderMaterialDO.getRentingMaterialCount());
                }
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 无论什么时候交租金，押金必须当天缴纳
                StatementOrderDetailDO depositDetail = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, orderMaterialDO.getRentDepositAmount(), orderMaterialDO.getDepositAmount(), BigDecimal.ZERO, currentTime, loginUserId);
                if (depositDetail != null) {
                    depositDetail.setItemName(orderMaterialDO.getMaterialName());
                    depositDetail.setItemIsNew(orderMaterialDO.getIsNewMaterial());
                    depositDetail.setStatementDetailPhase(0);
                    depositDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
                    addStatementOrderDetailDOList.add(depositDetail);
                }
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialAmount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId);
                    if (statementOrderDetailDO != null) {
                        statementOrderDetailDO.setItemName(orderMaterialDO.getMaterialName());
                        statementOrderDetailDO.setItemIsNew(orderMaterialDO.getIsNewMaterial());
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
                                statementOrderDetailDO.setItemName(orderMaterialDO.getMaterialName());
                                statementOrderDetailDO.setItemIsNew(orderMaterialDO.getIsNewMaterial());
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
                                statementOrderDetailDO.setItemName(orderMaterialDO.getMaterialName());
                                statementOrderDetailDO.setItemIsNew(orderMaterialDO.getIsNewMaterial());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            }
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderMaterialDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), statementDays, orderMaterialDO.getPayMode(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(orderMaterialDO.getMaterialName());
                                statementOrderDetailDO.setItemIsNew(orderMaterialDO.getIsNewMaterial());
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

        // 其他费用，包括运费、等费用
        BigDecimal otherAmount = orderDO.getLogisticsAmount();

        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
            // 其他费用统一结算
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_OTHER, BigInteger.ZERO.intValue(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUserId);
            if (thisStatementOrderDetailDO != null) {
                thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OTHER);
                addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
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
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return result;
        }
        String payVerifyResult = payVerify(statementOrderDO);
        if (!ErrorCode.SUCCESS.equals(payVerifyResult)) {
            result.setErrorCode(payVerifyResult);
            return result;
        }

        CustomerDO customerDO = customerMapper.findById(statementOrderDO.getCustomerId());
        BigDecimal payRentAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDO.getStatementRentPaidAmount());
        BigDecimal payRentDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDO.getStatementRentDepositPaidAmount());
        BigDecimal payDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDO.getStatementDepositPaidAmount());
        BigDecimal payOtherAmount = BigDecimalUtil.sub(statementOrderDO.getStatementOtherAmount(), statementOrderDO.getStatementOtherPaidAmount());
        BigDecimal payOverdueAmount = BigDecimalUtil.sub(statementOrderDO.getStatementOverdueAmount(), statementOrderDO.getStatementOverduePaidAmount());
        BigDecimal totalAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(payRentAmount, payRentDepositAmount), payDepositAmount), payOtherAmount), payOverdueAmount);

        List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderId(statementOrderDO.getId());
        if (CollectionUtil.isNotEmpty(statementOrderCorrectDOList)) {
            for (StatementOrderCorrectDO statementOrderCorrectDO : statementOrderCorrectDOList) {
                payRentAmount = BigDecimalUtil.sub(payRentAmount, statementOrderCorrectDO.getStatementCorrectRentAmount());
                payRentDepositAmount = BigDecimalUtil.sub(payRentDepositAmount, statementOrderCorrectDO.getStatementCorrectRentDepositAmount());
                payDepositAmount = BigDecimalUtil.sub(payDepositAmount, statementOrderCorrectDO.getStatementCorrectDepositAmount());
                payOtherAmount = BigDecimalUtil.sub(payOtherAmount, statementOrderCorrectDO.getStatementCorrectOtherAmount());
                payOverdueAmount = BigDecimalUtil.sub(payOverdueAmount, statementOrderCorrectDO.getStatementCorrectOverdueAmount());
                totalAmount = BigDecimalUtil.sub(totalAmount, statementOrderCorrectDO.getStatementCorrectAmount());
            }
        }
        if (BigDecimalUtil.compare(totalAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payRentAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payRentDepositAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payDepositAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payOtherAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payOverdueAmount, BigDecimal.ZERO) < 0) {
            result.setErrorCode(ErrorCode.STATEMENT_PAY_AMOUNT_ERROR);
            return result;
        }
        if (BigDecimalUtil.compare(totalAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payRentAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payRentDepositAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payDepositAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payOtherAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payOverdueAmount, BigDecimal.ZERO) <= 0) {
            result.setErrorCode(ErrorCode.STATEMENT_PAY_AMOUNT_ERROR);
            return result;
        }

        // 冲正后的结算单金额，必须要与现有的结算单金额相同
        if (BigDecimalUtil.compare(totalAmount, statementOrderDO.getStatementAmount()) != 0) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_AMOUNT_NOT_EQUAL_CORRECT_MOUNT);
            return result;
        }

        boolean haveOldRecord = false;
        StatementPayOrderDO statementPayOrderLastRecord = statementPaySupport.getLastRecord(statementOrderDO.getId());
        if (statementPayOrderLastRecord != null && PayStatus.PAY_STATUS_PAYING.equals(statementPayOrderLastRecord.getPayStatus())) {
            // 如果本次支付和上一次支付价格不同
            if ((statementPayOrderLastRecord.getCreateTime().getTime() + (90 * 60 * 1000)) <= currentTime.getTime()) {
                // 查询时间过去多久了，如果超过90分钟，即为失效，重新下单
//                boolean updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderLastRecord.getId(), PayStatus.PAY_STATUS_TIME_OUT, null, loginUserId, currentTime);
//                if (!updatePayOrderResult) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    result.setErrorCode(ErrorCode.STATEMENT_PAY_FAILED);
//                    return result;
//                }

                ServiceResult<String, Boolean> updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderLastRecord.getId(), PayStatus.PAY_STATUS_TIME_OUT, null, loginUser.getUserId(), currentTime);
                if (!ErrorCode.SUCCESS.equals(updatePayOrderResult.getErrorCode()) && !updatePayOrderResult.getResult()) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    result.setErrorCode(updatePayOrderResult.getErrorCode());
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
            statementPayOrderDO = statementPaySupport.saveStatementPayOrder(statementOrderDO.getId(), totalAmount, payRentAmount, payRentDepositAmount, payDepositAmount, payOtherAmount, payOverdueAmount, StatementOrderPayType.PAY_TYPE_WEIXIN, loginUserId, currentTime);
        }

        if (statementPayOrderDO == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_AMOUNT_MAST_MORE_THEN_ZERO);
            return result;
        }

        logger.info("wechat pay customer: {} , orderNo: {} , payRentAmount: {} , payRentDepositAmount: {} , payDepositAmount: {} , otherAmount: {} , totalAmount: {} , ",
                customerDO.getCustomerNo(), statementPayOrderDO.getStatementPayOrderNo(), payRentAmount, payDepositAmount, payOtherAmount, totalAmount);
        ServiceResult<String, String> wechatPayResult = paymentService.wechatPay(customerDO.getCustomerNo(), statementPayOrderDO.getStatementPayOrderNo(), statementOrderDO.getRemark(), totalAmount, openId, ip, loginUserId);
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

//        boolean updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderDO.getId(), WeixinPayCallbackParam.NOTIFY_STATUS_SUCCESS.equals(param.getNotifyStatus()) ? PayStatus.PAY_STATUS_PAID : PayStatus.PAY_STATUS_FAILED, param.getOrderNo(), loginUserId, currentTime);
//        if (!updatePayOrderResult) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            result.setErrorCode(ErrorCode.STATEMENT_PAY_FAILED);
//            return result;
//        }

        ServiceResult<String, Boolean> updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderDO.getId(), WeixinPayCallbackParam.NOTIFY_STATUS_SUCCESS.equals(param.getNotifyStatus()) ? PayStatus.PAY_STATUS_PAID : PayStatus.PAY_STATUS_FAILED, param.getOrderNo(), loginUserId, currentTime);
        if (!ErrorCode.SUCCESS.equals(updatePayOrderResult.getErrorCode()) && !updatePayOrderResult.getResult()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(updatePayOrderResult.getErrorCode());
            return result;
        }

        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementPayOrderDO.getStatementOrderId());
        if (statementOrderDO == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return result;
        }

        if (WeixinPayCallbackParam.NOTIFY_STATUS_SUCCESS.equals(param.getNotifyStatus())) {
            BigDecimal payRentAmount = statementPayOrderDO.getPayRentAmount();
            BigDecimal payRentDepositAmount = statementPayOrderDO.getPayRentDepositAmount();
            BigDecimal payDepositAmount = statementPayOrderDO.getPayDepositAmount();
            BigDecimal otherAmount = statementPayOrderDO.getOtherAmount();
            BigDecimal overdueAmount = statementPayOrderDO.getOverdueAmount();
            updateStatementOrderResult(statementOrderDO, payRentAmount, payRentDepositAmount, payDepositAmount, otherAmount, overdueAmount, currentTime, loginUserId);
        }

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
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return result;
        }

        String payVerifyResult = payVerify(statementOrderDO);
        if (!ErrorCode.SUCCESS.equals(payVerifyResult)) {
            result.setErrorCode(payVerifyResult);
            return result;
        }

        CustomerDO customerDO = customerMapper.findById(statementOrderDO.getCustomerId());
        BigDecimal payRentAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDO.getStatementRentPaidAmount());
        BigDecimal payRentDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDO.getStatementRentDepositPaidAmount());
        BigDecimal payDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDO.getStatementDepositPaidAmount());
        BigDecimal payOtherAmount = BigDecimalUtil.sub(statementOrderDO.getStatementOtherAmount(), statementOrderDO.getStatementOtherPaidAmount());
        BigDecimal payOverdueAmount = BigDecimalUtil.sub(statementOrderDO.getStatementOverdueAmount(), statementOrderDO.getStatementOverduePaidAmount());
        BigDecimal totalAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(payRentAmount, payRentDepositAmount), payDepositAmount), payOtherAmount), payOverdueAmount);

        List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderId(statementOrderDO.getId());
        if (CollectionUtil.isNotEmpty(statementOrderCorrectDOList)) {
            for (StatementOrderCorrectDO statementOrderCorrectDO : statementOrderCorrectDOList) {
                payRentAmount = BigDecimalUtil.sub(payRentAmount, statementOrderCorrectDO.getStatementCorrectRentAmount());
                payRentDepositAmount = BigDecimalUtil.sub(payRentDepositAmount, statementOrderCorrectDO.getStatementCorrectRentDepositAmount());
                payDepositAmount = BigDecimalUtil.sub(payDepositAmount, statementOrderCorrectDO.getStatementCorrectDepositAmount());
                payOtherAmount = BigDecimalUtil.sub(payOtherAmount, statementOrderCorrectDO.getStatementCorrectOtherAmount());
                payOverdueAmount = BigDecimalUtil.sub(payOverdueAmount, statementOrderCorrectDO.getStatementCorrectOverdueAmount());
                totalAmount = BigDecimalUtil.sub(totalAmount, statementOrderCorrectDO.getStatementCorrectAmount());
            }
        }
        if (BigDecimalUtil.compare(totalAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payRentAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payRentDepositAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payDepositAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payOtherAmount, BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(payOverdueAmount, BigDecimal.ZERO) < 0) {
            result.setErrorCode(ErrorCode.STATEMENT_PAY_AMOUNT_ERROR);
            return result;
        }

        if (BigDecimalUtil.compare(totalAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payRentAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payRentDepositAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payDepositAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payOtherAmount, BigDecimal.ZERO) <= 0
                && BigDecimalUtil.compare(payOverdueAmount, BigDecimal.ZERO) <= 0) {
            result.setErrorCode(ErrorCode.STATEMENT_PAY_AMOUNT_ERROR);
            return result;
        }

        // 冲正后的结算单金额，必须要与现有的结算单金额相同
        if (BigDecimalUtil.compare(totalAmount, BigDecimalUtil.sub(statementOrderDO.getStatementAmount(), statementOrderDO.getStatementPaidAmount())) != 0) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_AMOUNT_NOT_EQUAL_CORRECT_MOUNT);
            return result;
        }

        StatementPayOrderDO statementPayOrderDO = statementPaySupport.saveStatementPayOrder(statementOrderDO.getId(), totalAmount, payRentAmount, payRentDepositAmount, payDepositAmount, payOtherAmount, payOverdueAmount, StatementOrderPayType.PAY_TYPE_BALANCE, loginUser.getUserId(), currentTime);
        if (statementPayOrderDO == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_AMOUNT_MAST_MORE_THEN_ZERO);
            return result;
        }

        ServiceResult<String, Boolean> paymentResult = paymentService.balancePay(customerDO.getCustomerNo(), statementPayOrderDO.getStatementPayOrderNo(), statementOrderDO.getRemark(), BigDecimalUtil.add(payRentAmount, payOverdueAmount), payRentDepositAmount, payDepositAmount, payOtherAmount);
        if (!ErrorCode.SUCCESS.equals(paymentResult.getErrorCode()) || !paymentResult.getResult()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(paymentResult.getErrorCode());
            return result;
        }

//        boolean updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderDO.getId(), PayStatus.PAY_STATUS_PAID, null, loginUser.getUserId(), currentTime);
//        if (!updatePayOrderResult) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            result.setErrorCode(ErrorCode.STATEMENT_PAY_FAILED);
//            return result;
//        }

        ServiceResult<String, Boolean> updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderDO.getId(), PayStatus.PAY_STATUS_PAID, null, loginUser.getUserId(), currentTime);
        if (!ErrorCode.SUCCESS.equals(updatePayOrderResult.getErrorCode()) && !updatePayOrderResult.getResult()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(updatePayOrderResult.getErrorCode());
            return result;
        }

        updateStatementOrderResult(statementOrderDO, payRentAmount, payRentDepositAmount, payDepositAmount, payOtherAmount, payOverdueAmount, currentTime, loginUser.getUserId());
        result.setResult(true);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, List<String>> batchPayStatementOrder(List<StatementOrderPayParam> param) {
        ServiceResult<String, List<String>> result = new ServiceResult<>();

        Integer customerId = null;
        List<StatementOrderDO> newStatementOrderDO = new ArrayList<>();
        for (int i = 0; i < param.size(); i++) {
            StatementOrderDO statementOrderDO = statementOrderMapper.findByNo(param.get(i).getStatementOrderNo());
            if (statementOrderDO == null) {
                result.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
                return result;
            }
            if (!StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementOrderDO.getStatementStatus()) && !StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART.equals(statementOrderDO.getStatementStatus())) {
                result.setErrorCode(ErrorCode.STATEMENT_ORDER_STATUS_IS_ERROR);
                return result;
            }
            if (customerId == null) {
                customerId = statementOrderDO.getCustomerId();
            }
            if (!customerId.equals(statementOrderDO.getCustomerId())) {
                result.setErrorCode(ErrorCode.STATEMENT_BATCH_PAY_IS_CUSTOMER_IS_DIFFERENCE);
                return result;
            }
            newStatementOrderDO.add(statementOrderDO);
        }

        Collections.sort(newStatementOrderDO, new Comparator<StatementOrderDO>() {
            @Override
            public int compare(StatementOrderDO o1, StatementOrderDO o2) {
                Date dt1 = o1.getStatementExpectPayTime();
                Date dt2 = o2.getStatementExpectPayTime();
                if (dt1.getTime() > dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        List<String> data = new ArrayList<>();
        for (int i = 0; i < newStatementOrderDO.size(); i++) {
            ServiceResult<String, Boolean> pay = payStatementOrder(newStatementOrderDO.get(i).getStatementOrderNo());
            if (ErrorCode.SUCCESS.equals(pay.getErrorCode())) {
                data.add(i, newStatementOrderDO.get(i).getStatementOrderNo() + "：支付" + ErrorCode.getMessage(ErrorCode.SUCCESS));
            } else {
                data.add(i, newStatementOrderDO.get(i).getStatementOrderNo() + "：支付失败:" + ErrorCode.getMessage(pay.getErrorCode()));
                break;
            }
        }

        result.setResult(data);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    void updateStatementOrderResult(StatementOrderDO statementOrderDO, BigDecimal rentAmount, BigDecimal rentDepositAmount, BigDecimal depositAmount, BigDecimal otherAmount, BigDecimal overdueAmount, Date currentTime, Integer loginUserId) {

        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        statementOrderDO.setStatementOtherPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementOtherPaidAmount(), otherAmount));
        statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentPaidAmount(), rentAmount));
        statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositPaidAmount(), rentDepositAmount));
        statementOrderDO.setStatementDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositPaidAmount(), depositAmount));
        statementOrderDO.setStatementOverduePaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementOverduePaidAmount(), overdueAmount));
        statementOrderDO.setStatementPaidTime(currentTime);
        statementOrderDO.setUpdateTime(currentTime);
        statementOrderDO.setUpdateUser(loginUserId.toString());
        statementOrderMapper.update(statementOrderDO);
        Map<Integer, BigDecimal> orderPaidMap = new HashMap<>();

        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);

                // 查询有没有冲正业务金额
                List<StatementOrderCorrectDetailDO> statementOrderCorrectDetailDOList = statementOrderCorrectDetailMapper.findByStatementDetailIdAndType(statementOrderDetailDO.getId(), statementOrderDetailDO.getStatementDetailType());
                BigDecimal correctBusinessAmount = BigDecimal.ZERO;
                if (CollectionUtil.isNotEmpty(statementOrderCorrectDetailDOList)) {
                    for (StatementOrderCorrectDetailDO statementOrderCorrectDetailDO : statementOrderCorrectDetailDOList) {
                        if (statementOrderCorrectDetailDO != null && BigDecimalUtil.compare(statementOrderCorrectDetailDO.getStatementCorrectAmount(), BigDecimal.ZERO) > 0) {
                            correctBusinessAmount = statementOrderCorrectDetailDO.getStatementCorrectAmount();
                        }
                    }
                }

                BigDecimal noPaidStatementDetailRentPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(), statementOrderDetailDO.getStatementDetailRentPaidAmount());
                BigDecimal needStatementDetailRentPayAmount = BigDecimal.ZERO;
                if (BigDecimalUtil.compare(noPaidStatementDetailRentPayAmount, BigDecimal.ZERO) > 0) {
                    needStatementDetailRentPayAmount = BigDecimalUtil.sub(noPaidStatementDetailRentPayAmount, correctBusinessAmount);
                    statementOrderDetailDO.setStatementDetailRentPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), needStatementDetailRentPayAmount));
                }

                BigDecimal noPaidStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                if (BigDecimalUtil.compare(noPaidStatementDetailRentDepositPayAmount, BigDecimal.ZERO) > 0) {
                    BigDecimal needStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(noPaidStatementDetailRentDepositPayAmount, correctBusinessAmount);
                    statementOrderDetailDO.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), needStatementDetailRentDepositPayAmount));
                }

                BigDecimal noPaidStatementDetailDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositAmount(), statementOrderDetailDO.getStatementDetailDepositPaidAmount());
                if (BigDecimalUtil.compare(noPaidStatementDetailDepositPayAmount, BigDecimal.ZERO) > 0) {
                    BigDecimal needStatementDetailDepositPayAmount = BigDecimalUtil.sub(noPaidStatementDetailDepositPayAmount, correctBusinessAmount);
                    statementOrderDetailDO.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), needStatementDetailDepositPayAmount));
                }

                BigDecimal noPaidStatementDetailOtherPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOtherAmount(), statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                if (BigDecimalUtil.compare(noPaidStatementDetailOtherPayAmount, BigDecimal.ZERO) > 0) {
                    BigDecimal needStatementDetailOtherPayAmount = BigDecimalUtil.sub(noPaidStatementDetailOtherPayAmount, correctBusinessAmount);
                    statementOrderDetailDO.setStatementDetailOtherPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), needStatementDetailOtherPayAmount));
                }

                BigDecimal noPaidStatementDetailOverduePayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOverdueAmount(), statementOrderDetailDO.getStatementDetailOverduePaidAmount());
                if (BigDecimalUtil.compare(noPaidStatementDetailOverduePayAmount, BigDecimal.ZERO) > 0) {
                    BigDecimal needStatementDetailOverduePayAmount;

                    // 查询有没有冲正逾期金额
                    List<StatementOrderCorrectDetailDO> statementOrderCorrectOverdueDetailDOList = statementOrderCorrectDetailMapper.findByStatementDetailIdAndType(statementOrderDetailDO.getId(), StatementDetailType.STATEMENT_DETAIL_TYPE_OVERDUE);
                    BigDecimal correctOverdueAmount = BigDecimal.ZERO;
                    if (CollectionUtil.isNotEmpty(statementOrderCorrectOverdueDetailDOList)) {
                        for (StatementOrderCorrectDetailDO statementOrderCorrectDetailDO : statementOrderCorrectOverdueDetailDOList) {
                            if (statementOrderCorrectDetailDO != null && BigDecimalUtil.compare(statementOrderCorrectDetailDO.getStatementCorrectAmount(), BigDecimal.ZERO) > 0) {
                                correctOverdueAmount = statementOrderCorrectDetailDO.getStatementCorrectAmount();
                            }
                        }
                    }
                    if (BigDecimalUtil.compare(noPaidStatementDetailOverduePayAmount, correctOverdueAmount) >= 0) {
                        needStatementDetailOverduePayAmount = BigDecimalUtil.sub(noPaidStatementDetailOverduePayAmount, correctOverdueAmount);
                    } else {
                        needStatementDetailOverduePayAmount = BigDecimal.ZERO;
                    }
                    statementOrderDetailDO.setStatementDetailOverduePaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOverduePaidAmount(), needStatementDetailOverduePayAmount));
                }

                statementOrderDetailDO.setStatementDetailPaidTime(currentTime);
                statementOrderDetailDO.setUpdateTime(currentTime);
                statementOrderDetailDO.setUpdateUser(loginUserId.toString());
                statementOrderDetailMapper.update(statementOrderDetailDO);

                // 已支付的租金
                orderPaidMap.put(statementOrderDetailDO.getOrderId(), BigDecimalUtil.add(orderPaidMap.get(statementOrderDetailDO.getOrderId()), needStatementDetailRentPayAmount));
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : orderPaidMap.entrySet()) {
            Integer orderId = entry.getKey();
            BigDecimal paidAmount = entry.getValue();
            OrderDO orderDO = orderMapper.findByOrderId(orderId);
            if (orderDO == null) {
                continue;
            }
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
        if (statementOrderQueryParam.getOrderNo()!= null) {
            OrderDO byOrderNo = orderMapper.findByOrderNo(statementOrderQueryParam.getOrderNo());
            if (byOrderNo == null) {
                Page<StatementOrder> page = new Page<>(new ArrayList<StatementOrder>(), 0, statementOrderQueryParam.getPageNo(), statementOrderQueryParam.getPageSize());
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(page);
                return result;
            }
            List<StatementOrderDetailDO> byOrderId = statementOrderDetailMapper.findByOrderTypeAndId(OrderType.ORDER_TYPE_ORDER,byOrderNo.getId());
            if (CollectionUtil.isEmpty(byOrderId)) {
                Page<StatementOrder> page = new Page<>(new ArrayList<StatementOrder>(), 0, statementOrderQueryParam.getPageNo(), statementOrderQueryParam.getPageSize());
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(page);
                return result;
            }
            maps.put("statementOrderId", byOrderId.get(0).getStatementOrderId());
        }


        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("statementOrderQueryParam", statementOrderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = statementOrderMapper.listSaleCount(maps);
        List<StatementOrderDO> statementOrderDOList = statementOrderMapper.listSalePage(maps);
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
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return result;
        }
        StatementOrder statementOrder = ConverterUtil.convert(statementOrderDO, StatementOrder.class);

        StatementOrderDetail returnReferStatementOrderDetail = null;
        //获取各项的总和,区分了各商品
        Map<String, StatementOrderDetail> hashMap = new HashMap<>();

        if (statementOrder != null && CollectionUtil.isNotEmpty(statementOrder.getStatementOrderDetailList())) {
            Map<Integer, StatementOrderDetail> statementOrderDetailMap = ListUtil.listToMap(statementOrder.getStatementOrderDetailList(), "statementOrderDetailId");

            for (StatementOrderDetail statementOrderDetail : statementOrder.getStatementOrderDetailList()) {
                if (statementOrderDetail.getReturnReferId() != null) {
                    returnReferStatementOrderDetail = statementOrderDetailMap.get(statementOrderDetail.getReturnReferId());
                }
                convertStatementOrderDetailOtherInfo(statementOrderDetail, returnReferStatementOrderDetail, null);
                String key = null;
                if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
                    //为订单商品时
                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                        OrderProductDO orderProductDO = orderProductMapper.findById(statementOrderDetail.getOrderItemReferId());
                        if (orderProductDO != null) {
                            Integer productId = orderProductDO.getProductId();
                            Integer isNewProduct = orderProductDO.getIsNewProduct();
                            key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + productId + "-" + isNewProduct + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                        }
                    }
                    //为订单物料时
                    if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                        OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(statementOrderDetail.getOrderItemReferId());
                        if (orderMaterialDO != null) {
                            Integer materialId = orderMaterialDO.getMaterialId();
                            Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                            key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + materialId + "-" + isNewMaterial + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                        }
                    }
                    //为订单其他时
                    if (OrderItemType.ORDER_ITEM_TYPE_OTHER.equals(statementOrderDetail.getOrderItemType())) {
                        key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                        hashMap.put(key, statementOrderDetail);
                        continue;
                    }
                }
                if (OrderType.ORDER_TYPE_RETURN.equals(statementOrderDetail.getOrderType())) {
                    //为退还商品时
                    K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findById(statementOrderDetail.getOrderId());
                    K3ReturnOrderDetailDO k3ReturnOrderDetailDO = k3ReturnOrderDetailMapper.findById(statementOrderDetail.getOrderItemReferId());
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                        if (k3ReturnOrderDetailDO != null) {
                            String orderItemId = k3ReturnOrderDetailDO.getOrderItemId();
                            OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(orderItemId));
                            if (orderProductDO != null) {
                                Integer productId = orderProductDO.getProductId();
                                Integer isNewProduct = orderProductDO.getIsNewProduct();
                                key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + productId + "-" + isNewProduct + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                            }
                        }
                    }
                    //为退还物料时
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                        if (k3ReturnOrderDetailDO != null) {
                            String orderItemId = k3ReturnOrderDetailDO.getOrderItemId();
                            OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(orderItemId));
                            if (orderMaterialDO != null) {
                                Integer materialId = orderMaterialDO.getMaterialId();
                                Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                                key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + materialId + "-" + isNewMaterial + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                            }
                        }
                    }
                    //为退还物料时
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER.equals(statementOrderDetail.getOrderItemType())) {
                        if (k3ReturnOrderDO != null) {
                            statementOrderDetail.setOrderNo(k3ReturnOrderDO.getReturnOrderNo());
                        }
                        key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                    }
                }
                if (OrderType.ORDER_TYPE_CHANGE.equals(statementOrderDetail.getOrderType())) {
                    if (OrderType.ORDER_TYPE_CHANGE.equals(statementOrderDetail.getOrderType())) {
                        //为换商品时
                        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findById(statementOrderDetail.getOrderId());
                        K3ChangeOrderDetailDO k3ChangeOrderDetailDO = k3ChangeOrderDetailMapper.findById(statementOrderDetail.getOrderItemReferId());
                        if (OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                            if (k3ChangeOrderDetailDO != null) {
                                String orderItemId = k3ChangeOrderDetailDO.getOrderItemId();
                                OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(orderItemId));
                                statementOrderDetail.setOrderItemType(OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT);
                                statementOrderDetail.setItemName(k3ChangeOrderDetailDO.getProductName());
                                statementOrderDetail.setItemCount(k3ChangeOrderDetailDO.getProductCount());
                                statementOrderDetail.setItemRentType(k3ChangeOrderDetailDO.getRentType());
                                if (orderProductDO != null) {
                                    Integer productId = orderProductDO.getProductId();
                                    Integer isNewProduct = orderProductDO.getIsNewProduct();
                                    key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + productId + "-" + isNewProduct + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                                }
                            }
                        }
                        //为换物料时
                        if (OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                            if (k3ChangeOrderDetailDO != null) {
                                String orderItemId = k3ChangeOrderDetailDO.getOrderItemId();
                                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(orderItemId));
                                statementOrderDetail.setOrderItemType(OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL);
                                statementOrderDetail.setItemName(k3ChangeOrderDetailDO.getProductName());
                                statementOrderDetail.setItemCount(k3ChangeOrderDetailDO.getProductCount());
                                statementOrderDetail.setItemRentType(k3ChangeOrderDetailDO.getRentType());
                                if (orderMaterialDO != null) {
                                    Integer materialId = orderMaterialDO.getMaterialId();
                                    Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                                    key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + materialId + "-" + isNewMaterial + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                                }
                            }
                        }
                        //为换货其他费用时
                        if (OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER.equals(statementOrderDetail.getOrderItemType())) {
                            if (k3ChangeOrderDO != null) {
                                statementOrderDetail.setOrderNo(k3ChangeOrderDO.getChangeOrderNo());
                            }
                            key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                        }
                    }
                }
                //各商品物料
                StatementOrderDetail newStatementOrderDetail = hashMap.get(key);
                if (newStatementOrderDetail != null) {
                    newStatementOrderDetail.setStatementDetailRentAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentAmount(), statementOrderDetail.getStatementDetailRentAmount()));
                    newStatementOrderDetail.setStatementDetailRentPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentPaidAmount(), statementOrderDetail.getStatementDetailRentPaidAmount()));
                    newStatementOrderDetail.setStatementDetailRentDepositAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentDepositAmount(), statementOrderDetail.getStatementDetailRentDepositAmount()));
                    newStatementOrderDetail.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentDepositPaidAmount(), statementOrderDetail.getStatementDetailRentDepositPaidAmount()));
                    newStatementOrderDetail.setStatementDetailDepositAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailDepositAmount(), statementOrderDetail.getStatementDetailDepositAmount()));
                    newStatementOrderDetail.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailDepositPaidAmount(), statementOrderDetail.getStatementDetailDepositPaidAmount()));
                    newStatementOrderDetail.setStatementDetailOverdueAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailOverdueAmount(), statementOrderDetail.getStatementDetailOverdueAmount()));
                    newStatementOrderDetail.setStatementDetailOverduePaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailOverduePaidAmount(), statementOrderDetail.getStatementDetailOverduePaidAmount()));
                    newStatementOrderDetail.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentDepositReturnAmount(), statementOrderDetail.getStatementDetailRentDepositReturnAmount()));
                    newStatementOrderDetail.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailDepositReturnAmount(), statementOrderDetail.getStatementDetailDepositReturnAmount()));
                    newStatementOrderDetail.setStatementDetailCorrectAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailCorrectAmount(), statementOrderDetail.getStatementDetailCorrectAmount()));
                    newStatementOrderDetail.setStatementDetailOtherAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailOtherAmount(), statementOrderDetail.getStatementDetailOtherAmount()));
                    newStatementOrderDetail.setStatementDetailOtherPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailOtherPaidAmount(), statementOrderDetail.getStatementDetailOtherPaidAmount()));
                    newStatementOrderDetail.setStatementDetailAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailAmount(), statementOrderDetail.getStatementDetailAmount()));
                    newStatementOrderDetail.setStatementDetailPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailPaidAmount(), statementOrderDetail.getStatementDetailPaidAmount()));

                    // 开始计算结算时间
                    if (newStatementOrderDetail.getStatementStartTime() != null && statementOrderDetail.getStatementStartTime() != null) {
                        if (newStatementOrderDetail.getStatementStartTime().getTime() > statementOrderDetail.getStatementStartTime().getTime()) {
                            newStatementOrderDetail.setStatementStartTime(statementOrderDetail.getStatementStartTime());
                        }
                    }
                    if (newStatementOrderDetail.getStatementEndTime() != null && statementOrderDetail.getStatementEndTime() != null) {
                        if (newStatementOrderDetail.getStatementEndTime().getTime() < statementOrderDetail.getStatementEndTime().getTime()) {
                            newStatementOrderDetail.setStatementEndTime(statementOrderDetail.getStatementEndTime());
                        }
                    }
                } else {
                    //各项总金额
                    hashMap.put(key, statementOrderDetail);
                }
            }
        }

        for (StatementOrderDetail statementOrderDetail : hashMap.values()) {
            BigDecimal statementDetailPaidAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementOrderDetail.getStatementDetailDepositPaidAmount(), statementOrderDetail.getStatementDetailOverduePaidAmount()), statementOrderDetail.getStatementDetailRentDepositPaidAmount()), statementOrderDetail.getStatementDetailRentPaidAmount()), statementOrderDetail.getStatementDetailOtherPaidAmount());
            statementOrderDetail.setStatementDetailPaidAmount(statementDetailPaidAmount);
        }

        List<StatementOrderDetail> statementOrderDetailList = ListUtil.mapToList(hashMap);
        statementOrder.setStatementOrderDetailList(statementOrderDetailList);

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
            StatementOrderDetail returnReferStatementOrderDetail = null;
            Map<Integer, StatementOrderDetail> statementOrderDetailMap = ListUtil.listToMap(statementOrder.getStatementOrderDetailList(), "statementOrderDetailId");
            for (StatementOrderDetail statementOrderDetail : statementOrderDetailList) {
                if (customerId == null) {
                    customerId = statementOrderDetail.getCustomerId();
                }
                if (statementOrderDetail.getReturnReferId() != null) {
                    returnReferStatementOrderDetail = statementOrderDetailMap.get(statementOrderDetail.getReturnReferId());
                }
                convertStatementOrderDetailOtherInfo(statementOrderDetail, returnReferStatementOrderDetail, orderDO);

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

    private void convertStatementOrderDetailOtherInfo(StatementOrderDetail statementOrderDetail, StatementOrderDetail returnReferStatementOrderDetail, OrderDO orderDO) {
        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
            orderDO = orderDO == null ? orderMapper.findByOrderId(statementOrderDetail.getOrderId()) : orderDO;
            if (orderDO != null) {
                statementOrderDetail.setOrderNo(orderDO.getOrderNo());
                if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
                    for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                        if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(orderProductDO.getId())) {
                            statementOrderDetail.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                            statementOrderDetail.setItemCount(orderProductDO.getProductCount());
                            statementOrderDetail.setUnitAmount(orderProductDO.getProductUnitAmount());
                            statementOrderDetail.setItemRentType(orderProductDO.getRentType());
                            break;
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
                            break;
                        }
                    }
                }
            }
        }

        if (OrderType.ORDER_TYPE_RETURN.equals(statementOrderDetail.getOrderType())) {
            if (returnReferStatementOrderDetail != null) {
                orderDO = orderMapper.findByOrderId(returnReferStatementOrderDetail.getOrderId());
            }
            ReturnOrderDO returnOrderDO = returnOrderMapper.findById(statementOrderDetail.getOrderId());
            if (returnOrderDO != null) {
                statementOrderDetail.setOrderNo(returnOrderDO.getReturnOrderNo());
                if (CollectionUtil.isNotEmpty(returnOrderDO.getReturnOrderProductDOList())) {
                    for (ReturnOrderProductDO returnOrderProductDO : returnOrderDO.getReturnOrderProductDOList()) {
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(returnOrderProductDO.getId())) {
                            Product product = FastJsonUtil.toBean(returnOrderProductDO.getReturnProductSkuSnapshot(), Product.class);
                            if (CollectionUtil.isNotEmpty(product.getProductSkuList())) {
                                statementOrderDetail.setItemName(product.getProductName() + product.getProductSkuList().get(0).getSkuName());
                            }
                            statementOrderDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                            statementOrderDetail.setItemCount(returnOrderProductDO.getRealReturnProductSkuCount());
                            if (orderDO != null && CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
                                for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(returnReferStatementOrderDetail.getOrderItemType()) && returnReferStatementOrderDetail.getOrderItemReferId().equals(orderProductDO.getId())) {
                                        statementOrderDetail.setUnitAmount(orderProductDO.getProductUnitAmount());
                                        statementOrderDetail.setItemRentType(orderProductDO.getRentType());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (CollectionUtil.isNotEmpty(returnOrderDO.getReturnOrderMaterialDOList())) {
                    for (ReturnOrderMaterialDO returnOrderMaterialDO : returnOrderDO.getReturnOrderMaterialDOList()) {
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(returnOrderMaterialDO.getId())) {
                            Material material = FastJsonUtil.toBean(returnOrderMaterialDO.getReturnMaterialSnapshot(), Material.class);
                            statementOrderDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                            statementOrderDetail.setItemName(material.getMaterialName());
                            statementOrderDetail.setItemCount(returnOrderMaterialDO.getRealReturnMaterialCount());
                            if (orderDO != null && CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
                                for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                                    if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(returnReferStatementOrderDetail.getOrderItemType()) && returnReferStatementOrderDetail.getOrderItemReferId().equals(orderMaterialDO.getId())) {
                                        statementOrderDetail.setUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                                        statementOrderDetail.setItemRentType(orderMaterialDO.getRentType());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (OrderType.ORDER_TYPE_CHANGE.equals(statementOrderDetail.getOrderType())) {
            ChangeOrderDO changeOrderDO = changeOrderMapper.findById(statementOrderDetail.getOrderId());
            if (changeOrderDO != null) {
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
                        if (OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(changeOrderMaterialDO.getId())) {
                            Material material = FastJsonUtil.toBean(changeOrderMaterialDO.getSrcChangeMaterialSnapshot(), Material.class);
                            statementOrderDetail.setItemName(material.getMaterialName());
                            statementOrderDetail.setItemCount(changeOrderMaterialDO.getRealChangeMaterialCount());
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createK3ReturnOrderStatement(String returnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_IS_NOT_NULL);
            return result;
        }

        List<StatementOrderDetailDO> statementOrderDetails = statementOrderDetailMapper.findByOrderTypeAndId(OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId());
        if (CollectionUtil.isNotEmpty(statementOrderDetails)) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_CREATE_ERROR);
            return result;
        }
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        Map<String, StatementOrderDetailDO> addStatementOrderDetailDOMap = new HashMap<>();
        String buyerCustomerNo = k3ReturnOrderDO.getK3CustomerNo();
        CustomerDO customerDO = customerMapper.findByNo(buyerCustomerNo);
        Integer buyerCustomerId = customerDO.getId();
        Date statementDetailStartTime;
        Date statementDetailEndTime;
        // 其他的费用 包括服务费等费用
        BigDecimal otherAmount = BigDecimalUtil.add(k3ReturnOrderDO.getLogisticsAmount(), k3ReturnOrderDO.getServiceAmount());

        // 租金押金和设备押金
        BigDecimal totalReturnRentDepositAmount = BigDecimal.ZERO, totalReturnDepositAmount = BigDecimal.ZERO;
        Date returnTime = k3ReturnOrderDO.getReturnTime();
        Date otherStatementTime = returnTime;

        // TODO 完成原逻辑退货

        if (CollectionUtil.isNotEmpty(k3ReturnOrderDO.getK3ReturnOrderDetailDOList())) {
            for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDO.getK3ReturnOrderDetailDOList()) {

                if ((!k3ReturnOrderDetailDO.getProductNo().startsWith("10.") && !k3ReturnOrderDetailDO.getProductNo().startsWith("90.")) || k3ReturnOrderDetailDO.getOrderItemId() == null) {
                    continue;
                }

                OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId()));
                if (orderProductDO == null) {
                    continue;
                }
                BigDecimal returnCount = new BigDecimal(k3ReturnOrderDetailDO.getProductCount());
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
                BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderProductDO.getRentDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE), returnCount);
                BigDecimal thisReturnDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderProductDO.getDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE), returnCount);
                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                            // 第一期如果支付了押金，就要退押金，否则不退了
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                                //(针对违约长租设备)非即租即还设备，押金不退  //todo 后续增加续租商品 默认为即租即还
                                OrderDO orderDO = orderMapper.findById(statementOrderDetailDO.getOrderId());
                                if (orderDO == null) {
                                    result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                                    return result;
                                }
                                if (orderDO.getExpectReturnTime().compareTo(k3ReturnOrderDO.getReturnTime()) > 0) {
                                    if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                                        ProductDO product = productMapper.findById(orderProductDO.getProductId());
                                        if (product == null) {
                                            result.setErrorCode(ErrorCode.PRODUCT_NOT_EXISTS);
                                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                                            return result;
                                        }
                                        if (product.getIsReturnAnyTime() != IsReturnAnyTime.RETURN_ANY_TIME_YES)
                                            continue;
                                    }
                                }

                                StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
                                if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementOrderDetailDO.getStatementDetailDepositReturnAmount()), thisReturnDepositAmount) >= 0) {
                                    totalReturnDepositAmount = BigDecimalUtil.add(totalReturnDepositAmount, thisReturnDepositAmount);
                                    statementOrderDetailDO.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositReturnAmount(), thisReturnDepositAmount));
                                    statementOrderDetailDO.setStatementDetailDepositReturnTime(currentTime);
                                    statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositReturnAmount(), thisReturnDepositAmount));
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnDepositAmount, returnTime, loginUser.getUserId(), currentTime);
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
                            continue;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                        // 如果退货时间在结束时间之后，证明本期都没缴，不用处理，等待交
                        if (returnTime.getTime() > statementDetailEndTime.getTime()) {
                            continue;
                        }
                        BigDecimal itemAllAmount = orderProductDO.getProductAmount();
                        BigDecimal needPayAmount = BigDecimal.ZERO;
                        BigDecimal productUnitAmount = BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), returnCount);
                        if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
                            if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, returnTime, orderProductDO.getProductUnitAmount(), k3ReturnOrderDetailDO.getProductCount());
                                }
                            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderProductDO.getPayMode())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    BigDecimal firstAmount = BigDecimalUtil.mul(returnCount, BigDecimalUtil.div(BigDecimalUtil.mul(itemAllAmount, new BigDecimal(0.3)), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE));
                                    // 30%期
                                    if (statementOrderDetailDO.getStatementDetailPhase() == 1) {
                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
                                        needPayAmount = BigDecimalUtil.compare(needPayAmount, firstAmount) > 0 ? firstAmount : needPayAmount;
                                    } else if (statementOrderDetailDO.getStatementDetailPhase() == 2) {
                                        // 70% 期
                                        // 加2的原因是算上前面30%的那一天
                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 2;
                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
                                        // 减掉上期的30%
                                        needPayAmount = BigDecimalUtil.sub(needPayAmount, firstAmount);
                                    }
                                }
                            } else {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                    needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
                                }
                            }
                        }
                        // 正常全额退
                        BigDecimal payReturnAmount = BigDecimalUtil.mul(returnCount, BigDecimalUtil.div(statementOrderDetailDO.getStatementDetailAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE));
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            //运费结算时间放到最接近退货单日期的结算日（大于等于退货日）
                            if (DateUtil.daysBetween(otherStatementTime, statementOrderDetailDO.getStatementExpectPayTime()) <= 0&&DateUtil.daysBetween(returnTime, statementOrderDetailDO.getStatementExpectPayTime()) >= 0) {
                                otherStatementTime = statementOrderDetailDO.getStatementExpectPayTime();
                            }
                        }
                        // 退款金额，扣除未交款的部分
                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT + "-" + orderProductDO.getId() + "-" + orderProductDO.getProductSkuId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
                        if (addStatementOrderDetailDOMap.get(key) != null) {
                            StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                            thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                        } else {
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, k3ReturnOrderDetailDO.getId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            if (thisStatementOrderDetailDO != null) {
                                thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                                thisStatementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
                                addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                            }
                        }
                    }
                }
            }
        }


        if (CollectionUtil.isNotEmpty(k3ReturnOrderDO.getK3ReturnOrderDetailDOList())) {
            for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDO.getK3ReturnOrderDetailDOList()) {
                if ((!k3ReturnOrderDetailDO.getProductNo().startsWith("20.") && !k3ReturnOrderDetailDO.getProductNo().startsWith("30.")) || k3ReturnOrderDetailDO.getOrderItemId() == null) {
                    continue;
                }
                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId()));
                if (orderMaterialDO == null) {
                    continue;
                }

                BigDecimal returnCount = new BigDecimal(k3ReturnOrderDetailDO.getProductCount());
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
                BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderMaterialDO.getRentDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE), returnCount);
                BigDecimal thisReturnDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderMaterialDO.getDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE), returnCount);
                if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                    for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                            // 第一期如果支付了押金，就要退押金，否则不退了
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                                //(针对违约长租设备)非即租即还设备，押金不退 //todo 后续增加续租商品 默认为即租即还
                                OrderDO orderDO = orderMapper.findById(statementOrderDetailDO.getOrderId());
                                if (orderDO == null) {
                                    result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                                    return result;
                                }
                                if (orderDO.getExpectReturnTime().compareTo(k3ReturnOrderDO.getReturnTime()) > 0) {
                                    if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                                        MaterialDO material = materialMapper.findById(orderMaterialDO.getMaterialId());
                                        if (material == null) {
                                            result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                                            return result;
                                        }
                                        if (material.getIsReturnAnyTime() != IsReturnAnyTime.RETURN_ANY_TIME_YES)
                                            continue;
                                    }
                                }
                                StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
                                if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementOrderDetailDO.getStatementDetailDepositReturnAmount()), thisReturnDepositAmount) >= 0) {
                                    totalReturnDepositAmount = BigDecimalUtil.add(totalReturnDepositAmount, thisReturnDepositAmount);
                                    statementOrderDetailDO.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositReturnAmount(), thisReturnDepositAmount));
                                    statementOrderDetailDO.setStatementDetailDepositReturnTime(currentTime);
                                    statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositReturnAmount(), thisReturnDepositAmount));
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnDepositAmount, returnTime, loginUser.getUserId(), currentTime);
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
                            continue;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                        // 如果退货时间在结束时间之后，证明本期都没缴，不用处理，等待交
                        if (returnTime.getTime() > statementDetailEndTime.getTime()) {
                            continue;
                        }

                        BigDecimal needPayAmount = BigDecimal.ZERO;
                        BigDecimal productUnitAmount = BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), returnCount);

                        if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
                            if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, returnTime, productUnitAmount);
                                }
                            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderMaterialDO.getPayMode())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    BigDecimal firstAmount = BigDecimalUtil.mul(returnCount, BigDecimalUtil.div(BigDecimalUtil.mul(orderMaterialDO.getMaterialAmount(), new BigDecimal(0.3)), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE));
                                    // 30%期
                                    if (statementOrderDetailDO.getStatementDetailPhase() == 1) {
                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
                                        needPayAmount = BigDecimalUtil.compare(needPayAmount, firstAmount) > 0 ? firstAmount : needPayAmount;
                                    } else if (statementOrderDetailDO.getStatementDetailPhase() == 2) {
                                        // 70% 期
                                        // 加2的原因是算上前面30%的那一天
                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 2;
                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
                                        // 减掉上期的30%
                                        needPayAmount = BigDecimalUtil.sub(needPayAmount, firstAmount);
                                    }
                                }
                            } else {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                    needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
                                }
                            }
                        }

                        BigDecimal payReturnAmount = BigDecimalUtil.mul(returnCount, BigDecimalUtil.div(statementOrderDetailDO.getStatementDetailAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE));
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            if (DateUtil.daysBetween(otherStatementTime, statementOrderDetailDO.getStatementExpectPayTime()) <= 0&&DateUtil.daysBetween(returnTime, statementOrderDetailDO.getStatementExpectPayTime()) >= 0) {
                                otherStatementTime = statementOrderDetailDO.getStatementExpectPayTime();
                            }
                        }
                        // 退款金额，扣除未交款的部分
                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL + "-" + orderMaterialDO.getId() + "-" + orderMaterialDO.getMaterialId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
                        if (addStatementOrderDetailDOMap.get(key) != null) {
                            StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                            thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                        } else {
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, k3ReturnOrderDetailDO.getId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            if (thisStatementOrderDetailDO != null) {
                                thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                                thisStatementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
                                addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                            }
                        }
                    }
                }
            }
        }


        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
            String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER.toString();
            // 如果没有处理完差价，统一当天交钱
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER, BigInteger.ZERO.intValue(), otherStatementTime, returnTime, returnTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUser.getUserId());
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
            ServiceResult<String, Boolean> returnDepositResult = paymentService.returnDeposit(buyerCustomerNo, totalReturnRentDepositAmount, totalReturnDepositAmount);
            if (!ErrorCode.SUCCESS.equals(returnDepositResult.getErrorCode()) || !returnDepositResult.getResult()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(returnDepositResult.getErrorCode());
                return result;
            }
        }
        //将退货单状态设置为已结算
        k3ReturnOrderDO.setSuccessStatus(CommonConstant.YES);
        k3ReturnOrderDO.setUpdateUser(loginUser.getUserId().toString());
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
        //计算违约金
//        ServiceResult<String, BigDecimal> penaltyResult= penaltySupport.k3OrderPenalty(k3ReturnOrderDO.getReturnOrderNo());
//        if(!ErrorCode.SUCCESS.equals(penaltyResult.getErrorCode())){
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            result.setErrorCode(penaltyResult.getErrorCode());
//            return result;
//        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
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
        Date otherStatementTime = returnTime;

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
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnDepositAmount, returnTime, loginUser.getUserId(), currentTime);
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
                            result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_HAVE_NOT_PAY_DEPOSIT);
                            return result;
                        }

                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                        // 如果退货时间在结束时间之后，证明本期都没缴，不用处理，等待交
                        if (returnTime.getTime() > statementDetailEndTime.getTime()) {
                            continue;
                        }
                        BigDecimal needPayAmount = BigDecimal.ZERO;
                        BigDecimal itemAllAmount = orderProductDO.getProductAmount();
                        if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
                            if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, returnTime, orderProductEquipmentDO.getProductEquipmentUnitAmount());
                                }
                            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderProductDO.getPayMode())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    BigDecimal firstAmount = BigDecimalUtil.div(BigDecimalUtil.mul(itemAllAmount, new BigDecimal(0.3)), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE);
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
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                    needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(orderProductEquipmentDO.getProductEquipmentUnitAmount(), new BigDecimal(surplusDays)) : needPayAmount;
                                }
                            }
                        }
                        // 正常全额退
                        BigDecimal payReturnAmount = BigDecimalUtil.div(statementOrderDetailDO.getStatementDetailAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE);
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            if (DateUtil.isSameDay(otherStatementTime, returnTime) || DateUtil.daysBetween(otherStatementTime, statementOrderDetailDO.getStatementExpectPayTime()) < 0) {
                                otherStatementTime = statementOrderDetailDO.getStatementExpectPayTime();
                            }
                        }
                        // 退款金额，扣除未交款的部分
                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT + "-" + orderProductDO.getId() + "-" + orderProductDO.getProductSkuId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
                        if (addStatementOrderDetailDOMap.get(key) != null) {
                            StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                            thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                        } else {
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderProductEquipmentDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, returnOrderProductEquipmentDO.getReturnOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            if (thisStatementOrderDetailDO != null) {
                                thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                                thisStatementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
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
                                    statementOrderDetailDO.setStatementDetailDepositReturnTime(currentTime);
                                    statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositReturnAmount(), thisReturnDepositAmount));
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnDepositAmount, returnTime, loginUser.getUserId(), currentTime);
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
                            result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_HAVE_NOT_PAY_DEPOSIT);
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
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, returnTime, orderMaterialBulkDO.getMaterialBulkUnitAmount());
                                }
                            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderMaterialDO.getPayMode())) {
                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
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
                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
                                    int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
                                    needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(orderMaterialBulkDO.getMaterialBulkUnitAmount(), new BigDecimal(surplusDays)) : needPayAmount;
                                }
                            }
                        }

                        BigDecimal payReturnAmount = BigDecimalUtil.div(statementOrderDetailDO.getStatementDetailAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE);
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            if (DateUtil.isSameDay(otherStatementTime, returnTime) || DateUtil.daysBetween(otherStatementTime, statementOrderDetailDO.getStatementExpectPayTime()) < 0) {
                                otherStatementTime = statementOrderDetailDO.getStatementExpectPayTime();
                            }
                        }
                        // 退款金额，扣除未交款的部分
                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL + "-" + orderMaterialDO.getId() + "-" + orderMaterialDO.getMaterialId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
                        if (addStatementOrderDetailDOMap.get(key) != null) {
                            StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                            thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                        } else {
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderMaterialBulkDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, returnOrderMaterialBulkDO.getReturnOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                            if (thisStatementOrderDetailDO != null) {
                                thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                                thisStatementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
                                addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                            }
                        }
                    }
                }
            }
        }

        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
            String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER.toString();
            // 如果没有处理完差价，统一当天交钱
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER, BigInteger.ZERO.intValue(), otherStatementTime, returnTime, returnTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUser.getUserId());
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
    public ServiceResult<String, BigDecimal> createK3ChangeOrderStatement(String changeOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(changeOrderNo);
        if (k3ChangeOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_CHANGE_ORDER_IS_NOT_NULL);
            return result;
        }

        //是否计算了其他费用
        boolean isCountOther = false;

        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        BigDecimal otherAmount = BigDecimalUtil.add(k3ChangeOrderDO.getLogisticsAmount(), k3ChangeOrderDO.getServiceAmount());
        List<K3ChangeOrderDetailDO> k3ChangeOrderDetailDOList = k3ChangeOrderDO.getK3ChangeOrderDetailDOList();
        if (CollectionUtil.isNotEmpty(k3ChangeOrderDetailDOList)) {
            for (K3ChangeOrderDetailDO k3ChangeOrderDetailDO : k3ChangeOrderDetailDOList) {
                Integer orderItemType = null;
                //获取更换类型
                if (k3ChangeOrderDetailDO.getChangeSkuId() != null) {
                    orderItemType = OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT;
                } else {
                    orderItemType = OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL;
                }

                BigDecimal productDiffAmount = k3ChangeOrderDetailDO.getProductDiffAmount();
                BigDecimal totalProductDiffAmount = BigDecimalUtil.mul(productDiffAmount, new BigDecimal(k3ChangeOrderDetailDO.getProductCount()));
                if (BigDecimalUtil.compare(totalProductDiffAmount, BigDecimal.ZERO) <= 0) {
                    continue;
                }
                //当月差价
                BigDecimal thisMonthDiff = BigDecimal.ZERO;

                //如果订单是本系统订单才处理结算单
                OrderDO orderDO = orderMapper.findByOrderNo(k3ChangeOrderDetailDO.getOrderNo());
                if (orderDO == null) {
                    continue;
                }
                //获取结算单
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
                //获取下次结算点（如果没有下次了，就当天）

                StatementOrderDetailDO nextStatement = null;
                for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                    if (nextStatement != null) {
                        break;
                    }

                    //只取该订单下结算单更换类型匹配且单项ID也相同的数据
                    StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                    Integer oldOrderItemType = null;
                    if (k3ChangeOrderDetailDO.getChangeSkuId() != null) {
                        oldOrderItemType = OrderItemType.ORDER_ITEM_TYPE_PRODUCT;
                    } else {
                        oldOrderItemType = OrderItemType.ORDER_ITEM_TYPE_MATERIAL;
                    }
                    //如果单项类型和id不完全匹配则忽略此结算单
                    if (!(oldOrderItemType.equals(statementOrderDetailDO.getOrderItemType()) && k3ChangeOrderDetailDO.getOrderItemId().equals(statementOrderDetailDO.getOrderItemReferId().toString()))) {
                        continue;
                    }
                    //换货时间在结算开始时间和结算结束时间之间，且结算类型为租金，就计算换货时间到结算结束时间期间
                    if (statementOrderDetailDO.getStatementStartTime().getTime() < k3ChangeOrderDO.getChangeTime().getTime() &&
                            statementOrderDetailDO.getStatementEndTime().getTime() >= k3ChangeOrderDO.getChangeTime().getTime()) {
                        //按天算钱
                        if (OrderRentType.RENT_TYPE_DAY.equals(k3ChangeOrderDetailDO.getRentType())) {
                            int dayCount = DateUtil.daysBetween(statementOrderDetailDO.getStatementStartTime(), statementOrderDetailDO.getStatementEndTime());
                            thisMonthDiff = BigDecimalUtil.mul(new BigDecimal(dayCount * k3ChangeOrderDetailDO.getProductCount()), k3ChangeOrderDetailDO.getProductDiffAmount());
                        } else if (OrderRentType.RENT_TYPE_MONTH.equals(k3ChangeOrderDetailDO.getRentType())) {
                            //按月算钱
                            thisMonthDiff = amountSupport.calculateRentAmount(k3ChangeOrderDO.getChangeTime(), statementOrderDetailDO.getStatementEndTime(), productDiffAmount, k3ChangeOrderDetailDO.getProductCount());
                        }
                        if (i != statementOrderDetailDOList.size() - 1) {
                            nextStatement = statementOrderDetailDOList.get(i + 1);
                        } else {
                            StatementOrderDetailDO newStatementOrderDetailDO = new StatementOrderDetailDO();
                            newStatementOrderDetailDO.setCustomerId(statementOrderDetailDO.getCustomerId());
                            newStatementOrderDetailDO.setOrderId(k3ChangeOrderDetailDO.getChangeOrderId());
                            newStatementOrderDetailDO.setOrderItemType(orderItemType);
                            newStatementOrderDetailDO.setOrderItemReferId(Integer.parseInt(k3ChangeOrderDetailDO.getOrderItemId()));
                            newStatementOrderDetailDO.setStatementStartTime(k3ChangeOrderDO.getChangeTime());
                            newStatementOrderDetailDO.setStatementExpectPayTime(com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(k3ChangeOrderDO.getChangeTime()));
                            newStatementOrderDetailDO.setStatementEndTime(k3ChangeOrderDO.getChangeTime());
                            newStatementOrderDetailDO.setStatementDetailPhase(0);
                            newStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                            nextStatement = newStatementOrderDetailDO;
                        }
                    }
                }
                if (nextStatement != null) {
                    //如果差价大于0
                    if (BigDecimalUtil.compare(thisMonthDiff, BigDecimal.ZERO) > 0) {

                        StatementOrderDetailDO changeStatementOrderDetailDO = buildStatementOrderDetailDO(nextStatement.getCustomerId(), OrderType.ORDER_TYPE_CHANGE,
                                k3ChangeOrderDetailDO.getChangeOrderId(), orderItemType, Integer.parseInt(k3ChangeOrderDetailDO.getOrderItemId()),
                                nextStatement.getStatementExpectPayTime(), nextStatement.getStatementStartTime(), nextStatement.getStatementEndTime(),
                                thisMonthDiff, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                        changeStatementOrderDetailDO.setStatementDetailPhase(nextStatement.getStatementDetailPhase());
                        changeStatementOrderDetailDO.setStatementDetailType(nextStatement.getStatementDetailType());
                        addStatementOrderDetailDOList.add(changeStatementOrderDetailDO);
                    }
                    //如果其他费用没有计算过，且其他费用大于0
                    if (!isCountOther && BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                        StatementOrderDetailDO changeStatementOrderDetailDO = buildStatementOrderDetailDO(nextStatement.getCustomerId(), OrderType.ORDER_TYPE_CHANGE,
                                k3ChangeOrderDetailDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER, 0,
                                nextStatement.getStatementExpectPayTime(), nextStatement.getStatementStartTime(), nextStatement.getStatementEndTime(),
                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUser.getUserId());
                        changeStatementOrderDetailDO.setStatementDetailPhase(nextStatement.getStatementDetailPhase());
                        changeStatementOrderDetailDO.setStatementDetailType(nextStatement.getStatementDetailType());
                        addStatementOrderDetailDOList.add(changeStatementOrderDetailDO);
                        isCountOther = true;
                    }
                }


                for (int i = 0; i < statementOrderDetailDOList.size(); i++) {
                    StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailDOList.get(i);
                    //结算开始时间小于等于换货时间，不处理，不是租金不处理
                    if (!StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType()) &&
                            statementOrderDetailDO.getStatementStartTime().getTime() <= k3ChangeOrderDO.getChangeTime().getTime()) {
                        continue;
                    }
                    Integer oldOrderItemType = null;
                    if (k3ChangeOrderDetailDO.getChangeSkuId() != null) {
                        oldOrderItemType = OrderItemType.ORDER_ITEM_TYPE_PRODUCT;
                    } else {
                        oldOrderItemType = OrderItemType.ORDER_ITEM_TYPE_MATERIAL;
                    }
                    //如果单项类型和id不完全匹配则忽略此结算单
                    if (!(oldOrderItemType.equals(statementOrderDetailDO.getOrderItemType()) && k3ChangeOrderDetailDO.getOrderItemId().equals(statementOrderDetailDO.getOrderItemReferId().toString()))) {
                        continue;
                    }
                    BigDecimal diff = BigDecimal.ZERO;
                    //按天算钱
                    if (OrderRentType.RENT_TYPE_DAY.equals(k3ChangeOrderDetailDO.getRentType())) {
                        int dayCount = DateUtil.daysBetween(statementOrderDetailDO.getStatementStartTime(), statementOrderDetailDO.getStatementEndTime());
                        diff = BigDecimalUtil.mul(new BigDecimal(dayCount * k3ChangeOrderDetailDO.getProductCount()), k3ChangeOrderDetailDO.getProductDiffAmount());
                    } else if (OrderRentType.RENT_TYPE_MONTH.equals(k3ChangeOrderDetailDO.getRentType())) {
                        //按月算钱
                        diff = amountSupport.calculateRentAmount(statementOrderDetailDO.getStatementStartTime(), statementOrderDetailDO.getStatementEndTime(), totalProductDiffAmount);
                    }
                    StatementOrderDetailDO changeStatementOrderDetailDO = buildStatementOrderDetailDO(statementOrderDetailDO.getCustomerId(), OrderType.ORDER_TYPE_CHANGE,
                            k3ChangeOrderDetailDO.getChangeOrderId(), orderItemType, Integer.parseInt(k3ChangeOrderDetailDO.getOrderItemId()),
                            statementOrderDetailDO.getStatementExpectPayTime(), statementOrderDetailDO.getStatementStartTime(), statementOrderDetailDO.getStatementEndTime(),
                            diff, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId());
                    changeStatementOrderDetailDO.setStatementDetailPhase(statementOrderDetailDO.getStatementDetailPhase());
                    changeStatementOrderDetailDO.setStatementDetailType(statementOrderDetailDO.getStatementDetailType());
                    addStatementOrderDetailDOList.add(changeStatementOrderDetailDO);
                }
            }
        }
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUser.getUserId());
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
        // 先屏蔽换货的
        if (true) {
            result.setErrorCode(ErrorCode.SUCCESS);
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

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Boolean> handleOverdueStatementOrder(Date startTime, Date endTime) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        Date currentTime = new Date();
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        param.setStatementExpectPayStartTime(startTime);
        param.setStatementExpectPayEndTime(endTime);
        param.setStatementOrderNo("LXSO-731828-20180205-00412");
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderQueryParam", param);
        List<StatementOrderDO> statementOrderDOList = statementOrderMapper.listPage(maps);
        updateStatementOrderOverdue(statementOrderDOList, currentTime);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Boolean> handleNoPaidStatementOrder(Date startTime, Date endTime) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        StatementPayOrderQueryParam statementPayOrderQueryParam = new StatementPayOrderQueryParam();
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        statementPayOrderQueryParam.setCreateStartTime(startTime);
        statementPayOrderQueryParam.setCreateEndTime(endTime);
        statementPayOrderQueryParam.setPayStatus(PayStatus.PAY_STATUS_PAYING);
        maps.put("statementPayOrderQueryParam", statementPayOrderQueryParam);
        List<StatementPayOrderDO> statementPayOrderDOList = statementPayOrderMapper.listPage(maps);
        if (CollectionUtil.isNotEmpty(statementPayOrderDOList)) {
            for (StatementPayOrderDO statementPayOrderDO : statementPayOrderDOList) {
                try {
                    StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementPayOrderDO.getStatementOrderId());
                    CustomerDO customerDO = customerMapper.findById(statementOrderDO.getCustomerId());
                    Integer payType = StatementOrderPayType.PAY_TYPE_WEIXIN.equals(statementPayOrderDO.getPayType()) ? 2 : 4;
                    ServiceResult<String, PayResult> queryPayResult = paymentService.queryPayResult(statementPayOrderDO.getStatementPayOrderNo(), payType, customerDO.getCustomerNo());
                    if (!ErrorCode.SUCCESS.equals(queryPayResult.getErrorCode())) {
                        logger.error("查询结算单有误，请检查，{}，{}，{}", statementPayOrderDO.getStatementPayOrderNo(), statementOrderDO.getStatementOrderNo(), queryPayResult.getErrorCode());
                        continue;
                    }
                    PayResult payResult = queryPayResult.getResult();
                    if (!CommonConstant.COMMON_CONSTANT_YES.toString().equals(payResult.getStatus())
                            || (!PayStatus.PAY_STATUS_PAID.toString().equals(payResult.getPayStatus()) && !PayStatus.PAY_STATUS_FAILED.toString().equals(payResult.getPayStatus()))) {
                        logger.error("查询结算单还没有结果，{}，{}", statementPayOrderDO.getStatementPayOrderNo(), statementOrderDO.getStatementOrderNo());
                        continue;
                    }

                    WeixinPayCallbackParam param = new WeixinPayCallbackParam();
                    param.setAmount(new BigDecimal(payResult.getRespAmount()));
                    param.setBusinessOrderNo(payResult.getBusinessOrderNo());
                    param.setOrderNo(payResult.getBusinessOrderNo());
                    param.setNotifyStatus(PayStatus.PAY_STATUS_PAID.toString().equals(payResult.getPayStatus()) ? "1" : PayStatus.PAY_STATUS_FAILED.toString().equals(payResult.getPayStatus()) ? "2" : null);
                    ServiceResult<String, String> weixinPayCallbackResult = weixinPayCallback(param);
                    if (!ErrorCode.SUCCESS.equals(weixinPayCallbackResult.getErrorCode())) {
                        logger.error("更新结算单有误，请检查，{}，{}，{}", statementPayOrderDO.getStatementPayOrderNo(), statementOrderDO.getStatementOrderNo(), weixinPayCallbackResult.getErrorCode());
                        continue;
                    }
                } catch (Exception e) {
                    logger.error("自动检查支付结果遇到异常，请检查，{}，{}", statementPayOrderDO.getStatementPayOrderNo(), e.toString());
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<StatementOrder>> queryStatementOrderCheckParam(StatementOrderMonthQueryParam statementOrderMonthQueryParam) {
        ServiceResult<String, Page<StatementOrder>> result = new ServiceResult<>();

        if (statementOrderMonthQueryParam.getMonthTime() == null) {
            statementOrderMonthQueryParam.setMonthTime(new Date());
        }
        PageQuery pageQuery = new PageQuery(statementOrderMonthQueryParam.getPageNo(), statementOrderMonthQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("statementOrderMonthQueryParam", statementOrderMonthQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = statementOrderMapper.listMonthCount(maps);
        List<StatementOrderDO> statementOrderDOList = statementOrderMapper.listMonthPage(maps);
        List<StatementOrder> statementOrderList = ConverterUtil.convertList(statementOrderDOList, StatementOrder.class);
        Page<StatementOrder> page = new Page<>(statementOrderList, totalCount, statementOrderMonthQueryParam.getPageNo(), statementOrderMonthQueryParam.getPageSize());

        List<StatementOrder> newList = new ArrayList<>();
        for (int i = 0; i < page.getItemList().size(); i++) {
            StatementOrder statementOrder = page.getItemList().get(i);
            statementOrder.setMonthTime(statementOrderMonthQueryParam.getMonthTime());
            newList.add(statementOrder);
        }
        page.setItemList(newList);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, StatementOrder> queryStatementOrderMonthDetail(String customerNo, Date month) {
        ServiceResult<String, StatementOrder> result = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(customerNo);
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        if (month == null) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_NULL);
            return result;
        }
        List<StatementOrderDO> statementOrderDOList = statementOrderMapper.findByCustomerNo(customerDO.getCustomerNo(), month);
        if (statementOrderDOList == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        StatementOrderDO statementOrderDO = statementOrderDOList.get(0);
        List<StatementOrderDetailDO> statementOrderDetailDOList = new ArrayList<>();
        StatementOrderDetailDO statementOrderDetailDO = new StatementOrderDetailDO();
        for (int i = 0; i < statementOrderDOList.size(); i++) {
            List<StatementOrderDetailDO> list = statementOrderDOList.get(i).getStatementOrderDetailDOList();
            for (int j = 0; j < list.size(); j++) {
                statementOrderDetailDO = list.get(j);
            }
            statementOrderDetailDOList.add(statementOrderDetailDO);
        }
        statementOrderDO.setStatementOrderDetailDOList(statementOrderDetailDOList);

        StatementOrder statementOrder = ConverterUtil.convert(statementOrderDO, StatementOrder.class);

        StatementOrderDetail returnReferStatementOrderDetail = null;

        //获取各项的总和,区分了各商品
        Map<String, StatementOrderDetail> hashMap = new HashMap<>();

        if (statementOrder != null && CollectionUtil.isNotEmpty(statementOrder.getStatementOrderDetailList())) {
            Map<Integer, StatementOrderDetail> statementOrderDetailMap = ListUtil.listToMap(statementOrder.getStatementOrderDetailList(), "statementOrderDetailId");

            for (StatementOrderDetail statementOrderDetail : statementOrder.getStatementOrderDetailList()) {
                if (statementOrderDetail.getReturnReferId() != null) {
                    returnReferStatementOrderDetail = statementOrderDetailMap.get(statementOrderDetail.getReturnReferId());
                }
                convertStatementOrderDetailOtherInfo(statementOrderDetail, returnReferStatementOrderDetail, null);
                String key = null;
                if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
                    //为订单商品时
                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                        OrderProductDO orderProductDO = orderProductMapper.findById(statementOrderDetail.getOrderItemReferId());
                        if (orderProductDO != null) {
                            Integer productId = orderProductDO.getProductId();
                            Integer isNewProduct = orderProductDO.getIsNewProduct();
                            key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + productId + "-" + isNewProduct + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                        }
                    }
                    //为订单物料时
                    if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                        OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(statementOrderDetail.getOrderItemReferId());
                        if (orderMaterialDO != null) {
                            Integer materialId = orderMaterialDO.getMaterialId();
                            Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                            key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + materialId + "-" + isNewMaterial + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                        }
                    }
                    //为订单其他时
                    if (OrderItemType.ORDER_ITEM_TYPE_OTHER.equals(statementOrderDetail.getOrderItemType())) {
                        key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                        hashMap.put(key, statementOrderDetail);
                        continue;
                    }
                }
                if (OrderType.ORDER_TYPE_RETURN.equals(statementOrderDetail.getOrderType())) {
                    //为退还商品时
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                        K3ReturnOrderDetailDO k3ReturnOrderDetailDO = k3ReturnOrderDetailMapper.findById(statementOrderDetail.getOrderItemReferId());
                        if (k3ReturnOrderDetailDO != null) {
                            String orderItemId = k3ReturnOrderDetailDO.getOrderItemId();
                            OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(orderItemId));
                            if (orderProductDO != null) {
                                Integer productId = orderProductDO.getProductId();
                                Integer isNewProduct = orderProductDO.getIsNewProduct();
                                key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + productId + "-" + isNewProduct + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                            }
                        }
                    }
                    //为退还物料时
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                        K3ReturnOrderDetailDO k3ReturnOrderDetailDO = k3ReturnOrderDetailMapper.findById(statementOrderDetail.getOrderItemReferId());
                        if (k3ReturnOrderDetailDO != null) {
                            String orderItemId = k3ReturnOrderDetailDO.getOrderItemId();
                            OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(orderItemId));
                            if (orderMaterialDO != null) {
                                Integer materialId = orderMaterialDO.getMaterialId();
                                Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                                key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + materialId + "-" + isNewMaterial + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                            }
                        }
                    }
                }
                if (OrderType.ORDER_TYPE_CHANGE.equals(statementOrderDetail.getOrderType())) {
                    if (OrderType.ORDER_TYPE_CHANGE.equals(statementOrderDetail.getOrderType())) {
                        //为换商品时
                        K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findById(statementOrderDetail.getOrderId());
                        K3ChangeOrderDetailDO k3ChangeOrderDetailDO = k3ChangeOrderDetailMapper.findById(statementOrderDetail.getOrderItemReferId());
                        if (OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                            if (k3ChangeOrderDetailDO != null) {
                                String orderItemId = k3ChangeOrderDetailDO.getOrderItemId();
                                OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(orderItemId));
                                statementOrderDetail.setOrderItemType(OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT);
                                statementOrderDetail.setItemName(k3ChangeOrderDetailDO.getProductName());
                                statementOrderDetail.setItemCount(k3ChangeOrderDetailDO.getProductCount());
                                statementOrderDetail.setItemRentType(k3ChangeOrderDetailDO.getRentType());
                                if (orderProductDO != null) {
                                    Integer productId = orderProductDO.getProductId();
                                    Integer isNewProduct = orderProductDO.getIsNewProduct();
                                    key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + productId + "-" + isNewProduct + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                                }
                            }
                        }
                        //为换物料时
                        if (OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                            if (k3ChangeOrderDetailDO != null) {
                                String orderItemId = k3ChangeOrderDetailDO.getOrderItemId();
                                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(orderItemId));
                                statementOrderDetail.setOrderItemType(OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL);
                                statementOrderDetail.setItemName(k3ChangeOrderDetailDO.getProductName());
                                statementOrderDetail.setItemCount(k3ChangeOrderDetailDO.getProductCount());
                                statementOrderDetail.setItemRentType(k3ChangeOrderDetailDO.getRentType());
                                if (orderMaterialDO != null) {
                                    Integer materialId = orderMaterialDO.getMaterialId();
                                    Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                                    key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + materialId + "-" + isNewMaterial + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                                }
                            }
                        }
                        //为换货其他费用时
                        if (OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER.equals(statementOrderDetail.getOrderItemType())) {
                            if (k3ChangeOrderDO != null) {
                                statementOrderDetail.setOrderNo(k3ChangeOrderDO.getChangeOrderNo());
                            }
                            key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                        }
                    }
                }

                if (key == null) {
                    continue;
                }

                //各商品物料
                StatementOrderDetail newStatementOrderDetail = hashMap.get(key);
                if (newStatementOrderDetail != null) {
                    newStatementOrderDetail.setStatementDetailRentAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentAmount(), statementOrderDetail.getStatementDetailRentAmount()));
                    newStatementOrderDetail.setStatementDetailRentPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentPaidAmount(), statementOrderDetail.getStatementDetailRentPaidAmount()));
                    newStatementOrderDetail.setStatementDetailRentDepositAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentDepositAmount(), statementOrderDetail.getStatementDetailRentDepositAmount()));
                    newStatementOrderDetail.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentDepositPaidAmount(), statementOrderDetail.getStatementDetailRentDepositPaidAmount()));
                    newStatementOrderDetail.setStatementDetailDepositAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailDepositAmount(), statementOrderDetail.getStatementDetailDepositAmount()));
                    newStatementOrderDetail.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailDepositPaidAmount(), statementOrderDetail.getStatementDetailDepositPaidAmount()));
                    newStatementOrderDetail.setStatementDetailOverdueAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailOverdueAmount(), statementOrderDetail.getStatementDetailOverdueAmount()));
                    newStatementOrderDetail.setStatementDetailOverduePaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailOverduePaidAmount(), statementOrderDetail.getStatementDetailOverduePaidAmount()));
                    newStatementOrderDetail.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailRentDepositReturnAmount(), statementOrderDetail.getStatementDetailRentDepositReturnAmount()));
                    newStatementOrderDetail.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailDepositReturnAmount(), statementOrderDetail.getStatementDetailDepositReturnAmount()));
                    newStatementOrderDetail.setStatementDetailCorrectAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailCorrectAmount(), statementOrderDetail.getStatementDetailCorrectAmount()));
                    newStatementOrderDetail.setStatementDetailOtherAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailOtherAmount(), statementOrderDetail.getStatementDetailOtherAmount()));
                    newStatementOrderDetail.setStatementDetailOtherPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailOtherPaidAmount(), statementOrderDetail.getStatementDetailOtherPaidAmount()));
                    newStatementOrderDetail.setStatementDetailAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailAmount(), statementOrderDetail.getStatementDetailAmount()));
                    newStatementOrderDetail.setStatementDetailPaidAmount(BigDecimalUtil.add(newStatementOrderDetail.getStatementDetailPaidAmount(), statementOrderDetail.getStatementDetailPaidAmount()));

                    // 开始计算结算时间
                    if (newStatementOrderDetail.getStatementStartTime() != null && statementOrderDetail.getStatementStartTime() != null) {
                        if (newStatementOrderDetail.getStatementStartTime().getTime() > statementOrderDetail.getStatementStartTime().getTime()) {
                            newStatementOrderDetail.setStatementStartTime(statementOrderDetail.getStatementStartTime());
                        }
                    }
                    if (newStatementOrderDetail.getStatementEndTime() != null && statementOrderDetail.getStatementEndTime() != null) {
                        if (newStatementOrderDetail.getStatementEndTime().getTime() < statementOrderDetail.getStatementEndTime().getTime()) {
                            newStatementOrderDetail.setStatementEndTime(statementOrderDetail.getStatementEndTime());
                        }
                    }
                } else {
                    //各项总金额
                    hashMap.put(key, statementOrderDetail);
                }

            }
        }

        for (StatementOrderDetail statementOrderDetail : hashMap.values()) {
            BigDecimal statementDetailPaidAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementOrderDetail.getStatementDetailDepositPaidAmount(), statementOrderDetail.getStatementDetailOverduePaidAmount()), statementOrderDetail.getStatementDetailRentDepositPaidAmount()), statementOrderDetail.getStatementDetailRentPaidAmount()), statementOrderDetail.getStatementDetailOtherPaidAmount());
            statementOrderDetail.setStatementDetailPaidAmount(statementDetailPaidAmount);
        }

        List<StatementOrderDetail> statementOrderDetailList = ListUtil.mapToList(hashMap);
        statementOrder.setStatementOrderDetailList(statementOrderDetailList);

        statementOrder.setMonthTime(month);

        result.setResult(statementOrder);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    void updateStatementOrderOverdue(List<StatementOrderDO> statementOrderDOList, Date currentTime) {
        Integer monthStatementOrderAllowDays = 7;
        if (CollectionUtil.isEmpty(statementOrderDOList)) {
            return;
        }

        for (StatementOrderDO statementOrderDO : statementOrderDOList) {
            if (statementOrderDO == null || StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus())
                    || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDO.getStatementStatus())) {
                continue;
            }
            BigDecimal totalOverdueAmount = BigDecimal.ZERO;
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
                // 结算单为空或者状态异常，无需处理
                if (statementOrderDetailDO == null || StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())
                        || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                    continue;
                }
                // 结算单金额小于0，无需处理
                if (BigDecimalUtil.compare(statementOrderDetailDO.getStatementDetailAmount(), BigDecimal.ZERO) <= 0) {
                    continue;
                }
                int overdueDays = DateUtil.daysBetween(statementOrderDetailDO.getStatementExpectPayTime(), currentTime);
                // 小于等于0说明没逾期
                if (overdueDays <= 0) {
                    continue;
                }

                Integer statementOrderOverduePhaseCount = 0;
                // 判断订单的租赁类型，如果按月租的话，延后七天开始算逾期，如果按天租的话，延后第二天开始算逾期
                if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetailDO.getOrderType())) {
                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetailDO.getOrderItemType())) {
                        OrderProductDO orderProductDO = orderProductMapper.findById(statementOrderDetailDO.getOrderItemReferId());
                        if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType()) && overdueDays <= monthStatementOrderAllowDays) {
                            continue;
                        } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                            overdueDays = overdueDays - monthStatementOrderAllowDays;
                            statementOrderOverduePhaseCount = DateUtil.getMonthSpace(statementOrderDetailDO.getStatementExpectPayTime(), currentTime) + 1;
                        } else {
                            statementOrderOverduePhaseCount = overdueDays;
                        }
                    } else if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetailDO.getOrderItemType())) {
                        OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(statementOrderDetailDO.getOrderItemReferId());
                        if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType()) && overdueDays <= monthStatementOrderAllowDays) {
                            continue;
                        } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                            overdueDays = overdueDays - monthStatementOrderAllowDays;
                            statementOrderOverduePhaseCount = DateUtil.getMonthSpace(statementOrderDetailDO.getStatementExpectPayTime(), currentTime) + 1;
                        } else {
                            statementOrderOverduePhaseCount = overdueDays;
                        }
                    }
                }
                BigDecimal originalDetailOverdueAmount = statementOrderDetailDO.getStatementDetailOverdueAmount();
                BigDecimal originalDetailAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(), originalDetailOverdueAmount);

                // 以下均为逾期处理，overdueDays 为逾期天数，开始算逾期。
                BigDecimal detailOverdueAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(originalDetailAmount, new BigDecimal(0.003)), new BigDecimal(overdueDays)).setScale(BigDecimalUtil.STANDARD_SCALE, BigDecimal.ROUND_HALF_UP);
                statementOrderDetailDO.setStatementDetailOverdueAmount(detailOverdueAmount);
                statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(originalDetailAmount, detailOverdueAmount));
                statementOrderDetailDO.setStatementDetailOverdueDays(overdueDays);
                statementOrderDetailDO.setStatementDetailOverduePhaseCount(statementOrderOverduePhaseCount);
                statementOrderDetailMapper.update(statementOrderDetailDO);

                totalOverdueAmount = BigDecimalUtil.add(totalOverdueAmount, detailOverdueAmount);
            }

            BigDecimal originalOverdueAmount = statementOrderDO.getStatementOverdueAmount();
            BigDecimal originalAmount = BigDecimalUtil.sub(statementOrderDO.getStatementAmount(), originalOverdueAmount);
            BigDecimal nowOverdueAmount = BigDecimalUtil.add(totalOverdueAmount, statementOrderDO.getStatementOverduePaidAmount());
            statementOrderDO.setStatementAmount(BigDecimalUtil.add(BigDecimalUtil.round(originalAmount, BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(nowOverdueAmount, BigDecimalUtil.STANDARD_SCALE)));
            statementOrderDO.setStatementOverdueAmount(nowOverdueAmount);
            statementOrderMapper.update(statementOrderDO);
        }
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
                    if (dateKey != null) {
                        statementOrderDO.setStatementExpectPayTime(com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(dateKey));
                    }
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
                    statementOrderDO.setStatementCouponAmount(statementOrderDetailDO.getStatementCouponAmount());
                    statementOrderMapper.save(statementOrderDO);
                } else {
                    statementOrderDO = statementOrderDOMap.get(dateKey);
                    statementOrderDO.setStatementAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    // 结算单不能为负数
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) < 0) {
                        BigDecimal diffAmount = BigDecimalUtil.sub(BigDecimal.ZERO, statementOrderDO.getStatementAmount());
                        statementOrderDO.setStatementAmount(BigDecimal.ZERO);
                        statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(diffAmount, BigDecimalUtil.STANDARD_SCALE)));
                    }
                    statementOrderDO.setStatementRentAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementRentAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    // 结算单不能为负数
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementRentAmount(), BigDecimal.ZERO) < 0) {
                        BigDecimal diffAmount = BigDecimalUtil.sub(BigDecimal.ZERO, statementOrderDO.getStatementRentAmount());
                        statementOrderDO.setStatementRentAmount(BigDecimal.ZERO);
                        statementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(diffAmount, BigDecimalUtil.STANDARD_SCALE)));
                    }
                    statementOrderDO.setStatementDepositAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementDepositAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailDepositAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementRentDepositAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailRentDepositAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementOtherAmount(BigDecimalUtil.add(BigDecimalUtil.round(statementOrderDO.getStatementOtherAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailOtherAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus())) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
                    }
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), new BigDecimal(0.1)) < 0) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
                    }
                    if (statementOrderDetailDO.getStatementStartTime().getTime() < statementOrderDO.getStatementStartTime().getTime()) {
                        statementOrderDO.setStatementStartTime(statementOrderDetailDO.getStatementStartTime());
                    }
                    if (statementOrderDetailDO.getStatementEndTime().getTime() > statementOrderDO.getStatementEndTime().getTime()) {
                        statementOrderDO.setStatementEndTime(statementOrderDetailDO.getStatementEndTime());
                    }
                    statementOrderDO.setStatementCouponAmount(statementOrderDetailDO.getStatementCouponAmount());
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
            // 按月租的情况
            if (paymentCycle == null || rentTimeLength == null) {
                return null;
            }
            if (paymentCycle == null || paymentCycle >= rentTimeLength || paymentCycle == 0) {
                return statementMonthCount;
            }
            statementMonthCount = rentTimeLength / paymentCycle;
            if ((rentTimeLength % paymentCycle > 0) || (startDay > (statementDay + 1)) || (StatementMode.STATEMENT_MONTH_END.equals(statementDay) && startDay <= 10)) {
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
        Calendar rentStartTimeCalendar = Calendar.getInstance();
        rentStartTimeCalendar.setTime(rentStartTime);
        // 如果结算日为31日，并且租期在10日前，交到前一个月的即可
        if (StatementMode.STATEMENT_MONTH_END.equals(statementDays) && rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH) <= 10) {
            paymentCycle--;
        }
        Date statementEndTime = com.lxzl.se.common.util.date.DateUtil.monthInterval(rentStartTime, paymentCycle);
        Calendar statementEndTimeCalendar = Calendar.getInstance();
        statementEndTimeCalendar.setTime(statementEndTime);
        int statementEndMonthDays = DateUtil.getActualMaximum(statementEndTime);
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            // 不做任何动作，只是当天
            statementEndTime = rentStartTime;
        } else if (statementDays > statementEndMonthDays) {
            // 判断月份天数小于31的情况
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
            firstPhaseAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, unitAmount, itemCount);
        } else if (OrderRentType.RENT_TYPE_DAY.equals(rentType) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
            firstPhaseAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(BigDecimalUtil.mul(unitAmount, new BigDecimal(rentLength), 2), new BigDecimal(itemCount)), new BigDecimal(0.3));
        } else {
            // 如果按天的，本期结束就要还款，如果是按月的，本期结束第二天还款
            if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
                statementExpectPayTime = statementEndTime;
            } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
                statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, 1);
            }
            firstPhaseAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, unitAmount, itemCount);
        }
        firstPhaseAmount = BigDecimalUtil.round(firstPhaseAmount, 0);
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
        statementOrderDetailDO.setStatementExpectPayTime(com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementExpectPayTime));
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

    /**
     * 清除订单的结算单信息
     *
     * @param orderDO
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, String> clearStatementOrderDetail(OrderDO orderDO) {
        ServiceResult<String, String> result = new ServiceResult<>();
        CustomerDO customerDO = customerMapper.findById(orderDO.getBuyerCustomerId());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        //删除原结算数据(订单类型)
        List<StatementOrderDetailDO> dbStatementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
        BigDecimal needReturnAmount = BigDecimal.ZERO;
        BigDecimal totalDepositPaidAmount = BigDecimal.ZERO;
        BigDecimal totalDepositRentPaidAmount = BigDecimal.ZERO;
        Map<Date, StatementOrderDO> statementOrderDOMap = new HashMap<>();//结算单缓存
        String userId = userSupport.getCurrentUser().getUserId().toString();
        Date now = new Date();
        //部分结算或结算完成需退还
        Set<Integer> canReturnStatusSet = new HashSet<Integer>();
        canReturnStatusSet.add(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        canReturnStatusSet.add(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
        for (StatementOrderDetailDO statementOrderDetailDO : dbStatementOrderDetailDOList) {
            //非订单类型订单
            if (!statementOrderDetailDO.getOrderType().equals(OrderType.ORDER_TYPE_ORDER)) continue;
            Date dateKey = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementExpectPayTime());
            if (!statementOrderDOMap.containsKey(dateKey)) {
                StatementOrderDO statementOrderDO = statementOrderMapper.findByCustomerAndPayTime(statementOrderDetailDO.getCustomerId(), dateKey);
                statementOrderDOMap.put(dateKey, statementOrderDO);
            }
            StatementOrderDO statementOrderDO = statementOrderDOMap.get(dateKey);
            //结算单存在则修改结算单(不存在则直接删除结算详情)
            if (statementOrderDO != null) {
                //获取结算单关联退货结算单明细（目前只有押金和租金退货单）
                Integer statementDetailType = statementOrderDetailDO.getStatementDetailType();
                Integer returnType = null;
                if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementDetailType)) {
                    returnType = StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT;
                } else if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementDetailType)) {
                    returnType = StatementDetailType.STATEMENT_DETAIL_TYPE_RETURN_DEPOSIT;
                }
                //计算已退金额
                BigDecimal returnStatementAmount = BigDecimal.ZERO, returnStatementRentAmount = BigDecimal.ZERO, returnStatementDepositAmount = BigDecimal.ZERO, returnRentDepositAmount = BigDecimal.ZERO;
                if (returnType != null) {
                    List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByReturnReferIdAndStatementType(statementOrderDetailDO.getId(), returnType);
                    if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                        for (StatementOrderDetailDO sod : statementOrderDetailDOList) {
                            returnStatementAmount = BigDecimalUtil.add(returnStatementAmount, sod.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE);
                            returnStatementRentAmount = BigDecimalUtil.add(returnStatementRentAmount, sod.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE);
                            returnStatementDepositAmount = BigDecimalUtil.add(returnStatementDepositAmount, sod.getStatementDetailDepositAmount(), BigDecimalUtil.STANDARD_SCALE);
                            returnRentDepositAmount = BigDecimalUtil.add(returnRentDepositAmount, sod.getStatementDetailRentDepositAmount(), BigDecimalUtil.STANDARD_SCALE);

                        }
                    }
                }
                //计算还需退金额
                returnStatementAmount = BigDecimalUtil.add(returnStatementAmount, statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE);
                returnStatementRentAmount = BigDecimalUtil.add(returnStatementRentAmount, statementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE);
                returnStatementDepositAmount = BigDecimalUtil.add(returnStatementDepositAmount, statementOrderDetailDO.getStatementDetailDepositAmount(), BigDecimalUtil.STANDARD_SCALE);
                returnRentDepositAmount = BigDecimalUtil.add(returnRentDepositAmount, statementOrderDetailDO.getStatementDetailRentDepositAmount(), BigDecimalUtil.STANDARD_SCALE);


                statementOrderDO.setStatementAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementAmount(), BigDecimalUtil.STANDARD_SCALE), returnStatementAmount));
                statementOrderDO.setStatementRentAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementRentAmount(), BigDecimalUtil.STANDARD_SCALE), returnStatementRentAmount));
                // 结算单不能为负数
                if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) < 0 || BigDecimalUtil.compare(statementOrderDO.getStatementRentAmount(), BigDecimal.ZERO) < 0) {
                    result.setErrorCode(ErrorCode.STATEMENT_PAY_AMOUNT_ERROR);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return result;
                }
                statementOrderDO.setStatementDepositAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementDepositAmount(), BigDecimalUtil.STANDARD_SCALE), returnStatementDepositAmount));
                statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementRentDepositAmount(), BigDecimalUtil.STANDARD_SCALE), returnRentDepositAmount));
                statementOrderDO.setStatementOtherAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementOtherAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailOtherAmount(), BigDecimalUtil.STANDARD_SCALE)));


                if (canReturnStatusSet.contains(statementOrderDetailDO.getStatementDetailStatus())) {

                    BigDecimal needReturnStatementDepositPaidAmount = BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailDepositReturnAmount(), BigDecimalUtil.STANDARD_SCALE));
                    BigDecimal needReturnStatementRentDepositPaidAmount = BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailRentDepositReturnAmount(), BigDecimalUtil.STANDARD_SCALE));

                    BigDecimal orderDetailTotalPaid = BigDecimal.ZERO;
                    //非押金
                    orderDetailTotalPaid = BigDecimalUtil.add(orderDetailTotalPaid, BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailRentPaidAmount(), BigDecimalUtil.STANDARD_SCALE));
                    orderDetailTotalPaid = BigDecimalUtil.add(orderDetailTotalPaid, BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), BigDecimalUtil.STANDARD_SCALE));
                    orderDetailTotalPaid = BigDecimalUtil.add(orderDetailTotalPaid, BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailOverduePaidAmount(), BigDecimalUtil.STANDARD_SCALE));

                    //记录非押金金额（退回账户）
                    needReturnAmount = BigDecimalUtil.add(needReturnAmount, orderDetailTotalPaid);

                    //押金（退押金用）
                    //记录退押金 ······
                    boolean isDepositReturn = false, isRentDepositReturn = false;
                    if (BigDecimalUtil.compare(needReturnStatementDepositPaidAmount, BigDecimal.ZERO) > 0) {
                        statementOrderDetailDO.setStatementDetailDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositReturnAmount(), needReturnStatementDepositPaidAmount));
                        statementOrderDetailDO.setStatementDetailDepositReturnTime(now);
                        statementOrderDO.setStatementDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositReturnAmount(), needReturnStatementDepositPaidAmount));
                        statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, needReturnStatementDepositPaidAmount, now, userSupport.getCurrentUser().getUserId(), now);

                        isDepositReturn = true;
                        orderDetailTotalPaid = BigDecimalUtil.add(orderDetailTotalPaid, needReturnStatementDepositPaidAmount);
                        totalDepositPaidAmount = BigDecimalUtil.add(totalDepositPaidAmount, needReturnStatementDepositPaidAmount);
                    }
                    if (BigDecimalUtil.compare(needReturnStatementRentDepositPaidAmount, BigDecimal.ZERO) > 0) {
                        statementOrderDetailDO.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositReturnAmount(), needReturnStatementRentDepositPaidAmount));
                        statementOrderDetailDO.setStatementDetailRentDepositReturnTime(now);
                        statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositReturnAmount(), needReturnStatementRentDepositPaidAmount));
                        statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_RENT_DEPOSIT, needReturnStatementRentDepositPaidAmount, now, userSupport.getCurrentUser().getUserId(), now);

                        isRentDepositReturn = true;
                        orderDetailTotalPaid = BigDecimalUtil.add(orderDetailTotalPaid, needReturnStatementRentDepositPaidAmount);
                        totalDepositRentPaidAmount = BigDecimalUtil.add(totalDepositRentPaidAmount, needReturnStatementRentDepositPaidAmount);
                    }
                    if (isDepositReturn || isRentDepositReturn) {
                        statementOrderDetailMapper.update(statementOrderDetailDO);
                        statementOrderMapper.update(statementOrderDO);
                    }
                    //退押金end````````````


                    //修改结算单金额
                    statementOrderDO.setStatementPaidAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementPaidAmount(), BigDecimalUtil.STANDARD_SCALE), orderDetailTotalPaid));
                    statementOrderDO.setStatementDepositPaidAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementDepositPaidAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementOtherPaidAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementOtherPaidAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementRentDepositPaidAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementOverduePaidAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementOverduePaidAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailOverduePaidAmount(), BigDecimalUtil.STANDARD_SCALE)));
                    statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.sub(BigDecimalUtil.roundNullReturnZero(statementOrderDO.getStatementRentPaidAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.roundNullReturnZero(statementOrderDetailDO.getStatementDetailRentPaidAmount(), BigDecimalUtil.STANDARD_SCALE)));


                    if (BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(), BigDecimal.ZERO) < 0
                            || BigDecimalUtil.compare(statementOrderDO.getStatementDepositPaidAmount(), BigDecimal.ZERO) < 0
                            || BigDecimalUtil.compare(statementOrderDO.getStatementOtherPaidAmount(), BigDecimal.ZERO) < 0
                            || BigDecimalUtil.compare(statementOrderDO.getStatementRentDepositPaidAmount(), BigDecimal.ZERO) < 0
                            || BigDecimalUtil.compare(statementOrderDO.getStatementOverduePaidAmount(), BigDecimal.ZERO) < 0
                            || BigDecimalUtil.compare(statementOrderDO.getStatementRentPaidAmount(), BigDecimal.ZERO) < 0
                            || BigDecimalUtil.compare(statementOrderDO.getStatementPenaltyPaidAmount(), BigDecimal.ZERO) < 0) {
                        result.setErrorCode(ErrorCode.STATEMENT_PAY_AMOUNT_ERROR);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return result;
                    }
                }
                //todo 后续增加冲正判断
                if (BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(), BigDecimal.ZERO) <= 0)
                    statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
//                if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) <= 0)
//                    statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
                statementOrderDO.setUpdateUser(userId);
                statementOrderDO.setUpdateTime(now);
                statementOrderMapper.update(statementOrderDO);
            }
        }
        //返还已支付金额
        if (BigDecimalUtil.compare(needReturnAmount, BigDecimal.ZERO) > 0) {
            ManualChargeParam manualChargeParam = new ManualChargeParam();
            manualChargeParam.setBusinessCustomerNo(customerDO.getCustomerNo());
            manualChargeParam.setChargeAmount(needReturnAmount);
            manualChargeParam.setChargeRemark("订单重新结算退还金");
            ServiceResult<String, Boolean> payResult = paymentService.manualCharge(manualChargeParam);
            if (!ErrorCode.SUCCESS.equals(payResult.getErrorCode()) || !payResult.getResult()) {
                result.setErrorCode(ErrorCode.RE_STATEMENT_BACK_AMOUNT_FAIL);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return result;
            }
        }
        if (!(BigDecimalUtil.compare(totalDepositPaidAmount, BigDecimal.ZERO) <= 0 && BigDecimalUtil.compare(totalDepositRentPaidAmount, BigDecimal.ZERO) <= 0)) {
            ServiceResult<String, Boolean> payResult = paymentService.returnDeposit(customerDO.getCustomerNo(), totalDepositRentPaidAmount, totalDepositPaidAmount);
            if (!ErrorCode.SUCCESS.equals(payResult.getErrorCode()) || !payResult.getResult()) {
                result.setErrorCode(ErrorCode.RE_STATEMENT_BACK_AMOUNT_FAIL);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return result;
            }
        }
        //失效订单关联结算
        statementOrderDetailMapper.deleteByOrderId(orderDO.getId(), userId);

        //恢复订单状态为未支付
        if (PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())) {
            orderDO.setPayStatus(PayStatus.PAY_STATUS_INIT);
        }
        orderDO.setTotalPaidOrderAmount(BigDecimalUtil.sub(orderDO.getTotalPaidOrderAmount(), needReturnAmount));
        orderDO.setPayTime(now);
        orderMapper.update(orderDO);
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), OrderStatus.ORDER_STATUS_PAID, null, now, userSupport.getCurrentUser().getUserId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 清除订单的结算单信息
     *
     * @param orderDO
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, String> clearStatementOrder(OrderDO orderDO) {
        ServiceResult<String, String> result = new ServiceResult<>();
//        if(PayStatus.PAY_STATUS_PAYING.equals(orderDO.getPayStatus())||PayStatus.PAY_STATUS_PAID_PART.equals(orderDO.getPayStatus())||
//                PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())){
//            result.setErrorCode(ErrorCode.ORDER_ALREADY_PAID);
//            return result;
//        }
//        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByOrderNo(orderDO.getOrderNo());
//        if(k3ReturnOrderDetailDOList!=null&&k3ReturnOrderDetailDOList.size()>0){
//            result.setErrorCode(ErrorCode.HAS_RETURN_ORDER);
//            return result;
//        }
        statementOrderSupport.reStatement(orderDO,new Date());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createReletOrderStatement(ReletOrderDO reletOrderDO) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        //ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrderNo);
        if (reletOrderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        List<StatementOrderDetailDO> dbStatementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(reletOrderDO.getOrderId());
        if (CollectionUtil.isNotEmpty(dbStatementOrderDetailDOList)) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_CREATE_ERROR);
            return result;
        }
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        Date rentStartTime = reletOrderDO.getRentStartTime();


        CustomerDO customerDO = customerMapper.findById(reletOrderDO.getBuyerCustomerId());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        //统一拿订单结算日
        Integer statementDays = reletOrderDO.getStatementDate();
        if (statementDays == null) {
            statementDays = statementOrderSupport.getCustomerStatementDate(customerDO.getStatementDate(), rentStartTime);
        }
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = generateReletStatementDetailList(reletOrderDO, currentTime, statementDays, loginUser.getUserId());
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUser.getUserId());

        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                // 核算本次应该交多少钱
                if (DateUtil.isSameDay(rentStartTime, statementOrderDetailDO.getStatementExpectPayTime())) {
                    thisNeedPayAmount = BigDecimalUtil.add(thisNeedPayAmount, statementOrderDetailDO.getStatementDetailAmount().setScale(BigDecimalUtil.STANDARD_SCALE, BigDecimal.ROUND_HALF_UP));
                }
            }
        }

        result.setResult(thisNeedPayAmount);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    private List<StatementOrderDetailDO> generateReletStatementDetailList(ReletOrderDO reletOrderDO, Date currentTime, Integer statementDays, Integer loginUserId) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Date rentStartTime = reletOrderDO.getRentStartTime();
        Integer buyerCustomerId = reletOrderDO.getBuyerCustomerId();
        Integer orderId = reletOrderDO.getOrderId();

        // 商品生成结算单
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                BigDecimal itemAllAmount = reletOrderProductDO.getProductAmount();
//                // 如果是K3订单，那么数量就要为在租数
//                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
//                    orderProductDO.setProductCount(reletOrderProductDO.getRentingProductCount());
//                    itemAllAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(orderProductDO.getRentingProductCount()), orderProductDO.getProductUnitAmount(), BigDecimalUtil.STANDARD_SCALE), new BigDecimal(orderProductDO.getRentTimeLength()), BigDecimalUtil.STANDARD_SCALE);
//                }

                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);

                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderProductDO.getPayMode(), rentStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getId(), currentTime, loginUserId);
                    if (statementOrderDetailDO != null) {
                        statementOrderDetailDO.setItemName(reletOrderProductDO.getProductName() + reletOrderProductDO.getProductSkuName());
                        statementOrderDetailDO.setItemIsNew(reletOrderProductDO.getIsNewProduct());
                        statementOrderDetailDO.setStatementDetailPhase(statementMonthCount);
                        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                        //添加优惠券抵扣金额
                        ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
                        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                            statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
                        }
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    }
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;

                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), statementDays, reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getPayMode(), rentStartTime, reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getId(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(reletOrderProductDO.getProductName() + reletOrderProductDO.getProductSkuName());
                                statementOrderDetailDO.setItemIsNew(reletOrderProductDO.getIsNewProduct());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                //添加优惠券抵扣金额
                                ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
                                if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                                    statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
                                }
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                            }
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getId(), lastCalculateDate, rentStartTime, reletOrderProductDO.getPayMode(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(reletOrderProductDO.getProductName() + reletOrderProductDO.getProductSkuName());
                                statementOrderDetailDO.setItemIsNew(reletOrderProductDO.getIsNewProduct());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                //添加优惠券抵扣金额
                                ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
                                if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                                    statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
                                }
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            }
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(reletOrderDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getId(), lastCalculateDate, reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getProductCount(), statementDays, reletOrderProductDO.getPayMode(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(reletOrderProductDO.getProductName() + reletOrderProductDO.getProductSkuName());
                                statementOrderDetailDO.setItemIsNew(reletOrderProductDO.getIsNewProduct());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                //添加优惠券抵扣金额
                                ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
                                if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                                    statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
                                }
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
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList()) {
//                // 如果是K3订单，那么数量就要为在租数
//                if (CommonConstant.COMMON_CONSTANT_YES.equals(reletOrderDO.getIsK3Order())) {
//                    orderMaterialDO.setMaterialCount(orderMaterialDO.getRentingMaterialCount());
//                }
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);

                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderMaterialDO.getPayMode(), rentStartTime, reletOrderMaterialDO.getMaterialAmount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getId(), currentTime, loginUserId);
                    if (statementOrderDetailDO != null) {
                        statementOrderDetailDO.setItemName(reletOrderMaterialDO.getMaterialName());
                        statementOrderDetailDO.setItemIsNew(reletOrderMaterialDO.getIsNewMaterial());
                        statementOrderDetailDO.setStatementDetailPhase(statementMonthCount);
                        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    }
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    BigDecimal itemAllAmount = reletOrderMaterialDO.getMaterialAmount();
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), statementDays, reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getPayMode(), rentStartTime, reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getId(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(reletOrderMaterialDO.getMaterialName());
                                statementOrderDetailDO.setItemIsNew(reletOrderMaterialDO.getIsNewMaterial());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                            }
                        } else if (statementMonthCount == i) {
                            // 最后一期
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getId(), lastCalculateDate, rentStartTime, reletOrderMaterialDO.getPayMode(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(reletOrderMaterialDO.getMaterialName());
                                statementOrderDetailDO.setItemIsNew(reletOrderMaterialDO.getIsNewMaterial());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            }
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(reletOrderDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getId(), lastCalculateDate, reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getMaterialCount(), statementDays, reletOrderMaterialDO.getPayMode(), currentTime, loginUserId);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(reletOrderMaterialDO.getMaterialName());
                                statementOrderDetailDO.setItemIsNew(reletOrderMaterialDO.getIsNewMaterial());
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

    @Autowired
    private StatementPayOrderMapper statementPayOrderMapper;

    @Autowired
    private StatementOrderCorrectMapper statementOrderCorrectMapper;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private StatementOrderCorrectDetailMapper statementOrderCorrectDetailMapper;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private K3ChangeOrderMapper k3ChangeOrderMapper;
    @Autowired
    private K3ChangeOrderDetailMapper k3ChangeOrderDetailMapper;

    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;

    @Autowired
    private StatementOrderSupport statementOrderSupport;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private CouponSupport couponSupport;
    @Autowired
    private PenaltySupport penaltySupport;

    @Autowired
    private ReletOrderMapper reletOrderMapper;

}
