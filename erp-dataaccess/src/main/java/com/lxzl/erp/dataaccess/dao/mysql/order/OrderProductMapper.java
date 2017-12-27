package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface OrderProductMapper extends BaseMysqlDAO<OrderProductDO> {
    Integer findOrderProductCountByParams(@Param("maps") Map<String, Object> paramMap);

    List<OrderProductDO> findOrderProductByParams(@Param("maps") Map<String, Object> paramMap);

    List<OrderProductDO> findByOrderId(@Param("orderId") Integer orderId);

    List<Map<String, Object>> queryLastPrice(@Param("customerId") Integer customerId,
                                             @Param("productId") Integer productId);
}
