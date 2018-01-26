package com.lxzl.erp.common.domain.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-25 9:14
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeixinPayCallbackParam {
    private BigDecimal amount;
    private String orderNo;
    private String businessOrderNo;
    private String notifyStatus;

    public static final String NOTIFY_STATUS_SUCCESS = "1";
    public static final String NOTIFY_STATUS_FAIL = "2";
    public static final String NOTIFY_STATUS_IN_PROCESS = "3";

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBusinessOrderNo() {
        return businessOrderNo;
    }

    public void setBusinessOrderNo(String businessOrderNo) {
        this.businessOrderNo = businessOrderNo;
    }

    public String getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        this.notifyStatus = notifyStatus;
    }
}
