package com.lxzl.erp.dataaccess.dao.mysql.businessSystemConfig;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.businessSystemConfig.BusinessSystemConfigDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface BusinessSystemConfigMapper extends BaseMysqlDAO<BusinessSystemConfigDO> {

	List<BusinessSystemConfigDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer findCountByErpAppIdAndErpAppSecret(@Param("erpAppId") String erpAppId,@Param("erpAppSecret") String erpAppSecret);
}