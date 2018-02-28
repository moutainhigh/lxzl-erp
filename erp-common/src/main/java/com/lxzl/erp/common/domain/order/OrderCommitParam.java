package com.lxzl.erp.common.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCommitParam extends BaseCommitParam {

    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL)
    private String orderNo;
    private Integer verifyUser;
    private String commitRemark;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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
