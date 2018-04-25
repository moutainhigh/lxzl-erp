package com.lxzl.erp.dataaccess.dao.mysql.workflow;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowTemplateDingdingDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface WorkflowTemplateDingdingMapper extends BaseMysqlDAO<WorkflowTemplateDingdingDO> {

	List<WorkflowTemplateDingdingDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	/** 根据钉钉工作流模板代码获取列表信息 */
	List<WorkflowTemplateDingdingDO> listByDingdingProcessCode(@Param("dingdingProcessCode") String dingdingProcessCode);
}