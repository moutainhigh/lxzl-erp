package com.lxzl.erp.common.domain.jointProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class JointProductQueryParam extends BasePageParam {

    private Integer jointProductId;  //组合商品id
    private String jointProductName;  //组合商品名称
    private Date startDate;  //起始时间
    private Date endDate;  //结束时间

    public Integer getJointProductId() {
        return jointProductId;
    }

    public void setJointProductId(Integer jointProductId) {
        this.jointProductId = jointProductId;
    }

    public String getJointProductName() {
        return jointProductName;
    }

    public void setJointProductName(String jointProductName) {
        this.jointProductName = jointProductName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
