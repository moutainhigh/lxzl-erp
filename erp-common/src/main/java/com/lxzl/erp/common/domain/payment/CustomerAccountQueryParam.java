package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.payment.PaymentIdentityParam;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 19:29
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAccountQueryParam extends PaymentIdentityParam {
    private String businessCustomerNo;

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }
}
