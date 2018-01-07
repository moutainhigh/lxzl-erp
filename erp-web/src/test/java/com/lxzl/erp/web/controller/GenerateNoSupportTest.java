package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
public class GenerateNoSupportTest extends ERPUnTransactionalTest {
    @Test
    public void generateSupplierNo() throws Exception {
        long startTime = System.currentTimeMillis();
        generateNoSupport.generateSupplierNo(1);
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：0.023s
    }

    /**
     * ProductEquipmentNo 批量
     */
    @Test
    public void generateProductEquipmentNo() throws Exception {
        long startTime = System.currentTimeMillis();
        List<String> strings = generateNoSupport.generateProductEquipmentNoList(null,"", 1);
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：0.08s

    }

    /**
     * BulkMaterialNo 批量
     */
    @Test
    public void generateBulkMaterialNo1() throws Exception {
        long startTime = System.currentTimeMillis();
        List<String> strings = generateNoSupport.generateBulkMaterialNoList(null,"1000",1);
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //一次 执行时间：0.087s

    }

    @Test
    public void generateBulkMaterialNo() throws Exception {
//        generateNoSupport.generateBulkMaterialNo(new Date(),4000001);
        long startTime = System.currentTimeMillis();
//        for (int i = 0;i<3000;i++) {
        generateNoSupport.generateBulkMaterialNo(new Date(), 4000001);
//        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //一次 执行时间：0.061s

    }

    @Test
    public void generatePurchaseOrderNo() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            generateNoSupport.generatePurchaseOrderNo(new Date(), 4000001);
        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：40.55s
    }

    @Test
    public void generatePurchaseDeliveryOrderNo() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            generateNoSupport.generatePurchaseDeliveryOrderNo(new Date(), 4000001);
        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：39.685s
    }

    @Test
    public void generatePurchaseReceiveOrderNo() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            generateNoSupport.generatePurchaseReceiveOrderNo(new Date(), 4000001);
        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：40.719s
    }

    @Test
    public void generateEquipmentNo() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            generateNoSupport.generateProductEquipmentNo(null, "null");
        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：44.096s
    }

    @Test
    public void generateWarehouseNo() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            generateNoSupport.generateWarehouseNo(2, 1);
        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：14.448s
    }

    @Test
    public void generateWorkflowLinkNo() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            generateNoSupport.generateWorkflowLinkNo(new Date(), 500006);
        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：11.621s
    }

    @Test
    public void generateReturnOrderNo() throws Exception {

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 3000; i++) {
            generateNoSupport.generateReturnOrderNo(new Date(), 700003);
        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：12.833s
    }

    @Test
    public void generateChangeOrderNo() throws Exception {
//        generateNoSupport.generateChangeOrderNo(new Date(), 700003);
        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 3000; i++) {
        generateNoSupport.generateChangeOrderNo(new Date(), 700003);
//        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：12.414s
    }

    @Test
    public void generateStatementOrderNo() throws Exception {
//        generateNoSupport.generateStatementOrderNo(new Date(), 700003);
        long startTime = System.currentTimeMillis();
//        for (int i = 0;i<3000;i++) {
        generateNoSupport.generateStatementOrderNo(new Date(), 700003);
//        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：10.872s
    }

    @Test
    public void generateRepairOrderNo() throws Exception {
//        generateNoSupport.generateRepairOrderNo(new Date(), "W201708081508");
        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 3000; i++) {
        generateNoSupport.generateRepairOrderNo(new Date(), "W201708081508");
//        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：39.425s
    }

    @Test
    public void getMonthlongDate() throws Exception {
    }

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Test
    public void generateOrderNo() throws Exception {
//        generateNoSupport.generateOrderNo(new Date(), 700003);
        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 3000; i++) {
        generateNoSupport.generateOrderNo(new Date(), 700003);
//        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：30.127s
    }

    @Test
    public void generateCustomerNo() throws Exception {
//        generateNoSupport.generateCustomerNo(new Date(), 500001, 1);
        long startTime = System.currentTimeMillis();
//        for (int i = 0;i<3000;i++) {
        generateNoSupport.generateCustomerNo(new Date(),  1);
//        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：60.069s
        //一次 执行时间：0.066s
    }

    @Test
    public void generateDeploymentOrderNo() throws Exception {
//        generateNoSupport.generateDeploymentOrderNo(new Date(), 4000002, 4000001);
        long startTime = System.currentTimeMillis();
//        for (int i = 0;i<3000;i++) {
        generateNoSupport.generateDeploymentOrderNo(new Date(), 4000002, 4000001);
//        }
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
        //执行时间：89.916s
        //一次 执行时间：0.07s
    }


}