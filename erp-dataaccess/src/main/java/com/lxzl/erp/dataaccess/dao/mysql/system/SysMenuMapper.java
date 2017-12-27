package com.lxzl.erp.dataaccess.dao.mysql.system;

import com.lxzl.erp.dataaccess.domain.system.SysMenuDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/1.
 * Time: 14:03.
 */
@Repository
public interface SysMenuMapper extends BaseMysqlDAO<SysMenuDO> {

    SysMenuDO findByMenuId(@Param("menuId") Integer menuId);

    List<SysMenuDO> findByMenuURL(@Param("menuUrl") String menuUrl);

    List<SysMenuDO> findRoleMenu(@Param("maps") Map<String, Object> paramMap);

    List<SysMenuDO> findAllMenu(@Param("maps") Map<String, Object> paramMap);

    List<SysMenuDO> findByParentId(@Param("parentMenuId") Integer parentMenuId);

}
