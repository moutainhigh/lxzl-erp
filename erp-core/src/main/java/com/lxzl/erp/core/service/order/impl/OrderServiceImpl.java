package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.CustomerConsignInfo;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.ProductOutStockParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderConverter;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
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

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createOrder(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        String verifyCreateOrderCode = verifyCreateOrder(order);
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

        orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount()), orderDO.getLogisticsAmount()), orderDO.getTotalDiscountAmount()));
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
    public ServiceResult<String, String> commitOrder(String orderNo, Integer verifyUser) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (!OrderStatus.ORDER_STATUS_WAIT_COMMIT.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        if (CollectionUtil.isEmpty(orderDO.getOrderProductDOList())) {
            result.setErrorCode(ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL);
            return result;
        }
        // 是否需要审批
        boolean isNeedVerify = false;
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
            if (OrderRentType.RENT_TYPE_TIME.equals(orderDO.getRentType())) {
                productUnitAmount = thisProductSku.getTimeRentPrice();
            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                productUnitAmount = thisProductSku.getDayRentPrice();
            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
                productUnitAmount = thisProductSku.getMonthRentPrice();
            }
            // 订单价格低于商品租赁价，需要商务审批
            if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), productUnitAmount) < 0) {
                isNeedVerify = true;
            }
        }
        for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
            ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(orderMaterialDO.getMaterialId());
            if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                throw new BusinessException(materialServiceResult.getErrorCode());
            }
            Material material = materialServiceResult.getResult();
            if (material == null) {
                throw new BusinessException(ErrorCode.MATERIAL_NOT_EXISTS);
            }
            BigDecimal materialUnitAmount = null;
            if (OrderRentType.RENT_TYPE_TIME.equals(orderDO.getRentType())) {
                materialUnitAmount = material.getTimeRentPrice();
            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                materialUnitAmount = material.getDayRentPrice();
            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
                materialUnitAmount = material.getMonthRentPrice();
            }
            // 订单价格低于商品租赁价，需要商务审批
            if (BigDecimalUtil.compare(orderMaterialDO.getMaterialUnitAmount(), materialUnitAmount) < 0) {
                isNeedVerify = true;
            }
        }

        // TODO 先付后用的单子，如果没有付款就不能提交

        if (isNeedVerify) {
            // 检查是否需要审批流程
            ServiceResult<String, Boolean> isMeedVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_ORDER_INFO);
            if (!ErrorCode.SUCCESS.equals(isMeedVerifyResult.getErrorCode())) {
                result.setErrorCode(isMeedVerifyResult.getErrorCode());
                return result;
            }
            if (isMeedVerifyResult.getResult()) {
                ServiceResult<String, String> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderDO.getOrderNo(), verifyUser, null);
                if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
                    result.setErrorCode(workflowCommitResult.getErrorCode());
                    return result;
                }
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_VERIFYING);
            } else {
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
            }
        } else {
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
        }

        orderDO.setUpdateUser(loginUser.getUserId().toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);

        result.setResult(orderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        try {
            Date currentTime = new Date();
            User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
            OrderDO orderDO = orderMapper.findByOrderNo(businessNo);
            if (orderDO == null || !OrderStatus.ORDER_STATUS_VERIFYING.equals(orderDO.getOrderStatus())) {
                return false;
            }
            if (verifyResult) {
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
            } else {
                orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_COMMIT);
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
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, Order> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (loginUser != null && !orderDO.getBuyerCustomerId().equals(loginUser.getUserId())) {
            result.setErrorCode(ErrorCode.OPERATOR_IS_NOT_YOURSELF);
            return result;
        }
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
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, String> result = new ServiceResult<>();
        if (orderNo == null) {
            result.setErrorCode(ErrorCode.ID_NOT_NULL);
            return result;
        }
        OrderDO orderDO = orderMapper.findByOrderNo(orderNo);
        if (loginUser != null && !orderDO.getBuyerCustomerId().equals(loginUser.getUserId())) {
            result.setErrorCode(ErrorCode.OPERATOR_IS_NOT_YOURSELF);
            return result;
        }
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
        customerSupport.subCreditAmountUsed(orderDO.getBuyerCustomerId(),orderDO.getTotalCreditDepositAmount());

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
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
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
    public ServiceResult<String, String> processOrder(ProcessOrderParam param) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        if (param == null) {
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
            String errorCode = addOrderItem(orderDO, srcWarehouse.getWarehouseId(), param.getEquipmentNo(), param.getBulkMaterialNo(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(errorCode)) {
                result.setErrorCode(errorCode);
                return result;
            }
        } else if (CommonConstant.COMMON_DATA_OPERATION_TYPE_DELETE.equals(param.getOperationType())) {
            String errorCode = removeOrderItem(orderDO, param.getEquipmentNo(), param.getBulkMaterialNo(), loginUser.getUserId(), currentTime);
            if (!ErrorCode.SUCCESS.equals(errorCode)) {
                result.setErrorCode(errorCode);
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

    private String addOrderItem(OrderDO orderDO, Integer srcWarehouseId, String equipmentNo, String bulkMaterialNo, Integer loginUserId, Date currentTime) {
        // 计算预计归还时间
        Date expectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderDO.getRentType(), orderDO.getRentTimeLength());
        if (equipmentNo != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
            if (productEquipmentDO == null) {
                return ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())) {
                return ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE;
            }
            if (!srcWarehouseId.equals(productEquipmentDO.getCurrentWarehouseId())) {
                return ErrorCode.PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE;
            }
            Map<Integer, OrderProductDO> orderProductDOMap = ListUtil.listToMap(orderDO.getOrderProductDOList(), "productSkuId");
            OrderProductDO orderProductDO = orderProductDOMap.get(productEquipmentDO.getSkuId());
            if (orderProductDO == null) {
                return ErrorCode.ORDER_HAVE_NO_THIS_ITEM;
            }
            List<OrderProductEquipmentDO> orderProductEquipmentDOList = orderProductEquipmentMapper.findByOrderProductId(orderProductDO.getId());
            if (orderProductEquipmentDOList != null && orderProductEquipmentDOList.size() >= orderProductDO.getProductCount()) {
                return ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_MAX;
            }

            productEquipmentDO.setEquipmentStatus(ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY);
            productEquipmentDO.setOrderNo(orderDO.getOrderNo());
            productEquipmentDO.setUpdateTime(currentTime);
            productEquipmentDO.setUpdateUser(loginUserId.toString());
            productEquipmentMapper.update(productEquipmentDO);

            BigDecimal expectRentAmount = calculationOrderExpectRentAmount(orderProductDO.getProductUnitAmount(), orderDO.getRentType(), orderDO.getRentTimeLength());

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
        }
        if (bulkMaterialNo != null) {
            BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(bulkMaterialNo);
            if (bulkMaterialDO == null) {
                return ErrorCode.BULK_MATERIAL_NOT_EXISTS;
            }
            if (!BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE.equals(bulkMaterialDO.getBulkMaterialStatus())) {
                return ErrorCode.BULK_MATERIAL_IS_NOT_IDLE;
            }
            if (!srcWarehouseId.equals(bulkMaterialDO.getCurrentWarehouseId())) {
                return ErrorCode.PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE;
            }

            Map<Integer, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "materialId");
            OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(bulkMaterialDO.getMaterialId());
            if (orderMaterialDO == null) {
                return ErrorCode.ORDER_HAVE_NO_THIS_ITEM;
            }
            List<OrderMaterialBulkDO> orderMaterialBulkDOList = orderMaterialBulkMapper.findByOrderMaterialId(orderMaterialDO.getId());
            if (orderMaterialBulkDOList != null && orderMaterialBulkDOList.size() >= orderMaterialDO.getMaterialCount()) {
                return ErrorCode.ORDER_MATERIAL_BULK_COUNT_MAX;
            }
            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
            bulkMaterialDO.setOrderNo(orderDO.getOrderNo());
            bulkMaterialDO.setUpdateTime(currentTime);
            bulkMaterialDO.setUpdateUser(loginUserId.toString());
            bulkMaterialMapper.update(bulkMaterialDO);

            BigDecimal expectRentAmount = calculationOrderExpectRentAmount(orderMaterialDO.getMaterialUnitAmount(), orderDO.getRentType(), orderDO.getRentTimeLength());

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
        }

        return ErrorCode.SUCCESS;
    }

    private String removeOrderItem(OrderDO orderDO, String equipmentNo, String bulkMaterialNo, Integer loginUserId, Date currentTime) {

        if (equipmentNo != null) {
            ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
            if (productEquipmentDO == null) {
                return ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS;
            }
            if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_BUSY.equals(productEquipmentDO.getEquipmentStatus())) {
                return ErrorCode.PRODUCT_EQUIPMENT_STATUS_ERROR;
            }
            OrderProductEquipmentDO orderProductEquipmentDO = orderProductEquipmentMapper.findByOrderIdAndEquipmentNo(orderDO.getId(), productEquipmentDO.getEquipmentNo());
            if (orderProductEquipmentDO == null) {
                return ErrorCode.PARAM_IS_ERROR;
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
        if (bulkMaterialNo != null) {
            BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(bulkMaterialNo);
            if (bulkMaterialDO == null) {
                return ErrorCode.BULK_MATERIAL_NOT_EXISTS;
            }
            if (!BulkMaterialStatus.BULK_MATERIAL_STATUS_IDLE.equals(bulkMaterialDO.getBulkMaterialStatus())) {
                return ErrorCode.BULK_MATERIAL_IS_NOT_IDLE;
            }

            Map<Integer, OrderMaterialDO> orderMaterialDOMap = ListUtil.listToMap(orderDO.getOrderMaterialDOList(), "materialId");
            OrderMaterialDO orderMaterialDO = orderMaterialDOMap.get(bulkMaterialDO.getMaterialId());
            if (orderMaterialDO == null) {
                return ErrorCode.ORDER_HAVE_NO_THIS_ITEM;
            }
            bulkMaterialDO.setBulkMaterialStatus(BulkMaterialStatus.BULK_MATERIAL_STATUS_BUSY);
            bulkMaterialDO.setOrderNo("");
            bulkMaterialDO.setUpdateTime(currentTime);
            bulkMaterialDO.setUpdateUser(loginUserId.toString());
            bulkMaterialMapper.update(bulkMaterialDO);

            OrderMaterialBulkDO orderMaterialBulkDO = orderMaterialBulkMapper.findByOrderIdAndBulkMaterialNo(orderDO.getId(), bulkMaterialDO.getBulkMaterialNo());
            if (orderMaterialBulkDO == null) {
                return ErrorCode.PARAM_IS_ERROR;
            }

            orderMaterialBulkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            orderMaterialBulkDO.setCreateTime(currentTime);
            orderMaterialBulkDO.setCreateUser(loginUserId.toString());
            orderMaterialBulkDO.setUpdateTime(currentTime);
            orderMaterialBulkDO.setUpdateUser(loginUserId.toString());
            orderMaterialBulkMapper.save(orderMaterialBulkDO);
        }

        return ErrorCode.SUCCESS;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> deliveryOrder(Order order) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
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
        // 即将出库的设备ID
        List<Integer> productEquipmentIdList = new ArrayList<>();
        // 即将出库的散料ID
        List<Integer> bulkMaterialIdList = new ArrayList<>();

        Integer srcWarehouseId = null;
        Integer targetWarehouseId = null;
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
            }
        }

        // 计算预计归还时间
        Date expectReturnTime = calculationOrderExpectReturnTime(dbRecordOrder.getRentStartTime(), dbRecordOrder.getRentType(), dbRecordOrder.getRentTimeLength());

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
        } else if (OrderRentType.RENT_TYPE_TIME.equals(rentType)) {
            // TODO 按次的情况下，什么时候还
        }
        return null;
    }

    private BigDecimal calculationOrderExpectRentAmount(BigDecimal unitAmount, Integer rentType, Integer rentTimeLength) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return BigDecimalUtil.mul(unitAmount, new BigDecimal(rentTimeLength));
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return BigDecimalUtil.mul(unitAmount, new BigDecimal(rentTimeLength));
        } else if (OrderRentType.RENT_TYPE_TIME.equals(rentType)) {
            return unitAmount;
        }
        return null;
    }

    public void verifyCustomerRiskInfo(OrderDO orderDO) {
        CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
        if (!OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
            if (customerRiskManagementDO == null) {
                throw new BusinessException(ErrorCode.CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS);
            } else {
                orderDO.setDepositCycle(customerRiskManagementDO.getDepositCycle());
                orderDO.setPaymentCycle(customerRiskManagementDO.getPaymentCycle());
            }
        }

        BigDecimal totalCreditDepositAmount = orderDO.getTotalCreditDepositAmount();
        if (!OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
            if (BigDecimalUtil.compare(BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), totalCreditDepositAmount), BigDecimal.ZERO) < 0) {
                throw new BusinessException("额度不够无法下单。");
            }
        }

        customerSupport.addCreditAmountUsed(customerRiskManagementDO.getCustomerId(),totalCreditDepositAmount);
    }

    @Override
    public ServiceResult<String, String> confirmOrder(String orderNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

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
        if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
            orderDO.setExpectReturnTime(DateUtil.monthInterval(currentTime, orderDO.getRentTimeLength()));
        }
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
        Map<Integer, OrderProductDO> dbOrderProductDOMap = ListUtil.listToMap(dbOrderProductDOList, "productSkuId");
        for (OrderProductDO orderProductDO : orderProductDOList) {
            if (dbOrderProductDOMap.get(orderProductDO.getProductSkuId()) != null) {
                updateOrderProductDOMap.put(orderProductDO.getProductSkuId(), orderProductDO);
                dbOrderProductDOMap.remove(orderProductDO.getProductSkuId());
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
        Map<Integer, OrderMaterialDO> dbOrderMaterialDOMap = ListUtil.listToMap(dbOrderMaterialDOList, "materialId");
        for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
            if (dbOrderMaterialDOMap.get(orderMaterialDO.getMaterialId()) != null) {
                updateOrderMaterialDOMap.put(orderMaterialDO.getMaterialId(), orderMaterialDO);
                dbOrderMaterialDOMap.remove(orderMaterialDO.getMaterialId());
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
            orderConsignInfoDO.setId(dbOrderConsignInfoDO.getId());
            orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            orderConsignInfoDO.setUpdateTime(currentTime);
            orderConsignInfoMapper.update(orderConsignInfoDO);
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
                if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, thisProductSku.getSkuPrice());
                } else {
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, thisProductSku.getSkuPrice());
                }
                orderProductDO.setProductSkuName(thisProductSku.getSkuName());
                orderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getProductUnitAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(orderDO.getRentTimeLength())));
                orderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                orderProductDO.setInsuranceAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderProductDO.getInsuranceAmount(), new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(orderDO.getRentTimeLength())));
                productCount += orderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, orderProductDO.getProductAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, orderProductDO.getInsuranceAmount());
            }
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
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
                if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                    totalDepositAmount = BigDecimalUtil.add(totalDepositAmount, material.getMaterialPrice());
                } else {
                    totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, material.getMaterialPrice());
                }

                orderMaterialDO.setMaterialName(material.getMaterialName());
                orderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getMaterialUnitAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderDO.getRentTimeLength())));
                orderMaterialDO.setInsuranceAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(orderMaterialDO.getInsuranceAmount(), new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderDO.getRentTimeLength())));
                orderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
                materialCount += orderMaterialDO.getMaterialCount();
                materialAmountTotal = BigDecimalUtil.add(materialAmountTotal, orderMaterialDO.getMaterialAmount());
                totalInsuranceAmount = BigDecimalUtil.add(totalInsuranceAmount, orderMaterialDO.getInsuranceAmount());
            }
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMaterialCount(materialCount);
            orderDO.setTotalMaterialAmount(materialAmountTotal);
        }
    }

    private String verifyCreateOrder(Order order) {
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
        if (order.getPayMode() == null) {
            return ErrorCode.ORDER_PAY_MODE_NOT_NULL;
        }
        if (order.getRentType() == null || order.getRentTimeLength() == null || order.getRentTimeLength() <= 0) {
            return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
        }
        if (!OrderRentType.inThisScope(order.getRentType())) {
            return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
        }
        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                if (CollectionUtil.isEmpty(orderProduct.getProductSkuPropertyList()) || orderProduct.getProductCount() == null) {
                    return ErrorCode.PARAM_IS_NOT_ENOUGH;
                }
                List<Integer> propertyValueIdList = new ArrayList<>();
                for (ProductSkuProperty productSkuProperty : orderProduct.getProductSkuPropertyList()) {
                    propertyValueIdList.add(productSkuProperty.getPropertyValueId());
                }
                if (propertyValueIdList.size() == 0) {
                    return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
                }
                if (orderProduct.getProductCount() == null || orderProduct.getProductCount() <= 0) {
                    return ErrorCode.PARAM_IS_NOT_NULL;
                }
                if (orderProduct.getProductUnitAmount() == null || BigDecimalUtil.compare(orderProduct.getProductUnitAmount(), BigDecimal.ZERO) < 0) {
                    return ErrorCode.ORDER_PRODUCT_AMOUNT_ERROR;
                }

                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", orderProduct.getProductId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    return ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS;
                }

                orderProduct.setProductSkuId(skuId);
                ServiceResult<String, Product> productServiceResult = productService.queryProductById(orderProduct.getProductId());
                ProductSkuDO productSkuDO = productSkuMapper.findById(orderProduct.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode()) || productServiceResult.getResult() == null || productSkuDO == null) {
                    return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
                }
                Product product = productServiceResult.getResult();
                if (CommonConstant.COMMON_CONSTANT_NO.equals(product.getIsRent())) {
                    return ErrorCode.PRODUCT_IS_NOT_RENT;
                }
                if (productSkuDO.getStock() == null || productSkuDO.getStock() <= 0 || (productSkuDO.getStock() - orderProduct.getProductCount()) < 0) {
                    return ErrorCode.ORDER_PRODUCT_STOCK_INSUFFICIENT;
                }
            }
        }

        if (CollectionUtil.isNotEmpty(order.getOrderMaterialList())) {
            for (OrderMaterial orderMaterial : order.getOrderMaterialList()) {
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


    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private OrderConsignInfoMapper orderConsignInfoMapper;

    @Autowired
    private ProductSkuPropertyMapper productSkuPropertyMapper;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ProductSkuMapper productSkuMapper;

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
