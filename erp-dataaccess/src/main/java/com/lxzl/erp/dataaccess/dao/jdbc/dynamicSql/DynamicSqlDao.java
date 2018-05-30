package com.lxzl.erp.dataaccess.dao.jdbc.dynamicSql;

import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/28
 */
public interface DynamicSqlDao {

    /**
     * 考虑到mybaits无法满足动态sql一些需求，使用jdbc方式封装数据
     * 1. 连接查询需要允许重复列名，如id
     * 2. 无数据返回时也需要列名
     * @param selectSql
     * @return
     */
    List<List<Object>> selectBySql(@Param("selectSql") String selectSql, Integer limit) throws SQLException;
}
