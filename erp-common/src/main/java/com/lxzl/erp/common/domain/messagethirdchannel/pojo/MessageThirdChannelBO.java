package com.lxzl.erp.common.domain.messagethirdchannel.pojo;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import org.apache.commons.lang.StringUtils;

/**
 * 使用第三方渠道发送消息业务逻辑对象
 * @author daiqi
 * @create 2018-05-21 15:03
 */
public class MessageThirdChannelBO {
    private MessageThirdChannel messageThirdChannel;

    public MessageThirdChannelBO(MessageThirdChannel messageThirdChannel) {
        this.messageThirdChannel = messageThirdChannel;
    }

    /** 校验发送系统信息 */
    public ServiceResult<String, Object> verifySendMessage() {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        if (StringUtils.isBlank(messageThirdChannel.getMessageContent())) {
            serviceResult.setErrorCode(ErrorCode.MESSAGE_CONTENT_CANT_NULL);
            return serviceResult;
        }
        if (messageThirdChannel.getReceiverUserId() == null) {
            serviceResult.setErrorCode(ErrorCode.RECEIVER_USER_ID_CANT_NULL);
            return serviceResult;
        }
        return serviceResult;
    }

    /** 初始化发送系统消息数据 */
    public void initSendMessageData() {
        if (messageThirdChannel.getSenderUserId() == null) {
            messageThirdChannel.setSenderUserId(-1);
        }
        if (StringUtils.isBlank(messageThirdChannel.getSenderRemark())) {
            messageThirdChannel.setSenderRemark("System");
        }
        if (messageThirdChannel.getMessageChannel() == null) {
            messageThirdChannel.setMessageChannel(1);
        }
    }


}
