package com.lxzl.erp.core.service.statement.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.callback.WeixinPayCallbackParam;
import com.lxzl.erp.common.domain.export.FinanceStatementOrderPayDetail;
import com.lxzl.erp.common.domain.k3.pojo.OrderStatementDateSplit;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.domain.payment.account.pojo.PayResult;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.statement.*;
import com.lxzl.erp.common.domain.statement.pojo.CheckStatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.CheckStatementOrderDetail;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.coupon.impl.support.CouponSupport;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.order.impl.support.OrderSupport;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.core.service.statement.impl.support.StatementPaySupport;
import com.lxzl.erp.core.service.statement.impl.support.StatementReturnSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderProductMapper;
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
import com.lxzl.erp.dataaccess.domain.k3.K3OrderStatementConfigDO;
import com.lxzl.erp.dataaccess.domain.k3.OrderStatementDateSplitDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderProductDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderMaterialBulkDO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.statement.*;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDetailDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import com.lxzl.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderDO.getStatementDate(), rentStartTime);
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, statementDays, loginUserId);

        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        Map<String, BigDecimal> map = new HashMap();
        if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                // 核算本次应该交多少钱
                if (DateUtil.isSameDay(rentStartTime, statementOrderDetailDO.getStatementExpectPayTime())) {
                    thisNeedPayAmount = BigDecimalUtil.add(thisNeedPayAmount, statementOrderDetailDO.getStatementDetailAmount().setScale(BigDecimalUtil.STANDARD_SCALE, BigDecimal.ROUND_HALF_UP));

                    if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                        String key = statementOrderDetailDO.getItemName() + "-" + statementOrderDetailDO.getItemIsNew() + "-" + statementOrderDetailDO.getOrderItemReferId() + "-" + statementOrderDetailDO.getOrderId() + "-" + statementOrderDetailDO.getSerialNumber();
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createOrderStatement(String orderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        //保存初始订单结算日日志
        statementOrderSupport.recordStatementDateLog(orderNo, orderDO.getStatementDate());
        return createOrderStatement(orderDO, false);
    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, BigDecimal> createOrderStatement(OrderDO orderDO, boolean allowHasPartStatement) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        //部分重算会导致已有部分结算信息（部分重算中允许通过）
        if (!allowHasPartStatement) {
            List<StatementOrderDetailDO> dbStatementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
            if (CollectionUtil.isNotEmpty(dbStatementOrderDetailDOList)) {
                result.setErrorCode(ErrorCode.STATEMENT_ORDER_CREATE_ERROR);
                return result;
            }
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
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = getStatementOrderDetailDOS(orderDO.getOrderNo(), orderDO, currentTime, rentStartTime, loginUserId);

        List<StatementOrderDetailDO> finalAddStatementOrderDetailDOList = new ArrayList<>();

        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
            K3OrderStatementConfigDO k3OrderStatementConfigDO = k3OrderStatementConfigMapper.findByOrderId(orderDO.getId());
            if (k3OrderStatementConfigDO != null && k3OrderStatementConfigDO.getRentStartTime() != null) {
                Date k3RentStartTime = k3OrderStatementConfigDO.getRentStartTime();
                for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                    //如果结算结束时间大于等于k3配置起租时间，则保存该结算单
                    if (statementOrderDetailDO.getStatementEndTime().getTime() - k3RentStartTime.getTime() >= 0) {
                        finalAddStatementOrderDetailDOList.add(statementOrderDetailDO);
                    }
                }
            } else {
                finalAddStatementOrderDetailDOList = addStatementOrderDetailDOList;
            }
        } else {
            finalAddStatementOrderDetailDOList = addStatementOrderDetailDOList;
        }
        saveStatementOrder(finalAddStatementOrderDetailDOList, currentTime, loginUserId);

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

    private List<StatementOrderDetailDO> getStatementOrderDetailDOS(String orderNo, OrderDO orderDO, Date currentTime, Date rentStartTime, Integer loginUserId) {
        //结算类型分界
        OrderStatementDateSplitDO orderStatementDateSplitDO = orderStatementDateSplitMapper.findByOrderNo(orderNo);
        //是否订单在租期间改变结算日（前后两部分结算日不一样）
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        //Date expectReturnTime=orderDO.getExpectReturnTime();
        //考虑到续租会覆盖原订单的归还时间
        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);

        boolean isStatementDateChangeInOrderRentTime = orderStatementDateSplitDO != null && orderStatementDateSplitDO.getAfterStatementDate() != null && orderStatementDateSplitDO.getBeforeStatementDate() != null && !orderStatementDateSplitDO.getBeforeStatementDate().equals(orderStatementDateSplitDO.getAfterStatementDate()) && orderStatementDateSplitDO.getStatementDateChangeTime().compareTo(orderDO.getRentStartTime()) > 0 && orderStatementDateSplitDO.getStatementDateChangeTime().compareTo(expectReturnTime) < 0;
        if (isStatementDateChangeInOrderRentTime) {
            //获取分段点
            addStatementOrderDetailDOList = generateStatementDetailListSplit(orderDO, currentTime, loginUserId, orderStatementDateSplitDO);
//            Date statementDateChangeTime=getStatementChangeDate(rentStartTime,orderStatementDateSplitDO);
//
//            Integer beforeStatementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getBeforeStatementDate(), rentStartTime);
//            orderDO.setRentStartTime(rentStartTime);
//            orderDO.setExpectReturnTime(DateUtil.getDayByOffset(statementDateChangeTime,-1));
//
//            double percent=getOrderPercent(orderDO.getRentTimeLength(),orderDO.getRentType(), rentStartTime, orderStatementDateSplitDO, expectReturnTime, statementDateChangeTime);
//
//            Integer rentLenth=orderDO.getRentTimeLength();
//            int beforeLength=(int)(rentLenth*percent/CommonConstant.PROPORTION_MAX);
//            orderDO.setRentTimeLength(beforeLength);
//            orderDO.setStatementDate(orderStatementDateSplitDO.getBeforeStatementDate());
//            List<StatementOrderDetailDO> beforeStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, beforeStatementDays, loginUserId,percent);
//            //补全最后一期结算时间
//            List<StatementOrderDetailDO> lastPhaseList = getLastPhaseStatementOrderDetailDOS(beforeStatementOrderDetailDOList);
//            for (StatementOrderDetailDO statementOrderDetailDO:lastPhaseList) statementOrderDetailDO.setStatementEndTime(DateUtil.getDayByOffset(statementDateChangeTime,-1));
//
//            orderDO.setRentStartTime(statementDateChangeTime);
//            orderDO.setExpectReturnTime(expectReturnTime);
//            Integer afterStatementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getAfterStatementDate(),statementDateChangeTime);
//            orderDO.setRentTimeLength(rentLenth-beforeLength);
//
//            List<StatementOrderDetailDO> afterStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, afterStatementDays, loginUserId,CommonConstant.PROPORTION_MAX-percent);
//            lastPhaseList = getLastPhaseStatementOrderDetailDOS(afterStatementOrderDetailDOList);
//            for(StatementOrderDetailDO statementOrderDetailDO:lastPhaseList)statementOrderDetailDO.setStatementEndTime(expectReturnTime);
//
//            //过滤第二部分重算的押金
//            if(CollectionUtil.isNotEmpty(beforeStatementOrderDetailDOList))addStatementOrderDetailDOList.addAll(beforeStatementOrderDetailDOList);
//            if(CollectionUtil.isNotEmpty(afterStatementOrderDetailDOList))
//                for (StatementOrderDetailDO statementOrderDetailDO:afterStatementOrderDetailDOList){
//                   if(!StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType()))addStatementOrderDetailDOList.add(statementOrderDetailDO);
//            }
        } else {
            //已结算部分(不在节点内的订单或续租结算要么全清，要么全保留)
            List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderTypeAndId(OrderType.ORDER_TYPE_ORDER, orderDO.getId());
            Date lstPaidStatementEndTime = null;
            for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOList) {
                if (lstPaidStatementEndTime == null || lstPaidStatementEndTime.compareTo(orderDetailDO.getStatementEndTime()) < 0)
                    lstPaidStatementEndTime = orderDetailDO.getStatementEndTime();
            }
            //判断当前订单结算是否已全部生成
            if (lstPaidStatementEndTime != null && DateUtil.daysBetween(lstPaidStatementEndTime, expectReturnTime) <= 0)
                return new ArrayList<>();

            //如果结算日配置存在则以配置为准（不是用原订单结算日）
            if (orderStatementDateSplitDO != null) {
                if (orderStatementDateSplitDO.getStatementDateChangeTime().compareTo(orderDO.getExpectReturnTime()) >= 0)
                    orderDO.setStatementDate(orderStatementDateSplitDO.getBeforeStatementDate());
                if (orderStatementDateSplitDO.getStatementDateChangeTime().compareTo(orderDO.getRentStartTime()) <= 0)
                    orderDO.setStatementDate(orderStatementDateSplitDO.getAfterStatementDate());
            }
            //统一拿订单结算日
            Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderDO.getStatementDate(), rentStartTime);
            addStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, statementDays, loginUserId);
        }
        return addStatementOrderDetailDOList;
    }

    private List<StatementOrderDetailDO> getLastPhaseStatementOrderDetailDOS(List<StatementOrderDetailDO> beforeStatementOrderDetailDOList) {
        List<StatementOrderDetailDO> lastPhaseList = new ArrayList<>();
        Integer maxPhase = 1;
        for (StatementOrderDetailDO statementOrderDetailDO : beforeStatementOrderDetailDOList) {

            if (statementOrderDetailDO.getStatementDetailPhase().equals(maxPhase))
                lastPhaseList.add(statementOrderDetailDO);
            else if (statementOrderDetailDO.getStatementDetailPhase() > maxPhase) {
                lastPhaseList.clear();
                lastPhaseList.add(statementOrderDetailDO);
            }

        }
        return lastPhaseList;
    }

    private List<StatementOrderDetailDO> getStatementReletOrderDetailDOS(ReletOrderDO reletOrderDO, Date currentTime, Date rentStartTime, Integer loginUserId) {
        //结算类型分界
        OrderStatementDateSplitDO orderStatementDateSplitDO = orderStatementDateSplitMapper.findByOrderNo(reletOrderDO.getOrderNo());
        //是否订单在租期间改变结算日（前后两部分结算日不一样）
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        //Date expectReturnTime=orderDO.getExpectReturnTime();
        //考虑到续租会覆盖原订单的归还时间
        Date expectReturnTime = reletOrderDO.getExpectReturnTime();

        boolean isStatementDateChangeInOrderRentTime = orderStatementDateSplitDO != null && orderStatementDateSplitDO.getAfterStatementDate() != null && orderStatementDateSplitDO.getBeforeStatementDate() != null && !orderStatementDateSplitDO.getBeforeStatementDate().equals(orderStatementDateSplitDO.getAfterStatementDate()) && orderStatementDateSplitDO.getStatementDateChangeTime().compareTo(reletOrderDO.getRentStartTime()) > 0 && orderStatementDateSplitDO.getStatementDateChangeTime().compareTo(expectReturnTime) < 0;
        if (isStatementDateChangeInOrderRentTime) {
            //获取分段点
            addStatementOrderDetailDOList = generateReletStatementDetailListSplit(reletOrderDO, currentTime, loginUserId, orderStatementDateSplitDO);
        } else {
            //已结算部分(不在节点内的订单或续租结算要么全清，要么全保留)
            List<StatementOrderDetailDO> statementOrderDetailDOList = getReletOrderStatementDetails(reletOrderDO);

            Date lstPaidStatementEndTime = null;
            for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOList) {
                if (lstPaidStatementEndTime == null || lstPaidStatementEndTime.compareTo(orderDetailDO.getStatementEndTime()) < 0)
                    lstPaidStatementEndTime = orderDetailDO.getStatementEndTime();
            }
            //判断当前订单结算是否已全部生成
            if (lstPaidStatementEndTime != null && DateUtil.daysBetween(lstPaidStatementEndTime, expectReturnTime) <= 0)
                return new ArrayList<>();

            //如果结算日配置存在则以配置为准（不是用原订单结算日）
            if (orderStatementDateSplitDO != null) {
                if (orderStatementDateSplitDO.getStatementDateChangeTime().compareTo(reletOrderDO.getExpectReturnTime()) >= 0)
                    reletOrderDO.setStatementDate(orderStatementDateSplitDO.getBeforeStatementDate());
                if (orderStatementDateSplitDO.getStatementDateChangeTime().compareTo(reletOrderDO.getRentStartTime()) <= 0)
                    reletOrderDO.setStatementDate(orderStatementDateSplitDO.getAfterStatementDate());
            }
            //统一拿订单结算日
            Integer statementDays = statementOrderSupport.getCustomerStatementDate(reletOrderDO.getStatementDate(), rentStartTime);
            addStatementOrderDetailDOList = generateReletStatementDetailList(reletOrderDO, currentTime, statementDays, loginUserId);
        }
        return addStatementOrderDetailDOList;
    }

    private List<StatementOrderDetailDO> getReletOrderStatementDetails(ReletOrderDO reletOrderDO) {
        List<StatementOrderDetailDO> statementOrderDetailDOList = new ArrayList<>();
        List<Integer> reletItemReferIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList())
                reletItemReferIds.add(reletOrderProductDO.getId());
            List<StatementOrderDetailDO> productStatementOrderDetailDOList = statementOrderDetailMapper.findProductRentByReletOrderItemReferIds(reletItemReferIds);
            if (CollectionUtil.isNotEmpty(productStatementOrderDetailDOList))
                statementOrderDetailDOList.addAll(productStatementOrderDetailDOList);
        }

        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            reletItemReferIds.clear();
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderDO.getReletOrderMaterialDOList())
                reletItemReferIds.add(reletOrderMaterialDO.getId());
            List<StatementOrderDetailDO> materialStatementOrderDetailDOList = statementOrderDetailMapper.findMaterialRentByReletOrderItemReferIds(reletItemReferIds);
            if (CollectionUtil.isNotEmpty(materialStatementOrderDetailDOList))
                statementOrderDetailDOList.addAll(materialStatementOrderDetailDOList);
        }
        return statementOrderDetailDOList;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
            K3OrderStatementConfigDO k3OrderStatementConfigDO = k3OrderStatementConfigMapper.findByOrderId(orderDO.getId());
            if (k3OrderStatementConfigDO != null && k3OrderStatementConfigDO.getRentStartTime() != null) {
                rentStartTime = k3OrderStatementConfigDO.getRentStartTime();
            }
        }
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
        Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderDO.getStatementDate(), rentStartTime);
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = generateStatementDetailList(orderDO, currentTime, statementDays, loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId());
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> reCreateOrderStatement(String orderNo) {
        return reCreateOrderStatement(orderNo, null);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> reCreateOrderStatement(OrderStatementDateSplit k3StatementDateChange) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(k3StatementDateChange.getOrderNo());
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
            result.setErrorCode(ErrorCode.ONLY_RENT_TYPE_MONTH_NEED_SPLIT_STATEMENT);
            return result;
        }
        if (k3StatementDateChange.getBeforeStatementDate().equals(k3StatementDateChange.getAfterStatementDate())) {
            result.setErrorCode(ErrorCode.SPLIT_STATEMENT_CAN_NOT_SAME, getStatementModeString(k3StatementDateChange.getBeforeStatementDate()), getStatementModeString(k3StatementDateChange.getAfterStatementDate()));
            return result;
        }
        if (DateUtil.daysBetween(orderDO.getExpectReturnTime(), k3StatementDateChange.getStatementDateChangeTime()) >= 0 || DateUtil.daysBetween(orderDO.getRentStartTime(), k3StatementDateChange.getStatementDateChangeTime()) <= 0) {
            result.setErrorCode(ErrorCode.SPLIT_STATEMENT_TIME_ERROR);
            return result;
        }
        OrderStatementDateSplitDO orderStatementDateSplitDO = orderStatementDateSplitMapper.findByOrderNo(k3StatementDateChange.getOrderNo());
        Date currentTime = new Date();
        String userId = userSupport.getCurrentUserId().toString();
        if (orderStatementDateSplitDO != null) {
//            orderStatementDateSplitDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
//            orderStatementDateSplitDO.setUpdateUser(userId);
//            orderStatementDateSplitDO.setUpdateTime(currentTime);
//            orderStatementDateSplitMapper.update(orderStatementDateSplitDO);
            result.setErrorCode(ErrorCode.HAS_SPLIT_STATEMENT_CFG);
            return result;
        }
        OrderStatementDateSplitDO addStatementSplit = ConverterUtil.convert(k3StatementDateChange, OrderStatementDateSplitDO.class);
        addStatementSplit.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        addStatementSplit.setCreateUser(userId);
        addStatementSplit.setCreateTime(currentTime);
        addStatementSplit.setUpdateUser(userId);
        addStatementSplit.setUpdateTime(currentTime);
        orderStatementDateSplitMapper.save(addStatementSplit);

        ServiceResult<String, BigDecimal> serviceResult = reCreateOrderStatement(k3StatementDateChange.getOrderNo(), null, false,false);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            result.setErrorCode(serviceResult.getErrorCode(), serviceResult.getFormatArgs());
            return result;
        }
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> reCreateOrderStatement(String orderNo, Integer statementDate) {
        return reCreateOrderStatement(orderNo, statementDate, true,false);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> reCreateOrderStatementAllowConfirmCustommer(OrderDO orderDO) {
        return reCreateOrderStatement(orderDO, null, true,true);
    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, BigDecimal> reCreateOrderStatement(String orderNo, Integer statementDate, boolean clearStatementDateSplitCfg,boolean allowConfirmCustomer){
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
        return reCreateOrderStatement(orderDO,statementDate,clearStatementDateSplitCfg,allowConfirmCustomer);
    }
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, BigDecimal> reCreateOrderStatement(OrderDO orderDO, Integer statementDate, boolean clearStatementDateSplitCfg,boolean allowConfirmCustomer) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        if(orderDO==null){
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!Arrays.asList(OrderStatus.ORDER_STATUS_WAIT_DELIVERY, OrderStatus.ORDER_STATUS_PROCESSING, OrderStatus.ORDER_STATUS_DELIVERED, OrderStatus.ORDER_STATUS_CONFIRM, OrderStatus.ORDER_STATUS_PART_RETURN, OrderStatus.ORDER_STATUS_RETURN_BACK).contains(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_NOT_ALLOW_RE_STATEMEMT);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(orderDO.getBuyerCustomerNo());
        if(customerDO==null){
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;

        }
        // 客户为确认结算单状态时，不允许重算客户的订单
        if(!allowConfirmCustomer){
            if (ConfirmStatementStatus.CONFIRM_STATUS_YES.equals(customerDO.getConfirmStatementStatus())) {
                result.setErrorCode(ErrorCode.CUSTOMER_CONFIRM_STATEMENT_REFUSE_RECREATE);
                return result;
            }
        }

        //用户手动修改结算日
        if (statementDate != null) {
            if (!Arrays.asList(StatementMode.STATEMENT_MONTH_END, StatementMode.STATEMENT_20, StatementMode.STATEMENT_MONTH_NATURAL).contains(statementDate)) {
                result.setErrorCode(ErrorCode.STATEMENT_DATE_NOT_SUPPORT, statementDate.toString());
                return result;
            }
            orderDO.setStatementDate(statementDate);
            orderMapper.update(orderDO);
            statementOrderSupport.recordStatementDateLog(orderDO.getOrderNo(), statementDate);
        }
        //首先清除退货结算(有实际退货的)
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByOrderNo(orderDO.getOrderNo());
        clearReturnReturnOrderItems(k3ReturnOrderDetailDOList, clearStatementDateSplitCfg);

        ServiceResult<String, AmountNeedReturn> clearResult = clearStatementOrder(orderDO, clearStatementDateSplitCfg);
        if (!ErrorCode.SUCCESS.equals(clearResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            result.setErrorCode(clearResult.getErrorCode(), clearResult.getFormatArgs());
            return result;
        }
        ServiceResult<String, BigDecimal> createResult = createOrderStatement(orderDO, true);
        //创建失败回滚
        if (!ErrorCode.SUCCESS.equals(createResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return createResult;
        }

        //处理续租单重算
        List<ReletOrderDO> reletOrderDOList = reletOrderMapper.findReletedOrdersByOrderId(orderDO.getId());
        if (CollectionUtil.isNotEmpty(reletOrderDOList)) {
            for (ReletOrderDO reletOrderDO : reletOrderDOList) {
                //从订单同步更新续租单的结算类型和支付类型
                syncReletOrderStatementByOrder(orderDO, reletOrderDO);
                //旧结算信息已在前面清除这里不需再次处理
                ServiceResult<String, BigDecimal> reletService = reletOrderStatement(reletOrderDO, true);
                if (!ErrorCode.SUCCESS.equals(reletService.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    result.setErrorCode(ErrorCode.RELET_ORDER_RE_STATEMENT_FAIL, reletOrderDO.getReletOrderNo());
                    return reletService;
                }
            }
        }

        //退货重新结算到具体结算单
        statementReturnOrderItemRent(k3ReturnOrderDetailDOList, true);

        //修正结算单时间范围
        fixCustomerStatementOrderStatementTime(customerDO.getId());


        //更新订单首次需支付金额(首期为零是有支付的分段重算产生，首期已支付，所以为零)
        if (BigDecimalUtil.compare(createResult.getResult(), BigDecimal.ZERO) != 0) {
            orderDO.setFirstNeedPayAmount(createResult.getResult());
            orderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            orderDO.setUpdateTime(new Date());
            orderMapper.update(orderDO);
        }

        //资金最后退款（保证原子性）
        AmountNeedReturn amountNeedReturn = clearResult.getResult();
        if (amountNeedReturn != null && (BigDecimalUtil.compare(amountNeedReturn.getRentPaidAmount(), BigDecimal.ZERO) != 0 || BigDecimalUtil.compare(amountNeedReturn.getRentDepositPaidAmount(), BigDecimal.ZERO) != 0 || BigDecimalUtil.compare(amountNeedReturn.getDepositPaidAmount(), BigDecimal.ZERO) != 0 || BigDecimalUtil.compare(BigDecimalUtil.addAll(amountNeedReturn.getOtherPaidAmount(), amountNeedReturn.getOverduePaidAmount(), amountNeedReturn.getPenaltyPaidAmount()), BigDecimal.ZERO) != 0)) {
            String returnCode = paymentService.returnDepositExpand(customerDO.getCustomerNo(), amountNeedReturn.getRentPaidAmount(), BigDecimalUtil.addAll(amountNeedReturn.getOtherPaidAmount(), amountNeedReturn.getOverduePaidAmount(), amountNeedReturn.getPenaltyPaidAmount())
                    , amountNeedReturn.getRentDepositPaidAmount(), amountNeedReturn.getDepositPaidAmount(), "订单【" + orderDO.getOrderNo() + "】重算结算单，已支付金额退还到客户余额");
            if (!ErrorCode.SUCCESS.equals(returnCode)) {
                result.setErrorCode(returnCode);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return result;
            }
            //发送钉钉消息
            statementOrderSupport.sendOrderRestatementSuccess(orderDO);
        }
        return createResult;
    }

    /**
     * 修正当前客户下所有结算单的开始结束时间
     *
     * @param customerId
     */
    private void fixCustomerStatementOrderStatementTime(Integer customerId) {
        if (customerId == null) return;
        List<StatementOrderDO> customerStatementOrderList = statementOrderMapper.findByCustomerId(customerId);
        if (CollectionUtil.isEmpty(customerStatementOrderList)) return;
        for (StatementOrderDO statementOrderDO : customerStatementOrderList) {
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
    }

    /**
     * 仅同步订单：结算类型，支付类型
     *
     * @param orderDO
     * @param reletOrderDO
     */
    private void syncReletOrderStatementByOrder(OrderDO orderDO, ReletOrderDO reletOrderDO) {
        //同步订单结算类型
        Integer orderStatementDate = orderDO.getStatementDate();
        if (orderStatementDate == null) orderStatementDate = StatementMode.STATEMENT_MONTH_END;
        Integer reletOrderStatementDate = reletOrderDO.getStatementDate();
        if (reletOrderStatementDate == null) reletOrderStatementDate = StatementMode.STATEMENT_MONTH_END;
        if (!orderStatementDate.equals(reletOrderStatementDate)) {
            reletOrderDO.setStatementDate(orderDO.getStatementDate());
            reletOrderMapper.update(reletOrderDO);
        }
        //同步订单项支付方式（商品项）
        Map<Integer, OrderProductDO> orderProductCatch = new HashMap<>();
        for (OrderProductDO productDO : orderDO.getOrderProductDOList()) {
            orderProductCatch.put(productDO.getId(), productDO);
        }
        for (ReletOrderProductDO productDO : reletOrderDO.getReletOrderProductDOList()) {
            OrderProductDO orderProductDO = orderProductCatch.get(productDO.getOrderProductId());
            if (orderProductDO == null) continue;
            if (orderProductDO.getPayMode().equals(productDO.getPayMode())) continue;
            productDO.setPayMode(orderProductDO.getPayMode());
            reletOrderProductMapper.update(productDO);
        }
        //同步订单项支付方式（物料项）
        Map<Integer, OrderMaterialDO> orderMaterialCatch = new HashMap<>();
        for (OrderMaterialDO materialDO : orderDO.getOrderMaterialDOList()) {
            orderMaterialCatch.put(materialDO.getId(), materialDO);
        }
        for (ReletOrderMaterialDO materialDO : reletOrderDO.getReletOrderMaterialDOList()) {
            OrderMaterialDO orderMaterialDO = orderMaterialCatch.get(materialDO.getOrderMaterialId());
            if (orderMaterialDO == null) continue;
            if (orderMaterialDO.getPayMode().equals(materialDO.getPayMode())) continue;
            materialDO.setPayMode(orderMaterialDO.getPayMode());
            reletOrderMaterialMapper.update(materialDO);
        }
    }

    @Override
    public String batchReCreateOrderStatement(List<String> orderNoList, List<String> customerNoList) {

        StringBuffer sb = new StringBuffer();
        Set<String> orderNoSet = new HashSet<>();
        for (String orderNo : orderNoList) {
            orderNoSet.add(orderNo);
        }
//        待发货、已发货、确认收货，部分归还，全部归还，完成 状态可重算
        if (CollectionUtil.isNotEmpty(customerNoList)) {
            for (String customerNo : customerNoList) {
                if (StringUtil.isEmpty(customerNo)) {
                    continue;
                }
                CustomerDO customerDO = customerMapper.findByNo(customerNo);
                if (customerDO == null) {
                    sb.append("客户编号[" + customerNo + "]不存在\n");
                    continue;
                }
                if (ConfirmStatementStatus.CONFIRM_STATUS_YES.equals(customerDO.getConfirmStatementStatus())) {
                    sb.append("客户编号[" + customerNo + "]已经是确认结算单状态\n");
                    continue;
                }
                List<OrderDO> orderDOList = orderMapper.findByCustomerId(customerDO.getId());
                for (OrderDO orderDO : orderDOList) {
                    if (!PayStatus.PAY_STATUS_INIT.equals(orderDO.getPayStatus())) {
                        sb.append("批量重算不支持已支付订单[" + orderDO.getOrderNo() + "]，未处理\n");
                        continue;
                    }
                    if (!OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus()) &&
                            !OrderStatus.ORDER_STATUS_DELIVERED.equals(orderDO.getOrderStatus()) &&
                            !OrderStatus.ORDER_STATUS_CONFIRM.equals(orderDO.getOrderStatus()) &&
                            !OrderStatus.ORDER_STATUS_PART_RETURN.equals(orderDO.getOrderStatus()) &&
                            !OrderStatus.ORDER_STATUS_RETURN_BACK.equals(orderDO.getOrderStatus()) &&
                            !OrderStatus.ORDER_STATUS_OVER.equals(orderDO.getOrderStatus())
                            ) {
                        sb.append("订单[" + orderDO.getOrderNo() + "]状态不可重算，未处理\n");
                        continue;
                    }
                    orderNoSet.add(orderDO.getOrderNo());
                }
            }
        }
        if (CollectionUtil.isNotEmpty(orderNoSet)) {

            for (String orderNo : orderNoSet) {
                try {
                    if (StringUtil.isEmpty(orderNo)) {
                        continue;
                    }
                    OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
                    if (orderDO == null) {
                        sb.append("订单号[" + orderNo + "]" + "不存在\n");
                        continue;
                    }
                    if (!PayStatus.PAY_STATUS_INIT.equals(orderDO.getPayStatus())) {
                        sb.append("批量重算不支持已支付订单[" + orderDO.getOrderNo() + "]，未处理\n");
                        continue;
                    }
                    CustomerDO customerDO = customerMapper.findByNo(orderDO.getBuyerCustomerNo());
                    if (customerDO == null) {
                        sb.append("订单号[" + orderNo + "]" + "关联的客户不存在\n");
                        continue;
                    }
                    if (ConfirmStatementStatus.CONFIRM_STATUS_YES.equals(customerDO.getConfirmStatementStatus())) {
                        sb.append("订单号[" + orderNo + "]" + "关联的客户编号[" + customerDO.getCustomerNo() + "]已经是确认结算单状态\n");
                        continue;
                    }
                    ServiceResult<String, BigDecimal> result = reCreateOrderStatement(orderNo);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        String json = JSON.toJSONString(resultGenerator.generate(result.getErrorCode()));
                        sb.append("重算订单结算单【失败】：订单号[" + orderNo + "]" + json + "\n");
                    } else {
                        sb.append("成功重算订单：订单号[" + orderNo + "]\n");
                    }
                } catch (Exception e) {
                    logger.error("重算订单【系统错误】：订单号[" + orderNo + "]", e);
                    sb.append("重算订单【系统错误】：订单号[" + orderNo + "]" + e.getMessage() + "\n");
                }
            }
        }
        String message = sb.toString();
        if (StringUtil.isNotEmpty(message)) {
            dingDingSupport.dingDingSendMessage(message);
        }
        return ErrorCode.SUCCESS;
    }

    private List<StatementOrderDetailDO> generateStatementDetailList(OrderDO orderDO, Date currentTime, Integer statementDays, Integer loginUserId) {
        return generateStatementDetailList(orderDO, currentTime, statementDays, loginUserId, CommonConstant.PROPORTION_MAX);
    }

    private List<StatementOrderDetailDO> generateStatementDetailList(OrderDO orderDO, Date currentTime, Integer statementDays, Integer loginUserId, Double amountPercent) {
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
                    Integer hasReturnCount = k3ReturnOrderDetailMapper.findRealReturnCountByOrderEntry(orderProductDO.getFEntryID().toString(), orderDO.getOrderNo());
                    Integer productCount = orderProductDO.getRentingProductCount() + hasReturnCount;
                    orderProductDO.setProductCount(productCount);
                    itemAllAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(productCount), orderProductDO.getProductUnitAmount(), BigDecimalUtil.STANDARD_SCALE), new BigDecimal(orderProductDO.getRentTimeLength()), BigDecimalUtil.STANDARD_SCALE);
                }
                itemAllAmount = BigDecimalUtil.mul(itemAllAmount, new BigDecimal(amountPercent / CommonConstant.PROPORTION_MAX)).setScale(2, BigDecimal.ROUND_HALF_UP);

                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderProductDO.getRentType(), orderDO.getRentTimeLength(), orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                // 无论什么时候交租金，押金必须当天缴纳
                StatementOrderDetailDO depositDetail = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, orderProductDO.getRentDepositAmount(), orderProductDO.getDepositAmount(), BigDecimal.ZERO, currentTime, loginUserId, null);
                if (depositDetail != null) {
                    depositDetail.setSerialNumber(orderProductDO.getSerialNumber());
                    depositDetail.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                    depositDetail.setItemIsNew(orderProductDO.getIsNewProduct());
                    depositDetail.setStatementDetailPhase(0);
                    depositDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
                    addStatementOrderDetailDOList.add(depositDetail);
                }
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderProductDO.getRentType(), orderDO.getRentTimeLength(), orderProductDO.getPayMode(), rentStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId, null);
                    if (statementOrderDetailDO != null) {
                        statementOrderDetailDO.setSerialNumber(orderProductDO.getSerialNumber());
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
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(orderProductDO.getRentType(), orderDO.getRentTimeLength(), statementDays, orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTime, orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId, orderDO.getStatementDate(), null);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setSerialNumber(orderProductDO.getSerialNumber());
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
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, rentStartTime, orderProductDO.getPayMode(), orderProductDO.getRentType(), orderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId, null);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setSerialNumber(orderProductDO.getSerialNumber());
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
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderProductDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, orderProductDO.getPaymentCycle(), orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), statementDays, orderProductDO.getPayMode(), currentTime, loginUserId, null);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setSerialNumber(orderProductDO.getSerialNumber());
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
                BigDecimal itemAllAmount = orderMaterialDO.getMaterialAmount();
                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
                    Integer hasReturnCount = k3ReturnOrderDetailMapper.findRealReturnCountByOrderEntry(orderMaterialDO.getFEntryID().toString(), orderDO.getOrderNo());
                    Integer materialCount = orderMaterialDO.getRentingMaterialCount() + hasReturnCount;
                    orderMaterialDO.setMaterialCount(materialCount);
                    itemAllAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(materialCount), orderMaterialDO.getMaterialUnitAmount(), BigDecimalUtil.STANDARD_SCALE), new BigDecimal(orderMaterialDO.getRentTimeLength()), BigDecimalUtil.STANDARD_SCALE);
                }
                itemAllAmount = BigDecimalUtil.mul(itemAllAmount, new BigDecimal(amountPercent / CommonConstant.PROPORTION_MAX)).setScale(2, BigDecimal.ROUND_HALF_UP);
                Calendar rentStartTimeCalendar = Calendar.getInstance();
                rentStartTimeCalendar.setTime(rentStartTime);
                // 无论什么时候交租金，押金必须当天缴纳
                StatementOrderDetailDO depositDetail = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, orderMaterialDO.getRentDepositAmount(), orderMaterialDO.getDepositAmount(), BigDecimal.ZERO, currentTime, loginUserId, null);
                if (depositDetail != null) {
                    depositDetail.setSerialNumber(orderMaterialDO.getSerialNumber());
                    depositDetail.setItemName(orderMaterialDO.getMaterialName());
                    depositDetail.setItemIsNew(orderMaterialDO.getIsNewMaterial());
                    depositDetail.setStatementDetailPhase(0);
                    depositDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
                    addStatementOrderDetailDOList.add(depositDetail);
                }
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(orderMaterialDO.getRentType(), orderDO.getRentTimeLength(), orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderMaterialDO.getRentType(), orderDO.getRentTimeLength(), orderMaterialDO.getPayMode(), rentStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId, null);
                    if (statementOrderDetailDO != null) {
                        statementOrderDetailDO.setSerialNumber(orderMaterialDO.getSerialNumber());
                        statementOrderDetailDO.setItemName(orderMaterialDO.getMaterialName());
                        statementOrderDetailDO.setItemIsNew(orderMaterialDO.getIsNewMaterial());
                        statementOrderDetailDO.setStatementDetailPhase(statementMonthCount);
                        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    }
                } else {
                    Date lastCalculateDate = rentStartTime;
                    BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(orderMaterialDO.getRentType(), orderDO.getRentTimeLength(), statementDays, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTime, orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId, orderDO.getStatementDate(), null);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setSerialNumber(orderMaterialDO.getSerialNumber());
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
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, rentStartTime, orderMaterialDO.getPayMode(), orderMaterialDO.getRentType(), orderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId, null);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setSerialNumber(orderMaterialDO.getSerialNumber());
                                statementOrderDetailDO.setItemName(orderMaterialDO.getMaterialName());
                                statementOrderDetailDO.setItemIsNew(orderMaterialDO.getIsNewMaterial());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            }
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderMaterialDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), statementDays, orderMaterialDO.getPayMode(), currentTime, loginUserId, null);
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setSerialNumber(orderMaterialDO.getSerialNumber());
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
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_OTHER, BigInteger.ZERO.intValue(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUserId, null);
            if (thisStatementOrderDetailDO != null) {
                thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OTHER);
                addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
            }
        }
        return addStatementOrderDetailDOList;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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

        // 允许冲正后的计算单更改为已支付状态
