package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCategoryDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface K3MappingCategoryMapper extends BaseMysqlDAO<K3MappingCategoryDO> {

	List<K3MappingCategoryDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	K3MappingCategoryDO findByErpCode(@Param("erpCode") String erpCode);

	K3MappingCategoryDO findByK3Code(@Param("k3Code") String k3Code);
}