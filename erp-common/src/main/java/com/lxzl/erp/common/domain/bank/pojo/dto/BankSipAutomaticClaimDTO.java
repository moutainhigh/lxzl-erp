package com.lxzl.erp.common.domain.bank.pojo.dto;

/**
 * 资金流水自动认领DTO
 */
public class BankSipAutomaticClaimDTO {
    //当前用户名
    private String payerName;
    //匹配到最新的用户
    private String companyName;
    //匹配到最新的用户no
    private String companyNo;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }
}
