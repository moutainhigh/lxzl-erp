package com.lxzl.erp.common.domain.statementOrderCorrect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/30
 * @Time : Created in 11:23
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderCorrectQueryParam extends BasePageParam {

    private String statementCorrectNo;   //冲正单号
    private Integer statementOrderId;   //结算单ID
    private Integer statementOrderCorrectStatus;   //结算冲正单状态
    private Integer statementOrderDetailId;   //结算单项ID
    private Date createStartTime;
    private Date createEndTime;

    public String getStatementCorrectNo() {
        return statementCorrectNo;
    }

    public void setStatementCorrectNo(String statementCorrectNo) {
        this.statementCorrectNo = statementCorrectNo;
    }

    public Integer getStatementOrderId() {
        return statementOrderId;
    }

    public void setStatementOrderId(Integer statementOrderId) {
        this.statementOrderId = statementOrderId;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Integer getStatementOrderCorrectStatus() {
        return statementOrderCorrectStatus;
    }

    public void setStatementOrderCorrectStatus(Integer statementOrderCorrectStatus) {
        this.statementOrderCorrectStatus = statementOrderCorrectStatus;
    }

    public Integer getStatementOrderDetailId() {
        return statementOrderDetailId;
    }

    public void setStatementOrderDetailId(Integer statementOrderDetailId) {
        this.statementOrderDetailId = statementOrderDetailId;
    }
}
