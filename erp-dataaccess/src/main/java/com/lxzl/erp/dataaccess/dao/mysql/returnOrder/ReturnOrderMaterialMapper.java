package com.lxzl.erp.dataaccess.dao.mysql.returnOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderMaterialDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ReturnOrderMaterialMapper extends BaseMysqlDAO<ReturnOrderMaterialDO> {

	List<ReturnOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	void batchSave(@Param("returnOrderId") Integer returnOrderId, @Param("returnOrderNo") String returnOrderNo, @Param("returnOrderMaterialDOList")List<ReturnOrderMaterialDO> returnOrderMaterialDOList);

	void saveList(@Param("returnOrderMaterialDOList")List<ReturnOrderMaterialDO> returnOrderMaterialDOList);

	void updateListForReturn(@Param("returnOrderMaterialDOList")List<ReturnOrderMaterialDO> returnOrderMaterialDOList);

	ReturnOrderMaterialDO findByMaterialIdAndReturnOrderId(@Param("materialId") Integer materialId,@Param("returnOrderId") Integer returnOrderId);

	List<ReturnOrderMaterialDO> findByReturnOrderId(@Param("returnOrderId") Integer returnOrderId);
}