//        if (BigDecimalUtil.compare(totalAmount, BigDecimal.ZERO) <= 0
//                && BigDecimalUtil.compare(payRentAmount, BigDecimal.ZERO) <= 0
//                && BigDecimalUtil.compare(payRentDepositAmount, BigDecimal.ZERO) <= 0
//                && BigDecimalUtil.compare(payDepositAmount, BigDecimal.ZERO) <= 0
//                && BigDecimalUtil.compare(payOtherAmount, BigDecimal.ZERO) <= 0
//                && BigDecimalUtil.compare(payOverdueAmount, BigDecimal.ZERO) <= 0) {
//            result.setErrorCode(ErrorCode.STATEMENT_PAY_AMOUNT_ERROR);
//            return result;
//        }

        // 冲正后的结算单金额，必须要与现有的结算单金额相同
        if (BigDecimalUtil.compare(totalAmount, BigDecimalUtil.sub(statementOrderDO.getStatementAmount(), statementOrderDO.getStatementPaidAmount())) != 0) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_AMOUNT_NOT_EQUAL_CORRECT_MOUNT);
            return result;
        }

        updateStatementOrderResult(statementOrderDO, payRentAmount, payRentDepositAmount, payDepositAmount, payOtherAmount, payOverdueAmount, currentTime, loginUser.getUserId());

        //最后支付
        String resultCode=payStatement(loginUser, currentTime, statementOrderDO, customerDO, payRentAmount, payRentDepositAmount, payDepositAmount, payOtherAmount, payOverdueAmount, totalAmount);
        if (!ErrorCode.SUCCESS.equals(resultCode)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(resultCode);
            return result;
        }

        result.setResult(true);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private String payStatement(User loginUser, Date currentTime, StatementOrderDO statementOrderDO, CustomerDO customerDO, BigDecimal payRentAmount, BigDecimal payRentDepositAmount, BigDecimal payDepositAmount, BigDecimal payOtherAmount, BigDecimal payOverdueAmount, BigDecimal totalAmount) {
        if (BigDecimalUtil.compare(totalAmount, BigDecimal.ZERO) > 0) {
            StatementPayOrderDO statementPayOrderDO = statementPaySupport.saveStatementPayOrder(statementOrderDO.getId(), totalAmount, payRentAmount, payRentDepositAmount, payDepositAmount, payOtherAmount, payOverdueAmount, StatementOrderPayType.PAY_TYPE_BALANCE, loginUser.getUserId(), currentTime);
            if (statementPayOrderDO == null) {
                return ErrorCode.STATEMENT_ORDER_AMOUNT_MAST_MORE_THEN_ZERO;
            }

            ServiceResult<String, Boolean> paymentResult = paymentService.balancePay(customerDO.getCustomerNo(), statementPayOrderDO.getStatementPayOrderNo(), statementOrderDO.getRemark(), BigDecimalUtil.add(payRentAmount, payOverdueAmount), payRentDepositAmount, payDepositAmount, payOtherAmount);
            if (!ErrorCode.SUCCESS.equals(paymentResult.getErrorCode()) || !paymentResult.getResult()) {
                return paymentResult.getErrorCode();
            }
            ServiceResult<String, Boolean> updatePayOrderResult = statementPaySupport.updateStatementPayOrderStatus(statementPayOrderDO.getId(), PayStatus.PAY_STATUS_PAID, null, loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(updatePayOrderResult.getErrorCode()) && !updatePayOrderResult.getResult()) {
                return updatePayOrderResult.getErrorCode();
            }
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
        Map<Integer, BigDecimal> reletOrderPaidMap = new HashMap<>();

        BigDecimal totalPaidAmount = statementOrderDO.getStatementPaidAmount();

        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);

                // 查询有没有冲正业务金额
                BigDecimal correctBusinessAmount = getStatementItemCorrectAmount(statementOrderDetailDO);

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

                BigDecimal noPaidStatementDetailOtherPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOtherAmount(), statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                if (BigDecimalUtil.compare(noPaidStatementDetailOtherPayAmount, BigDecimal.ZERO) > 0) {
                    BigDecimal needStatementDetailOtherPayAmount = BigDecimalUtil.sub(noPaidStatementDetailOtherPayAmount, correctBusinessAmount);
                    if (BigDecimalUtil.compare(needStatementDetailOtherPayAmount, totalPaidAmount) > 0) {
                        needStatementDetailOtherPayAmount = totalPaidAmount;
                        totalPaidAmount = BigDecimal.ZERO;
                    } else {
                        totalPaidAmount = BigDecimalUtil.sub(totalPaidAmount, needStatementDetailOtherPayAmount);
                    }
                    statementOrderDetailDO.setStatementDetailOtherPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), needStatementDetailOtherPayAmount));
                }

                BigDecimal noPaidStatementDetailRentPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(), statementOrderDetailDO.getStatementDetailRentPaidAmount());
                BigDecimal needStatementDetailRentPayAmount = BigDecimal.ZERO;
                if (BigDecimalUtil.compare(noPaidStatementDetailRentPayAmount, BigDecimal.ZERO) > 0) {
                    needStatementDetailRentPayAmount = BigDecimalUtil.sub(noPaidStatementDetailRentPayAmount, correctBusinessAmount);
                    needStatementDetailRentPayAmount = BigDecimalUtil.add(needStatementDetailRentPayAmount, returnStatementRentAmount);
                    needStatementDetailRentPayAmount = BigDecimalUtil.compare(needStatementDetailRentPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailRentPayAmount;
                    if (BigDecimalUtil.compare(needStatementDetailRentPayAmount, totalPaidAmount) > 0) {
                        needStatementDetailRentPayAmount = totalPaidAmount;
                        totalPaidAmount = BigDecimal.ZERO;
                    } else {
                        totalPaidAmount = BigDecimalUtil.sub(totalPaidAmount, needStatementDetailRentPayAmount);
                    }
                    statementOrderDetailDO.setStatementDetailRentPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), needStatementDetailRentPayAmount));
                }

                BigDecimal noPaidStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                if (BigDecimalUtil.compare(noPaidStatementDetailRentDepositPayAmount, BigDecimal.ZERO) > 0) {
                    BigDecimal needStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(noPaidStatementDetailRentDepositPayAmount, correctBusinessAmount);
                    needStatementDetailRentDepositPayAmount = BigDecimalUtil.add(needStatementDetailRentDepositPayAmount, returnStatementRentDepositAmount);
                    needStatementDetailRentDepositPayAmount = BigDecimalUtil.compare(needStatementDetailRentDepositPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailRentDepositPayAmount;
                    if (BigDecimalUtil.compare(needStatementDetailRentDepositPayAmount, totalPaidAmount) > 0) {
                        needStatementDetailRentDepositPayAmount = totalPaidAmount;
                        totalPaidAmount = BigDecimal.ZERO;
                    } else {
                        totalPaidAmount = BigDecimalUtil.sub(totalPaidAmount, needStatementDetailRentDepositPayAmount);
                    }
                    statementOrderDetailDO.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), needStatementDetailRentDepositPayAmount));
                }

                BigDecimal noPaidStatementDetailDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositAmount(), statementOrderDetailDO.getStatementDetailDepositPaidAmount());
                if (BigDecimalUtil.compare(noPaidStatementDetailDepositPayAmount, BigDecimal.ZERO) > 0) {
                    BigDecimal needStatementDetailDepositPayAmount = BigDecimalUtil.sub(noPaidStatementDetailDepositPayAmount, correctBusinessAmount);
                    needStatementDetailDepositPayAmount = BigDecimalUtil.add(needStatementDetailDepositPayAmount, returnStatementDepositAmount);
                    needStatementDetailDepositPayAmount = BigDecimalUtil.compare(needStatementDetailDepositPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailDepositPayAmount;
                    if (BigDecimalUtil.compare(needStatementDetailDepositPayAmount, totalPaidAmount) > 0) {
                        needStatementDetailDepositPayAmount = totalPaidAmount;
                        totalPaidAmount = BigDecimal.ZERO;
                    } else {
                        totalPaidAmount = BigDecimalUtil.sub(totalPaidAmount, needStatementDetailDepositPayAmount);
                    }
                    statementOrderDetailDO.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), needStatementDetailDepositPayAmount));
                }

                BigDecimal noPaidStatementDetailOverduePayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOverdueAmount(), statementOrderDetailDO.getStatementDetailOverduePaidAmount());
                if (BigDecimalUtil.compare(noPaidStatementDetailOverduePayAmount, BigDecimal.ZERO) > 0) {
                    BigDecimal needStatementDetailOverduePayAmount;

                    // 查询有没有冲正逾期金额
                    BigDecimal correctOverdueAmount = getStatementItemCorrectAmountByType(statementOrderDetailDO);
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
//                orderPaidMap.put(statementOrderDetailDO.getOrderId(), BigDecimalUtil.add(orderPaidMap.get(statementOrderDetailDO.getOrderId()), needStatementDetailRentPayAmount));

                if (statementOrderDetailDO.getReletOrderItemReferId() == null) {
                    orderPaidMap.put(statementOrderDetailDO.getOrderId(), BigDecimalUtil.add(orderPaidMap.get(statementOrderDetailDO.getOrderId()), needStatementDetailRentPayAmount));
                } else {
                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetailDO.getOrderItemType())) {
                        ReletOrderProductDO reletOrderProductDO = reletOrderProductMapper.findById(statementOrderDetailDO.getReletOrderItemReferId());
                        reletOrderPaidMap.put(reletOrderProductDO.getReletOrderId(), BigDecimalUtil.add(reletOrderPaidMap.get(reletOrderProductDO.getReletOrderId()), needStatementDetailRentPayAmount));

                    }
                    if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetailDO.getOrderItemType())) {
                        ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialMapper.findById(statementOrderDetailDO.getReletOrderItemReferId());
                        reletOrderPaidMap.put(reletOrderMaterialDO.getReletOrderId(), BigDecimalUtil.add(reletOrderPaidMap.get(reletOrderMaterialDO.getReletOrderId()), needStatementDetailRentPayAmount));
                    }
                }
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
            orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), OrderStatus.ORDER_STATUS_PAID, null, currentTime, loginUserId,OperationType.STATEMENT_PAID);
        }
        //更新续租单 支付状态和已付款金额
        for (Map.Entry<Integer, BigDecimal> entry : reletOrderPaidMap.entrySet()) {
            Integer reletOrderId = entry.getKey();
            BigDecimal paidAmount = entry.getValue();
            ReletOrderDO reletOrderDO = reletOrderMapper.findById(reletOrderId);
            if (reletOrderDO == null) {
                continue;
            }
            if (!PayStatus.PAY_STATUS_PAID.equals(reletOrderDO.getPayStatus())) {
                reletOrderDO.setPayStatus(PayStatus.PAY_STATUS_PAID);
            }

            reletOrderDO.setTotalPaidOrderAmount(BigDecimalUtil.add(reletOrderDO.getTotalPaidOrderAmount(), paidAmount));
            reletOrderDO.setPayTime(currentTime);
            reletOrderMapper.update(reletOrderDO);
        }
    }

    private BigDecimal getStatementItemCorrectAmount(StatementOrderDetailDO statementOrderDetailDO) {
        List<StatementOrderCorrectDetailDO> statementOrderCorrectDetailDOList = statementOrderCorrectDetailMapper.findByStatementDetailIdAndType(statementOrderDetailDO.getId(), statementOrderDetailDO.getStatementDetailType());
        BigDecimal correctBusinessAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderCorrectDetailDOList)) {
            for (StatementOrderCorrectDetailDO statementOrderCorrectDetailDO : statementOrderCorrectDetailDOList) {
                if (statementOrderCorrectDetailDO != null && BigDecimalUtil.compare(statementOrderCorrectDetailDO.getStatementCorrectAmount(), BigDecimal.ZERO) > 0) {
                    correctBusinessAmount = statementOrderCorrectDetailDO.getStatementCorrectAmount();
                }
            }
        }
        return correctBusinessAmount;
    }

    @Override
    public ServiceResult<String, Page<StatementOrder>> queryStatementOrder(StatementOrderQueryParam statementOrderQueryParam) {
        ServiceResult<String, Page<StatementOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(statementOrderQueryParam.getPageNo(), statementOrderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        if (StringUtil.isNotEmpty(statementOrderQueryParam.getOrderNo())) {
            OrderDO orderDO = orderMapper.findByOrderNo(statementOrderQueryParam.getOrderNo());
            Page<StatementOrder> pageEmpty = new Page<>(new ArrayList<StatementOrder>(), 0, statementOrderQueryParam.getPageNo(), statementOrderQueryParam.getPageSize());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(pageEmpty);
                return result;
            }
            List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderTypeAndId(OrderType.ORDER_TYPE_ORDER, orderDO.getId());
            if (CollectionUtil.isEmpty(statementOrderDetailDOList)) {
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(pageEmpty);
                return result;
            }
            List<Integer> statementOrderIdList = new ArrayList<>();
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                statementOrderIdList.add(statementOrderDetailDO.getStatementOrderId());
            }
            maps.put("statementOrderIdList", statementOrderIdList);
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
//                            OrderProductDO orderProductDO = orderProductMapper.findById(Integer.parseInt(orderItemId));
                            OrderProductDO orderProductDO = productSupport.getOrderProductDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
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
//                            OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(orderItemId));
                            OrderMaterialDO orderMaterialDO = productSupport.getOrderMaterialDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
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
                    List<Integer>mergerList= newStatementOrderDetail.getMergeStatementItemIdList();
                    mergerList.add(statementOrderDetail.getStatementOrderDetailId());
                    newStatementOrderDetail.setMergeStatementItemIdList(mergerList);

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
                    List<Integer>mergerList= new ArrayList<>();
                    mergerList.add(statementOrderDetail.getStatementOrderDetailId());
                    statementOrderDetail.setMergeStatementItemIdList(mergerList);
                }
            }
        }

        for (StatementOrderDetail statementOrderDetail : hashMap.values()) {
            BigDecimal statementDetailPaidAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementOrderDetail.getStatementDetailDepositPaidAmount(), statementOrderDetail.getStatementDetailOverduePaidAmount()), statementOrderDetail.getStatementDetailRentDepositPaidAmount()), statementOrderDetail.getStatementDetailRentPaidAmount()), statementOrderDetail.getStatementDetailOtherPaidAmount());
            statementOrderDetail.setStatementDetailPaidAmount(statementDetailPaidAmount);
        }

        List<StatementOrderDetail> statementOrderDetailList = ListUtil.mapToList(hashMap);
        //排序
        if (CollectionUtil.isNotEmpty(statementOrderDetailList)) {
            statementOrderDetailList = sorting(statementOrderDetailList);
        }
        statementOrder.setStatementOrderDetailList(statementOrderDetailList);

        result.setResult(statementOrder);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private List<StatementOrderDetail> sorting(List<StatementOrderDetail> statementOrderDetailList) {
        //存放非退货单结算单详情项
//        List<StatementOrderDetail> notReturnOrderList = new ArrayList<>();
        Map<Integer, StatementOrderDetail> notReturnOrderMap = new TreeMap<>();
        //存放最终结果
        List<StatementOrderDetail> allList = new ArrayList<>();
        //存放退货结算单详情项其它费用项
        List<StatementOrderDetail> returnOrderOtherList = new ArrayList<>();
        //存放退货结算单商品、配件项
        Map<Integer, List<StatementOrderDetail>> returnOrderProudctAndMaterialMap = new HashMap<>();
        for (StatementOrderDetail statementOrderDetail : statementOrderDetailList) {
            if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
                notReturnOrderMap.put(statementOrderDetail.getStatementOrderDetailId(), statementOrderDetail);
            } else {
                if (null == statementOrderDetail.getReturnReferId()) {
                    returnOrderOtherList.add(statementOrderDetail);
                } else {
                    if (null != returnOrderProudctAndMaterialMap.get(statementOrderDetail.getReturnReferId())) {
                        returnOrderProudctAndMaterialMap.get(statementOrderDetail.getReturnReferId()).add(statementOrderDetail);
                    } else {
                        List<StatementOrderDetail> returnOrderProudctAndMaterialList = new ArrayList<>();
                        returnOrderProudctAndMaterialList.add(statementOrderDetail);
                        returnOrderProudctAndMaterialMap.put(statementOrderDetail.getReturnReferId(), returnOrderProudctAndMaterialList);
                    }
                }
            }
        }
        if (CollectionUtil.isNotEmpty(returnOrderOtherList)) {
            allList.addAll(returnOrderOtherList);
        }
        for (Integer statementOrderDetailId : notReturnOrderMap.keySet()) {
            StatementOrderDetail statementOrderDetail = notReturnOrderMap.get(statementOrderDetailId);
            allList.add(statementOrderDetail);
            List<StatementOrderDetail> statementOrderDetails = returnOrderProudctAndMaterialMap.get(statementOrderDetail.getStatementOrderDetailId());
            if (CollectionUtil.isNotEmpty(statementOrderDetails)) {
                allList.addAll(statementOrderDetails);
            }
        }
        return allList;
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
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderIdForOrderDetail(orderDO.getId());
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

            orderDO = orderDO == null ? orderMapper.findByOrderIdSimple(statementOrderDetail.getOrderId()) : orderDO;
            if (orderDO != null) {
                statementOrderDetail.setOrderNo(orderDO.getOrderNo());
                if (statementOrderDetail.getReletOrderItemReferId() == null) {
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
                } else {
                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                        ReletOrderProductDO reletOrderProductDO = reletOrderProductMapper.findById(statementOrderDetail.getReletOrderItemReferId());
                        if (reletOrderProductDO != null) {
                            statementOrderDetail.setItemName(reletOrderProductDO.getProductName() + reletOrderProductDO.getProductSkuName());
                            statementOrderDetail.setItemCount(reletOrderProductDO.getRentingProductCount());
                            statementOrderDetail.setUnitAmount(reletOrderProductDO.getProductUnitAmount());
                            statementOrderDetail.setItemRentType(orderDO.getRentType());
                        }
                    }
                    if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                        ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialMapper.findById(statementOrderDetail.getReletOrderItemReferId());
                        if (reletOrderMaterialDO != null) {
                            statementOrderDetail.setItemName(reletOrderMaterialDO.getMaterialName());
                            statementOrderDetail.setItemCount(reletOrderMaterialDO.getRentingMaterialCount());
                            statementOrderDetail.setUnitAmount(reletOrderMaterialDO.getMaterialUnitAmount());
                            statementOrderDetail.setItemRentType(orderDO.getRentType());
                        }
                    }
                }
            }

        }

        if (OrderType.ORDER_TYPE_RETURN.equals(statementOrderDetail.getOrderType())) {
            //获取退货单
            K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findById(statementOrderDetail.getOrderId());
            if (k3ReturnOrderDO != null) {
                //存入退货单编号
                statementOrderDetail.setOrderNo(k3ReturnOrderDO.getReturnOrderNo());
                //如果退货单详情不为空
                if (CollectionUtil.isNotEmpty(k3ReturnOrderDO.getK3ReturnOrderDetailDOList())) {
                    //循环退货单详情
                    for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDO.getK3ReturnOrderDetailDOList()) {
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(k3ReturnOrderDetailDO.getId())) {
//                            OrderProductDO orderProductDO = orderProductMapper.findById(Integer.valueOf(k3ReturnOrderDetailDO.getOrderItemId()));
                            OrderProductDO orderProductDO = productSupport.getOrderProductDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                            //存入商品名称
                            if (orderProductDO != null) {
                                statementOrderDetail.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                                //存入商品单价
                                statementOrderDetail.setUnitAmount(orderProductDO.getProductUnitAmount());
                                //存入租赁方式，1按天租，2按月租
                                statementOrderDetail.setItemRentType(orderProductDO.getRentType());
                            }
                            //存入结算单明细类型：3-抵消租金（退租）
                            statementOrderDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                            //存入实际退还商品数量
                            statementOrderDetail.setItemCount(k3ReturnOrderDetailDO.getRealProductCount());
                        }
                        //如果是退换配件
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(k3ReturnOrderDetailDO.getId())) {
//                            OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.valueOf(k3ReturnOrderDetailDO.getOrderItemId()));
                            OrderMaterialDO orderMaterialDO = productSupport.getOrderMaterialDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                            if (orderMaterialDO != null) {
                                //保存配件名
                                statementOrderDetail.setItemName(orderMaterialDO.getMaterialName());
                                //保存配件单价
                                statementOrderDetail.setUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                                //保存租赁方式，1按天租，2按月租
                                statementOrderDetail.setItemRentType(orderMaterialDO.getRentType());
                            }
                            statementOrderDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                            //保存退货数量
                            statementOrderDetail.setItemCount(k3ReturnOrderDetailDO.getRealProductCount());
                        }
                    }
                }
                if (statementOrderDetail.getReletOrderItemReferId() != null) {

                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                        ReletOrderProductDO reletOrderProductDO = reletOrderProductMapper.findById(statementOrderDetail.getReletOrderItemReferId());
                        if (reletOrderProductDO != null) {
//                            ReletOrderDO reletOrderDO = reletOrderMapper.findById(reletOrderProductDO.getReletOrderId());
//                            statementOrderDetail.setItemName(reletOrderProductDO.getProductName() + reletOrderProductDO.getProductSkuName());
//                            statementOrderDetail.setItemCount(reletOrderProductDO.getRentingProductCount());
                            statementOrderDetail.setUnitAmount(reletOrderProductDO.getProductUnitAmount());
//                            statementOrderDetail.setItemRentType(reletOrderDO.getRentType());
                        }
                    }
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                        ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialMapper.findById(statementOrderDetail.getReletOrderItemReferId());
                        if (reletOrderMaterialDO != null) {
//                            ReletOrderDO reletOrderDO = reletOrderMapper.findById(reletOrderMaterialDO.getReletOrderId());
//                            statementOrderDetail.setItemName(reletOrderMaterialDO.getMaterialName());
//                            statementOrderDetail.setItemCount(reletOrderMaterialDO.getRentingMaterialCount());
                            statementOrderDetail.setUnitAmount(reletOrderMaterialDO.getMaterialUnitAmount());
//                            statementOrderDetail.setItemRentType(reletOrderDO.getRentType());
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createK3ReturnOrderStatement(String returnOrderNo) {
        ServiceResult<String, BigDecimal> serviceResult = createK3ReturnOrderStatementCore(returnOrderNo);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
        }

        return serviceResult;
    }

    // k3退货回调使用
    public ServiceResult<String, BigDecimal> createK3ReturnOrderStatementNoTransaction(String returnOrderNo) {
        return createK3ReturnOrderStatementCore(returnOrderNo);
    }

    /**
     * k3退货回调处理结算单时的回滚不能影响主逻辑，如果是k3回调处理结算单，这里不能添加事务，也不能设置RollbackOnly。
     */
    private ServiceResult<String, BigDecimal> createK3ReturnOrderStatementCore(String returnOrderNo) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_IS_NOT_NULL);
            return result;
        }
        //修正续租单，如果有（退货单之后的续租单）
        ServiceResult<String, BigDecimal> fixResult= fixReletOrderItemCount(k3ReturnOrderDO);
        if (!ErrorCode.SUCCESS.equals(fixResult.getErrorCode())) {
            return fixResult;
        }
        List<StatementOrderDetailDO> statementOrderDetails = statementOrderDetailMapper.findByOrderTypeAndId(OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId());
        if (CollectionUtil.isNotEmpty(statementOrderDetails)) {
            result.setErrorCode(ErrorCode.RETURN_STATEMENT_ORDER_CREATE_ERROR);
            return result;
        }
        if (!ReturnOrderStatus.RETURN_ORDER_STATUS_END.equals(k3ReturnOrderDO.getReturnOrderStatus())) {
            result.setErrorCode(ErrorCode.RETURN_ORDER_CAN_NOT_CREATE_STATEMENT);
            return result;
        }
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();

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

        //完成原逻辑退货
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDO.getK3ReturnOrderDetailDOList())) {
            for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDO.getK3ReturnOrderDetailDOList()) {
                OrderProductDO orderProductDO = null;
                //如果是商品
                if (productSupport.isProduct(k3ReturnOrderDetailDO.getProductNo())) {
                    //兼容erp订单和k3订单商品项
                    orderProductDO = productSupport.getOrderProductDO(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
                }
                if (orderProductDO == null) {
                    continue;
                }
                BigDecimal returnCount = new BigDecimal(k3ReturnOrderDetailDO.getRealProductCount());
                if (BigDecimalUtil.compare(returnCount, BigDecimal.ZERO) <= 0) {
                    continue;
                }
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
                BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderProductDO.getRentDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE), returnCount);
                BigDecimal thisReturnDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderProductDO.getDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE), returnCount);

                //获取此订单是否有续租成功的记录
                boolean isReletOrder = false;
                ReletOrderDO reletOrderDO = reletOrderMapper.findRecentlyReletedOrderByOrderId(orderProductDO.getOrderId());
                if (reletOrderDO != null) {
                    isReletOrder = true;
                }


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
                                    return result;
                                }
                                if (orderDO.getExpectReturnTime().compareTo(k3ReturnOrderDO.getReturnTime()) > 0) {
                                    if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                                        ProductDO product = productMapper.findById(orderProductDO.getProductId());
                                        if (product == null) {
                                            result.setErrorCode(ErrorCode.PRODUCT_NOT_EXISTS);
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
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnDepositAmount, returnTime, loginUserId, currentTime);
                                }
                                if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount()), thisReturnRentDepositAmount) >= 0) {
                                    totalReturnRentDepositAmount = BigDecimalUtil.add(totalReturnRentDepositAmount, thisReturnRentDepositAmount);
                                    statementOrderDetailDO.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositReturnAmount(), thisReturnRentDepositAmount));
                                    statementOrderDetailDO.setStatementDetailRentDepositReturnTime(currentTime);
                                    statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositReturnAmount(), thisReturnRentDepositAmount));
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_RENT_DEPOSIT, thisReturnRentDepositAmount, returnTime, loginUserId, currentTime);
                                }
                                statementOrderDetailMapper.update(statementOrderDetailDO);
                                statementOrderMapper.update(statementOrderDO);
                            }
                            continue;
                        }

                        // 如果有押金还没交，不让退货
                        if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                            result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_HAVE_NOT_PAY_DEPOSIT);
                            return result;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                        // 如果退货时间在结束时间之后，证明本期都没缴，不用处理，等待交
