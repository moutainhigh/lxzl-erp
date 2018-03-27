package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.erp.dataaccess.domain.customer.CustomerRiskManagementHistoryDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustomerRiskManagementHistoryMapper extends BaseMysqlDAO<CustomerRiskManagementHistoryDO> {

	List<CustomerRiskManagementHistoryDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<CustomerRiskManagementHistoryDO> findCustomerRiskHistoryByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findCustomerRiskHistoryCountByParams(@Param("maps") Map<String, Object> paramMap);

	CustomerRiskManagementHistoryDO findByCustomerRiskHistoryId(@Param("customerRiskManagementHistoryId") Integer customerRiskManagementHistoryId);

}