package com.lxzl.erp.dataaccess.dao.mysql.transferOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.transferOrder.TransferOrderMaterialBulkDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface TransferOrderMaterialBulkMapper extends BaseMysqlDAO<TransferOrderMaterialBulkDO> {

	List<TransferOrderMaterialBulkDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<TransferOrderMaterialBulkDO> findByTransferOrderIdAndTransferOrderMaterialId(@Param("transferOrderId") Integer transferOrderId,@Param("transferOrderMaterialId")Integer transferOrderMaterialId);

    List<TransferOrderMaterialBulkDO> findByTransferOrderId(@Param("transferOrderId")Integer transferOrderId);

}