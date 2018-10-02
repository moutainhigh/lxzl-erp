package com.lxzl.erp.common.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 提交换货单
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeOrderCommitParam extends BaseCommitParam {
    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String exchangeOrderNo;
    private Integer verifyUser;
    private String commitRemark;

    public String getExchangeOrderNo() {
        return exchangeOrderNo;
    }

    public void setOrderNo(String exchangeOrderNo) {
        this.exchangeOrderNo = exchangeOrderNo;
    }

    public Integer getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(Integer verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getCommitRemark() {
        return commitRemark;
    }

    public void setCommitRemark(String commitRemark) {
        this.commitRemark = commitRemark;
    }
}
