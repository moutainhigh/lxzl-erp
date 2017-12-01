package com.lxzl.erp.dataaccess.dao.mysql.returnOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ReturnOrderMapper extends BaseMysqlDAO<ReturnOrderDO> {

	List<ReturnOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	ReturnOrderDO findByNo(@Param("returnOrderNo") String returnOrderNO);

	List<ReturnOrderDO> findReturnOrderByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findReturnOrderCountByParams(@Param("maps") Map<String, Object> paramMap);
}