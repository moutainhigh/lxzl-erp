package com.lxzl.erp.core.service.company.impl.support;

import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CompanyConverter {
    public static SubCompanyDO convertSubCompany(SubCompany subCompany){
        SubCompanyDO subCompanyDO = new SubCompanyDO();
        BeanUtils.copyProperties(subCompany,subCompanyDO);
        subCompanyDO.setId(subCompany.getSubCompanyId());
        return subCompanyDO;
    }
    public static SubCompany convertSubCompany(SubCompanyDO subCompanyDO){
        SubCompany subCompany = new SubCompany();
        BeanUtils.copyProperties(subCompanyDO,subCompany);
        subCompany.setSubCompanyId(subCompanyDO.getId());
        subCompany.setDepartmentList(DepartmentConverter.convertDepartmentDOList(subCompanyDO.getDepartmentDOList()));
        return subCompany;
    }
    public static List<SubCompany> convertSubCompanyDOList(List<SubCompanyDO> subCompanyDOList){
        List<SubCompany> subCompanyList = new ArrayList<>();
        if (subCompanyDOList != null && subCompanyDOList.size() > 0) {
            for (SubCompanyDO subCompanyDO : subCompanyDOList) {
                subCompanyList.add(convertSubCompany(subCompanyDO));
            }
        }
        return subCompanyList;
    }
}
