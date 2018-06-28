package com.lxzl.erp.common.domain.dynamicSql;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 16:08 2018/6/13
 * @Modified By:
 */
public class DynamicSqlQueryParam extends BasePageParam {
    private String sqlTitle;   //sql语句标题

    public String getSqlTitle() {
        return sqlTitle;
    }

    public void setSqlTitle(String sqlTitle) {
        this.sqlTitle = sqlTitle;
    }
}
