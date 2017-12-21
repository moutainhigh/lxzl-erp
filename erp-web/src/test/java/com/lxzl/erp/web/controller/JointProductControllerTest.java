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
//        jointProductQueryParam.setJointProductName("测试");
        Calendar instance = Calendar.getInstance();
        instance.set(2015, 10, 12,11,32,52);
        Date time = instance.getTime();
        jointProductQueryParam.setStartDate(time);
        jointProductQueryParam.setEndDate(new Date());
        TestResult jsonTestResult = getJsonTestResult("/jointProduct/page", jointProductQueryParam);
    }

    @Test
    public void queryJointProductByJointProductId() throws Exception {
        JointProduct jointProduct = new JointProduct();
        jointProduct.setJointProductId(21);
        TestResult jsonTestResult = getJsonTestResult("/jointProduct/query", jointProduct);
        System.out.println("_____________");
    }

    @Test
    public void deleteJointProduct() throws Exception {
        JointProduct jointProduct = new JointProduct();
        jointProduct.setJointProductId(26);
        TestResult testResult = getJsonTestResult("/jointProduct/delete",jointProduct);
    }

    @Test
    public void addJointProduct() throws Exception {

        JointProduct jointProduct = new JointProduct();
//        jointProduct.setJointProductName("测试！！");
//        ArrayList<JointProductSku> jointProductSkuList = new ArrayList<>();
//        JointProductSku jointProductSku1 = new JointProductSku();
//        jointProductSku1.setJointProductSkuId(2);
//        jointProductSku1.setSkuId(3);
//        jointProductSku1.setSkuCount(3);
//        jointProductSkuList.add(jointProductSku1);
//        jointProduct.setJointProductSkuList(jointProductSkuList);
//
//        ArrayList<JointMaterial> jointMaterialList = new ArrayList<>();
//        JointMaterial jointMaterial1 = new JointMaterial();
//        jointMaterial1.setJointMaterialId(1);
//        jointMaterial1.setMaterialId(3);
//        jointMaterial1.setMaterialCount(3);
//        JointMaterial jointMaterial2 = new JointMaterial();
//        jointMaterial2.setJointMaterialId(2);
//        jointMaterial2.setMaterialId(3);
//        jointMaterial2.setMaterialCount(3);
//        jointMaterialList.add(jointMaterial1);
//        jointMaterialList.add(jointMaterial2);
//        jointProduct.setJointMaterialList(jointMaterialList);

        TestResult testResult = getJsonTestResult("/jointProduct/add", jointProduct);
    }

    @Test
    public void updateJointProduct() throws Exception {
        JointProduct jointProduct = new JointProduct();
        jointProduct.setJointProductId(26);
        jointProduct.setJointProductName("测试跟新！！");
//        ArrayList<JointProductSku> jointProductSkuList = new ArrayList<>();
//        JointProductSku jointProductSku = new JointProductSku();
//        jointProductSku.setJointProductSkuId(28);
//        jointProductSku.setSkuId(10);
//        jointProductSku.setSkuCount(10);
//        JointProductSku jointProductSku1 = new JointProductSku();
//        jointProductSku1.setJointProductSkuId(27);
//        jointProductSku1.setSkuId(11);
//        jointProductSku1.setSkuCount(11);
//        jointProductSkuList.add(jointProductSku);
//        jointProductSkuList.add(jointProductSku1);
//        jointProduct.setJointProductSkuList(jointProductSkuList);

        ArrayList<JointMaterial> jointMaterial = new ArrayList<>();

        JointMaterial jointMaterial1 = new JointMaterial();
        jointMaterial1.setJointMaterialId(29);
        jointMaterial1.setMaterialId(4);
        jointMaterial1.setMaterialCount(1);
        JointMaterial jointMaterial2 = new JointMaterial();
        jointMaterial1.setJointMaterialId(30);
        jointMaterial2.setMaterialId(11);
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