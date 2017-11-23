package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.product.ProductQueryParam;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductCategoryPropertyValue;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.JSONUtil;
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
        TestResult result = getJsonTestResult("/product/queryAllProduct", productQueryParam);
    }


    @Test
    public void addProductCategoryPropertyValue() throws Exception {
        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
        productCategoryPropertyValue.setPropertyId(11);
        productCategoryPropertyValue.setPropertyValueName("小小机箱");
        productCategoryPropertyValue.setMaterialModelId(11);
        TestResult result = getJsonTestResult("/product/addProductCategoryPropertyValue", productCategoryPropertyValue);
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
        productSku.setSkuPrice(new BigDecimal(5000.00));
        productSku.setTimeRentPrice(new BigDecimal(98.00));
        productSku.setDayRentPrice(new BigDecimal(98.00));
        productSku.setMonthRentPrice(new BigDecimal(98.00));
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
        TestResult result = getJsonTestResult("/product/add", product);
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
        productSku.setSkuPrice(new BigDecimal(5100.00));
        productSku.setTimeRentPrice(new BigDecimal(98.00));
        productSku.setDayRentPrice(new BigDecimal(98.00));
        productSku.setMonthRentPrice(new BigDecimal(98.00));
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
        TestResult result = getJsonTestResult("/product/update", product);
    }

    @Test
    public void updateProductJson() throws Exception {
        String productJson = "{\"productImgList\":[{\"imgId\":117},{\"imgId\":113},{\"imgId\":115},{\"imgId\":116},{\"imgId\":114}],\"productDescImgList\":[{\"imgId\":120},{\"imgId\":119},{\"imgId\":122},{\"imgId\":118},{\"imgId\":121}],\"productSkuList\":[{\"productSkuPropertyList\":[{\"propertyId\":1,\"propertyValueId\":1,\"propertyValueName\":\"黑色\",\"propertyName\":\"颜色\",\"skuPropertyId\":244,\"isSku\":1,\"skuId\":39,\"remark\":\"\"},{\"propertyId\":2,\"propertyValueId\":3,\"propertyValueName\":\"2G\",\"propertyName\":\"内存\",\"skuPropertyId\":245,\"isSku\":1,\"skuId\":39,\"remark\":\"\"},{\"propertyId\":5,\"propertyValueId\":10,\"propertyValueName\":\"I3-6100\",\"propertyName\":\"CPU\",\"skuPropertyId\":246,\"isSku\":1,\"skuId\":39,\"remark\":\"\"},{\"propertyId\":6,\"propertyValueId\":12,\"propertyValueName\":\"1T HDD\",\"propertyName\":\"机械硬盘\",\"skuPropertyId\":247,\"isSku\":1,\"skuId\":39,\"remark\":\"\"},{\"propertyId\":7,\"propertyValueId\":14,\"propertyValueName\":\"集成\",\"propertyName\":\"显卡\",\"skuPropertyId\":248,\"isSku\":1,\"skuId\":39,\"remark\":\"\"},{\"propertyId\":10,\"propertyValueId\":21,\"propertyValueName\":\"256G SSD\",\"propertyName\":\"固态硬盘\",\"skuPropertyId\":249,\"isSku\":1,\"skuId\":39,\"remark\":\"\"}],\"skuId\":\"39\",\"skuPrice\":\"99\",\"timeRentPrice\":\"88\",\"dayRentPrice\":\"77\",\"monthRentPrice\":\"66\"}],\"productPropertyList\":[{\"propertyId\":3,\"propertyValueId\":7,\"isSku\":1,\"remark\":\"\"},{\"propertyId\":4,\"propertyValueId\":9,\"isSku\":1,\"remark\":\"\"},{\"propertyId\":8,\"propertyValueId\":17,\"isSku\":1,\"remark\":\"\"},{\"propertyId\":9,\"propertyValueId\":19,\"isSku\":1,\"remark\":\"\"},{\"propertyId\":11,\"propertyValueId\":22,\"isSku\":1,\"remark\":\"\"}],\"productName\":\"thinkpad001\",\"categoryId\":\"800003\",\"subtitle\":\"thinkpad001\",\"unit\":\"303719\",\"listPrice\":\"100\",\"isRent\":\"1\",\"productDesc\":\"thinkpad001\",\"productId\":\"2000012\"}";
        TestResult result = getJsonTestResult("/product/update", FastJsonUtil.toBean(productJson, Product.class));
    }

    @Test
    public void testQueryProductEquipmentDetail() throws Exception {
        ProductEquipmentQueryParam productEquipmentQueryParam = new ProductEquipmentQueryParam();
        productEquipmentQueryParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017111110130");
        TestResult result = getJsonTestResult("/product/queryProductEquipmentDetail", productEquipmentQueryParam);
    }
}
