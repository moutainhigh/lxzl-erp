package com.lxzl.erp.dataaccess.dao.mysql.orderOperationLog;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.orderOperationLog.OrderOperationLogDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface OrderOperationLogMapper extends BaseMysqlDAO<OrderOperationLogDO> {

	List<OrderOperationLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}