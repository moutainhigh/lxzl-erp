package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.returnOrder.AddReturnOrderParam;
import com.lxzl.erp.common.domain.returnOrder.DoReturnEquipmentParam;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderConsignInfo;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class ReturnOrderControllerTest extends ERPUnTransactionalTest{
    @Test
    public void create() throws Exception {
        AddReturnOrderParam addReturnOrderParam = new AddReturnOrderParam();
        addReturnOrderParam.setCustomerNo("CC201711230928540471145");
        List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setSkuId(2);
        productSku.setReturnCount(2);
        productSkuList.add(productSku);
        addReturnOrderParam.setProductSkuList(productSkuList);

        List<Material> materialList = new ArrayList<>();
        Material material = new Material();
        material.setMaterialId(5);
//        material.setReturnCount(1);
        materialList.add(material);
//        addReturnOrderParam.setMaterialList(materialList);
        addReturnOrderParam.setRemark("这是一条备注");

        ReturnOrderConsignInfo returnOrderConsignInfo = new ReturnOrderConsignInfo();
        returnOrderConsignInfo.setConsigneeName("张三");
        returnOrderConsignInfo.setConsigneePhone("13612342234");
        returnOrderConsignInfo.setAddress("深圳市宝安区");
        addReturnOrderParam.setReturnOrderConsignInfo(returnOrderConsignInfo);
        TestResult result = getJsonTestResult("/returnOrder/add",addReturnOrderParam);
    }
    @Test
    public void doReturnEquipment() throws Exception {
        DoReturnEquipmentParam doReturnEquipmentParam = new DoReturnEquipmentParam();
        doReturnEquipmentParam.setReturnOrderNo("RO201711281440283001372");
//        doReturnEquipmentParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010006");
        TestResult result = getJsonTestResult("/returnOrder/doReturnEquipment",doReturnEquipmentParam);
    }
}