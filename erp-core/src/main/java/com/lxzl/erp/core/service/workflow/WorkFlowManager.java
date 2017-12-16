package com.lxzl.erp.core.service.workflow;

import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.erp.core.service.changeOrder.ChangeOrderService;
import com.lxzl.erp.core.service.deploymentOrder.DeploymentOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:06 2017/12/15
 * @Modified By:
 */

@Component
public class WorkFlowManager {

    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DeploymentOrderService deploymentOrderService;
    @Autowired
    private ChangeOrderService changeOrderService;


    public VerifyReceiver getService(Integer workflowType){
        if (WorkflowType.WORKFLOW_TYPE_PURCHASE.equals(workflowType)){
            return purchaseOrderService;
        }else if(WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowType)){
            return orderService;
        }else if(WorkflowType.WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO.equals(workflowType)){
            return deploymentOrderService;
        }else if(WorkflowType.WORKFLOW_TYPE_CHANGE.equals(workflowType)){
            return changeOrderService;
        }
        return null;
    }
}
