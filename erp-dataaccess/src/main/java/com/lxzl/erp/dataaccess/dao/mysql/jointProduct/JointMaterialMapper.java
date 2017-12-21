package com.lxzl.erp.dataaccess.dao.mysql.jointProduct;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.jointProduct.JointMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface JointMaterialMapper extends BaseMysqlDAO<JointMaterialDO> {

	List<JointMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	//jointProductDOId 的数据
    List<JointMaterialDO> findJointMaterialId(@Param("jointProductDOId")Integer jointProductDOId);

	//查找 jointMaterialId 是否存在  erp_joint_material 的主键id
	Integer findJointMaterialIdCount(@Param("jointMaterialId")Integer jointMaterialId);

    Integer deleteByJointProductId(JointMaterialDO jointMaterialDO);
}