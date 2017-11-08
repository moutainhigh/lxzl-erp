package com.lxzl.erp.common.domain.purchase;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class PurchaseOrderCommitParam {

    @NotEmpty(message = ErrorCode.PURCHASE_ORDER_NO_NOT_NULL , groups = {ExtendGroup.class})
    private String purchaseNo;
    @NotNull(message = ErrorCode.VERIFY_USER_NOT_NULL , groups = {ExtendGroup.class})
    private Integer verifyUserId;

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
}
