package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class PostHelper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String URL_PREFIX = "http://";
    private static String DOMAIN = "192.168.1.1";

    @Autowired
    private PostK3ServiceManager postK3ServiceManager;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;

    public void post(String url, Object data, Integer postK3Type) {

        try {
            url = URL_PREFIX + DOMAIN + url;
            ConvertK3DataService convertK3DataService = postK3ServiceManager.getService(postK3Type);
            if(convertK3DataService == null){
                logger.error("=============【 PostHelper ERROR 】=============");
                logger.error("【 ERROR INFO 】Non implementation class for postK3Type : " + postK3Type );
                return;
            }
            String json = convertK3DataService.getK3PostJson(data);
            threadPoolTaskExecutor.execute(new K3PostRunner(url, json, k3SendRecordMapper));
        }catch (Exception e){
            logger.error("=============【 PostHelper ERROR 】=============");
            logger.error("【 ERROR INFO 】"+e);
        }

    }
}
