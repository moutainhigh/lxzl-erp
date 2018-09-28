package com.lxzl.erp.common.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import org.hibernate.validator.constraints.NotBlank;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeOrderParam {
    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String orderNo;

    private String exchangeOrderNo;

    public String getExchangeOrderNo() {
        return exchangeOrderNo;
    }

    public void setExchangeOrderNo(String exchangeOrderNo) {
        this.exchangeOrderNo = exchangeOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
