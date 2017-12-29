package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderTimeAxisDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface OrderTimeAxisMapper extends BaseMysqlDAO<OrderTimeAxisDO> {

	List<OrderTimeAxisDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<OrderTimeAxisDO> findByOrderId(@Param("orderId") Integer orderId);
}