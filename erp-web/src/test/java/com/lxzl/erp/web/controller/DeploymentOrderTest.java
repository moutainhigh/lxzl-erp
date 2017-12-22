package com.lxzl.erp.web.controller;

import com.alibaba.dubbo.common.json.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.DeploymentType;
import com.lxzl.erp.common.domain.deploymentOrder.DeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.deploymentOrder.ProcessDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderProduct;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 调拨单单测试类
 *
 * @author gaochao
 * @date 2017-11-15 14:14
 */
public class DeploymentOrderTest extends ERPUnTransactionalTest {
    @Test
    public void testCreateDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentType(DeploymentType.DEPLOYMENT_TYPE_BORROW);
        deploymentOrder.setSrcWarehouseId(4000002);
        deploymentOrder.setTargetWarehouseId(4000001);

        List<DeploymentOrderProduct> deploymentOrderProductList = new ArrayList<>();
        DeploymentOrderProduct deploymentOrderProduct = new DeploymentOrderProduct();
        deploymentOrderProduct.setDeploymentProductSkuId(40);
        deploymentOrderProduct.setDeploymentProductSkuCount(5);
        deploymentOrderProduct.setDeploymentProductUnitAmount(new BigDecimal(20));
        deploymentOrderProductList.add(deploymentOrderProduct);
        deploymentOrder.setDeploymentOrderProductList(deploymentOrderProductList);

        TestResult testResult = getJsonTestResult("/deploymentOrder/create", deploymentOrder);
    }

    @Test
    public void testCreateOrderJson() throws Exception {
        String str = "{\n" +
                "  \"deploymentType\": \"1\",\n" +
                "  \"srcWarehouseId\": \"4000002\",\n" +
                "  \"targetWarehouseId\": \"4000003\",\n" +
                "  \"expectReturnTime\": 1513814400000,\n" +
                "  \"deploymentOrderProductList\": [{\n" +
                "    \"deploymentProductSkuId\": 81,\n" +
                "    \"deploymentProductSkuCount\": \"1\",\n" +
                "    \"deploymentProductUnitAmount\": \"100\"\n" +
                "  }],\n" +
                "  \"deploymentOrderMaterialList\": [{\n" +
                "    \"deploymentMaterialId\": \"24\",\n" +
                "    \"deploymentProductMaterialCount\": \"1\",\n" +
                "    \"deploymentMaterialUnitAmount\": \"100\"\n" +
                "  }]\n" +
                "}";
        DeploymentOrder deploymentOrder = JSONUtil.convertJSONToBean(str, DeploymentOrder.class);
        TestResult testResult = getJsonTestResult("/deploymentOrder/create", deploymentOrder);
    }

    @Test
    public void testCommitDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("DO201712011137331391559");
        deploymentOrder.setVerifyUser(500006);
        TestResult testResult = getJsonTestResult("/deploymentOrder/commit", deploymentOrder);
    }

    @Test
    public void testCancelDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("O201711151901080841608");
        deploymentOrder.setVerifyUser(1);
        TestResult testResult = getJsonTestResult("/deploymentOrder/cancel", deploymentOrder);
    }

    @Test
    public void testProcessDeploymentOrder() throws Exception {
        ProcessDeploymentOrderParam processDeploymentOrderParam = new ProcessDeploymentOrderParam();
        processDeploymentOrderParam.setDeploymentOrderNo("O201711301513586691582");
//        processDeploymentOrderParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017111110114");
        processDeploymentOrderParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult testResult = getJsonTestResult("/deploymentOrder/process", processDeploymentOrderParam);
    }


    @Test
    public void testDeliveryDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("O201711301513586691582");
        TestResult testResult = getJsonTestResult("/deploymentOrder/delivery", deploymentOrder);
    }


    @Test
    public void testQueryPageDeploymentOrder() throws Exception {
        DeploymentOrderQueryParam param = new DeploymentOrderQueryParam();
        param.setPageNo(1);
        param.setPageSize(15);
//        param.setDeploymentOrderNo("DO201712011137331391559");
        TestResult testResult = getJsonTestResult("/deploymentOrder/queryPage", param);
    }

    @Test
    public void testQueryDetailDeploymentOrder() throws Exception {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        deploymentOrder.setDeploymentOrderNo("DO201712011137331391559");
        TestResult testResult = getJsonTestResult("/deploymentOrder/queryDetail", deploymentOrder);
    }
}