//                        if (returnTime.getTime() > statementDetailEndTime.getTime()) {
//                            continue;
//                        }
                        BigDecimal itemAllAmount = orderProductDO.getProductAmount();
                        BigDecimal needPayAmount = BigDecimal.ZERO;
                        BigDecimal payReturnAmount = BigDecimal.ZERO;
                        BigDecimal reletCurrentPhaseReturnAmount = BigDecimal.ZERO;
                        BigDecimal productUnitAmount = BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), returnCount);

                        if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
//                            if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
//                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
//                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
//                                    needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, returnTime, orderProductDO.getProductUnitAmount(), k3ReturnOrderDetailDO.getProductCount());
//                                }
//                            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderProductDO.getPayMode())) {
//                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
//                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
//                                    BigDecimal firstAmount = BigDecimalUtil.mul(returnCount, BigDecimalUtil.div(BigDecimalUtil.mul(itemAllAmount, new BigDecimal(0.3)), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE));
//                                    // 30%期
//                                    if (statementOrderDetailDO.getStatementDetailPhase() == 1) {
//                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
//                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
//                                        needPayAmount = BigDecimalUtil.compare(needPayAmount, firstAmount) > 0 ? firstAmount : needPayAmount;
//                                    } else if (statementOrderDetailDO.getStatementDetailPhase() == 2) {
//                                        // 70% 期
//                                        // 加2的原因是算上前面30%的那一天
//                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 2;
//                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
//                                        // 减掉上期的30%
//                                        needPayAmount = BigDecimalUtil.sub(needPayAmount, firstAmount);
//                                    }
//                                }
//                            } else {
//                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
//                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
//                                    int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
//                                    needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
//                                }
//                            }
                            //计算续租 退还金额
                            if (isReletOrder && statementOrderDetailDO.getReletOrderItemReferId() != null) {

                                ReletOrderProductDO reletOrderProductDO = reletOrderProductMapper.findById(statementOrderDetailDO.getReletOrderItemReferId());
                                //不在当前续租单，则跳过
                                ReletOrderDO reletOrder = reletOrderMapper.findById(reletOrderProductDO.getReletOrderId());
                                if (!isReletOrderRefReturn(returnTime, reletOrder))
                                    continue;
                                Integer rentingProductCount = reletOrderProductDO.getRentingProductCount();

                                //计算续租当期退还金额0
                                if (returnTime.getTime() >= statementDetailStartTime.getTime() && returnTime.getTime() <= statementDetailEndTime.getTime()) {
                                    Integer dayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(returnTime, statementDetailEndTime);
                                    Integer maxDayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(statementDetailStartTime, statementDetailEndTime) + 1;

                                    reletCurrentPhaseReturnAmount = BigDecimalUtil.div(BigDecimalUtil.div(BigDecimalUtil.mul(new BigDecimal(dayCount),
                                            BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount())), new BigDecimal(rentingProductCount), BigDecimalUtil.SCALE),
                                            new BigDecimal(maxDayCount), BigDecimalUtil.SCALE);

                                    payReturnAmount = BigDecimalUtil.add(reletCurrentPhaseReturnAmount, payReturnAmount);
                                }

                                // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
                                if (statementDetailStartTime.getTime() > returnTime.getTime()) {

                                    payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(rentingProductCount), BigDecimalUtil.SCALE));
                                }
                            } else {
                                // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
                                if (!OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && DateUtil.daysBetween(returnTime,statementDetailStartTime) > 0) {
//                                payReturnAmount = statementOrderDetailDO.getStatementDetailAmount();
                                    payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE));
                                }
                            }

                        }
                        // 正常全额退
//                        BigDecimal payReturnAmount = BigDecimalUtil.mul(returnCount, BigDecimalUtil.div(statementOrderDetailDO.getStatementDetailAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE));
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            //运费结算时间放到最接近退货单日期的结算日（大于等于退货日）
                            if (DateUtil.daysBetween(otherStatementTime, statementOrderDetailDO.getStatementExpectPayTime()) <= 0 && DateUtil.daysBetween(returnTime, statementOrderDetailDO.getStatementExpectPayTime()) >= 0) {
                                otherStatementTime = statementOrderDetailDO.getStatementExpectPayTime();
                            }
                        }
                        // 退款金额，扣除未交款的部分
//                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT + "-" + orderProductDO.getId() + "-" + orderProductDO.getProductSkuId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
                        if (addStatementOrderDetailDOMap.get(key) != null) {
                            StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                            thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                        } else {
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, k3ReturnOrderDetailDO.getId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, statementOrderDetailDO.getReletOrderItemReferId());
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
                OrderMaterialDO orderMaterialDO = null;
                //如果是配件
                if (productSupport.isMaterial(k3ReturnOrderDetailDO.getProductNo())) {
                    //兼容erp订单和k3订单配件项
                    orderMaterialDO = productSupport.getOrderMaterialDO(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
                }
                if (orderMaterialDO == null) {
                    continue;
                }
                BigDecimal returnCount = new BigDecimal(k3ReturnOrderDetailDO.getRealProductCount());
                if (BigDecimalUtil.compare(returnCount, BigDecimal.ZERO) <= 0) {
                    continue;
                }
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
                BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderMaterialDO.getRentDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE), returnCount);
                BigDecimal thisReturnDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderMaterialDO.getDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE), returnCount);

                //获取此订单是否有续租成功的记录
                boolean isReletOrder = false;
                ReletOrderDO reletOrderDO = reletOrderMapper.findRecentlyReletedOrderByOrderId(orderMaterialDO.getOrderId());
                if (reletOrderDO != null) {
                    isReletOrder = true;
                }


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
                                    return result;
                                }
                                if (orderDO.getExpectReturnTime().compareTo(k3ReturnOrderDO.getReturnTime()) > 0) {
                                    if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                                        MaterialDO material = materialMapper.findById(orderMaterialDO.getMaterialId());
                                        if (material == null) {
                                            result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
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
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnDepositAmount, returnTime, loginUserId, currentTime);
                                }
                                if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount()), thisReturnRentDepositAmount) >= 0) {
                                    totalReturnRentDepositAmount = BigDecimalUtil.add(totalReturnRentDepositAmount, thisReturnRentDepositAmount);
                                    statementOrderDetailDO.setStatementDetailRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositReturnAmount(), thisReturnRentDepositAmount));
                                    statementOrderDetailDO.setStatementDetailRentDepositReturnTime(currentTime);
                                    statementOrderDO.setStatementRentDepositReturnAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositReturnAmount(), thisReturnRentDepositAmount));
                                    statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_RENT_DEPOSIT, thisReturnRentDepositAmount, returnTime, loginUserId, currentTime);
                                }
                                statementOrderDetailMapper.update(statementOrderDetailDO);
                                statementOrderMapper.update(statementOrderDO);
                            }
                            continue;
                        }

                        // 如果有押金还没交，不让退货
                        if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                            result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_HAVE_NOT_PAY_DEPOSIT);
                            return result;
                        }
                        statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                        statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();
                        // 如果退货时间在结束时间之后，证明本期都没缴，不用处理，等待交
//                        if (returnTime.getTime() > statementDetailEndTime.getTime()) {
//                            continue;
//                        }

                        BigDecimal needPayAmount = BigDecimal.ZERO;
                        BigDecimal productUnitAmount = BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), returnCount);
                        BigDecimal payReturnAmount = BigDecimal.ZERO;
                        BigDecimal reletCurrentPhaseReturnAmount = BigDecimal.ZERO;
                        if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
//                            if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
//                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
//                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
//                                    needPayAmount = amountSupport.calculateRentAmount(statementDetailStartTime, returnTime, productUnitAmount);
//                                }
//                            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderMaterialDO.getPayMode())) {
//                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
//                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
//                                    BigDecimal firstAmount = BigDecimalUtil.mul(returnCount, BigDecimalUtil.div(BigDecimalUtil.mul(orderMaterialDO.getMaterialAmount(), new BigDecimal(0.3)), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE));
//                                    // 30%期
//                                    if (statementOrderDetailDO.getStatementDetailPhase() == 1) {
//                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
//                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
//                                        needPayAmount = BigDecimalUtil.compare(needPayAmount, firstAmount) > 0 ? firstAmount : needPayAmount;
//                                    } else if (statementOrderDetailDO.getStatementDetailPhase() == 2) {
//                                        // 70% 期
//                                        // 加2的原因是算上前面30%的那一天
//                                        int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 2;
//                                        needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
//                                        // 减掉上期的30%
//                                        needPayAmount = BigDecimalUtil.sub(needPayAmount, firstAmount);
//                                    }
//                                }
//                            } else {
//                                // 如果开始时间在当前时间之前，证明先用后付，要计未缴纳费用。
//                                if (returnTime.getTime() >= statementDetailStartTime.getTime()) {
//                                    int surplusDays = DateUtil.daysBetween(statementDetailStartTime, returnTime) + 1;
//                                    needPayAmount = surplusDays > 0 ? BigDecimalUtil.mul(productUnitAmount, new BigDecimal(surplusDays)) : needPayAmount;
//                                }
//                            }

                            //计算续租 退还金额
                            if (isReletOrder && statementOrderDetailDO.getReletOrderItemReferId() != null) {
                                ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialMapper.findById(statementOrderDetailDO.getReletOrderItemReferId());
                                //不在当前续租单，则跳过
                                ReletOrderDO reletOrder = reletOrderMapper.findById(reletOrderMaterialDO.getReletOrderId());
                                if (!isReletOrderRefReturn(returnTime, reletOrder))
                                    continue;
                                Integer rentingMaterialCount = reletOrderMaterialDO.getRentingMaterialCount();

                                //计算续租当期退还金额
                                if (returnTime.getTime() >= statementDetailStartTime.getTime() && returnTime.getTime() <= statementDetailEndTime.getTime()) {
                                    Integer dayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(returnTime, statementDetailEndTime);
                                    Integer maxDayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(statementDetailStartTime, statementDetailEndTime) + 1;

                                    reletCurrentPhaseReturnAmount = BigDecimalUtil.div(BigDecimalUtil.div(BigDecimalUtil.mul(new BigDecimal(dayCount),
                                            BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount())), new BigDecimal(rentingMaterialCount), BigDecimalUtil.SCALE),
                                            new BigDecimal(maxDayCount), BigDecimalUtil.SCALE);

                                    payReturnAmount = BigDecimalUtil.add(reletCurrentPhaseReturnAmount, payReturnAmount);
                                }
                                // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
                                if (statementDetailStartTime.getTime() > returnTime.getTime()) {

                                    payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(rentingMaterialCount), BigDecimalUtil.SCALE));
                                }

                            } else {
                                // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
                                if (!OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && DateUtil.daysBetween(returnTime,statementDetailStartTime) > 0) {
//                                payReturnAmount = statementOrderDetailDO.getStatementDetailAmount();
                                    payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE));
                                }
                            }

                        }

//                        BigDecimal payReturnAmount = BigDecimalUtil.mul(returnCount, BigDecimalUtil.div(statementOrderDetailDO.getStatementDetailAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE));
                        if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                            if (DateUtil.daysBetween(otherStatementTime, statementOrderDetailDO.getStatementExpectPayTime()) <= 0 && DateUtil.daysBetween(returnTime, statementOrderDetailDO.getStatementExpectPayTime()) >= 0) {
                                otherStatementTime = statementOrderDetailDO.getStatementExpectPayTime();
                            }
                        }
                        // 退款金额，扣除未交款的部分
//                        payReturnAmount = BigDecimalUtil.sub(payReturnAmount, needPayAmount);

                        String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL + "-" + orderMaterialDO.getId() + "-" + orderMaterialDO.getMaterialId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
                        if (addStatementOrderDetailDOMap.get(key) != null) {
                            StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                            thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                            addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                        } else {
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, k3ReturnOrderDetailDO.getId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, statementOrderDetailDO.getReletOrderItemReferId());
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
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER, BigInteger.ZERO.intValue(), otherStatementTime, returnTime, returnTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUserId, null);
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
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUserId);


        //将退货单状态设置为已结算
        k3ReturnOrderDO.setSuccessStatus(CommonConstant.YES);
        k3ReturnOrderDO.setUpdateUser(loginUserId.toString());
        k3ReturnOrderDO.setUpdateTime(currentTime);
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
        //计算违约金
//        ServiceResult<String, BigDecimal> penaltyResult= penaltySupport.k3OrderPenalty(k3ReturnOrderDO.getReturnOrderNo());
//        if(!ErrorCode.SUCCESS.equals(penaltyResult.getErrorCode())){
//            result.setErrorCode(penaltyResult.getErrorCode());
//            return result;
//        }
        //押金最后退
        if (BigDecimalUtil.compare(totalReturnRentDepositAmount, BigDecimal.ZERO) > 0
                || BigDecimalUtil.compare(totalReturnDepositAmount, BigDecimal.ZERO) > 0) {
            ServiceResult<String, Boolean> returnDepositResult = paymentService.returnDeposit(buyerCustomerNo, totalReturnRentDepositAmount, totalReturnDepositAmount);
            if (!ErrorCode.SUCCESS.equals(returnDepositResult.getErrorCode()) || !returnDepositResult.getResult()) {
                result.setErrorCode(returnDepositResult.getErrorCode());
                return result;
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

 private boolean isReletOrderRefReturn(Date returnTime, ReletOrderDO reletOrder) {
        return DateUtil.daysBetween(returnTime,reletOrder.getExpectReturnTime())>=0&& DateUtil.daysBetween(reletOrder.getRentStartTime(),returnTime)>=0;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderProductEquipmentDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, returnOrderProductEquipmentDO.getReturnOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId(), null);
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
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderMaterialBulkDO.getReturnOrderId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, returnOrderMaterialBulkDO.getReturnOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId(), null);
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
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, returnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER, BigInteger.ZERO.intValue(), otherStatementTime, returnTime, returnTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUser.getUserId(), null);
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
                                thisMonthDiff, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId(), null);
                        changeStatementOrderDetailDO.setStatementDetailPhase(nextStatement.getStatementDetailPhase());
                        changeStatementOrderDetailDO.setStatementDetailType(nextStatement.getStatementDetailType());
                        addStatementOrderDetailDOList.add(changeStatementOrderDetailDO);
                    }
                    //如果其他费用没有计算过，且其他费用大于0
                    if (!isCountOther && BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                        StatementOrderDetailDO changeStatementOrderDetailDO = buildStatementOrderDetailDO(nextStatement.getCustomerId(), OrderType.ORDER_TYPE_CHANGE,
                                k3ChangeOrderDetailDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER, 0,
                                nextStatement.getStatementExpectPayTime(), nextStatement.getStatementStartTime(), nextStatement.getStatementEndTime(),
                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUser.getUserId(), null);
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
                            diff, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId(), null);
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, productEquipmentDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId(), null);
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

                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderProductEquipmentDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_PRODUCT, changeOrderProductEquipmentDO.getChangeOrderProductId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, payDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId(), null);
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
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementDetailStartTime, statementDetailStartTime, statementDetailEndTime, materialBulkDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId(), null);
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
                            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(statementOrderDetailDO.getCustomerId(), OrderType.ORDER_TYPE_CHANGE, changeOrderMaterialBulkDO.getChangeOrderId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_MATERIAL, changeOrderMaterialBulkDO.getChangeOrderMaterialId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, payDiffAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUser.getUserId(), null);
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
            StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, changeOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_CHANGE_OTHER, BigInteger.ZERO.intValue(), currentTime, currentTime, currentTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUser.getUserId(), null);
            addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
        }
        saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUser.getUserId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
            Map<Date, Date> statementStartTimeMap = new HashMap<>();
            Map<Date, Date> statementEndTimeMap = new HashMap<>();
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
                    //结算单为已支付或者部分支付时为部分支付状态；结算金额小于零则无需支付；其它为未支付状态。
                    if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus())
                            || StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART.equals(statementOrderDO.getStatementStatus())) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
                    } else if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), new BigDecimal(0.1)) < 0) {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
                    } else {
                        statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
                    }
