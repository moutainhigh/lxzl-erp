package com.lxzl.erp.dataaccess.dao.mysql.transferOrder;

import com.lxzl.erp.dataaccess.domain.transferOrder.TransferOrderProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransferOrderProductMapper extends BaseMysqlDAO<TransferOrderProductDO> {

	List<TransferOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<TransferOrderProductDO> findByTransferOrderId(@Param("transferOrderId")Integer transferOrderId);

    TransferOrderProductDO findByTransferOrderIdAndSkuIdAndIsNew(@Param("transferOrderId")Integer transferOrderId,@Param("productSkuId")Integer productSkuId,@Param("isNew")Integer isNew);

}