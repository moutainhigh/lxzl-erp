package com.lxzl.erp.common.domain.user.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/12/24.
 * Time: 13:54.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyRoleTree implements Serializable {

    private List<SubCompany> subCompanyList;

    public List<SubCompany> getSubCompanyList() {
        return subCompanyList;
    }

    public void setSubCompanyList(List<SubCompany> subCompanyList) {
        this.subCompanyList = subCompanyList;
    }
}
