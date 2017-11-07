package com.lxzl.erp.common.domain.purchase;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class PurchaseOrderCommitParam {

    @NotEmpty(message = ErrorCode.PURCHASE_ORDER_NO_NOT_NULL , groups = {ExtendGroup.class})
    private String purchaseOrderNo;
    @NotNull(message = ErrorCode.VERIFY_USER_NOT_NULL , groups = {ExtendGroup.class})
    private Integer verifyUserId;

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public Integer getVerifyUserId() {
        return verifyUserId;
    }

    public void setVerifyUserId(Integer verifyUserId) {
        this.verifyUserId = verifyUserId;
    }
}
