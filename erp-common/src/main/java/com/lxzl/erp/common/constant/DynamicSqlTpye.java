package com.lxzl.erp.common.constant;

public enum DynamicSqlTpye {
    SELECT("SELECT", 1), //查询

    UPDATE("UPDATE", 2), //更新
    INSERT("INSERT", 3), //插入
    DELETE("DELETE", 4), //删除

    DEFAULT("DEFAULT", 8); //其他

    String sqlTpyeName;
    int level;

    DynamicSqlTpye(String sqlTpyeName, int level) {
        this.sqlTpyeName = sqlTpyeName;
    }

    public String getSqlTpyeName() {
        return sqlTpyeName;
    }

    public void setSqlTpyeName(String sqlTpyeName) {
        this.sqlTpyeName = sqlTpyeName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static int getHighest() {
        return DEFAULT.level;
    }
}