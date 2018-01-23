package com.lxzl.erp.dataaccess.dao.mysql.transferOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.transferOrder.TransferOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface TransferOrderMaterialMapper extends BaseMysqlDAO<TransferOrderMaterialDO> {

	List<TransferOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<TransferOrderMaterialDO> findByTransferOrderId(@Param("transferOrderId")Integer transferOrderId);

    TransferOrderMaterialDO findByTransferOrderIdAndMaterialNoAndIsNew(@Param("transferOrderId")Integer transferOrderId,@Param("materialNo") String materialNo,@Param("isNew")Integer isNew);

}