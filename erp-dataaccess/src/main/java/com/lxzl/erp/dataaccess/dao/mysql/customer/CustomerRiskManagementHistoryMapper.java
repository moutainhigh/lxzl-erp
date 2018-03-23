package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementHistoryDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface CustomerRiskManagementHistoryMapper extends BaseMysqlDAO<CustomerRiskManagementHistoryDO> {

	List<CustomerRiskManagementHistoryDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}