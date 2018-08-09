package com.lxzl.erp.dataaccess.domain.statistics;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/8/7
 * Time: 9:28
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceStatisticsDataMeta {
    private Integer year;
    private Integer month;
    private Integer weekOfMonth;

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
