package com.lxzl.erp.dataaccess.dao.mysql.delivery;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderProductDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface DeliveryOrderProductMapper extends BaseMysqlDAO<DeliveryOrderProductDO> {

	List<DeliveryOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}