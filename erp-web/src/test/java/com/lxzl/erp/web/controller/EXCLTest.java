package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.web.util.NetworkUtil;
import com.lxzl.se.common.util.StringUtil;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
public class EXCLTest extends ERPUnTransactionalTest {
    @Test
    public void say() throws Exception {
        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\企业客户信息及风控模板20180110.xlsx");
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("商务版");
        Map<String, String> map = creatMap();

        // 获取当前工作薄的每一行
//        try {
        for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            //公司
            CustomerCompany customerCompany = new CustomerCompany();
            //风控
            CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
            //客户
            Customer customer = new Customer();
            Field[] customerCompanyFields = customerCompany.getClass().getDeclaredFields();
            Field[] customerRiskManagementFields = customerRiskManagement.getClass().getDeclaredFields();
            Field[] declaredFields = customer.getClass().getDeclaredFields();
            //每一行
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow == null) {
                return;
            }
            if (xssfRow.getLastCellNum() < 0) {
                return;
            }
            Map<String, Field> customerCompanyAttributes = new HashMap<>();

            for (Field field : customerCompanyFields) {
                field.setAccessible(true);
                customerCompanyAttributes.put(field.getName(), field);
            }

            Map<String, Field> customerRiskManagementAttributes = new HashMap<>();

            for (Field field : customerRiskManagementFields) {
                field.setAccessible(true);
                customerRiskManagementAttributes.put(field.getName(), field);
            }

