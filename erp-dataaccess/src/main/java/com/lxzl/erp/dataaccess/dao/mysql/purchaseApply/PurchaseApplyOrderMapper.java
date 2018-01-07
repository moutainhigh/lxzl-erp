package com.lxzl.erp.dataaccess.dao.mysql.purchaseApply;

import com.lxzl.erp.dataaccess.domain.purchaseApply.PurchaseApplyOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PurchaseApplyOrderMapper extends BaseMysqlDAO<PurchaseApplyOrderDO> {

    List<PurchaseApplyOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    PurchaseApplyOrderDO findByNo(@Param("purchaseApplyOrderNo") String purchaseApplyOrderNo);
}