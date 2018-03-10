package com.lxzl.erp.core.service.permission;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.base.PermissionParam;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
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
    @Autowired
    private CompanyService companyService;
    @Autowired
    private DepartmentMapper departmentMapper;

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

    public List<Integer> getCanAccessSubCompanyAllList() {
        ServiceResult<String, List<SubCompany>> subCompanyAll = companyService.subCompanyAll();
        List<SubCompany> subCompanyList = subCompanyAll.getResult();
        List<Integer> passiveSubCompanyIdList = new ArrayList<>();;

        if (CollectionUtil.isNotEmpty(subCompanyList)) {
            for (SubCompany subCompany : subCompanyList) {
                passiveSubCompanyIdList.add(subCompany.getSubCompanyId());
            }
        }
        return passiveSubCompanyIdList;
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
     * 根据仓库获取可查看公司
     *
     * @return
     */
    public List<Integer> getCanAccessSubCompanyIdList() {
        ServiceResult<String, List<Warehouse>> warehouseListResult = warehouseService.getAvailableWarehouse();
        List<Warehouse> warehouseList = warehouseListResult.getResult();
        List<Integer> subCompanyIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(warehouseList)) {
            for (Warehouse warehouse : warehouseList) {
                subCompanyIdList.add(warehouse.getSubCompanyId());
            }
        }
        return subCompanyIdList;
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

    public Integer getCanAccessOrderSubCompany(Integer userId) {
        UserDO userDO = userMapper.findByUserId(userId);
        for(RoleDO roleDO :userDO.getRoleDOList()){
            if(!CommonConstant.HEADER_COMPANY_ID.equals(roleDO.getSubCompanyId())){
                DepartmentDO departmentDO = departmentMapper.findById(roleDO.getDepartmentId());
                if(DepartmentType.DEPARTMENT_TYPE_BUSINESS_AFFAIRS.equals(departmentDO.getDepartmentType())){
                    return roleDO.getSubCompanyId();
                }
            }
        }
        return null;
    }

    public PermissionParam getPermissionParam(Integer... permissionTypes) {
        Integer userId = userSupport.getCurrentUserId();
        //超级管理员不加权限控制
        if(userId==null||userRoleService.isSuperAdmin(userId)){
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
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_SUB_COMPANY_ALL)) {
            permissionParam.setPermissionSubCompanyIdList(getCanAccessSubCompanyAllList());
        }
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE)) {
            permissionParam.setPermissionSubCompanyId(getCanAccessSubCompanyForService(userId));
        }
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_SUB_COMPANY)) {
            permissionParam.setPermissionSubCompanyId(getCanAccessSubCompany(userId));
        }
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_WAREHOUSE_SUB_COMPANY)) {
            permissionParam.setPermissionSubCompanyIdList(getCanAccessSubCompanyIdList());
        }
        if (permissionSet.contains(PermissionType.PERMISSION_TYPE_ORDER_SUB_COMPANY)) {
            permissionParam.setPermissionSubCompanyId(getCanAccessOrderSubCompany(userId));
        }
        return permissionParam;
    }
}
