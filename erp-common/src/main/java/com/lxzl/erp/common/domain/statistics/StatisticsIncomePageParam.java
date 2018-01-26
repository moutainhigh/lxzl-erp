package com.lxzl.erp.common.domain.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lk
 * @Description: 收入统计分页查询
 * @date 2018/1/17 14:42
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsIncomePageParam extends BasePageParam {

    @NotNull(message = ErrorCode.START_TIME_NOT_NULL)
    private Date startTime; //查询起始时间
    @NotNull(message = ErrorCode.END_TIME_NOT_NULL)
    private Date endTime; //查询结束时间
    private String customerName; //客户名称模糊查询
    private Integer rentLengthType; //（业务类型）租赁时长类型，1-长租，2-短租
    private Integer subCompanyId; //所属公司ID
    private String salesmanName; //业务员姓名模糊查询

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

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }
}
