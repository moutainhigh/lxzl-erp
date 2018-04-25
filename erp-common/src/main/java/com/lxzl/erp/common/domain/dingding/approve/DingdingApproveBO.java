package com.lxzl.erp.common.domain.dingding.approve;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 钉钉审批业务逻辑对象
 *
 * @author daiqi
 * @create 2018-04-20 10:58
 */
public class DingdingApproveBO {
    private DingdingApproveDTO dingdingApproveDTO;


    public DingdingApproveBO() {
        this.dingdingApproveDTO = new DingdingApproveDTO();
    }

    public DingdingApproveBO(DingdingApproveDTO dingdingApproveDTO) {
        this.dingdingApproveDTO = dingdingApproveDTO;
    }

    public DingdingApproveBO buildDeptId(Integer depId) {
        this.dingdingApproveDTO.setDeptId(depId);
        return this;
    }
    /** 构建工作流单号 */
    public DingdingApproveBO buildWorkflowLinkNo(String workflowLinkNo) {
        this.dingdingApproveDTO.setWorkflowLinkNo(workflowLinkNo);
        return this;
    }

    public DingdingApproveBO buildOriginatorUserId(Integer userId) {
        this.dingdingApproveDTO.setOriginatorUserId(userId);
        return this;
    }

    public DingdingApproveBO buildProcessCode(String processCode) {
        this.dingdingApproveDTO.setProcessCode(processCode);
        return this;
    }

    /**
     * 新增钉钉表单组件数据传输对象
     */
    public DingdingApproveBO buildFormComponentObj(Object formComponentObj) {
        this.dingdingApproveDTO.setFormComponentObj(formComponentObj);
        return this;
    }

    /**
     * add审批人用户id
     */
    public void addApprover(Integer approver) {
        if (approver == null) {
            return;
        }
        Set<Integer> approversFromDTO = this.dingdingApproveDTO.getApproverList();
        approversFromDTO = getLinkedHashSetNotNull(approversFromDTO);
        approversFromDTO.add(approver);
        this.dingdingApproveDTO.setApproverList(approversFromDTO);
    }

    /**
     * 新增审批人用户id列表
     */
    public void addApprovers(Set<Integer> approvers) {
        if (approvers == null) {
            return;
        }
        Set<Integer> approversFromDTO = this.dingdingApproveDTO.getApproverList();
        approversFromDTO = getLinkedHashSetNotNull(approversFromDTO);
        approversFromDTO.addAll(approvers);
        this.dingdingApproveDTO.setApproverList(approversFromDTO);
    }

    /**
     * add抄送人用户id
     */
    public void addCcUserId(Integer ccUserId) {
        if (ccUserId == null) {
            return;
        }
        Set<Integer> ccUserIdsFromDTO = this.dingdingApproveDTO.getCcUserIdsList();
        ccUserIdsFromDTO = getLinkedHashSetNotNull(ccUserIdsFromDTO);
        ccUserIdsFromDTO.add(ccUserId);
        this.dingdingApproveDTO.setCcUserIdsList(ccUserIdsFromDTO);
    }

    /**
     * 新增抄送人列表信息
     */
    public void addCcUserIds(Set<Integer> ccUserIds) {
        if (ccUserIds == null) {
            return;
        }
        Set<Integer> ccUserIdsFromDTO = this.dingdingApproveDTO.getCcUserIdsList();
        ccUserIdsFromDTO = getLinkedHashSetNotNull(ccUserIdsFromDTO);
        ccUserIdsFromDTO.addAll(ccUserIds);
        this.dingdingApproveDTO.setCcUserIdsList(ccUserIdsFromDTO);
    }

    private <T> Set<T> getLinkedHashSetNotNull(Set<T> set) {
        if (set == null) {
            return new LinkedHashSet<>();
        }
        return set;
    }

    public DingdingApproveDTO getDingdingApproveDTO() {
        return dingdingApproveDTO;
    }

}
