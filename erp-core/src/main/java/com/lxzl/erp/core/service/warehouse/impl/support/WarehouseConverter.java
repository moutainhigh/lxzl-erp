package com.lxzl.erp.core.service.warehouse.impl.support;

import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.erp.common.domain.warehouse.pojo.WarehousePosition;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehousePositionDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 17:57
 */
public class WarehouseConverter {

    public static List<Warehouse> convertWarehouseDOList(List<WarehouseDO> warehouseDOList) {
        List<Warehouse> warehouseList = new ArrayList<>();
        if (warehouseDOList != null && !warehouseDOList.isEmpty()) {
            for (WarehouseDO warehouseDO : warehouseDOList) {
                warehouseList.add(convertWarehouseDO(warehouseDO));
            }
        }

        return warehouseList;
    }

    public static Warehouse convertWarehouseDO(WarehouseDO warehouseDO) {
        Warehouse warehouse = new Warehouse();
        if (warehouseDO.getId() != null) {
            warehouse.setWarehouseId(warehouseDO.getId());
        }
        BeanUtils.copyProperties(warehouseDO, warehouse);
        if (warehouseDO.getWarehousePositionDOList() != null && !warehouseDO.getWarehousePositionDOList().isEmpty()) {
            warehouse.setWarehousePositionList(convertWarehousePositionDOList(warehouseDO.getWarehousePositionDOList()));
        }

        return warehouse;
    }

    public static List<WarehousePosition> convertWarehousePositionDOList(List<WarehousePositionDO> warehousePositionDOList) {
        List<WarehousePosition> warehousePositionList = new ArrayList<>();
        if (warehousePositionDOList != null && !warehousePositionDOList.isEmpty()) {
            for (WarehousePositionDO warehousePositionDO : warehousePositionDOList) {
                warehousePositionList.add(convertWarehousePositionDO(warehousePositionDO));
            }
        }
        return warehousePositionList;
    }


    public static WarehousePosition convertWarehousePositionDO(WarehousePositionDO warehousePositionDO) {
        WarehousePosition warehousePosition = new WarehousePosition();
        if (warehousePositionDO.getId() != null) {
            warehousePosition.setWarehousePositionId(warehousePositionDO.getId());
        }
        BeanUtils.copyProperties(warehousePositionDO, warehousePosition);
        return warehousePosition;
    }
}
