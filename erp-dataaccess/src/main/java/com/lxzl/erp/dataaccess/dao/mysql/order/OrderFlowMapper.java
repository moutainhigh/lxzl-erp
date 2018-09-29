package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.common.domain.order.pojo.OrderFlow;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderFlowDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface OrderFlowMapper extends BaseMysqlDAO<OrderFlowDO> {

	List<OrderFlowDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	/**
	 * 根据原订单号获取订单流
	 * @param originalOrderNo
	 * @return
	 */
	List<OrderFlowDO> findByOriginalOrderNo(@Param("originalOrderNo") String originalOrderNo);
}