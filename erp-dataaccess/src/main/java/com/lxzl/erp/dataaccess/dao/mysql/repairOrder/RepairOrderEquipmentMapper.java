package com.lxzl.erp.dataaccess.dao.mysql.repairOrder;

import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderEquipmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RepairOrderEquipmentMapper extends BaseMysqlDAO<RepairOrderEquipmentDO> {

    List<RepairOrderEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<RepairOrderEquipmentDO> findByRepairOrderNo(@Param("repairOrderNo") String repairOrderNo);

    void clearDateStatus(@Param("repairOrderNo") String repairOrderNo);

    Integer findRepairOrderEquipmentCountByParams(@Param("maps") Map<String, Object> maps);

    List<RepairOrderEquipmentDO> findRepairOrderEquipmentByParams(@Param("maps") Map<String, Object> maps);

    void clearDateStatusByEquipmentNo(@Param("equipmentNo") String equipmentNo);

    RepairOrderEquipmentDO findByEquipmentNoAndRepairOrderNo(@Param("equipmentNo") String equipmentNo, @Param("repairOrderNo") String repairOrderNo);

}