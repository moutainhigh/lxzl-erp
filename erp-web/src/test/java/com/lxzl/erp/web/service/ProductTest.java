package com.lxzl.erp.web.service;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CategoryType;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.*;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.erp.common.util.AlgorithmUtil;
import com.lxzl.erp.core.service.product.ProductCategoryService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductCategoryPropertyValueMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderMapper;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyDO;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderDO;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductTest extends BaseUnTransactionalTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private StockOrderMapper stockOrderMapper;


    @Test
    public void testQueryAllProductCategory() {
        ProductCategoryQueryParam productCategoryQueryParam = new ProductCategoryQueryParam();
        productCategoryQueryParam.setCategoryType(2);
        ServiceResult<String, List<ProductCategory>> result = productCategoryService.queryAllProductCategory(productCategoryQueryParam);
    }

    @Test
    public void testQueryProductCategoryPropertyListByCategoryId() {
        ServiceResult<String, List<ProductCategoryProperty>> result = productCategoryService.queryProductCategoryPropertyListByCategoryId(800002);
    }

    @Test
    public void testQueryPropertiesByProductId() {
        ServiceResult<String, List<ProductCategoryProperty>> result = productCategoryService.queryPropertiesByProductId(2000001);
    }

    @Test
    public void addProductCategoryPropertyValue() {
        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
        productCategoryPropertyValue.setPropertyId(2);
        productCategoryPropertyValue.setPropertyValueName("DDR4  16G");
        ServiceResult<String, Integer> result = productCategoryService.addProductCategoryPropertyValue(productCategoryPropertyValue);
    }

    @Test
    public void testVerifyProductMaterial() {
        Product product = productService.queryProductById(2000009).getResult();
        ProductSku productSku = product.getProductSkuList().get(0);

        ServiceResult<String, Integer> result = productService.verifyProductMaterial(productSku);
    }

    @Test
    public void testAddProduct() {
        Product product = new Product();
        product.setProductName("一个小小的台式机商品");
        product.setCategoryId(800002);
        product.setBrandId(1);
        product.setIsRent(1);
        product.setProductDesc("商品描述：一个台式机，内存16G，512硬盘");
        product.setDataStatus(1);
        product.setSubtitle("台式机小标题");

        List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setSkuPrice(new BigDecimal(5000.00));
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

        ServiceResult<String, Integer> result = productService.addProduct(product);
    }


    @Test
    public void testUpdateProduct() {
        Product product = new Product();
        product.setProductId(2000001);
        product.setProductName("优威派克一体机E270DMG");
        product.setBrandId(1);
        product.setIsRent(1);
        product.setUnit(303719);
        product.setProductDesc("商品描述：wifi下体验更佳");
        product.setSubtitle("一体机E270DMG");

        List<ProductSku> productSkuList = new ArrayList<>();


        List<String> propertyList1 = new ArrayList<String>();
        propertyList1.add("1");
        propertyList1.add("2");

        List<String> propertyList2 = new ArrayList<String>();
        propertyList2.add("4");

        List<String> propertyList3 = new ArrayList<String>();
        propertyList3.add("11");


        List<List<String>> dimValue = new ArrayList<List<String>>();
        dimValue.add(propertyList1);
        dimValue.add(propertyList2);
        dimValue.add(propertyList3);

        List<List<String>> recursiveResult = new ArrayList<List<String>>();
        AlgorithmUtil.descartes(dimValue, recursiveResult, 0, new ArrayList<String>());


        for (List<String> list : recursiveResult) {
            String color = list.get(0);
            String memory = list.get(1);
            String disk = list.get(2);

            ProductSku productSku = new ProductSku();
            productSku.setDayRentPrice(new BigDecimal(98.00));
            productSku.setMonthRentPrice(new BigDecimal(98.00));
            productSku.setSkuPrice(new BigDecimal(6500));
            List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
            ProductSkuProperty productSkuProperty = new ProductSkuProperty();
            productSkuProperty.setPropertyValueId(Integer.parseInt(color));
            productSkuPropertyList.add(productSkuProperty);
            ProductSkuProperty productSkuProperty2 = new ProductSkuProperty();
            productSkuProperty2.setPropertyValueId(Integer.parseInt(memory));
            productSkuPropertyList.add(productSkuProperty2);
            ProductSkuProperty productSkuProperty3 = new ProductSkuProperty();
            productSkuProperty3.setPropertyValueId(Integer.parseInt(disk));
            productSkuPropertyList.add(productSkuProperty3);
            productSku.setProductSkuPropertyList(productSkuPropertyList);
            productSkuList.add(productSku);
        }


        /*List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setOriginalPrice(new BigDecimal(100.00));
        productSku.setSalePrice(new BigDecimal(98.00));
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

        ProductSku productSku2 = new ProductSku();
        productSku2.setOriginalPrice(new BigDecimal(102.00));
        productSku2.setSalePrice(new BigDecimal(96.00));
        productSku2.setStock(500);
        List<ProductSkuProperty> productSkuPropertyList2 = new ArrayList<>();
        ProductSkuProperty productSkuProperty3 = new ProductSkuProperty();
        productSkuProperty3.setPropertyId(1);
        productSkuProperty3.setPropertyValueId(2);
        productSkuPropertyList2.add(productSkuProperty3);
        ProductSkuProperty productSkuProperty4 = new ProductSkuProperty();
        productSkuProperty4.setPropertyId(2);
        productSkuProperty4.setPropertyValueId(4);
        productSkuPropertyList2.add(productSkuProperty4);
        productSku2.setProductSkuPropertyList(productSkuPropertyList2);
        productSkuList.add(productSku2);

        ProductSku productSku3 = new ProductSku();
        productSku3.setOriginalPrice(new BigDecimal(102.00));
        productSku3.setSalePrice(new BigDecimal(96.00));
        productSku3.setStock(500);
        List<ProductSkuProperty> productSkuPropertyList3 = new ArrayList<>();
        ProductSkuProperty productSkuProperty5 = new ProductSkuProperty();
        productSkuProperty5.setPropertyId(1);
        productSkuProperty5.setPropertyValueId(5);
        productSkuPropertyList3.add(productSkuProperty5);
        ProductSkuProperty productSkuProperty6 = new ProductSkuProperty();
        productSkuProperty6.setPropertyId(2);
        productSkuProperty6.setPropertyValueId(4);
        productSkuPropertyList3.add(productSkuProperty6);
        productSku3.setProductSkuPropertyList(productSkuPropertyList3);
        productSkuList.add(productSku3);*/

        product.setProductSkuList(productSkuList);

        List<ProductSkuProperty> productPropertyList = new ArrayList<>();

        ProductSkuProperty productProperty2 = new ProductSkuProperty();
        productProperty2.setPropertyId(3);
        productProperty2.setPropertyValueId(6);
        productPropertyList.add(productProperty2);

        ProductSkuProperty productProperty3 = new ProductSkuProperty();
        productProperty3.setPropertyId(4);
        productProperty3.setPropertyValueId(7);
        productPropertyList.add(productProperty3);

        ProductSkuProperty productProperty4 = new ProductSkuProperty();
        productProperty4.setPropertyId(5);
        productProperty4.setPropertyValueId(10);
        productPropertyList.add(productProperty4);

        ProductSkuProperty productProperty6 = new ProductSkuProperty();
        productProperty6.setPropertyId(7);
        productProperty6.setPropertyValueId(14);
        productPropertyList.add(productProperty6);

        ProductSkuProperty productProperty7 = new ProductSkuProperty();
        productProperty7.setPropertyId(8);
        productProperty7.setPropertyValueId(16);
        productPropertyList.add(productProperty7);

        product.setProductPropertyList(productPropertyList);
        ServiceResult<String, Integer> result = productService.updateProduct(product);
    }

    @Test
    public void testQueryProductById() {
        Integer productId = 2000001;
        ServiceResult<String, Product> result = productService.queryProductById(productId);
    }

    @Test
    public void queryProductDetailById() {
        Integer productId = 2000005;
        ServiceResult<String, Product> result = productService.queryProductDetailById(productId);
    }

    @Test
    public void testQueryProduct() {
        ProductQueryParam param = new ProductQueryParam();
        param.setPageNo(1);
        param.setPageSize(10);
//        param.setProductId(2000001);
//        param.setProductName("一体机");
//        param.setIsSale(0);
        ServiceResult<String, Page<Product>> result = productService.queryAllProduct(param);
    }

    @Test
    public void testQueryProductEquipment() {
        ProductEquipmentQueryParam productEquipmentQueryParam = new ProductEquipmentQueryParam();
        productEquipmentQueryParam.setPageNo(1);
        productEquipmentQueryParam.setPageSize(10);
        ServiceResult<String, Page<ProductEquipment>> result = productService.queryAllProductEquipment(productEquipmentQueryParam);
    }

    @Test
    public void testQueryProductSku() {
        ProductSkuQueryParam productSkuQueryParam = new ProductSkuQueryParam();
        productSkuQueryParam.setProductId(2000004);
        productSkuQueryParam.setPageNo(1);
        productSkuQueryParam.setPageSize(10);
        ServiceResult<String, Page<ProductSku>> result = productService.queryProductSkuList(productSkuQueryParam);
        System.out.println(JSON.toJSONString(result, true));
    }

    @Test
    public void testStockOrder() {
        StockOrderDO stockOrderDO = stockOrderMapper.findOrderByTypeAndRefer(1, "PR2017122613482675460001641534");
    }


    @Test
    public void testSavePropertiesValue() {
        Map<String, Object> params = new HashMap<>();
        params.put("start", 0);
        params.put("pageSize", Integer.MAX_VALUE);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.listPage(params);

        Map<Integer, String> categoryMap = new HashMap<>();
        categoryMap.put(800002, "笔记本");

        for (Map.Entry<Integer, String> entry : categoryMap.entrySet()) {
            Integer key = entry.getKey();
            String categoryName = entry.getValue();
            for (ProductCategoryPropertyDO productCategoryPropertyDO : productCategoryPropertyDOList) {
                if (key.equals(productCategoryPropertyDO.getCategoryId())) {
                    if (productCategoryPropertyDO.getPropertyName().equals("屏幕尺寸")) {
                        String[] valueName = new String[]{"11.6", "12.1", "13.3", "14.1", "15.4", "15.6"};
                        for (String value : valueName) {
                            ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId());
                            productCategoryPropertyValueDO.setRemark(categoryName + productCategoryPropertyDO.getPropertyName() + value);
                            productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                        }
                    }
                }
            }
        }

    }

    private ProductCategoryPropertyValueDO buildProductCategoryPropertyValueDO(String name, Integer propertyId, Integer categoryId) {
        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = new ProductCategoryPropertyValueDO();
        productCategoryPropertyValueDO.setPropertyValueName(name);
        productCategoryPropertyValueDO.setPropertyId(propertyId);
        productCategoryPropertyValueDO.setCategoryId(categoryId);
        productCategoryPropertyValueDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        return productCategoryPropertyValueDO;
    }

    @Autowired
    private ProductCategoryPropertyMapper productCategoryPropertyMapper;

    @Autowired
    private ProductCategoryPropertyValueMapper productCategoryPropertyValueMapper;
}
