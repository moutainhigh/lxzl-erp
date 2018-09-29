package com.lxzl.erp.core.service.replace.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.K3Config;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.replace.ReplaceOrderCommitParam;
import com.lxzl.erp.common.domain.replace.ReplaceOrderConfirmChangeParam;
import com.lxzl.erp.common.domain.replace.ReplaceOrderQueryParam;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.common.util.http.client.HttpHeaderBuilder;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSEOrderOelet;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.customer.order.CustomerOrderSupport;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.erp.core.service.k3.PostK3ServiceManager;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.replace.ReplaceOrderService;
import com.lxzl.erp.core.service.replace.support.ReplaceOrderSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementReplaceOrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMaterialBulkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
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
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpSession;
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

        replaceOrderDO.setCustomerName(customerDO.getCustomerName());

        //校验订单编号
        OrderDO orderDO = orderMapper.findByOrderNo(replaceOrderDO.getOrderNo());
        if (orderDO == null) {
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        //只有确认收货和部分归还状态的才可以换货
        if (!OrderStatus.ORDER_STATUS_CONFIRM.equals(orderDO.getOrderStatus()) &&
                !OrderStatus.ORDER_STATUS_PART_RETURN.equals(orderDO.getOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        replaceOrderDO.setDeliverySubCompanyId(orderDO.getDeliverySubCompanyId());
        replaceOrderDO.setDeliverySubCompanyName(orderDO.getDeliverySubCompanyName());
        //校验换货时间
        Date replaceTime = replaceOrder.getReplaceTime();
        Date rentStartTime = orderDO.getRentStartTime();
        Date expectReturnTime = orderDO.getExpectReturnTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String replaceTimeString = simpleDateFormat.format(replaceTime);
        String rentStartTimeString = simpleDateFormat.format(rentStartTime);
        String expectReturnTimeString = simpleDateFormat.format(expectReturnTime);
        //校验换货时间
        if (checkReplaceTime(serviceResult, simpleDateFormat, replaceTimeString, rentStartTimeString, expectReturnTimeString)) {
            return serviceResult;
        }
        //校验换货时间不能再当月之前，不能超过当前时间15天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String monthTimeString = sdf.format(date);
        String nowTimeString = simpleDateFormat.format(date);
        if (checkReplaceTimeForNowDay(serviceResult, replaceTime, simpleDateFormat, replaceTimeString, sdf, monthTimeString, nowTimeString)){
            return serviceResult;
        }

        //校验是否在续租单开始之前换货
        ReletOrderDO exReletOrderDO = reletOrderMapper.findRecentlyReletOrderByOrderNo(orderDO.getOrderNo());
        if (exReletOrderDO != null) {
            Date reletTime = exReletOrderDO.getRentStartTime();
            String reletTimeString = simpleDateFormat.format(reletTime);
            //校验是否在续租单开始之前换货
            if (checkReplaceTiamAndReletTime(serviceResult, simpleDateFormat, replaceTimeString, reletTimeString)){
                return serviceResult;
            }
        }

        //查出该订单的当期续租单
        Map<Integer,ReletOrderProductDO> reletOrderProductDOMap = new HashMap<>();
        Map<Integer,ReletOrderMaterialDO> reletOrderMaterialDOMap = new HashMap<>();
        //查出当前续租期
        List<ReletOrderDO> reletOrderDOList = reletOrderMapper.findReletedOrdersByOrderId(orderDO.getId());
        List<ReletOrderDO> list = new ArrayList<>();
        replaceOrderDO.setIsReletOrderReplace(CommonConstant.COMMON_CONSTANT_NO);
        replaceOrderDO.setReletOrderNo(null);
        for(ReletOrderDO reletOrderDO:reletOrderDOList) {
            if (com.lxzl.erp.common.util.DateUtil.daysBetween(reletOrderDO.getExpectReturnTime(),replaceTime) < 0) {
                list.add(reletOrderDO);
            }
            if (com.lxzl.erp.common.util.DateUtil.daysBetween(reletOrderDO.getRentStartTime(),replaceTime) >= 0 &&
                    com.lxzl.erp.common.util.DateUtil.daysBetween(reletOrderDO.getExpectReturnTime(),replaceTime) < 0 ) {
                //装配该订单续租单map集合
                assemblyReletOrder(reletOrderProductDOMap, reletOrderMaterialDOMap, reletOrderDO);
                replaceOrderDO.setIsReletOrderReplace(CommonConstant.COMMON_CONSTANT_YES);
                replaceOrderDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
            }
        }
        if (list.size()>1) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_BEFORE_RELET_TIME);
            return serviceResult;
        }

        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();

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

        //获取客户风控信息
        CustomerRiskManagementDO customerRiskManagementDO = customerSupport.getCustomerRiskManagementDO(orderDO.getBuyerCustomerId());
        // 原订单设备信用押金总额
        BigDecimal oldTotalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        // 换货后订单设备信用押金总额
        BigDecimal newTotalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();

        // 校验客户风控信息
        verifyCustomerRiskInfo(replaceOrderDO,customerDO,orderDO,customerRiskManagementDO);
        //补全换货商品项信息
        newTotalCreditDepositAmount = calculateOrderProductInfo(replaceOrderProductDOList,orderProductDOMap, orderDO,newTotalCreditDepositAmount,customerRiskManagementDO);
        //补全换货配件项信息
//        newTotalCreditDepositAmount = calculateOrderMaterialInfo(replaceOrderMaterialDOList,orderMaterialDOMap, orderDO,newTotalCreditDepositAmount,customerRiskManagementDO);

        //校验换货项（新的不能换，普通和苹果不能互换）
        if (checkProductIsNewAndIsApple(serviceResult, replaceOrderProductDOList)){
            return serviceResult;
        }

        if (customerRiskManagementDO == null && BigDecimalUtil.compare(newTotalCreditDepositAmount, BigDecimal.ZERO) > 0) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_NEED_RISK_INFO);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(newTotalCreditDepositAmount, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), newTotalCreditDepositAmount), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_AMOUNT_OVER_FLOW);
            return serviceResult;
        }
        replaceOrderDO.setOldTotalCreditDepositAmount(oldTotalCreditDepositAmount);
        replaceOrderDO.setNewTotalCreditDepositAmount(newTotalCreditDepositAmount);
        replaceOrderDO.setUpdateTotalCreditDepositAmount(BigDecimalUtil.sub(oldTotalCreditDepositAmount,newTotalCreditDepositAmount));
        //校验换货数量

        Integer totalReplaceProductCount = 0;
        Integer totalReplaceMaterialCount = 0;
        for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
            totalReplaceProductCount += replaceOrderProductDO.getReplaceProductCount();
        }
        for (ReplaceOrderMaterialDO replaceOrderMaterialDO : replaceOrderMaterialDOList) {
            totalReplaceMaterialCount += replaceOrderMaterialDO.getReplaceMaterialCount();
        }
        //获取换货数量
        Map<Integer, Integer> replaceProductCountMap = new HashMap<>();
        Map<Integer, Integer> replaceMaterialCountMap = new HashMap<>();
        saveRepalceCountInMap(replaceOrderProductDOList, replaceOrderMaterialDOList, replaceProductCountMap, replaceMaterialCountMap);
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
        saveExistedReplaceCount( productCountMap, materialCountMap, replaceOrderDOList,replaceOrder.getReplaceOrderNo());
        //比较设备项
        if (compareProductCount(serviceResult, orderProductDOList, orderProductDOMap, replaceProductCountMap, rentingProductCountMap, productCountMap)){
            return serviceResult;
        }
        //比较物料项
        if (compareMaterialCount(serviceResult, orderMaterialDOList, orderMaterialDOMap, replaceMaterialCountMap, rentingMaterialCountMap, materialCountMap)){
            return serviceResult;
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
        replaceOrderDO.setReplaceDeliveryTime(null);
        replaceOrderDO.setRealReplaceTime(null);
        replaceOrderDO.setOrderRentStartTime(orderDO.getRentStartTime());
        replaceOrderDO.setOrderExpectReturnTime(orderDO.getExpectReturnTime());
        replaceOrderMapper.save(replaceOrderDO);
        //保存换货商品项
        List<ReplaceOrderProductDO> saveReplaceOrderProductDOList = new ArrayList<>();
        for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
            OrderProductDO orderProductDO = orderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
            replaceOrderProductDO.setOldProductUnitAmount(orderProductDO.getProductUnitAmount());
            replaceOrderProductDO.setReplaceOrderId(replaceOrderDO.getId());
            replaceOrderProductDO.setReplaceOrderNo(replaceOrderDO.getReplaceOrderNo());
            replaceOrderProductDO.setNewOrderProductId(null);
            replaceOrderProductDO.setCreateUser(userSupport.getCurrentUserId().toString());
            replaceOrderProductDO.setCreateTime(date);
            replaceOrderProductDO.setUpdateTime(date);
            replaceOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            replaceOrderProductDO.setRealReplaceProductCount(CommonConstant.COMMON_ZERO);
            replaceOrderProductDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
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
            replaceOrderMaterialDO.setOldMaterialUnitAmount(orderMaterialDO.getMaterialUnitAmount());
            replaceOrderMaterialDO.setReplaceOrderId(replaceOrderDO.getId());
            replaceOrderMaterialDO.setReplaceOrderNo(replaceOrderDO.getReplaceOrderNo());
            replaceOrderMaterialDO.setNewOrderMaterialId(null);
            replaceOrderMaterialDO.setCreateUser(userSupport.getCurrentUserId().toString());
            replaceOrderMaterialDO.setCreateTime(date);
            replaceOrderMaterialDO.setUpdateTime(date);
            replaceOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            replaceOrderMaterialDO.setRealReplaceMaterialCount(CommonConstant.COMMON_ZERO);
            replaceOrderMaterialDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
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
     * 校验换货项（新的不能换，普通和苹果不能互换）
     * @param serviceResult
     * @param replaceOrderProductDOList
     * @return
     */
    private boolean checkProductIsNewAndIsApple(ServiceResult<String, String> serviceResult, List<ReplaceOrderProductDO> replaceOrderProductDOList) {
        if (CollectionUtil.isNotEmpty(replaceOrderProductDOList)) {
            for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
                Integer oldIsNewProduct = replaceOrderProductDO.getOldIsNewProduct();
                Integer isNewProduct = replaceOrderProductDO.getIsNewProduct();
                //换货商品或被换商品新旧属性不能为空
                if(oldIsNewProduct == null || isNewProduct == null){
                    serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_IS_NEW_PRODUCT_NOT_NULL);
                    return true;
                }
                //全新商不能换货或被换，只能选择次新商品进行换货操作
                if (CommonConstant.COMMON_CONSTANT_YES.equals(oldIsNewProduct) ||
                        CommonConstant.COMMON_CONSTANT_YES.equals(isNewProduct)) {
                    serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_PRODUCT_NOT_NEW);
                    return true;
                }
                //判断原商品是否是苹果机
                ServiceResult<String, Product> oldProductServiceResult = productService.queryProductBySkuId(replaceOrderProductDO.getOldProductSkuId());
                if (!oldProductServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return true;
                }
                Product oldProduct = oldProductServiceResult.getResult();
                boolean oldProductIsApple = BrandId.BRAND_ID_APPLE.equals(oldProduct.getBrandId());

                //判断新商品是否是苹果机
                ServiceResult<String, Product> newProductServiceResult = productService.queryProductBySkuId(replaceOrderProductDO.getProductSkuId());
                if (!newProductServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                    serviceResult.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return true;
                }
                Product newProduct = newProductServiceResult.getResult();
                boolean newProductIsApple = BrandId.BRAND_ID_APPLE.equals(newProduct.getBrandId());
                //苹果商品只能更换苹果商品
                if (oldProductIsApple && !newProductIsApple) {
                    serviceResult.setErrorCode(ErrorCode.APPLE_NOT_REPLACE_OTHER);
                    return true;
                } else if (!oldProductIsApple && newProductIsApple) {
                    //非苹果商品不能更换苹果商品
                    serviceResult.setErrorCode(ErrorCode.OTHER_NOT_REPLACE_APPLE);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 修改换货单
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> update(ReplaceOrder replaceOrder) {
        ServiceResult<String, String> serviceResult = new ServiceResult();
        Date date = new Date();
        //校验客户编号
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(replaceOrder.getCustomerNo());
        if (customerDO == null) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return serviceResult;
        }
        ReplaceOrderDO dbreplaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(replaceOrder.getReplaceOrderNo());
        if (dbreplaceOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_ERROR);
            return serviceResult;
        }
        if (!userSupport.getCurrentUserId().toString().equals(dbreplaceOrderDO.getCreateUser())) {
            serviceResult.setErrorCode(ErrorCode.UPDATE_REPLACE_ORDER_BY_CREATE_USER);
            return serviceResult;
        }
        if (!ReplaceOrderStatus.REPLACE_ORDER_STATUS_WAIT_COMMIT.equals(dbreplaceOrderDO.getReplaceOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.UPDATE_REPLACE_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        //先作废原换货单项
        List<ReplaceOrderProductDO> dbreplaceOrderProductDOList = dbreplaceOrderDO.getReplaceOrderProductDOList();
        if (CollectionUtil.isNotEmpty(dbreplaceOrderProductDOList)) {
            for (ReplaceOrderProductDO replaceOrderProductDO:dbreplaceOrderProductDOList) {
                replaceOrderProductDO.setDataStatus(CommonConstant.COMMON_TWO);
                replaceOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                replaceOrderProductDO.setUpdateTime(date);
            }
        }
        List<ReplaceOrderMaterialDO> dbreplaceOrderMaterialDOList = dbreplaceOrderDO.getReplaceOrderMaterialDOList();
        if (CollectionUtil.isNotEmpty(dbreplaceOrderMaterialDOList)) {
            for (ReplaceOrderMaterialDO replaceOrderMaterialDO:dbreplaceOrderMaterialDOList) {
                replaceOrderMaterialDO.setDataStatus(CommonConstant.COMMON_TWO);
                replaceOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                replaceOrderMaterialDO.setUpdateTime(date);
            }
        }


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

        //只有确认收货和部分归还状态的才可以换货
        if (!OrderStatus.ORDER_STATUS_CONFIRM.equals(orderDO.getOrderStatus()) &&
                !OrderStatus.ORDER_STATUS_PART_RETURN.equals(orderDO.getOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        //存放原订单商品项和配件项
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();
        Map<Integer,OrderProductDO> orderProductDOMap =  ListUtil.listToMap(orderProductDOList, "id");
        Map<Integer,OrderMaterialDO> orderMaterialDOMap =  ListUtil.listToMap(orderMaterialDOList, "id");

        //校验换货时间
        Date replaceTime = replaceOrder.getReplaceTime();
        Date rentStartTime = orderDO.getRentStartTime();
        Date expectReturnTime = orderDO.getExpectReturnTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String replaceTimeString = simpleDateFormat.format(replaceTime);
        String rentStartTimeString = simpleDateFormat.format(rentStartTime);
        String expectReturnTimeString = simpleDateFormat.format(expectReturnTime);
        //校验换货时间和起租时间和退货时间
        if (checkReplaceTime(serviceResult, simpleDateFormat, replaceTimeString, rentStartTimeString, expectReturnTimeString)){
            return serviceResult;
        }
        //校验换货时间不能再当月之前，不能超过当前时间15天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String monthTimeString = sdf.format(date);
        String nowTimeString = simpleDateFormat.format(date);
        if (checkReplaceTimeForNowDay(serviceResult, replaceTime, simpleDateFormat, replaceTimeString, sdf, monthTimeString, nowTimeString)){
            return serviceResult;
        }



        //校验是否在续租单开始之前换货
        ReletOrderDO exReletOrderDO = reletOrderMapper.findRecentlyReletOrderByOrderNo(orderDO.getOrderNo());
        if (exReletOrderDO != null) {
            Date reletTime = exReletOrderDO.getRentStartTime();
            String reletTimeString = simpleDateFormat.format(reletTime);
            if (checkReplaceTiamAndReletTime(serviceResult, simpleDateFormat, replaceTimeString, reletTimeString)){
                return serviceResult;
            }
        }
        
        //查出该订单的当期续租单
        Map<Integer,ReletOrderProductDO> reletOrderProductDOMap = new HashMap<>();
        Map<Integer,ReletOrderMaterialDO> reletOrderMaterialDOMap = new HashMap<>();
        //查出当前续租期
        List<ReletOrderDO> reletOrderDOList = reletOrderMapper.findReletedOrdersByOrderId(orderDO.getId());
        List<ReletOrderDO> list = new ArrayList<>();
        replaceOrderDO.setIsReletOrderReplace(CommonConstant.COMMON_CONSTANT_NO);
        replaceOrderDO.setReletOrderNo(null);
        for(ReletOrderDO reletOrderDO:reletOrderDOList) {
            if (com.lxzl.erp.common.util.DateUtil.daysBetween(reletOrderDO.getExpectReturnTime(),replaceTime) < 0) {
                list.add(reletOrderDO);
            }
            if (com.lxzl.erp.common.util.DateUtil.daysBetween(reletOrderDO.getRentStartTime(),replaceTime) >= 0 &&
                    com.lxzl.erp.common.util.DateUtil.daysBetween(reletOrderDO.getExpectReturnTime(),replaceTime) < 0 ) {
                //装配该订单续租单map集合
                assemblyReletOrder(reletOrderProductDOMap, reletOrderMaterialDOMap, reletOrderDO);
                replaceOrderDO.setIsReletOrderReplace(CommonConstant.COMMON_CONSTANT_YES);
                replaceOrderDO.setReletOrderNo(reletOrderDO.getReletOrderNo());
            }
        }
        if (list.size()>1) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_BEFORE_RELET_TIME);
            return serviceResult;
        }

        //获取客户风控信息
        CustomerRiskManagementDO customerRiskManagementDO = customerSupport.getCustomerRiskManagementDO(orderDO.getBuyerCustomerId());
        // 原订单设备信用押金总额
        BigDecimal oldTotalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        // 换货后订单设备信用押金总额
        BigDecimal newTotalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();

        // 校验客户风控信息
        verifyCustomerRiskInfo(replaceOrderDO,customerDO,orderDO,customerRiskManagementDO);
        //补全换货商品项信息
        newTotalCreditDepositAmount = calculateOrderProductInfo(replaceOrderProductDOList,orderProductDOMap, orderDO,newTotalCreditDepositAmount,customerRiskManagementDO);
        //补全换货配件项信息
        newTotalCreditDepositAmount = calculateOrderMaterialInfo(replaceOrderMaterialDOList,orderMaterialDOMap, orderDO,newTotalCreditDepositAmount,customerRiskManagementDO);

        //校验换货项（新的不能换，普通和苹果不能互换）
        if (checkProductIsNewAndIsApple(serviceResult, replaceOrderProductDOList)){
            return serviceResult;
        }
        
        if (customerRiskManagementDO == null && BigDecimalUtil.compare(newTotalCreditDepositAmount, BigDecimal.ZERO) > 0) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_NEED_RISK_INFO);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(newTotalCreditDepositAmount, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), newTotalCreditDepositAmount), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_AMOUNT_OVER_FLOW);
            return serviceResult;
        }
        replaceOrderDO.setOldTotalCreditDepositAmount(oldTotalCreditDepositAmount);
        replaceOrderDO.setNewTotalCreditDepositAmount(newTotalCreditDepositAmount);
        replaceOrderDO.setUpdateTotalCreditDepositAmount(BigDecimalUtil.sub(oldTotalCreditDepositAmount,newTotalCreditDepositAmount));

        //校验换货数量
        //获取总换货商品数和总配件数
        Integer totalReplaceProductCount = 0;
        Integer totalReplaceMaterialCount = 0;
        for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
            totalReplaceProductCount += replaceOrderProductDO.getReplaceProductCount();
        }
        for (ReplaceOrderMaterialDO replaceOrderMaterialDO : replaceOrderMaterialDOList) {
            totalReplaceMaterialCount += replaceOrderMaterialDO.getReplaceMaterialCount();
        }
        //获取换货数量并保存到map
        Map<Integer, Integer> replaceProductCountMap = new HashMap<>();
        Map<Integer, Integer> replaceMaterialCountMap = new HashMap<>();
        saveRepalceCountInMap(replaceOrderProductDOList, replaceOrderMaterialDOList, replaceProductCountMap, replaceMaterialCountMap);
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
        saveExistedReplaceCount(productCountMap, materialCountMap, replaceOrderDOList,replaceOrder.getReplaceOrderNo());

        //比较设备项
        if (compareProductCount(serviceResult, orderProductDOList, orderProductDOMap, replaceProductCountMap, rentingProductCountMap, productCountMap)){
            return serviceResult;
        }

        //比较物料项
        if (compareMaterialCount(serviceResult, orderMaterialDOList, orderMaterialDOMap, replaceMaterialCountMap, rentingMaterialCountMap, materialCountMap)){
            return serviceResult;
        }

        //保存换货单信息
        replaceOrderDO.setId(dbreplaceOrderDO.getId());
        replaceOrderDO.setCreateUser(dbreplaceOrderDO.getCreateUser());
        replaceOrderDO.setCreateTime(dbreplaceOrderDO.getCreateTime());
        replaceOrderDO.setTotalReplaceProductCount(totalReplaceProductCount);
        replaceOrderDO.setTotalReplaceMaterialCount(totalReplaceMaterialCount);
        replaceOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        replaceOrderDO.setUpdateTime(date);
        replaceOrderDO.setOrderRentStartTime(orderDO.getRentStartTime());
        replaceOrderDO.setOrderExpectReturnTime(orderDO.getExpectReturnTime());
        replaceOrderMapper.update(replaceOrderDO);
        //保存换货商品项
        List<ReplaceOrderProductDO> saveReplaceOrderProductDOList = new ArrayList<>();
        for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
            replaceOrderProductDO.setId(null);
            OrderProductDO orderProductDO = orderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
            replaceOrderProductDO.setOldProductEntry(orderProductDO.getFEntryID());
            replaceOrderProductDO.setOldProductUnitAmount(orderProductDO.getProductUnitAmount());
            replaceOrderProductDO.setReplaceOrderId(replaceOrderDO.getId());
            replaceOrderProductDO.setReplaceOrderNo(replaceOrderDO.getReplaceOrderNo());
            replaceOrderProductDO.setNewOrderProductId(null);
            replaceOrderProductDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
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
            replaceOrderMaterialDO.setId(null);
            OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
            replaceOrderMaterialDO.setOldMaterialEntry(orderMaterialDO.getFEntryID());
            replaceOrderMaterialDO.setOldMaterialUnitAmount(orderMaterialDO.getMaterialUnitAmount());
            replaceOrderMaterialDO.setReplaceOrderId(replaceOrderDO.getId());
            replaceOrderMaterialDO.setReplaceOrderNo(replaceOrderDO.getReplaceOrderNo());
            replaceOrderMaterialDO.setNewOrderMaterialId(null);
            replaceOrderMaterialDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
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
        if (CollectionUtil.isNotEmpty(dbreplaceOrderProductDOList)) {
            replaceOrderProductMapper.updateListForCancel(dbreplaceOrderProductDOList);
        }
        if (CollectionUtil.isNotEmpty(dbreplaceOrderMaterialDOList)) {
            replaceOrderMaterialMapper.updateListForCancel(dbreplaceOrderMaterialDOList);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
    /**
     * 校验换货时间不能再当月之前，不能超过当前时间15天
     */
    private boolean checkReplaceTimeForNowDay(ServiceResult<String, String> serviceResult, Date replaceTime, SimpleDateFormat simpleDateFormat, String replaceTimeString, SimpleDateFormat sdf, String monthTimeString, String nowTimeString) {
        try {
            Date nowTime = simpleDateFormat.parse(nowTimeString);
            Date monthTime = sdf.parse(monthTimeString);
            Date replaceTimeDate = simpleDateFormat.parse(replaceTimeString);
            if (replaceTimeDate.before(nowTime) && replaceTimeDate.before(monthTime)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_COUNT_NOT_BEFORE_MONTH_TIME);
                return true;
            }
            Integer x = com.lxzl.erp.common.util.DateUtil.daysBetween(nowTime,replaceTime);
            if(replaceTimeDate.after(nowTime) && com.lxzl.erp.common.util.DateUtil.daysBetween(nowTime,replaceTime)>15){
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_MORE_THAN_NOW_TIME_FIFTEEN);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("【创建换货单,校验换货时间parse出错】", e);
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_PARSE_ERROR);
            return true;
        }
        return false;
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
        if (replaceOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_ERROR);
            return serviceResult;
        }
        if (!userSupport.getCurrentUserId().toString().equals(replaceOrderDO.getCreateUser())) {
            serviceResult.setErrorCode(ErrorCode.CANCEL_REPLACE_ORDER_BY_CREATE_USER);
            return serviceResult;
        }
        if (!ReplaceOrderStatus.REPLACE_ORDER_STATUS_WAIT_COMMIT.equals(replaceOrderDO.getReplaceOrderStatus()) &&
                !ReplaceOrderStatus.REPLACE_ORDER_STATUS_VERIFYING.equals(replaceOrderDO.getReplaceOrderStatus()) &&
                !ReplaceOrderStatus.REPLACE_ORDER_STATUS_BACKED.equals(replaceOrderDO.getReplaceOrderStatus())) {
            serviceResult.setErrorCode(ErrorCode.CANCEL_REPLACE_ORDER_STATUS_ERROR);
            return serviceResult;
        }
        //判断状态审核中执行工作流取消审核
        if (ReplaceOrderStatus.REPLACE_ORDER_STATUS_VERIFYING.equals(replaceOrderDO.getReplaceOrderStatus())) {
            ServiceResult<String, String> cancelWorkFlowResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_CHANGE, replaceOrderDO.getReplaceOrderNo());
            if (!ErrorCode.SUCCESS.equals(cancelWorkFlowResult.getErrorCode())) {
                serviceResult.setErrorCode(cancelWorkFlowResult.getErrorCode());
                return serviceResult;
            }
            // 审核中或者的换货单取消要返还扣走或者添加的信用额度
            BigDecimal updateTotalCreditDepositAmount = replaceOrderDO.getUpdateTotalCreditDepositAmount();
            if (BigDecimalUtil.compare(updateTotalCreditDepositAmount, BigDecimal.ZERO) != 0) {
                if (BigDecimalUtil.compare(updateTotalCreditDepositAmount, BigDecimal.ZERO) > 0) {
                    customerSupport.addCreditAmountUsed(replaceOrderDO.getCustomerId(), updateTotalCreditDepositAmount, CustomerRiskBusinessType.CANCEL_REPLACE_ORDER_TYPE, replaceOrderDO.getReplaceOrderNo(), null);
                } else if (BigDecimalUtil.compare(updateTotalCreditDepositAmount, BigDecimal.ZERO) < 0) {
                    customerSupport.subCreditAmountUsed(replaceOrderDO.getCustomerId(), BigDecimalUtil.mul(updateTotalCreditDepositAmount, new BigDecimal(-1)), CustomerRiskBusinessType.CANCEL_REPLACE_ORDER_TYPE, replaceOrderDO.getReplaceOrderNo(), null);
                }
            }
        }

        replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_CANCEL);
        replaceOrderDO.setUpdateTime(date);
        replaceOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        replaceOrderMapper.update(replaceOrderDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
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
        //权限
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER,PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_WAREHOUSE));
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> confirmReplaceOrder(ReplaceOrderConfirmChangeParam replaceOrderConfirmChangeParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date date = new Date();
        if (null == replaceOrderConfirmChangeParam.getReplaceOrder()) {
            result.setErrorCode(ErrorCode.CONFIRM_REPLACE_ORDER_NOT_NULL);
            return result;
        }
        ReplaceOrder replaceOrder = replaceOrderConfirmChangeParam.getReplaceOrder();
        ReplaceOrderDO dbReplaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(replaceOrder.getReplaceOrderNo());
        if (dbReplaceOrderDO == null) {
            result.setErrorCode(ErrorCode.REPLACE_ORDER_NO_ERROR);
            return result;
        }
        Date realReplaceTime = replaceOrder.getRealReplaceTime();
        if (realReplaceTime == null) {
            result.setErrorCode(ErrorCode.REAL_REPLACE_TIME_NOT_NULL);
            return result;
        }
        //校验客户编号
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(replaceOrder.getCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        //校验订单编号
        OrderDO orderDO = orderMapper.findByOrderNo(replaceOrder.getOrderNo());
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();

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

        //校验实际换货时间和起租时间和退货时间
        Date rentStartTime = orderDO.getRentStartTime();
        Date expectReturnTime = orderDO.getExpectReturnTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String realReplaceTimeString = simpleDateFormat.format(realReplaceTime);
        String rentStartTimeString = simpleDateFormat.format(rentStartTime);
        String expectReturnTimeString = simpleDateFormat.format(expectReturnTime);
        if (checkReplaceTime(result, simpleDateFormat, realReplaceTimeString, rentStartTimeString, expectReturnTimeString)){
            return result;
        }

        Integer replaceOrderStatus = dbReplaceOrderDO.getReplaceOrderStatus();
        if (replaceOrderStatus == null || !ReplaceOrderStatus.REPLACE_ORDER_STATUS_DELIVERED.equals(replaceOrderStatus)) {
            result.setErrorCode(ErrorCode.CONFIRM_REPLACE_ORDER_REPLACE_ORDER_STATUS_ERROR);
            return result;
        }

//        //校验实际换货时间和预计换货时间与当前时间
//        Date replaceTime = replaceOrder.getReplaceTime();
//        String replaceTimeString = simpleDateFormat.format(replaceTime);
//        String nowTimeString = simpleDateFormat.format(date);
//        if (checkRealReplaceTime(result, simpleDateFormat, realReplaceTimeString, replaceTimeString, nowTimeString)){
//            return result;
//        }

        ReplaceOrderDO replaceOrderDO = ConverterUtil.convert(replaceOrder,ReplaceOrderDO.class);

        List<ReplaceOrderProductDO> replaceOrderProductDOList = replaceOrderDO.getReplaceOrderProductDOList();
        List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList = replaceOrderDO.getReplaceOrderMaterialDOList();
        boolean replaceOrderProductDOListIsNotEmpty = CollectionUtil.isNotEmpty(replaceOrderProductDOList);
        boolean replaceOrderMaterialDOListIsNotEmpty = CollectionUtil.isNotEmpty(replaceOrderMaterialDOList);
        if (!replaceOrderProductDOListIsNotEmpty && !replaceOrderMaterialDOListIsNotEmpty) {
            result.setErrorCode(ErrorCode.REPLACE_ORDER_DETAIL_LIST_NOT_NULL);
            return result;
        }

        //获取客户风控信息
        CustomerRiskManagementDO customerRiskManagementDO = customerSupport.getCustomerRiskManagementDO(orderDO.getBuyerCustomerId());
        // 原订单设备信用押金总额
        BigDecimal oldTotalCreditDepositAmount = replaceOrderDO.getOldTotalCreditDepositAmount();
        // 换货后订单设备信用押金总额
        BigDecimal newTotalCreditDepositAmount = replaceOrderDO.getOldTotalCreditDepositAmount();

        Integer totalReplaceProductCount = replaceOrderDO.getTotalReplaceProductCount();
        Integer totalReplaceMaterialCount = replaceOrderDO.getTotalReplaceMaterialCount();
        Integer realTotalReplaceProductCount = 0;
        Integer realTotalReplaceMaterialCount = 0;

        if (replaceOrderProductDOListIsNotEmpty) {
            for (ReplaceOrderProductDO replaceOrderProductDO:replaceOrderProductDOList) {
                realTotalReplaceProductCount += replaceOrderProductDO.getRealReplaceProductCount();
                //确认换货数量不能为负
                if (replaceOrderProductDO.getRealReplaceProductCount()<0) {
                    result.setErrorCode(ErrorCode.REAL_REPLACE_PRODUCT_COUNT_NOT_NEGATIVE);
                    return result;
                }
                //确认换货数量大于换货单下单数量
                if (replaceOrderProductDO.getRealReplaceProductCount() > replaceOrderProductDO.getReplaceProductCount()) {
                    result.setErrorCode(ErrorCode.REAL_REPLACE_PRODUCT_COUNT_MORE_THAN_REPLACE_PRODUCT_COUNT);
                    return result;
                }
                replaceOrderProductDO.setUpdateTime(date);
                replaceOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            }
            newTotalCreditDepositAmount = changeReplaceOrderProductInfo(replaceOrderProductDOList,orderProductDOMap, orderDO,newTotalCreditDepositAmount,customerRiskManagementDO);
        }

//        if (replaceOrderMaterialDOListIsNotEmpty) {
//            for (ReplaceOrderMaterialDO replaceOrderMaterialDO:replaceOrderMaterialDOList) {
//                realTotalReplaceProductCount += replaceOrderMaterialDO.getRealReplaceMaterialCount();
//                //确认换货数量不能为负
//                if (replaceOrderMaterialDO.getRealReplaceMaterialCount()<0) {
//                    result.setErrorCode(ErrorCode.REAL_REPLACE_PRODUCT_COUNT_NOT_NEGATIVE);
//                    return result;
//                }
//                //确认换货数量大于换货单下单数量
//                if (replaceOrderMaterialDO.getRealReplaceMaterialCount() > replaceOrderMaterialDO.getReplaceMaterialCount()){
//                    result.setErrorCode(ErrorCode.REAL_REPLACE_PRODUCT_COUNT_MORE_THAN_REPLACE_PRODUCT_COUNT);
//                    return result;
//                }
//                replaceOrderMaterialDO.setUpdateTime(date);
//                replaceOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
//            }
//            newTotalCreditDepositAmount = changeReplaceOrderMaterialInfo(replaceOrderMaterialDOList,orderMaterialDOMap, orderDO,newTotalCreditDepositAmount,customerRiskManagementDO);
//        }

        BigDecimal totalCreditDepositAmount = BigDecimalUtil.sub(oldTotalCreditDepositAmount,newTotalCreditDepositAmount);
        BigDecimal updateTotalCreditDepositAmount = replaceOrderDO.getUpdateTotalCreditDepositAmount();
        BigDecimal changeTotalCreditDepositAmount = BigDecimalUtil.sub(updateTotalCreditDepositAmount,totalCreditDepositAmount);
        if (BigDecimalUtil.compare(changeTotalCreditDepositAmount, BigDecimal.ZERO) != 0) {
            replaceOrderDO.setNewTotalCreditDepositAmount(newTotalCreditDepositAmount);
            replaceOrderDO.setUpdateTotalCreditDepositAmount(totalCreditDepositAmount);
            //未全部收货变更授信额度
            if (BigDecimalUtil.compare(changeTotalCreditDepositAmount, BigDecimal.ZERO) > 0) {
                customerSupport.addCreditAmountUsed(replaceOrderDO.getCustomerId(), changeTotalCreditDepositAmount, CustomerRiskBusinessType.CONFIRM_REPLACE_ORDER_TYPE, replaceOrderDO.getReplaceOrderNo(), null);
            } else if (BigDecimalUtil.compare(updateTotalCreditDepositAmount, BigDecimal.ZERO) < 0) {
                customerSupport.subCreditAmountUsed(replaceOrderDO.getCustomerId(), BigDecimalUtil.mul(changeTotalCreditDepositAmount, new BigDecimal(-1)), CustomerRiskBusinessType.CONFIRM_REPLACE_ORDER_TYPE, replaceOrderDO.getReplaceOrderNo(), null);
            }
        }
        //更新订单授信押金金额，商品授信押金金额
        orderDO.setTotalCreditDepositAmount(BigDecimalUtil.sub(orderDO.getTotalCreditDepositAmount(),totalCreditDepositAmount));
        orderDO.setTotalProductCreditDepositAmount(BigDecimalUtil.sub(orderDO.getTotalProductCreditDepositAmount(),totalCreditDepositAmount));
        orderDO.setUpdateTime(date);
        orderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        orderMapper.update(orderDO);
        //保存图片
        if (saveImg(replaceOrderConfirmChangeParam, result, date, replaceOrderDO)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return result;
        }
        replaceOrderDO.setConfirmReplaceTime(date);
        replaceOrderDO.setConfirmReplaceUser(userSupport.getCurrentUserId().toString());
        replaceOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        replaceOrderDO.setUpdateTime(date);
        replaceOrderDO.setRealTotalReplaceProductCount(realTotalReplaceProductCount);
        replaceOrderDO.setRealTotalReplaceMaterialCount(realTotalReplaceMaterialCount);

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        // 有换货，生成订单
        if (0 != (realTotalReplaceProductCount+realTotalReplaceMaterialCount)) {

            replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_CONFIRM);

            if (replaceOrderProductDOListIsNotEmpty) {
                for (ReplaceOrderProductDO replaceOrderProductDO:replaceOrderProductDOList) {
                    OrderProductDO orderProductDO = new OrderProductDO();
                    OrderProductDO oldOrderProductDO = orderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
                    // TODO: 2018\9\18 0018 生成新的订单项
                    orderProductDO.setOrderId(oldOrderProductDO.getOrderId());
                    orderProductDO.setRentType(replaceOrderProductDO.getRentType());
                    orderProductDO.setRentLengthType(replaceOrderProductDO.getRentLengthType());
                    orderProductDO.setProductId(replaceOrderProductDO.getProductId());
                    orderProductDO.setProductName(replaceOrderProductDO.getProductName());
                    orderProductDO.setProductSkuId(replaceOrderProductDO.getProductSkuId());
                    orderProductDO.setProductSkuName(replaceOrderProductDO.getProductSkuName());
                    orderProductDO.setProductCount(replaceOrderProductDO.getRealReplaceProductCount());
                    orderProductDO.setProductUnitAmount(replaceOrderProductDO.getProductUnitAmount());
                    orderProductDO.setRentDepositAmount(replaceOrderProductDO.getRentDepositAmount());
                    orderProductDO.setDepositAmount(replaceOrderProductDO.getDepositAmount());
                    orderProductDO.setCreditDepositAmount(replaceOrderProductDO.getCreditDepositAmount());
                    orderProductDO.setInsuranceAmount(BigDecimal.ZERO);
                    orderProductDO.setDepositCycle(replaceOrderProductDO.getDepositCycle());
                    orderProductDO.setPaymentCycle(replaceOrderProductDO.getPaymentCycle());
                    orderProductDO.setPayMode(replaceOrderProductDO.getPayMode());
                    orderProductDO.setIsNewProduct(replaceOrderProductDO.getIsNewProduct());
                    orderProductDO.setDataStatus(replaceOrderProductDO.getDataStatus());
                    orderProductDO.setRemark(null);
                    orderProductDO.setCreateUser(replaceOrderProductDO.getCreateUser());
                    orderProductDO.setCreateTime(date);
                    orderProductDO.setUpdateUser(replaceOrderProductDO.getCreateUser());
                    orderProductDO.setUpdateTime(date);
                    orderProductDO.setRentingProductCount(replaceOrderProductDO.getRealReplaceProductCount());
                    orderProductDO.setFEntryID(0);
                    orderProductDO.setProductNumber(replaceOrderProductDO.getProductNumber());
                    orderProductDO.setOrderJointProductId(null);
                    orderProductDO.setJointProductProductId(null);
                    orderProductDO.setStableProductCount(replaceOrderProductDO.getRealReplaceProductCount());
                    orderProductDO.setProductSkuSnapshot(replaceOrderProductDO.getNewProductSkuSnapshot());
                    // TODO: 2018\9\18 0018 设置租赁期限
                    // TODO: 2018\9\18 0018 设置商品价格
                    if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                        Integer dayRentTimeLength = com.lxzl.erp.common.util.DateUtil.daysBetween(realReplaceTime,expectReturnTime)+1;
                        orderProductDO.setRentTimeLength(dayRentTimeLength);
                        orderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getRentTimeLength()), 2), new BigDecimal(orderProductDO.getProductCount())));
                    }else {
                        int[] diff = com.lxzl.erp.common.util.DateUtil.getDiff(realReplaceTime,expectReturnTime);
                        if (diff[1]>0) {
                            diff[0] += 1;
                        }
                        if (diff[0] == 0) {
                            diff[0] += 1;
                        }
                        orderProductDO.setRentTimeLength(diff[0]);
                        BigDecimal productAmount = amountSupport.calculateRentAmount(realReplaceTime,expectReturnTime,orderProductDO.getProductUnitAmount(),orderProductDO.getProductCount());
                        orderProductDO.setProductAmount(productAmount);
                    }
                    // TODO: 2018\9\18 0018 修改原订单项
                    oldOrderProductDO.setRentingProductCount(oldOrderProductDO.getRentingProductCount()-orderProductDO.getProductCount());
                    oldOrderProductDO.setUpdateTime(date);
                    oldOrderProductDO.setUpdateUser(replaceOrderDO.getUpdateUser());
                    // TODO: 2018\9\18 0018 保存订单项
                    orderProductMapper.save(orderProductDO);
                    // TODO: 2018\9\18 0018 更新订单项
                    orderProductMapper.update(oldOrderProductDO);
                    replaceOrderProductDO.setNewOrderProductId(orderProductDO.getId());
                    //更新换货单项
                    replaceOrderProductMapper.update(replaceOrderProductDO);
                }
            }
