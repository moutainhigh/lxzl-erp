package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/1/23 11:20
 */

@Component
public class WebServiceHelper {
    private static final Logger logger = LoggerFactory.getLogger(WebServiceHelper.class);
    @Autowired
    private PostK3ServiceManager postK3ServiceManager;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private K3SendRecordMapper k3SendRecordMapper;

    public void post(Integer postK3OperatorType , Integer postK3Type, Object data) {

        try {
            ConvertK3DataService convertK3DataService = postK3ServiceManager.getService(postK3Type);
            if (convertK3DataService == null) {
                logger.error("=============【 PostWebServiceHelper ERROR 】=============");
                logger.error("【 ERROR INFO 】Non implementation class for postK3Type : " + postK3Type);
                return;
            }
            Object postData = convertK3DataService.getK3PostWebServiceData(postK3OperatorType,data);
            threadPoolTaskExecutor.execute(new K3WebServicePostRunner(postK3Type, postData, k3SendRecordMapper));
        } catch (Exception e) {
            logger.error("=============【 PostWebServiceHelper ERROR 】=============");
            logger.error("【 ERROR INFO 】" + e);
        }

    }
}
