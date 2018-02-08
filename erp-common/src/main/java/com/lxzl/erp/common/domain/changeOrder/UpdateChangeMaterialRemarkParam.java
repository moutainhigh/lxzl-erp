package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateChangeMaterialRemarkParam {

    @NotNull(message = ErrorCode.CHANGE_ORDER_MATERIAL_ID_NOT_NULL)
    private Integer changeOrderMaterialId;
    @Size(max = 200,message = ErrorCode.REMARK_PATTERN)
    private String remark;

    public Integer getChangeOrderMaterialId() {
        return changeOrderMaterialId;
    }

    public void setChangeOrderMaterialId(Integer changeOrderMaterialId) {
        this.changeOrderMaterialId = changeOrderMaterialId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
