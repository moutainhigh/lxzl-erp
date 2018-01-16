package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.TransferOrderType;
import com.lxzl.erp.common.domain.transferOrder.*;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrder;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderMaterial;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderProduct;
import com.lxzl.erp.common.util.FastJsonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:46 2018/1/4
 * @Modified By:
 */
public class TransferOrderControllerTest extends ERPUnTransactionalTest  {

    @Test
    public void createTransferOrderInto() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderName("测试转移单转入");
        transferOrder.setTransferOrderType(TransferOrderType.INTEGER_ORDER_TYPE_INTO_LEND_OUT_INTO_STOCK);
        transferOrder.setRemark("转入的转移单备注");
        transferOrder.setWarehouseId(4000001);

        List<TransferOrderProduct> transferOrderProductList= new ArrayList<>();
        TransferOrderProduct transferOrderProduct1 = new TransferOrderProduct();
        transferOrderProduct1.setProductSkuId(57);
        transferOrderProduct1.setProductCount(3);
        transferOrderProduct1.setIsNew(0);

        TransferOrderProduct transferOrderProduct2 = new TransferOrderProduct();
        transferOrderProduct2.setProductSkuId(57);
        transferOrderProduct2.setProductCount(3);
        transferOrderProduct2.setIsNew(1);

        transferOrderProductList.add(transferOrderProduct1);
        transferOrderProductList.add(transferOrderProduct2);


        List<TransferOrderMaterial> TransferOrderMaterialList = new ArrayList<>();
        TransferOrderMaterial transferOrderMaterial1 = new TransferOrderMaterial();
        transferOrderMaterial1.setMaterialNo("M201711201501547451898");
        transferOrderMaterial1.setMaterialCount(3);
        transferOrderMaterial1.setIsNew(0);

        TransferOrderMaterial transferOrderMaterial2 = new TransferOrderMaterial();
        transferOrderMaterial2.setMaterialNo("M201711291547098231693");
        transferOrderMaterial2.setMaterialCount(3);
        transferOrderMaterial2.setIsNew(0);
        TransferOrderMaterialList.add(transferOrderMaterial1);
        TransferOrderMaterialList.add(transferOrderMaterial2);

        transferOrder.setTransferOrderProductList(transferOrderProductList);
//        transferOrder.setTransferOrderMaterialList(TransferOrderMaterialList);

        TestResult testResult = getJsonTestResult("/transferOrder/createTransferOrderInto", transferOrder);
    }

    @Test
    public void createTransferOrderOut() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderName("转移单转出商品pengbinjie");
        transferOrder.setTransferOrderType(TransferOrderType.INTEGER_ORDER_TYPE_OUT_SOLD);
        transferOrder.setRemark("转出123备注");

        TestResult testResult = getJsonTestResult("/transferOrder/createTransferOrderOut", transferOrder);
    }

    @Test
    public void updateTransferOrderInto() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT40000012018011488");
        transferOrder.setTransferOrderName("测试转移单转入update");
        transferOrder.setRemark("update测试备注");

        List<TransferOrderProduct> transferOrderProductList= new ArrayList<>();
//        TransferOrderProduct transferOrderProduct1 = new TransferOrderProduct();
//        transferOrderProduct1.setProductSkuId(57);
//        transferOrderProduct1.setProductCount(2);
//        transferOrderProduct1.setIsNew(0);
//
//        TransferOrderProduct transferOrderProduct2 = new TransferOrderProduct();
//        transferOrderProduct2.setProductSkuId(57);
//        transferOrderProduct2.setProductCount(2);
//        transferOrderProduct2.setIsNew(1);

        TransferOrderProduct transferOrderProduct3 = new TransferOrderProduct();
        transferOrderProduct3.setProductSkuId(96);
        transferOrderProduct3.setProductCount(4);
        transferOrderProduct3.setIsNew(0);

        TransferOrderProduct transferOrderProduct4 = new TransferOrderProduct();
        transferOrderProduct4.setProductSkuId(96);
        transferOrderProduct4.setProductCount(4);
        transferOrderProduct4.setIsNew(1);

