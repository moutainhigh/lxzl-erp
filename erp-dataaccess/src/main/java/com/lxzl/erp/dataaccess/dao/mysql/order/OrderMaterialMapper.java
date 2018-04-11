package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface OrderMaterialMapper extends BaseMysqlDAO<OrderMaterialDO> {

    List<OrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<OrderMaterialDO> findByOrderId(@Param("orderId") Integer orderId);

    List<Map<String, Object>> queryLastPrice(@Param("customerId") Integer customerId,
                                             @Param("materialId") Integer materialId,
                                             @Param("isNewMaterial") Integer isNewMaterial);

    List<OrderMaterialDO> findOrderMaterialByName(@Param("materialName") String materialName);

    Integer findTotalRentingMaterialCountByOrderId(@Param("orderId") Integer orderId);
}
