package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

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
    private Date monthTime;

    public Integer getStatementOrderCustomerId() { return statementOrderCustomerId; }

    public void setStatementOrderCustomerId(Integer statementOrderCustomerId) { this.statementOrderCustomerId = statementOrderCustomerId; }

    public String getStatementOrderCustomerNo() { return statementOrderCustomerNo; }

    public void setStatementOrderCustomerNo(String statementOrderCustomerNo) { this.statementOrderCustomerNo = statementOrderCustomerNo; }

    public Date getMonthTime() { return monthTime; }

    public void setMonthTime(Date monthTime) { this.monthTime = monthTime; }

    public String getStatementOrderCustomerName() { return statementOrderCustomerName; }

    public void setStatementOrderCustomerName(String statementOrderCustomerName) { this.statementOrderCustomerName = statementOrderCustomerName; }
}
