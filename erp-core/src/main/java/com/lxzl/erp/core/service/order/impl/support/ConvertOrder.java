package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.domain.order.pojo.*;
import com.lxzl.erp.dataaccess.domain.order.*;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ConvertOrder {
    public static OrderDO convertOrder(Order order) {
        OrderDO orderDO = new OrderDO();
        if (order.getOrderNo() != null) {
            orderDO.setOrderNo(order.getOrderNo());
        }
        if (order.getOrderStatus() != null) {
            orderDO.setOrderStatus(order.getOrderStatus());
        }
        if (order.getBuyerUserId() != null) {
            orderDO.setBuyerUserId(order.getBuyerUserId());
        }
        if (order.getRentType() != null) {
            orderDO.setRentType(order.getRentType());
        }
        if (order.getRentTimeLength() != null) {
            orderDO.setRentTimeLength(order.getRentTimeLength());
        }
        if (order.getPayMode() != null) {
            orderDO.setPayMode(order.getPayMode());
        }
        if (order.getProductCountTotal() != null) {
            orderDO.setProductCountTotal(order.getProductCountTotal());
        }
        if (order.getProductAmountTotal() != null) {
            orderDO.setProductAmountTotal(order.getProductAmountTotal());
        }
        if (order.getOrderAmountTotal() != null) {
            orderDO.setOrderAmountTotal(order.getOrderAmountTotal());
        }
        if (order.getDiscountAmountTotal() != null) {
            orderDO.setDiscountAmountTotal(order.getDiscountAmountTotal());
        }
        if (order.getLogisticsAmount() != null) {
            orderDO.setLogisticsAmount(order.getLogisticsAmount());
        }
        if (order.getPayTime() != null) {
            orderDO.setPayTime(order.getPayTime());
        }
        if (order.getBuyerRemark() != null) {
            orderDO.setBuyerRemark(order.getBuyerRemark());
        }
        if (order.getDataStatus() != null) {
            orderDO.setDataStatus(order.getDataStatus());
        }
        if (order.getRemark() != null) {
            orderDO.setRemark(order.getRemark());
        }
        return orderDO;
    }


    public static OrderProductDO convertOrderProduct(OrderProduct orderProduct) {
        OrderProductDO orderProductDO = new OrderProductDO();
        if (orderProduct.getOrderProductId() != null) {
            orderProductDO.setId(orderProduct.getOrderProductId());
        }
        if (orderProduct.getOrderId() != null) {
            orderProductDO.setOrderId(orderProduct.getOrderId());
        }
        if (orderProduct.getProductId() != null) {
            orderProductDO.setProductId(orderProduct.getProductId());
        }
        if (orderProduct.getProductName() != null) {
            orderProductDO.setProductName(orderProduct.getProductName());
        }
        if (orderProduct.getProductSkuId() != null) {
            orderProductDO.setProductSkuId(orderProduct.getProductSkuId());
        }
        if (orderProduct.getProductSkuName() != null) {
            orderProductDO.setProductSkuName(orderProduct.getProductSkuName());
        }
        if (orderProduct.getProductCount() != null) {
            orderProductDO.setProductCount(orderProduct.getProductCount());
        }
        if (orderProduct.getProductUnitAmount() != null) {
            orderProductDO.setProductUnitAmount(orderProduct.getProductUnitAmount());
        }
        if (orderProduct.getProductAmount() != null) {
            orderProductDO.setProductAmount(orderProduct.getProductAmount());
        }
        if (orderProduct.getEquipmentNoList() != null) {
            orderProductDO.setEquipmentNoList(orderProduct.getEquipmentNoList());
        }
        if (orderProduct.getProductSnapshot() != null) {
            orderProductDO.setProductSnapshot(orderProduct.getProductSnapshot());
        }
        if (orderProduct.getDataStatus() != null) {
            orderProductDO.setDataStatus(orderProduct.getDataStatus());
        }
        if (orderProduct.getRemark() != null) {
            orderProductDO.setRemark(orderProduct.getRemark());
        }
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


    public static Order convertOrderDO(OrderDO orderDO) {
        Order order = new Order();
        if (orderDO.getId() != null) {
            order.setOrderId(orderDO.getId());
        }
        if (orderDO.getOrderNo() != null) {
            order.setOrderNo(orderDO.getOrderNo());
        }
        if (orderDO.getOrderStatus() != null) {
            order.setOrderStatus(orderDO.getOrderStatus());
        }
        if (orderDO.getBuyerUserId() != null) {
            order.setBuyerUserId(orderDO.getBuyerUserId());
        }
        if (orderDO.getBuyerRealName() != null) {
            order.setBuyerRealName(orderDO.getBuyerRealName());
        }
        if (orderDO.getRentType() != null) {
            order.setRentType(orderDO.getRentType());
        }
        if (orderDO.getRentTimeLength() != null) {
            order.setRentTimeLength(orderDO.getRentTimeLength());
        }
        if (orderDO.getPayMode() != null) {
            order.setPayMode(orderDO.getPayMode());
        }
        if (orderDO.getProductCountTotal() != null) {
            order.setProductCountTotal(orderDO.getProductCountTotal());
        }
        if (orderDO.getProductAmountTotal() != null) {
            order.setProductAmountTotal(orderDO.getProductAmountTotal());
        }
        if (orderDO.getOrderAmountTotal() != null) {
            order.setOrderAmountTotal(orderDO.getOrderAmountTotal());
        }
        if (orderDO.getDiscountAmountTotal() != null) {
            order.setDiscountAmountTotal(orderDO.getDiscountAmountTotal());
        }
        if (orderDO.getLogisticsAmount() != null) {
            order.setLogisticsAmount(orderDO.getLogisticsAmount());
        }
        if (orderDO.getPayTime() != null) {
            order.setPayTime(orderDO.getPayTime());
        }
        if (orderDO.getDeliveryTime() != null) {
            order.setDeliveryTime(orderDO.getDeliveryTime());
        }
        if (orderDO.getConfirmDeliveryTime() != null) {
            order.setConfirmDeliveryTime(orderDO.getConfirmDeliveryTime());
        }
        if (orderDO.getExpectReturnTime() != null) {
            order.setExpectReturnTime(orderDO.getExpectReturnTime());
        }
        if (orderDO.getActualReturnTime() != null) {
            order.setActualReturnTime(orderDO.getActualReturnTime());
        }
        if (orderDO.getDeliveryTime() != null) {
            order.setDeliveryTime(orderDO.getDeliveryTime());
        }
        if (orderDO.getBuyerRemark() != null) {
            order.setBuyerRemark(orderDO.getBuyerRemark());
        }
        if (orderDO.getDataStatus() != null) {
            order.setDataStatus(orderDO.getDataStatus());
        }
        if (orderDO.getRemark() != null) {
            order.setRemark(orderDO.getRemark());
        }
        if (orderDO.getCreateTime() != null) {
            order.setCreateTime(orderDO.getCreateTime());
        }
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
        if (orderProductDO.getOrderId() != null) {
            orderProduct.setOrderId(orderProductDO.getOrderId());
        }
        if (orderProductDO.getProductId() != null) {
            orderProduct.setProductId(orderProductDO.getProductId());
        }
        if (orderProductDO.getProductName() != null) {
            orderProduct.setProductName(orderProductDO.getProductName());
        }
        if (orderProductDO.getProductSkuId() != null) {
            orderProduct.setProductSkuId(orderProductDO.getProductSkuId());
        }
        if (orderProductDO.getProductSkuName() != null) {
            orderProduct.setProductSkuName(orderProductDO.getProductSkuName());
        }
        if (orderProductDO.getProductCount() != null) {
            orderProduct.setProductCount(orderProductDO.getProductCount());
        }
        if (orderProductDO.getProductUnitAmount() != null) {
            orderProduct.setProductUnitAmount(orderProductDO.getProductUnitAmount());
        }
        if (orderProductDO.getProductAmount() != null) {
            orderProduct.setProductAmount(orderProductDO.getProductAmount());
        }
        if (orderProductDO.getProductSnapshot() != null) {
            orderProduct.setProductSnapshot(orderProductDO.getProductSnapshot());
        }
        if (orderProductDO.getEquipmentNoList() != null) {
            orderProduct.setEquipmentNoList(orderProductDO.getEquipmentNoList());
        }
        if (orderProductDO.getDataStatus() != null) {
            orderProduct.setDataStatus(orderProductDO.getDataStatus());
        }
        if (orderProductDO.getRemark() != null) {
            orderProduct.setRemark(orderProductDO.getRemark());
        }
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
        if (orderConsignInfoDO.getOrderId() != null) {
            orderConsignInfo.setOrderId(orderConsignInfoDO.getOrderId());
        }
        if (orderConsignInfoDO.getConsigneeName() != null) {
            orderConsignInfo.setConsigneeName(orderConsignInfoDO.getConsigneeName());
        }
        if (orderConsignInfoDO.getConsigneePhone() != null) {
            orderConsignInfo.setConsigneePhone(orderConsignInfoDO.getConsigneePhone());
        }
        if (orderConsignInfoDO.getProvince() != null) {
            orderConsignInfo.setProvince(orderConsignInfoDO.getProvince());
        }
        if (orderConsignInfoDO.getCity() != null) {
            orderConsignInfo.setCity(orderConsignInfoDO.getCity());
        }
        if (orderConsignInfoDO.getDistrict() != null) {
            orderConsignInfo.setDistrict(orderConsignInfoDO.getDistrict());
        }
        if (orderConsignInfoDO.getProvinceName() != null) {
            orderConsignInfo.setProvinceName(orderConsignInfoDO.getProvinceName());
        }
        if (orderConsignInfoDO.getCityName() != null) {
            orderConsignInfo.setCityName(orderConsignInfoDO.getCityName());
        }
        if (orderConsignInfoDO.getDistrictName() != null) {
            orderConsignInfo.setDistrictName(orderConsignInfoDO.getDistrictName());
        }
        if (orderConsignInfoDO.getAddress() != null) {
            orderConsignInfo.setAddress(orderConsignInfoDO.getAddress());
        }
        if (orderConsignInfoDO.getDataStatus() != null) {
            orderConsignInfo.setDataStatus(orderConsignInfoDO.getDataStatus());
        }
        if (orderConsignInfoDO.getRemark() != null) {
            orderConsignInfo.setRemark(orderConsignInfoDO.getRemark());
        }
        return orderConsignInfo;
    }

}
