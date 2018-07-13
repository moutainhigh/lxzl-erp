package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.erp.common.domain.k3.pojo.K3OrderStatementConfig;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.K3OrderStatementConfigDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface K3OrderStatementConfigMapper extends BaseMysqlDAO<K3OrderStatementConfigDO> {

	List<K3OrderStatementConfigDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	K3OrderStatementConfigDO findByOrderId(@Param("orderId") Integer orderId);

    List<K3OrderStatementConfigDO> findByCustomerId(@Param("customerId") Integer customerId);
}