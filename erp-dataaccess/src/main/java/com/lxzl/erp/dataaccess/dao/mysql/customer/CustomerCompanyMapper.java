package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.erp.common.domain.customer.pojo.dto.CustomerCompanyDTO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface CustomerCompanyMapper extends BaseMysqlDAO<CustomerCompanyDO> {

	List<CustomerCompanyDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	CustomerCompanyDO findByCustomerId(@Param("customerId") Integer customerId);

	List<CustomerCompanyDO> findCustomerCompanyByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findCustomerCompanyCountByParams(@Param("maps") Map<String, Object> paramMap);

    CustomerCompanyDO findByDefaultAddressReferId(@Param("defaultAddressReferId")Integer defaultAddressReferId);

	CustomerCompanyDO findBySimpleCompanyName(@Param("simpleCompanyName")String simpleCompanyName);

	List<CustomerCompanyDO> findBySimpleCompanyNameIsNull();

	void  batchAddSimpleCompanyName(@Param("customerCompanyList") List<CustomerCompanyDO> newCustomerCompanyList);

	List<CustomerCompanyDO> findCustomerCompanyByName(@Param("list") List<CustomerCompanyDO> customerCompanyDOList);

    List<CustomerCompanyDO> findByCustomerIdList(@Param("list") List<Integer> customerIdList);

	void updateList(@Param("customerCompanyDOList") List<CustomerCompanyDO> customerCompanyDOList);

	/**
	 * 根据customerId获取母公司信息
	 *
	 * @param customerId
	 * @return
	 */
	CustomerCompanyDTO findParentCustomerByCustomerId(@Param("customerId") Integer customerId);
}