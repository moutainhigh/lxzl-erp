package com.lxzl.erp.core.service.workflow;

import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.core.service.StatementOrderCorrect.StatementOrderCorrectService;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.erp.core.service.changeOrder.ChangeOrderService;
import com.lxzl.erp.core.service.deploymentOrder.DeploymentOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.peerDeploymentOrder.PeerDeploymentOrderService;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.erp.core.service.purchaseApply.PurchaseApplyOrderService;
import com.lxzl.erp.core.service.repairOrder.RepairOrderService;
import com.lxzl.erp.core.service.returnOrder.ReturnOrderService;
import com.lxzl.erp.core.service.transferOrder.TransferOrderService;
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
    @Autowired
    private RepairOrderService repairOrderService;
    @Autowired
    private ReturnOrderService returnOrderService;
    @Autowired
    private PurchaseApplyOrderService purchaseApplyOrderService;
    @Autowired
    private TransferOrderService transferOrderService;
    @Autowired
    private PeerDeploymentOrderService peerDeploymentOrderService;
    @Autowired
    private StatementOrderCorrectService statementOrderCorrectService;

    public VerifyReceiver getService(Integer workflowType) {
        if (WorkflowType.WORKFLOW_TYPE_PURCHASE.equals(workflowType)) {
            return purchaseOrderService;
        } else if (WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowType)) {
            return orderService;
        } else if (WorkflowType.WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO.equals(workflowType)) {
            return deploymentOrderService;
        } else if (WorkflowType.WORKFLOW_TYPE_CHANGE.equals(workflowType)) {
            return changeOrderService;
        } else if (WorkflowType.WORKFLOW_TYPE_REPAIR.equals(workflowType)) {
            return repairOrderService;
        } else if (WorkflowType.WORKFLOW_TYPE_RETURN.equals(workflowType)) {
            return returnOrderService;
        } else if (WorkflowType.WORKFLOW_TYPE_PURCHASE_APPLY_ORDER.equals(workflowType)) {
            return purchaseApplyOrderService;
        } else if(WorkflowType.WORKFLOW_TYPE_TRANSFER_IN_ORDER.equals(workflowType)
                ||WorkflowType.WORKFLOW_TYPE_TRANSFER_OUT_ORDER.equals(workflowType)){
            return transferOrderService;
        }else if(WorkflowType.WORKFLOW_TYPE_PEER_DEPLOYMENT_INTO.equals(workflowType)
                ||WorkflowType.WORKFLOW_TYPE_PEER_DEPLOYMENT_OUT.equals(workflowType)){
            return peerDeploymentOrderService;
        }else if(WorkflowType.WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT.equals(workflowType)){
            return statementOrderCorrectService;
        }
        return null;
    }
}
