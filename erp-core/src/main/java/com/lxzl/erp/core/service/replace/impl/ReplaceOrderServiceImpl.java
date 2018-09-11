package com.lxzl.erp.core.service.replace.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.replace.ReplaceOrderQueryParam;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderMaterial;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderProduct;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.replace.ReplaceOrderService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderProductDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderProductDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\5 0005 10:08
 */
@Service
public class ReplaceOrderServiceImpl implements ReplaceOrderService{
    private static final Logger logger = LoggerFactory.getLogger(ReplaceOrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> add(ReplaceOrder replaceOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date date = new Date();
        //校验客户编号
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(replaceOrder.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        //校验换货项是否为空
        ReplaceOrderDO replaceOrderDO = ConverterUtil.convert(replaceOrder, ReplaceOrderDO.class);
        List<ReplaceOrderProductDO> replaceOrderProductDOList = replaceOrderDO.getReplaceOrderProductDOList();
        List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList = replaceOrderDO.getReplaceOrderMaterialDOList();
        if (CollectionUtil.isEmpty(replaceOrderProductDOList) && CollectionUtil.isEmpty(replaceOrderMaterialDOList)) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_DETAIL_LIST_NOT_NULL);
            return serviceResult;
        }

        //校验订单编号
        OrderDO orderDO = orderMapper.findByOrderNo(replaceOrderDO.getOrderNo());
        if (orderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }

        //查出该订单的续租单
        Map<Integer,ReletOrderProductDO> reletOrderProductDOMap = new HashMap<>();
        Map<Integer,ReletOrderMaterialDO> reletOrderMaterialDOMap = new HashMap<>();
        ReletOrderDO reletOrderDO = reletOrderMapper.findRecentlyReletedOrderByOrderId(orderDO.getId());
        if (reletOrderDO != null){
            List<ReletOrderProductDO> reletOrderProductDOList = reletOrderDO.getReletOrderProductDOList();
            List<ReletOrderMaterialDO> reletOrderMaterialDOList = reletOrderDO.getReletOrderMaterialDOList();
            if (CollectionUtil.isNotEmpty(reletOrderProductDOList)) {
                for (ReletOrderProductDO reletOrderProductDO:reletOrderProductDOList) {
                    reletOrderProductDOMap.put(reletOrderProductDO.getOrderProductId(),reletOrderProductDO);
                }
            }
            if (CollectionUtil.isNotEmpty(reletOrderMaterialDOList)) {
                for (ReletOrderMaterialDO reletOrderMaterialDO:reletOrderMaterialDOList) {
                    reletOrderMaterialDOMap.put(reletOrderMaterialDO.getOrderMaterialId(),reletOrderMaterialDO);
                }
            }
        }

        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
        //只有确认收货和部分归还状态的才可以换货
        if (!OrderStatus.ORDER_STATUS_CONFIRM.equals(orderDO.getOrderStatus()) &&
                !OrderStatus.ORDER_STATUS_PART_RETURN.equals(orderDO.getOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        //存放原订单商品项和配件项
        Map<Integer,OrderProductDO> orderProductDOMap = new TreeMap<>();
        Map<Integer,OrderMaterialDO> orderMaterialDOMap = new TreeMap<>();
        if (CollectionUtil.isNotEmpty(orderProductDOList)){
            for (OrderProductDO orderProductDO : orderProductDOList) {
                orderProductDOMap.put(orderProductDO.getId(),orderProductDO);
            }
        }
        if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                orderMaterialDOMap.put(orderMaterialDO.getId(),orderMaterialDO);
            }
        }

        //校验换货时间
        Date replaceTime = replaceOrder.getReplaceTime();
        Date rentStartTime = orderDO.getRentStartTime();
        Date expectReturnTime = orderDO.getExpectReturnTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String replaceTimeString = simpleDateFormat.format(replaceTime);
        String rentStartTimeString = simpleDateFormat.format(rentStartTime);
        String expectReturnTimeString = simpleDateFormat.format(expectReturnTime);
        try {
            Date replaceTimeDate = simpleDateFormat.parse(replaceTimeString);
            Date rentStartTimeDate = simpleDateFormat.parse(rentStartTimeString);
            Date expectReturnTimeDate = simpleDateFormat.parse(expectReturnTimeString);
            if (!(replaceTimeDate.compareTo(rentStartTimeDate)>0)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_MUST_AFTER_RENT_START_TIME);
                return serviceResult;
            } else if (!(rentStartTimeDate.compareTo(expectReturnTimeDate)<0)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_MUST_BEFORE_EXPECT_RETURN_TIME);
                return serviceResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("【创建换货单,校验换货时间parse出错】", e);
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_PARSE_ERROR);
            return serviceResult;
        }
        //校验是否在续租单开始之前换货
        ReletOrderDO exReletOrderDO = reletOrderMapper.findRecentlyReletOrderByOrderNo(orderDO.getOrderNo());
        Date reletTime = exReletOrderDO.getRentStartTime();
        String reletTimeString = simpleDateFormat.format(reletTime);
        try {
            Date replaceTimeDate = simpleDateFormat.parse(replaceTimeString);
            Date reletTimeDate = simpleDateFormat.parse(reletTimeString);
            if (!(replaceTimeDate.compareTo(reletTimeDate)>0)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_BEFORE_RELET_TIME);
                return serviceResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("【创建换货单,校验是否在续租单开始之前换货,时间parse出错】", e);
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_PARSE_ERROR);
            return serviceResult;
        }

        //获取客户风控信息
        CustomerRiskManagementDO customerRiskManagementDO = customerSupport.getCustomerRiskManagementDO(orderDO.getBuyerCustomerId());
        // 设备信用押金总额
        BigDecimal totalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        // 校验客户风控信息
        verifyCustomerRiskInfo(replaceOrderDO,customerDO,orderDO,customerRiskManagementDO);
        //补全换货商品项信息
        calculateOrderProductInfo(replaceOrderProductDOList,orderProductDOMap, orderDO,totalCreditDepositAmount,customerRiskManagementDO);
        //补全换货配件项信息
        calculateOrderMaterialInfo(replaceOrderMaterialDOList,orderMaterialDOMap, orderDO,totalCreditDepositAmount,customerRiskManagementDO);

        if (customerRiskManagementDO == null && BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) > 0) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_NEED_RISK_INFO);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), totalCreditDepositAmount), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_AMOUNT_OVER_FLOW);
            return serviceResult;
        }

        //校验换货数量

        //获取换货数量
        Integer totalReplaceProductCount = 0;
        Integer totalReplaceMaterialCount = 0;
        Map<Integer, Integer> replaceProductCountMap = new HashMap<>();
        Map<Integer, Integer> replaceMaterialCountMap = new HashMap<>();
        for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
            totalReplaceProductCount += replaceOrderProductDO.getProductCount();
            if (!replaceProductCountMap.containsKey(replaceOrderProductDO.getOldOrderProductId())) {
                replaceProductCountMap.put(replaceOrderProductDO.getOldOrderProductId(), replaceOrderProductDO.getProductCount());
            }else {
                Integer replaceProductCount = replaceOrderProductDO.getProductCount() + replaceProductCountMap.get(replaceOrderProductDO.getOldOrderProductId());
                replaceProductCountMap.put(replaceOrderProductDO.getOldOrderProductId(), replaceProductCount);
            }
        }
        for (ReplaceOrderMaterialDO replaceOrderMaterialDO : replaceOrderMaterialDOList) {
            totalReplaceMaterialCount += replaceOrderMaterialDO.getMaterialCount();
            if (!replaceMaterialCountMap.containsKey(replaceOrderMaterialDO.getOldOrderMaterialId())) {
                replaceMaterialCountMap.put(replaceOrderMaterialDO.getOldOrderMaterialId(), replaceOrderMaterialDO.getMaterialCount());
            }else {
                Integer replaceMaterialCount = replaceOrderMaterialDO.getMaterialCount() + replaceMaterialCountMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
                replaceMaterialCountMap.put(replaceOrderMaterialDO.getOldOrderMaterialId(), replaceMaterialCount);
            }
        }
        //获取在租数量
        Map<Integer, Integer> rentingProductCountMap = new HashMap<>();
        Map<Integer, Integer> rentingMaterialCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderProductDOList)) {
            for (OrderProductDO orderProductDO : orderProductDOList) {
                rentingProductCountMap.put(orderProductDO.getId(), orderProductDO.getRentingProductCount());
            }
        }
        if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                rentingMaterialCountMap.put(orderMaterialDO.getId(), orderMaterialDO.getRentingMaterialCount());
            }
        }
        //获取该用户未完成的退货、换货
        Map<Integer, Integer> productCountMap = new HashMap<>();
        Map<Integer, Integer> materialCountMap = new HashMap<>();
        List<K3ReturnOrderDO> k3ReturnOrderDOList = k3ReturnOrderMapper.findByCustomerNo(orderDO.getBuyerCustomerNo());
        //获取该用户处于待提交、审核中、处理中三种状态的商品或者配件的数量
        getReturnCount(productCountMap, materialCountMap, k3ReturnOrderDOList);
        //获取该订单待提交和审核中的换货单，统计换货的商品和配件的数量
        List<ReplaceOrderDO> replaceOrderDOList = replaceOrderMapper.findByOrderNoForCheck(replaceOrder.getOrderNo());
        //将该订单的待提交、审核中两种状态的换货单商品或配件数量保存
        if (CollectionUtil.isNotEmpty(replaceOrderDOList)) {
            for (ReplaceOrderDO exReplaceOrderDO:replaceOrderDOList){
                List<ReplaceOrderProductDO> exReplaceOrderProductDOList = exReplaceOrderDO.getReplaceOrderProductDOList();
                List<ReplaceOrderMaterialDO> exReplaceOrderMaterialDOList = exReplaceOrderDO.getReplaceOrderMaterialDOList();
                if (CollectionUtil.isNotEmpty(exReplaceOrderProductDOList)) {
                    for (ReplaceOrderProductDO replaceOrderProductDO:exReplaceOrderProductDOList) {
                        if (!productCountMap.containsKey(replaceOrderProductDO.getOldOrderProductId())) {
                            productCountMap.put(replaceOrderProductDO.getOldOrderProductId(), replaceOrderProductDO.getProductCount());
                        }else {
                            Integer productCount = replaceOrderProductDO.getProductCount() + productCountMap.get(replaceOrderProductDO.getOldOrderProductId());
                            productCountMap.put(replaceOrderProductDO.getOldOrderProductId(), productCount);
                        }
                    }
                }
                if (CollectionUtil.isNotEmpty(exReplaceOrderMaterialDOList)) {
                    for (ReplaceOrderMaterialDO replaceOrderMaterialDO:exReplaceOrderMaterialDOList) {
                        if (!replaceMaterialCountMap.containsKey(replaceOrderMaterialDO.getOldOrderMaterialId())) {
                            materialCountMap.put(replaceOrderMaterialDO.getOldOrderMaterialId(), replaceOrderMaterialDO.getMaterialCount());
                        }else {
                            Integer materialCount = replaceOrderMaterialDO.getMaterialCount() + materialCountMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
                            materialCountMap.put(replaceOrderMaterialDO.getOldOrderMaterialId(), materialCount);
                        }
                    }
                }
            }
        }
        //比较设备项
        for (OrderProductDO orderProductDO : orderProductDOList) {
            orderProductDOMap.put(orderProductDO.getId(),orderProductDO);
            Integer rentingProductCount = rentingProductCountMap.get(orderProductDO.getId()) == null ? 0 : rentingProductCountMap.get(orderProductDO.getId());//在租数
            Integer processProductCount = productCountMap.get(orderProductDO.getId()) == null ? 0 : productCountMap.get(orderProductDO.getId()); //待提交、处理中和审核中数量
            Integer replaceProductCount = replaceProductCountMap.get(orderProductDO.getId()) == null ? 0 : replaceProductCountMap.get(orderProductDO.getId()); //换货数量
            Integer canReturnProductCount = rentingProductCount - processProductCount;
            if (replaceProductCount > canReturnProductCount) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_PRODUCT_COUNT_MORE_THAN_CANREPLACE_COUNT);
                return serviceResult;
            }
        }

        //比较物料项
        for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
            orderMaterialDOMap.put(orderMaterialDO.getId(),orderMaterialDO);
            Integer rentingMaterialCount = rentingMaterialCountMap.get(orderMaterialDO.getId()) == null ? 0 : rentingMaterialCountMap.get(orderMaterialDO.getId());//在租数
            Integer processMaterialCount = materialCountMap.get(orderMaterialDO.getId()) == null ? 0 : materialCountMap.get(orderMaterialDO.getId()); //待提交、处理中和审核中数量
            Integer replaceMaterialCount = replaceMaterialCountMap.get(orderMaterialDO.getId()) == null ? 0 : replaceMaterialCountMap.get(orderMaterialDO.getId()); //换货数量
            Integer canReturnMaterialCount = rentingMaterialCount - processMaterialCount;
            if (replaceMaterialCount > canReturnMaterialCount) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_MATERIAL_COUNT_MORE_THAN_CANREPLACE_COUNT);
                return serviceResult;
            }
        }
        //保存换货单信息
        replaceOrderDO.setReplaceOrderNo("LXREO" + DateUtil.formatDate(date, "yyyyMMddHHmmssSSS"));
        replaceOrderDO.setOrderId(orderDO.getId());
        replaceOrderDO.setCustomerId(customerDO.getId());
        replaceOrderDO.setTotalReplaceProductCount(totalReplaceProductCount);
        replaceOrderDO.setTotalReplaceMaterialCount(totalReplaceMaterialCount);
        replaceOrderDO.setRealTotalReplaceProductCount(CommonConstant.COMMON_ZERO);
        replaceOrderDO.setRealTotalReplaceMaterialCount(CommonConstant.COMMON_ZERO);
        replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_WAIT_COMMIT);
        replaceOrderDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
        replaceOrderDO.setCreateUser(userSupport.getCurrentUserId().toString());
        replaceOrderDO.setCreateTime(date);
        replaceOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        replaceOrderDO.setUpdateTime(date);
        replaceOrderDO.setConfirmReplaceUser(null);
        replaceOrderDO.setConfirmReplaceTime(null);
        replaceOrderMapper.save(replaceOrderDO);
        //保存换货商品项
        List<ReplaceOrderProductDO> saveReplaceOrderProductDOList = new ArrayList<>();
        for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
            OrderProductDO orderProductDO = orderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
            replaceOrderProductDO.setOldProductEntry(orderProductDO.getFEntryID());
            replaceOrderProductDO.setOldProductUnitAmount(orderProductDO.getProductUnitAmount());
            replaceOrderProductDO.setReplaceOrderId(replaceOrderDO.getId());
            replaceOrderProductDO.setReplaceOrderNo(replaceOrderDO.getReplaceOrderNo());
            replaceOrderProductDO.setNewOrderProductId(null);
            replaceOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
            replaceOrderProductDO.setCreateTime(date);
            replaceOrderProductDO.setUpdateTime(date);
            replaceOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            replaceOrderProductDO.setRealReplaceProductCount(CommonConstant.COMMON_ZERO);
            ReletOrderProductDO reletOrderProductDO = reletOrderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
            if (reletOrderProductDO!= null) {
                replaceOrderProductDO.setIsReletOrderReplace(CommonConstant.COMMON_CONSTANT_YES);
                replaceOrderProductDO.setReletOrderItemId(reletOrderProductDO.getId());
                replaceOrderProductDO.setOldProductUnitAmount(reletOrderProductDO.getProductUnitAmount());
            }else {
                replaceOrderProductDO.setIsReletOrderReplace(CommonConstant.COMMON_CONSTANT_NO);
                replaceOrderProductDO.setReletOrderItemId(null);
            }
            saveReplaceOrderProductDOList.add(replaceOrderProductDO);
        }
        //保存换货商品项
        List<ReplaceOrderMaterialDO> saveReplaceOrderMaterialDOList = new ArrayList<>();
        for (ReplaceOrderMaterialDO replaceOrderMaterialDO : replaceOrderMaterialDOList) {
            OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
            replaceOrderMaterialDO.setOldMaterialEntry(orderMaterialDO.getFEntryID());
            replaceOrderMaterialDO.setOldMaterialUnitAmount(orderMaterialDO.getMaterialUnitAmount());
            replaceOrderMaterialDO.setReplaceOrderId(replaceOrderDO.getId());
            replaceOrderMaterialDO.setReplaceOrderNo(replaceOrderDO.getReplaceOrderNo());
            replaceOrderMaterialDO.setNewOrderMaterialId(null);
            replaceOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
            replaceOrderMaterialDO.setCreateTime(date);
            replaceOrderMaterialDO.setUpdateTime(date);
            replaceOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            replaceOrderMaterialDO.setRealReplaceMaterialCount(CommonConstant.COMMON_ZERO);
            ReletOrderMaterialDO reletOrderMaterialDO = reletOrderMaterialDOMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
            if (reletOrderMaterialDO!= null) {
                replaceOrderMaterialDO.setIsReletOrderReplace(CommonConstant.COMMON_CONSTANT_YES);
                replaceOrderMaterialDO.setReletOrderItemId(reletOrderMaterialDO.getId());
                replaceOrderMaterialDO.setOldMaterialUnitAmount(reletOrderMaterialDO.getMaterialUnitAmount());
            }else {
                replaceOrderMaterialDO.setIsReletOrderReplace(CommonConstant.COMMON_CONSTANT_NO);
                replaceOrderMaterialDO.setReletOrderItemId(null);
            }
            saveReplaceOrderMaterialDOList.add(replaceOrderMaterialDO);
        }
        if (CollectionUtil.isNotEmpty(saveReplaceOrderProductDOList)) {
            replaceOrderProductMapper.saveList(saveReplaceOrderProductDOList);
        }
        if (CollectionUtil.isNotEmpty(saveReplaceOrderMaterialDOList)) {
            replaceOrderMaterialMapper.saveList(saveReplaceOrderProductDOList);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 修改换货单
     */
    @Override
    public ServiceResult<String, String> update(ReplaceOrder replaceOrder) {
        return null;
    }
    /**
     * 换货取消接口
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult cancel(ReplaceOrder replaceOrder) {
        ServiceResult serviceResult = new ServiceResult();
        Date date = new Date();
        ReplaceOrderDO replaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(replaceOrder.getReplaceOrderNo());
        if (replaceOrderDO != null) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_ERROR);
            return serviceResult;
        }
        if (!userSupport.getCurrentUserId().toString().equals(replaceOrder.getCreateUser())) {
            serviceResult.setErrorCode(ErrorCode.CANCEL_REPLACE_ORDER_BY_CREATE_USER);
            return serviceResult;
        }
        if (!ReplaceOrderStatus.REPLACE_ORDER_STATUS_WAIT_COMMIT.equals(replaceOrder.getReplaceOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.CANCEL_REPLACE_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        replaceOrderDO.setDataStatus(CommonConstant.COMMON_TWO);
        replaceOrderDO.setUpdateTime(date);
        replaceOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        List<ReplaceOrderProductDO> replaceOrderProductDOList = replaceOrderDO.getReplaceOrderProductDOList();
        if (CollectionUtil.isNotEmpty(replaceOrderProductDOList)) {
            for (ReplaceOrderProductDO replaceOrderProductDO:replaceOrderProductDOList) {
                replaceOrderProductDO.setDataStatus(CommonConstant.COMMON_TWO);
                replaceOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                replaceOrderProductDO.setUpdateTime(date);
            }
        }
        List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList = replaceOrderDO.getReplaceOrderMaterialDOList();
        if (CollectionUtil.isNotEmpty(replaceOrderMaterialDOList)) {
            for (ReplaceOrderMaterialDO replaceOrderMaterialDO:replaceOrderMaterialDOList) {
                replaceOrderMaterialDO.setDataStatus(CommonConstant.COMMON_TWO);
                replaceOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                replaceOrderMaterialDO.setUpdateTime(date);
            }
        }
        replaceOrderMapper.update(replaceOrderDO);
        replaceOrderProductMapper.updateListForCancel(replaceOrderProductDOList);
        replaceOrderMaterialMapper.updateListForCancel(replaceOrderMaterialDOList);
        return null;
    }
    /**
     * 分页查询换货单
     */
    @Override
    public ServiceResult<String, Page<ReplaceOrder>> queryAllReplaceOrder(ReplaceOrderQueryParam param) {
        ServiceResult<String, Page<ReplaceOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("replaceOrderQueryParam", param);
//        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER,PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_WAREHOUSE));
        Integer totalCount = replaceOrderMapper.findReplaceOrderCountByParams(maps);
        List<ReplaceOrderDO> replaceOrderDOList = replaceOrderMapper.findReplaceOrderByParams(maps);
        List<ReplaceOrder> replaceOrderList = ConverterUtil.convertList(replaceOrderDOList, ReplaceOrder.class);
        Page<ReplaceOrder> page = new Page<>(replaceOrderList, totalCount, param.getPageNo(), param.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }
    /**
     * 通过换货单编号查询换货单详情
     */
    @Override
    public ServiceResult<String, ReplaceOrder> queryReplaceOrderByNo(String replaceOrderNo) {
        ServiceResult<String, ReplaceOrder> result = new ServiceResult<>();
        ReplaceOrderDO replaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(replaceOrderNo);
        if (replaceOrderDO == null) {
            result.setErrorCode(ErrorCode.REPLACE_ORDER_ERROR);
        }else {
            ReplaceOrder replaceOrder = ConverterUtil.convert(replaceOrderDO,ReplaceOrder.class);
            result.setResult(replaceOrder);
            result.setErrorCode(ErrorCode.SUCCESS);
        }
        return result;
    }
    /**
     * 确认换货
     */
    @Override
    public ServiceResult<String, String> confirmReplaceOrder(ReplaceOrder replaceOrder) {
        return null;
    }

    /**
     * 获取该用户处于待提交、审核中、处理中三种状态的商品或者配件的数量
     *
     * @param productCountMap
     * @param materialCountMap
     * @param k3ReturnOrderDOList
     */
    private void getReturnCount(Map<Integer, Integer> productCountMap, Map<Integer, Integer> materialCountMap, List<K3ReturnOrderDO> k3ReturnOrderDOList) {
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDOList)) {
            for (K3ReturnOrderDO dBK3ReturnOrderDO : k3ReturnOrderDOList) {
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(dBK3ReturnOrderDO.getReturnOrderStatus())
                        || ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(dBK3ReturnOrderDO.getReturnOrderStatus())
                        || ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(dBK3ReturnOrderDO.getReturnOrderStatus())) {
                    List<K3ReturnOrderDetailDO> dBK3ReturnOrderDetailDOList = k3ReturnOrderDetailMapper.findListByReturnOrderId(dBK3ReturnOrderDO.getId());
                    //获取商品和配件的退货数量和存入集合中
                    getProductAndMaterialReturnCount(productCountMap, materialCountMap, dBK3ReturnOrderDetailDOList);
                }
            }
        }
    }
    /**
     * 获取商品和配件的退货数量和存入集合中
     *
     * @param productCountMap
     * @param materialCountMap
     * @param dBK3ReturnOrderDetailDOList
     */
    private void getProductAndMaterialReturnCount(Map<Integer, Integer> productCountMap, Map<Integer, Integer> materialCountMap, List<K3ReturnOrderDetailDO> dBK3ReturnOrderDetailDOList) {
        Integer materialId;
        Integer productId;
        for (K3ReturnOrderDetailDO k3ReturnOrderDetailDO : dBK3ReturnOrderDetailDOList) {
            String productNo = k3ReturnOrderDetailDO.getProductNo();
            if (productSupport.isMaterial(productNo)) {
                //物料
                materialId = productSupport.getMaterialId(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
                if (materialCountMap.get(materialId) == null) {
                    materialCountMap.put(materialId, k3ReturnOrderDetailDO.getProductCount());
                } else {
                    materialCountMap.put(materialId, k3ReturnOrderDetailDO.getProductCount() + materialCountMap.get(materialId));
                }
            } else {
                //设备
                productId = productSupport.getProductId(k3ReturnOrderDetailDO.getOrderNo(), k3ReturnOrderDetailDO.getOrderItemId(), k3ReturnOrderDetailDO.getOrderEntry());
                if (productCountMap.get(productId) == null) {
                    productCountMap.put(productId, k3ReturnOrderDetailDO.getProductCount());
                } else {
                    productCountMap.put(productId, k3ReturnOrderDetailDO.getProductCount() + productCountMap.get(productId));
                }
            }
        }
    }
    /**
     * 创建换货单校验风控信息
     */
    public void verifyCustomerRiskInfo(ReplaceOrderDO replaceOrderDO,CustomerDO customerDO,OrderDO orderDO,CustomerRiskManagementDO customerRiskManagementDO) {
        boolean isCheckRiskManagement = orderService.isCheckRiskManagement(orderDO);
        if (isCheckRiskManagement) {
            if (customerRiskManagementDO == null) {
                throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
            }
        }
        if (CollectionUtil.isNotEmpty(replaceOrderDO.getReplaceOrderProductDOList())) {
            for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderDO.getReplaceOrderProductDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(replaceOrderProductDO.getRentType())
                        && replaceOrderProductDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }
                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(replaceOrderProductDO.getProductSkuId());
                    if (!productServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                        throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    Product product = productServiceResult.getResult();
                    ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);

                    if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderProductDO.getIsNewProduct()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(skuPrice, customerRiskManagementDO.getSingleLimitPrice()) > 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }
                    boolean productIsCheckRiskManagement = isCheckRiskManagement(replaceOrderProductDO, null);
                    if (!productIsCheckRiskManagement) {
                        if (replaceOrderProductDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        continue;
                    }
                } else {
                    if (replaceOrderProductDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(replaceOrderDO.getReplaceOrderMaterialDOList())) {
            for (ReplaceOrderMaterialDO replaceOrderMaterialDO : replaceOrderDO.getReplaceOrderMaterialDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(replaceOrderMaterialDO.getRentType())
                        && replaceOrderMaterialDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }
                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialById(replaceOrderMaterialDO.getMaterialId());
                    Material material = materialResult.getResult();
                    if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderMaterialDO.getIsNewMaterial()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(materialPrice, customerRiskManagementDO.getSingleLimitPrice()) >= 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }
                    boolean materialIsCheckRiskManagement = isCheckRiskManagement(null, replaceOrderMaterialDO);
                    if (!materialIsCheckRiskManagement) {
                        if (replaceOrderMaterialDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        continue;
                    }
                } else {
                    if (replaceOrderMaterialDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    }
                }
            }
        }
    }

    private boolean isCheckRiskManagement(ReplaceOrderProductDO replaceOrderProductDO, ReplaceOrderMaterialDO replaceOrderMaterialDO) {
        if (replaceOrderProductDO != null) {
            if (OrderRentType.RENT_TYPE_DAY.equals(replaceOrderProductDO.getRentType())
                    && replaceOrderProductDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                return true;
            }
            if (OrderRentType.RENT_TYPE_MONTH.equals(replaceOrderProductDO.getRentType())) {
                return true;
            }
        }
        if (replaceOrderMaterialDO != null) {
            if (OrderRentType.RENT_TYPE_DAY.equals(replaceOrderMaterialDO.getRentType())
                    && replaceOrderMaterialDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                return true;
            }
            if (OrderRentType.RENT_TYPE_MONTH.equals(replaceOrderMaterialDO.getRentType())) {
                return true;
            }
        }
        return false;
    }

    public void calculateOrderProductInfo(List<ReplaceOrderProductDO> replaceOrderProductDOList,Map<Integer,OrderProductDO> orderProductDOMap,OrderDO orderDO,BigDecimal totalCreditDepositAmount,CustomerRiskManagementDO customerRiskManagementDO) {

        if (CollectionUtil.isNotEmpty(replaceOrderProductDOList)) {
            for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;

                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(replaceOrderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                ProductSku productSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (productSku == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                String skuName = productSku.getSkuName();

                // 小于等于90天的,不走风控，大于90天的，走风控授信
                if (OrderRentType.RENT_TYPE_DAY.equals(replaceOrderProductDO.getRentType()) && replaceOrderProductDO.getRentTimeLength() <= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    if (replaceOrderProductDO.getProductCount() > 0) {
                        BigDecimal remainder = replaceOrderProductDO.getDepositAmount().divideAndRemainder(new BigDecimal(replaceOrderProductDO.getProductCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_PRODUCT_DEPOSIT_ERROR);
                        }
                    }
                    depositAmount = replaceOrderProductDO.getDepositAmount();
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(replaceOrderProductDO.getProductCount()));
                } else {
                    if ((BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) || CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderProductDO.getIsNewProduct())) {
                        Integer depositCycle = replaceOrderProductDO.getDepositCycle() <= replaceOrderProductDO.getRentTimeLength() ? replaceOrderProductDO.getDepositCycle() : replaceOrderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderProductDO.getProductUnitAmount(), new BigDecimal(replaceOrderProductDO.getProductCount()), 2), new BigDecimal(depositCycle));
                    } else {
                        Integer depositCycle = replaceOrderProductDO.getDepositCycle() <= replaceOrderProductDO.getRentTimeLength() ? replaceOrderProductDO.getDepositCycle() : replaceOrderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderProductDO.getProductUnitAmount(), new BigDecimal(replaceOrderProductDO.getProductCount()), 2), new BigDecimal(depositCycle));
                    }
                    creditDepositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(replaceOrderProductDO.getProductCount()));
                    OrderProductDO orderProductDO = orderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
                    BigDecimal oldSkuPrice= BigDecimalUtil.div(orderProductDO.getCreditDepositAmount(),new BigDecimal(orderProductDO.getProductCount()),4);
                    BigDecimal oldOrderProductCreditDepositAmount = BigDecimalUtil.mul(oldSkuPrice, new BigDecimal(replaceOrderProductDO.getProductCount()));
                    totalCreditDepositAmount = BigDecimalUtil.sub(totalCreditDepositAmount,oldOrderProductCreditDepositAmount);
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                }
                replaceOrderProductDO.setProductId(product.getProductId());
                replaceOrderProductDO.setProductName(product.getProductName());
                replaceOrderProductDO.setRentDepositAmount(rentDepositAmount);
                replaceOrderProductDO.setDepositAmount(depositAmount);
                replaceOrderProductDO.setCreditDepositAmount(creditDepositAmount);
                replaceOrderProductDO.setProductSkuName(skuName);
            }
        }
    }

    public void calculateOrderMaterialInfo(List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList,Map<Integer,OrderMaterialDO> orderMaterialDOMap,OrderDO orderDO,BigDecimal totalCreditDepositAmount,CustomerRiskManagementDO customerRiskManagementDO) {
        if (CollectionUtil.isNotEmpty(replaceOrderMaterialDOList)) {

            for (ReplaceOrderMaterialDO replaceOrderMaterialDO : replaceOrderMaterialDOList) {
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;

                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(replaceOrderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                if (material == null) {
                    throw new BusinessException(ErrorCode.MATERIAL_NOT_EXISTS);
                }
                String materialName = material.getMaterialName();
                BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();

                // 小于等于90天的,不走风控，大于90天的，走风控授信
                if (OrderRentType.RENT_TYPE_DAY.equals(replaceOrderMaterialDO.getRentType()) && replaceOrderMaterialDO.getRentTimeLength() <= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    if (replaceOrderMaterialDO.getMaterialCount() > 0) {
                        BigDecimal remainder = replaceOrderMaterialDO.getDepositAmount().divideAndRemainder(new BigDecimal(replaceOrderMaterialDO.getMaterialCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_MATERIAL_DEPOSIT_ERROR);
                        }
                    }

                    depositAmount = replaceOrderMaterialDO.getDepositAmount();
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(replaceOrderMaterialDO.getMaterialCount()));
                } else {
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderMaterialDO.getIsNewMaterial())) {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(replaceOrderMaterialDO.getMaterialCount()), 2), new BigDecimal(replaceOrderMaterialDO.getDepositCycle()));
                    } else {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(replaceOrderMaterialDO.getMaterialCount()), 2), new BigDecimal(replaceOrderMaterialDO.getDepositCycle()));
                    }
                    MaterialTypeDO materialTypeDO = materialTypeMapper.findById(material.getMaterialType());
                    if (materialTypeDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsMainMaterial())) {
                        creditDepositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(replaceOrderMaterialDO.getMaterialCount()));
                        totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);

                        OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
                        BigDecimal oldSkuPrice= BigDecimalUtil.div(orderMaterialDO.getCreditDepositAmount(),new BigDecimal(orderMaterialDO.getMaterialCount()),4);
                        BigDecimal oldOrderMaterialCreditDepositAmount = BigDecimalUtil.mul(oldSkuPrice, new BigDecimal(replaceOrderMaterialDO.getMaterialCount()));
                        totalCreditDepositAmount = BigDecimalUtil.sub(totalCreditDepositAmount,oldOrderMaterialCreditDepositAmount);
                        totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                    }
                }
                replaceOrderMaterialDO.setMaterialId(material.getMaterialId());
                replaceOrderMaterialDO.setMaterialName(material.getMaterialName());
                replaceOrderMaterialDO.setRentDepositAmount(rentDepositAmount);
                replaceOrderMaterialDO.setDepositAmount(depositAmount);
                replaceOrderMaterialDO.setCreditDepositAmount(creditDepositAmount);
            }
        }
    }

    @Autowired
    private ReplaceOrderMapper replaceOrderMapper;
    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;
    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;
    @Autowired
    private ProductSupport productSupport;
    @Autowired
    private CustomerOrderSupport customerOrderSupport;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private ChangeOrderProductEquipmentMapper changeOrderProductEquipmentMapper;
    @Autowired
    private ChangeOrderMaterialBulkMapper changeOrderMaterialBulkMapper;
    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BulkMaterialService bulkMaterialService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private StatementService statementService;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private PermissionSupport permissionSupport;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseSupport warehouseSupport;
    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;
    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;
    @Autowired
    private OrderMaterialMapper orderMaterialMapper;
    @Autowired
    private CustomerSupport customerSupport;
    @Autowired
    private MaterialTypeMapper materialTypeMapper;
    @Autowired
    private ReletOrderMapper reletOrderMapper;
    @Autowired
    private ReplaceOrderProductMapper replaceOrderProductMapper;
    @Autowired
    private ReplaceOrderMaterialMapper replaceOrderMaterialMapper;

}