//                    if (statementOrderDetailDO.getStatementStartTime().getTime() < statementOrderDO.getStatementStartTime().getTime()) {
//                        statementOrderDO.setStatementStartTime(statementOrderDetailDO.getStatementStartTime());
//                    }
//                    //存储结算单详情中最小的开始日期
//                    if (statementStartTimeMap.containsKey(dateKey)) {
//                        if (statementOrderDetailDO.getStatementStartTime().getTime() < statementStartTimeMap.get(dateKey).getTime()) {
//                            statementStartTimeMap.put(dateKey,statementOrderDetailDO.getStatementStartTime());
//                        }
//                    }else {
//                        statementStartTimeMap.put(dateKey,statementOrderDetailDO.getStatementStartTime());
//                    }
//                    if (statementOrderDetailDO.getStatementEndTime().getTime() > statementOrderDO.getStatementEndTime().getTime()) {
//                        statementOrderDO.setStatementEndTime(statementOrderDetailDO.getStatementEndTime());
//                    }
//                    //存储结算单详情中最大的结束日期
//                    if (statementEndTimeMap.containsKey(dateKey)) {
//                        if (statementOrderDetailDO.getStatementEndTime().getTime() > statementEndTimeMap.get(dateKey).getTime()) {
//                            statementEndTimeMap.put(dateKey,statementOrderDetailDO.getStatementEndTime());
//                        }
//                    }else {
//                        statementEndTimeMap.put(dateKey,statementOrderDetailDO.getStatementEndTime());
//                    }
                    statementOrderDO.setStatementCouponAmount(statementOrderDetailDO.getStatementCouponAmount());
                    statementOrderMapper.update(statementOrderDO);
                }
                statementOrderDOMap.put(dateKey, statementOrderDO);
                statementOrderDetailDO.setStatementOrderId(statementOrderDO.getId());
            }

            if (CollectionUtil.isNotEmpty(addStatementOrderDetailDOList)) {
                SqlLogInterceptor.setExecuteSql("skip print statementOrderDetailMapper.saveList  sql ......");
                statementOrderDetailMapper.saveList(addStatementOrderDetailDOList);
            }
            //找出结算单对应结算单所有结算单项的最大值和最小值
            for (Date dateKey : statementOrderDOMap.keySet()) {
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByStatementOrderId(statementOrderDOMap.get(dateKey).getId());
                for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                    if (statementStartTimeMap.containsKey(dateKey)) {
                        if (statementOrderDetailDO.getStatementStartTime().getTime() < statementStartTimeMap.get(dateKey).getTime()) {
                            statementStartTimeMap.put(dateKey, statementOrderDetailDO.getStatementStartTime());
                        }
                    } else {
                        statementStartTimeMap.put(dateKey, statementOrderDetailDO.getStatementStartTime());
                    }
                    //存储结算单详情中最大的结束日期
                    if (statementEndTimeMap.containsKey(dateKey)) {
                        if (statementOrderDetailDO.getStatementEndTime().getTime() > statementEndTimeMap.get(dateKey).getTime()) {
                            statementEndTimeMap.put(dateKey, statementOrderDetailDO.getStatementEndTime());
                        }
                    } else {
                        statementEndTimeMap.put(dateKey, statementOrderDetailDO.getStatementEndTime());
                    }
                }
            }
            //将最小值和最大值存储进去
            if (!statementStartTimeMap.isEmpty() && !statementEndTimeMap.isEmpty()) {
                for (Date dateKey : statementOrderDOMap.keySet()) {
                    if (statementStartTimeMap.containsKey(dateKey)) {
                        statementOrderDOMap.get(dateKey).setStatementStartTime(statementStartTimeMap.get(dateKey));
                    }
                    if (statementEndTimeMap.containsKey(dateKey)) {
                        statementOrderDOMap.get(dateKey).setStatementEndTime(statementEndTimeMap.get(dateKey));
                    }
                    statementOrderMapper.update(statementOrderDOMap.get(dateKey));
                }
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
    StatementOrderDetailDO calculateOneStatementOrderDetail(Integer rentType, Integer rentTimeLength, Integer payMode, Date rentStartTime, BigDecimal statementDetailAmount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId, Integer reletOrderItemReferId) {
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

        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, statementDetailAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, reletOrderItemReferId);
    }

    /**
     * 计算多期中的第一期明细
     */
    StatementOrderDetailDO calculateFirstStatementOrderDetail(Integer rentType, Integer rentLength, Integer statementDays, Integer paymentCycle, Integer payMode, Date rentStartTime, BigDecimal unitAmount, Integer itemCount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId, Integer statementMode, Integer reletOrderItemReferId) {
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
        //如果是自然日结算，并且是按月算的
        if (statementMode == null) {
            statementMode = StatementMode.STATEMENT_MONTH_END;
        }
        if (StatementMode.STATEMENT_MONTH_NATURAL.equals(statementMode) &&
                OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            //获取2月1号
            Calendar c = Calendar.getInstance();
            c.setTime(rentStartTime);
            c.set(Calendar.MONTH, 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            //如果是闰年
            if (DateUtil.isLeapYesr(rentStartTime)) {
                //1.30 ,1.31日不要钱，例如1.30-2.29日按一个月算钱
                if (rentStartTimeCalendar.get(Calendar.MONTH) == 0) {
                    if (rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH) == 30 || rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH) == 31) {
                        firstPhaseAmount = amountSupport.calculateRentAmount(c.getTime(), statementEndTime, unitAmount, itemCount);
                    }
                }
            } else {
                //如果不是闰年
                //1.29 ,1.30 ,1.31日不要钱，例如1.29-2.28日按一个月算钱
                if (rentStartTimeCalendar.get(Calendar.MONTH) == 0) {
                    if (rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH) == 29 || rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH) == 30 || rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH) == 31) {
                        firstPhaseAmount = amountSupport.calculateRentAmount(c.getTime(), statementEndTime, unitAmount, itemCount);
                    }
                }
            }

        }


        firstPhaseAmount = BigDecimalUtil.round(firstPhaseAmount, 0);
        // 先不考虑保险
        /*BigDecimal insuranceTotalAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, BigDecimalUtil.mul(insuranceAmount, new BigDecimal(itemCount)));
        firstPhaseAmount = BigDecimalUtil.add(firstPhaseAmount, insuranceTotalAmount);*/
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, firstPhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, reletOrderItemReferId);
    }

    /**
     * 计算多期中的最后一期明细
     */
    StatementOrderDetailDO calculateLastStatementOrderDetail(Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date lastCalculateDate, Date rentStartTime, Integer payMode, Integer rentType, Integer rentTimeLength, BigDecimal itemAllAmount, BigDecimal alreadyPaidAmount, Date currentTime, Integer loginUserId, Integer reletOrderItemReferId) {
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
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, lastPhaseStartTime, statementEndTime, lastPhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, reletOrderItemReferId);
    }

    /**
     * 计算多期中的中间期明细
     */
    StatementOrderDetailDO calculateMiddleStatementOrderDetail(Integer rentType, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date lastCalculateDate, Integer paymentCycle, BigDecimal itemUnitAmount, Integer itemCount, Integer statementDays, Integer payMode, Date currentTime, Integer loginUserId, Integer reletOrderItemReferId) {
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
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, thisPhaseStartTime, statementEndTime, middlePhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, reletOrderItemReferId);
    }

    StatementOrderDetailDO buildStatementOrderDetailDO(Integer customerId, Integer orderType, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date statementExpectPayTime, Date startTime, Date endTime, BigDecimal statementDetailRentAmount, BigDecimal statementDetailRentDepositAmount, BigDecimal statementDetailDepositAmount, BigDecimal otherAmount, Date currentTime, Integer loginUserId, Integer reletOrderItemReferId) {
        StatementOrderDetailDO statementOrderDetailDO = new StatementOrderDetailDO();
        statementOrderDetailDO.setCustomerId(customerId);
        statementOrderDetailDO.setOrderType(orderType);
        statementOrderDetailDO.setOrderId(orderId);
        statementOrderDetailDO.setOrderItemType(orderItemType);
        statementOrderDetailDO.setOrderItemReferId(orderItemReferId);
        statementOrderDetailDO.setReletOrderItemReferId(reletOrderItemReferId);
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), OrderStatus.ORDER_STATUS_PAID, null, now, userSupport.getCurrentUser().getUserId(),OperationType.STATEMENT_PAID);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<FinanceStatementOrderPayDetail>> queryFinanceStatementOrderPayDetail(StatementOrderDetailQueryParam statementOrderDetailQueryParam) {
        ServiceResult<String, Page<FinanceStatementOrderPayDetail>> serviceResult = new ServiceResult<>();
        Integer currentUserType = 0;
        if (userSupport.isSuperUser()) {
            currentUserType = 1;
        } else if (userSupport.isHeadUser()) {
            currentUserType = 2;
        }

        PageQuery pageQuery = new PageQuery(statementOrderDetailQueryParam.getPageNo(), statementOrderDetailQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("statementOrderDetailQueryParam", statementOrderDetailQueryParam);
        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
        maps.put("currentUserType", currentUserType);
        Integer totalCount = statementOrderDetailMapper.queryStatementOrderDetailCountByParam(maps);
        List<FinanceStatementOrderPayDetail> financeStatementOrderPayDetailList = statementOrderDetailMapper.queryStatementOrderDetailByParam(maps);
        Page<FinanceStatementOrderPayDetail> page = new Page<>(financeStatementOrderPayDetailList, totalCount, statementOrderDetailQueryParam.getPageNo(), statementOrderDetailQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
        return serviceResult;
    }

    /**
     * 清除订单的结算单信息
     *
     * @param orderDO
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, AmountNeedReturn> clearStatementOrder(OrderDO orderDO, boolean clearStatementDateSplitCfg) {
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderTypeAndId(OrderType.ORDER_TYPE_ORDER, orderDO.getId());
        //分段日之前的已支付结算保留（为兼容分段结算,分段结算仅删除分段时间点后的结算进行重算）
        OrderStatementDateSplitDO orderStatementDateSplitDO = orderStatementDateSplitMapper.findByOrderNo(orderDO.getOrderNo());
        boolean hasPaidPhase = false;
        if (orderStatementDateSplitDO != null) {
            if (clearStatementDateSplitCfg) {
                orderStatementDateSplitDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderStatementDateSplitMapper.update(orderStatementDateSplitDO);
            } else {
                //前结算日与订单原结算日不一致则不允许分段结算
                ServiceResult<String, AmountNeedReturn> filterSettledOrderDetailResult = new ServiceResult<>();
                List<StatementOrderDetailDO> canClearList = new ArrayList<>();
                for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOList) {
                    if (DateUtil.daysBetween(orderStatementDateSplitDO.getStatementDateChangeTime(), orderDetailDO.getStatementEndTime()) > 0) {
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(orderDetailDO.getStatementDetailStatus())) {
                            filterSettledOrderDetailResult.setErrorCode(ErrorCode.STATEMENT_SPLIT_TIME_ERROR);
                            return filterSettledOrderDetailResult;
                        }
                        canClearList.add(orderDetailDO);
                    } else {
                        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(orderDetailDO.getStatementDetailStatus()))
                            hasPaidPhase = true;
                    }
                }
                //如果有支付的期数，则认为认可原支付方式，只清除改变节点后的结算
                if (hasPaidPhase) {
                    Integer statementDate = orderDO.getStatementDate();
                    if (statementDate == null) {
                        DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
                        statementDate = dataDictionaryDO == null ? StatementMode.STATEMENT_MONTH_END : Integer.parseInt(dataDictionaryDO.getDataName());
                    }
                    if (!orderStatementDateSplitDO.getBeforeStatementDate().equals(statementDate)) {
                        filterSettledOrderDetailResult.setErrorCode(ErrorCode.BEFORE_STATEMENT_MODE_NOT_SAME, getStatementModeString(orderStatementDateSplitDO.getBeforeStatementDate()), getStatementModeString(statementDate));
                        return filterSettledOrderDetailResult;
                    }
                    statementOrderDetailDOList = canClearList;
                }
            }
        }
        //处理已支付订单
        //boolean paid = PayStatus.PAY_STATUS_PAID_PART.equals(orderDO.getPayStatus()) || PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus());
        boolean paid = true;//物理删除会导致问题
        //清除结算信息
        ServiceResult<String, AmountNeedReturn> result = clearStatement(paid, orderDO.getBuyerCustomerNo(), statementOrderDetailDOList);
        //创建失败回滚
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        //非完全退款，且有支付记录，不清除订单转态
        if (paid && !(!clearStatementDateSplitCfg && hasPaidPhase)) {
            //此处逻辑与强制取消订单不同，强制取消订单留存支付记录，不修改订单，重算修改订单支付状态
            orderDO.setPayStatus(PayStatus.PAY_STATUS_INIT);
            orderDO.setTotalPaidOrderAmount(BigDecimal.ZERO);
            orderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            orderDO.setUpdateTime(new Date());
            orderMapper.update(orderDO);
        }
        return result;
    }

    private ServiceResult<String, AmountNeedReturn> clearStatement(boolean paid, String buyerCustomerNo, List<StatementOrderDetailDO> statementOrderDetailDOList) {
        ServiceResult<String, AmountNeedReturn> result = new ServiceResult<>();
        Map<Integer, StatementOrderDO> statementOrderDOMap = statementOrderSupport.getStatementOrderByDetails(statementOrderDetailDOList);
        statementOrderSupport.reStatement(new Date(), statementOrderDOMap, statementOrderDetailDOList);
        //删除相关冲正单
        statementOrderSupport.clearStatementRefCorrect(statementOrderDetailDOList);
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
            statementOrderSupport.reStatementPaid(statementOrderDOMap, statementOrderDetailDOList);
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


    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> createReletOrderStatement(ReletOrderDO reletOrderDO) {
        return reletOrderStatement(reletOrderDO, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> reletOrderStatement(ReletOrderDO reletOrderDO, boolean allowUnpay) {
        ServiceResult<String, BigDecimal> result = new ServiceResult<>();
        if (reletOrderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
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
        if (!allowUnpay) {
            List<StatementOrderDetailDO> dbStatementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(reletOrderDO.getOrderId());
            if (CollectionUtil.isNotEmpty(dbStatementOrderDetailDOList)) {
                for (StatementOrderDetailDO statementOrderDetailDO : dbStatementOrderDetailDOList) {
                    Integer dayCount = DateUtil.daysBetween(statementOrderDetailDO.getStatementExpectPayTime(), new Date());
                    if (CommonConstant.NO == statementOrderDetailDO.getStatementDetailStatus() && dayCount > 0) {
                        result.setErrorCode(ErrorCode.RELET_ORDER_EXISTS_UNPAID_STATEMENT);
                        return result;
                    }
                }
            }
        }
        //统一拿订单结算日
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = getStatementReletOrderDetailDOS(reletOrderDO, currentTime, rentStartTime, loginUserId);
        List<StatementOrderDetailDO> finalAddStatementOrderDetailDOList = new ArrayList<>();

        OrderDO orderDO = orderMapper.findByOrderNo(reletOrderDO.getOrderNo());
        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
            K3OrderStatementConfigDO k3OrderStatementConfigDO = k3OrderStatementConfigMapper.findByOrderId(orderDO.getId());
            if (k3OrderStatementConfigDO != null && k3OrderStatementConfigDO.getRentStartTime() != null) {
                Date k3RentStartTime = k3OrderStatementConfigDO.getRentStartTime();
                for (StatementOrderDetailDO statementOrderDetailDO : addStatementOrderDetailDOList) {
                    //如果结算结束时间大于等于k3配置起租时间，则保存该结算单
                    if (statementOrderDetailDO.getStatementEndTime().getTime() - k3RentStartTime.getTime() >= 0) {
                        finalAddStatementOrderDetailDOList.add(statementOrderDetailDO);
                    }
                }
            } else {
                finalAddStatementOrderDetailDOList = addStatementOrderDetailDOList;
            }
        } else {
            finalAddStatementOrderDetailDOList = addStatementOrderDetailDOList;
        }
        saveStatementOrder(finalAddStatementOrderDetailDOList, currentTime, loginUser.getUserId());

        // 生成单子后，本次需要付款的金额
        BigDecimal thisNeedPayAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(finalAddStatementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : finalAddStatementOrderDetailDOList) {
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
        return generateReletStatementDetailList(reletOrderDO, currentTime, statementDays, loginUserId, CommonConstant.PROPORTION_MAX);
    }

    private List<StatementOrderDetailDO> generateReletStatementDetailList(ReletOrderDO reletOrderDO, Date currentTime, Integer statementDays, Integer loginUserId, double amountPercent) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Date rentStartTime = reletOrderDO.getRentStartTime();
        Integer buyerCustomerId = reletOrderDO.getBuyerCustomerId();
        Integer orderId = reletOrderDO.getOrderId();

        // 商品生成结算单
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            for (ReletOrderProductDO reletOrderProductDO : reletOrderDO.getReletOrderProductDOList()) {

                BigDecimal itemAllAmount = reletOrderProductDO.getProductAmount();
                itemAllAmount = BigDecimalUtil.mul(itemAllAmount, new BigDecimal(amountPercent / CommonConstant.PROPORTION_MAX)).setScale(2, BigDecimal.ROUND_HALF_UP);
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
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderProductDO.getPayMode(), rentStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), currentTime, loginUserId, reletOrderProductDO.getId());
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
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), statementDays, reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getPayMode(), rentStartTime, reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getRentingProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), currentTime, loginUserId, reletOrderDO.getStatementDate(), reletOrderProductDO.getId());
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
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), lastCalculateDate, rentStartTime, reletOrderProductDO.getPayMode(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId, reletOrderProductDO.getId());
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
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(reletOrderDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), lastCalculateDate, reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getRentingProductCount(), statementDays, reletOrderProductDO.getPayMode(), currentTime, loginUserId, reletOrderProductDO.getId());
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

                BigDecimal itemAllAmount = reletOrderMaterialDO.getMaterialAmount();
                itemAllAmount = BigDecimalUtil.mul(itemAllAmount, new BigDecimal(amountPercent / CommonConstant.PROPORTION_MAX)).setScale(2, BigDecimal.ROUND_HALF_UP);
                // 先确定订单需要结算几期
                Integer statementMonthCount = calculateStatementMonthCount(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
                if (statementMonthCount == 1) {
                    StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderMaterialDO.getPayMode(), rentStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), currentTime, loginUserId, reletOrderMaterialDO.getId());
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
                    for (int i = 1; i <= statementMonthCount; i++) {
                        // 第一期
                        if (i == 1) {
                            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), statementDays, reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getPayMode(), rentStartTime, reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getRentingMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), currentTime, loginUserId, reletOrderDO.getStatementDate(), reletOrderMaterialDO.getId());
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
                            StatementOrderDetailDO statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), lastCalculateDate, rentStartTime, reletOrderMaterialDO.getPayMode(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId, reletOrderMaterialDO.getId());
                            if (statementOrderDetailDO != null) {
                                statementOrderDetailDO.setItemName(reletOrderMaterialDO.getMaterialName());
                                statementOrderDetailDO.setItemIsNew(reletOrderMaterialDO.getIsNewMaterial());
                                statementOrderDetailDO.setStatementDetailPhase(i);
                                statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
                                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                            }
                        } else {
                            // 中间期数
                            StatementOrderDetailDO statementOrderDetailDO = calculateMiddleStatementOrderDetail(reletOrderDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), lastCalculateDate, reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getRentingMaterialCount(), statementDays, reletOrderMaterialDO.getPayMode(), currentTime, loginUserId, reletOrderMaterialDO.getId());
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

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, BigDecimal> reCreateReletOrderStatement(String reletOrderNo) {


        return reCreateReletOrderStatement(reletOrderNo,false);
    }

    private ServiceResult<String, BigDecimal> reCreateReletOrderStatement(String reletOrderNo,boolean allowCustomerConfirm) {
        ServiceResult<String, BigDecimal> serviceResult = new ServiceResult<>();

        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrderNo);
        if (reletOrderDO == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(ErrorCode.RELET_ORDER_NOT_EXISTS);
            return serviceResult;
        }

        if(!allowCustomerConfirm){
            // 客户为确认结算单状态时，不允许重算客户的订单
            CustomerDO customerDO = customerMapper.findByNo(reletOrderDO.getBuyerCustomerNo());
            if (customerDO != null && ConfirmStatementStatus.CONFIRM_STATUS_YES.equals(customerDO.getConfirmStatementStatus())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_CONFIRM_STATEMENT_REFUSE_RECREATE);
                return serviceResult;
            }
            if (!Arrays.asList(OrderStatus.ORDER_STATUS_WAIT_DELIVERY, OrderStatus.ORDER_STATUS_PROCESSING, OrderStatus.ORDER_STATUS_DELIVERED, OrderStatus.ORDER_STATUS_CONFIRM, OrderStatus.ORDER_STATUS_PART_RETURN, OrderStatus.ORDER_STATUS_RETURN_BACK).contains(reletOrderDO.getReletOrderStatus())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(ErrorCode.ORDER_STATUS_NOT_ALLOW_RE_STATEMEMT);
                return serviceResult;
            }
        }

        OrderDO orderDO = orderMapper.findByOrderNo(reletOrderDO.getOrderNo());
        if (orderDO == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return serviceResult;
        }
        syncReletOrderStatementByOrder(orderDO, reletOrderDO);

        List<K3ReturnOrderDetailDO> reletReturnOrderDetailDOList = new ArrayList<>();
        //首先清除该续租下退货结算(有实际退货的)
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByOrderNo(reletOrderDO.getOrderNo());
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDetailDOList)) {
            Map<Integer, K3ReturnOrderDO> k3ReturnOrderDOCatch = new HashMap<>();
            for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOList) {
                Integer returnOrderId = k3ReturnOrderDetailDO.getReturnOrderId();
                if (!k3ReturnOrderDOCatch.containsKey(returnOrderId)) {
                    k3ReturnOrderDOCatch.put(returnOrderId, k3ReturnOrderMapper.findById(returnOrderId));
                }
                K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderDOCatch.get(returnOrderId);
                if (k3ReturnOrderDO != null && DateUtil.daysBetween(reletOrderDO.getRentStartTime(), k3ReturnOrderDO.getReturnTime()) >= 0 && DateUtil.daysBetween(reletOrderDO.getExpectReturnTime(), k3ReturnOrderDO.getReturnTime()) <= 0) {
                    reletReturnOrderDetailDOList.add(k3ReturnOrderDetailDO);
                }
            }
            if (CollectionUtil.isNotEmpty(reletReturnOrderDetailDOList))
                clearReturnReturnOrderItems(reletReturnOrderDetailDOList, true);
        }


        ServiceResult<String, AmountNeedReturn> result = clearReletOrderStatement(reletOrderDO);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(result.getErrorCode());
            return serviceResult;
        }

        serviceResult = reletOrderStatement(reletOrderDO, true);
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return serviceResult;
        }

        //退货重新结算到具体结算单
        if (CollectionUtil.isNotEmpty(reletReturnOrderDetailDOList))
            statementReturnOrderItemRent(reletReturnOrderDetailDOList, false);

        //修正结算单时间范围
        fixCustomerStatementOrderStatementTime(reletOrderDO.getBuyerCustomerId());
        //最后退款
        AmountNeedReturn amountNeedReturn = result.getResult();
        if (amountNeedReturn != null && (BigDecimalUtil.compare(amountNeedReturn.getRentPaidAmount(), BigDecimal.ZERO) != 0 || BigDecimalUtil.compare(amountNeedReturn.getRentDepositPaidAmount(), BigDecimal.ZERO) != 0 || BigDecimalUtil.compare(amountNeedReturn.getDepositPaidAmount(), BigDecimal.ZERO) != 0 || BigDecimalUtil.compare(BigDecimalUtil.addAll(amountNeedReturn.getOtherPaidAmount(), amountNeedReturn.getOverduePaidAmount(), amountNeedReturn.getPenaltyPaidAmount()), BigDecimal.ZERO) != 0)) {
            String returnCode = paymentService.returnDepositExpand(reletOrderDO.getBuyerCustomerNo(), amountNeedReturn.getRentPaidAmount(), BigDecimalUtil.addAll(amountNeedReturn.getOtherPaidAmount(), amountNeedReturn.getOverduePaidAmount(), amountNeedReturn.getPenaltyPaidAmount())
                    , amountNeedReturn.getRentDepositPaidAmount(), amountNeedReturn.getDepositPaidAmount(), "续租单【" + reletOrderDO.getReletOrderNo() + "】重算结算单，已支付金额退还到客户余额");
            if (!ErrorCode.SUCCESS.equals(returnCode)) {
                result.setErrorCode(returnCode);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(returnCode);
                return serviceResult;
            }
            statementOrderSupport.sendReletOrderRestatementSuccess(reletOrderDO);
        }

        return serviceResult;
    }

    /**
     * 清除续租单的结算
     *
     * @param reletOrderNo
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, AmountNeedReturn> clearReletOrderStatement(String reletOrderNo) {
        ServiceResult<String, AmountNeedReturn> serviceResult = new ServiceResult<>();
        ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrderNo);
        if (reletOrderDO == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(ErrorCode.RELET_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        return clearReletOrderStatement(reletOrderDO);
    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, AmountNeedReturn> clearReletOrderStatement(ReletOrderDO reletOrderDO) {
        List<StatementOrderDetailDO> statementOrderDetailDOList = getReletOrderStatementDetails(reletOrderDO);

        //处理已支付订单
        //boolean paid = PayStatus.PAY_STATUS_PAID_PART.equals(reletOrderDO.getPayStatus()) || PayStatus.PAY_STATUS_PAID.equals(reletOrderDO.getPayStatus());
        //续租单默认已支付（考虑到脏数据，交了钱状态为未支付）
        boolean paid = true;
        //清除结算信息
        ServiceResult<String, AmountNeedReturn> result = clearStatement(paid, reletOrderDO.getBuyerCustomerNo(), statementOrderDetailDOList);
        //创建失败回滚
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        if (paid) {
            reletOrderDO.setPayStatus(PayStatus.PAY_STATUS_INIT);
            reletOrderDO.setTotalPaidOrderAmount(BigDecimal.ZERO);
            reletOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            reletOrderDO.setUpdateTime(new Date());
            reletOrderMapper.update(reletOrderDO);
        }
        return result;
    }

    @Override
    public ServiceResult<String, BigDecimal> batchReCreateReletOrderStatement(List<String> reletOrderNos) {
        ServiceResult<String, BigDecimal> serviceResult = new ServiceResult<>();
        if (CollectionUtil.isNotEmpty(reletOrderNos)) {
            BigDecimal needPay = BigDecimal.ZERO;
            for (String reletOrderNo : reletOrderNos) {
                ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(reletOrderNo);
                if (reletOrderDO == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.RELET_ORDER_NOT_EXISTS);
                    return serviceResult;
                }
                if (PayStatus.PAY_STATUS_PAID_PART.equals(reletOrderDO.getPayStatus()) || PayStatus.PAY_STATUS_PAID.equals(reletOrderDO.getPayStatus())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(ErrorCode.RELET_ORDER_HAS_PAID);
                    return serviceResult;
                }
                ServiceResult<String, BigDecimal> result = reCreateReletOrderStatement(reletOrderNo);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    serviceResult.setErrorCode(result.getErrorCode());
                    return serviceResult;
                }
                needPay = needPay.add(result.getResult());
            }
            serviceResult.setResult(needPay);
        }
        return serviceResult;
    }

    /**
     * 根据退货单项，清除退货租金结算
     *
     * @param k3ReturnOrderDetailDO
     */

    private void deleteK3ReturnOrderDetailRefRentStatement(K3ReturnOrderDetailDO k3ReturnOrderDetailDO, boolean paidReturn) {
        if (k3ReturnOrderDetailDO == null) return;
        Integer orderItemType = productSupport.isProduct(k3ReturnOrderDetailDO.getProductNo()) ? OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT : OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL;
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findReturnByOrderItemTypeAndId(orderItemType, k3ReturnOrderDetailDO.getId());
        if (CollectionUtil.isEmpty(statementOrderDetailDOList)) return;
        Map<Date, StatementOrderDO> statementOrderCatch = new HashMap<>();
        List<StatementOrderDetailDO> needDeleteList = new ArrayList<>();
        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
            //只处理租金（押金已在结清除订单结算时全部返还）
            if (!StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT.equals(statementOrderDetailDO.getStatementDetailType()))
                continue;
            Date dateKey = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementExpectPayTime());
            if (!statementOrderCatch.containsKey(dateKey)) {
                StatementOrderDO statementOrderDO = statementOrderMapper.findByCustomerAndPayTime(statementOrderDetailDO.getCustomerId(), dateKey);
                statementOrderCatch.put(dateKey, statementOrderDO);
            }
            StatementOrderDO statementOrderDO = statementOrderCatch.get(dateKey);
            if (statementOrderDO == null) continue;
            //结算了的跳过
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus()) && !paidReturn)
                continue;

            statementOrderDO.setStatementRentAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDO.getStatementRentAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE)));
            statementOrderDO.setStatementAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDO.getStatementAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.STANDARD_SCALE)));
            statementOrderMapper.update(statementOrderDO);
            needDeleteList.add(statementOrderDetailDO);
        }
        //删除结算详情
        if (CollectionUtil.isNotEmpty(needDeleteList))
            statementOrderDetailMapper.deleteStatementOrderDetailList(needDeleteList);
    }

    /**
     * 根据退货单商品项，结算租金（仅处理租金）
     *
     * @param k3ReturnOrderDetailDO
     */
    private void statementReturnOrderProductItemRent(K3ReturnOrderDetailDO k3ReturnOrderDetailDO, boolean ifDealDeposit) {
        if (k3ReturnOrderDetailDO == null || k3ReturnOrderDetailDO.getRealProductCount() <= 0) return;
        if (!productSupport.isProduct(k3ReturnOrderDetailDO.getProductNo())) return;
        OrderProductDO orderProductDO = productSupport.getOrderProductDO(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
        if (orderProductDO == null) return;

        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());

        if (CollectionUtil.isEmpty(statementOrderDetailDOList)) return;

        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findReturnOrderById(k3ReturnOrderDetailDO.getReturnOrderId());
        if(!CommonConstant.COMMON_CONSTANT_YES.equals(k3ReturnOrderDO.getSuccessStatus()))return;
        CustomerDO customerDO = customerMapper.findByNo(k3ReturnOrderDO.getK3CustomerNo());
        Integer buyerCustomerId = customerDO.getId();
        Date statementDetailStartTime;
        Date statementDetailEndTime;
        Map<String, StatementOrderDetailDO> addStatementOrderDetailDOMap = new HashMap<>();
        Date returnTime = k3ReturnOrderDO.getReturnTime();
        User loginUser = userSupport.getCurrentUser();
        Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();
        Date currentTime = new Date();
        BigDecimal returnCount = new BigDecimal(k3ReturnOrderDetailDO.getRealProductCount());

        //获取此订单是否有续租成功的记录
        boolean isReletOrder = reletOrderMapper.findRecentlyReletedOrderByOrderId(orderProductDO.getOrderId()) != null;

        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
            //结算了的跳过
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus()))
                continue;

            //押金结算(退货押金直接扣除)
            if (ifDealDeposit && StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderProductDO.getRentDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE), returnCount);
                BigDecimal thisReturnDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderProductDO.getDepositAmount(), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE), returnCount);
                reStatementOrderSubDeposit(loginUserId, returnTime, currentTime, statementOrderDetailDO, thisReturnRentDepositAmount, thisReturnDepositAmount);
                continue;
            }
            statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
            statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();


            BigDecimal payReturnAmount = BigDecimal.ZERO;
            BigDecimal reletCurrentPhaseReturnAmount = BigDecimal.ZERO;
            if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
