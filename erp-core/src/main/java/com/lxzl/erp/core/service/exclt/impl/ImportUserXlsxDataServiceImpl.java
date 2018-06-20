package com.lxzl.erp.core.service.exclt.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.domain.user.RoleQueryParam;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.exclt.ImportUserXlsxDataService;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.se.common.util.secret.MD5Util;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/23
 * @Time : Created in 16:06
 */
@Service
public class ImportUserXlsxDataServiceImpl implements ImportUserXlsxDataService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    UserSupport userSupport;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Autowired
    SubCompanyMapper subCompanyMapper;

    @Autowired
    UserRoleService userRoleService;

    /**
     * 导入用户数据
     *
     * @param : str
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/23 16:08
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Map<String, String>> importData(String str) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
        ServiceResult<String, Map<String, String>> serviceResult = new ServiceResult<>();

        FileInputStream fileIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\用户信息模板20180206.xlsx");
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("Sheet1");
        Map<String, String> map = creatMap();
        Map<String, String> errorData = new HashMap<>();
        // 获取当前工作薄的每一行
        for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            //用户
            User user = new User();
            Field[] userFields = user.getClass().getDeclaredFields();
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
            //部门名称
            HashMap<String, String> queryParam = new HashMap<>();

            //用户
            Map<String, Field> userAttributes = new HashMap<>();

            for (Field field : userFields) {
                field.setAccessible(true);
                userAttributes.put(field.getName(), field);
            }
            int j = 0;
            //每行的每个元素
            aaa:
            for (int i = 0; i < xssfRow.getLastCellNum(); i++) {
                j = j++;
                //查看是否是这列 并取出这列对应的属性名称
                String stringCellValue = null;
                try {
                    XSSFRow row = xssfSheet.getRow(1);
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
                    //判断User是否有属性
                    if (userAttributes.containsKey(param)) {
                        contains = true;
                    }

                    XSSFCell xssfCell = xssfRow.getCell(i);
                    if (xssfCell == null) {
                        continue;
                    }
                    //以下都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置
                    //这里转换类型都为String
                    String value = getValue(xssfCell);
                    if (value == null || value == "") {
                        continue aaa;
                    }
                    if (value.equals("会计")) {
                        System.out.println();
                    }

                    if (stringCellValue.equals("禁用")) {
                        switch (value) {
                            case "否":
                                value = "0";
                                break;
                            case "是":
                                value = "1";
                                break;
                        }
                    }


                    //保存部门名称,后面要用
                    if (stringCellValue.equals("部门（与系统中名字相同）")) {
                        queryParam.put("departmentName", value);
                    }

                    //保存角色名称,后面要用
                    if (stringCellValue.equals("角色")) {
                        queryParam.put("roleName", value);

                    }
                    if (value.equals("别永刚")) {
                        System.out.println();
                    }
                    //保存角色名称,后面要用
                    if (stringCellValue.equals("分公司")) {
                        queryParam.put("subCompanyName", value);
                        if (value.equals("上海分公司")) {
                            System.out.println();
                        }
                    }


                    //以上都是一些文字的判断,以后还需要根据文件的内容不同增加或者重新设置

                    //判断要用那个类型  这是同一个表,且没用到同样的列的情况下可以使用   用到了同一列的还需增加代码
                    Field field = null;
                    if (contains) {
                        field = userAttributes.get(param);
                    }

                    //开始往属性中set值
                    if (field != null) {
                        String name = param.substring(0, 1).toUpperCase() + param.substring(1); //将属性的首字符大写，方便构造get，set方法
                        String type = field.getGenericType().toString();    //获取属性的类型
                        if (type.equals("class java.lang.String")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = user.getClass().getMethod("set" + name, String.class);
                                m.invoke(user, value);    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Double")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            int ping = value.indexOf("平");
                            if (ping > 0) {
                                value = value.substring(0, ping);
                            }
                            if (contains) {
                                Method m = user.getClass().getMethod("set" + name, Double.class);
                                m.invoke(user, Double.parseDouble(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Boolean")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = user.getClass().getMethod("set" + name, Boolean.class);
                                m.invoke(user, Boolean.parseBoolean(value));    //调用getter方法获取属性值
                            }
                            continue;
                        }
                        if (type.equals("class java.lang.Integer")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = user.getClass().getMethod("set" + name, Integer.class);
                                try {
                                    m.invoke(user, (int) Double.parseDouble(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            continue;
                        }

                        if (type.equals("class java.math.BigDecimal")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = user.getClass().getMethod("set" + name, BigDecimal.class);
                                int i1 = value.length() - 1;
                                int i2 = value.lastIndexOf("万");
                                if (i2 == i1) {
                                    String aaa = value.substring(0, i2);
                                    BigDecimal bd = new BigDecimal(aaa);
                                    BigDecimal bd1 = new BigDecimal("10000");
                                    BigDecimal multiply = bd.multiply(bd1);
                                    BigDecimal bigDecimal = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
                                    m.invoke(user, bigDecimal);    //调用getter方法获取属性值
                                    continue;
                                }
                                try {
                                    m.invoke(user, new BigDecimal(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }

                        }
                        if (type.equals("class java.util.Date")) {   //如果type是类类型，则前面包含"class "，后面跟类名
                            if (contains) {
                                Method m = user.getClass().getMethod("set" + name, Date.class);
                                try {
                                    m.invoke(user, new SimpleDateFormat("yyyy/MM/dd").parse(value));    //调用getter方法获取属性值
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                            continue;
                        }
                    }
                }

            }

            boolean flag = false;
            Map<Object, Object> map1 = new HashMap<>();
            Integer departmentId = null;
            if (queryParam != null && queryParam.size() > 0) {
                //获取分公司id
                HashMap<String, Object> subCompanyHashMap = new HashMap<>();
                SubCompanyQueryParam subCompanyQueryParam = new SubCompanyQueryParam();
                subCompanyQueryParam.setSubCompanyName(queryParam.get("subCompanyName"));
                subCompanyHashMap.put("subCompanyQueryParam", subCompanyQueryParam);
                subCompanyHashMap.put("start", 0);
                subCompanyHashMap.put("pageSize", Integer.MAX_VALUE);
                List<SubCompanyDO> subCompanyDOList = subCompanyMapper.listPage(subCompanyHashMap);
                SubCompanyDO subCompanyDO = subCompanyDOList.get(0);

                String departmentName = queryParam.get("departmentName");
                if (subCompanyDO != null) {
                    //分公司id获取部门信息
                    HashMap<String, Object> departmentQueryParamMap = new HashMap<>();
                    DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();

                    departmentQueryParam.setSubCompanyId(subCompanyDO.getId());
                    departmentQueryParamMap.put("departmentQueryParam", departmentQueryParam);
                    departmentQueryParamMap.put("start", 0);
                    departmentQueryParamMap.put("pageSize", Integer.MAX_VALUE);
                    List<DepartmentDO> departmentDOList = departmentMapper.listPage(departmentQueryParamMap);
                    //判断是否有这些部门

                    //获取父部门id
                    Integer parentDepartmentId = null;
                    int i = departmentName.indexOf("-");
                    if (i > 0) {
                        haha:
                        for (DepartmentDO departmentDO : departmentDOList) {
                            String departmentName1 = departmentName.substring(0, i);
                            if (departmentDO.getDepartmentName().equals(departmentName1)) {
                                parentDepartmentId = departmentDO.getId();
                                break haha;
                            }
                        }
                    }
                    //获取部门
                    for (DepartmentDO departmentDO : departmentDOList) {
                        int q = departmentName.indexOf("-");
                        if (q > 0) {
                            String departmentName2 = departmentName.substring(i + 1, departmentName.length());
                            if (departmentDO.getDepartmentName().equals(departmentName2)) {

                                //分公司id获取部门信息
                                HashMap<String, Object> departmentQueryParamMap1 = new HashMap<>();
                                DepartmentQueryParam departmentQueryParam1 = new DepartmentQueryParam();

                                departmentQueryParam1.setFullDepartmentName(departmentName2);
                                departmentQueryParam1.setParentDepartmentId(parentDepartmentId);
                                departmentQueryParamMap1.put("departmentQueryParam", departmentQueryParam1);
                                departmentQueryParamMap1.put("start", 0);
                                departmentQueryParamMap1.put("pageSize", Integer.MAX_VALUE);
                                List<DepartmentDO> departmentDOList1 = departmentMapper.listPage(departmentQueryParamMap1);
                                if (CollectionUtil.isEmpty(departmentDOList1)) {
                                    serviceResult.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
                                    return serviceResult;
                                }
                                map1.put(departmentDO.getDepartmentName(), departmentDO);
                                departmentId = departmentDO.getId();
                            }
                        } else {
                            if (departmentDO.getDepartmentName().equals(departmentName)) {
                                flag = true;
                                departmentId = departmentDO.getId();
                            }
                        }
                    }

                }
                //判断是否有这个角色(通过部门id查角色是否存在)
                if (map1.size() >= 1 || flag) {
                    Map<String, Object> roleQueryParamMap = new HashMap<>();
                    RoleQueryParam roleQueryParam = new RoleQueryParam();
                    roleQueryParam.setDepartmentId(departmentId);
                    roleQueryParam.setFullRoleName(queryParam.get("roleName"));
                    roleQueryParamMap.put("roleQueryParam", roleQueryParam);
                    roleQueryParamMap.put("start", 0);
                    roleQueryParamMap.put("pageSize", Integer.MAX_VALUE);
                    List<RoleDO> list = roleMapper.findList(roleQueryParamMap);
                    //如果没有就添加


                    //保存不存在的部門
                    if (CollectionUtil.isEmpty(list)) {
                        Role role = new Role();
                        role.setRoleName(queryParam.get("roleName"));
                        role.setDepartmentId(departmentId);
                        role.setIsSuperAdmin(CommonConstant.COMMON_CONSTANT_NO);
                        role.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        ServiceResult<String, Integer> serviceResult1 = userRoleService.addRole(role);
                        user.setRoleId(serviceResult1.getResult());
                    } else {
                        user.setRoleId(list.get(0).getId());
                    }

                }
            }


            if (xssfRow == null) {
                serviceResult.setErrorCode("没有数据了");
                return serviceResult;
            }
//            if(user.getRealName().equals("谢朋叶")){
//                System.out.println();
//            }
            String six = six();
            user.setPassword(generateMD5Password(user.getUserName(), six, ApplicationConfig.authKey));
            errorData.put(user.getUserName(), six);
            ServiceResult<String, Integer> serviceResult1 = userService.addUser(user);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(errorData);
        return serviceResult;

    }


    private String generateMD5Password(String username, String password, String md5Key) {
        String value = MD5Util.encryptWithKey(username + password, md5Key);
        return value;
    }

    public static String six() {
        String a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] rands = new char[2];
        for (int i = 0; i < rands.length; i++) {
            int rand = (int) (Math.random() * a.length());
            rands[i] = a.charAt(rand);
        }
        String b = "0123456789";
        char[] rands1 = new char[2];
        for (int i = 0; i < rands1.length; i++) {
            int rand = (int) (Math.random() * b.length());
            rands1[i] = b.charAt(rand);
        }
        String c = "abcdefghijklmnopqrstuvwxyz";
        char[] rands2 = new char[2];
        for (int i = 0; i < rands1.length; i++) {
            int rand = (int) (Math.random() * c.length());
            rands2[i] = c.charAt(rand);
        }
        return String.valueOf(rands) + String.valueOf(rands1) + String.valueOf(rands2);
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
        maps.put("姓名", "realName");
        maps.put("用户名", "userName");
        maps.put("手机号码", "phone");
        maps.put("初始密码", "password");
        maps.put("角色", "roleName");
        maps.put("企业邮箱", "email");
        maps.put("禁用", "isDisabled");
        maps.put("部门（与系统中名字相同）", "departmentName");
        maps.put("分公司", "subCompanyName");

        return maps;
    }
}
