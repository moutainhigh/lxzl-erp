package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface CustomerRiskManagementMapper extends BaseMysqlDAO<CustomerRiskManagementDO> {

	List<CustomerRiskManagementDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	CustomerRiskManagementDO findByCustomerId(@Param("customerId") Integer customerId);

	CustomerRiskManagementDO findCreditAmountByCustomerId(@Param("customerId") Integer customerId);
}