package com.lxzl.erp.core.service.workbench.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.workbench.WorkbenchQueryParam;
import com.lxzl.erp.common.domain.workbench.pojo.Workbench;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.k3.K3ReturnOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderSupport;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workbench.WorkbenchService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workbench.WorkbenchMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowVerifyUserGroupMapper;
import com.lxzl.erp.dataaccess.dao.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;



/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:08 2018/7/16
 * @Modified By:
 */
@Service("workbenchService")
public class WorkbenchServiceImpl implements WorkbenchService{

    public ServiceResult<String, Workbench> queryWorkbenchCount(WorkbenchQueryParam workbenchQueryParam) {
        ServiceResult<String, Workbench> serviceResult = new ServiceResult<>();
        Workbench workbench = new Workbench();

        List<Map<String,Object>> orderListMap = new ArrayList<>();
        List<Map<String,Object>> k3ReturnOrderListMap = new ArrayList<>();
        List<Map<String,Object>> customerListMap = new ArrayList<>();
        List<Map<String,Object>> workflowListMap = new ArrayList<>();
        List<Map<String,Object>> workflowBusinessAffairsListMap = new ArrayList<>();
        List<Map<String,Object>> bankSlipDetailListMap = new ArrayList<>();
        List<Map<String,Object>> statementOrderBusinessAffairsListMap = new ArrayList<>();

        Map<String,Object> maps = new HashMap<>();

        //业务工作台
        if (CommonConstant.COMMON_SALES_WORKBENCH.equals(workbenchQueryParam.getWorkbenchName())){

            Workbench workbenchRedis = redisManager.get("SALES_WORKBENCH_" + userSupport.getCurrentUserId().toString(), Workbench.class);
            if (workbenchRedis != null){
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(workbenchRedis);
                return serviceResult;
            }
            //工作流审核人组
            if (!userSupport.isSuperUser()) {
                maps.put("verifyUserId", userSupport.getCurrentUserId().toString());
                maps.put("currentUserGroupList",workflowVerifyUserGroupMapper.findGroupUUIDByUserId(userSupport.getCurrentUserId()));
            }

            //可续租单的提前时间值
            maps.put("reletTimeOfDay",CommonConstant.RELET_TIME_OF_RENT_TYPE_DAY ); //短租提前15天
            maps.put("reletTimeOfMonth",CommonConstant.RELET_TIME_OF_RENT_TYPE_MONTH); //长租提前30天
            maps.put("workbenchName",workbenchQueryParam.getWorkbenchName());
            maps.put("workbenchQueryParam",workbenchQueryParam);
            //订单权限
            maps.put("orderPermissionParam",permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));
            //退货单权限
            maps.put("k3ReturnOrderPermissionParam",permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));
            //企业客户权限
            maps.put("companyCustomerPermissionParam",permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
            //个人客户权限
            maps.put("personCustomerPermissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

            maps.put("workbenchName",workbenchQueryParam.getWorkbenchName());

            Map<String,Integer> orderCountMap = workbenchMapper.findOrderWorkbenchCount(maps);
            Map<String,Integer> k3ReturnOrderCountMap = workbenchMapper.findK3ReturnOrderWorkbenchCount(maps);
            Map<String,Integer> customerCountMap = workbenchMapper.findCustomerWorkbenchCount(maps);
            Map<String,Integer> workflowCountMap = workbenchMapper.findWorkflowWorkbenchCountForSales(maps);

            //审核中的订单
            Map<String,Object> verifyingOrderMap = new HashMap();
            verifyingOrderMap.put("params","orderStatus");
            verifyingOrderMap.put("paramsValue",OrderStatus.ORDER_STATUS_VERIFYING);
            verifyingOrderMap.put("workbenchType",WorkbenchType.ORDER_STATUS_VERIFYING);
            verifyingOrderMap.put("count",orderCountMap.get("orderVerifyingCount"));
            orderListMap.add(verifyingOrderMap);

            //待发货的订单
            Map<String,Object> waitDeliveryMap = new HashMap();
            waitDeliveryMap.put("params","orderStatus");
            waitDeliveryMap.put("paramsValue",OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
            waitDeliveryMap.put("workbenchType",WorkbenchType.ORDER_STATUS_WAIT_DELIVERY);
            waitDeliveryMap.put("count",orderCountMap.get("orderWaitDeliveryCount"));
            orderListMap.add(waitDeliveryMap);

            //到期未处理的订单
            Map<String,Object> overDueMap= new HashMap();
            overDueMap.put("params","isReturnOverDue");
            overDueMap.put("paramsValue",CommonConstant.COMMON_CONSTANT_YES);
            overDueMap.put("workbenchType",WorkbenchType.ORDER_STATUS_OVER_DUE);
            overDueMap.put("count",orderCountMap.get("orderOverDueCount"));
            orderListMap.add(overDueMap);

            //可续租的订单
            Map<String,Object> canReletOrderMap= new HashMap();
            canReletOrderMap.put("params","isCanReletOrder");
            canReletOrderMap.put("paramsValue",CommonConstant.COMMON_CONSTANT_YES);
            canReletOrderMap.put("workbenchType",WorkbenchType.ORDER_STATUS_CAN_RELET);
            canReletOrderMap.put("count",orderCountMap.get("canReletOrderCount"));
            orderListMap.add(canReletOrderMap);

            //未提交的退货单
            Map<String,Object> waitCommitMap = new HashMap();
            waitCommitMap.put("params","returnOrderStatus");
            waitCommitMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
            waitCommitMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_WAIT_COMMIT);
            waitCommitMap.put("count",k3ReturnOrderCountMap.get("returnOrderWaitCommitCount"));
            k3ReturnOrderListMap.add(waitCommitMap);

            //审核中的退货单
            Map<String,Object> verifyingReturnOrderMap = new HashMap();
            verifyingReturnOrderMap.put("params","returnOrderStatus");
            verifyingReturnOrderMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING);
            verifyingReturnOrderMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_VERIFYING);
            verifyingReturnOrderMap.put("count",k3ReturnOrderCountMap.get("returnOrderVerifyingCount"));
            k3ReturnOrderListMap.add(verifyingReturnOrderMap);

