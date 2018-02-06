package com.lxzl.erp.core.service.dingding;


import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.se.core.service.BaseService;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-25 19:59
 */
public interface DingdingService extends BaseService {

    /**
     * 叮叮消息推送信息
     *
     * @param userGroupUrl
     * @param request
     * @return
     */
    String sendUserGroupMessage(String userGroupUrl, DingdingSendTextMessageRequest request);
}
