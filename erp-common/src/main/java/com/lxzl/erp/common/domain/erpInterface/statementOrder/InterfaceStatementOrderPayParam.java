package com.lxzl.erp.common.domain.erpInterface.statementOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.erpInterface.ErpIdentityParam;

import javax.validation.constraints.NotNull;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/29
 * @Time : Created in 19:00
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceStatementOrderPayParam  extends ErpIdentityParam {
    @NotNull(message = ErrorCode.STATEMENT_ORDER_NO_NOT_NULL)
    private String statementOrderNo;
    private Integer statementOrderPayType;
    @NotNull(message = ErrorCode.OPEN_ID_NOT_NULL)
    private String openId;  //公众号唯一标识

    public String getStatementOrderNo() {
        return statementOrderNo;
    }

    public void setStatementOrderNo(String statementOrderNo) {
        this.statementOrderNo = statementOrderNo;
    }

    public Integer getStatementOrderPayType() {
        return statementOrderPayType;
    }

    public void setStatementOrderPayType(Integer statementOrderPayType) {
        this.statementOrderPayType = statementOrderPayType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
