package com.lxzl.erp.dataaccess.dao.mysql.material;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.material.MaterialImgDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MaterialImgMapper extends BaseMysqlDAO<MaterialImgDO> {

    List<MaterialImgDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<MaterialImgDO> findByMaterialIdAndType(@Param("materialId") Integer materialId,
                                                @Param("imgType") Integer imgType);
}