package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.RoleMenuQueryParam;
import com.lxzl.erp.common.domain.user.RoleQueryParam;
import com.lxzl.erp.common.domain.user.UserRoleQueryParam;
import com.lxzl.erp.common.domain.user.pojo.*;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
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

import java.util.List;

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

    @RequestMapping(value = "getRoleTree", method = RequestMethod.POST)
    public Result getRoleTree(@RequestBody @Validated({QueryGroup.class}) UserRoleQueryParam param, BindingResult validResult) {
        ServiceResult<String, RoleTree> serviceResult = userRoleService.getRoleTree(param);
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

    /**
     * 更新角色可观察部门（数据级）
     * @param roleDepartmentData
     * @param validResult
     * @return
     */
    @RequestMapping(value = "updateRoleDepartmentData", method = RequestMethod.POST)
    public Result updateRoleDepartmentData(@RequestBody @Validated({AddGroup.class}) RoleDepartmentData roleDepartmentData, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userRoleService.updateRoleDepartmentData(roleDepartmentData);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 查询角色可观察部门列表（数据级）
     * @param role
     * @param validResult
     * @return
     */
    @RequestMapping(value = "getRoleDepartmentDataListByRole", method = RequestMethod.POST)
    public Result getRoleDepartmentDataListByRole(@RequestBody @Validated({IdGroup.class}) Role role, BindingResult validResult) {
        ServiceResult<String, RoleDepartmentData> serviceResult = userRoleService.getRoleDepartmentDataListByRole(role.getRoleId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }




    /**
     * 更新用户可观察用户（数据级）
     * @param roleUserData
     * @param validResult
     * @return
     */
    @RequestMapping(value = "updateRoleUserData", method = RequestMethod.POST)
    public Result updateRoleUserData(@RequestBody @Validated({AddGroup.class}) RoleUserData roleUserData, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userRoleService.updateRoleUserData(roleUserData);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 查询观察者可观察用户列表（数据级）
     * @param roleUserData
     * @param validResult
     * @return
     */
    @RequestMapping(value = "getRoleUserDataListByUser", method = RequestMethod.POST)
    public Result getRoleUserDataListByUser(@RequestBody @Validated({ExtendGroup.class}) RoleUserData roleUserData, BindingResult validResult) {
        ServiceResult<String, RoleUserData> serviceResult = userRoleService.getRoleUserDataListByUser(roleUserData.getActiveUserId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }




    /**
     * 重新构建观察者最终可观察用户权限并保存（数据级）
     * @param roleUserData
     * @param validResult
     * @return
     */
    @RequestMapping(value = "rebuildFinalRoleUserData", method = RequestMethod.POST)
    public Result rebuildFinalRoleUserData(@RequestBody @Validated({ExtendGroup.class}) RoleUserData roleUserData, BindingResult validResult) {
        ServiceResult<String, RoleUserFinal> serviceResult = userRoleService.rebuildFinalRoleUserData(roleUserData.getActiveUserId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 获取用户最终数据权限（数据级）
     * @param roleUserData
     * @param validResult
     * @return
     */
    @RequestMapping(value = "getFinalRoleUserData", method = RequestMethod.POST)
    public Result getFinalRoleUserData(@RequestBody @Validated({ExtendGroup.class}) RoleUserData roleUserData, BindingResult validResult) {
        ServiceResult<String, RoleUserFinal> serviceResult = userRoleService.getFinalRoleUserData(roleUserData.getActiveUserId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ResultGenerator resultGenerator;
}
