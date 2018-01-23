package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderPayParam implements Serializable {
    private String statementOrderNo;
    private Integer statementOrderPayType;

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
}