//            if (replaceOrderMaterialDOListIsNotEmpty) {
//                replaceOrderMaterialMapper.updateListForConfirm(replaceOrderMaterialDOList);
//            }
            replaceOrderMapper.update(replaceOrderDO);
            // TODO: 2018\9\19 0019 换货单结算
            String errorCode = statementReplaceOrderSupport.createStatement(replaceOrderDO.getReplaceOrderNo());
            if (!ErrorCode.SUCCESS.equals(errorCode)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                result.setErrorCode(errorCode);
                return result;
            }

            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(replaceOrderDO.getReletOrderNo());

        }else {
            //没有换货，关闭换货单
            replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_CLOSED);
            replaceOrderDO.setUpdateTime(date);
            replaceOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            if (CollectionUtil.isNotEmpty(replaceOrderProductDOList)) {
                for (ReplaceOrderProductDO replaceOrderProductDO:replaceOrderProductDOList) {
                    replaceOrderProductDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    replaceOrderProductDO.setUpdateTime(date);
                    replaceOrderProductMapper.update(replaceOrderProductDO);
                }
            }
//            if (CollectionUtil.isNotEmpty(replaceOrderMaterialDOList)) {
//                for (ReplaceOrderMaterialDO replaceOrderMaterialDO:replaceOrderMaterialDOList) {
//                    replaceOrderMaterialDO.setUpdateUser(userSupport.getCurrentUserId().toString());
//                    replaceOrderMaterialDO.setUpdateTime(date);
//                    replaceOrderMaterialMapper.update(replaceOrderMaterialDO);
//                }
//            }
            replaceOrderMapper.update(replaceOrderDO);
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(replaceOrderDO.getReletOrderNo());
        }
        // TODO: 2018\9\19 0019 发送换货单信息到K3
