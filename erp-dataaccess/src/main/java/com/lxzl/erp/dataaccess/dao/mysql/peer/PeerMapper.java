package com.lxzl.erp.dataaccess.dao.mysql.peer;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.peer.PeerDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface PeerMapper extends BaseMysqlDAO<PeerDO> {

	List<PeerDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	PeerDO findDetailByPeerId(@Param("peerId") Integer peerId);

	PeerDO finByNo(@Param("peerNo") String peerNo);
}