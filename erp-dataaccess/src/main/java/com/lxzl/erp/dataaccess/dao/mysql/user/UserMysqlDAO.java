package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.common.domain.erp.user.UserPageParam;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMysqlDAO extends BaseMysqlDAO<UserDO> {

	UserDO findByUsername(@Param("userName") String username);
	UserDO findByUserId(@Param("userId") Integer userId);
	List<UserDO> listPage(@Param("userPageParam") UserPageParam userPageParam, @Param("pageQuery") PageQuery pageQuery);
	Integer listCount(@Param("userPageParam") UserPageParam userPageParam, @Param("pageQuery") PageQuery pageQuery);

}
