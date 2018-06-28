package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.erp.dataaccess.domain.statement.CheckStatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface StatementOrderMapper extends BaseMysqlDAO<StatementOrderDO> {

    List<StatementOrderDO> listSalePage(@Param("maps") Map<String, Object> paramMap);

    Integer listSaleCount(@Param("maps") Map<String, Object> paramMap);

    List<StatementOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    StatementOrderDO findByCustomerAndPayTime(@Param("customerId") Integer customerId,
                                              @Param("payTime") Date payTime);

    List<StatementOrderDO> findByCustomerId(@Param("customerId") Integer customerId);

    StatementOrderDO findByNo(@Param("statementOrderNo") String statementOrderNo);

    Integer listMonthCount(@Param("maps") Map<String, Object> paramMap);

    List<StatementOrderDO> listMonthPage(@Param("maps") Map<String, Object> paramMap);

    List<StatementOrderDO> findByCustomerNo(@Param("customerNo") String customerNo,@Param("monthTime") Date monthTime);

    void realDeleteStatementOrderList(List<StatementOrderDO> statementOrderDOList);

    List<CheckStatementOrderDO> exportListMonthPage(@Param("maps") Map<String, Object> paramMap);
}