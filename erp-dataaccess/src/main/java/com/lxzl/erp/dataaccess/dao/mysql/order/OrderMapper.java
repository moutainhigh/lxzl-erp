package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
public interface OrderMapper extends BaseMysqlDAO<OrderDO> {

    OrderDO findByOrderId(@Param("orderId") Integer orderId);
    OrderDO findByOrderIdAndTime(@Param("orderId") Integer id, @Param("typeStartTime")Date typeStartTime, @Param("typeEndTime")Date typeEndTime);
    OrderDO findByOrderNo(@Param("orderNo") String orderNo);
    List<OrderDO> findByCustomerId(@Param("customerId") Integer customerId);
    Integer findOrderCountByParams(@Param("maps") Map<String, Object> paramMap);
    List<OrderDO> findOrderByParams(@Param("maps") Map<String, Object> paramMap);
    void updateListForReturn(@Param("orderDOList") List<OrderDO> orderDOList);

    /**
     * 已支付的订单总金额
     * */
    BigDecimal findPaidOrderAmount();

    List<Map<String,Object>> querySubCompanyOrderAmount(@Param("maps") Map<String, Object> paramMap);
}
