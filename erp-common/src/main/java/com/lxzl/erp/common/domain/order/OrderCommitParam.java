package com.lxzl.erp.common.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
