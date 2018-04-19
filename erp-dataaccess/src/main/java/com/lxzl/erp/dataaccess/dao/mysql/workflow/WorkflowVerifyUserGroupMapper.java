package com.lxzl.erp.dataaccess.dao.mysql.workflow;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowVerifyUserGroupDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface WorkflowVerifyUserGroupMapper extends BaseMysqlDAO<WorkflowVerifyUserGroupDO> {

	List<WorkflowVerifyUserGroupDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer listAllCount();

	List<WorkflowVerifyUserGroupDO> findByVerifyUserGroupId(@Param("verifyUserGroupId") String verifyUserGroupId);

	List<String> findGroupUUIDByUserId(@Param("userId") Integer userId);
}