package com.lxzl.erp.common.domain.statementOrderCorrect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.validGroup.statementOrderCorrect.CommitGroup;

import javax.validation.constraints.NotNull;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/2/9
 * @Time : Created in 14:15
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderCorrectParam extends BaseCommitParam{
    @NotNull(message = ErrorCode.STATEMENT_ORDER_CORRECT_NO_NOT_NULL ,groups = {CommitGroup.class})
    private String statementCorrectNo;

    public String getStatementCorrectNo() {
        return statementCorrectNo;
    }

    public void setStatementCorrectNo(String statementCorrectNo) {
        this.statementCorrectNo = statementCorrectNo;
    }
}
