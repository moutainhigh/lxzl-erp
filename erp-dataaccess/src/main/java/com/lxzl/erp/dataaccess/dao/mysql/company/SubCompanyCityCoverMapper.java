package com.lxzl.erp.dataaccess.dao.mysql.company;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyCityCoverDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface SubCompanyCityCoverMapper extends BaseMysqlDAO<SubCompanyCityCoverDO> {

	List<SubCompanyCityCoverDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	SubCompanyCityCoverDO findByCityId(@Param("cityId") Integer cityId);

	SubCompanyCityCoverDO findByProvinceId(@Param("provinceId") Integer provinceId);
}