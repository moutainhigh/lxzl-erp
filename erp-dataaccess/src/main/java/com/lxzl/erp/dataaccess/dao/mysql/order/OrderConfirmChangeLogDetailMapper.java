package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderConfirmChangeLogDetailDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface OrderConfirmChangeLogDetailMapper extends BaseMysqlDAO<OrderConfirmChangeLogDetailDO> {

	List<OrderConfirmChangeLogDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}