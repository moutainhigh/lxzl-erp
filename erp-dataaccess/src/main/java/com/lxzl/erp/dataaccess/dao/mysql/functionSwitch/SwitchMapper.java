package com.lxzl.erp.dataaccess.dao.mysql.functionSwitch;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.interfaceSwitch.SwitchDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface SwitchMapper extends BaseMysqlDAO<SwitchDO> {

	List<SwitchDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer findSwitchCountByParam(@Param("maps") Map<String, Object> map);

	List<SwitchDO> findSwitchByParam(@Param("maps") Map<String, Object> map);

	SwitchDO findByInterfaceUrl(@Param("interfaceUrl") String interfaceUrl);

	List<SwitchDO> findByIsOpen(@Param("isOpen") Integer isOpen);
}