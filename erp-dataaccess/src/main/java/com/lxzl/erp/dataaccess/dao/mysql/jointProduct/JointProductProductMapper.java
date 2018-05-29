package com.lxzl.erp.dataaccess.dao.mysql.jointProduct;

import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface JointProductProductMapper extends BaseMysqlDAO<JointProductProductDO> {

    List<JointProductProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer deleteByJointProductId(JointProductProductDO jointProductProductDO);

    List<JointProductProductDO> findByIds(@Param("ids") Set<Integer> ids);
}