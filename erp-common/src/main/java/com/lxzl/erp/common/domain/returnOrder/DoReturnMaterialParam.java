package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DoReturnMaterialParam {

    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String returnOrderNo;
    @CollectionNotNull(message = ErrorCode.MATERIAL_NO_NOT_NULL)
    private List<String> materialNoList;

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public List<String> getMaterialNoList() {
        return materialNoList;
    }

    public void setMaterialNoList(List<String> materialNoList) {
        this.materialNoList = materialNoList;
    }
}
