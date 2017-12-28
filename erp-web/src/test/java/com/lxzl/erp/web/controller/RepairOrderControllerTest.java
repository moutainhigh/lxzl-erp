package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.repairOrder.FixRepairOrderQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderBulkMaterialQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderEquipmentQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderQueryParam;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrder;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderBulkMaterial;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderEquipment;
import com.lxzl.erp.core.service.repairOrder.RepairOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:03 2017/12/14
 * @Modified By:
 */
public class RepairOrderControllerTest extends ERPUnTransactionalTest {

    @Autowired
    private RepairOrderService repairOrderService;
   @Test
    public void addRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairReason("测试");
        repairOrder.setRemark("测试备注");//可有可无

        List<RepairOrderEquipment> repairOrderEquipmentList = new ArrayList<>();
        RepairOrderEquipment repairOrderEquipment1= new RepairOrderEquipment();
        repairOrderEquipment1.setEquipmentNo("LX-EQUIPMENT-4000002-2017121610001");
        repairOrderEquipmentList.add(repairOrderEquipment1);

        RepairOrderEquipment repairOrderEquipment2 = new RepairOrderEquipment();
        repairOrderEquipment2.setEquipmentNo("LX-EQUIPMENT-4000002-2017121610003");
        repairOrderEquipmentList.add(repairOrderEquipment2);
        repairOrder.setRepairOrderEquipmentList(repairOrderEquipmentList);

        List<RepairOrderBulkMaterial> repairOrderBulkMaterialList = new ArrayList<>();

        RepairOrderBulkMaterial repairOrderBulkMaterial1 = new RepairOrderBulkMaterial();
        repairOrderBulkMaterial1.setBulkMaterialNo("BM2017122011073198122546");
        repairOrderBulkMaterialList.add(repairOrderBulkMaterial1);

        RepairOrderBulkMaterial repairOrderBulkMaterial2 = new RepairOrderBulkMaterial();
        repairOrderBulkMaterial2.setBulkMaterialNo("BM2017121618064454121532");
        repairOrderBulkMaterialList.add(repairOrderBulkMaterial2);
        repairOrder.setRepairOrderBulkMaterialList(repairOrderBulkMaterialList);


        TestResult result = getJsonTestResult("/repairOrder/addRepairOrder",repairOrder);
    }

    @Test
    public void commitRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RE201712191152480581851");
        repairOrder.setVerifyUser(500006);
        repairOrder.setCommitRemark("提交审核的备注");

        TestResult result = getJsonTestResult("/repairOrder/commitRepairOrder",repairOrder);
    }


    @Test
    public void receiveRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RE201712191152480581851");

        TestResult result = getJsonTestResult("/repairOrder/receiveRepairOrder",repairOrder);
    }

    @Test
    public void cancelRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RE201712191152480581851");

        TestResult result = getJsonTestResult("/repairOrder/cancelRepairOrder",repairOrder);
    }

    @Test
    public void updateRepairOrder() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RE201712231750266641293");
        repairOrder.setRepairReason("测试update");
        repairOrder.setRemark("测试update备注");//可有可无

        List<RepairOrderEquipment> repairOrderEquipmentList = new ArrayList<>();
        RepairOrderEquipment repairOrderEquipment1 = new RepairOrderEquipment();
        repairOrderEquipment1.setEquipmentNo("LX-EQUIPMENT-4000002-2017121610001");
        repairOrderEquipment1.setRepairEndTime(new Date());
        //        repairOrderEquipment1.setRemark("update测试备注1");
        repairOrderEquipmentList.add(repairOrderEquipment1);

        RepairOrderEquipment repairOrderEquipment2 = new RepairOrderEquipment();
        repairOrderEquipment2.setEquipmentNo("LX-EQUIPMENT-4000002-2017121610007");
        repairOrderEquipment2.setRemark("update测试备注2");
        repairOrderEquipmentList.add(repairOrderEquipment2);

      RepairOrderEquipment repairOrderEquipment3 = new RepairOrderEquipment();
//        repairOrderEquipment3.setRepairOrderEquipmentId(73);
//        repairOrderEquipment3.setRepairOrderNo("RE201712191152480581851");
//        repairOrderEquipment3.setEquipmentNo("LX-EQUIPMENT-4000002-2017121610008");
//        repairOrderEquipment3.setRemark("update测试备注3");
//        repairOrderEquipmentList.add(repairOrderEquipment3);

        repairOrder.setRepairOrderEquipmentList(repairOrderEquipmentList);

        List<RepairOrderBulkMaterial> repairOrderBulkMaterialList = new ArrayList<>();
        RepairOrderBulkMaterial repairOrderBulkMaterial1 = new RepairOrderBulkMaterial();
        repairOrderBulkMaterial1.setBulkMaterialNo("BM2017121618064454121367");
        repairOrderBulkMaterialList.add(repairOrderBulkMaterial1);

