package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erp.user.*;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/userRole")
@Controller
@ControllerLog
public class UserRoleController extends BaseController {


    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addRole(@RequestBody Role role, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userRoleService.addRole(role);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateRole(@RequestBody @Validated({IdGroup.class}) Role role, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userRoleService.updateRole(role);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result deleteRole(@RequestBody @Validated({IdGroup.class})Role role, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userRoleService.deleteRole(role.getRoleId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result page(@RequestBody RoleQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Role>> serviceResult = userRoleService.getRoleList(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    @RequestMapping(value = "saveUserRole", method = RequestMethod.POST)
    public Result saveUserRole(@RequestBody UserRole userRole, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userRoleService.saveUserRole(userRole);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getUserRoleList", method = RequestMethod.POST)
    public Result getUserRoleList(@RequestBody @Validated({QueryGroup.class}) UserRoleQueryParam param, BindingResult validResult) {
        ServiceResult<String, UserRole> serviceResult = userRoleService.getUserRoleList(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "saveRoleMenu", method = RequestMethod.POST)
    public Result saveRoleMenu(@RequestBody RoleMenu roleMenu, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userRoleService.saveRoleMenu(roleMenu);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getRoleMenuList", method = RequestMethod.POST)
    public Result getRoleMenuList(@RequestBody @Validated({QueryGroup.class}) RoleMenuQueryParam param, BindingResult validResult) {
        ServiceResult<String, RoleMenu> serviceResult = userRoleService.getRoleMenuList(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ResultGenerator resultGenerator;
}
