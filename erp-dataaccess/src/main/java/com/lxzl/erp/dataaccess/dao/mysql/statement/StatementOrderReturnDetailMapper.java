package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderReturnDetailDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface StatementOrderReturnDetailMapper extends BaseMysqlDAO<StatementOrderReturnDetailDO> {

	List<StatementOrderReturnDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<StatementOrderReturnDetailDO> findByReturnOrderId(@Param("returnOrderId") Integer returnOrderId,@Param("orderType") Integer orderType);

	List<StatementOrderReturnDetailDO> findByReturnOrderDetailId(@Param("returnOrderDetailId") Integer returnOrderDetailId,@Param("orderType") Integer orderType);

	void batchDelete(@Param("list") List<StatementOrderReturnDetailDO> list);
}