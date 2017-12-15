package com.lxzl.erp.common.domain.payment;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 19:30
 */
public class PaymentIdentityParam implements Serializable {

    private String businessAppId;
    private String businessAppSecret;

    public String getBusinessAppId() {
        return businessAppId;
    }

    public void setBusinessAppId(String businessAppId) {
        this.businessAppId = businessAppId;
    }

    public String getBusinessAppSecret() {
        return businessAppSecret;
    }

    public void setBusinessAppSecret(String businessAppSecret) {
        this.businessAppSecret = businessAppSecret;
    }
}
