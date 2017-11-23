package com.lxzl.erp.core.service.message.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.message.pojo.Message;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.message.MessageDO;
import com.thoughtworks.xstream.mapper.Mapper;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageConverter {
    public static Message convertMessageDO(MessageDO messageDO){
        Message message = new Message();
        BeanUtils.copyProperties(messageDO,message);
        message.setMessageId(messageDO.getId());
        if (messageDO.getReadTime() ==  null){
            message.setIsRead(CommonConstant.COMMON_CONSTANT_NO);
        }else {
            message.setIsRead(CommonConstant.COMMON_CONSTANT_YES);
        }

        return message;
    }

    public static List<Message> convertMessageDOList(List<MessageDO> messageDOList){
        List<Message> messageList = new ArrayList<Message>();
        if (CollectionUtil.isNotEmpty(messageDOList)) {
            for (MessageDO messageDO : messageDOList) {
                messageList.add(convertMessageDO(messageDO));
            }
        }
        return messageList;
    }
}
