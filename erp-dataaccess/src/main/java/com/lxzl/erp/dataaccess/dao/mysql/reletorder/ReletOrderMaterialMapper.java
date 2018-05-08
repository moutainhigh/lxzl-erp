package com.lxzl.erp.dataaccess.dao.mysql.reletorder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ReletOrderMaterialMapper extends BaseMysqlDAO<ReletOrderMaterialDO> {

	List<ReletOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<ReletOrderMaterialDO> findByReletOrderId(@Param("reletOrderId") Integer reletOrderId);
}