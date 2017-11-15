package com.lxzl.erp.dataaccess.dao.mysql.area;

import com.lxzl.erp.dataaccess.domain.area.AreaProvinceDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface AreaProvinceMapper extends BaseMysqlDAO<AreaProvinceDO> {

	List<AreaProvinceDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}