package com.lxzl.erp.common.constant;

/**
 * 审核类型
 */
public class VerifyType {

    public static final Integer VERIFY_TYPE_THIS_IS_PASS = 1;          // 1-本条审核通过则直接通过
    public static final Integer VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS = 2;          // 2-相同审核组的所有2状态的审核通过才算通过
}
