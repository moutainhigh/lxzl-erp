package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.DeliveryMode;
import com.lxzl.erp.common.constant.PeerDeploymentOrderRentType;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderCommitParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderMaterialBulkQueryGroup;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderProductEquipmentQueryGroup;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderConsignInfo;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderMaterial;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderProduct;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderDO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 同行调拨单单测试类
 *
 * @author kai
 * @date 2018-1-15 09:40
 */
public class PeerDeploymentOrderControllerTest extends ERPUnTransactionalTest {

    /**
     * Test 创建同行调拨单
     * @throws Exception
     */
    @Test
    public void createPeerDeploymentOrderTest() throws Exception {
        PeerDeploymentOrder peerDeploymentOrder = new PeerDeploymentOrder();
        peerDeploymentOrder.setPeerNo("LXPEER099900003");

//        String str = "2018-1-15";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date rentStartTime = sdf.parse(str);

        peerDeploymentOrder.setRentStartTime(new Date());
        peerDeploymentOrder.setRentType(PeerDeploymentOrderRentType.RENT_TYPE_DAY);
        peerDeploymentOrder.setRentTimeLength(5);
//        peerDeploymentOrder.setWarehouseId(4000001);
        peerDeploymentOrder.setWarehouseNo("LXW10001");
        peerDeploymentOrder.setDeliveryMode(DeliveryMode.DELIVERY_MODE_SINCE);
        peerDeploymentOrder.setTaxRate(0.17);
        peerDeploymentOrder.setRemark("kai创建同行调拨单测试");

        List<PeerDeploymentOrderProduct> peerDeploymentOrderProductList = new ArrayList<>();
        PeerDeploymentOrderProduct peerDeploymentOrderProduct = new PeerDeploymentOrderProduct();
        peerDeploymentOrderProduct.setProductSkuId(40);
        peerDeploymentOrderProduct.setProductSkuCount(2);
        peerDeploymentOrderProduct.setProductUnitAmount(new BigDecimal(50));
        peerDeploymentOrderProduct.setIsNew(1);
        peerDeploymentOrderProduct.setRemark("kai创建同行调拨单商品测试");

        PeerDeploymentOrderProduct peerDeploymentOrderProduct2 = new PeerDeploymentOrderProduct();
        peerDeploymentOrderProduct2.setProductSkuId(40);
        peerDeploymentOrderProduct2.setProductSkuCount(3);
        peerDeploymentOrderProduct2.setProductUnitAmount(new BigDecimal(20));
        peerDeploymentOrderProduct2.setIsNew(0);
        peerDeploymentOrderProduct2.setRemark("kai创建同行调拨单商品测试");

        peerDeploymentOrderProductList.add(peerDeploymentOrderProduct);
        peerDeploymentOrderProductList.add(peerDeploymentOrderProduct2);
//        peerDeploymentOrder.setPeerDeploymentOrderProductList(peerDeploymentOrderProductList);

        List<PeerDeploymentOrderMaterial> peerDeploymentOrderMaterialList = new ArrayList<>();
        PeerDeploymentOrderMaterial peerDeploymentOrderMaterial = new PeerDeploymentOrderMaterial();
//        peerDeploymentOrderMaterial.setMaterialId(24);
        peerDeploymentOrderMaterial.setMaterialNo("M201711291745413251585");
        peerDeploymentOrderMaterial.setMaterialCount(2);
        peerDeploymentOrderMaterial.setMaterialUnitAmount(new BigDecimal(10));
        peerDeploymentOrderMaterial.setIsNew(1);
        peerDeploymentOrderMaterial.setRemark("kai创建同行调拨单配件测试");

        PeerDeploymentOrderMaterial peerDeploymentOrderMaterial2 = new PeerDeploymentOrderMaterial();
//        peerDeploymentOrderMaterial2.setMaterialId(24);
        peerDeploymentOrderMaterial2.setMaterialNo("M201711291745413251585");
        peerDeploymentOrderMaterial2.setMaterialCount(2);
        peerDeploymentOrderMaterial2.setMaterialUnitAmount(new BigDecimal(10));
        peerDeploymentOrderMaterial2.setIsNew(0);
        peerDeploymentOrderMaterial2.setRemark("kai创建同行调拨单配件测试");

        peerDeploymentOrderMaterialList.add(peerDeploymentOrderMaterial);
        peerDeploymentOrderMaterialList.add(peerDeploymentOrderMaterial2);
        peerDeploymentOrder.setPeerDeploymentOrderMaterialList(peerDeploymentOrderMaterialList);

        PeerDeploymentOrderConsignInfo peerDeploymentOrderConsignInfo = new PeerDeploymentOrderConsignInfo();

//        peerDeploymentOrderConsignInfo.setContactName("kai");
//        peerDeploymentOrderConsignInfo.setContactPhone("12345678900");
//        peerDeploymentOrderConsignInfo.setProvince(19);
//        peerDeploymentOrderConsignInfo.setCity(202);
//        peerDeploymentOrderConsignInfo.setDistrict(1956);
//        peerDeploymentOrderConsignInfo.setAddress("鬼屋");

        peerDeploymentOrder.setPeerDeploymentOrderConsignInfo(peerDeploymentOrderConsignInfo);

        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/create", peerDeploymentOrder);
    }

