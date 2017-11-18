package com.lxzl.erp.dataaccess.dao.mysql.purchase;

import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface PurchaseOrderProductMapper extends BaseMysqlDAO<PurchaseOrderProductDO> {

	List<PurchaseOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer deleteByPurchaseOrderId(PurchaseOrderProductDO purchaseOrderProductDO);

}