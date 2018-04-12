package com.lxzl.erp.common.domain.statistics.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lk
 * @Description: 待收汇总
 * @date 2018/1/17 14:30
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsAwaitReceivable implements Serializable {

    private Integer totalCount;    //总条数
    private BigDecimal totalLastMonthRent;  //总上月租金
    private BigDecimal totalAwaitReceivableLong;  //总长租待收
    private BigDecimal totalAwaitReceivableShort;  //总短租待收
    private BigDecimal totalAwaitReceivable;  //总合计待收
    private Double totalAwaitReceivablePercentage;  //总未收占比(合计未收/上月租金),如果结果为百分之七十五点二，该值为75.20
    private Integer totalCustomerCount;  //总客户数
    private Integer totalAwaitReceivableCustomerCountShort;  //短租待收客户数
    private Integer totalAwaitReceivableCustomerCountLong;  //长租待收客户数
    private Integer totalRentedCustomerCountShort;  //短租合作客户数
    private Integer totalRentingCustomerCountLong;  //长租在租客户数
    private Page<StatisticsAwaitReceivableDetail> statisticsAwaitReceivableDetailPage;    //统计项分页

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalLastMonthRent() {
        return totalLastMonthRent;
    }

    public void setTotalLastMonthRent(BigDecimal totalLastMonthRent) {
        this.totalLastMonthRent = totalLastMonthRent;
    }

    public BigDecimal getTotalAwaitReceivableLong() {
        return totalAwaitReceivableLong;
    }

    public void setTotalAwaitReceivableLong(BigDecimal totalAwaitReceivableLong) {
        this.totalAwaitReceivableLong = totalAwaitReceivableLong;
    }

    public BigDecimal getTotalAwaitReceivableShort() {
        return totalAwaitReceivableShort;
    }

    public void setTotalAwaitReceivableShort(BigDecimal totalAwaitReceivableShort) {
        this.totalAwaitReceivableShort = totalAwaitReceivableShort;
    }

    public BigDecimal getTotalAwaitReceivable() {
        return totalAwaitReceivable;
    }

    public void setTotalAwaitReceivable(BigDecimal totalAwaitReceivable) {
        this.totalAwaitReceivable = totalAwaitReceivable;
    }

    public Double getTotalAwaitReceivablePercentage() {
        return totalAwaitReceivablePercentage;
    }

    public void setTotalAwaitReceivablePercentage(Double totalAwaitReceivablePercentage) {
        this.totalAwaitReceivablePercentage = totalAwaitReceivablePercentage;
    }

    public Integer getTotalCustomerCount() {
        return totalCustomerCount;
    }

    public void setTotalCustomerCount(Integer totalCustomerCount) {
        this.totalCustomerCount = totalCustomerCount;
    }

    public Integer getTotalAwaitReceivableCustomerCountShort() {
        return totalAwaitReceivableCustomerCountShort;
    }

    public void setTotalAwaitReceivableCustomerCountShort(Integer totalAwaitReceivableCustomerCountShort) {
        this.totalAwaitReceivableCustomerCountShort = totalAwaitReceivableCustomerCountShort;
    }

    public Integer getTotalAwaitReceivableCustomerCountLong() {
        return totalAwaitReceivableCustomerCountLong;
    }

    public void setTotalAwaitReceivableCustomerCountLong(Integer totalAwaitReceivableCustomerCountLong) {
        this.totalAwaitReceivableCustomerCountLong = totalAwaitReceivableCustomerCountLong;
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

    public Page<StatisticsAwaitReceivableDetail> getStatisticsAwaitReceivableDetailPage() {
        return statisticsAwaitReceivableDetailPage;
    }

    public void setStatisticsAwaitReceivableDetailPage(Page<StatisticsAwaitReceivableDetail> statisticsAwaitReceivableDetailPage) {
        this.statisticsAwaitReceivableDetailPage = statisticsAwaitReceivableDetailPage;
    }
}

