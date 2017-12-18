package com.lxzl.erp.dataaccess.dao.mysql.returnOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderConsignInfoDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ReturnOrderConsignInfoMapper extends BaseMysqlDAO<ReturnOrderConsignInfoDO> {

	List<ReturnOrderConsignInfoDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	ReturnOrderConsignInfoDO findByReturnOrderId(@Param("returnOrderId") Integer returnOrderId);
}