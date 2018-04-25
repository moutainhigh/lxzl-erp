package com.lxzl.erp.core.service.dingding.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.DingDingConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.DingdingBaseDTO;
import com.lxzl.erp.common.domain.dingding.DingdingResultDTO;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveBO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveCallBackDTO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveDTO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveResultDTO;
import com.lxzl.erp.common.domain.dingding.member.DingdingUserDTO;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowTemplateMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowVerifyUserGroupMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowTemplateDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowVerifyUserGroupDO;
import com.lxzl.se.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018-02-05 15:00
 */
@Service("dingdingService")
public class DingdingServiceImpl implements DingdingService {
    private static final Logger logger = LoggerFactory.getLogger(DingdingServiceImpl.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private WorkflowVerifyUserGroupMapper workflowVerifyUserGroupMapper;
    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkflowTemplateMapper workflowTemplateMapper;

    @Override
    public String sendUserGroupMessage(String userGroupUrl, DingdingSendTextMessageRequest request) {
        String respContent = null;
        try {
            HttpPost httpPost = new HttpPost(userGroupUrl);
            CloseableHttpClient client = HttpClients.createDefault();

            StringEntity entity = new StringEntity(FastJsonUtil.toJSONString(request), "UTF-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
            logger.info("dingding send message success,{},response : {}", respContent);
        } catch (Exception e) {
            logger.error("dingding send message error,{}", e);
        }
        return respContent;
    }

    @Override
    public ServiceResult<String, Object> registerUserToDingding(Integer userId) {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        try {
            if (userId == null) {
                throw new BusinessException("userId为空" + userId);
            }
            UserDO userDO = userMapper.findByUserId(userId);
            if (userDO == null) {
                throw new BusinessException("用户对象不存在-----" + userDO);
            }
            DingdingUserDTO dingdingUserDTO = new DingdingUserDTO(ConverterUtil.convert(userDO, User.class));
            if (dingdingUserDTO == null) {
                throw new BusinessException("钉钉用户数据传输对象为空：" + JSONObject.toJSONString(dingdingUserDTO));
            }
            logger.info("注册的钉钉用户数据传输对象为：" + JSONObject.toJSONString(dingdingUserDTO));
            // 往钉钉上注册用户信息
            dingdingUserDTO.setRequestDingdingUrl(DingDingConfig.getInputMemberUrl());
            DingdingResultDTO dingdingResultDTO = postDingdingGatway(dingdingUserDTO);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Object> getAllUsersToDingding() {
        ServiceResult<String, Object> result = new ServiceResult<>();
        result.setErrorCode(ErrorCode.SUCCESS);
        List<UserDO> userDOS = userMapper.listAllUser();
        if (userDOS == null) {
            logger.info("从钉钉网关获取用户信息失败：" + JSONObject.toJSONString(userDOS));
            result.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
            return result;
        }
        List<User> users = ConverterUtil.convertList(userDOS, User.class);
        List<DingdingUserDTO> dingdingUserDTOS = new ArrayList<>();
        for (User user : users) {
            dingdingUserDTOS.add(new DingdingUserDTO(user));
        }
        result.setResult(dingdingUserDTOS);
        // 更新数据库中钉钉编号
        return result;
    }

    @Override
    public ServiceResult<String, Object> applyApprovingWorkflowCallBack(DingdingApproveCallBackDTO dingdingApproveCallBackDTO) {
        logger.info("工作流回调接口数据是：" + JSONObject.toJSONString(dingdingApproveCallBackDTO));
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }



    /**
     * <p>
     * 简要说明
     * </p>
     * <pre>
     * 1 erp审批流程的单号
     * 2 需要传入审批人用户id列表---根据审批流程的单号去获取(erp_workflow_verify_user_group.verify_user)
     * 3 需要传入抄送人用户id列表---根据审批流程的单号去获取(非必须)
     * 4 抄送的时期选择（非必须）
     * 5 钉钉审批表单组件值列表---根据审批流程的单号去获取(erp_workflow_link.verify_mattters,erp_workflow_template.workflow_name,erp_workflow_template.workflow_desc)
     * 6 钉钉审批实例的模板代码---根据审批流程的单号去获取
     * 7 当前用户的对应的部门编号---通过当前用户关联角色关联部门获取
     * </pre>
     *
     * @param
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     * @author daiqi
     * @date 2018/4/24 9:22
     */
    @Override
    public ServiceResult<String, Object> applyApprovingWorkflowToDingding(String workflowLinkNo) {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        try {
            if (StringUtils.isBlank(workflowLinkNo)) {
                throw new BusinessException("提交钉钉工作流信息失败,workflowLinkNo为空" + workflowLinkNo);
            }
            // 获取工作流实体对象
            WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
            if (workflowLinkDO == null) {
                throw new BusinessException("工作流实体对象不存在：" + workflowLinkNo);
            }
            DingdingApproveBO dingdingApproveBO = new DingdingApproveBO();
            // 构建提交审批实例的用户的数据
            dingdingApproveBO = buildDingdingOriginatorUserData(dingdingApproveBO);
            // 构建审批人信息
            dingdingApproveBO = buildDingdingApprovers(dingdingApproveBO, workflowLinkDO);
            // 构建表单组件信息
            dingdingApproveBO = buildFormComponentObj(dingdingApproveBO, workflowLinkDO);
            // 设置请求地址
            dingdingApproveBO.getDingdingApproveDTO().setRequestDingdingUrl(DingDingConfig.getInputMemberUrl());
            // 申请
            applyApprovingWorkflowToDingding(dingdingApproveBO.getDingdingApproveDTO());
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
        }
        return serviceResult;
    }

    /**
     * 构建钉钉提交审批实例的用户的数据
     */
    private DingdingApproveBO buildDingdingOriginatorUserData(DingdingApproveBO dingdingApproveBO) {
        User currentUser = userSupport.getCurrentUser();
        if (currentUser.getUserId() == null) {
            throw new BusinessException("当前用户的id为空"+ JSONObject.toJSONString(currentUser));
        }
        // 1 获取当前用户的部门编号
        DepartmentDO departmentDO = departmentMapper.findByUserId(currentUser.getUserId());
        if (departmentDO == null || departmentDO.getId() == null) {
            throw new BusinessException("当前用户对应的部门为空"+ JSONObject.toJSONString(departmentDO));
        }
        dingdingApproveBO.buildDeptId(departmentDO.getId());
        dingdingApproveBO.buildOriginatorUserId(currentUser.getUserId());
        return dingdingApproveBO;
    }

    /**
     * 构建钉钉审批人列表信息
     */
    private DingdingApproveBO buildDingdingApprovers(DingdingApproveBO dingdingApproveBO, WorkflowLinkDO workflowLinkDO) {
        List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOs = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDO.getVerifyUserGroupId());
        Set<Integer> userIds = new LinkedHashSet<>();
        for (WorkflowVerifyUserGroupDO verifyUserGroupDO : workflowVerifyUserGroupDOs) {
            userIds.add(verifyUserGroupDO.getVerifyUser());
        }
        if (CollectionUtil.isEmpty(userIds)) {
            logger.error("审核用户编号为空: " + JSONArray.toJSONString(userIds));
            throw new BusinessException("审核用户编号为空");
        }
        // 设置审批人id列表信息
        dingdingApproveBO.addApprovers(userIds);
        return dingdingApproveBO;
    }

    /**
     * 构建表单组件值信息
     */
    private DingdingApproveBO buildFormComponentObj(DingdingApproveBO dingdingApproveBO, WorkflowLinkDO workflowLinkDO) {
        // 构建工作流单号
        dingdingApproveBO.buildWorkflowLinkNo(workflowLinkDO.getWorkflowLinkNo());
        // 构建模板单号
        dingdingApproveBO.buildProcessCode(String.valueOf(workflowLinkDO.getWorkflowTemplateId()));
        // 构建表单组件对象
        dingdingApproveBO.buildFormComponentObj(new Object());
        return dingdingApproveBO;
    }

    /** 申请钉钉工作流 */
    private DingdingApproveResultDTO applyApprovingWorkflowToDingding(DingdingApproveDTO dingdingApproveDTO) {
        dingdingApproveDTO.setCallbackUrl(DingDingConfig.applyApprovingWorkflowCallBackUrl);
        DingdingResultDTO dingdingResultDTO = postDingdingGatway(dingdingApproveDTO);
        if (!dingdingResultDTO.isSuccess()) {
            throw new BusinessException("通过钉钉网关发起审批实例失败：" + JSONObject.toJSONString(dingdingResultDTO));
        }
        DingdingApproveResultDTO resultDTO = dingdingResultDTO.getTObj(DingdingApproveResultDTO.class);
        if (resultDTO == null || StringUtils.isBlank(resultDTO.getProcessInstanceId())) {
            throw new BusinessException("获取钉钉审批实例id失败：" + JSONObject.toJSONString(resultDTO));
        }
        return resultDTO;
    }

    /** 使用post方式对钉钉网关的发送请求 */
    private DingdingResultDTO postDingdingGatway(DingdingBaseDTO dingdingBaseDTO) {
        DingdingResultDTO dingdingResultDTO = null;
        String respContent = null;
        try {
            StringBuilder requestUrlBuild = new StringBuilder();
            requestUrlBuild.append(dingdingBaseDTO.getRequestDingdingUrl());
            requestUrlBuild.append("?").append("secret=").append(dingdingBaseDTO.getSecret());
            if (StringUtils.isNotBlank(dingdingBaseDTO.getCallbackUrl())) {
                requestUrlBuild.append("&").append("callbackUrl=").append(dingdingBaseDTO.getCallbackUrl());
            }
            HttpPost httpPost = new HttpPost(requestUrlBuild.toString());
            CloseableHttpClient client = HttpClients.createDefault();
            String jsonStr = JSONObject.toJSONString(dingdingBaseDTO);
            logger.info("发给钉钉网关请求数据：" + jsonStr);
            //解决中文乱码问题
            StringEntity entity = new StringEntity(jsonStr, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json;charset=utf-8");
            httpPost.setEntity(entity);
            HttpResponse resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
            dingdingResultDTO = JSONObject.parseObject(respContent, DingdingResultDTO.class);
            logger.info("从钉钉网关返回的结果为：" + respContent);
        } catch (Exception e) {
            dingdingResultDTO = new DingdingResultDTO();
            dingdingResultDTO.setCode(ErrorCode.BUSINESS_EXCEPTION);
            logger.error("applyApprovingWorkflow message error,{}", e);
        }
        return dingdingResultDTO;
    }
}
