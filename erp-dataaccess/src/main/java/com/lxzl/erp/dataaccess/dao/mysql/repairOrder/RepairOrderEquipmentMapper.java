package com.lxzl.erp.dataaccess.dao.mysql.repairOrder;

import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderEquipmentDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface RepairOrderEquipmentMapper extends BaseMysqlDAO<RepairOrderEquipmentDO> {

	List<RepairOrderEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<RepairOrderEquipmentDO> findByRepairOrderNo(@Param("repairOrderNo")String repairOrderNo);

    void clearDateStatus(String repairOrderNo);
}