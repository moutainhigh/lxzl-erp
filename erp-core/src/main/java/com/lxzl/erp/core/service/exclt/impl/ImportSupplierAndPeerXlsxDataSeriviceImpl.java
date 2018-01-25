package com.lxzl.erp.core.service.exclt.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.peer.pojo.Peer;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.domain.user.RoleQueryParam;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.exclt.ImportSupplierAndPeerXlsxDataSerivice;
import com.lxzl.erp.core.service.peer.PeerService;
import com.lxzl.erp.core.service.supplier.SupplierService;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/25
 * @Time : Created in 9:48
 */

@Service
public class ImportSupplierAndPeerXlsxDataSeriviceImpl implements ImportSupplierAndPeerXlsxDataSerivice {

    @Autowired
    SupplierService supplierService;
    @Autowired
    PeerService peerService;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Map<String, String>> importData(String str) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ServiceResult<String, Map<String, String>> serviceResult = new ServiceResult<>();
        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\供应商.xlsx");
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("Sheet1");
        Map<String, String> map = creatMap();
        Map<String, String> errorData = new HashMap<>();
        // 获取当前工作薄的每一行
        qqq:
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            //供应商
            Supplier supplier = new Supplier();
            Field[] supplierFields = supplier.getClass().getDeclaredFields();

//            //同行供应商
//            Peer peer = new Peer();
//            Field[] peerFields = peer.getClass().getDeclaredFields();

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

//            供应商
            Map<String, Field> supplierAttributes = new HashMap<>();

            for (Field field : supplierFields) {
                field.setAccessible(true);
                supplierAttributes.put(field.getName(), field);
            }

//            //同行供应商
//            Map<String, Field> peerAttributes = new HashMap<>();
//
//            for (Field field : peerFields) {
//                field.setAccessible(true);
//                peerAttributes.put(field.getName(), field);
//            }


            int j = 0;
            //每行的每个元素
            aaa:
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
//                    判断Supper是否有属性
                    if (supplierAttributes.containsKey(param)) {
                        contains = true;
                    }


//                    boolean contains1 = false;
//                    //判断Peer是否有属性
//                    if (peerAttributes.containsKey(param)) {
//                        contains1 = true;
//                    }


                    XSSFCell xssfCell = xssfRow.getCell(i);
                    if (xssfCell == null) {
                        continue;
                    }
                    //以下都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
                    //这里转换类型都为String
                    String value = getValue(xssfCell);
                    String erroeValue = value;
                    if (value == null || value == "") {
                        continue;
                    }

                    //保存部门名称,后面要用
                    if (stringCellValue.equals("FNumber")) {
                        value = value.replaceAll("\\d+", "");
//                        if(value.equals("THBJ") || value.equals("THGZ") || value.equals("THSH") || value.equals("THSZ") || value.equals("THXM")){
//                            contains = false;
//                        }else {
//                            contains1 = false;
//                        }

                        if (value.equals("FP")) {
                            supplier.setSupplierType(1);
                        } else {
                            supplier.setSupplierType(0);
                        }

                        switch (value) {
                            case "BJ":
                                value = "010";
                                break;
                            case "CD":
                                value = "028";
                                break;
                            case "GZ":
                                value = "020";
                                break;
                            case "NJ":
                                value = "025";
                                break;
                            case "LX":
                            case "FP":
                                value = "1000";
                                break;
                            case "SH":
                                value = "021";
                                break;
                            case "WH":
                                value = "027";
                                break;


//                            case "THBJ":
//                                value = "010";
//                                break;
//                            case "THSH":
//                                value = "021";
//                                break;
//                            case "THSZ":
//                                value = "0755";
//                                break;
//                            case "THXM":
//                                value = "0592";
//                                break;
//                            case "TH":
//                                value = "1000";
//                                break;
//                            case "THGZ":
//                                value = "020";
//                                break;

                        }
                        Pattern pattern = Pattern.compile("[0-9]{1,}");
                        Matcher matcher = pattern.matcher((CharSequence) value);
                        boolean result = matcher.matches();
                        if (!result) {
                            errorData.put(erroeValue, "");
                            continue qqq;
                        }

                    }
                    //判断city


                    //以上都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置

                    //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
                    Field field = null;
                    if (contains) {
                        field = supplierAttributes.get(param);
                    }

//                    if (contains1) {
//                        field = peerAttributes.get(param);
//                    }


                    //开始往属性中set值
                    if (field != null) {
                        String name = param.substring(0, 1).toUpperCase() + param.substring(1); //将属性的首字符大写，方便构造get，set方法
                        String type = field.getGenericType().toString();    //获取属性的类型
                        if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = supplier.getClass().getMethod("set" + name, String.class);
                                m.invoke(supplier, value);    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Double")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            int ping = value.indexOf("平");
                            if (ping > 0) {
                                value = value.substring(0, ping);
                            }
                            if (contains) {
                                Method m = supplier.getClass().getMethod("set" + name, Double.class);
                                m.invoke(supplier, Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Boolean")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = supplier.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(supplier, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Integer")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = supplier.getClass().getMethod("set" + name, Integer.class);
                                try {
                                    m.invoke(supplier, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            continue;
                        }

                        if (type.equals("class java.math.BigDecimal")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = supplier.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(supplier, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                try {
                                    m.invoke(supplier, new BigDecimal(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }

                        }
                        if (type.equals("class java.util.Date")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = supplier.getClass().getMethod("set" + name, Date.class);
                                try {
                                    m.invoke(supplier, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
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

//            ServiceResult<String, Integer> serviceResult1 = peerService.addPeer(peer);
            ServiceResult<String, String> serviceResult1 = supplierService.addSupplier(supplier);
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
        maps.put("FNumber", "cityCode");
//        maps.put("FName", "peerName");
        maps.put("FName", "supplierName");

        return maps;
    }
}
