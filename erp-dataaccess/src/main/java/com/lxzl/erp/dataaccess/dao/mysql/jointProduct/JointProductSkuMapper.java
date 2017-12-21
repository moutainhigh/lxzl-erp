package com.lxzl.erp.dataaccess.dao.mysql.jointProduct;

import com.lxzl.erp.common.domain.jointProduct.JointProductSku;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductSkuDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface JointProductSkuMapper extends BaseMysqlDAO<JointProductSkuDO> {

	List<JointProductSkuDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	//判断SKU_ID 是否存在
    List<JointProductSkuDO> findJointProductId(@Param("jointProductId")Integer jointProductId);

	Integer deleteByJointProductId(JointProductSkuDO jointProductSkuDO);
}