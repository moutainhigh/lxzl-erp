package com.lxzl.erp.dataaccess.dao.mysql.replace;

import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ReplaceOrderMapper extends BaseMysqlDAO<ReplaceOrderDO> {

	List<ReplaceOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<ReplaceOrderDO> findByOrderNoForCheck(@Param("orderNo") String orderNo);

    ReplaceOrderDO findByReplaceOrderNo(@Param("replaceOrderNo") String replaceOrderNo);

    Integer findReplaceOrderCountByParams(@Param("maps") Map<String, Object> maps);

    List<ReplaceOrderDO> findReplaceOrderByParams(@Param("maps") Map<String, Object> maps);

    List<ReplaceOrderDO> findByOrderNoForOrderDetail(@Param("orderNo") String orderNo);

    List<ReplaceOrderDO> listByMonthQuery(@Param(value = "queryParam") StatementOrderMonthQueryParam queryParam);
}