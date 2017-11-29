package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface OrderProductEquipmentMapper extends BaseMysqlDAO<OrderProductEquipmentDO> {

	List<OrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer saveList(@Param("orderProductEquipmentDOList") List<OrderProductEquipmentDO> orderProductEquipmentDOList);

	/**
	 * 根据客户和设备查询在租设备的订单商品设备
	 * @param customerId
	 * @param equipmentId
	 * @return
	 */
	OrderProductEquipmentDO findRentByCustomerIdAndEquipmentId(@Param("customerId") Integer customerId , @Param("equipmentId") Integer equipmentId);
}