package com.lxzl.erp.common.domain.statementOrderCorrect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.util.validate.constraints.In;

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
    private Integer subCompanyId; //分公司ID
    private String orderNo; //订单编号
    private String customerName; //客户名称
    private String returnOrderNo; //退货单编号

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

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
