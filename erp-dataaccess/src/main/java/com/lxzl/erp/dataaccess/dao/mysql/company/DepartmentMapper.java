package com.lxzl.erp.dataaccess.dao.mysql.company;

import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface DepartmentMapper extends BaseMysqlDAO<DepartmentDO> {

	List<DepartmentDO> listPage(@Param("maps") Map<String, Object> paramMap);
	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<DepartmentDO> getRoleList(@Param("maps") Map<String, Object> paramMap);
	List<DepartmentDO> getUserList(@Param("maps") Map<String, Object> paramMap);

	/** 根据用户编号查找该用户所在部门编号 */
	DepartmentDO findByUserId(@Param(value = "userId") Integer userId);
}