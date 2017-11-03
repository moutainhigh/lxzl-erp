package com.lxzl.erp.dataaccess.dao.mysql.warehouse;

import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface StockOrderMapper extends BaseMysqlDAO<StockOrderDO> {
    List<StockOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

}