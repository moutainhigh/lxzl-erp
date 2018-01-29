package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface CustomerConsignInfoMapper extends BaseMysqlDAO<CustomerConsignInfoDO> {

	List<CustomerConsignInfoDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer findCustomerConsignInfoCountByParams(@Param("maps")Map<String, Object> maps);

	List<CustomerConsignInfoDO> findCustomerConsignInfoByParams(@Param("maps")Map<String, Object> maps);

	Integer countByCustomerId(@Param("customerId")Integer customerId);

	void clearIsMainByCustomerId(@Param("customerId")Integer customerId);

    List<CustomerConsignInfoDO> findByCustomerId(@Param("customerId")Integer customerId);
}