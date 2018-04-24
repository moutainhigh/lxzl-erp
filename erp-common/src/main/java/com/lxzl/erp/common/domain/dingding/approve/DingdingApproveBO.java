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

    public DingdingApproveBO buildDeptId(String depId) {
        this.dingdingApproveDTO.setDeptId(depId);
        return this;
    }

    public DingdingApproveBO buildOriginatorUserId(String originatorUserId) {
        this.dingdingApproveDTO.setOriginatorUserId(originatorUserId);
        return this;
    }

    public DingdingApproveBO buildProcessCode(String processCode) {
        this.dingdingApproveDTO.setProcessCode(processCode);
        return this;
    }

    /**
     * 新增钉钉表单组件数据传输对象
     */
    public DingdingApproveBO addDingdingFormComponentValue(DingdingFormComponentValueDTO formComponentValueDTO) {
        List<DingdingFormComponentValueDTO> valueDTOS = getArrayListNotNull(this.dingdingApproveDTO.getFormComponentValueList());
        valueDTOS.add(formComponentValueDTO);
        this.dingdingApproveDTO.setFormComponentValueList(valueDTOS);
        return this;
    }

    /**
     * 新增钉钉表单组件数据传输对象
     */
    public DingdingApproveBO addDingdingFormComponentValue(String name, String value) {
        List<DingdingFormComponentValueDTO> valueDTOS = getArrayListNotNull(this.dingdingApproveDTO.getFormComponentValueList());
        valueDTOS.add(new DingdingFormComponentValueDTO(name, value));
        this.dingdingApproveDTO.setFormComponentValueList(valueDTOS);
        return this;
    }

    /**
     * 新增钉钉表单组件数据传输对象
     */
    public DingdingApproveBO addDingdingFormComponentValueList(List<DingdingFormComponentValueDTO> formComponentValueDTOs) {
        List<DingdingFormComponentValueDTO> valueDTOS = getArrayListNotNull(this.dingdingApproveDTO.getFormComponentValueList());
        valueDTOS.addAll(formComponentValueDTOs);
        this.dingdingApproveDTO.setFormComponentValueList(valueDTOS);
        return this;
    }

    /**
     * add审批人用户id
     */
    public void addApprover(String approver) {
        if (StringUtils.isBlank(approver)) {
            return;
        }
        Set<String> approversFromDTO = this.dingdingApproveDTO.getApproverList();
        approversFromDTO = getLinkedHashSetNotNull(approversFromDTO);
        approversFromDTO.add(approver);
        this.dingdingApproveDTO.setApproverList(approversFromDTO);
    }

    /**
     * 新增审批人用户id列表
     */
    public void addApprovers(Set<String> approvers) {
        if (approvers == null) {
            return;
        }
        Set<String> approversFromDTO = this.dingdingApproveDTO.getApproverList();
        approversFromDTO = getLinkedHashSetNotNull(approversFromDTO);
        approversFromDTO.addAll(approvers);
        this.dingdingApproveDTO.setApproverList(approversFromDTO);
    }

    /**
     * add抄送人用户id
     */
    public void addCcUserId(String ccUserId) {
        if (StringUtils.isBlank(ccUserId)) {
            return;
        }
        Set<String> ccUserIdsFromDTO = this.dingdingApproveDTO.getCcUserIdsList();
        ccUserIdsFromDTO = getLinkedHashSetNotNull(ccUserIdsFromDTO);
        ccUserIdsFromDTO.add(ccUserId);
        this.dingdingApproveDTO.setCcUserIdsList(ccUserIdsFromDTO);
    }

    /**
     * 新增抄送人列表信息
     */
    public void addCcUserIds(Set<String> ccUserIds) {
        if (ccUserIds == null) {
            return;
        }
        Set<String> ccUserIdsFromDTO = this.dingdingApproveDTO.getCcUserIdsList();
        ccUserIdsFromDTO = getLinkedHashSetNotNull(ccUserIdsFromDTO);
        ccUserIdsFromDTO.addAll(ccUserIds);
        this.dingdingApproveDTO.setCcUserIdsList(ccUserIdsFromDTO);
    }

    private <T> List<T> getArrayListNotNull(List<T> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
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
