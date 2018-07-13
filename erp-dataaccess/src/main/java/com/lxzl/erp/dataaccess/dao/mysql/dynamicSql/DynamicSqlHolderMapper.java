package com.lxzl.erp.dataaccess.dao.mysql.dynamicSql;

import com.lxzl.erp.dataaccess.domain.dynamicSql.DynamicSqlHolderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DynamicSqlHolderMapper extends BaseMysqlDAO<DynamicSqlHolderDO> {
    List<DynamicSqlHolderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);
}
