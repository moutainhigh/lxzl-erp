package com.lxzl.erp.dataaccess.dao.mysql.dynamicSql;

import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DynamicSqlMapper extends BaseMysqlDAO<DynamicSqlDO> {

	List<DynamicSqlDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer findDynamicSqlCountByParams(@Param("maps") Map<String, Object> maps);

	List<DynamicSqlDO> findDynamicSqlByParams(@Param("maps") Map<String, Object> maps);
}