package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ChangeOrderMaterialMapper extends BaseMysqlDAO<ChangeOrderMaterialDO> {

	List<ChangeOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    void batchSave(@Param("changeOrderId") Integer changeOrderId, @Param("changeOrderNo")String changeOrderNo, @Param("changeOrderMaterialDOList")List<ChangeOrderMaterialDO> changeOrderMaterialDOList);

    void batchUpdate(@Param("list")List<ChangeOrderMaterialDO> changeOrderMaterialDOList);

	List<ChangeOrderMaterialDO> findByChangeOrderId(@Param("changeOrderId") Integer changeOrderId);

}