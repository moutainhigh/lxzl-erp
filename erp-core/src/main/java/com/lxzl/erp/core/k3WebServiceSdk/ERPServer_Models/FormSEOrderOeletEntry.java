package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

import java.math.BigDecimal;

public class FormSEOrderOeletEntry {
    private Integer orderEntryId;  //行号
    private Integer orderItemType;   //类型：1-商品项，2-配件项
    private BigDecimal unitPrice;     //续租后单价


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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }



}
