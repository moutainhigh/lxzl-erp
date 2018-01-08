package com.lxzl.erp.dataaccess.dao.mysql.assembleOderMaterialEquipment;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.assembleOder.AssembleOrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface AssembleOrderProductEquipmentMapper extends BaseMysqlDAO<AssembleOrderProductEquipmentDO> {

	List<AssembleOrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}