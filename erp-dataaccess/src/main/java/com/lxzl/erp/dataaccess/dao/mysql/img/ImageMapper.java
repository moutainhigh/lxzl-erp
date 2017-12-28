package com.lxzl.erp.dataaccess.dao.mysql.img;

import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ImageMapper extends BaseMysqlDAO<ImageDO> {

	List<ImageDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}