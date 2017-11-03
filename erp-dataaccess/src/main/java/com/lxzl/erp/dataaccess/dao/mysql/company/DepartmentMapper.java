package com.lxzl.erp.dataaccess.dao.mysql.company;

import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface DepartmentMapper extends BaseMysqlDAO<DepartmentDO> {

	List<DepartmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}