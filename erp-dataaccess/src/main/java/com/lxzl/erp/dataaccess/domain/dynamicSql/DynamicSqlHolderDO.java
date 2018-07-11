package com.lxzl.erp.dataaccess.domain.dynamicSql;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicSqlHolderDO extends BaseDO {

    private Integer id;
    private String sqlContent;
    private String sqlTpye;
    private Integer status;
    private String results;
    private Integer dataStatus;

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
