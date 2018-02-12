package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingBrandDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface K3MappingBrandMapper extends BaseMysqlDAO<K3MappingBrandDO> {

	List<K3MappingBrandDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	K3MappingBrandDO findByErpCode(@Param("erpCode") String erpCode);

	K3MappingBrandDO findByK3Code(@Param("k3Code") String k3Code);
}