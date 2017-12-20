package com.lxzl.erp.common.domain.repairOrder;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 10:26 2017/12/19
 * @Modified By:
 */
public class FixRepairOrderQueryParam {
    private List<Integer> repairEquipmentIdList;
    private List<Integer> repairBulkMaterialIdList;

    public List<Integer> getRepairEquipmentIdList() {
        return repairEquipmentIdList;
    }

    public void setRepairEquipmentIdList(List<Integer> repairEquipmentIdList) {
        this.repairEquipmentIdList = repairEquipmentIdList;
    }

    public List<Integer> getRepairBulkMaterialIdList() {
        return repairBulkMaterialIdList;
    }

    public void setRepairBulkMaterialIdList(List<Integer> repairBulkMaterialIdList) {
        this.repairBulkMaterialIdList = repairBulkMaterialIdList;
    }
}