//                // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
//                if (!OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && statementDetailStartTime.getTime() > returnTime.getTime()) {
//                    payReturnAmount = BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE);
//                }
                if (isReletOrder && statementOrderDetailDO.getReletOrderItemReferId() != null) {
                    ReletOrderProductDO reletOrderProductDO = reletOrderProductMapper.findById(statementOrderDetailDO.getReletOrderItemReferId());
                    ReletOrderDO reletOrder = reletOrderMapper.findById(reletOrderProductDO.getReletOrderId());
                    if (!isReletOrderRefReturn(returnTime, reletOrder))
                        continue;
                    Integer rentingProductCount = reletOrderProductDO.getRentingProductCount();
                    //计算续租当期退还金额
                    if (returnTime.getTime() >= statementDetailStartTime.getTime() && returnTime.getTime() <= statementDetailEndTime.getTime()) {
                        Integer dayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(returnTime, statementDetailEndTime);
                        Integer maxDayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(statementDetailStartTime, statementDetailEndTime) + 1;

                        reletCurrentPhaseReturnAmount = BigDecimalUtil.div(BigDecimalUtil.div(BigDecimalUtil.mul(new BigDecimal(dayCount),
                                BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount())), new BigDecimal(rentingProductCount), BigDecimalUtil.SCALE),
                                new BigDecimal(maxDayCount), BigDecimalUtil.SCALE);

                        payReturnAmount = BigDecimalUtil.add(reletCurrentPhaseReturnAmount, payReturnAmount);
                    }

                    // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
                    if (statementDetailStartTime.getTime() > returnTime.getTime()) {

                        payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(rentingProductCount), BigDecimalUtil.SCALE));
                    }
                } else {
                    // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
                    if (!OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) &&DateUtil.daysBetween(returnTime,statementDetailStartTime)>0) {
//                                payReturnAmount = statementOrderDetailDO.getStatementDetailAmount();
                        payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(orderProductDO.getProductCount()), BigDecimalUtil.SCALE));
                    }
                }
            }
            String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT + "-" + orderProductDO.getId() + "-" + orderProductDO.getProductSkuId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
            if (addStatementOrderDetailDOMap.containsKey(key)) {
                StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
            } else {
                StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT, k3ReturnOrderDetailDO.getId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, statementOrderDetailDO.getReletOrderItemReferId());
                if (thisStatementOrderDetailDO != null) {
                    thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                    thisStatementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
                    addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                }
            }
        }
        saveStatementOrder(new ArrayList(addStatementOrderDetailDOMap.values()), currentTime, loginUserId);
    }


    /**
     * 根据退货单物料项，结算租金（仅处理租金）
     *
     * @param k3ReturnOrderDetailDO
     */
    private void statementReturnOrderMaterialItemRent(K3ReturnOrderDetailDO k3ReturnOrderDetailDO, boolean ifDealDeposit) {
        if (k3ReturnOrderDetailDO == null || k3ReturnOrderDetailDO.getRealProductCount() <= 0) return;

        if (!productSupport.isMaterial(k3ReturnOrderDetailDO.getProductNo())) return;

        OrderMaterialDO orderMaterialDO = productSupport.getOrderMaterialDO(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
        if (orderMaterialDO == null) return;

        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {

            User loginUser = userSupport.getCurrentUser();
            Integer loginUserId = loginUser == null ? CommonConstant.SUPER_USER_ID : loginUser.getUserId();

            K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findReturnOrderById(k3ReturnOrderDetailDO.getReturnOrderId());
            CustomerDO customerDO = customerMapper.findByNo(k3ReturnOrderDO.getK3CustomerNo());
            Integer buyerCustomerId = customerDO.getId();

            Date statementDetailStartTime;
            Date statementDetailEndTime;
            Date returnTime = k3ReturnOrderDO.getReturnTime();
            Date currentTime = new Date();
            BigDecimal returnCount = new BigDecimal(k3ReturnOrderDetailDO.getRealProductCount());
            Map<String, StatementOrderDetailDO> addStatementOrderDetailDOMap = new HashMap<>();
            //获取此订单是否有续租成功的记录
            boolean isReletOrder = reletOrderMapper.findRecentlyReletedOrderByOrderId(orderMaterialDO.getOrderId()) != null;
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus()))
                    continue;
                //押金结算不处理
                if (ifDealDeposit && StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    BigDecimal thisReturnRentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderMaterialDO.getRentDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE), returnCount);
                    BigDecimal thisReturnDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.div(orderMaterialDO.getDepositAmount(), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE), returnCount);
                    reStatementOrderSubDeposit(loginUserId, returnTime, currentTime, statementOrderDetailDO, thisReturnRentDepositAmount, thisReturnDepositAmount);
                    continue;
                }

                statementDetailStartTime = statementOrderDetailDO.getStatementStartTime();
                statementDetailEndTime = statementOrderDetailDO.getStatementEndTime();

                BigDecimal payReturnAmount = BigDecimal.ZERO;
                BigDecimal reletCurrentPhaseReturnAmount = BigDecimal.ZERO;
                if (statementOrderDetailDO.getStatementDetailPhase() != 0) {
                    //计算续租 退还金额
                    if (isReletOrder && statementOrderDetailDO.getReletOrderItemReferId() != null) {
                        ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialMapper.findById(statementOrderDetailDO.getReletOrderItemReferId());
                        //退货不在此续租单内则跳过
                        ReletOrderDO reletOrder = reletOrderMapper.findById(reletOrderMaterialDO.getReletOrderId());
                        if (!isReletOrderRefReturn(returnTime, reletOrder))
                            continue;
                        Integer rentingMaterialCount = reletOrderMaterialDO.getRentingMaterialCount();
                        //计算续租当期退还金额
                        if (returnTime.getTime() >= statementDetailStartTime.getTime() && returnTime.getTime() <= statementDetailEndTime.getTime()) {
                            Integer dayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(returnTime, statementDetailEndTime);
                            Integer maxDayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(statementDetailStartTime, statementDetailEndTime) + 1;

                            reletCurrentPhaseReturnAmount = BigDecimalUtil.div(BigDecimalUtil.div(BigDecimalUtil.mul(new BigDecimal(dayCount),
                                    BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount())), new BigDecimal(rentingMaterialCount), BigDecimalUtil.SCALE),
                                    new BigDecimal(maxDayCount), BigDecimalUtil.SCALE);

                            payReturnAmount = BigDecimalUtil.add(reletCurrentPhaseReturnAmount, payReturnAmount);
                        }
                        // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
                        if (statementDetailStartTime.getTime() > returnTime.getTime()) {

                            payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(rentingMaterialCount), BigDecimalUtil.SCALE));
                        }

                    } else {
                        // 结算明细开始时间大于退货时间，则生成该明细对应的退货结算单明细
                        if (!OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && DateUtil.daysBetween(returnTime,statementDetailStartTime)>0) {
//                                payReturnAmount = statementOrderDetailDO.getStatementDetailAmount();
                            payReturnAmount = BigDecimalUtil.add(payReturnAmount, BigDecimalUtil.div(BigDecimalUtil.mul(returnCount, statementOrderDetailDO.getStatementDetailAmount()), new BigDecimal(orderMaterialDO.getMaterialCount()), BigDecimalUtil.SCALE));
                        }
                    }
                }
                String key = OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL + "-" + orderMaterialDO.getId() + "-" + orderMaterialDO.getMaterialId() + "-" + statementOrderDetailDO.getStatementExpectPayTime();
                if (addStatementOrderDetailDOMap.containsKey(key)) {
                    StatementOrderDetailDO thisStatementOrderDetailDO = addStatementOrderDetailDOMap.get(key);
                    thisStatementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                    thisStatementOrderDetailDO.setStatementDetailRentAmount(BigDecimalUtil.add(thisStatementOrderDetailDO.getStatementDetailRentAmount(), BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1))));
                    addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                } else {
                    StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_RETURN, k3ReturnOrderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL, k3ReturnOrderDetailDO.getId(), statementOrderDetailDO.getStatementExpectPayTime(), statementDetailStartTime, statementDetailEndTime, BigDecimalUtil.mul(payReturnAmount, new BigDecimal(-1)), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, statementOrderDetailDO.getReletOrderItemReferId());
                    if (thisStatementOrderDetailDO != null) {
                        thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                        thisStatementOrderDetailDO.setReturnReferId(statementOrderDetailDO.getId());
                        addStatementOrderDetailDOMap.put(key, thisStatementOrderDetailDO);
                    }
                }
            }
            List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
            if (!addStatementOrderDetailDOMap.isEmpty()) {
                for (Map.Entry<String, StatementOrderDetailDO> entry : addStatementOrderDetailDOMap.entrySet()) {
                    addStatementOrderDetailDOList.add(entry.getValue());
                }
            }
            saveStatementOrder(addStatementOrderDetailDOList, currentTime, loginUserId);
        }
    }

    /**
     * 重算时减去退货的押金（临时解决方案）
     *
     * @param loginUserId
     * @param returnTime
     * @param currentTime
     * @param statementOrderDetailDO
     * @param thisReturnRentDepositAmount
     * @param thisReturnDepositAmount
     */
    private void reStatementOrderSubDeposit(Integer loginUserId, Date returnTime, Date currentTime, StatementOrderDetailDO statementOrderDetailDO, BigDecimal thisReturnRentDepositAmount, BigDecimal thisReturnDepositAmount) {
        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
        if (BigDecimalUtil.compare(thisReturnDepositAmount, BigDecimal.ZERO) > 0) {
            statementOrderDetailDO.setStatementDetailDepositAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositAmount(), thisReturnDepositAmount));
            statementOrderDetailDO.setStatementDetailDepositReturnTime(currentTime);
            statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(), thisReturnDepositAmount));
            statementOrderDO.setStatementDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), thisReturnDepositAmount));
            statementOrderDO.setStatementAmount(BigDecimalUtil.sub(statementOrderDO.getStatementAmount(), thisReturnDepositAmount));
            statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_DEPOSIT, thisReturnDepositAmount, returnTime, loginUserId, currentTime);
        }
        if (BigDecimalUtil.compare(thisReturnRentDepositAmount, BigDecimal.ZERO) > 0) {
            statementOrderDetailDO.setStatementDetailRentDepositAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), thisReturnRentDepositAmount));
            statementOrderDetailDO.setStatementDetailRentDepositReturnTime(currentTime);
            statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailAmount(), thisReturnRentDepositAmount));
            statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), thisReturnRentDepositAmount));
            statementOrderDO.setStatementAmount(BigDecimalUtil.sub(statementOrderDO.getStatementAmount(), thisReturnRentDepositAmount));
            statementReturnSupport.saveStatementReturnRecord(statementOrderDO.getId(), statementOrderDO.getCustomerId(), statementOrderDetailDO.getOrderId(), statementOrderDetailDO.getOrderItemReferId(), StatementReturnType.RETURN_TYPE_RENT_DEPOSIT, thisReturnRentDepositAmount, returnTime, loginUserId, currentTime);
        }
        statementOrderDetailMapper.update(statementOrderDetailDO);
        statementOrderMapper.update(statementOrderDO);
    }

    /**
     * 仅重算退货单租金
     *
     * @param returnOrderNo
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> reStatementK3ReturnOrderRentOnly(String returnOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(returnOrderNo);
        if (k3ReturnOrderDO == null) {
            result.setErrorCode(ErrorCode.K3_RETURN_ORDER_IS_NOT_NULL);
            return result;
        }
        if (!CommonConstant.COMMON_CONSTANT_YES.equals(k3ReturnOrderDO.getSuccessStatus())) {
            result.setErrorCode(ErrorCode.ONLY_SUCCESS_RETURN_ORDER_ALLOW_RE_STATEMENT);
            return result;
        }
        // 客户为确认结算单状态时，不允许重算客户的订单
        CustomerDO customerDO = customerMapper.findByNo(k3ReturnOrderDO.getK3CustomerNo());
        if (customerDO != null && ConfirmStatementStatus.CONFIRM_STATUS_YES.equals(customerDO.getConfirmStatementStatus())) {
            result.setErrorCode(ErrorCode.CUSTOMER_CONFIRM_STATEMENT_REFUSE_RECREATE);
            return result;
        }
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOS = k3ReturnOrderDO.getK3ReturnOrderDetailDOList();
        //退货重新结算到具体结算单
        clearReturnReturnOrderItems(k3ReturnOrderDetailDOS, false);
        statementReturnOrderItemRent(k3ReturnOrderDetailDOS, false);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

//    /**
//     * 重结算算退货单项租金
//     * @param k3ReturnOrderDetailDOS
//     */
//    private void reStatementReturnOrderItems(List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOS,boolean ifDealDeposit) {
//        if (CollectionUtil.isNotEmpty(k3ReturnOrderDetailDOS)) {
//            for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOS) {
//                deleteK3ReturnOrderDetailRefRentStatement(k3ReturnOrderDetailDO);
//                if (productSupport.isProduct(k3ReturnOrderDetailDO.getProductNo()))
//                    statementReturnOrderProductItemRent(k3ReturnOrderDetailDO,ifDealDeposit);
//                else statementReturnOrderMaterialItemRent(k3ReturnOrderDetailDO,ifDealDeposit);
//            }
//        }
//    }


    /**
     * 导入对账单查询
     *
     * @Author : XiaoLuYu
     * @Date : Created in 2018/6/21 9:29
     */
    @Override
    public ServiceResult<String, List<CheckStatementOrder>> exportQueryStatementOrderCheckParam(StatementOrderMonthQueryParam statementOrderMonthQueryParam) {
        ServiceResult<String, List<CheckStatementOrder>> result = new ServiceResult<>();

        CustomerDO customerDO = customerMapper.findByNo(statementOrderMonthQueryParam.getStatementOrderCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
//        Date statementOrderStartTime = statementOrderMonthQueryParam.getStatementOrderStartTime();
        Date statementOrderEndTime = statementOrderMonthQueryParam.getStatementOrderEndTime();
        statementOrderMonthQueryParam = new StatementOrderMonthQueryParam();
        statementOrderMonthQueryParam.setStatementOrderCustomerId(customerDO.getId());
//        statementOrderMonthQueryParam.setStatementOrderStartTime(statementOrderStartTime);
        statementOrderMonthQueryParam.setStatementOrderEndTime(statementOrderEndTime);
        Map<String, Object> maps = new HashMap<>();
        maps.put("statementOrderMonthQueryParam", statementOrderMonthQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
        //todo 查处结算单总表
        List<CheckStatementOrderDO> statementOrderDOList = statementOrderMapper.exportListMonthPage(maps);
        //todo 查处结算单详情
        StatementOrderDetailQueryParam statementOrderDetailQueryParam = new StatementOrderDetailQueryParam();
        statementOrderDetailQueryParam.setCustomerId(customerDO.getId());
        statementOrderDetailQueryParam.setStatementOrderStartTime(statementOrderMonthQueryParam.getStatementOrderStartTime());
        statementOrderDetailQueryParam.setStatementOrderEndTime(statementOrderMonthQueryParam.getStatementOrderEndTime());
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderDetailQueryParam", statementOrderDetailQueryParam);
        //查出有结算单的订单商品
        List<CheckStatementOrderDetailDO> listPage = statementOrderDetailMapper.exportListPage(maps);
        //查出有结算单的订单配件
        List<CheckStatementOrderDetailDO> listPage1 = statementOrderDetailMapper.exportListPage1(maps);
        //查出没有结算单的订单商品
        List<CheckStatementOrderDetailDO> listPage2 = statementOrderDetailMapper.exportListPage2(maps);
        //查出没有结算单的订单配件
        List<CheckStatementOrderDetailDO> listPage3 = statementOrderDetailMapper.exportListPage3(maps);
        listPage.addAll(listPage1);
        listPage.addAll(listPage2);
        listPage.addAll(listPage3);

        if (statementOrderDOList == null || CollectionUtil.isEmpty(statementOrderDOList)) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        List<CheckStatementOrderDetailDO> returnListPage = statementOrderDetailMapper.exportReturnListPage(maps);
        if (CollectionUtil.isNotEmpty(returnListPage)) {
            for (CheckStatementOrderDetailDO checkStatementOrderDetailDO : returnListPage) {
                listPage.add(checkStatementOrderDetailDO);
            }
        }
        //退货
        List<K3ReturnOrderDO> k3ReturnOrderDOList = k3ReturnOrderMapper.findByCustomerNoForExport(customerDO.getCustomerNo());
        Map<Integer, Map<String, Integer>> returnDetailIdMap = new HashMap<>();
        Map<Integer,Integer> returnCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDOList)) {
            for (K3ReturnOrderDO k3ReturnOrderDO : k3ReturnOrderDOList) {
                if (CollectionUtil.isNotEmpty(k3ReturnOrderDO.getK3ReturnOrderDetailDOList())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                    Date returnTime = k3ReturnOrderDO.getReturnTime();
                    String returnTimeString = simpleDateFormat.format(returnTime);
                    for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDO.getK3ReturnOrderDetailDOList()) {
                        returnCountMap.put(k3ReturnOrderDetailDO.getId(),k3ReturnOrderDetailDO.getRealProductCount());
                        String productNo = k3ReturnOrderDetailDO.getProductNo();
                        Integer OrderItemId = Integer.parseInt(k3ReturnOrderDetailDO.getOrderItemId());
                        if (productSupport.isMaterial(productNo)) {
                            OrderItemId = productSupport.getMaterialId(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                        } else {
                            OrderItemId = productSupport.getProductId(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                        }
                        if (!returnDetailIdMap.containsKey(OrderItemId)) {
                            Map<String, Integer> returnDataMap = new HashMap<>();
                            returnDataMap.put(returnTimeString, k3ReturnOrderDetailDO.getProductCount());
                            returnDetailIdMap.put(OrderItemId, returnDataMap);
                            //老订单获取商品项配件项ID
//                            String productNo = k3ReturnOrderDetailDO.getProductNo();
//                            if (productSupport.isMaterial(productNo)) {
//                                Integer OrderItemId = productSupport.getMaterialId(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
//                                returnDetailIdMap.put(OrderItemId, returnDataMap);
//                            } else {
//                                Integer OrderItemId = productSupport.getProductId(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
//                                returnDetailIdMap.put(OrderItemId, returnDataMap);
//                            }
                        } else {
                            Map<String, Integer> returnDataMap = returnDetailIdMap.get(OrderItemId);
                            if (!returnDataMap.containsKey(returnTimeString)) {
                                returnDataMap.put(returnTimeString, k3ReturnOrderDetailDO.getProductCount());
                            } else {
                                Integer count = k3ReturnOrderDetailDO.getProductCount() + returnDataMap.get(returnTimeString);
                                returnDataMap.put(returnTimeString, count);
                            }
                        }
                    }
                }
            }
        }
        List<CheckStatementOrderDetailDO> firstReturnListPage = statementOrderDetailMapper.exportFirstReturnListPage(maps);
        // TODO: 2018\7\5 0005 新逻辑  将所有第一次创建的放到里面
        Map<String,Map<Integer,List<CheckStatementOrderDetailDO>>> firstReturnMonthMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(firstReturnListPage)) {
            for (CheckStatementOrderDetailDO checkStatementOrderDetailDO : firstReturnListPage) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                Date statementExpectPayTime = checkStatementOrderDetailDO.getStatementExpectPayTime();
                String monthTimeString = simpleDateFormat.format(statementExpectPayTime);
                if (!firstReturnMonthMap.containsKey(monthTimeString)) {
                    Map<Integer,List<CheckStatementOrderDetailDO>> itemIdMap = new HashMap<>();
                    List<CheckStatementOrderDetailDO> returnCheckStatementOrderDetailDOList = new ArrayList<>();
                    returnCheckStatementOrderDetailDOList.add(checkStatementOrderDetailDO);
                    StatementOrderDetailDO byStatementOrderId = statementOrderDetailMapper.findById(checkStatementOrderDetailDO.getReturnReferId());
                    itemIdMap.put(byStatementOrderId.getOrderItemReferId(),returnCheckStatementOrderDetailDOList);
                    firstReturnMonthMap.put(monthTimeString,itemIdMap);
                }else {
                    Map<Integer,List<CheckStatementOrderDetailDO>> itemIdMap = firstReturnMonthMap.get(monthTimeString);
                    StatementOrderDetailDO byStatementOrderId = statementOrderDetailMapper.findById(checkStatementOrderDetailDO.getReturnReferId());
                    if (!itemIdMap.containsKey(byStatementOrderId.getOrderItemReferId())) {
                        List<CheckStatementOrderDetailDO> returnCheckStatementOrderDetailDOList = new ArrayList<>();
                        returnCheckStatementOrderDetailDOList.add(checkStatementOrderDetailDO);
                        itemIdMap.put(byStatementOrderId.getOrderItemReferId(),returnCheckStatementOrderDetailDOList);
                    }else {
                        List<CheckStatementOrderDetailDO> returnCheckStatementOrderDetailDOList = itemIdMap.get(byStatementOrderId.getOrderItemReferId());
                        returnCheckStatementOrderDetailDOList.add(checkStatementOrderDetailDO);
                    }
                }
            }
        }
        
        for (CheckStatementOrderDO exportStatementOrderDO : statementOrderDOList) {
            List<CheckStatementOrderDetailDO> checkStatementOrderDetailDOList = exportStatementOrderDO.getCheckStatementOrderDetailDOList();
            if (checkStatementOrderDetailDOList == null) {
                checkStatementOrderDetailDOList = new ArrayList<>();
            }
            for (CheckStatementOrderDetailDO checkStatementOrderDetailDO : listPage) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                Date statementOrderDetailExpectPayTime = checkStatementOrderDetailDO.getStatementExpectPayTime();
                if (statementOrderDetailExpectPayTime != null) {
                    String statementOrderDetailExpectPayTimeString = simpleDateFormat.format(statementOrderDetailExpectPayTime);
                    String monthTime = exportStatementOrderDO.getMonthTime();
                    if (monthTime.equals(statementOrderDetailExpectPayTimeString)) {
                        checkStatementOrderDetailDOList.add(checkStatementOrderDetailDO);
                    }
                }
            }
            exportStatementOrderDO.setCheckStatementOrderDetailDOList(checkStatementOrderDetailDOList);
        }

        List<CheckStatementOrder> checkStatementOrderList = new ArrayList<>();
        for (CheckStatementOrderDO exportStatementOrderDO : statementOrderDOList) {

            CheckStatementOrder statementOrder = ConverterUtil.convert(exportStatementOrderDO, CheckStatementOrder.class);
            List<CheckStatementOrderDetailDO> checkStatementOrderDetailDOList = exportStatementOrderDO.getCheckStatementOrderDetailDOList();
            if (CollectionUtil.isNotEmpty(exportStatementOrderDO.getCheckStatementOrderDetailDOList())) {
                List<CheckStatementOrderDetail> list = new ArrayList<>();
                for (CheckStatementOrderDetailDO checkStatementOrderDetailDO : checkStatementOrderDetailDOList) {
                    CheckStatementOrderDetail checkStatementOrderDetail = ConverterUtil.convert(checkStatementOrderDetailDO, CheckStatementOrderDetail.class);
                    checkStatementOrderDetail.setStatementOrderDetailId(checkStatementOrderDetailDO.getId());
                    checkStatementOrderDetail.setStatementOrderId(checkStatementOrderDetailDO.getStatementOrderId());
                    checkStatementOrderDetail.setOrderItemReferId(checkStatementOrderDetailDO.getOrderItemReferId());
                    checkStatementOrderDetail.setReturnReferId(checkStatementOrderDetailDO.getReturnReferId());
                    list.add(checkStatementOrderDetail);
                }
                statementOrder.setStatementOrderDetailList(list);
            }


            CheckStatementOrderDetail returnReferStatementOrderDetail = null;

            //获取各项的总和,区分了各商品
            Map<String, CheckStatementOrderDetail> hashMap = new HashMap<>();

            if (statementOrder != null && CollectionUtil.isNotEmpty(statementOrder.getStatementOrderDetailList())) {
                Map<Integer, CheckStatementOrderDetail> statementOrderDetailMap = ListUtil.listToMap(statementOrder.getStatementOrderDetailList(), "statementOrderDetailId");

                Map<String, Date[]> statementTimeMap = new HashMap<>();
                for (CheckStatementOrderDetail statementOrderDetail : statementOrder.getStatementOrderDetailList()) {
                    if (statementOrderDetail.getReturnReferId() != null) {
                        returnReferStatementOrderDetail = statementOrderDetailMap.get(statementOrderDetail.getReturnReferId());
                    }
                    exportConvertStatementOrderDetailOtherInfo(statementOrderDetail, returnReferStatementOrderDetail, null);

                    String key = null;
                    if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
                        Integer payMode = 0;
                        //为订单商品时
                        if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                            OrderProductDO orderProductDO = orderProductMapper.findById(statementOrderDetail.getOrderItemReferId());
                            if (orderProductDO != null) {
                                Integer productId = orderProductDO.getProductId();
                                Integer isNewProduct = orderProductDO.getIsNewProduct();
                                key = statementOrderDetail.getOrderItemReferId() + statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + productId + "-" + isNewProduct + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                                payMode = orderProductDO.getPayMode();
                            }
                        }
                        //为订单物料时
                        if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                            OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(statementOrderDetail.getOrderItemReferId());
                            if (orderMaterialDO != null) {
                                Integer materialId = orderMaterialDO.getMaterialId();
                                Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                                key = statementOrderDetail.getOrderItemReferId() + statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + materialId + "-" + isNewMaterial + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                                payMode = orderMaterialDO.getPayMode();
                            }
                        }
                        if ((OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())
                                || OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType()))
                                && payMode != 0) {

                            // 查询本期订单 只处理商品和配件
                            // 如果本期账单租金为0，则去找应该什么时候支付
                            if ((statementOrderDetail.getStatementDetailType() == null || StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetail.getStatementDetailType())) && BigDecimalUtil.compare(statementOrderDetail.getStatementDetailRentAmount(), BigDecimal.ZERO) == 0) {
                                Map<String, Object> thisPeriodsByOrderInfoMap = statementOrderDetailMapper.findThisPeriodsByOrderInfo(statementOrderDetail.getOrderId(), statementOrderDetail.getOrderItemReferId(), statementOrderDetail.getStatementExpectPayTime(), payMode);
                                if (thisPeriodsByOrderInfoMap != null && thisPeriodsByOrderInfoMap.size() != 0) {
                                    statementOrderDetail.setStatementDetailRentEndAmount(new BigDecimal(thisPeriodsByOrderInfoMap.get("statementDetailRentAmount").toString()));
                                    statementOrderDetail.setStatementDetailEndAmount(new BigDecimal(thisPeriodsByOrderInfoMap.get("statementDetailAmount").toString()));
                                    try {
                                        statementOrderDetail.setStatementExpectPayEndTime(new SimpleDateFormat("yyyy-MM-dd").parse(thisPeriodsByOrderInfoMap.get("statementExpectPayTime").toString()));
                                        statementOrderDetail.setStatementStartTime(new SimpleDateFormat("yyyy-MM-dd").parse(thisPeriodsByOrderInfoMap.get("statementStartTime").toString()));
                                        statementOrderDetail.setStatementEndTime(new SimpleDateFormat("yyyy-MM-dd").parse(thisPeriodsByOrderInfoMap.get("statementEndTime").toString()));

                                        if (statementTimeMap.get(statementOrderDetail.getOrderId().toString()) == null) {
                                            Date[] dateArr = new Date[2];
                                            dateArr[0] = statementOrderDetail.getStatementStartTime();
                                            dateArr[1] = statementOrderDetail.getStatementEndTime();
                                            statementTimeMap.put(statementOrderDetail.getOrderId().toString(), dateArr);
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            if ((statementOrderDetail.getStatementStartTime() == null
                                    || statementOrderDetail.getStatementEndTime() == null) && statementTimeMap.get(statementOrderDetail.getOrderId().toString()) != null) {
                                Date[] dateArr = statementTimeMap.get(statementOrderDetail.getOrderId().toString());
                                if (dateArr != null && dateArr.length >= 2) {
                                    statementOrderDetail.setStatementStartTime(dateArr[0]);
                                    statementOrderDetail.setStatementEndTime(dateArr[1]);
                                }
                            }
                        }
                        //为订单其他时
                        if (OrderItemType.ORDER_ITEM_TYPE_OTHER.equals(statementOrderDetail.getOrderItemType())) {
                            key = statementOrderDetail.getOrderItemReferId() + statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                            hashMap.put(key, statementOrderDetail);
                            continue;
                        }
                    }
                    if (OrderType.ORDER_TYPE_RETURN.equals(statementOrderDetail.getOrderType())) {
                        //为退还商品时
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                            K3ReturnOrderDetailDO k3ReturnOrderDetailDO = k3ReturnOrderDetailMapper.findById(statementOrderDetail.getOrderItemReferId());
                            if (k3ReturnOrderDetailDO != null) {
//                                OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetailDO.getOrderNo());
//                                OrderProductDO orderProductDO = null;
//                                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order()) && k3ReturnOrderDetailDO.getOrderEntry() != null) {
//                                    orderProductDO = orderProductMapper.findK3OrderProduct(orderDO.getId(), Integer.parseInt(k3ReturnOrderDetailDO.getOrderEntry()));
//                                } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderDO.getIsK3Order())) {
//                                    String orderItemId = k3ReturnOrderDetailDO.getOrderItemId();
//                                    orderProductDO = orderProductMapper.findById(Integer.parseInt(orderItemId));
//                                }
                                OrderProductDO orderProductDO = productSupport.getOrderProductDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                                if (orderProductDO != null) {
                                    Integer productId = orderProductDO.getProductId();
                                    Integer isNewProduct = orderProductDO.getIsNewProduct();
                                    key = statementOrderDetail.getOrderItemReferId() + statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + productId + "-" + isNewProduct + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                                }
                            }
                        }
                        //为退还物料时
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                            K3ReturnOrderDetailDO k3ReturnOrderDetailDO = k3ReturnOrderDetailMapper.findById(statementOrderDetail.getOrderItemReferId());
                            if (k3ReturnOrderDetailDO != null) {
//                                OrderDO orderDO = orderMapper.findByOrderNo(k3ReturnOrderDetailDO.getOrderNo());
//                                OrderMaterialDO orderMaterialDO = null;
//                                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order()) && k3ReturnOrderDetailDO.getOrderEntry() != null) {
//                                    orderMaterialDO = orderMaterialMapper.findK3OrderMaterial(orderDO.getId(), Integer.parseInt(k3ReturnOrderDetailDO.getOrderEntry()));
//                                } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderDO.getIsK3Order())) {
//                                    String orderItemId = k3ReturnOrderDetailDO.getOrderItemId();
//                                    orderMaterialDO = orderMaterialMapper.findById(Integer.parseInt(orderItemId));
//                                }
                                OrderMaterialDO orderMaterialDO = productSupport.getOrderMaterialDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                                if (orderMaterialDO != null) {
                                    Integer materialId = orderMaterialDO.getMaterialId();
                                    Integer isNewMaterial = orderMaterialDO.getIsNewMaterial();
                                    key = statementOrderDetail.getOrderItemReferId() + statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + materialId + "-" + isNewMaterial + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType();
                                }
                            }
                        }
                        //为退还物料时
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_OTHER.equals(statementOrderDetail.getOrderItemType())) {
                            key = statementOrderDetail.getOrderItemType() + "-" + statementOrderDetail.getOrderType() + "-" + statementOrderDetail.getOrderNo() + "-" + statementOrderDetail.getItemRentType() + "-" + statementOrderDetail.getOrderItemReferId();
                        }
                        statementOrderDetail.setItemCount(statementOrderDetail.getItemCount() == null ? 0 : statementOrderDetail.getItemCount() * -1);
                    }
                    if (key == null) {
                        continue;
                    }

                    //各商品物料
                    CheckStatementOrderDetail newStatementOrderDetail = hashMap.get(key);
                    if (newStatementOrderDetail != null) {
                        if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetail.getStatementDetailType())) {
                            newStatementOrderDetail.setStatementOrderDetailId(statementOrderDetail.getStatementOrderDetailId());
                            if (statementOrderDetail.getReletOrderItemReferId() != null) {
                                newStatementOrderDetail.setReletOrderItemReferId(statementOrderDetail.getReletOrderItemReferId());
                            }
                        }
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

            for (CheckStatementOrderDetail statementOrderDetail : hashMap.values()) {
                BigDecimal statementDetailPaidAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementOrderDetail.getStatementDetailDepositPaidAmount(), statementOrderDetail.getStatementDetailOverduePaidAmount()), statementOrderDetail.getStatementDetailRentDepositPaidAmount()), statementOrderDetail.getStatementDetailRentPaidAmount()), statementOrderDetail.getStatementDetailOtherPaidAmount());
                statementOrderDetail.setStatementDetailPaidAmount(statementDetailPaidAmount);
            }

            List<CheckStatementOrderDetail> statementOrderDetailList = ListUtil.mapToList(hashMap);

            //k3老订单支付截止时间限制
