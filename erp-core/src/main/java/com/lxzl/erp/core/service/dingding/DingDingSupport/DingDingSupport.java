package com.lxzl.erp.core.service.dingding.DingDingSupport;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageContent;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.util.DingdingPropertiesUtil;
import com.lxzl.erp.core.service.dingding.DingdingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


}
