package com.lxzl.erp.dataaccess.domain.dynamicSql;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicSqlHolderDO extends BaseDO {

    private Integer id;
    private String sqlContent;
    private String sqlTpye;
    private Integer status;
    private String results;
    private Integer dataStatus;

    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人


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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public enum Status {
        UNCHECK(0), CHECKED(1), REJECT(2);
        public int value;

        Status(int value) {
            this.value = value;
        }
    }
}
