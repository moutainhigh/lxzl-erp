package com.lxzl.erp.common.domain.repairOrder;

import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderBulkMaterial;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderEquipment;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 10:26 2017/12/19
 * @Modified By:
 */

public class FixRepairOrderQueryParam {
    //todo 如何进行前端校验
    private List<RepairOrderEquipment> repairOrderEquipmentList;
    private List<RepairOrderBulkMaterial> repairOrderBulkMaterialList;

    public List<RepairOrderEquipment> getRepairOrderEquipmentList() {
        return repairOrderEquipmentList;
    }

    public void setRepairOrderEquipmentList(List<RepairOrderEquipment> repairOrderEquipmentIdList) {
        this.repairOrderEquipmentList = repairOrderEquipmentIdList;
    }

    public List<RepairOrderBulkMaterial> getRepairOrderBulkMaterialList() {
        return repairOrderBulkMaterialList;
    }

    public void setRepairOrderBulkMaterialList(List<RepairOrderBulkMaterial> repairOrderBulkMaterialIdList) {
        this.repairOrderBulkMaterialList = repairOrderBulkMaterialIdList;
    }
}
