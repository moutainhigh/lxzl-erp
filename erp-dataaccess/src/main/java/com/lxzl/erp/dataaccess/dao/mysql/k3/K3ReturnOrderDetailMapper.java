package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDetailDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Set;

@Repository
public interface K3ReturnOrderDetailMapper extends BaseMysqlDAO<K3ReturnOrderDetailDO> {

	List<K3ReturnOrderDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<K3ReturnOrderDetailDO> findListByReturnOrderId(@Param("returnOrderId")Integer returnOrderId);

    Integer findRealReturnCountByOrderEntry(@Param("orderEntry")String orderEntry,@Param("orderNo")String orderNo);

    List<K3ReturnOrderDetailDO> findListByOrderNo(@Param("orderNo")String orderNo);

    void saveList(List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList);

    void batchUpdateRealProductCount(@Param("list") List<K3ReturnOrderDetailDO> k3ReturnOrderDetailDOList);

    /**
     * 根据退货单id列表获取退货单详情列表信息
     */
    List<K3ReturnOrderDetailDO> listByReturnOrderIds(@Param(value = "returnOrderIds") Set<Integer> returnOrderIds);
}