package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/7/17
 * Time: 18:16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceStatisticsParam extends BasePageParam {
    private Integer orderOrigin;
    private Integer rentLengthType;
    private Date statisticsStartTime;
    private Date statisticsEndTime;
    private String newCustomerKey;
    private Integer statisticsInterval;
    private Integer year;
    private Integer month;
    private Integer weekOfMonth;

    public Integer getOrderOrigin() {
        return orderOrigin;
    }

    public void setOrderOrigin(Integer orderOrigin) {
        this.orderOrigin = orderOrigin;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }

    public Date getStatisticsStartTime() {
        return statisticsStartTime;
    }

    public void setStatisticsStartTime(Date statisticsStartTime) {
        this.statisticsStartTime = statisticsStartTime;
    }

    public Date getStatisticsEndTime() {
        return statisticsEndTime;
    }

    public void setStatisticsEndTime(Date statisticsEndTime) {
        this.statisticsEndTime = statisticsEndTime;
    }

    public String getNewCustomerKey() {
        return newCustomerKey;
    }

    public void setNewCustomerKey(String newCustomerKey) {
        this.newCustomerKey = newCustomerKey;
    }

    public Integer getStatisticsInterval() {
        return statisticsInterval;
    }

    public void setStatisticsInterval(Integer statisticsInterval) {
        this.statisticsInterval = statisticsInterval;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(Integer weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }
}
