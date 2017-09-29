package com.lxzl.erp.core.mq;

import com.lxzl.se.core.mq.config.MqUtil;
import com.lxzl.se.core.mq.consumer.MqMessageListener;

/**
 * 
 * @author lxzl
 *
 */
public class DemoMqMessageListener extends MqMessageListener {

	@Override
	public Object handleMessage(String messageId, String messageContent, String exchange, String routingKey, String queue) {
		if (MqUtil.equalsMessage(exchange, MqConstants.DEMOWEB_EXCHANGE, routingKey, MqConstants.ORDER_MESSAGE_ROUTINGKEY)) {
			System.out.println("Hand order message " + messageContent);
		} else if (MqUtil.equalsMessage(exchange, MqConstants.DEMOWEB_EXCHANGE, routingKey, MqConstants.GOODS_MESSAGE_ROUTINGKEY)) {
			System.out.println("Hand goods message " + messageContent);
		}
		
		return "success";
	}

}
