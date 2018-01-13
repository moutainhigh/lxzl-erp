package com.lxzl.erp.core.service.exclt.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerConsignInfo;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.exclt.EXCLService;
import com.lxzl.se.common.util.StringUtil;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/10
 * @Time : Created in 21:05
 */
@Service
public class EXCLServiceImpl implements EXCLService {
    @Autowired
    CustomerService customerService;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Map<String, String>> importData(String str) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
        ServiceResult<String, Map<String, String>> serviceResult = new ServiceResult<>();

        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\企业客户信息及风控模板-电销2018-1-9(5).xlsx");
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("客户资料（企业客户基本信息=商务提供，风控结果=风控复核提供）");
        Map<String, String> map = creatMap();
        Map<String, String> errorData = new HashMap<>();
        // 获取当前工作薄的每一行
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {

            //公司
            CustomerCompany customerCompany = new CustomerCompany();
            //风控
            CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
            //客户
            Customer customer = new Customer();
            //收货地址
            CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
            Field[] customerCompanyFields = customerCompany.getClass().getDeclaredFields();
            Field[] customerRiskManagementFields = customerRiskManagement.getClass().getDeclaredFields();
            Field[] customerFields = customer.getClass().getDeclaredFields();
            Field[] CustomerConsignInfoFields = customerConsignInfo.getClass().getDeclaredFields();
            //每一行
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow == null) {
                serviceResult.setErrorCode("行为空");
                return serviceResult;
            }
            if (xssfRow.getLastCellNum() < 0) {
                serviceResult.setErrorCode("行数小于0");
                return serviceResult;
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

            for (Field field : customerFields) {
                field.setAccessible(true);
                customerAttributes.put(field.getName(), field);
            }

            Map<String, Field> CustomerConsignInfoAttributes = new HashMap<>();

            for (Field field : CustomerConsignInfoFields) {
                field.setAccessible(true);
                CustomerConsignInfoAttributes.put(field.getName(), field);
            }
            int j = 0;
            //每行的每个元素
            for (int i = 0; i < xssfRow.getLastCellNum(); i++) {
                j = j++;
                //查看是否是这列 并取出这列对应的属性名称
                String stringCellValue = null;
                try {
                    XSSFRow row = xssfSheet.getRow(0);
                    XSSFCell cell = row.getCell(i);
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
                    boolean contains = false;
                    boolean contains1 = false;
                    boolean contains2 = false;
                    boolean contains3 = false;
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

                    //判断Customer是否有属性
                    if (CustomerConsignInfoAttributes.containsKey(param)) {
                        contains3 = true;
                    }

                    XSSFCell xssfCell = xssfRow.getCell(i);
                    if (xssfCell == null) {
                        continue;
                    }
                    //以下都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
                    String value = getValue(xssfCell);
                    if (stringCellValue.equals("业务员*")) {
                        continue;
                    }
//                    if(value == null || value.equals("")){
//                        continue;
//                    }
                    if (stringCellValue.equals("经营面积")) {
                        if (value.contains("m²")) {
                            value = value.substring(0, value.lastIndexOf("m²"));
                        }
                    }

                    if (stringCellValue.equals("经营面积") || stringCellValue.equals("注册资本") || stringCellValue.equals("办公人数") || stringCellValue.equals("单位参保人数")) {
                        if (value.contains("万")) {
                            value = value.substring(0, value.lastIndexOf("万"));
                        }
                        if (value.contains("平")) {
                            value = value.substring(0, value.lastIndexOf("平"));
                        }
                        if (value.contains("人")) {
                            value = value.substring(0, value.lastIndexOf("人"));
                        }
                        if (value.contains("无")) {
                            value = "0";
                        }

                        if (!isNumeric(value)) {
                            errorData.put(customerCompany.getCompanyName(), stringCellValue);
                            continue;
                        }
                    }


                    if (stringCellValue.equals("所属行业")) {
                        switch (value) {
                            case "计算机软硬件(互联网推广_APP开发等)":
                            case "计算机软硬件（互联网推广_APP开发等）":
                            case "计算机硬件（互联网推广_APP开发等）":
                                value = "1";
                                break;
                            case "互联网电子商务":
                                value = "2";
                                break;
                            case "网络游戏_视频直播_微信营销":
                                value = "3";
                                break;
                            case "通信_电信_电子产品":
                            case "通讯_电信_电子产品":
                                value = "4";
                                break;
                            case "互联网金融_贷款_期货现货贵金属_分期支付_担保拍卖":
                                value = "5";
                                break;
                            case "实业投资_保险_证券_银行":
                                value = "6";
                                break;
                            case "教育_培训":
                                value = "7";
                                break;
                            case "政府_公共事业_非盈利性机构":
                                value = "8";
                                break;
                            case "媒体_出版_影视_文化传播":
                                value = "9";
                                break;
                            case "婚纱摄影_旅游度假_酒店餐饮":
                                value = "10";
                                break;
                            case "服务娱乐_医疗美容":
                                value = "11";
                                break;
                            case "专业服务(租赁服务_企业注册_人力资源_贸易报关_中介咨询_实体广告等)":
                            case "专业服务（租赁服务_企业注册_人力资源_贸易报关_中介咨询_实体广告等）":
                                value = "12";
                                break;
                            case "展览会议_公关活动":
                                value = "13";
                                break;
                            case "房地产_建筑建设_开发":
                                value = "14";
                                break;
                            case "家居建材_装饰设计":
                            case "家具建材_装饰设计":
                                value = "15";
                                break;
                            case "交通运输_物流仓储_供应链":
                                value = "16";
                                break;
                            case "维修安装_家政_叫车服务":
                                value = "17";
                                break;

                            case "加工制造_工业自动化_汽车摩托车销售":
                                value = "18";
                                break;
                            case "快速消费品(服饰日化_食品烟酒等)":
                            case "快速消费品（服饰日化_食品烟酒等）":
                                value = "19";
                                break;
                            case "其他":
                            case "其它":
                                value = "99";
                                break;
                        }
                    }

                    if (stringCellValue.equals("业务员*")) {
                        switch (value) {
                            case "admin":
                                value = "500001";
                                break;
                            case "喻晓艳":
                                value = "500002";
                                break;
                            case "谢朋叶":
                                value = "500003";
                                break;
                            case "陈发俐":
                                value = "500004";
                                break;
                            case "王文怡":
                                value = "500005";
                                break;
                            case "秦汉印":
                                value = "500006";
                                break;
                            case "尹鸿熙":
                                value = "500007";
                                break;
                            case "古建宇":
                                value = "500008";
                                break;
                            case "谢林鹏":
                                value = "500009";
                                break;
                            case "高来春":
                                value = "500010";
                                break;
                            case "宋运芳":
                                value = "500011";
                                break;
                            case "唐友元":
                                value = "500012";
                                break;
                        }
                    }

                    if (stringCellValue.equals("前端备注")) {
                        contains = true;
                        contains1 = false;
                        contains2 = true;
                        contains3 = false;
                    }

                    if (stringCellValue.equals("公司地址*")) {
                        contains = true;
                        contains1 = false;
                        contains2 = false;
                        contains3 = false;
                    }

                    if (stringCellValue.equals("收货地址*")) {
                        contains = false;
                        contains1 = false;
                        contains2 = false;
                        contains3 = true;
                    }
                    if (stringCellValue.equals("风控备注")) {
                        contains = false;
                        contains1 = true;
                        contains2 = false;
                        contains3 = false;
                    }


                    if (stringCellValue.equals("租赁方案")) {
                        if (value.contains("%") || value.contains("设备押")) {
                            value = "1";
                        }
                    }

                    if (stringCellValue.equals("押金期数*")) {
                        if (value.contains("%") || value.contains("设备押")) {
                            value = "1";
                        }
                        if (!isNumeric(value)) {
                            value = "1";
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
                        value = substring1 + substring;
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
                        value = "1";
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
                        case "先用后付":
                        case "每周回访一次":
                        case "地推活动":
                        case "每月回访一次":
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
                        case "每2个月回访一次":
                        case "每2个月回访一次。":
                        case "每两个月回访一次":
                        case "每两月回访一次":
                        case "每两月回复一次":
                        case "展会了解":
                            qwe = 2;
                            flag = true;
                            break;
                        case "每季度回访一次":
                        case "主动开发":
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
                    if (contains2) {
                        field = customerAttributes.get(param);
                    }
                    if (contains3) {
                        field = CustomerConsignInfoAttributes.get(param);
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
                            if (contains3) {
                                Method m = customerConsignInfo.getClass().getMethod("set" + name, String.class);
                                m.invoke(customerConsignInfo, value);    //调用getter方法获取属性值
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
                            if (contains3) {
                                Method m = customerConsignInfo.getClass().getMethod("set" + name, Double.class);
                                m.invoke(customerConsignInfo, Double.parseDouble(value));    //调用getter方法获取属性值
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
                            if (contains3) {
                                Method m = customerConsignInfo.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(customerConsignInfo, Boolean.parseBoolean(value));    //调用getter方法获取属性值
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
                                try {
                                    m.invoke(customer, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (contains3) {
                                Method m = customerConsignInfo.getClass().getMethod("set" + name, Integer.class);
                                m.invoke(customerConsignInfo, (int) Double.parseDouble(value));    //调用getter方法获取属性值
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
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(customerCompany, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                try {
                                    m.invoke(customerCompany, new BigDecimal(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
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
                                try {
                                    m.invoke(customer, new BigDecimal(value));    //调用getter方法获取属性值
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                continue;
                            }
                            if (contains3) {
                                Method m = customerConsignInfo.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(customerConsignInfo, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                m.invoke(customerConsignInfo, new BigDecimal(value));    //调用getter方法获取属性值
                                continue;
                            }

                        }
                        if (type.equals("class java.util.Date")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = customerCompany.getClass().getMethod("set" + name, Date.class);
                                try {
                                    m.invoke(customerCompany, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                            if (contains1) {
                                Method m = customerRiskManagement.getClass().getMethod("set" + name, Date.class);
                                m.invoke(customerRiskManagement, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                            }
                            if (contains2) {
                                Method m = customer.getClass().getMethod("set" + name, Date.class);
                                m.invoke(customer, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                            }
                            if (contains3) {
                                Method m = customerConsignInfo.getClass().getMethod("set" + name, Date.class);
                                m.invoke(customerConsignInfo, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
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
            customer.setCustomerCompany(customerCompany);
            customer.setCustomerRiskManagement(customerRiskManagement);
            customer.setCustomerConsignInfo(customerConsignInfo);

            ServiceResult<String, String> stringStringServiceResult = customerService.addCompany(customer);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(errorData);
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
        maps.put("客户编码*", "customerNo");
        maps.put("客户名称*", "companyName");
        maps.put("业务员*", "owner");
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
        maps.put("收货地址*", "address");
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
//        maps.put("回访频率（*个月回访一次）", "returnVisitFrequency");
        maps.put("回访频率", "returnVisitFrequency");
        maps.put("送货方式", null);
        maps.put("是否支持货到付款", null);
        maps.put("风控备注", "remark");
        maps.put("后期申请额度", "laterApplyAmount");
        maps.put("首期申请额度", "firstApplyAmount");

        return maps;
    }

}
