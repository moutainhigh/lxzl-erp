package com.lxzl.erp.dataaccess.dao.mysql.company;

import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SubCompanyMapper extends BaseMysqlDAO<SubCompanyDO> {
    List<SubCompanyDO> listPage(@Param("maps") Map<String, Object> paramMap);
    Integer listCount(@Param("maps") Map<String, Object> paramMap);
}