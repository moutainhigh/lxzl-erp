package com.lxzl.erp.dataaccess.dao.mysql.changeOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.changeOrder.ChangeOrderProductDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ChangeOrderProductMapper extends BaseMysqlDAO<ChangeOrderProductDO> {

	List<ChangeOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer batchSave(@Param("changeOrderId") Integer changeOrderId,@Param("changeOrderNo") String changeOrderNo, @Param("changeOrderProductDOList") List<ChangeOrderProductDO> changeOrderProductDOList);
}