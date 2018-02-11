package com.lxzl.erp.core.service.k3;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        K3SendRecordDO k3SendRecordDO = new K3SendRecordDO();
        try {
            ConvertK3DataService convertK3DataService = postK3ServiceManager.getService(postK3Type);
            Object postData = convertK3DataService.getK3PostWebServiceData(postK3OperatorType,data);
            //创建推送记录，此时发送状态失败，接收状态失败
            k3SendRecordDO.setRecordType(postK3Type);
            k3SendRecordDO.setRecordJson(JSON.toJSONString(postData));
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_NO);
            k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
            k3SendRecordDO.setSendTime(new Date());
            k3SendRecordMapper.save(k3SendRecordDO);
            threadPoolTaskExecutor.execute(new K3WebServicePostRunner(postK3Type, postData, k3SendRecordMapper,k3SendRecordDO));
        } catch (Exception e) {
            logger.error("=============【 PostWebServiceHelper ERROR 】=============");
            logger.error("【 ERROR INFO 】" + e);
        }

    }
}
