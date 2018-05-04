package com.lxzl.erp.core.service.workflow.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.WorkflowTemplateQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowNode;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowTemplate;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.erp.core.service.message.MessageService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkFlowManager;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyCityCoverMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder.DeploymentOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.*;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyCityCoverDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.erp.dataaccess.domain.workflow.*;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-04 16:12
 */
@Service("workflowService")
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private WorkflowTemplateMapper workflowTemplateMapper;

    @Autowired
    private WorkflowNodeMapper workflowNodeMapper;

    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;

    @Autowired
    private WorkflowLinkDetailMapper workflowLinkDetailMapper;

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private WorkFlowManager workFlowManager;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;
    @Autowired
    private DeploymentOrderMapper deploymentOrderMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private ImgMysqlMapper imgMysqlMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;

    @Autowired
    private WorkflowVerifyUserGroupMapper workflowVerifyUserGroupMapper;

    @Autowired
    private SubCompanyCityCoverMapper subCompanyCityCoverMapper;

    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;

    @Autowired
    private DingdingService dingdingService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitWorkFlow(Integer workflowType, String workflowReferNo, Integer verifyUser, String verifyMatters, String commitRemark, List<Integer> imgIdList, String orderRemark) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_HAVE_NO_CONFIG);
            return result;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_HAVE_NO_NODE);
            return result;
        }
        String workflowLinkNo = null;
        if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
            ServiceResult<String, String> customerCommitWorkFlow = customerCommitWorkFlow(workflowType, workflowReferNo, verifyUser, verifyMatters, commitRemark, imgIdList, orderRemark, currentTime, workflowNodeDOList, workflowTemplateDO);
            if (!ErrorCode.SUCCESS.equals(customerCommitWorkFlow.getErrorCode())) {
                result.setErrorCode(customerCommitWorkFlow.getErrorCode());
                return result;
            }
            workflowLinkNo = customerCommitWorkFlow.getResult();
        } else {
            Integer subCompanyId = getSubCompanyId(workflowType, workflowReferNo,workflowNodeDOList.get(0));
            if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(subCompanyId)) {
                subCompanyId = CommonConstant.HEAD_COMPANY_ID;
            }
            WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
            if (!verifyVerifyUsers(thisWorkflowNodeDO, verifyUser, subCompanyId)) {
                result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
                return result;
            }
            WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
            if (workflowLinkDO == null) {
                workflowLinkNo = generateWorkflowLink(workflowTemplateDO, workflowReferNo, commitRemark, verifyUser, verifyMatters, imgIdList, currentTime, orderRemark);
                workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
            } else {
                String errorCode = continueWorkflowLink(workflowLinkDO, commitRemark, verifyUser, verifyMatters, imgIdList, currentTime, orderRemark);
                if (!ErrorCode.SUCCESS.equals(errorCode)) {
                    result.setErrorCode(errorCode);
                    return result;
                }
                workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
            }
            messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), verifyUser);
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkNo);
        // 提交工作流到钉钉上
        dingdingService.applyApprovingWorkflowToDingding(workflowLinkNo);
        return result;
    }

    private void saveWorkflowImage(Integer workflowDetailId, List<Integer> imgIdList, Date currentTime) {
        //对营业执照图片操作
        if (CollectionUtil.isNotEmpty(imgIdList)) {
            for (Integer imageId : imgIdList) {
                ImageDO workflowImage = imgMysqlMapper.findById(imageId);
                if (workflowImage == null) {
                    return;
                }
                if (StringUtil.isNotEmpty(workflowImage.getRefId())) {
                    return;
                }
                workflowImage.setImgType(ImgType.WORKFLOW_IMG_TYPE);
                workflowImage.setRefId(workflowDetailId.toString());
                workflowImage.setUpdateUser(userSupport.getCurrentUserId().toString());
                workflowImage.setUpdateTime(currentTime);
                imgMysqlMapper.update(workflowImage);
            }
        }
    }

    private Integer getSubCompanyId(Integer workflowType, String workflowReferNo,WorkflowNodeDO workflowNodeDO) {
        Integer subCompanyId = -1;
        if (WorkflowType.WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO.equals(workflowType)) {
            DeploymentOrderDO deploymentOrderDO = deploymentOrderMapper.findByNo(workflowReferNo);
            WarehouseDO warehouseDO = warehouseMapper.findById(deploymentOrderDO.getTargetWarehouseId());
            if (warehouseDO != null) {
                subCompanyId = warehouseDO.getSubCompanyId();
            }
        } else if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
            CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
            if (customerDO != null) {
                subCompanyId = customerDO.getOwnerSubCompanyId();
            }
        } else if (WorkflowType.WORKFLOW_TYPE_CUSTOMER_CONSIGN.equals(workflowType)) {
            CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(Integer.valueOf(workflowReferNo));
            if (customerConsignInfoDO != null) {
                SubCompanyCityCoverDO citySubCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerConsignInfoDO.getCity());
                if (citySubCompanyCityCoverDO != null) {
                    subCompanyId = citySubCompanyCityCoverDO.getSubCompanyId();
                } else {
                    //针对香港澳门审核
                    SubCompanyCityCoverDO ProvinceSubCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerConsignInfoDO.getProvince());
                    if (ProvinceSubCompanyCityCoverDO != null) {
                        subCompanyId = ProvinceSubCompanyCityCoverDO.getSubCompanyId();
                    }
                }
            }
        }else if (WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowType)) {
            if(CommonConstant.WORKFLOW_STEP_TWO.equals(workflowNodeDO.getWorkflowStep())){
                OrderDO orderDO = orderMapper.findByOrderNo(workflowReferNo);
                if(orderDO != null){
                    subCompanyId = orderDO.getDeliverySubCompanyId();
                }
            }else{
                subCompanyId = userSupport.getCurrentUserCompanyId();
            }
        } else {
            subCompanyId = userSupport.getCurrentUserCompanyId();
        }
        return subCompanyId;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> cancelWorkFlow(Integer workflowType, String workflowReferNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }

        if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowLinkDO.getCurrentVerifyStatus())
                || VerifyStatus.VERIFY_STATUS_BACK.equals(workflowLinkDO.getCurrentVerifyStatus())) {
            result.setErrorCode(ErrorCode.VERIFY_STATUS_ERROR);
            return result;
        }

        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_CANCEL);
        workflowLinkDO.setCurrentVerifyUser(userSupport.getCurrentUserId());
        workflowLinkMapper.update(workflowLinkDO);
        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();

        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList != null && workflowLinkDetailDOList.size() > 1) {
            WorkflowLinkDetailDO previousWorkflowLinkDetailDO = workflowLinkDetailDOList.get(1);
            workflowLinkDetailDO.setWorkflowPreviousNodeId(previousWorkflowLinkDetailDO.getWorkflowCurrentNodeId());
            workflowLinkDetailDO.setWorkflowStep(previousWorkflowLinkDetailDO.getWorkflowStep() + 1);
        }

        workflowLinkDetailDO.setVerifyUser(userSupport.getCurrentUserId());
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_CANCEL);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> rejectPassWorkFlow(Integer workflowType, String workflowReferNo, String commitRemark) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        User loginUser = userSupport.getCurrentUser();

        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);

        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS);
            return result;
        }
        if (workflowLinkDO != null) {
            WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = saveWorkflowVerifyUserGroup(loginUser.getUserId(), currentTime, commitRemark);

            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
            workflowLinkDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
            workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
            workflowLinkDO.setUpdateTime(currentTime);
            workflowLinkMapper.update(workflowLinkDO);

            saveWorkflowLinkDetail(workflowLinkDO.getId(), workflowReferNo, (workflowTemplateDO.getWorkflowNodeDOList().size() + 1), loginUser.getUserId(), workflowVerifyUserGroupDO.getVerifyUserGroupId(), currentTime, commitRemark);
        } else {
            WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = saveWorkflowVerifyUserGroup(loginUser.getUserId(), currentTime, commitRemark);

            WorkflowLinkDO newWorkflowLinkDO = new WorkflowLinkDO();
            newWorkflowLinkDO.setWorkflowLinkNo(generateNoSupport.generateWorkflowLinkNo(currentTime, loginUser.getUserId()));
            newWorkflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
            newWorkflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
            newWorkflowLinkDO.setWorkflowReferNo(workflowReferNo);
            newWorkflowLinkDO.setWorkflowStep(3);
            newWorkflowLinkDO.setWorkflowLastStep(3);
            newWorkflowLinkDO.setWorkflowCurrentNodeId(workflowTemplateDO.getWorkflowNodeDOList().get(1).getId());
            newWorkflowLinkDO.setCommitUser(loginUser.getUserId());
            newWorkflowLinkDO.setCurrentVerifyUser(loginUser.getUserId());
            newWorkflowLinkDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
            newWorkflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
            newWorkflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            newWorkflowLinkDO.setCreateUser(loginUser.getUserId().toString());
            newWorkflowLinkDO.setCreateTime(currentTime);
            newWorkflowLinkDO.setRemark(commitRemark);
            workflowLinkMapper.save(newWorkflowLinkDO);

            saveWorkflowLinkDetail(newWorkflowLinkDO.getId(), workflowReferNo, (workflowTemplateDO.getWorkflowNodeDOList().size() + 1), loginUser.getUserId(), workflowVerifyUserGroupDO.getVerifyUserGroupId(), currentTime, commitRemark);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    public WorkflowVerifyUserGroupDO saveWorkflowVerifyUserGroup(Integer loginUserId, Date currentTime, String commitRemark) {
        WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        workflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
        workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
        workflowVerifyUserGroupDO.setVerifyUser(loginUserId);
        workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
        workflowVerifyUserGroupDO.setVerifyTime(currentTime);
        workflowVerifyUserGroupDO.setVerifyOpinion(commitRemark);
        workflowVerifyUserGroupDO.setRemark(commitRemark);
        workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowVerifyUserGroupDO.setCreateTime(currentTime);
        workflowVerifyUserGroupDO.setCreateUser(loginUserId.toString());
        workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);
        return workflowVerifyUserGroupDO;
    }

    public void saveWorkflowLinkDetail(Integer workflowLinkId, String workflowReferNo, Integer workflowStep, Integer loginUserId, String verifyUserGroupId, Date currentTime, String commitRemark) {
        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkId);
        workflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDetailDO.setWorkflowStep(workflowStep);
        workflowLinkDetailDO.setVerifyUser(loginUserId);
        workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowLinkDetailDO.setVerifyTime(currentTime);
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
        workflowLinkDetailDO.setVerifyOpinion(commitRemark);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailDO.setCreateUser(loginUserId.toString());
        workflowLinkDetailMapper.save(workflowLinkDetailDO);
    }

    @Override
    public ServiceResult<String, String> workflowImportData() {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
//        超级管理员权限控制
        if (!userSupport.isSuperUser()) {
            result.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return result;
        }

        Date now = new Date();
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("workflowLinkQueryParam", workflowLinkQueryParam);
        List<WorkflowLinkDO> workflowLinkDOList = workflowLinkMapper.listPage(paramMap);

        for (WorkflowLinkDO workflowLinkDO : workflowLinkDOList) {

            for (WorkflowLinkDetailDO workflowLinkDetailDO : workflowLinkDO.getWorkflowLinkDetailDOList()) {
                Integer data = 19;
                if (data.equals(workflowLinkDetailDO.getWorkflowCurrentNodeId())) {
                    CustomerDO customerDO = customerMapper.findByNo(workflowLinkDetailDO.getWorkflowReferNo());

                    List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
                    Map<Integer, Integer> map = new HashMap<>();
                    String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
                    SubCompanyCityCoverDO subCompanyCityCoverDO;
                    //判断收货地址
                    for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
                        subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerConsignInfoDO.getCity());
                        if (subCompanyCityCoverDO == null) {
                            subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerConsignInfoDO.getProvince());
                        }
                        map.put(subCompanyCityCoverDO.getSubCompanyId(), subCompanyCityCoverDO.getSubCompanyId());
                    }

                    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                        UserQueryParam userQueryParam = new UserQueryParam();
                        userQueryParam.setRoleType(100004);
                        userQueryParam.setSubCompanyId(entry.getValue());
                        ServiceResult<String, List<User>> userResult = userService.getUserListByParam(userQueryParam);
                        if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                            for (User user : userResult.getResult()) {
                                WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                                workflowVerifyUserGroupDO.setVerifyUserGroupId(verifyUserGroupId);
                                workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS);
                                workflowVerifyUserGroupDO.setVerifyUser(user.getUserId());
                                workflowVerifyUserGroupDO.setVerifyOpinion(workflowLinkDetailDO.getVerifyOpinion());
                                workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                                workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                                workflowVerifyUserGroupDO.setCreateUser(workflowLinkDetailDO.getCreateUser());
                                workflowVerifyUserGroupDO.setCreateTime(now);
                                workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);
                            }
                        }
                    }
                    workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
                    workflowLinkDetailMapper.update(workflowLinkDetailDO);
                    System.out.println(workflowLinkDetailDO.getId());
                } else {
                    String groupId = generateNoSupport.generateVerifyUserGroupId();
                    WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                    workflowVerifyUserGroupDO.setVerifyUserGroupId(groupId);
                    workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
                    workflowVerifyUserGroupDO.setVerifyUser(workflowLinkDetailDO.getVerifyUser());
                    workflowVerifyUserGroupDO.setVerifyTime(workflowLinkDetailDO.getVerifyTime());
                    workflowVerifyUserGroupDO.setVerifyStatus(workflowLinkDetailDO.getVerifyStatus());
                    workflowVerifyUserGroupDO.setVerifyOpinion(workflowLinkDetailDO.getVerifyOpinion());
                    workflowVerifyUserGroupDO.setRemark(workflowLinkDetailDO.getRemark());
                    workflowVerifyUserGroupDO.setDataStatus(workflowLinkDetailDO.getDataStatus());
                    workflowVerifyUserGroupDO.setCreateTime(now);
                    workflowVerifyUserGroupDO.setUpdateTime(now);
                    workflowVerifyUserGroupDO.setCreateUser(workflowLinkDetailDO.getCreateUser());
                    workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);

                    workflowLinkDetailDO.setVerifyUserGroupId(groupId);
                    workflowLinkDetailMapper.update(workflowLinkDetailDO);
                }
            }
            WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDO.getWorkflowLinkDetailDOList().get(0);
            workflowLinkDO.setVerifyUserGroupId(lastWorkflowLinkDetailDO.getVerifyUserGroupId());
            workflowLinkMapper.update(workflowLinkDO);

        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, List<User>> getNextVerifyUsers(Integer workflowType, String workflowReferNo) {
        ServiceResult<String, List<User>> result = new ServiceResult<>();
        if (workflowType == null
                || StringUtil.isBlank(workflowReferNo)) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        WorkflowNodeDO workflowNodeDO = null;
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowLinkDO == null) {
            if (workflowTemplateDO == null || CollectionUtil.isEmpty(workflowTemplateDO.getWorkflowNodeDOList())) {
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                return result;
            }
            if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(0);
        } else {
            if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowLinkDO.getCurrentVerifyStatus())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
            if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL);
                return result;
            }

            //如果是订单的商务例行审核，则判断是否需要二审
