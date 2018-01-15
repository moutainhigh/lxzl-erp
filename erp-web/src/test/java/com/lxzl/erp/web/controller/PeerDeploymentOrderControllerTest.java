package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.DeliveryMode;
import com.lxzl.erp.common.constant.PeerDeploymentOrderRentType;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderConsignInfo;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderMaterial;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderProduct;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @Test
    public void createPeerDeploymentOrderTest() throws Exception {
        PeerDeploymentOrder peerDeploymentOrder = new PeerDeploymentOrder();
        peerDeploymentOrder.setPeerId(5);

        String str = "2018-1-15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = sdf.parse(str);

        peerDeploymentOrder.setRentStartTime(now);
        peerDeploymentOrder.setRentType(PeerDeploymentOrderRentType.RENT_TYPE_DAY);
        peerDeploymentOrder.setRentTimeLength(5);
        peerDeploymentOrder.setWarehouseId(4000001);
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
        peerDeploymentOrderProduct2.setProductSkuCount(1);
        peerDeploymentOrderProduct2.setProductUnitAmount(new BigDecimal(20));
        peerDeploymentOrderProduct2.setIsNew(0);
        peerDeploymentOrderProduct2.setRemark("kai创建同行调拨单商品测试");

        peerDeploymentOrderProductList.add(peerDeploymentOrderProduct);
        peerDeploymentOrderProductList.add(peerDeploymentOrderProduct2);
        peerDeploymentOrder.setPeerDeploymentOrderProductList(peerDeploymentOrderProductList);

        List<PeerDeploymentOrderMaterial> peerDeploymentOrderMaterialList = new ArrayList<>();
        PeerDeploymentOrderMaterial peerDeploymentOrderMaterial = new PeerDeploymentOrderMaterial();
        peerDeploymentOrderMaterial.setMaterialId(24);
        peerDeploymentOrderMaterial.setProductMaterialCount(2);
        peerDeploymentOrderMaterial.setMaterialUnitAmount(new BigDecimal(10));
        peerDeploymentOrderMaterial.setIsNew(1);
        peerDeploymentOrderMaterial.setRemark("kai创建同行调拨单配件测试");

        PeerDeploymentOrderMaterial peerDeploymentOrderMaterial2 = new PeerDeploymentOrderMaterial();
        peerDeploymentOrderMaterial2.setMaterialId(24);
        peerDeploymentOrderMaterial2.setProductMaterialCount(2);
        peerDeploymentOrderMaterial2.setMaterialUnitAmount(new BigDecimal(10));
        peerDeploymentOrderMaterial2.setIsNew(1);
        peerDeploymentOrderMaterial2.setRemark("kai创建同行调拨单配件测试");

        peerDeploymentOrderMaterialList.add(peerDeploymentOrderMaterial);
        peerDeploymentOrderMaterialList.add(peerDeploymentOrderMaterial2);
        peerDeploymentOrder.setPeerDeploymentOrderMaterialList(peerDeploymentOrderMaterialList);

        PeerDeploymentOrderConsignInfo peerDeploymentOrderConsignInfo = new PeerDeploymentOrderConsignInfo();

        peerDeploymentOrderConsignInfo.setContactName("kai");
        peerDeploymentOrderConsignInfo.setContactPhone("12345678900");
        peerDeploymentOrderConsignInfo.setProvince(19);
        peerDeploymentOrderConsignInfo.setCity(202);
        peerDeploymentOrderConsignInfo.setDistrict(1956);
        peerDeploymentOrderConsignInfo.setAddress("鬼屋");

        peerDeploymentOrder.setPeerDeploymentOrderConsignInfo(peerDeploymentOrderConsignInfo);

        TestResult testResult = getJsonTestResult("/peerDeploymentOrder/create", peerDeploymentOrder);
    }
}
