package com.lxzl.erp.common.domain;

/**
 * 钉钉常量配置类
 *
 * @author daiqi
 * @create 2018-04-20 9:46
 */
public class DingDingConfig {
    /**
     * 钉钉网关url
     */
    public static String dingDingGatewayUrl;
    /**
     * 接口---根据部门编号获取人员列表信息
     */
    private static String interfaceGetMemberListbyDepartment = "getMemberListbyDepartment";
    /**
     * 接口---发起审批实例
     */
    private static String interfaceApplyApprovingWorkflow = "applyApprovingWorkflow";

    /**
     * 获取根据部门编号获取人员列表信息的url
     */
    public static String getGetMemberListbyDepartmentUrl() {
        return dingDingGatewayUrl + "/" + interfaceGetMemberListbyDepartment;
    }

    /**
     * 获取发起审批实例的url
     */
    public static String getApplyApprovingWorkflowUrl() {
        return dingDingGatewayUrl + "/" + interfaceApplyApprovingWorkflow;
    }


    public void setDingDingGatewayUrl(String dingDingGatewayUrl) {
        DingDingConfig.dingDingGatewayUrl = dingDingGatewayUrl;
    }
}
