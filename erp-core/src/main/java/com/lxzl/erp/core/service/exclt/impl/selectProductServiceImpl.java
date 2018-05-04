
package com.lxzl.erp.core.service.exclt.impl;


import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.exclt.SelectProductService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.basic.BrandMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialModelMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/2/2
 * @Time : Created in 10:19
 */
@Service
public class selectProductServiceImpl implements SelectProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductCategoryPropertyMapper productCategoryPropertyMapper;

    @Autowired
    private ProductCategoryPropertyValueMapper productCategoryPropertyValueMapper;

    @Autowired
    private MaterialModelMapper materialModelMapper;
    @Autowired
    private UserSupport userSupport;

    Integer productCategoryId = 800004;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Map<String, String>> selectData(String str) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        int success = 0;
        Date now = new Date();
        ServiceResult<String, Map<String, String>> serviceResult = new ServiceResult<>();
        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\备份\\数据20180312-22.xlsx");
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("台式机");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {

            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            //存商品
            //改为根据型号查
//            ProductDO productDO = productMapper.findProductModel(xssfRow.getCell(4).toString());
//
//            if (productDO == null) {
//                productDO = new ProductDO();
//                //todo
////                productDO.setProductNo();
//                //subtitle,unit,list_price,product_desc,is_return_any_time 不是必填
//                productDO.setProductName(xssfRow.getCell(0).toString());
//                productDO.setK3ProductNo(xssfRow.getCell(1).toString());
//                productDO.setCategoryId(productCategoryId);
//                productDO.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
//                String brandName = xssfRow.getCell(3).toString();
//                productDO.setBrandId(brandMapper.findBrandIdByName(brandName));
//                productDO.setProductModel(xssfRow.getCell(4).toString());
//                productDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
//                productDO.setCreateTime(now);
//                productDO.setUpdateTime(now);
//                productDO.setCreateUser(userSupport.getCurrentUserId().toString());
//                productDO.setUpdateUser(userSupport.getCurrentUserId().toString());
//                productMapper.save(productDO);
//            }
//            //sku
//
//            ProductSku productSku = new ProductSku();
//            String skuName = "CPU:" + xssfRow.getCell(5).toString() + "/内存:" + xssfRow.getCell(6).toString() + "/硬盘:" + xssfRow.getCell(7).toString() + "/固态:" + xssfRow.getCell(8).toString() + "/独显:" + xssfRow.getCell(9).toString();
//            productSku.setSkuName(skuName);
//            productSku.setSkuPrice(new BigDecimal(getValue(xssfRow.getCell(10))));
//            productSku.setDayRentPrice(new BigDecimal(getValue(xssfRow.getCell(11))));
//            productSku.setMonthRentPrice(new BigDecimal(getValue(xssfRow.getCell(12))));
//            productSku.setNewSkuPrice(new BigDecimal(getValue(xssfRow.getCell(13))));
//            productSku.setNewDayRentPrice(new BigDecimal(getValue(xssfRow.getCell(14))));
//            productSku.setNewMonthRentPrice(new BigDecimal(getValue(xssfRow.getCell(15))));
//            productSku.setProductId(productDO.getId());
//            productSku.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
////                    arrayList.add(productSku);
//            productSkuMapper.save(ConverterUtil.convert(productSku, ProductSkuDO.class));
////                    product.setProductSkuList(arrayList);
////                    ServiceResult<String, Integer> serviceResult1 = productService.addProduct(product);
//
//            //存商品属性
//
//            //保存CPU属性
//            String value5 = getValue(xssfRow.getCell(5));
//            ProductCategoryPropertyValueDO valueDO5 = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(value5, productCategoryId);
//            if (valueDO5 == null) {
//                //todo data_order
//                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = new ProductCategoryPropertyValueDO();
//                productCategoryPropertyValueDO.setPropertyValueName(getValue(xssfRow.getCell(5)));
//                productCategoryPropertyValueDO.setPropertyId(1);
//                productCategoryPropertyValueDO.setCategoryId(productCategoryId);
//                productCategoryPropertyValueDO.setPropertyCapacityValue(Double.parseDouble(getValue(xssfRow.getCell(8))));
////                materialModelMapper.findMaterialModeIdlByModelName();
////                productCategoryPropertyValueDO.setMaterialModelId();
////                productCategoryPropertyValueDO.setReferId();
////                productCategoryPropertyValueDO.setDataStatus();
//                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
//            }
//            //todo 保存内存属性
//            String value6 = getValue(xssfRow.getCell(5));
//            ProductCategoryPropertyValueDO valueDO6 = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(value6, productCategoryId);
//            if (valueDO6 == null) {
//                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = new ProductCategoryPropertyValueDO();
//                productCategoryPropertyValueDO.setPropertyValueName(getValue(xssfRow.getCell(5)));
//                productCategoryPropertyValueDO.setPropertyId(1);
//                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
//            }


//            if ("CPU".equals(propertyName)) {
//                String value = getValue(xssfRow.getCell(5));
//                ProductCategoryPropertyValueDO nameAndCategoryId = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(value, productCategoryId);
//                if (nameAndCategoryId == null) {
//                    ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
//                    productCategoryPropertyValue.setPropertyValueName(getValue(xssfRow.getCell(5)));
//                    productCategoryPropertyValueMapper.save(ConverterUtil.convert(productCategoryPropertyValue,ProductCategoryPropertyValueDO.class));
//                }
//            }
//                if ("内存".equals(propertyName)) {
//                    String value = getValue(xssfRow.getCell(6));
//                    ProductCategoryPropertyValueDO nameAndCategoryId = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(value, productCategoryId);
//                    if (nameAndCategoryId == null) {
//                        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
//                        productCategoryPropertyValue.setPropertyValueName(getValue(xssfRow.getCell(6)));
//                        productCategoryPropertyValueMapper.save(ConverterUtil.convert(productCategoryPropertyValue,ProductCategoryPropertyValueDO.class));
//                    }
//                }
//                if ("硬盘".equals(propertyName)) {
//                    String value = getValue(xssfRow.getCell(7));
//                    ProductCategoryPropertyValueDO nameAndCategoryId = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(value, productCategoryId);
//                    if (nameAndCategoryId == null) {
//                        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
//                        productCategoryPropertyValue.setPropertyValueName(getValue(xssfRow.getCell(7)));
//                        productCategoryPropertyValueMapper.save(ConverterUtil.convert(productCategoryPropertyValue,ProductCategoryPropertyValueDO.class));
//                    }
//                }
//                if ("固态".equals(propertyName)) {
//                    String value = getValue(xssfRow.getCell(8));
//                    ProductCategoryPropertyValueDO nameAndCategoryId = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(value, productCategoryId);
//                    if (nameAndCategoryId == null) {
//                        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
//                        productCategoryPropertyValue.setPropertyValueName(getValue(xssfRow.getCell(8)));
//                        productCategoryPropertyValueMapper.save(ConverterUtil.convert(productCategoryPropertyValue,ProductCategoryPropertyValueDO.class));
//                    }
//                }
//                if ("独显".equals(propertyName)) {
//                    String value = getValue(xssfRow.getCell(8));
//                    ProductCategoryPropertyValueDO nameAndCategoryId = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(value, productCategoryId);
//                    if (nameAndCategoryId == null) {
//                        ProductCategoryPropertyValue productCategoryPropertyValue = new ProductCategoryPropertyValue();
//                        productCategoryPropertyValue.setPropertyValueName("独显");
//                        productCategoryPropertyValue.setPropertyId(productCategoryPropertyDO.getId());
//                        productCategoryPropertyValue.setCategoryId(productCategoryId);
//                        productCategoryPropertyValue.setPropertyCapacityValue(Double.parseDouble(getValue(xssfRow.getCell(8))));
//                        materialModelMapper.findMaterialModeIdlByModelName();
//                        productCategoryPropertyValue.setMaterialModelId();
//                        productCategoryPropertyValue.setReferId();
//                        productCategoryPropertyValue.setDataStatus();
//                        productCategoryPropertyValueMapper.save(ConverterUtil.convert(productCategoryPropertyValue,ProductCategoryPropertyValueDO.class));
//                    }
//                }
//            List<ProductSkuDO> productSkuDOList = productSkuMapper.findByProductId(productDO.getId());
//
//            ProductSkuDO productSkuDO;
//            productDO.getProductPropertyDOList();
        }
        return null;

        //                // 获取当前工作薄的每一行
