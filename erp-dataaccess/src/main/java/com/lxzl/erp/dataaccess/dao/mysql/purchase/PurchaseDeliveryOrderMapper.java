package com.lxzl.erp.dataaccess.dao.mysql.purchase;

import com.lxzl.erp.dataaccess.domain.purchase.PurchaseDeliveryOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface PurchaseDeliveryOrderMapper extends BaseMysqlDAO<PurchaseDeliveryOrderDO> {

	List<PurchaseDeliveryOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<PurchaseDeliveryOrderDO> findPurchaseDeliveryOrderByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findPurchaseDeliveryOrderCountByParams(@Param("maps") Map<String, Object> paramMap);

	PurchaseDeliveryOrderDO findByNo(@Param("purchaseDeliveryNo") String purchaseDeliveryNo);
}