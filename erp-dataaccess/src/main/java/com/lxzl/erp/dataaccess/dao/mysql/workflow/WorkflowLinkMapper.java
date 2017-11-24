package com.lxzl.erp.dataaccess.dao.mysql.workflow;

import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface WorkflowLinkMapper extends BaseMysqlDAO<WorkflowLinkDO> {

	List<WorkflowLinkDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	WorkflowLinkDO findByWorkflowTypeAndReferNo(@Param("workflowType") Integer workflowType,
												@Param("workflowReferNo") String workflowReferNo);

	WorkflowLinkDO findByNo(@Param("workflowLinkNo") String workflowLinkNo);
}