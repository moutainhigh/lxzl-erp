package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.user.*;
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
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
     *
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
     *
     * @param user
     * @param validResult
     * @return
     */
    @RequestMapping(value = "enableUser", method = RequestMethod.POST)
    public Result enableUser(@RequestBody @Validated({IdGroup.class}) User user, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userService.enableUser(user);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    /**
     * 在线用户列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "onLineUserInfo", method = RequestMethod.POST)
    public Result onLineUserInfo(HttpServletRequest request, @RequestBody UserOnLineQueryParam userOnLineQueryParam) {
        ServiceResult<String, Page<Map<String, Object>>> result = new ServiceResult<>();
        List<Map<String, Object>> userlist = new ArrayList<>();//采用list装数据
        HttpSession httpSession = request.getSession();
        if (null != httpSession) {
            Map<String, HttpSession> onLineList = (Map<String, HttpSession>) httpSession.getServletContext().getAttribute("onLineMap");
            Iterator<Map.Entry<String, HttpSession>> it = onLineList.entrySet().iterator();
            Map<String, String> userMap = new HashMap<>();
            while (it.hasNext()) {
                HttpSession session = it.next().getValue();//拿到单个的session对象了
                if (null != session) {
                    Object user = session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
                    if (null != user) {
                        User userInfo = (User) user;
                        if (null != userMap.get(userInfo.getUserName())||StringUtil.isEmpty(userInfo.getUserName()))
                            continue;
                        userMap.put(userInfo.getUserName(),userInfo.getUserName());
                        Map<String, Object> userInfoMap = new HashMap<>();//采用map封装一行数据，然后放在list 中去，就是一个表的数据
                        //userInfoMap.put("id", session.getId());//获取session的id
                        userInfoMap.put("createTime", session.getCreationTime());//创建的时间.传过去的是date类型我们前台进行解析，显示出来
                        userInfoMap.put("lastAccessedTime", session.getLastAccessedTime());//上次访问的时间
                        userInfoMap.put("userName", userInfo.getUserName());
                        userInfoMap.put("RealName", userInfo.getRealName());
                        userlist.add(userInfoMap);
                    }
                } else {
                    it.remove();
                }
            }
        }
        int count = userlist.size();
        if (count > 0) {
            //自定义Comparator对象，自定义排序
            Comparator c = new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    if ((long) o1.get("createTime") < (long) o2.get("createTime")) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            };
            Collections.sort(userlist, c);
            int pageStar = (userOnLineQueryParam.getPageNo() - 1) * userOnLineQueryParam.getPageSize();
            List<Map<String, Object>> pageList = userlist.subList(pageStar, count - pageStar > userOnLineQueryParam.getPageSize() ? pageStar + userOnLineQueryParam.getPageSize() : count);
            Page<Map<String, Object>> page = new Page<>(pageList, count, userOnLineQueryParam.getPageNo(), userOnLineQueryParam.getPageSize());
            result.setResult(page);
            result.setErrorCode(ErrorCode.SUCCESS);
        }
        return resultGenerator.generate(result);
    }

    /**
     * 在线人数
     *
     * @param
     * @return
     */
    @RequestMapping(value = "onLineUserCount", method = RequestMethod.GET)
    public Result onLineUserCount(HttpServletRequest request) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Integer count = 0;
        HttpSession httpSession = request.getSession();
        if (null != httpSession) {
            Map<String, HttpSession> onLineList = (Map<String, HttpSession>) httpSession.getServletContext().getAttribute("onLineMap");
            Iterator<Map.Entry<String, HttpSession>> it = onLineList.entrySet().iterator();
            while (it.hasNext()) {
                HttpSession session = it.next().getValue();//拿到单个的session对象了
                Object user = session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
                if (null != user) {
                    count++;
                }
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(count);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 移除在线用户
     *
     * @param
     * @return
     */
    @RequestMapping(value = "delOnLineUser", method = RequestMethod.POST)
    public Result delOnLineUser(HttpServletRequest request, @RequestParam String name) {
        ServiceResult<String, Boolean> serviceResult = new ServiceResult<>();
        Boolean re = true;
        HttpSession httpSession = request.getSession();
        if (null != httpSession) {
            Map<String, HttpSession> onLineList = (Map<String, HttpSession>) httpSession.getServletContext().getAttribute("onLineMap");
            Iterator<Map.Entry<String, HttpSession>> it = onLineList.entrySet().iterator();
            while (it.hasNext()) {
                HttpSession session = it.next().getValue();//拿到单个的session对象了
                Object user = session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
                if (null != user) {
                    User userInfo = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
                    if (name.equals(userInfo.getUserName())) {
                        session.invalidate();
                        //break;
                    }
                }
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(re);
        return resultGenerator.generate(serviceResult);
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
