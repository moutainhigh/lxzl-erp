package com.lxzl.erp.core.service.workflow;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-04 16:05
 */
public interface WorkflowService extends BaseService {
    ServiceResult<String, Integer> commitWorkFlow(Integer workflowType, Integer workflowReferId);
    ServiceResult<String, Integer> verifyWorkFlow(Integer workflowLinkId);
}
