package com.lxzl.erp.core.service.company;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.CompanyDepartmentTree;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;

import java.util.List;

public interface CompanyService {

    ServiceResult<String,Integer> addSubCompany(SubCompany subCompany);
    ServiceResult<String,Integer> updateSubCompany(SubCompany subCompany);
    ServiceResult<String,SubCompany> getSubCompanyById(Integer subCompanyId);
    ServiceResult<String,Page<SubCompany>> subCompanyPage(SubCompanyQueryParam subCompanyQueryParam);
    ServiceResult<String,List<Department>> getDepartmentList(DepartmentQueryParam departmentQueryParam);
    ServiceResult<String,CompanyDepartmentTree> getCompanyDepartmentTree(DepartmentQueryParam departmentQueryParam);
    ServiceResult<String,CompanyDepartmentTree> getCompanyDepartmentUserTree(DepartmentQueryParam departmentQueryParam);
    ServiceResult<String,SubCompany> getHeaderCompany();

}
