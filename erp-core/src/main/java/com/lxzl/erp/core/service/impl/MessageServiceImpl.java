package com.lxzl.erp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.erp.core.mq.MqConstants;
import com.lxzl.erp.core.service.MessageService;
import com.lxzl.se.core.mq.producer.MqMessageSender;
import com.lxzl.se.core.service.impl.BaseServiceImpl;

@Service("messageService")
public class MessageServiceImpl extends BaseServiceImpl implements MessageService {
	
	@Autowired(required = false)
	private MqMessageSender mqMessageSender;
	
	@Override
	public String sendCommentMsg(String message) {
		String messageId = mqMessageSender.sendMessage(MqConstants.DEMOWEB_EXCHANGE, MqConstants.ORDER_MESSAGE_ROUTINGKEY, message);
		return messageId;
	}

}
