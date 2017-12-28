package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
public interface StatementOrderMapper extends BaseMysqlDAO<StatementOrderDO> {

    List<StatementOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    StatementOrderDO findByCustomerAndPayTime(@Param("customerId") Integer customerId,
                                              @Param("payTime") Date payTime);

    StatementOrderDO findByNo(@Param("statementOrderNo") String statementOrderNo);
    //通过时间查找
    Integer findStatementOrderCountByDate(@Param("firstdayDate") Date firstdayDate,@Param("lastdayDate") Date lastdayDate);
}