//            if(WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowLinkDO.getWorkflowType())&&workflowLinkDO.getWorkflowStep()==1){
//                ServiceResult<String,Boolean> isNeedSecondVerifyResult = orderService.isNeedSecondVerify(workflowReferNo);
//                if(!ErrorCode.SUCCESS.equals(isNeedSecondVerifyResult.getErrorCode())){
//                    result.setErrorCode(isNeedSecondVerifyResult.getErrorCode());
//                    return result;
//                }
//                if(!isNeedSecondVerifyResult.getResult()){
//                    result.setErrorCode(ErrorCode.SUCCESS);
//                    return result;
//                }
//            }


            WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
            if (VerifyStatus.VERIFY_STATUS_PASS.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            } else if (VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
                // 如果 最后是驳回状态，审核人就要从头来
                if (workflowTemplateDO == null || CollectionUtil.isEmpty(workflowTemplateDO.getWorkflowNodeDOList())) {
                    result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                    return result;
                }
                if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType())) {
                    CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
                    if (customerDO == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                        return result;
                    }
                    if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                        customerDO = customerMapper.findCustomerCompanyByNo(customerDO.getCustomerNo());
                        if (CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())) {
                            result.setErrorCode(ErrorCode.SUCCESS);
                            return result;
                        }
                    }
                    List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findVerifyStatusByCustomerId(customerDO.getId());
                    if (customerConsignInfoDOList.size() == 0) {
                        if (lastWorkflowLinkDetailDO.getWorkflowStep() == 2 || lastWorkflowLinkDetailDO.getWorkflowStep() == 3) {
                            workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(1);
                        } else {
                            result.setErrorCode(ErrorCode.SUCCESS);
                            return result;
                        }
                    } else {
                        result.setErrorCode(ErrorCode.SUCCESS);
                        return result;
                    }
                } else {
                    workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(0);
                }
            } else {
                List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(lastWorkflowLinkDetailDO.getVerifyUserGroupId());
                if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
                    result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS);
                    return result;
                }
                Integer count = 0;
                for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowVerifyUserGroupDOList) {
                    if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                        count++;
                    }
                }
                if (count < workflowVerifyUserGroupDOList.size() - 1) {
                    result.setErrorCode(ErrorCode.SUCCESS);
                    return result;
                }
                workflowNodeDO = workflowNodeMapper.findById(lastWorkflowLinkDetailDO.getWorkflowNextNodeId());
            }
        }
        if (workflowNodeDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_NODE_NOT_EXISTS);
            return result;
        }
        Integer subCompanyId = getSubCompanyId(workflowType, workflowReferNo,workflowNodeDO);
        if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(subCompanyId)) {
            subCompanyId = CommonConstant.HEAD_COMPANY_ID;
        }
        List<User> userList = getUserListByNode(workflowNodeDO, subCompanyId);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userList);
        return result;
    }

    @Override
    public ServiceResult<String, WorkflowLink> getWorkflowLink(String workflowLinkNo) {
        ServiceResult<String, WorkflowLink> result = new ServiceResult<>();
        if (workflowLinkNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }

        if (workflowLinkDO.getVerifyUserGroupId() != null) {
            List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDO.getVerifyUserGroupId());
            if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
                result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS);
                return result;
            }
            workflowLinkDO.setWorkflowVerifyUserGroupDOList(workflowVerifyUserGroupDOList);
            for (WorkflowLinkDetailDO workflowLinkDetailDO : workflowLinkDO.getWorkflowLinkDetailDOList()) {
                if (workflowLinkDetailDO.getVerifyUserGroupId() != null) {
                    workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDetailDO.getVerifyUserGroupId());
                    if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
                        result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS);
                        return result;
                    }
                    workflowLinkDetailDO.setWorkflowVerifyUserGroupDOList(workflowVerifyUserGroupDOList);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(workflowLinkDO.getWorkflowLinkDetailDOList())) {
            for (WorkflowLinkDetailDO workflowLinkDetailDO : workflowLinkDO.getWorkflowLinkDetailDOList()) {
                if (CollectionUtil.isNotEmpty(workflowLinkDetailDO.getWorkflowVerifyUserGroupDOList())) {
                    for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowLinkDetailDO.getWorkflowVerifyUserGroupDOList()) {
                        List<ImageDO> groupImageDOList = imgMysqlMapper.findByRefIdAndType(workflowVerifyUserGroupDO.getId().toString(), ImgType.WORKFLOW_IMG_TYPE);
                        if (CollectionUtil.isNotEmpty(groupImageDOList)) {
                            workflowVerifyUserGroupDO.setImageDOList(groupImageDOList);
                        }
                    }
                }
            }
        }
        result.setResult(ConverterUtil.convert(workflowLinkDO, WorkflowLink.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, WorkflowLink> getWorkflowLink(Integer workflowType, String workflowReferNo) {
        ServiceResult<String, WorkflowLink> result = new ServiceResult<>();
        if (workflowType == null || workflowReferNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }

        if (workflowLinkDO.getVerifyUserGroupId() != null) {
            List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDO.getVerifyUserGroupId());
            if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
                result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS);
                return result;
            }
            workflowLinkDO.setWorkflowVerifyUserGroupDOList(workflowVerifyUserGroupDOList);
            for (WorkflowLinkDetailDO workflowLinkDetailDO : workflowLinkDO.getWorkflowLinkDetailDOList()) {
                if (workflowLinkDetailDO.getVerifyUserGroupId() != null) {
                    workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDetailDO.getVerifyUserGroupId());
                    if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
                        result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS);
                        return result;
                    }
                    workflowLinkDetailDO.setWorkflowVerifyUserGroupDOList(workflowVerifyUserGroupDOList);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(workflowLinkDO.getWorkflowLinkDetailDOList())) {
            for (WorkflowLinkDetailDO workflowLinkDetailDO : workflowLinkDO.getWorkflowLinkDetailDOList()) {
                if (CollectionUtil.isNotEmpty(workflowLinkDetailDO.getWorkflowVerifyUserGroupDOList())) {
                    for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowLinkDetailDO.getWorkflowVerifyUserGroupDOList()) {
                        List<ImageDO> groupImageDOList = imgMysqlMapper.findByRefIdAndType(workflowVerifyUserGroupDO.getId().toString(), ImgType.WORKFLOW_IMG_TYPE);
                        if (CollectionUtil.isNotEmpty(groupImageDOList)) {
                            workflowVerifyUserGroupDO.setImageDOList(groupImageDOList);
                        }
                    }
                }
            }
        }
        result.setResult(ConverterUtil.convert(workflowLinkDO, WorkflowLink.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<WorkflowLink>> getWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Page<WorkflowLink>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowQueryParam", workflowLinkQueryParam);
        //只有创建人与相关审核人查看自己数据
        if (!userSupport.isSuperUser()) {
            paramMap.put("verifyUserId", userSupport.getCurrentUserId().toString());
        }
        List<String> currentUserGroupList = workflowVerifyUserGroupMapper.findGroupUUIDByUserId(userSupport.getCurrentUserId());
        paramMap.put("currentUserGroupList",currentUserGroupList );
        if(workflowLinkQueryParam.getCurrentVerifyUser()!=null){
            List<String> groupList = workflowVerifyUserGroupMapper.findGroupUUIDByUserId(workflowLinkQueryParam.getCurrentVerifyUser());
            paramMap.put("groupIdList",groupList );
        }
        Integer dataCount = workflowLinkMapper.listCount(paramMap);
        List<WorkflowLinkDO> dataList = workflowLinkMapper.listPage(paramMap);
        Page<WorkflowLink> page = new Page<>(ConverterUtil.convertList(dataList, WorkflowLink.class), dataCount, workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());

        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<WorkflowLink>> getCurrentUserWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Page<WorkflowLink>> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        workflowLinkQueryParam.setCurrentVerifyUser(loginUser.getUserId());
        workflowLinkQueryParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        PageQuery pageQuery = new PageQuery(workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowQueryParam", workflowLinkQueryParam);
        paramMap.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

        Integer dataCount = workflowLinkMapper.listCount(paramMap);
        List<WorkflowLinkDO> dataList = workflowLinkMapper.listPage(paramMap);
        Page<WorkflowLink> page = new Page<>(ConverterUtil.convertList(dataList, WorkflowLink.class), dataCount, workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());

        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;

    }

    /**
     * <pre>
     * 将该方法类的相关代码移到verifyWorkFlowFromCore方法中
     * 目的是为了动态传入当前审核人用户编号
     * </pre>
     * @author daiqi
     * @date 2018/4/28 15:12
     * @return
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> verifyWorkFlow(String workflowLinkNo, Integer verifyStatus, Integer returnType, String verifyOpinion, Integer nextVerifyUser, List<Integer> imgIdList) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        return verifyWorkFlowFromCore(workflowLinkNo, verifyStatus, returnType, verifyOpinion, loginUser.getUserId(), nextVerifyUser, imgIdList);
    }

    @Override
    public ServiceResult<String, Integer> verifyWorkFlowFromCore(String workflowLinkNo, Integer verifyStatus, Integer returnType, String verifyOpinion, Integer currentVerifyUser, Integer nextVerifyUser, List<Integer> imgIdList) {
        Date currentTime = new Date();
        ServiceResult<String, Integer> result = new ServiceResult<>();
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }
        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL);
            return result;
        }
        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
        if (VerifyStatus.VERIFY_STATUS_PASS.equals(lastWorkflowLinkDetailDO.getVerifyStatus())
                || VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_VERIFY_ALREADY_OVER);
            return result;
        }

        List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(lastWorkflowLinkDetailDO.getVerifyUserGroupId());
        if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
            result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS);
            return result;
        }
        boolean flag = false;
        for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowVerifyUserGroupDOList) {
            if (currentVerifyUser.equals(workflowVerifyUserGroupDO.getVerifyUser())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            result.setErrorCode(ErrorCode.WORKFLOW_NOT_BELONG_TO_YOU);
            return result;
        }

        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findById(workflowLinkDO.getWorkflowTemplateId());
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS);
            return result;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_HAVE_NO_NODE);
            return result;
        }
        // 是否通知业务模块
        boolean noticeBusinessModule = false;
        // 本步骤节点
        WorkflowNodeDO nextWorkflowNodeDO = null;
        WorkflowNodeDO previousWorkflowNodeDO = null;
        for (WorkflowNodeDO workflowNodeDO : workflowNodeDOList) {
            if (workflowNodeDO.getId().equals(lastWorkflowLinkDetailDO.getWorkflowPreviousNodeId())) {
                previousWorkflowNodeDO = workflowNodeDO;
            }
            if (workflowNodeDO.getId().equals(lastWorkflowLinkDetailDO.getWorkflowNextNodeId())) {
                nextWorkflowNodeDO = workflowNodeDO;
                break;
            }
        }
        Integer passCount = 0;
        boolean flagBoolean = false;
        for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowVerifyUserGroupDOList) {
            if (currentVerifyUser.equals(workflowVerifyUserGroupDO.getVerifyUser()) && VerifyStatus.VERIFY_STATUS_COMMIT.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                workflowVerifyUserGroupDO.setVerifyTime(currentTime);
                workflowVerifyUserGroupDO.setVerifyStatus(verifyStatus);
                workflowVerifyUserGroupDO.setVerifyOpinion(verifyOpinion);
                workflowVerifyUserGroupDO.setRemark(verifyOpinion);
                workflowVerifyUserGroupDO.setUpdateTime(currentTime);
                workflowVerifyUserGroupDO.setUpdateUser(currentVerifyUser.toString());
                workflowVerifyUserGroupMapper.update(workflowVerifyUserGroupDO);
                saveWorkflowImage(workflowVerifyUserGroupDO.getId(), imgIdList, currentTime);

                if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType())) {
                    CustomerDO customerDO = customerMapper.findByNo(workflowLinkDO.getWorkflowReferNo());
                    if (customerDO == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                        return result;
                    }
                    Integer companyId = userSupport.getCurrentUserCompanyId();

                    if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                        customerDO = customerMapper.findCustomerCompanyByNo(customerDO.getCustomerNo());

                        if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())) {
                            SubCompanyCityCoverDO subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerDO.getCustomerCompanyDO().getCity());
                            if (subCompanyCityCoverDO == null) {
                                subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerDO.getCustomerCompanyDO().getProvince());
                                if (subCompanyCityCoverDO == null) {
                                    result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NOT_CITY_AND_PROVINCE_IS_NULL);
                                    return result;
                                }
                            }
                            if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus()) && companyId.equals(subCompanyCityCoverDO.getSubCompanyId())) {
                                customerDO.getCustomerCompanyDO().setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS);
                                customerDO.getCustomerCompanyDO().setUpdateTime(currentTime);
                                customerDO.getCustomerCompanyDO().setUpdateUser(currentVerifyUser.toString());
                                customerCompanyMapper.update(customerDO.getCustomerCompanyDO());
                            }
                        } else if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus())
                                && CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())
                                && userSupport.isRiskManagementPerson()) {
                            customerDO.getCustomerCompanyDO().setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_END_PASS);
                            customerDO.getCustomerCompanyDO().setUpdateTime(currentTime);
                            customerDO.getCustomerCompanyDO().setUpdateUser(currentVerifyUser.toString());
                            customerCompanyMapper.update(customerDO.getCustomerCompanyDO());
                        }
                    } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
                        customerDO = customerMapper.findCustomerPersonByNo(customerDO.getCustomerNo());
                    }
                    List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
                    if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
                        result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
                        return result;
                    }
                    //判断收货地址
                    for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
                        if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerConsignInfoDO.getVerifyStatus())) {
                            SubCompanyCityCoverDO subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerConsignInfoDO.getCity());
                            if (subCompanyCityCoverDO == null) {
                                subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerConsignInfoDO.getProvince());
                                if (subCompanyCityCoverDO == null) {
                                    result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_CITY_AND_PROVINCE_IS_NULL);
                                    return result;
                                }
                            }
                            if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus()) && companyId.equals(subCompanyCityCoverDO.getSubCompanyId())) {
                                customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS);
                                customerConsignInfoDO.setUpdateTime(currentTime);
                                customerConsignInfoDO.setUpdateUser(currentVerifyUser.toString());
                                customerConsignInfoMapper.update(customerConsignInfoDO);
                            }
                        } else if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus())
                                && CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS.equals(customerConsignInfoDO.getVerifyStatus())
                                && userSupport.isRiskManagementPerson()) {
                            customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_END_PASS);
                            customerConsignInfoDO.setUpdateTime(currentTime);
                            customerConsignInfoDO.setUpdateUser(currentVerifyUser.toString());
                            customerConsignInfoMapper.update(customerConsignInfoDO);
                        }
                    }
                } else if (WorkflowType.WORKFLOW_TYPE_CUSTOMER_CONSIGN.equals(workflowLinkDO.getWorkflowType()) && VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                    updateCustomerConsignVerifyStatus(Integer.valueOf(workflowLinkDO.getWorkflowReferNo()), CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS, currentTime, currentVerifyUser);
                }
            }
            if (VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(workflowVerifyUserGroupDO.getVerifyType()) && VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                passCount++;
            } else if (VerifyStatus.VERIFY_STATUS_BACK.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                //在审核中的人员改为取消中
                for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDOIng : workflowVerifyUserGroupDOList) {
                    if (VerifyStatus.VERIFY_STATUS_COMMIT.equals(workflowVerifyUserGroupDOIng.getVerifyStatus())) {
                        workflowVerifyUserGroupDOIng.setVerifyTime(currentTime);
                        workflowVerifyUserGroupDOIng.setVerifyStatus(VerifyStatus.VERIFY_STATUS_CANCEL);
                        workflowVerifyUserGroupDOIng.setUpdateTime(currentTime);
                        workflowVerifyUserGroupDOIng.setUpdateUser(currentVerifyUser.toString());
                        workflowVerifyUserGroupMapper.update(workflowVerifyUserGroupDOIng);
                    }
                    if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType())) {
                        CustomerDO customerDO = customerMapper.findByNo(workflowLinkDO.getWorkflowReferNo());
                        if (customerDO == null) {
                            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                            return result;
                        }
                        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                            customerDO = customerMapper.findCustomerCompanyByNo(customerDO.getCustomerNo());
                            if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())) {
                                customerDO.getCustomerCompanyDO().setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_BACK);
                                customerDO.getCustomerCompanyDO().setUpdateTime(currentTime);
                                customerDO.getCustomerCompanyDO().setUpdateUser(currentVerifyUser.toString());
                                customerCompanyMapper.update(customerDO.getCustomerCompanyDO());
                            }
                        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
                            customerDO = customerMapper.findCustomerPersonByNo(customerDO.getCustomerNo());
                        }
                        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
                        if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
                            result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
                            return result;
                        }
                        for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
                            if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerConsignInfoDO.getVerifyStatus())) {
                                customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_BACK);
                                customerConsignInfoDO.setUpdateTime(currentTime);
                                customerConsignInfoDO.setUpdateUser(currentVerifyUser.toString());
                                customerConsignInfoMapper.update(customerConsignInfoDO);
                            }
                        }
                    }
                }
                flagBoolean = true;
                break;
            }
            if (VerifyType.VERIFY_TYPE_THIS_IS_PASS.equals(workflowVerifyUserGroupDO.getVerifyType())) {
                flagBoolean = true;
                break;
            }
        }
        if (!flagBoolean) {
            if (!passCount.equals(workflowVerifyUserGroupDOList.size())) {
                result.setResult(workflowLinkDO.getId());
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
        }
//        boolean isNeedNextVerify = true;
//        //如果是订单审核，则判断是否需要下一步审核
//        if(WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowLinkDO.getWorkflowType())&&
//                workflowLinkDO.getWorkflowStep()==1){
//            ServiceResult<String,Boolean> isNeedSecondVerifyResult = orderService.isNeedSecondVerify(workflowLinkDO.getWorkflowReferNo());
//            if(!ErrorCode.SUCCESS.equals(isNeedSecondVerifyResult.getErrorCode())){
//                result.setErrorCode(isNeedSecondVerifyResult.getErrorCode());
//                return result;
//            }
//            isNeedNextVerify = isNeedSecondVerifyResult.getResult();
//        }

        // 如果审核通过并且下一步审核不为空的时候，判断下一步的审核人是否正确
        if (
//                isNeedNextVerify&&
                VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus) && nextWorkflowNodeDO != null) {
            Integer subCompanyId = getSubCompanyId(workflowLinkDO.getWorkflowType(), workflowLinkDO.getWorkflowReferNo(),nextWorkflowNodeDO);
            if (!verifyVerifyUsers(nextWorkflowNodeDO, nextVerifyUser, subCompanyId)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 回滚
                result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
                return result;
            }
        }
        lastWorkflowLinkDetailDO.setVerifyStatus(verifyStatus);
        lastWorkflowLinkDetailDO.setVerifyTime(currentTime);
        lastWorkflowLinkDetailDO.setVerifyOpinion(verifyOpinion);
        lastWorkflowLinkDetailDO.setRemark(verifyOpinion);
        lastWorkflowLinkDetailDO.setUpdateUser(currentVerifyUser.toString());
        lastWorkflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailMapper.update(lastWorkflowLinkDetailDO);

        if (VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus)) {

            // 审核通过并且有下一步的情况
            if (
//                    isNeedNextVerify&&
                    nextWorkflowNodeDO != null) {

                WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                workflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
                workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
                workflowVerifyUserGroupDO.setVerifyUser(nextVerifyUser);
                workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowVerifyUserGroupDO.setCreateUser(currentVerifyUser.toString());
                workflowVerifyUserGroupDO.setCreateTime(currentTime);
                workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);

                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
                workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
                workflowLinkDetailDO.setWorkflowReferNo(lastWorkflowLinkDetailDO.getWorkflowReferNo());
                workflowLinkDetailDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
                workflowLinkDetailDO.setWorkflowPreviousNodeId(lastWorkflowLinkDetailDO.getWorkflowCurrentNodeId());
                workflowLinkDetailDO.setWorkflowCurrentNodeId(nextWorkflowNodeDO.getId());
                workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getWorkflowNextNodeId());
                workflowLinkDetailDO.setVerifyUser(nextVerifyUser);
                workflowLinkDetailDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
                workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowLinkDetailDO.setUpdateUser(currentVerifyUser.toString());
                workflowLinkDetailDO.setCreateUser(currentVerifyUser.toString());
                workflowLinkDetailDO.setUpdateTime(currentTime);
                workflowLinkDetailDO.setCreateTime(currentTime);
                workflowLinkDetailMapper.save(workflowLinkDetailDO);
                workflowLinkDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
                workflowLinkDO.setWorkflowCurrentNodeId(nextWorkflowNodeDO.getId());
                workflowLinkDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
                messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), nextVerifyUser);
            } else {
                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
                noticeBusinessModule = true;
            }
            if (nextVerifyUser != null) {
                workflowLinkDO.setCurrentVerifyUser(nextVerifyUser);
            }
        } else {
            workflowLinkDO.setCurrentVerifyUser(null);
            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
            // 拒绝并且一步驳回到底
            if (returnType == null || WorkflowReturnType.RETURN_TYPE_ROOT.equals(returnType)) {
                noticeBusinessModule = true;
                workflowLinkDO.setWorkflowStep(0);
                workflowLinkDO.setCurrentVerifyUser(CommonConstant.SUPER_USER_ID);
            } else if (previousWorkflowNodeDO != null) {
                if ((WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) && userSupport.isRiskManagementPerson())) {
                    // 如果是客户和地址并且是风控审核就驳回，那么就相当于驳回到根部
                    noticeBusinessModule = true;
                    workflowLinkDO.setWorkflowStep(0);
                    workflowLinkDO.setCurrentVerifyUser(CommonConstant.SUPER_USER_ID);
                } else {
                    WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
                    List<Integer> userIdList = new ArrayList<>();
                    if (workflowLinkDetailDOList.size() > 1) {
                        WorkflowLinkDetailDO previousWorkflowLinkDetailDO = workflowLinkDetailDOList.get(1);
                        List<WorkflowVerifyUserGroupDO> newWorkflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(previousWorkflowLinkDetailDO.getVerifyUserGroupId());
                        String groupId = generateNoSupport.generateVerifyUserGroupId();
                        for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : newWorkflowVerifyUserGroupDOList) {
                            WorkflowVerifyUserGroupDO newWorkflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                            newWorkflowVerifyUserGroupDO.setVerifyUserGroupId(groupId);
                            if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType())) {
                                newWorkflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS);
                            } else {
                                newWorkflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
                            }
                            newWorkflowVerifyUserGroupDO.setVerifyUser(workflowVerifyUserGroupDO.getVerifyUser());
                            newWorkflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                            newWorkflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                            newWorkflowVerifyUserGroupDO.setCreateUser(currentVerifyUser.toString());
                            newWorkflowVerifyUserGroupDO.setCreateTime(currentTime);
                            workflowVerifyUserGroupMapper.save(newWorkflowVerifyUserGroupDO);
                            userIdList.add(workflowVerifyUserGroupDO.getVerifyUser());

                        }
                        workflowLinkDetailDO.setVerifyUserGroupId(groupId);
//                    workflowLinkDetailDO.setVerifyUser(previousWorkflowLinkDetailDO.getVerifyUser());
//                    workflowLinkDO.setCurrentVerifyUser(previousWorkflowLinkDetailDO.getVerifyUser());
                        workflowLinkDO.setVerifyUserGroupId(groupId);
                        workflowLinkDO.setWorkflowCurrentNodeId(previousWorkflowNodeDO.getId());
                        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                    }
                    workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
                    workflowLinkDetailDO.setWorkflowReferNo(lastWorkflowLinkDetailDO.getWorkflowReferNo());
                    workflowLinkDetailDO.setWorkflowStep(previousWorkflowNodeDO.getWorkflowStep());
                    workflowLinkDetailDO.setWorkflowPreviousNodeId(previousWorkflowNodeDO.getWorkflowPreviousNodeId());
                    workflowLinkDetailDO.setWorkflowCurrentNodeId(previousWorkflowNodeDO.getId());
                    workflowLinkDetailDO.setWorkflowNextNodeId(previousWorkflowNodeDO.getWorkflowNextNodeId());
                    workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                    workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    workflowLinkDetailDO.setCreateUser(currentVerifyUser.toString());
                    workflowLinkDetailDO.setCreateTime(currentTime);
                    workflowLinkDetailMapper.save(workflowLinkDetailDO);
                    workflowLinkDO.setWorkflowStep(previousWorkflowNodeDO.getWorkflowStep());
                    for (Integer userId : userIdList) {
                        messageService.superSendMessage(MessageContant.WORKFLOW_VERIFY_BACK_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), userId);
                    }
                }
            } else {
                // 如果第一步就驳回了，那么就相当于驳回到根部
                noticeBusinessModule = true;
                workflowLinkDO.setWorkflowStep(0);
                workflowLinkDO.setCurrentVerifyUser(CommonConstant.SUPER_USER_ID);
            }
        }

        if (noticeBusinessModule) {
            // 根据不同业务，回调业务系统
            VerifyReceiver verifyReceiver = workFlowManager.getService(workflowLinkDO.getWorkflowType());
            if (verifyReceiver == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 回滚
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                return result;
            }
            String code = verifyReceiver.receiveVerifyResult(VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus), workflowLinkDO.getWorkflowReferNo());
            if (!ErrorCode.SUCCESS.equals(code)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 回滚
                result.setErrorCode(code);
                return result;
            }
        }
        workflowLinkDO.setUpdateUser(currentVerifyUser.toString());
        workflowLinkDO.setUpdateTime(currentTime);
        workflowLinkMapper.update(workflowLinkDO);

        result.setResult(workflowLinkDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Boolean> isNeedVerify(Integer workflowType) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        if (workflowType == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }

        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowTemplateDO == null || CollectionUtil.isEmpty(workflowTemplateDO.getWorkflowNodeDOList())) {
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(Boolean.FALSE);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(Boolean.TRUE);
        return result;
    }

    /**
     * 生成工作流线，只适用于首次创建
     */
    private String generateWorkflowLink(WorkflowTemplateDO workflowTemplateDO, String workflowReferNo, String commitRemark, Integer verifyUser, String verifyMatters, List<Integer> imgIdList, Date currentTime, String orderRemark) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        if (workflowTemplateDO == null) {
            return null;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            return null;
        }
        WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
        WorkflowNodeDO lastWorkflowNodeDO = workflowNodeDOList.get(workflowNodeDOList.size() - 1);

        //生成审核组id
        WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        workflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
        workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
        workflowVerifyUserGroupDO.setVerifyUser(verifyUser);
        workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowVerifyUserGroupDO.setCreateUser(loginUser.getUserId().toString());
        workflowVerifyUserGroupDO.setCreateTime(currentTime);
        workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);

        WorkflowLinkDO workflowLinkDO = new WorkflowLinkDO();
        workflowLinkDO.setWorkflowLinkNo(generateNoSupport.generateWorkflowLinkNo(currentTime, loginUser.getUserId()));
        workflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
        workflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
        workflowLinkDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        workflowLinkDO.setCommitUser(loginUser.getUserId());
        workflowLinkDO.setCurrentVerifyUser(verifyUser);
        workflowLinkDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDO.setVerifyMatters(verifyMatters);
        workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDO.setCreateTime(currentTime);
        workflowLinkDO.setRemark(orderRemark);
        workflowLinkMapper.save(workflowLinkDO);

        // 生成提交人工作流
        WorkflowVerifyUserGroupDO commitWorkflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        commitWorkflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
        commitWorkflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
        commitWorkflowVerifyUserGroupDO.setVerifyUser(loginUser.getUserId());
        commitWorkflowVerifyUserGroupDO.setVerifyTime(currentTime);
        commitWorkflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowVerifyUserGroupDO.setVerifyOpinion(commitRemark);
        commitWorkflowVerifyUserGroupDO.setRemark(commitRemark);
        commitWorkflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowVerifyUserGroupDO.setCreateUser(loginUser.getUserId().toString());
        commitWorkflowVerifyUserGroupDO.setCreateTime(currentTime);
        workflowVerifyUserGroupMapper.save(commitWorkflowVerifyUserGroupDO);

        saveWorkflowImage(commitWorkflowVerifyUserGroupDO.getId(), imgIdList, currentTime);

        WorkflowLinkDetailDO commitWorkflowLinkDetailDO = new WorkflowLinkDetailDO();
        commitWorkflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        commitWorkflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        commitWorkflowLinkDetailDO.setWorkflowCurrentNodeId(0);
        commitWorkflowLinkDetailDO.setWorkflowStep(0);
        commitWorkflowLinkDetailDO.setWorkflowNextNodeId(thisWorkflowNodeDO.getId());
        commitWorkflowLinkDetailDO.setVerifyUser(loginUser.getUserId());
        commitWorkflowLinkDetailDO.setVerifyUserGroupId(commitWorkflowVerifyUserGroupDO.getVerifyUserGroupId());
        commitWorkflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowLinkDetailDO.setVerifyOpinion(commitRemark);
        commitWorkflowLinkDetailDO.setRemark(commitRemark);
        commitWorkflowLinkDetailDO.setVerifyTime(currentTime);
        commitWorkflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        commitWorkflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(commitWorkflowLinkDetailDO);

        // 生成审批人工作流
        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        if (workflowNodeDOList.size() > 1) {
            workflowLinkDetailDO.setWorkflowNextNodeId(workflowNodeDOList.get(1).getId());
        }
        workflowLinkDetailDO.setVerifyUser(verifyUser);
        workflowLinkDetailDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        return workflowLinkDO.getWorkflowLinkNo();
    }

    /**
     * 继续工作流
     */
    private String continueWorkflowLink(WorkflowLinkDO workflowLinkDO, String commitRemark, Integer verifyUser, String verifyMatters, List<Integer> imgIdList, Date currentTime, String orderRemark) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        if (workflowLinkDO == null) {
            return ErrorCode.WORKFLOW_LINK_NOT_EXISTS;
        }
        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            return ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL;
        }

        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
        if (!VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
            return ErrorCode.WORKFLOW_LINK_STATUS_ERROR;
        }
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findById(workflowLinkDO.getWorkflowTemplateId());
        if (workflowTemplateDO == null) {
            return ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            return ErrorCode.WORKFLOW_TEMPLATE_HAVE_NO_NODE;
        }

        //生成审核组id
        WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        workflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
        workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
        workflowVerifyUserGroupDO.setVerifyUser(verifyUser);
        workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowVerifyUserGroupDO.setCreateUser(loginUser.getUserId().toString());
        workflowVerifyUserGroupDO.setCreateTime(currentTime);
        workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);

        WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
        WorkflowNodeDO lastWorkflowNodeDO = workflowNodeDOList.get(workflowNodeDOList.size() - 1);
        workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        workflowLinkDO.setCurrentVerifyUser(verifyUser);
        workflowLinkDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDO.setVerifyMatters(verifyMatters);
        workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDO.setUpdateTime(currentTime);
        workflowLinkDO.setRemark(orderRemark);
        workflowLinkMapper.update(workflowLinkDO);

        // 生成提交人工作流

        WorkflowVerifyUserGroupDO commitWorkflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        commitWorkflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
        commitWorkflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
        commitWorkflowVerifyUserGroupDO.setVerifyUser(loginUser.getUserId());
        commitWorkflowVerifyUserGroupDO.setVerifyTime(currentTime);
        commitWorkflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowVerifyUserGroupDO.setVerifyOpinion(commitRemark);
        commitWorkflowVerifyUserGroupDO.setRemark(commitRemark);
        commitWorkflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowVerifyUserGroupDO.setCreateUser(loginUser.getUserId().toString());
        commitWorkflowVerifyUserGroupDO.setCreateTime(currentTime);
        workflowVerifyUserGroupMapper.save(commitWorkflowVerifyUserGroupDO);

        saveWorkflowImage(commitWorkflowVerifyUserGroupDO.getId(), imgIdList, currentTime);

        WorkflowLinkDetailDO commitWorkflowLinkDetailDO = new WorkflowLinkDetailDO();
        commitWorkflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        commitWorkflowLinkDetailDO.setWorkflowReferNo(workflowLinkDO.getWorkflowReferNo());
        commitWorkflowLinkDetailDO.setWorkflowCurrentNodeId(0);
        commitWorkflowLinkDetailDO.setWorkflowStep(0);
        commitWorkflowLinkDetailDO.setWorkflowNextNodeId(thisWorkflowNodeDO.getId());
        commitWorkflowLinkDetailDO.setVerifyUser(loginUser.getUserId());
        commitWorkflowLinkDetailDO.setVerifyUserGroupId(commitWorkflowVerifyUserGroupDO.getVerifyUserGroupId());
        commitWorkflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowLinkDetailDO.setVerifyOpinion(commitRemark);
        commitWorkflowLinkDetailDO.setRemark(commitRemark);
        commitWorkflowLinkDetailDO.setVerifyTime(currentTime);
        commitWorkflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        commitWorkflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(commitWorkflowLinkDetailDO);

        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferNo(lastWorkflowLinkDetailDO.getWorkflowReferNo());
        workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        if (workflowNodeDOList.size() > 1) {
            workflowLinkDetailDO.setWorkflowNextNodeId(workflowNodeDOList.get(1).getId());
        }
        workflowLinkDetailDO.setVerifyUser(verifyUser);
        workflowLinkDetailDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        return ErrorCode.SUCCESS;
    }

    private boolean verifyVerifyUsers(WorkflowNodeDO workflowNodeDO, Integer userId, Integer subCompanyId) {
        List<User> userList = getUserListByNode(workflowNodeDO, subCompanyId);
        if (CollectionUtil.isNotEmpty(userList)) {
            Map<Integer, User> userMap = ListUtil.listToMap(userList, "userId");
            if (userMap.containsKey(userId)) {
                return true;
            }
        }
        return false;
    }

    private List<User> getUserListByNode(WorkflowNodeDO workflowNodeDO, Integer subCompanyId) {
        List<User> userList = new ArrayList<>();
        if (workflowNodeDO == null) {
            return userList;
        }
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO);
        if (workflowNodeDO.getWorkflowUser() != null) {
            ServiceResult<String, User> userResult = userService.getUserById(workflowNodeDO.getWorkflowUser());
            if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                userList.add(userResult.getResult());
            }
        } else if (workflowNodeDO.getWorkflowRoleType() != null) {
            userQueryParam.setRoleType(workflowNodeDO.getWorkflowRoleType());
            userQueryParam.setSubCompanyId(subCompanyId);
            ServiceResult<String, List<User>> userResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                userList = userResult.getResult();
            }
        } else if (workflowNodeDO.getWorkflowRole() != null) {
            userQueryParam.setRoleId(workflowNodeDO.getWorkflowRole());
            ServiceResult<String, List<User>> userResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                userList = userResult.getResult();
            }
        } else if (workflowNodeDO.getWorkflowDepartment() != null) {
            userQueryParam.setDepartmentId(workflowNodeDO.getWorkflowDepartment());
            ServiceResult<String, List<User>> departmentUserResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(departmentUserResult.getErrorCode())) {
                userList = departmentUserResult.getResult();
            }
        } else if (workflowNodeDO.getWorkflowDepartmentType() != null) {
            List<Integer> subCompanyIdList = new ArrayList<>();
            subCompanyIdList.add(subCompanyId);

            userQueryParam.setDepartmentType(workflowNodeDO.getWorkflowDepartmentType());
            userQueryParam.setSubCompanyIdList(subCompanyIdList);
            ServiceResult<String, List<User>> subCompanyUserResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(subCompanyUserResult.getErrorCode())) {
                userList = subCompanyUserResult.getResult();
            }
        }
        return userList;
    }

    @Override
    public ServiceResult<String, WorkflowTemplate> findWorkflowTemplate(Integer workflowTemplateId) {
        ServiceResult<String, WorkflowTemplate> result = new ServiceResult<>();
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowTemplateId(workflowTemplateId);
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(ConverterUtil.convert(workflowTemplateDO, WorkflowTemplate.class));
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> updateWorkflowNodeList(WorkflowTemplate workflowTemplate) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date currentTime = new Date();
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowTemplateId(workflowTemplate.getWorkflowTemplateId());

        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS);
            return result;
        }
        //删除原节点列表
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (CollectionUtil.isNotEmpty(workflowNodeDOList)) {
            //删除
            for (WorkflowNodeDO workflowNodeDO : workflowNodeDOList) {
                workflowNodeDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                workflowNodeDO.setUpdateTime(currentTime);
                workflowNodeDO.setUpdateUser(currentUserId);
                workflowNodeMapper.update(workflowNodeDO);
            }
        }
        List<WorkflowNode> workflowNodeList = workflowTemplate.getWorkflowNodeList();
        //构建新节点列表
        if (CollectionUtil.isNotEmpty(workflowNodeList)) {
            //构建
            Integer previousNodeId = null;
            LinkedList<WorkflowNodeDO> linkedList = new LinkedList();
            for (int i = 0; i < workflowNodeList.size(); i++) {
                WorkflowNode workflowNode = workflowNodeList.get(i);
                WorkflowNodeDO workflowNodeDO = ConverterUtil.convert(workflowNode, WorkflowNodeDO.class);

                Integer workflowDepartmentTypeId = workflowNodeDO.getWorkflowDepartmentType();
                if (workflowDepartmentTypeId != null) {
                    DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findByDictionaryId(workflowDepartmentTypeId);
                    if (dataDictionaryDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
                        return result;
                    }
                }
                Integer workflowDepartmentId = workflowNodeDO.getWorkflowDepartment();
                if (workflowDepartmentId != null) {
                    DepartmentDO departmentDO = departmentMapper.findById(workflowDepartmentId);
                    if (departmentDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
                        return result;
                    }
                }
                Integer workflowRoleId = workflowNodeDO.getWorkflowRole();
                if (workflowRoleId != null) {
                    RoleDO roleDO = roleMapper.findById(workflowRoleId);
                    if (roleDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.setErrorCode(ErrorCode.ROLE_NOT_NULL);
                        return result;
                    }
                }
                Integer workflowUserId = workflowNodeDO.getWorkflowUser();
                if (workflowUserId != null) {
                    UserDO userDO = userMapper.findByUserId(workflowUserId);
                    if (userDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
                        return result;
                    }
                }

                workflowNodeDO.setWorkflowTemplateId(workflowTemplate.getWorkflowTemplateId());
                workflowNodeDO.setWorkflowStep(i + 1);
                workflowNodeDO.setWorkflowPreviousNodeId(previousNodeId);
                workflowNodeDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowNodeDO.setCreateTime(currentTime);
                workflowNodeDO.setCreateUser(currentUserId);
                workflowNodeDO.setUpdateTime(currentTime);
                workflowNodeDO.setUpdateUser(currentUserId);
                workflowNodeMapper.save(workflowNodeDO);
                previousNodeId = workflowNodeDO.getId();

                if (CollectionUtil.isNotEmpty(linkedList)) {
                    WorkflowNodeDO lastWorkflowNodeDO = linkedList.getLast();
                    lastWorkflowNodeDO.setWorkflowNextNodeId(workflowNodeDO.getId());
                    workflowNodeMapper.update(lastWorkflowNodeDO);
                }
                linkedList.add(workflowNodeDO);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowTemplateDO.getId());
        return result;
    }

    @Override
    public ServiceResult<String, Page<WorkflowTemplate>> pageWorkflowTemplate(WorkflowTemplateQueryParam workflowTemplateQueryParam) {
        ServiceResult<String, Page<WorkflowTemplate>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(workflowTemplateQueryParam.getPageNo(), workflowTemplateQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowTemplateQueryParam", workflowTemplateQueryParam);
        Integer totalCount = workflowTemplateMapper.listCount(paramMap);
        List<WorkflowTemplateDO> workflowTemplateDOList = workflowTemplateMapper.listPage(paramMap);
        List<WorkflowTemplate> list = ConverterUtil.convertList(workflowTemplateDOList, WorkflowTemplate.class);
        Page<WorkflowTemplate> page = new Page<>(list, totalCount, workflowTemplateQueryParam.getPageNo(), workflowTemplateQueryParam.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    public ServiceResult<String, String> customerCommitWorkFlow(Integer workflowType, String workflowReferNo, Integer verifyUser, String verifyMatters, String commitRemark, List<Integer> imgIdList, String orderRemark, Date currentTime, List<WorkflowNodeDO> workflowNodeDOList, WorkflowTemplateDO workflowTemplateDO) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();

        CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(workflowReferNo);
        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerPersonByNo(workflowReferNo);
        }

        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
        if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
            result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
            return result;
        }
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> verifyUserList = new ArrayList<>();
        String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();

        SubCompanyCityCoverDO subCompanyCityCoverDO;
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            if (CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())
                    || CustomerConsignVerifyStatus.VERIFY_STATUS_BACK.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())) {
                //判断经营地址
                subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerDO.getCustomerCompanyDO().getCity());
                if (subCompanyCityCoverDO == null) {
                    subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerDO.getCustomerCompanyDO().getProvince());
                    if (subCompanyCityCoverDO == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_COMPANY_NOT_CITY_AND_PROVINCE_IS_NULL);
                        return result;
                    }
                }
                map.put(subCompanyCityCoverDO.getSubCompanyId(), subCompanyCityCoverDO.getSubCompanyId());

                customerDO.getCustomerCompanyDO().setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT);
                customerDO.getCustomerCompanyDO().setUpdateTime(currentTime);
                customerDO.getCustomerCompanyDO().setUpdateUser(loginUser.getUserId().toString());
                customerCompanyMapper.update(customerDO.getCustomerCompanyDO());
            }
        }

        //判断收货地址
        for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
            if (CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING.equals(customerConsignInfoDO.getVerifyStatus())
                    || CustomerConsignVerifyStatus.VERIFY_STATUS_BACK.equals(customerConsignInfoDO.getVerifyStatus())) {
                subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerConsignInfoDO.getCity());
                if (subCompanyCityCoverDO == null) {
                    subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerConsignInfoDO.getProvince());
                    if (subCompanyCityCoverDO == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_INFO_NOT_CITY_AND_PROVINCE_IS_NULL);
                        return result;
                    }
                }
                map.put(subCompanyCityCoverDO.getSubCompanyId(), subCompanyCityCoverDO.getSubCompanyId());

                customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT);
                customerConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
                customerConsignInfoDO.setUpdateTime(currentTime);
                customerConsignInfoDO.setWorkflowType(WorkflowType.WORKFLOW_TYPE_CUSTOMER);
                customerConsignInfoMapper.update(customerConsignInfoDO);
            }
        }
        String workflowLinkNo = null;
        if (map.size() > 0) {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                UserQueryParam userQueryParam = new UserQueryParam();
                userQueryParam.setRoleType(workflowNodeDOList.get(0).getWorkflowRoleType());
                userQueryParam.setSubCompanyId(entry.getValue());
                userQueryParam.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO);
                ServiceResult<String, List<User>> userResult = userService.getUserListByParam(userQueryParam);
                if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                    if (CollectionUtil.isEmpty(userResult.getResult())) {
                        result.setErrorCode(ErrorCode.WORKFLOW_CONFIG_ERROR);
                        return result;
                    }
                    for (User user : userResult.getResult()) {
                        WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                        workflowVerifyUserGroupDO.setVerifyUserGroupId(verifyUserGroupId);
                        workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS);
                        workflowVerifyUserGroupDO.setVerifyUser(user.getUserId());
                        workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                        workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        workflowVerifyUserGroupDO.setCreateUser(loginUser.getUserId().toString());
                        workflowVerifyUserGroupDO.setCreateTime(currentTime);
                        workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);
                        verifyUserList.add(user.getUserId());
                    }
                }
            }

            WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);

            WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
            WorkflowNodeDO lastWorkflowNodeDO = workflowNodeDOList.get(workflowNodeDOList.size() - 1);

            if (workflowLinkDO == null) {
                WorkflowLinkDO newWorkflowLinkDO = new WorkflowLinkDO();
                newWorkflowLinkDO.setWorkflowLinkNo(generateNoSupport.generateWorkflowLinkNo(currentTime, loginUser.getUserId()));
                newWorkflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
                newWorkflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
                newWorkflowLinkDO.setWorkflowReferNo(workflowReferNo);
                newWorkflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
                newWorkflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
                newWorkflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
                newWorkflowLinkDO.setCommitUser(loginUser.getUserId());
                newWorkflowLinkDO.setCurrentVerifyUser(verifyUser);
                newWorkflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
                newWorkflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                newWorkflowLinkDO.setVerifyMatters(verifyMatters);
                newWorkflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                newWorkflowLinkDO.setCreateUser(loginUser.getUserId().toString());
                newWorkflowLinkDO.setCreateTime(currentTime);
                newWorkflowLinkDO.setRemark(orderRemark);
                workflowLinkMapper.save(newWorkflowLinkDO);

                saveWorkflowLink(newWorkflowLinkDO, workflowReferNo, thisWorkflowNodeDO, loginUser.getUserId(), commitRemark, currentTime, imgIdList, verifyUser, verifyUserList, workflowNodeDOList, verifyUserGroupId);
                workflowLinkNo = newWorkflowLinkDO.getWorkflowLinkNo();

            } else {
                List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
                if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
                    result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                    return result;
                }

                WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
                if (!VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
                    result.setErrorCode(ErrorCode.WORKFLOW_LINK_STATUS_ERROR);
                    return result;
                }

                workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
                workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
                workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
                workflowLinkDO.setCurrentVerifyUser(verifyUser);
                workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                workflowLinkDO.setVerifyMatters(verifyMatters);
                workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
                workflowLinkDO.setUpdateTime(currentTime);
                workflowLinkDO.setRemark(orderRemark);
                workflowLinkMapper.update(workflowLinkDO);

                saveWorkflowLink(workflowLinkDO, workflowReferNo, thisWorkflowNodeDO, loginUser.getUserId(), commitRemark, currentTime, imgIdList, verifyUser, verifyUserList, workflowNodeDOList, verifyUserGroupId);
                workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
            }
        } else {
            Integer subCompanyId = getSubCompanyId(workflowType, workflowReferNo,workflowNodeDOList.get(1));
            if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(subCompanyId)) {
                subCompanyId = CommonConstant.HEAD_COMPANY_ID;
            }
            WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(1);
            if (!verifyVerifyUsers(thisWorkflowNodeDO, verifyUser, subCompanyId)) {
                result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
                return result;
            }

            WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);

            //生成审核组id
            WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
            workflowVerifyUserGroupDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
            workflowVerifyUserGroupDO.setVerifyUser(verifyUser);
            workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowVerifyUserGroupDO.setCreateUser(loginUser.getUserId().toString());
            workflowVerifyUserGroupDO.setCreateTime(currentTime);
            workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);

            workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
            workflowLinkDO.setWorkflowLastStep(thisWorkflowNodeDO.getWorkflowStep());
            workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
            workflowLinkDO.setCurrentVerifyUser(verifyUser);
            workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkDO.setVerifyMatters(verifyMatters);
            workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
            workflowLinkDO.setUpdateTime(currentTime);
            workflowLinkDO.setRemark(orderRemark);
            workflowLinkMapper.update(workflowLinkDO);

            // 生成提交人工作流
            WorkflowVerifyUserGroupDO commitWorkflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
            commitWorkflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
            commitWorkflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
            commitWorkflowVerifyUserGroupDO.setVerifyUser(loginUser.getUserId());
            commitWorkflowVerifyUserGroupDO.setVerifyTime(currentTime);
            commitWorkflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
            commitWorkflowVerifyUserGroupDO.setVerifyOpinion(commitRemark);
            commitWorkflowVerifyUserGroupDO.setRemark(commitRemark);
            commitWorkflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            commitWorkflowVerifyUserGroupDO.setCreateUser(loginUser.getUserId().toString());
            commitWorkflowVerifyUserGroupDO.setCreateTime(currentTime);
            workflowVerifyUserGroupMapper.save(commitWorkflowVerifyUserGroupDO);

            saveWorkflowImage(commitWorkflowVerifyUserGroupDO.getId(), imgIdList, currentTime);

            WorkflowLinkDetailDO commitWorkflowLinkDetailDO = new WorkflowLinkDetailDO();
            commitWorkflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
            commitWorkflowLinkDetailDO.setWorkflowReferNo(workflowLinkDO.getWorkflowReferNo());
            commitWorkflowLinkDetailDO.setWorkflowCurrentNodeId(0);
            commitWorkflowLinkDetailDO.setWorkflowStep(0);
            commitWorkflowLinkDetailDO.setWorkflowNextNodeId(thisWorkflowNodeDO.getId());
            commitWorkflowLinkDetailDO.setVerifyUser(loginUser.getUserId());
            commitWorkflowLinkDetailDO.setVerifyUserGroupId(commitWorkflowVerifyUserGroupDO.getVerifyUserGroupId());
            commitWorkflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
            commitWorkflowLinkDetailDO.setVerifyOpinion(commitRemark);
            commitWorkflowLinkDetailDO.setRemark(commitRemark);
            commitWorkflowLinkDetailDO.setVerifyTime(currentTime);
            commitWorkflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            commitWorkflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
            commitWorkflowLinkDetailDO.setCreateTime(currentTime);
            workflowLinkDetailMapper.save(commitWorkflowLinkDetailDO);

            WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
            workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
            workflowLinkDetailDO.setWorkflowReferNo(workflowLinkDO.getWorkflowReferNo());
            workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
            workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
            workflowLinkDetailDO.setWorkflowPreviousNodeId(0);
            workflowLinkDetailDO.setVerifyUser(verifyUser);
            workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
            workflowLinkDetailDO.setCreateTime(currentTime);
            workflowLinkDetailMapper.save(workflowLinkDetailDO);

            messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), verifyUser);
            workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkNo);
        return result;
    }

    private void saveWorkflowLink(WorkflowLinkDO workflowLinkDO, String workflowReferNo, WorkflowNodeDO thisWorkflowNodeDO, Integer loginUserId, String commitRemark, Date currentTime, List<Integer> imgIdList, Integer verifyUser, List<Integer> verifyUserList, List<WorkflowNodeDO> workflowNodeDOList, String verifyUserGroupId) {
        // 生成提交人工作流
        WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        workflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
        workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
        workflowVerifyUserGroupDO.setVerifyUser(loginUserId);
        workflowVerifyUserGroupDO.setVerifyTime(currentTime);
        workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        workflowVerifyUserGroupDO.setVerifyOpinion(commitRemark);
        workflowVerifyUserGroupDO.setRemark(commitRemark);
        workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowVerifyUserGroupDO.setCreateUser(loginUserId.toString());
        workflowVerifyUserGroupDO.setCreateTime(currentTime);
        workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);

        saveWorkflowImage(workflowVerifyUserGroupDO.getId(), imgIdList, currentTime);

        WorkflowLinkDetailDO commitWorkflowLinkDetailDO = new WorkflowLinkDetailDO();
        commitWorkflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        commitWorkflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        commitWorkflowLinkDetailDO.setWorkflowCurrentNodeId(0);
        commitWorkflowLinkDetailDO.setWorkflowStep(0);
        commitWorkflowLinkDetailDO.setWorkflowNextNodeId(thisWorkflowNodeDO.getId());
        commitWorkflowLinkDetailDO.setVerifyUser(loginUserId);
        commitWorkflowLinkDetailDO.setVerifyUserGroupId(workflowVerifyUserGroupDO.getVerifyUserGroupId());
        commitWorkflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowLinkDetailDO.setVerifyOpinion(commitRemark);
        commitWorkflowLinkDetailDO.setRemark(commitRemark);
        commitWorkflowLinkDetailDO.setVerifyTime(currentTime);
        commitWorkflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowLinkDetailDO.setCreateUser(loginUserId.toString());
        commitWorkflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(commitWorkflowLinkDetailDO);

        // 生成审批人工作流
        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        if (workflowNodeDOList.size() > 1) {
            WorkflowNodeDO nextWorkflowNodeDO = workflowNodeDOList.get(1);
            workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getId());
        }
        workflowLinkDetailDO.setVerifyUser(verifyUser);
        workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setCreateUser(loginUserId.toString());
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        if (CollectionUtil.isNotEmpty(verifyUserList)) {
            for (Integer userId : verifyUserList) {
                messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), userId);
            }
        }
    }

    private String updateCustomerConsignVerifyStatus(Integer customerConsignId, Integer verifyStatus, Date currentTime, Integer loginUserId) {

        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignId);
        if (customerConsignInfoDO == null) {
            return ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS;
        }
        customerConsignInfoDO.setVerifyStatus(verifyStatus);
        customerConsignInfoDO.setUpdateTime(currentTime);
        customerConsignInfoDO.setUpdateUser(loginUserId.toString());
        customerConsignInfoMapper.update(customerConsignInfoDO);

        return ErrorCode.SUCCESS;
    }
}
