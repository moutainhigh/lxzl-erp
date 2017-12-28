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
	//查找district_name
	List<String> selectAreaDistrictDOs();
	//添加邮政编号
	Integer savePostCode(@Param("postCode")String postCode,@Param("districtName")String districtName);
	//中英文简写
	void updateAbbCnAndAbbEn(@Param("abbCn")String abbCn, @Param("abbEn")String abbEn,@Param("districtName")String districtName);
	//获取城市id
	List<Integer> findCityIdByDistrictName(@Param("districtName")String districtName);

	List<String> selectPostCodeIsNull();
}