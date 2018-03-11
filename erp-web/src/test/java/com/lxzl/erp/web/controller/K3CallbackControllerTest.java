package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrder;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-03-11 15:23
 */
public class K3CallbackControllerTest extends ERPTransactionalTest {


    @Test
    public void addMaterial() throws Exception {
        String str = "{\"deliveryOrderMaterialList\":[{\"deliveryMaterialCount\":4,\"isNew\":0,\"orderMaterialId\":1284,\"remark\":\"无 \"},{\"deliveryMaterialCount\":2,\"isNew\":0,\"orderMaterialId\":1285,\"remark\":\"无\"},{\"deliveryMaterialCount\":2,\"isNew\":0,\"orderMaterialId\":1286,\"remark\":\"无\"},{\"deliveryMaterialCount\":2,\"isNew\":0,\"orderMaterialId\":1287,\"remark\":\"无\"}],\"deliveryOrderProductList\":[{\"deliveryProductSkuCount\":2,\"isNew\":0,\"orderProductId\":630,\"remark\":\"无\"},{\"deliveryProductSkuCount\":2,\"isNew\":0,\"orderProductId\":631,\"remark\":\"无\"}],\"deliveryTimeStr\":\"2018/3/10 0:00:00\",\"deliveryUser\":\"黄颂裕\",\"orderNo\":\"LXO-20180310-0755-00443\",\"subCompanyCode\":\"01\"}";

        DeliveryOrder deliveryOrder = JSON.parseObject(str,DeliveryOrder.class );
        TestResult testResult = getJsonTestResult("/k3Callback/receiveDeliveryInfo",deliveryOrder);
    }
}
