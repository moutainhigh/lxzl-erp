package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.dataaccess.domain.user.RoleMenuDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/12/26.
 * Time: 9:21.
 */
@Repository
public interface RoleMenuMapper extends BaseMysqlDAO<RoleMenuDO> {

    // 根据用户ID查找其拥有哪些角色
    RoleMenuDO findRoleMenu(@Param("roleId") Integer roleId,
                                      @Param("menuId") Integer menuId);
    void deleteMenuByRoleId(@Param("roleId") Integer roleId);
    List<Integer> findMenuListByRoleSet(@Param("roleSet") List<Integer> roleSet);
    List<RoleMenuDO> findListByRoleSet(@Param("roleSet") List<Integer> roleSet);
}
