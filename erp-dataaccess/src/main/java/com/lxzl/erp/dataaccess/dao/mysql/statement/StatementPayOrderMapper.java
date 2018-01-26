package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statement.StatementPayOrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface StatementPayOrderMapper extends BaseMysqlDAO<StatementPayOrderDO> {

    List<StatementPayOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    StatementPayOrderDO findByStatementOrderId(@Param("statementOrderId") Integer statementOrderId);

    StatementPayOrderDO findByNo(@Param("statementPayOrderNo") String statementPayOrderNo);
}