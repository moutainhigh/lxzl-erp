package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface StatementOrderDetailMapper extends BaseMysqlDAO<StatementOrderDetailDO> {

    List<StatementOrderDetailDO> findByOrderId(@Param("orderId") Integer orderId);

    List<StatementOrderDetailDO> findByOrderItemTypeAndId(@Param("orderItemType") Integer orderItemType, @Param("orderItemId") Integer orderItemId);

    List<StatementOrderDetailDO> findByStatementOrderId(@Param("statementOrderId") Integer statementOrderId);

    List<StatementOrderDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer saveList(@Param("statementOrderDetailDOList") List<StatementOrderDetailDO> statementOrderDetailDOList);
}