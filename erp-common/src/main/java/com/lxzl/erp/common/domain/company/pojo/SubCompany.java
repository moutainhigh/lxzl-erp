package com.lxzl.erp.common.domain.company.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubCompany implements Serializable {

    private Integer subCompanyId;

    @NotEmpty(message = ErrorCode.SUB_COMPANY_NAME_NOT_NULL , groups = {AddGroup.class})
    private String subCompanyName;

    private Integer dataStatus;

    private String remark;

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName == null ? null : subCompanyName.trim();
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

}