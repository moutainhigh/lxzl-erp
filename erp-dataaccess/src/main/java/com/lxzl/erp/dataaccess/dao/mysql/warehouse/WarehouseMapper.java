package com.lxzl.erp.dataaccess.dao.mysql.warehouse;

import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WarehouseMapper extends BaseMysqlDAO<WarehouseDO>{


    List<WarehouseDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    WarehouseDO finByNo(@Param("warehouseNo") String warehouseNo);

}