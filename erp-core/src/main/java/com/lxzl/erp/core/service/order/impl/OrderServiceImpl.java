package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.*;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.core.service.material.impl.support.MaterialSupport;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.*;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
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

import java.math.BigDecimal;
import java.util.*;

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

//        List<OrderProductDO> orderProductDOList = ConverterUtil.convertList(order.getOrderProductList(), OrderProductDO.class);
//        List<OrderMaterialDO> orderMaterialDOList = ConverterUtil.convertList(order.getOrderMaterialList(), OrderMaterialDO.class);
        OrderDO orderDO = ConverterUtil.convert(order, OrderDO.class);
//        orderDO.setOrderProductDOList(orderProductDOList);
//        orderDO.setOrderMaterialDOList(orderMaterialDOList);
        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);
        calculateOrderProductInfo(orderDO.getOrderProductDOList(), orderDO);
        calculateOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO);

        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setOrderNo(generateNoSupport.generateOrderNo(currentTime, orderDO.getBuyerCustomerId()));
        orderDO.setOrderSubCompanyId(userSupport.getCurrentUserCompanyId());
        orderDO.setOrderSellerId(loginUser.getUserId());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setCreateUser(loginUser.getUserId().toString());
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setCreateTime(currentTime);
        orderDO.setUpdateTime(currentTime);
        orderMapper.save(orderDO);
        saveOrderProductInfo(orderDO.getOrderProductDOList(), orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderDO.getOrderMaterialDOList(), orderDO.getId(), loginUser, currentTime);
        updateOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);

        Order saveOrder = queryOrderByNo(orderDO.getOrderNo()).getResult();
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), FastJsonUtil.toJSONString(saveOrder), currentTime, loginUser.getUserId());

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
        OrderDO dbOrderDO = orderMapper.findByOrderNo(order.getOrderNo());
        if (dbOrderDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
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

        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalInsuranceAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setId(dbOrderDO.getId());
        orderDO.setOrderNo(dbOrderDO.getOrderNo());
        orderDO.setOrderSellerId(loginUser.getUserId());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
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
    public ServiceResult<String, String> commitOrder(String orderNo, Integer verifyUser, String commitRemark) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (CollectionUtil.isEmpty(orderDO.getOrderProductDOList())
                && CollectionUtil.isEmpty(orderDO.getOrderMaterialDOList())) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL);
            return result;
        }
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        BigDecimal totalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), totalCreditDepositAmount), BigDecimal.ZERO) < 0) {
            result.setErrorCode(ErrorCode.CUSTOMER_GETCREDIT_AMOUNT_OVER_FLOW);
            return result;
        }

        ServiceResult<String, Boolean> isNeedVerifyResult = isNeedVerify(orderNo);
        if (!ErrorCode.SUCCESS.equals(isNeedVerifyResult.getErrorCode())) {
            result.setErrorCode(isNeedVerifyResult.getErrorCode(), isNeedVerifyResult.getFormatArgs());
            return result;
        }
        // 是否需要审批
        boolean isNeedVerify = isNeedVerifyResult.getResult();

        if (isNeedVerify) {
            ServiceResult<String, String> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderDO.getOrderNo(), verifyUser, commitRemark);
            if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(workflowCommitResult.getErrorCode());
                return result;
            }
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_VERIFYING);
        } else {
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
            // 只有审批通过才生成结算单
            ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createOrderStatement(orderNo);
            if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(createStatementOrderResult.getErrorCode());
                return result;
            }
            orderDO.setFirstNeedPayAmount(createStatementOrderResult.getResult());
        }

        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);
        // 扣除信用额度
        if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) != 0) {
            customerSupport.addCreditAmountUsed(orderDO.getBuyerCustomerId(), totalCreditDepositAmount);
        }

        Order saveOrder = queryOrderByNo(orderDO.getOrderNo()).getResult();
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), FastJsonUtil.toJSONString(saveOrder), currentTime, loginUser.getUserId());
        result.setResult(orderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> payOrder(String orderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
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
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_NOT_NULL);
                    return result;
                }
                ProductSku thisProductSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (thisProductSku == null) {
                    result.setErrorCode(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                    return result;
                }

                BigDecimal productUnitAmount = null;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    productUnitAmount = thisProductSku.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProductDO.getRentType())) {
                    productUnitAmount = thisProductSku.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价，需要商务审批
                if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), productUnitAmount) < 0) {
                    isNeedVerify = true;
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
                    materialUnitAmount = material.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterialDO.getRentType())) {
                    materialUnitAmount = material.getMonthRentPrice();
                }
                // 订单价格低于商品租赁价，需要商务审批
                if (BigDecimalUtil.compare(orderMaterialDO.getMaterialUnitAmount(), materialUnitAmount) < 0) {
                    isNeedVerify = true;
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
            List<Map<String, Object>> productSkuLastPriceMap = orderProductMapper.queryLastPrice(customerDO.getId(), request.getProductSkuId());
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
            response.setProductSkuDayPrice(product.getProductSkuList().get(0).getDayRentPrice());
            response.setProductSkuMonthPrice(product.getProductSkuList().get(0).getMonthRentPrice());
        }

        if (request.getMaterialId() != null) {
            ServiceResult<String, Material> queryMaterialResult = materialService.queryMaterialById(request.getMaterialId());
            if (!ErrorCode.SUCCESS.equals(queryMaterialResult.getErrorCode())) {
                result.setErrorCode(queryMaterialResult.getErrorCode());
                return result;
            }
            Material material = queryMaterialResult.getResult();
            List<Map<String, Object>> materialSkuLastPriceMap = orderMaterialMapper.queryLastPrice(customerDO.getId(), request.getProductSkuId());
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
            response.setMaterialDayPrice(material.getDayRentPrice());
            response.setMaterialMonthPrice(material.getMonthRentPrice());
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
        if (orderNo == null || returnNBulkMaterialNo == null || returnDate == null) {
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
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            Date currentTime = new Date();
            User loginUser = userSupport.getCurrentUser();
            OrderDO orderDO = orderMapper.findByOrderNo(businessNo);
            if (orderDO == null || !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
                // 只有审批通过的订单才生成结算单
                ServiceResult<String, BigDecimal> createStatementOrderResult = statementService.createOrderStatement(orderDO.getOrderNo());
                if (!ErrorCode.SUCCESS.equals(createStatementOrderResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return false;
                }
                orderDO.setFirstNeedPayAmount(createStatementOrderResult.getResult());
            } else {
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
                // 如果拒绝，则退还授信额度
                if (BigDecimalUtil.compare(orderDO.getTotalCreditDepositAmount(), BigDecimal.ZERO) != 0) {
                    customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), orderDO.getTotalCreditDepositAmount());
                }
            }
            orderDO.setUpdateTime(currentTime);
            orderDO.setUpdateUser(loginUser.getUserId().toString());
            orderMapper.update(orderDO);

            // 记录订单时间轴
            Order saveOrder = queryOrderByNo(orderDO.getOrderNo()).getResult();
            orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), FastJsonUtil.toJSONString(saveOrder), currentTime, loginUser.getUserId());
        } catch (Exception e) {
            logger.error("审批订单通知失败： {}", businessNo);
            return false;
        }
        return true;
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
        Order order = ConverterUtil.convert(orderDO, Order.class);

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
                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProduct.getOrderProductId());
                orderProduct.setOrderProductEquipmentList(ConverterUtil.convertList(orderProductEquipmentDOList, OrderProductEquipment.class));
            }
        }
        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterial.getOrderMaterialId());
                orderMaterial.setOrderMaterialBulkList(ConverterUtil.convertList(orderMaterialBulkDOList, OrderMaterialBulk.class));
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
        if (orderDO.getOrderStatus() == null || !OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
        orderDO.setUpdateTime(currentTime);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderMapper.update(orderDO);
        customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), orderDO.getTotalCreditDepositAmount());
        // 记录订单时间轴
        Order saveOrder = queryOrderByNo(orderDO.getOrderNo()).getResult();
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), FastJsonUtil.toJSONString(saveOrder), currentTime, loginUser.getUserId());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    public ServiceResult<String, Page<Order>> queryAllOrder(OrderQueryParam orderQueryParam) {
        User user = userSupport.getCurrentUser();
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        //数据级权限控制-查找用户可查看用户列表
        Integer currentUserId = userSupport.getCurrentUserId();
        //获取用户最【新的】最终可观察用户列表
        List<UserDO> userDOList = userMapper.getPassiveUserByUser(currentUserId);
        //数据级权限控制-查找用户可查看本分公司
        Integer subCompanyId = userSupport.getCurrentUserCompanyId();
        List<Integer> passiveUserIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userDOList)) {
            for (UserDO userDO : userDOList) {
                passiveUserIdList.add(userDO.getId());
            }
        }

        orderQueryParam.setPassiveUserIdList(passiveUserIdList);
        orderQueryParam.setSubCompanyId(subCompanyId);

        maps.put("orderQueryParam", orderQueryParam);

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
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus())
                && !OrderStatus.ORDER_STATUS_PROCESSING.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        Integer payType = null;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProductDO.getPayMode())) {
                    payType = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    break;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList()) && !OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payType)) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderMaterialDO.getPayMode())) {
                    payType = OrderPayMode.PAY_MODE_PAY_BEFORE;
                    break;
                }
            }
        }


        if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(payType) &&
                !PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())
                && !PayStatus.PAY_STATUS_PAID_PART.equals(orderDO.getPayStatus())) {
            result.setErrorCode(ErrorCode.ORDER_HAVE_NO_PAID);
            return result;
        }
        if (!CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())
                && !CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }

        if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())) {
            ServiceResult<String, Object> addOrderItemResult = addOrderItem(orderDO, param.getWarehouseId(), param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(addOrderItemResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setErrorCode(addOrderItemResult.getErrorCode(), addOrderItemResult.getFormatArgs());
                return result;
            }
        } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            ServiceResult<String, Object> removeOrderItemResult = removeOrderItem(orderDO, param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), loginUser.getUserId(), currentTime);
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
        Order saveOrder = queryOrderByNo(orderDO.getOrderNo()).getResult();
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), FastJsonUtil.toJSONString(saveOrder), currentTime, loginUser.getUserId());

        result.setResult(param.getOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private ServiceResult<String, Object> addOrderItem(OrderDO orderDO, Integer srcWarehouseId, String equipmentNo, Integer materialId, Integer materialCount, Integer loginUserId, Date currentTime) {
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
            WarehouseDO currentWarehouse = warehouseSupport.getAvailableWarehouse(productEquipmentDO.getCurrentWarehouseId());
            if (currentWarehouse == null) {
                result.setErrorCode(ErrorCode.PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE, equipmentNo, productEquipmentDO.getCurrentWarehouseId());
                return result;
            }

            boolean isMatching = false;
            Map<String, OrderProductDO> orderProductDOMap = ListUtil.listToMap(orderDO.getOrderProductDOList(), "productSkuId", "rentType", "rentTimeLength");
            for (Map.Entry<String, OrderProductDO> entry : orderProductDOMap.entrySet()) {
                String key = entry.getKey();
                // 如果输入进来的设备skuID 为当前订单项需要的，那么就匹配
                if (key.startsWith(productEquipmentDO.getSkuId().toString())) {
                    OrderProductDO orderProductDO = orderProductDOMap.get(key);
                    List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProductDO.getId());
                    // 如果这个订单项满了，那么就换下一个
                    if (orderProductEquipmentDOList != null && orderProductEquipmentDOList.size() >= orderProductDO.getProductCount()) {
                        continue;
                    }
                    if (!productEquipmentDO.getIsNew().equals(orderProductDO.getIsNewProduct())) {
                        continue;
                    }

                    productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
                    productEquipmentDO.setOrderNo(orderDO.getOrderNo());
                    productEquipmentDO.setUpdateTime(currentTime);
                    productEquipmentDO.setUpdateUser(loginUserId.toString());
                    productEquipmentMapper.update(productEquipmentDO);

                    // 将该设备上的物料统一加上订单号
                    bulkMaterialMapper.updateEquipmentOrderNo(productEquipmentDO.getEquipmentNo(), orderDO.getOrderNo());

                    BigDecimal expectRentAmount = calculationOrderExpectRentAmount(orderProductDO.getProductUnitAmount(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength());
                    Date expectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength());
                    OrderProductEquipmentDO orderProductEquipmentDO = new OrderProductEquipmentDO();
                    orderProductEquipmentDO.setOrderId(orderProductDO.getOrderId());
                    orderProductEquipmentDO.setOrderProductId(orderProductDO.getId());
                    orderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
                    orderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
                    orderProductEquipmentDO.setRentStartTime(orderDO.getRentStartTime());
                    orderProductEquipmentDO.setProductEquipmentUnitAmount(orderProductDO.getProductUnitAmount());
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
                    break;
                }
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
            // 必须是当前库房闲置的物料
            List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialSupport.queryFitBulkMaterialDOList(srcWarehouseId, materialId, materialCount);
            if (CollectionUtil.isEmpty(bulkMaterialDOList) || bulkMaterialDOList.size() < materialCount) {
                result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                return result;
            }
            for (int i = 0; i < materialCount; i++) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialDOList.get(i);
                if (!bulkMaterialDO.getCurrentWarehouseId().equals(srcWarehouseId)) {
                    result.setErrorCode(ErrorCode.BULK_MATERIAL_NOT_IN_THIS_WAREHOUSE, equipmentNo, bulkMaterialDO.getCurrentWarehouseId());
                    return result;
                }

                boolean isMatching = false;
                Map<String, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "materialId", "rentType", "rentTimeLength");
                for (Map.Entry<String, OrderMaterialDO> entry : orderMaterialDOMap.entrySet()) {
                    String key = entry.getKey();
                    // 如果输入进来的散料ID 为当前订单项需要的，那么就匹配
                    if (key.startsWith(bulkMaterialDO.getMaterialId().toString())) {
                        OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(key);
                        List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterialDO.getId());
                        if (orderMaterialBulkDOList != null && orderMaterialBulkDOList.size() >= orderMaterialDO.getMaterialCount()) {
                            continue;
                        }
                        if (!bulkMaterialDO.getIsNew().equals(orderMaterialDO.getIsNewMaterial())) {
                            continue;
                        }
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
                        orderMaterialBulkDO.setExpectReturnTime(expectReturnTime);
                        orderMaterialBulkDO.setExpectRentAmount(expectRentAmount);
                        orderMaterialBulkDO.setActualRentAmount(BigDecimal.ZERO);
                        orderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        orderMaterialBulkDO.setCreateTime(currentTime);
                        orderMaterialBulkDO.setCreateUser(loginUserId.toString());
                        orderMaterialBulkDO.setUpdateTime(currentTime);
                        orderMaterialBulkDO.setUpdateUser(loginUserId.toString());
                        orderMaterialBulkMapper.save(orderMaterialBulkDO);
                        isMatching = true;
                        break;
                    }
                }
                if (!isMatching) {
                    result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                    return result;
                }
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

    private ServiceResult<String, Object> removeOrderItem(OrderDO orderDO, String equipmentNo, Integer materialId, Integer materialCount, Integer loginUserId, Date currentTime) {
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
            List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialSupport.queryBusyBulkMaterialDOList(orderDO.getOrderNo(), materialId, materialCount);
            if (CollectionUtil.isEmpty(bulkMaterialDOList) || bulkMaterialDOList.size() < materialCount) {
                result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                return result;
            }
            for (int i = 0; i < materialCount; i++) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialDOList.get(i);
                OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderIdAndBulkMaterialNo(orderDO.getId(), bulkMaterialDO.getBulkMaterialNo());
                if (orderMaterialBulkDO == null) {
                    result.setErrorCode(ErrorCode.ORDER_HAVE_NO_THIS_ITEM, equipmentNo);
                    return result;
                }

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

        if (DateUtil.getBeginOfDay(currentTime).getTime() < DateUtil.dateInterval(DateUtil.getBeginOfDay(dbRecordOrder.getRentStartTime()), -2).getTime()) {
            result.setErrorCode(ErrorCode.ORDER_CAN_NOT_DELIVERY);
            return result;
        }
        Date expectReturnTime = null;
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : dbRecordOrder.getOrderProductDOList()) {
                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProductDO.getId());
                if (orderProductEquipmentDOList == null || orderProductDO.getProductCount() != orderProductEquipmentDOList.size()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR);
                    return result;
                }
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(dbRecordOrder.getRentStartTime(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
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
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(dbRecordOrder.getRentStartTime(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }

        dbRecordOrder.setDeliveryTime(currentTime);
        dbRecordOrder.setExpectReturnTime(expectReturnTime);
        dbRecordOrder.setOrderStatus(OrderStatus.ORDER_STATUS_DELIVERED);
        dbRecordOrder.setUpdateUser(loginUser.getUserId().toString());
        dbRecordOrder.setUpdateTime(currentTime);
        orderMapper.update(dbRecordOrder);

        // 记录订单时间轴
        Order saveOrder = queryOrderByNo(dbRecordOrder.getOrderNo()).getResult();
        orderTimeAxisSupport.addOrderTimeAxis(dbRecordOrder.getId(), dbRecordOrder.getOrderStatus(), FastJsonUtil.toJSONString(saveOrder), currentTime, loginUser.getUserId());

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
        if (customerRiskManagementDO == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                Product product = productServiceResult.getResult();
                // 商品品牌为苹果品牌
                if (BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) {
                    orderProductDO.setDepositCycle(customerRiskManagementDO.getAppleDepositCycle());
                    orderProductDO.setPaymentCycle(customerRiskManagementDO.getApplePaymentCycle());
                    orderProductDO.setPayMode(customerRiskManagementDO.getApplePayMode());
                } else if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                    orderProductDO.setDepositCycle(customerRiskManagementDO.getNewDepositCycle());
                    orderProductDO.setPaymentCycle(customerRiskManagementDO.getNewPaymentCycle());
                    orderProductDO.setPayMode(customerRiskManagementDO.getNewPayMode());
                } else {
                    orderProductDO.setDepositCycle(customerRiskManagementDO.getDepositCycle());
                    orderProductDO.setPaymentCycle(customerRiskManagementDO.getPaymentCycle());
                    orderProductDO.setPayMode(customerRiskManagementDO.getPayMode());
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                ServiceResult<String, Material> materialResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
                Material material = materialResult.getResult();
                if (BrandId.BRAND_ID_APPLE.equals(material.getBrandId())) {
                    orderMaterialDO.setDepositCycle(customerRiskManagementDO.getAppleDepositCycle());
                    orderMaterialDO.setPaymentCycle(customerRiskManagementDO.getApplePaymentCycle());
                    orderMaterialDO.setPayMode(customerRiskManagementDO.getApplePayMode());
                } else if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                    orderMaterialDO.setDepositCycle(customerRiskManagementDO.getNewDepositCycle());
                    orderMaterialDO.setPaymentCycle(customerRiskManagementDO.getNewPaymentCycle());
                    orderMaterialDO.setPaymentCycle(customerRiskManagementDO.getNewPaymentCycle());
                } else {
                    orderMaterialDO.setDepositCycle(customerRiskManagementDO.getDepositCycle());
                    orderMaterialDO.setPaymentCycle(customerRiskManagementDO.getPaymentCycle());
                }
            }
        }
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
        Order saveOrder = queryOrderByNo(orderDO.getOrderNo()).getResult();
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), FastJsonUtil.toJSONString(saveOrder), currentTime, loginUser.getUserId());
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

        Map<Integer, OrderProductDO> saveOrderProductDOMap = new HashMap<>();
        Map<Integer, OrderProductDO> updateOrderProductDOMap = new HashMap<>();
        List<OrderProductDO> dbOrderProductDOList = orderProductMapper.findByOrderId(orderId);
        Map<Integer, OrderProductDO> dbOrderProductDOMap = ListUtil.listToMap(dbOrderProductDOList, "id");
        for (OrderProductDO orderProductDO : orderProductDOList) {
            if (dbOrderProductDOMap.get(orderProductDO.getId()) != null) {
                updateOrderProductDOMap.put(orderProductDO.getProductSkuId(), orderProductDO);
                dbOrderProductDOMap.remove(orderProductDO.getId());
            } else {
                saveOrderProductDOMap.put(orderProductDO.getProductSkuId(), orderProductDO);
            }
        }

        if (saveOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, OrderProductDO> entry : saveOrderProductDOMap.entrySet()) {
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
            for (Map.Entry<Integer, OrderProductDO> entry : updateOrderProductDOMap.entrySet()) {
                OrderProductDO orderProductDO = entry.getValue();
                orderProductDO.setOrderId(orderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateTime(currentTime);
                orderProductMapper.update(orderProductDO);
            }
        }

        if (dbOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, OrderProductDO> entry : dbOrderProductDOMap.entrySet()) {
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

        Map<Integer, OrderMaterialDO> saveOrderMaterialDOMap = new HashMap<>();
        Map<Integer, OrderMaterialDO> updateOrderMaterialDOMap = new HashMap<>();
        List<OrderMaterialDO> dbOrderMaterialDOList = orderMaterialMapper.findByOrderId(orderId);
        Map<Integer, OrderMaterialDO> dbOrderMaterialDOMap = ListUtil.listToMap(dbOrderMaterialDOList, "id");
        if (CollectionUtil.isNotEmpty(orderMaterialDOList)) {
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                if (dbOrderMaterialDOMap.get(orderMaterialDO.getId()) != null) {
                    updateOrderMaterialDOMap.put(orderMaterialDO.getMaterialId(), orderMaterialDO);
                    dbOrderMaterialDOMap.remove(orderMaterialDO.getId());
                } else {
                    saveOrderMaterialDOMap.put(orderMaterialDO.getMaterialId(), orderMaterialDO);
                }
            }
        }

        if (saveOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, OrderMaterialDO> entry : saveOrderMaterialDOMap.entrySet()) {
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
            for (Map.Entry<Integer, OrderMaterialDO> entry : updateOrderMaterialDOMap.entrySet()) {
                OrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialMapper.update(orderMaterialDO);
            }
        }

        if (dbOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, OrderMaterialDO> entry : dbOrderMaterialDOMap.entrySet()) {
                OrderMaterialDO orderMaterialDO = entry.getValue();
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialMapper.update(orderMaterialDO);
            }
        }
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
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    depositAmount = BigDecimalUtil.mul(productSku.getSkuPrice(), new BigDecimal(orderProductDO.getProductCount()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else if ((BrandId.BRAND_ID_APPLE.equals(product.getBrandId())) || CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                    Integer depositCycle = orderProductDO.getDepositCycle() <= orderProductDO.getRentTimeLength() ? orderProductDO.getDepositCycle() : orderProductDO.getRentTimeLength();
                    rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(depositCycle));
                    totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);

                    creditDepositAmount = BigDecimalUtil.mul(productSku.getSkuPrice(), new BigDecimal(orderProductDO.getProductCount()));
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
                } else {
                    Integer depositCycle = orderProductDO.getDepositCycle() <= orderProductDO.getRentTimeLength() ? orderProductDO.getDepositCycle() : orderProductDO.getRentTimeLength();
                    rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(depositCycle));
                    totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);

                    creditDepositAmount = BigDecimalUtil.mul(productSku.getSkuPrice(), new BigDecimal(orderProductDO.getProductCount()));
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
                orderMaterialDO.setMaterialName(material.getMaterialName());
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                BigDecimal rentDepositAmount = BigDecimal.ZERO;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                    depositAmount = BigDecimalUtil.mul(material.getMaterialPrice(), new BigDecimal(orderMaterialDO.getMaterialCount()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                    rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getDepositCycle()));
                    totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);

                    depositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(material.getMaterialPrice(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getDepositCycle()));
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, depositAmount);
                } else {
                    rentDepositAmount = BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getDepositCycle()));
                    totalRentDepositAmount = BigDecimalUtil.add(totalRentDepositAmount, rentDepositAmount);

                    creditDepositAmount = BigDecimalUtil.mul(material.getMaterialPrice(), new BigDecimal(orderMaterialDO.getMaterialCount()));
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, creditDepositAmount);
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
        CustomerDO customerDO = customerMapper.findByNo(order.getBuyerCustomerNo());
        if (customerDO == null) {
            return ErrorCode.CUSTOMER_NOT_EXISTS;
        }
        order.setBuyerCustomerId(customerDO.getId());

        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(order.getCustomerConsignId());
        if (customerConsignInfoDO == null || !customerConsignInfoDO.getCustomerId().equals(customerDO.getId())) {
            return ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS;
        }
        if (order.getRentStartTime() == null) {
            return ErrorCode.ORDER_HAVE_NO_RENT_START_TIME;
        }
        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            Map<String, OrderProduct> orderProductMap = new HashMap<>();
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                if (orderProduct.getRentType() == null || orderProduct.getRentTimeLength() == null || orderProduct.getRentTimeLength() <= 0) {
                    return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
                }
                if (!OrderRentType.inThisScope(orderProduct.getRentType())) {
                    return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
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
                ProductSku productSku = product.getProductSkuList().get(0);
                if (productSku == null || productSku.getStock() == null || productSku.getStock() <= 0 || (productSku.getStock() - orderProduct.getProductCount()) < 0) {
                    return ErrorCode.ORDER_PRODUCT_STOCK_INSUFFICIENT;
                }

                String key = orderProduct.getProductSkuId() + "-" + orderProduct.getRentType() + "-" + orderProduct.getRentTimeLength();
                if (orderProductMap.get(key) != null) {
                    return ErrorCode.ORDER_PRODUCT_LIST_REPEAT;
                } else {
                    orderProductMap.put(key, orderProduct);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            Map<String, OrderMaterial> orderMaterialMap = new HashMap<>();
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                if (orderMaterial.getRentType() == null || orderMaterial.getRentTimeLength() == null || orderMaterial.getRentTimeLength() <= 0) {
                    return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
                }
                if (!OrderRentType.inThisScope(orderMaterial.getRentType())) {
                    return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
                }
                if (orderMaterial.getMaterialId() == null || orderMaterial.getMaterialCount() == null) {
                    return ErrorCode.PARAM_IS_NOT_ENOUGH;
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
                if (material.getStock() == null || material.getStock() <= 0 || (material.getStock() - orderMaterial.getMaterialCount()) < 0) {
                    return ErrorCode.ORDER_MATERIAL_STOCK_INSUFFICIENT;
                }
                String key = orderMaterial.getMaterialId() + "-" + orderMaterial.getRentType() + "-" + orderMaterial.getRentTimeLength();
                if (orderMaterialMap.get(key) != null) {
                    return ErrorCode.ORDER_MATERIAL_LIST_REPEAT;
                } else {
                    orderMaterialMap.put(key, orderMaterial);
                }
            }
        }
        return ErrorCode.SUCCESS;
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
    private UserMapper userMapper;
}
