package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
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
public class TransferOrderControllerTest extends ERPUnTransactionalTest {

    @Test
    public void createTransferOrderInto() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderName("测试转移单转入");
        transferOrder.setTransferOrderType(TransferOrderType.INTEGER_ORDER_TYPE_INTO_LEND_OUT_INTO_STOCK);
        transferOrder.setRemark("peng转入的转移单备注");

        List<TransferOrderProduct> transferOrderProductList= new ArrayList<>();
        TransferOrderProduct transferOrderProduct1 = new TransferOrderProduct();
        transferOrderProduct1.setProductSkuId(57);
        transferOrderProduct1.setProductCount(20);
        transferOrderProduct1.setIsNew(0);

        TransferOrderProduct transferOrderProduct2 = new TransferOrderProduct();
        transferOrderProduct2.setProductSkuId(57);
        transferOrderProduct2.setProductCount(50);
        transferOrderProduct2.setIsNew(1);

//        transferOrderProductList.add(transferOrderProduct1);
//        transferOrderProductList.add(transferOrderProduct2);


        List<TransferOrderMaterial> TransferOrderMaterialList = new ArrayList<>();
        TransferOrderMaterial transferOrderMaterial1 = new TransferOrderMaterial();
        transferOrderMaterial1.setMaterialNo("M201712251528189361350");
        transferOrderMaterial1.setMaterialCount(3500);
        transferOrderMaterial1.setIsNew(0);

        TransferOrderMaterial transferOrderMaterial2 = new TransferOrderMaterial();
        transferOrderMaterial2.setMaterialNo("M201711291547098231693");
        transferOrderMaterial2.setMaterialCount(3);
        transferOrderMaterial2.setIsNew(0);
        TransferOrderMaterialList.add(transferOrderMaterial1);
//        TransferOrderMaterialList.add(transferOrderMaterial2);

        transferOrder.setTransferOrderProductList(transferOrderProductList);
        transferOrder.setTransferOrderMaterialList(TransferOrderMaterialList);

        TestResult testResult = getJsonTestResult("/transferOrder/createTransferOrderInto", transferOrder);
    }

    @Test
    public void createTransferOrderOut() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderName("转移单转出商品pengbinjie");
        transferOrder.setTransferOrderType(TransferOrderType.INTEGER_ORDER_TYPE_OUT_SOLD);
        transferOrder.setRemark("转出1234备注");

        TestResult testResult = getJsonTestResult("/transferOrder/createTransferOrderOut", transferOrder);
    }

    @Test
    public void updateTransferOrderInto() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT-4000001-20180119-00117");
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
        transferOrderProduct4.setProductCount(2000);
        transferOrderProduct4.setIsNew(1);

//        transferOrderProductList.add(transferOrderProduct1);
//        transferOrderProductList.add(transferOrderProduct2);
//        transferOrderProductList.add(transferOrderProduct3);
//        transferOrderProductList.add(transferOrderProduct4);

        List<TransferOrderMaterial> TransferOrderMaterialList = new ArrayList<>();
        TransferOrderMaterial transferOrderMaterial1 = new TransferOrderMaterial();
        transferOrderMaterial1.setMaterialNo("M201712251528189361350");
        transferOrderMaterial1.setMaterialCount(4);
        transferOrderMaterial1.setIsNew(1);

        TransferOrderMaterial transferOrderMaterial2 = new TransferOrderMaterial();
        transferOrderMaterial2.setMaterialNo("M201712251528189361350");
        transferOrderMaterial2.setMaterialCount(2000);
        transferOrderMaterial2.setIsNew(0);

//        TransferOrderMaterialList.add(transferOrderMaterial1);
        TransferOrderMaterialList.add(transferOrderMaterial2);

        transferOrder.setTransferOrderProductList(transferOrderProductList);
        transferOrder.setTransferOrderMaterialList(TransferOrderMaterialList);


        TestResult testResult = getJsonTestResult("/transferOrder/updateTransferOrderInto", transferOrder);
    }

    @Test
    public void updateTransferOrderOut() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT-4000001-20180122-00127");
        transferOrder.setTransferOrderName("测试转移单转入update");
        transferOrder.setRemark("update测试备注");

        TestResult testResult = getJsonTestResult("/transferOrder/updateTransferOrderOut", transferOrder);
    }

    @Test
    public void transferOrderProductEquipmentOut() throws Exception{

        TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam = new TransferOrderProductEquipmentOutParam();
        transferOrderProductEquipmentOutParam.setTransferOrderNo("LXT-4000001-20180123-00142");
        transferOrderProductEquipmentOutParam.setProductEquipmentNo("LXE-1000--20180119-10262");


        TestResult testResult = getJsonTestResult("/transferOrder/transferOrderProductEquipmentOut", transferOrderProductEquipmentOutParam);

    }

    @Test
    public void dumpTransferOrderProductEquipmentOut() throws Exception{

        TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam = new TransferOrderProductEquipmentOutParam();
        transferOrderProductEquipmentOutParam.setTransferOrderNo("LXT-4000001-20180120-00121");
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
        transferOrderMaterialOutParam.setTransferOrderNo("LXT-4000001-20180123-00146");

        transferOrderMaterialOutParam.setMaterialNo("M201712251528189361350");
        transferOrderMaterialOutParam.setMaterialCount(3500);
        transferOrderMaterialOutParam.setIsNew(0);
        transferOrderMaterialOutParam.setRemark("物料备货");

        long startTime = System.currentTimeMillis();
        TestResult testResult = getJsonTestResult("/transferOrder/transferOrderMaterialOut",transferOrderMaterialOutParam);
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");

    }

    @Test
    public void dumpTransferOrderMaterialOut() throws Exception{

        TransferOrderMaterialOutParam transferOrderMaterialOutParam = new TransferOrderMaterialOutParam();
        transferOrderMaterialOutParam.setTransferOrderNo("LXT-4000001-20180122-00139");
        transferOrderMaterialOutParam.setMaterialNo("M201712251528189361350");
        transferOrderMaterialOutParam.setMaterialCount(27);
        transferOrderMaterialOutParam.setIsNew(0);
        transferOrderMaterialOutParam.setRemark("物料清货货");
        long startTime = System.currentTimeMillis();
        TestResult testResult = getJsonTestResult("/transferOrder/dumpTransferOrderMaterialOut",transferOrderMaterialOutParam);
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
    }

    @Test
    public void cancelTransferOrder() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT-4000001-20180123-00146");

        TestResult testResult = getJsonTestResult("/transferOrder/cancelTransferOrder", transferOrder);
    }


    @Test
    public void commitTransferOrder() throws Exception{
        TransferOrderCommitParam transferOrderCommitParam = new TransferOrderCommitParam();
        transferOrderCommitParam.setTransferOrderNo("LXT-4000001-20180123-00146");
        transferOrderCommitParam.setVerifyUserId(500006);
        transferOrderCommitParam.setRemark("提交转出转移单审核的备注");

        TestResult testResult = getJsonTestResult("/transferOrder/commitTransferOrder", transferOrderCommitParam);
    }

    @Test
    public void pageTransferOrder() throws Exception{
        TransferOrderQueryParam transferOrderQueryParam = new TransferOrderQueryParam();
//        transferOrderQueryParam.setTransferOrderName("庄凯麟");
//        transferOrderQueryParam.setTransferOrderNo("LXT400000120");
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
