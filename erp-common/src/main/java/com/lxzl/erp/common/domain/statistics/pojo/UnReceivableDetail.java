package com.lxzl.erp.common.domain.statistics.pojo;

import java.math.BigDecimal;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/1/17 14:55
 */
public class UnReceivableDetail {

    private String customerNo;  //客户编号
    private String customerName;  //客户姓名
    private Integer rentLengthType;  //业务类型，1-长租，2-短租
    private Integer subCompanyId;  //分公司ID
    private String subCompanyName;  //分公司名称
    private Integer orderSellerId;  //业务员ID
    private String orderSellerName;  //业务员姓名
    private BigDecimal unReceivable;  //应收金额
    private Integer overduePhaseCount;  //逾期期数
    private Integer orderItemReferId;  //订单项ID
    private Integer orderItemType;  //订单项类型，1为商品，2为配件

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Integer getOrderSellerId() {
        return orderSellerId;
    }

    public void setOrderSellerId(Integer orderSellerId) {
        this.orderSellerId = orderSellerId;
    }

    public String getOrderSellerName() {
        return orderSellerName;
    }

    public void setOrderSellerName(String orderSellerName) {
        this.orderSellerName = orderSellerName;
    }

    public BigDecimal getUnReceivable() {
        return unReceivable;
    }

    public void setUnReceivable(BigDecimal unReceivable) {
        this.unReceivable = unReceivable;
    }

    public Integer getOverduePhaseCount() {
        return overduePhaseCount;
    }

    public void setOverduePhaseCount(Integer overduePhaseCount) {
        this.overduePhaseCount = overduePhaseCount;
    }

    public Integer getOrderItemReferId() {
        return orderItemReferId;
    }

    public void setOrderItemReferId(Integer orderItemReferId) {
        this.orderItemReferId = orderItemReferId;
    }

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }
}
