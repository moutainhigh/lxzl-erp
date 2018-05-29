package com.lxzl.erp.core.service.messagethirdchannel;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannel;

/**
 * @author daiqi
 * @create 2018-05-21 14:24
 */
public interface MessageThirdChannelService {
    /**  
     * <p>
     * 发送消息
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     *     messageThirdChannel.messageContent : 消息内容 : 消息内容 : 是
     *     messageThirdChannel.receiverUserId : 12121 : 消息接收者用户编号 : 是
     *     messageThirdChannel.messageTitle : 这是消息主题 : 消息主题 : 否
     *     messageThirdChannel.messageType : 消息类型 : 不传默认系统消息 : 否
     *     messageThirdChannel.messageChannel : 1 : 消息渠道 默认钉钉 : 否
     *     messageThirdChannel.senderUserId : -1 : 发送者用户编号 : 否
     *     messageThirdChannel.senderRemark : System : 发送者备注 : 否
     * </pre>
     * @author daiqi  
     * @date 2018/5/21 14:26
     * @param  messageThirdChannel
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>  
     */  
    ServiceResult<String, Object> sendMessage(MessageThirdChannel messageThirdChannel);
}
