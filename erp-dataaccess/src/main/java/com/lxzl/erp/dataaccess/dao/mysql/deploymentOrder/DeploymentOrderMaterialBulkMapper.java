package com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderMaterialBulkDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeploymentOrderMaterialBulkMapper extends BaseMysqlDAO<DeploymentOrderMaterialBulkDO> {

    List<DeploymentOrderMaterialBulkDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    DeploymentOrderMaterialBulkDO findDeploymentOrderByBulkMaterialNo(@Param("bulkMaterialNo") String byBulkMaterialNo);

    List<DeploymentOrderMaterialBulkDO> findByDeploymentOrderMaterialId(@Param("deploymentOrderMaterialId") Integer deploymentOrderMaterialId);
}