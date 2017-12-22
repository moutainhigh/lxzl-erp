package com.lxzl.erp.dataaccess.dao.mysql.jointProduct;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointMaterialDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface JointMaterialMapper extends BaseMysqlDAO<JointMaterialDO> {

    List<JointMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer deleteByJointProductId(JointMaterialDO jointMaterialDO);

}