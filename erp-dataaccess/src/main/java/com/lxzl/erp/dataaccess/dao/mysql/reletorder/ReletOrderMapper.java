package com.lxzl.erp.dataaccess.dao.mysql.reletorder;

import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Set;

@Repository
public interface ReletOrderMapper extends BaseMysqlDAO<ReletOrderDO> {

	List<ReletOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	ReletOrderDO findDetailByReletOrderId(@Param("reletOrderId") Integer reletOrderId);

	Integer findReletOrderCountByParams(@Param("maps") Map<String, Object> paramMap);

	List<ReletOrderDO> findReletOrderByParams(@Param("maps") Map<String, Object> paramMap);

	ReletOrderDO findByReletOrderNo(@Param("reletOrderNo") String reletOrderNo);

	ReletOrderDO findRecentlyReletOrderByOrderNo(@Param("orderNo") String orderNo);

	ReletOrderDO findRecentlyReletedOrderByOrderId(@Param("orderId") Integer orderId);

	/**
	 * 查询订单下续租成功的单
	 * @param orderId
	 * @return
	 */
	List<ReletOrderDO> findReletedOrdersByOrderId(@Param("orderId") Integer orderId);

	List<ReletOrderDO> findRecentlyReletedOrderByParams(@Param("maps") Map<String, Object> paramMap);

	List<ReletOrderDO> findReletOrderByOrderNo(@Param("orderNo") String orderNo);

	/**
	 * 批量查找订单下关联续租单
	 * @param orderNos
	 * @return
	 */
	List<ReletOrderDO> findReletedOrdersByOrderNos(@Param("orderNos") Set<String> orderNos);

	void batchUpdate(@Param("list") List<ReletOrderDO> list);
}