package com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDO;import org.apache.ibatis.annotations.Param;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface StatementOrderCorrectMapper extends BaseMysqlDAO<StatementOrderCorrectDO> {

	List<StatementOrderCorrectDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	//通过查询参数查询 结算单 结算冲正单信息
    List<StatementOrderCorrectDO> findStatementOrderCorrectAndStatementOrderByQueryParam(@Param("maps") Map<String, Object> maps);

    List<StatementOrderCorrectDO> findStatementOrderId(@Param("statementOrderId") Integer statementOrderId);
    List<StatementOrderCorrectDO> findStatementOrderIdAndItemId(@Param("statementOrderId") Integer statementOrderId, @Param("orderReferId") Integer orderReferId, @Param("orderItemId") Integer orderItemId);

	List<StatementOrderCorrectDO> findStatementOrderIdAndItemIdAndReletReferId(@Param("statementOrderId") Integer statementOrderId, @Param("orderReferId") Integer orderReferId, @Param("orderItemId") Integer orderItemId, @Param("reletItemReferId") Integer reletItemReferId);

	StatementOrderCorrectDO findByNo(@Param("statementCorrectNo") String statementCorrectNo);

	List<StatementOrderCorrectDO> findStatementOrderIdAndReferId(@Param("statementOrderId")Integer statementOrderId,@Param("referId")Integer referId);

	int deleteByIds(@Param("list")List<Integer>ids);

}