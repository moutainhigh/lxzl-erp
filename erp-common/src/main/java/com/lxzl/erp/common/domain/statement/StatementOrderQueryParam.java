package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderQueryParam extends BasePageParam {
    private String statementOrderCustomerName;
    private String statementOrderNo;
    private Integer statementOrderStatus;
    private Date statementExpectPayStartTime;
    private Date statementExpectPayEndTime;

    public String getStatementOrderNo() {
        return statementOrderNo;
    }

    public void setStatementOrderNo(String statementOrderNo) {
        this.statementOrderNo = statementOrderNo;
    }

    public Integer getStatementOrderStatus() {
        return statementOrderStatus;
    }

    public void setStatementOrderStatus(Integer statementOrderStatus) {
        this.statementOrderStatus = statementOrderStatus;
    }

    public Date getStatementExpectPayStartTime() {
        return statementExpectPayStartTime;
    }

    public void setStatementExpectPayStartTime(Date statementExpectPayStartTime) {
        this.statementExpectPayStartTime = statementExpectPayStartTime;
    }

    public Date getStatementExpectPayEndTime() {
        return statementExpectPayEndTime;
    }

    public void setStatementExpectPayEndTime(Date statementExpectPayEndTime) {
        this.statementExpectPayEndTime = statementExpectPayEndTime;
    }

    public String getStatementOrderCustomerName() {
        return statementOrderCustomerName;
    }

    public void setStatementOrderCustomerName(String statementOrderCustomerName) {
        this.statementOrderCustomerName = statementOrderCustomerName;
    }
}
