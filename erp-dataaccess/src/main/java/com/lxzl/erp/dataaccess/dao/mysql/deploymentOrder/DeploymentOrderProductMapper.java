package com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderProductDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeploymentOrderProductMapper extends BaseMysqlDAO<DeploymentOrderProductDO> {

    List<DeploymentOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer saveList(@Param("deploymentOrderProductDOList") List<DeploymentOrderProductDO> deploymentOrderProductDOList);

    List<DeploymentOrderProductDO> findByDeploymentOrderNo(@Param("findByDeploymentOrderNo") String findByDeploymentOrderNo);
}