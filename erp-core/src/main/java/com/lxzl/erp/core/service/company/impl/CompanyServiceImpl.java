package com.lxzl.erp.core.service.company.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.SubCompanyType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.CompanyDepartmentTree;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.domain.user.RoleQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.company.impl.support.CompanyConverter;
import com.lxzl.erp.core.service.company.impl.support.DepartmentConverter;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.se.common.util.StringUtil;
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

    @Autowired
    private RoleMapper roleMapper;

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
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
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
    public ServiceResult<String, Department> getDepartmentById(Integer departmentId) {
        ServiceResult<String, Department> result = new ServiceResult<>();
        DepartmentDO departmentDO = departmentMapper.findById(departmentId);
        if(departmentDO == null){
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }
        result.setResult(DepartmentConverter.convertDepartmentDO(departmentDO));
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

    @Override
    public ServiceResult<String, SubCompany> getHeaderCompany() {
        ServiceResult<String, SubCompany> result = new ServiceResult<>();
        Map<String, Object> paramMap = new HashMap<>();
        SubCompanyQueryParam subCompanyQueryParam = new SubCompanyQueryParam();
        subCompanyQueryParam.setSubCompanyType(SubCompanyType.SUB_COMPANY_TYPE_HEADER);
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("subCompanyQueryParam", subCompanyQueryParam);
        List<SubCompanyDO> subCompanyDOList = subCompanyMapper.listPage(paramMap);

        if (subCompanyDOList == null || subCompanyDOList.isEmpty()) {
            return result;
        }

        result.setResult(CompanyConverter.convertSubCompany(subCompanyDOList.get(0)));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> addDepartment(Department department) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        if (StringUtil.isBlank(department.getDepartmentName())
                || department.getDepartmentType() == null
                || department.getSubCompanyId() == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        if (department.getParentDepartmentId() == null) {
            department.setParentDepartmentId(CommonConstant.SUPER_DEPARTMENT_ID);
        } else {
            DepartmentDO parentDepartment = departmentMapper.findById(department.getParentDepartmentId());
            if (parentDepartment == null) {
                result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
                return result;
            }
        }

        DepartmentDO departmentDO = DepartmentConverter.convertDepartment(department);
        departmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        departmentDO.setCreateTime(currentTime);
        departmentDO.setUpdateTime(currentTime);
        departmentDO.setCreateUser(loginUser.getUserId().toString());
        departmentDO.setUpdateUser(loginUser.getUserId().toString());
        departmentMapper.save(departmentDO);

        result.setResult(departmentDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> updateDepartment(Department department) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        if (department.getParentDepartmentId() != null) {
            DepartmentDO parentDepartment = departmentMapper.findById(department.getParentDepartmentId());
            if (parentDepartment == null) {
                result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
                return result;
            }
        }

        DepartmentDO departmentDO = DepartmentConverter.convertDepartment(department);
        departmentDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        departmentDO.setUpdateTime(currentTime);
        departmentDO.setUpdateUser(loginUser.getUserId().toString());
        departmentMapper.update(departmentDO);

        result.setResult(departmentDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> deleteDepartment(Integer departmentId) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        DepartmentDO dbDepartment = departmentMapper.findById(departmentId);
        if (dbDepartment == null) {
            result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
            return result;
        }

        RoleQueryParam roleQueryParam = new RoleQueryParam();
        roleQueryParam.setDepartmentId(dbDepartment.getId());
        Map<String, Object> params = new HashMap<>();
        params.put("start", 0);
        params.put("pageSize", Integer.MAX_VALUE);
        params.put("roleQueryParam", roleQueryParam);
        Integer roleCount = roleMapper.findListCount(params);
        if (roleCount != null && roleCount > 0) {
            result.setErrorCode(ErrorCode.RECORD_USED_CAN_NOT_DELETE);
            return result;
        }

        dbDepartment.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        dbDepartment.setUpdateUser(loginUser.getUserId().toString());
        dbDepartment.setUpdateTime(currentTime);
        departmentMapper.update(dbDepartment);

        result.setResult(dbDepartment.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;

    }
}
