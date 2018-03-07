package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.PostK3Type;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private DingDingSupport dingdingSupport;

    public void post(Integer postK3OperatorType, Integer postK3Type, Object data,boolean skipSuccess) {


        try {
            Integer refId = null;
            Field[] doFields = data.getClass().getDeclaredFields();

            Map<String, Field> doFiledMap = new HashMap<>();
            for (Field field : doFields) {
                field.setAccessible(true);
                doFiledMap.put(field.getName(), field);
            }
            if (postK3Type.equals(PostK3Type.POST_K3_TYPE_CUSTOMER)) {
                refId = (Integer) doFiledMap.get("customerId").get(data);
            } else if (postK3Type.equals(PostK3Type.POST_K3_TYPE_PRODUCT)) {
                refId = (Integer) doFiledMap.get("productId").get(data);
            } else if (postK3Type.equals(PostK3Type.POST_K3_TYPE_MATERIAL)) {
                refId = (Integer) doFiledMap.get("materialId").get(data);
            } else if (postK3Type.equals(PostK3Type.POST_K3_TYPE_SUPPLIER)) {
                refId = (Integer) doFiledMap.get("supplierId").get(data);
            } else if (postK3Type.equals(PostK3Type.POST_K3_TYPE_ORDER)) {
                refId = (Integer) doFiledMap.get("orderId").get(data);
            } else if (postK3Type.equals(PostK3Type.POST_K3_TYPE_USER)) {
                refId = (Integer) doFiledMap.get("userId").get(data);
            } else if (postK3Type.equals(PostK3Type.POST_K3_TYPE_K3_RETURN_ORDER)) {
                refId = (Integer) doFiledMap.get("k3ReturnOrderId").get(data);
            }
            K3SendRecordDO k3SendRecordDO = k3SendRecordMapper.findByReferIdAndType(refId, postK3Type);

            if (k3SendRecordDO == null) {
                //创建推送记录，此时发送状态失败，接收状态失败
                k3SendRecordDO = new K3SendRecordDO();
                k3SendRecordDO.setRecordType(postK3Type);
                k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_NO);
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
                k3SendRecordDO.setSendTime(new Date());
                k3SendRecordDO.setRecordReferId(refId);
                k3SendRecordMapper.save(k3SendRecordDO);
            }
            //成功过的不再发
            if (skipSuccess && CommonConstant.COMMON_CONSTANT_YES.equals(k3SendRecordDO.getReceiveResult())) {
                return;
            }
            ConvertK3DataService convertK3DataService = postK3ServiceManager.getService(postK3Type);
            Object postData = convertK3DataService.getK3PostWebServiceData(postK3OperatorType, data);

            threadPoolTaskExecutor.execute(new K3WebServicePostRunner(postK3Type, postData, k3SendRecordMapper, k3SendRecordDO, convertK3DataService,dingdingSupport));
        } catch (Exception e) {
            logger.error("=============【 PostWebServiceHelper ERROR 】=============");
            logger.error("【 ERROR INFO 】", e);
        }

    }
}
