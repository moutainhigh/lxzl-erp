package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskLogDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface CustomerRiskLogMapper extends BaseMysqlDAO<CustomerRiskLogDO> {

	List<CustomerRiskLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}