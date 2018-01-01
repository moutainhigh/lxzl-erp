package com.lxzl.erp.dataaccess.dao.mysql.material;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.material.MaterialImgDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MaterialImgMapper extends BaseMysqlDAO<MaterialImgDO> {

    List<MaterialImgDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    MaterialImgDO findByImgId(@Param("imgId") Integer imgId);

    List<MaterialImgDO> findByMaterialIdAndType(@Param("materialId") Integer materialId,
                                                @Param("imgType") Integer imgType);
}