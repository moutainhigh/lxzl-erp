package com.lxzl.erp.core.service.k3.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrder;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrderMaterial;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrderProduct;
import com.lxzl.erp.common.domain.k3.pojo.callback.K3DeliveryOrder;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.k3.K3CallbackService;
import com.lxzl.erp.core.service.order.impl.support.OrderTimeAxisSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.delivery.DeliveryOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.delivery.DeliveryOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.delivery.DeliveryOrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingSubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderDO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderProductDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingSubCompanyDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-19 13:37
 */

@Service("k3CallbackService")
public class K3CallbackServiceImpl implements K3CallbackService {

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
        Date deliveryTime = deliveryOrder.getDeliveryTime() == null ? currentTime : deliveryOrder.getDeliveryTime();

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

    @Autowired
    private OrderMapper orderMapper;

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
}
