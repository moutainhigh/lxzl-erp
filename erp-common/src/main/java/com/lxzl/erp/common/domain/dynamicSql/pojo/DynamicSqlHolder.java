package com.lxzl.erp.common.domain.dynamicSql.pojo;

import com.lxzl.erp.common.domain.base.BasePO;

import java.util.Date;

public class DynamicSqlHolder extends BasePO {
    private Integer id;
    private String sqlContent;
    private String sqlTpye;
    private Integer status;
    private String results;
    private Integer dataStatus;
    private Date

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSqlContent() {
        return sqlContent;
    }

    public void setSqlContent(String sqlContent) {
        this.sqlContent = sqlContent;
    }

    public String getSqlTpye() {
        return sqlTpye;
    }

    public void setSqlTpye(String sqlTpye) {
        this.sqlTpye = sqlTpye;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }
}
