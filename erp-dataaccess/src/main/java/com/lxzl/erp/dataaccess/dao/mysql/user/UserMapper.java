package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMysqlDAO<UserDO> {

	UserDO findByUsername(@Param("userName") String username);
	UserDO findByUserId(@Param("userId") Integer userId);
	List<UserDO> listPage(@Param("userQueryParam") UserQueryParam userQueryParam, @Param("pageQuery") PageQuery pageQuery);
	Integer listCount(@Param("userQueryParam") UserQueryParam userQueryParam, @Param("pageQuery") PageQuery pageQuery);

	List<UserDO> getPassiveUserByUser(@Param("userId") Integer userId);
}
