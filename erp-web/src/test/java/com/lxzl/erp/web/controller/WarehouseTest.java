package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.StockCauseType;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
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
        productInStorage.setProductId(2000001);
        productInStorage.setProductSkuId(2);
        productInStorage.setProductCount(100);
        productInStorageList.add(productInStorage);
        productInStockParam.setProductInStorageList(productInStorageList);
        productInStockParam.setTargetWarehouseId(4000001);
        productInStockParam.setReferNo("C201711071720430655000051081");
        productInStockParam.setCauseType(StockCauseType.STOCK_CAUSE_TYPE_IN_PURCHASE);
        TestResult result = getJsonTestResult("/warehouse/productInStock", productInStockParam);
    }
}
