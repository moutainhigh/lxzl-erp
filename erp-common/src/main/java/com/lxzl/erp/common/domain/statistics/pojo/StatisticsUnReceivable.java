package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lk
 * @Description: 未收汇总
 * @date 2018/1/17 14:30
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsUnReceivable implements Serializable {

    private Integer totalCount;    //总条数
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
    private Page<StatisticsUnReceivableDetail> statisticsUnReceivableDetailPage;    //统计项分页

    private BigDecimal totalReturnUnReceivableLong;  //总退换货长租结算
    private BigDecimal totalReturnUnReceivableShort;  //总退换货短租结算
    private BigDecimal totalReturnUnReceivable;  //总退换货合计结算


    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

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

    public Page<StatisticsUnReceivableDetail> getStatisticsUnReceivableDetailPage() {
        return statisticsUnReceivableDetailPage;
    }

    public void setStatisticsUnReceivableDetailPage(Page<StatisticsUnReceivableDetail> statisticsUnReceivableDetailPage) {
        this.statisticsUnReceivableDetailPage = statisticsUnReceivableDetailPage;
    }

    public BigDecimal getTotalReturnUnReceivableLong() {
        return totalReturnUnReceivableLong;
    }

    public void setTotalReturnUnReceivableLong(BigDecimal totalReturnUnReceivableLong) {
        this.totalReturnUnReceivableLong = totalReturnUnReceivableLong;
    }

    public BigDecimal getTotalReturnUnReceivableShort() {
        return totalReturnUnReceivableShort;
    }

    public void setTotalReturnUnReceivableShort(BigDecimal totalReturnUnReceivableShort) {
        this.totalReturnUnReceivableShort = totalReturnUnReceivableShort;
    }

    public BigDecimal getTotalReturnUnReceivable() {
        return totalReturnUnReceivable;
    }

    public void setTotalReturnUnReceivable(BigDecimal totalReturnUnReceivable) {
        this.totalReturnUnReceivable = totalReturnUnReceivable;
    }
}

