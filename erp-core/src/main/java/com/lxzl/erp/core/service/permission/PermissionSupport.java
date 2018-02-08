package com.lxzl.erp.core.service.permission;

import com.lxzl.erp.common.constant.PermissionType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.base.PermissionParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PermissionSupport {

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 获取可观察用户列表
     *
     * @return
     */
    public List<Integer> getCanAccessPassiveUserList(Integer userId) {

        //获取用户最【新的】最终可观察用户列表
        List<UserDO> userDOList = userMapper.getPassiveUserByUser(userId);
        List<Integer> passiveUserIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userDOList)) {
            for (UserDO userDO : userDOList) {
                passiveUserIdList.add(userDO.getId());
            }
        }
        return passiveUserIdList;
    }

    /**
     * 获取可查看仓库列表
     *
     * @return
     */
    public List<Integer> getCanAccessWarehouseIdList() {
        //数据级权限控制-查找用户可查看仓库列表
        ServiceResult<String, List<Warehouse>> warehouseListResult = warehouseService.getAvailableWarehouse();
        List<Warehouse> warehouseList = warehouseListResult.getResult();
        List<Integer> warehouseIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(warehouseList)) {
            for (Warehouse warehouse : warehouseList) {
                warehouseIdList.add(warehouse.getWarehouseId());
            }
        }
        return warehouseIdList;
    }

    /**
     * 获取可查看公司（售后和仓库类型用户不限制）
     *
     * @return
     */
    public Integer getCanAccessSubCompanyForService(Integer userId) {
        if (userSupport.isServicePerson(userId)) {
            return userSupport.getCompanyIdByUser(userId);
        }
        return null;
    }

    /**
     * 获取当前用户可查看公司
     */
    public Integer getCanAccessSubCompany(Integer userId) {
        return userSupport.getCompanyIdByUser(userId);
    }

    public PermissionParam getPermissionParam(Integer... permissionTypes) {
        Integer userId = userSupport.getCurrentUserId();
        //超级管理员不加权限控制
        if(userRoleService.isSuperAdmin(userId)){
            return null;
        }
        Set<Integer> permissionSet = new HashSet<>();
        for (Integer permissionType : permissionTypes) {
            permissionSet.add(permissionType);
        }
        PermissionParam permissionParam = new PermissionParam();
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_USER)) {
            permissionParam.setPermissionUserIdList(getCanAccessPassiveUserList(userId));
        }
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_WAREHOUSE)) {
            if (userSupport.isDepartmentPerson()) {
                permissionParam.setPermissionWarehouseIdList(getCanAccessWarehouseIdList());
            }
        }
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE)) {
            permissionParam.setPermissionSubCompanyId(getCanAccessSubCompanyForService(userId));
        }
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_SUB_COMPANY)) {
            permissionParam.setPermissionSubCompanyId(getCanAccessSubCompany(userId));
        }
        return permissionParam;
    }
}
