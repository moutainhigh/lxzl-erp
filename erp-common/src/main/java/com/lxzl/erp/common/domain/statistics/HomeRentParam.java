package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lk
 * @Description: 长短租首页统计
 * @date 2018/1/17 14:42
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeRentParam implements Serializable {
    @NotNull(message = ErrorCode.START_TIME_NOT_NULL)
    private Date startTime; //查询起始时间
    @NotNull(message = ErrorCode.END_TIME_NOT_NULL)
    private Date endTime; //查询结束时间

    private Integer rentLengthType ;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getRentLengthType() {
        return rentLengthType;
    }

    public void setRentLengthType(Integer rentLengthType) {
        this.rentLengthType = rentLengthType;
    }
}
