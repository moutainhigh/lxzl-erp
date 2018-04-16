package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StatementOrderDetailMapper extends BaseMysqlDAO<StatementOrderDetailDO> {

    List<StatementOrderDetailDO> findByOrderId(@Param("orderId") Integer orderId);

    List<StatementOrderDetailDO> findByOrderIdAndOrderItemType(@Param("orderId") Integer orderId, @Param("orderItemType") Integer orderItemType, @Param("orderItemReferId") Integer orderItemReferId);

    List<StatementOrderDetailDO> findByReturnOrderId(@Param("orderId") Integer orderId);

    List<StatementOrderDetailDO> findByOrderItemTypeAndId(@Param("orderItemType") Integer orderItemType, @Param("orderItemId") Integer orderItemId);

    List<StatementOrderDetailDO> findByOrderTypeAndId(@Param("orderType") Integer orderType, @Param("orderId") Integer orderId);

    List<StatementOrderDetailDO> findByStatementOrderId(@Param("statementOrderId") Integer statementOrderId);

    List<StatementOrderDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer saveList(@Param("statementOrderDetailDOList") List<StatementOrderDetailDO> statementOrderDetailDOList);

    List<StatementOrderDetailDO> listAllForStatistics(@Param("maps") Map<String, Object> paramMap);

    List<StatementOrderDetailDO> listAllForHome(@Param("maps") Map<String, Object> paramMap);

    List<StatementOrderDetailDO> findByStatementOrderIdAndItemReferId(@Param("itemReferId") Integer itemReferId, @Param("statementOrderId") Integer statementOrderId);

    Integer batchUpdate(@Param("list") List<StatementOrderDetailDO> list);

    void deleteByOrderId(@Param("orderId")Integer orderId,@Param("updateUser")String updateUser);
}