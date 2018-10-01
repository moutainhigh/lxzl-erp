package com.lxzl.erp.common.domain.statement.pojo.dto;

import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderMaterial;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderProduct;
import com.lxzl.erp.common.util.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author daiqi
 * @create 2018-07-09 12:30
 */
public class CheckStatementMapContainer {
    private Map<Integer, Order> idOrderMap;
    private Map<Integer, K3ReturnOrder> idK3ReturnOrderMap;
    private Map<Integer, K3ReturnOrderDetail> idK3ReturnOrderDetailMap;
    private Map<Integer, OrderProduct> idOrderProductMap;
    private Map<Integer, OrderMaterial> idOrderMaterialMap;
    private Map<Integer, ReletOrder> idReletOrderMap;
    private Map<Integer, ReletOrderProduct> idReletOrderProductMap;
    private Map<Integer, ReletOrderMaterial> idReletOrderMaterialMap;
    private Map<Integer, ReplaceOrder> idReplaceOrderMap;
    private Map<Integer, ReplaceOrderProduct> idReplaceOrderProductMap;
    private Map<Integer, ReplaceOrderMaterial> idReplaceOrderMaterialMap;
    private Map<String, Order> noOrderMap;

    public Map<Integer, Order> getIdOrderMap() {
        return idOrderMap;
    }

    public Map<Integer, K3ReturnOrder> getIdK3ReturnOrderMap() {
        return idK3ReturnOrderMap;
    }

    public Map<Integer, K3ReturnOrderDetail> getIdK3ReturnOrderDetailMap() {
        return idK3ReturnOrderDetailMap;
    }

    public Map<Integer, OrderProduct> getIdOrderProductMap() {
        return idOrderProductMap;
    }

    public Map<Integer, OrderMaterial> getIdOrderMaterialMap() {
        return idOrderMaterialMap;
    }

    public Map<Integer, ReletOrder> getIdReletOrderMap() {
        return idReletOrderMap;
    }

    public Map<Integer, ReletOrderProduct> getIdReletOrderProductMap() {
        return idReletOrderProductMap;
    }

    public Map<Integer, ReletOrderMaterial> getIdReletOrderMaterialMap() {
        return idReletOrderMaterialMap;
    }

    public Map<Integer, ReplaceOrderProduct> getIdReplaceOrderProductMap() {
        return idReplaceOrderProductMap;
    }

    public void setIdReplaceOrderProductMap(Map<Integer, ReplaceOrderProduct> idReplaceOrderProductMap) {
        this.idReplaceOrderProductMap = idReplaceOrderProductMap;
    }

    public Map<Integer, ReplaceOrderMaterial> getIdReplaceOrderMaterialMap() {
        return idReplaceOrderMaterialMap;
    }

    public void setIdReplaceOrderMaterialMap(Map<Integer, ReplaceOrderMaterial> idReplaceOrderMaterialMap) {
        this.idReplaceOrderMaterialMap = idReplaceOrderMaterialMap;
    }

    public Map<Integer, ReplaceOrder> getIdReplaceOrderMap() {
        return idReplaceOrderMap;
    }

    public void setIdReplaceOrderMap(Map<Integer, ReplaceOrder> idReplaceOrderMap) {
        this.idReplaceOrderMap = idReplaceOrderMap;
    }

    public Map<String, Order> getNoOrderMap() {
        return noOrderMap;
    }

    public void setIdOrderMap(Map<Integer, Order> idOrderMap) {
        this.idOrderMap = idOrderMap;
    }

    public void setIdK3ReturnOrderMap(Map<Integer, K3ReturnOrder> idK3ReturnOrderMap) {
        this.idK3ReturnOrderMap = idK3ReturnOrderMap;
    }

    public void setIdK3ReturnOrderDetailMap(Map<Integer, K3ReturnOrderDetail> idK3ReturnOrderDetailMap) {
        this.idK3ReturnOrderDetailMap = idK3ReturnOrderDetailMap;
    }

    public void setIdOrderProductMap(Map<Integer, OrderProduct> idOrderProductMap) {
        this.idOrderProductMap = idOrderProductMap;
    }

    public void setIdOrderMaterialMap(Map<Integer, OrderMaterial> idOrderMaterialMap) {
        this.idOrderMaterialMap = idOrderMaterialMap;
    }

    public void setIdReletOrderMap(Map<Integer, ReletOrder> idReletOrderMap) {
        this.idReletOrderMap = idReletOrderMap;
    }

    public void setIdReletOrderProductMap(Map<Integer, ReletOrderProduct> idReletOrderProductMap) {
        this.idReletOrderProductMap = idReletOrderProductMap;
    }

    public void setIdReletOrderMaterialMap(Map<Integer, ReletOrderMaterial> idReletOrderMaterialMap) {
        this.idReletOrderMaterialMap = idReletOrderMaterialMap;
    }

    public void setNoOrderMap(Map<String, Order> noOrderMap) {
        this.noOrderMap = noOrderMap;
    }

    public CheckStatementMapContainer idOrderMap(Map<Integer, Order> idOrderMap) {
        this.idOrderMap = idOrderMap;
        return this;
    }


    public CheckStatementMapContainer idK3ReturnOrderMap(Map<Integer, K3ReturnOrder> idK3ReturnOrderMap) {
        this.idK3ReturnOrderMap = idK3ReturnOrderMap;
        return this;
    }


