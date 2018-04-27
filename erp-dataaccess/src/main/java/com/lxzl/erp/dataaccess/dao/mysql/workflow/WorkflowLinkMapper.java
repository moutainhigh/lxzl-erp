package com.lxzl.erp.dataaccess.dao.mysql.workflow;

import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
public interface WorkflowLinkMapper extends BaseMysqlDAO<WorkflowLinkDO> {

	List<WorkflowLinkDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	WorkflowLinkDO findByWorkflowTypeAndReferNo(@Param("workflowType") Integer workflowType,
												@Param("workflowReferNo") String workflowReferNo);

	List<WorkflowLinkDO> findByWorkflowTypeAndReferNoList(@Param("workflowType") Integer workflowType,@Param("workflowReferNoList") List<String> workflowReferNoList);

	WorkflowLinkDO findByNo(@Param("workflowLinkNo") String workflowLinkNo);

    List<String> findWorkflowReferNoList(@Param("currentVerifyUser") Integer currentVerifyUser);
}