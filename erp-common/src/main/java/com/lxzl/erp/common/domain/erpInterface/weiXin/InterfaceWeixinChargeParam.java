package com.lxzl.erp.common.domain.erpInterface.weiXin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.erpInterface.ErpIdentityParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/29
 * @Time : Created in 18:53
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceWeixinChargeParam  extends ErpIdentityParam {
    @NotNull(message = ErrorCode.CUSTOMER_NO_NOT_NULL)
    private String customerNo;
    @NotNull(message = ErrorCode.AMOUNT_MAST_MORE_THEN_ZERO)
    private BigDecimal amount;  //充值金额
    @NotNull(message = ErrorCode.OPEN_ID_NOT_NULL)
    private String openId; //公众号唯一标识

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
