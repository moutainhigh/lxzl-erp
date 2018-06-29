package com.lxzl.erp.core.service.dingding.DingDingSupport;

import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.dingding.DingDingCommonMsg;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageContent;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.util.DingdingPropertiesUtil;
import com.lxzl.erp.common.util.thread.ThreadFactoryDefault;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018-02-05 11:01
 */
@Component
public class DingDingSupport {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private ExecutorService sendDingdingMsgExecutor = Executors.newCachedThreadPool(new ThreadFactoryDefault("sendDingdingMsg"));

    @Autowired
    private DingdingService dingdingService;

    public void dingDingSendMessage(final String error){
        sendDingdingMsgExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DingdingSendTextMessageRequest request = new DingdingSendTextMessageRequest();
                DingdingSendTextMessageContent content = new DingdingSendTextMessageContent();
                request.setMsgtype("text");
                content.setContent(error);
                request.setText(content);
                dingdingService.sendUserGroupMessage(DingdingPropertiesUtil.DINGDING_USER_GROUP_DEPARTMENT_DEVELOPER, request);
            }
        });

    }

    public void dingDingSendMessageLogInfo(final String location ,final String error){
        sendDingdingMsgExecutor.execute(new Runnable() {
            @Override
            public void run() {
                log.info(location,error);
                DingdingSendTextMessageRequest request = new DingdingSendTextMessageRequest();
                DingdingSendTextMessageContent content = new DingdingSendTextMessageContent();
                request.setMsgtype("text");
                content.setContent(error);
                request.setText(content);
                dingdingService.sendUserGroupMessage(DingdingPropertiesUtil.DINGDING_USER_GROUP_DEPARTMENT_DEVELOPER, request);
            }
        });
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

    public void dingDingSendMessage(final DingDingCommonMsg dingDingCommonMsg,final Object... args){
        sendDingdingMsgExecutor.execute(new Runnable() {
            @Override
            public void run() {
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
        });

    }
}
