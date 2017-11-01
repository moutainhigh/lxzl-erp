package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.dataaccess.domain.user.RoleUserFinalDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/12/26.
 * Time: 9:21.
 */
public interface RoleUserFinalMapper extends BaseMysqlDAO<RoleUserFinalDO> {

    Integer deleteByActiveUserAadPassiveUser(@Param("roleUserFinalDO") RoleUserFinalDO roleUserFinalDO);
    List<RoleUserFinalDO> getFinalRoleUserData(@Param("activeUserId") Integer activeUserId);
}
