package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderConfirmChangeLogDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface OrderConfirmChangeLogMapper extends BaseMysqlDAO<OrderConfirmChangeLogDO> {

	List<OrderConfirmChangeLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    OrderConfirmChangeLogDO findLastByOrderId(@Param("orderId")Integer orderId);
}