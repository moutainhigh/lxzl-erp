package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
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
    public void testCreateOrderJSON() throws Exception {
        String str = "{\"orderId\":3003015,\"orderNo\":\"LXO-20180910-1000-00037\",\"customerId\":705765,\"customerNo\":\"LXCC-027-20180824-00071\",\"address\":\"天津市天津和平区123\",\"logisticsCost\":\"200\",\"serviceCost\":\"200\",\"repairCost\":\"200\",\"replaceReasonType\":\"1\",\"replaceTime\":1536969600000,\"replaceMode\":\"1\",\"remark\":\"\",\"consigneeName\":\"收货测试员工\",\"consigneePhone\":\"18566324595\",\"replaceOrderProductList\":[{\"oldOrderProductId\":4415,\"oldProductEntry\":4415,\"rentType\":2,\"rentTimeLength\":6,\"rentLengthType\":2,\"depositCycle\":1,\"payMode\":2,\"paymentCycle\":3,\"oldProductUnitAmount\":149,\"depositAmount\":0,\"productId\":2000003,\"productName\":\"ThinkPad T430\",\"productSkuId\":1054,\"productSkuName\":\"CPU:i5 3代/内存:4G/机械硬盘:500G/固态硬盘:120G/显卡:集显/尺寸:14.1\",\"replaceProductCount\":\"4\",\"productUnitAmount\":\"200\",\"isNewProduct\":1,\"remark\":\"\"}]}";

        ReplaceOrder replaceOrder = FastJsonUtil.toBean(str, ReplaceOrder.class);
        TestResult testResult = getJsonTestResult("/replaceOrder/add", replaceOrder);

    }

}
