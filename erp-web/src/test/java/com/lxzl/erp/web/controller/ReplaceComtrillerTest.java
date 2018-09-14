package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.replace.ReplaceOrderQueryParam;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\13 0013 11:59
 */
public class ReplaceComtrillerTest extends ERPUnTransactionalTest {

    @Test
    public void testAddJSON() throws Exception {
        String str = "{\"orderId\":3002932,\"orderNo\":\"LXO-20180817-1000-00039\",\"customerId\":705743,\"customerNo\":\"LXCC-1000-20180817-00049\",\"address\":\"湖北省武汉市洪山区你家大门口\",\"logisticsCost\":\"200\",\"serviceCost\":\"200\",\"repairCost\":\"200\",\"replaceReasonType\":\"0\",\"replaceTime\":1537228800000,\"replaceMode\":\"2\",\"remark\":\"\",\"consigneeName\":\"咩咩\",\"consigneePhone\":\"18566324590\",\"replaceOrderProductList\":[{\"oldOrderProductId\":4312,\"oldProductEntry\":4312,\"rentType\":1,\"rentTimeLength\":15,\"rentLengthType\":1,\"depositCycle\":0,\"payMode\":1,\"paymentCycle\":0,\"oldProductUnitAmount\":10,\"depositAmount\":100,\"productId\":2000439,\"productName\":\"苹果IPADPRO-16G\",\"productSkuId\":1525,\"productSkuName\":\"CPU:双核/内存:16G/尺寸:9.7\",\"replaceProductCount\":\"1\",\"productUnitAmount\":\"210\",\"isNewProduct\":1,\"remark\":\"\"}]}";

        ReplaceOrder replaceOrder = FastJsonUtil.toBean(str, ReplaceOrder.class);
        TestResult testResult = getJsonTestResult("/replaceOrder/add", replaceOrder);
        System.out.println(JSON.toJSONString(testResult));

    }

    @Test
    public void testQueryAllReplaceOrderJSON() throws Exception {
        String str = "{\"pageNo\":1,\"pageSize\":15,\"createStartTime\":\"\",\"createEndTime\":\"\",\"createTimePicker\":\"\",\"replaceOrderStatus\":\"\",\"customerName\":\"\",\"customerNo\":\"\",\"replaceOrderNo\":\"\",\"orderNo\":\"\"}";
        ReplaceOrderQueryParam param = FastJsonUtil.toBean(str, ReplaceOrderQueryParam.class);
        TestResult testResult = getJsonTestResult("/replaceOrder/queryAllReplaceOrder", param);
        System.out.println(JSON.toJSONString(testResult));

    }
}
