package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostHelper {
    private static final Logger logger = LoggerFactory.getLogger(PostHelper.class);

    private static String URL_PREFIX = "http://";
    private static String DOMAIN = "103.239.207.170:9090";
    private static Map<Integer, String> METHOD_MAP = new HashMap<>();

    static {
        METHOD_MAP.put(PostK3Type.POST_K3_TYPE_CUSTOMER, "/api/Organization");
    }


    @Autowired
    private PostK3ServiceManager postK3ServiceManager;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;

    public void post(Integer postK3Type, Object data) {

        try {
            String url = URL_PREFIX + DOMAIN + METHOD_MAP.get(postK3Type);
            ConvertK3DataService convertK3DataService = postK3ServiceManager.getService(postK3Type);
            if (convertK3DataService == null) {
                logger.error("=============【 PostHelper ERROR 】=============");
                logger.error("【 ERROR INFO 】Non implementation class for postK3Type : " + postK3Type);
                return;
            }
            List<NameValuePair> params = convertK3DataService.getK3PostForm(data);
            threadPoolTaskExecutor.execute(new K3PostRunner(postK3Type, url, params, k3SendRecordMapper));
        } catch (Exception e) {
            logger.error("=============【 PostHelper ERROR 】=============");
            logger.error("【 ERROR INFO 】" + e);
        }

    }
}
