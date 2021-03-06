package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.message.MessageBatchReadParam;
import com.lxzl.erp.common.domain.message.MessageQueryParam;
import com.lxzl.erp.common.domain.message.pojo.Message;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.message.MessageService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/message")
@Controller
@ControllerLog
public class MessageController extends BaseController{

    @Autowired
    private MessageService messageService;
    @Autowired
    private ResultGenerator resultGenerator;


    @RequestMapping(value = "sendMessage",method = RequestMethod.POST)
    public Result sendMessage(@RequestBody @Validated(AddGroup.class) Message message , BindingResult bindingResult){
        ServiceResult<String, Integer> serviceResult = messageService.sendMessage(message);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageSendMessage",method = RequestMethod.POST)
    public Result pageSendMessage(@RequestBody MessageQueryParam messageQueryParam){
        ServiceResult<String, Page<Message>> serviceResult = messageService.pageSendMessage(messageQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value="queryMessage",method = RequestMethod.POST)
    public Result updateMessage(@RequestBody @Validated(IdGroup.class)Message message, BindingResult bindingResult){
        ServiceResult<String, Message> serviceResult = messageService.queryMessage(message);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value="pageReceiveMessage",method = RequestMethod.POST)
    public Result pageReceiveMessage(@RequestBody MessageQueryParam messageQueryParam){
        ServiceResult<String, Page<Message>> serviceResult = messageService.pageReceiveMessage(messageQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "superSendMessage",method = RequestMethod.POST)
    public Result superSendMessage(@RequestBody @Validated(AddGroup.class) Message message , BindingResult bindingResult){
        ServiceResult<String, Integer> serviceResult = messageService.superSendMessage(message);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "noReadCount",method = RequestMethod.POST)
    public Result noReadCount(){
        ServiceResult<String, Integer> serviceResult = messageService.noReadCount();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "batchRead",method = RequestMethod.POST)
    public Result batchRead(@RequestBody @Validated(IdGroup.class)MessageBatchReadParam param, BindingResult bindingResult){
        ServiceResult<String, Integer> serviceResult = messageService.batchRead(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
