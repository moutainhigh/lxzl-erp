package com.lxzl.erp.dataaccess.dao.mysql.repairOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderBulkMaterialDO;import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface RepairOrderBulkMaterialMapper extends BaseMysqlDAO<RepairOrderBulkMaterialDO> {

	List<RepairOrderBulkMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<RepairOrderBulkMaterialDO> findByRepairOrderNo(@Param("repairOrderNo")String repairOrderNo);

    Integer findRepairOrderBulkMaterialCountByParams(@Param("maps")Map<String, Object> maps);

    List<RepairOrderBulkMaterialDO> findRepairOrderBulkMaterialByParams(@Param("maps")Map<String, Object> maps);

    void clearDateStatus(@Param("repairOrderNo")String repairOrderNo);

    RepairOrderBulkMaterialDO findByBulkMaterialNoAndRepairOrderNo(@Param("bulkMaterialNo")String bulkMaterialNo,@Param("repairOrderNo")String repairOrderNo);

    void clearDateStatusByBulkMaterialNo(String bulkMaterialNo);

}