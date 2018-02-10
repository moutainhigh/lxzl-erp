package com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface StatementOrderCorrectDetailMapper extends BaseMysqlDAO<StatementOrderCorrectDetailDO> {

    StatementOrderCorrectDetailDO findByStatementDetailIdAndType(@Param("statementDetailId") Integer statementDetailId, @Param("statementDetailType") Integer statementDetailType);

    List<StatementOrderCorrectDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);
}