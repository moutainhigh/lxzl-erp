package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderFromTestMachineDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface OrderFromTestMachineMapper extends BaseMysqlDAO<OrderFromTestMachineDO> {

	List<OrderFromTestMachineDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	OrderFromTestMachineDO findByTestOrderNo(@Param("testMachineOrderNo") String testMachineOrderNo);

	OrderFromTestMachineDO findByOrderNo(@Param("orderNo") String orderNo);
}