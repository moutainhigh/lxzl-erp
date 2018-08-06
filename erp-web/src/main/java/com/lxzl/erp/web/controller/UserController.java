package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.domain.user.LoginParam;
import com.lxzl.erp.common.domain.user.UpdatePasswordParam;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.web.util.NetworkUtil;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/user")
@Controller
@ControllerLog
public class UserController extends BaseController {
    /**
     * 添加用户
     *
     * @param user
     * @return Result
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addUser(@RequestBody @Validated({AddGroup.class}) User user, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userService.addUser(user);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 编辑用户
     *
     * @param user
     * @return Result
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateUser(@RequestBody @Validated({UpdateGroup.class}) User user, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userService.updateUser(user);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 修改密码
     *
     * @param user
     * @return Result
     */
    @RequestMapping(value = "updatePassword", method = RequestMethod.POST)
    public Result updateUserPassword(@RequestBody @Validated({ExtendGroup.class}) User user, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userService.updateUserPassword(user);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 未登录状态修改密码
     *
     * @param updatePasswordParam
     * @return Result
     */
    @RequestMapping(value = "updatePasswordForNoLogin", method = RequestMethod.POST)
    public Result updatePasswordForNoLogin(@RequestBody @Validated UpdatePasswordParam updatePasswordParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userService.updatePasswordForNoLogin(updatePasswordParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 用户登录
     *
     * @param loginParam
     * @return Result
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestBody LoginParam loginParam) {
        return resultGenerator.generate(userService.login(loginParam, NetworkUtil.getIpAddress(request)));
    }

    @RequestMapping(value = "getUserById", method = RequestMethod.POST)
    public Result getUserById(@RequestBody UserQueryParam userQueryParam) {
        ServiceResult<String, User> serviceResult = userService.getUserById(userQueryParam.getUserId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getCurrentUser", method = RequestMethod.POST)
    public Result getCurrentUser() {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        return resultGenerator.generate(ErrorCode.SUCCESS, loginUser);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result page(@RequestBody UserQueryParam userQueryParam) {
        ServiceResult<String, Page<User>> serviceResult = userService.userPage(userQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 用户退出
     *
     * @return Result
     */
    @RequestMapping(value = "/logout")
    public Result logout() {
        session.setAttribute(CommonConstant.ERP_USER_SESSION_KEY, null);
        session.invalidate();
        return resultGenerator.generate(ErrorCode.SUCCESS);
    }

    /**
     * 用户禁用
     * @param user
     * @param validResult
     * @return
     */
    @RequestMapping(value = "disabledUser", method = RequestMethod.POST)
    public Result disabledUser(@RequestBody @Validated({IdGroup.class}) User user, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userService.disabledUser(user);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 用户不禁用
     * @param user
     * @param validResult
     * @return
     */
    @RequestMapping(value = "enableUser", method = RequestMethod.POST)
    public Result enableUser(@RequestBody @Validated({IdGroup.class}) User user, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userService.enableUser(user);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private ResultGenerator resultGenerator;
}
