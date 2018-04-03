package com.lxzl.erp.common.domain.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.validGroup.customer.CommitCustomerGroup;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerConsignCommitParam extends BaseCommitParam {

    @NotNull(message = ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS , groups = {CommitCustomerGroup.class})
    private Integer customerConsignId;   //客戶地址id

    public Integer getCustomerConsignId() { return customerConsignId; }

    public void setCustomerConsignId(Integer customerConsignId) { this.customerConsignId = customerConsignId; }
}
