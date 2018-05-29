package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

import java.math.BigDecimal;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:02 2018/5/23
 * @Modified By:
 */
public class FormSEOrderConfirmlEntry implements java.io.Serializable{
    private Integer orderEntryId;

    private Integer orderItemType;

    private BigDecimal qty;

    public Integer getOrderEntryId() {
        return orderEntryId;
    }

    public void setOrderEntryId(Integer orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
