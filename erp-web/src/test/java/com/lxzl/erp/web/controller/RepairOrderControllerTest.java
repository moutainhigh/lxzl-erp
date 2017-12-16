package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrder;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderBulkMaterial;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderEquipment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:03 2017/12/14
 * @Modified By:
 */
public class RepairOrderControllerTest extends ERPUnTransactionalTest {

    @Test
    public void addRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairReason("测试");
        repairOrder.setRemark("测试备注");//可有可无

        List<RepairOrderEquipment> repairOrderEquipmentList = new ArrayList<>();
        RepairOrderEquipment repairOrderEquipment = new RepairOrderEquipment();
        repairOrderEquipment.setEquipmentNo("LX-52RENTAL-VIEWPAKER-4000001-2017110710001");
//        repairOrderEquipment.setOrderId();
//        repairOrderEquipment.setOrderProductId();
        repairOrderEquipmentList.add(repairOrderEquipment);

//        RepairOrderEquipment repairOrderEquipment2 = new RepairOrderEquipment();
        repairOrderEquipment.setEquipmentNo("LX-52RENTAL-VIEWPAKER-4000001-2017110710002");
        repairOrderEquipmentList.add(repairOrderEquipment);
        repairOrder.setRepairOrderEquipmentList(repairOrderEquipmentList);

        List<RepairOrderBulkMaterial> repairOrderBulkMaterialList = new ArrayList<>();

        RepairOrderBulkMaterial repairOrderBulkMaterial = new RepairOrderBulkMaterial();
        repairOrderBulkMaterial.setBulkMaterialNo("BM2017112017070030810011");
        repairOrderBulkMaterialList.add(repairOrderBulkMaterial);

        repairOrderBulkMaterial.setBulkMaterialNo("BM2017112017070030810013");
        repairOrderBulkMaterialList.add(repairOrderBulkMaterial);
        repairOrder.setRepairOrderBulkMaterialList(repairOrderBulkMaterialList);


        TestResult result = getJsonTestResult("/repairOrder/addRepairOrder",repairOrder);
    }

    @Test
    public void commitRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RO201712151358410721138");

        TestResult result = getJsonTestResult("/repairOrder/commitRepairOrder",repairOrder);
    }

    @Test
    public void receiveRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RO201712151358410721138");

        TestResult result = getJsonTestResult("/repairOrder/receiveRepairOrder",repairOrder);
    }

    @Test
    public void cancelRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RO201712151358410721138");

        TestResult result = getJsonTestResult("/repairOrder/cancelRepairOrder",repairOrder);
    }




    @Test
    public void updateRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RO201712151749023261331");
        repairOrder.setRepairReason("测试update");
        repairOrder.setRemark("测试备注");//可有可无

        List<RepairOrderEquipment> repairOrderEquipmentList = new ArrayList<>();
        RepairOrderEquipment repairOrderEquipment = new RepairOrderEquipment();
        repairOrderEquipment.setRepairOrderEquipmentId(12);
        repairOrderEquipment.setRepairOrderNo("RO201712151749023261331");
        repairOrderEquipment.setEquipmentId(383);
        repairOrderEquipment.setEquipmentNo("LX-52RENTAL-VIEWPAKER-4000001-2017110710001");
        repairOrderEquipment.setRemark("update测试备注");
        repairOrderEquipmentList.add(repairOrderEquipment);
        repairOrder.setRepairOrderEquipmentList(repairOrderEquipmentList);

        List<RepairOrderBulkMaterial> repairOrderBulkMaterialList = new ArrayList<>();
        RepairOrderBulkMaterial repairOrderBulkMaterial = new RepairOrderBulkMaterial();
        repairOrderBulkMaterial.setRepairOrderBulkMaterialId(10);
        repairOrderBulkMaterial.setRepairOrderNo("RO201712151749023261331");
        repairOrderBulkMaterial.setBulkMaterialId(158);
        repairOrderBulkMaterial.setBulkMaterialNo("BM2017112017070030810011");
        repairOrderBulkMaterialList.add(repairOrderBulkMaterial);
        repairOrder.setRepairOrderBulkMaterialList(repairOrderBulkMaterialList);

        TestResult result = getJsonTestResult("/repairOrder/updateRepairOrder",repairOrder);
    }

}
