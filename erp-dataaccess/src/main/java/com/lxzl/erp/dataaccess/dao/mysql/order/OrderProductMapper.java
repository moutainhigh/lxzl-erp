package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderProductMapper extends BaseMysqlDAO<OrderProductDO> {
    Integer findOrderProductCountByParams(@Param("maps") Map<String, Object> paramMap);
    List<OrderProductDO> findOrderProductByParams(@Param("maps") Map<String, Object> paramMap);
}
