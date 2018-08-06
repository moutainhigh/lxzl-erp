package com.lxzl.erp.common.domain.taskScheduler.pojo;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.AddGroup;

import javax.validation.constraints.NotNull;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/25
 * @Time : Created in 21:53
 */
public class HolidayDTO {
    @NotNull(message = ErrorCode.QUARTZ_HOLIDAY_YEAR_NOT_NULL,groups = {AddGroup.class})
    private Integer year;
    @NotNull(message = ErrorCode.QUARTZ_HOLIDAY_MONTH_NOT_NULL,groups = {AddGroup.class})
    private Integer month;
    @NotNull(message = ErrorCode.QUARTZ_HOLIDAY_DAY_NOT_NULL,groups = {AddGroup.class})
    private Integer day;

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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
