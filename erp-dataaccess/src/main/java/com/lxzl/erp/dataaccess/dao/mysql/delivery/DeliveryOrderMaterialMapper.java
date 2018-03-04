package com.lxzl.erp.dataaccess.dao.mysql.delivery;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.delivery.DeliveryOrderMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface DeliveryOrderMaterialMapper extends BaseMysqlDAO<DeliveryOrderMaterialDO> {

	List<DeliveryOrderMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}