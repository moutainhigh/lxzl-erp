package com.lxzl.erp.common.domain.jointProduct;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

public class JointProductQueryParam extends BasePageParam {

    private Integer jointProductId;
    private String jointProductName;
    private Date startDate;
    private Date endDate;

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
