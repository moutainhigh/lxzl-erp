package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnMaterialParam {

    @NotNull(message = ErrorCode.MATERIAL_NO_NOT_NULL)
    private String materialNo;   //物料编号
    @NotNull(message = ErrorCode.RETURN_COUNT_ERROR)
    @Min(value = 1, message = ErrorCode.RETURN_COUNT_ERROR)
    private Integer returnCount;

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }
}
