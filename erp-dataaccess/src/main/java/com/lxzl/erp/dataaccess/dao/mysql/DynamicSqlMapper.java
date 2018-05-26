package com.lxzl.erp.dataaccess.dao.mysql;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
@Repository
public interface DynamicSqlMapper extends BaseMysqlDAO {

    List<Map> selectBySql(@Param("selectSql") String selectSql);
}
