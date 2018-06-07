package com.lxzl.erp.common.domain.reletorder;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReletOrderQueryParam extends BasePageParam implements Serializable {

    private Integer reletOrderId;
    private String reletOrderNo;

    private Integer orderId;
    private String orderNo;
    private String buyerRealName;
    private Integer buyerCustomerId;
    private String buyerCustomerNo;

    private Integer rentType;
    private Integer reletOrderStatus;
    private Date createStartTime;
    private Date createEndTime;
    private Integer orderSellerId;

    private Integer subCompanyId;
    private Integer deliverySubCompanyId;                       // 发货所属分公司

    private Date startExpectReturnTime;  //起始预计归还时间   （查询到期）
    private Date endExpectReturnTime;
    private Integer rentLengthType;    //1 短租 2长租

    public Integer getReletOrderId(){return reletOrderId;}

    public void setReletOrderId(Integer reletOrderId) {
        this.reletOrderId = reletOrderId;
    }

    public String getReletOrderNo() {
        return reletOrderNo;
    }

    public void setReletOrderNo(String reletOrderNo) {
        this.reletOrderNo = reletOrderNo;
    }


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getBuyerCustomerId() {
        return buyerCustomerId;
    }

    public void setBuyerCustomerId(Integer buyerCustomerId) {
        this.buyerCustomerId = buyerCustomerId;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public Integer getReletOrderStatus() {
        return reletOrderStatus;
    }

    public void setReletOrderStatus(Integer reletOrderStatus) {
        this.reletOrderStatus = reletOrderStatus;
    }


    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public String getBuyerRealName() {
        return buyerRealName;
    }

    public void setBuyerRealName(String buyerRealName) {
        this.buyerRealName = buyerRealName;
    }



    public Integer getOrderSellerId() {
        return orderSellerId;
    }

    public void setOrderSellerId(Integer orderSellerId) {
        this.orderSellerId = orderSellerId;
    }



    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }



    public String getBuyerCustomerNo() {
        return buyerCustomerNo;
    }

    public void setBuyerCustomerNo(String buyerCustomerNo) {
        this.buyerCustomerNo = buyerCustomerNo;
    }



    public Integer getDeliverySubCompanyId() { return deliverySubCompanyId; }

    public void setDeliverySubCompanyId(Integer deliverySubCompanyId) { this.deliverySubCompanyId = deliverySubCompanyId; }

    public Date getStartExpectReturnTime() {
        return startExpectReturnTime;
    }

    public void setStartExpectReturnTime(Date startExpectReturnTime) {
        this.startExpectReturnTime = startExpectReturnTime;
    }

    public Date getEndExpectReturnTime() {
        return endExpectReturnTime;
    }

    public void setEndExpectReturnTime(Date endExpectReturnTime) {
        this.endExpectReturnTime = endExpectReturnTime;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

}
