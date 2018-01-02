package com.lxzl.erp.core.service.dataAccess;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.WarehouseService;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataAccessSupport {

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseService warehouseService;
    public void setDataAccessPassiveUserList(BasePageParam basePageParam){

        //数据级权限控制-查找用户可查看用户列表
        Integer currentUserId = userSupport.getCurrentUserId();
        //获取用户最【新的】最终可观察用户列表
        List<UserDO> userDOList = userMapper.getPassiveUserByUser(currentUserId);
        List<Integer> passiveUserIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userDOList)) {
            for (UserDO userDO : userDOList) {
                passiveUserIdList.add(userDO.getId());
            }
        }
        basePageParam.setDataAccessUserIdList(passiveUserIdList);
    }

    public void setDataAccessWarehouseList(BasePageParam basePageParam){

        //数据级权限控制-查找用户可查看仓库列表
        ServiceResult<String, List<Warehouse>> warehouseListResult = warehouseService.getAvailableWarehouse();
        List<Warehouse> warehouseList = warehouseListResult.getResult();
        List<Integer> warehouseIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(warehouseList)) {
            for (Warehouse warehouse : warehouseList) {
                warehouseIdList.add(warehouse.getWarehouseId());
            }
        }
        basePageParam.setDataAccessWarehouseIdList(warehouseIdList);
    }
    public void setDataAccessSubCompany(BasePageParam basePageParam){
        Integer subCompanyId = userSupport.getCurrentUserCompanyId();
        basePageParam.setDataAccessSubCompanyId(subCompanyId);
    }

}
