package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ChangeOrderMapper extends BaseMysqlDAO<ChangeOrderDO> {

    List<ChangeOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    ChangeOrderDO findByNo(@Param("changeOrderNo") String changeOrderNO);

    List<ChangeOrderDO> findChangeOrderByParams(@Param("maps") Map<String, Object> paramMap);

    Integer findChangeOrderCountByParams(@Param("maps") Map<String, Object> paramMap);

}