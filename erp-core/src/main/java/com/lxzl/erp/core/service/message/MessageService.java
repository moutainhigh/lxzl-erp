package com.lxzl.erp.core.service.message;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.message.MessageQueryParam;
import com.lxzl.erp.common.domain.message.pojo.Message;
import com.lxzl.erp.dataaccess.domain.message.MessageDO;
import com.lxzl.se.core.service.BaseService;

public interface MessageService extends BaseService {
    /**
     *发送信息
     *
     * @param message 发送信息的内容
     * @return
     */
    ServiceResult<String,Integer> sendMessage(Message message);

    /**
     * 根据参数显示发送信息列表
     *
     * @param messageQueryParam 分页传递的参数
     * @return 信息列表
     */
    ServiceResult<String,Page<Message>> pageSendMessage(MessageQueryParam messageQueryParam);


    /**
     * 显示需要编辑的站内信内容
     *
     * @param message 需要编辑的站内行信息
     * @return
     */
    ServiceResult<String,Message> queryMessage(Message message);

    /**
     * 收件箱分页显示内容
     *
     * @param messageQueryParam 分页传递的参数
     * @return
     */
    ServiceResult<String,Page<Message>> pageReadMessage(MessageQueryParam messageQueryParam);

    /**
     *  管理者发送系统信息
     * @param message
     * @return
     */
    ServiceResult<String,Integer> superSendMessage(Message message);

    /**
     * 获取未阅读的站内信数量
     *@return
     */
    ServiceResult<String,Integer> noReadCount();
}
