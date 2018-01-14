package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.TransferOrderMode;
import com.lxzl.erp.common.constant.TransferOrderType;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrder;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderMaterial;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderProduct;
import com.lxzl.erp.common.domain.transferOrder.TransferOrderQueryParam;
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
        transferOrder.setTransferOrderType(TransferOrderType.INTEGER_ORDER_TYPE_LEND_OUT_INTO_STOCK);
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

        TransferOrderProduct transferOrderProduct3 = new TransferOrderProduct();
        transferOrderProduct3.setProductSkuId(92);
        transferOrderProduct3.setProductCount(3);
        transferOrderProduct3.setIsNew(0);

        TransferOrderProduct transferOrderProduct4 = new TransferOrderProduct();
        transferOrderProduct4.setProductSkuId(96);
        transferOrderProduct4.setProductCount(2);
        transferOrderProduct4.setIsNew(1);

//        transferOrderProductList.add(transferOrderProduct1);
//        transferOrderProductList.add(transferOrderProduct2);
        transferOrderProductList.add(transferOrderProduct3);
//        transferOrderProductList.add(transferOrderProduct4);


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
        transferOrder.setTransferOrderMaterialList(TransferOrderMaterialList);

        TestResult testResult = getJsonTestResult("/transferOrder/createTransferOrderInto", transferOrder);
    }

    @Test
    public void createTransferOrderOut() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderName("转移单转出商品1234");
        transferOrder.setTransferOrderType(TransferOrderType.INTEGER_ORDER_TYPE_OUT);
        transferOrder.setRemark("转出123备注");

        TestResult testResult = getJsonTestResult("/transferOrder/createTransferOrderOut", transferOrder);
    }

    @Test
    public void updateTransferOrderInto() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT40000012018011386");
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

        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderId(96);
        transferOrder.setProductEquipmentNo("LX-1000--2018011001815");

        List<String> strings = new ArrayList<>();

        strings.add("LX-1000--2018011001810");
        strings.add("LX-1000--2018011001811");
        strings.add("LX-1000--2018011001812");
        strings.add("LX-1000--2018011001813");
        strings.add("LX-1000--2018011001814");
        strings.add("LX-1000--2018011001815");

        TestResult testResult = getJsonTestResult("/transferOrder/transferOrderProductEquipmentOut",transferOrder);

    }

    @Test
    public void dumpTransferOrderProductEquipmentOut() throws Exception{

        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderId(44);
        transferOrder.setProductEquipmentNo("LX-1000--2018011001812");

        TestResult testResult = getJsonTestResult("/transferOrder/dumpTransferOrderProductEquipmentOut",transferOrder);
    }

    @Test
    public void transferOrderMaterialOut() throws Exception{

        TransferOrderMaterial transferOrderMaterial = new TransferOrderMaterial();
        transferOrderMaterial.setTransferOrderId(96);
        transferOrderMaterial.setMaterialNo("M201711201500267591516");
        transferOrderMaterial.setMaterialCount(4);
        transferOrderMaterial.setIsNew(1);

        TestResult testResult = getJsonTestResult("/transferOrder/transferOrderMaterialOut",transferOrderMaterial);

    }

    @Test
    public void dumpTransferOrderMaterialOut() throws Exception{

        TransferOrderMaterial transferOrderMaterial = new TransferOrderMaterial();
        transferOrderMaterial.setTransferOrderId(45);
        transferOrderMaterial.setMaterialNo("M201711201500267591516");
        transferOrderMaterial.setMaterialCount(6);
        transferOrderMaterial.setIsNew(1);

        TestResult testResult = getJsonTestResult("/transferOrder/dumpTransferOrderMaterialOut",transferOrderMaterial);
    }

    @Test
    public void cancelTransferOrder() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT40000012018010922");

        TestResult testResult = getJsonTestResult("/transferOrder/cancelTransferOrder", transferOrder);
    }


    @Test
    public void endTransferOrder() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT40000012018010922");

        TestResult testResult = getJsonTestResult("/transferOrder/endTransferOrder", transferOrder);
    }

    @Test
    public void commitTransferOrder() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT40000012018011272");
        transferOrder.setVerifyUser(500006);
        transferOrder.setCommitRemark("提交转出转移单审核的备注");
        TestResult testResult = getJsonTestResult("/transferOrder/commitTransferOrder", transferOrder);
    }

    @Test
    public void pageTransferOrder() throws Exception{
        TransferOrderQueryParam transferOrderQueryParam = new TransferOrderQueryParam();
//        transferOrderQueryParam.setTransferOrderName("庄凯麟");
        transferOrderQueryParam.setTransferOrderNo("LXT40000012018011386");
        TestResult testResult = getJsonTestResult("/transferOrder/pageTransferOrder", transferOrderQueryParam);
    }

    @Test
    public void detailTransferOrderByNo() throws Exception{
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferOrderNo("LXT40000012018011386");
        TestResult testResult = getJsonTestResult("/transferOrder/detailTransferOrderByNo", transferOrder);
    }

    @Test
    public void detailTransferOrderProductEquipmentById() throws Exception{
        Integer transferOrderProductId = 220;

        TestResult testResult = getJsonTestResult("/transferOrder/detailTransferOrderProductEquipmentById", transferOrderProductId);
    }

    @Test
    public void detailTransferOrderMaterialBulkById() throws Exception{
        Integer transferOrderMaterialId = 227;

        TestResult testResult = getJsonTestResult("/transferOrder/detailTransferOrderMaterialBulkById", transferOrderMaterialId);
    }

}
