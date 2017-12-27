package com.lxzl.erp.dataaccess.dao.mysql.warehouse;

import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderEquipmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface StockOrderEquipmentMapper extends BaseMysqlDAO<StockOrderEquipmentDO> {

	Integer saveList(@Param("stockOrderEquipmentDOList")List<StockOrderEquipmentDO> stockOrderEquipmentDOList);

	List<StockOrderEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<StockOrderEquipmentDO> findByStockOrderNo(@Param("stockOrderNo")String stockOrderNo);
}