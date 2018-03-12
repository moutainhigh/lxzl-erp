package com.lxzl.erp.common.domain.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.validGroup.customer.CommitCustomerGroup;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.CUSTOMER_NO_NOT_NULL , groups = {CommitCustomerGroup.class})
    private String customerNo;   //客戶编号
    private String remark;

    public String getCustomerNo() { return customerNo; }

    public void setCustomerNo(String customerNo) { this.customerNo = customerNo; }
}
