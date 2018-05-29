package com.lxzl.erp.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/9
 */
public class DBUtil {

    private static Logger logger = LoggerFactory.getLogger(DBUtil.class);

    /**
     * 获取数据库连接
     * @param driverName
     * @param url
     * @param userName
     * @param password
     * @return
     */
    public static Connection getConn(String driverName, String url, String userName, String password) {
        Connection connection = null;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            logger.error("数据连接异常", e);
        }
        return connection;
    }

    /**
     * 关闭数据库连接
     * @param conn
     * @param stat
     * @param resultSet
     */
    public static void closeConn(Connection conn, Statement stat, ResultSet resultSet) {
        try {
            if (conn != null)
                conn.close();
            if (stat != null)
                stat.close();
            if (resultSet != null)
                resultSet.close();
            logger.info("关闭资源成功。。。");
        } catch (SQLException e) {
            logger.error("关闭连接异常", e);
        }
    }
}
