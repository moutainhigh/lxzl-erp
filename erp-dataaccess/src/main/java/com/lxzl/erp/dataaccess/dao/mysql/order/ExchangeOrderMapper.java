package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.erp.common.domain.order.pojo.ExchangeOrder;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.ExchangeOrderDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ExchangeOrderMapper extends BaseMysqlDAO<ExchangeOrderDO> {

	List<ExchangeOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	/**
	 * 根据单号查询
	 * @param exchangeOrderNo
	 * @return
	 */
	ExchangeOrderDO findByExchangeOrderNo(@Param("exchangeOrderNo") String exchangeOrderNo);
}