//        transferOrderProductList.add(transferOrderProduct1);
//        transferOrderProductList.add(transferOrderProduct2);
//        transferOrderProductList.add(transferOrderProduct3);
        transferOrderProductList.add(transferOrderProduct4);

        List<TransferOrderMaterial> TransferOrderMaterialList = new ArrayList<>();
        TransferOrderMaterial transferOrderMaterial1 = new TransferOrderMaterial();
        transferOrderMaterial1.setMaterialNo("M201712251528189361350");
        transferOrderMaterial1.setMaterialCount(4);
        transferOrderMaterial1.setIsNew(1);

        TransferOrderMaterial transferOrderMaterial2 = new TransferOrderMaterial();
        transferOrderMaterial2.setMaterialNo("M201712251528189361350");
        transferOrderMaterial2.setMaterialCount(4);
        transferOrderMaterial2.setIsNew(0);

        TransferOrderMaterialList.add(transferOrderMaterial1);
        TransferOrderMaterialList.add(transferOrderMaterial2);

        transferOrder.setTransferOrderProductList(transferOrderProductList);
        transferOrder.setTransferOrderMaterialList(TransferOrderMaterialList);


        TestResult testResult = getJsonTestResult("/transferOrder/updateTransferOrderInto", transferOrder);
    }

    @Test
    public void updateTransferOrderOut() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT40000012018011129");
        transferOrder.setTransferOrderName("测试转移单转入update");
        transferOrder.setRemark("update测试备注");

        TestResult testResult = getJsonTestResult("/transferOrder/updateTransferOrderOut", transferOrder);
    }

    @Test
    public void transferOrderProductEquipmentOut() throws Exception{

        TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam = new TransferOrderProductEquipmentOutParam();
        transferOrderProductEquipmentOutParam.setTransferOrderNo("LXT-4000001-20180116-00104");
        transferOrderProductEquipmentOutParam.setProductEquipmentNo("LX-EQUIPMENT-4000001-2017121610115");

        List<String> strings = new ArrayList<>();

        strings.add("LX-1000--2018011402064");
        strings.add("LX-1000--2018011402065");
        strings.add("LX-1000--2018011402069");
        strings.add("LX-1000--2018011402070");
        strings.add("LX-1000--2018011402071");

        TestResult testResult = getJsonTestResult("/transferOrder/transferOrderProductEquipmentOut", transferOrderProductEquipmentOutParam);

    }

    @Test
    public void dumpTransferOrderProductEquipmentOut() throws Exception{

        TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam = new TransferOrderProductEquipmentOutParam();
        transferOrderProductEquipmentOutParam.setTransferOrderNo("LXT40000012018011492");
        transferOrderProductEquipmentOutParam.setProductEquipmentNo("LX-1000--2018011402071");

        TestResult testResult = getJsonTestResult("/transferOrder/dumpTransferOrderProductEquipmentOut", transferOrderProductEquipmentOutParam);
    }

    @Test
    public void testDumpTransferOrderProductEquipmentOut() throws Exception{

        String str = "{\n" +
                "\t\"transferOrderNo\": \"LXT40000012018011490\",\n" +
                "\t\"productEquipmentNo\": \"LX-EQUIPMENT-4000001-2017122210004\"\n" +
                "}";

        TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam =  FastJsonUtil.toBean(str, TransferOrderProductEquipmentOutParam.class);

        TestResult testResult = getJsonTestResult("/transferOrder/dumpTransferOrderProductEquipmentOut", transferOrderProductEquipmentOutParam);
    }

    @Test
    public void transferOrderMaterialOut() throws Exception{

        TransferOrderMaterialOutParam transferOrderMaterialOutParam = new TransferOrderMaterialOutParam();
        transferOrderMaterialOutParam.setTransferOrderNo("LXT40000012018011492");
        transferOrderMaterialOutParam.setMaterialNo("M201711291547098231693");
        transferOrderMaterialOutParam.setMaterialCount(5);
        transferOrderMaterialOutParam.setIsNew(0);
        transferOrderMaterialOutParam.setRemark("物料备货");

        TestResult testResult = getJsonTestResult("/transferOrder/transferOrderMaterialOut",transferOrderMaterialOutParam);

    }

    @Test
    public void dumpTransferOrderMaterialOut() throws Exception{

        TransferOrderMaterialOutParam transferOrderMaterialOutParam = new TransferOrderMaterialOutParam();
        transferOrderMaterialOutParam.setTransferOrderNo("LXT40000012018011492");
        transferOrderMaterialOutParam.setMaterialNo("M201711291547098231693");
        transferOrderMaterialOutParam.setMaterialCount(2);
        transferOrderMaterialOutParam.setIsNew(0);
        transferOrderMaterialOutParam.setRemark("物料清货货");

        TestResult testResult = getJsonTestResult("/transferOrder/dumpTransferOrderMaterialOut",transferOrderMaterialOutParam);
    }

    @Test
    public void cancelTransferOrder() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT-4000001-20180116-00104");

        TestResult testResult = getJsonTestResult("/transferOrder/cancelTransferOrder", transferOrder);
    }


