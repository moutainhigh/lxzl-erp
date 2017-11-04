package com.lxzl.erp.core.service.company.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.core.service.user.impl.support.UserConverter;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-03 10:35
 */
public class DepartmentConverter {

    public static Department convertDepartmentDO(DepartmentDO departmentDO) {
        Department department = new Department();
        department.setDepartmentId(departmentDO.getId());
        BeanUtils.copyProperties(departmentDO, department);
        if(departmentDO.getChildren() != null && !departmentDO.getChildren().isEmpty()){
            List<Department> childList = new ArrayList<>();
            for(DepartmentDO departmentDOChild : departmentDO.getChildren()){
                childList.add(convertDepartmentDO(departmentDOChild));
            }
            department.setChildren(childList);
        }

        if(departmentDO.getRoleDOList() != null && !departmentDO.getRoleDOList().isEmpty()){
            department.setRoleList(UserConverter.convertUserRoleDOList(departmentDO.getRoleDOList()));
        }

        return department;
    }


    public static List<Department> convertDepartmentDOList(List<DepartmentDO>  departmentDOList) {
        List<Department> departmentList = new ArrayList<>();
        if(departmentDOList != null && !departmentDOList.isEmpty()){
            for(DepartmentDO departmentDO : departmentDOList){
                departmentList.add(convertDepartmentDO(departmentDO));
            }
        }
        return departmentList;
    }


    public static List<DepartmentDO> convertTree(List<DepartmentDO> departmentDOList) {
        List<DepartmentDO> nodeList = new ArrayList<>();
        if (departmentDOList != null) {
            for (DepartmentDO node1 : departmentDOList) {
                if (node1.getParentDepartmentId().equals(CommonConstant.SUPER_DEPARTMENT_ID)) {
                    nodeList.add(node1);
                }
                for (DepartmentDO t : departmentDOList) {
                    if (t.getParentDepartmentId().equals(node1.getId())) {
                        if (node1.getChildren() == null) {
                            List<DepartmentDO> myChildren = new ArrayList<DepartmentDO>();
                            myChildren.add(t);
                            node1.setChildren(myChildren);
                        } else {
                            node1.getChildren().add(t);
                        }
                    }
                }
            }
        }

        return nodeList;
    }
}
