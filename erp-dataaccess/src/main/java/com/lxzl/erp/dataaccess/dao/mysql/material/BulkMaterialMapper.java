package com.lxzl.erp.dataaccess.dao.mysql.material;

import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BulkMaterialMapper extends BaseMysqlDAO<BulkMaterialDO> {

    Integer saveList(@Param("bulkMaterialDOList") List<BulkMaterialDO> bulkMaterialDOList);

    Integer updateList(@Param("bulkMaterialDOList") List<BulkMaterialDO> bulkMaterialDOList);

    Integer updateEquipmentOrderNo(@Param("equipmentNo") String equipmentNo, @Param("orderNo") String orderNo);

    List<BulkMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    BulkMaterialDO findByNo(@Param("bulkMaterialNo") String bulkMaterialNo);

    List<BulkMaterialDO> findByMaterialTypeAndModelId(@Param("materialType") Integer materialType,
                                                      @Param("materialModelId") Integer materialModelId);

    List<BulkMaterialDO> findRentByCustomerId(@Param("customerId") Integer customerId);

    Integer getRentBulkCountByOrderNo(@Param("orderNo") String orderNo);

    List<BulkMaterialDO> findByMaterialId(@Param("materialId") Integer materialId);
}