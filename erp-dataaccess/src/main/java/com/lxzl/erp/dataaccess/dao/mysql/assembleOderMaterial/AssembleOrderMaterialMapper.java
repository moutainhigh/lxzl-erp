package com.lxzl.erp.dataaccess.dao.mysql.assembleOderMaterial;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.assembleOder.AssembleOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface AssembleOrderMaterialMapper extends BaseMysqlDAO<AssembleOrderMaterialDO> {

	List<AssembleOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}