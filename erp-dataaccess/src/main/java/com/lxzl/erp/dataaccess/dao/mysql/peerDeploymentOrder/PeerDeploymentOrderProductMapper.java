package com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderProductDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PeerDeploymentOrderProductMapper extends BaseMysqlDAO<PeerDeploymentOrderProductDO> {

	List<PeerDeploymentOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	//todo
	List<PeerDeploymentOrderProductDO> findByPeerDeploymentOrderNo(@Param("peerDeploymentOrderNo") String peerDeploymentOrderNo);
	//todo
	Integer saveList(@Param("peerDeploymentOrderProductDOList") List<PeerDeploymentOrderProductDO> peerDeploymentOrderProductDOList);
	//todo
	PeerDeploymentOrderProductDO findByPeerDeploymentOrderNoAndSkuIdAndIsNew(@Param("peerDeploymentOrderNo") String deploymentOrderNo, @Param("productSkuId") Integer productSkuId ,@Param("isNew") Integer isNew);
}