//    @Test
//    public void endTransferOrder() throws Exception{
//        TransferOrder transferOrder = new TransferOrder();
//        transferOrder.setTransferOrderNo("LXT40000012018010922");
//
//        TestResult testResult = getJsonTestResult("/transferOrder/endTransferOrder", transferOrder);
//    }

    @Test
    public void commitTransferOrder() throws Exception{
        TransferOrderCommitParam transferOrderCommitParam = new TransferOrderCommitParam();
        transferOrderCommitParam.setTransferOrderNo("LXT-4000001-20180116-00104");
        transferOrderCommitParam.setVerifyUserId(500006);
        transferOrderCommitParam.setRemark("提交转出转移单审核的备注");
        TestResult testResult = getJsonTestResult("/transferOrder/commitTransferOrder", transferOrderCommitParam);
    }

    @Test
    public void pageTransferOrder() throws Exception{
        TransferOrderQueryParam transferOrderQueryParam = new TransferOrderQueryParam();
//        transferOrderQueryParam.setTransferOrderName("庄凯麟");
        transferOrderQueryParam.setTransferOrderNo("LXT40000012018011488");
        TestResult testResult = getJsonTestResult("/transferOrder/pageTransferOrder", transferOrderQueryParam);
    }

    @Test
    public void detailTransferOrderByNo() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT40000012018011488");
        TestResult testResult = getJsonTestResult("/transferOrder/detailTransferOrderByNo", transferOrder);
    }

    @Test
    public void detailTransferOrderProductEquipmentById() throws Exception{
        TransferOrderProductEquipmentQueryParam transferOrderProductEquipmentQueryParam = new TransferOrderProductEquipmentQueryParam();
        transferOrderProductEquipmentQueryParam.setTransferOrderProductId(254);

        TestResult testResult = getJsonTestResult("/transferOrder/detailTransferOrderProductEquipmentById", transferOrderProductEquipmentQueryParam);
    }

    @Test
    public void detailTransferOrderMaterialBulkById() throws Exception{
        TransferOrderMaterialBulkQueryParam transferOrderMaterialBulkQueryParam = new TransferOrderMaterialBulkQueryParam();
        transferOrderMaterialBulkQueryParam.setTransferOrderMaterialId(194);

        TestResult testResult = getJsonTestResult("/transferOrder/detailTransferOrderMaterialBulkById", transferOrderMaterialBulkQueryParam);
    }

}
