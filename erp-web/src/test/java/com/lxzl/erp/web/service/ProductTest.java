package com.lxzl.erp.web.service;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CategoryType;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.MaterialType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.MaterialModel;
import com.lxzl.erp.common.domain.product.*;
import com.lxzl.erp.common.domain.product.pojo.*;
import com.lxzl.erp.common.util.AlgorithmUtil;
import com.lxzl.erp.core.service.material.MaterialService;
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
        productCategoryQueryParam.setCategoryType(1);
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
    public void testSaveProperties() {


        Integer notepad = 800002, AIO = 800003, bchassis = 800005, mchassis = 800006, schassis = 800007, mnchassis = 800008, workstation = 800009, server = 800010,
                tv = 800011, projector = 800012, zPrinters = 800013, jPrinters = 800014, aioPrinters = 800015, Copier = 800016, pad = 800017, monitor = 800018;
        Map<Integer, String> categoryMap = new HashMap<>();
        categoryMap.put(notepad, "笔记本");
        categoryMap.put(AIO, "一体机");
        categoryMap.put(bchassis, "大机箱");
        categoryMap.put(mchassis, "中机箱");
        categoryMap.put(schassis, "小机箱");
        categoryMap.put(mnchassis, "mini机箱");
        categoryMap.put(workstation, "工作站");
        categoryMap.put(server, "服务器");
        categoryMap.put(tv, "电视机");
        categoryMap.put(projector, "投影仪");
        categoryMap.put(zPrinters, "针式打印机");
        categoryMap.put(jPrinters, "激光打印机");
        categoryMap.put(aioPrinters, "打印、复印、传真一体机");
        categoryMap.put(Copier, "数码复印件");
        categoryMap.put(pad, "平板电脑");
        categoryMap.put(monitor, "显示器");

        for (Map.Entry<Integer, String> entry : categoryMap.entrySet()) {
            if (notepad.equals(entry.getKey())) {
                String[] valueName = new String[]{"屏幕尺寸", "其他属性", "CPU", "显卡", "内存", "机械硬盘", "固态硬盘"};
                for (int i = 0; i < valueName.length; i++) {
                    String value = valueName[i];
                    ProductCategoryPropertyDO productCategoryPropertyDO = buildProductCategoryPropertyDO(value, notepad, 1, null, (valueName.length - i));
                    productCategoryPropertyDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                    productCategoryPropertyMapper.save(productCategoryPropertyDO);
                }
            }
            if (AIO.equals(entry.getKey())) {

            }
            if (bchassis.equals(entry.getKey())) {

            }
            if (mchassis.equals(entry.getKey())) {

            }
            if (schassis.equals(entry.getKey())) {

            }
            if (mnchassis.equals(entry.getKey())) {

            }
            if (workstation.equals(entry.getKey())) {

            }
            if (server.equals(entry.getKey())) {

            }
            if (tv.equals(entry.getKey())) {

            }
            if (projector.equals(entry.getKey())) {

            }
            if (zPrinters.equals(entry.getKey())) {

            }
            if (jPrinters.equals(entry.getKey())) {

            }
            if (aioPrinters.equals(entry.getKey())) {

            }
            if (Copier.equals(entry.getKey())) {

            }
            if (pad.equals(entry.getKey())) {

            }
            if (monitor.equals(entry.getKey())) {

            }
        }
    }

    private ProductCategoryPropertyDO buildProductCategoryPropertyDO(String name, Integer categoryId, Integer propertyType,Integer materialType, Integer order) {
        ProductCategoryPropertyDO productCategoryPropertyDO = new ProductCategoryPropertyDO();
        productCategoryPropertyDO.setPropertyName(name);
        productCategoryPropertyDO.setCategoryId(categoryId);
        productCategoryPropertyDO.setPropertyType(propertyType);
        productCategoryPropertyDO.setMaterialType(materialType);
        productCategoryPropertyDO.setDataOrder(order);
        productCategoryPropertyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        return productCategoryPropertyDO;
    }

    @Test
    public void testSavePropertiesValue() {
        Map<String, Object> params = new HashMap<>();
        params.put("start", 0);
        params.put("pageSize", Integer.MAX_VALUE);
        List<ProductCategoryPropertyDO> productCategoryPropertyDOList = productCategoryPropertyMapper.listPage(params);

        Integer notepad = 800002, AIO = 800003, bchassis = 800005, mchassis = 800006, schassis = 800007, mnchassis = 800008, workstation = 800009, server = 800010,
                tv = 800011, projector = 800012, zPrinters = 800013, jPrinters = 800014, aioPrinters = 800015, Copier = 800016, pad = 800017, monitor = 800018;
        Map<Integer, String> categoryMap = new HashMap<>();
        categoryMap.put(notepad, "笔记本");
        categoryMap.put(AIO, "一体机");
        categoryMap.put(bchassis, "大机箱");
        categoryMap.put(mchassis, "中机箱");
        categoryMap.put(schassis, "小机箱");
        categoryMap.put(mnchassis, "mini机箱");
        categoryMap.put(workstation, "工作站");
        categoryMap.put(server, "服务器");
        categoryMap.put(tv, "电视机");
        categoryMap.put(projector, "投影仪");
        categoryMap.put(zPrinters, "针式打印机");
        categoryMap.put(jPrinters, "激光打印机");
        categoryMap.put(aioPrinters, "打印、复印、传真一体机");
        categoryMap.put(Copier, "数码复印件");
        categoryMap.put(pad, "平板电脑");
        categoryMap.put(monitor, "显示器");


        for (ProductCategoryPropertyDO productCategoryPropertyDO : productCategoryPropertyDOList) {
            // 笔记本
            if (notepad.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("屏幕尺寸")) {
                    String[] valueName = new String[]{"11.6", "12.1", "13.3", "14.1", "15.4", "15.6"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"可触摸", "不可触摸", "无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("CPU")) {
                    String[] valueName = new String[]{"Intel Core i3 520M @ 2.50GHZ", "Intel Core i5 2520M @ 2.50GHZ", "Intel Core i7 2520M @ 2.50GHZ", "Intel Core i7 3520M @ 2.60GHZ", "Intel Core i5 3320M @ 2.60GHZ", "Intel Core i5 4200 @ 3.1GHZ",
                            "Intel Core i5 4200 @ 3.1GHZ", "Intel Core i5 M520 @ 2.40GHZ", "Intel Core i5  5200@ 2.60GHZ", "Intel Core i5 3120M @ 2.60GHZ", "Intel Core i7  720@ 2.60GHZ", "Intel Core i7  2720@ 2.60GHZ", "Intel Core i7  3720@ 2.60GHZ",
                            "Intel Core i5  520@ 2.50GHZ", "Intel Core i5  2520@ 2.50GHZ", "Intel Core i5  4200@ 2.20GHZ", "Intel Core i5  5200@ 2.20GHZ", "Intel Core i5 1600 MHZ", "Intel Core i5 3210 @ 2.50GHZ", "Intel Core i5 3210", "Intel Core i7 2675QM @ 2.20GHZ", "Intel Core i7 3615QM @ 2.30GHZ", "Intel Core i7 4850QM @ 2.30GHZ"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_CPU);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("显卡")) {
                    String[] valueName = new String[]{"集成", "GT650M"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_GRAPHICS_CARD);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("内存")) {
                    String[] valueName = new String[]{"1", "2", "4", "8", "16", "32", "64"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("机械硬盘")) {
                    String[] valueName = new String[]{"320", "500", "1024", "2048"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        String tempName = value.equals("1024") ? "1T" : value.equals("2048") ? "2T" : value + "G";
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(tempName, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("固态硬盘")) {
                    String[] valueName = new String[]{"128", "256"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }


            // 一体机
            if (AIO.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("屏幕尺寸")) {
                    String[] valueName = new String[]{"20", "21.5", "23", "27"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"薄款", "厚款", "无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("CPU")) {
                    String[] valueName = new String[]{"双核 Intel Pentium G640 @ 2.80GHZ", "双核 AMD IIX2 221 @ 2.80GHZ", "双核  Intel Pentium", "G3250T/DDR3", "Intel Core i5 3330S @ 2.70GHZ", "Intel Core i5 3330S @ 2.70GHZ", "Intel Core i5 3570 @ 3.20GHZ", "Intel Core i5 4570 @ 3.20GHZ"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_CPU);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("显卡")) {
                    String[] valueName = new String[]{"集成"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_GRAPHICS_CARD);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("内存")) {
                    String[] valueName = new String[]{"1", "2", "4", "8", "16", "32", "64"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("机械硬盘")) {
                    String[] valueName = new String[]{"320", "500", "1024", "2048"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        String tempName = value.equals("1024") ? "1T" : value.equals("2048") ? "2T" : value + "G";
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(tempName, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("固态硬盘")) {
                    String[] valueName = new String[]{"128", "256"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }

            // 大机箱，小机箱，中机箱，mni机箱
            if (bchassis.equals(productCategoryPropertyDO.getCategoryId())
                    || mchassis.equals(productCategoryPropertyDO.getCategoryId())
                    || schassis.equals(productCategoryPropertyDO.getCategoryId())
                    || mnchassis.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("CPU")) {
                    String[] valueName = new String[]{"Intel Core i5 4590", "Intel  i5 4430@ 3.40GHZ", "Intel  i3 4130@ 3.40GHZ", "Intel  双核G3220@ 3.40GH", "Intel  i3 2100@ 2.70GHZ", "Intel  i5 2400@ 2.30GHZ", "Intel Core i7 2600 @ 3.40GHZ", "Intel Core i7 4770 @ 3.40GHZ", "Intel  i7 @ 2.30GHZ", "Intel  i5 2400@ 2.30GHZ", "CPU: Intel  i5 2500@ 2.30GHZ", "CPU: Intel  i5 2500@ 2.30GHZ", "CPU: Intel  i3 4130@ 3.40GHZ 4G/", "CPU: Intel i3 2代@ 2.30GHZ 4G/"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_CPU);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("显卡")) {
                    String[] valueName = new String[]{"集成"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_GRAPHICS_CARD);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("" +
                        "内存")) {
                    String[] valueName = new String[]{"1", "2", "4", "8", "16", "32", "64"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("机械硬盘")) {
                    String[] valueName = new String[]{"500", "1024", "2048"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        String tempName = value.equals("1024") ? "1T" : value.equals("2048") ? "2T" : value + "G";
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(tempName, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("固态硬盘")) {
                    String[] valueName = new String[]{"128", "256"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }

            if (workstation.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("CPU")) {
                    String[] valueName = new String[]{"CPU: Intel 至强 E3 1245 @ 3.30GHZ", "CPU: Intel 至强 E3 1225 @ 3.20GHZ", "CPU: Intel 至强 E5 2603 @ 1.80GHZ*2"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_CPU);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("显卡")) {
                    String[] valueName = new String[]{"集成"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_GRAPHICS_CARD);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("内存")) {
                    String[] valueName = new String[]{"1", "2", "4", "8", "16", "32", "64"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("机械硬盘")) {
                    String[] valueName = new String[]{"500", "1024", "2048"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        String tempName = value.equals("1024") ? "1T" : value.equals("2048") ? "2T" : value + "G";
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(tempName, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("固态硬盘")) {
                    String[] valueName = new String[]{"128", "256"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }
            if (server.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("CPU")) {
                    String[] valueName = new String[]{"Intel 至强六核Xeon E5620*2 2.4GHz", "Intel 至强四核Xeon E5600*2 2.4GHz/16G/1TB*2"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_CPU);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("显卡")) {
                    String[] valueName = new String[]{"集成"};

                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        MaterialModel materialModel = new MaterialModel();
                        materialModel.setMaterialType(MaterialType.MATERIAL_TYPE_GRAPHICS_CARD);
                        materialModel.setModelName(value);
                        Integer modeId = materialService.addMaterialModel(materialModel).getResult();
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, modeId, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("内存")) {
                    String[] valueName = new String[]{"1", "2", "4", "8", "16", "32", "64"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("机械硬盘")) {
                    String[] valueName = new String[]{"500", "1024", "2048"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        String tempName = value.equals("1024") ? "1T" : value.equals("2048") ? "2T" : value + "G";
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(tempName, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("固态硬盘")) {
                    String[] valueName = new String[]{"128", "256"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "G", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), Integer.parseInt(value) * 1024.0, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }


            if (tv.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("尺寸")) {
                    String[] valueName = new String[]{"42", "43", "49", "50", "55", "60", "65", "75"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "寸", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("显示器")) {
                    String[] valueName = new String[]{"LED", "高清"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("操作系统")) {
                    String[] valueName = new String[]{"安卓", "普通"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }
            if (projector.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("分辨率")) {
                    String[] valueName = new String[]{"1024×768"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value + "寸", productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("对比度")) {
                    String[] valueName = new String[]{"3000：1"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("灯泡功率")) {
                    String[] valueName = new String[]{"280W"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("流明")) {
                    String[] valueName = new String[]{"5000"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }
            if (zPrinters.equals(productCategoryPropertyDO.getCategoryId())
                    || jPrinters.equals(productCategoryPropertyDO.getCategoryId())
                    || aioPrinters.equals(productCategoryPropertyDO.getCategoryId())
                    || Copier.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("颜色")) {
                    String[] valueName = new String[]{"黑", "白", "彩色"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }
            if (pad.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("颜色")) {
                    String[] valueName = new String[]{"黑灰", "银", "土豪金"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("容量")) {
                    String[] valueName = new String[]{"8G", "16G", "32G", "64G", "128G", "256G"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("型号")) {
                    String[] valueName = new String[]{"A5", "A6", "A7", "A8", "A9", "A10"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }
            if (monitor.equals(productCategoryPropertyDO.getCategoryId())) {
                if (productCategoryPropertyDO.getPropertyName().equals("其他属性")) {
                    String[] valueName = new String[]{"无"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("尺寸")) {
                    String[] valueName = new String[]{"17寸", "18.5寸", "20寸", "21.5寸", "23寸", "24寸"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
                if (productCategoryPropertyDO.getPropertyName().equals("屏幕型号")) {
                    String[] valueName = new String[]{"正方屏", "宽屏", "其他"};
                    for (int i = 0; i < valueName.length; i++) {
                        String value = valueName[i];
                        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(value, productCategoryPropertyDO.getId(), productCategoryPropertyDO.getCategoryId(), null, null, (valueName.length - i));
                        productCategoryPropertyValueDO.setRemark(categoryMap.get(productCategoryPropertyDO.getCategoryId()) + productCategoryPropertyDO.getPropertyName() + value);
                        productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                    }
                }
            }
        }
    }

    private ProductCategoryPropertyValueDO buildProductCategoryPropertyValueDO(String name, Integer propertyId, Integer categoryId, Double propertyCapacityValue, Integer modeId, Integer order) {
        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = new ProductCategoryPropertyValueDO();
        productCategoryPropertyValueDO.setPropertyValueName(name);
        productCategoryPropertyValueDO.setPropertyId(propertyId);
        productCategoryPropertyValueDO.setCategoryId(categoryId);
        productCategoryPropertyValueDO.setDataOrder(order);
        productCategoryPropertyValueDO.setMaterialModelId(modeId);
        productCategoryPropertyValueDO.setPropertyCapacityValue(propertyCapacityValue);
        productCategoryPropertyValueDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        return productCategoryPropertyValueDO;
    }

    @Autowired
    private ProductCategoryPropertyMapper productCategoryPropertyMapper;

    @Autowired
    private ProductCategoryPropertyValueMapper productCategoryPropertyValueMapper;

    @Autowired
    private MaterialService materialService;
}