            //处理中的退货单
            Map<String,Object> processingMap = new HashMap();
            processingMap.put("params","returnOrderStatus");
            processingMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
            processingMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_PROCESSING);
            processingMap.put("count",k3ReturnOrderCountMap.get("returnOrderProcessingCount"));
            k3ReturnOrderListMap.add(processingMap);

            //被驳回的退货单
            Map<String,Object> backedMap = new HashMap();
            backedMap.put("params","returnOrderStatus");
            backedMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED);
            backedMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_REJECT);
            backedMap.put("count",k3ReturnOrderCountMap.get("returnOrderBackedCount"));
            k3ReturnOrderListMap.add(backedMap);

            //审核中的企业客户
            Map<String,Object> verifyingCompanyMap = new HashMap();
            verifyingCompanyMap.put("params","customerStatus");
            verifyingCompanyMap.put("paramsValue",CustomerStatus.STATUS_COMMIT);
            verifyingCompanyMap.put("workbenchType",WorkbenchType.COMPANY_CUSTOMER_STATUS_VERIFYING);
            verifyingCompanyMap.put("count",customerCountMap.get("companyCustomerVerifyingCount"));
            customerListMap.add(verifyingCompanyMap);

            //被驳回的企业客户
            Map<String,Object> rejectCompanyMap = new HashMap();
            rejectCompanyMap.put("params","customerStatus");
            rejectCompanyMap.put("paramsValue",CustomerStatus.STATUS_REJECT);
            rejectCompanyMap.put("workbenchType",WorkbenchType.COMPANY_CUSTOMER_STATUS_REJECT);
            rejectCompanyMap.put("count",customerCountMap.get("companyCustomerRejectCount"));
            customerListMap.add(rejectCompanyMap);

            //审核中的个人客户
            Map<String,Object> verifyingPersonMap = new HashMap();
            verifyingPersonMap.put("params","customerStatus");
            verifyingPersonMap.put("paramsValue",CustomerStatus.STATUS_COMMIT);
            verifyingPersonMap.put("workbenchType",WorkbenchType.PERSON_CUSTOMER_STATUS_VERIFYING);
            verifyingPersonMap.put("count",customerCountMap.get("personCustomerVerifyingCount"));
            customerListMap.add(verifyingPersonMap);

            //被驳回的个人客户
            Map<String,Object> rejectPersonMap = new HashMap();
            rejectPersonMap.put("params","customerStatus");
            rejectPersonMap.put("paramsValue",CustomerStatus.STATUS_REJECT);
            rejectPersonMap.put("workbenchType",WorkbenchType.PERSON_CUSTOMER_STATUS_REJECT);
            rejectPersonMap.put("count",customerCountMap.get("personCustomerRejectCount"));
            customerListMap.add(rejectPersonMap);

            //审核中的工作流
            Map<String,Object> verifyingWorkflowMap = new HashMap();
            verifyingWorkflowMap.put("params","verifyStatus");
            verifyingWorkflowMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_COMMIT);
            verifyingWorkflowMap.put("workbenchType",WorkbenchType.WORKFLOW_STATUS_VERIFYING);
            verifyingWorkflowMap.put("count",workflowCountMap.get("workflowVerifyingCount"));
            workflowListMap.add(verifyingWorkflowMap);

            //被驳回的工作流
            Map<String,Object> rejectWorkflowMap = new HashMap();
            rejectWorkflowMap.put("params","verifyStatus");
            rejectWorkflowMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_BACK);
            rejectWorkflowMap.put("workbenchType",WorkbenchType.WORKFLOW__STATUS_REJECT);
            rejectWorkflowMap.put("count",workflowCountMap.get("workflowRejectCount"));
            workflowListMap.add(rejectWorkflowMap);

            workbench.setOrderListMap(orderListMap);
            workbench.setK3ReturnOrderListMap(k3ReturnOrderListMap);
            workbench.setCustomerListMap(customerListMap);
            workbench.setWorkflowListMap(workflowListMap);

            redisManager.add("SALES_WORKBENCH_"+userSupport.getCurrentUserId().toString(),workbench,CommonConstant.WORKBENCH_REDIS_SAVE_TIME);
        }

        //商务工作台
        if (CommonConstant.COMMON_BUSINESS_AFFAIRS_WORKBENCH.equals(workbenchQueryParam.getWorkbenchName())){

            Workbench workbenchRedis = redisManager.get("BUSINESS_WORKBENCH_" + userSupport.getCurrentUserId().toString(), Workbench.class);
            if (workbenchRedis != null){
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(workbenchRedis);
                return serviceResult;
            }

            Map<String, Object> paramMap = new HashMap<>();
            //只有审核人数据
            //----------------------商务待审核工作流---------------------------
            paramMap.put("businessAffairsVerifyUserId", userSupport.getCurrentUserId().toString());
            //----------------------商务待审核工作流---------------------------

            //----------------------待认领银行流水---------------------------
            Integer departmentType = bankSlipSupport.departmentType();

            paramMap.put("departmentType", departmentType);
            paramMap.put("subCompanyId", userSupport.getCurrentUserCompanyId());
            paramMap.put("currentUser", userSupport.getCurrentUserId().toString());
            //----------------------待认领银行流水---------------------------

            //----------------------未支付，部分支付的结算单---------------------------
            //预计支付时间提前七天查询
            paramMap.put("statementExpectPayEndTime", DateUtil.getDayByOffset(new Date(), CommonConstant.STATEMENT_ADVANCE_EXPECT_PAY_END_TIME));
            paramMap.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
            //----------------------未支付，部分支付的结算单---------------------------

            //订单权限
            paramMap.put("orderPermissionParam",permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));
            //退货单权限
            paramMap.put("k3ReturnOrderPermissionParam",permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

            paramMap.put("workbenchQueryParam",workbenchQueryParam);
            paramMap.put("workbenchName",workbenchQueryParam.getWorkbenchName());

            Map<String,Integer> orderCountMap = workbenchMapper.findOrderWorkbenchCount(paramMap);
            Map<String,Integer> k3ReturnOrderCountMap = workbenchMapper.findK3ReturnOrderWorkbenchCount(paramMap);
            Map<String,Integer> workflowBusinessAffairsCountMap = workbenchMapper.findWorkflowBusinessAffairsWorkbenchCount(paramMap);
            Map<String,Integer> bankSlipDetailBusinessAffairsCountMap = workbenchMapper.findBankSlipDetailBusinessAffairsWorkbenchCount(paramMap);
            Map<String,Integer> statementOrderBusinessAffairsCountMap = workbenchMapper.findStatementOrderBusinessAffairsWorkbenchCount(paramMap);


            //----------------------商务待审核工作数量---------------------------
            Map<String,Object> workflowLinkWaitVerifyBusinessAffairsMap = new HashMap();
            workflowLinkWaitVerifyBusinessAffairsMap.put("params","verifyStatus");
            workflowLinkWaitVerifyBusinessAffairsMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkWaitVerifyBusinessAffairsMap.put("workbenchType",WorkbenchType.WORK_FLOW_LINK_WAIT_VERIFY);  //审核中的工作流
            workflowLinkWaitVerifyBusinessAffairsMap.put("count",workflowBusinessAffairsCountMap.get("waitVerifyWorkflowBusinessAffairsWorkbenchCount"));
            workflowBusinessAffairsListMap.add(workflowLinkWaitVerifyBusinessAffairsMap);
            //----------------------商务待审核工作流数量---------------------------

            //----------------------待认领银行流水数量---------------------------
            Map<String,Object> bankSlipDetailWaitClaimMap = new HashMap();
            bankSlipDetailWaitClaimMap.put("params","bankSlipDetailStatus");
            bankSlipDetailWaitClaimMap.put("paramsValue",BankSlipDetailStatus.UN_CLAIMED);
            bankSlipDetailWaitClaimMap.put("workbenchType",WorkbenchType.BANK_SLIP_DETAIL_WAIT_CLAIM);  //待认领银行流水数量
            bankSlipDetailWaitClaimMap.put("count",bankSlipDetailBusinessAffairsCountMap.get("waitClaimCount"));
            bankSlipDetailListMap.add(bankSlipDetailWaitClaimMap);
            //----------------------待认领银行流水数量---------------------------

            //----------------------未支付的结算单数量---------------------------
            Map<String,Object> statementOrderUnpaidMap = new HashMap();
            statementOrderUnpaidMap.put("params","statementOrderStatus");
            statementOrderUnpaidMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
            statementOrderUnpaidMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_INIT);
            statementOrderUnpaidMap.put("count",statementOrderBusinessAffairsCountMap.get("unpaidStatementOrderBusinessAffairsWorkbenchCount"));
            statementOrderBusinessAffairsListMap.add(statementOrderUnpaidMap);
            //----------------------未支付的结算单数量---------------------------

            //----------------------部分支付的结算单数量---------------------------
            Map<String,Object> statementOrderStatusSettledPartMap = new HashMap();
            statementOrderStatusSettledPartMap.put("params","statementOrderStatus");
            statementOrderStatusSettledPartMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
            statementOrderStatusSettledPartMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_SETTLED_PART);
            statementOrderStatusSettledPartMap.put("count",statementOrderBusinessAffairsCountMap.get("settledPartStatementOrderBusinessAffairsWorkbenchCount"));
            statementOrderBusinessAffairsListMap.add(statementOrderStatusSettledPartMap);
            //----------------------部分支付的结算单数量---------------------------
            //----------------------------审核中的订单---------------------------
            Map<String,Object> verifyingOrderMap = new HashMap();
            verifyingOrderMap.put("params","orderStatus");
            verifyingOrderMap.put("paramsValue",OrderStatus.ORDER_STATUS_VERIFYING);
            verifyingOrderMap.put("workbenchType",WorkbenchType.ORDER_STATUS_VERIFYING);
            verifyingOrderMap.put("count",orderCountMap.get("orderVerifyingCount"));
            orderListMap.add(verifyingOrderMap);
            //----------------------------审核中的订单---------------------------

            //----------------------------到期未处理的订单---------------------------
            Map<String,Object> overDueMap= new HashMap();
            overDueMap.put("params","isReturnOverDue");
            overDueMap.put("paramsValue",CommonConstant.COMMON_CONSTANT_YES);
            overDueMap.put("workbenchType",WorkbenchType.ORDER_STATUS_OVER_DUE);
            overDueMap.put("count",orderCountMap.get("orderOverDueCount"));
            orderListMap.add(overDueMap);
            //----------------------------到期未处理的订单---------------------------

            //----------------------------审核中的退货单---------------------------
            Map<String,Object> verifyingReturnOrderMap = new HashMap();
            verifyingReturnOrderMap.put("params","returnOrderStatus");
            verifyingReturnOrderMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING);
            verifyingReturnOrderMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_VERIFYING);
            verifyingReturnOrderMap.put("count",k3ReturnOrderCountMap.get("returnOrderVerifyingCount"));
            k3ReturnOrderListMap.add(verifyingReturnOrderMap);
            //----------------------------审核中的退货单---------------------------

            workbench.setOrderListMap(orderListMap);
            workbench.setK3ReturnOrderListMap(k3ReturnOrderListMap);
            workbench.setWorkflowBusinessAffairsListMap(workflowBusinessAffairsListMap);
            workbench.setBankSlipDetailBusinessAffairsListMap(bankSlipDetailListMap);
            workbench.setStatementOrderBusinessAffairsListMap(statementOrderBusinessAffairsListMap);

            redisManager.add("BUSINESS_WORKBENCH_"+userSupport.getCurrentUserId().toString(),workbench,CommonConstant.WORKBENCH_REDIS_SAVE_TIME);
        }

        if (CommonConstant.COMMON_SALES_AND_BUSINESS_AFFAIRS_WORKBENCH.equals(workbenchQueryParam.getWorkbenchName())){

            Workbench workbenchRedis = redisManager.get("BUSINESS_AND_SALES_WORKBENCH_" + userSupport.getCurrentUserId().toString(), Workbench.class);
            if (workbenchRedis != null){
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(workbenchRedis);
                return serviceResult;
            }

            //只有审核人数据
            if (!userSupport.isSuperUser()) {
                maps.put("verifyUserId", userSupport.getCurrentUserId().toString());
                maps.put("currentUserGroupList",workflowVerifyUserGroupMapper.findGroupUUIDByUserId(userSupport.getCurrentUserId()));
            }

            //----------------------商务待审核工作流---------------------------
            maps.put("businessAffairsVerifyUserId", userSupport.getCurrentUserId().toString());
            //----------------------商务待审核工作流---------------------------

            //----------------------待认领银行流水---------------------------
            Integer departmentType = bankSlipSupport.departmentType();

            maps.put("departmentType", departmentType);
            maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
            maps.put("currentUser", userSupport.getCurrentUserId().toString());
            //----------------------待认领银行流水---------------------------

            //----------------------未支付，部分支付的结算单---------------------------
            //预计支付时间提前七天查询
            maps.put("statementExpectPayEndTime", DateUtil.getDayByOffset(new Date(), CommonConstant.STATEMENT_ADVANCE_EXPECT_PAY_END_TIME));
            maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
            //----------------------未支付，部分支付的结算单---------------------------

            //可续租单的提前时间值
            maps.put("reletTimeOfDay",CommonConstant.RELET_TIME_OF_RENT_TYPE_DAY ); //短租提前15天
            maps.put("reletTimeOfMonth",CommonConstant.RELET_TIME_OF_RENT_TYPE_MONTH); //长租提前30天

            maps.put("workbenchQueryParam",workbenchQueryParam);
            maps.put("workbenchName",workbenchQueryParam.getWorkbenchName());
            //订单权限
            maps.put("orderPermissionParam",permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));
            //退货单权限
            maps.put("k3ReturnOrderPermissionParam",permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));
            //企业客户权限
            maps.put("companyCustomerPermissionParam",permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
            //个人客户权限
            maps.put("personCustomerPermissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

            Map<String,Integer> orderCountMap = workbenchMapper.findOrderWorkbenchCount(maps);
            Map<String,Integer> k3ReturnOrderCountMap = workbenchMapper.findK3ReturnOrderWorkbenchCount(maps);
            Map<String,Integer> customerCountMap = workbenchMapper.findCustomerWorkbenchCount(maps);
            Map<String,Integer> workflowCountMap = workbenchMapper.findWorkflowWorkbenchCountForSales(maps);
            Map<String,Integer> workflowBusinessAffairsCountMap = workbenchMapper.findWorkflowBusinessAffairsWorkbenchCount(maps);
            Map<String,Integer> bankSlipDetailBusinessAffairsCountMap = workbenchMapper.findBankSlipDetailBusinessAffairsWorkbenchCount(maps);
            Map<String,Integer> statementOrderBusinessAffairsCountMap = workbenchMapper.findStatementOrderBusinessAffairsWorkbenchCount(maps);

            //审核中的订单
            Map<String,Object> verifyingOrderMap = new HashMap();
            verifyingOrderMap.put("params","orderStatus");
            verifyingOrderMap.put("paramsValue",OrderStatus.ORDER_STATUS_VERIFYING);
            verifyingOrderMap.put("workbenchType",WorkbenchType.ORDER_STATUS_VERIFYING);
            verifyingOrderMap.put("count",orderCountMap.get("orderVerifyingCount"));
            orderListMap.add(verifyingOrderMap);

            //待发货的订单
            Map<String,Object> waitDeliveryMap = new HashMap();
            waitDeliveryMap.put("params","orderStatus");
            waitDeliveryMap.put("paramsValue",OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
            waitDeliveryMap.put("workbenchType",WorkbenchType.ORDER_STATUS_WAIT_DELIVERY);
            waitDeliveryMap.put("count",orderCountMap.get("orderWaitDeliveryCount"));
            orderListMap.add(waitDeliveryMap);

            //到期未处理的订单
            Map<String,Object> overDueMap= new HashMap();
            overDueMap.put("params","isReturnOverDue");
            overDueMap.put("paramsValue",CommonConstant.COMMON_CONSTANT_YES);
            overDueMap.put("workbenchType",WorkbenchType.ORDER_STATUS_OVER_DUE);
            overDueMap.put("count",orderCountMap.get("orderOverDueCount"));
            orderListMap.add(overDueMap);

            //可续租的订单
            Map<String,Object> canReletOrderMap= new HashMap();
            canReletOrderMap.put("params","isCanReletOrder");
            canReletOrderMap.put("paramsValue",CommonConstant.COMMON_CONSTANT_YES);
            canReletOrderMap.put("workbenchType",WorkbenchType.ORDER_STATUS_CAN_RELET);
            canReletOrderMap.put("count",orderCountMap.get("canReletOrderCount"));
            orderListMap.add(canReletOrderMap);

            //未提交的退货单
            Map<String,Object> waitCommitMap = new HashMap();
            waitCommitMap.put("params","returnOrderStatus");
            waitCommitMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
            waitCommitMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_WAIT_COMMIT);
            waitCommitMap.put("count",k3ReturnOrderCountMap.get("returnOrderWaitCommitCount"));
            k3ReturnOrderListMap.add(waitCommitMap);

            //审核中的退货单
            Map<String,Object> verifyingReturnOrderMap = new HashMap();
            verifyingReturnOrderMap.put("params","returnOrderStatus");
            verifyingReturnOrderMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING);
            verifyingReturnOrderMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_VERIFYING);
            verifyingReturnOrderMap.put("count",k3ReturnOrderCountMap.get("returnOrderVerifyingCount"));
            k3ReturnOrderListMap.add(verifyingReturnOrderMap);

            //处理中的退货单
            Map<String,Object> processingMap = new HashMap();
            processingMap.put("params","returnOrderStatus");
            processingMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
            processingMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_PROCESSING);
            processingMap.put("count",k3ReturnOrderCountMap.get("returnOrderProcessingCount"));
            k3ReturnOrderListMap.add(processingMap);

            //被驳回的退货单
            Map<String,Object> backedMap = new HashMap();
            backedMap.put("params","returnOrderStatus");
            backedMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED);
            backedMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_REJECT);
            backedMap.put("count",k3ReturnOrderCountMap.get("returnOrderBackedCount"));
            k3ReturnOrderListMap.add(backedMap);

            //审核中的企业客户
            Map<String,Object> verifyingCompanyMap = new HashMap();
            verifyingCompanyMap.put("params","customerStatus");
            verifyingCompanyMap.put("paramsValue",CustomerStatus.STATUS_COMMIT);
            verifyingCompanyMap.put("workbenchType",WorkbenchType.COMPANY_CUSTOMER_STATUS_VERIFYING);
            verifyingCompanyMap.put("count",customerCountMap.get("companyCustomerVerifyingCount"));
            customerListMap.add(verifyingCompanyMap);

            //被驳回的企业客户
            Map<String,Object> rejectCompanyMap = new HashMap();
            rejectCompanyMap.put("params","customerStatus");
            rejectCompanyMap.put("paramsValue",CustomerStatus.STATUS_REJECT);
            rejectCompanyMap.put("workbenchType",WorkbenchType.COMPANY_CUSTOMER_STATUS_REJECT);
            rejectCompanyMap.put("count",customerCountMap.get("companyCustomerRejectCount"));
            customerListMap.add(rejectCompanyMap);

            //审核中的个人客户
            Map<String,Object> verifyingPersonMap = new HashMap();
            verifyingPersonMap.put("params","customerStatus");
            verifyingPersonMap.put("paramsValue",CustomerStatus.STATUS_COMMIT);
            verifyingPersonMap.put("workbenchType",WorkbenchType.PERSON_CUSTOMER_STATUS_VERIFYING);
            verifyingPersonMap.put("count",customerCountMap.get("personCustomerVerifyingCount"));
            customerListMap.add(verifyingPersonMap);

            //被驳回的个人客户
            Map<String,Object> rejectPersonMap = new HashMap();
            rejectPersonMap.put("params","customerStatus");
            rejectPersonMap.put("paramsValue",CustomerStatus.STATUS_REJECT);
            rejectPersonMap.put("workbenchType",WorkbenchType.PERSON_CUSTOMER_STATUS_REJECT);
            rejectPersonMap.put("count",customerCountMap.get("personCustomerRejectCount"));
            customerListMap.add(rejectPersonMap);

            //审核中的工作流
            Map<String,Object> verifyingWorkflowMap = new HashMap();
            verifyingWorkflowMap.put("params","verifyStatus");
            verifyingWorkflowMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_COMMIT);
            verifyingWorkflowMap.put("workbenchType",WorkbenchType.WORKFLOW_STATUS_VERIFYING);
            verifyingWorkflowMap.put("count",workflowCountMap.get("workflowVerifyingCount"));
            workflowListMap.add(verifyingWorkflowMap);

            //被驳回的工作流
            Map<String,Object> rejectWorkflowMap = new HashMap();
            rejectWorkflowMap.put("params","verifyStatus");
            rejectWorkflowMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_BACK);
            rejectWorkflowMap.put("workbenchType",WorkbenchType.WORKFLOW__STATUS_REJECT);
            rejectWorkflowMap.put("count",workflowCountMap.get("workflowRejectCount"));
            workflowListMap.add(rejectWorkflowMap);


            //----------------------商务待审核工作数量---------------------------
            Map<String,Object> workflowLinkWaitVerifyBusinessAffairsMap = new HashMap();
            workflowLinkWaitVerifyBusinessAffairsMap.put("params","verifyStatus");
            workflowLinkWaitVerifyBusinessAffairsMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkWaitVerifyBusinessAffairsMap.put("workbenchType",WorkbenchType.WORK_FLOW_LINK_WAIT_VERIFY);  //审核中的工作流
            workflowLinkWaitVerifyBusinessAffairsMap.put("count",workflowBusinessAffairsCountMap.get("waitVerifyWorkflowBusinessAffairsWorkbenchCount"));
            workflowBusinessAffairsListMap.add(workflowLinkWaitVerifyBusinessAffairsMap);
            //----------------------商务待审核工作流数量---------------------------

            //----------------------待认领银行流水数量---------------------------
            Map<String,Object> bankSlipDetailWaitClaimMap = new HashMap();
            bankSlipDetailWaitClaimMap.put("params","bankSlipDetailStatus");
            bankSlipDetailWaitClaimMap.put("paramsValue",BankSlipDetailStatus.UN_CLAIMED);
            bankSlipDetailWaitClaimMap.put("workbenchType",WorkbenchType.BANK_SLIP_DETAIL_WAIT_CLAIM);  //待认领银行流水数量
            bankSlipDetailWaitClaimMap.put("count",bankSlipDetailBusinessAffairsCountMap.get("waitClaimCount"));
            bankSlipDetailListMap.add(bankSlipDetailWaitClaimMap);
            //----------------------待认领银行流水数量---------------------------

            //----------------------未支付的结算单数量---------------------------
            Map<String,Object> statementOrderUnpaidMap = new HashMap();
            statementOrderUnpaidMap.put("params","statementOrderStatus");
            statementOrderUnpaidMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
            statementOrderUnpaidMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_INIT);
            statementOrderUnpaidMap.put("count",statementOrderBusinessAffairsCountMap.get("unpaidStatementOrderBusinessAffairsWorkbenchCount"));
            statementOrderBusinessAffairsListMap.add(statementOrderUnpaidMap);
            //----------------------未支付的结算单数量---------------------------

            //----------------------部分支付的结算单数量---------------------------
            Map<String,Object> statementOrderStatusSettledPartMap = new HashMap();
            statementOrderStatusSettledPartMap.put("params","statementOrderStatus");
            statementOrderStatusSettledPartMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
            statementOrderStatusSettledPartMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_SETTLED_PART);
            statementOrderStatusSettledPartMap.put("count",statementOrderBusinessAffairsCountMap.get("settledPartStatementOrderBusinessAffairsWorkbenchCount"));
            statementOrderBusinessAffairsListMap.add(statementOrderStatusSettledPartMap);
            //----------------------部分支付的结算单数量---------------------------

            workbench.setOrderListMap(orderListMap);
            workbench.setK3ReturnOrderListMap(k3ReturnOrderListMap);
            workbench.setCustomerListMap(customerListMap);
            workbench.setWorkflowListMap(workflowListMap);
            workbench.setWorkflowBusinessAffairsListMap(workflowBusinessAffairsListMap);
            workbench.setBankSlipDetailBusinessAffairsListMap(bankSlipDetailListMap);
            workbench.setStatementOrderBusinessAffairsListMap(statementOrderBusinessAffairsListMap);

            redisManager.add("BUSINESS_AND_SALES_WORKBENCH_"+userSupport.getCurrentUserId().toString(),workbench,CommonConstant.WORKBENCH_REDIS_SAVE_TIME);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(workbench);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Workbench> queryWarehouseWorkbenchCount(WorkbenchQueryParam workbenchQueryParam) {
        ServiceResult<String, Workbench> serviceResult = new ServiceResult<>();
        Workbench workbench = new Workbench();


        List<Map<String,Object>> workflowWarehouseListMap = new ArrayList<>();
        List<Map<String,Object>> orderWarehouseListMap = new ArrayList<>();
        List<Map<String,Object>> returnOrderWarehouseListMap = new ArrayList<>();

        if (CommonConstant.COMMON_WORKHOUSE_WORKBENCH.equals(workbenchQueryParam.getWorkbenchName())){
            Date now = new Date();
            Date currentDateTime = DateUtil.getDayByOffset(now, CommonConstant.COMMON_ZERO);
            Date currentDateAddOne = DateUtil.getDayByOffset(now, CommonConstant.COMMON_ONE);
            Date currentDateAddSix = com.lxzl.erp.common.util.DateUtil.getDayByOffset(now, CommonConstant.COMMON_SIX);
            Date currentDateAddEight = com.lxzl.erp.common.util.DateUtil.getDayByOffset(now, CommonConstant.COMMON_EIGHT);

            Workbench workhouseWorkbenchRedis = redisManager.get("WAREHOUSE_WORKBENCH_COUNT_" + userSupport.getCurrentUserId().toString(), Workbench.class);
            if (workhouseWorkbenchRedis != null){
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(workhouseWorkbenchRedis);
                return serviceResult;
            }
            //只有审核人数据
            //----------------------仓库待审核工作流查询条件---------------------------
            Map<String, Object> workflowWarehouseWorkbenchParamMap = new HashMap<>();
            workflowWarehouseWorkbenchParamMap.put("currentUserId", userSupport.getCurrentUserId().toString());
            //----------------------仓库待审核工作流查询条件---------------------------

            //----------------------仓库未打印的订单查询条件---------------------------
            Map<String, Object> orderWarehouseWorkbenchParamMap = new HashMap<>();
            orderWarehouseWorkbenchParamMap.put("currentUserId", userSupport.getCurrentUserId().toString());
            orderWarehouseWorkbenchParamMap.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));
            //----------------------仓库未打印的订单查询条件---------------------------

            //----------------------今日待发货的订单查询条件---------------------------
            orderWarehouseWorkbenchParamMap.put("deliverySubCompanyId",userSupport.getCurrentUserCompanyId());
            orderWarehouseWorkbenchParamMap.put("orderSubCompanyId",userSupport.getCurrentUserCompanyId());
            orderWarehouseWorkbenchParamMap.put("currentDateTime",currentDateTime);
            orderWarehouseWorkbenchParamMap.put("currentDateAddOne",currentDateAddOne);
            //----------------------今日待发货的订单查询条件---------------------------

            //----------------------快递待发货的订单查询条件---------------------------
            orderWarehouseWorkbenchParamMap.put("currentDateAddSix",currentDateAddSix);
            //----------------------快递待发货的订单查询条件---------------------------

            //----------------------转单待发货的订单查询条件---------------------------
            orderWarehouseWorkbenchParamMap.put("currentDateAddEight",currentDateAddEight);
            //----------------------转单待发货的订单查询条件---------------------------


            //----------------------处理中的退货单---------------------------
            Map<String, Object> returnOrderWarehouseWorkbenchParamMap = new HashMap<>();
            returnOrderWarehouseWorkbenchParamMap.put("deliverySubCompanyId",userSupport.getCurrentUserCompanyId());
            returnOrderWarehouseWorkbenchParamMap.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER,PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_WAREHOUSE));
            //----------------------处理中的退货单---------------------------


            Map<String,Integer> workflowWarehouseCountMap = workbenchMapper.findWorkflowWarehouseWorkbenchCount(workflowWarehouseWorkbenchParamMap);
            Map<String,Integer> orderCountMap = workbenchMapper.findOrderWarehouseWorkbenchCount(orderWarehouseWorkbenchParamMap);
            Map<String,Integer> returnOrderCountMap = workbenchMapper.findReturnOderWarehouseWorkbenchCount(returnOrderWarehouseWorkbenchParamMap);

            //----------------------仓库待审核工作数量---------------------------
            Map<String,Object> workflowLinkWaitVerifyWarehouseMap = new HashMap();
            workflowLinkWaitVerifyWarehouseMap.put("params","verifyStatus");
            workflowLinkWaitVerifyWarehouseMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkWaitVerifyWarehouseMap.put("workbenchType",WorkbenchType.WORK_FLOW_LINK_WAIT_VERIFY);  //审核中的工作流
            workflowLinkWaitVerifyWarehouseMap.put("count",workflowWarehouseCountMap.get("waitVerifyWorkflowWarehouseWorkbenchCount"));
            workflowWarehouseListMap.add(workflowLinkWaitVerifyWarehouseMap);
            //----------------------仓库待审核工作流数量---------------------------
            //----------------------仓库未打印的订单数量---------------------------
            Map<String,Object> notPrintLogOderWarehouseMap = new HashMap();
            notPrintLogOderWarehouseMap.put("params","warehouseWorkbenchOrderType");
            notPrintLogOderWarehouseMap.put("paramsValue",WarehouseWorkbenchOrderType.NOT_PRINT_LOG_ODER);
            notPrintLogOderWarehouseMap.put("workbenchType",WorkbenchType.NOT_PRINT_LOG_ODER);  //未打印订单
            notPrintLogOderWarehouseMap.put("count",orderCountMap.get("notPrintLogOderWarehouseWorkbenchCount"));
            orderWarehouseListMap.add(notPrintLogOderWarehouseMap);
            //----------------------仓库未打印的订单数量---------------------------
            // ----------------------今日待发货的订单数量---------------------------
            Map<String,Object> todayAwaitDeliveryOderWarehouseMap = new HashMap();
            todayAwaitDeliveryOderWarehouseMap.put("params","warehouseWorkbenchOrderType");
            todayAwaitDeliveryOderWarehouseMap.put("paramsValue",WarehouseWorkbenchOrderType.TODAY_AWAIT_DELIVERY_ODER);
            todayAwaitDeliveryOderWarehouseMap.put("workbenchType",WorkbenchType.TODAY_AWAIT_DELIVERY_ODER);
            todayAwaitDeliveryOderWarehouseMap.put("count",orderCountMap.get("todayAwaitDeliveryOderCount"));
            orderWarehouseListMap.add(todayAwaitDeliveryOderWarehouseMap);
            //----------------------今日待发货的订单数量---------------------------

            //----------------------快递待发货的订单数量---------------------------
            Map<String,Object> expressAwaitDeliveryOderWarehouseMap = new HashMap();
            expressAwaitDeliveryOderWarehouseMap.put("params","warehouseWorkbenchOrderType");
            expressAwaitDeliveryOderWarehouseMap.put("paramsValue",WarehouseWorkbenchOrderType.EXPRESS_AWAIT_DELIVERY_ODER);
            expressAwaitDeliveryOderWarehouseMap.put("workbenchType",WorkbenchType.EXPRESS_AWAIT_DELIVERY_ODER);
            expressAwaitDeliveryOderWarehouseMap.put("count",orderCountMap.get("expressAwaitDeliveryOderCount"));
            orderWarehouseListMap.add(expressAwaitDeliveryOderWarehouseMap);
            //----------------------快递待发货的订单数量---------------------------

            //----------------------转单待发货的订单数量---------------------------
            Map<String,Object> slipAwaitDeliveryOderWarehouseMap = new HashMap();
            slipAwaitDeliveryOderWarehouseMap.put("params","warehouseWorkbenchOrderType");
            slipAwaitDeliveryOderWarehouseMap.put("paramsValue",WarehouseWorkbenchOrderType.SLIP_AWAIT_DELIVERY_ODER);
            slipAwaitDeliveryOderWarehouseMap.put("workbenchType",WorkbenchType.SLIP_AWAIT_DELIVERY_ODER);
            slipAwaitDeliveryOderWarehouseMap.put("count",orderCountMap.get("slipAwaitDeliveryOderCount"));
            orderWarehouseListMap.add(slipAwaitDeliveryOderWarehouseMap);
            //----------------------转单待发货的订单数量---------------------------


            //----------------------逾期未发货的订单数量---------------------------
            Map<String,Object> overdueAwaitDeliveryOderWarehouseMap = new HashMap();
            overdueAwaitDeliveryOderWarehouseMap.put("params","warehouseWorkbenchOrderType");
            overdueAwaitDeliveryOderWarehouseMap.put("paramsValue",WarehouseWorkbenchOrderType.OVERDUE_UN_SHIPPED_DELIVERY_ODER);
            overdueAwaitDeliveryOderWarehouseMap.put("workbenchType",WorkbenchType.OVERDUE_UN_SHIPPED_DELIVERY_ODER);
            overdueAwaitDeliveryOderWarehouseMap.put("count",orderCountMap.get("overdueUnShippedDeliveryOderCount"));
            orderWarehouseListMap.add(overdueAwaitDeliveryOderWarehouseMap);
            //----------------------逾期未发货的订单数量---------------------------


            //----------------------未确认收货的订单数量---------------------------
            Map<String,Object> unConfirmedAwaitDeliveryOderWarehouseMap = new HashMap();
            unConfirmedAwaitDeliveryOderWarehouseMap.put("params","warehouseWorkbenchOrderType");
            unConfirmedAwaitDeliveryOderWarehouseMap.put("paramsValue",WarehouseWorkbenchOrderType.UN_CONFIRMED_DELIVERY_ODER);
            unConfirmedAwaitDeliveryOderWarehouseMap.put("workbenchType",WorkbenchType.UN_CONFIRMED_AWAIT_DELIVERY_ODER);
            unConfirmedAwaitDeliveryOderWarehouseMap.put("count",orderCountMap.get("unConfirmedDeliveryOderCount"));
            orderWarehouseListMap.add(unConfirmedAwaitDeliveryOderWarehouseMap);
            //----------------------未确认收货的订单数量---------------------------

            //----------------------处理中的退货单数量---------------------------
            Map<String,Object> returnOrderWarehouseMap = new HashMap();
            returnOrderWarehouseMap.put("params","returnOrderStatus");
            returnOrderWarehouseMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
            returnOrderWarehouseMap.put("workbenchType",WorkbenchType.RETURN_ORDER_STATUS_PROCESSING);
            returnOrderWarehouseMap.put("count",returnOrderCountMap.get("processingReturnOderCount"));
            returnOrderWarehouseListMap.add(returnOrderWarehouseMap);
            //----------------------处理中的退货单数量---------------------------


            workbench.setWorkflowWarehouseListMap(workflowWarehouseListMap);
            workbench.setOrderWarehouseListMap(orderWarehouseListMap);
            workbench.setReturnOrderWarehouseListMap(returnOrderWarehouseListMap);

            redisManager.add("WAREHOUSE_WORKBENCH_COUNT_" + userSupport.getCurrentUserId().toString(),workbench,CommonConstant.WORKBENCH_REDIS_SAVE_TIME);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(workbench);
        return serviceResult;
    }

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private OrderService orderService;

    @Autowired
    private K3ReturnOrderService k3ReturnOrderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private OrderSupport orderSupport;

    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;

    @Autowired
    private BankSlipDetailMapper bankSlipDetailMapper;

    @Autowired
    private BankSlipSupport bankSlipSupport;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private WorkflowVerifyUserGroupMapper workflowVerifyUserGroupMapper;

    @Autowired
    private WorkbenchMapper workbenchMapper;

    @Autowired
    private RedisManager redisManager;

}

