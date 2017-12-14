package com.lxzl.erp.common.domain.payment.account.pojo;

import com.lxzl.erp.common.domain.payment.PaymentIdentityParam;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 19:29
 */
public class CustomerAccountQueryParam extends PaymentIdentityParam {
    private String businessCustomerNo;

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }
}
