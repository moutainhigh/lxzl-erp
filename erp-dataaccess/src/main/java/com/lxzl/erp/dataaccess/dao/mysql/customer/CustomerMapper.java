package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMysqlDAO<CustomerDO> {

	List<CustomerDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	CustomerDO findByNo(@Param("customerNo") String customerNo);
}