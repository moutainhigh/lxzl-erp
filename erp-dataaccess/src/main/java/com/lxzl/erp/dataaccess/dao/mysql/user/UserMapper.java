package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.common.domain.dingding.member.DingdingUserDTO;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends BaseMysqlDAO<UserDO> {

    UserDO findByUsername(@Param("userName") String username);

    UserDO findByUserId(@Param("userId") Integer userId);

    List<UserDO> findByUserParam(@Param("startTime")Date startTime,@Param("endTime")Date endTime);

    List<UserDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<UserDO> getPassiveUserByUser(@Param("userId") Integer userId);

    UserDO findByUserRealName(@Param("realName") String realName);

    List<UserDO>  listAllUser();

    /** 根据钉钉用户数据传输列表获取用户列表信息 */
    List<UserDO>  findUsersByDingdingUsers(@Param("queryParams") List<DingdingUserDTO> dingdingUserDTOS);
    /** 根据id根性钉钉用户id */
    int updateDingdingUserIdById(@Param("user") UserDO userDO);
}
