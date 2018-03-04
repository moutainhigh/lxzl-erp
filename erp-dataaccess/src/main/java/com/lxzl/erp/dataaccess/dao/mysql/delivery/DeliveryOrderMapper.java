package com.lxzl.erp.dataaccess.dao.mysql.delivery;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface DeliveryOrderMapper extends BaseMysqlDAO<DeliveryOrderDO> {

	List<DeliveryOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}