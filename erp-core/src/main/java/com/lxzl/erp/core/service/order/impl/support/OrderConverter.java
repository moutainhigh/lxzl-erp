package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.domain.order.pojo.*;
import com.lxzl.erp.dataaccess.domain.order.*;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderConverter {
    public static OrderDO convertOrder(Order order) {
        OrderDO orderDO = new OrderDO();
        if (order.getOrderNo() != null) {
            orderDO.setOrderNo(order.getOrderNo());
        }
        BeanUtils.copyProperties(order, orderDO);
        return orderDO;
    }


    public static OrderProductDO convertOrderProduct(OrderProduct orderProduct) {
        OrderProductDO orderProductDO = new OrderProductDO();
        if (orderProduct.getOrderProductId() != null) {
            orderProductDO.setId(orderProduct.getOrderProductId());
        }
        BeanUtils.copyProperties(orderProduct, orderProductDO);
        return orderProductDO;
    }


    public static List<OrderProductDO> convertOrderProductList(List<OrderProduct> orderProductList) {
        List<OrderProductDO> orderProductDOList = new ArrayList<>();
        if (orderProductList != null && !orderProductList.isEmpty()) {
            for (OrderProduct orderProduct : orderProductList) {
                orderProductDOList.add(convertOrderProduct(orderProduct));
            }
        }
        return orderProductDOList;
    }

    public static List<OrderMaterialDO> convertOrderMaterialList(List<OrderMaterial> orderMaterialList) {
        List<OrderMaterialDO> orderMaterialDOList = new ArrayList<>();
        if (orderMaterialList != null && !orderMaterialList.isEmpty()) {
            for (OrderMaterial orderMaterial : orderMaterialList) {
                orderMaterialDOList.add(convertOrderProduct(orderMaterial));
            }
        }
        return orderMaterialDOList;
    }


    public static OrderMaterialDO convertOrderProduct(OrderMaterial orderMaterial) {
        OrderMaterialDO orderMaterialDO = new OrderMaterialDO();
        if (orderMaterial.getOrderMaterialId() != null) {
            orderMaterialDO.setId(orderMaterial.getOrderMaterialId());
        }
        BeanUtils.copyProperties(orderMaterial, orderMaterialDO);
        return orderMaterialDO;
    }



    public static Order convertOrderDO(OrderDO orderDO) {
        Order order = new Order();
        if (orderDO.getId() != null) {
            order.setOrderId(orderDO.getId());
        }
        BeanUtils.copyProperties(orderDO, order);
        if (orderDO.getOrderProductDOList() != null && !orderDO.getOrderProductDOList().isEmpty()) {
            order.setOrderProductList(convertOrderProductDOList(orderDO.getOrderProductDOList()));
        }
        if (orderDO.getOrderConsignInfoDO() != null) {
            order.setOrderConsignInfo(convertOrderConsignInfoDO(orderDO.getOrderConsignInfoDO()));
        }
        return order;
    }


    public static List<Order> convertOrderDOList(List<OrderDO> orderDOList) {
        List<Order> orderList = new ArrayList<>();
        if (orderDOList != null && !orderDOList.isEmpty()) {
            for (OrderDO orderDO : orderDOList) {
                orderList.add(convertOrderDO(orderDO));
            }
        }
        return orderList;
    }

    public static OrderProduct convertOrderProductDO(OrderProductDO orderProductDO) {
        OrderProduct orderProduct = new OrderProduct();
        if (orderProductDO.getId() != null) {
            orderProduct.setOrderProductId(orderProductDO.getId());
        }
        BeanUtils.copyProperties(orderProductDO, orderProduct);
        return orderProduct;
    }


    public static List<OrderProduct> convertOrderProductDOList(List<OrderProductDO> orderProductDOList) {
        List<OrderProduct> orderProductList = new ArrayList<>();
        if (orderProductDOList != null && !orderProductDOList.isEmpty()) {
            for (OrderProductDO orderProductDO : orderProductDOList) {
                orderProductList.add(convertOrderProductDO(orderProductDO));
            }
        }
        return orderProductList;
    }

    public static OrderConsignInfo convertOrderConsignInfoDO(OrderConsignInfoDO orderConsignInfoDO) {
        OrderConsignInfo orderConsignInfo = new OrderConsignInfo();
        if (orderConsignInfoDO.getId() != null) {
            orderConsignInfo.setOrderConsignId(orderConsignInfoDO.getId());
        }
        BeanUtils.copyProperties(orderConsignInfoDO, orderConsignInfo);
        return orderConsignInfo;
    }

}