    public CheckStatementMapContainer idK3ReturnOrderDetailMap(Map<Integer, K3ReturnOrderDetail> idK3ReturnOrderDetailMap) {
        this.idK3ReturnOrderDetailMap = idK3ReturnOrderDetailMap;
        return this;
    }


    public CheckStatementMapContainer idOrderProductMap(Map<Integer, OrderProduct> idOrderProductMap) {
        this.idOrderProductMap = idOrderProductMap;
        return this;
    }


    public CheckStatementMapContainer idOrderMaterialMap(Map<Integer, OrderMaterial> idOrderMaterialMap) {
        this.idOrderMaterialMap = idOrderMaterialMap;
        return this;
    }


    public CheckStatementMapContainer idReletOrderMap(Map<Integer, ReletOrder> idReletOrderMap) {
        this.idReletOrderMap = idReletOrderMap;
        return this;
    }


    public CheckStatementMapContainer idReletOrderProductMap(Map<Integer, ReletOrderProduct> idReletOrderProductMap) {
        this.idReletOrderProductMap = idReletOrderProductMap;
        return this;
    }


    public CheckStatementMapContainer idReletOrderMaterialMap(Map<Integer, ReletOrderMaterial> idReletOrderMaterialMap) {
        this.idReletOrderMaterialMap = idReletOrderMaterialMap;
        return this;
    }


    public CheckStatementMapContainer noOrderMap(Map<String, Order> noOrderMap) {
        this.noOrderMap = noOrderMap;
        return this;
    }

    public CheckStatementMapContainer idOrderMap(List<Order> orders) {
        this.idOrderMap = ListUtil.listToMap(orders, "orderId");
        return this;
    }

    public CheckStatementMapContainer noOrderMap(List<Order> orders) {
        this.noOrderMap = ListUtil.listToMap(orders, "orderNo");
        return this;
    }

    public CheckStatementMapContainer idK3ReturnOrderMap(List<K3ReturnOrder> k3ReturnOrderS) {
        this.idK3ReturnOrderMap = ListUtil.listToMap(k3ReturnOrderS, "k3ReturnOrderId");
        return this;
    }

    public CheckStatementMapContainer idK3ReturnOrderDetailMap(List<K3ReturnOrderDetail> k3ReturnOrderDetailS) {
        this.idK3ReturnOrderDetailMap = ListUtil.listToMap(k3ReturnOrderDetailS, "k3ReturnOrderDetailId");
        return this;
    }

    public CheckStatementMapContainer idOrderProductMap(List<OrderProduct> orderProductS) {
        this.idOrderProductMap = ListUtil.listToMap(orderProductS, "orderProductId");
        return this;
    }

    public CheckStatementMapContainer idOrderMaterialMap(List<OrderMaterial> orderMaterialS) {
        this.idOrderMaterialMap = ListUtil.listToMap(orderMaterialS, "orderMaterialId");
        return this;
    }

    public CheckStatementMapContainer idReletOrderMap(List<ReletOrder> reletOrderS) {
        this.idReletOrderMap = ListUtil.listToMap(reletOrderS, "reletOrderId");
        return this;
    }

    public CheckStatementMapContainer idReletOrderProductMap(List<ReletOrderProduct> reletOrderProductS) {
        this.idReletOrderProductMap = ListUtil.listToMap(reletOrderProductS, "reletOrderProductId");
        return this;
    }

    public CheckStatementMapContainer idReletOrderMaterialMap(List<ReletOrderMaterial> reletOrderMaterialS) {
        this.idReletOrderMaterialMap = ListUtil.listToMap(reletOrderMaterialS, "reletOrderMaterialId");
        return this;
    }

    public CheckStatementMapContainer idReplaceOrderMap(List<ReplaceOrder> replaceOrders) {
        this.idReplaceOrderMap = ListUtil.listToMap(replaceOrders, "replaceOrderId");
        return this;
    }

    public CheckStatementMapContainer idReplaceOrderProductMap(List<ReplaceOrderProduct> replaceOrderProducts) {
        this.idReplaceOrderProductMap = ListUtil.listToMap(replaceOrderProducts, "replaceOrderProductId");
        return this;
    }

    public CheckStatementMapContainer idReplaceOrderMaterialMap(List<ReplaceOrderMaterial> replaceOrderMaterials) {
        this.idReplaceOrderMaterialMap = ListUtil.listToMap(replaceOrderMaterials, "replaceOrderMaterialId");
        return this;
    }

    public CheckStatementMapContainer addIdOrderMap(List<Order> orders) {
        Map<Integer, Order> orderMap = ListUtil.listToMap(orders, "orderId");
        if (idOrderMap == null) {
            idOrderMap = new HashMap<>();
        }
        this.idOrderMap.putAll(orderMap);
        return this;
    }

    public CheckStatementMapContainer addIdOrderProductMap(List<OrderProduct> orderProducts) {
        Map<Integer, OrderProduct> orderProductId = ListUtil.listToMap(orderProducts, "orderProductId");
        if (idOrderProductMap == null) {
            idOrderProductMap = new HashMap<>();
        }
        this.idOrderProductMap.putAll(orderProductId);
        return this;
    }
}
