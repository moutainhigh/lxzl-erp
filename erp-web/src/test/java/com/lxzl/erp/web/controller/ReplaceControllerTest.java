package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.replace.ReplaceOrderConfirmChangeParam;
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
public class ReplaceControllerTest extends ERPUnTransactionalTest {

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

    @Test
    public void testUpdateJSON() throws Exception {
        String str = "{\"replaceOrderId\":5,\"replaceOrderNo\":\"LXREO20180914111048191\",\"orderId\":3002932,\"orderNo\":\"LXO-20180817-1000-00039\",\"customerId\":705743,\"customerNo\":\"LXCC-1000-20180817-00049\",\"address\":\"湖北省武汉市洪山区你家大门口\",\"logisticsCost\":\"200\",\"serviceCost\":\"200\",\"repairCost\":\"200\",\"replaceReasonType\":\"0\",\"replaceTime\":1537228800000,\"replaceMode\":\"2\",\"remark\":\"\",\"consigneeName\":\"咩咩\",\"consigneePhone\":\"18566324590\",\"replaceOrderProductList\":[{\"oldOrderProductId\":4312,\"oldProductEntry\":4312,\"rentType\":1,\"rentTimeLength\":15,\"rentLengthType\":1,\"depositCycle\":0,\"payMode\":1,\"paymentCycle\":0,\"oldProductUnitAmount\":10,\"depositAmount\":100,\"productId\":2000439,\"productName\":\"苹果IPADPRO-16G\",\"productSkuId\":1525,\"productSkuName\":\"CPU:双核/内存:16G/尺寸:9.7\",\"replaceProductCount\":1,\"productUnitAmount\":210,\"isNewProduct\":1,\"remark\":\"\"}]}";

        ReplaceOrder replaceOrder = FastJsonUtil.toBean(str, ReplaceOrder.class);
        TestResult testResult = getJsonTestResult("/replaceOrder/update", replaceOrder);
        System.out.println(JSON.toJSONString(testResult));

    }

    @Test
    public void testConfirmReplaceOrderJSON() throws Exception {
        String str = "{\"deliveryNoteCustomerSignImg\":{\"imgId\":5749},\"replaceOrder\":{\"address\":\"湖北省武汉市洪山区你家大门口\",\"confirmReplaceUser\":\"\",\"consigneeName\":\"咩咩\",\"consigneePhone\":\"18566324590\",\"createTime\":1536894648000,\"createUser\":\"500001\",\"createUserRealName\":\"管理员\",\"customerId\":705743,\"customerName\":\"RedDoorzs\",\"customerNo\":\"LXCC-1000-20180817-00049\",\"dataStatus\":1,\"deliverySubCompanyId\":2,\"deliverySubCompanyName\":\"深圳分公司\",\"isReletOrderReplace\":0,\"logisticsCost\":200,\"newTotalCreditDepositAmount\":0,\"oldTotalCreditDepositAmount\":0,\"orderId\":3002932,\"orderNo\":\"LXO-20180817-1000-00039\",\"realTotalReplaceMaterialCount\":0,\"realTotalReplaceProductCount\":0,\"remark\":\"\",\"repairCost\":200,\"replaceMode\":2,\"replaceOrderId\":5,\"replaceOrderMaterialList\":[],\"replaceOrderNo\":\"LXREO20180914111048191\",\"replaceOrderProductList\":[{\"createTime\":1536894648000,\"createUser\":\"500001\",\"createUserRealName\":\"管理员\",\"creditDepositAmount\":0,\"dataStatus\":1,\"depositAmount\":100,\"depositCycle\":0,\"isNewProduct\":1,\"isReletOrderReplace\":0,\"oldIsNewProduct\":1,\"oldOrderProductId\":4312,\"oldProductEntry\":4312,\"oldProductId\":2000439,\"oldProductName\":\"苹果IPADPRO-16G\",\"oldProductNumber\":\"10.TPC.AP.IPADPRO-16G\",\"oldProductSkuId\":1525,\"oldProductSkuName\":\"CPU:双核/内存:16G/尺寸:9.7\",\"oldProductUnitAmount\":10,\"oldRentingProductCount\":10,\"payMode\":1,\"paymentCycle\":0,\"productId\":2000439,\"productName\":\"苹果IPADPRO-16G\",\"productNumber\":\"10.TPC.AP.IPADPRO-16G\",\"productSkuId\":1525,\"productSkuName\":\"CPU:双核/内存:16G/尺寸:9.7\",\"productUnitAmount\":210,\"realReplaceProductCount\":1,\"remark\":\"\",\"rentDepositAmount\":0,\"rentLengthType\":1,\"rentTimeLength\":15,\"rentType\":1,\"replaceOrderId\":5,\"replaceOrderNo\":\"LXREO20180914111048191\",\"replaceOrderProductId\":5,\"replaceProductCount\":1,\"updateTime\":1536894648000,\"updateUser\":\"500001\",\"updateUserRealName\":\"管理员\",\"serialNumber\":\"BjybCbgR\"}],\"replaceOrderStatus\":4,\"replaceReasonType\":0,\"replaceTime\":1537228800000,\"serviceCost\":200,\"totalReplaceMaterialCount\":0,\"totalReplaceProductCount\":1,\"updateTime\":1537340263000,\"updateTotalCreditDepositAmount\":0,\"updateUser\":\"500001\",\"updateUserRealName\":\"管理员\",\"realReplaceTime\":\"1537372800000\"}}";

        ReplaceOrderConfirmChangeParam param = FastJsonUtil.toBean(str, ReplaceOrderConfirmChangeParam.class);
        TestResult testResult = getJsonTestResult("/replaceOrder/confirmReplaceOrder", param);
        System.out.println(JSON.toJSONString(testResult));

    }
}
