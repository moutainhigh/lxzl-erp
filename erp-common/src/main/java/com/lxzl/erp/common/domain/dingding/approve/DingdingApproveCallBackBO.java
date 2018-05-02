package com.lxzl.erp.common.domain.dingding.approve;

import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.constant.WorkflowReturnType;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * 钉钉审批回调业务逻辑类
 * @author daiqi
 * @create 2018-04-28 13:56
 */
public class DingdingApproveCallBackBO {

    private DingdingApproveCallBackDTO dingdingApproveCallBackDTO;

    public DingdingApproveCallBackBO(DingdingApproveCallBackDTO dingdingApproveCallBackDTO) {
        this.dingdingApproveCallBackDTO = dingdingApproveCallBackDTO;
    }

    /** 获取审批状态 */
    public Integer getVerifyStatus() {
        String result = dingdingApproveCallBackDTO.getResult();
        if (StringUtils.isBlank(result)) {
            return null;
        }
        // 只处理通过或驳回的状态
        switch (result) {
            case "agree" : return VerifyStatus.VERIFY_STATUS_PASS;
            case "refuse" : return VerifyStatus.VERIFY_STATUS_BACK;
            default: return null;
        }
    }

    /** 获取返回类型 */
    public Integer getReturnType() {
        if (VerifyStatus.VERIFY_STATUS_BACK.equals(getVerifyStatus())) {
            return WorkflowReturnType.RETURN_TYPE_ROOT;
        }
        return null;
    }

    /** 获取审核意见 */
    public String getVerifyOpinion() {
        return dingdingApproveCallBackDTO.getRemark();
    }

    /**
     *  是否执行工作流
     *  true: 是 ，false:否
     */
    public boolean isDOVerifyWorkFlow() {
        boolean isDOFlag = isTaskEventType() && getVerifyStatus() != null && "finish".equals(dingdingApproveCallBackDTO.getType());
        if (isDOFlag) {
            return true;
        }
        return false;
    }

    /** 是任务事件类型 */
    public boolean isTaskEventType() {
        if (StringUtils.equals("bpms_task_change", dingdingApproveCallBackDTO.getEventType())) {
            return true;
        }
        return false;
    }

    /** 是实例事件类型 */
    private boolean isInstanceEventType() {
        if (StringUtils.equals("bpms_instance_change", dingdingApproveCallBackDTO.getEventType())) {
            return true;
        }
        return false;
    }

}
