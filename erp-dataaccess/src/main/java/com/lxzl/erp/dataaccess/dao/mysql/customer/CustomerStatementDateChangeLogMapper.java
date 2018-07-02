package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerStatementDateChangeLogDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface CustomerStatementDateChangeLogMapper extends BaseMysqlDAO<CustomerStatementDateChangeLogDO> {

	List<CustomerStatementDateChangeLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}