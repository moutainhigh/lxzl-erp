package com.lxzl.erp.common.domain.purchase;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class PurchaseOrderCommitParam {

    @NotBlank(message = ErrorCode.PURCHASE_ORDER_NO_NOT_NULL , groups = {ExtendGroup.class})
    private String purchaseNo;
    @NotNull(message = ErrorCode.VERIFY_USER_NOT_NULL , groups = {ExtendGroup.class})
    private Integer verifyUserId;
    private String remark;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Integer getVerifyUserId() {
        return verifyUserId;
    }

    public void setVerifyUserId(Integer verifyUserId) {
        this.verifyUserId = verifyUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
