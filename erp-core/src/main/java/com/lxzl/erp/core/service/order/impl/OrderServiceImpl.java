package com.lxzl.erp.core.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erpInterface.order.InterfaceOrderQueryParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.*;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.ERPServiceLocator;
import com.lxzl.erp.core.k3WebServiceSdk.ErpServer.IERPService;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.WebServiceHelper;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.material.impl.support.MaterialSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statement.impl.support.StatementOrderSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.xml.rpc.ServiceException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createOrder(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        String verifyCreateOrderCode = verifyOperateOrder(order);
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }
        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);

        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);
        calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
        calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);

        if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(userSupport.getCurrentUserCompanyId())) {
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(order.getDeliverySubCompanyId());
            if (order.getDeliverySubCompanyId() == null || subCompanyDO == null) {
                result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
                return result;
            }
            orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
            orderDO.setDeliverySubCompanyId(order.getDeliverySubCompanyId());
        } else {
            orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
            orderDO.setDeliverySubCompanyId(userSupport.getCurrentUserCompanyId());
        }
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(orderDO.getOrderSubCompanyId());
        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setOrderNo(generateNoSupport.generateOrderNo(currentTime, subCompanyDO != null ? subCompanyDO.getSubCompanyCode() : null));
        orderDO.setOrderSellerId(customerDO.getOwner());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setCreateUser(loginUser.getUserId().toString());
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setCreateTime(currentTime);
        orderDO.setUpdateTime(currentTime);
        //添加当前客户名称
        orderDO.setBuyerCustomerName(customerDO.getCustomerName());

        Date expectReturnTime = generateExpectReturnTime(orderDO);
        orderDO.setExpectReturnTime(expectReturnTime);
        orderMapper.save(orderDO);
        saveOrderProductInfo(orderDO.getOrderProductDOList(), orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO.getId(), loginUser, currentTime);
        updateOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);

        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> updateOrder(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        String verifyCreateOrderCode = verifyOperateOrder(order);
        if (!ErrorCode.SUCCESS.equals(verifyCreateOrderCode)) {
            result.setErrorCode(verifyCreateOrderCode);
            return result;
        }
        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        OrderDO dbOrderDO = orderMapper.findByOrderNo(order.getOrderNo());
        if (dbOrderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(dbOrderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (!loginUser.getUserId().toString().equals(dbOrderDO.getCreateUser())) {
            result.setErrorCode(ErrorCode.DATA_NOT_BELONG_TO_YOU);
            return result;
        }

        List<OrderProductDO> orderProductDOList = ConverterUtil.convertList(order.getOrderProductList(), OrderProductDO.class);
        List<OrderMaterialDO> orderMaterialDOList = ConverterUtil.convertList(order.getOrderMaterialList(), OrderMaterialDO.class);
        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);
        orderDO.setOrderProductDOList(orderProductDOList);
        orderDO.setOrderMaterialDOList(orderMaterialDOList);
        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);
        calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
        calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);

        if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(userSupport.getCurrentUserCompanyId())) {
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(order.getDeliverySubCompanyId());
            if (order.getDeliverySubCompanyId() == null || subCompanyDO == null) {
                result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
                return result;
            }
            orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
            orderDO.setDeliverySubCompanyId(order.getDeliverySubCompanyId());
        } else {
            orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
            orderDO.setDeliverySubCompanyId(userSupport.getCurrentUserCompanyId());
        }
        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setId(dbOrderDO.getId());
        orderDO.setOrderNo(dbOrderDO.getOrderNo());
        orderDO.setOrderSellerId(customerDO.getOwner());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        //添加当前客户名称
        orderDO.setBuyerCustomerName(customerDO.getCustomerName());

        Date expectReturnTime = generateExpectReturnTime(orderDO);
        orderDO.setExpectReturnTime(expectReturnTime);
        orderMapper.update(orderDO);
        saveOrderProductInfo(orderDO.getOrderProductDOList(), orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO.getId(), loginUser, currentTime);
        updateOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitOrder(OrderCommitParam orderCommitParam) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        String orderNo = orderCommitParam.getOrderNo();
        Integer verifyUser = orderCommitParam.getVerifyUser();
        String commitRemark = orderCommitParam.getCommitRemark();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (CollectionUtil.isEmpty(orderDO.getOrderProductDOList())
                && CollectionUtil.isEmpty(orderDO.getOrderMaterialDOList())) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL);
            return result;
        }

        //只有创建订单本人可以提交
        if (!orderDO.getCreateUser().equals(loginUser.getUserId().toString())) {
            result.setErrorCode(ErrorCode.COMMIT_ONLY_SELF);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(orderDO.getBuyerCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }

        String verifyOrderShortRentReceivableResult = verifyOrderShortRentReceivable(customerDO, orderDO);
        if (!ErrorCode.SUCCESS.equals(verifyOrderShortRentReceivableResult)) {
            result.setErrorCode(verifyOrderShortRentReceivableResult);
            return result;
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            int oldProductCount = 0, newProductCount = 0;
            Map<Integer, Integer> productNewStockMap = new HashMap<>();
            Map<Integer, Integer> productOldStockMap = new HashMap<>();
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    result.setErrorCode(productServiceResult.getErrorCode());
                    return result;
                }
                Product product = productServiceResult.getResult();
                if (CommonConstant.COMMON_CONSTANT_NO.equals(product.getIsRent())) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NOT_RENT);
                    return result;
                }
                if (CollectionUtil.isEmpty(product.getProductSkuList())) {
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                if (productNewStockMap.get(product.getProductId()) == null) {
                    productNewStockMap.put(product.getProductId(), product.getNewProductCount());
                }
                if (productOldStockMap.get(product.getProductId()) == null) {
                    productOldStockMap.put(product.getProductId(), product.getOldProductCount());
                }
                oldProductCount = productOldStockMap.get(product.getProductId());
                newProductCount = productNewStockMap.get(product.getProductId());

                /*if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                    if ((newProductCount - orderProductDO.getProductCount()) < 0) {
                        result.setErrorCode(ErrorCode.ORDER_PRODUCT_STOCK_NEW_INSUFFICIENT);
                        return result;
                    } else {
                        newProductCount = newProductCount - orderProductDO.getProductCount();
                        productNewStockMap.put(product.getProductId(), newProductCount);
                    }
                } else {
                    if ((oldProductCount - orderProductDO.getProductCount()) < 0) {
                        result.setErrorCode(ErrorCode.ORDER_PRODUCT_STOCK_OLD_INSUFFICIENT);
                        return result;
                    } else {
                        oldProductCount = oldProductCount - orderProductDO.getProductCount();
                        productOldStockMap.put(product.getProductId(), oldProductCount);
                    }
                }*/
            }
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())
                        || materialServiceResult.getResult() == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }
                Material material = materialServiceResult.getResult();
                /*if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                    if (material == null || material.getNewMaterialCount() == null || material.getNewMaterialCount() <= 0 || (material.getNewMaterialCount() - orderMaterialDO.getMaterialCount()) < 0) {
                        result.setErrorCode(ErrorCode.ORDER_MATERIAL_STOCK_NEW_INSUFFICIENT);
                        return result;
                    }
                } else {
                    if (material == null || material.getOldMaterialCount() == null || material.getOldMaterialCount() <= 0 || (material.getOldMaterialCount() - orderMaterialDO.getMaterialCount()) < 0) {
                        result.setErrorCode(ErrorCode.ORDER_MATERIAL_STOCK_OLD_INSUFFICIENT);
                        return result;
                    }
                }*/
            }
        }

        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        BigDecimal totalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        if (customerRiskManagementDO == null && BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) > 0) {
            result.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_NEED_RISK_INFO);
            return result;
        }
        if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) > 0 && BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), totalCreditDepositAmount), BigDecimal.ZERO) < 0) {
            result.setErrorCode(ErrorCode.CUSTOMER_GET_CREDIT_AMOUNT_OVER_FLOW);
            return result;
        }

        ServiceResult<String, Boolean> isNeedVerifyResult = isNeedVerify(orderNo);
        if (!ErrorCode.SUCCESS.equals(isNeedVerifyResult.getErrorCode())) {
            result.setErrorCode(isNeedVerifyResult.getErrorCode(), isNeedVerifyResult.getFormatArgs());
            return result;
        }
        // 是否需要审批
        boolean isNeedVerify = isNeedVerifyResult.getResult();

        String orderRemark = null;
        if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
            orderRemark = "租赁类型：天租";
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
            orderRemark = "租赁类型：月租";
        }

        if (isNeedVerify) {
            //如果要审核，判断审核注意事项
            ServiceResult<String, String> verifyMattersResult = getVerifyMatters(orderDO);
            if (!ErrorCode.SUCCESS.equals(verifyMattersResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(verifyMattersResult.getErrorCode());
                return result;
            }
            String verifyMatters = verifyMattersResult.getResult();

            ServiceResult<String, String> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderDO.getOrderNo(), verifyUser, verifyMatters, commitRemark, orderCommitParam.getImgIdList(), orderRemark);
            if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(workflowCommitResult.getErrorCode());
                return result;
            }
            orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId());
        }

        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_VERIFYING);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);

        // 扣除信用额度
        if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) != 0) {
            customerSupport.addCreditAmountUsed(orderDO.getBuyerCustomerId(), totalCreditDepositAmount);
        }
        if (!isNeedVerify) {
            String code = receiveVerifyResult(true, orderDO.getOrderNo());
            if (!ErrorCode.SUCCESS.equals(code)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(ErrorCode.SYSTEM_EXCEPTION);
                return result;
            }
        }
        result.setResult(orderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> payOrder(String orderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        result.setResult(orderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Boolean> isNeedVerify(String orderNo) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }

        Boolean isNeedVerify = false;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    result.setErrorCode(productServiceResult.getErrorCode());
                    return result;
                }
                Product product = productServiceResult.getResult();
                if (product == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                ProductSku thisProductSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (thisProductSku == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }

                BigDecimal productUnitAmount = null;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    productUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? thisProductSku.getNewDayRentPrice() : thisProductSku.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                    productUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? thisProductSku.getNewMonthRentPrice() : thisProductSku.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价，需要商务审批
                if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), productUnitAmount) < 0) {
                    isNeedVerify = true;
                    break;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    result.setErrorCode(materialServiceResult.getErrorCode());
                    return result;
                }
                Material material = materialServiceResult.getResult();
                if (material == null) {
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }

                BigDecimal materialUnitAmount = null;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                    materialUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewDayRentPrice() : material.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                    materialUnitAmount = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMonthRentPrice() : material.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价，需要商务审批
                if (BigDecimalUtil.compare(orderMaterialDO.getMaterialUnitAmount(), materialUnitAmount) < 0) {
                    isNeedVerify = true;
                    break;
                }
            }
        }

        // 检查是否需要审批流程
        if (isNeedVerify) {
            ServiceResult<String, Boolean> isMeedVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_ORDER_INFO);
            if (!ErrorCode.SUCCESS.equals(isMeedVerifyResult.getErrorCode())) {
                result.setErrorCode(isMeedVerifyResult.getErrorCode());
                return result;
            }
            if (!isMeedVerifyResult.getResult()) {
                result.setErrorCode(ErrorCode.WORKFLOW_HAVE_NO_CONFIG);
                return result;
            }
        }

        result.setResult(isNeedVerify);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, LastRentPriceResponse> queryLastPrice(LastRentPriceRequest request) {
        ServiceResult<String, LastRentPriceResponse> result = new ServiceResult<>();

        if (request.getCustomerNo() == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NO_NOT_NULL);
            return result;
        }
        if (request.getProductSkuId() == null && request.getMaterialId() == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }

        CustomerDO customerDO = customerMapper.findByNo(request.getCustomerNo());
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }


        LastRentPriceResponse response = new LastRentPriceResponse();
        response.setCustomerNo(request.getCustomerNo());
        BigDecimal productLastDayAmount = null, productLastMonthAmount = null, monthLastDayAmount = null, monthLastMonthAmount = null;
        if (request.getProductSkuId() != null) {
            ServiceResult<String, Product> queryProductResult = productService.queryProductBySkuId(request.getProductSkuId());
            if (!ErrorCode.SUCCESS.equals(queryProductResult.getErrorCode())) {
                result.setErrorCode(queryProductResult.getErrorCode());
                return result;
            }
            Product product = queryProductResult.getResult();
            List<Map<String, Object>> productSkuLastPriceMap = orderProductMapper.queryLastPrice(customerDO.getId(), request.getProductSkuId(), request.getIsNewProduct());
            for (Map<String, Object> map : productSkuLastPriceMap) {
                if (map.get("rent_type") != null && OrderRentType.RENT_TYPE_DAY.equals(map.get("rent_type"))) {
                    productLastDayAmount = (BigDecimal) (map.get("product_unit_amount"));
                } else if (map.get("rent_type") != null && OrderRentType.RENT_TYPE_MONTH.equals(map.get("rent_type"))) {
                    productLastMonthAmount = (BigDecimal) (map.get("product_unit_amount"));
                }
            }
            response.setProductSkuId(request.getProductSkuId());
            response.setProductSkuLastDayPrice(productLastDayAmount);
            response.setProductSkuLastMonthPrice(productLastMonthAmount);
            response.setProductSkuDayPrice(request.getIsNewProduct() == null || CommonConstant.COMMON_CONSTANT_NO.equals(request.getIsNewProduct()) ? product.getProductSkuList().get(0).getDayRentPrice() : product.getProductSkuList().get(0).getNewDayRentPrice());
            response.setProductSkuMonthPrice(request.getIsNewProduct() == null || CommonConstant.COMMON_CONSTANT_NO.equals(request.getIsNewProduct()) ? product.getProductSkuList().get(0).getMonthRentPrice() : product.getProductSkuList().get(0).getNewMonthRentPrice());
        }

        if (request.getMaterialId() != null) {
            ServiceResult<String, Material> queryMaterialResult = materialService.queryMaterialById(request.getMaterialId());
            if (!ErrorCode.SUCCESS.equals(queryMaterialResult.getErrorCode())) {
                result.setErrorCode(queryMaterialResult.getErrorCode());
                return result;
            }
            Material material = queryMaterialResult.getResult();
            List<Map<String, Object>> materialSkuLastPriceMap = orderMaterialMapper.queryLastPrice(customerDO.getId(), request.getProductSkuId(), request.getIsNewProduct());
            for (Map<String, Object> map : materialSkuLastPriceMap) {
                if (map.get("rent_type") != null && OrderRentType.RENT_TYPE_DAY.equals(map.get("rent_type"))) {
                    monthLastDayAmount = (BigDecimal) (map.get("product_unit_amount"));
                } else if (map.get("rent_type") != null && OrderRentType.RENT_TYPE_MONTH.equals(map.get("rent_type"))) {
                    monthLastMonthAmount = (BigDecimal) (map.get("product_unit_amount"));
                }
            }
            response.setMaterialId(request.getMaterialId());
            response.setMaterialLastDayPrice(monthLastDayAmount);
            response.setMaterialLastMonthPrice(monthLastMonthAmount);
            response.setMaterialDayPrice(request.getIsNewMaterial() == null || CommonConstant.COMMON_CONSTANT_NO.equals(request.getIsNewMaterial()) ? material.getDayRentPrice() : material.getNewDayRentPrice());
            response.setMaterialMonthPrice(request.getIsNewMaterial() == null || CommonConstant.COMMON_CONSTANT_NO.equals(request.getIsNewMaterial()) ? material.getMonthRentPrice() : material.getNewMonthRentPrice());
        }

        result.setResult(response);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> returnEquipment(String orderNo, String returnEquipmentNo, String changeEquipmentNo, Date returnDate) {
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null || returnEquipmentNo == null || returnDate == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), returnEquipmentNo);
        if (orderProductEquipmentDO == null) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_EXISTS);
            return result;
        }
        if (orderProductEquipmentDO.getActualReturnTime() != null) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_ALREADY_RETURN);
            return result;
        }
        OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
        // 如果是换货，产生新的记录
        if (StringUtil.isNotBlank(changeEquipmentNo)) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(changeEquipmentNo);
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS);
                return result;
            }
            ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(productEquipmentDO.getSkuId());
            if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                result.setErrorCode(productServiceResult.getErrorCode());
                return result;
            }
            OrderProductEquipmentDO newOrderProductEquipmentDO = new OrderProductEquipmentDO();
            newOrderProductEquipmentDO.setOrderId(orderProductEquipmentDO.getOrderId());
            newOrderProductEquipmentDO.setOrderProductId(orderProductEquipmentDO.getOrderProductId());
            newOrderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
            newOrderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
            newOrderProductEquipmentDO.setRentStartTime(returnDate);
            newOrderProductEquipmentDO.setExpectReturnTime(orderProductEquipmentDO.getExpectReturnTime());

            // TODO 换货价格按照最新的价格来算还是按照原来的价格来算，现在采用的是按照之前价格来算
            BigDecimal productUnitAmount = orderProductDO.getProductUnitAmount();
            if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                productUnitAmount = productServiceResult.getResult().getProductSkuList().get(0).getDayRentPrice();
            } else if ((OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType()))) {
                productUnitAmount = productServiceResult.getResult().getProductSkuList().get(0).getMonthRentPrice();
            }
            newOrderProductEquipmentDO.setProductEquipmentUnitAmount(orderProductEquipmentDO.getProductEquipmentUnitAmount());
            newOrderProductEquipmentDO.setExpectRentAmount(amountSupport.calculateRentAmount(returnDate, orderProductEquipmentDO.getExpectReturnTime(), orderProductEquipmentDO.getProductEquipmentUnitAmount()));
            newOrderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            newOrderProductEquipmentDO.setCreateTime(currentTime);
            newOrderProductEquipmentDO.setUpdateTime(currentTime);
            newOrderProductEquipmentDO.setCreateUser(loginUser.getUserId().toString());
            newOrderProductEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
            orderProductEquipmentMapper.save(newOrderProductEquipmentDO);
        }

        orderProductEquipmentDO.setActualRentAmount(amountSupport.calculateRentAmount(orderProductEquipmentDO.getRentStartTime(), returnDate, orderProductEquipmentDO.getProductEquipmentUnitAmount()));
        orderProductEquipmentDO.setActualReturnTime(returnDate);
        orderProductEquipmentDO.setUpdateTime(currentTime);
        orderProductEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
        orderProductEquipmentMapper.update(orderProductEquipmentDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> returnBulkMaterial(String orderNo, String returnNBulkMaterialNo, String changeBulkMaterialNo, Date returnDate) {
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ORDER_NO_NOT_NULL);
            return result;
        }
        if (returnNBulkMaterialNo == null || returnDate == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        BulkMaterialDO returnBulkMaterialDO = bulkMaterialMapper.findByNo(returnNBulkMaterialNo);
        if (returnBulkMaterialDO == null) {
            result.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS);
            return result;
        }
        // 如果物料存在设备号，证明是由设备上拆下，设备升配
        if (StringUtil.isNotBlank(returnBulkMaterialDO.getCurrentEquipmentNo())) {
            OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), returnBulkMaterialDO.getCurrentEquipmentNo());
            if (orderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_EXISTS);
                return result;
            }
            OrderProductDO orderProductDO = orderProductMapper.findById(orderProductEquipmentDO.getOrderProductId());
            if (StringUtil.isNotBlank(changeBulkMaterialNo)) {
                BulkMaterialDO changeBulkMaterialDO = bulkMaterialMapper.findByNo(changeBulkMaterialNo);
                if (changeBulkMaterialDO == null) {
                    result.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS);
                    return result;
                }
                ServiceResult<String, Material> changeMaterialServiceResult = materialService.queryMaterialById(changeBulkMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(changeMaterialServiceResult.getErrorCode())) {
                    result.setErrorCode(changeMaterialServiceResult.getErrorCode());
                    return result;
                }
                BigDecimal changeMaterialBulkUnitAmount = BigDecimal.ZERO;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    changeMaterialBulkUnitAmount = changeMaterialServiceResult.getResult().getDayRentPrice();
                } else if ((OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType()))) {
                    changeMaterialBulkUnitAmount = changeMaterialServiceResult.getResult().getMonthRentPrice();
                }
                ServiceResult<String, Material> returnMaterialServiceResult = materialService.queryMaterialById(returnBulkMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(returnMaterialServiceResult.getErrorCode())) {
                    result.setErrorCode(returnMaterialServiceResult.getErrorCode());
                    return result;
                }
                BigDecimal returnMaterialBulkUnitAmount = BigDecimal.ZERO;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    returnMaterialBulkUnitAmount = returnMaterialServiceResult.getResult().getDayRentPrice();
                } else if ((OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType()))) {
                    returnMaterialBulkUnitAmount = returnMaterialServiceResult.getResult().getMonthRentPrice();
                }
                //  TODO 计算差价 并且把单价提升 暂时不提升
                /*if (BigDecimalUtil.compare(changeMaterialBulkUnitAmount, returnMaterialBulkUnitAmount) > 0) {
                    BigDecimal diffAmount = BigDecimalUtil.sub(changeMaterialBulkUnitAmount, returnMaterialBulkUnitAmount);
                    orderProductEquipmentDO.setProductEquipmentUnitAmount(BigDecimalUtil.add(orderProductEquipmentDO.getProductEquipmentUnitAmount(), diffAmount));
                    orderProductEquipmentDO.setUpdateTime(currentTime);
                    orderProductEquipmentMapper.update(orderProductEquipmentDO);
                }*/

            }
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }

        // 以下为换不在设备上的物料逻辑
        OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderIdAndBulkMaterialNo(orderDO.getId(), returnNBulkMaterialNo);
        if (orderMaterialBulkDO == null) {
            result.setErrorCode(ErrorCode.ORDER_MATERIAL_BULK_NOT_EXISTS);
            return result;
        }
        if (orderMaterialBulkDO.getActualReturnTime() != null) {
            result.setErrorCode(ErrorCode.ORDER_MATERIAL_BULK_ALREADY_RETURN);
            return result;
        }
        OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(orderMaterialBulkDO.getOrderMaterialId());
        // 如果是换货，产生新的记录
        if (StringUtil.isNotBlank(changeBulkMaterialNo)) {
            BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(changeBulkMaterialNo);
            if (bulkMaterialDO == null) {
                result.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_EXISTS);
                return result;
            }
            ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(bulkMaterialDO.getMaterialId());
            if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                result.setErrorCode(materialServiceResult.getErrorCode());
                return result;
            }
            OrderMaterialBulkDO newOrderMaterialBulkDO = new OrderMaterialBulkDO();
            newOrderMaterialBulkDO.setOrderId(orderMaterialBulkDO.getOrderId());
            newOrderMaterialBulkDO.setOrderMaterialId(orderMaterialBulkDO.getOrderMaterialId());
            newOrderMaterialBulkDO.setBulkMaterialId(bulkMaterialDO.getId());
            newOrderMaterialBulkDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
            newOrderMaterialBulkDO.setRentStartTime(returnDate);
            newOrderMaterialBulkDO.setMaterialBulkUnitAmount(orderMaterialDO.getMaterialUnitAmount());
            newOrderMaterialBulkDO.setExpectReturnTime(orderMaterialBulkDO.getExpectReturnTime());
            BigDecimal materialBulkUnitAmount = orderMaterialDO.getMaterialUnitAmount();
            if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                materialBulkUnitAmount = materialServiceResult.getResult().getDayRentPrice();
            } else if ((OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType()))) {
                materialBulkUnitAmount = materialServiceResult.getResult().getMonthRentPrice();
            }
            newOrderMaterialBulkDO.setExpectRentAmount(amountSupport.calculateRentAmount(returnDate, orderMaterialBulkDO.getExpectReturnTime(), materialBulkUnitAmount));
            newOrderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            newOrderMaterialBulkDO.setCreateTime(currentTime);
            newOrderMaterialBulkDO.setUpdateTime(currentTime);
            newOrderMaterialBulkDO.setCreateUser(loginUser.getUserId().toString());
            newOrderMaterialBulkDO.setUpdateUser(loginUser.getUserId().toString());
            orderMaterialBulkMapper.save(newOrderMaterialBulkDO);
        }

        orderMaterialBulkDO.setActualRentAmount(amountSupport.calculateRentAmount(orderMaterialBulkDO.getRentStartTime(), returnDate, orderMaterialBulkDO.getMaterialBulkUnitAmount()));
        orderMaterialBulkDO.setActualReturnTime(returnDate);
        orderMaterialBulkDO.setUpdateTime(currentTime);
        orderMaterialBulkDO.setUpdateUser(loginUser.getUserId().toString());
        orderMaterialBulkMapper.update(orderMaterialBulkDO);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            Date currentTime = new Date();
            User loginUser = userSupport.getCurrentUser();
            OrderDO orderDO = orderMapper.findByOrderNo(businessNo);
            if (orderDO == null || !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus())) {
                return ErrorCode.BUSINESS_EXCEPTION;
            }
            if (verifyResult) {
                CustomerDO customerDO = customerMapper.findById(orderDO.getBuyerCustomerId());
                // 审核通过时，对当前订单做短租应收额度校验
                String verifyOrderShortRentReceivableResult = verifyOrderShortRentReceivable(customerDO, orderDO);
                if (!ErrorCode.SUCCESS.equals(verifyOrderShortRentReceivableResult)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return verifyOrderShortRentReceivableResult;
                }

                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
                // 只有审批通过的订单才生成结算单
                ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createOrderStatement(orderDO.getOrderNo());
                if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return createStatementOrderResult.getErrorCode();
                }
                orderDO.setFirstNeedPayAmount(createStatementOrderResult.getResult());
                orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId());
                orderDO.setUpdateTime(currentTime);
                orderDO.setUpdateUser(loginUser.getUserId().toString());
                orderMapper.update(orderDO);
                //获取订单详细信息，发送给k3
                Order order = queryOrderByNo(orderDO.getOrderNo()).getResult();
                webServiceHelper.post(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD, PostK3Type.POST_K3_TYPE_ORDER, order, true);
            } else {
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
                // 如果拒绝，则退还授信额度
                if (BigDecimalUtil.compare(orderDO.getTotalCreditDepositAmount(), BigDecimal.ZERO) != 0) {
                    customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), orderDO.getTotalCreditDepositAmount());
                }
                orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), OrderStatus.ORDER_STATUS_REJECT, null, currentTime, loginUser.getUserId());
                orderDO.setUpdateTime(currentTime);
                orderDO.setUpdateUser(loginUser.getUserId().toString());
                orderMapper.update(orderDO);
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("审批订单通知失败： {} {}", businessNo, e.toString());
            return ErrorCode.BUSINESS_EXCEPTION;
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, Order> queryOrderByNo(String orderNo) {
        ServiceResult<String, Order> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        List<OrderTimeAxisDO> orderTimeAxisDOList = orderTimeAxisSupport.getOrderTimeAxis(orderDO.getId());
        orderDO.setOrderTimeAxisDOList(orderTimeAxisDOList);

        Order order = ConverterUtil.convert(orderDO, Order.class);

        ServiceResult<String, StatementOrder> statementOrderResult = statementService.queryStatementOrderDetailByOrderId(order.getOrderNo());
        if (ErrorCode.SUCCESS.equals(statementOrderResult.getErrorCode())) {
            order.setStatementOrder(statementOrderResult.getResult());
        }

        if (orderDO.getFirstNeedPayAmount() == null || BigDecimalUtil.compare(orderDO.getFirstNeedPayAmount(), BigDecimal.ZERO) == 0) {
            ServiceResult<String, BigDecimal> firstNeedPayAmountResult = statementService.calculateOrderFirstNeedPayAmount(order.getOrderNo());
            if (ErrorCode.SUCCESS.equals(firstNeedPayAmountResult.getErrorCode())) {
                order.setFirstNeedPayAmount(firstNeedPayAmountResult.getResult());
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                Product product = FastJsonUtil.toBean(orderProduct.getProductSkuSnapshot(), Product.class);
                if (product != null && CollectionUtil.isNotEmpty(product.getProductSkuList())) {
                    for (ProductSku productSku : product.getProductSkuList()) {
                        if (orderProduct.getProductSkuId().equals(productSku.getSkuId())) {
                            orderProduct.setProductSkuPropertyList(productSku.getProductSkuPropertyList());
                            break;
                        }
                    }
                }
                // 计算首付
                if (order.getStatementOrder() != null && CollectionUtil.isNotEmpty(order.getStatementOrder().getStatementOrderDetailList())) {
                    for (StatementOrderDetail statementOrderDetail : order.getStatementOrder().getStatementOrderDetailList()) {
                        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())
                                && OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetail.getOrderItemType())
                                && statementOrderDetail.getOrderId().equals(orderProduct.getOrderId())
                                && statementOrderDetail.getOrderItemReferId().equals(orderProduct.getOrderProductId())
                                && com.lxzl.erp.common.util.DateUtil.isSameDay(statementOrderDetail.getStatementExpectPayTime(), order.getRentStartTime())) {
                            orderProduct.setFirstNeedPayAmount(BigDecimalUtil.add(orderProduct.getFirstNeedPayAmount(), statementOrderDetail.getStatementDetailAmount()));
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetail.getStatementDetailType())) {
                                orderProduct.setFirstNeedPayRentAmount(BigDecimalUtil.add(orderProduct.getFirstNeedPayRentAmount(), statementOrderDetail.getStatementDetailAmount()));
                            }
                        }
                        orderProduct.setFirstNeedPayAmount(orderProduct.getFirstNeedPayAmount() == null ? BigDecimal.ZERO : orderProduct.getFirstNeedPayAmount());
                        orderProduct.setFirstNeedPayRentAmount(orderProduct.getFirstNeedPayRentAmount() == null ? BigDecimal.ZERO : orderProduct.getFirstNeedPayRentAmount());
                    }
                }

                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProduct.getOrderProductId());
                orderProduct.setOrderProductEquipmentList(ConverterUtil.convertList(orderProductEquipmentDOList, OrderProductEquipment.class));
            }
        }
        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterial.getOrderMaterialId());
                orderMaterial.setOrderMaterialBulkList(ConverterUtil.convertList(orderMaterialBulkDOList, OrderMaterialBulk.class));

                // 计算首付
                if (order.getStatementOrder() != null && CollectionUtil.isNotEmpty(order.getStatementOrder().getStatementOrderDetailList())) {
                    for (StatementOrderDetail statementOrderDetail : order.getStatementOrder().getStatementOrderDetailList()) {
                        if (OrderType.ORDER_TYPE_ORDER.equals(statementOrderDetail.getOrderType())
                                && OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetail.getOrderItemType())
                                && statementOrderDetail.getOrderId().equals(orderMaterial.getOrderId())
                                && statementOrderDetail.getOrderItemReferId().equals(orderMaterial.getOrderMaterialId())
                                && com.lxzl.erp.common.util.DateUtil.isSameDay(statementOrderDetail.getStatementExpectPayTime(), order.getRentStartTime())) {
                            orderMaterial.setFirstNeedPayAmount(BigDecimalUtil.add(orderMaterial.getFirstNeedPayAmount(), statementOrderDetail.getStatementDetailAmount()));
                            if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetail.getStatementDetailType())) {
                                orderMaterial.setFirstNeedPayRentAmount(BigDecimalUtil.add(orderMaterial.getFirstNeedPayRentAmount(), statementOrderDetail.getStatementDetailAmount()));
                            }
                        }
                    }
                    orderMaterial.setFirstNeedPayAmount(orderMaterial.getFirstNeedPayAmount() == null ? BigDecimal.ZERO : orderMaterial.getFirstNeedPayAmount());
                    orderMaterial.setFirstNeedPayRentAmount(orderMaterial.getFirstNeedPayRentAmount() == null ? BigDecimal.ZERO : orderMaterial.getFirstNeedPayRentAmount());
                }
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> cancelOrder(String orderNo) {
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (orderDO.getOrderStatus() == null || !OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (!loginUser.getUserId().toString().equals(orderDO.getCreateUser())) {
            result.setErrorCode(ErrorCode.DATA_NOT_BELONG_TO_YOU);
            return result;
        }
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderMapper.update(orderDO);
        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> forceCancelOrder(String orderNo) {
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (PayStatus.PAY_STATUS_PAID_PART.equals(orderDO.getPayStatus()) || PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())) {
            result.setErrorCode(ErrorCode.ORDER_ALREADY_PAID);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus()) &&
                !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus()) &&
                !OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        //审核中的订单，处理工作流
        if (OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus())) {
            ServiceResult<String, String> cancelWorkFlowResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderDO.getOrderNo());
            if (!ErrorCode.SUCCESS.equals(cancelWorkFlowResult.getErrorCode())) {
                result.setErrorCode(cancelWorkFlowResult.getErrorCode());
                return result;
            }
        }
        //审核中或者待发货订单，处理风控额度及结算单
        if (OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus()) ||
                OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus())) {
            //恢复信用额度
            BigDecimal totalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
            if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) != 0) {
                customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), totalCreditDepositAmount);
            }
            //处理结算单
            //缓存查询到的结算单
            Map<Integer, StatementOrderDO> statementCache = new HashMap<>();
            List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByOrderId(orderDO.getId());
            if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
                for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                    StatementOrderDO statementOrderDO = statementCache.get(statementOrderDetailDO.getStatementOrderId());
                    if (statementOrderDO == null) {
                        statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
                        statementCache.put(statementOrderDO.getId(), statementOrderDO);
                    }
                    //处理结算单总金额
                    statementOrderDO.setStatementAmount(BigDecimalUtil.sub(statementOrderDO.getStatementAmount(), statementOrderDetailDO.getStatementDetailAmount()));
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
                    statementOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                    statementOrderDetailDO.setUpdateTime(currentTime);
                    statementOrderDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    statementOrderDetailMapper.update(statementOrderDetailDO);
                }
                for (Integer key : statementCache.keySet()) {
                    StatementOrderDO statementOrderDO = statementCache.get(key);
                    if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) == 0) {
                        statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                    }
                    statementOrderDO.setUpdateTime(currentTime);
                    statementOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    statementOrderMapper.update(statementOrderDO);
                }
            }
        }

        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderMapper.update(orderDO);
        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId());
        IERPService service = null;
        try {
            K3SendRecordDO k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(orderDO.getId(), PostK3Type.POST_K3_TYPE_CANCEL_ORDER);
            if (k3SendRecordDO == null) {
                //创建推送记录，此时发送状态失败，接收状态失败
                k3SendRecordDO = new K3SendRecordDO();
                k3SendRecordDO.setRecordType(PostK3Type.POST_K3_TYPE_CANCEL_ORDER);
                k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_NO);
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                k3SendRecordDO.setRecordJson(orderDO.getOrderNo());
                k3SendRecordDO.setSendTime(new Date());
                k3SendRecordDO.setRecordReferId(orderDO.getId());
                k3SendRecordMapper.save(k3SendRecordDO);
                logger.info("【推送消息】" + orderDO.getOrderNo());
            }
            service = new ERPServiceLocator().getBasicHttpBinding_IERPService();
            com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response = service.cancelOrder(orderDO.getOrderNo());
            //修改推送记录
            if (response == null || response.getStatus() != 0) {
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： " + JSON.toJSONString(response));
                dingDingSupport.dingDingSendMessage(getErrorMessage(response, k3SendRecordDO));
                //失败要回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            } else {
                logger.info("【PUSH DATA TO K3 RESPONSE SUCCESS】 ： " + JSON.toJSONString(response));
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_YES);
            }
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_YES);
            k3SendRecordDO.setResponseJson(JSON.toJSONString(response));
            k3SendRecordMapper.update(k3SendRecordDO);
            logger.info("【返回结果】" + response);

        } catch (ServiceException e) {
            throw new BusinessException("k3取消订单失败:", e.getMessage());
        } catch (RemoteException e) {
            throw new BusinessException("k3取消订单失败:", e.getMessage());
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    private String getErrorMessage(com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult response, K3SendRecordDO k3SendRecordDO) {
        String type = null;
        if ("erp-prod".equals(ApplicationConfig.application)) {
            type = "【线上环境】";
        } else if ("erp-dev".equals(ApplicationConfig.application)) {
            type = "【开发环境】";
        } else if ("erp-adv".equals(ApplicationConfig.application)) {
            type = "【预发环境】";
        } else if ("erp-test".equals(ApplicationConfig.application)) {
            type = "【测试环境】";
        }
        StringBuffer sb = new StringBuffer(type);
        sb.append("向K3推送【取消订单-").append(k3SendRecordDO.getRecordReferId()).append("】数据失败：");
        sb.append(JSON.toJSONString(response));
        return sb.toString();
    }

    @Override
    public ServiceResult<String, Page<Order>> queryAllOrder(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(orderQueryParam.getPageNo(), orderQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("orderQueryParam", orderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

        Integer totalCount = orderMapper.findOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);
        List<Order> orderList = ConverterUtil.convertList(orderDOList, Order.class);
        Page<Order> page = new Page<>(orderList, totalCount, orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Order>> queryOrderByUserId(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        PageQuery pageQuery = new PageQuery(orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        orderQueryParam.setBuyerCustomerId(loginUser.getUserId());
        maps.put("orderQueryParam", orderQueryParam);

        Integer totalCount = orderMapper.findOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);
        Page<Order> page = new Page<>(ConverterUtil.convertList(orderDOList, Order.class), totalCount, orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Order>> queryOrderByUserIdInterface(InterfaceOrderQueryParam interfaceOrderQueryParam) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(interfaceOrderQueryParam.getPageNo(), interfaceOrderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        interfaceOrderQueryParam.setBuyerCustomerId(interfaceOrderQueryParam.getBuyerCustomerId());
        maps.put("orderQueryParam", interfaceOrderQueryParam);

        Integer totalCount = orderMapper.findOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);
        Page<Order> page = new Page<>(ConverterUtil.convertList(orderDOList, Order.class), totalCount, interfaceOrderQueryParam.getPageNo(), interfaceOrderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> processOrder(ProcessOrderParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        if (param == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        if (StringUtil.isBlank(param.getEquipmentNo())
                && (param.getMaterialId() == null || param.getMaterialCount() == null)) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        OrderDO orderDO = orderMapper.findByOrderNo(param.getOrderNo());
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus())
                && !OrderStatus.ORDER_STATUS_PROCESSING.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (!CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())
                && !CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }

        if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())) {
            ServiceResult<String, Object> addOrderItemResult = addOrderItem(orderDO, param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), param.getIsNewMaterial(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(addOrderItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(addOrderItemResult.getErrorCode(), addOrderItemResult.getFormatArgs());
                return result;
            }
        } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            ServiceResult<String, Object> removeOrderItemResult = removeOrderItem(orderDO, param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), param.getIsNewMaterial(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(removeOrderItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(removeOrderItemResult.getErrorCode(), removeOrderItemResult.getFormatArgs());
                return result;
            }
        }

        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_PROCESSING);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);


        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId());

        result.setResult(param.getOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private ServiceResult<String, Object> addOrderItem(OrderDO orderDO, String equipmentNo, Integer materialId, Integer materialCount, Integer isNewMaterial, Integer loginUserId, Date currentTime) {
        ServiceResult<String, Object> result = new ServiceResult<>();
        // 计算预计归还时间
        if (equipmentNo != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS, equipmentNo);
                return result;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE, equipmentNo);
                return result;
            }
            WarehouseDO currentWarehouse = warehouseSupport.getSubCompanyWarehouse(orderDO.getOrderSubCompanyId());
            if (currentWarehouse == null) {
                result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                return result;
            }
            currentWarehouse = warehouseSupport.getAvailableWarehouse(currentWarehouse.getId());
            if (currentWarehouse == null) {
                result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
                return result;
            }
            if (!productEquipmentDO.getCurrentWarehouseId().equals(currentWarehouse.getId())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE, equipmentNo, productEquipmentDO.getCurrentWarehouseId());
                return result;
            }

            boolean isMatching = false;
            Map<String, OrderProductDO> orderProductDOMap = ListUtil.listToMap(orderDO.getOrderProductDOList(), "productSkuId", "rentType", "rentTimeLength", "isNewProduct");
            OrderProductDO matchingOrderProductDO = null;

            // 匹配SKU
            for (Map.Entry<String, OrderProductDO> entry : orderProductDOMap.entrySet()) {
                String key = entry.getKey();
                OrderProductDO orderProductDO = entry.getValue();
                // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
                if (key.startsWith(productEquipmentDO.getSkuId().toString()) && productEquipmentDO.getIsNew().equals(orderProductDO.getIsNewProduct())) {
                    matchingOrderProductDO = orderProductDO;
                    break;
                }
            }
            // 匹配商品
            if (matchingOrderProductDO == null) {
                for (Map.Entry<String, OrderProductDO> entry : orderProductDOMap.entrySet()) {
                    OrderProductDO orderProductDO = entry.getValue();
                    // 如果输入进来的设备productId,订单项中包含，就匹配 为当前订单项需要的，那么就匹配
                    if (orderProductDO.getProductId().equals(productEquipmentDO.getProductId()) && productEquipmentDO.getIsNew().equals(orderProductDO.getIsNewProduct())) {
                        matchingOrderProductDO = orderProductDO;
                        break;
                    }
                }
            }
            if (matchingOrderProductDO != null) {
                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(matchingOrderProductDO.getId());
                // 如果这个订单项满了，那么就换下一个
                if (orderProductEquipmentDOList != null && orderProductEquipmentDOList.size() >= matchingOrderProductDO.getProductCount()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_MAX);
                    return result;
                }
                if (!productEquipmentDO.getIsNew().equals(matchingOrderProductDO.getIsNewProduct())) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NEW_NOT_MATCHING);
                    return result;
                }

                productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
                productEquipmentDO.setOrderNo(orderDO.getOrderNo());
                productEquipmentDO.setUpdateTime(currentTime);
                productEquipmentDO.setUpdateUser(loginUserId.toString());
                productEquipmentMapper.update(productEquipmentDO);

                // 将该设备上的物料统一加上订单号
                bulkMaterialMapper.updateEquipmentOrderNo(productEquipmentDO.getEquipmentNo(), orderDO.getOrderNo());

                BigDecimal expectRentAmount = calculationOrderExpectRentAmount(matchingOrderProductDO.getProductUnitAmount(), matchingOrderProductDO.getRentType(), matchingOrderProductDO.getRentTimeLength());
                Date expectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), matchingOrderProductDO.getRentType(), matchingOrderProductDO.getRentTimeLength());
                OrderProductEquipmentDO orderProductEquipmentDO = new OrderProductEquipmentDO();
                orderProductEquipmentDO.setOrderId(matchingOrderProductDO.getOrderId());
                orderProductEquipmentDO.setOrderProductId(matchingOrderProductDO.getId());
                orderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
                orderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
                orderProductEquipmentDO.setRentStartTime(orderDO.getRentStartTime());
                orderProductEquipmentDO.setProductEquipmentUnitAmount(matchingOrderProductDO.getProductUnitAmount());
                orderProductEquipmentDO.setExpectReturnTime(expectReturnTime);
                orderProductEquipmentDO.setExpectRentAmount(expectRentAmount);
                orderProductEquipmentDO.setActualRentAmount(BigDecimal.ZERO);
                orderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductEquipmentDO.setCreateTime(currentTime);
                orderProductEquipmentDO.setCreateUser(loginUserId.toString());
                orderProductEquipmentDO.setUpdateTime(currentTime);
                orderProductEquipmentDO.setUpdateUser(loginUserId.toString());
                orderProductEquipmentMapper.save(orderProductEquipmentDO);
                isMatching = true;
            }

            if (!isMatching) {
                result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                return result;
            }

            String operateSkuStockResult = productSupport.operateSkuStock(productEquipmentDO.getSkuId(), -1);
            if (!ErrorCode.SUCCESS.equals(operateSkuStockResult)) {
                result.setErrorCode(operateSkuStockResult);
                return result;
            }
        }

        if (materialId != null) {
            boolean isMatching = false;
            Map<String, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "materialId", "rentType", "rentTimeLength", "isNewMaterial");
            for (Map.Entry<String, OrderMaterialDO> entry : orderMaterialDOMap.entrySet()) {
                String key = entry.getKey();
                OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(key);
                // 如果输入进来的散料ID 为当前订单项需要的，那么就匹配
                if (key.startsWith(materialId.toString()) && orderMaterialDO.getIsNewMaterial().equals(isNewMaterial)) {
                    //已经配好的
                    List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterialDO.getId());
                    if (orderMaterialBulkDOList != null && orderMaterialBulkDOList.size() >= orderMaterialDO.getMaterialCount()) {
                        continue;
                    }

                    WarehouseDO currentWarehouse = warehouseSupport.getSubCompanyWarehouse(orderDO.getOrderSubCompanyId());
                    if (currentWarehouse == null) {
                        result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
                        return result;
                    }
                    currentWarehouse = warehouseSupport.getAvailableWarehouse(currentWarehouse.getId());
                    if (currentWarehouse == null) {
                        result.setErrorCode(ErrorCode.WAREHOUSE_NOT_AVAILABLE);
                        return result;
                    }

                    // 必须是当前库房闲置的物料
                    List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialSupport.queryFitBulkMaterialDOList(currentWarehouse.getId(), materialId, materialCount, orderMaterialDO.getIsNewMaterial());
                    if (CollectionUtil.isEmpty(bulkMaterialDOList) || bulkMaterialDOList.size() < materialCount) {
                        result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                        return result;
                    }

                    for (BulkMaterialDO bulkMaterialDO : bulkMaterialDOList) {
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
                        bulkMaterialDO.setOrderNo(orderDO.getOrderNo());
                        bulkMaterialDO.setUpdateTime(currentTime);
                        bulkMaterialDO.setUpdateUser(loginUserId.toString());
                        bulkMaterialMapper.update(bulkMaterialDO);

                        BigDecimal expectRentAmount = calculationOrderExpectRentAmount(orderMaterialDO.getMaterialUnitAmount(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength());
                        Date expectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength());
                        OrderMaterialBulkDO orderMaterialBulkDO = new OrderMaterialBulkDO();
                        orderMaterialBulkDO.setOrderId(orderMaterialDO.getOrderId());
                        orderMaterialBulkDO.setOrderMaterialId(orderMaterialDO.getId());
                        orderMaterialBulkDO.setBulkMaterialId(bulkMaterialDO.getId());
                        orderMaterialBulkDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                        orderMaterialBulkDO.setMaterialBulkUnitAmount(orderMaterialDO.getMaterialUnitAmount());
                        orderMaterialBulkDO.setRentStartTime(orderDO.getRentStartTime());
                        orderMaterialBulkDO.setExpectReturnTime(expectReturnTime);
                        orderMaterialBulkDO.setExpectRentAmount(expectRentAmount);
                        orderMaterialBulkDO.setActualRentAmount(BigDecimal.ZERO);
                        orderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        orderMaterialBulkDO.setCreateTime(currentTime);
                        orderMaterialBulkDO.setCreateUser(loginUserId.toString());
                        orderMaterialBulkDO.setUpdateTime(currentTime);
                        orderMaterialBulkDO.setUpdateUser(loginUserId.toString());
                        orderMaterialBulkMapper.save(orderMaterialBulkDO);
                    }
                    isMatching = true;
                    break;
                }
            }
            if (!isMatching) {
                result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, materialId);
                return result;
            }

            String operateMaterialStockResult = materialSupport.operateMaterialStock(materialId, (materialCount * -1));
            if (!ErrorCode.SUCCESS.equals(operateMaterialStockResult)) {
                result.setErrorCode(operateMaterialStockResult);
                return result;
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private ServiceResult<String, Object> removeOrderItem(OrderDO orderDO, String equipmentNo, Integer materialId, Integer materialCount, Integer isNewMaterial, Integer loginUserId, Date currentTime) {
        ServiceResult<String, Object> result = new ServiceResult<>();
        if (equipmentNo != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
            if (productEquipmentDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS, equipmentNo);
                return result;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY.equals(productEquipmentDO.getEquipmentStatus())) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_BUSY, equipmentNo);
                return result;
            }
            OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), productEquipmentDO.getEquipmentNo());
            if (orderProductEquipmentDO == null) {
                result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                return result;
            }

            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE);
            productEquipmentDO.setOrderNo("");
            productEquipmentDO.setUpdateTime(currentTime);
            productEquipmentDO.setUpdateUser(loginUserId.toString());
            productEquipmentMapper.update(productEquipmentDO);

            bulkMaterialMapper.updateEquipmentOrderNo(productEquipmentDO.getEquipmentNo(), "");

            orderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            orderProductEquipmentDO.setUpdateTime(currentTime);
            orderProductEquipmentDO.setUpdateUser(loginUserId.toString());
            orderProductEquipmentMapper.update(orderProductEquipmentDO);

            String operateSkuStockResult = productSupport.operateSkuStock(productEquipmentDO.getSkuId(), 1);
            if (!ErrorCode.SUCCESS.equals(operateSkuStockResult)) {
                result.setErrorCode(operateSkuStockResult);
                return result;
            }
        }

        if (materialId != null) {
            Map<String, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "materialId", "rentType", "rentTimeLength", "isNewMaterial");
            for (Map.Entry<String, OrderMaterialDO> entry : orderMaterialDOMap.entrySet()) {
                String key = entry.getKey();
                OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(key);
                // 如果输入进来的散料ID 为当前订单项需要的，那么就匹配
                if (key.startsWith(materialId.toString()) && orderMaterialDO.getIsNewMaterial().equals(isNewMaterial)) {
                    List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterialDO.getId());
                    if (CollectionUtil.isEmpty(orderMaterialBulkDOList)) {
                        result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, materialId);
                        return result;
                    }

                    if (orderMaterialBulkDOList.size() < materialCount) {
                        result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                        return result;
                    }
                    for (int i = 0; i < materialCount; i++) {
                        OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkDOList.get(i);
                        BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(orderMaterialBulkDO.getBulkMaterialNo());
                        bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);
                        bulkMaterialDO.setOrderNo("");
                        bulkMaterialDO.setUpdateTime(currentTime);
                        bulkMaterialDO.setUpdateUser(loginUserId.toString());
                        bulkMaterialMapper.update(bulkMaterialDO);

                        orderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                        orderMaterialBulkDO.setUpdateTime(currentTime);
                        orderMaterialBulkDO.setUpdateUser(loginUserId.toString());
                        orderMaterialBulkMapper.update(orderMaterialBulkDO);
                    }

                    String operateMaterialStockResult = materialSupport.operateMaterialStock(materialId, materialCount);
                    if (!ErrorCode.SUCCESS.equals(operateMaterialStockResult)) {
                        result.setErrorCode(operateMaterialStockResult);
                        return result;
                    }
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> deliveryOrder(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();
        OrderDO dbRecordOrder = orderMapper.findByOrderNo(order.getOrderNo());
        if (dbRecordOrder == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_PROCESSING.equals(dbRecordOrder.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_NOT_PROCESSED);
            return result;
        }
        Integer payType = null;
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : dbRecordOrder.getOrderProductDOList()) {
                if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProductDO.getPayMode())) {
                    payType = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    break;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderMaterialDOList()) && !OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payType)) {
            for (OrderMaterialDO orderMaterialDO : dbRecordOrder.getOrderMaterialDOList()) {
                if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderMaterialDO.getPayMode())) {
                    payType = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    break;
                }
            }
        }
        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payType) &&
                !PayStatus.PAY_STATUS_PAID.equals(dbRecordOrder.getPayStatus())
                && !PayStatus.PAY_STATUS_PAID_PART.equals(dbRecordOrder.getPayStatus())) {
            result.setErrorCode(ErrorCode.ORDER_HAVE_NO_PAID);
            return result;
        }
        if (BigDecimalUtil.compare(dbRecordOrder.getFirstNeedPayAmount(), BigDecimal.ZERO) > 0 &&
                !PayStatus.PAY_STATUS_PAID.equals(dbRecordOrder.getPayStatus())) {
            result.setErrorCode(ErrorCode.ORDER_HAVE_NO_PAID);
            return result;
        }

        if (DateUtil.getBeginOfDay(currentTime).getTime() < DateUtil.dateInterval(DateUtil.getBeginOfDay(dbRecordOrder.getRentStartTime()), -2).getTime()) {
            result.setErrorCode(ErrorCode.ORDER_CAN_NOT_DELIVERY_TIME_REASON);
            return result;
        }
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : dbRecordOrder.getOrderProductDOList()) {
                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProductDO.getId());
                if (orderProductEquipmentDOList == null || orderProductDO.getProductCount() != orderProductEquipmentDOList.size()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR);
                    return result;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : dbRecordOrder.getOrderMaterialDOList()) {
                List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterialDO.getId());
                if (orderMaterialBulkDOList == null || orderMaterialDO.getMaterialCount() != orderMaterialBulkDOList.size()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_BULK_MATERIAL_COUNT_ERROR);
                    return result;
                }
            }
        }

        dbRecordOrder.setDeliveryTime(currentTime);
        dbRecordOrder.setOrderStatus(OrderStatus.ORDER_STATUS_DELIVERED);
        dbRecordOrder.setUpdateUser(loginUser.getUserId().toString());
        dbRecordOrder.setUpdateTime(currentTime);
        orderMapper.update(dbRecordOrder);

        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(dbRecordOrder.getId(), dbRecordOrder.getOrderStatus(), null, currentTime, loginUser.getUserId());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order.getOrderNo());
        return result;
    }

    /**
     * 计算订单预计归还时间
     */
    private Date calculationOrderExpectReturnTime(Date rentStartTime, Integer rentType, Integer rentTimeLength) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return DateUtil.dateInterval(rentStartTime, rentTimeLength - 1);
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return DateUtil.dateInterval(DateUtil.monthInterval(rentStartTime, rentTimeLength), -1);
        }
        return null;
    }

    private BigDecimal calculationOrderExpectRentAmount(BigDecimal unitAmount, Integer rentType, Integer rentTimeLength) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return BigDecimalUtil.mul(unitAmount, new BigDecimal(rentTimeLength));
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return BigDecimalUtil.mul(unitAmount, new BigDecimal(rentTimeLength));
        }
        return null;
    }

    public void verifyCustomerRiskInfo(OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        boolean isCheckRiskManagement = isCheckRiskManagement(orderDO);
        if (isCheckRiskManagement) {
            if (customerRiskManagementDO == null) {
                throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())
                        && orderProductDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }

                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                    if (!productServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                        throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    }
                    Product product = productServiceResult.getResult();
                    ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);

                    if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(skuPrice, customerRiskManagementDO.getSingleLimitPrice()) > 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }

                    Integer depositCycle, payCycle, payMode;
                    boolean productIsCheckRiskManagement = isCheckRiskManagement(orderProductDO, null);
                    if (!productIsCheckRiskManagement) {
                        if (orderProductDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        // 如果不走风控，就押0付0
                        orderProductDO.setDepositCycle(0);
                        orderProductDO.setPaymentCycle(0);
                        continue;
                    }
                    // 判断用户是否为全额押金用户，如果为全额押金用户，即全额押金，并且押0付1
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                        depositCycle = 0;
                        payCycle = 1;
                        payMode = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    } else if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) {
                        // 商品品牌为苹果品牌
                        depositCycle = customerRiskManagementDO.getAppleDepositCycle();
                        payCycle = customerRiskManagementDO.getApplePaymentCycle();
                        payMode = customerRiskManagementDO.getApplePayMode();
                    } else if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                        depositCycle = customerRiskManagementDO.getNewDepositCycle();
                        payCycle = customerRiskManagementDO.getNewPaymentCycle();
                        payMode = customerRiskManagementDO.getNewPayMode();
                    } else {
                        depositCycle = customerRiskManagementDO.getDepositCycle();
                        payCycle = customerRiskManagementDO.getPaymentCycle();
                        payMode = customerRiskManagementDO.getPayMode();
                    }
                    if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                        payCycle = payCycle > orderProductDO.getRentTimeLength() ? orderProductDO.getRentTimeLength() : payCycle;
                    }
                    orderProductDO.setDepositCycle(depositCycle);
                    orderProductDO.setPaymentCycle(payCycle);
                    orderProductDO.setPayMode(payMode);
                } else {
                    if (orderProductDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())
                        && orderMaterialDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    throw new BusinessException(ErrorCode.ORDER_RENT_LENGTH_MORE_THAN_90);
                }

                // 如果走风控
                if (isCheckRiskManagement) {
                    ServiceResult<String, Material> materialResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                    Material material = materialResult.getResult();
                    if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitApple())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_APPLE_LIMIT);
                    }
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) && CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsLimitNew())) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NEW_LIMIT);
                    }
                    BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                    if (customerRiskManagementDO.getSingleLimitPrice() != null && BigDecimalUtil.compare(materialPrice, customerRiskManagementDO.getSingleLimitPrice()) >= 0) {
                        throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_PRICE_LIMIT);
                    }
                    boolean materialIsCheckRiskManagement = isCheckRiskManagement(null, orderMaterialDO);
                    if (!materialIsCheckRiskManagement) {
                        if (orderMaterialDO.getPayMode() == null) {
                            throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                        }
                        // 如果不走风控，就押0付0
                        orderMaterialDO.setDepositCycle(0);
                        orderMaterialDO.setPaymentCycle(0);
                        continue;
                    }
                    Integer depositCycle, payCycle, payMode;
                    // 判断用户是否为全额押金用户，如果为全额押金用户，即全额押金，并且押0付1
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                        depositCycle = 0;
                        payCycle = 1;
                        payMode = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    } else if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId())) {
                        // 商品品牌为苹果品牌
                        depositCycle = customerRiskManagementDO.getAppleDepositCycle();
                        payCycle = customerRiskManagementDO.getApplePaymentCycle();
                        payMode = customerRiskManagementDO.getApplePayMode();
                    } else if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                        depositCycle = customerRiskManagementDO.getNewDepositCycle();
                        payCycle = customerRiskManagementDO.getNewPaymentCycle();
                        payMode = customerRiskManagementDO.getNewPayMode();
                    } else {
                        depositCycle = customerRiskManagementDO.getDepositCycle();
                        payCycle = customerRiskManagementDO.getPaymentCycle();
                        payMode = customerRiskManagementDO.getPayMode();
                    }
                    if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                        depositCycle = depositCycle > orderMaterialDO.getRentTimeLength() ? orderMaterialDO.getRentTimeLength() : depositCycle;
                        payCycle = payCycle > orderMaterialDO.getRentTimeLength() ? orderMaterialDO.getRentTimeLength() : payCycle;
                    }
                    orderMaterialDO.setDepositCycle(depositCycle);
                    orderMaterialDO.setPaymentCycle(payCycle);
                    orderMaterialDO.setPayMode(payMode);
                } else {
                    if (orderMaterialDO.getPayMode() == null) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_NOT_NULL);
                    }
                }
            }
        }
    }

    private boolean isCheckRiskManagement(OrderProductDO orderProductDO, OrderMaterialDO orderMaterialDO) {
        if (orderProductDO != null) {
            if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())
                    && orderProductDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                return true;
            }
            if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                return true;
            }
        }
        if (orderMaterialDO != null) {
            if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())
                    && orderMaterialDO.getRentTimeLength() >= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                return true;
            }
            if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                return true;
            }
        }
        return false;
    }

    private boolean isCheckRiskManagement(OrderDO orderDO) {

        BigDecimal totalProductAmount = BigDecimal.ZERO;
        BigDecimal totalMaterialAmount = BigDecimal.ZERO;
        Integer totalProductCount = 0;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                boolean isCheckRiskManagement = isCheckRiskManagement(orderProductDO, null);
                if (isCheckRiskManagement) {
                    return true;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                ProductSku productSku = productServiceResult.getResult().getProductSkuList().get(0);
                BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                totalProductAmount = BigDecimalUtil.add(totalProductAmount, BigDecimalUtil.mul(new BigDecimal(orderProductDO.getProductCount()), skuPrice));
                totalProductCount += orderProductDO.getProductCount();
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {

                boolean isCheckRiskManagement = isCheckRiskManagement(null, orderMaterialDO);
                if (isCheckRiskManagement) {
                    return true;
                }
                ServiceResult<String, Material> materialResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                Material material = materialResult.getResult();
                BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                totalMaterialAmount = BigDecimalUtil.add(totalMaterialAmount, BigDecimalUtil.mul(new BigDecimal(orderMaterialDO.getMaterialCount()), materialPrice));
            }
        }
        BigDecimal totalAmount = BigDecimalUtil.add(totalProductAmount, totalMaterialAmount);
        if (totalProductCount >= CommonConstant.ORDER_NEED_VERIFY_PRODUCT_COUNT || BigDecimalUtil.compare(totalAmount, CommonConstant.ORDER_NEED_VERIFY_PRODUCT_AMOUNT) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public ServiceResult<String, String> confirmOrder(String orderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();

        Date currentTime = new Date();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (orderDO == null) {
            logger.info("未查询到交易订单{}相关信息", orderNo);
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        // 判断订单状态，如果状态已经终结，就不用再继续了
        Integer orderState = orderDO.getOrderStatus();
        if (orderState == null || !OrderStatus.ORDER_STATUS_DELIVERED.equals(orderState)) {
            logger.error("交易订单{}状态为{}，不能确认收货", orderNo, orderState);
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }

        orderDO.setConfirmDeliveryTime(currentTime);
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CONFIRM);
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderMapper.update(orderDO);

        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, loginUser.getUserId());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    public ServiceResult<String, Page<OrderProduct>> queryOrderProductInfo(OrderQueryProductParam queryProductParam) {
        ServiceResult<String, Page<OrderProduct>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(queryProductParam.getPageNo(), queryProductParam.getPageSize());
        //根据orderNo获取orderId
        if (StringUtil.isNotEmpty(queryProductParam.getOrderNo())) {
            OrderDO orderDO = orderMapper.findByOrderNo(queryProductParam.getOrderNo());
            if (orderDO == null) {
                result.setErrorCode(ErrorCode.SUCCESS);
                result.setResult(new Page<OrderProduct>());
                return result;
            } else {
                queryProductParam.setOrderId(orderDO.getId());
            }
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("queryProductParam", queryProductParam);

        Integer totalCount = orderProductMapper.findOrderProductCountByParams(maps);
        List<OrderProductDO> orderProductDOList = orderProductMapper.findOrderProductByParams(maps);
        List<OrderProduct> productSkuList = ConverterUtil.convertList(orderProductDOList, OrderProduct.class);
        for (OrderProduct orderProduct : productSkuList) {
            Product product = FastJsonUtil.toBean(orderProduct.getProductSkuSnapshot(), Product.class);
            for (ProductSku productSku : product.getProductSkuList()) {
                if (orderProduct.getProductSkuId().equals(productSku.getSkuId())) {
                    orderProduct.setProductSkuPropertyList(productSku.getProductSkuPropertyList());
                    break;
                }
            }
        }
        Page<OrderProduct> page = new Page<>(productSkuList, totalCount, queryProductParam.getPageNo(), queryProductParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    private void saveOrderProductInfo(List<OrderProductDO> orderProductDOList, Integer orderId, User loginUser, Date currentTime) {

        Map<String, OrderProductDO> saveOrderProductDOMap = new HashMap<>();
        Map<String, OrderProductDO> updateOrderProductDOMap = new HashMap<>();
        List<OrderProductDO> dbOrderProductDOList = orderProductMapper.findByOrderId(orderId);
        Map<String, OrderProductDO> dbOrderProductDOMap = ListUtil.listToMap(dbOrderProductDOList, "productSkuId", "rentType", "rentTimeLength", "isNewProduct");
        if (CollectionUtil.isNotEmpty(orderProductDOList)) {
            for (OrderProductDO orderProductDO : orderProductDOList) {
                // "productSkuId", "rentType", "rentTimeLength", "isNewProduct"
                String productRecordKey = orderProductDO.getProductSkuId() + "-" + orderProductDO.getRentType() + "-" + orderProductDO.getRentTimeLength() + "-" + orderProductDO.getIsNewProduct();
                OrderProductDO dbOrderProductDO = dbOrderProductDOMap.get(productRecordKey);
                if (dbOrderProductDO != null) {
                    orderProductDO.setId(dbOrderProductDO.getId());
                    updateOrderProductDOMap.put(productRecordKey, orderProductDO);
                    dbOrderProductDOMap.remove(productRecordKey);
                } else {
                    saveOrderProductDOMap.put(productRecordKey, orderProductDO);
                }
            }
        }

        if (saveOrderProductDOMap.size() > 0) {
            for (Map.Entry<String, OrderProductDO> entry : saveOrderProductDOMap.entrySet()) {
                OrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setOrderId(orderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setCreateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setCreateTime(currentTime);
                orderProductDO.setUpdateTime(currentTime);
                orderProductMapper.save(orderProductDO);
            }
        }

        if (updateOrderProductDOMap.size() > 0) {
            for (Map.Entry<String, OrderProductDO> entry : updateOrderProductDOMap.entrySet()) {
                OrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setOrderId(orderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                orderProductMapper.update(orderProductDO);
            }
        }

        if (dbOrderProductDOMap.size() > 0) {
            for (Map.Entry<String, OrderProductDO> entry : dbOrderProductDOMap.entrySet()) {
                OrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setOrderId(orderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                orderProductMapper.update(orderProductDO);
            }
        }
    }

    private void saveOrderMaterialInfo(List<OrderMaterialDO> orderMaterialDOList, Integer orderId, User loginUser, Date currentTime) {

        Map<String, OrderMaterialDO> saveOrderMaterialDOMap = new HashMap<>();
        Map<String, OrderMaterialDO> updateOrderMaterialDOMap = new HashMap<>();
        List<OrderMaterialDO> dbOrderMaterialDOList = orderMaterialMapper.findByOrderId(orderId);
        Map<String, OrderMaterialDO> dbOrderMaterialDOMap = ListUtil.listToMap(dbOrderMaterialDOList, "materialId", "rentType", "rentTimeLength", "isNewMaterial");
        if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                String materialRecordKey = orderMaterialDO.getMaterialId() + "-" + orderMaterialDO.getRentType() + "-" + orderMaterialDO.getRentTimeLength() + "-" + orderMaterialDO.getIsNewMaterial();
                OrderMaterialDO dbOrderMaterialDO = dbOrderMaterialDOMap.get(materialRecordKey);
                if (dbOrderMaterialDO != null) {
                    orderMaterialDO.setId(dbOrderMaterialDO.getId());
                    updateOrderMaterialDOMap.put(materialRecordKey, orderMaterialDO);
                    dbOrderMaterialDOMap.remove(materialRecordKey);
                } else {
                    saveOrderMaterialDOMap.put(materialRecordKey, orderMaterialDO);
                }
            }
        }

        if (saveOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<String, OrderMaterialDO> entry : saveOrderMaterialDOMap.entrySet()) {
                OrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setCreateTime(currentTime);
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialMapper.save(orderMaterialDO);
            }
        }

        if (updateOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<String, OrderMaterialDO> entry : updateOrderMaterialDOMap.entrySet()) {
                OrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialMapper.update(orderMaterialDO);
            }
        }

        if (dbOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<String, OrderMaterialDO> entry : dbOrderMaterialDOMap.entrySet()) {
                OrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialMapper.update(orderMaterialDO);
            }
        }
    }

    Date generateExpectReturnTime(OrderDO orderDO) {
        Date expectReturnTime = null;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }
        return expectReturnTime;
    }

    private void updateOrderConsignInfo(Integer userConsignId, Integer orderId, User loginUser, Date currentTime) {
        CustomerConsignInfoDO userConsignInfoDO = customerConsignInfoMapper.findById(userConsignId);
        OrderConsignInfoDO dbOrderConsignInfoDO = orderConsignInfoMapper.findByOrderId(orderId);
        OrderConsignInfoDO orderConsignInfoDO = new OrderConsignInfoDO();
        orderConsignInfoDO.setOrderId(orderId);
        orderConsignInfoDO.setCustomerConsignId(userConsignId);
        orderConsignInfoDO.setConsigneeName(userConsignInfoDO.getConsigneeName());
        orderConsignInfoDO.setConsigneePhone(userConsignInfoDO.getConsigneePhone());
        orderConsignInfoDO.setProvince(userConsignInfoDO.getProvince());
        orderConsignInfoDO.setCity(userConsignInfoDO.getCity());
        orderConsignInfoDO.setDistrict(userConsignInfoDO.getDistrict());
        orderConsignInfoDO.setAddress(userConsignInfoDO.getAddress());
        orderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);

        if (dbOrderConsignInfoDO == null) {
            orderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
            orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            orderConsignInfoDO.setCreateTime(currentTime);
            orderConsignInfoDO.setUpdateTime(currentTime);
            orderConsignInfoMapper.save(orderConsignInfoDO);
        } else {
            if (!dbOrderConsignInfoDO.getCustomerConsignId().equals(userConsignId)) {
                dbOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                dbOrderConsignInfoDO.setId(dbOrderConsignInfoDO.getId());
                dbOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                dbOrderConsignInfoDO.setUpdateTime(currentTime);
                orderConsignInfoMapper.update(dbOrderConsignInfoDO);

                orderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
                orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                orderConsignInfoDO.setCreateTime(currentTime);
                orderConsignInfoDO.setUpdateTime(currentTime);
                orderConsignInfoMapper.save(orderConsignInfoDO);
            }
        }
    }

    private void calculateOrderProductInfo(List<OrderProductDO> orderProductDOList, OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        if (orderProductDOList != null && !orderProductDOList.isEmpty()) {
            int productCount = 0;
            // 商品租赁总额
            BigDecimal productAmountTotal = new BigDecimal(0.0);
            // 保险总额
            BigDecimal totalInsuranceAmount = new BigDecimal(0.0);
            // 设备该交押金总额
            BigDecimal totalDepositAmount = new BigDecimal(0.0);
            // 设备信用押金总额
            BigDecimal totalCreditDepositAmount = new BigDecimal(0.0);
            // 租赁押金总额
            BigDecimal totalRentDepositAmount = new BigDecimal(0.0);
            for (OrderProductDO orderProductDO : orderProductDOList) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                orderProductDO.setProductName(product.getProductName());
                ProductSku productSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (productSku == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;

                BigDecimal skuPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSku.getNewSkuPrice() : productSku.getSkuPrice();
                // 小于等于90天的,不走风控，大于90天的，走风控授信
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType()) && orderProductDO.getRentTimeLength() <= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    BigDecimal remainder = orderProductDO.getDepositAmount().divideAndRemainder(new BigDecimal(orderProductDO.getProductCount()))[1];
                    if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                        throw new BusinessException(ErrorCode.ORDER_PRODUCT_DEPOSIT_ERROR);
                    }
                    depositAmount = orderProductDO.getDepositAmount();
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(orderProductDO.getProductCount()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else {
                    if ((BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) || CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                        Integer depositCycle = orderProductDO.getDepositCycle() <= orderProductDO.getRentTimeLength() ? orderProductDO.getDepositCycle() : orderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(depositCycle));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    } else {
                        Integer depositCycle = orderProductDO.getDepositCycle() <= orderProductDO.getRentTimeLength() ? orderProductDO.getDepositCycle() : orderProductDO.getRentTimeLength();
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(depositCycle));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    }
                    creditDepositAmount = BigDecimalUtil.mul(skuPrice, new BigDecimal(orderProductDO.getProductCount()));
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                }


                orderProductDO.setRentDepositAmount(rentDepositAmount);
                orderProductDO.setDepositAmount(depositAmount);
                orderProductDO.setCreditDepositAmount(creditDepositAmount);
                orderProductDO.setProductSkuName(productSku.getSkuName());
                orderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(orderProductDO.getRentTimeLength())));
                orderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                BigDecimal thisOrderProductInsuranceAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getInsuranceAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(orderProductDO.getRentTimeLength()));
                productCount += orderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, orderProductDO.getProductAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, thisOrderProductInsuranceAmount);
            }
            orderDO.setTotalRentDepositAmount(BigDecimalUtil.add(orderDO.getTotalRentDepositAmount(), totalRentDepositAmount));
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMustDepositAmount(BigDecimalUtil.add(orderDO.getTotalMustDepositAmount(), totalDepositAmount));
            orderDO.setTotalProductDepositAmount(totalDepositAmount);
            orderDO.setTotalProductCreditDepositAmount(totalCreditDepositAmount);
            orderDO.setTotalProductRentDepositAmount(totalRentDepositAmount);
            orderDO.setTotalProductCount(productCount);
            orderDO.setTotalProductAmount(productAmountTotal);
        }
    }

    private void calculateOrderMaterialInfo(List<OrderMaterialDO> orderMaterialDOList, OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        if (orderMaterialDOList != null && !orderMaterialDOList.isEmpty()) {
            int materialCount = 0;
            // 商品租赁总额
            BigDecimal materialAmountTotal = new BigDecimal(0.0);
            // 保险总额
            BigDecimal totalInsuranceAmount = new BigDecimal(0.0);
            // 设备该交押金总额
            BigDecimal totalDepositAmount = new BigDecimal(0.0);
            // 设备信用押金总额
            BigDecimal totalCreditDepositAmount = new BigDecimal(0.0);
            // 租赁押金总额
            BigDecimal totalRentDepositAmount = new BigDecimal(0.0);
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                if (material == null) {
                    throw new BusinessException(ErrorCode.MATERIAL_NOT_EXISTS);
                }
                MaterialTypeDO materialTypeDO = materialTypeMapper.findById(material.getMaterialType());

                orderMaterialDO.setMaterialName(material.getMaterialName());
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;

                BigDecimal materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? material.getNewMaterialPrice() : material.getMaterialPrice();
                // 小于等于90天的,不走风控，大于90天的，走风控授信
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType()) && orderMaterialDO.getRentTimeLength() <= CommonConstant.ORDER_NEED_VERIFY_DAYS) {
                    BigDecimal remainder = orderMaterialDO.getDepositAmount().divideAndRemainder(new BigDecimal(orderMaterialDO.getMaterialCount()))[1];
                    if (BigDecimalUtil.compare(remainder, BigDecimal.ZERO) != 0) {
                        throw new BusinessException(ErrorCode.ORDER_MATERIAL_DEPOSIT_ERROR);
                    }
                    depositAmount = orderMaterialDO.getDepositAmount();
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else if (CommonConstant.COMMON_CONSTANT_YES.equals(customerRiskManagementDO.getIsFullDeposit())) {
                    depositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(orderMaterialDO.getMaterialCount()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else {
                    if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getDepositCycle()));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    } else {
                        rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getDepositCycle()));
                        totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);
                    }

                    if (materialTypeDO != null && CommonConstant.COMMON_CONSTANT_YES.equals(materialTypeDO.getIsMainMaterial())) {
                        creditDepositAmount = BigDecimalUtil.mul(materialPrice, new BigDecimal(orderMaterialDO.getMaterialCount()));
                        totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                    }
                }

                orderMaterialDO.setRentDepositAmount(rentDepositAmount);
                orderMaterialDO.setDepositAmount(depositAmount);
                orderMaterialDO.setCreditDepositAmount(creditDepositAmount);
                orderMaterialDO.setMaterialName(material.getMaterialName());
                orderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getRentTimeLength())));
                orderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
                BigDecimal thisOrderMaterialInsuranceAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getInsuranceAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getRentTimeLength()));
                materialCount += orderMaterialDO.getMaterialCount();
                materialAmountTotal = BigDecimalUtil.add(materialAmountTotal, orderMaterialDO.getMaterialAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, thisOrderMaterialInsuranceAmount);
            }
            orderDO.setTotalRentDepositAmount(BigDecimalUtil.add(orderDO.getTotalRentDepositAmount(), totalRentDepositAmount));
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMustDepositAmount(BigDecimalUtil.add(orderDO.getTotalMustDepositAmount(), totalDepositAmount));
            orderDO.setTotalMaterialDepositAmount(totalDepositAmount);
            orderDO.setTotalMaterialCreditDepositAmount(totalCreditDepositAmount);
            orderDO.setTotalMaterialRentDepositAmount(totalRentDepositAmount);
            orderDO.setTotalMaterialCount(materialCount);
            orderDO.setTotalMaterialAmount(materialAmountTotal);
        }
    }

    private String verifyOperateOrder(Order order) {
        if (order == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if ((order.getOrderProductList() == null || order.getOrderProductList().isEmpty())
                && (order.getOrderMaterialList() == null || order.getOrderMaterialList().isEmpty())) {
            return ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL;
        }
        if (order.getCustomerConsignId() == null) {
            return ErrorCode.ORDER_CUSTOMER_CONSIGN_NOT_NULL;
        }
        if (order.getDeliveryMode() == null) {
            return ErrorCode.ORDER_DELIVERY_MODE_IS_NULL;
        }
        if (!DeliveryMode.inThisScope(order.getDeliveryMode())) {
            return ErrorCode.ORDER_DELIVERY_MODE_ERROR;
        }

        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        if (customerDO == null) {
            return ErrorCode.CUSTOMER_NOT_EXISTS;
        }
        order.setBuyerCustomerId(customerDO.getId());

        // 判断逾期情况，如果客户存在未支付的逾期的结算单，不能产生新订单
        List<StatementOrderDO> overdueStatementOrderList = statementOrderSupport.getOverdueStatementOrderList(customerDO.getId());


        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(order.getCustomerConsignId());
        if (customerConsignInfoDO == null || !customerConsignInfoDO.getCustomerId().equals(customerDO.getId())) {
            return ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS;
        }
        if (order.getRentStartTime() == null) {
            return ErrorCode.ORDER_HAVE_NO_RENT_START_TIME;
        }
        if (order.getExpectDeliveryTime() == null) {
            return ErrorCode.ORDER_EXPECT_DELIVERY_TIME;
        }
        Integer deliveryBetweenDays = com.lxzl.erp.common.util.DateUtil.daysBetween(order.getExpectDeliveryTime(), order.getRentStartTime());
        if (deliveryBetweenDays < 0 || deliveryBetweenDays > 2) {
            return ErrorCode.ORDER_RENT_START_TIME_ERROR;
        }
        if (order.getRentType() == null) {
            return ErrorCode.ORDER_RENT_TYPE_IS_NULL;
        }
        if (order.getRentTimeLength() == null || order.getRentTimeLength() <= 0) {
            return ErrorCode.ORDER_RENT_TIME_LENGTH_IS_ZERO_OR_IS_NULL;
        }
        if (!OrderRentType.inThisScope(order.getRentType())) {
            return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
        }
        order.setRentLengthType(OrderRentType.RENT_TYPE_MONTH.equals(order.getRentType()) && order.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT);

        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            Map<String, OrderProduct> orderProductMap = new HashMap<>();
            int oldProductCount = 0, newProductCount = 0;
            Map<Integer, Integer> productNewStockMap = new HashMap<>();
            Map<Integer, Integer> productOldStockMap = new HashMap<>();
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                orderProduct.setRentType(order.getRentType());
                orderProduct.setRentTimeLength(order.getRentTimeLength());
                orderProduct.setRentLengthType(order.getRentLengthType());

                if (orderProduct.getProductCount() == null || orderProduct.getProductCount() <= 0) {
                    return ErrorCode.ORDER_PRODUCT_COUNT_ERROR;
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProduct.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode()) || productServiceResult.getResult() == null) {
                    return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
                }
                Product product = productServiceResult.getResult();
                if (CommonConstant.COMMON_CONSTANT_NO.equals(product.getIsRent())) {
                    return ErrorCode.PRODUCT_IS_NOT_RENT;
                }
                if (CollectionUtil.isEmpty(product.getProductSkuList())) {
                    return ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS;
                }
                if (productNewStockMap.get(product.getProductId()) == null) {
                    productNewStockMap.put(product.getProductId(), product.getNewProductCount());
                }
                if (productOldStockMap.get(product.getProductId()) == null) {
                    productOldStockMap.put(product.getProductId(), product.getOldProductCount());
                }
                oldProductCount = productOldStockMap.get(product.getProductId());
                newProductCount = productNewStockMap.get(product.getProductId());

                // 订单商品库存先去掉
                /*if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProduct.getIsNewProduct())) {
                    if ((newProductCount - orderProduct.getProductCount()) < 0) {
                        return ErrorCode.ORDER_PRODUCT_STOCK_NEW_INSUFFICIENT;
                    } else {
                        newProductCount = newProductCount - orderProduct.getProductCount();
                        productNewStockMap.put(product.getProductId(), newProductCount);
                    }
                } else {
                    if ((oldProductCount - orderProduct.getProductCount()) < 0) {
                        return ErrorCode.ORDER_PRODUCT_STOCK_OLD_INSUFFICIENT;
                    } else {
                        oldProductCount = oldProductCount - orderProduct.getProductCount();
                        productOldStockMap.put(product.getProductId(), oldProductCount);
                    }
                }*/

                String key = orderProduct.getProductSkuId() + "-" + orderProduct.getRentType() + "-" + orderProduct.getRentTimeLength() + "-" + orderProduct.getIsNewProduct();
                if (orderProductMap.get(key) != null) {
                    return ErrorCode.ORDER_PRODUCT_LIST_REPEAT;
                } else {
                    orderProductMap.put(key, orderProduct);
                }

                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                Integer rentLengthType = OrderRentType.RENT_TYPE_MONTH.equals(orderProduct.getRentType()) && orderProduct.getRentTimeLength() >= CommonConstant.ORDER_RENT_TYPE_LONG_MIN ? OrderRentLengthType.RENT_LENGTH_TYPE_LONG : OrderRentLengthType.RENT_LENGTH_TYPE_SHORT;
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(rentLengthType)
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            Map<String, OrderMaterial> orderMaterialMap = new HashMap<>();
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                orderMaterial.setRentType(order.getRentType());
                orderMaterial.setRentTimeLength(order.getRentTimeLength());
                orderMaterial.setRentLengthType(order.getRentLengthType());
                if (orderMaterial.getMaterialCount() == null || orderMaterial.getMaterialCount() <= 0) {
                    return ErrorCode.ORDER_MATERIAL_COUNT_ERROR;
                }
                if (orderMaterial.getMaterialId() == null) {
                    return ErrorCode.MATERIAL_NOT_EXISTS;
                }
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterial.getMaterialId());

                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())
                        || materialServiceResult.getResult() == null) {
                    return ErrorCode.MATERIAL_NOT_EXISTS;
                }
                if (orderMaterial.getMaterialUnitAmount() == null || BigDecimalUtil.compare(orderMaterial.getMaterialUnitAmount(), BigDecimal.ZERO) < 0) {
                    return ErrorCode.ORDER_MATERIAL_AMOUNT_ERROR;
                }
                Material material = materialServiceResult.getResult();
                // 配件库存判断先去掉
                /*if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterial.getIsNewMaterial())) {
                    if (material == null || material.getNewMaterialCount() == null || material.getNewMaterialCount() <= 0 || (material.getNewMaterialCount() - orderMaterial.getMaterialCount()) < 0) {
                        return ErrorCode.ORDER_MATERIAL_STOCK_NEW_INSUFFICIENT;
                    }
                } else {
                    if (material == null || material.getOldMaterialCount() == null || material.getOldMaterialCount() <= 0 || (material.getOldMaterialCount() - orderMaterial.getMaterialCount()) < 0) {
                        return ErrorCode.ORDER_MATERIAL_STOCK_OLD_INSUFFICIENT;
                    }
                }*/
                String key = orderMaterial.getMaterialId() + "-" + orderMaterial.getRentType() + "-" + orderMaterial.getRentTimeLength() + "-" + orderMaterial.getIsNewMaterial();
                if (orderMaterialMap.get(key) != null) {
                    return ErrorCode.ORDER_MATERIAL_LIST_REPEAT;
                } else {
                    orderMaterialMap.put(key, orderMaterial);
                }

                // 如果为长租，但凡有逾期，就不可以下单，如果为短租，在可用额度范围内，就可以下单
                if (OrderRentLengthType.RENT_LENGTH_TYPE_LONG.equals(order.getRentLengthType())
                        && CollectionUtil.isNotEmpty(overdueStatementOrderList)) {
                    return ErrorCode.CUSTOMER_HAVE_OVERDUE_STATEMENT_ORDER;
                }
            }
        }
        return verifyOrderShortRentReceivable(customerDO, ConverterUtil.convert(order, OrderDO.class));
    }

    String verifyOrderShortRentReceivable(CustomerDO customerDO, OrderDO orderDO) {

        Integer subCompanyId = orderDO.getOrderSubCompanyId();
        subCompanyId = subCompanyId == null ? userSupport.getCurrentUserCompanyId() : subCompanyId;

        BigDecimal customerTotalShortRentReceivable = statementOrderSupport.getShortRentReceivable(customerDO.getId());
        //分公司的应收短期上线
        BigDecimal subCompanyTotalShortRentReceivable = statementOrderSupport.getSubCompanyShortRentReceivable(subCompanyId);
        if (BigDecimalUtil.compare(subCompanyTotalShortRentReceivable, BigDecimal.ZERO) < 0) {
            return ErrorCode.SHORT_RECEIVABLE_CALCULATE_FAIL;
        }
        //得到分公司设置的短期上线
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
        BigDecimal shortLimitReceivableAmount = customerDO.getShortLimitReceivableAmount() == null ? new BigDecimal(Integer.MAX_VALUE) : customerDO.getShortLimitReceivableAmount();
        BigDecimal subCompanyShortLimitReceivableAmount = subCompanyDO.getShortLimitReceivableAmount() == null ? new BigDecimal(Integer.MAX_VALUE) : subCompanyDO.getShortLimitReceivableAmount();

        BigDecimal otherAmount = orderDO.getLogisticsAmount();
        customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, otherAmount);
        subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, otherAmount);


        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                if (OrderRentLengthType.RENT_LENGTH_TYPE_SHORT.equals(orderDO.getRentLengthType())) {
                    BigDecimal thisTotalAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(orderProductDO.getProductCount()), orderProductDO.getProductUnitAmount()), new BigDecimal(orderDO.getRentTimeLength()));
                    thisTotalAmount = BigDecimalUtil.add(thisTotalAmount, orderProductDO.getDepositAmount());
                    customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(shortLimitReceivableAmount, customerTotalShortRentReceivable) < 0) {
                        return ErrorCode.CUSTOMER_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                    //检验分公司是否超出
                    subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(subCompanyShortLimitReceivableAmount, subCompanyTotalShortRentReceivable) < 0) {
                        return ErrorCode.SUB_COMPANY_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                if (OrderRentLengthType.RENT_LENGTH_TYPE_SHORT.equals(orderDO.getRentLengthType())) {
                    BigDecimal thisTotalAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(new BigDecimal(orderMaterialDO.getMaterialCount()), orderMaterialDO.getMaterialUnitAmount()), new BigDecimal(orderDO.getRentTimeLength()));
                    thisTotalAmount = BigDecimalUtil.add(thisTotalAmount, orderMaterialDO.getDepositAmount());
                    customerTotalShortRentReceivable = BigDecimalUtil.add(customerTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(shortLimitReceivableAmount, customerTotalShortRentReceivable) < 0) {
                        return ErrorCode.CUSTOMER_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                    //检验分公司是否超出
                    subCompanyTotalShortRentReceivable = BigDecimalUtil.add(subCompanyTotalShortRentReceivable, thisTotalAmount);
                    if (BigDecimalUtil.compare(subCompanyShortLimitReceivableAmount, subCompanyTotalShortRentReceivable) < 0) {
                        return ErrorCode.SUB_COMPANY_SHORT_LIMIT_RECEIVABLE_OVERFLOW;
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    /**
     * 审核注意事项
     *
     * @param orderDO
     * @return
     */
    private ServiceResult<String, String> getVerifyMatters(OrderDO orderDO){
        ServiceResult<String, String> result = new ServiceResult<>();

        String verifyProduct = null;
        BigDecimal productPrice = null;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            Integer count = 1;
            OrderProductDO orderProductDO;
            for(int i=0;i<orderDO.getOrderProductDOList().size();i++){
                orderProductDO = orderDO.getOrderProductDOList().get(i);
                ProductSkuDO productSkuDO = productSkuMapper.findById(orderProductDO.getProductSkuId());
                if(productSkuDO == null){
                    result.setErrorCode(ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }
                //得到
                if(OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())){
                    productPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSkuDO.getNewDayRentPrice() : productSkuDO.getDayRentPrice();
                }else if(OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())){
                    productPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ? productSkuDO.getNewMonthRentPrice() : productSkuDO.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价
                if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), productPrice) < 0) {
                    if(verifyProduct == null){
                        if(OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())){
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：天租，全新商品名称："+orderProductDO.getProductName() + "，订单租赁价格："+ orderProductDO.getProductUnitAmount() +"，预设租赁价格："+ productPrice +"。":
                                    "商品项：" + count + "；租赁方式：天租，次新商品名称："+orderProductDO.getProductName() + "，订单租赁价格："+ orderProductDO.getProductUnitAmount() +"，预设租赁价格："+ productPrice +"。";
                        }else{
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：月租，全新商品名称："+orderProductDO.getProductName() + "，订单租赁价格："+ orderProductDO.getProductUnitAmount() +"，预设租赁价格："+ productPrice +"。":
                                    "商品项：" + count + "；租赁方式：月租，次新商品名称："+orderProductDO.getProductName() + "，订单租赁价格："+ orderProductDO.getProductUnitAmount() +"，预设租赁价格："+ productPrice +"。";
                        }
                    }else{
                        if(OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())){
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ?
                                    "商品项：" + count + "；租赁方式：天租，全新商品名称："+orderProductDO.getProductName() + "，订单租赁价格："+ orderProductDO.getProductUnitAmount() +"，预设租赁价格："+ productPrice +"。":
                                    "商品项：" + count + "；租赁方式：天租，次新商品名称："+orderProductDO.getProductName() + "，订单租赁价格："+ orderProductDO.getProductUnitAmount() +"，预设租赁价格："+ productPrice +"。";
                        }else{
                            verifyProduct = CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct()) ?
                                    verifyProduct + count + "；租赁方式：月租，全新商品名称："+orderProductDO.getProductName() + "，订单租赁价格："+ orderProductDO.getProductUnitAmount() +"，预设租赁价格："+ productPrice +"。":
                                    verifyProduct + count + "；租赁方式：月租，次新商品名称："+orderProductDO.getProductName() + "，订单租赁价格："+ orderProductDO.getProductUnitAmount() +"，预设租赁价格："+ productPrice +"。";
                        }
                    }
                    count++;
                }
            }
        }

        String verifyMaterial = null;
        BigDecimal materialPrice = null;
        if(CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())){
            Integer count = 1;
            OrderMaterialDO orderMaterialDO;
            for(int i=0;i<orderDO.getOrderMaterialDOList().size();i++){
                orderMaterialDO = orderDO.getOrderMaterialDOList().get(i);
                MaterialDO materialDO = materialMapper.findById(orderMaterialDO.getMaterialId());
                if(materialDO == null){
                    result.setErrorCode(ErrorCode.MATERIAL_NOT_EXISTS);
                    return result;
                }
                if(OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())){
                    materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? materialDO.getNewDayRentPrice() : materialDO.getDayRentPrice();
                }else if(OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())){
                    materialPrice = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ? materialDO.getNewMonthRentPrice() : materialDO.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价
                if (BigDecimalUtil.compare(orderMaterialDO.getMaterialUnitAmount(), productPrice) < 0) {
                    if(verifyMaterial == null){
                        if(OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())){
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ?
                                    "配件项：" + count + "；租赁方式：天租，全新配件名称："+orderMaterialDO.getMaterialName() + "，订单租赁价格："+ orderMaterialDO.getMaterialUnitAmount() +"，预设租赁价格："+ materialPrice +"。":
                                    "配件项：" + count + "；租赁方式：天租，次新配件名称："+orderMaterialDO.getMaterialName() + "，订单租赁价格："+ orderMaterialDO.getMaterialUnitAmount() +"，预设租赁价格："+ materialPrice +"。";
                        }else{
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ?
                                    "配件项：" + count + "；租赁方式：月租，全新配件名称："+orderMaterialDO.getMaterialName() + "，订单租赁价格："+ orderMaterialDO.getMaterialUnitAmount() +"，预设租赁价格："+ materialPrice +"。":
                                    "配件项：" + count + "；租赁方式：月租，次新配件名称："+orderMaterialDO.getMaterialName() + "，订单租赁价格："+ orderMaterialDO.getMaterialUnitAmount() +"，预设租赁价格："+ materialPrice +"。";
                        }
                    }else{
                        if(OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())){
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ?
                                    verifyMaterial + count + "；租赁方式：天租，全新配件名称："+orderMaterialDO.getMaterialName() + "，订单租赁价格："+ orderMaterialDO.getMaterialUnitAmount() +"，预设租赁价格："+ materialPrice +"。":
                                    verifyMaterial + count + "；租赁方式：天租，次新配件名称："+orderMaterialDO.getMaterialName() + "，订单租赁价格："+ orderMaterialDO.getMaterialUnitAmount() +"，预设租赁价格："+ materialPrice +"。";
                        }else{
                            verifyMaterial = CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial()) ?
                                    verifyMaterial + count + "；租赁方式：月租，全新配件名称："+orderMaterialDO.getMaterialName() + "，订单租赁价格："+ orderMaterialDO.getMaterialUnitAmount() +"，预设租赁价格："+ materialPrice +"。":
                                    verifyMaterial + count + "；租赁方式：月租，次新配件名称："+orderMaterialDO.getMaterialName() + "，订单租赁价格："+ orderMaterialDO.getMaterialUnitAmount() +"，预设租赁价格："+ materialPrice +"。";
                        }
                    }
                    count++;
                }
            }
        }
        String verifyMatters = verifyProduct + verifyMaterial;

        result.setResult(verifyMatters);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private AmountSupport amountSupport;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StatementService statementService;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private OrderConsignInfoMapper orderConsignInfoMapper;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderProductEquipmentMapper orderProductEquipmentMapper;

    @Autowired
    private OrderMaterialBulkMapper orderMaterialBulkMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;

    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CustomerSupport customerSupport;

    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;

    @Autowired
    private WarehouseSupport warehouseSupport;

    @Autowired
    private ProductSupport productSupport;

    @Autowired
    private MaterialSupport materialSupport;

    @Autowired
    private OrderTimeAxisSupport orderTimeAxisSupport;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private StatementOrderSupport statementOrderSupport;

    @Autowired
    private WebServiceHelper webServiceHelper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private DingDingSupport dingDingSupport;

    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private MaterialMapper materialMapper;
}
