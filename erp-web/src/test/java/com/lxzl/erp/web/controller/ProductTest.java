package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.product.ProductCategoryPageParam;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.product.ProductQueryParam;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.erp.common.util.FastJsonUtil;
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
    public void queryAllProduct() throws Exception {
        ProductQueryParam productQueryParam = new ProductQueryParam();
        productQueryParam.setPageNo(1);
        productQueryParam.setPageSize(15);
        productQueryParam.setProductId(2000065);
//        productQueryParam.setIsRent(0);
//        productQueryParam.setBrandId(15);
        TestResult testResult = getJsonTestResult("/product/queryAllProduct", productQueryParam);
    }
    @Test
    public void queryProductById() throws Exception {
        Product product = new Product();
        product.setProductId(2000065);
        TestResult testResult = getJsonTestResult("/product/queryProductById", product);
    }
    @Test
    public void queryAllProductEquipment() throws Exception {
        Product product = new Product();
        product.setProductId(2000013);
        TestResult testResult = getJsonTestResult("/product/queryAllProductEquipment", product);
    }


    @Test
    public void addProductCategoryPropertyValue() throws Exception {
        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
        productCategoryPropertyValue.setPropertyId(1);
        productCategoryPropertyValue.setPropertyValueName("黑灰色");
        TestResult testResult = getJsonTestResult("/product/addProductCategoryPropertyValue", productCategoryPropertyValue);
    }


    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setProductName("一个小小的台式机商品2018");
        product.setCategoryId(800003);
        product.setBrandId(1);
        product.setIsRent(1);
        product.setProductDesc("商品描述：一个台式机，内存16G，512硬盘");
        product.setDataStatus(1);
        product.setSubtitle("台式机小标题");
        product.setIsReturnAnyTime(1);

        List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setSkuPrice(new BigDecimal(5000.00));
        productSku.setDayRentPrice(new BigDecimal(98.00));
        productSku.setMonthRentPrice(new BigDecimal(98.00));
        productSku.setNewSkuPrice(new BigDecimal(1314.00));
        productSku.setNewDayRentPrice(new BigDecimal(520.00));
        productSku.setNewMonthRentPrice(new BigDecimal(99.00));
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
        TestResult testResult = getJsonTestResult("/product/add", product);
    }

    @Test
    public void queryPropertiesByCategoryId() throws Exception {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(800003);
        TestResult testResult = getJsonTestResult("/product/queryPropertiesByCategoryId", productCategory);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setProductId(2000062);
        product.setProductName("一个小小的台式机商品");
        product.setCategoryId(800003);
        product.setBrandId(1);
        product.setIsRent(1);
        product.setProductDesc("商品描述：一个台式机，内存16G，512硬盘");
        product.setDataStatus(1);
        product.setSubtitle("台式机小标题");

        List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setSkuId(214);
        productSku.setSkuPrice(new BigDecimal(5100.00));
        productSku.setDayRentPrice(new BigDecimal(98.00));
        productSku.setMonthRentPrice(new BigDecimal(98.00));
        productSku.setNewSkuPrice(new BigDecimal(5.00));
        productSku.setNewDayRentPrice(new BigDecimal(6.00));
        productSku.setNewMonthRentPrice(new BigDecimal(7.00));
        List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
        ProductSkuProperty productSkuProperty = new ProductSkuProperty();
        productSkuProperty.setPropertyId(1372);
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
        TestResult testResult = getJsonTestResult("/product/update", product);
    }

    @Test
    public void updateProductJson() throws Exception {
        String productJson = "{\"productImgList\":[{\"productImgId\":190}],\"productDescImgList\":[{\"productImgId\":195},{\"productImgId\":193},{\"productImgId\":191},{\"productImgId\":196},{\"productImgId\":194},{\"productImgId\":192}],\"productSkuList\":[{\"productSkuPropertyList\":[{\"propertyId\":18,\"propertyValueId\":63,\"propertyValueName\":\"4G\",\"propertyName\":\"内存\",\"skuPropertyId\":437,\"isSku\":1,\"skuId\":63,\"remark\":\"\"},{\"propertyId\":20,\"propertyValueId\":67,\"propertyValueName\":\"I3-6100\",\"propertyName\":\"CPU\",\"skuPropertyId\":434,\"isSku\":1,\"skuId\":63,\"remark\":\"\"},{\"propertyId\":21,\"propertyValueId\":69,\"propertyValueName\":\"1T HDD\",\"propertyName\":\"机械硬盘\",\"skuPropertyId\":435,\"isSku\":1,\"skuId\":63,\"remark\":\"\"},{\"propertyId\":22,\"propertyValueId\":71,\"propertyValueName\":\"集成\",\"propertyName\":\"显卡\",\"skuPropertyId\":436,\"isSku\":1,\"skuId\":63,\"remark\":\"\"},{\"propertyId\":23,\"propertyValueId\":73,\"propertyValueName\":\"128G SSD\",\"propertyName\":\"固态硬盘\",\"skuPropertyId\":438,\"isSku\":1,\"skuId\":63,\"remark\":\"\"}],\"skuId\":\"63\",\"skuPrice\":\"8000\",\"timeRentPrice\":\"120\",\"dayRentPrice\":\"60\",\"monthRentPrice\":\"500\"},{\"productSkuPropertyList\":[{\"propertyId\":18,\"propertyValueId\":63,\"propertyValueName\":\"4G\",\"propertyName\":\"内存\",\"skuPropertyId\":439,\"isSku\":1,\"skuId\":64,\"remark\":\"\"},{\"propertyId\":20,\"propertyValueId\":67,\"propertyValueName\":\"I3-6100\",\"propertyName\":\"CPU\",\"skuPropertyId\":440,\"isSku\":1,\"skuId\":64,\"remark\":\"\"},{\"propertyId\":21,\"propertyValueId\":69,\"propertyValueName\":\"1T HDD\",\"propertyName\":\"机械硬盘\",\"skuPropertyId\":441,\"isSku\":1,\"skuId\":64,\"remark\":\"\"},{\"propertyId\":22,\"propertyValueId\":71,\"propertyValueName\":\"集成\",\"propertyName\":\"显卡\",\"skuPropertyId\":442,\"isSku\":1,\"skuId\":64,\"remark\":\"\"},{\"propertyId\":23,\"propertyValueId\":74,\"propertyValueName\":\"256G SSD\",\"propertyName\":\"固态硬盘\",\"skuPropertyId\":443,\"isSku\":1,\"skuId\":64,\"remark\":\"\"}],\"skuId\":\"64\",\"skuPrice\":\"8500\",\"timeRentPrice\":\"180\",\"dayRentPrice\":\"70\",\"monthRentPrice\":\"600\"}],\"productPropertyList\":[{\"propertyId\":19,\"propertyValueId\":65,\"isSku\":1,\"remark\":\"\"}],\"productName\":\"联想T420\",\"categoryId\":\"800005\",\"subtitle\":\"T420\",\"unit\":\"303719\",\"listPrice\":\"800\",\"isRent\":\"1\",\"productDesc\":\"T420基本描述\",\"productId\":\"2000018\"}";
        TestResult testResult = getJsonTestResult("/product/update", FastJsonUtil.toBean(productJson, Product.class));
    }

    @Test
    public void testQueryProductEquipmentDetail() throws Exception {
        ProductEquipmentQueryParam productEquipmentQueryParam = new ProductEquipmentQueryParam();
        productEquipmentQueryParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017111110130");
        TestResult testResult = getJsonTestResult("/product/queryProductEquipmentDetail", productEquipmentQueryParam);
    }

    @Test
    public void addProductCategoryProperty() throws Exception {
        ProductCategoryProperty productCategoryProperty = new ProductCategoryProperty();
        productCategoryProperty.setPropertyName("尺寸");
        productCategoryProperty.setCategoryId(800003);
        productCategoryProperty.setPropertyType(1);
//        productCategoryProperty.setMaterialType(24);
        productCategoryProperty.setDataOrder(20);

        List<ProductCategoryPropertyValue> productCategoryPropertyValueList = new ArrayList<>();

        ProductCategoryPropertyValue productCategoryPropertyValue1 = new ProductCategoryPropertyValue();
        productCategoryPropertyValue1.setPropertyValueName("21寸");
        productCategoryPropertyValue1.setCategoryId(800003);
        productCategoryPropertyValue1.setPropertyCapacityValue(21.00);
        productCategoryPropertyValue1.setMaterialModelId(6);

        ProductCategoryPropertyValue productCategoryPropertyValue2 = new ProductCategoryPropertyValue();
        productCategoryPropertyValue2.setPropertyValueName("23寸");
        productCategoryPropertyValue2.setCategoryId(800003);
        productCategoryPropertyValue2.setPropertyCapacityValue(23.00);
        productCategoryPropertyValue2.setMaterialModelId(5);


        ProductCategoryPropertyValue productCategoryPropertyValue3 = new ProductCategoryPropertyValue();
        productCategoryPropertyValue3.setPropertyValueName("24寸");
        productCategoryPropertyValue3.setCategoryId(800003);
        productCategoryPropertyValue3.setPropertyCapacityValue(24.00);
        productCategoryPropertyValue3.setMaterialModelId(5);


        productCategoryPropertyValueList.add(productCategoryPropertyValue1);
        productCategoryPropertyValueList.add(productCategoryPropertyValue2);
        productCategoryPropertyValueList.add(productCategoryPropertyValue3);

        productCategoryProperty.setProductCategoryPropertyValueList(productCategoryPropertyValueList);

        TestResult testResult = getJsonTestResult("/product/addProductCategoryProperty", productCategoryProperty);
    }


    @Test
    public void deleteProductCategoryPropertyValue() throws Exception {
        ProductCategoryProperty productCategoryProperty = new ProductCategoryProperty();
        productCategoryProperty.setCategoryPropertyId(31);

        List<ProductCategoryPropertyValue> productCategoryPropertyValueList = new ArrayList<>();
        ProductCategoryPropertyValue productCategoryPropertyValue1 = new ProductCategoryPropertyValue();
        ProductCategoryPropertyValue productCategoryPropertyValue2 = new ProductCategoryPropertyValue();
        ProductCategoryPropertyValue productCategoryPropertyValue3 = new ProductCategoryPropertyValue();

        productCategoryPropertyValue1.setCategoryPropertyValueId(86);
        productCategoryPropertyValue2.setCategoryPropertyValueId(87);
        productCategoryPropertyValue3.setCategoryPropertyValueId(88);

        productCategoryPropertyValueList.add(productCategoryPropertyValue1);
        productCategoryPropertyValueList.add(productCategoryPropertyValue2);
        productCategoryPropertyValueList.add(productCategoryPropertyValue3);

        productCategoryProperty.setProductCategoryPropertyValueList(productCategoryPropertyValueList);

        TestResult testResult = getJsonTestResult("/product/deleteProductCategoryPropertyValue", productCategoryProperty);
    }

    @Test
    public void updateCategoryPropertyValue() throws Exception {
        ProductCategoryProperty productCategoryProperty = new ProductCategoryProperty();
        productCategoryProperty.setCategoryPropertyId(5);


        List<ProductCategoryPropertyValue> productCategoryPropertyValueList = new ArrayList<>();
        ProductCategoryPropertyValue productCategoryPropertyValue1 = new ProductCategoryPropertyValue();
        ProductCategoryPropertyValue productCategoryPropertyValue2 = new ProductCategoryPropertyValue();

        productCategoryPropertyValue1.setCategoryPropertyValueId(10);
        productCategoryPropertyValue1.setPropertyValueName("I4_6400");
//        productCategoryPropertyValue1.setPropertyCapacityValue(2048.00);
        productCategoryPropertyValue1.setMaterialModelId(19);

        productCategoryPropertyValue2.setCategoryPropertyValueId(11);
        productCategoryPropertyValue2.setPropertyValueName("i7-001");
//        productCategoryPropertyValue2.setPropertyCapacityValue(32);
        productCategoryPropertyValue2.setMaterialModelId(32);


        productCategoryPropertyValueList.add(productCategoryPropertyValue1);
        productCategoryPropertyValueList.add(productCategoryPropertyValue2);

        productCategoryProperty.setProductCategoryPropertyValueList(productCategoryPropertyValueList);


        TestResult testResult = getJsonTestResult("/product/updateCategoryPropertyValue", productCategoryProperty);
    }


    @Test
    public void pageProductCategory() throws Exception {
        ProductCategoryPageParam productCategoryPageParam = new ProductCategoryPageParam();
//        productCategoryPageParam.setCategoryType(1);
        productCategoryPageParam.setPageNo(1);
        productCategoryPageParam.setPageSize(5);

        TestResult testResult = getJsonTestResult("/product/pageProductCategory", productCategoryPageParam);
    }

    @Test
    public void detailProductCategory() throws Exception {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(800001);

        TestResult testResult = getJsonTestResult("/product/detailProductCategory", productCategory);
    }
}
