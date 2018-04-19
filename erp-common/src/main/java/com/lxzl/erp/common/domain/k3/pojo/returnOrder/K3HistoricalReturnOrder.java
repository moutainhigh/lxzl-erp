package com.lxzl.erp.common.domain.k3.pojo.returnOrder;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * k3历史退货单类
 *
 * @author daiqi
 * @create 2018-04-18 17:36
 */
public class K3HistoricalReturnOrder {
    private K3ReturnOrder k3ReturnOrder;
    private List<K3ReturnOrderDetail> k3ReturnOrderDetails;

    public K3ReturnOrder getK3ReturnOrder() {
        return k3ReturnOrder;
    }

    @JSONField(name = "OrderBill")
    public void setK3ReturnOrder(K3ReturnOrder k3ReturnOrder) {
        this.k3ReturnOrder = k3ReturnOrder;
    }

    public List<K3ReturnOrderDetail> getK3ReturnOrderDetails() {
        return k3ReturnOrderDetails;
    }

    @JSONField(name = "ProductList")
    public void setK3ReturnOrderDetails(List<K3ReturnOrderDetail> k3ReturnOrderDetails) {
        this.k3ReturnOrderDetails = k3ReturnOrderDetails;
    }

    public void setReturnOrderIdToDetails(int returnOrderId) {
        if (k3ReturnOrderDetails != null) {
            for (K3ReturnOrderDetail k3ReturnOrderDetail : k3ReturnOrderDetails) {
                k3ReturnOrderDetail.setReturnOrderId(returnOrderId);
            }
        }
    }
}
