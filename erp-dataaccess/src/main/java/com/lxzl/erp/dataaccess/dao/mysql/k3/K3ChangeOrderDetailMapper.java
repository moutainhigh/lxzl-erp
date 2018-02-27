package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.K3ChangeOrderDetailDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface K3ChangeOrderDetailMapper extends BaseMysqlDAO<K3ChangeOrderDetailDO> {

	List<K3ChangeOrderDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}