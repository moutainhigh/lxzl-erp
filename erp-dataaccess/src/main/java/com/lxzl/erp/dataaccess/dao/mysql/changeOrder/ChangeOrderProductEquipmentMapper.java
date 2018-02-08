package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ChangeOrderProductEquipmentMapper extends BaseMysqlDAO<ChangeOrderProductEquipmentDO> {

	List<ChangeOrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<ChangeOrderProductEquipmentDO> findByChangeOrderNo(@Param("changeOrderNo") String changeOrderNo);

	ChangeOrderProductEquipmentDO findByChangeOrderNoAndDestEquipmentNo(@Param("changeOrderNo") String changeOrderNo,@Param("destEquipmentNo") String equipmentNo);

	ChangeOrderProductEquipmentDO findByChangeOrderNoAndSrcEquipmentNo(@Param("changeOrderNo") String changeOrderNo,@Param("destEquipmentNo") String equipmentNo);
}