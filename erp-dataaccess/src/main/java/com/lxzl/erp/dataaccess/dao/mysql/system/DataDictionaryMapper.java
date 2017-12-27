package com.lxzl.erp.dataaccess.dao.mysql.system;

import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface DataDictionaryMapper extends BaseMysqlDAO<DataDictionaryDO> {

    List<DataDictionaryDO> findAllData(@Param("maps") Map<String, Object> paramMap);

    Integer findAllDataCount(@Param("maps") Map<String, Object> paramMap);

    List<DataDictionaryDO> findDataByType(@Param("dataType") Integer dataType);

    DataDictionaryDO findDataByOnlyOneType(@Param("dataType") Integer dataType);

    List<DataDictionaryDO> findByParentId(@Param("parentDictionaryId") Integer parentDictionaryId);

    DataDictionaryDO findByDictionaryId(@Param("dictionaryId") Integer dictionaryId);

}
