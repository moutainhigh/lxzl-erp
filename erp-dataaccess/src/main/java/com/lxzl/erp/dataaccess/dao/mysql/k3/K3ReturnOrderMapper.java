package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface K3ReturnOrderMapper extends BaseMysqlDAO<K3ReturnOrderDO> {

    List<K3ReturnOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    K3ReturnOrderDO findByNo(@Param("returnOrderNo") String returnOrderNo);

    K3ReturnOrderDO findByIdAndTime(@Param("id") Integer id, @Param("typeStartTime")Date typeStartTime, @Param("typeEndTime")Date typeEndTime);
}