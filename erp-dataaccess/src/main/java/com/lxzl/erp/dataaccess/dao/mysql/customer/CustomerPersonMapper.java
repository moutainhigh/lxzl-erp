package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerPersonDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface CustomerPersonMapper extends BaseMysqlDAO<CustomerPersonDO> {

	List<CustomerPersonDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	CustomerPersonDO findByCustomerId(@Param("customerId") Integer customerId);

	List<CustomerPersonDO> findCustomerPersonByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findCustomerPersonCountByParams(@Param("maps") Map<String, Object> paramMap);
}