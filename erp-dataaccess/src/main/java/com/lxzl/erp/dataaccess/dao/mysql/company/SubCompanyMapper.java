package com.lxzl.erp.dataaccess.dao.mysql.company;

import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubCompanyMapper extends BaseMysqlDAO<SubCompanyDO> {
    List<SubCompanyDO> listPage(@Param("subCompanyQueryParam") SubCompanyQueryParam subCompanyQueryParam, @Param("pageQuery") PageQuery pageQuery);
    Integer listCount(@Param("subCompanyQueryParam") SubCompanyQueryParam subCompanyQueryParam, @Param("pageQuery") PageQuery pageQuery);
}