package com.lxzl.erp.dataaccess.dao.mysql.reletorder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderProductDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ReletOrderProductMapper extends BaseMysqlDAO<ReletOrderProductDO> {

	List<ReletOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<ReletOrderProductDO> findByReletOrderId(@Param("reletOrderId") Integer reletOrderId);
}