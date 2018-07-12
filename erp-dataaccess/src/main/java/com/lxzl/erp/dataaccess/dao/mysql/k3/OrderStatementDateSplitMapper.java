package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.OrderStatementDateSplitDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface OrderStatementDateSplitMapper extends BaseMysqlDAO<OrderStatementDateSplitDO> {

	List<OrderStatementDateSplitDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	OrderStatementDateSplitDO findByOrderNo(@Param("orderNo")String orderNo);
}