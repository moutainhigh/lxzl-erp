package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeMaterialParam {

    @NotNull(message = ErrorCode.MATERIAL_NO_NOT_NULL,groups = {AddChangeOrderParam.class})
    private String materialNo;   //物料编号
    @NotNull(message = ErrorCode.CHANGE_COUNT_ERROR,groups = {AddChangeOrderParam.class})
    @Min(value = 1 , message = ErrorCode.CHANGE_COUNT_ERROR,groups = {AddChangeOrderParam.class})
    private Integer changeCount;

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public Integer getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }
}
