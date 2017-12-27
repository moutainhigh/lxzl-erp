package com.lxzl.erp.dataaccess.dao.mysql.company;

import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SubCompanyMapper extends BaseMysqlDAO<SubCompanyDO> {
    List<SubCompanyDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);
}