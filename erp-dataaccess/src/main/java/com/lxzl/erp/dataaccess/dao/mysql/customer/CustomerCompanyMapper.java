package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface CustomerCompanyMapper extends BaseMysqlDAO<CustomerCompanyDO> {

	List<CustomerCompanyDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	CustomerCompanyDO findByCustomerId(@Param("customerId") Integer customerId);

	List<CustomerCompanyDO> findCustomerCompanyByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findCustomerCompanyCountByParams(@Param("maps") Map<String, Object> paramMap);
}