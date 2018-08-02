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
     * 钉钉网关的secret
     */
    public static String dingdingSecret;

    /**
     * <p>
     * 审批回调URL
     * </p>
     *
     * @author daiqi
     * @date 2018/4/28 16:12
     * @param null
     * @return
     */
    public static String applyApprovingWorkflowCallBackUrl;

    /**
     * 接口---发起审批实例
     */
    private static String interfaceApplyApprovingWorkflow = "applyApprovingWorkflow";

    /**
     * 接口---往钉钉网关上注册erp系统的userId接口
     */
    private static String interfaceInputMember = "inputMember";

    /**
     * 接口---注销钉钉网关工作流接口
     */
    private static String delApprovingWorkflow = "delApprovingWorkflow";
    /**
     * 接口---发起审批实例
     */
    private static String interfaceSendSystemMessage = "sendDingTalkMsg";
    /**
     * 获取注销钉钉网关工作流接口的url
     */
    public static String getDelApprovingWorkflowUrl() {
        return dingDingGatewayUrl + "/" + delApprovingWorkflow;
    }

    /**
     * 获取发起审批实例的url
     */
    public static String getApplyApprovingWorkflowUrl() {
        return dingDingGatewayUrl + "/" + interfaceApplyApprovingWorkflow;
    }

    /**
     * 获取发起审批实例的url
     */
    public static String getInputMemberUrl() {
        return dingDingGatewayUrl + "/" + interfaceInputMember;
    }

    /**
     * 获取发送系统消息的url
     */
    public static String getSendSystemMessageUrl() {
        return dingDingGatewayUrl + "/" + interfaceSendSystemMessage;
    }

    public void setApplyApprovingWorkflowCallBackUrl(String applyApprovingWorkflowCallBackUrl) {
        DingDingConfig.applyApprovingWorkflowCallBackUrl = applyApprovingWorkflowCallBackUrl;
    }

    public void setDingdingSecret(String dingdingSecret) {
        DingDingConfig.dingdingSecret = dingdingSecret;
    }

    public void setDingDingGatewayUrl(String dingDingGatewayUrl) {
        DingDingConfig.dingDingGatewayUrl = dingDingGatewayUrl;
    }
}
