package com.lxzl.erp.dataaccess.dao.mysql.transferOrder;

import com.lxzl.erp.dataaccess.domain.transferOrder.TransferOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransferOrderMapper extends BaseMysqlDAO<TransferOrderDO> {

	List<TransferOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer findTransferOrderCountByParams(@Param("maps") Map<String, Object> maps);

	List<TransferOrderDO> findTransferOrderByParams(@Param("maps") Map<String, Object> maps);

    TransferOrderDO findByNo(@Param("transferOrderNo") String transferOrderNo);

    TransferOrderDO findDetailById(@Param("transferOrderId") Integer transferOrderId);

    TransferOrderDO findDetailByNo(@Param("transferOrderNo") String transferOrderNo);
}