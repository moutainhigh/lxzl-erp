package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.ProductQueryParam;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryPropertyValue;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        productCategoryPropertyValue.setPropertyValueName("普通机箱");
        TestResult result = getJsonTestResult("/product/addProductCategoryPropertyValue",productCategoryPropertyValue);
    }


    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setProductName("一个小小的台式机商品");
        product.setCategoryId(800003);
        product.setBrandId(1);
        product.setIsRent(1);
        product.setProductDesc("商品描述：一个台式机，内存16G，512硬盘");
        product.setDataStatus(1);
        product.setSubtitle("台式机小标题");

        List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setOriginalPrice(new BigDecimal(100.00));
        productSku.setSkuPrice(new BigDecimal(5000.00));
        productSku.setRentPrice(new BigDecimal(98.00));
        productSku.setStock(500);
        List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
        ProductSkuProperty productSkuProperty = new ProductSkuProperty();
        productSkuProperty.setPropertyId(1);
        productSkuProperty.setPropertyValueId(1);
        productSkuPropertyList.add(productSkuProperty);
        ProductSkuProperty productSkuProperty2 = new ProductSkuProperty();
        productSkuProperty2.setPropertyId(2);
        productSkuProperty2.setPropertyValueId(4);
        productSkuPropertyList.add(productSkuProperty2);
        productSku.setProductSkuPropertyList(productSkuPropertyList);
        productSkuList.add(productSku);
        product.setProductSkuList(productSkuList);

        List<ProductSkuProperty> productPropertyList = new ArrayList<>();
        ProductSkuProperty productProperty = new ProductSkuProperty();
        productProperty.setPropertyId(1);
        productProperty.setPropertyValueId(1);
        productPropertyList.add(productProperty);
        product.setProductPropertyList(productPropertyList);
        TestResult result = getJsonTestResult("/product/add",product);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setProductId(2000005);
        product.setProductName("一个小小的台式机商品");
        product.setCategoryId(800003);
        product.setBrandId(1);
        product.setIsRent(1);
        product.setProductDesc("商品描述：一个台式机，内存16G，512硬盘");
        product.setDataStatus(1);
        product.setSubtitle("台式机小标题");

        List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setSkuId(14);
        productSku.setOriginalPrice(new BigDecimal(1050.00));
        productSku.setSkuPrice(new BigDecimal(5100.00));
        productSku.setRentPrice(new BigDecimal(198.00));
        List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
        ProductSkuProperty productSkuProperty = new ProductSkuProperty();
        productSkuProperty.setPropertyId(1);
        productSkuProperty.setPropertyValueId(1);
        productSkuPropertyList.add(productSkuProperty);
        ProductSkuProperty productSkuProperty2 = new ProductSkuProperty();
        productSkuProperty2.setPropertyId(2);
        productSkuProperty2.setPropertyValueId(4);
        productSkuPropertyList.add(productSkuProperty2);
        productSku.setProductSkuPropertyList(productSkuPropertyList);
        productSkuList.add(productSku);
        product.setProductSkuList(productSkuList);

        List<ProductSkuProperty> productPropertyList = new ArrayList<>();
        ProductSkuProperty productProperty = new ProductSkuProperty();
        productProperty.setPropertyId(1);
        productProperty.setPropertyValueId(1);
        productPropertyList.add(productProperty);
        product.setProductPropertyList(productPropertyList);
        TestResult result = getJsonTestResult("/product/update",product);
    }
}
