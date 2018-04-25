package com.lxzl.erp.core.service.user;


import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.LoginParam;
import com.lxzl.erp.common.domain.user.UpdatePasswordParam;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

public interface UserService extends BaseService {

    /**
     * 登录
     *
     * @param loginParam 登录参数
     * @param ip         ip
     * @return 用户信息
     */
    ServiceResult<String, User> login(LoginParam loginParam, String ip);

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 用户ID
     */
    ServiceResult<String, Integer> addUser(User user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 用户ID
     */
    ServiceResult<String, Integer> updateUser(User user);

    /**
     * 修改用户密码
     *
     * @param user 用户信息
     * @return 用户ID
     */
    ServiceResult<String, Integer> updateUserPassword(User user);

    /**
     * 根据ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    ServiceResult<String, User> getUserById(Integer userId);

    /**
     * 查询用户列表（分页）
     *
     * @param userQueryParam 查询参数
     * @return 用户列表
     */
    ServiceResult<String, Page<User>> userPage(UserQueryParam userQueryParam);

    /**
     * 查询用户列表（非分页）
     *
     * @param userQueryParam 查询参数
     * @return 用户列表
     */
    ServiceResult<String, List<User>> getUserListByParam(UserQueryParam userQueryParam);

    /**
     * 修改用户禁用
     *
     * @param user
     * @return
     */
    ServiceResult<String, Integer> disabledUser(User user);

    /**
     * 修改用户不禁用
     *
     * @param user
     * @return
     */
    ServiceResult<String, Integer> enableUser(User user);

    /**
     * 未登录状态下修改密码
     *
     * @param updatePasswordParam
     * @return
     */
    ServiceResult<String, Integer> updatePasswordForNoLogin(UpdatePasswordParam updatePasswordParam);

}
