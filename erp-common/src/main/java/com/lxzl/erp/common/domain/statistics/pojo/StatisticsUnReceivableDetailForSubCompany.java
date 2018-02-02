package com.lxzl.erp.common.domain.statistics.pojo;

import java.math.BigDecimal;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/1/17 14:55
 */
public class StatisticsUnReceivableDetailForSubCompany {

    private Integer subCompanyId;  //分公司ID
    private String subCompanyName;  //分公司名称
    private BigDecimal lastMonthRent;  //上月租金
    private BigDecimal unReceivableLong;  //长租未收
    private BigDecimal unReceivableShort;  //短租未收
    private BigDecimal unReceivable;  //合计未收
    private Double unReceivablePercentage;  //未收占比(合计未收/上月租金),如果结果为百分之七十五点二，该值为75.20
    private Integer customerCount;  //总客户数
    private Integer unReceivableCustomerCountShort;  //短租未收客户数
    private Integer unReceivableCustomerCountLong;  //长租未收客户数
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

    public BigDecimal getLastMonthRent() {
        return lastMonthRent;
    }

    public void setLastMonthRent(BigDecimal lastMonthRent) {
        this.lastMonthRent = lastMonthRent;
    }

    public BigDecimal getUnReceivableLong() {
        return unReceivableLong;
    }

    public void setUnReceivableLong(BigDecimal unReceivableLong) {
        this.unReceivableLong = unReceivableLong;
    }

    public BigDecimal getUnReceivableShort() {
        return unReceivableShort;
    }

    public void setUnReceivableShort(BigDecimal unReceivableShort) {
        this.unReceivableShort = unReceivableShort;
    }

    public BigDecimal getUnReceivable() {
        return unReceivable;
    }

    public void setUnReceivable(BigDecimal unReceivable) {
        this.unReceivable = unReceivable;
    }

    public Double getUnReceivablePercentage() {
        return unReceivablePercentage;
    }

    public void setUnReceivablePercentage(Double unReceivablePercentage) {
        this.unReceivablePercentage = unReceivablePercentage;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public Integer getUnReceivableCustomerCountShort() {
        return unReceivableCustomerCountShort;
    }

    public void setUnReceivableCustomerCountShort(Integer unReceivableCustomerCountShort) {
        this.unReceivableCustomerCountShort = unReceivableCustomerCountShort;
    }

    public Integer getUnReceivableCustomerCountLong() {
        return unReceivableCustomerCountLong;
    }

    public void setUnReceivableCustomerCountLong(Integer unReceivableCustomerCountLong) {
        this.unReceivableCustomerCountLong = unReceivableCustomerCountLong;
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
}
