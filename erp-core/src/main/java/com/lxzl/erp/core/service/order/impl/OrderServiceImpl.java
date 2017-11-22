package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
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
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderConverter;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
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
        saveOrderConsignInfo(order.getCustomerConsignId(), orderDO.getId(), loginUser, currentTime);

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
        boolean isNeedVerify = false;
        for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
            ProductSkuDO productSkuDO = productSkuMapper.findById(orderProductDO.getProductSkuId());
            if (productSkuDO == null) {
                result.setErrorCode(ErrorCode.PRODUCT_SKU_NOT_NULL);
                return result;
            }
            BigDecimal productUnitAmount = null;
            if (OrderRentType.RENT_TYPE_TIME.equals(orderDO.getRentType())) {
                productUnitAmount = productSkuDO.getTimeRentPrice();
            } else if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                productUnitAmount = productSkuDO.getDayRentPrice();
            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
                productUnitAmount = productSkuDO.getMonthRentPrice();
            }
            // 订单价格低于商品租赁价，需要商务审批
            if (BigDecimalUtil.compare(orderProductDO.getProductUnitAmount(), productUnitAmount) < 0) {
                isNeedVerify = true;
            }
        }

        // TODO 先付后用的单子，如果没有付款就不能提交

        if (isNeedVerify) {
            ServiceResult<String, Integer> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderDO.getId(), verifyUser);
            if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
                result.setErrorCode(workflowCommitResult.getErrorCode());
                return result;
            }
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_VERIFYING);
            orderDO.setUpdateUser(loginUser.getUserId().toString());
            orderDO.setUpdateTime(currentTime);
            orderMapper.update(orderDO);
        } else {
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
            orderDO.setUpdateUser(loginUser.getUserId().toString());
            orderDO.setUpdateTime(currentTime);
            orderMapper.update(orderDO);
        }

        result.setResult(orderNo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean receiveVerifyResult(boolean verifyResult, Integer businessId) {
        try {
            Date currentTime = new Date();
            User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
            OrderDO orderDO = orderMapper.findByOrderId(businessId);
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
            logger.error("审批订单通知失败： {}", businessId);
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
        maps.put("oderQueryParam", orderQueryParam);

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

        orderQueryParam.setBuyerUserId(loginUser.getUserId());
        maps.put("oderQueryParam", orderQueryParam);

        Integer totalCount = orderMapper.findOrderCountByParams(maps);
        List<OrderDO> orderDOList = orderMapper.findOrderByParams(maps);
        Page<Order> page = new Page<>(OrderConverter.convertOrderDOList(orderDOList), totalCount, orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
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
        if (!OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(dbRecordOrder.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
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
        Warehouse targetWarehouse = null;
        for (Warehouse warehouse : warehouseList) {
            if (WarehouseType.WAREHOUSE_TYPE_DEFAULT.equals(warehouse.getWarehouseType())) {
                srcWarehouse = warehouse;
            } else if (WarehouseType.WAREHOUSE_TYPE_RENT.equals(warehouse.getWarehouseType())) {
                targetWarehouse = warehouse;
            }
        }
        if (srcWarehouse == null || targetWarehouse == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }


        Map<Integer, OrderProduct> orderProductMap = ListUtil.listToMap(order.getOrderProductList(), "orderProductId");
        Map<Integer, OrderMaterial> orderMaterialMap = ListUtil.listToMap(order.getOrderMaterialList(), "orderMaterialId");

        // 计算预计归还时间
        Date expectReturnTime = calculationOrderExpectReturnTime(dbRecordOrder.getRentStartTime(), dbRecordOrder.getRentType(), dbRecordOrder.getRentTimeLength());
        // 即将出库的设备ID
        List<Integer> productEquipmentIdList = new ArrayList<>();
        // 即将出库的散料ID
        List<Integer> bulkMaterialIdList = new ArrayList<>();
        // 订单项设备项
        List<OrderProductEquipmentDO> orderProductEquipmentDOList = new ArrayList<>();
        // 订单项设备项
        List<OrderMaterialBulkDO> orderMaterialBulkDOList = new ArrayList<>();

        // 判断挨个订单项，都要发货，并且整合发货的设备及物料
        for (OrderProductDO orderProductDO : dbRecordOrder.getOrderProductDOList()) {
            OrderProduct orderProduct = orderProductMap.get(orderProductDO.getId());
            if ((orderProduct.getEquipmentNoList() == null || orderProduct.getEquipmentNoList().size() == 0)) {
                throw new BusinessException(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_NULL);
            }
            if (orderProduct.getEquipmentNoList().size() != orderProductDO.getProductCount()) {
                throw new BusinessException(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR);
            }

            // 计算预计金额
            BigDecimal expectRentAmount = calculationOrderExpectRentAmount(orderProductDO.getProductUnitAmount(), dbRecordOrder.getRentType(), dbRecordOrder.getRentTimeLength());
            for (String equipmentNo : orderProduct.getEquipmentNoList()) {
                ProductEquipmentDO productEquipmentDO = productEquipmentMapper.findByEquipmentNo(equipmentNo);
                if (productEquipmentDO == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_EQUIPMENT_NOT_EXISTS);
                }
                if (!ProductEquipmentStatus.PRODUCT_EQUIPMENT_STATUS_IDLE.equals(productEquipmentDO.getEquipmentStatus())) {
                    throw new BusinessException(ErrorCode.PRODUCT_EQUIPMENT_IS_NOT_IDLE);
                }
                if (!productEquipmentDO.getSkuId().equals(orderProductDO.getProductSkuId())) {
                    throw new BusinessException(ErrorCode.ORDER_PRODUCT_EQUIPMENT_SKU_NOT_SAME);
                }
                if (!productEquipmentDO.getCurrentWarehouseId().equals(srcWarehouse.getWarehouseId())) {
                    throw new BusinessException(ErrorCode.PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE);
                }
                productEquipmentIdList.add(productEquipmentDO.getId());

                OrderProductEquipmentDO orderProductEquipmentDO = new OrderProductEquipmentDO();
                orderProductEquipmentDO.setOrderId(orderProductDO.getOrderId());
                orderProductEquipmentDO.setOrderProductId(orderProductDO.getId());
                orderProductEquipmentDO.setEquipmentId(productEquipmentDO.getId());
                orderProductEquipmentDO.setEquipmentNo(productEquipmentDO.getEquipmentNo());
                orderProductEquipmentDO.setExpectReturnTime(expectReturnTime);
                orderProductEquipmentDO.setExpectRentAmount(expectRentAmount);
                orderProductEquipmentDO.setCreateTime(currentTime);
                orderProductEquipmentDO.setCreateUser(loginUser.getUserId().toString());
                orderProductEquipmentDO.setUpdateTime(currentTime);
                orderProductEquipmentDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductEquipmentDOList.add(orderProductEquipmentDO);
            }

            orderProductDO.setEquipmentNoList(orderProduct.getEquipmentNoList());
        }

        for (OrderMaterialDO orderMaterialDO : dbRecordOrder.getOrderMaterialDOList()) {

            OrderMaterial orderMaterial = orderMaterialMap.get(orderMaterialDO.getId());
            if ((orderMaterial.getBulkMaterialNoList() == null || orderMaterial.getBulkMaterialNoList().size() == 0)) {
                throw new BusinessException(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_NULL);
            }
            if (orderMaterial.getBulkMaterialNoList().size() != orderMaterial.getMaterialCount()) {
                throw new BusinessException(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR);
            }

            BigDecimal expectRentAmount = calculationOrderExpectRentAmount(orderMaterialDO.getMaterialUnitAmount(), dbRecordOrder.getRentType(), dbRecordOrder.getRentTimeLength());
            for (String bulkMaterialNo : orderMaterial.getBulkMaterialNoList()) {
                BulkMaterialDO bulkMaterialDO = bulkMaterialMapper.findByNo(bulkMaterialNo);
                if (bulkMaterialDO == null) {
                    throw new BusinessException(ErrorCode.BULK_MATERIAL_NOT_EXISTS);
                }
                if (bulkMaterialDO.getCurrentEquipmentId() != null) {
                    throw new BusinessException(ErrorCode.BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT);
                }
                bulkMaterialIdList.add(bulkMaterialDO.getId());

                OrderMaterialBulkDO orderMaterialBulkDO = new OrderMaterialBulkDO();
                orderMaterialBulkDO.setOrderId(orderMaterialDO.getOrderId());
                orderMaterialBulkDO.setOrderMaterialId(orderMaterialDO.getId());
                orderMaterialBulkDO.setBulkMaterialId(bulkMaterialDO.getId());
                orderMaterialBulkDO.setBulkMaterialNo(bulkMaterialDO.getBulkMaterialNo());
                orderMaterialBulkDO.setExpectReturnTime(expectReturnTime);
                orderMaterialBulkDO.setExpectRentAmount(expectRentAmount);
                orderMaterialBulkDO.setCreateTime(currentTime);
                orderMaterialBulkDO.setCreateUser(loginUser.getUserId().toString());
                orderMaterialBulkDO.setUpdateTime(currentTime);
                orderMaterialBulkDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialBulkDOList.add(orderMaterialBulkDO);
            }
        }
        if (CollectionUtil.isNotEmpty(orderProductEquipmentDOList)) {
            orderProductEquipmentMapper.saveList(orderProductEquipmentDOList);
        }
        if (CollectionUtil.isNotEmpty(orderMaterialBulkDOList)) {
            orderMaterialBulkMapper.saveList(orderMaterialBulkDOList);
        }

        ProductOutStockParam productOutStockParam = new ProductOutStockParam();
        productOutStockParam.setSrcWarehouseId(srcWarehouse.getWarehouseId());
        productOutStockParam.setTargetWarehouseId(targetWarehouse.getWarehouseId());
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
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            } else {
                orderDO.setDepositCycle(customerRiskManagementDO.getDepositCycle());
                orderDO.setPaymentCycle(customerRiskManagementDO.getPaymentCycle());
            }
        }

        // 商品加上物料的总和，即为本次的总额度
        BigDecimal totalCreditDepositAmount = BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getTotalMaterialAmount());
        BigDecimal zero = new BigDecimal(0);
        if (!OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
            if (BigDecimalUtil.compare(zero, BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), totalCreditDepositAmount)) < 0) {
                throw new BusinessException("额度不够无法下单。");
            }
        }
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
        if (orderProductDOList != null && !orderProductDOList.isEmpty()) {
            for (OrderProductDO orderProductDO : orderProductDOList) {
                orderProductDO.setOrderId(orderId);
                orderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderProductDO.setCreateUser(loginUser.getUserId().toString());
                orderProductDO.setUpdateUser(loginUser.getUserId().toString());
                orderProductDO.setCreateTime(currentTime);
                orderProductDO.setUpdateTime(currentTime);
                orderProductMapper.save(orderProductDO);
            }
        }
    }

    private void saveOrderMaterialInfo(List<OrderMaterialDO> orderMaterialDOList, Integer orderId, User loginUser, Date currentTime) {
        if (orderMaterialDOList != null && !orderMaterialDOList.isEmpty()) {
            for (OrderMaterialDO orderMaterialDO : orderMaterialDOList) {
                orderMaterialDO.setOrderId(orderId);
                orderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                orderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                orderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                orderMaterialDO.setCreateTime(currentTime);
                orderMaterialDO.setUpdateTime(currentTime);
                orderMaterialMapper.save(orderMaterialDO);
            }
        }
    }

    private void saveOrderConsignInfo(Integer userConsignId, Integer orderId, User loginUser, Date currentTime) {
        CustomerConsignInfoDO userConsignInfoDO = customerConsignInfoMapper.findById(userConsignId);
        OrderConsignInfoDO orderConsignInfoDO = new OrderConsignInfoDO();
        orderConsignInfoDO.setOrderId(orderId);
        orderConsignInfoDO.setConsigneeName(userConsignInfoDO.getConsigneeName());
        orderConsignInfoDO.setConsigneePhone(userConsignInfoDO.getConsigneePhone());
        orderConsignInfoDO.setProvince(userConsignInfoDO.getProvince());
        orderConsignInfoDO.setCity(userConsignInfoDO.getCity());
        orderConsignInfoDO.setDistrict(userConsignInfoDO.getDistrict());
        orderConsignInfoDO.setAddress(userConsignInfoDO.getAddress());
        orderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
        orderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
        orderConsignInfoDO.setCreateTime(currentTime);
        orderConsignInfoDO.setUpdateTime(currentTime);
        orderConsignInfoMapper.save(orderConsignInfoDO);
    }

    private void calculateOrderProductInfo(List<OrderProductDO> orderProductDOList, OrderDO orderDO) {
        if (orderProductDOList != null && !orderProductDOList.isEmpty()) {
            int productCount = 0;
            BigDecimal productAmountTotal = new BigDecimal(0.0);
            BigDecimal totalInsuranceAmount = new BigDecimal(0.0);
            BigDecimal totalDepositAmount = new BigDecimal(0.0);
            BigDecimal totalCreditDepositAmount = new BigDecimal(0.0);
            for (OrderProductDO orderProductDO : orderProductDOList) {
                ServiceResult<String, Product> productServiceResult = productService.queryProductById(orderProductDO.getProductId(), orderProductDO.getProductSkuId());
                Product product = productServiceResult.getResult();
                orderProductDO.setProductName(product.getProductName());
                ProductSku thisProductSku = CollectionUtil.isNotEmpty(product.getProductSkuList()) ? product.getProductSkuList().get(0) : null;
                if (thisProductSku == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS);
                }
                BigDecimal productUnitAmount = null;
                if (OrderRentType.RENT_TYPE_TIME.equals(orderDO.getRentType())) {
                    productUnitAmount = thisProductSku.getTimeRentPrice();
                } else if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                    productUnitAmount = thisProductSku.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
                    productUnitAmount = thisProductSku.getMonthRentPrice();
                }
                totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, thisProductSku.getSkuPrice());
                orderProductDO.setProductSkuName(thisProductSku.getSkuName());
                orderProductDO.setProductUnitAmount(productUnitAmount);
                orderProductDO.setProductAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(productUnitAmount, new BigDecimal(orderProductDO.getProductCount())), new BigDecimal(orderDO.getRentTimeLength())));
                orderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                productCount += orderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, orderProductDO.getProductAmount());
            }
            orderDO.setTotalCreditDepositAmount(totalCreditDepositAmount);
            orderDO.setTotalInsuranceAmount(totalInsuranceAmount);
            orderDO.setTotalDepositAmount(totalDepositAmount);
            orderDO.setTotalProductCount(productCount);
            orderDO.setTotalProductAmount(BigDecimalUtil.mul(productAmountTotal, new BigDecimal(orderDO.getRentTimeLength())));
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
                Material material = materialServiceResult.getResult();
                if (material == null) {
                    throw new BusinessException(ErrorCode.MATERIAL_NOT_EXISTS);
                }
                orderMaterialDO.setMaterialName(material.getMaterialName());
                BigDecimal productUnitAmount = null;
                if (OrderRentType.RENT_TYPE_TIME.equals(orderDO.getRentType())) {
                    productUnitAmount = material.getTimeRentPrice();
                } else if (OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                    productUnitAmount = material.getDayRentPrice();
                } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderDO.getRentType())) {
                    productUnitAmount = material.getMonthRentPrice();
                }
                totalCreditDepositAmount = BigDecimalUtil.add(totalCreditDepositAmount, material.getMaterialPrice());
                orderMaterialDO.setMaterialName(material.getMaterialName());
                orderMaterialDO.setMaterialUnitAmount(productUnitAmount);
                orderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(BigDecimalUtil.mul(productUnitAmount, new BigDecimal(orderMaterialDO.getMaterialCount())), new BigDecimal(orderDO.getRentTimeLength())));
                orderMaterialDO.setMaterialSnapshot(FastJsonUtil.toJSONString(material));
                materialCount += orderMaterialDO.getMaterialCount();
                materialAmountTotal = BigDecimalUtil.add(materialAmountTotal, orderMaterialDO.getMaterialAmount());
            }
            orderDO.setTotalCreditDepositAmount(BigDecimalUtil.add(orderDO.getTotalCreditDepositAmount(), totalCreditDepositAmount));
            orderDO.setTotalInsuranceAmount(BigDecimalUtil.add(orderDO.getTotalInsuranceAmount(), totalInsuranceAmount));
            orderDO.setTotalDepositAmount(BigDecimalUtil.add(orderDO.getTotalDepositAmount(), totalDepositAmount));
            orderDO.setTotalMaterialCount(materialCount);
            orderDO.setTotalMaterialAmount(BigDecimalUtil.mul(materialAmountTotal, new BigDecimal(orderDO.getRentTimeLength())));
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
        if (order.getRentStartTime() == null) {
            return ErrorCode.ORDER_HAVE_NO_RENT_START_TIME;
        }
        if (order.getPayMode() == null) {
            return ErrorCode.ORDER_PAY_MODE_NOT_NULL;
        }
        if (order.getRentType() == null || order.getRentTimeLength() == null || order.getRentTimeLength() <= 0) {
            return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
        }
        if (CollectionUtil.isNotEmpty(order.getOrderProductList())) {
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                if (orderProduct.getProductSkuPropertyList() == null || orderProduct.getProductSkuPropertyList().isEmpty()) {
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
                if (orderMaterial.getMaterialId() == null) {
                    return ErrorCode.PARAM_IS_NOT_ENOUGH;
                }
                ServiceResult<String, Material> materialServiceResult =  materialService.queryMaterialById(orderMaterial.getMaterialId());

                if(!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())
                        || materialServiceResult.getResult() == null){
                    return ErrorCode.MATERIAL_NOT_EXISTS;
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
    private CustomerConsignInfoMapper customerConsignInfoMapper;

    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private MaterialService materialService;
}
