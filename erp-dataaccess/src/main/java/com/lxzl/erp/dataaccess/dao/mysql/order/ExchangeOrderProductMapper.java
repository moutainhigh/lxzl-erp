package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.ExchangeOrderProductDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ExchangeOrderProductMapper extends BaseMysqlDAO<ExchangeOrderProductDO> {

	List<ExchangeOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	/**
	 * 批量添加
	 * @param list
	 * @return
	 */
	Integer saveList(@Param("list") List<ExchangeOrderProductDO> list);

	/**
	 * 根据订单id获取数据列表
	 * @param exchangeOrderId
	 * @return
	 */
	List<ExchangeOrderProductDO> findByExchangeOrderId(@Param("exchangeOrderId") Integer exchangeOrderId );
}