package com.lxzl.erp.core.service.dingding;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveCallBackDTO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveDTO;
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
     * 绑定钉钉用户列表接口
     * </p>
     * @author daiqi
     * @date 2018/4/20 9:35
     * @param

     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     */
    ServiceResult<String, Object> bindDingDingUsers();

    /** 根据手机号获取钉钉id */
    String getDingdingIdByPhone(String phone);

    /**  
     * <p>
     * 发起审批实例
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
    ServiceResult<String, Object> applyApprovingWorkflow(DingdingApproveDTO dingdingApproveDTO);

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

}
