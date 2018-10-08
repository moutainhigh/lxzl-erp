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
//    /**
//     * 获取发送钉钉机器人消息的url
//     */
//    public static String getSendRobotMessageUrl() {
//       // return dingDingGatewayUrl + "/robot/sendRobotMsg?accessToken=b20074e334cddd5f41728a7283969e0528259c5f2929e05811b63a09af78eddf";
//        return dingDingGatewayUrl +DingDingMsgConfig.exceptionMsgUrl;
//
//    }


    /**
     * 获取注销钉钉网关工作流接口的url
     */
    public static String getDelApprovingWorkflowUrl() {
        return dingDingGatewayUrl + "/" + DingTalkGatewayRUSTApi.delApprovingWorkflow.uri;
    }

    /**
     * 获取发起审批实例的url
     */
    public static String getApplyApprovingWorkflowUrl() {
        return dingDingGatewayUrl + "/" + DingTalkGatewayRUSTApi.interfaceApplyApprovingWorkflow.uri;
    }

    /**
     * 获取发起审批实例的url
     */
    public static String getInputMemberUrl() {
        return dingDingGatewayUrl + "/" + DingTalkGatewayRUSTApi.interfaceInputMember.uri;
    }

    /**
     * 获取发送系统消息的url
     */
    public static String getSendSystemMessageUrl() {
        return dingDingGatewayUrl + "/" +  DingTalkGatewayRUSTApi.interfaceSendSystemMessage.uri;
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

    private enum DingTalkGatewayRUSTApi {
        //发起审批实例
        interfaceApplyApprovingWorkflow("applyApprovingWorkflow"),
        //往钉钉网关上注册erp系统的userId接口
        interfaceInputMember("inputMember"),
        //注销钉钉网关工作流接口
        delApprovingWorkflow("delApprovingWorkflow"),
        //发起钉钉消息
        interfaceSendSystemMessage("sendDingTalkMsg");

        private String uri;

        DingTalkGatewayRUSTApi(String uri) {
            String interfaceNamespace = "api/";
            this.uri = interfaceNamespace + uri;
        }
    }
}
