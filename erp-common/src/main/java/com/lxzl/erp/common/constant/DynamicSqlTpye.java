package com.lxzl.erp.common.constant;

public enum DynamicSqlTpye {
    SELECT("SELECT"), //查询
    UPDATE("UPDATE"), //更新
    DELETE("DELETE"), //删除
    INSERT("INSERT"), //插入

    DEFAULT("DEFAULT"); //其他

    String sqlTpyeName;

    DynamicSqlTpye(String sqlTpyeName) {
        this.sqlTpyeName = sqlTpyeName;
    }

    public String getSqlTpyeName() {
        return sqlTpyeName;
    }

    public void setSqlTpyeName(String sqlTpyeName) {
        this.sqlTpyeName = sqlTpyeName;
    }
}