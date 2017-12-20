package com.lxzl.erp.common.domain.repairOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:50 2017/12/14
 * @Modified By:
 */
public class RepairOrderEquipmentQueryParam extends BasePageParam {

    private Integer repairOrderEquipmentId;   //唯一标识
    private String repairOrderNo;   //维修单编号
    private Integer equipmentId;   //设备ID
    @NotBlank(message = ErrorCode.EQUIPMENT_NO_NOT_NULL,groups = {AddGroup.class})
    private String equipmentNo;   //设备编号唯一
    private Date repairEndTime;   //维修完成时间
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private Integer orderId;   //订单ID，如果是在客户手里出现的维修，此字段不能为空
    private Integer orderProductId;   //订单商品项ID,如果是在客户手里出现的维修，此字段不能为空

    public Integer getRepairOrderEquipmentId() {
        return repairOrderEquipmentId;
    }

    public void setRepairOrderEquipmentId(Integer repairOrderEquipmentId) {
        this.repairOrderEquipmentId = repairOrderEquipmentId;
    }

    public String getRepairOrderNo() {
        return repairOrderNo;
    }

    public void setRepairOrderNo(String repairOrderNo) {
        this.repairOrderNo = repairOrderNo;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public Date getRepairEndTime() {
        return repairEndTime;
    }

    public void setRepairEndTime(Date repairEndTime) {
        this.repairEndTime = repairEndTime;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Integer orderProductId) {
        this.orderProductId = orderProductId;
    }
}
