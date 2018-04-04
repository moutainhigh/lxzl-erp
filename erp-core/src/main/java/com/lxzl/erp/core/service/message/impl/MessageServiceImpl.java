package com.lxzl.erp.core.service.message.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.message.MessageBatchReadParam;
import com.lxzl.erp.common.domain.message.MessageQueryParam;
import com.lxzl.erp.common.domain.message.pojo.Message;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.message.MessageService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.message.MessageMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.message.MessageDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceResult<String, Integer> sendMessage(Message message) {
        return sendMessageDetail(message, userSupport.getCurrentUserId());
    }

    @Override
    public ServiceResult<String, Integer> superSendMessage(Message message) {

        return sendMessageDetail(message, CommonConstant.SUPER_USER_ID);
    }

    @Override
    public ServiceResult<String, Integer> superSendMessage(String title, String messageTest, Integer... userIds) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        if (userIds.length == 0) {
            result.setErrorCode(ErrorCode.MESSAGE_RECEIVER_NOT_NULL);
            return result;
        }
        Message message = new Message();
        message.setTitle(title);
        message.setMessageText(messageTest);
        List<Integer> receiverUserIdList = new ArrayList<>();
        Collections.addAll(receiverUserIdList, userIds);
        message.setReceiverUserIdList(receiverUserIdList);
        return sendMessageDetail(message, CommonConstant.SUPER_USER_ID);
    }

    private ServiceResult<String, Integer> sendMessageDetail(Message message, Integer senderId) {
        Date currentTime = new Date();
        ServiceResult<String, Integer> result = new ServiceResult<>();


        List<MessageDO> messageDOList = new ArrayList<>();
        //获取收信人的列表
        List<Integer> receiverIdList = message.getReceiverUserIdList();

        //将收信人进行去重操作
        Set<Integer> receiverIdSet = new HashSet<>();
        for (Integer receiverId : receiverIdList) {
            receiverIdSet.add(receiverId);
        }
        if (receiverIdSet.size() == 1 && receiverIdSet.iterator().next().equals(userSupport.getCurrentUserId())) {
            result.setErrorCode(ErrorCode.MESSAGE_CAN_NOT_SEND_SELF);
            return result;
        }
        //去重后的收信人保存到数据库表中
        for (Integer receiverId : receiverIdSet) {
            if (!receiverId.equals(userSupport.getCurrentUserId())) {
                UserDO user = userMapper.findByUserId(receiverId);
                if (user == null) {
                    result.setErrorCode(ErrorCode.USER_NAME_NOT_FOUND);
                    return result;
                }
                MessageDO messageDO = new MessageDO();
                messageDO.setSenderUserId(senderId);
                messageDO.setReceiverUserId(receiverId);
                messageDO.setSendTime(currentTime);
                messageDO.setTitle(message.getTitle());
                messageDO.setMessageText(message.getMessageText());
                messageDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                messageDO.setCreateTime(currentTime);
                messageDO.setCreateUser(userSupport.getCurrentUserId().toString());
                messageDO.setUpdateTime(currentTime);
                messageDO.setUpdateUser(userSupport.getCurrentUserId().toString());

                messageDOList.add(messageDO);
            }
        }
        if (CollectionUtil.isNotEmpty(messageDOList)) {
            messageMapper.batchSave(messageDOList);
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Message>> pageSendMessage(MessageQueryParam messageQueryParam) {
        ServiceResult<String, Page<Message>> result = new ServiceResult<>();
        //从分页参数中获取第几页，和每页页数的值，放入分页对象中
        PageQuery pageQuery = new PageQuery(messageQueryParam.getPageNo(), messageQueryParam.getPageSize());

        //将分页查询的条件存入map中
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("senderUserId", userSupport.getCurrentUserId());
        maps.put("messageQueryParam", messageQueryParam);

        //通过分页查询数据库，获取总的数据条数以及所有的数据
        Integer totalCount = messageMapper.findSendMessageCountByParams(maps);
        List<MessageDO> messageDOList = messageMapper.findSendMessageByParams(maps);

        for (MessageDO messageDO: messageDOList){
            if (messageDO.getReadTime() == null){
                messageDO.setIsRead(CommonConstant.COMMON_CONSTANT_NO);
            }else{
                messageDO.setIsRead(CommonConstant.COMMON_CONSTANT_YES);
            }
        }

        //将查询的数据转换为前端专用的数据
        List<Message> messageList = ConverterUtil.convertList(messageDOList, Message.class);
        Page<Message> page = new Page<>(messageList, totalCount, messageQueryParam.getPageNo(), messageQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);

        return result;
    }


    @Override
    public ServiceResult<String, Message> queryMessage(Message message) {
        ServiceResult<String, Message> result = new ServiceResult<>();
        Date now = new Date();

        //通过前端用户的Id来查询数据
        MessageDO messageDO = messageMapper.findById(message.getMessageId());
        if (messageDO == null) {
            result.setErrorCode(ErrorCode.MESSAGE_NOT_EXISTS);
            return result;
        }

        //如果当前用户与收件人是同一个人，那么就将站内信的读取时间设置为现在时间
        if (userSupport.getCurrentUserId().equals(messageDO.getReceiverUserId()) && messageDO.getReadTime() == null) {
            messageDO.setReadTime(new Date());
            messageDO.setUpdateTime(now);
            messageDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            messageMapper.update(messageDO);
        }

        Message messagePojo = ConverterUtil.convert(messageDO, Message.class);

        if (messageDO.getReadTime() != null){
            messagePojo.setIsRead(CommonConstant.YES);
        }else{
            messagePojo.setIsRead(CommonConstant.NO);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(messagePojo);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Message>> pageReceiveMessage(MessageQueryParam messageQueryParam) {
        ServiceResult<String, Page<Message>> result = new ServiceResult<>();
        //从分页参数中获取第几页，和每页页数的值，放入分页对象中
        PageQuery pageQuery = new PageQuery(messageQueryParam.getPageNo(), messageQueryParam.getPageSize());

        //将分页查询的条件存入map中
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("receiverUserId", userSupport.getCurrentUserId());
        maps.put("messageQueryParam", messageQueryParam);

        //通过分页查询数据库，获取总的数据条数以及所有的数据
        Integer totalCount = messageMapper.findReceiveMessageCountByParams(maps);
        List<MessageDO> messageDOList = messageMapper.findReceiveMessageByParams(maps);

        for (MessageDO messageDO: messageDOList){
            if (messageDO.getReadTime() == null){
                messageDO.setIsRead(CommonConstant.COMMON_CONSTANT_NO);
            }else{
                messageDO.setIsRead(CommonConstant.COMMON_CONSTANT_YES);
            }
        }

        //将查询的数据转换为前端专用的数据
        List<Message> messageList = ConverterUtil.convertList(messageDOList, Message.class);
        Page<Message> page = new Page<>(messageList, totalCount, messageQueryParam.getPageNo(), messageQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);

        return result;
    }


    @Override
    public ServiceResult<String, Integer> noReadCount() {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Integer noReadCount = messageMapper.findNotReadCount(userSupport.getCurrentUserId());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(noReadCount);

        return result;
    }

    @Override
    public ServiceResult<String, Integer> batchRead(MessageBatchReadParam param) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date currentTime = new Date();
        Integer userId = userSupport.getCurrentUserId();
        List<MessageDO> needUpdateList = new ArrayList<MessageDO>();
        List<Message> messageList = param.getMessageList();
        for (Message mess : messageList) {
            //通过前端用户的Id来查询数据
            MessageDO messageDO = messageMapper.findById(mess.getMessageId());
            if (messageDO == null) {
                result.setErrorCode(ErrorCode.MESSAGE_NOT_EXISTS);
                return result;
            }
            //当前消息已读，跳过
            if (messageDO.getReadTime() != null) continue;
            //不是自己的消息
            if (!userId.equals(messageDO.getReceiverUserId()) && !userSupport.isSuperUser()) {
                result.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
                return result;
            }
            //如果当前用户与收件人是同一个人，那么就将站内信的读取时间设置为现在时间
            messageDO.setReadTime(currentTime);
            messageDO.setUpdateUser(userId.toString());
            messageDO.setUpdateTime(currentTime);
            needUpdateList.add(messageDO);
        }
        messageMapper.batchUpdate(needUpdateList);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }
}




