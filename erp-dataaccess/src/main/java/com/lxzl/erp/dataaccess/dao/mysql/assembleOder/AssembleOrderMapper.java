package com.lxzl.erp.dataaccess.dao.mysql.assembleOder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.assembleOder.AssembleOrderDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface AssembleOrderMapper extends BaseMysqlDAO<AssembleOrderDO> {

	List<AssembleOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}