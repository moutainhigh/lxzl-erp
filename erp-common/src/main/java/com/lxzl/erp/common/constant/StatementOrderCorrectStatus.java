package com.lxzl.erp.common.constant;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/30
 * @Time : Created in 15:33
 */
public class StatementOrderCorrectStatus {
    public static Integer VERIFY_STATUS_PENDING = 0; //待提交
    public static Integer VERIFY_STATUS_COMMIT = 1; //审核中
    public static Integer VERIFY_STATUS_PASS = 2; //审核通过（待冲正）
    public static Integer CORRECT_SUCCEES = 3; //冲正成功
    public static Integer CORRECT_FAIL = 4; //冲正失败
    public static Integer CORRECT_CANCEL = 5; //取消冲正'

}
