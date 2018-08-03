package com.lxzl.erp.common.constant;

/**
 * @Author: Sunzhipeng
 * @Description:  延迟任务列表任务状态：1排队中；2处理中；3已完成；4已取消；5执行失败
 * @Date: Created in 2018\7\27 0027 20:04
 */
public class DelayedTaskStatus {
    public static final Integer DELAYED_TASK_QUEUE = 1;      // 1排队中
    public static final Integer DELAYED_TASK_PROCESSING = 2;      // 2处理中
    public static final Integer DELAYED_TASK_COMPLETED = 3;      // 3已完成
    public static final Integer DELAYED_TASK_CANCELLED = 4;      // 4已取消
    public static final Integer DELAYED_TASK_EXECUTION_FAILED = 5;      // 5执行失败
}
