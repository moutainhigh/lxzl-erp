package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.user.UserSysDataPrivilegeDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface UserSysDataPrivilegeMapper extends BaseMysqlDAO<UserSysDataPrivilegeDO> {

	List<UserSysDataPrivilegeDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer saveList(@Param("list")List<UserSysDataPrivilegeDO> userSysDataPrivilegeDOList);

	UserSysDataPrivilegeDO findByUserId(@Param("userId")Integer userId);

    Integer hasPrevilegeOfCurrRole(@Param("roleId")Integer roleId);

	Integer hasPrevilegeOfCurrUser(@Param("userId")Integer currentUserId);
}