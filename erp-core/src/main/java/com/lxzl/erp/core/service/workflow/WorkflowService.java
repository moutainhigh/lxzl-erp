package com.lxzl.erp.core.service.workflow;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

/**
 * 描述: 工作流
 *
 * @author gaochao
 * @date 2017-11-04 16:05
 */
public interface WorkflowService extends BaseService {
    /**
     * 提交工作流
     *
     * @param workflowType    工作流类型，详见WorkflowType
     * @param workflowReferNo 工作流关联ID，业务NO
     * @param verifyUser      目标审核人
     * @return 错误代码，生成的工作流ID
     */
    ServiceResult<String, String> commitWorkFlow(Integer workflowType, String workflowReferNo, Integer verifyUser);


    /**
     * 获取可审核人
     *
     * @param workflowType    工作流类型，详见WorkflowType
     * @param workflowReferNo 工作流关联ID，业务ID
     * @return 错误代码，可审核人
     */
    ServiceResult<String, List<User>> getNextVerifyUsers(Integer workflowType, String workflowReferNo);

    /**
     * 获取当前工作流记录
     *
     * @param workflowType    工作流类型，详见WorkflowType
     * @param workflowReferNo 工作流关联ID，业务ID
     * @return 错误代码，工作流记录
     */
    ServiceResult<String, WorkflowLink> getWorkflowLink(Integer workflowType, String workflowReferNo);

    /**
     * 获取工作流列表
     *
     * @param workflowLinkQueryParam    获取工作流列表参数
     * @return 错误代码，工作流记录
     */
    ServiceResult<String, Page<WorkflowLink>> getWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam);

    /**
     * 获取当前人工作流列表
     *
     * @param workflowLinkQueryParam    获取工作流列表参数
     * @return 错误代码，工作流记录
     */
    ServiceResult<String, Page<WorkflowLink>> getCurrentUserWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam);

    /**
     * 审核工作流
     *
     * @param workflowLinkNo 工作流ID
     * @param verifyStatus   审核状态
     * @param returnType   返回类型0返回根部，1返回上一级
     * @param verifyOpinion  审核意见
     * @param nextVerifyUser 下一步的审核人，没有下一步的时候可空
     * @return 工作流ID
     */
    ServiceResult<String, Integer> verifyWorkFlow(String workflowLinkNo, Integer verifyStatus, Integer returnType, String verifyOpinion, Integer nextVerifyUser);
}
