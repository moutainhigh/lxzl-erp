package com.lxzl.erp.common.domain.statistics.pojo;

import java.math.BigDecimal;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/1/17 14:55
 */
public class StatisticsAwaitReceivableDetail {

    private Integer subCompanyId;  //分公司ID
    private String subCompanyName;  //分公司名称
    private Integer orderSellerId;  //业务员ID
    private String orderSellerName;  //业务员姓名
    private BigDecimal lastMonthRent;  //上月租金
    private BigDecimal awaitReceivableLong;  //长租待收
    private BigDecimal awaitReceivableShort;  //短租待收
    private BigDecimal awaitReceivable;  //合计待收
    private Double awaitReceivablePercentage;  //待收占比(合计未收/上月租金),如果结果为百分之七十五点二，该值为75.20
    private Integer customerCount;  //总客户数
    private Integer awaitReceivableCustomerCountShort;  //短租待收客户数
    private Integer awaitReceivableCustomerCountLong;  //长租待收客户数
    private Integer rentedCustomerCountShort;  //短租合作客户数
    private Integer rentingCustomerCountLong;  //长租在租客户数

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

    public BigDecimal getLastMonthRent() {
        return lastMonthRent;
    }

    public void setLastMonthRent(BigDecimal lastMonthRent) {
        this.lastMonthRent = lastMonthRent;
    }

    public Double getAwaitReceivablePercentage() {
        return awaitReceivablePercentage;
    }

    public void setAwaitReceivablePercentage(Double awaitReceivablePercentage) {
        this.awaitReceivablePercentage = awaitReceivablePercentage;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public Integer getRentedCustomerCountShort() {
        return rentedCustomerCountShort;
    }

    public void setRentedCustomerCountShort(Integer rentedCustomerCountShort) {
        this.rentedCustomerCountShort = rentedCustomerCountShort;
    }

    public Integer getRentingCustomerCountLong() {
        return rentingCustomerCountLong;
    }

    public void setRentingCustomerCountLong(Integer rentingCustomerCountLong) {
        this.rentingCustomerCountLong = rentingCustomerCountLong;
    }

    public BigDecimal getAwaitReceivableLong() {
        return awaitReceivableLong;
    }

    public void setAwaitReceivableLong(BigDecimal awaitReceivableLong) {
        this.awaitReceivableLong = awaitReceivableLong;
    }

    public BigDecimal getAwaitReceivableShort() {
        return awaitReceivableShort;
    }

    public void setAwaitReceivableShort(BigDecimal awaitReceivableShort) {
        this.awaitReceivableShort = awaitReceivableShort;
    }

    public BigDecimal getAwaitReceivable() {
        return awaitReceivable;
    }

    public void setAwaitReceivable(BigDecimal awaitReceivable) {
        this.awaitReceivable = awaitReceivable;
    }

    public Integer getAwaitReceivableCustomerCountShort() {
        return awaitReceivableCustomerCountShort;
    }

    public void setAwaitReceivableCustomerCountShort(Integer awaitReceivableCustomerCountShort) {
        this.awaitReceivableCustomerCountShort = awaitReceivableCustomerCountShort;
    }

    public Integer getAwaitReceivableCustomerCountLong() {
        return awaitReceivableCustomerCountLong;
    }

    public void setAwaitReceivableCustomerCountLong(Integer awaitReceivableCustomerCountLong) {
        this.awaitReceivableCustomerCountLong = awaitReceivableCustomerCountLong;
    }
}
