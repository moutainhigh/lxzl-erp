package com.lxzl.erp.dataaccess.dao.mysql.dynamicSql;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.lxzl.erp.dataaccess.dao.mysql.dynamicSql.provider.DynamicSqlDaoProvider;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface DynamicSqlDao {

//    @SelectProvider(type = DynamicSqlDaoProvider.class, method = "executeSql")
//    List<Map> selectBySql(String sql) throws SQLException;

    @InsertProvider(type = DynamicSqlDaoProvider.class, method = "executeSql")
    int insertBySql(String sql) throws SQLException;

    @UpdateProvider(type = DynamicSqlDaoProvider.class, method = "executeSql")
    int updateBySql(String sql) throws SQLException;


}
