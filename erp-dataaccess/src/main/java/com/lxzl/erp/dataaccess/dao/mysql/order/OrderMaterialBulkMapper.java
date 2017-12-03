package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialBulkDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface OrderMaterialBulkMapper extends BaseMysqlDAO<OrderMaterialBulkDO> {

	List<OrderMaterialBulkDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer saveList(@Param("orderMaterialBulkDOList") List<OrderMaterialBulkDO> orderMaterialBulkDOList);

	Integer updateList(@Param("orderMaterialBulkDOList") List<OrderMaterialBulkDO> orderMaterialBulkDOList);

	OrderMaterialBulkDO findByOrderIdAndBulkMaterialNo(@Param("orderId") Integer orderId , @Param("bulkMaterialNo") String bulkMaterialNo);

	List<OrderMaterialBulkDO> findByOrderMaterialId(@Param("orderMaterialId") Integer orderMaterialId);

	OrderMaterialBulkDO findRentByCustomerIdAndBulkMaterialId(@Param("customerId") Integer customerId , @Param("bulkMaterialId") Integer bulkMaterialId);
}