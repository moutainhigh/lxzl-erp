package com.lxzl.erp.common.domain.k3.pojo.order;

import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-11 21:10
 */
public class K3Order {
    private Order OrderBill;
    private OrderConsignInfo Address;
    private List<OrderMaterial> MeasureList;
    private List<OrderProduct> ProductList;

    public Order getOrderBill() {
        return OrderBill;
    }

    public void setOrderBill(Order orderBill) {
        OrderBill = orderBill;
    }

    public OrderConsignInfo getAddress() {
        return Address;
    }

    public void setAddress(OrderConsignInfo address) {
        Address = address;
    }

    public List<OrderMaterial> getMeasureList() {
        return MeasureList;
    }

    public void setMeasureList(List<OrderMaterial> measureList) {
        MeasureList = measureList;
    }

    public List<OrderProduct> getProductList() {
        return ProductList;
    }

    public void setProductList(List<OrderProduct> productList) {
        ProductList = productList;
    }
}
