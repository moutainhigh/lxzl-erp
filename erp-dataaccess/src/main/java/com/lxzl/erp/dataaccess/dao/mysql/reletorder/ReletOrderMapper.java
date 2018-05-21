package com.lxzl.erp.dataaccess.dao.mysql.reletorder;

import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ReletOrderMapper extends BaseMysqlDAO<ReletOrderDO> {

	List<ReletOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	ReletOrderDO findDetailByReletOrderId(@Param("reletOrderId") Integer reletOrderId);

	Integer findReletOrderCountByParams(@Param("maps") Map<String, Object> paramMap);

	List<ReletOrderDO> findReletOrderByParams(@Param("maps") Map<String, Object> paramMap);

	ReletOrderDO findByReletOrderNo(@Param("reletOrderNo") String reletOrderNo);

	ReletOrderDO findRecentlyReletOrderByOrderNo(@Param("orderNo") String orderNo);
}