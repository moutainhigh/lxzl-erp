package com.lxzl.erp.core.service.dingding.impl;

import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.core.service.dingding.DingdingService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-25 20:10
 */
@Service("dingdingService")
public class DingdingServiceImpl implements DingdingService {
    private static final Logger logger = LoggerFactory.getLogger(DingdingServiceImpl.class);

    @Override
    public String sendUserGroupMessage(String userGroupUrl, DingdingSendTextMessageRequest request) {
        String respContent = null;
        try {
            HttpPost httpPost = new HttpPost(userGroupUrl);
            CloseableHttpClient client = HttpClients.createDefault();

            StringEntity entity = new StringEntity(FastJsonUtil.toJSONString(request), "UTF-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
            logger.info("dingding send message success,{},response : {}", respContent);
        } catch (Exception e) {
            logger.error("dingding send message error,{}", e);
        }

        return respContent;
    }
}