//        ServiceResult<String, String> k3ServiceResult = sendReplaceOrderInfoToK3(replaceOrderDO.getReplaceOrderNo());
//        if (!ErrorCode.SUCCESS.equals(k3ServiceResult.getErrorCode())) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
//            result.setErrorCode(k3ServiceResult.getErrorCode(), k3ServiceResult.getFormatArgs());
//        }

        return result;
    }
    /**
     *保存图片
     */
    private boolean saveImg(ReplaceOrderConfirmChangeParam replaceOrderConfirmChangeParam, ServiceResult<String, String> result, Date date, ReplaceOrderDO replaceOrderDO) {
        if (replaceOrderConfirmChangeParam.getDeliveryNoteCustomerSignImg() != null) {
            ImageDO deliveryNoteCustomerSignImgDO = imgMysqlMapper.findById(replaceOrderConfirmChangeParam.getDeliveryNoteCustomerSignImg().getImgId());
            if (deliveryNoteCustomerSignImgDO == null) {
                result.setErrorCode(ErrorCode.DELIVERY_NOTE_CUSTOMER_SIGN_IMAGE_NOT_EXISTS);
                return true;
            }
            deliveryNoteCustomerSignImgDO.setImgType(ImgType.DELIVERY_NOTE_CUSTOMER_SIGN);
            deliveryNoteCustomerSignImgDO.setRefId(replaceOrderDO.getId().toString());
            deliveryNoteCustomerSignImgDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            deliveryNoteCustomerSignImgDO.setUpdateTime(date);
            imgMysqlMapper.update(deliveryNoteCustomerSignImgDO);
        }
        return false;
    }

    /**
     * 提交换货单
     * @Author : sunzhipeng
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitReplaceOrder(ReplaceOrderCommitParam replaceOrderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date now = new Date();
        ReplaceOrderDO replaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(replaceOrderCommitParam.getReplaceOrderNo());
        if (replaceOrderDO == null) {
            result.setErrorCode(ErrorCode.REPLACE_ORDER_NO_ERROR);
            return result;
        } else if (!ReplaceOrderStatus.REPLACE_ORDER_STATUS_WAIT_COMMIT.equals(replaceOrderDO.getReplaceOrderStatus())
                && !ReplaceOrderStatus.REPLACE_ORDER_STATUS_BACKED.equals(replaceOrderDO.getReplaceOrderStatus())) {
            //只有待提交和已驳回状态的换货单可以提交
            result.setErrorCode(ErrorCode.REPLACE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN);
            return result;
        }
        if (!replaceOrderDO.getCreateUser().equals(userSupport.getCurrentUserId().toString())) {
            //只有创建换货单本人可以提交
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }
        if (CollectionUtil.isEmpty(replaceOrderDO.getReplaceOrderProductDOList())) {
            result.setErrorCode(ErrorCode.REPLACE_ORDER_DETAIL_COMMITTED_NOT_NULL);
            return result;
        }
        List<ReplaceOrderProductDO> replaceOrderProductDOList = replaceOrderDO.getReplaceOrderProductDOList();
        List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList = replaceOrderDO.getReplaceOrderMaterialDOList();
        //校验订单编号
        OrderDO orderDO = orderMapper.findByOrderNo(replaceOrderDO.getOrderNo());
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        //只有确认收货和部分归还状态的才可以换货
        if (!OrderStatus.ORDER_STATUS_CONFIRM.equals(orderDO.getOrderStatus()) &&
                !OrderStatus.ORDER_STATUS_PART_RETURN.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.REPLACE_ORDER_STATUS_ERROR);
            return result;
        }
        List<OrderProductDO> orderProductDOList = orderDO.getOrderProductDOList();
        List<OrderMaterialDO> orderMaterialDOList = orderDO.getOrderMaterialDOList();

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
        //校验换货数量
        //获取总换货商品数和总配件数
        Integer totalReplaceProductCount = 0;
        Integer totalReplaceMaterialCount = 0;
        for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
            totalReplaceProductCount += replaceOrderProductDO.getReplaceProductCount();
        }
        for (ReplaceOrderMaterialDO replaceOrderMaterialDO : replaceOrderMaterialDOList) {
            totalReplaceMaterialCount += replaceOrderMaterialDO.getReplaceMaterialCount();
        }
        //获取换货数量并保存到map
        Map<Integer, Integer> replaceProductCountMap = new HashMap<>();
        Map<Integer, Integer> replaceMaterialCountMap = new HashMap<>();
        saveRepalceCountInMap(replaceOrderProductDOList, replaceOrderMaterialDOList, replaceProductCountMap, replaceMaterialCountMap);
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
        List<ReplaceOrderDO> replaceOrderDOList = replaceOrderMapper.findByOrderNoForCheck(replaceOrderDO.getOrderNo());
        //将该订单的待提交、审核中两种状态的换货单商品或配件数量保存
        saveExistedReplaceCount(productCountMap, materialCountMap, replaceOrderDOList,replaceOrderDO.getReplaceOrderNo());

        //比较设备项
        if (compareProductCount(result, orderProductDOList, orderProductDOMap, replaceProductCountMap, rentingProductCountMap, productCountMap)){
            return result;
        }
        //比较物料项
        if (compareMaterialCount(result, orderMaterialDOList, orderMaterialDOMap, replaceMaterialCountMap, rentingMaterialCountMap, materialCountMap)){
            return result;
        }
        //校验客户编号
        CustomerDO customerDO = customerMapper.findCustomerPersonByNo(replaceOrderDO.getCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }

        //获取客户风控信息
        CustomerRiskManagementDO customerRiskManagementDO = customerSupport.getCustomerRiskManagementDO(orderDO.getBuyerCustomerId());
        // 设备信用押金总额
        BigDecimal newTotalCreditDepositAmount = replaceOrderDO.getNewTotalCreditDepositAmount();
        // 校验客户风控信息
        verifyCustomerRiskInfo(replaceOrderDO,customerDO,orderDO,customerRiskManagementDO);

        if (customerRiskManagementDO == null && BigDecimalUtil.compare(newTotalCreditDepositAmount, BigDecimal.ZERO) > 0) {
            result.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_NEED_RISK_INFO);
            return result;
        }
        if (BigDecimalUtil.compare(newTotalCreditDepositAmount, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), newTotalCreditDepositAmount), BigDecimal.ZERO) < 0) {
            result.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_AMOUNT_OVER_FLOW);
            return result;
        }
        BigDecimal totalCreditDepositAmount = replaceOrderDO.getUpdateTotalCreditDepositAmount();
        //扣除信用额度
        if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) > 0) {
            customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), totalCreditDepositAmount, CustomerRiskBusinessType.COMMIT_REPLACE_ORDER_TYPE, replaceOrderDO.getReplaceOrderNo(), null);
        } else if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) < 0) {
            customerSupport.addCreditAmountUsed(orderDO.getBuyerCustomerId(), BigDecimalUtil.mul(totalCreditDepositAmount, new BigDecimal(-1)), CustomerRiskBusinessType.COMMIT_REPLACE_ORDER_TYPE, replaceOrderDO.getReplaceOrderNo(), null);
        }

        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_CHANGE);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            result.setErrorCode(needVerifyResult.getErrorCode());
            return result;
        } else if (needVerifyResult.getResult()) {
            if (replaceOrderCommitParam.getVerifyUserId() == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                result.setErrorCode(ErrorCode.VERIFY_USER_NOT_NULL);
                return result;
            }
            //调用提交审核服务
            replaceOrderCommitParam.setVerifyMatters("换货单审核事项：1.服务费和运费 2.退还方式 3.商品与配件的换货数量");
            ServiceResult<String, String> verifyResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_CHANGE, replaceOrderCommitParam.getReplaceOrderNo(), replaceOrderCommitParam.getVerifyUserId(), replaceOrderCommitParam.getVerifyMatters(), replaceOrderCommitParam.getRemark(), replaceOrderCommitParam.getImgIdList(), null);
            //修改提交审核状态
            if (ErrorCode.SUCCESS.equals(verifyResult.getErrorCode())) {
                replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_VERIFYING);
                replaceOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                replaceOrderDO.setUpdateTime(now);
                replaceOrderMapper.update(replaceOrderDO);
                return verifyResult;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                result.setErrorCode(verifyResult.getErrorCode());
                return result;
            }
        } else {
            replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_PROCESSING);
            replaceOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            replaceOrderDO.setUpdateTime(now);
            replaceOrderMapper.update(replaceOrderDO);
            // TODO: 2018\9\15 0015 发送数据到K3
//            result = sendReplaceOrderInfoToK3(replaceOrderDO.getReplaceOrderNo());
//            if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
//                result.setErrorCode(result.getErrorCode(), result.getFormatArgs());
//            }
            return result;
        }
    }

    @Override
    public ServiceResult<String, String> sendReplaceOrderToK3(String replaceOrderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        ReplaceOrderDO replaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(replaceOrderNo);
        if (replaceOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        K3SendRecordDO k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(replaceOrderDO.getId(), PostK3Type.POST_K3_TYPE_REPLACE_ORDER);
        ReplaceOrder replaceOrder = ConverterUtil.convert(replaceOrderDO, ReplaceOrder.class);
        replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_PROCESSING);
        replaceOrderDO.setUpdateTime(currentTime);
        replaceOrderDO.setUpdateUser(loginUser.getUserId().toString());
        replaceOrderMapper.update(replaceOrderDO);
        if (k3SendRecordDO == null) {
            //创建推送记录，此时发送状态失败，接收状态失败
            k3SendRecordDO = new K3SendRecordDO();
            k3SendRecordDO.setRecordType(PostK3Type.POST_K3_TYPE_REPLACE_ORDER);
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_NO);
            k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
            k3SendRecordDO.setRecordJson(JSON.toJSONString(replaceOrder));
            k3SendRecordDO.setSendTime(new Date());
            k3SendRecordDO.setRecordReferId(replaceOrderDO.getId());
            k3SendRecordMapper.save(k3SendRecordDO);
            logger.info("【推送消息】" + JSON.toJSONString(replaceOrder));
        }
        //异步向K3推送退货单
        replaceOrderSupport.sendReplaceOrderToK3Asynchronous(replaceOrder, k3SendRecordDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> sendReplaceOrderInfoToK3(String replaceOrderNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        ReplaceOrderDO replaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(replaceOrderNo);
        if (replaceOrderDO == null){
            serviceResult.setErrorCode(ErrorCode.REPLACE_ORDER_ERROR);
            return serviceResult;
        }
        Map<String, Object> requestData = new HashMap<>();
        Map responseMap = new HashMap();
        String response = null;
        List<ReplaceOrderProductDO> replaceOrderProductDOList = replaceOrderDO.getReplaceOrderProductDOList();
        if (CollectionUtil.isNotEmpty(replaceOrderProductDOList)) {
            for (ReplaceOrderProductDO replaceOrderProductDO:replaceOrderProductDOList){
                replaceOrderProductDO.setOldProductSkuSnapshot(null);
                replaceOrderProductDO.setNewProductSkuSnapshot(null);
            }
        }

        requestData.put("replaceOrder",replaceOrderDO);
        String requestJson  = JSONObject.toJSONString(requestData);
        String k3confirmOrderUrl = null;
        try{
            HttpHeaderBuilder headerBuilder = HttpHeaderBuilder.custom();
            headerBuilder.contentType("application/json");
            if (ReplaceOrderStatus.REPLACE_ORDER_STATUS_PROCESSING.equals(replaceOrderDO.getReplaceOrderStatus())) {
                //审核通过推送换货单信息
                k3confirmOrderUrl = K3Config.k3Server + "/DataDelivery/Barter";
            } else if (ReplaceOrderStatus.REPLACE_ORDER_STATUS_CONFIRM.equals(replaceOrderDO.getReplaceOrderStatus()) ||
                    ReplaceOrderStatus.REPLACE_ORDER_STATUS_CLOSED.equals(replaceOrderDO.getReplaceOrderStatus())) {
                //确认换货推送换货单信息
                k3confirmOrderUrl = K3Config.k3Server + "/DataDelivery/ConfirmlBarter";
            } else {
                serviceResult.setErrorCode(ErrorCode.SEND_REPLACE_ORDER_TO_K3_STATUS_ERROR);
                return serviceResult;
            }
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
//            serviceResult.setResult(responseMap.get("Message").toString());
            return serviceResult;
//            response = HttpClientUtil.post(k3confirmOrderUrl, requestJson, headerBuilder, "UTF-8");
//            responseMap = JSONObject.parseObject(response,HashMap.class);
//            if ("true".equals(responseMap.get("IsSuccess").toString())){
//                serviceResult.setErrorCode(ErrorCode.SUCCESS);
//                serviceResult.setResult(responseMap.get("Message").toString());
//                return serviceResult;
//            }else{
//                StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
//                sb.append("向K3推送【换货单-").append(replaceOrderDO.getReplaceOrderNo()).append("】数据失败：");
//                sb.append(responseMap.get("Message").toString());
//                sb.append("\r\n").append("请求参数：").append(requestJson);
//                dingDingSupport.dingDingSendMessage(sb.toString());
//                serviceResult.setErrorCode(ErrorCode.K3_REPLACE_ORDER_ERROR,responseMap.get("Message").toString());
//                serviceResult.setResult(responseMap.get("Message").toString());
//                return serviceResult;
//            }
        }catch (Exception e){
            logger.error("向K3推送换货单异常：",e);
            StringBuffer sb = new StringBuffer(dingDingSupport.getEnvironmentString());
            sb.append("向K3推送【换货单-").append(replaceOrderDO.getReplaceOrderNo()).append("】数据失败：");
            sb.append(JSON.toJSONString(response));
            sb.append("\r\n").append("请求参数：").append(requestJson);
            dingDingSupport.dingDingSendMessage(sb.toString());
            serviceResult.setErrorCode(ErrorCode.K3_SERVER_ERROR);
            return serviceResult;
        }
    }

    @Override
    public ServiceResult<String,  Page<ReplaceOrder>> queryReplaceOrderListForOrderNo(ReplaceOrderQueryParam param) {
        ServiceResult<String, Page<ReplaceOrder>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("replaceOrderQueryParam", param);
        Integer totalCount = replaceOrderMapper.findReplaceOrderCountByParams(maps);
        List<ReplaceOrderDO> replaceOrderDOList = replaceOrderMapper.findReplaceOrderByParams(maps);
        List<ReplaceOrder> replaceOrderList = ConverterUtil.convertList(replaceOrderDOList, ReplaceOrder.class);
        Page<ReplaceOrder> page = new Page<>(replaceOrderList, totalCount, param.getPageNo(), param.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }



    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        ReplaceOrderDO replaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(businessNo);
        try {
            if (replaceOrderDO != null) {
                //不是审核中状态的收货单，拒绝处理
                if (!ReplaceOrderStatus.REPLACE_ORDER_STATUS_VERIFYING.equals(replaceOrderDO.getReplaceOrderStatus())) {
                    return ErrorCode.BUSINESS_EXCEPTION;
                }
                if (verifyResult) {
                    replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_PROCESSING);
                    replaceOrderMapper.update(replaceOrderDO);
//                    ServiceResult result = sendReplaceOrderInfoToK3(replaceOrderDO.getReplaceOrderNo());
//                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode().toString())) {
//                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
//                        return result.getErrorCode().toString();
//                    }

                } else {
                    replaceOrderDO.setReplaceOrderStatus(ReplaceOrderStatus.REPLACE_ORDER_STATUS_BACKED);
                    // 返还扣走或者添加的信用额度
                    BigDecimal updateTotalCreditDepositAmount = replaceOrderDO.getUpdateTotalCreditDepositAmount();
                    if (BigDecimalUtil.compare(updateTotalCreditDepositAmount, BigDecimal.ZERO) != 0) {
                        if (BigDecimalUtil.compare(updateTotalCreditDepositAmount, BigDecimal.ZERO) > 0) {
                            customerSupport.addCreditAmountUsed(replaceOrderDO.getCustomerId(), updateTotalCreditDepositAmount, CustomerRiskBusinessType.CANCEL_REPLACE_ORDER_TYPE, replaceOrderDO.getReplaceOrderNo(), null);
                        } else if (BigDecimalUtil.compare(updateTotalCreditDepositAmount, BigDecimal.ZERO) < 0) {
                            customerSupport.subCreditAmountUsed(replaceOrderDO.getCustomerId(), BigDecimalUtil.mul(updateTotalCreditDepositAmount, new BigDecimal(-1)), CustomerRiskBusinessType.CANCEL_REPLACE_ORDER_TYPE, replaceOrderDO.getReplaceOrderNo(), null);
                        }
                    }
                }
                replaceOrderMapper.update(replaceOrderDO);
                return ErrorCode.SUCCESS;
            } else {
                return ErrorCode.BUSINESS_EXCEPTION;
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            if (replaceOrderDO != null) {
                logger.error("【换货单审核后，业务处理异常】", e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                logger.error("【数据已回滚】");
            }
            return ErrorCode.BUSINESS_EXCEPTION;
        }
    }


    /**
     * 比较物料项
     */
    private boolean compareMaterialCount(ServiceResult<String, String> serviceResult, List<OrderMaterialDO> orderMaterialDOList, Map<Integer, OrderMaterialDO> orderMaterialDOMap, Map<Integer, Integer> replaceMaterialCountMap, Map<Integer, Integer> rentingMaterialCountMap, Map<Integer, Integer> materialCountMap) {
        for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
            orderMaterialDOMap.put(orderMaterialDO.getId(),orderMaterialDO);
            //在租数
            Integer rentingMaterialCount = rentingMaterialCountMap.get(orderMaterialDO.getId()) == null ? 0 : rentingMaterialCountMap.get(orderMaterialDO.getId());
            //待提交、处理中和审核中数量
            Integer processMaterialCount = materialCountMap.get(orderMaterialDO.getId()) == null ? 0 : materialCountMap.get(orderMaterialDO.getId());
            //换货数量
            Integer replaceMaterialCount = replaceMaterialCountMap.get(orderMaterialDO.getId()) == null ? 0 : replaceMaterialCountMap.get(orderMaterialDO.getId());
            //可换数量=在租数-待提交、处理中和审核中数量（包含退货和换货）
            Integer canReturnMaterialCount = rentingMaterialCount - processMaterialCount;
            if (replaceMaterialCount > canReturnMaterialCount) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_MATERIAL_COUNT_MORE_THAN_CANREPLACE_COUNT);
                return true;
            }
        }
        return false;
    }

    /**
     * 比较设备项
     */
    private boolean compareProductCount(ServiceResult<String, String> serviceResult, List<OrderProductDO> orderProductDOList, Map<Integer, OrderProductDO> orderProductDOMap, Map<Integer, Integer> replaceProductCountMap, Map<Integer, Integer> rentingProductCountMap, Map<Integer, Integer> productCountMap) {
        for (OrderProductDO orderProductDO : orderProductDOList) {
            orderProductDOMap.put(orderProductDO.getId(),orderProductDO);
            //在租数
            Integer rentingProductCount = rentingProductCountMap.get(orderProductDO.getId()) == null ? 0 : rentingProductCountMap.get(orderProductDO.getId());
            //待提交、处理中和审核中数量
            Integer processProductCount = productCountMap.get(orderProductDO.getId()) == null ? 0 : productCountMap.get(orderProductDO.getId());
            //换货数量
            Integer replaceProductCount = replaceProductCountMap.get(orderProductDO.getId()) == null ? 0 : replaceProductCountMap.get(orderProductDO.getId());
            //可换数量=在租数-待提交、处理中和审核中数量（包含退货和换货）
            Integer canReturnProductCount = rentingProductCount - processProductCount;
            if (replaceProductCount > canReturnProductCount) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_PRODUCT_COUNT_MORE_THAN_CANREPLACE_COUNT);
                return true;
            }
        }
        return false;
    }

    /**
     * 将该订单的待提交、审核中两种状态的换货单商品或配件数量保存
     */
    private void saveExistedReplaceCount( Map<Integer, Integer> productCountMap, Map<Integer, Integer> materialCountMap, List<ReplaceOrderDO> replaceOrderDOList,String replaceOrderNo) {
        if (CollectionUtil.isNotEmpty(replaceOrderDOList)) {
            for (ReplaceOrderDO exReplaceOrderDO:replaceOrderDOList){
                if (exReplaceOrderDO.getReplaceOrderNo().equals(replaceOrderNo)) {
                    continue;
                }
                List<ReplaceOrderProductDO> exReplaceOrderProductDOList = exReplaceOrderDO.getReplaceOrderProductDOList();
                List<ReplaceOrderMaterialDO> exReplaceOrderMaterialDOList = exReplaceOrderDO.getReplaceOrderMaterialDOList();
                if (CollectionUtil.isNotEmpty(exReplaceOrderProductDOList)) {
                    for (ReplaceOrderProductDO replaceOrderProductDO:exReplaceOrderProductDOList) {
                        if (!productCountMap.containsKey(replaceOrderProductDO.getOldOrderProductId())) {
                            productCountMap.put(replaceOrderProductDO.getOldOrderProductId(), replaceOrderProductDO.getReplaceProductCount());
                        }else {
                            Integer productCount = replaceOrderProductDO.getReplaceProductCount() + productCountMap.get(replaceOrderProductDO.getOldOrderProductId());
                            productCountMap.put(replaceOrderProductDO.getOldOrderProductId(), productCount);
                        }
                    }
                }
                if (CollectionUtil.isNotEmpty(exReplaceOrderMaterialDOList)) {
                    for (ReplaceOrderMaterialDO replaceOrderMaterialDO:exReplaceOrderMaterialDOList) {
                        if (!materialCountMap.containsKey(replaceOrderMaterialDO.getOldOrderMaterialId())) {
                            materialCountMap.put(replaceOrderMaterialDO.getOldOrderMaterialId(), replaceOrderMaterialDO.getReplaceMaterialCount());
                        }else {
                            Integer materialCount = replaceOrderMaterialDO.getReplaceMaterialCount() + materialCountMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
                            materialCountMap.put(replaceOrderMaterialDO.getOldOrderMaterialId(), materialCount);
                        }
                    }
                }
            }
        }
    }

    /**
     * 校验是否在续租单开始之前换货
     */
    private boolean checkReplaceTiamAndReletTime(ServiceResult<String, String> serviceResult, SimpleDateFormat simpleDateFormat, String replaceTimeString, String reletTimeString) {
        try {
            Date replaceTimeDate = simpleDateFormat.parse(replaceTimeString);
            Date reletTimeDate = simpleDateFormat.parse(reletTimeString);
            if (!(replaceTimeDate.compareTo(reletTimeDate)>=0)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_BEFORE_RELET_TIME);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("【创建换货单,校验是否在续租单开始之前换货,时间parse出错】", e);
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_PARSE_ERROR);
            return true;
        }
        return false;
    }

    /**
     * 校验换货时间
     */
    private boolean checkReplaceTime(ServiceResult<String, String> serviceResult, SimpleDateFormat simpleDateFormat, String replaceTimeString, String rentStartTimeString, String expectReturnTimeString) {
        try {
            Date replaceTimeDate = simpleDateFormat.parse(replaceTimeString);
            Date rentStartTimeDate = simpleDateFormat.parse(rentStartTimeString);
            Date expectReturnTimeDate = simpleDateFormat.parse(expectReturnTimeString);
            if (!(replaceTimeDate.compareTo(rentStartTimeDate)>0)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_MUST_AFTER_RENT_START_TIME);
                return true;
            } else if (!(rentStartTimeDate.compareTo(expectReturnTimeDate)<0)) {
                serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_MUST_BEFORE_EXPECT_RETURN_TIME);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("【创建换货单,校验换货时间parse出错】", e);
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_PARSE_ERROR);
            return true;
        }
        return false;
    }
    /**
     * 校验实际换货时间
     */
    private boolean checkRealReplaceTime(ServiceResult<String, String> serviceResult, SimpleDateFormat simpleDateFormat, String realReplaceTimeString, String replaceTimeString, String nowTimeString) {
        try {
            Date realReplaceTimeDate = simpleDateFormat.parse(realReplaceTimeString);
            Date replaceTimeDate = simpleDateFormat.parse(replaceTimeString);
            Date nowTimeDate = simpleDateFormat.parse(nowTimeString);
            if (realReplaceTimeDate.compareTo(replaceTimeDate)<0) {
                //实际换货时间不能小于预计换货时间
                serviceResult.setErrorCode(ErrorCode.REAL_REPLACE_TIME_MUST_AFTER_REPLACE_TIME);
                return true;
            } else if (realReplaceTimeDate.compareTo(nowTimeDate)>0) {
                //实际换货时间不能大于当前时间
                serviceResult.setErrorCode(ErrorCode.REAL_REPLACE_TIME_MUST_BEFORE_CONFIRM_TIME);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("【确认换货单,校验换货时间parse出错】", e);
            serviceResult.setErrorCode(ErrorCode.REPLACE_TIME_PARSE_ERROR);
            return true;
        }
        return false;
    }
    /**
     * 装配该订单续租单map集合
     */
    private void assemblyReletOrder(Map<Integer, ReletOrderProductDO> reletOrderProductDOMap, Map<Integer, ReletOrderMaterialDO> reletOrderMaterialDOMap, ReletOrderDO reletOrderDO) {
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
    }
    /**
     * 获取换货数量并保存到map
     */
    private void saveRepalceCountInMap(List<ReplaceOrderProductDO> replaceOrderProductDOList, List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList, Map<Integer, Integer> replaceProductCountMap, Map<Integer, Integer> replaceMaterialCountMap) {
        for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
            if (!replaceProductCountMap.containsKey(replaceOrderProductDO.getOldOrderProductId())) {
                replaceProductCountMap.put(replaceOrderProductDO.getOldOrderProductId(), replaceOrderProductDO.getReplaceProductCount());
            }else {
                Integer replaceProductCount = replaceOrderProductDO.getReplaceProductCount() + replaceProductCountMap.get(replaceOrderProductDO.getOldOrderProductId());
                replaceProductCountMap.put(replaceOrderProductDO.getOldOrderProductId(), replaceProductCount);
            }
        }
        for (ReplaceOrderMaterialDO replaceOrderMaterialDO : replaceOrderMaterialDOList) {
            if (!replaceMaterialCountMap.containsKey(replaceOrderMaterialDO.getOldOrderMaterialId())) {
                replaceMaterialCountMap.put(replaceOrderMaterialDO.getOldOrderMaterialId(), replaceOrderMaterialDO.getReplaceMaterialCount());
            }else {
                Integer replaceMaterialCount = replaceOrderMaterialDO.getReplaceMaterialCount() + replaceMaterialCountMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
                replaceMaterialCountMap.put(replaceOrderMaterialDO.getOldOrderMaterialId(), replaceMaterialCount);
            }
        }
    }


    /**
     * 获取该用户处于待提交、审核中、处理中三种状态的商品或者配件的数量
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
    /**
     * 补全换货商品项信息
     */
    public BigDecimal calculateOrderProductInfo(List<ReplaceOrderProductDO> replaceOrderProductDOList,Map<Integer,OrderProductDO> orderProductDOMap,OrderDO orderDO,BigDecimal totalCreditDepositAmount,CustomerRiskManagementDO customerRiskManagementDO) {

        if (CollectionUtil.isNotEmpty(replaceOrderProductDOList)) {
            for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;
                OrderProductDO orderProductDO = orderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
                if (orderProductDO == null) {
                    throw new BusinessException(ErrorCode.ORDER_PRODUCT_NOT_EXISTS);
                }
                ServiceResult<String, Product> oldproductServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(oldproductServiceResult.getErrorCode())) {
                    throw new BusinessException(oldproductServiceResult.getErrorCode());
                }
                Product oldProduct = oldproductServiceResult.getResult();


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
                    if (replaceOrderProductDO.getReplaceProductCount() > 0) {
                        BigDecimal remainder = replaceOrderProductDO.getDepositAmount().divideAndRemainder(new BigDecimal(replaceOrderProductDO.getReplaceProductCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_PRODUCT_DEPOSIT_ERROR);
                        }
                    }
                    depositAmount = replaceOrderProductDO.getDepositAmount();
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(replaceOrderProductDO.getReplaceProductCount()));
                } else {
                    if ((BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) || CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderProductDO.getIsNewProduct())) {
                        Integer depositCycle = replaceOrderProductDO.getDepositCycle() <= replaceOrderProductDO.getRentTimeLength() ? replaceOrderProductDO.getDepositCycle() : replaceOrderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderProductDO.getProductUnitAmount(), new BigDecimal(replaceOrderProductDO.getReplaceProductCount()), 2), new BigDecimal(depositCycle));
                    } else {
                        Integer depositCycle = replaceOrderProductDO.getDepositCycle() <= replaceOrderProductDO.getRentTimeLength() ? replaceOrderProductDO.getDepositCycle() : replaceOrderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderProductDO.getProductUnitAmount(), new BigDecimal(replaceOrderProductDO.getReplaceProductCount()), 2), new BigDecimal(depositCycle));
                    }
                    creditDepositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(replaceOrderProductDO.getReplaceProductCount()));
                    BigDecimal oldSkuPrice= BigDecimalUtil.div(orderProductDO.getCreditDepositAmount(),new BigDecimal(orderProductDO.getProductCount()),4);
                    BigDecimal oldOrderProductCreditDepositAmount = BigDecimalUtil.mul(oldSkuPrice, new BigDecimal(replaceOrderProductDO.getReplaceProductCount()));
                    totalCreditDepositAmount = BigDecimalUtil.sub(totalCreditDepositAmount,oldOrderProductCreditDepositAmount);
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                }
                replaceOrderProductDO.setOldProductSkuSnapshot(orderProductDO.getProductSkuSnapshot());
                replaceOrderProductDO.setNewProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                replaceOrderProductDO.setProductId(product.getProductId());
                replaceOrderProductDO.setProductName(product.getProductName());
                replaceOrderProductDO.setRentDepositAmount(rentDepositAmount);
                replaceOrderProductDO.setDepositAmount(depositAmount);
                replaceOrderProductDO.setCreditDepositAmount(creditDepositAmount);
                replaceOrderProductDO.setProductSkuName(skuName);
                replaceOrderProductDO.setOldProductId(orderProductDO.getProductId());
                replaceOrderProductDO.setOldProductName(orderProductDO.getProductName());
                replaceOrderProductDO.setOldProductSkuId(orderProductDO.getProductSkuId());
                replaceOrderProductDO.setOldProductSkuName(orderProductDO.getProductSkuName());
                replaceOrderProductDO.setOldProductNumber(oldProduct.getK3ProductNo());
                replaceOrderProductDO.setOldIsNewProduct(orderProductDO.getIsNewProduct());
                replaceOrderProductDO.setProductNumber(product.getK3ProductNo());
                replaceOrderProductDO.setOldRentingProductCount(orderProductDO.getRentingProductCount());
            }
        }
        return totalCreditDepositAmount;
    }
    /**
     * 补全换货配件项信息
     */
    public BigDecimal calculateOrderMaterialInfo(List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList,Map<Integer,OrderMaterialDO> orderMaterialDOMap,OrderDO orderDO,BigDecimal totalCreditDepositAmount,CustomerRiskManagementDO customerRiskManagementDO) {
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
                    if (replaceOrderMaterialDO.getReplaceMaterialCount() > 0) {
                        BigDecimal remainder = replaceOrderMaterialDO.getDepositAmount().divideAndRemainder(new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_MATERIAL_DEPOSIT_ERROR);
                        }
                    }

                    depositAmount = replaceOrderMaterialDO.getDepositAmount();
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()));
                } else {
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderMaterialDO.getIsNewMaterial())) {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()), 2), new BigDecimal(replaceOrderMaterialDO.getDepositCycle()));
                    } else {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()), 2), new BigDecimal(replaceOrderMaterialDO.getDepositCycle()));
                    }
                    MaterialTypeDO materialTypeDO = materialTypeMapper.findById(material.getMaterialType());
                    if (materialTypeDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsMainMaterial())) {
                        creditDepositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()));
                        totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);

                        OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
                        BigDecimal oldSkuPrice= BigDecimalUtil.div(orderMaterialDO.getCreditDepositAmount(),new BigDecimal(orderMaterialDO.getMaterialCount()),4);
                        BigDecimal oldOrderMaterialCreditDepositAmount = BigDecimalUtil.mul(oldSkuPrice, new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()));
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
        return totalCreditDepositAmount;
    }

    /**
     * 确认换货没有全部收货时变更换货单商品信息
     */
    public BigDecimal changeReplaceOrderProductInfo(List<ReplaceOrderProductDO> replaceOrderProductDOList,Map<Integer,OrderProductDO> orderProductDOMap,OrderDO orderDO,BigDecimal totalCreditDepositAmount,CustomerRiskManagementDO customerRiskManagementDO) {

        if (CollectionUtil.isNotEmpty(replaceOrderProductDOList)) {
            for (ReplaceOrderProductDO replaceOrderProductDO : replaceOrderProductDOList) {
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;
                OrderProductDO orderProductDO = orderProductDOMap.get(replaceOrderProductDO.getOldOrderProductId());
                if (orderProductDO == null) {
                    throw new BusinessException(ErrorCode.ORDER_PRODUCT_NOT_EXISTS);
                }
                ServiceResult<String, Product> oldproductServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(oldproductServiceResult.getErrorCode())) {
                    throw new BusinessException(oldproductServiceResult.getErrorCode());
                }
                Product oldProduct = oldproductServiceResult.getResult();


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
                    if (replaceOrderProductDO.getRealReplaceProductCount() > 0) {
                        BigDecimal remainder = replaceOrderProductDO.getDepositAmount().divideAndRemainder(new BigDecimal(replaceOrderProductDO.getRealReplaceProductCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_PRODUCT_DEPOSIT_ERROR);
                        }
                    }
                    depositAmount = replaceOrderProductDO.getDepositAmount();
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(replaceOrderProductDO.getRealReplaceProductCount()));
                } else {
                    if ((BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) || CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderProductDO.getIsNewProduct())) {
                        Integer depositCycle = replaceOrderProductDO.getDepositCycle() <= replaceOrderProductDO.getRentTimeLength() ? replaceOrderProductDO.getDepositCycle() : replaceOrderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderProductDO.getProductUnitAmount(), new BigDecimal(replaceOrderProductDO.getRealReplaceProductCount()), 2), new BigDecimal(depositCycle));
                    } else {
                        Integer depositCycle = replaceOrderProductDO.getDepositCycle() <= replaceOrderProductDO.getRentTimeLength() ? replaceOrderProductDO.getDepositCycle() : replaceOrderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderProductDO.getProductUnitAmount(), new BigDecimal(replaceOrderProductDO.getRealReplaceProductCount()), 2), new BigDecimal(depositCycle));
                    }
                    creditDepositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(replaceOrderProductDO.getRealReplaceProductCount()));
                    BigDecimal oldSkuPrice= BigDecimalUtil.div(orderProductDO.getCreditDepositAmount(),new BigDecimal(orderProductDO.getProductCount()),4);
                    BigDecimal oldOrderProductCreditDepositAmount = BigDecimalUtil.mul(oldSkuPrice, new BigDecimal(replaceOrderProductDO.getRealReplaceProductCount()));
                    totalCreditDepositAmount = BigDecimalUtil.sub(totalCreditDepositAmount,oldOrderProductCreditDepositAmount);
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                }
                replaceOrderProductDO.setRentDepositAmount(rentDepositAmount);
                replaceOrderProductDO.setDepositAmount(depositAmount);
                replaceOrderProductDO.setCreditDepositAmount(creditDepositAmount);
            }
        }
        return totalCreditDepositAmount;
    }

    /**
     * 确认换货没有全部收货时变更换货单配件信息
     */
    public BigDecimal changeReplaceOrderMaterialInfo(List<ReplaceOrderMaterialDO> replaceOrderMaterialDOList,Map<Integer,OrderMaterialDO> orderMaterialDOMap,OrderDO orderDO,BigDecimal totalCreditDepositAmount,CustomerRiskManagementDO customerRiskManagementDO) {
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
                    if (replaceOrderMaterialDO.getReplaceMaterialCount() > 0) {
                        BigDecimal remainder = replaceOrderMaterialDO.getDepositAmount().divideAndRemainder(new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()))[1];
                        if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                            throw new BusinessException(ErrorCode.ORDER_MATERIAL_DEPOSIT_ERROR);
                        }
                    }

                    depositAmount = replaceOrderMaterialDO.getDepositAmount();
                } else if (customerRiskManagementDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()));
                } else {
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(replaceOrderMaterialDO.getIsNewMaterial())) {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()), 2), new BigDecimal(replaceOrderMaterialDO.getDepositCycle()));
                    } else {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(replaceOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()), 2), new BigDecimal(replaceOrderMaterialDO.getDepositCycle()));
                    }
                    MaterialTypeDO materialTypeDO = materialTypeMapper.findById(material.getMaterialType());
                    if (materialTypeDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsMainMaterial())) {
                        creditDepositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()));
                        totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);

                        OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(replaceOrderMaterialDO.getOldOrderMaterialId());
                        BigDecimal oldSkuPrice= BigDecimalUtil.div(orderMaterialDO.getCreditDepositAmount(),new BigDecimal(orderMaterialDO.getMaterialCount()),4);
                        BigDecimal oldOrderMaterialCreditDepositAmount = BigDecimalUtil.mul(oldSkuPrice, new BigDecimal(replaceOrderMaterialDO.getReplaceMaterialCount()));
                        totalCreditDepositAmount = BigDecimalUtil.sub(totalCreditDepositAmount,oldOrderMaterialCreditDepositAmount);
                        totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                    }
                }
                replaceOrderMaterialDO.setRentDepositAmount(rentDepositAmount);
                replaceOrderMaterialDO.setDepositAmount(depositAmount);
                replaceOrderMaterialDO.setCreditDepositAmount(creditDepositAmount);
            }
        }
        return totalCreditDepositAmount;
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
    @Autowired
    private ImgMysqlMapper imgMysqlMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;
    @Autowired
    private PostK3ServiceManager postK3ServiceManager;
    @Autowired
    private DingDingSupport dingDingSupport;
    @Autowired
    private K3Service k3Service;
    @Autowired
    private ReplaceOrderSupport replaceOrderSupport;
    @Autowired
    private StatementReplaceOrderSupport statementReplaceOrderSupport;
    @Autowired
    private AmountSupport amountSupport;
    @Autowired
    private HttpSession httpSession;


}