//            List<K3OrderStatementConfigDO> k3OrderStatementConfigList = k3OrderStatementConfigMapper.findByCustomerId(customerDO.getId());
//            if (CollectionUtil.isNotEmpty(k3OrderStatementConfigList)) {
//                Map<Integer,K3OrderStatementConfigDO> k3OrderStatementConfigMap = ListUtil.listToMap(k3OrderStatementConfigList,"orderId");
//                List<CheckStatementOrderDetail> notK3k3OrderStatementConfigList = new ArrayList<>();
//                for (CheckStatementOrderDetail checkStatementOrderDetail : statementOrderDetailList) {
//                    //是订单进行查询
//                    if (OrderType.ORDER_TYPE_ORDER.equals(checkStatementOrderDetail.getOrderType())) {
//                        K3OrderStatementConfigDO k3OrderStatementConfigDO = k3OrderStatementConfigMap.get(checkStatementOrderDetail.getOrderId());
//                        if (k3OrderStatementConfigDO != null) {
//                            //统一日期格式进行比较
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
//                            String k3RentStartTimeString = simpleDateFormat.format(k3OrderStatementConfigDO.getRentStartTime());
//                            String monthTimeString = statementOrder.getMonthTime();
//                            try {
//                                Date k3RentStartTime = simpleDateFormat.parse(k3RentStartTimeString);
//                                Date monthTime = simpleDateFormat.parse(monthTimeString);
//                                if (monthTime.after(k3RentStartTime)) {
//                                    notK3k3OrderStatementConfigList.add(checkStatementOrderDetail);
//                                } else if (k3RentStartTime.equals(monthTime)) {
////                                    checkStatementOrderDetail.setStatementStartTime(k3OrderStatementConfigDO.getRentStartTime());
//                                    notK3k3OrderStatementConfigList.add(checkStatementOrderDetail);
//                                }
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                                logger.error("【导出对账单功能————筛选K3老订单支付截止时间之前结算单出错，错误原因对比结算单结束时间和K3老订单支付截止时间出错】，结算单id:【"+checkStatementOrderDetail.getStatementOrderDetailId()+"】,k3老订单(erp_k3_order_statement_config)订单编号：【"+k3OrderStatementConfigDO.getOrderNo()+"】",e);
//                            }
//                        }else {
//                            notK3k3OrderStatementConfigList.add(checkStatementOrderDetail);
//                        }
//                    }else {
//                        notK3k3OrderStatementConfigList.add(checkStatementOrderDetail);
//                    }
//                }
//                statementOrderDetailList = notK3k3OrderStatementConfigList;
//            }
            //排序
            if (CollectionUtil.isNotEmpty(statementOrderDetailList)) {
                statementOrderDetailList = sortingCheckStatementOrderDetail(statementOrderDetailList);
                statementOrder.setStatementOrderDetailList(statementOrderDetailList);
                checkStatementOrderList.add(statementOrder);
            }
        }
        //退货日期之后减数量
        if (CollectionUtil.isNotEmpty(checkStatementOrderList)) {
            for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
                for (CheckStatementOrderDetail checkStatementOrderDetail : checkStatementOrder.getStatementOrderDetailList()) {
                    if (returnDetailIdMap.get(checkStatementOrderDetail.getOrderItemReferId()) != null) {
                        Map<String, Integer> returnDataMap = returnDetailIdMap.get(checkStatementOrderDetail.getOrderItemReferId());
                        for (String returnTimeString : returnDataMap.keySet()) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                            try {
                                Date returnStartTime = simpleDateFormat.parse(returnTimeString);
                                Date monthTime = simpleDateFormat.parse(checkStatementOrder.getMonthTime());
                                if (monthTime.after(returnStartTime)) {
                                    Integer count = checkStatementOrderDetail.getItemCount() - returnDataMap.get(returnTimeString);
                                    if (count < 0) {
                                        count = 0;
                                    }
                                    checkStatementOrderDetail.setItemCount(count);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        if (!firstReturnMonthMap.isEmpty()) {
            //插入退货的第一期
            for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
                if (firstReturnMonthMap.containsKey(checkStatementOrder.getMonthTime())) {
                    Map<Integer, List<CheckStatementOrderDetailDO>> itemIdMap = firstReturnMonthMap.get(checkStatementOrder.getMonthTime());
                    List<CheckStatementOrderDetail> statementOrderDetailList = checkStatementOrder.getStatementOrderDetailList();
                    List<CheckStatementOrderDetail> newList = new ArrayList<>();
                    for (CheckStatementOrderDetail checkStatementOrderDetail:statementOrderDetailList){
                        if (!itemIdMap.containsKey(checkStatementOrderDetail.getOrderItemReferId())) {
                            newList.add(checkStatementOrderDetail);
                        }else {
                            newList.add(checkStatementOrderDetail);
                            List<CheckStatementOrderDetailDO> returnCheckStatementOrderDetailDOList = itemIdMap.get(checkStatementOrderDetail.getOrderItemReferId());
                            List<CheckStatementOrderDetail> returnCheckStatementOrderDetailList = ConverterUtil.convertList(returnCheckStatementOrderDetailDOList,CheckStatementOrderDetail.class);
                            for (CheckStatementOrderDetail detail:returnCheckStatementOrderDetailList) {
                                detail.setStatementStartTime(detail.getStatementExpectPayTime());
                                detail.setStatementEndTime(detail.getStatementExpectPayTime());
                                detail.setOrderExpectReturnTime(detail.getStatementExpectPayTime());
                                detail.setStatementDetailPaidTime(detail.getStatementExpectPayTime());
                                detail.setOrderRentStartTime(detail.getStatementExpectPayTime());
                                detail.setOrderRentTimeLength(null);
                                detail.setItemName(checkStatementOrderDetail.getItemName());
                                detail.setItemSkuName(checkStatementOrderDetail.getItemSkuName());
                                detail.setUnitAmount(checkStatementOrderDetail.getUnitAmount());
                                detail.setStatementDetailAmount(BigDecimal.ZERO);
                                detail.setReturnReferId(checkStatementOrderDetail.getStatementOrderDetailId());
                            }
                            newList.addAll(returnCheckStatementOrderDetailList);
                        }
                    }
                    if (CollectionUtil.isNotEmpty(newList)) {
                        checkStatementOrder.setStatementOrderDetailList(newList);
                    }
                }
            }
            //存储生成的第一期的退货数量
            for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
                List<CheckStatementOrderDetail> statementOrderDetailList = checkStatementOrder.getStatementOrderDetailList();
                if (CollectionUtil.isNotEmpty(statementOrderDetailList)) {
                    for (CheckStatementOrderDetail checkStatementOrderDetail:statementOrderDetailList){
                        if (OrderType.ORDER_TYPE_RETURN.equals(checkStatementOrderDetail.getOrderType())) {
                            if (returnCountMap.containsKey(checkStatementOrderDetail.getOrderItemReferId())) {
                                Integer count = 0-returnCountMap.get(checkStatementOrderDetail.getOrderItemReferId());
                                checkStatementOrderDetail.setItemCount(count);
                            }
                        }
                    }
                }
            }
        }
        //去掉商品数量为零，钱也为零的项
        for (CheckStatementOrder checkStatementOrder : checkStatementOrderList) {
            List<CheckStatementOrderDetail> lastList = new ArrayList<>();
            for (CheckStatementOrderDetail checkStatementOrderDetail : checkStatementOrder.getStatementOrderDetailList()) {
                if (checkStatementOrderDetail.getItemCount()!= 0) {
                    lastList.add(checkStatementOrderDetail);
                }else if(BigDecimalUtil.compare(checkStatementOrderDetail.getStatementDetailAmount(),BigDecimal.ZERO) != 0 ||
                        BigDecimalUtil.compare(checkStatementOrderDetail.getStatementDetailRentDepositAmount(),BigDecimal.ZERO) != 0 ||
                        BigDecimalUtil.compare(checkStatementOrderDetail.getStatementDetailRentAmount(),BigDecimal.ZERO) != 0 ||
                        BigDecimalUtil.compare(checkStatementOrderDetail.getStatementDetailEndAmount(),BigDecimal.ZERO) != 0 ||
                        BigDecimalUtil.compare(checkStatementOrderDetail.getStatementDetailRentEndAmount(),BigDecimal.ZERO) != 0 ||
                        BigDecimalUtil.compare(checkStatementOrderDetail.getStatementDetailCorrectAmount(),BigDecimal.ZERO) != 0 ||
                        BigDecimalUtil.compare(checkStatementOrderDetail.getStatementDetailOtherAmount(),BigDecimal.ZERO) != 0){
                    lastList.add(checkStatementOrderDetail);
                }
            }
            checkStatementOrder.setStatementOrderDetailList(lastList);
        }
        result.setResult(checkStatementOrderList);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private List<CheckStatementOrderDetail> sortingCheckStatementOrderDetail(List<CheckStatementOrderDetail> statementOrderDetailList) {
        //存放非退货单结算单详情项
//        List<StatementOrderDetail> notReturnOrderList = new ArrayList<>();
        Map<Integer, CheckStatementOrderDetail> notReturnOrderMap = new TreeMap<>();
        //存放最终结果
        List<CheckStatementOrderDetail> allList = new ArrayList<>();
        //存放退货结算单详情项其它费用项
        List<CheckStatementOrderDetail> returnOrderOtherList = new ArrayList<>();
        //存放退货结算单商品、配件项
        Map<Integer, List<CheckStatementOrderDetail>> returnOrderProudctAndMaterialMap = new HashMap<>();
        for (CheckStatementOrderDetail statementOrderDetail : statementOrderDetailList) {
            if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {
                notReturnOrderMap.put(statementOrderDetail.getStatementOrderDetailId(), statementOrderDetail);
            } else {
                if (null == statementOrderDetail.getReturnReferId()) {
                    returnOrderOtherList.add(statementOrderDetail);
                } else {
                    if (null != returnOrderProudctAndMaterialMap.get(statementOrderDetail.getReturnReferId())) {
                        returnOrderProudctAndMaterialMap.get(statementOrderDetail.getReturnReferId()).add(statementOrderDetail);
                    } else {
                        List<CheckStatementOrderDetail> returnOrderProudctAndMaterialList = new ArrayList<>();
                        returnOrderProudctAndMaterialList.add(statementOrderDetail);
                        returnOrderProudctAndMaterialMap.put(statementOrderDetail.getReturnReferId(), returnOrderProudctAndMaterialList);
                    }
                }
            }
        }
        if (CollectionUtil.isNotEmpty(returnOrderOtherList)) {
            allList.addAll(returnOrderOtherList);
        }
        for (Integer statementOrderDetailId : notReturnOrderMap.keySet()) {

            List list = new ArrayList();
            CheckStatementOrderDetail statementOrderDetail = notReturnOrderMap.get(statementOrderDetailId);
            list.add(statementOrderDetail);

            List<CheckStatementOrderDetail> statementOrderDetails = returnOrderProudctAndMaterialMap.get(statementOrderDetail.getStatementOrderDetailId());
            if (CollectionUtil.isNotEmpty(statementOrderDetails)) {
                //这里添加退货单合并逻辑
                BigDecimal statementDetailAmount = BigDecimal.ZERO;
                BigDecimal statementDetailRentAmount = BigDecimal.ZERO;
                BigDecimal statementDetailDepositAmount = BigDecimal.ZERO;
                BigDecimal statementDetailOtherAmount = BigDecimal.ZERO;
                BigDecimal statementDetailPaidAmount = BigDecimal.ZERO;
                BigDecimal statementDetailPenaltyAmount = BigDecimal.ZERO;
                BigDecimal statementDetailPenaltyPaidAmount = BigDecimal.ZERO;
                BigDecimal statementDetailRentDepositAmount = BigDecimal.ZERO;
                for (CheckStatementOrderDetail checkStatementOrderDetail : statementOrderDetails) {
                    if (BigDecimalUtil.compare(checkStatementOrderDetail.getStatementDetailAmount(), BigDecimal.ZERO) == 0) {
                        //判断金额，如果金额为0，则直接保存
                        list.add(checkStatementOrderDetail);
                    } else {
                        //如果金额不为0，将数量进行累加，然后跟最外层的订单对账单进行相减，金额相加，之后退货单不存储
                        statementDetailAmount = BigDecimalUtil.add(statementDetailAmount, checkStatementOrderDetail.getStatementDetailAmount());
                        statementDetailRentAmount = BigDecimalUtil.add(statementDetailRentAmount, checkStatementOrderDetail.getStatementDetailRentAmount());
                        statementDetailDepositAmount = BigDecimalUtil.add(statementDetailDepositAmount, checkStatementOrderDetail.getStatementDetailDepositAmount());
                        statementDetailOtherAmount = BigDecimalUtil.add(statementDetailOtherAmount, checkStatementOrderDetail.getStatementDetailOtherAmount());
                        statementDetailPaidAmount = BigDecimalUtil.add(statementDetailPaidAmount, checkStatementOrderDetail.getStatementDetailPaidAmount());
                        statementDetailPenaltyAmount = BigDecimalUtil.add(statementDetailPenaltyAmount, checkStatementOrderDetail.getStatementDetailPenaltyAmount());
                        statementDetailPenaltyPaidAmount = BigDecimalUtil.add(statementDetailPenaltyPaidAmount, checkStatementOrderDetail.getStatementDetailPenaltyPaidAmount());
                        statementDetailRentDepositAmount = BigDecimalUtil.add(statementDetailRentDepositAmount, checkStatementOrderDetail.getStatementDetailRentDepositAmount());
                    }
                }
                statementOrderDetail.setStatementDetailAmount(BigDecimalUtil.add(statementDetailAmount, statementOrderDetail.getStatementDetailAmount()));
                statementOrderDetail.setStatementDetailAmount(BigDecimalUtil.compare(statementOrderDetail.getStatementDetailAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : statementOrderDetail.getStatementDetailAmount());
                statementOrderDetail.setStatementDetailRentAmount(BigDecimalUtil.add(statementDetailRentAmount, statementOrderDetail.getStatementDetailRentAmount()));
                statementOrderDetail.setStatementDetailRentAmount(BigDecimalUtil.compare(statementOrderDetail.getStatementDetailRentAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : statementOrderDetail.getStatementDetailRentAmount());
                statementOrderDetail.setStatementDetailDepositAmount(BigDecimalUtil.add(statementDetailDepositAmount, statementOrderDetail.getStatementDetailDepositAmount()));
                statementOrderDetail.setStatementDetailDepositAmount(BigDecimalUtil.compare(statementOrderDetail.getStatementDetailDepositAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : statementOrderDetail.getStatementDetailDepositAmount());
                statementOrderDetail.setStatementDetailOtherAmount(BigDecimalUtil.add(statementDetailOtherAmount, statementOrderDetail.getStatementDetailOtherAmount()));
                statementOrderDetail.setStatementDetailOtherAmount(BigDecimalUtil.compare(statementOrderDetail.getStatementDetailOtherAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : statementOrderDetail.getStatementDetailOtherAmount());
                statementOrderDetail.setStatementDetailPaidAmount(BigDecimalUtil.add(statementDetailPaidAmount, statementOrderDetail.getStatementDetailPaidAmount()));
                statementOrderDetail.setStatementDetailPaidAmount(BigDecimalUtil.compare(statementOrderDetail.getStatementDetailPaidAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : statementOrderDetail.getStatementDetailPaidAmount());
                statementOrderDetail.setStatementDetailPenaltyAmount(BigDecimalUtil.add(statementDetailPenaltyAmount, statementOrderDetail.getStatementDetailPenaltyAmount()));
                statementOrderDetail.setStatementDetailPenaltyAmount(BigDecimalUtil.compare(statementOrderDetail.getStatementDetailPenaltyAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : statementOrderDetail.getStatementDetailPenaltyAmount());
                statementOrderDetail.setStatementDetailPenaltyPaidAmount(BigDecimalUtil.add(statementDetailPenaltyPaidAmount, statementOrderDetail.getStatementDetailPenaltyPaidAmount()));
                statementOrderDetail.setStatementDetailPenaltyPaidAmount(BigDecimalUtil.compare(statementOrderDetail.getStatementDetailPenaltyPaidAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : statementOrderDetail.getStatementDetailPenaltyPaidAmount());
                statementOrderDetail.setStatementDetailRentDepositAmount(BigDecimalUtil.add(statementDetailRentDepositAmount, statementOrderDetail.getStatementDetailRentDepositAmount()));
                statementOrderDetail.setStatementDetailRentDepositAmount(BigDecimalUtil.compare(statementOrderDetail.getStatementDetailRentDepositAmount(), BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : statementOrderDetail.getStatementDetailRentDepositAmount());
            }
            //判断相减之后最外层的对账单商品数或配件数是否为0，如果为0，则直接不存储
            if (OrderItemType.ORDER_ITEM_TYPE_OTHER.equals(statementOrderDetail.getOrderItemType())) {
                allList.addAll(list);
            }else {
                if (statementOrderDetail.getItemCount() != 0 || BigDecimalUtil.compare(statementOrderDetail.getStatementDetailAmount(), BigDecimal.ZERO) != 0) {
                    allList.addAll(list);
                }
            }
        }
        return allList;
    }

    private void exportConvertStatementOrderDetailOtherInfo(CheckStatementOrderDetail statementOrderDetail, CheckStatementOrderDetail returnReferStatementOrderDetail, OrderDO orderDO) {
        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())) {

            orderDO = orderDO == null ? orderMapper.findByOrderId(statementOrderDetail.getOrderId()) : orderDO;
            if (orderDO != null) {
                statementOrderDetail.setOrderNo(orderDO.getOrderNo());
                if (statementOrderDetail.getReletOrderItemReferId() == null) {
                    if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
                        for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                            if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(orderProductDO.getId())) {
                                statementOrderDetail.setItemName(orderProductDO.getProductName());
                                statementOrderDetail.setItemSkuName(orderProductDO.getProductSkuName());
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
                                statementOrderDetail.setItemSkuName("无");
                                statementOrderDetail.setItemCount(orderMaterialDO.getMaterialCount());
                                statementOrderDetail.setUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                                statementOrderDetail.setItemRentType(orderMaterialDO.getRentType());
                                break;
                            }
                        }
                    }
                } else {
                    if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                        ReletOrderProductDO reletOrderProductDO = reletOrderProductMapper.findById(statementOrderDetail.getReletOrderItemReferId());
                        if (reletOrderProductDO != null) {
                            statementOrderDetail.setItemName(reletOrderProductDO.getProductName());
                            statementOrderDetail.setItemSkuName(reletOrderProductDO.getProductSkuName());
                            statementOrderDetail.setItemCount(reletOrderProductDO.getProductCount());
                            statementOrderDetail.setUnitAmount(reletOrderProductDO.getProductUnitAmount());
                            statementOrderDetail.setItemRentType(orderDO.getRentType());
                        }
                    }
                    if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                        ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialMapper.findById(statementOrderDetail.getReletOrderItemReferId());
                        if (reletOrderMaterialDO != null) {
                            statementOrderDetail.setItemName(reletOrderMaterialDO.getMaterialName());
                            statementOrderDetail.setItemSkuName("无");
                            statementOrderDetail.setItemCount(reletOrderMaterialDO.getMaterialCount());
                            statementOrderDetail.setUnitAmount(reletOrderMaterialDO.getMaterialUnitAmount());
                            statementOrderDetail.setItemRentType(orderDO.getRentType());
                        }
                    }
                }
            }

        }

        if (OrderType.ORDER_TYPE_RETURN.equals(statementOrderDetail.getOrderType())) {
            //获取退货单
            K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findById(statementOrderDetail.getOrderId());
            if (k3ReturnOrderDO != null) {
                //存入退货单编号
                statementOrderDetail.setOrderNo(k3ReturnOrderDO.getReturnOrderNo());
                //如果退货单详情不为空
                if (CollectionUtil.isNotEmpty(k3ReturnOrderDO.getK3ReturnOrderDetailDOList())) {
                    //循环退货单详情
                    for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDO.getK3ReturnOrderDetailDOList()) {
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(k3ReturnOrderDetailDO.getId())) {
//                            OrderProductDO orderProductDO = orderProductMapper.findById(Integer.valueOf(k3ReturnOrderDetailDO.getOrderItemId()));
                            OrderProductDO orderProductDO = productSupport.getOrderProductDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                            //存入商品名称
                            if (orderProductDO != null) {
                                statementOrderDetail.setItemName(orderProductDO.getProductName());
                                statementOrderDetail.setItemSkuName(orderProductDO.getProductSkuName());
                                //存入商品单价
                                statementOrderDetail.setUnitAmount(orderProductDO.getProductUnitAmount());
                                //存入租赁方式，1按天租，2按月租
                                statementOrderDetail.setItemRentType(orderProductDO.getRentType());
                            }
                            //存入结算单明细类型：3-抵消租金（退租）
                            statementOrderDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                            //存入实际退还商品数量
                            statementOrderDetail.setItemCount(k3ReturnOrderDetailDO.getRealProductCount());
                        }
                        //如果是退换配件
                        if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(statementOrderDetail.getOrderItemType()) && statementOrderDetail.getOrderItemReferId().equals(k3ReturnOrderDetailDO.getId())) {
//                            OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(Integer.valueOf(k3ReturnOrderDetailDO.getOrderItemId()));
                            OrderMaterialDO orderMaterialDO = productSupport.getOrderMaterialDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                            if (orderMaterialDO != null) {
                                //保存配件名
                                statementOrderDetail.setItemName(orderMaterialDO.getMaterialName());
                                //保存配件单价
                                statementOrderDetail.setUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                                //保存租赁方式，1按天租，2按月租
                                statementOrderDetail.setItemRentType(orderMaterialDO.getRentType());
                            }
                            statementOrderDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OFFSET_RENT);
                            //保存退货数量
                            statementOrderDetail.setItemCount(k3ReturnOrderDetailDO.getRealProductCount());
                        }
                    }
                }
                if (statementOrderDetail.getReletOrderItemReferId() != null) {

                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(statementOrderDetail.getOrderItemType())) {
                        ReletOrderProductDO reletOrderProductDO = reletOrderProductMapper.findById(statementOrderDetail.getReletOrderItemReferId());
                        if (reletOrderProductDO != null) {
//                            ReletOrderDO reletOrderDO = reletOrderMapper.findById(reletOrderProductDO.getReletOrderId());
//                            statementOrderDetail.setItemName(reletOrderProductDO.getProductName() + reletOrderProductDO.getProductSkuName());
//                            statementOrderDetail.setItemCount(reletOrderProductDO.getRentingProductCount());
                            statementOrderDetail.setUnitAmount(reletOrderProductDO.getProductUnitAmount());
//                            statementOrderDetail.setItemRentType(reletOrderDO.getRentType());
                        }
                    }
                    if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(statementOrderDetail.getOrderItemType())) {
                        ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialMapper.findById(statementOrderDetail.getReletOrderItemReferId());
                        if (reletOrderMaterialDO != null) {
//                            ReletOrderDO reletOrderDO = reletOrderMapper.findById(reletOrderMaterialDO.getReletOrderId());
//                            statementOrderDetail.setItemName(reletOrderMaterialDO.getMaterialName());
//                            statementOrderDetail.setItemCount(reletOrderMaterialDO.getRentingMaterialCount());
                            statementOrderDetail.setUnitAmount(reletOrderMaterialDO.getMaterialUnitAmount());
//                            statementOrderDetail.setItemRentType(reletOrderDO.getRentType());
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

    /**
     * 退货结算
     *
     * @param k3ReturnOrderDetailDOS
     * @param ifDealDeposit
     */
    private void statementReturnOrderItemRent(List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOS, boolean ifDealDeposit) {
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDetailDOS)) {
            for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOS) {
                if (productSupport.isProduct(k3ReturnOrderDetailDO.getProductNo()))
                    statementReturnOrderProductItemRent(k3ReturnOrderDetailDO, ifDealDeposit);
                else statementReturnOrderMaterialItemRent(k3ReturnOrderDetailDO, ifDealDeposit);
            }
        }
    }


    /**
     * 清除退款信息（退回原结算单）
     *
     * @param k3ReturnOrderDetailDOS
     */
    private void clearReturnReturnOrderItems(List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOS, boolean paidReturn) {
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDetailDOS)) {
            for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOS) {
                deleteK3ReturnOrderDetailRefRentStatement(k3ReturnOrderDetailDO, paidReturn);
            }
        }
    }

    /**
     * 订单分段结算
     *
     * @param orderDO
     * @param currentTime
     * @param loginUserId
     * @param orderStatementDateSplitDO
     * @return
     */
    private List<StatementOrderDetailDO> generateStatementDetailListSplit(OrderDO orderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Date rentStartTime = orderDO.getRentStartTime();
        Integer buyerCustomerId = orderDO.getBuyerCustomerId();
        Integer orderId = orderDO.getId();
        //考虑到续租会覆盖原订单的归还时间
        Date expectReturnTime = orderSupport.generateExpectReturnTime(orderDO);

        K3OrderStatementConfigDO k3OrderStatementConfigDO = null;
        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
            k3OrderStatementConfigDO = k3OrderStatementConfigMapper.findByOrderId(orderDO.getId());
        }
        // 商品生成结算单
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId());
                getSplitStatementProductDetails(orderDO, currentTime, loginUserId, orderStatementDateSplitDO, addStatementOrderDetailDOList, rentStartTime, buyerCustomerId, orderId, expectReturnTime, orderProductDO, statementOrderDetailDOList, k3OrderStatementConfigDO);

            }
        }
        // 物料生成结算单
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderItemTypeAndId(OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId());
                getSplitStatementMaterialDetails(orderDO, currentTime, loginUserId, orderStatementDateSplitDO, addStatementOrderDetailDOList, rentStartTime, buyerCustomerId, orderId, expectReturnTime, orderMaterialDO, statementOrderDetailDOList, k3OrderStatementConfigDO);
            }
        }
        //处理其他费用（可能已生成过，即已支付未删除）
        boolean isOtherAmountStatement = false;
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderTypeAndId(OrderType.ORDER_TYPE_ORDER, orderDO.getId());
        for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOList) {
            if (StatementDetailType.STATEMENT_DETAIL_TYPE_OTHER.equals(orderDetailDO.getStatementDetailType()))
                isOtherAmountStatement = true;
        }
        // 其他费用，包括运费、等费用
        if (!isOtherAmountStatement) {
            BigDecimal otherAmount = orderDO.getLogisticsAmount();
            if (BigDecimalUtil.compare(otherAmount, BigDecimal.ZERO) > 0) {
                // 其他费用统一结算
                StatementOrderDetailDO thisStatementOrderDetailDO = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderDO.getId(), OrderItemType.ORDER_ITEM_TYPE_OTHER, BigInteger.ZERO.intValue(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, otherAmount, currentTime, loginUserId, null);
                if (thisStatementOrderDetailDO != null) {
                    thisStatementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_OTHER);
                    addStatementOrderDetailDOList.add(thisStatementOrderDetailDO);
                }
            }
        }
        return addStatementOrderDetailDOList;
    }

    /**
     * 续租单分段结算
     *
     * @param reletOrderDO
     * @param currentTime
     * @param loginUserId
     * @param orderStatementDateSplitDO
     * @return
     */
    private List<StatementOrderDetailDO> generateReletStatementDetailListSplit(ReletOrderDO reletOrderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        Date rentStartTime = reletOrderDO.getRentStartTime();
        Integer buyerCustomerId = reletOrderDO.getBuyerCustomerId();
        Integer orderId = reletOrderDO.getOrderId();
        //考虑到续租会覆盖原订单的归还时间
        Date expectReturnTime = reletOrderDO.getExpectReturnTime();
        K3OrderStatementConfigDO k3OrderStatementConfigDO = null;
        OrderDO orderDO = orderMapper.findByOrderId(reletOrderDO.getOrderId());
        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
            k3OrderStatementConfigDO = k3OrderStatementConfigMapper.findByOrderId(orderDO.getId());
        }
        // 商品生成结算单
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderProductDOList())) {
            List<ReletOrderProductDO> reletOrderProductDOS = reletOrderDO.getReletOrderProductDOList();
            for (ReletOrderProductDO reletOrderProductDO : reletOrderProductDOS) {
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findPrdcByReletOrderItemReferIds(reletOrderProductDO.getId());
                getSplitStatementReletProductDetails(reletOrderDO, currentTime, loginUserId, orderStatementDateSplitDO, addStatementOrderDetailDOList, rentStartTime, buyerCustomerId, orderId, expectReturnTime, reletOrderProductDO, statementOrderDetailDOList, k3OrderStatementConfigDO);
            }
        }
        // 物料生成结算单
        if (CollectionUtil.isNotEmpty(reletOrderDO.getReletOrderMaterialDOList())) {
            List<ReletOrderMaterialDO> reletOrderMaterialDOS = reletOrderDO.getReletOrderMaterialDOList();
            for (ReletOrderMaterialDO reletOrderMaterialDO : reletOrderMaterialDOS) {
                List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findMtrByReletOrderItemReferIds(reletOrderMaterialDO.getId());
                getSplitStatementReletMaterialDetails(reletOrderDO, currentTime, loginUserId, orderStatementDateSplitDO, addStatementOrderDetailDOList, rentStartTime, buyerCustomerId, orderId, expectReturnTime, reletOrderMaterialDO, statementOrderDetailDOList, k3OrderStatementConfigDO);
            }
        }
        return addStatementOrderDetailDOList;
    }

    //订单商品项
    private List<StatementOrderDetailDO> getSplitStatementProductDetails(OrderDO orderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO, Date rentStartTime, Integer buyerCustomerId, Integer orderId, Date expectReturnTime, OrderProductDO orderProductDO, List<StatementOrderDetailDO> statementOrderDetailDOList, BigDecimal k3PartRemoveAmount) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        //已有部分结算需留（已支付）
        boolean isDepositStatemented = false;
        Date lastStatementTime = null;
        int phase = 0;
        BigDecimal hasStatementAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOList) {
                if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(orderDetailDO.getStatementDetailType())) {
                    hasStatementAmount = BigDecimalUtil.add(orderDetailDO.getStatementDetailAmount(), hasStatementAmount, BigDecimalUtil.STANDARD_SCALE);
                    phase++;
                    if (lastStatementTime == null || orderDetailDO.getStatementEndTime().compareTo(lastStatementTime) > 0)
                        lastStatementTime = orderDetailDO.getStatementEndTime();
                } else if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(orderDetailDO.getStatementDetailType()))
                    isDepositStatemented = true;
            }
        }
        BigDecimal itemAllAmount = orderProductDO.getProductAmount();
        // 如果是K3订单，那么数量就要为在租数
        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
            Integer hasReturnCount = k3ReturnOrderDetailMapper.findRealReturnCountByOrderEntry(orderProductDO.getFEntryID().toString(), orderDO.getOrderNo());
            Integer productCount = orderProductDO.getRentingProductCount() + hasReturnCount;
            orderProductDO.setProductCount(productCount);
            itemAllAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(productCount), orderProductDO.getProductUnitAmount(), BigDecimalUtil.STANDARD_SCALE), new BigDecimal(orderProductDO.getRentTimeLength()), BigDecimalUtil.STANDARD_SCALE);
        }
        itemAllAmount = BigDecimalUtil.sub(itemAllAmount, hasStatementAmount, BigDecimalUtil.STANDARD_SCALE);
        itemAllAmount = BigDecimalUtil.sub(itemAllAmount, k3PartRemoveAmount, BigDecimalUtil.STANDARD_SCALE);

        //结算日改变日期
        Calendar rentStartTimeCalendar = Calendar.getInstance();
        rentStartTimeCalendar.setTime(rentStartTime);
        if (!isDepositStatemented) {
            StatementOrderDetailDO depositDetail = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, orderProductDO.getRentDepositAmount(), orderProductDO.getDepositAmount(), BigDecimal.ZERO, currentTime, loginUserId, null);
            if (depositDetail != null && BigDecimalUtil.compare(depositDetail.getStatementDetailAmount(), BigDecimal.ZERO) > 0) {
                depositDetail.setSerialNumber(orderProductDO.getSerialNumber());
                depositDetail.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
                depositDetail.setItemIsNew(orderProductDO.getIsNewProduct());
                depositDetail.setStatementDetailPhase(0);
                depositDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
                addStatementOrderDetailDOList.add(depositDetail);
            }
        }
        if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), BigDecimal.ZERO) == 0)
            return addStatementOrderDetailDOList;
        Date lastCalculateDate = lastStatementTime == null ? DateUtil.getDayByOffset(rentStartTime, -1) : lastStatementTime;
        BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
        Date firstPhaseStartTime=DateUtil.getDayByOffset(lastCalculateDate,1);
        rentStartTimeCalendar.setTime(firstPhaseStartTime);
        Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getBeforeStatementDate(), firstPhaseStartTime);
        Integer statementMonthCount = calculateStatementMonthCount(orderProductDO.getRentType(), orderDO.getRentTimeLength(), orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
        if(statementMonthCount==1){
            StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderProductDO.getRentType(), orderDO.getRentTimeLength(), orderProductDO.getPayMode(), firstPhaseStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId, null);
            if (statementOrderDetailDO != null) {
                fillProductStatementInfo(orderProductDO, ++phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
            }
        }else{
            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(orderProductDO.getRentType(), orderDO.getRentTimeLength(), statementDays, orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), firstPhaseStartTime, orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId, orderStatementDateSplitDO.getBeforeStatementDate(), null);
            if (statementOrderDetailDO != null) {
                fillProductStatementInfo(orderProductDO, ++phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
            }
            while (DateUtil.daysBetween(lastCalculateDate, orderStatementDateSplitDO.getStatementDateChangeTime()) > 0) {
                // 中间期数
                statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderProductDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, orderProductDO.getPaymentCycle(), orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), statementDays, orderProductDO.getPayMode(), currentTime, loginUserId, null);
                if (statementOrderDetailDO == null) break;
                fillProductStatementInfo(orderProductDO, ++phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
            }
            //处理上半段最后一期，越界则删除
            if(addStatementOrderDetailDOList.size()>0){
                statementOrderDetailDO = addStatementOrderDetailDOList.get(addStatementOrderDetailDOList.size() - 1);
                if (DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(), orderStatementDateSplitDO.getStatementDateChangeTime()) < 0&&StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    addStatementOrderDetailDOList.remove(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.sub(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = DateUtil.getDayByOffset(statementOrderDetailDO.getStatementStartTime(), -1);
                    phase--;
                }
            }

            //后半段
            if (CommonConstant.COMMON_ZERO.equals(orderStatementDateSplitDO.getChangeType())) {//截止到月底（单独一期）
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastCalculateDate);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date phaseEndTime = calendar.getTime();
                if (DateUtil.daysBetween(lastCalculateDate, phaseEndTime) > 0) {
                    statementOrderDetailDO = calculateStatementOrderDetailByActualTime(orderProductDO.getRentType(), orderProductDO.getPayMode(), lastCalculateDate, phaseEndTime, orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId, null);
                    if (statementOrderDetailDO != null) {
                        fillProductStatementInfo(orderProductDO, ++phase, statementOrderDetailDO);
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                        alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                        lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                    }
                }
            }
            //使用第二种结算日(第一期)
            statementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getAfterStatementDate(), DateUtil.getDayByOffset(lastCalculateDate, 1));
            if (DateUtil.daysBetween(lastCalculateDate, expectReturnTime) > 0) {
                statementOrderDetailDO = calculateFirstStatementOrderDetail(orderProductDO.getRentType(), orderDO.getRentTimeLength(), statementDays, orderProductDO.getPaymentCycle(), orderProductDO.getPayMode(), DateUtil.getDayByOffset(lastCalculateDate, 1), orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), currentTime, loginUserId, orderStatementDateSplitDO.getAfterStatementDate(), null);
                if (statementOrderDetailDO != null) {
                    fillProductStatementInfo(orderProductDO, ++phase, statementOrderDetailDO);
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                }
            }
            while (DateUtil.daysBetween(lastCalculateDate, expectReturnTime) > 0) {
                statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderProductDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, orderProductDO.getPaymentCycle(), orderProductDO.getProductUnitAmount(), orderProductDO.getProductCount(), statementDays, orderProductDO.getPayMode(), currentTime, loginUserId, null);
                if (statementOrderDetailDO == null) break;
                fillProductStatementInfo(orderProductDO, ++phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();

            }
            //处理下半段最后一期，冲正总额
            {
                if(addStatementOrderDetailDOList.size()>0){
                    statementOrderDetailDO = addStatementOrderDetailDOList.get(addStatementOrderDetailDOList.size() - 1);
                    if(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
                        addStatementOrderDetailDOList.remove(statementOrderDetailDO);
                        alreadyPaidAmount = BigDecimalUtil.sub(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                        lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementOrderDetailDO.getStatementStartTime(), -1);
                        phase--;
                    }
                }

                statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, orderProductDO.getId(), lastCalculateDate, rentStartTime, orderProductDO.getPayMode(), orderProductDO.getRentType(), orderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId, null);
                if (statementOrderDetailDO != null) {
                    fillProductStatementInfo(orderProductDO, ++phase, statementOrderDetailDO);
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                }
            }
        }
        return addStatementOrderDetailDOList;
    }

    private void getSplitStatementProductDetails(OrderDO orderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO, List<StatementOrderDetailDO> addStatementOrderDetailDOList, Date rentStartTime, Integer buyerCustomerId, Integer orderId, Date expectReturnTime, OrderProductDO orderProductDO, List<StatementOrderDetailDO> statementOrderDetailDOList, K3OrderStatementConfigDO k3OrderStatementConfigDO) {
        BigDecimal k3PartRemoveAmount = BigDecimal.ZERO;
        boolean isK3PartRemove = k3OrderStatementConfigDO != null && k3OrderStatementConfigDO.getRentStartTime() != null && DateUtil.daysBetween(orderDO.getRentStartTime(), k3OrderStatementConfigDO.getRentStartTime()) > 0 && DateUtil.daysBetween(expectReturnTime, k3OrderStatementConfigDO.getRentStartTime()) < 0;
        if (isK3PartRemove&&CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            List<StatementOrderDetailDO> statementOrderDetailDOS = getSplitStatementProductDetails(orderDO, currentTime, loginUserId, orderStatementDateSplitDO, rentStartTime, buyerCustomerId, orderId, expectReturnTime, orderProductDO, null, BigDecimal.ZERO);
            Date partTime = k3OrderStatementConfigDO.getRentStartTime();
            if (CollectionUtil.isNotEmpty(statementOrderDetailDOS)) {
                for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOS) {
                    if (DateUtil.daysBetween(orderDetailDO.getStatementEndTime(), partTime) > 0 && StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(orderDetailDO.getStatementDetailType())) {
                        k3PartRemoveAmount = BigDecimalUtil.add(k3PartRemoveAmount, orderDetailDO.getStatementDetailAmount());
                    }
                }
            }
        }
        List<StatementOrderDetailDO> statementOrderDetailDOS = getSplitStatementProductDetails(orderDO, currentTime, loginUserId, orderStatementDateSplitDO, rentStartTime, buyerCustomerId, orderId, expectReturnTime, orderProductDO, statementOrderDetailDOList, k3PartRemoveAmount);
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOS))
            addStatementOrderDetailDOList.addAll(statementOrderDetailDOS);
    }

    //续租单商品项
    private void getSplitStatementReletProductDetails(ReletOrderDO reletOrderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO, List<StatementOrderDetailDO> addStatementOrderDetailDOList, Date rentStartTime, Integer buyerCustomerId, Integer orderId, Date expectReturnTime, ReletOrderProductDO reletOrderProductDO, List<StatementOrderDetailDO> statementOrderDetailDOList, K3OrderStatementConfigDO k3OrderStatementConfigDO) {
        //处理k3抛弃部分金额
        BigDecimal k3PartRemoveAmount = BigDecimal.ZERO;
        boolean isK3PartRemove = k3OrderStatementConfigDO != null && k3OrderStatementConfigDO.getRentStartTime() != null && DateUtil.daysBetween(reletOrderDO.getRentStartTime(), k3OrderStatementConfigDO.getRentStartTime()) > 0 && DateUtil.daysBetween(expectReturnTime, k3OrderStatementConfigDO.getRentStartTime()) < 0;
        if (isK3PartRemove&&CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            List<StatementOrderDetailDO> statementOrderDetailDOS = getSplitStatementReletProductDetails(reletOrderDO, currentTime, loginUserId, orderStatementDateSplitDO, rentStartTime, buyerCustomerId, orderId, expectReturnTime, reletOrderProductDO, null, BigDecimal.ZERO);
            Date partTime = k3OrderStatementConfigDO.getRentStartTime();
            if (CollectionUtil.isNotEmpty(statementOrderDetailDOS)) {
                for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOS) {
                    if (DateUtil.daysBetween(orderDetailDO.getStatementEndTime(), partTime) > 0 && StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(orderDetailDO.getStatementDetailType())) {
                        k3PartRemoveAmount = BigDecimalUtil.add(k3PartRemoveAmount, orderDetailDO.getStatementDetailAmount());
                    }
                }
            }
        }
        List<StatementOrderDetailDO> statementOrderDetailDOS = getSplitStatementReletProductDetails(reletOrderDO, currentTime, loginUserId, orderStatementDateSplitDO, rentStartTime, buyerCustomerId, orderId, expectReturnTime, reletOrderProductDO, statementOrderDetailDOList, k3PartRemoveAmount);
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOS))
            addStatementOrderDetailDOList.addAll(statementOrderDetailDOS);
    }

    private List<StatementOrderDetailDO> getSplitStatementReletProductDetails(ReletOrderDO reletOrderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO, Date rentStartTime, Integer buyerCustomerId, Integer orderId, Date expectReturnTime, ReletOrderProductDO reletOrderProductDO, List<StatementOrderDetailDO> statementOrderDetailDOList, BigDecimal k3PartRemoveAmount) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        if (BigDecimalUtil.compare(reletOrderProductDO.getProductUnitAmount(), BigDecimal.ZERO) == 0)
            return addStatementOrderDetailDOList;
        //已有部分结算需留（已支付）
        Date lastStatementTime = null;
        int phase = 0;
        BigDecimal hasStatementAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOList) {
                if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(orderDetailDO.getStatementDetailType())) {
                    hasStatementAmount = BigDecimalUtil.add(orderDetailDO.getStatementDetailAmount(), hasStatementAmount, BigDecimalUtil.STANDARD_SCALE);
                    phase++;
                    if (lastStatementTime == null || orderDetailDO.getStatementEndTime().compareTo(lastStatementTime) > 0)
                        lastStatementTime = orderDetailDO.getStatementEndTime();
                }
            }
        }
        BigDecimal itemAllAmount = reletOrderProductDO.getProductAmount();
        itemAllAmount = BigDecimalUtil.sub(itemAllAmount, hasStatementAmount, BigDecimalUtil.STANDARD_SCALE);
        itemAllAmount = BigDecimalUtil.sub(itemAllAmount, k3PartRemoveAmount, BigDecimalUtil.STANDARD_SCALE);

        //结算日改变日期
        Calendar rentStartTimeCalendar = Calendar.getInstance();
        rentStartTimeCalendar.setTime(rentStartTime);

        Date lastCalculateDate = lastStatementTime == null ? DateUtil.getDayByOffset(rentStartTime,-1) : lastStatementTime;
        BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
        Date firstPhaseStartTime=DateUtil.getDayByOffset(lastCalculateDate,1);
        Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getBeforeStatementDate(), firstPhaseStartTime);

        rentStartTimeCalendar.setTime(firstPhaseStartTime);
        Integer statementMonthCount = calculateStatementMonthCount(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
        if(statementMonthCount==1){
            StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderProductDO.getPayMode(), firstPhaseStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), currentTime, loginUserId, reletOrderProductDO.getId());
            if (statementOrderDetailDO != null) {
                phase=fillReletOrderProductStatement(reletOrderProductDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
            }
        }else{
            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), statementDays, reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getPayMode(), firstPhaseStartTime, reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getRentingProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), currentTime, loginUserId, orderStatementDateSplitDO.getBeforeStatementDate(), reletOrderProductDO.getId());
            if (statementOrderDetailDO != null) {
                phase = fillReletOrderProductStatement(reletOrderProductDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
            }
            while (DateUtil.daysBetween(lastCalculateDate, orderStatementDateSplitDO.getStatementDateChangeTime()) > 0) {
                // 中间期数
                statementOrderDetailDO = calculateMiddleStatementOrderDetail(reletOrderDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), lastCalculateDate, reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getRentingProductCount(), statementDays, reletOrderProductDO.getPayMode(), currentTime, loginUserId, reletOrderProductDO.getId());
                if (statementOrderDetailDO == null)break;
                phase = fillReletOrderProductStatement(reletOrderProductDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
            }
            //处理上半段最后一期，越界则删除
            if(addStatementOrderDetailDOList.size()>0){
                statementOrderDetailDO = addStatementOrderDetailDOList.get(addStatementOrderDetailDOList.size() - 1);
                if (DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(), orderStatementDateSplitDO.getStatementDateChangeTime()) < 0&&StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    addStatementOrderDetailDOList.remove(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.sub(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = DateUtil.getDayByOffset(statementOrderDetailDO.getStatementStartTime(), -1);
                    phase--;
                }
            }

            //后半段
            if (CommonConstant.COMMON_ZERO.equals(orderStatementDateSplitDO.getChangeType())) {//截止到月底（单独一期）
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastCalculateDate);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date phaseEndTime = calendar.getTime();
                if (DateUtil.daysBetween(lastCalculateDate, phaseEndTime) > 0) {
                    statementOrderDetailDO = calculateStatementOrderDetailByActualTime(reletOrderDO.getRentType(), reletOrderProductDO.getPayMode(), lastCalculateDate, phaseEndTime, reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getRentingProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), currentTime, loginUserId, reletOrderProductDO.getId());
                    if (statementOrderDetailDO != null) {
                        phase = fillReletOrderProductStatement(reletOrderProductDO, phase, statementOrderDetailDO);
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                        alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                        lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                    }
                }
            }
            //使用第二种结算日(第一期)
            statementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getAfterStatementDate(), DateUtil.getDayByOffset(lastCalculateDate, 1));
            if (DateUtil.daysBetween(lastCalculateDate, expectReturnTime) > 0) {
                statementOrderDetailDO = calculateFirstStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), statementDays, reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getPayMode(), DateUtil.getDayByOffset(lastCalculateDate, 1), reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getRentingProductCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), currentTime, loginUserId, orderStatementDateSplitDO.getAfterStatementDate(), reletOrderProductDO.getId());
                if (statementOrderDetailDO != null) {
                    phase = fillReletOrderProductStatement(reletOrderProductDO, phase, statementOrderDetailDO);
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                }
            }
            while (DateUtil.daysBetween(lastCalculateDate, expectReturnTime) > 0) {
                statementOrderDetailDO = calculateMiddleStatementOrderDetail(reletOrderDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), lastCalculateDate, reletOrderProductDO.getPaymentCycle(), reletOrderProductDO.getProductUnitAmount(), reletOrderProductDO.getRentingProductCount(), statementDays, reletOrderProductDO.getPayMode(), currentTime, loginUserId, reletOrderProductDO.getId());
                if (statementOrderDetailDO == null) break;
                phase = fillReletOrderProductStatement(reletOrderProductDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();

            }
            //处理下半段最后一期，冲正总额
            {
                if(addStatementOrderDetailDOList.size()>0){
                    statementOrderDetailDO = addStatementOrderDetailDOList.get(addStatementOrderDetailDOList.size() - 1);
                    if(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
                        addStatementOrderDetailDOList.remove(statementOrderDetailDO);
                        alreadyPaidAmount = BigDecimalUtil.sub(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                        lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementOrderDetailDO.getStatementStartTime(), -1);
                        phase--;
                    }
                }
                statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_PRODUCT, reletOrderProductDO.getOrderProductId(), lastCalculateDate, rentStartTime, reletOrderProductDO.getPayMode(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId, reletOrderProductDO.getId());
                if (statementOrderDetailDO != null) {
                    fillReletOrderProductStatement(reletOrderProductDO, phase, statementOrderDetailDO);
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                }
            }
        }
        return addStatementOrderDetailDOList;
    }

    private int fillReletOrderProductStatement(ReletOrderProductDO reletOrderProductDO, int phase, StatementOrderDetailDO statementOrderDetailDO) {
        statementOrderDetailDO.setItemName(reletOrderProductDO.getProductName() + reletOrderProductDO.getProductSkuName());
        statementOrderDetailDO.setItemIsNew(reletOrderProductDO.getIsNewProduct());
        statementOrderDetailDO.setStatementDetailPhase(++phase);
        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
        //添加优惠券抵扣金额
        ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
        }
        return phase;
    }

    //物料商品项
    private void getSplitStatementMaterialDetails(OrderDO orderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO, List<StatementOrderDetailDO> addStatementOrderDetailDOList, Date rentStartTime, Integer buyerCustomerId, Integer orderId, Date expectReturnTime, OrderMaterialDO orderMaterialDO, List<StatementOrderDetailDO> statementOrderDetailDOList, K3OrderStatementConfigDO k3OrderStatementConfigDO) {
        BigDecimal k3PartRemoveAmount = BigDecimal.ZERO;
        boolean isK3PartRemove = k3OrderStatementConfigDO != null && k3OrderStatementConfigDO.getRentStartTime() != null && DateUtil.daysBetween(orderDO.getRentStartTime(), k3OrderStatementConfigDO.getRentStartTime()) > 0 && DateUtil.daysBetween(expectReturnTime, k3OrderStatementConfigDO.getRentStartTime()) < 0;
        if (isK3PartRemove&&CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            List<StatementOrderDetailDO> statementOrderDetailDOS = getSplitStatementMaterialDetails(orderDO, currentTime, loginUserId, orderStatementDateSplitDO, rentStartTime, buyerCustomerId, orderId, expectReturnTime, orderMaterialDO, null, BigDecimal.ZERO);
            Date partTime = k3OrderStatementConfigDO.getRentStartTime();
            if (CollectionUtil.isNotEmpty(statementOrderDetailDOS)) {
                for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOS) {
                    if (DateUtil.daysBetween(orderDetailDO.getStatementEndTime(), partTime) > 0 && StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(orderDetailDO.getStatementDetailType())) {
                        k3PartRemoveAmount = BigDecimalUtil.add(k3PartRemoveAmount, orderDetailDO.getStatementDetailAmount());
                    }
                }
            }
        }
        List<StatementOrderDetailDO> statementOrderDetailDOS = getSplitStatementMaterialDetails(orderDO, currentTime, loginUserId, orderStatementDateSplitDO, rentStartTime, buyerCustomerId, orderId, expectReturnTime, orderMaterialDO, statementOrderDetailDOList, k3PartRemoveAmount);
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOS))
            addStatementOrderDetailDOList.addAll(statementOrderDetailDOS);
    }

    private List<StatementOrderDetailDO> getSplitStatementMaterialDetails(OrderDO orderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO, Date rentStartTime, Integer buyerCustomerId, Integer orderId, Date expectReturnTime, OrderMaterialDO orderMaterialDO, List<StatementOrderDetailDO> statementOrderDetailDOList, BigDecimal k3PartRemoveAmount) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        //已有部分结算需留（已支付）
        boolean isDepositStatemented = false;
        Date lastStatementTime = null;
        int phase = 0;
        BigDecimal hasStatementAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOList) {
                if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(orderDetailDO.getStatementDetailType())) {
                    hasStatementAmount = BigDecimalUtil.add(orderDetailDO.getStatementDetailAmount(), hasStatementAmount, BigDecimalUtil.STANDARD_SCALE);
                    phase++;
                    if (lastStatementTime == null || orderDetailDO.getStatementEndTime().compareTo(lastStatementTime) > 0)
                        lastStatementTime = orderDetailDO.getStatementEndTime();
                } else if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(orderDetailDO.getStatementDetailType()))
                    isDepositStatemented = true;
            }
        }

        BigDecimal itemAllAmount = orderMaterialDO.getMaterialAmount();
        // 如果是K3订单，那么数量就要为在租数
        if (CommonConstant.COMMON_CONSTANT_YES.equals(orderDO.getIsK3Order())) {
            Integer hasReturnCount = k3ReturnOrderDetailMapper.findRealReturnCountByOrderEntry(orderMaterialDO.getFEntryID().toString(), orderDO.getOrderNo());
            Integer materialCount = orderMaterialDO.getRentingMaterialCount() + hasReturnCount;
            orderMaterialDO.setMaterialCount(materialCount);
            itemAllAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(materialCount), orderMaterialDO.getMaterialUnitAmount(), BigDecimalUtil.STANDARD_SCALE), new BigDecimal(orderMaterialDO.getRentTimeLength()), BigDecimalUtil.STANDARD_SCALE);
        }
        itemAllAmount = BigDecimalUtil.sub(itemAllAmount, hasStatementAmount, BigDecimalUtil.STANDARD_SCALE);
        itemAllAmount = BigDecimalUtil.sub(itemAllAmount, k3PartRemoveAmount, BigDecimalUtil.STANDARD_SCALE);

        //结算日改变日期
        Calendar rentStartTimeCalendar = Calendar.getInstance();
        rentStartTimeCalendar.setTime(rentStartTime);
        if (!isDepositStatemented) {
            StatementOrderDetailDO depositDetail = buildStatementOrderDetailDO(buyerCustomerId, OrderType.ORDER_TYPE_ORDER, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), rentStartTime, rentStartTime, rentStartTime, BigDecimal.ZERO, orderMaterialDO.getRentDepositAmount(), orderMaterialDO.getDepositAmount(), BigDecimal.ZERO, currentTime, loginUserId, null);
            if (depositDetail != null && BigDecimalUtil.compare(depositDetail.getStatementDetailAmount(), BigDecimal.ZERO) > 0) {
                depositDetail.setSerialNumber(orderMaterialDO.getSerialNumber());
                depositDetail.setItemName(orderMaterialDO.getMaterialName());
                depositDetail.setItemIsNew(orderMaterialDO.getIsNewMaterial());
                depositDetail.setStatementDetailPhase(0);
                depositDetail.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT);
                addStatementOrderDetailDOList.add(depositDetail);
            }
        }
        if (BigDecimalUtil.compare(orderMaterialDO.getMaterialUnitAmount(), BigDecimal.ZERO) == 0)
            return addStatementOrderDetailDOList;
        Date lastCalculateDate = lastStatementTime == null ? DateUtil.getDayByOffset(rentStartTime,-1) : lastStatementTime;
        BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
        Date firstPhaseStartTime=DateUtil.getDayByOffset(lastCalculateDate,1);
        Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getBeforeStatementDate(), firstPhaseStartTime);

        rentStartTimeCalendar.setTime(firstPhaseStartTime);
        Integer statementMonthCount = calculateStatementMonthCount(orderMaterialDO.getRentType(), orderDO.getRentTimeLength(), orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
        if(statementMonthCount==1){
            StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength(), orderMaterialDO.getPayMode(), firstPhaseStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId, null);
            if (statementOrderDetailDO != null) {
                phase=fillMaterialStatementInfo(orderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
            }
        }else{
            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(orderMaterialDO.getRentType(), orderDO.getRentTimeLength(), statementDays, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), firstPhaseStartTime, orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId, orderStatementDateSplitDO.getBeforeStatementDate(), null);
            if (statementOrderDetailDO != null) {
                phase = fillMaterialStatementInfo(orderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
            }
            while (DateUtil.daysBetween(lastCalculateDate, orderStatementDateSplitDO.getStatementDateChangeTime()) > 0) {
                // 中间期数
                statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderMaterialDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), statementDays, orderMaterialDO.getPayMode(), currentTime, loginUserId, null);
                if (statementOrderDetailDO == null) break;
                phase = fillMaterialStatementInfo(orderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
            }
            //处理上半段最后一期，越界则删除
            if(addStatementOrderDetailDOList.size()>0){
                statementOrderDetailDO = addStatementOrderDetailDOList.get(addStatementOrderDetailDOList.size() - 1);
                if (DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(), orderStatementDateSplitDO.getStatementDateChangeTime()) < 0&&StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    addStatementOrderDetailDOList.remove(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.sub(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = DateUtil.getDayByOffset(statementOrderDetailDO.getStatementStartTime(), -1);
                    phase--;
                }
            }

            //后半段
            if (CommonConstant.COMMON_ZERO.equals(orderStatementDateSplitDO.getChangeType())) {//截止到月底（单独一期）
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastCalculateDate);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date phaseEndTime = calendar.getTime();
                if (DateUtil.daysBetween(lastCalculateDate, phaseEndTime) > 0) {
                    statementOrderDetailDO = calculateStatementOrderDetailByActualTime(orderMaterialDO.getRentType(), orderMaterialDO.getPayMode(), lastCalculateDate, phaseEndTime, orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId, null);
                    if (statementOrderDetailDO != null) {
                        phase = fillMaterialStatementInfo(orderMaterialDO, phase, statementOrderDetailDO);
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                        alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                        lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                    }
                }
            }
            //使用第二种结算日(第一期)
            statementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getAfterStatementDate(), DateUtil.getDayByOffset(lastCalculateDate, 1));
            if (DateUtil.daysBetween(lastCalculateDate, expectReturnTime) > 0) {
                statementOrderDetailDO = calculateFirstStatementOrderDetail(orderMaterialDO.getRentType(), orderDO.getRentTimeLength(), statementDays, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getPayMode(), DateUtil.getDayByOffset(lastCalculateDate, 1), orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), currentTime, loginUserId, orderStatementDateSplitDO.getAfterStatementDate(), null);
                if (statementOrderDetailDO != null) {
                    phase = fillMaterialStatementInfo(orderMaterialDO, phase, statementOrderDetailDO);
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                }
            }
            while (DateUtil.daysBetween(lastCalculateDate, expectReturnTime) > 0) {
                statementOrderDetailDO = calculateMiddleStatementOrderDetail(orderMaterialDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, orderMaterialDO.getPaymentCycle(), orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getMaterialCount(), statementDays, orderMaterialDO.getPayMode(), currentTime, loginUserId, null);
                if (statementOrderDetailDO == null) break;
                phase = fillMaterialStatementInfo(orderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();

            }
            //处理下半段最后一期，冲正总额
            if(addStatementOrderDetailDOList.size()>0){
                statementOrderDetailDO = addStatementOrderDetailDOList.get(addStatementOrderDetailDOList.size() - 1);
                if(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
                    addStatementOrderDetailDOList.remove(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.sub(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementOrderDetailDO.getStatementStartTime(), -1);
                    phase--;
                }
            }
            statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, orderMaterialDO.getId(), lastCalculateDate, rentStartTime, orderMaterialDO.getPayMode(), orderMaterialDO.getRentType(), orderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId, null);
            if (statementOrderDetailDO != null) {
                fillMaterialStatementInfo(orderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
            }
        }

        return addStatementOrderDetailDOList;
    }

    private int fillMaterialStatementInfo(OrderMaterialDO orderMaterialDO, int phase, StatementOrderDetailDO statementOrderDetailDO) {
        statementOrderDetailDO.setSerialNumber(orderMaterialDO.getSerialNumber());
        statementOrderDetailDO.setItemName(orderMaterialDO.getMaterialName());
        statementOrderDetailDO.setItemIsNew(orderMaterialDO.getIsNewMaterial());
        statementOrderDetailDO.setStatementDetailPhase(++phase);
        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
        //添加优惠券抵扣金额
        ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
        }
        return phase;
    }

    //续租物料商品项
    private void getSplitStatementReletMaterialDetails(ReletOrderDO reletOrderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO, List<StatementOrderDetailDO> addStatementOrderDetailDOList, Date rentStartTime, Integer buyerCustomerId, Integer orderId, Date expectReturnTime, ReletOrderMaterialDO reletOrderMaterialDO, List<StatementOrderDetailDO> statementOrderDetailDOList, K3OrderStatementConfigDO k3OrderStatementConfigDO) {
        BigDecimal k3PartRemoveAmount = BigDecimal.ZERO;
        boolean isK3PartRemove = k3OrderStatementConfigDO != null && k3OrderStatementConfigDO.getRentStartTime() != null && DateUtil.daysBetween(reletOrderDO.getRentStartTime(), k3OrderStatementConfigDO.getRentStartTime()) > 0 && DateUtil.daysBetween(expectReturnTime, k3OrderStatementConfigDO.getRentStartTime()) < 0;
        if (isK3PartRemove&&CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            List<StatementOrderDetailDO> statementOrderDetailDOS = getSplitStatementReletMaterialDetails(reletOrderDO, currentTime, loginUserId, orderStatementDateSplitDO, rentStartTime, buyerCustomerId, orderId, expectReturnTime, reletOrderMaterialDO, null, BigDecimal.ZERO);
            Date partTime = k3OrderStatementConfigDO.getRentStartTime();
            if (CollectionUtil.isNotEmpty(statementOrderDetailDOS)) {
                for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOS) {
                    if (DateUtil.daysBetween(orderDetailDO.getStatementEndTime(), partTime) > 0 && StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(orderDetailDO.getStatementDetailType())) {
                        k3PartRemoveAmount = BigDecimalUtil.add(k3PartRemoveAmount, orderDetailDO.getStatementDetailAmount());
                    }
                }
            }
        }
        List<StatementOrderDetailDO> statementOrderDetailDOS = getSplitStatementReletMaterialDetails(reletOrderDO, currentTime, loginUserId, orderStatementDateSplitDO, rentStartTime, buyerCustomerId, orderId, expectReturnTime, reletOrderMaterialDO, statementOrderDetailDOList, k3PartRemoveAmount);
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOS))
            addStatementOrderDetailDOList.addAll(statementOrderDetailDOS);
    }

    private List<StatementOrderDetailDO> getSplitStatementReletMaterialDetails(ReletOrderDO reletOrderDO, Date currentTime, Integer loginUserId, OrderStatementDateSplitDO orderStatementDateSplitDO, Date rentStartTime, Integer buyerCustomerId, Integer orderId, Date expectReturnTime, ReletOrderMaterialDO reletOrderMaterialDO, List<StatementOrderDetailDO> statementOrderDetailDOList, BigDecimal k3PartRemoveAmount) {
        List<StatementOrderDetailDO> addStatementOrderDetailDOList = new ArrayList<>();
        if (BigDecimalUtil.compare(reletOrderMaterialDO.getMaterialUnitAmount(), BigDecimal.ZERO) == 0)
            return addStatementOrderDetailDOList;
        //已有部分结算
        Date lastStatementTime = null;
        int phase = 0;
        BigDecimal hasStatementAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO orderDetailDO : statementOrderDetailDOList) {
                if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(orderDetailDO.getStatementDetailType())) {
                    hasStatementAmount = BigDecimalUtil.add(orderDetailDO.getStatementDetailAmount(), hasStatementAmount, BigDecimalUtil.STANDARD_SCALE);
                    phase++;
                    if (lastStatementTime == null || orderDetailDO.getStatementEndTime().compareTo(lastStatementTime) > 0)
                        lastStatementTime = orderDetailDO.getStatementEndTime();
                }
            }
        }
        BigDecimal itemAllAmount = reletOrderMaterialDO.getMaterialAmount();
        itemAllAmount = BigDecimalUtil.sub(itemAllAmount, hasStatementAmount, BigDecimalUtil.STANDARD_SCALE);
        itemAllAmount = BigDecimalUtil.sub(itemAllAmount, k3PartRemoveAmount, BigDecimalUtil.STANDARD_SCALE);

        //结算日改变日期
        Calendar rentStartTimeCalendar = Calendar.getInstance();
        rentStartTimeCalendar.setTime(rentStartTime);

        Date lastCalculateDate = lastStatementTime == null ? DateUtil.getDayByOffset(rentStartTime,-1) :lastStatementTime;
        BigDecimal alreadyPaidAmount = BigDecimal.ZERO;
        Date firstPhaseStartTime=DateUtil.getDayByOffset(lastCalculateDate,1);
        Integer statementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getBeforeStatementDate(), firstPhaseStartTime);

        rentStartTimeCalendar.setTime(firstPhaseStartTime);
        Integer statementMonthCount = calculateStatementMonthCount(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getPayMode(), rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH), statementDays);
        if(statementMonthCount==1){
            StatementOrderDetailDO statementOrderDetailDO = calculateOneStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), reletOrderMaterialDO.getPayMode(), firstPhaseStartTime, itemAllAmount, buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), currentTime, loginUserId, reletOrderMaterialDO.getId());
            if (statementOrderDetailDO != null) {
                phase=fillReletMaterialStatementInfo(reletOrderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
            }
        }else{
            StatementOrderDetailDO statementOrderDetailDO = calculateFirstStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), statementDays, reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getPayMode(), firstPhaseStartTime, reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getRentingMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), currentTime, loginUserId, orderStatementDateSplitDO.getBeforeStatementDate(), reletOrderMaterialDO.getId());
            if (statementOrderDetailDO != null) {
                phase = fillReletMaterialStatementInfo(reletOrderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
            }
            while (DateUtil.daysBetween(lastCalculateDate, orderStatementDateSplitDO.getStatementDateChangeTime()) > 0) {
                // 中间期数
                statementOrderDetailDO = calculateMiddleStatementOrderDetail(reletOrderDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), lastCalculateDate, reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getRentingMaterialCount(), statementDays, reletOrderMaterialDO.getPayMode(), currentTime, loginUserId, reletOrderMaterialDO.getId());
                if (statementOrderDetailDO == null)break;
                phase = fillReletMaterialStatementInfo(reletOrderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
            }
            //处理上半段最后一期，越界则删除
            if(addStatementOrderDetailDOList.size()>0){
                statementOrderDetailDO = addStatementOrderDetailDOList.get(addStatementOrderDetailDOList.size() - 1);
                if (DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(), orderStatementDateSplitDO.getStatementDateChangeTime()) < 0&&StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    addStatementOrderDetailDOList.remove(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.sub(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = DateUtil.getDayByOffset(statementOrderDetailDO.getStatementStartTime(), -1);
                    phase--;
                }
            }

            //后半段
            if (CommonConstant.COMMON_ZERO.equals(orderStatementDateSplitDO.getChangeType())) {//截止到月底（单独一期）
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastCalculateDate);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date phaseEndTime = calendar.getTime();
                if (DateUtil.daysBetween(lastCalculateDate, phaseEndTime) > 0) {
                    statementOrderDetailDO = calculateStatementOrderDetailByActualTime(reletOrderDO.getRentType(), reletOrderMaterialDO.getPayMode(), lastCalculateDate, phaseEndTime, reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getRentingMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), currentTime, loginUserId, reletOrderMaterialDO.getId());
                    if (statementOrderDetailDO != null) {
                        phase = fillReletMaterialStatementInfo(reletOrderMaterialDO, phase, statementOrderDetailDO);
                        addStatementOrderDetailDOList.add(statementOrderDetailDO);
                        alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                        lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
                    }
                }
            }
            //使用第二种结算日(第一期)
            statementDays = statementOrderSupport.getCustomerStatementDate(orderStatementDateSplitDO.getAfterStatementDate(), DateUtil.getDayByOffset(lastCalculateDate, 1));
            if (DateUtil.daysBetween(lastCalculateDate, expectReturnTime) > 0) {
                statementOrderDetailDO = calculateFirstStatementOrderDetail(reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), statementDays, reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getPayMode(), DateUtil.getDayByOffset(lastCalculateDate, 1), reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getRentingMaterialCount(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), currentTime, loginUserId, orderStatementDateSplitDO.getAfterStatementDate(), reletOrderMaterialDO.getId());
                if (statementOrderDetailDO != null) {
                    phase = fillReletMaterialStatementInfo(reletOrderMaterialDO, phase, statementOrderDetailDO);
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                    alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                    lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.getBeginOfDay(statementOrderDetailDO.getStatementEndTime());
                }
            }
            while (DateUtil.daysBetween(lastCalculateDate, expectReturnTime) > 0) {
                statementOrderDetailDO = calculateMiddleStatementOrderDetail(reletOrderDO.getRentType(), buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), lastCalculateDate, reletOrderMaterialDO.getPaymentCycle(), reletOrderMaterialDO.getMaterialUnitAmount(), reletOrderMaterialDO.getRentingMaterialCount(), statementDays, reletOrderMaterialDO.getPayMode(), currentTime, loginUserId, reletOrderMaterialDO.getId());
                if (statementOrderDetailDO == null)break;
                phase = fillReletMaterialStatementInfo(reletOrderMaterialDO, phase, statementOrderDetailDO);
                addStatementOrderDetailDOList.add(statementOrderDetailDO);
                alreadyPaidAmount = BigDecimalUtil.add(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                lastCalculateDate = statementOrderDetailDO.getStatementEndTime();
            }
            //处理下半段最后一期，冲正总额
            {
                if(addStatementOrderDetailDOList.size()>0){
                    statementOrderDetailDO = addStatementOrderDetailDOList.get(addStatementOrderDetailDOList.size() - 1);
                    if(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
                        addStatementOrderDetailDOList.remove(statementOrderDetailDO);
                        alreadyPaidAmount = BigDecimalUtil.sub(alreadyPaidAmount, statementOrderDetailDO.getStatementDetailAmount());
                        lastCalculateDate = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementOrderDetailDO.getStatementStartTime(), -1);
                        phase--;
                    }
                }
                statementOrderDetailDO = calculateLastStatementOrderDetail(buyerCustomerId, orderId, OrderItemType.ORDER_ITEM_TYPE_MATERIAL, reletOrderMaterialDO.getOrderMaterialId(), lastCalculateDate, rentStartTime, reletOrderMaterialDO.getPayMode(), reletOrderDO.getRentType(), reletOrderDO.getRentTimeLength(), itemAllAmount, alreadyPaidAmount, currentTime, loginUserId, reletOrderMaterialDO.getId());
                if (statementOrderDetailDO != null) {
                    fillReletMaterialStatementInfo(reletOrderMaterialDO, phase, statementOrderDetailDO);
                    addStatementOrderDetailDOList.add(statementOrderDetailDO);
                }
            }
        }

        return addStatementOrderDetailDOList;
    }

    private int fillReletMaterialStatementInfo(ReletOrderMaterialDO reletOrderMaterialDO, int phase, StatementOrderDetailDO statementOrderDetailDO) {
        statementOrderDetailDO.setItemName(reletOrderMaterialDO.getMaterialName());
        statementOrderDetailDO.setItemIsNew(reletOrderMaterialDO.getIsNewMaterial());
        statementOrderDetailDO.setStatementDetailPhase(++phase);
        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
        //添加优惠券抵扣金额
        ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
        }
        return phase;
    }

    /**
     * 根据准确的起始结束时间算出当期结算(仅月租)
     */
    StatementOrderDetailDO calculateStatementOrderDetailByActualTime(Integer rentType, Integer payMode, Date lastCalculateDate, Date statementEndTime, BigDecimal unitAmount, Integer itemCount, Integer customerId, Integer orderId, Integer orderItemType, Integer orderItemReferId, Date currentTime, Integer loginUserId, Integer reletOrderItemReferId) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) return null;
        Calendar rentStartTimeCalendar = Calendar.getInstance();
        rentStartTimeCalendar.setTime(lastCalculateDate);
        rentStartTimeCalendar.add(Calendar.DAY_OF_MONTH, 1);
        Date rentStartTime = rentStartTimeCalendar.getTime();

        Date statementExpectPayTime = null;
        BigDecimal firstPhaseAmount;
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payMode)) {
            statementExpectPayTime = rentStartTime;
        } else {
            statementExpectPayTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(statementEndTime, 1);
        }
        firstPhaseAmount = amountSupport.calculateRentAmount(rentStartTime, statementEndTime, unitAmount, itemCount);

        firstPhaseAmount = BigDecimalUtil.round(firstPhaseAmount, 0);
        return buildStatementOrderDetailDO(customerId, OrderType.ORDER_TYPE_ORDER, orderId, orderItemType, orderItemReferId, statementExpectPayTime, rentStartTime, statementEndTime, firstPhaseAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentTime, loginUserId, reletOrderItemReferId);
    }

    private String getStatementModeString(Integer statementMode) {
        if (statementMode == null) {
            DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
            statementMode = dataDictionaryDO == null ? StatementMode.STATEMENT_MONTH_END : Integer.parseInt(dataDictionaryDO.getDataName());
        }
        if (StatementMode.STATEMENT_MONTH_END.equals(statementMode)) {
            return "月底结算";
        }
        if (StatementMode.STATEMENT_20.equals(statementMode)) {
            return "二十号结算";
        }
        if (StatementMode.STATEMENT_MONTH_NATURAL.equals(statementMode)) {
            return "自然日结算";
        }
        return "";
    }

    private void fillProductStatementInfo(OrderProductDO orderProductDO, int phase, StatementOrderDetailDO statementOrderDetailDO) {
        statementOrderDetailDO.setSerialNumber(orderProductDO.getSerialNumber());
        statementOrderDetailDO.setItemName(orderProductDO.getProductName() + orderProductDO.getProductSkuName());
        statementOrderDetailDO.setItemIsNew(orderProductDO.getIsNewProduct());
        statementOrderDetailDO.setStatementDetailPhase(phase);
        statementOrderDetailDO.setStatementDetailType(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT);
        //添加优惠券抵扣金额
        ServiceResult<String, BigDecimal> serviceResult = couponSupport.setDeductionAmount(statementOrderDetailDO);
        if (serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            statementOrderDetailDO.setStatementCouponAmount(serviceResult.getResult());
        }
    }

    /**
     * 订单退货时修改已成功还未开始的续租单数量
     * @param k3ReturnOrderDO
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    ServiceResult<String, BigDecimal> fixReletOrderItemCount(K3ReturnOrderDO k3ReturnOrderDO){
        //默认返回成功
        ServiceResult<String, BigDecimal> result=new ServiceResult<>();
        result.setErrorCode(ErrorCode.SUCCESS);
        if(k3ReturnOrderDO==null)return result;
        List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList=k3ReturnOrderDO.getK3ReturnOrderDetailDOList();
        if(CollectionUtil.isEmpty(k3ReturnOrderDetailDOList))return result;
        Set<String> returnRefOrderNoSet=new HashSet<>();

        //退货数量
        Map<String,Integer> returnMap=new HashMap<>();
        for(K3ReturnOrderDetailDO k3ReturnOrderDetailDO:k3ReturnOrderDetailDOList){
            returnRefOrderNoSet.add(k3ReturnOrderDetailDO.getOrderNo());
            //兼容erp订单和k3订单商品项
            if (productSupport.isProduct(k3ReturnOrderDetailDO.getProductNo())) {
                OrderProductDO orderProductDO = productSupport.getOrderProductDO(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
                returnMap.put(OrderItemType.ORDER_ITEM_TYPE_PRODUCT+"_"+orderProductDO.getId(),k3ReturnOrderDetailDO.getRealProductCount());
            }else {
                OrderMaterialDO orderMaterialDO=productSupport.getOrderMaterialDO(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
                returnMap.put(OrderItemType.ORDER_ITEM_TYPE_MATERIAL+"_"+orderMaterialDO.getId(),k3ReturnOrderDetailDO.getRealProductCount());
            }
        }
        //获取所有关联续租单（成功续租单）
        List<ReletOrderDO> reletOrderDOList= reletOrderMapper.findReletedOrdersByOrderNos(returnRefOrderNoSet);
        if(CollectionUtil.isEmpty(reletOrderDOList))return result;

        Set<ReletOrderDO> needFixReletOrders=new HashSet<>();
        Set<ReletOrderProductDO> needFixReletOrderProducts=new HashSet<>();
        Set<ReletOrderMaterialDO> needFixReletOrderMaterials=new HashSet<>();
        Date returnTime=k3ReturnOrderDO.getReturnTime();
        for(ReletOrderDO reletOrderDO:reletOrderDOList){
            //退货节点前的续租不用修正
            if(DateUtil.daysBetween(returnTime,reletOrderDO.getRentStartTime())<=0){
                continue;
            }
            List<ReletOrderProductDO> reletOrderProductDOList=reletOrderDO.getReletOrderProductDOList();
            if(CollectionUtil.isNotEmpty(reletOrderDOList)){
                for(ReletOrderProductDO orderProductDO:reletOrderProductDOList){
                    String key=OrderItemType.ORDER_ITEM_TYPE_PRODUCT+"_"+orderProductDO.getOrderProductId();
                    if(returnMap.containsKey(key)){
                        Integer returnCount=returnMap.get(key);
                        if(returnCount<=0)continue;
                        //修改续租项
                        Integer beforeCount=orderProductDO.getRentingProductCount();
                        Integer realCount=beforeCount-returnCount;
                        BigDecimal beforeProductAmount=orderProductDO.getProductAmount();
                        orderProductDO.setRentingProductCount(realCount);
                        orderProductDO.setProductAmount(BigDecimalUtil.div(BigDecimalUtil.mul(orderProductDO.getProductAmount(),new BigDecimal(realCount)),new BigDecimal(beforeCount),BigDecimalUtil.STANDARD_SCALE));

                        BigDecimal gap=BigDecimalUtil.sub(beforeProductAmount,orderProductDO.getProductAmount());
                        //修改续租单
                        reletOrderDO.setTotalProductCount(reletOrderDO.getTotalProductCount()-returnCount);
                        reletOrderDO.setTotalProductAmount(BigDecimalUtil.sub(reletOrderDO.getTotalProductAmount(),gap));
                        reletOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(reletOrderDO.getTotalOrderAmount(),gap));

                        needFixReletOrderProducts.add(orderProductDO);
                        needFixReletOrders.add(reletOrderDO);
                    }
                }
            }
            List<ReletOrderMaterialDO> reletOrderMaterialDOList=reletOrderDO.getReletOrderMaterialDOList();
            if(CollectionUtil.isNotEmpty(reletOrderMaterialDOList)){
                for(ReletOrderMaterialDO orderMaterialDO:reletOrderMaterialDOList){
                    String key=OrderItemType.ORDER_ITEM_TYPE_MATERIAL+"_"+orderMaterialDO.getOrderMaterialId();
                    if(returnMap.containsKey(key)){
                        Integer returnCount=returnMap.get(key);
                        if(returnCount<=0)continue;
                        //修改续租项
                        Integer beforeCount=orderMaterialDO.getRentingMaterialCount();
                        Integer realCount=beforeCount-returnCount;
                        BigDecimal beforeProductAmount=orderMaterialDO.getMaterialAmount();
                        orderMaterialDO.setRentingMaterialCount(realCount);
                        orderMaterialDO.setMaterialAmount(BigDecimalUtil.div(BigDecimalUtil.mul(beforeProductAmount,new BigDecimal(realCount)),new BigDecimal(beforeCount),BigDecimalUtil.STANDARD_SCALE));

                        BigDecimal gap=BigDecimalUtil.sub(beforeProductAmount,orderMaterialDO.getMaterialAmount());
                        //修改续租单
                        reletOrderDO.setTotalMaterialCount(reletOrderDO.getTotalMaterialCount()-returnCount);
                        reletOrderDO.setTotalMaterialAmount(BigDecimalUtil.sub(reletOrderDO.getTotalMaterialAmount(),gap));
                        reletOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(reletOrderDO.getTotalOrderAmount(),gap));

                        needFixReletOrderMaterials.add(orderMaterialDO);
                        needFixReletOrders.add(reletOrderDO);
                    }
                }
            }

        }
        //同步数据库
        if(CollectionUtil.isNotEmpty(needFixReletOrders)){
            SqlLogInterceptor.setExecuteSql("skip   print   reletOrderMapper.batchUpdate   sql ......");
            reletOrderMapper.batchUpdate(new ArrayList<>(needFixReletOrders));
        }
        if(CollectionUtil.isNotEmpty(needFixReletOrderMaterials)){
            SqlLogInterceptor.setExecuteSql("skip   print   reletOrderMaterialMapper.batchUpdate   sql ......");
            reletOrderMaterialMapper.batchUpdate(new ArrayList<>(needFixReletOrderMaterials));
        }
        if(CollectionUtil.isNotEmpty(needFixReletOrderProducts)){
            SqlLogInterceptor.setExecuteSql("skip   print   reletOrderProductMapper.batchUpdate   sql ......");
            reletOrderProductMapper.batchUpdate(new ArrayList<>(needFixReletOrderProducts));
        }
        //重算续租单
        for (ReletOrderDO reletOrderDO:needFixReletOrders){
            //todo ? 目前允许任意续租单重算，需确认
            ServiceResult<String, BigDecimal> serviceResult=reCreateReletOrderStatement(reletOrderDO.getReletOrderNo(),true);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return serviceResult;
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Boolean> payStatementOrderDetail(List<Integer> mergeStatementItemList) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        if(CollectionUtil.isEmpty(mergeStatementItemList)){
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }
        List<StatementOrderDetailDO> statementOrderDetailDOList= statementOrderDetailMapper.findByIdList(mergeStatementItemList);
        if(CollectionUtil.isEmpty(statementOrderDetailDOList)||statementOrderDetailDOList.size()<mergeStatementItemList.size()){
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_NOT_EXISTS);
            return result;
        }
        //必须保证所有详情来自同一结算单和同一订单
        Integer statementOrderId=statementOrderDetailDOList.get(0).getStatementOrderId();
        Integer orderId=statementOrderDetailDOList.get(0).getOrderId();
        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderId);
        if (statementOrderDO == null) {
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return result;
        }

        Date currentTime=new Date();
        BigDecimal rentPayAmount=BigDecimal.ZERO, rentDepositPayAmount=BigDecimal.ZERO, depositPayAmount=BigDecimal.ZERO, otherPayAmount=BigDecimal.ZERO, overduePayAmount=BigDecimal.ZERO, realTotalNeedPayAmount=BigDecimal.ZERO;
        for(StatementOrderDetailDO statementOrderDetailDO:statementOrderDetailDOList){
            if(OrderType.ORDER_TYPE_RETURN.equals(statementOrderDetailDO.getOrderType()))continue;
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())
                    || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                continue;
            }
            if(!statementOrderId.equals(statementOrderDetailDO.getStatementOrderId())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_NOT_IN_SAME_STATEMENT_ORDER);
                return result;
            }
            if(!orderId.equals(statementOrderDetailDO.getOrderId())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                result.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_NOT_IN_SAME_ORDER);
                return result;
            }
            String payVerifyResult = payVerify(statementOrderDetailDO);
            if (!ErrorCode.SUCCESS.equals(payVerifyResult)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                result.setErrorCode(payVerifyResult);
                return result;
            }

            // 查询有没有冲正业务金额
            BigDecimal correctBusinessAmount = getStatementItemCorrectAmount(statementOrderDetailDO);
            //计算已退金额
            AmountHasReturn amountHasReturn=statementOrderSupport.getStatementItemHasReturn(statementOrderDetailDO);
            BigDecimal returnStatementAmount = amountHasReturn.getReturnStatementAmount(), returnStatementRentAmount = amountHasReturn.getReturnStatementRentAmount(), returnStatementDepositAmount =amountHasReturn.getReturnStatementDepositAmount(), returnStatementRentDepositAmount = amountHasReturn.getReturnStatementRentDepositAmount();

            //计算需支付金额
            BigDecimal realNeedPayAmount=BigDecimal.ZERO;
            BigDecimal needStatementDetailOtherPayAmount=BigDecimal.ZERO;
            BigDecimal noPaidStatementDetailOtherPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOtherAmount(), statementOrderDetailDO.getStatementDetailOtherPaidAmount());
            if (BigDecimalUtil.compare(noPaidStatementDetailOtherPayAmount, BigDecimal.ZERO) > 0) {
                needStatementDetailOtherPayAmount = BigDecimalUtil.sub(noPaidStatementDetailOtherPayAmount, correctBusinessAmount);
                needStatementDetailOtherPayAmount = BigDecimalUtil.compare(needStatementDetailOtherPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailOtherPayAmount;
                statementOrderDetailDO.setStatementDetailOtherPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), needStatementDetailOtherPayAmount));
                realNeedPayAmount=BigDecimalUtil.add(realNeedPayAmount,needStatementDetailOtherPayAmount);
            }

            BigDecimal noPaidStatementDetailRentPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(), statementOrderDetailDO.getStatementDetailRentPaidAmount());
            BigDecimal needStatementDetailRentPayAmount = BigDecimal.ZERO;
            if (BigDecimalUtil.compare(noPaidStatementDetailRentPayAmount, BigDecimal.ZERO) > 0) {
                needStatementDetailRentPayAmount = BigDecimalUtil.sub(noPaidStatementDetailRentPayAmount, correctBusinessAmount);
                needStatementDetailRentPayAmount = BigDecimalUtil.add(needStatementDetailRentPayAmount, returnStatementRentAmount);
                needStatementDetailRentPayAmount = BigDecimalUtil.compare(needStatementDetailRentPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailRentPayAmount;
                statementOrderDetailDO.setStatementDetailRentPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), needStatementDetailRentPayAmount));
                realNeedPayAmount=BigDecimalUtil.add(realNeedPayAmount,needStatementDetailRentPayAmount);
            }
            BigDecimal needStatementDetailRentDepositPayAmount=BigDecimal.ZERO;
            BigDecimal noPaidStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
            if (BigDecimalUtil.compare(noPaidStatementDetailRentDepositPayAmount, BigDecimal.ZERO) > 0) {
                needStatementDetailRentDepositPayAmount = BigDecimalUtil.sub(noPaidStatementDetailRentDepositPayAmount, correctBusinessAmount);
                needStatementDetailRentDepositPayAmount = BigDecimalUtil.add(needStatementDetailRentDepositPayAmount, returnStatementRentDepositAmount);
                needStatementDetailRentDepositPayAmount = BigDecimalUtil.compare(needStatementDetailRentDepositPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailRentDepositPayAmount;
                statementOrderDetailDO.setStatementDetailRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), needStatementDetailRentDepositPayAmount));
                realNeedPayAmount=BigDecimalUtil.add(realNeedPayAmount,needStatementDetailRentDepositPayAmount);
            }

            BigDecimal noPaidStatementDetailDepositPayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositAmount(), statementOrderDetailDO.getStatementDetailDepositPaidAmount());
            BigDecimal needStatementDetailDepositPayAmount=BigDecimal.ZERO;
            if (BigDecimalUtil.compare(noPaidStatementDetailDepositPayAmount, BigDecimal.ZERO) > 0) {
                needStatementDetailDepositPayAmount = BigDecimalUtil.sub(noPaidStatementDetailDepositPayAmount, correctBusinessAmount);
                needStatementDetailDepositPayAmount = BigDecimalUtil.add(needStatementDetailDepositPayAmount, returnStatementDepositAmount);
                needStatementDetailDepositPayAmount = BigDecimalUtil.compare(needStatementDetailDepositPayAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : needStatementDetailDepositPayAmount;
                statementOrderDetailDO.setStatementDetailDepositPaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), needStatementDetailDepositPayAmount));
                realNeedPayAmount=BigDecimalUtil.add(realNeedPayAmount,needStatementDetailDepositPayAmount);
            }

            BigDecimal needStatementDetailOverduePayAmount=BigDecimal.ZERO;
            BigDecimal noPaidStatementDetailOverduePayAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOverdueAmount(), statementOrderDetailDO.getStatementDetailOverduePaidAmount());
            if (BigDecimalUtil.compare(noPaidStatementDetailOverduePayAmount, BigDecimal.ZERO) > 0) {
                // 查询有没有冲正逾期金额
                BigDecimal correctOverdueAmount = getStatementItemCorrectAmountByType(statementOrderDetailDO);
                needStatementDetailOverduePayAmount=BigDecimalUtil.compare(noPaidStatementDetailOverduePayAmount, correctOverdueAmount) >= 0?BigDecimalUtil.sub(noPaidStatementDetailOverduePayAmount, correctOverdueAmount):BigDecimal.ZERO;
                statementOrderDetailDO.setStatementDetailOverduePaidAmount(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOverduePaidAmount(), needStatementDetailOverduePayAmount));
                realNeedPayAmount=BigDecimalUtil.add(realNeedPayAmount,needStatementDetailOverduePayAmount);
            }
            String loginUserId=userSupport.getCurrentUserId().toString();

            //更新结算单项
            if(BigDecimalUtil.compare(realNeedPayAmount,BigDecimal.ZERO)>0){
                statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
                statementOrderDetailDO.setStatementDetailPaidTime(currentTime);
                statementOrderDetailDO.setUpdateTime(currentTime);
                statementOrderDetailDO.setUpdateUser(loginUserId);
                statementOrderDetailMapper.update(statementOrderDetailDO);
                //修改结算单项关联订单或续租单
                updateOrderAfterStatementItemPay(statementOrderDetailDO, needStatementDetailRentPayAmount, currentTime, loginUserId);

                rentPayAmount=BigDecimalUtil.add(rentPayAmount,needStatementDetailRentPayAmount);
                rentDepositPayAmount=BigDecimalUtil.add(rentDepositPayAmount,needStatementDetailRentDepositPayAmount);
                depositPayAmount=BigDecimalUtil.add(depositPayAmount,needStatementDetailDepositPayAmount);
                otherPayAmount=BigDecimalUtil.add(otherPayAmount,needStatementDetailOtherPayAmount);
                overduePayAmount=BigDecimalUtil.add(overduePayAmount,needStatementDetailOverduePayAmount);
                realTotalNeedPayAmount=BigDecimalUtil.add(realTotalNeedPayAmount,realNeedPayAmount);
            }

        }
        //根据结算单项支付金额修改结算单
        if(BigDecimalUtil.compare(otherPayAmount,BigDecimal.ZERO)<=0&&BigDecimalUtil.compare(rentPayAmount,BigDecimal.ZERO)<=0&&BigDecimalUtil.compare(rentDepositPayAmount,BigDecimal.ZERO)<=0&&BigDecimalUtil.compare(depositPayAmount,BigDecimal.ZERO)<=0&&BigDecimalUtil.compare(overduePayAmount,BigDecimal.ZERO)<=0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_ITEM_NO_NEED_PAY);
            return result;
        }
        updateStatementOrderByItemPay(statementOrderDO, otherPayAmount, rentPayAmount, rentDepositPayAmount, depositPayAmount,overduePayAmount, currentTime, userSupport.getCurrentUserId().toString());

        //最后支付
        CustomerDO customerDO = customerMapper.findById(statementOrderDO.getCustomerId());
        String resultCode=payStatement(userSupport.getCurrentUser(), currentTime, statementOrderDO, customerDO, rentPayAmount, rentDepositPayAmount, depositPayAmount, otherPayAmount, overduePayAmount, realTotalNeedPayAmount);
        if (!ErrorCode.SUCCESS.equals(resultCode)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setErrorCode(resultCode);
            return result;
        }
        //支付
        result.setResult(true);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private void updateStatementOrderByItemPay(StatementOrderDO statementOrderDO, BigDecimal needStatementDetailOtherPayAmount, BigDecimal needStatementDetailRentPayAmount, BigDecimal needStatementDetailRentDepositPayAmount, BigDecimal needStatementDetailDepositPayAmount,BigDecimal needStatementDetailOverduePayAmount, Date currentTime, String loginUserId) {
        statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentPaidAmount(),needStatementDetailRentPayAmount));
        statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementRentDepositPaidAmount(),needStatementDetailRentDepositPayAmount));
        statementOrderDO.setStatementDepositPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementDepositPaidAmount(),needStatementDetailDepositPayAmount));
        statementOrderDO.setStatementOtherPaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementOtherPaidAmount(),needStatementDetailOtherPayAmount));
        statementOrderDO.setStatementOverduePaidAmount(BigDecimalUtil.add(statementOrderDO.getStatementOverduePaidAmount(),needStatementDetailOverduePayAmount));
        statementOrderDO.setStatementPaidTime(currentTime);
        statementOrderDO.setUpdateTime(currentTime);
        statementOrderDO.setUpdateUser(loginUserId.toString());

        BigDecimal totalPaidAmount=BigDecimalUtil.addAll(statementOrderDO.getStatementRentPaidAmount(),statementOrderDO.getStatementRentDepositPaidAmount(),statementOrderDO.getStatementDepositPaidAmount(),statementOrderDO.getStatementOtherPaidAmount(),statementOrderDO.getStatementOverduePaidAmount());
        if(BigDecimalUtil.compare(statementOrderDO.getStatementAmount(),totalPaidAmount)<=0){
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
        }else if(BigDecimalUtil.compare(BigDecimal.ZERO,totalPaidAmount)==0){
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
        }
        else{
            statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
        }
        statementOrderMapper.update(statementOrderDO);
    }

    private void updateOrderAfterStatementItemPay(StatementOrderDetailDO statementOrderDetailDO, BigDecimal realNeedPayAmount, Date currentTime, String loginUserId) {
        if (statementOrderDetailDO.getReletOrderItemReferId() == null) {
            OrderDO orderDO=orderMapper.findByOrderId(statementOrderDetailDO.getOrderId());
            if (orderDO != null) {
                if (!PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())) {
                    orderDO.setPayStatus(PayStatus.PAY_STATUS_PAID);
                }
                orderDO.setTotalPaidOrderAmount(BigDecimalUtil.add(orderDO.getTotalPaidOrderAmount(), realNeedPayAmount));
                orderDO.setPayTime(currentTime);
                orderMapper.update(orderDO);
                orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), OrderStatus.ORDER_STATUS_PAID, null, currentTime, loginUserId, OperationType.STATEMENT_PAID);
            }

        } else {
            Integer reletOrderId=null;
            if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetailDO.getOrderItemType())) {
                ReletOrderProductDO reletOrderProductDO = reletOrderProductMapper.findById(statementOrderDetailDO.getReletOrderItemReferId());
                reletOrderId=reletOrderProductDO.getReletOrderId();
            }
            if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetailDO.getOrderItemType())) {
                ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialMapper.findById(statementOrderDetailDO.getReletOrderItemReferId());
                reletOrderId=reletOrderMaterialDO.getReletOrderId();
            }
            if(reletOrderId!=null){
                ReletOrderDO reletOrderDO= reletOrderMapper.findById(reletOrderId);
                if(reletOrderDO!=null){
                    if (!PayStatus.PAY_STATUS_PAID.equals(reletOrderDO.getPayStatus())) {
                        reletOrderDO.setPayStatus(PayStatus.PAY_STATUS_PAID);
                    }
                    reletOrderDO.setTotalPaidOrderAmount(BigDecimalUtil.add(reletOrderDO.getTotalPaidOrderAmount(), realNeedPayAmount));
                    reletOrderDO.setPayTime(currentTime);
                    reletOrderMapper.update(reletOrderDO);
                }
            }
        }
    }

    private BigDecimal getStatementItemCorrectAmountByType(StatementOrderDetailDO statementOrderDetailDO) {
        List<StatementOrderCorrectDetailDO> statementOrderCorrectOverdueDetailDOList = statementOrderCorrectDetailMapper.findByStatementDetailIdAndType(statementOrderDetailDO.getId(), StatementDetailType.STATEMENT_DETAIL_TYPE_OVERDUE);
        BigDecimal correctOverdueAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderCorrectOverdueDetailDOList)) {
            for (StatementOrderCorrectDetailDO statementOrderCorrectDetailDO : statementOrderCorrectOverdueDetailDOList) {
                if (statementOrderCorrectDetailDO != null && BigDecimalUtil.compare(statementOrderCorrectDetailDO.getStatementCorrectAmount(), BigDecimal.ZERO) > 0) {
                    correctOverdueAmount = statementOrderCorrectDetailDO.getStatementCorrectAmount();
                }
            }
        }
        return correctOverdueAmount;
    }

    String payVerify(StatementOrderDetailDO statementOrderDetailDO) {
        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus())
                || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDetailDO.getStatementDetailStatus())) {
            return ErrorCode.STATEMENT_ORDER_STATUS_ERROR;
        }

        List<StatementOrderDetailDO> statementOrderDetailDOList=statementOrderDetailMapper.findByOrderId(statementOrderDetailDO.getOrderId());
        for (StatementOrderDetailDO dbStatementOrderDetail : statementOrderDetailDOList) {
            if (!StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(dbStatementOrderDetail.getStatementDetailStatus())
                    && !StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(dbStatementOrderDetail.getStatementDetailStatus())) {
                if (dbStatementOrderDetail.getStatementOrderId().equals(statementOrderDetailDO.getStatementOrderId())) {
                    break;
                }
                //如果未支付状态但，需支付金额为零可以跳过
                BigDecimal returnAmount=statementOrderSupport.getStatementItemHasReturn(dbStatementOrderDetail).getReturnStatementAmount();
                BigDecimal hasPaidAmount=BigDecimalUtil.addAll(dbStatementOrderDetail.getStatementDetailDepositPaidAmount(),dbStatementOrderDetail.getStatementDetailOtherPaidAmount(),dbStatementOrderDetail.getStatementDetailOverduePaidAmount(),dbStatementOrderDetail.getStatementDetailPenaltyPaidAmount(),dbStatementOrderDetail.getStatementDetailRentPaidAmount(),dbStatementOrderDetail.getStatementDetailRentDepositPaidAmount());
                if(BigDecimalUtil.compare(BigDecimalUtil.add(returnAmount,BigDecimalUtil.sub(dbStatementOrderDetail.getStatementDetailAmount(),hasPaidAmount)),dbStatementOrderDetail.getStatementDetailCorrectAmount())<=0)continue;
                return ErrorCode.STATEMENT_ORDER_CAN_NOT_PAID_THIS;
            }
        }

        return ErrorCode.SUCCESS;
    }

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ReletOrderMapper reletOrderMapper;

    @Autowired
    private ReletOrderProductMapper reletOrderProductMapper;

    @Autowired
    private ReletOrderMaterialMapper reletOrderMaterialMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private AmountSupport amountSupport;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

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
    private DingDingSupport dingDingSupport;
    @Autowired
    private ResultGenerator resultGenerator;
    @Autowired
    private ProductSupport productSupport;
    @Autowired
    private K3OrderStatementConfigMapper k3OrderStatementConfigMapper;
    @Autowired
    private OrderStatementDateSplitMapper orderStatementDateSplitMapper;
    @Autowired
    private OrderSupport orderSupport;
    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;


}
