package com.lxzl.erp.common.domain;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/8/6
 * @Time : Created in 20:04
 */
public class TaskSchedulerSystemConfig {

    public static String taskSchedulerSystemAppId;
    public static String taskSchedulerAppSecret;

    public static String getTaskSchedulerSystemAppId() {
        return taskSchedulerSystemAppId;
    }

    public static void setTaskSchedulerSystemAppId(String taskSchedulerSystemAppId) {
        TaskSchedulerSystemConfig.taskSchedulerSystemAppId = taskSchedulerSystemAppId;
    }

    public static String getTaskSchedulerAppSecret() {
        return taskSchedulerAppSecret;
    }

    public static void setTaskSchedulerAppSecret(String taskSchedulerAppSecret) {
        TaskSchedulerSystemConfig.taskSchedulerAppSecret = taskSchedulerAppSecret;
    }
}
