package com.lxzl.erp.common.domain.purchaseApply;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import org.hibernate.validator.constraints.NotBlank;

public class PurchaseApplyOrderCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.PURCHASE_ORDER_NO_NOT_NULL, groups = {ExtendGroup.class})
    private String purchaseNo;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

}
