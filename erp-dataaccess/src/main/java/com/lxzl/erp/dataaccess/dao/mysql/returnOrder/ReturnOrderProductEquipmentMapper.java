package com.lxzl.erp.dataaccess.dao.mysql.returnOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ReturnOrderProductEquipmentMapper extends BaseMysqlDAO<ReturnOrderProductEquipmentDO> {

	List<ReturnOrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}