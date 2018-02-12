package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface K3SendRecordMapper extends BaseMysqlDAO<K3SendRecordDO> {

	List<K3SendRecordDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	K3SendRecordDO findByReferIdAndType(@Param("referId") Integer referId , @Param("type") Integer type);
}