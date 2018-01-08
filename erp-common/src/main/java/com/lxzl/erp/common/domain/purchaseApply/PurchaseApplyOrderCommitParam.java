package com.lxzl.erp.common.domain.purchaseApply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseApplyOrderCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.PURCHASE_APPLY_ORDER_NO_NOT_NULL, groups = {ExtendGroup.class})
    private String purchaseApplyNo;

    public String getPurchaseApplyNo() {
        return purchaseApplyNo;
    }

    public void setPurchaseApplyNo(String purchaseApplyNo) {
        this.purchaseApplyNo = purchaseApplyNo;
    }
}
