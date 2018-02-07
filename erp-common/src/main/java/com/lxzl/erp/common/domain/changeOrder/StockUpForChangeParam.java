package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockUpForChangeParam implements Serializable {

    @NotBlank(message = ErrorCode.CHANGE_ORDER_NO_NOT_NULL)
    private String changeOrderNo;
    private String equipmentNo;
    private Integer changeOrderMaterialId;
    @NotNull(message = ErrorCode.PARAM_IS_ERROR)
    private Integer operationType;

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

    public Integer getChangeOrderMaterialId() {
        return changeOrderMaterialId;
    }

    public void setChangeOrderMaterialId(Integer changeOrderMaterialId) {
        this.changeOrderMaterialId = changeOrderMaterialId;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}
