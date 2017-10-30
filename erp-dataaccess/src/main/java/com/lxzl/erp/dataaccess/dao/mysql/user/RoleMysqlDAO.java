package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/15.
 * Time: 11:19.
 */
public interface RoleMysqlDAO extends BaseMysqlDAO<RoleDO> {

    RoleDO findByMapId(@Param("roleId") Integer roleId);

    List<RoleDO> findByUserId(@Param("userId") Integer userId);

    List<RoleDO> findList(@Param("maps") Map<String, Object> map);

    Integer findListCount(@Param("maps") Map<String, Object> map);
}
