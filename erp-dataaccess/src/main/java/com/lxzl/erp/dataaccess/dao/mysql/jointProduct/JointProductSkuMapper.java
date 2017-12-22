package com.lxzl.erp.dataaccess.dao.mysql.jointProduct;

import com.lxzl.erp.common.domain.jointProduct.JointProductSku;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductSkuDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface JointProductSkuMapper extends BaseMysqlDAO<JointProductSkuDO> {

	List<JointProductSkuDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer deleteByJointProductId(JointProductSkuDO jointProductSkuDO);
}