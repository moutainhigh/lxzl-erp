package com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PeerDeploymentOrderMaterialMapper extends BaseMysqlDAO<PeerDeploymentOrderMaterialDO> {

	List<PeerDeploymentOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	//todo 123
	Integer saveList(@Param("peerDeploymentOrderMaterialDOList") List<PeerDeploymentOrderMaterialDO> peerDeploymentOrderMaterialDOList);
	//todo 123
	List<PeerDeploymentOrderMaterialDO> findByPeerDeploymentOrderNo(@Param("peerDeploymentOrderNo") String peerDeploymentOrderNo);

}