//        RepairOrderBulkMaterial repairOrderBulkMaterial2 = new RepairOrderBulkMaterial();
//        repairOrderBulkMaterial2.setBulkMaterialNo("BM2017121616120446910272");
//        repairOrderBulkMaterialList.add(repairOrderBulkMaterial2);

        repairOrder.setRepairOrderBulkMaterialList(repairOrderBulkMaterialList);

        TestResult result = getJsonTestResult("/repairOrder/updateRepairOrder",repairOrder);
    }

    @Test
    public void queryRepairOrderByNo() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RE201712231750266641293");

        TestResult result = getJsonTestResult("/repairOrder/queryRepairOrderByNo",repairOrder);
    }

    @Test
    public void pageRepairOrder() throws Exception {
        RepairOrderQueryParam  repairOrderQueryParam = new RepairOrderQueryParam();
        repairOrderQueryParam.setPageNo(1);
        repairOrderQueryParam.setPageSize(10);

        TestResult result = getJsonTestResult("/repairOrder/pageRepairOrder",repairOrderQueryParam);
    }


    @Test
    public void pageRepairEquipment() throws Exception {
        RepairOrderEquipmentQueryParam  repairOrderEquipmentQueryParam = new RepairOrderEquipmentQueryParam();
        repairOrderEquipmentQueryParam.setPageNo(1);
        repairOrderEquipmentQueryParam.setPageSize(10);

        TestResult result = getJsonTestResult("/repairOrder/pageRepairEquipment",repairOrderEquipmentQueryParam);
    }


    @Test
    public void pageRepairBulkMaterial() throws Exception {
        RepairOrderBulkMaterialQueryParam repairOrderBulkMaterialQueryParam = new RepairOrderBulkMaterialQueryParam();
        repairOrderBulkMaterialQueryParam.setPageNo(1);
        repairOrderBulkMaterialQueryParam.setPageSize(10);

        TestResult result = getJsonTestResult("/repairOrder/pageRepairBulkMaterial",repairOrderBulkMaterialQueryParam);
    }


    @Test
    public void fix() throws Exception {
        FixRepairOrderQueryParam fixRepairOrderQueryParam = new FixRepairOrderQueryParam();
        List<RepairOrderEquipment>  repairOrderEquipmentList = new ArrayList<>();
        RepairOrderEquipment repairOrderEquipment1 = new RepairOrderEquipment();
        repairOrderEquipment1.setRepairOrderEquipmentId(158);
        repairOrderEquipment1.setRepairEndRemark("维修完成1");

        repairOrderEquipmentList.add(repairOrderEquipment1);

        RepairOrderEquipment repairOrderEquipment2 = new RepairOrderEquipment();
        repairOrderEquipment2.setRepairOrderEquipmentId(159);
        repairOrderEquipment2.setRepairEndRemark("维修完成2");

        repairOrderEquipmentList.add(repairOrderEquipment2);


        List<RepairOrderBulkMaterial> repairOrderBulkMaterialList = new ArrayList<>();
        RepairOrderBulkMaterial repairOrderBulkMaterial1 = new RepairOrderBulkMaterial();
        repairOrderBulkMaterial1.setRepairOrderBulkMaterialId(89);
        repairOrderBulkMaterial1.setRepairEndRemark("维修完成1");

        repairOrderBulkMaterialList.add(repairOrderBulkMaterial1);
        RepairOrderBulkMaterial repairOrderBulkMaterial2 = new RepairOrderBulkMaterial();
        repairOrderBulkMaterial2.setRepairOrderBulkMaterialId(90);
        repairOrderBulkMaterial2.setRepairEndRemark("维修完成2");

        repairOrderBulkMaterialList.add(repairOrderBulkMaterial2);

        fixRepairOrderQueryParam.setRepairOrderEquipmentList(repairOrderEquipmentList);
        fixRepairOrderQueryParam.setRepairOrderBulkMaterialList(repairOrderBulkMaterialList);
        TestResult result = getJsonTestResult("/repairOrder/fix",fixRepairOrderQueryParam);
    }

    @Test
    public void end() throws Exception {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setRepairOrderNo("RE201712251737417831752");
        TestResult result = getJsonTestResult("/repairOrder/end",repairOrder);
    }


}
