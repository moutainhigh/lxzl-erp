package com.lxzl.erp.core.service.exclt.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.K3MappingCustomer;
import com.lxzl.erp.common.domain.k3.pojo.K3MappingMaterial;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.exclt.ImplK3MappingCustomerService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingCustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingCustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.K3MappingMaterialDO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/2/2
 * @Time : Created in 10:19
 */
@Service
public class ImplK3MappingCustomerServiceImpl implements ImplK3MappingCustomerService {
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Map<String, String>> importData(String str) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException, InvalidFormatException {

        ServiceResult<String, Map<String, String>> serviceResult = new ServiceResult<>();
        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\销售订单 - 副本.xlsx");

        HashMap<String, String> errorHashMap = new HashMap<>();
        HSSFWorkbook xssfWorkbook = new HSSFWorkbook(fileIn);
        HSSFSheet xssfSheet = xssfWorkbook.getSheet("Page1");

        Map<String, String> map = creatMap();
        // 获取当前工作薄的每一行
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            //用户
            K3MappingCustomer k3MappingCustomer = new K3MappingCustomer();
            Field[] k3MappingCustomerFields = k3MappingCustomer.getClass().getDeclaredFields();
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

            //用户
            Map<String, Field> k3MappingCustomerAttributes = new HashMap<>();

            for (Field field : k3MappingCustomerFields) {
                field.setAccessible(true);
                k3MappingCustomerAttributes.put(field.getName(), field);
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
                    boolean contains = false;
                    //判断k3MappingMaterial是否有属性
                    if (k3MappingCustomerAttributes.containsKey(param)) {
                        contains = true;
                    }

                    HSSFCell xssfCell = xssfRow.getCell(i);
                    if (xssfCell == null) {
                        continue;
                    }
                    //以下都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
                    //这里转换类型都为String
//                    String value = getValue(xssfCell);
                    String value = xssfCell.toString();
                    if (value == null || value == "") {
                        continue aaa;
                    }

                    //保存数据库中的客户NO
                    if (stringCellValue.equals("客户名称_FName")) {
                        CustomerDO customerDO = customerMapper.findByName(value);
                        if (customerDO != null) {
                            k3MappingCustomer.setErpCustomerCode(customerDO.getCustomerNo());
                        } else {
                            errorHashMap.put(value, "");
                        }
                    }

                    //以上都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置

                    //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
                    Field field = null;
                    if (contains) {
                        field = k3MappingCustomerAttributes.get(param);
                    }

                    //开始往属性中set值
                    if (field != null) {
                        String name = param.substring(0, 1).toUpperCase() + param.substring(1); //将属性的首字符大写，方便构造get，set方法
                        String type = field.getGenericType().toString();    //获取属性的类型
                        if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = k3MappingCustomer.getClass().getMethod("set" + name, String.class);
                                m.invoke(k3MappingCustomer, value);    //调用getter方法获取属性值
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
            if (k3MappingCustomer == null) {
                serviceResult.setErrorCode(ErrorCode.SYSTEM_ERROR);
                return serviceResult;
            }

            k3MappingCustomerMapper.save(ConverterUtil.convert(k3MappingCustomer, K3MappingCustomerDO.class));

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
        maps.put("客户名称_FName", "customerName");
        maps.put("客户名称_FNumber", "k3CustomerCode");
        return maps;
    }


    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;

    @Autowired
    private CustomerMapper customerMapper;

}
