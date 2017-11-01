package com.lxzl.erp.core.service.company.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.company.impl.support.CompanyConverter;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@Service
public class CompanyServiceImpl implements CompanyService{

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Override
    public ServiceResult<String, Integer> addSubCompany(SubCompany subCompany) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        SubCompanyDO subCompanyDO = CompanyConverter.convertSubCompany(subCompany);
        subCompanyDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_CREATE);
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
        Integer totalCount = subCompanyMapper.listCount(subCompanyQueryParam, pageQuery);
        List<SubCompanyDO> dolist = subCompanyMapper.listPage(subCompanyQueryParam, pageQuery);
        List<SubCompany> list = CompanyConverter.convertSubCompanyDOList(dolist);
        Page<SubCompany> page = new Page<>(list, totalCount, subCompanyQueryParam.getPageNo(), subCompanyQueryParam.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


}
