package com.lxzl.erp.common.constant;

/**
 * 审核状态
 */
public class VerifyStatus {

    public static final Integer VERIFY_STATUS_PENDING = 0;          // 0-待提交
    public static final Integer VERIFY_STATUS_COMMIT = 1;          // 1-已提交
    public static final Integer VERIFY_STATUS_PASS = 2;          // 2-审批通过
    public static final Integer VERIFY_STATUS_BACK = 3;          // 3-审批驳回
    public static final Integer VERIFY_STATUS_CANCEL = 4;          // 4-取消
    public static final Integer VERIFY_STATUS_REJECT_PASS = 5;          // 5-驳回已通过审批
}
