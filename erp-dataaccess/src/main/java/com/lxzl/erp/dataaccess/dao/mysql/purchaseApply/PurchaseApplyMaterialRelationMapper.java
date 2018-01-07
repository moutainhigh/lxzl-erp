package com.lxzl.erp.dataaccess.dao.mysql.purchaseApply;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.purchaseApply.PurchaseApplyMaterialRelationDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PurchaseApplyMaterialRelationMapper extends BaseMysqlDAO<PurchaseApplyMaterialRelationDO> {

	List<PurchaseApplyMaterialRelationDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}