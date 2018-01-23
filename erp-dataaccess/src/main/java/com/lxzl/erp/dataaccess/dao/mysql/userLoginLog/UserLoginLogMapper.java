package com.lxzl.erp.dataaccess.dao.mysql.userLoginLog;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.userLoginLog.UserLoginLogDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface UserLoginLogMapper extends BaseMysqlDAO<UserLoginLogDO> {

	List<UserLoginLogDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}