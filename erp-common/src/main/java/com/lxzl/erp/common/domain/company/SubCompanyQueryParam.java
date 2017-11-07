package com.lxzl.erp.common.domain.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubCompanyQueryParam extends BasePageParam {

    private Integer subCompanyId;
    private String subCompanyName;
    private Integer subCompanyType;

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
        this.subCompanyName = subCompanyName;
    }

    public Integer getSubCompanyType() {
        return subCompanyType;
    }

    public void setSubCompanyType(Integer subCompanyType) {
        this.subCompanyType = subCompanyType;
    }
}
