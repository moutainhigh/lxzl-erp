package com.lxzl.erp.dataaccess.dao.mysql.dingdingGroupMessageConfig;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.dingdingGroupMessageConfig.DingdingGroupMessageConfigDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface DingdingGroupMessageConfigMapper extends BaseMysqlDAO<DingdingGroupMessageConfigDO> {

	List<DingdingGroupMessageConfigDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<DingdingGroupMessageConfigDO> findBySendTypeAndSubCompanyId(@Param("sendType") Integer sendType, @Param("subCompanyId") Integer subCompanyId);
}