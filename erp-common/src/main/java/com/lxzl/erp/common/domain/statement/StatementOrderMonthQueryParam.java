package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018 2-3 09:11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderMonthQueryParam extends BasePageParam {

    private String statementOrderCustomerName;
    private Integer statementOrderCustomerId;
    private String statementOrderCustomerNo;
    @DateTimeFormat(pattern="yyyy-MM")
    private Date statementOrderStartTime;
    @DateTimeFormat(pattern="yyyy-MM")
    private Date statementOrderEndTime;
    private String statementOrderSubCompanyName;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date monthTime;
    private Integer subCompanyId;//分公司ID
    private String ownerName;//业务员

    private List<Integer> passiveUserIdList;//控制数据权限

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getStatementOrderCustomerId() { return statementOrderCustomerId; }

    public void setStatementOrderCustomerId(Integer statementOrderCustomerId) { this.statementOrderCustomerId = statementOrderCustomerId; }

    public String getStatementOrderCustomerNo() { return statementOrderCustomerNo; }

    public void setStatementOrderCustomerNo(String statementOrderCustomerNo) { this.statementOrderCustomerNo = statementOrderCustomerNo; }

    public Date getMonthTime() { return monthTime; }

    public void setMonthTime(Date monthTime) { this.monthTime = monthTime; }

    public String getStatementOrderCustomerName() { return statementOrderCustomerName; }

    public void setStatementOrderCustomerName(String statementOrderCustomerName) { this.statementOrderCustomerName = statementOrderCustomerName; }

    public List<Integer> getPassiveUserIdList() { return passiveUserIdList; }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) { this.passiveUserIdList = passiveUserIdList; }

    public String getStatementOrderSubCompanyName() {
        return statementOrderSubCompanyName;
    }

    public void setStatementOrderSubCompanyName(String statementOrderSubCompanyName) {
        this.statementOrderSubCompanyName = statementOrderSubCompanyName;
    }

    public Date getStatementOrderStartTime() {
        return statementOrderStartTime;
    }

    public void setStatementOrderStartTime(Date statementOrderStartTime) {
        this.statementOrderStartTime = statementOrderStartTime;
    }

    public Date getStatementOrderEndTime() {
        return statementOrderEndTime;
    }

    public void setStatementOrderEndTime(Date statementOrderEndTime) {
        this.statementOrderEndTime = statementOrderEndTime;
    }
}
