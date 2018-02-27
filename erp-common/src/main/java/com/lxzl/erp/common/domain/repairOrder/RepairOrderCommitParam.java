package com.lxzl.erp.common.domain.repairOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairOrderCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String repairOrderNo;

    public String getRepairOrderNo() {
        return repairOrderNo;
    }

    public void setRepairOrderNo(String repairOrderNo) {
        this.repairOrderNo = repairOrderNo;
    }
}
