package com.lxzl.erp.core.service.workflow.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyCityCoverMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder.DeploymentOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ChangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.reletorder.ReletOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.replace.ReplaceOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.ImgMysqlMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowVerifyUserGroupMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyCityCoverDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.K3ChangeOrderDO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.replace.ReplaceOrderDO;
import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.erp.dataaccess.domain.workflow.*;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WorkflowSupport {

    /**
     * 保存审核图片
     */
    public void saveWorkflowImage(Integer workflowDetailId, List<Integer> imgIdList, Date currentTime) {
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

    /**
     * 获取分公司id
     */
    public Integer getSubCompanyId(Integer workflowType, String workflowReferNo, WorkflowNodeDO workflowNodeDO) {
        Integer subCompanyId = -1;
        if (WorkflowType.WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO.equals(workflowType)) {
            DeploymentOrderDO deploymentOrderDO = deploymentOrderMapper.findByNo(workflowReferNo);
            WarehouseDO warehouseDO = warehouseMapper.findById(deploymentOrderDO.getTargetWarehouseId());
            if (warehouseDO != null) {
                subCompanyId = warehouseDO.getSubCompanyId();
            }
        } else if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType) ||
                WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowType)) {
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
        } else if (WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowType)||WorkflowType.WORKFLOW_TYPE_EXCHANGE_ORDER.equals(workflowType)) {
            if (CommonConstant.WORKFLOW_STEP_TWO.equals(workflowNodeDO.getWorkflowStep())) {
                OrderDO orderDO = orderMapper.findByOrderNo(workflowReferNo);
                if (orderDO != null) {
                    subCompanyId = orderDO.getDeliverySubCompanyId();
                }
            } else {
                subCompanyId = userSupport.getCurrentUserCompanyId();
            }

        } else if (WorkflowType.WORKFLOW_TYPE_CHANGE.equals(workflowType)) {
            if (CommonConstant.WORKFLOW_STEP_TWO.equals(workflowNodeDO.getWorkflowStep())) {
                ReplaceOrderDO replaceOrderDO = replaceOrderMapper.findByReplaceOrderNo(workflowReferNo);
                if (replaceOrderDO != null) {
                    subCompanyId = replaceOrderDO.getDeliverySubCompanyId();
                }
            } else {
                subCompanyId = userSupport.getCurrentUserCompanyId();
            }
            //todo 等待商城确定方案
        } else if (WorkflowType.WORKFLOW_TYPE_MALL_ORDER.equals(workflowType)) {
            //商城订单-暂定原逻辑
            OrderDO orderDO = orderMapper.findByOrderNo(workflowReferNo);
            if (CommonConstant.WORKFLOW_STEP_TWO.equals(workflowNodeDO.getWorkflowStep())) {
                if (orderDO != null) subCompanyId = orderDO.getDeliverySubCompanyId();
            } else {
                if (orderDO != null) subCompanyId = orderDO.getOrderSubCompanyId();
            }
        } else if (WorkflowType.WORKFLOW_TYPE_MALL_RELET_ORDER.equals(workflowType)) {
            //商城续租订单-暂定原逻辑
            ReletOrderDO reletOrderDO = reletOrderMapper.findByReletOrderNo(workflowReferNo);
            if (reletOrderDO != null) subCompanyId = reletOrderDO.getOrderSubCompanyId();
        } else if (WorkflowType.WORKFLOW_TYPE_MALL_CHANGE_ORDER.equals(workflowType)) {
            //商城换货单-暂定以客户所属业务员判断公司
            K3ChangeOrderDO k3ChangeOrderDO = k3ChangeOrderMapper.findByNo(workflowReferNo);
            if (k3ChangeOrderDO != null) {
                CustomerDO customerDO = customerMapper.findByNo(k3ChangeOrderDO.getK3CustomerNo());
                if (customerDO != null) subCompanyId = customerDO.getOwnerSubCompanyId();
            }
        } else if (WorkflowType.WORKFLOW_TYPE_MALL_RETURN_ORDER.equals(workflowType)) {
            //商城退货单-暂定以客户所属业务员判断公司
            K3ReturnOrderDO k3ReturnOrderDO = k3ReturnOrderMapper.findByNo(workflowReferNo);
            if (k3ReturnOrderDO != null) {
                CustomerDO customerDO = customerMapper.findByNo(k3ReturnOrderDO.getK3CustomerNo());
                if (customerDO != null) subCompanyId = customerDO.getOwnerSubCompanyId();
            }
        } else if (WorkflowType.WORKFLOW_TYPE_MALL_CUSTOMER.equals(workflowType) || WorkflowType.WORKFLOW_TYPE_MALL_CUSTOMER_CONSIGN.equals(workflowType)) {
            //商城客户和客户收货地址-暂定以总公司判断公司
            subCompanyId = CommonConstant.HEAD_COMPANY_ID;
        } else {
            subCompanyId = userSupport.getCurrentUserCompanyId();
        }
        return subCompanyId;
    }

    /**
     * 保存单条审核组审核人员并且返回审核组id
     */
    public String saveVerifyGroupAndGetUserGroupId(Integer loginUserId, Date currentTime, String commitRemark) {
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
        return workflowVerifyUserGroupDO.getVerifyUserGroupId();
    }

    /**
     * 保存工作流详情记录
     */
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

    /**
     * 得到审核组人和图片
     */
    public WorkflowLinkDO getVerifyUserGroupAndImg(WorkflowLinkDO workflowLinkDO) {
        if(workflowLinkDO != null){
            if (workflowLinkDO.getVerifyUserGroupId() != null) {
                List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDO.getVerifyUserGroupId());
                if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
                    throw new BusinessException(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS, ErrorCode.getMessage(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS));
                }
                workflowLinkDO.setWorkflowVerifyUserGroupDOList(workflowVerifyUserGroupDOList);
                for (WorkflowLinkDetailDO workflowLinkDetailDO : workflowLinkDO.getWorkflowLinkDetailDOList()) {
                    if (workflowLinkDetailDO.getVerifyUserGroupId() != null) {
                        workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDetailDO.getVerifyUserGroupId());
                        if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
                            throw new BusinessException(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS, ErrorCode.getMessage(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS));
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
        }
        return workflowLinkDO;
    }

    /**
     * 得到该节点的使用人列表
     */
    public List<User> getUserListByNode(WorkflowNodeDO workflowNodeDO, Integer subCompanyId) {
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

    /**
     * 判断当前审核人是否正确
     */
    public boolean verifyVerifyUsers(WorkflowNodeDO workflowNodeDO, Integer userId, Integer subCompanyId) {
        List<User> userList = getUserListByNode(workflowNodeDO, subCompanyId);
        if (CollectionUtil.isNotEmpty(userList)) {
            Map<Integer, User> userMap = ListUtil.listToMap(userList, "userId");
            if (userMap.containsKey(userId)) {
                return true;
            }
        }
        return false;
    }

    public void saveVerifyWorkflowLinkDetailDO(Integer workflowLinkId, String workflowReferNo, WorkflowNodeDO thisWorkflowNodeDO, List<WorkflowNodeDO> workflowNodeDOList, Integer verifyUser, String verifyUserGroupId, Integer loginUser, Date currentTime) {
        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkId);
        workflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        if (workflowNodeDOList.size() > 1) {
            workflowLinkDetailDO.setWorkflowNextNodeId(workflowNodeDOList.get(1).getId());
        }
        workflowLinkDetailDO.setVerifyUser(verifyUser);
        workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setCreateUser(loginUser.toString());
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);
    }

    public void saveCommitWorkflowLinkDetailDO(Integer workflowLinkId, String workflowReferNo, Integer thisWorkflowNodeId, Integer loginUser, String verifyUserGroupId, String commitRemark, Date currentTime) {
        WorkflowLinkDetailDO commitWorkflowLinkDetailDO = new WorkflowLinkDetailDO();
        commitWorkflowLinkDetailDO.setWorkflowLinkId(workflowLinkId);
        commitWorkflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        commitWorkflowLinkDetailDO.setWorkflowCurrentNodeId(0);
        commitWorkflowLinkDetailDO.setWorkflowStep(0);
        commitWorkflowLinkDetailDO.setWorkflowNextNodeId(thisWorkflowNodeId);
        commitWorkflowLinkDetailDO.setVerifyUser(loginUser);
        commitWorkflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
        commitWorkflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowLinkDetailDO.setVerifyOpinion(commitRemark);
        commitWorkflowLinkDetailDO.setRemark(commitRemark);
        commitWorkflowLinkDetailDO.setVerifyTime(currentTime);
        commitWorkflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowLinkDetailDO.setCreateUser(loginUser.toString());
        commitWorkflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(commitWorkflowLinkDetailDO);
    }

    public WorkflowVerifyUserGroupDO saveCommitWorkflowVerifyUserGroupDO(Integer loginUser, Date currentTime, String commitRemark) {
        WorkflowVerifyUserGroupDO commitWorkflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        commitWorkflowVerifyUserGroupDO.setVerifyUserGroupId(generateNoSupport.generateVerifyUserGroupId());
        commitWorkflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
        commitWorkflowVerifyUserGroupDO.setVerifyUser(loginUser);
        commitWorkflowVerifyUserGroupDO.setVerifyTime(currentTime);
        commitWorkflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowVerifyUserGroupDO.setVerifyOpinion(commitRemark);
        commitWorkflowVerifyUserGroupDO.setRemark(commitRemark);
        commitWorkflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowVerifyUserGroupDO.setCreateUser(loginUser.toString());
        commitWorkflowVerifyUserGroupDO.setCreateTime(currentTime);
        workflowVerifyUserGroupMapper.save(commitWorkflowVerifyUserGroupDO);
        return commitWorkflowVerifyUserGroupDO;
    }

    public WorkflowVerifyUserGroupDO getWorkflowVerifyUserGroupDO(String verifyUserGroupId, Integer verifyUser, Date currentTime, String createUser, Integer verifyType) {
        WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        workflowVerifyUserGroupDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowVerifyUserGroupDO.setVerifyType(verifyType);
        workflowVerifyUserGroupDO.setVerifyUser(verifyUser);
        workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowVerifyUserGroupDO.setCreateUser(createUser);
        workflowVerifyUserGroupDO.setCreateTime(currentTime);
        return workflowVerifyUserGroupDO;
    }

    public void saveWorkflowVerifyUserGroup(String verifyUserGroupId, List<User> verifyUserList, Date currentTime, Integer verifyUser, Integer loginUser,Integer verifyType) {
        if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(verifyType) || VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(verifyType)) {
            List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = new ArrayList<>();
            for (User user : verifyUserList) {
                workflowVerifyUserGroupDOList.add(getWorkflowVerifyUserGroupDO(verifyUserGroupId, user.getUserId(), currentTime, loginUser.toString(), verifyType));
            }
            if (CollectionUtil.isNotEmpty(workflowVerifyUserGroupDOList)) {
                SqlLogInterceptor.setExecuteSql("skip print  workflowVerifyUserGroupMapper.saveList  sql ......");
                workflowVerifyUserGroupMapper.saveList(workflowVerifyUserGroupDOList);
            }
        } else {
            workflowVerifyUserGroupMapper.save(getWorkflowVerifyUserGroupDO(verifyUserGroupId, verifyUser, currentTime, loginUser.toString(), verifyType));
        }
    }
    /** 得到经营范围的公司id */
    public Set<Integer> getSubCompanyIdSet(CustomerDO customerDO,List<CustomerConsignInfoDO> customerConsignInfoDOList, Integer loginUser ,Date currentTime){
        Set<Integer> subCompanySet = new HashSet<>();
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            if (CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus()) || CustomerConsignVerifyStatus.VERIFY_STATUS_BACK.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())) {
                //判断经营地址
                SubCompanyCityCoverDO subCompanyCityCoverDO = getSubCompanyCityCoverDO(customerDO.getCustomerCompanyDO().getCity(), customerDO.getCustomerCompanyDO().getProvince());

                subCompanySet.add(subCompanyCityCoverDO.getSubCompanyId());
                customerDO.getCustomerCompanyDO().setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT);
                customerDO.getCustomerCompanyDO().setUpdateTime(currentTime);
                customerDO.getCustomerCompanyDO().setUpdateUser(loginUser.toString());
                customerCompanyMapper.update(customerDO.getCustomerCompanyDO());
            }
        }
        //判断收货地址
        for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
            if (CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING.equals(customerConsignInfoDO.getVerifyStatus()) || CustomerConsignVerifyStatus.VERIFY_STATUS_BACK.equals(customerConsignInfoDO.getVerifyStatus())) {
                SubCompanyCityCoverDO subCompanyCityCoverDO = getSubCompanyCityCoverDO(customerConsignInfoDO.getCity(), customerConsignInfoDO.getProvince());

                subCompanySet.add(subCompanyCityCoverDO.getSubCompanyId());
                customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT);
                customerConsignInfoDO.setUpdateUser(loginUser.toString());
                customerConsignInfoDO.setUpdateTime(currentTime);
                customerConsignInfoDO.setWorkflowType(WorkflowType.WORKFLOW_TYPE_CUSTOMER);
                customerConsignInfoMapper.update(customerConsignInfoDO);
            }
        }
        return subCompanySet;
    }

    public SubCompanyCityCoverDO getSubCompanyCityCoverDO(Integer cityId, Integer provinceId) {
        if(cityId == null){
            cityId = -1;
        }
        SubCompanyCityCoverDO subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(cityId);
        if (subCompanyCityCoverDO == null) {
            if(provinceId == null){
                provinceId = -1;
            }
            subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(provinceId);
            if (subCompanyCityCoverDO == null) {
                throw new BusinessException(ErrorCode.CUSTOMER_COMPANY_NOT_CITY_AND_PROVINCE_IS_NULL, ErrorCode.getMessage(ErrorCode.CUSTOMER_COMPANY_NOT_CITY_AND_PROVINCE_IS_NULL));
            }
        }
        return subCompanyCityCoverDO;
    }

    /** 保存经营范围的审核人并返回審核人id */
    public List<Integer> saveVerifyUserAndGetVerifyUser(Set<Integer> subCompanySet,Integer roleType,String verifyUserGroupId,Date currentTime,Integer loginUser){
        List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = new ArrayList<>();
        List<Integer> verifyUserList = new ArrayList<>();
        for (Integer subCompanyId : subCompanySet) {
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setRoleType(roleType);
            userQueryParam.setSubCompanyId(subCompanyId);
            userQueryParam.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO);
            ServiceResult<String, List<User>> userResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                if (CollectionUtil.isEmpty(userResult.getResult())) {
                    throw new BusinessException(ErrorCode.WORKFLOW_CONFIG_ERROR, ErrorCode.getMessage(ErrorCode.WORKFLOW_CONFIG_ERROR));
                }
                for (User user : userResult.getResult()) {
                    workflowVerifyUserGroupDOList.add(getWorkflowVerifyUserGroupDO(verifyUserGroupId, user.getUserId(), currentTime, loginUser.toString(), VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS));
                    verifyUserList.add(user.getUserId());
                }
            }
        }
        if (CollectionUtil.isNotEmpty(workflowVerifyUserGroupDOList)) {
            SqlLogInterceptor.setExecuteSql("skip print  workflowVerifyUserGroupMapper.saveList  sql ......");
            workflowVerifyUserGroupMapper.saveList(workflowVerifyUserGroupDOList);
        }
        return verifyUserList;
    }

    public List<User> checkVerifyUserType(WorkflowNodeDO thisWorkflowNodeDO ,Integer subCompanyId,Integer verifyUser){
        List<User> verifyUserList = null;
        //todo 针对商城 获取审核人列表
        if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(thisWorkflowNodeDO.getVerifyType()) || VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(thisWorkflowNodeDO.getVerifyType())) {
            verifyUserList = getUserListByNode(thisWorkflowNodeDO, subCompanyId);
            if (CollectionUtil.isEmpty(verifyUserList)) {
                throw new BusinessException(ErrorCode.WORKFLOW_VERIFY_USER_IS_NULL, ErrorCode.getMessage(ErrorCode.WORKFLOW_VERIFY_USER_IS_NULL));
            }
        } else {
            if (!verifyVerifyUsers(thisWorkflowNodeDO, verifyUser, subCompanyId)) {
                throw new BusinessException(ErrorCode.WORKFLOW_VERIFY_USER_ERROR, ErrorCode.getMessage(ErrorCode.WORKFLOW_VERIFY_USER_ERROR));
            }
        }
        return verifyUserList;
    }

    @Autowired
    private ImgMysqlMapper imgMysqlMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private DeploymentOrderMapper deploymentOrderMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;

    @Autowired
    private SubCompanyCityCoverMapper subCompanyCityCoverMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ReletOrderMapper reletOrderMapper;

    @Autowired
    private K3ChangeOrderMapper k3ChangeOrderMapper;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private WorkflowVerifyUserGroupMapper workflowVerifyUserGroupMapper;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private WorkflowLinkDetailMapper workflowLinkDetailMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;

    @Autowired
    private ReplaceOrderMapper replaceOrderMapper;
}
