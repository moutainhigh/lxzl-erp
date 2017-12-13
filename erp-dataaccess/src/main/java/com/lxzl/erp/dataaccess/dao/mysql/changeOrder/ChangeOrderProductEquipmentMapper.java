package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ChangeOrderProductEquipmentMapper extends BaseMysqlDAO<ChangeOrderProductEquipmentDO> {

	List<ChangeOrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}