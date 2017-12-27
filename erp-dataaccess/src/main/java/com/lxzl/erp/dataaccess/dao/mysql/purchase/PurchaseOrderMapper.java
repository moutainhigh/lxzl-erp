package com.lxzl.erp.dataaccess.dao.mysql.purchase;

import com.lxzl.erp.dataaccess.domain.purchase.PurchaseOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PurchaseOrderMapper extends BaseMysqlDAO<PurchaseOrderDO> {

	PurchaseOrderDO findByPurchaseNo(@Param("purchaseNo") String purchaseNo);

	PurchaseOrderDO findDetailByPurchaseNo(@Param("purchaseNo") String purchaseNo);

	PurchaseOrderDO findDetailById(@Param("id") Integer id);

	List<PurchaseOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<PurchaseOrderDO> findPurchaseOrderByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findPurchaseOrderCountByParams(@Param("maps") Map<String, Object> paramMap);

}