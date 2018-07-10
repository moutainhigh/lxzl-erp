package com.lxzl.erp.dataaccess.dao.jdbc.dynamicSql.impl;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/28
 */
/*
@Repository
public class DynamicSqlDaoImpl implements DynamicSqlDao {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Override
    public List<List<Object>> selectBySql(String selectSql, Integer limit) throws SQLException {
        // 获取当前事务的连接
        Connection connection = DataSourceUtils.getConnection(dynamicDataSource);

        List<List<Object>> result = new LinkedList<>();
        Statement sta = null;
        ResultSet rs = null;
        try {
            connection.setReadOnly(true);
            sta = connection.createStatement();
            rs = sta.executeQuery(selectSql);
            ResultSetMetaData rsm = rs.getMetaData();
            int columnCount = rsm.getColumnCount();
            List<Object> columnNameList = new LinkedList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNameList.add(rsm.getColumnLabel(i));
            }
            result.add(columnNameList);

            List<Object> columnList;
            Integer num = 1;
            while(rs.next()) {
                if (num > limit) break;
                num++;
                columnList = new LinkedList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnList.add(rs.getObject(i));
                }
                result.add(columnList);
            }
        } catch (SQLException e) {
            logger.error("动态SQL查询执行错误", e);
            throw e;
        } finally {
            DBUtil.closeConn(null, sta, rs);
            DataSourceUtils.releaseConnection(connection, dynamicDataSource);
        }

        return  result;
    }
}
*/