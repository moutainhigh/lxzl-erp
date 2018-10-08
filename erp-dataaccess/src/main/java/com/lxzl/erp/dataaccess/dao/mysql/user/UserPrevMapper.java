package com.lxzl.erp.dataaccess.dao.mysql.user;

import com.lxzl.erp.dataaccess.domain.user.UserPrevDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPrevMapper extends BaseMysqlDAO<UserPrevDO> {

    Integer hasPrevilegeOfCurrUser(Integer userId);

    /**
     * 查询除管理员外所有用户信息
     * @return
     */
   // List<UserPrevDO> queryAllUsers();

    /**
     *  取消授权
     * @return
     */
    Integer deleteUserPrev(Integer userId);



    Integer hasPrevilegeOfCurrRole(Integer roleId);

    /**
     * 单个对象授权
      */
    Integer AddPrev(UserPrevDO userPrevDO);

    /**
     * 批量授权
     */
   Integer BatchPrev(List<UserPrevDO> userPrevDOS);
}
