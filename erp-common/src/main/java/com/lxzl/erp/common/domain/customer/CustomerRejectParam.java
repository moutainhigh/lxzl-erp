package com.lxzl.erp.common.domain.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRejectParam {

    private String customerNo;   //客戶编号
    private String remark;

    public String getCustomerNo() { return customerNo; }

    public void setCustomerNo(String customerNo) { this.customerNo = customerNo; }

    public String getRemark() { return remark; }

    public void setRemark(String remark) { this.remark = remark; }
}
