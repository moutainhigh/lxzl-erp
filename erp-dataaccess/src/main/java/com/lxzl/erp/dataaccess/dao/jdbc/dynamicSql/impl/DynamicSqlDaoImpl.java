package com.lxzl.erp.dataaccess.dao.jdbc.dynamicSql.impl;

import com.lxzl.erp.common.util.DBUtil;
import com.lxzl.se.dataaccess.mysql.source.DynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/28
 */
@Repository
public class DynamicSqlDaoImpl {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<List<Object>> selectBySql(String selectSql) throws SQLException {
        List<List<Object>> result = jdbcTemplate.query(selectSql, new ResultSetExtractor<List<List<Object>>>() {
            @Override
            public List<List<Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<List<Object>> result = new LinkedList<>();
                ResultSetMetaData rsm = rs.getMetaData();
                int columnCount = rsm.getColumnCount();
                List<Object> columnNameList = new LinkedList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNameList.add(rsm.getColumnLabel(i));
                }
                result.add(columnNameList);
                List<Object> columnList;
                while (rs.next()) {
                    columnList = new LinkedList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnList.add(rs.getObject(i));
                    }
                    result.add(columnList);
                }
                return result;
            }
        });

        return result;
    }
}