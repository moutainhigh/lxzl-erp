package com.lxzl.erp.dataaccess.dao.mysql.img;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.img.ImgDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ImgMapper extends BaseMysqlDAO<ImgDO> {

	List<ImgDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}