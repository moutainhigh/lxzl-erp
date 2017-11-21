package com.lxzl.erp.core.service.order.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderConverter;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerRiskManagementMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.*;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
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
        OrderDO orderDO = OrderConverter.convertOrder(order);
        calculateOrderProductInfo(orderProductDOList, orderDO);

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

        if (isNeedVerify) {
            ServiceResult<String, Integer> workflowCommitResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_ORDER_INFO, orderDO.getId(), verifyUser);
            if (!ErrorCode.SUCCESS.equals(workflowCommitResult.getErrorCode())) {
                result.setErrorCode(workflowCommitResult.getErrorCode());
                return result;
            }
            orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
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
    public ServiceResult<String, Integer> cancelOrder(String orderNo) {
        Date currentTime = new Date();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, Integer> result = new ServiceResult<>();
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
        result.setResult(orderDO.getId());
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
    public ServiceResult<String, Integer> deliveryOrder(Order order) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        OrderDO dbRecordOrder = orderMapper.findByOrderId(order.getOrderId());
        if (dbRecordOrder == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }
        if (!OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(dbRecordOrder.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        Map<Integer, OrderProduct> orderProductMap = ListUtil.listToMap(order.getOrderProductList(), "orderProductId");

        for (OrderProductDO orderProductDO : dbRecordOrder.getOrderProductDOList()) {
            if (orderProductDO.getEquipmentNoList() == null || orderProductDO.getEquipmentNoList().size() < orderProductDO.getProductCount()) {
                OrderProduct orderProduct = orderProductMap.get(orderProductDO.getId());
                if (orderProduct.getEquipmentNoList() == null || orderProduct.getEquipmentNoList().size() == 0) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_NOT_NULL);
                    return result;
                }

                // 判断该订单项还有几个设备没录入
                if ((orderProductDO.getEquipmentNoList().size() + orderProduct.getEquipmentNoList().size()) != orderProductDO.getProductCount()) {
                    result.setErrorCode(ErrorCode.ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR);
                    return result;
                }

                //TODO 发货设备
            }
            orderProductMap.remove(orderProductDO.getId());
        }

        dbRecordOrder.setDeliveryTime(currentTime);
        dbRecordOrder.setOrderStatus(OrderStatus.ORDER_STATUS_DELIVERED);
        dbRecordOrder.setUpdateUser(loginUser.getUserId().toString());
        dbRecordOrder.setUpdateTime(currentTime);
        orderMapper.update(dbRecordOrder);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(order.getOrderId());
        return result;
    }

    @Override
    public ServiceResult<String, Integer> confirmOrder(String orderNo) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
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
        result.setResult(orderDO.getId());
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

                // TODO 减库存
                ProductSkuDO productSkuDO = productSkuMapper.findById(orderProductDO.getProductSkuId());
                productSkuDO.setStock(productSkuDO.getStock() - orderProductDO.getProductCount());
                productSkuMapper.update(productSkuDO);

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
            CustomerRiskManagementDO customerRiskManagementDO = customerRiskManagementMapper.findByCustomerId(orderDO.getBuyerCustomerId());
            if (!OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                if (customerRiskManagementDO == null) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                } else {
                    orderDO.setDepositCycle(customerRiskManagementDO.getDepositCycle());
                    orderDO.setPaymentCycle(customerRiskManagementDO.getPaymentCycle());
                }
            }

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
                orderProductDO.setProductAmount(BigDecimalUtil.mul(productUnitAmount, new BigDecimal(orderProductDO.getProductCount())));
                orderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                productCount += orderProductDO.getProductCount();
                productAmountTotal = BigDecimalUtil.add(productAmountTotal, orderProductDO.getProductAmount());
            }
            BigDecimal zero = new BigDecimal(0);
            if (!OrderRentType.RENT_TYPE_DAY.equals(orderDO.getRentType())) {
                if (BigDecimalUtil.compare(zero, BigDecimalUtil.sub(BigDecimalUtil.sub(customerRiskManagementDO.getCreditAmount(), customerRiskManagementDO.getCreditAmountUsed()), totalCreditDepositAmount)) < 0) {
                    throw new BusinessException("额度不够无法下单。");
                }
            }
            orderDO.setTotalCreditDepositAmount(totalCreditDepositAmount);
            orderDO.setTotalInsuranceAmount(totalInsuranceAmount);
            orderDO.setTotalDepositAmount(totalDepositAmount);
            orderDO.setTotalProductCount(productCount);
            orderDO.setTotalProductAmount(BigDecimalUtil.mul(productAmountTotal, new BigDecimal(orderDO.getRentTimeLength())));
            orderDO.setTotalOrderAmount(BigDecimalUtil.sub(BigDecimalUtil.add(orderDO.getTotalProductAmount(), orderDO.getLogisticsAmount()), orderDO.getTotalDiscountAmount()));
        }
    }

    private String verifyCreateOrder(Order order) {
        if (order == null) {
            return ErrorCode.PARAM_IS_NOT_NULL;
        }
        if (order.getOrderProductList() == null || order.getOrderProductList().isEmpty()) {
            return ErrorCode.ORDER_PRODUCT_LIST_NOT_NULL;
        }
        if (order.getCustomerConsignId() == null) {
            return ErrorCode.ORDER_CUSTOMER_CONSIGN_NOT_NULL;
        }
        if (order.getPayMode() == null) {
            return ErrorCode.ORDER_PAY_MODE_NOT_NULL;
        }
        if (order.getRentType() == null || order.getRentTimeLength() == null || order.getRentTimeLength() <= 0) {
            return ErrorCode.ORDER_RENT_TYPE_OR_LENGTH_ERROR;
        }
        BigDecimal zero = new BigDecimal(0);
        for (OrderProduct orderProduct : order.getOrderProductList()) {
            if (orderProduct.getProductSkuPropertyList() == null || orderProduct.getProductSkuPropertyList().isEmpty()) {
                return ErrorCode.PRODUCT_IS_NULL_OR_NOT_EXISTS;
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
            if (productSkuDO.getStock() == null || productSkuDO.getStock() <= 0 || (productSkuDO.getStock() - orderProduct.getProductCount()) < 0) {
                return ErrorCode.ORDER_PRODUCT_STOCK_INSUFFICIENT;
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
    private ProductService productService;

    @Autowired
    private ProductEquipmentRepairRecordMapper productEquipmentRepairRecordMapper;

    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;

    @Autowired
    private CustomerRiskManagementMapper customerRiskManagementMapper;
}
