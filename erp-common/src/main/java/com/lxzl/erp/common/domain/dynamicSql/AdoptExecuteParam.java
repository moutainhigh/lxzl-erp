package com.lxzl.erp.common.domain.dynamicSql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdoptExecuteParam {

    @NotNull(message = ErrorCode.PARAM_IS_NOT_NULL)
    private Integer dynamicSqlHolderId;

    private String result;

    public Integer getDynamicSqlHolderId() {
        return dynamicSqlHolderId;
    }

    public void setDynamicSqlHolderId(Integer dynamicSqlHolderId) {
        this.dynamicSqlHolderId = dynamicSqlHolderId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
