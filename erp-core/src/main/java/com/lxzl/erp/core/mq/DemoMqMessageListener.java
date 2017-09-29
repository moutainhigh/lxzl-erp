package com.lxzl.erp.core.mq;

import com.lxzl.se.core.mq.consumer.MqMessageListener;

/**
 * 
 * @author lxzl
 *
 */
public class DemoMqMessageListener extends MqMessageListener {

	@Override
	public Object handleMessage(String messageId, String messageContent, String exchange, String routingKey, String queue) {
		System.out.println(messageContent);
		return "success";
	}
}
