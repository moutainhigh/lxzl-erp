package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderMaterialBulkDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ChangeOrderMaterialBulkMapper extends BaseMysqlDAO<ChangeOrderMaterialBulkDO> {

	List<ChangeOrderMaterialBulkDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	/**
	 * 查询某收货单所有未录入src的散料
	 * @param changeOrderNo
	 * @return
	 */
	List<ChangeOrderMaterialBulkDO> listByChangeOrderPickUp(@Param("changeOrderNo") String changeOrderNo);
}