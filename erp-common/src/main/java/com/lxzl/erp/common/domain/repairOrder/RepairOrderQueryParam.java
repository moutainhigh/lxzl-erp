package com.lxzl.erp.common.domain.repairOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import org.hibernate.validator.constraints.NotBlank;


/**
 * @Author: your name
 * @Description：
 * @Date: Created in 16:21 2017/12/14
 * @Modified By:
 */
public class RepairOrderQueryParam extends BasePageParam {

    private Integer repairOrderId;   //唯一标识
    @NotBlank(message = ErrorCode.REPAIR_ORDER_NO_IS_NOT_NULL,groups = {IdGroup.class})
    private String repairOrderNo;   //维修单编号
    private String repairReason;   //维修原因，由发起人填写
    private Integer repairOrderStatus;   //维修单状态，0-初始化维修单,4-审核中,8-待维修,12-维修中,16-维修完成回库,20-取消维修

    private Integer repairEquipmentCount; //送修设备数量
    private Integer repairBulkMaterialCount; //送修物料数量
    private Integer fixEquipmentCount; //修复设备数量
    private Integer fixBulkMaterialCount; //修复物料数量
    private String warehouseNo;//当前仓库编号

    public Integer getRepairOrderId() {
        return repairOrderId;
    }

    public void setRepairOrderId(Integer repairOrderId) {
        this.repairOrderId = repairOrderId;
    }

    public String getRepairReason() {
        return repairReason;
    }

    public void setRepairReason(String repairReason) {
        this.repairReason = repairReason;
    }

    public Integer getRepairOrderStatus() {
        return repairOrderStatus;
    }

    public void setRepairOrderStatus(Integer repairOrderStatus) {
        this.repairOrderStatus = repairOrderStatus;
    }

    public String getRepairOrderNo() {
        return repairOrderNo;
    }

    public void setRepairOrderNo(String repairOrderNo) {
        this.repairOrderNo = repairOrderNo;
    }

    public Integer getRepairEquipmentCount() {
        return repairEquipmentCount;
    }

    public void setRepairEquipmentCount(Integer repairEquipmentCount) {
        this.repairEquipmentCount = repairEquipmentCount;
    }

    public Integer getRepairBulkMaterialCount() {
        return repairBulkMaterialCount;
    }

    public void setRepairBulkMaterialCount(Integer repairBulkMaterialCount) {
        this.repairBulkMaterialCount = repairBulkMaterialCount;
    }

    public Integer getFixEquipmentCount() {
        return fixEquipmentCount;
    }

    public void setFixEquipmentCount(Integer fixEquipmentCount) {
        this.fixEquipmentCount = fixEquipmentCount;
    }

    public Integer getFixBulkMaterialCount() {
        return fixBulkMaterialCount;
    }

    public void setFixBulkMaterialCount(Integer fixBulkMaterialCount) {
        this.fixBulkMaterialCount = fixBulkMaterialCount;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

}