//
//                qqq:
//                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
//                    String skuName ="";
//
//                    //商品
//                    Product product = new Product();
//                    Field[] productFields = product.getClass().getDeclaredFields();
//
//                    //sku
//                    ProductSku productSku = new ProductSku();
//                    Field[] productSkuFields = productSku.getClass().getDeclaredFields();
//
//                    //每一行
//                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
//                    if (xssfRow == null) {
//                        serviceResult.setErrorCode("行为空");
//                        return serviceResult;
//                    }
//                    if (xssfRow.getLastCellNum() < 0) {
//                        serviceResult.setErrorCode("行数小于0");
//                        return serviceResult;
//                    }
//
//                    //商品
//                    Map<String, Field> productAttributes = new HashMap<>();
//
//                    for (Field field : productFields) {
//                        field.setAccessible(true);
//                        productAttributes.put(field.getName(), field);
//                    }
//
//                    //sku商品
//                    Map<String, Field> productSkuAttributes = new HashMap<>();
//
//                    for (Field field : productSkuFields) {
//                        field.setAccessible(true);
//                        productSkuAttributes.put(field.getName(), field);
//                    }
//
//
//                    int j = 0;
//                    //每行的每个元素
//                    aaa:
//                    for (int i = 0; i < xssfRow.getLastCellNum(); i++) {
//                        j = j++;
//                        //查看是否是这列 并取出这列对应的属性名称
//                        String stringCellValue = null;
//                        try {
//                            XSSFRow row = xssfSheet.getRow(0);
//                            XSSFCell cell = row.getCell(i);
//                            if (cell == null) {
//                                continue;
//                            }
//                            stringCellValue = cell.getStringCellValue();
//                            if (stringCellValue == null) {
//                                continue;
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (map.containsKey(stringCellValue)) {
//                            String param = map.get(stringCellValue);
//                            if (param == null) {
//                                continue;
//                            }
//                            boolean contains = false;
//                            if (productAttributes.containsKey(param)) {
//                                contains = true;
//                            }
//
//                            boolean contains1 = false;
//                            if (productSkuAttributes.containsKey(param)) {
//                                contains1 = true;
//                            }
//
//                            XSSFCell xssfCell = xssfRow.getCell(i);
//                            if (xssfCell == null) {
//                                continue;
//                            }
//                            //以下都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
//                            //这里转换类型都为String
//                            String value = getValue(xssfCell);
//                            String erroeValue = value;
//                            if (value == null || value == "") {
//                                continue;
//                            }
//
//
//                            //CPU
//                            if (stringCellValue.equals("CPU")) {
//                               skuName = stringCellValue +skuName+":"+value+"/";
//                            }
//                            //内存
//                            if (stringCellValue.equals("内存")) {
//                                skuName = value +skuName+":"+value+"/";
//                            }
//                            //硬盘
//                            if (stringCellValue.equals("硬盘")) {
//                                skuName = value +skuName+":"+value+"/";
//                            }
//                            //固态
//                            if (stringCellValue.equals("固态")) {
//                                skuName = value +skuName+":"+value+"/";
//                            }
//                            //独显
//                            if (stringCellValue.equals("独显")) {
//                                skuName = value +skuName+":"+value;
//                            }
//
//                            //品牌
//                            if (stringCellValue.equals("品牌")) {
//                                Integer brandIdByName = brandMapper.findBrandIdByName(value);
//                                product.setBrandId(brandIdByName);
//                                //此时没有查到品牌
//                                if(brandIdByName <= 0){
//                                    Brand brand = new Brand();
//
//                                    ConverterUtil.convert(brand,BrandDO);
//                                    brandMapper.save(brand);
//                                }
//
//                            }
//
//                            //以上都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
//
//                            //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
//                            Field field = null;
//                            if (contains) {
//                                field = productAttributes.get(param);
//                            }
//
//                            //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
//                            if (contains1) {
//                                field = productSkuAttributes.get(param);
//                            }
//
//                            //开始往属性中set值
//                            if (field != null) {
//                                String name = param.substring(0, 1).toUpperCase() + param.substring(1); //将属性的首字符大写，方便构造get，set方法
//                                String type = field.getGenericType().toString();    //获取属性的类型
//                                if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
//                                    if (contains) {
//                                        Method m = product.getClass().getMethod("set" + name, String.class);
//                                        m.invoke(product, value);    //调用getter方法获取属性值
//                                    }
//                                    if (contains1) {
//                                        Method m = productSku.getClass().getMethod("set" + name, String.class);
//                                        m.invoke(productSku, value);    //调用getter方法获取属性值
//                                    }
//                                    continue;
//                                }
//                                if (type.equals("class java.lang.Double")) {   //如果type是类类型，则前面包含"class "，后面跟类名
//                                    int ping = value.indexOf("平");
//                                    if (ping > 0) {
//                                        value = value.substring(0, ping);
//                                    }
//                                    if (contains) {
//                                        Method m = product.getClass().getMethod("set" + name, Double.class);
//                                        m.invoke(product, Double.parseDouble(value));    //调用getter方法获取属性值
//                                    }
//                                    if (contains1) {
//                                        Method m = productSku.getClass().getMethod("set" + name, Double.class);
//                                        m.invoke(productSku, Double.parseDouble(value));    //调用getter方法获取属性值
//                                    }
//                                    continue;
//                                }
//                                if (type.equals("class java.lang.Boolean")) {   //如果type是类类型，则前面包含"class "，后面跟类名
//                                    if (contains) {
//                                        Method m = product.getClass().getMethod("set" + name, Boolean.class);
//                                        m.invoke(product, Boolean.parseBoolean(value));    //调用getter方法获取属性值
//                                    }
//                                    if (contains1) {
//                                        Method m = productSku.getClass().getMethod("set" + name, Boolean.class);
//                                        m.invoke(productSku, Boolean.parseBoolean(value));    //调用getter方法获取属性值
//                                    }
//                                    continue;
//                                }
//                                if (type.equals("class java.lang.Integer")) {   //如果type是类类型，则前面包含"class "，后面跟类名
//                                    if (contains) {
//                                        Method m = product.getClass().getMethod("set" + name, Integer.class);
//                                        try {
//                                            m.invoke(product, (int) Double.parseDouble(value));    //调用getter方法获取属性值
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    if (contains1) {
//                                        Method m = productSku.getClass().getMethod("set" + name, Integer.class);
//                                        try {
//                                            m.invoke(productSku, (int) Double.parseDouble(value));    //调用getter方法获取属性值
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                    continue;
//                                }
//
//                                if (type.equals("class java.math.BigDecimal")) {   //如果type是类类型，则前面包含"class "，后面跟类名
//                                    if (contains) {
//                                        Method m = product.getClass().getMethod("set" + name, BigDecimal.class);
//                                        int i1 = value.length() - 1;
//                                        int i2 = value.lastIndexOf("万");
//                                        if (i2 == i1) {
//                                            String aaa = value.substring(0, i2);
//                                            BigDecimal bd = new BigDecimal(aaa);
//                                            BigDecimal bd1 = new BigDecimal("10000");
//                                            BigDecimal multiply = bd.multiply(bd1);
//                                            BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
//                                            m.invoke(product, bigDecimal);    //调用getter方法获取属性值
//                                            continue;
//                                        }
//                                        try {
//                                            m.invoke(product, new BigDecimal(value));    //调用getter方法获取属性值
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        continue;
//                                    }
//                                    if (contains1) {
//                                        Method m = productSku.getClass().getMethod("set" + name, BigDecimal.class);
//                                        int i1 = value.length() - 1;
//                                        int i2 = value.lastIndexOf("万");
//                                        if (i2 == i1) {
//                                            String aaa = value.substring(0, i2);
//                                            BigDecimal bd = new BigDecimal(aaa);
//                                            BigDecimal bd1 = new BigDecimal("10000");
//                                            BigDecimal multiply = bd.multiply(bd1);
//                                            BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
//                                            m.invoke(productSku, bigDecimal);    //调用getter方法获取属性值
//                                            continue;
//                                        }
//                                        try {
//                                            m.invoke(productSku, new BigDecimal(value));    //调用getter方法获取属性值
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        continue;
//                                    }
//
//                                }
//                                if (type.equals("class java.util.Date")) {   //如果type是类类型，则前面包含"class "，后面跟类名
//                                    if (contains) {
//                                        Method m = product.getClass().getMethod("set" + name, Date.class);
//                                        try {
//                                            m.invoke(product, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
//                                        } catch (Exception e) {
//                                            continue;
//                                        }
//                                    }
//                                    continue;
//                                }
//                                if (type.equals("class java.util.Date")) {   //如果type是类类型，则前面包含"class "，后面跟类名
//                                    if (contains1) {
//                                        Method m = productSku.getClass().getMethod("set" + name, Date.class);
//                                        try {
//                                            m.invoke(productSku, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
//                                        } catch (Exception e) {
//                                            continue;
//                                        }
//                                    }
//                                    continue;
//                                }
//                            }
//                        }
//
//                    }
//
//                    if (xssfRow == null) {
//                        serviceResult.setErrorCode("没有数据了");
//                        return serviceResult;
//                    }
//
//                    Product convert = ConverterUtil.convert(product, Product.class);
//                    convert.setCategoryId(productCategoryId);
//                    //sku
//                    productSku.setSkuName(skuName);
//
//                    ArrayList<ProductSku> list = new ArrayList<>();
//                    list.add(productSku);
//                    convert.setProductSkuList(list);
//
//                    ServiceResult<String, Integer> serviceResult1 = productService.addProduct(convert);
//                    if(ErrorCode.SUCCESS.equals(serviceResult1.getErrorCode())){
//                        success = ++success;
//                    }
//                }
//                errorData.put("成功:"+success+"条",success+"");
//                serviceResult.setErrorCode(ErrorCode.SUCCESS);
//                serviceResult.setResult(errorData);
//                return serviceResult;
    }

    private String getValue(XSSFCell xssfCell) {
        switch (xssfCell.getCellType()) {
            case 0:
                if (DateUtil.isCellDateFormatted(xssfCell)) {
                    DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    return sdf.format(xssfCell.getDateCellValue());
                }
                double value = xssfCell.getNumericCellValue();
                if (value > 1000000000) {
                    DecimalFormat decimalFormat = new DecimalFormat("##0");//格式化设置
                    return decimalFormat.format(value);
                } else {
                    return value + "";
                }


            case 1:
                return xssfCell.getRichStringCellValue().toString();
            case 2:
                return xssfCell.getCellFormula();
            case 3:
                return "";
            case 4:
                return xssfCell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case 5:
                return ErrorEval.getText(xssfCell.getErrorCellValue());
            default:
                return "Unknown Cell Type: " + xssfCell.getCellType();
        }
    }


    /**
     * 判断字符串是否是数字
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[-\\+]?[.\\d]*$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * xlsx表的数据
     */

    public Map<String, String> createMap() {

        Map<String, String> maps = new HashMap<>();
        maps.put("商品名称", "productName");
        maps.put("型号", "productModel");
        maps.put("商品编码", "k3ProductNo");
        maps.put("次新设备价值", "skuPrice");
        maps.put("次新日租金", "dayRentPrice");
        maps.put("次新月租金", "monthRentPrice");
        maps.put("全新设备价值", "newSkuPrice");
        maps.put("全新日租金", "newDayRentPrice");
        maps.put("全新月租金", "newMonthRentPrice");
        return maps;
    }


    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

}
