package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends BaseMysqlDAO<UserDO> {

    UserDO findByUsername(@Param("userName") String username);

    UserDO findByUserId(@Param("userId") Integer userId);

    List<UserDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<UserDO> getPassiveUserByUser(@Param("userId") Integer userId);

    UserDO findByUserRealName(@Param("realName") String realName);
}
