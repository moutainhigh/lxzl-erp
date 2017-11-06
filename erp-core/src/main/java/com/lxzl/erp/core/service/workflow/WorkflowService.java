package com.lxzl.erp.core.service.workflow;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-04 16:05
 */
public interface WorkflowService extends BaseService {
    /**
     * @param workflowType      工作流类型，详见WorkflowType
     * @param workflowReferId   工作流关联ID，业务ID
     * @param verifyUser        目标审核人
     * @return  错误代码，生成的工作流ID
     */
    ServiceResult<String, Integer> commitWorkFlow(Integer workflowType, Integer workflowReferId, Integer verifyUser);


    /**
     * @param workflowType      工作流类型，详见WorkflowType
     * @param workflowReferId   工作流关联ID，业务ID
     * @return 错误代码，可审核人
     */
    ServiceResult<String, List<User>> getNextVerifyUsers(Integer workflowType, Integer workflowReferId);

    /**
     * @param workflowLinkId    工作流ID
     * @param verifyStatus      审核状态
     * @param verifyOpinion     审核意见
     * @param nextVerifyUser    下一步的审核人，没有下一步的时候可空
     * @return
     */
    ServiceResult<String, Integer> verifyWorkFlow(Integer workflowLinkId, Integer verifyStatus, String verifyOpinion, Integer nextVerifyUser);
}
