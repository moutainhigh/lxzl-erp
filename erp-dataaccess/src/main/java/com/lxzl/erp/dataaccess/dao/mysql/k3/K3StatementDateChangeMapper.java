package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.K3StatementDateChangeDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface K3StatementDateChangeMapper extends BaseMysqlDAO<K3StatementDateChangeDO> {

	List<K3StatementDateChangeDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	K3StatementDateChangeDO findByOrderNo(@Param("orderNo")String orderNo);
}