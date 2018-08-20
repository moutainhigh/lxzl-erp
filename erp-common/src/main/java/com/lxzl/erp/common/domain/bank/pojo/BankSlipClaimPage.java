package com.lxzl.erp.common.domain.bank.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\8\15 0015 14:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipClaimPage implements Serializable{
    private Integer claimCount;//记录数
    private BigDecimal totalAmount;//总金额数
    private Page<BankSlipClaimDetail> bankSlipClaimDetailPage;//资金流水认领明细

    public Integer getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(Integer claimCount) {
        this.claimCount = claimCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Page<BankSlipClaimDetail> getBankSlipClaimDetailPage() {
        return bankSlipClaimDetailPage;
    }

    public void setBankSlipClaimDetailPage(Page<BankSlipClaimDetail> bankSlipClaimDetailPage) {
        this.bankSlipClaimDetailPage = bankSlipClaimDetailPage;
    }
}
