package com.lxzl.erp.core.service.k3.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderStatus;
import com.lxzl.erp.common.constant.ReturnOrderStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrder;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrderMaterial;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrderProduct;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.thread.ThreadFactoryDefault;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.customer.impl.support.CustomerSupport;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.K3CallbackService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.delivery.DeliveryOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.delivery.DeliveryOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.delivery.DeliveryOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingSubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderDO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderProductDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingSubCompanyDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-19 13:37
 */

@Service("k3CallbackService")
public class K3CallbackServiceImpl implements K3CallbackService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private ExecutorService k3HnadleExecutor = Executors.newCachedThreadPool(new ThreadFactoryDefault("k3HnadleExecutor"));

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> callbackDelivery(DeliveryOrder deliveryOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        OrderDO orderDO = orderMapper.findByOrderNo(deliveryOrder.getOrderNo());
        if (orderDO == null) {
            result.setErrorCode(ErrorCode.ORDER_NOT_EXISTS);
            return result;
        }

        K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByK3Code(deliveryOrder.getSubCompanyCode());
        SubCompanyDO subCompanyDO = subCompanyMapper.findBySubCompanyCode(k3MappingSubCompanyDO.getErpSubCompanyCode());
        if (subCompanyDO == null) {
            result.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
            return result;
        }

        if (!OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderDO.getOrderStatus())
                && !OrderStatus.ORDER_STATUS_DELIVERED.equals(orderDO.getOrderStatus())) {
            result.setErrorCode(ErrorCode.ORDER_STATUS_ERROR);
            return result;
        }
        Date deliveryTime = null;
        try {
            deliveryTime = deliveryOrder.getDeliveryTimeStr() == null ? currentTime : new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(deliveryOrder.getDeliveryTimeStr());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        orderDO.setDeliveryTime(deliveryTime);
        orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_DELIVERED);
        orderDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
        orderDO.setUpdateTime(currentTime);
        orderMapper.update(orderDO);

        DeliveryOrderDO deliveryOrderDO = ConverterUtil.convert(deliveryOrder, DeliveryOrderDO.class);
        deliveryOrderDO.setOrderId(orderDO.getId());
        deliveryOrderDO.setDeliveryTime(deliveryTime);
        deliveryOrderDO.setSubCompanyId(subCompanyDO.getId());
        deliveryOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        deliveryOrderDO.setCreateTime(currentTime);
        deliveryOrderDO.setCreateUser(CommonConstant.SUPER_USER_ID.toString());
        deliveryOrderDO.setUpdateTime(currentTime);
        deliveryOrderDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
        deliveryOrderMapper.save(deliveryOrderDO);


        if (CollectionUtil.isNotEmpty(deliveryOrder.getDeliveryOrderProductList())) {
            for (DeliveryOrderProduct deliveryOrderProduct : deliveryOrder.getDeliveryOrderProductList()) {
                OrderProductDO orderProductDO = orderProductMapper.findById(deliveryOrderProduct.getOrderProductId());
                DeliveryOrderProductDO deliveryOrderProductDO = ConverterUtil.convert(deliveryOrderProduct, DeliveryOrderProductDO.class);
                deliveryOrderProductDO.setDeliveryOrderId(deliveryOrderDO.getId());
                if (orderProductDO == null) {
                    continue;
                }
                orderProductDO.setRentingProductCount(orderProductDO.getProductCount());
                orderProductMapper.update(orderProductDO);
                deliveryOrderProductDO.setOrderProductId(orderProductDO.getId());
                deliveryOrderProductDO.setProductId(orderProductDO.getProductId());
                deliveryOrderProductDO.setProductSkuId(orderProductDO.getProductSkuId());
                deliveryOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deliveryOrderProductDO.setCreateTime(currentTime);
                deliveryOrderProductDO.setCreateUser(CommonConstant.SUPER_USER_ID.toString());
                deliveryOrderProductDO.setUpdateTime(currentTime);
                deliveryOrderProductDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
                deliveryOrderProductMapper.save(deliveryOrderProductDO);

            }
        }

        if (CollectionUtil.isNotEmpty(deliveryOrder.getDeliveryOrderMaterialList())) {
            for (DeliveryOrderMaterial deliveryOrderMaterial : deliveryOrder.getDeliveryOrderMaterialList()) {
                OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(deliveryOrderMaterial.getOrderMaterialId());
                DeliveryOrderMaterialDO deliveryOrderMaterialDO = ConverterUtil.convert(deliveryOrderMaterial, DeliveryOrderMaterialDO.class);
                deliveryOrderMaterialDO.setDeliveryOrderId(deliveryOrderDO.getId());
                if (orderMaterialDO == null) {
                    continue;
                }
                orderMaterialDO.setRentingMaterialCount(orderMaterialDO.getMaterialCount());
                orderMaterialMapper.update(orderMaterialDO);
                deliveryOrderMaterialDO.setOrderMaterialId(orderMaterialDO.getId());
                deliveryOrderMaterialDO.setMaterialId(orderMaterialDO.getMaterialId());
                deliveryOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                deliveryOrderMaterialDO.setCreateTime(currentTime);
                deliveryOrderMaterialDO.setCreateUser(CommonConstant.SUPER_USER_ID.toString());
                deliveryOrderMaterialDO.setUpdateTime(currentTime);
                deliveryOrderMaterialDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
                deliveryOrderMaterialMapper.save(deliveryOrderMaterialDO);
            }
        }

        // 记录订单时间轴
        orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, currentTime, CommonConstant.SUPER_USER_ID);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> callbackCancelOrder(String orderNo,Integer cancelOrderReasonType) {
        return orderService.cancelOrder(orderNo,cancelOrderReasonType);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> callbackReturnOrder(K3ReturnOrder k3ReturnOrder) {
        String json = JSON.toJSONString(k3ReturnOrder);
        logger.info("return order call back : "+json);
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrder.getReturnOrderNo());

        if(k3ReturnOrderDO==null){
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if(!ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(k3ReturnOrderDO.getReturnOrderStatus())){
            serviceResult.setErrorCode(ErrorCode.RETURN_ORDER_STATUS_CAN_NOT_RETURN);
            return serviceResult;
        }

        ServiceResult<String, String> result = callbackReturnDetail(k3ReturnOrder,k3ReturnOrderDO);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }

        return result;
    }
    @Override
    public ServiceResult<String, String> callbackReturnDetail(K3ReturnOrder k3ReturnOrder,K3ReturnOrderDO k3ReturnOrderDO){
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        String userId = null;
        if(StringUtil.isNotBlank(k3ReturnOrder.getUpdateUserRealName())){
            UserDO userDO = userMapper.findByUserRealName(k3ReturnOrder.getUpdateUserRealName().trim());
            if(userDO==null){
                serviceResult.setErrorCode(ErrorCode.USER_NOT_EXISTS);
                return serviceResult;
            }
            userId = userDO.getId().toString();

        }
        BigDecimal b = k3ReturnOrder.getEqAmount();
        if (BigDecimalUtil.compare(b, BigDecimal.ZERO) != 0) {
            K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByK3Code(k3ReturnOrderDO.getK3CustomerNo());
            CustomerDO customerDO = null;
            if(k3MappingCustomerDO == null){
                customerDO = customerMapper.findByNo(k3ReturnOrderDO.getK3CustomerNo());
            }else{
                customerDO = customerMapper.findByNo(k3MappingCustomerDO.getErpCustomerCode());
            }
            if(customerDO!=null){
                customerSupport.subCreditAmountUsed(customerDO.getId(), b);
            }
        }

        Date now = new Date();
        Integer returnOrderStatus =  k3ReturnOrder.getReturnOrderStatus()==null?ReturnOrderStatus.RETURN_ORDER_STATUS_END:k3ReturnOrder.getReturnOrderStatus();
        k3ReturnOrderDO.setReturnOrderStatus(returnOrderStatus);
        k3ReturnOrderDO.setUpdateTime(now);
        k3ReturnOrderDO.setUpdateUser(userId);
        k3ReturnOrderMapper.update(k3ReturnOrderDO);
        //退货状态为退货完成的，才修改在租数及考虑退货结算
        if(ReturnOrderStatus.RETURN_ORDER_STATUS_END.equals(returnOrderStatus)){
            List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList = k3ReturnOrderDO.getK3ReturnOrderDetailDOList();
            Set<Integer> set = new HashSet();
            for(K3ReturnOrderDetailDO k3ReturnOrderDetailDO : k3ReturnOrderDetailDOList){
                if (productSupport.isProduct(k3ReturnOrderDetailDO.getProductNo())) {
                    //兼容erp订单和k3订单商品项
                    OrderProductDO orderProductDO = productSupport.getOrderProductDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                    if(orderProductDO!=null){
                        Integer productCount = orderProductDO.getRentingProductCount() - k3ReturnOrderDetailDO.getProductCount();
                        if(productCount<0){
                            dingDingSupport.dingDingSendMessage(dingDingSupport.getEnvironmentString()+"订单ID["+orderProductDO.getOrderId()+"]商品项ID["+orderProductDO.getId()+")]退货后数量为"+(productCount)+"台");
                        }
                        productCount = productCount<0?0:productCount;
                        orderProductDO.setRentingProductCount(productCount);
                        orderProductDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
                        orderProductDO.setUpdateTime(now);
                        orderProductMapper.update(orderProductDO);
                        set.add(orderProductDO.getOrderId());
                        k3ReturnOrderDetailDO.setRealProductCount(k3ReturnOrderDetailDO.getProductCount());
                        k3ReturnOrderDetailDO.setUpdateUser(userId);
                        k3ReturnOrderDetailDO.setUpdateTime(now);
                        k3ReturnOrderDetailMapper.update(k3ReturnOrderDetailDO);
                    }else{
                        dingDingSupport.dingDingSendMessage(dingDingSupport.getEnvironmentString()+"未找到退货单项对应的商品项，退货单编号["+k3ReturnOrderDO.getReturnOrderNo()+"]，退货单项ID["+k3ReturnOrderDetailDO.getId()+"]");
                    }

                } else {
                    //兼容erp订单和k3订单配件项
                    OrderMaterialDO orderMaterialDO = productSupport.getOrderMaterialDO(k3ReturnOrderDetailDO.getOrderNo(),k3ReturnOrderDetailDO.getOrderItemId(),k3ReturnOrderDetailDO.getOrderEntry());
                    if(orderMaterialDO!=null){
                        Integer materialCount = orderMaterialDO.getRentingMaterialCount()-k3ReturnOrderDetailDO.getProductCount();
                        if(materialCount<0){
                            dingDingSupport.dingDingSendMessage(dingDingSupport.getEnvironmentString()+"订单ID["+orderMaterialDO.getOrderId()+"]商品项ID["+orderMaterialDO.getId()+")]退货后数量为"+(materialCount)+"台");
                        }
                        materialCount = materialCount<0?0:materialCount;
                        orderMaterialDO.setRentingMaterialCount(materialCount);
                        orderMaterialDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
                        orderMaterialDO.setUpdateTime(now);
                        orderMaterialMapper.update(orderMaterialDO);
                        set.add(orderMaterialDO.getOrderId());
                        k3ReturnOrderDetailDO.setRealProductCount(k3ReturnOrderDetailDO.getProductCount());
                        k3ReturnOrderDetailDO.setUpdateUser(userId);
                        k3ReturnOrderDetailDO.setUpdateTime(now);
                        k3ReturnOrderDetailMapper.update(k3ReturnOrderDetailDO);
                    }else{
                        dingDingSupport.dingDingSendMessage(dingDingSupport.getEnvironmentString()+"未找到退货单项对应的配件项，退货单编号["+k3ReturnOrderDO.getReturnOrderNo()+"]，退货单项ID["+k3ReturnOrderDetailDO.getId()+"]");
                    }

                }
            }
            //是否生成退货结算单
            Boolean isCreateReturnStatement = true;
            for (Integer orderId : set) {

                Integer totalRentingProductCount = orderProductMapper.findTotalRentingProductCountByOrderId(orderId);
                totalRentingProductCount = totalRentingProductCount == null ? 0 : totalRentingProductCount;
                Integer totalRentingMaterialCount = orderMaterialMapper.findTotalRentingMaterialCountByOrderId(orderId);
                totalRentingMaterialCount = totalRentingMaterialCount == null ? 0 : totalRentingMaterialCount;

                OrderDO orderDO = orderMapper.findById(orderId);
                if(orderDO == null){
                    continue;
                }
                //如果訂單有未支付的就將標記改成false
//                if (isCreateReturnStatement && !PayStatus.PAY_STATUS_PAID.equals(orderDO.getPayStatus())) {
//                    isCreateReturnStatement = false;
//                }
                if (totalRentingProductCount==0 && totalRentingMaterialCount==0) {
                    //处理最后一件商品退还时间
                    List<K3ReturnOrderDetailDO> list = k3ReturnOrderDetailMapper.findListByOrderNo(orderDO.getOrderNo());
                    Date max = null;
                    for(K3ReturnOrderDetailDO k3ReturnOrderDetailDO : list){
                        K3ReturnOrderDO returnOrderDO = k3ReturnOrderMapper.findByNo(k3ReturnOrderDetailDO.getReturnOrderNo());
                        if(max==null){
                            max = returnOrderDO.getReturnTime();
                        }else{
                            max = max.getTime()<returnOrderDO.getReturnTime().getTime()?returnOrderDO.getReturnTime():max;
                        }
                    }
                    orderDO.setActualReturnTime(max);
                    orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_RETURN_BACK);
                    orderDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
                    orderDO.setUpdateTime(now);
                    orderMapper.update(orderDO);
                }else if(orderDO.getTotalProductCount()>totalRentingProductCount||orderDO.getTotalMaterialCount()>totalRentingMaterialCount){//部分退货
                    orderDO.setOrderStatus(OrderStatus.ORDER_STATUS_PART_RETURN);
                    orderDO.setUpdateUser(CommonConstant.SUPER_USER_ID.toString());
                    orderDO.setUpdateTime(now);
                    orderMapper.update(orderDO);
                }
                // 记录订单时间轴
                orderTimeAxisSupport.addOrderTimeAxis(orderDO.getId(), orderDO.getOrderStatus(), null, now, CommonConstant.SUPER_USER_ID.toString());
            }

            // 如果退货单关联的所有订单都支付了，才生成退货单结算单,如果该方法返回错误代码，则内部会自动回滚，结算状态不会改变
            // 如果该方法抛出异常，内部会自动回滚，这里捕获异常，结算状态不改变但是不影响其他逻辑，发送钉钉通知
            if (isCreateReturnStatement) {
                // 设置事务回滚点
                Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
                try{
                    ServiceResult<String,BigDecimal> result = statementService.createK3ReturnOrderStatementThrowException(k3ReturnOrderDO.getReturnOrderNo());
                    if(!ErrorCode.SUCCESS.equals(result.getErrorCode())){
                        // 创建结算单部分回滚
                        TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
                        dingDingSupport.dingDingSendMessage(dingDingSupport.getEnvironmentString()+"退货单["+k3ReturnOrderDO.getReturnOrderNo()+"]生成结算单失败："+JSON.toJSONString(resultGenerator.generate(result.getErrorCode())));
                    }
                }catch (Exception e){
                    // 创建结算单部分回滚
                    TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
                    StringWriter exceptionFormat=new StringWriter();
                    e.printStackTrace(new PrintWriter(exceptionFormat,true));
                    dingDingSupport.dingDingSendMessage(dingDingSupport.getEnvironmentString()+"退货单["+k3ReturnOrderDO.getReturnOrderNo()+"]生成结算单失败："+exceptionFormat.toString());
                }
            }
        }
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTimeAxisSupport orderTimeAxisSupport;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    private DeliveryOrderProductMapper deliveryOrderProductMapper;

    @Autowired
    private DeliveryOrderMaterialMapper deliveryOrderMaterialMapper;

    @Autowired
    private K3MappingSubCompanyMapper k3MappingSubCompanyMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private K3ReturnOrderDetailMapper k3ReturnOrderDetailMapper;
    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerSupport customerSupport;
    @Autowired
    private DingDingSupport dingDingSupport;
    @Autowired
    private ProductSupport productSupport;
    @Autowired
    private StatementService statementService;
    @Autowired
    private ResultGenerator resultGenerator;
}
