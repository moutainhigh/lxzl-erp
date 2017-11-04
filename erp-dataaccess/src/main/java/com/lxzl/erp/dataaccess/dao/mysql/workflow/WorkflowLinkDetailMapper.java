package com.lxzl.erp.dataaccess.dao.mysql.workflow;

import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDetailDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface WorkflowLinkDetailMapper extends BaseMysqlDAO<WorkflowLinkDetailDO> {

	List<WorkflowLinkDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}