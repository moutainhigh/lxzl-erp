package com.lxzl.erp.common.domain.customer.pojo.dto;


/**
 * 公司信息，显示是否子母公司
 */
public class CustomerCompanyDTO {
    private Boolean isSubsidiary; //是否为子公司，1是子公司，0不是子公司
    private String parentCustomerNo; //母公司customerNo
    private String parentCustomerName; //母公司Name

    public Boolean getSubsidiary() {
        return isSubsidiary;
    }

    public void setSubsidiary(Boolean subsidiary) {
        isSubsidiary = subsidiary;
    }

    public String getParentCustomerNo() {
        return parentCustomerNo;
    }

    public void setParentCustomerNo(String parentCustomerNo) {
        this.parentCustomerNo = parentCustomerNo;
    }

    public String getParentCustomerName() {
        return parentCustomerName;
    }

    public void setParentCustomerName(String parentCustomerName) {
        this.parentCustomerName = parentCustomerName;
    }
}
