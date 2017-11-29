package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderDoReturnParam {

    private List<String> equipmentNoList;
    private List<String> materialNoList;

    public List<String> getEquipmentNoList() {
        return equipmentNoList;
    }

    public void setEquipmentNoList(List<String> equipmentNoList) {
        this.equipmentNoList = equipmentNoList;
    }

    public List<String> getMaterialNoList() {
        return materialNoList;
    }

    public void setMaterialNoList(List<String> materialNoList) {
        this.materialNoList = materialNoList;
    }
}
