package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.ProductOutStockParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderConverter;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.*;
import com.lxzl.erp.dataaccess.domain.product.*;
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

        List<OrderProductDO> orderProductDOList = OrderConverter.convertOrderProductList(order.getOrderProductList());
        List<OrderMaterialDO> orderMaterialDOList = OrderConverter.convertOrderMaterialList(order.getOrderMaterialList());
        OrderDO orderDO = OrderConverter.convertOrder(order);
        calculateOrderProductInfo(orderProductDOList, orderDO);
        calculateOrderMaterialInfo(orderMaterialDOList, orderDO);
        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);

        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalMustDepositAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setOrderNo(GenerateNoUtil.generateOrderNo(currentTime));
        orderDO.setOrderSellerId(loginUser.getUserId());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setCreateUser(loginUser.getUserId().toString());
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setCreateTime(currentTime);
        orderDO.setUpdateTime(currentTime);
        orderMapper.save(orderDO);
        saveOrderProductInfo(orderProductDOList, orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderMaterialDOList, orderDO.getId(), loginUser, currentTime);
        updateOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);

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

        List<OrderProductDO> orderProductDOList = OrderConverter.convertOrderProductList(order.getOrderProductList());
        List<OrderMaterialDO> orderMaterialDOList = OrderConverter.convertOrderMaterialList(order.getOrderMaterialList());
        OrderDO orderDO = OrderConverter.convertOrder(order);
        calculateOrderProductInfo(orderProductDOList, orderDO);
        calculateOrderMaterialInfo(orderMaterialDOList, orderDO);
        // 校验客户风控信息
        verifyCustomerRiskInfo(orderDO);

        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalMustDepositAmount()), orderDO.getTotalDiscountAmount()));
        orderDO.setId(dbOrderDO.getId());
        orderDO.setOrderNo(dbOrderDO.getOrderNo());
        orderDO.setOrderSellerId(loginUser.getUserId());
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
        orderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);
        saveOrderProductInfo(orderProductDOList, orderDO.getId(), loginUser, currentTime);
        saveOrderMaterialInfo(orderMaterialDOList, orderDO.getId(), loginUser, currentTime);
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
                || CollectionUtil.isEmpty(orderDO.getOrderMaterialDOList())) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL);
            return result;
        }
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        BigDecimal totalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), totalCreditDepositAmount), BigDecimal.ZERO) < 0) {
            result.setErrorCode(ErrorCode.CUSTOMER_GETCREDIT_AMOUNT_OVER_FLOW);
            return result;
        }
        // TODO 先付后用的单子，如果没有付款就不能提交
        if (PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())) {
            result.setErrorCode(ErrorCode.ORDER_HAVE_NO_PAID);
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
        }

        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);

        // 扣除信用额度
        if (BigDecimalUtil.compare(totalCreditDepositAmount, BigDecimal.ZERO) != 0) {
            customerSupport.addCreditAmountUsed(orderDO.getBuyerCustomerId(), totalCreditDepositAmount);
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
        } catch (Exception e) {
            logger.error("审批订单通知失败： {}", businessNo);
            return false;
        }
        return true;
    }

    @Override
    public ServiceResult<String, Order> queryOrderByNo(String orderNo) {
        User loginUser = userSupport.getCurrentUser();
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
        Order order = OrderConverter.convertOrderDO(orderDO);

        for (OrderProduct orderProduct : order.getOrderProductList()) {
            Product product = FastJsonUtil.toBean(orderProduct.getProductSkuSnapshot(), Product.class);
            for (ProductSku productSku : product.getProductSkuList()) {
                if (orderProduct.getProductSkuId().equals(productSku.getSkuId())) {
                    orderProduct.setProductSkuPropertyList(productSku.getProductSkuPropertyList());
                    break;
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
        if (orderDO.getOrderStatus() == null || !OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_CANCEL);
        orderDO.setUpdateTime(currentTime);
        if (loginUser != null) {
            orderDO.setUpdateUser(loginUser.getUserId().toString());
        }
        orderMapper.update(orderDO);
        customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(), orderDO.getTotalCreditDepositAmount());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(orderDO.getOrderNo());
        return result;
    }

    @Override
    public ServiceResult<String, Page<Order>> queryAllOrder(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Page<Order>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("orderQueryParam", orderQueryParam);

        Integer totalCount = orderMapper.findOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);
        List<Order> orderList = OrderConverter.convertOrderDOList(orderDOList);
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
        Page<Order> page = new Page<>(OrderConverter.convertOrderDOList(orderDOList), totalCount, orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
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
        ServiceResult<String, List<Warehouse>> warehouseResult = warehouseService.getWarehouseByCurrentCompany();
        if (!ErrorCode.SUCCESS.equals(warehouseResult.getErrorCode())) {
            result.setErrorCode(warehouseResult.getErrorCode());
            return result;
        }
        // 取仓库，本公司的默认仓库和客户仓
        List<Warehouse> warehouseList = warehouseResult.getResult();
        Warehouse srcWarehouse = null;
        for (Warehouse warehouse : warehouseList) {
            if (WarehouseType.WAREHOUSE_TYPE_DEFAULT.equals(warehouse.getWarehouseType())) {
                srcWarehouse = warehouse;
            }
        }
        if (srcWarehouse == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }

        if (!CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())
                && !CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }


        if (CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD.equals(param.getOperationType())) {
            ServiceResult<String, Object> addOrderItemResult = addOrderItem(orderDO, srcWarehouse.getWarehouseId(), param.getEquipmentNo(), param.getMaterialId(), param.getMaterialCount(), loginUser.getUserId(), currentTime);
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
            if (!srcWarehouseId.equals(productEquipmentDO.getCurrentWarehouseId())) {
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

                    productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
                    productEquipmentDO.setOrderNo(orderDO.getOrderNo());
                    productEquipmentDO.setUpdateTime(currentTime);
                    productEquipmentDO.setUpdateUser(loginUserId.toString());
                    productEquipmentMapper.update(productEquipmentDO);

                    BigDecimal expectRentAmount = calculationOrderExpectRentAmount(orderProductDO.getProductUnitAmount(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength());
                    Date expectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength());
                    OrderProductEquipmentDO orderProductEquipmentDO = new OrderProductEquipmentDO();
                    orderProductEquipmentDO.setOrderId(orderProductDO.getOrderId());
                    orderProductEquipmentDO.setOrderProductId(orderProductDO.getId());
                    orderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
                    orderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
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
        }

        if (materialId != null) {
            // 必须是当前库房闲置的物料
            BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
            bulkMaterialQueryParam.setMaterialId(materialId);
            bulkMaterialQueryParam.setCurrentWarehouseId(srcWarehouseId);
            bulkMaterialQueryParam.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE);

            Map<String, Object> bulkQueryParam = new HashMap<>();
            bulkQueryParam.put("start", 0);
            bulkQueryParam.put("pageSize", Integer.MAX_VALUE);
            bulkQueryParam.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
            List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listPage(bulkQueryParam);
            if (CollectionUtil.isEmpty(bulkMaterialDOList) || bulkMaterialDOList.size() < materialCount) {
                result.setErrorCode(ErrorCode.BULK_MATERIAL_HAVE_NOT_ENOUGH);
                return result;
            }
            for (int i = 0; i < materialCount; i++) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialDOList.get(i);
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

            orderProductEquipmentDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            orderProductEquipmentDO.setUpdateTime(currentTime);
            orderProductEquipmentDO.setUpdateUser(loginUserId.toString());
            orderProductEquipmentMapper.save(orderProductEquipmentDO);
        }

        if (materialId != null) {
            BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
            bulkMaterialQueryParam.setMaterialId(materialId);
            bulkMaterialQueryParam.setOrderNo(orderDO.getOrderNo());
            bulkMaterialQueryParam.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
            Map<String, Object> bulkQueryParam = new HashMap<>();
            bulkQueryParam.put("start", 0);
            bulkQueryParam.put("pageSize", Integer.MAX_VALUE);
            bulkQueryParam.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
            List<BulkMaterialDO> bulkMaterialDOList = bulkMaterialMapper.listPage(bulkQueryParam);
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
        // 即将出库的设备ID
        List<Integer> productEquipmentIdList = new ArrayList<>();
        // 即将出库的散料ID
        List<Integer> bulkMaterialIdList = new ArrayList<>();

        Integer srcWarehouseId = null;
        Integer targetWarehouseId = null;
        Date expectReturnTime = null;
        if (CollectionUtil.isNotEmpty(dbRecordOrder.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : dbRecordOrder.getOrderProductDOList()) {
                List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProductDO.getId());
                if (orderProductEquipmentDOList == null || orderProductDO.getProductCount() != orderProductEquipmentDOList.size()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR);
                    return result;
                } else if (srcWarehouseId == null || targetWarehouseId == null) {
                    ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(orderProductEquipmentDOList.get(0).getEquipmentNo());
                    srcWarehouseId = productEquipmentDO.getCurrentWarehouseId();
                    targetWarehouseId = productEquipmentDO.getCurrentWarehouseId();
                }
                for (OrderProductEquipmentDO orderProductEquipmentDO : orderProductEquipmentDOList) {
                    productEquipmentIdList.add(orderProductEquipmentDO.getEquipmentId());
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
                } else if (srcWarehouseId == null || targetWarehouseId == null) {
                    BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(orderMaterialBulkDOList.get(0).getBulkMaterialNo());
                    srcWarehouseId = bulkMaterialDO.getCurrentWarehouseId();
                    targetWarehouseId = bulkMaterialDO.getCurrentWarehouseId();
                }
                for (OrderMaterialBulkDO orderMaterialBulkDO : orderMaterialBulkDOList) {
                    bulkMaterialIdList.add(orderMaterialBulkDO.getBulkMaterialId());
                }
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(dbRecordOrder.getRentStartTime(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }

        // 计算预计归还时间
        ProductOutStockParam productOutStockParam = new ProductOutStockParam();
        productOutStockParam.setSrcWarehouseId(srcWarehouseId);
        productOutStockParam.setTargetWarehouseId(targetWarehouseId);
        productOutStockParam.setCauseType(StockCauseType.STOCK_CAUSE_TYPE_ORDER_DELIVERY);
        productOutStockParam.setReferNo(dbRecordOrder.getOrderNo());
        productOutStockParam.setProductEquipmentIdList(productEquipmentIdList);
        productOutStockParam.setBulkMaterialIdList(bulkMaterialIdList);
        warehouseService.productOutStock(productOutStockParam);

        dbRecordOrder.setDeliveryTime(currentTime);
        dbRecordOrder.setExpectReturnTime(expectReturnTime);
        dbRecordOrder.setOrderStatus(OrderStatus.ORDER_STATUS_DELIVERED);
        dbRecordOrder.setUpdateUser(loginUser.getUserId().toString());
        dbRecordOrder.setUpdateTime(currentTime);
        orderMapper.update(dbRecordOrder);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order.getOrderNo());
        return result;
    }

    /**
     * 计算订单预计归还时间
     */
    private Date calculationOrderExpectReturnTime(Date rentStartTime, Integer rentType, Integer rentTimeLength) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return DateUtil.dateInterval(rentStartTime, rentTimeLength);
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return DateUtil.monthInterval(rentStartTime, rentTimeLength);
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
                // TODO 商品品牌为苹果品牌
                if (product.getBrandId().equals(1)) {
                    orderProductDO.setDepositCycle(customerRiskManagementDO.getAppleDepositCycle());
                    orderProductDO.setPaymentCycle(customerRiskManagementDO.getApplePaymentCycle());
                    if (!orderProductDO.getPayMode().equals(customerRiskManagementDO.getApplePayMode())) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_ERROR);
                    }
                } else if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProductDO.getIsNewProduct())) {
                    orderProductDO.setDepositCycle(customerRiskManagementDO.getNewDepositCycle());
                    orderProductDO.setPaymentCycle(customerRiskManagementDO.getNewPaymentCycle());
                    if (!orderProductDO.getPayMode().equals(customerRiskManagementDO.getNewPayMode())) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_ERROR);
                    }
                } else {
                    orderProductDO.setDepositCycle(customerRiskManagementDO.getDepositCycle());
                    orderProductDO.setPaymentCycle(customerRiskManagementDO.getPaymentCycle());
                }
            }
        }

        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterialDO.getIsNewMaterial())) {
                    orderMaterialDO.setDepositCycle(customerRiskManagementDO.getNewDepositCycle());
                    orderMaterialDO.setPaymentCycle(customerRiskManagementDO.getNewPaymentCycle());
                    if (!orderMaterialDO.getPayMode().equals(customerRiskManagementDO.getNewPayMode())) {
                        throw new BusinessException(ErrorCode.ORDER_PAY_MODE_ERROR);
                    }
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
        List<OrderProduct> productSkuList = OrderConverter.convertOrderProductDOList(orderProductDOList);
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
        Map<String, OrderProductDO> dbOrderProductDOMap = ListUtil.listToMap(dbOrderProductDOList, "productSkuId", "rentType", "rentTimeLength");
        for (OrderProductDO orderProductDO : orderProductDOList) {
            String key = orderProductDO.getProductSkuId() + "-" + orderProductDO.getRentType() + "-" + orderProductDO.getRentTimeLength();
            if (dbOrderProductDOMap.get(key) != null) {
                orderProductDO.setId(dbOrderProductDOMap.get(key).getId());
                updateOrderProductDOMap.put(orderProductDO.getProductSkuId(), orderProductDO);
                dbOrderProductDOMap.remove(key);
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

        Map<Integer, OrderMaterialDO> saveOrderMaterialDOMap = new HashMap<>();
        Map<Integer, OrderMaterialDO> updateOrderMaterialDOMap = new HashMap<>();
        List<OrderMaterialDO> dbOrderMaterialDOList = orderMaterialMapper.findByOrderId(orderId);
        Map<String, OrderMaterialDO> dbOrderMaterialDOMap = ListUtil.listToMap(dbOrderMaterialDOList, "materialId", "rentType", "rentTimeLength");
        for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
            String key = orderMaterialDO.getMaterialId() + "-" + orderMaterialDO.getRentType() + "-" + orderMaterialDO.getRentTimeLength();
            if (dbOrderMaterialDOMap.get(key) != null) {
                updateOrderMaterialDOMap.put(orderMaterialDO.getMaterialId(), orderMaterialDO);
                dbOrderMaterialDOMap.remove(key);
            } else {
                saveOrderMaterialDOMap.put(orderMaterialDO.getMaterialId(), orderMaterialDO);
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
            BigDecimal productAmountTotal = new BigDecimal(0.0);
            BigDecimal totalInsuranceAmount = new BigDecimal(0.0);
            BigDecimal totalDepositAmount = new BigDecimal(0.0);
            BigDecimal totalCreditDepositAmount = new BigDecimal(0.0);
            for (OrderProductDO orderProductDO : orderProductDOList) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(orderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                orderProductDO.setProductName(product.getProductName());
                ProductSku thisProductSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (thisProductSku == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                BigDecimal depositAmount = BigDecimal.ZERO;
                BigDecimal creditDepositAmount = BigDecimal.ZERO;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProductDO.getRentType())) {
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, BigDecimalUtil.mul(thisProductSku.getSkuPrice(), new BigDecimal(orderProductDO.getProductCount())));
                    depositAmount = BigDecimalUtil.mul(thisProductSku.getSkuPrice(), new BigDecimal(orderProductDO.getProductCount()));
                } else {
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, BigDecimalUtil.mul(thisProductSku.getSkuPrice(), new BigDecimal(orderProductDO.getProductCount())));
                    creditDepositAmount = BigDecimalUtil.mul(thisProductSku.getSkuPrice(), new BigDecimal(orderProductDO.getProductCount()));
                }
                orderProductDO.setDepositAmount(depositAmount);
                orderProductDO.setCreditDepositAmount(creditDepositAmount);
                orderProductDO.setProductSkuName(thisProductSku.getSkuName());
                orderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(orderProductDO.getRentTimeLength())));
                orderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                orderProductDO.setInsuranceAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getInsuranceAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(orderProductDO.getRentTimeLength())));
                productCount += orderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, orderProductDO.getProductAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, orderProductDO.getInsuranceAmount());
            }
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMustDepositAmount(BigDecimalUtil.add(orderDO.getTotalMustDepositAmount(), totalDepositAmount));
            orderDO.setTotalProductCount(productCount);
            orderDO.setTotalProductAmount(productAmountTotal);
        }
    }

    private void calculateOrderMaterialInfo(List<OrderMaterialDO> orderMaterialDOList, OrderDO orderDO) {
        if (orderMaterialDOList != null && !orderMaterialDOList.isEmpty()) {

            int materialCount = 0;
            BigDecimal materialAmountTotal = new BigDecimal(0.0);
            BigDecimal totalInsuranceAmount = new BigDecimal(0.0);
            BigDecimal totalDepositAmount = new BigDecimal(0.0);
            BigDecimal totalCreditDepositAmount = new BigDecimal(0.0);
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
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterialDO.getRentType())) {
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, BigDecimalUtil.mul(material.getMaterialPrice(), new BigDecimal(orderMaterialDO.getMaterialCount())));
                    depositAmount = BigDecimalUtil.mul(material.getMaterialPrice(), new BigDecimal(orderMaterialDO.getMaterialCount()));
                } else {
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, material.getMaterialPrice());
                    creditDepositAmount = BigDecimalUtil.mul(material.getMaterialPrice(), new BigDecimal(orderMaterialDO.getMaterialCount()));
                }

                orderMaterialDO.setDepositAmount(depositAmount);
                orderMaterialDO.setCreditDepositAmount(creditDepositAmount);
                orderMaterialDO.setMaterialName(material.getMaterialName());
                orderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getRentTimeLength())));
                orderMaterialDO.setInsuranceAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getInsuranceAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderMaterialDO.getRentTimeLength())));
                orderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
                materialCount += orderMaterialDO.getMaterialCount();
                materialAmountTotal = BigDecimalUtil.add(materialAmountTotal, orderMaterialDO.getMaterialAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, orderMaterialDO.getInsuranceAmount());
            }
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMustDepositAmount(BigDecimalUtil.add(orderDO.getTotalMustDepositAmount(), totalDepositAmount));
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
            for (OrderProduct orderProduct : order.getOrderProductList()) {

                if (orderProduct.getPayMode() == null) {
                    return ErrorCode.ORDER_PAY_MODE_NOT_NULL;
                }
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
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
                if (orderMaterial.getPayMode() == null) {
                    return ErrorCode.ORDER_PAY_MODE_NOT_NULL;
                }
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
                    return ErrorCode.ORDER_PRODUCT_STOCK_INSUFFICIENT;
                }
            }
        }
        return ErrorCode.SUCCESS;
    }


    @Autowired
    private UserSupport userSupport;

    @Autowired
    private OrderMapper orderMapper;

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
    private WarehouseService warehouseService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CustomerSupport customerSupport;
}
