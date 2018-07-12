package com.lxzl.erp.common.domain.dynamicSql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import org.hibernate.validator.constraints.NotBlank;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicSqlSelectParam extends BasePageParam {

    @NotBlank(message = ErrorCode.PARAM_IS_NOT_NULL)
    private String sql;

    private Integer limit;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}