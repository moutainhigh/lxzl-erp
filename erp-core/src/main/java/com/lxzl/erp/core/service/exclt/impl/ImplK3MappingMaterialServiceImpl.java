package com.lxzl.erp.core.service.exclt.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.pojo.K3MappingMaterial;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.exclt.ImplK3MappingMaterialService;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
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
 * @Date : Created in 2018/2/1
 * @Time : Created in 21:12
 */
@Service
public class ImplK3MappingMaterialServiceImpl implements ImplK3MappingMaterialService {

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Map<String, String>> importData(String str) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException, InvalidFormatException {

        ServiceResult<String, Map<String, String>> serviceResult = new ServiceResult<>();
        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\销售订单 - 副本.xlsx");
//        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
//        XSSFSheet xssfSheet = xssfWorkbook.getSheet("Page2");

        HSSFWorkbook xssfWorkbook = new HSSFWorkbook(fileIn);
        HSSFSheet xssfSheet = xssfWorkbook.getSheet("Page2");

        Map<String, String> map = creatMap();
        Map<String, String> errorData = new HashMap<>();
        // 获取当前工作薄的每一行
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            //用户
            K3MappingMaterial k3MappingMaterial = new K3MappingMaterial();
            Field[] k3MappingMaterialFields = k3MappingMaterial.getClass().getDeclaredFields();
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
            Map<String, Field> k3MappingMaterialAttributes = new HashMap<>();

            for (Field field : k3MappingMaterialFields) {
                field.setAccessible(true);
                k3MappingMaterialAttributes.put(field.getName(), field);
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
                    if (k3MappingMaterialAttributes.containsKey(param)) {
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

                    //保存数据库中的物料NO
                    if (stringCellValue.equals("产品代码_FName")) {
                        List<String> materialByNameList = materialMapper.findMaterialByName(value);
                        if (CollectionUtil.isNotEmpty(materialByNameList)) {
                            String materialNo = materialByNameList.get(0);
                            k3MappingMaterial.setErpMaterialCode(materialNo);
                        }
                    }

                    //以上都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置

                    //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
                    Field field = null;
                    if (contains) {
                        field = k3MappingMaterialAttributes.get(param);
                    }

                    //开始往属性中set值
                    if (field != null) {
                        String name = param.substring(0, 1).toUpperCase() + param.substring(1); //将属性的首字符大写，方便构造get，set方法
                        String type = field.getGenericType().toString();    //获取属性的类型
                        if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = k3MappingMaterial.getClass().getMethod("set" + name, String.class);
                                m.invoke(k3MappingMaterial, value);    //调用getter方法获取属性值
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
            if (k3MappingMaterial == null) {
                serviceResult.setErrorCode(ErrorCode.SYSTEM_ERROR);
                return serviceResult;
            }
            k3MappingMaterialMapper.save(ConverterUtil.convert(k3MappingMaterial, K3MappingMaterialDO.class));
            return null;
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
        maps.put("单据号_FBillno", "k3MaterialCode");
        maps.put("产品代码_FName", "materialName");
        return maps;
    }


    @Autowired
    private K3MappingMaterialMapper k3MappingMaterialMapper;

    @Autowired
    private MaterialMapper materialMapper;

}
