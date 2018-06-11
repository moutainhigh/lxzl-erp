package com.lxzl.erp.core.service.dingding;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveCallBackDTO;
import com.lxzl.erp.common.domain.dingding.member.DingdingUserDTO;
import com.lxzl.erp.common.domain.dingding.message.DingdingMessageDTO;
import com.lxzl.se.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-25 19:59
 */
public interface DingdingService extends BaseService {

    /**
     * 发送消息给钉钉
     */
    ServiceResult<String, Object> sendMessageToDingding(DingdingMessageDTO dingdingMessageDTO);

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
     *
     * @param
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     * @author daiqi
     * @date 2018/4/20 9:35
     */
    ServiceResult<String,  List<DingdingUserDTO>> getAllUsersToDingding();

    /**
     * 注册用户到信息到钉钉网关
     */
    ServiceResult<String, Object> registerUserToDingding(Integer userId);

    /**
     * <p>
     * 校验钉钉网关用户数据
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     *
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     * @author daiqi
     * @date 2018/6/4 11:47
     */
    ServiceResult<String, Object> checkDingdingUserDatas();

    /**
     * <p>
     * 钉钉审批结果回调接口
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     *
     * @param
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     * @author daiqi
     * @date 2018/4/20 10:56
     */
    ServiceResult<String, Object> applyApprovingWorkflowCallBack(DingdingApproveCallBackDTO dingdingApproveCallBackDTO, HttpServletRequest request);

    /**
     * <p>
     * 向钉钉提交审批流实例
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     *
     * @param workflowLinkNo 工作流单号
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     * @author daiqi
     * @date 2018/4/20 10:56
     */
    ServiceResult<String, Object> applyApprovingWorkflowToDingding(final String workflowLinkNo);

    /**
     * 注销钉钉网关的审批工作流实例
     */
    ServiceResult<String, Object> delApprovingWorkflow(String workflowLinkNo);

}
