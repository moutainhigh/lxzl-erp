package com.lxzl.erp.dataaccess.dao.mysql.material;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.material.MaterialTypeDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface MaterialTypeMapper extends BaseMysqlDAO<MaterialTypeDO> {

	List<MaterialTypeDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}