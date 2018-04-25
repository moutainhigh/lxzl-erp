package com.lxzl.erp.core.service.dingding.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.DingDingConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.DingdingResultDTO;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveBO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveCallBackDTO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveDTO;
import com.lxzl.erp.common.domain.dingding.approve.DingdingApproveResultDTO;
import com.lxzl.erp.common.domain.dingding.member.DingdingUserDTO;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowTemplateDingding;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowTemplateDingdingMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowTemplateMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowVerifyUserGroupMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowTemplateDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowTemplateDingdingDO;
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
import java.util.List;

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
    @Autowired
    private WorkflowTemplateDingdingMapper workflowTemplateDingdingMapper;

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
    public String getDingdingIdByPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        List<DingdingUserDTO> dingdingUserDTOS = getUsersFromDingdingGateway();
        if (dingdingUserDTOS == null) {
            logger.info("从钉钉中获取的用户数据失败-----" + phone);
            return null;
        }
        for (DingdingUserDTO dingdingUserDTO : dingdingUserDTOS) {
            if (dingdingUserDTO == null || StringUtils.isBlank(dingdingUserDTO.getMobile())) {
                continue;
            }
            if (StringUtils.equals(dingdingUserDTO.getMobile(), phone)) {
                return dingdingUserDTO.getUserId();
            }
        }
        return null;
    }

    @Override
    public ServiceResult<String, Object> applyApprovingWorkflow(DingdingApproveDTO dingdingApproveDTO) {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        dingdingApproveDTO.setCallbackUrl(DingDingConfig.applyApprovingWorkflowCallBackUrl);
        DingdingResultDTO dingdingResultDTO = doApplyApprovingWorkflowToDingdingGateway(dingdingApproveDTO);
        if (!dingdingResultDTO.isSuccess()) {
            logger.error("通过钉钉网关发起审批实例失败：" + JSONObject.toJSONString(dingdingResultDTO));
            serviceResult.setResult(dingdingResultDTO.getResultMap());
            serviceResult.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
            return serviceResult;
        }
        DingdingApproveResultDTO resultDTO = dingdingResultDTO.getTObj(DingdingApproveResultDTO.class);
        if (resultDTO == null || StringUtils.isBlank(resultDTO.getProcessInstanceId())) {
            logger.error("获取钉钉审批实例id失败：" + JSONObject.toJSONString(resultDTO));
            serviceResult.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
            return serviceResult;
        }
        serviceResult.setResult(resultDTO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Object> applyApprovingWorkflowCallBack(DingdingApproveCallBackDTO dingdingApproveCallBackDTO) {
        logger.info("工作流回调接口数据是：" + JSONObject.toJSONString(dingdingApproveCallBackDTO));
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Object> bindDingDingUsers() {
        ServiceResult<String, Object> result = new ServiceResult<>();
        result.setErrorCode(ErrorCode.SUCCESS);
        List<DingdingUserDTO> dingdingUserDTOS = getUsersFromDingdingGateway();
        if (dingdingUserDTOS == null) {
            logger.info("从钉钉网关获取用户信息失败：" + JSONObject.toJSONString(dingdingUserDTOS));
            result.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
            return result;
        }
        logger.info("获取钉钉的用户列表为：" + JSONObject.toJSONString(dingdingUserDTOS));
        List<User> usersFromDataBase = userService.findUsersByDingdingUsers(dingdingUserDTOS);
        // 更新数据库中钉钉编号
        int count = updateDingdingIdUsers(dingdingUserDTOS, usersFromDataBase);
        logger.info("更新钉钉id的条数为：" + count);
        List<DingdingUserDTO> notMatchUsers = getNotMatchUsers(dingdingUserDTOS, usersFromDataBase);
        logger.info("没有匹配上的钉钉用户为：" + JSONObject.toJSONString(notMatchUsers));
        result.setResult(notMatchUsers);
        return result;
    }

    /**
     * <p>
     * 简要说明
     * </p>
     * <pre>
     * 1 erp审批流程的单号
     * 2 需要传入审批人对应钉钉用户id---根据审批流程的单号去获取(erp_workflow_verify_user_group.verify_user)
     * 3 需要传入抄送人对应钉钉用户id---根据审批流程的单号去获取(非必须)
     * 4 抄送的时期选择（非必须）
     * 5 钉钉审批表单组件值列表---根据审批流程的单号去获取(erp_workflow_link.verify_mattters,erp_workflow_template.workflow_name,erp_workflow_template.workflow_desc)
     * 6 钉钉审批实例的模板代码---根据审批流程的单号去获取
     * 7 当前用户的对应的钉钉部门编号---通过当前用户关联角色关联部门获取
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
                logger.error("提交钉钉工作流信息失败,workflowLinkNo为空：" + workflowLinkNo);
                throw new BusinessException("提交钉钉工作流信息失败,workflowLinkNo为空");
            }
            DingdingApproveBO dingdingApproveBO = new DingdingApproveBO();
            // 构建钉钉提交审批实例的用户的数据
            dingdingApproveBO = buildDingdingOriginatorUserData(dingdingApproveBO);
            // 获取工作流实体对象
            WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
            // 构建钉钉审批人列表信息
            dingdingApproveBO = buildDingdingApprovers(dingdingApproveBO, workflowLinkDO);
            // 构建钉钉表单组件值列表信息
            dingdingApproveBO = buildDindingFormComponentValues(dingdingApproveBO, workflowLinkDO);
            // 更新钉钉工作流编号
            updateDingdingWorkflowId(dingdingApproveBO, workflowLinkDO);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
            return serviceResult;
        }
        return serviceResult;
    }

    /**
     * 构建钉钉提交审批实例的用户的数据
     */
    private DingdingApproveBO buildDingdingOriginatorUserData(DingdingApproveBO dingdingApproveBO) {
        User currentUser = userSupport.getCurrentUser();
        if (StringUtils.isBlank(currentUser.getDingdingUserId())) {
            logger.error("当前用户的钉钉id为空: " + JSONObject.toJSONString(currentUser));
            throw new BusinessException("当前用户的钉钉id为空");
        }
        // 1 获取当前用户的钉钉部门编号
        DepartmentDO departmentDO = departmentMapper.findByUserIdOfDingdingDepId(currentUser.getUserId());
        if (departmentDO == null || StringUtils.isBlank(departmentDO.getDingdingDeptId())) {
            logger.error("当前用户对应的钉钉部门为空: " + JSONObject.toJSONString(departmentDO));
            throw new BusinessException("当前用户对应的钉钉部门为空");
        }
        dingdingApproveBO.buildDeptId(departmentDO.getDingdingDeptId());
        dingdingApproveBO.buildOriginatorUserId(currentUser.getDingdingUserId());
        return dingdingApproveBO;
    }

    /**
     * 构建钉钉审批人列表信息
     */
    private DingdingApproveBO buildDingdingApprovers(DingdingApproveBO dingdingApproveBO, WorkflowLinkDO workflowLinkDO) {
        List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOs = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDO.getVerifyUserGroupId());
        List<Integer> userIds = new ArrayList<>();
        for (WorkflowVerifyUserGroupDO verifyUserGroupDO : workflowVerifyUserGroupDOs) {
            userIds.add(verifyUserGroupDO.getVerifyUser());
        }
        if (CollectionUtil.isEmpty(userIds)) {
            logger.error("审核用户编号为空: " + JSONArray.toJSONString(userIds));
            throw new BusinessException("审核用户编号为空");
        }
        // 根据id列表获取用户列表信息
        List<UserDO> userDOS = userMapper.findUsersByIds(userIds);
        if (CollectionUtil.isEmpty(userDOS) || userDOS.size() != userIds.size()) {
            logger.error("用户实体对象列表数量不匹配userIds列表为: " + JSONArray.toJSONString(userIds));
            logger.error("用户实体对象列表数量不匹配实体对象列表为: " + JSONArray.toJSONString(userDOS));
            throw new BusinessException("用户实体对象列表数量不匹配");
        }
        // 设置审批人id列表信息
        for (UserDO userDO : userDOS) {
            dingdingApproveBO.addApprover(userDO.getDingdingUserId());
        }
        return dingdingApproveBO;
    }

    /**
     * 构建钉钉表单组件值列表信息
     */
    private DingdingApproveBO buildDindingFormComponentValues(DingdingApproveBO dingdingApproveBO, WorkflowLinkDO workflowLinkDO) {
        // 设置钉钉表单值
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowTemplateId(workflowLinkDO.getWorkflowTemplateId());
        if (workflowTemplateDO == null || StringUtils.isBlank(workflowTemplateDO.getDingdingProcessCode())) {
            logger.error("模板对象为空或者钉钉模板编号为空: " + JSONObject.toJSONString(workflowTemplateDO));
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION);
        }
        List<WorkflowTemplateDingdingDO> workflowTemplateDingdings = workflowTemplateDingdingMapper.listByDingdingProcessCode(workflowTemplateDO.getDingdingProcessCode());
        if (CollectionUtil.isEmpty(workflowTemplateDingdings)) {
            logger.error("钉钉模板列表为空workflowTemplateDingdings: " + JSONObject.toJSONString(workflowTemplateDingdings));
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION);
        }
        dingdingApproveBO.buildProcessCode(workflowTemplateDO.getDingdingProcessCode());

        dingdingApproveBO.addDingdingFormComponentValue("工作流名称", workflowTemplateDO.getWorkflowName());
        dingdingApproveBO.addDingdingFormComponentValue("工作流类型", workflowLinkDO.getVerifyMatters());
        dingdingApproveBO.addDingdingFormComponentValue("工作流描述", workflowTemplateDO.getWorkflowDesc());
        return dingdingApproveBO;
    }

    /**
     * 更新钉钉工作流编号
     */
    private int updateDingdingWorkflowId(DingdingApproveBO dingdingApproveBO, WorkflowLinkDO workflowLinkDO) {
        ServiceResult<String, Object> dingdingResult = applyApprovingWorkflow(dingdingApproveBO.getDingdingApproveDTO());
        if (!ErrorCode.SUCCESS.equals(dingdingResult.getErrorCode())) {
            logger.error("提交钉钉审批流程失败: " + JSONArray.toJSONString(dingdingResult));
            throw new BusinessException("提交钉钉审批流程失败");
        }
        String resultJsonStr = JSONObject.toJSONString(dingdingResult.getResult());
        DingdingApproveResultDTO resultDTO = JSONObject.parseObject(resultJsonStr, DingdingApproveResultDTO.class);
        workflowLinkDO.setDingdingWorkflowId(resultDTO.getProcessInstanceId());
        return workflowLinkMapper.update(workflowLinkDO);
    }

    /**
     * 获取没有匹配的钉钉用户列表信息
     */
    private List<DingdingUserDTO> getNotMatchUsers(List<DingdingUserDTO> dingdingUserDTOS, List<User> usersFromDataBase) {
        if (CollectionUtil.isEmpty(dingdingUserDTOS) || CollectionUtil.isEmpty(usersFromDataBase)) {
            return dingdingUserDTOS;
        }
        List<DingdingUserDTO> notMatchUsers = new ArrayList<>();
        for (DingdingUserDTO dingdingUserDTO : dingdingUserDTOS) {
            boolean notMatch = true;
            for (User user : usersFromDataBase) {
                if (isMatchUser(user, dingdingUserDTO)) {
                    notMatch = false;
                    break;
                }
            }
            if (notMatch) {
                notMatchUsers.add(dingdingUserDTO);
            }
        }
        return notMatchUsers;
    }

    /**
     * 更新用户信息的钉钉编号
     */
    private int updateDingdingIdUsers(List<DingdingUserDTO> dingdingUserDTOS, List<User> usersFromDataBase) {
        if (CollectionUtil.isEmpty(dingdingUserDTOS) || CollectionUtil.isEmpty(usersFromDataBase)) {
            return 0;
        }
        List<User> bindDingdingIdUsers = new ArrayList<>();
        for (User user : usersFromDataBase) {
            for (DingdingUserDTO dingdingUserDTO : dingdingUserDTOS) {
                if (isMatchUser(user, dingdingUserDTO)) {
                    user.setDingdingUserId(dingdingUserDTO.getUserId());
                }
            }
            bindDingdingIdUsers.add(user);
        }
        return userService.updateDingdingIdUsers(bindDingdingIdUsers);
    }

    /**
     * 是否是匹配的用户
     */
    private boolean isMatchUser(User user, DingdingUserDTO dingdingUserDTO) {
        boolean phoneEqual = StringUtils.equals(user.getPhone(), dingdingUserDTO.getMobile());
        boolean nameEqual = !phoneEqual && StringUtils.equals(user.getRealName(), dingdingUserDTO.getName());

        return phoneEqual || nameEqual;
    }

    /**
     * 从钉钉网关获取钉钉用户数据传输对象列表
     */
    private List<DingdingUserDTO> getUsersFromDingdingGateway() {
        String respContent = doGetUsersFromDingdingGateway();
        DingdingResultDTO dingdingResultDTO = JSONObject.parseObject(respContent, DingdingResultDTO.class);
        if (dingdingResultDTO == null || !dingdingResultDTO.isSuccess()) {
            logger.info("从钉钉网关获取用户信息失败：" + JSONObject.toJSONString(dingdingResultDTO));
            return null;
        }
        String resultMapStr = JSONObject.toJSONString(dingdingResultDTO.getResultMap());
        return JSONObject.parseArray(resultMapStr, DingdingUserDTO.class);
    }

    /**
     * 从钉钉网关获取用户列表信息
     */
    private String doGetUsersFromDingdingGateway() {
        String respContent = null;
        try {
            HttpGet httpGet = new HttpGet(DingDingConfig.getGetMemberListbyDepartmentUrl() + "?departmentId=1");
            CloseableHttpClient client = HttpClients.createDefault();
            httpGet.setHeader("content-type", "utf-8");
            HttpResponse resp = client.execute(httpGet);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = resp.getEntity();
                respContent = EntityUtils.toString(entity, "UTF-8");
                logger.info("网关处返回的结果为：" + respContent);
                return respContent;
            }
        } catch (Exception e) {
            logger.error("importDingDingUsers message error,{}", e);
        }
        return respContent;
    }

    /**
     * <p>
     * 发起审批实例到钉钉网关
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     *
     * @param dingdingApproveDTO : 钉钉发起审批实例数据传输对象
     * @return com.lxzl.erp.common.domain.dingding.DingdingResultDTO
     * @author daiqi
     * @date 2018/4/23 9:19
     */
    private DingdingResultDTO doApplyApprovingWorkflowToDingdingGateway(DingdingApproveDTO dingdingApproveDTO) {
        DingdingResultDTO dingdingResultDTO = null;
        String respContent = null;
        try {
            String requestUrl = DingDingConfig.getApplyApprovingWorkflowUrl() + "?callbackUrl=" + dingdingApproveDTO.getCallbackUrl();
            HttpPost httpPost = new HttpPost(requestUrl);
            CloseableHttpClient client = HttpClients.createDefault();
            String jsonStr = JSONObject.toJSONString(dingdingApproveDTO);
            logger.info("申请审批实例钉钉网关请求数据：" + jsonStr);
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
            logger.info("申请审批实例钉钉网关返回的结果为：" + respContent);
        } catch (Exception e) {
            dingdingResultDTO = new DingdingResultDTO();
            dingdingResultDTO.setCode(ErrorCode.BUSINESS_EXCEPTION);
            logger.error("applyApprovingWorkflow message error,{}", e);
        }
        return dingdingResultDTO;
    }
}
