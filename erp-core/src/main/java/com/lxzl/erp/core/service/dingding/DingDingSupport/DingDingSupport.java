package com.lxzl.erp.core.service.dingding.DingDingSupport;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.dingding.DingDingCommonMsg;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageContent;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DingdingPropertiesUtil;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.se.common.util.StringUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018-02-05 11:01
 */
@Component
public class DingDingSupport {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DingdingService dingdingService;

    public void dingDingSendMessage(String error){
        DingdingSendTextMessageRequest request = new DingdingSendTextMessageRequest();
        DingdingSendTextMessageContent content = new DingdingSendTextMessageContent();
        request.setMsgtype("text");
        content.setContent(error);
        request.setText(content);
        dingdingService.sendUserGroupMessage(DingdingPropertiesUtil.DINGDING_USER_GROUP_DEPARTMENT_DEVELOPER, request);
    }

    public void dingDingSendMessageLogInfo(String location , String error){
        log.info(location,error);
        DingdingSendTextMessageRequest request = new DingdingSendTextMessageRequest();
        DingdingSendTextMessageContent content = new DingdingSendTextMessageContent();
        request.setMsgtype("text");
        content.setContent(error);
        request.setText(content);
        dingdingService.sendUserGroupMessage(DingdingPropertiesUtil.DINGDING_USER_GROUP_DEPARTMENT_DEVELOPER, request);
    }

    public String getEnvironmentString(){
        String type = null;
        if ("erp-prod".equals(ApplicationConfig.application)) {
            type = "【线上环境】";
        } else if ("erp-dev".equals(ApplicationConfig.application)) {
            type = "【开发环境】";
        } else if ("erp-adv".equals(ApplicationConfig.application)) {
            type = "【预发环境】";
        } else if ("erp-test".equals(ApplicationConfig.application)) {
            type = "【测试环境】";
        }
        return type;
    }

    public void dingDingSendMessage(List<DingDingCommonMsg> dingDingCommonMsgs,Object... args){
        if(CollectionUtil.isEmpty(dingDingCommonMsgs))return;
        for (DingDingCommonMsg dingDingCommonMsg:dingDingCommonMsgs)dingDingSendMessage(dingDingCommonMsg,args);
    }

    public void dingDingSendMessage(DingDingCommonMsg dingDingCommonMsg,Object... args){
        if(dingDingCommonMsg==null|| StringUtil.isBlank(dingDingCommonMsg.getContent())|| StringUtil.isBlank(dingDingCommonMsg.getUserGroupUrl()))return;
        DingdingSendTextMessageRequest request = new DingdingSendTextMessageRequest();
        DingdingSendTextMessageContent content = new DingdingSendTextMessageContent();
        request.setMsgtype("text");
        String msgContent=dingDingCommonMsg.getContent();
        if(!ObjectUtils.isEmpty(args))msgContent=String.format(msgContent,args);
        content.setContent(msgContent);
        request.setText(content);
        dingdingService.sendUserGroupMessage(dingDingCommonMsg.getUserGroupUrl(), request);
    }

    @Test
    public void tt(){
        String temp="【%s】，分公司：%s，业务员：%s，客户名称：%s，订单：【%s】相关结算单重算成功，续重新支付，请及时操作。";
        String msgContent=String.format(temp,"结算单重算成功，续重新支付","武汉分公司","李量","利好","LXO-20180522-027-02675");

        DingdingSendTextMessageRequest request = new DingdingSendTextMessageRequest();
        DingdingSendTextMessageContent content = new DingdingSendTextMessageContent();
        request.setMsgtype("text");

        content.setContent(msgContent);
        request.setText(content);
        dingdingService.sendUserGroupMessage("https://oapi.dingtalk.com/robot/send?access_token=e31ffecf6018175f4f92e5a8a82848ba648db0dbbf50e66dab38bcb59a08f3b8", request);
    }

}
