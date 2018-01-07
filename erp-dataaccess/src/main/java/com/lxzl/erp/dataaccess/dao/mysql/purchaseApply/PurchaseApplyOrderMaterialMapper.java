package com.lxzl.erp.dataaccess.dao.mysql.purchaseApply;

import com.lxzl.erp.dataaccess.domain.purchaseApply.PurchaseApplyOrderMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PurchaseApplyOrderMaterialMapper extends BaseMysqlDAO<PurchaseApplyOrderMaterialDO> {

    List<PurchaseApplyOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer saveList(List<PurchaseApplyOrderMaterialDO> purchaseApplyOrderMaterialDOList);
}