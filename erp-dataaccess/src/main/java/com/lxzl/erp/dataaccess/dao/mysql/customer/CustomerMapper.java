package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMysqlDAO<CustomerDO> {

	List<CustomerDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	CustomerDO findByNo(@Param("customerNo") String customerNo);

	List<CustomerDO> findCustomerCompanyByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findCustomerCompanyCountByParams(@Param("maps") Map<String, Object> paramMap);

	List<CustomerDO> findCustomerPersonByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findCustomerPersonCountByParams(@Param("maps") Map<String, Object> paramMap);

	CustomerDO findCustomerCompanyByNo(@Param("customerNo") String customerNo);

	CustomerDO findCustomerPersonByNo(@Param("customerNo") String customerNo);
}