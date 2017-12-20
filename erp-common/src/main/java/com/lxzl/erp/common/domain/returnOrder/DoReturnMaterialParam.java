package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoReturnMaterialParam {

    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String returnOrderNo;
    @NotBlank(message = ErrorCode.MATERIAL_NO_NOT_NULL)
    private String materialNo;
    @NotNull(message = ErrorCode.CHANGE_COUNT_ERROR)
    @Min(value = 1,message = ErrorCode.CHANGE_COUNT_ERROR)
    private Integer returnCount;


    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

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
