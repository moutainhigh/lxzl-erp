package com.lxzl.erp.dataaccess.dao.mysql.workflow;

import com.lxzl.erp.dataaccess.domain.workflow.WorkflowTemplateDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface WorkflowTemplateMapper extends BaseMysqlDAO<WorkflowTemplateDO> {

	List<WorkflowTemplateDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	WorkflowTemplateDO findByWorkflowType(Integer workflowType);
}