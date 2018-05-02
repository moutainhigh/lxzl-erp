package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface K3ReturnOrderMapper extends BaseMysqlDAO<K3ReturnOrderDO> {

    List<K3ReturnOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    K3ReturnOrderDO findByNo(@Param("returnOrderNo") String returnOrderNo);

    List<K3ReturnOrderDO> findByCustomerNo(@Param("k3CustomerNo")String k3CustomerNo);

    /** 根据退货单列表获取退货单列表信息 */
    List<K3ReturnOrderDO> listByReturnOrderNos(@Param("returnOrderNos") List<String> returnOrderNos);

    void saveList(List<K3ReturnOrderDO> k3ReturnOrderDOList);
}