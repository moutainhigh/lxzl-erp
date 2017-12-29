package com.lxzl.erp.dataaccess.dao.mysql.material;

import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
public interface BulkMaterialMapper extends BaseMysqlDAO<BulkMaterialDO> {

    Integer saveList(List<BulkMaterialDO> bulkMaterialDOList);

    Integer updateList(@Param("bulkMaterialDOList") List<BulkMaterialDO> bulkMaterialDOList);

    Integer updateEquipmentOrderNo(@Param("equipmentNo") String equipmentNo, @Param("orderNo") String orderNo);

    Integer updateEquipmentBulkMaterialStatus(@Param("equipmentNo") String equipmentNo, @Param("bulkMaterialStatus") Integer bulkMaterialStatus);

    List<BulkMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    BulkMaterialDO findByNo(@Param("bulkMaterialNo") String bulkMaterialNo);

    List<BulkMaterialDO> findByMaterialTypeAndModelId(@Param("materialType") Integer materialType,
                                                      @Param("materialModelId") Integer materialModelId);

    List<BulkMaterialDO> findRentByCustomerId(@Param("customerId") Integer customerId);

    List<BulkMaterialDO> findRentByCustomerIdAndMaterialId(@Param("customerId") Integer customerId, @Param("materialId") Integer materialId);

    Integer getRentBulkCountByOrderNo(@Param("orderNo") String orderNo);

    List<BulkMaterialDO> findByMaterialId(@Param("materialId") Integer materialId);

    List<BulkMaterialDO> findByPurchaseOrderNo(@Param("purchaseOrderNo") String purchaseOrderNo);

    List<BulkMaterialDO> listByPurchaseReceiveOrderMaterialId(@Param("maps") Map<String, Object> paramMap);

    Integer listByPurchaseReceiveOrderMaterialIdCount(@Param("maps") Map<String, Object> paramMap);
}