package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderProductEquipmentSrcDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ChangeOrderProductEquipmentSrcMapper extends BaseMysqlDAO<ChangeOrderProductEquipmentSrcDO> {

	List<ChangeOrderProductEquipmentSrcDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}