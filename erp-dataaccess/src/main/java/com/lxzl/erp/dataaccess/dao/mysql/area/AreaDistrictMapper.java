package com.lxzl.erp.dataaccess.dao.mysql.area;

import com.lxzl.erp.dataaccess.domain.area.AreaDistrictDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface AreaDistrictMapper extends BaseMysqlDAO<AreaDistrictDO> {

	List<AreaDistrictDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}