package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.jointProduct.JointMaterial;
import com.lxzl.erp.common.domain.jointProduct.JointProduct;
import com.lxzl.erp.common.domain.jointProduct.JointProductQueryParam;
import com.lxzl.erp.common.domain.jointProduct.JointProductSku;
import com.lxzl.erp.common.util.ConverterUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class JointProductControllerTest extends ERPUnTransactionalTest {
    @Test
    public void pageJointProduct() throws Exception {
        JointProductQueryParam jointProductQueryParam = new JointProductQueryParam();
//        jointProductQueryParam.setJointProductName("26");
        jointProductQueryParam.setPageNo(1);
        jointProductQueryParam.setPageSize(2);
//        Calendar instance = Calendar.getInstance();
//        instance.set(2015, 10, 12,11,32,52);
//        Date time = instance.getTime();
//        jointProductQueryParam.setStartDate(time);
//        jointProductQueryParam.setEndDate(new Date());
        TestResult jsonTestResult = getJsonTestResult("/jointProduct/page", jointProductQueryParam);
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
        TestResult testResult = getJsonTestResult("/jointProduct/delete",jointProduct);
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
        JointProduct jointProduct = new JointProduct();
        jointProduct.setJointProductId(38);
        jointProduct.setJointProductName("测试跟新！！");
        ArrayList<JointProductSku> jointProductSkuList = new ArrayList<>();
        JointProductSku jointProductSku = new JointProductSku();
        jointProductSku.setJointProductId(38);
        jointProductSku.setJointProductSkuId(38);
        jointProductSku.setSkuId(10);
        jointProductSku.setSkuCount(10);
        jointProductSkuList.add(jointProductSku);
        jointProduct.setJointProductSkuList(jointProductSkuList);

        ArrayList<JointMaterial> jointMaterial = new ArrayList<>();

        JointMaterial jointMaterial1 = new JointMaterial();
        jointMaterial1.setJointProductId(38);
        jointMaterial1.setJointMaterialId(36);
        jointMaterial1.setMaterialId(4);
        jointMaterial1.setMaterialNo("M201711171838059981293");
        jointMaterial1.setMaterialCount(1);
        JointMaterial jointMaterial2 = new JointMaterial();
        jointMaterial2.setJointProductId(38);
        jointMaterial2.setJointMaterialId(37);
        jointMaterial2.setMaterialId(11);
        jointMaterial2.setMaterialNo("M201711171838059981293");
        jointMaterial2.setMaterialCount(27);
        jointMaterial.add(jointMaterial1);
        jointMaterial.add(jointMaterial2);
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