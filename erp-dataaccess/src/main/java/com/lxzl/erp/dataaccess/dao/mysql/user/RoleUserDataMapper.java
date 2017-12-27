package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.dataaccess.domain.user.RoleUserDataDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoleUserDataMapper extends BaseMysqlDAO<RoleUserDataDO> {
    Integer deleteByActiveAndPassive(@Param("activeUserId") Integer activeUserId, @Param("passiveUserId") Integer passiveUserId);
    List<RoleUserDataDO> getRoleUserDataListByActiveId(@Param("activeUserId") Integer activeUserId);
}