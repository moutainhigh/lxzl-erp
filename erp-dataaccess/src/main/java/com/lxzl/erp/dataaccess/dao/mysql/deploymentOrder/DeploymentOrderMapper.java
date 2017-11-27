package com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface DeploymentOrderMapper extends BaseMysqlDAO<DeploymentOrderDO> {

	List<DeploymentOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}