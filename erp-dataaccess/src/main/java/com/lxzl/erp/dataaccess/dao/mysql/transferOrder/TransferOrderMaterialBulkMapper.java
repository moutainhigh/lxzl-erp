package com.lxzl.erp.dataaccess.dao.mysql.transferOrder;

import com.lxzl.erp.dataaccess.domain.transferOrder.TransferOrderMaterialBulkDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransferOrderMaterialBulkMapper extends BaseMysqlDAO<TransferOrderMaterialBulkDO> {

	List<TransferOrderMaterialBulkDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<TransferOrderMaterialBulkDO> findByTransferOrderIdAndTransferOrderMaterialId(@Param("transferOrderId") Integer transferOrderId,@Param("transferOrderMaterialId")Integer transferOrderMaterialId);

    List<TransferOrderMaterialBulkDO> findByTransferOrderId(@Param("transferOrderId")Integer transferOrderId);

    List<TransferOrderMaterialBulkDO> findByTransferOrderMaterialId(@Param("transferOrderMaterialId")Integer transferOrderMaterialId);

    Integer findTransferOrderMaterialBulkCountByParams(@Param("maps")Map<String, Object> maps);

    List<TransferOrderMaterialBulkDO> findTransferOrderMaterialBulkByParams(@Param("maps")Map<String, Object> maps);

    Integer saveList(List<TransferOrderMaterialBulkDO> transferOrderMaterialBulkDOList);

    Integer updateList(@Param("transferOrderMaterialBulkDOList")List<TransferOrderMaterialBulkDO> transferOrderMaterialBulkDOList);
}