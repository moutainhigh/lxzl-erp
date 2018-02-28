package com.lxzl.erp.common.domain.repairOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairOrderCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String repairOrderNo;
    private Integer verifyUser;
    private String commitRemark;

    public String getRepairOrderNo() {
        return repairOrderNo;
    }

    public void setRepairOrderNo(String repairOrderNo) {
        this.repairOrderNo = repairOrderNo;
    }

    public Integer getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(Integer verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getCommitRemark() {
        return commitRemark;
    }

    public void setCommitRemark(String commitRemark) {
        this.commitRemark = commitRemark;
    }
}
