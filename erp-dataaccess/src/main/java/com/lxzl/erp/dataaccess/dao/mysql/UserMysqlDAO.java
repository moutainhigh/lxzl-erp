package com.lxzl.erp.dataaccess.dao.mysql;

import org.apache.ibatis.annotations.Param;

import com.lxzl.erp.dataaccess.domain.UserDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;

public interface UserMysqlDAO extends BaseMysqlDAO<UserDO> {

	UserDO findByUsername(@Param("username") String username);

}
