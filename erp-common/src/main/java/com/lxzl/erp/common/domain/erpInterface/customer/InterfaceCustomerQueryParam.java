package com.lxzl.erp.common.domain.erpInterface.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.erpInterface.ErpIdentityParam;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerNameGroup;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerNoGroup;

import javax.validation.constraints.NotNull;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/29
 * @Time : Created in 14:36
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceCustomerQueryParam extends ErpIdentityParam {

    @NotNull(message = ErrorCode.CUSTOMER_NO_NOT_NULL,groups = {QueryCustomerNoGroup.class})
    private String CustomerNo;
    @NotNull(message = ErrorCode.CUSTOMER_NAME_NOT_NULL,groups = {QueryCustomerNameGroup.class})
    private String CustomerName;

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }
}
