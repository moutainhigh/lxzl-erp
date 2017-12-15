package com.lxzl.erp.common.domain.changeOrder;

import com.lxzl.erp.common.constant.ErrorCode;
import org.hibernate.validator.constraints.NotBlank;

public class ProcessNoChangeEquipmentParam {

    @NotBlank(message = ErrorCode.CHANGE_ORDER_NO_NOT_NULL)
    private String changeOrderNo;
    @NotBlank(message = ErrorCode.EQUIPMENT_NO_NOT_NULL)
    private String equipmentNo;

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }
}
