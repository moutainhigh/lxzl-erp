package com.lxzl.erp.core.service.messagethirdchannel.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.dingding.message.DingdingMessageDTO;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannel;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannelBO;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.erp.core.service.messagethirdchannel.MessageThirdChannelService;
import com.lxzl.erp.dataaccess.dao.mysql.messagethirdchannel.MessageThirdChannelMapper;
import com.lxzl.erp.dataaccess.domain.messagethirdchannel.MessageThirdChannelDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author daiqi
 * @create 2018-05-21 14:24
 */
@Service
public class MessageThirdChannelServiceImpl implements MessageThirdChannelService{
    @Autowired
    private MessageThirdChannelMapper messageThirdChannelMapper;
    @Autowired
    private DingdingService dingdingService;


    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Object> sendMessage(MessageThirdChannel messageThirdChannel) {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);

        MessageThirdChannelBO messageThirdChannelBO = new MessageThirdChannelBO(messageThirdChannel);
        // 初始化
        messageThirdChannelBO.initSendMessageData();
        // 数据校验
        ServiceResult<String, Object> verifyService = messageThirdChannelBO.verifySendMessage();
        if (!ErrorCode.SUCCESS.equals(verifyService.getErrorCode())) {
            return verifyService;
        }
        // 保存数据
        messageThirdChannelMapper.save(ConverterUtil.convert(messageThirdChannel, MessageThirdChannelDO.class));
        // 发送消息
        serviceResult = doSendMessage(messageThirdChannel);
        return serviceResult;
    }

    /** 执行发送消息 */
    private ServiceResult<String, Object> doSendMessage(MessageThirdChannel messageThirdChannel) {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        if (messageThirdChannel.getMessageChannel() == 1) {
            serviceResult = dingdingService.sendMessageToDingding(new DingdingMessageDTO(messageThirdChannel));
        }
        return serviceResult;
    }
}
