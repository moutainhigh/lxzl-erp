package com.lxzl.erp.dataaccess.dao.mysql.jointProduct;

import com.lxzl.erp.dataaccess.domain.jointProduct.JointProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface JointProductMapper extends BaseMysqlDAO<JointProductDO> {

    List<JointProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    JointProductDO findDetailByJointProductId(@Param("jointProductId") Integer jointProductId);

    //所有的要查询的记录数
    Integer findJointProductCountByParam(@Param("maps") HashMap<String, Object> maps);

    //分页一页的数据
    List<JointProductDO> findJointProductByParams(@Param("maps") HashMap<String, Object> maps);
}