    /**
     * Test修改调拨单
     * @throws Exception
     */
    @Test
    public void updatePeerDeploymentOrderTest() throws Exception {
        PeerDeploymentOrder peerDeploymentOrder = new PeerDeploymentOrder();

//        peerDeploymentOrder.setPeerDeploymentOrderId(9000044);
        peerDeploymentOrder.setPeerDeploymentOrderNo("LXPDO-0471-20180118-0021");

        peerDeploymentOrder.setPeerNo("LXS031700019");

//        String str = "2018-1-19";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date rentStartTime = sdf.parse(str);

        peerDeploymentOrder.setRentStartTime(new Date());
        peerDeploymentOrder.setRentType(PeerDeploymentOrderRentType.RENT_TYPE_MONTH);
        peerDeploymentOrder.setRentTimeLength(2);
//        peerDeploymentOrder.setWarehouseId(4000001);
        peerDeploymentOrder.setWarehouseNo("LXW10001");
        peerDeploymentOrder.setDeliveryMode(DeliveryMode.DELIVERY_MODE_SINCE);
        peerDeploymentOrder.setTaxRate(0.06);
        peerDeploymentOrder.setRemark("kai修改同行调拨单测试");

        List<PeerDeploymentOrderProduct> peerDeploymentOrderProductList = new ArrayList<>();
        PeerDeploymentOrderProduct peerDeploymentOrderProduct = new PeerDeploymentOrderProduct();
        peerDeploymentOrderProduct.setProductSkuId(92);
        peerDeploymentOrderProduct.setProductSkuCount(1);
        peerDeploymentOrderProduct.setProductUnitAmount(new BigDecimal(60));
        peerDeploymentOrderProduct.setIsNew(1);
        peerDeploymentOrderProduct.setRemark("kai修改同行调拨单商品测试");

        PeerDeploymentOrderProduct peerDeploymentOrderProduct2 = new PeerDeploymentOrderProduct();
        peerDeploymentOrderProduct2.setProductSkuId(92);
        peerDeploymentOrderProduct2.setProductSkuCount(1);
        peerDeploymentOrderProduct2.setProductUnitAmount(new BigDecimal(20));
        peerDeploymentOrderProduct2.setIsNew(0);
        peerDeploymentOrderProduct2.setRemark("kai修改同行调拨单商品测试");

        peerDeploymentOrderProductList.add(peerDeploymentOrderProduct);
        peerDeploymentOrderProductList.add(peerDeploymentOrderProduct2);
        peerDeploymentOrder.setPeerDeploymentOrderProductList(peerDeploymentOrderProductList);

        List<PeerDeploymentOrderMaterial> peerDeploymentOrderMaterialList = new ArrayList<>();
        PeerDeploymentOrderMaterial peerDeploymentOrderMaterial = new PeerDeploymentOrderMaterial();
        peerDeploymentOrderMaterial.setMaterialNo("M201711171838059981292");
        peerDeploymentOrderMaterial.setMaterialCount(2);
        peerDeploymentOrderMaterial.setMaterialUnitAmount(new BigDecimal(10));
        peerDeploymentOrderMaterial.setIsNew(1);
        peerDeploymentOrderMaterial.setRemark("kai修改同行调拨单配件测试");

        PeerDeploymentOrderMaterial peerDeploymentOrderMaterial2 = new PeerDeploymentOrderMaterial();
        peerDeploymentOrderMaterial2.setMaterialNo("M201712250956366751399");
        peerDeploymentOrderMaterial2.setMaterialCount(2);
        peerDeploymentOrderMaterial2.setMaterialUnitAmount(new BigDecimal(15));
        peerDeploymentOrderMaterial2.setIsNew(0);
        peerDeploymentOrderMaterial2.setRemark("kai修改同行调拨单配件测试");

        peerDeploymentOrderMaterialList.add(peerDeploymentOrderMaterial);
        peerDeploymentOrderMaterialList.add(peerDeploymentOrderMaterial2);
        peerDeploymentOrder.setPeerDeploymentOrderMaterialList(peerDeploymentOrderMaterialList);

        PeerDeploymentOrderConsignInfo peerDeploymentOrderConsignInfo = new PeerDeploymentOrderConsignInfo();

        peerDeploymentOrderConsignInfo.setContactName("kai修改");
        peerDeploymentOrderConsignInfo.setContactPhone("666");
        peerDeploymentOrderConsignInfo.setProvince(19);
        peerDeploymentOrderConsignInfo.setCity(202);
        peerDeploymentOrderConsignInfo.setDistrict(1956);
        peerDeploymentOrderConsignInfo.setAddress("鬼屋-修改");

        peerDeploymentOrder.setPeerDeploymentOrderConsignInfo(peerDeploymentOrderConsignInfo);

        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/update", peerDeploymentOrder);
    }

