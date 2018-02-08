package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderQueryParam extends BasePageParam {
    private String statementOrderCustomerName;
    private Integer statementOrderCustomerId;
    private String statementOrderCustomerNo;
    private String statementOrderNo;
    private Integer statementOrderStatus;
    private Date statementExpectPayStartTime;
    private Date statementExpectPayEndTime;
    private Integer isNeedToPay;
    private Integer isOverdue;
    private String orderNo;
    private String returnOrderNo;
    private String changeOrderNo;
    private Date createStartTime;
    private Date createEndTime;

    private Integer owner;		//数据归属人，跟单员
    private String ownerName; //业务员姓名

    //控制数据权限
    private List<Integer> passiveUserIdList;

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

    public String getStatementOrderNo() {
        return statementOrderNo;
    }

    public void setStatementOrderNo(String statementOrderNo) {
        this.statementOrderNo = statementOrderNo;
    }

    public Integer getStatementOrderStatus() {
        return statementOrderStatus;
    }

    public void setStatementOrderStatus(Integer statementOrderStatus) { this.statementOrderStatus = statementOrderStatus; }

    public Date getStatementExpectPayStartTime() {
        return statementExpectPayStartTime;
    }

    public void setStatementExpectPayStartTime(Date statementExpectPayStartTime) { this.statementExpectPayStartTime = statementExpectPayStartTime; }

    public Date getStatementExpectPayEndTime() {
        return statementExpectPayEndTime;
    }

    public void setStatementExpectPayEndTime(Date statementExpectPayEndTime) { this.statementExpectPayEndTime = statementExpectPayEndTime; }

    public String getStatementOrderCustomerName() {
        return statementOrderCustomerName;
    }

    public void setStatementOrderCustomerName(String statementOrderCustomerName) { this.statementOrderCustomerName = statementOrderCustomerName; }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public Integer getStatementOrderCustomerId() {
        return statementOrderCustomerId;
    }

    public void setStatementOrderCustomerId(Integer statementOrderCustomerId) { this.statementOrderCustomerId = statementOrderCustomerId; }

    public String getStatementOrderCustomerNo() {
        return statementOrderCustomerNo;
    }

    public void setStatementOrderCustomerNo(String statementOrderCustomerNo) { this.statementOrderCustomerNo = statementOrderCustomerNo; }

    public Integer getIsNeedToPay() {
        return isNeedToPay;
    }

    public void setIsNeedToPay(Integer isNeedToPay) {
        this.isNeedToPay = isNeedToPay;
    }

    public Integer getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Integer isOverdue) {
        this.isOverdue = isOverdue;
    }

    public Integer getOwner() { return owner; }

    public void setOwner(Integer owner) { this.owner = owner; }

    public String getOwnerName() { return ownerName; }

    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public List<Integer> getPassiveUserIdList() { return passiveUserIdList; }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) { this.passiveUserIdList = passiveUserIdList; }
}
