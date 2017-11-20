package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.StockCauseType;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipmentOutStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.ProductOutStockParam;
import com.lxzl.erp.common.domain.warehouse.StockOrderQueryParam;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 18:18
 */
public class WarehouseTest extends ERPUnTransactionalTest {

    @Test
    public void getWarehousePage() throws Exception {
        WarehouseQueryParam warehouseQueryParam = new WarehouseQueryParam();
        warehouseQueryParam.setPageNo(1);
        warehouseQueryParam.setPageSize(20);
        TestResult result = getJsonTestResult("/warehouse/getWarehousePage", warehouseQueryParam);
    }

    @Test
    public void getWarehouseByCompany() throws Exception {
        WarehouseQueryParam warehouseQueryParam = new WarehouseQueryParam();
        warehouseQueryParam.setSubCompanyId(2);
        TestResult result = getJsonTestResult("/warehouse/getWarehouseByCompany", warehouseQueryParam);
    }

    @Test
    public void getWarehouseByCurrentCompany() throws Exception {
        WarehouseQueryParam warehouseQueryParam = new WarehouseQueryParam();
        TestResult result = getJsonTestResult("/warehouse/getWarehouseByCurrentCompany", warehouseQueryParam);
    }

    @Test
    public void getWarehouseById() throws Exception {
        WarehouseQueryParam warehouseQueryParam = new WarehouseQueryParam();
        warehouseQueryParam.setWarehouseId(4000001);
        TestResult result = getJsonTestResult("/warehouse/getWarehouseById", warehouseQueryParam);
    }

    @Test
    public void productInStock() throws Exception {
        ProductInStockParam productInStockParam = new ProductInStockParam();
        List<ProductInStorage> productInStorageList = new ArrayList<>();
        ProductInStorage productInStorage = new ProductInStorage();
        productInStorage.setProductId(2000013);
        productInStorage.setProductSkuId(40);
        productInStorage.setProductCount(10);

        List<ProductMaterial> productMaterialList = new ArrayList<>();
        ProductMaterial productMaterial = new ProductMaterial();
        productMaterial.setMaterialId(2);
        productMaterial.setMaterialCount(1);
        productMaterialList.add(productMaterial);
        productInStorage.setProductMaterialList(productMaterialList);

        productInStorageList.add(productInStorage);
        productInStockParam.setProductInStorageList(productInStorageList);
        productInStockParam.setTargetWarehouseId(4000001);
        productInStockParam.setReferNo("C201711071720430655000051084");
        productInStockParam.setCauseType(StockCauseType.STOCK_CAUSE_TYPE_IN_PURCHASE);
        TestResult result = getJsonTestResult("/warehouse/productInStock", productInStockParam);
    }

    @Test
    public void productOutStock() throws Exception {
        ProductOutStockParam productOutStockParam = new ProductOutStockParam();
        productOutStockParam.setCauseType(StockCauseType.STOCK_CAUSE_TYPE_ALLOCATION);
        productOutStockParam.setSrcWarehouseId(4000001);
        productOutStockParam.setTargetWarehouseId(4000002);
        productOutStockParam.setReferNo("6500001");

        List<Integer> productEquipmentIdList = new ArrayList<>();
        for (int i = 1300; i < 1310; i++) {
            productEquipmentIdList.add(i);
        }
        productOutStockParam.setProductEquipmentIdList(productEquipmentIdList);

        TestResult result = getJsonTestResult("/warehouse/productOutStock", productOutStockParam);
    }



    @Test
    public void getStockOrderPage() throws Exception {
        StockOrderQueryParam stockOrderQueryParam = new StockOrderQueryParam();
        stockOrderQueryParam.setCauseType(1);
        TestResult result = getJsonTestResult("/warehouse/getStockOrderPage", stockOrderQueryParam);
    }
}
