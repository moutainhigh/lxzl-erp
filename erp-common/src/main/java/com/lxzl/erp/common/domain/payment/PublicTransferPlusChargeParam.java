package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Sunzhipeng
 * @Description: 批量加款参数
 * @Date: Created in 2018\6\7 0007 14:33
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicTransferPlusChargeParam extends PaymentIdentityParam{
    private List<ChargeRequestParam> chargeRequests;

    public List<ChargeRequestParam> getChargeRequests() {
        return chargeRequests;
    }

    public void setChargeRequests(List<ChargeRequestParam> chargeRequests) {
        this.chargeRequests = chargeRequests;
    }
}
