package com.lxzl.erp.common.domain.workbench;

import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;

import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/20
 * @Time : Created in 10:05
 */
public class WorkbenchStatementOrderQueryParam {

    List<StatementOrderQueryParam> statementOrderQueryParamList;

    public List<StatementOrderQueryParam> getStatementOrderQueryParamList() {
        return statementOrderQueryParamList;
    }

    public void setStatementOrderQueryParamList(List<StatementOrderQueryParam> statementOrderQueryParamList) {
        this.statementOrderQueryParamList = statementOrderQueryParamList;
    }
}