            Map<String, Field> customerAttributes = new HashMap<>();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                customerAttributes.put(field.getName(), field);
            }

            int j = 0;
            //每行的每个元素
            for (int i = 0; i < xssfRow.getLastCellNum(); i++) {
                j = j++;
                //查看是否是这列 并取出这列对应的属性名称
                String stringCellValue = xssfSheet.getRow(1).getCell(i).getStringCellValue();
                if (map.containsKey(stringCellValue)) {
                    String param = map.get(stringCellValue);
                    if (param == null) {
                        continue;
                    }
                    boolean contains = false;
                    boolean contains1 = false;
                    boolean contains2 = false;
                    //判断CustomerCompany是否有属性
                    if (customerCompanyAttributes.containsKey(param)) {
                        contains = true;
                    }

                    //判断CustomerRiskManagement是否有属性
                    if (customerRiskManagementAttributes.containsKey(param)) {
                        contains1 = true;
                    }

                    //判断Customer是否有属性
                    if (customerAttributes.containsKey(param)) {
                        contains2 = true;
                    }

                    XSSFCell xssfCell = xssfRow.getCell(i);
                    if (xssfCell == null) {
                        continue;
                    }
                    //以下都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
                    String value = getValue(xssfCell);
                    if (stringCellValue.equals("租赁方案")) {
                        if (value.contains("%") || value.contains("设备押")) {
                            continue;
                        }
                    }

                    if (stringCellValue.equals("押金期数*")) {
                        if (value.contains("%") || value.contains("设备押")) {
                            continue;
                        }
                    }

                    if (value.indexOf("押") == 0 && value.indexOf("付") > 1) {
                        int indexOf = value.indexOf("押");
                        int indexOf1 = value.indexOf("付");
                        String substring = value.substring(indexOf + 1, indexOf1);
                        String substring1 = value.substring(indexOf1 + 1, indexOf1 + 2);
                        value = substring + substring1;
                    }

                    if (value.indexOf("首付") == 0 && value.indexOf("后付") > 2) {
                        int indexOf = value.indexOf("首付");
                        int lastIndexOf = value.lastIndexOf("后付");
                        String substring1 = value.substring(indexOf + 2, lastIndexOf);
                        String substring = value.substring(lastIndexOf + 2, value.length());
                        value = substring + substring1;
                    }

                    if (value.indexOf("首付押") == 0 && value.indexOf("后付押") > 6) {
                        int index = value.indexOf("押");
                        int index1 = value.indexOf("付", 2);
                        int index2 = value.lastIndexOf("押");
                        int index3 = value.lastIndexOf("付");
                        int index4 = value.indexOf("，");

                        String substring = value.substring(index + 1, index1);
                        String substring1 = value.substring(index1 + 1, index4);
                        String substring2 = value.substring(index2 + 1, index3);
                        String substring3 = value.substring(index3 + 1, index3 + 2);
                        value = substring + substring1 + substring2 + substring3;
                    }

                    if ("全额押金".equals(value)) {
                        continue;
                    }

                    if (value == "" || StringUtil.isBlank(value)) {
                        continue;
                    }
                    int qwe = 0;
                    boolean flag = false;

                    if (value.indexOf("Y") == 0) {
                        qwe = 0;
                        flag = true;
                    }

                    if (value.indexOf("单台") == 0) {
                        value = value.substring(2);
                    }

                    if (stringCellValue.equals("经营面积")) {
                        if (value.contains("无")) {
                            qwe = 0;
                            flag = true;
                        }
                    }

                    switch (value) {
                        case "是":
                            qwe = 1;
                            flag = true;
                            break;
                        case "否":
                            flag = true;
                            break;
                        case "线上":
                            qwe = 8;
                            flag = true;
                            break;
                        case "线下":
                            qwe = 9;
                            flag = true;
                            break;
                        case "先付后用":
                            qwe = 2;
                            flag = true;
                            break;
                        case "先用后付":
                            qwe = 1;
                            flag = true;
                            break;
                        case "每2个月回访一次":
                            qwe = 2;
                            flag = true;
                            break;
                        case "每2个月回访一次。":
                            qwe = 2;
                            flag = true;
                            break;
                        case "每两个月回访一次":
                            qwe = 2;
                            flag = true;
                            break;
                        case "每两月回访一次":
                            qwe = 2;
                            flag = true;
                            break;
                        case "每月回访一次":
                            qwe = 2;
                            flag = true;
                            break;
                        case "每两月回复一次":
                            qwe = 2;
                            flag = true;
                            break;
                        case "每季度回访一次":
                            qwe = 3;
                            flag = true;
                            break;
                        case "每周回访一次":
                            qwe = 3;
                            flag = true;
                            break;
                        case "地推活动":
                            qwe = 1;
                            flag = true;
                            break;
                        case "展会了解":
                            qwe = 2;
                            flag = true;
                            break;
                        case "业务联系":
                            qwe = 3;
                            flag = true;
                            break;
                        case "百度推广":
                            qwe = 4;
                            flag = true;
                            break;
                        case "朋友推荐":
                            qwe = 5;
                            flag = true;
                            break;
                        case "其他广告":
                            qwe = 6;
                            flag = true;
                            break;
                    }
                    if (flag) {
                        value = String.valueOf(qwe);
                    }
                    //以上都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置

                    //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
                    Field field = null;
                    if (contains) {
                        field = customerCompanyAttributes.get(param);
                    }
                    if (contains1) {
                        field = customerRiskManagementAttributes.get(param);
                    }
                    if(contains2){
                        field = customerAttributes.get(param);
                    }

                    //开始往属性中set值
                    if (field != null) {
                        String name = param.substring(0, 1).toUpperCase() + param.substring(1); //将属性的首字符大写，方便构造get，set方法
                        String type = field.getGenericType().toString();    //获取属性的类型
                        if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = customerCompany.getClass().getMethod("set" + name, String.class);
                                m.invoke(customerCompany, value);    //调用getter方法获取属性值
                            }
                            if (contains1) {
                                Method m = customerRiskManagement.getClass().getMethod("set" + name, String.class);
                                m.invoke(customerRiskManagement, value);    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = customer.getClass().getMethod("set" + name, String.class);
                                m.invoke(customer, value);    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Double")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            int ping = value.indexOf("平");
                            if (ping > 0) {
                                value = value.substring(0, ping);

                            }

                            if (contains) {
                                Method m = customerCompany.getClass().getMethod("set" + name, Double.class);
                                m.invoke(customerCompany, Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            if (contains1) {
                                Method m = customerRiskManagement.getClass().getMethod("set" + name, Double.class);
                                m.invoke(customerRiskManagement, Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = customer.getClass().getMethod("set" + name, Double.class);
                                m.invoke(customer, Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Boolean")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = customerCompany.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(customerCompany, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            if (contains1) {
                                Method m = customerRiskManagement.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(customerRiskManagement, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = customer.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(customer, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Integer")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = customerCompany.getClass().getMethod("set" + name, Integer.class);
                                m.invoke(customerCompany, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            if (contains1) {
                                Method m = customerRiskManagement.getClass().getMethod("set" + name, Integer.class);
                                m.invoke(customerRiskManagement, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = customer.getClass().getMethod("set" + name, Integer.class);
                                m.invoke(customer, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }

                        if (type.equals("class java.math.BigDecimal")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = customerCompany.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = null;
                                    try {
                                        bd = new BigDecimal(aaa);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(customerCompany, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                try{
                                    m.invoke(customerCompany, new BigDecimal(value));    //调用getter方法获取属性值
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                continue;
                            }

                            if (contains1) {
                                Method m = customerRiskManagement.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(customerRiskManagement, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                m.invoke(customerRiskManagement, new BigDecimal(value));    //调用getter方法获取属性值
                                continue;
                            }
                            if (contains2) {
                                Method m = customer.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(customer, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                m.invoke(customer, new BigDecimal(value));    //调用getter方法获取属性值
                                continue;
                            }

                        }
                        if (type.equals("class java.util.Date")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = customerCompany.getClass().getMethod("set" + name, Date.class);
                                m.invoke(customerCompany, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                            }
                            if (contains1) {
                                Method m = customerRiskManagement.getClass().getMethod("set" + name, Date.class);
                                m.invoke(customerRiskManagement, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = customer.getClass().getMethod("set" + name, Date.class);
                                m.invoke(customer, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                    }
                }

            }

            if (xssfRow == null) {
                return;
            }

            customer.setCustomerCompany(customerCompany);
            customer.setCustomerRiskManagement(customerRiskManagement);
            TestResult testResult = getJsonTestResult("/customer/addCompany", customer);
            System.out.println(customer);
        }
//        } catch (Exception e) {
        //错误提示
//            String stringCellValue = xssfSheet.getRow(1).getCell(j).getStringCellValue();
//            Logger log = LoggerFactory.getLogger(NetworkUtil.class);
//            log.debug("列:" + stringCellValue + "错误,客户编号:" + customerCompany.getCustomerNo());
        //这里错误回滚
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
//        }
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

    public Map<Integer, String> generateProperty(Object... args) throws Exception {
        Map<Integer, String> map = new HashMap<>();

        for (Object model : args) {
            Field[] field = model.getClass().getDeclaredFields();        //获取实体类的所有属性，返回Field数组
            for (int j = 0; j < field.length; j++) {     //遍历所有属性
                String name = field[j].getName();    //获取属性的名字

                System.out.println("attribute name:" + name);
                name = name.substring(0, 1).toUpperCase() + name.substring(1); //将属性的首字符大写，方便构造get，set方法
                String type = field[j].getGenericType().toString();    //获取属性的类型
                if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = model.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(model);    //调用getter方法获取属性值
                    if (value != null) {

                        System.out.println("attribute value:" + value);
                    }
                }
                if (type.equals("class java.lang.Integer")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value);
                    }
                }
                if (type.equals("class java.lang.Short")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Short value = (Short) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value);
                    }
                }
                if (type.equals("class java.lang.Double")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Double value = (Double) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value);
                    }
                }
                if (type.equals("class java.lang.Boolean")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Boolean value = (Boolean) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value);
                    }
                }
                if (type.equals("class java.util.Date")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Date value = (Date) m.invoke(model);
                    if (value != null) {
                        System.out.println("attribute value:" + value.toLocaleString());
                    }
                }
            }
        }
        return map;
    }

    /**
     * xlsx表的数据
     */

    public Map<String, String> creatMap() {

        Map<String, String> maps = new HashMap<>();
        maps.put("客户编码*", "customerNo");
        maps.put("客户名称*", "companyName");
        maps.put("业务员*", "ownerName");
        maps.put("联合区域", "unionArea");
        maps.put("联合业务员", "unionSalesMan");
        maps.put("客户来源*", "customerOrigin");
        maps.put("统一信用代码", "unifiedCreditCode");
        maps.put("是否法人代表申请", "isLegalPersonApple");
        maps.put("法人姓名*", "legalPerson");
        maps.put("法人身份证号码*", "legalPersonNo");
        maps.put("法人手机号*", "legalPersonPhone");
        maps.put("单位座机", "landline");
        maps.put("经办人姓名*", "agentPersonName");
        maps.put("经办人身份证号码*", "agentPersonNo");
        maps.put("经办人手机号吗*", "agentPersonPhone");
        maps.put("紧急联系人姓名*", "connectRealName");
        maps.put("紧急联系人手机号码* ", "connectPhone");
        maps.put("公司地址*", "address");
        maps.put("收货地址*", "consignAddress");
        maps.put("注册资本", "registeredCapital");
        maps.put("成立时间", "companyFoundTime");
        maps.put("所属行业", "industry");
        maps.put("经营面积", "operatingArea");
        maps.put("办公人数", "officeNumber");
        maps.put("设备用途*", "productPurpose");
        maps.put("单位参保人数", "unitInsuredNumber");
        maps.put("关联企业", "affiliatedEnterprise");
        maps.put("首次所需设备*", "listFirstNeedProducts");
        maps.put("后期所需设备", "listLaterNeedProducts");
        maps.put("前端备注", "remark");
        maps.put("授信额度*", "creditAmount");
        maps.put("押金期数*", "depositCycle");
        maps.put("付款期数*", "paymentCycle");
        maps.put("支付方式*", "payMode");
        maps.put("租赁方案", null);
        maps.put("单台设备价值*", "singleLimitPrice");
        maps.put("是否限制苹果*", "isLimitApple");
        maps.put("苹果押金期数*", "appleDepositCycle");
        maps.put("苹果付款期数*", "applePaymentCycle");
        maps.put("苹果设备支付方式*", "applePayMode");
        maps.put("是否限制全新*", "isLimitNew");
        maps.put("全新设备押金期数*", "newDepositCycle");
        maps.put("全新设备付款期数*", "newPaymentCycle");
        maps.put("全新设备支付方式*", "newPayMode");
        maps.put("回访频率（*个月回访一次）", "returnVisitFrequency");
        maps.put("送货方式", null);
        maps.put("是否支持货到付款", null);
        maps.put("风控备注", "remark");
        maps.put("后期申请额度", "laterApplyAmount");
        maps.put("首期申请额度", "firstApplyAmount");

        return maps;
    }


}


