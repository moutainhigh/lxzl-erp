package com.lxzl.erp.core.service.exclt.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.exclt.ImportMaterialService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.basic.BrandMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3MappingMaterialTypeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialModelMapper;
import com.lxzl.erp.dataaccess.domain.material.MaterialModelDO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/2/2
 * @Time : Created in 17:37
 */
@Service
public class ImportMaterialServiceImpl implements ImportMaterialService {

    @Autowired
    private K3MappingMaterialMapper k3MappingMaterialMapper;

    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private K3MappingMaterialTypeMapper k3MappingMaterialTypeMapper;

    @Autowired
    private MaterialModelMapper materialModelMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Map<String, String>> importData(String str) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException, InvalidFormatException {

        ServiceResult<String, Map<String, String>> serviceResult = new ServiceResult<>();
        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\配件模板-20180203(1).xlsx");

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("Sheet1");

        Map<String, String> map = creatMap();
        Map<String, String> errorHashMap = creatMap();
        // 获取当前工作薄的每一行
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            //物料
            Material material = new Material();
            Field[] materialFields = material.getClass().getDeclaredFields();
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

            //物料
            Map<String, Field> materialAttributes = new HashMap<>();

            for (Field field : materialFields) {
                field.setAccessible(true);
                materialAttributes.put(field.getName(), field);
            }
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
                    //判断material是否有属性
                    if (materialAttributes.containsKey(param)) {
                        contains = true;
                    }
                    XSSFCell xssfCell = xssfRow.getCell(i);
                    if (xssfCell == null) {
                        continue;
                    }
                    //以下都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
                    //这里转换类型都为String
                    String value = getValue(xssfCell);
//                    String value = xssfCell.toString();
                    if (value == null || value == "") {
                        continue aaa;
                    }

                    if(value.equals("2.5寸 SSD固态硬盘 500G")){
                        System.out.println("");
                    }

//                    保存数据库中的配件类型id
                    if (stringCellValue.equals("小分类型号")) {
                        String materialId = k3MappingMaterialTypeMapper.findMaterialTypeIdByK3MaterialTypeCode(value);
                        if(materialId == null || "".equals(materialId)){
                            material.setMaterialType(200);
                            errorHashMap.put(value,"");
                            continue ;
                        }
                        material.setMaterialType(Integer.parseInt(materialId));
                        continue ;
                    }

                    if (stringCellValue.equals("品牌")) {
                        Integer brandIdByName = brandMapper.findBrandIdByName(value);
                        material.setBrandId(brandIdByName);
                        continue ;
                    }

                    if (stringCellValue.equals("配件型号（内存硬盘不填）")) {
                        Integer modelId = materialModelMapper.findMaterialModeIdlByModelName(value);
                        if (modelId == null || modelId == 0) {
                            MaterialModelDO materialModelDO = new MaterialModelDO();
                            Date now = new Date();
                            materialModelDO.setMaterialType(material.getMaterialType());
                            materialModelDO.setModelName(value);
                            materialModelDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
                            materialModelDO.setCreateTime(now);
                            materialModelDO.setCreateUser("500001");
                            materialModelDO.setUpdateTime(now);
                            materialModelDO.setUpdateUser("500001");
                            materialModelMapper.save(materialModelDO);
                            material.setMaterialModelId(materialModelDO.getId());
                            continue ;
                        } else {
                            material.setMaterialModelId(modelId);
                            continue ;
                        }
                    }

                    //以上都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置

                    //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
                    Field field = null;
                    if (contains) {
                        field = materialAttributes.get(param);
                    }

                    //开始往属性中set值
                    if (field != null) {
                        String name = param.substring(0, 1).toUpperCase() + param.substring(1); //将属性的首字符大写，方便构造get，set方法
                        String type = field.getGenericType().toString();    //获取属性的类型
                        if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = material.getClass().getMethod("set" + name, String.class);
                                m.invoke(material, value);    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Double")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            int ping = value.indexOf("平");
                            if (ping > 0) {
                                value = value.substring(0, ping);
                            }
                            if (contains) {
                                Method m = material.getClass().getMethod("set" + name, Double.class);
                                m.invoke(material, Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Boolean")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = material.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(material, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Integer")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = material.getClass().getMethod("set" + name, Integer.class);
                                try {
                                    m.invoke(material, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            continue;
                        }

                        if (type.equals("class java.math.BigDecimal")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = material.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(material, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                try {
                                    m.invoke(material, new BigDecimal(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }

                        }
                        if (type.equals("class java.util.Date")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = material.getClass().getMethod("set" + name, Date.class);
                                try {
                                    m.invoke(material, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
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
            if (material == null) {
                serviceResult.setErrorCode(ErrorCode.SYSTEM_ERROR);
                return serviceResult;
            }
            ServiceResult<String, String> serviceResult1 = materialService.addMaterial(material);
            System.out.println();

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
        maps.put("小分类型号","");
        maps.put("配件型号（内存硬盘不填）", "materialModelId");
        maps.put("英文型号", "materialModel");
        maps.put("配件名称", "materialName");
        maps.put("配件字面量（内存硬盘）", "materialCapacityValue");
        maps.put("品牌","");
        maps.put("全新价值", "newMaterialPrice");
        maps.put("全新日租价", "newDayRentPrice");
        maps.put("全新月租价", "newMonthRentPrice");
        maps.put("次新价值", "materialPrice");
        maps.put("次新日租价", "dayRentPrice");
        maps.put("次新月租价", "monthRentPrice");
        maps.put("备注", "remark");
        return maps;
    }
}
