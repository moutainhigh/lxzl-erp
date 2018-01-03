package com.lxzl.erp.core.service.user.impl.support;

import com.lxzl.erp.common.cache.CommonCache;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.DepartmentType;
import com.lxzl.erp.common.constant.SubCompanyType;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.user.pojo.UserRole;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class UserSupport {

    @Autowired(required = false)
    private HttpSession httpSession;
    @Autowired
    private UserService userService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DepartmentMapper departmentMapper;


    public User getCurrentUser() {
        return (User) httpSession.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
    }

    public Integer getCurrentUserId() {
        User user = (User) httpSession.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        return user.getUserId();
    }

    /**
     * 判断用户是否属于总公司
     *
     * @return
     */

    public boolean isHeadUser(Integer userId) {
        List<Role> userRoleList = userService.getUserById(userId).getResult().getRoleList();
        return checkRoleListHaveHeaderCompany(userRoleList);
    }


    public Integer getCurrentUserCompanyId() {
        User user = (User) httpSession.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        List<Role> userRoleList = userService.getUserById(user.getUserId()).getResult().getRoleList();
        for (Role role : userRoleList) {
            return role.getSubCompanyId();
        }
        return null;
    }

    public Integer getCompanyIdByUser(Integer userId) {
        UserDO userDO = userMapper.findByUserId(userId);
        List<Role> userRoleList = userService.getUserById(userDO.getId()).getResult().getRoleList();
        for (Role role : userRoleList) {
            return role.getSubCompanyId();
        }
        return null;
    }

    /**
     * 判断当前用户是否属于总公司
     *
     * @return
     */

    public boolean isHeadUser() {
        List<Role> userRoleList = getCurrentUser().getRoleList();
        return checkRoleListHaveHeaderCompany(userRoleList);
    }

    private boolean checkRoleListHaveHeaderCompany(List<Role> userRoleList) {
        boolean flag = false;
        for (Role role : userRoleList) {
            //如果是总公司角色
            if (SubCompanyType.SUB_COMPANY_TYPE_HEADER.equals(role.getSubCompanyType())) {
                return true;
            }
        }
        return flag;
    }

    /**
     * 判断当前用户是否可以使用该仓库
     */

    public boolean checkCurrentUserWarehouse(Integer warehouseId) {
        boolean flag = false;
        List<Warehouse> warehouseList = warehouseService.getWarehouseByCurrentCompany().getResult();
        for (Warehouse warehouse : warehouseList) {
            if (warehouse.getWarehouseId().equals(warehouseId)) {
                return true;
            }
        }
        return flag;
    }

    /**
     * 是否是售后人员
     */
    public boolean isServicePerson(Integer userId) {
        List<Role> userRoleList = getCurrentUser().getRoleList();
        if (CollectionUtil.isNotEmpty(userRoleList)) {
            for (Role role : userRoleList) {
                DepartmentDO departmentDO = departmentMapper.findById(role.getDepartmentId());
                if (DepartmentType.DEPARTMENT_TYPE_SERVICE.equals(departmentDO.getDepartmentType())) {
                    return true;
                }
            }
        }

        return false;
    }
}