    /**
     * Test提交同行调拨单
     * @throws Exception
     */
    @Test
    public void commitPeerDeploymentOrderIntoTest() throws Exception {
        PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam = new PeerDeploymentOrderCommitParam();
        peerDeploymentOrderCommitParam.setPeerDeploymentOrderNo("LXPDO-0471-20180118-0014");
        peerDeploymentOrderCommitParam.setVerifyUserId(500006);
        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/commitPeerDeploymentOrderInto",peerDeploymentOrderCommitParam);
    }
    /**
     * Test确认收货同行调拨单
     * @throws Exception
     */
    @Test
    public void confirmPeerDeploymentOrderIntoTest() throws Exception {
        PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam = new PeerDeploymentOrderCommitParam();
        peerDeploymentOrderCommitParam.setPeerDeploymentOrderNo("LXPDO-0452-20180118-0013");
        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/confirmPeerDeploymentOrderInto",peerDeploymentOrderCommitParam);
    }

    @Test
    public void cancelPeerDeploymentOrderTest() throws Exception {
        PeerDeploymentOrderDO peerDeploymentOrderDO = new PeerDeploymentOrderDO();
        peerDeploymentOrderDO.setPeerDeploymentOrderNo("LXPDO-0317-20180117-0006");
        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/cancel",peerDeploymentOrderDO);
    }

    /**
     * Test提交同行调拨单
     * @throws Exception
     */
    @Test
    public void commitPeerDeploymentOrderReturn() throws Exception {
        PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam = new PeerDeploymentOrderCommitParam();
        peerDeploymentOrderCommitParam.setPeerDeploymentOrderNo("LXPDO-0452-20180118-0013");
        peerDeploymentOrderCommitParam.setVerifyUserId(500006);
        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/commitPeerDeploymentOrderReturn",peerDeploymentOrderCommitParam);
    }

    @Test
    public void endPeerDeploymentOrderOut() throws Exception {
        PeerDeploymentOrder peerDeploymentOrder = new PeerDeploymentOrder();
        peerDeploymentOrder.setPeerDeploymentOrderNo("LXPDO-0452-20180118-0013");
        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/endPeerDeploymentOrderOut",peerDeploymentOrder);
    }


    @Test
    public void page() throws Exception {
        PeerDeploymentOrderQueryParam peerDeploymentOrderQueryParam = new PeerDeploymentOrderQueryParam();
        peerDeploymentOrderQueryParam.setPageNo(1);
        peerDeploymentOrderQueryParam.setPageSize(5);
//        peerDeploymentOrderQueryParam.setRentType(2);
//        peerDeploymentOrderQueryParam.setPeerDeploymentOrderNo("LXPDO0317201801162");
        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/page",peerDeploymentOrderQueryParam);
    }

    @Test
    public void detailPeerDeploymentOrder() throws Exception {
        PeerDeploymentOrder peerDeploymentOrder = new PeerDeploymentOrder();
        peerDeploymentOrder.setPeerDeploymentOrderNo("LXPDO-0317-20180116-0004");

        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/detailPeerDeploymentOrder",peerDeploymentOrder);
    }

    @Test
    public void detailPeerDeploymentOrderProductEquipment() throws Exception {
        PeerDeploymentOrderProductEquipmentQueryGroup peerDeploymentOrderProductEquipmentQueryGroup = new PeerDeploymentOrderProductEquipmentQueryGroup();
        peerDeploymentOrderProductEquipmentQueryGroup.setPeerDeploymentOrderProductId(153);

        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/detailPeerDeploymentOrderProductEquipment",peerDeploymentOrderProductEquipmentQueryGroup);
    }

    @Test
    public void detailPeerDeploymentOrderMaterialBulk() throws Exception {
        PeerDeploymentOrderMaterialBulkQueryGroup peerDeploymentOrderMaterialBulkQueryGroup = new PeerDeploymentOrderMaterialBulkQueryGroup();
        peerDeploymentOrderMaterialBulkQueryGroup.setPeerDeploymentOrderMaterialId(130);

        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/detailPeerDeploymentOrderMaterialBulk",peerDeploymentOrderMaterialBulkQueryGroup);
    }


}
