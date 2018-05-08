package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map;
@Repository
public interface OrderMapper extends BaseMysqlDAO<OrderDO> {

    OrderDO findByOrderId(@Param("orderId") Integer orderId);
    List<OrderDO> findByOrderParam(@Param("startTime")Date startTime,@Param("endTime")Date endTime);
    OrderDO findByOrderNo(@Param("orderNo") String orderNo);
    List<OrderDO> findByCustomerId(@Param("customerId") Integer customerId);
    Integer findOrderCountByParams(@Param("maps") Map<String, Object> paramMap);
    List<OrderDO> findOrderByParams(@Param("maps") Map<String, Object> paramMap);
    void updateListForReturn(@Param("orderDOList") List<OrderDO> orderDOList);

    /**
     * 根据订单项类型和订单项id查找关联的订单
     */
    OrderDO findByOrderItemTypeAndOrderItemReferId(@Param("orderItemType") Integer orderItemType, @Param("orderItemReferId") Integer orderItemReferId);

    /**
     * 已支付的订单总金额
     * */
    BigDecimal findPaidOrderAmount();

    List<Map<String,Object>> querySubCompanyOrderAmount(@Param("maps") Map<String, Object> paramMap);
    /** 根据订单号列表获取订单信息 */
    List<OrderDO> listByOrderNOs(@Param("orderNOs")Set<String> orderNOs);

    List<OrderDO> findVerifyOrderByParams(@Param("maps") Map<String, Object> maps);

    Integer findVerifyOrderCountByParams(@Param("maps") Map<String, Object> maps);

    OrderDO findConsignByCustomerNo(@Param("customerNo") String customerNo);

    List<OrderDO> findByOrderStatus(@Param("orderStatus") Integer orderStatus);

    Integer findOrderForReturnCountParam(@Param("maps") Map<String, Object> maps);

    List<OrderDO> findOrderForReturnParam(@Param("maps") Map<String, Object> maps);
}
