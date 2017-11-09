package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.ProductQueryParam;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryPropertyValue;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 15:54
 */
public class ProductTest extends ERPUnTransactionalTest {

    @Test
    public void queryAllProduct() throws Exception{
        ProductQueryParam productQueryParam = new ProductQueryParam();
        productQueryParam.setPageNo(1);
        productQueryParam.setPageSize(15);
        TestResult result = getJsonTestResult("/product/queryAllProduct",productQueryParam);
    }


    @Test
    public void addProductCategoryPropertyValue() throws Exception {
        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
        productCategoryPropertyValue.setPropertyId(11);
        productCategoryPropertyValue.setPropertyValueName("水冷机箱");
        TestResult result = getJsonTestResult("/product/addProductCategoryPropertyValue",productCategoryPropertyValue);
    }
}
