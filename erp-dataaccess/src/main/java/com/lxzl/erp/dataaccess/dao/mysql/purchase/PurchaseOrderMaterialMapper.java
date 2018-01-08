package com.lxzl.erp.dataaccess.dao.mysql.purchase;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface PurchaseOrderMaterialMapper extends BaseMysqlDAO<PurchaseOrderMaterialDO> {

	List<PurchaseOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer deleteByPurchaseOrderId(PurchaseOrderMaterialDO purchaseOrderMaterialDO);

}