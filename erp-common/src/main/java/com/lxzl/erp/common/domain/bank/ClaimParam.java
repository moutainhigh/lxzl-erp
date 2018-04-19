package com.lxzl.erp.common.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.bank.ClaimBankSlipDetailGroup;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/3/22
 * @Time : Created in 20:12
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimParam extends BasePO {

    @NotNull(message = ErrorCode.CUSTOMER_NO_NOT_NULL,groups = {ClaimBankSlipDetailGroup.class})
    private String customerNo;
    @NotNull(message = ErrorCode.BANK_SLIP_CLAIM_AMOUNT_NOT_NULL,groups = {ClaimBankSlipDetailGroup.class})
    private BigDecimal claimAmount;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }
}
