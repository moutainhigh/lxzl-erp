package com.lxzl.erp.dataaccess.dao.mysql.purchase;

import com.lxzl.erp.dataaccess.domain.purchase.PurchaseDeliveryOrderMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface PurchaseDeliveryOrderMaterialMapper extends BaseMysqlDAO<PurchaseDeliveryOrderMaterialDO> {

	List<PurchaseDeliveryOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}