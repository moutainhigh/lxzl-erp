package com.lxzl.erp.core.service.dingding;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveCallBackDTO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveDTO;
import com.lxzl.erp.common.domain.dingding.member.DingdingUserDTO;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-25 19:59
 */
public interface DingdingService extends BaseService {

    /**
     * 叮叮消息推送信息
     *
     * @param userGroupUrl
     * @param request
     * @return
     */
    String sendUserGroupMessage(String userGroupUrl, DingdingSendTextMessageRequest request);

    /**
     * <p>
     * 获取所有的用户信息提供给钉钉网关
     * </p>
     * @author daiqi
     * @date 2018/4/20 9:35
     * @param

     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     */
    ServiceResult<String, Object> getAllUsersToDingding();

    /** 注册用户到信息到钉钉网关 */
    ServiceResult<String, Object> registerUserToDingding(Integer userId);

    /**
     * <p>
     * 钉钉审批结果回调接口
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     * @author daiqi
     * @date 2018/4/20 10:56
     * @param

     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     */
    ServiceResult<String, Object> applyApprovingWorkflowCallBack(DingdingApproveCallBackDTO dingdingApproveCallBackDTO);

    /**
     * <p>
     * 钉钉审批结果回调接口
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     * @author daiqi
     * @date 2018/4/20 10:56
     * @param workflowLinkNo 工作流单号

     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     */
    ServiceResult<String, Object> applyApprovingWorkflowToDingding(String workflowLinkNo);

}
