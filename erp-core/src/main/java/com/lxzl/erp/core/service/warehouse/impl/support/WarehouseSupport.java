package com.lxzl.erp.core.service.warehouse.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.SubCompanyType;
import com.lxzl.erp.common.constant.WarehouseType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 18:08
 */
@Component
public class WarehouseSupport {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private WarehouseMapper warehouseMapper;

    public WarehouseDO getCurrentWarehouse() {
        WarehouseDO returnWarehouseDO = null;
        WarehouseQueryParam param = new WarehouseQueryParam();
        User loginUser = userSupport.getCurrentUser();
        List<Integer> subCompanyIdList = new ArrayList<>();
        for (Role role : loginUser.getRoleList()) {
            if (SubCompanyType.SUB_COMPANY_TYPE_HEADER.equals(role.getSubCompanyType())) {
                subCompanyIdList.clear();
                break;
            }
            subCompanyIdList.add(role.getSubCompanyId());
        }
        param.setSubCompanyIdList(subCompanyIdList);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("warehouseQueryParam", param);
        List<WarehouseDO> warehouseList = warehouseMapper.listPage(paramMap);
        for (WarehouseDO warehouseDO : warehouseList) {
            if (WarehouseType.WAREHOUSE_TYPE_DEFAULT.equals(warehouseDO.getWarehouseType())) {
                returnWarehouseDO = warehouseDO;
            }
        }
        return returnWarehouseDO;
    }

    public WarehouseDO getSubCompanyWarehouse(Integer subCompanyId) {
        WarehouseDO returnWarehouseDO = null;
        WarehouseQueryParam param = new WarehouseQueryParam();
        List<Integer> subCompanyIdList = new ArrayList<>();
        subCompanyIdList.add(subCompanyId);
        param.setSubCompanyIdList(subCompanyIdList);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("warehouseQueryParam", param);
        List<WarehouseDO> warehouseList = warehouseMapper.listPage(paramMap);
        for (WarehouseDO warehouseDO : warehouseList) {
            if (WarehouseType.WAREHOUSE_TYPE_DEFAULT.equals(warehouseDO.getWarehouseType())) {
                returnWarehouseDO = warehouseDO;
            }
        }
        return returnWarehouseDO;
    }
}
