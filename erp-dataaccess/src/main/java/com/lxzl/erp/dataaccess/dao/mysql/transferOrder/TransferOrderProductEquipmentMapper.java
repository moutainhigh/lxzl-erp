package com.lxzl.erp.dataaccess.dao.mysql.transferOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.transferOrder.TransferOrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransferOrderProductEquipmentMapper extends BaseMysqlDAO<TransferOrderProductEquipmentDO> {

	List<TransferOrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<TransferOrderProductEquipmentDO> findByTransferOrderIdAndTransferOrderProductId(@Param("transferOrderId") Integer transferOrderId,@Param("transferOrderProductId")Integer transferOrderMaterialId);

	List<TransferOrderProductEquipmentDO> findByTransferOrderId(@Param("transferOrderId")Integer transferOrderId);

	void clearDateStatusByTransferOrderIdAndProductEquipmentNo(@Param("transferOrderId")Integer transferOrderId,@Param("productEquipmentNo")String productEquipmentNo);

	TransferOrderProductEquipmentDO findByTransferOrderIdAndEquipmentNo(@Param("transferOrderId")Integer transferOrderId,@Param("productEquipmentNo")String productEquipmentNo);

    Integer saveList(List<TransferOrderProductEquipmentDO> transferOrderProductEquipmentDOList);

	List<TransferOrderProductEquipmentDO> findByTransferOrderProductId(@Param("transferOrderProductId")Integer transferOrderProductId);
}