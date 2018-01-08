package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.assembleOder.pojo.AssembleOrder;
import com.lxzl.erp.common.domain.assembleOder.pojo.AssembleOrderMaterial;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.product.ProductMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
public class AssembleOrderControllerTest extends ERPTransactionalTest {
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Test
    public void addAssembleOrder() throws Exception {
        AssembleOrder assembleOrder = new AssembleOrder();
        assembleOrder.setAssembleProductSkuId(40);
        assembleOrder.setWarehouseId(4000001);
        assembleOrder.setAssembleProductCount(2);

        ProductSkuDO productSkuDO = productSkuMapper.findById(40);
        List<ProductMaterialDO> productMaterialDOList = productSkuDO.getProductMaterialDOList();
        List<AssembleOrderMaterial> assembleOrderMaterialList = JSON.parseArray(JSON.toJSONString(productMaterialDOList),AssembleOrderMaterial.class);
        assembleOrder.setAssembleOrderMaterialList(assembleOrderMaterialList);
        TestResult result = getJsonTestResult("/assemble/add", assembleOrder);
    }

    @Test
    public void updateAssembleOrder() throws Exception {

    }

    @Test
    public void deleteAssembleOrder() throws Exception {
    }

    @Test
    public void queryAssembleOrderByAssembleOrderId() throws Exception {
        AssembleOrder assembleOrder = new AssembleOrder();
        assembleOrder.setAssembleOrderId(18);
        TestResult result = getJsonTestResult("/assemble/query", assembleOrder);
    }

    @Test
    public void pageAssembleOrder() throws Exception {
        AssembleOrder assembleOrder = new AssembleOrder();
        assembleOrder.setAssembleOrderId(18);
        TestResult result = getJsonTestResult("/assemble/page", assembleOrder);
    }

}