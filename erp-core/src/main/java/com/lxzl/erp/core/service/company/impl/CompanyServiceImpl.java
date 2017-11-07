package com.lxzl.erp.core.service.company.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.CompanyDepartmentTree;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.company.impl.support.CompanyConverter;
import com.lxzl.erp.core.service.company.impl.support.DepartmentConverter;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;


@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ServiceResult<String, Integer> addSubCompany(SubCompany subCompany) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        SubCompanyDO subCompanyDO = CompanyConverter.convertSubCompany(subCompany);
        subCompanyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        if (loginUser != null) {
            subCompanyDO.setCreateUser(loginUser.getUserId().toString());
            subCompanyDO.setUpdateUser(loginUser.getUserId().toString());
        }
        subCompanyDO.setCreateTime(new Date());
        subCompanyDO.setUpdateTime(new Date());
        subCompanyMapper.save(subCompanyDO);

        result.setResult(subCompanyDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> updateSubCompany(SubCompany subCompany) {
        return null;
    }

    @Override
    public ServiceResult<String, SubCompany> getSubCompanyById(Integer subCompanyId) {
        return null;
    }

    @Override
    public ServiceResult<String, Page<SubCompany>> subCompanyPage(SubCompanyQueryParam subCompanyQueryParam) {
        ServiceResult<String, Page<SubCompany>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(subCompanyQueryParam.getPageNo(), subCompanyQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", subCompanyQueryParam.getStart());
        paramMap.put("pageSize", subCompanyQueryParam.getPageSize());
        paramMap.put("subCompanyQueryParam", subCompanyQueryParam);

        Integer totalCount = subCompanyMapper.listCount(paramMap);
        List<SubCompanyDO> dolist = subCompanyMapper.listPage(paramMap);
        List<SubCompany> list = CompanyConverter.convertSubCompanyDOList(dolist);
        Page<SubCompany> page = new Page<>(list, totalCount, subCompanyQueryParam.getPageNo(), subCompanyQueryParam.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, List<Department>> getDepartmentList(DepartmentQueryParam departmentQueryParam) {
        ServiceResult<String, List<Department>> result = new ServiceResult<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("departmentQueryParam", departmentQueryParam);

        List<DepartmentDO> departmentDOList = departmentMapper.listPage(paramMap);
        List<DepartmentDO> nodeList = DepartmentConverter.convertTree(departmentDOList);

        List<Department> resultList = new ArrayList<>();
        for (DepartmentDO node1 : nodeList) {
            resultList.add(DepartmentConverter.convertDepartmentDO(node1));
        }
        result.setResult(resultList);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, CompanyDepartmentTree> getCompanyDepartmentTree(DepartmentQueryParam departmentQueryParam) {
        ServiceResult<String, CompanyDepartmentTree> result = new ServiceResult<>();
        CompanyDepartmentTree companyDepartmentTree = new CompanyDepartmentTree();
        List<SubCompany> subCompanyList = new ArrayList<>();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        if (departmentQueryParam.getSubCompanyId() != null) {
            SubCompanyQueryParam subCompanyQueryParam = new SubCompanyQueryParam();
            subCompanyQueryParam.setSubCompanyId(departmentQueryParam.getSubCompanyId());
            paramMap.put("subCompanyQueryParam", subCompanyQueryParam);
        }

        List<SubCompanyDO> subCompanyDOList = subCompanyMapper.listPage(paramMap);
        if (subCompanyDOList != null && !subCompanyDOList.isEmpty()) {
            for (SubCompanyDO subCompanyDO : subCompanyDOList) {
                departmentQueryParam.setSubCompanyId(subCompanyDO.getId());
                paramMap.put("departmentQueryParam", departmentQueryParam);
                List<DepartmentDO> departmentDOList = departmentMapper.listPage(paramMap);
                List<DepartmentDO> nodeList = DepartmentConverter.convertTree(departmentDOList);
                subCompanyDO.setDepartmentDOList(nodeList);
                subCompanyList.add(CompanyConverter.convertSubCompany(subCompanyDO));
            }
        }
        companyDepartmentTree.setSubCompanyList(subCompanyList);

        result.setResult(companyDepartmentTree);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, CompanyDepartmentTree> getCompanyDepartmentUserTree(DepartmentQueryParam departmentQueryParam) {
        ServiceResult<String, CompanyDepartmentTree> result = new ServiceResult<>();
        CompanyDepartmentTree companyDepartmentTree = new CompanyDepartmentTree();
        List<SubCompany> subCompanyList = new ArrayList<>();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        if (departmentQueryParam.getSubCompanyId() != null) {
            SubCompanyQueryParam subCompanyQueryParam = new SubCompanyQueryParam();
            subCompanyQueryParam.setSubCompanyId(departmentQueryParam.getSubCompanyId());
            paramMap.put("subCompanyQueryParam", subCompanyQueryParam);
        }

        List<SubCompanyDO> subCompanyDOList = subCompanyMapper.listPage(paramMap);
        if (subCompanyDOList != null && !subCompanyDOList.isEmpty()) {
            for (SubCompanyDO subCompanyDO : subCompanyDOList) {
                departmentQueryParam.setSubCompanyId(subCompanyDO.getId());
                paramMap.put("departmentQueryParam", departmentQueryParam);
                List<DepartmentDO> departmentDOList = departmentMapper.getUserList(paramMap);
                List<DepartmentDO> nodeList = DepartmentConverter.convertTree(departmentDOList);
                subCompanyDO.setDepartmentDOList(nodeList);
                subCompanyList.add(CompanyConverter.convertSubCompany(subCompanyDO));
            }
        }
        companyDepartmentTree.setSubCompanyList(subCompanyList);

        result.setResult(companyDepartmentTree);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }
}
