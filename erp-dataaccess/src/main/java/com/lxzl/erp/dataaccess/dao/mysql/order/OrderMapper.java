package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMysqlDAO<OrderDO> {

    OrderDO findByOrderId(@Param("orderId") Integer orderId);
    OrderDO findByOrderNo(@Param("orderNo") String orderNo);
    List<OrderDO> findByCustomerId(@Param("customerId") Integer customerId);
    Integer findOrderCountByParams(@Param("maps") Map<String, Object> paramMap);
    List<OrderDO> findOrderByParams(@Param("maps") Map<String, Object> paramMap);
}
