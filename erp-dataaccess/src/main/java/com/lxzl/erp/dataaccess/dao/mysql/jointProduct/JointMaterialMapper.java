package com.lxzl.erp.dataaccess.dao.mysql.jointProduct;

import com.lxzl.erp.dataaccess.domain.jointProduct.JointMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface JointMaterialMapper extends BaseMysqlDAO<JointMaterialDO> {

    List<JointMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer deleteByJointProductId(JointMaterialDO jointMaterialDO);

    List<JointMaterialDO> findByIds(@Param("ids") Set<Integer> ids);
}