package com.lxzl.erp.core.service.dingding.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.DingDingConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerConsignInfo;
import com.lxzl.erp.common.domain.dingding.DingdingBaseDTO;
import com.lxzl.erp.common.domain.dingding.DingdingResultDTO;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.domain.dingding.approve.*;
import com.lxzl.erp.common.domain.dingding.approve.formvalue.DingdingFormComponentValueDTO;
import com.lxzl.erp.common.domain.dingding.approve.formvalue.DingdingOrderValueDTO;
import com.lxzl.erp.common.domain.dingding.member.DingdingUserDTO;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.erp.core.service.k3.K3ReturnOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowVerifyUserGroupMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDetailDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowVerifyUserGroupDO;
import com.lxzl.se.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
    private OrderService orderService;
    @Autowired
    private K3ReturnOrderService k3ReturnOrderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WorkflowService workflowService;
    /** 申请审批实例的线程 */
    private ExecutorService approveThreadExecutor = Executors.newFixedThreadPool(10);
    /** 注册用户的线程 */
    private ExecutorService registUserThreadExecutor = Executors.newFixedThreadPool(10);
    @Override
    public String sendUserGroupMessage(String userGroupUrl, DingdingSendTextMessageRequest request) {
        String respContent = null;
        try {
            HttpPost httpPost = new HttpPost(userGroupUrl);
            CloseableHttpClient client = HttpClients.createDefault();
            //解决中文乱码问题
            StringEntity entity = new StringEntity(FastJsonUtil.toJSONString(request), "UTF-8");
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
    public ServiceResult<String, Object> registerUserToDingding(final Integer userId) {
        final ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        try {
            if (userId == null) {
                throw new BusinessException("userId为空" + userId);
            }
            UserDO userDO = userMapper.findByUserId(userId);
            if (userDO == null) {
                throw new BusinessException("用户对象不存在-----" + userDO);
            }
            final DingdingUserDTO dingdingUserDTO = new DingdingUserDTO(ConverterUtil.convert(userDO, User.class));
            if (dingdingUserDTO == null) {
                throw new BusinessException("钉钉用户数据传输对象为空：" + JSONObject.toJSONString(dingdingUserDTO));
            }
            logger.info("注册的钉钉用户数据传输对象为：" + JSONObject.toJSONString(dingdingUserDTO));
            // 往钉钉上注册用户信息
            dingdingUserDTO.setRequestDingdingUrl(DingDingConfig.getInputMemberUrl());
            // 异步线程执行
            registUserThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    DingdingResultDTO dingdingResultDTO = postDingdingGatway(dingdingUserDTO);
                    if (dingdingResultDTO == null || !dingdingResultDTO.isSuccess()) {
                        throw new BusinessException("与钉钉网关交互失败：" + JSONObject.toJSONString(dingdingResultDTO));
                    }
                }
            });
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

    /**  
     * <pre>
     *    1 根据工作回调的工作流编号获取erp系统的工作流信息
     *    2 获取erp系统当前审核人是否与钉钉当前审核人一致，不一致不进行处理（记录日志）
     *    3 钉钉和erp审核人一致 转发状态不提供服务
     *    4 根据钉钉状态判断是同意还是拒绝调用相关接口修改工作流状态
     *
     * </pre>
     * @author daiqi  
     * @date 2018/4/27 17:31  
     * @param  dingdingApproveCallBackDTO
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>  
     */  
    @Override
    public ServiceResult<String, Object> applyApprovingWorkflowCallBack(DingdingApproveCallBackDTO dingdingApproveCallBackDTO, HttpServletRequest request) {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        // 根据用户编号获取用户id，并设置到session中
        UserDO userDO = userMapper.findByUserId(dingdingApproveCallBackDTO.getCurrentVerifyUser());
        request.getSession().setAttribute(CommonConstant.ERP_USER_SESSION_KEY, ConverterUtil.convert(userDO, User.class));
        DingdingApproveCallBackBO dingdingApproveCallBackBO = new DingdingApproveCallBackBO(dingdingApproveCallBackDTO);
        try{
            logger.info("工作流回调接口数据是：" + JSONObject.toJSONString(dingdingApproveCallBackDTO));
            // 审批流单号
            String workFlowNo = dingdingApproveCallBackDTO.getInstanceMarking();
            // 审批状态
            Integer verifyStatus = dingdingApproveCallBackBO.getVerifyStatus();
            // 返回类型---只有当驳回的时候才有用
            Integer returnType = dingdingApproveCallBackBO.getReturnType();
            // 审批备注
            String verifyOpinion = dingdingApproveCallBackBO.getVerifyOpinion();
            // 当前审核的用户
            Integer currentVerifyUser = dingdingApproveCallBackDTO.getCurrentVerifyUser();
            if (dingdingApproveCallBackBO.isDOVerifyWorkFlow()) {
                // 审核工作流---当需要执行审核工作流才执行---校验交给erp系统来处理
                ServiceResult<String, Integer> result = workflowService.verifyWorkFlowFromCore(workFlowNo, verifyStatus, returnType, verifyOpinion, currentVerifyUser, null, null);
                if (!StringUtils.equals(ErrorCode.SUCCESS, result.getErrorCode())) {
                    throw new BusinessException(ErrorCode.getMessage(result.getErrorCode()) +" : " + JSONObject.toJSONString(dingdingApproveCallBackDTO));
                }
            } else {
                // 日志记录
                logger.info("当前状态不需要执行审核工作流接口" + JSONObject.toJSONString(dingdingApproveCallBackDTO));
            }
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
        }
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
    public ServiceResult<String, Object> applyApprovingWorkflowToDingding(final String workflowLinkNo) {
        final ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        final User currentUser = userSupport.getCurrentUser();

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
            dingdingApproveBO = buildDingdingOriginatorUserData(dingdingApproveBO, currentUser);
            // 构建审批人信息
            dingdingApproveBO = buildDingdingApprovers(dingdingApproveBO, workflowLinkDO);
            // 构建表单组件信息
            dingdingApproveBO = buildFormComponentObj(dingdingApproveBO, workflowLinkDO);
            // 设置请求地址
            dingdingApproveBO.getDingdingApproveDTO().setRequestDingdingUrl(DingDingConfig.getApplyApprovingWorkflokwUrl());
            final DingdingApproveBO dingdingApproveBOThread = dingdingApproveBO;
            // 申请
            // 异步线程执行
            approveThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    applyApprovingWorkflowToDingding(dingdingApproveBOThread.getDingdingApproveDTO());
                }
            });
        } catch (Exception e) {
            serviceResult.setErrorCode(ErrorCode.BUSINESS_EXCEPTION);
            e.printStackTrace();
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Object> delApprovingWorkflow(String workflowLinkNo) {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        try {
            Assert.notNull(workflowLinkNo);
            DingdingApproveDTO dingdingApproveDTO = new DingdingApproveDTO();
            dingdingApproveDTO.setInstanceMarking(workflowLinkNo);
            dingdingApproveDTO.setRequestDingdingUrl(DingDingConfig.getDelApprovingWorkflowUrl());
            postDingdingGatway(dingdingApproveDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }

    /**
     * 构建钉钉提交审批实例的用户的数据
     */
    private DingdingApproveBO buildDingdingOriginatorUserData(DingdingApproveBO dingdingApproveBO, User currentUser) {
        if (currentUser.getUserId() == null) {
            throw new BusinessException("当前用户的id为空" + JSONObject.toJSONString(currentUser));
        }
        // 1 获取当前用户的部门编号
        DepartmentDO departmentDO = departmentMapper.findByUserId(currentUser.getUserId());
        if (departmentDO == null || departmentDO.getId() == null) {
            throw new BusinessException("当前用户对应的部门为空" + JSONObject.toJSONString(departmentDO));
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
        dingdingApproveBO.buildInstanceMarking(workflowLinkDO.getWorkflowLinkNo());
        // 构建模板单号
        dingdingApproveBO.buildProcessCode(String.valueOf(workflowLinkDO.getWorkflowTemplateId()));
        // 构建表单组件对象
        DingdingFormComponentValueDTO formComponentValueDTO = getFormComponentObjByTypeAndRefNO(workflowLinkDO);
        if (formComponentValueDTO == null) {
            throw new BusinessException("审批流获取详情数据为空：" + formComponentValueDTO);
        }
        formComponentValueDTO.setVerifyMatters(workflowLinkDO.getVerifyMatters());
        formComponentValueDTO.setWorkflowLinkNo(workflowLinkDO.getWorkflowLinkNo());
        formComponentValueDTO.setWorkflowTemplateName(workflowLinkDO.getWorkflowTemplateName());

        dingdingApproveBO.buildFormComponentObj(formComponentValueDTO);
        return dingdingApproveBO;
    }

    /**
     * 申请钉钉工作流
     */
    private DingdingApproveResultDTO applyApprovingWorkflowToDingding(DingdingApproveDTO dingdingApproveDTO) {
        dingdingApproveDTO.setCallbackUrl(DingDingConfig.applyApprovingWorkflowCallBackUrl);
        DingdingResultDTO dingdingResultDTO = postDingdingGatway(dingdingApproveDTO);
        if (dingdingResultDTO == null || !dingdingResultDTO.isSuccess()) {
            throw new BusinessException("通过钉钉网关发起审批实例失败：" + JSONObject.toJSONString(dingdingResultDTO));
        }
        DingdingApproveResultDTO resultDTO = dingdingResultDTO.getTObj(DingdingApproveResultDTO.class);
        if (resultDTO == null || StringUtils.isBlank(resultDTO.getProcessInstanceId())) {
            throw new BusinessException("获取钉钉审批实例id失败：" + JSONObject.toJSONString(resultDTO));
        }
        return resultDTO;
    }

    /**
     * 使用post方式对钉钉网关的发送请求
     */
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
            } else {
                throw new RuntimeException("响应状态不正确：" + JSONObject.toJSONString(resp));
            }
            logger.info("从钉钉网关返回的结果为：" + respContent);
            dingdingResultDTO = JSONObject.parseObject(respContent, DingdingResultDTO.class);
        } catch (Exception e) {
            dingdingResultDTO = new DingdingResultDTO();
            dingdingResultDTO.setCode(ErrorCode.BUSINESS_EXCEPTION);
            logger.error("applyApprovingWorkflow message error,{}", e);
        }
        return dingdingResultDTO;
    }

    /**
     * 根据工作流类型和引用编号获取表单组件对象
     */
    private DingdingFormComponentValueDTO getFormComponentObjByTypeAndRefNO(WorkflowLinkDO workflowLinkDO) {
        Integer workflowType = workflowLinkDO.getWorkflowType();
        String workflowReferNo = workflowLinkDO.getWorkflowReferNo();
        DingdingFormComponentValueDTO formComponentValueDTO = null;
        boolean supportFlag = true;
        if (WorkflowType.WORKFLOW_TYPE_PURCHASE.equals(workflowType)) {
            // 采购单审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowType)) {
            // 订单信息审批
            formComponentValueDTO = getOrderByNO(workflowReferNo);
        } else if (WorkflowType.WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO.equals(workflowType)) {
            // 调配单审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_CHANGE.equals(workflowType)) {
            // 换货单审批

        } else if (WorkflowType.WORKFLOW_TYPE_RETURN.equals(workflowType)) {
            // 退货单审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_REPAIR.equals(workflowType)) {
            // 维修单审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_PURCHASE_APPLY_ORDER.equals(workflowType)) {
            // 采购申请单审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_TRANSFER_IN_ORDER.equals(workflowType)) {
            // 转移单入审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_TRANSFER_OUT_ORDER.equals(workflowType)) {
            // 转移单出审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_PEER_DEPLOYMENT_INTO.equals(workflowType)) {
            // 订单信息审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_PEER_DEPLOYMENT_OUT.equals(workflowType)) {
            // 同行调拨单出审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT.equals(workflowType)) {
            // 结算单冲正
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_K3_CHANGE.equals(workflowType)) {
            // K3换货单审批
            supportFlag = false;
        } else if (WorkflowType.WORKFLOW_TYPE_K3_RETURN.equals(workflowType)) {
            // K3退货单审批
            formComponentValueDTO = getK3ReturnOrderByNO(workflowReferNo);
        } else if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
            // 客户审批流程
            formComponentValueDTO = getCustomerByNO(workflowReferNo);
        } else if (WorkflowType.WORKFLOW_TYPE_CUSTOMER_CONSIGN.equals(workflowType)) {
            // 客户地址审批流程
            formComponentValueDTO = getCustomerConsignInfoByNO(workflowReferNo);
        }
        if (!supportFlag) {
            throw new BusinessException("钉钉网关对该审批流提供支持");
        }
        return formComponentValueDTO;
    }

    /**
     * 获取订单信息
     */
    private DingdingFormComponentValueDTO getOrderByNO(String refNo) {
        ServiceResult<String, Order> result = orderService.queryOrderByNo(refNo);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            throw new BusinessException(ErrorCode.getMessage(result.getErrorCode()));
        }
        Order order = result.getResult();
        return JSONObject.parseObject(JSONObject.toJSONString(order), DingdingOrderValueDTO.class);
    }

    /**
     * 获取k3退货单信息
     */
    private DingdingFormComponentValueDTO getK3ReturnOrderByNO(String refNo) {
        ServiceResult<String, K3ReturnOrder> result = k3ReturnOrderService.queryReturnOrderByNo(refNo);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            throw new BusinessException(ErrorCode.getMessage(result.getErrorCode()));
        }
        K3ReturnOrder k3ReturnOrder = result.getResult();
        return JSONObject.parseObject(JSONObject.toJSONString(k3ReturnOrder), DingdingOrderValueDTO.class);
    }

    /**
     * 获取客户信息
     */
    private DingdingFormComponentValueDTO getCustomerByNO(String refNo) {
        ServiceResult<String, Customer> result = customerService.detailCustomer(refNo);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            throw new BusinessException(ErrorCode.getMessage(result.getErrorCode()));
        }
        Customer customer = result.getResult();
        return JSONObject.parseObject(JSONObject.toJSONString(customer), DingdingOrderValueDTO.class);
    }

    /**
     * 获取客户地址信息
     */
    private DingdingFormComponentValueDTO getCustomerConsignInfoByNO(String refNo) {
        CustomerConsignInfo queryInfo = new CustomerConsignInfo();
        queryInfo.setCustomerConsignInfoId(Integer.parseInt(refNo));
        ServiceResult<String, CustomerConsignInfo> result = customerService.detailCustomerConsignInfo(queryInfo);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            throw new BusinessException(ErrorCode.getMessage(result.getErrorCode()));
        }
        CustomerConsignInfo customerConsignInfo = result.getResult();
        return JSONObject.parseObject(JSONObject.toJSONString(customerConsignInfo), DingdingOrderValueDTO.class);
    }
}
