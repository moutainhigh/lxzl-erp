package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingMaterialTypeDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface K3MappingMaterialTypeMapper extends BaseMysqlDAO<K3MappingMaterialTypeDO> {

	List<K3MappingMaterialTypeDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	K3MappingMaterialTypeDO findByErpCode(@Param("erpCode") String erpCode);

    String findMaterialTypeIdByK3MaterialTypeCode(@Param("k3MaterialTypeCode")String k3MaterialTypeCode);
}