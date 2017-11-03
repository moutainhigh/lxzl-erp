package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.dataaccess.domain.user.UserDepartmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDepartmentMapper extends BaseMysqlDAO<UserDepartmentDO> {

    List<UserDepartmentDO> findListByUserId(@Param("userId") Integer userId);

    List<UserDepartmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);
}