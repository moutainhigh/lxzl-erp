package com.lxzl.erp.core.service.exclt.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.K3MappingCustomer;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.exclt.ImportOrderDataService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCustomerDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/2/1
 * @Time : Created in 18:58
 */
@Service
public class ImportOrderDataServiceImpl implements ImportOrderDataService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Map<String, String>> importData(String str) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
        ServiceResult<String, Map<String, String>> serviceResult = new ServiceResult<>();
        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\销售订单 - 副本.xlsx");

        HashMap<String, String> errorHashMap = new HashMap<>();
        HSSFWorkbook xssfWorkbook = new HSSFWorkbook(fileIn);
        HSSFSheet xssfSheet = xssfWorkbook.getSheet("Page2");

        Map<String, String> map = creatMap();

        String customerNovalue = null;
        //订单商品集合
        List<OrderProduct> orderProductList = new ArrayList<>();

        //订单物料集合
        List<OrderMaterial> orderMaterialList = new ArrayList<>();

        // 获取当前工作薄的每一行
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            boolean flag = false;

            boolean productNoFlag = false;
            boolean materialNoFlag = false;
            //订单
            Order order = new Order();
            Field[] orderFields = order.getClass().getDeclaredFields();

            //订单商品
            OrderProduct orderProduct = new OrderProduct();
            Field[] orderProductFields = orderProduct.getClass().getDeclaredFields();

            //订单物料
            OrderMaterial orderMaterial = new OrderMaterial();
            Field[] orderMaterialProductFields = orderMaterial.getClass().getDeclaredFields();

            //每一行
            HSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow == null) {
                serviceResult.setErrorCode("行为空");
                return serviceResult;
            }
            if (xssfRow.getLastCellNum() < 0) {
                serviceResult.setErrorCode("行数小于0");
                return serviceResult;
            }

            //订单
            Map<String, Field> orderAttributes = new HashMap<>();

            for (Field field : orderFields) {
                field.setAccessible(true);
                orderAttributes.put(field.getName(), field);
            }

            //订单商品
            Map<String, Field> orderProductAttributes = new HashMap<>();

            for (Field field : orderProductFields) {
                field.setAccessible(true);
                orderProductAttributes.put(field.getName(), field);
            }

            //订单物料
            Map<String, Field> materialProductAttributes = new HashMap<>();

            for (Field field : orderMaterialProductFields) {
                field.setAccessible(true);
                materialProductAttributes.put(field.getName(), field);
            }

            int j = 0;
            //每行的每个元素
            aaa:
            for (int i = 0; i < xssfRow.getLastCellNum(); i++) {
                j = j++;
                //查看是否是这列 并取出这列对应的属性名称
                String stringCellValue = null;
                try {
                    HSSFRow row = xssfSheet.getRow(0);
                    HSSFCell cell = row.getCell(i);
                    if (cell == null) {
                        continue;
                    }
                    stringCellValue = cell.getStringCellValue();
                    if (stringCellValue == null) {
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (map.containsKey(stringCellValue)) {
                    String param = map.get(stringCellValue);
                    if (param == null) {
                        continue;
                    }


                    //判断orderProduct是否有属性
                    boolean contains2 = false;

                    //判断orderProduct是否有属性
                    boolean contains = false;

                    //判断materialProduct是否有属性
                    boolean contains1 = false;

                    HSSFCell xssfCell = xssfRow.getCell(i);
                    if (xssfCell == null) {
                        continue;
                    }
                    //以下都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
                    //这里转换类型都为String
                    String value = xssfCell.toString();
                    if (value == null || value == "") {
                        continue aaa;
                    }

                    //单据号校验
                    if (stringCellValue.equals("单据号_FBillno")) {
                        if (!customerNovalue.equals(value)) {
                            customerNovalue = value;
                            flag = true;
                        }
                    }

                    //单据号校验
                    if (stringCellValue.equals("产品代码_FNumber")) {

                        if(value.contains("10")){
                            productNoFlag = true;
                            contains = true;
                        }
                        if(value.contains("20")){
                            materialNoFlag = true;
                            contains1 = true;
                        }
                    }

                    if (!orderProductAttributes.containsKey(param)) {
                        contains = false;
                    }
                    if (!materialProductAttributes.containsKey(param)) {
                        contains1 = false;
                    }
                    if (orderAttributes.containsKey(param)) {
                        contains2 = true;
                    }
                    //单据号校验
                    if (stringCellValue.equals("发货方式")) {

                        switch (value){
                            case"物流发货":
                                value = "1";
                                break ;
                            case"客户自提":
                                value = "2";
                                break ;
                            case"送货上门":
                                value = "3";
                                break ;
                        }
                    }

                    if (stringCellValue.equals("产品代码_FName")) {
                        if(productNoFlag){
                            List<ProductDO> productDOList = productMapper.findProductByName(value);
                            ProductDO productDO = productDOList.get(0);
                            orderProduct.setProductId(productDO.getId());
                        }
                        if(materialNoFlag){
                            List<OrderMaterialDO> orderMaterialDOList = orderMaterialMapper.findOrderMaterialByName(value);
                            OrderMaterialDO orderMaterialDO = orderMaterialDOList.get(0);
                            orderMaterialList.add(ConverterUtil.convert(orderMaterialDO,OrderMaterial.class));
                        }
                    }

                    //以上都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置

                    //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
                    Field field = null;
                    if (contains) {
                        field = orderProductAttributes.get(param);
                    }
                    if (contains1) {
                        field = materialProductAttributes.get(param);
                    }
                    if (contains2) {
                        field = orderAttributes.get(param);
                    }


                    //开始往属性中set值
                    if (field != null) {
                        String name = param.substring(0, 1).toUpperCase() + param.substring(1); //将属性的首字符大写，方便构造get，set方法
                        String type = field.getGenericType().toString();    //获取属性的类型
                        if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = orderProduct.getClass().getMethod("set" + name, String.class);
                                m.invoke(orderProduct, value);    //调用getter方法获取属性值
                            }
                            if (contains1) {
                                Method m = orderMaterial.getClass().getMethod("set" + name, String.class);
                                m.invoke(orderMaterial, value);    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = order.getClass().getMethod("set" + name, String.class);
                                m.invoke(order, value);    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Double")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            int ping = value.indexOf("平");
                            if (ping > 0) {
                                value = value.substring(0, ping);
                            }
                            if (contains) {
                                Method m = orderProduct.getClass().getMethod("set" + name, Double.class);
                                m.invoke(orderProduct, Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            if (contains1) {
                                Method m = orderMaterial.getClass().getMethod("set" + name, Double.class);
                                m.invoke(orderMaterial, Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = order.getClass().getMethod("set" + name, Double.class);
                                m.invoke(order, Double.parseDouble(value));    //调用getter方法获取属性值
                            }

                            continue;
                        }
                        if (type.equals("class java.lang.Boolean")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = orderProduct.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(orderProduct, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            if (contains1) {
                                Method m = orderMaterial.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(orderMaterial, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = order.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(order, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Integer")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = orderProduct.getClass().getMethod("set" + name, Integer.class);
                                try {
                                    m.invoke(orderProduct, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (contains1) {
                                Method m = orderMaterial.getClass().getMethod("set" + name, Integer.class);
                                try {
                                    m.invoke(orderMaterial, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (contains2) {
                                Method m = order.getClass().getMethod("set" + name, Integer.class);
                                try {
                                    m.invoke(order, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            continue;
                        }

                        if (type.equals("class java.math.BigDecimal")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = orderProduct.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(orderProduct, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                try {
                                    m.invoke(orderProduct, new BigDecimal(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }

                            if (contains1) {
                                Method m = orderMaterial.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(orderMaterial, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                try {
                                    m.invoke(orderMaterial, new BigDecimal(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }
                            if (contains2) {
                                Method m = order.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(order, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                try {
                                    m.invoke(order, new BigDecimal(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }

                        }
                        if (type.equals("class java.util.Date")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = orderProduct.getClass().getMethod("set" + name, Date.class);
                                try {
                                    m.invoke(orderProduct, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                            if (contains1) {
                                Method m = orderMaterial.getClass().getMethod("set" + name, Date.class);
                                try {
                                    m.invoke(orderMaterial, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                            if (contains2) {
                                Method m = order.getClass().getMethod("set" + name, Date.class);
                                try {
                                    m.invoke(order, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                            continue;
                        }
                    }
                }

            }



            if (xssfRow == null) {
                serviceResult.setErrorCode("没有数据了");
                return serviceResult;
            }
            if (orderProduct == null) {
                serviceResult.setErrorCode(ErrorCode.SYSTEM_ERROR);
                return serviceResult;
            }
            if (orderMaterial == null) {
                serviceResult.setErrorCode(ErrorCode.SYSTEM_ERROR);
                return serviceResult;
            }


            if(productNoFlag){
                orderProductList.add(orderProduct);
            }

            if(materialNoFlag){
                orderMaterialList.add(orderMaterial);
            }

            if (flag) {
                order.setOrderProductList(orderProductList);
                order.setOrderMaterialList(orderMaterialList);
                orderService.createOrder(order);
                orderProductList.clear();
                orderMaterialList.clear();
            }

        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(errorHashMap);
        return serviceResult;

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

    public Map<String, String> creatMap() {

        Map<String, String> maps = new HashMap<>();
        maps.put("'交货方式_FName", "deliveryMode");
        maps.put("产品代码_FName","allName");
        maps.put("数量","allCount");
        maps.put("单价","allUnitAmount");
        maps.put("数量","allCount");
        return maps;
    }

}
