package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
public interface CustomerMapper extends BaseMysqlDAO<CustomerDO> {

	List<CustomerDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	CustomerDO findByNo(@Param("customerNo") String customerNo);

	List<CustomerDO> findByCustomerParam(@Param("startTime")Date startTime,@Param("endTime")Date endTime);

	CustomerDO findByName(@Param("customerName") String customerName);

	List<CustomerDO> findCustomerCompanyByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findCustomerCompanyCountByParams(@Param("maps") Map<String, Object> paramMap);

	List<CustomerDO> findCustomerPersonByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findCustomerPersonCountByParams(@Param("maps") Map<String, Object> paramMap);

	CustomerDO findCustomerCompanyByNo(@Param("customerNo") String customerNo);

	CustomerDO findCustomerPersonByNo(@Param("customerNo") String customerNo);

    void setIsRisk();

	List<CustomerDO>  findCustomer();

}