package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointMaterial;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProduct;
import com.lxzl.erp.common.domain.jointProduct.JointProductQueryParam;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProductSku;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JointProductControllerTest extends ERPUnTransactionalTest {
    @Test
    public void pageJointProduct() throws Exception {
//        JointProductQueryParam jointProductQueryParam = new JointProductQueryParam();
//        jointProductQueryParam.setJointProductName("组合007");
////        jointProductQueryParam.setJointProductId(48);
//        jointProductQueryParam.setPageNo(1);
//        jointProductQueryParam.setPageSize(15);
//        Calendar instance = Calendar.getInstance();
////        instance.set(2015, 10, 12,11,32,52);
//        Date time = instance.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date d1 = sdf.parse("2018-03-10");
//        Date d2 = sdf.parse("2018-03-14");
//
////        jointProductQueryParam.setStartDate(d1);
////        jointProductQueryParam.setEndDate(d2);
//        TestResult jsonTestResult = getJsonTestResult("/jointProduct/page", jointProductQueryParam);

        String str = "{\"pageNo\":1,\"pageSize\":15,\"jointProductName\":\"\",\"jointProductId\":\"\",\"startDate\":\"1520352000000\",\"endDate\":\"1521129599999\",\"timePicker\":\"2018-03-07 - 2018-03-15\"}";
        JointProductQueryParam jointProductQueryParam = JSONUtil.convertJSONToBean(str, JointProductQueryParam.class);
        TestResult result = getJsonTestResult("/jointProduct/page", jointProductQueryParam);

    }

    @Test
    public void query() throws Exception {
        JointProduct jointProduct = new JointProduct();
        jointProduct.setJointProductId(38);
        TestResult jsonTestResult = getJsonTestResult("/jointProduct/query", jointProduct);
    }

    @Test
    public void deleteJointProduct() throws Exception {
        JointProduct jointProduct = new JointProduct();
        jointProduct.setJointProductId(38);
        TestResult testResult = getJsonTestResult("/jointProduct/delete", jointProduct);
    }

    @Test
    public void addJointProduct() throws Exception {

        JointProduct jointProduct = new JointProduct();
        jointProduct.setJointProductName("测试！！");
        ArrayList<JointProductSku> jointProductSkuList = new ArrayList<>();
        JointProductSku jointProductSku1 = new JointProductSku();
        jointProductSku1.setSkuId(1);
        jointProductSku1.setSkuCount(3);
        jointProductSkuList.add(jointProductSku1);
        jointProduct.setJointProductSkuList(jointProductSkuList);

        ArrayList<JointMaterial> jointMaterialList = new ArrayList<>();
        JointMaterial jointMaterial1 = new JointMaterial();
        jointMaterial1.setMaterialNo("M201711171838059981292");
        jointMaterial1.setMaterialCount(3);
        JointMaterial jointMaterial2 = new JointMaterial();
        jointMaterial2.setMaterialNo("M201711171838059981293");
        jointMaterial2.setMaterialCount(3);
        jointMaterialList.add(jointMaterial1);
        jointMaterialList.add(jointMaterial2);
        jointProduct.setJointMaterialList(jointMaterialList);

        TestResult testResult = getJsonTestResult("/jointProduct/add", jointProduct);
    }

    @Test
    public void updateJointProduct() throws Exception {
//        String str ="{\"jointProductName\":\"测试跟新！！\",\n" +
//                "\"jointProductId\":\"46\",\n" +
//                "\"jointProductSkuList\":[{\"productId\":2000080,\n" +
//                "\"skuId\":223,\n" +
//                "\"skuCount\":\"1\",\n" +
//                "\"isNewProduct\":null}],\n" +
//                "\"jointMaterialList\":[{\"materialNo\":\"M201712250956366751399\",\n" +
//                "\"materialId\":40,\n" +
//                "\"materialCount\":\"5\",\n" +
//                "\"isNewMaterial\":null},\n" +
//                "{\"materialNo\":\"M201711201356145971009\",\n" +
//                "\"materialId\":5,\n" +
//                "\"materialCount\":\"3\",\n" +
//                "\"isNewMaterial\":null}]}";
//        Object object = JSONUtil.convertJSONToBean(str, Object.class);
//        JointProduct jointProduct = (JointProduct) object;
//        TestResult result = getJsonTestResult("/jointProduct/update",jointProduct );

        JointProduct jointProduct = new JointProduct();
        jointProduct.setJointProductId(46);
        jointProduct.setJointProductName("十点整测试！");
        List<JointProductSku> jointProductSkuList = new ArrayList<>();
        JointProductSku jointProductSku = new JointProductSku();
        jointProductSku.setJointProductId(46);
        jointProductSku.setJointProductSkuId(80);
        jointProductSku.setSkuId(222);
        jointProductSku.setSkuCount(1);
        jointProductSkuList.add(jointProductSku);
        JointProductSku jointProductSku1 = new JointProductSku();
//        jointProductSku.setJointProductId(46);
//        jointProductSku.setJointProductSkuId(61);
        jointProductSku1.setSkuId(333);
        jointProductSku1.setSkuCount(1);
        jointProductSkuList.add(jointProductSku1);
        jointProduct.setJointProductSkuList(jointProductSkuList);
        ArrayList<JointMaterial> jointMaterial = new ArrayList<>();
//
        JointMaterial jointMaterial1 = new JointMaterial();
        jointMaterial1.setJointMaterialId(63);
        jointMaterial1.setJointProductId(46);
//        jointMaterial1.setJointMaterialId(40);
        jointMaterial1.setMaterialCount(10);
        jointMaterial1.setMaterialId(40);
        jointMaterial1.setMaterialNo("M201712250956366751399");
        jointMaterial1.setMaterialCount(5);
        JointMaterial jointMaterial2 = new JointMaterial();
//        jointMaterial2.setJointProductId(46);
//        jointMaterial2.setJointMaterialId(52);
        jointMaterial2.setMaterialCount(3);
        jointMaterial2.setMaterialId(5);
        jointMaterial2.setMaterialNo("M201711201356145971009");
        JointMaterial jointMaterial3 = new JointMaterial();
        jointMaterial3.setMaterialNo("M201712250956366751399");
        jointMaterial3.setMaterialId(5);
        jointMaterial3.setMaterialCount(5);
        jointMaterial.add(jointMaterial1);
        jointMaterial.add(jointMaterial2);
        jointMaterial.add(jointMaterial3);
        jointProduct.setJointMaterialList(jointMaterial);

        TestResult testResult = getJsonTestResult("/jointProduct/update", jointProduct);
    }
//    @Test
//    public void say2(){
//        ArrayList<Integer> integers = new ArrayList<>();
//        integers.add(1);
//        integers.add(2);
//        integers.add(3);
//        ArrayList<Integer> integer3 = new ArrayList<>();
//        final List<Integer> integers1 = ConverterUtil.convertList(integers, Integer.class);
//        System.out.println(integers1);
//    }

}