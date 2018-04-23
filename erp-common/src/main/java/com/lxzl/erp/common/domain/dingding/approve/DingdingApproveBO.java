package com.lxzl.erp.common.domain.dingding.approve;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 钉钉审批业务逻辑对象
 *
 * @author daiqi
 * @create 2018-04-20 10:58
 */
public class DingdingApproveBO {
    private DingdingApproveDTO dingdingApproveDTO;

    public DingdingApproveBO(DingdingApproveDTO dingdingApproveDTO) {
        this.dingdingApproveDTO = dingdingApproveDTO;
    }


    public DingdingApproveDTO getDingdingApproveDTO() {
        return dingdingApproveDTO;
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
    }

    /** 新增审批人用户id列表 */
    public void addApprovers(Set<String> approvers) {
        if (approvers == null) {
            return;
        }
        Set<String> approversFromDTO = this.dingdingApproveDTO.getApproverList();
        approversFromDTO = getLinkedHashSetNotNull(approversFromDTO);
        approversFromDTO.addAll(approvers);
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
    }

    private <T> Set<T> getLinkedHashSetNotNull(Set<T> set) {
        if (set == null) {
            return new LinkedHashSet<>();
        }
        return set;
    }

}
