package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannel;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.messagethirdchannel.MessageThirdChannelService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author daiqi
 * @create 2018-05-21 15:21
 */
@RequestMapping("/messageThirdChannel")
@Controller
@ControllerLog
public class MessageThirdChannelController extends BaseController {
    @Autowired
    private MessageThirdChannelService messageThirdChannelService;
    @Autowired
    private ResultGenerator resultGenerator;

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
     *     messageThirdChannel.messageType : 1 : 不传默认系统消息 : 否
     *     messageThirdChannel.messageChannel : 1 : 消息渠道 默认钉钉 : 否
     *     messageThirdChannel.senderUserId : -1 : 发送者用户编号 : 否
     *     messageThirdChannel.senderRemark : System : 发送者备注 : 否
     * </pre>
     *
     * @param messageThirdChannel
     * @param bindingResult
     * @return com.lxzl.se.common.domain.Result
     * @author daiqi
     * @date 2018/5/21 15:24
     */
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST)
    public Result sendMessage(@RequestBody MessageThirdChannel messageThirdChannel, BindingResult bindingResult) {
        ServiceResult<String, Object> serviceResult = messageThirdChannelService.sendMessage(messageThirdChannel);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
