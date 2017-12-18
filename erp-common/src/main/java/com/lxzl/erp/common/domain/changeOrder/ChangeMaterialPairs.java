package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.changeOrder.AddChangeOrderGroup;
import com.lxzl.erp.common.domain.validGroup.changeOrder.UpdateChangeOrderGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeMaterialPairs {

    private Integer changeOrderMaterialId;
    @NotBlank(message = ErrorCode.MATERIAL_NO_NOT_NULL ,groups = {AddChangeOrderGroup.class,UpdateChangeOrderGroup.class})
    private String materialNoSrc;
    @NotBlank(message = ErrorCode.MATERIAL_NO_NOT_NULL ,groups = {AddChangeOrderGroup.class,UpdateChangeOrderGroup.class})
    private String materialNoDest;
    @NotNull(message = ErrorCode.CHANGE_COUNT_ERROR ,groups = {AddChangeOrderGroup.class,UpdateChangeOrderGroup.class})
    @Min(value = 0 , message = ErrorCode.CHANGE_COUNT_ERROR ,groups = {AddChangeOrderGroup.class,UpdateChangeOrderGroup.class})
    private Integer changeCount;

    public Integer getChangeOrderMaterialId() {
        return changeOrderMaterialId;
    }

    public void setChangeOrderMaterialId(Integer changeOrderMaterialId) {
        this.changeOrderMaterialId = changeOrderMaterialId;
    }

    public String getMaterialNoSrc() {
        return materialNoSrc;
    }

    public void setMaterialNoSrc(String materialNoSrc) {
        this.materialNoSrc = materialNoSrc;
    }

    public String getMaterialNoDest() {
        return materialNoDest;
    }

    public void setMaterialNoDest(String materialNoDest) {
        this.materialNoDest = materialNoDest;
    }

    public Integer getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }
}
