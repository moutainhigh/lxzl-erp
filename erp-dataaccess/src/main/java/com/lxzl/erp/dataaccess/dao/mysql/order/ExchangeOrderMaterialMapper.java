package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.ExchangeOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ExchangeOrderMaterialMapper extends BaseMysqlDAO<ExchangeOrderMaterialDO> {

	List<ExchangeOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);


	/**
	 * 批量添加
	 * @param list
	 * @return
	 */
	Integer saveList(@Param("list") List<ExchangeOrderMaterialDO> list);

	/**
	 * 根据订单号获取
	 * @param exchangeOrderId
	 * @return
	 */
	List<ExchangeOrderMaterialDO> findByExchangeOrderId(@Param("exchangeOrderId") Integer exchangeOrderId );
}