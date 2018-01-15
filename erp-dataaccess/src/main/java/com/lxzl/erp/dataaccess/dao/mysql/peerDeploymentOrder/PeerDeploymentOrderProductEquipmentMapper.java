package com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PeerDeploymentOrderProductEquipmentMapper extends BaseMysqlDAO<PeerDeploymentOrderProductEquipmentDO> {

	List<PeerDeploymentOrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}