package com.lxzl.erp.common.domain.dynamicSql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdoptExecuteParam {

    @NotBlank(message = ErrorCode.PARAM_IS_NOT_NULL)
    private Integer dynamicSqlHolderId;

    public Integer getDynamicSqlHolderId() {
        return dynamicSqlHolderId;
    }

    public void setDynamicSqlHolderId(Integer dynamicSqlHolderId) {
        this.dynamicSqlHolderId = dynamicSqlHolderId;
    }
}
