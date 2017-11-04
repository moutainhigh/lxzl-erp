package com.lxzl.erp.common.domain.company.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDepartmentTree implements Serializable {
    private List<SubCompany> subCompanyList;

    public List<SubCompany> getSubCompanyList() {
        return subCompanyList;
    }

    public void setSubCompanyList(List<SubCompany> subCompanyList) {
        this.subCompanyList = subCompanyList;
    }
}