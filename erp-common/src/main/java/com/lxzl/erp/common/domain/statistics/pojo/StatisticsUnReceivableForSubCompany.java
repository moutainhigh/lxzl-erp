package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lk
 * @Description: 未收汇总
 * @date 2018/1/17 14:30
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsUnReceivableForSubCompany implements Serializable {

    private BigDecimal totalLastMonthRent;  //总上月租金
    private BigDecimal totalUnReceivableLong;  //总长租未收
    private BigDecimal totalUnReceivableShort;  //总短租未收
    private BigDecimal totalUnReceivable;  //总合计未收
    private Double totalUnReceivablePercentage;  //总未收占比(合计未收/上月租金),如果结果为百分之七十五点二，该值为75.20
    private Integer totalCustomerCount;  //总客户数
    private Integer totalUnReceivableCustomerCountShort;  //短租未收客户数
    private Integer totalUnReceivableCustomerCountLong;  //长租未收客户数
    private Integer totalRentedCustomerCountShort;  //短租合作客户数
    private Integer totalRentingCustomerCountLong;  //长租在租客户数
    private List<StatisticsUnReceivableDetailForSubCompany> statisticsUnReceivableDetailForSubCompanyList;    //统计项列表

    public BigDecimal getTotalUnReceivable() {
        return totalUnReceivable;
    }

    public void setTotalUnReceivable(BigDecimal totalUnReceivable) {
        this.totalUnReceivable = totalUnReceivable;
    }

    public BigDecimal getTotalLastMonthRent() {
        return totalLastMonthRent;
    }

    public void setTotalLastMonthRent(BigDecimal totalLastMonthRent) {
        this.totalLastMonthRent = totalLastMonthRent;
    }

    public BigDecimal getTotalUnReceivableLong() {
        return totalUnReceivableLong;
    }

    public void setTotalUnReceivableLong(BigDecimal totalUnReceivableLong) {
        this.totalUnReceivableLong = totalUnReceivableLong;
    }

    public BigDecimal getTotalUnReceivableShort() {
        return totalUnReceivableShort;
    }

    public void setTotalUnReceivableShort(BigDecimal totalUnReceivableShort) {
        this.totalUnReceivableShort = totalUnReceivableShort;
    }

    public Double getTotalUnReceivablePercentage() {
        return totalUnReceivablePercentage;
    }

    public void setTotalUnReceivablePercentage(Double totalUnReceivablePercentage) {
        this.totalUnReceivablePercentage = totalUnReceivablePercentage;
    }

    public Integer getTotalCustomerCount() {
        return totalCustomerCount;
    }

    public void setTotalCustomerCount(Integer totalCustomerCount) {
        this.totalCustomerCount = totalCustomerCount;
    }

    public Integer getTotalUnReceivableCustomerCountShort() {
        return totalUnReceivableCustomerCountShort;
    }

    public void setTotalUnReceivableCustomerCountShort(Integer totalUnReceivableCustomerCountShort) {
        this.totalUnReceivableCustomerCountShort = totalUnReceivableCustomerCountShort;
    }

    public Integer getTotalUnReceivableCustomerCountLong() {
        return totalUnReceivableCustomerCountLong;
    }

    public void setTotalUnReceivableCustomerCountLong(Integer totalUnReceivableCustomerCountLong) {
        this.totalUnReceivableCustomerCountLong = totalUnReceivableCustomerCountLong;
    }

    public Integer getTotalRentedCustomerCountShort() {
        return totalRentedCustomerCountShort;
    }

    public Integer getTotalRentingCustomerCountLong() {
        return totalRentingCustomerCountLong;
    }

    public void setTotalRentingCustomerCountLong(Integer totalRentingCustomerCountLong) {
        this.totalRentingCustomerCountLong = totalRentingCustomerCountLong;
    }

    public void setTotalRentedCustomerCountShort(Integer totalRentedCustomerCountShort) {
        this.totalRentedCustomerCountShort = totalRentedCustomerCountShort;
    }

    public List<StatisticsUnReceivableDetailForSubCompany> getStatisticsUnReceivableDetailForSubCompanyList() {
        return statisticsUnReceivableDetailForSubCompanyList;
    }

    public void setStatisticsUnReceivableDetailForSubCompanyList(List<StatisticsUnReceivableDetailForSubCompany> statisticsUnReceivableDetailForSubCompanyList) {
        this.statisticsUnReceivableDetailForSubCompanyList = statisticsUnReceivableDetailForSubCompanyList;
    }
}

