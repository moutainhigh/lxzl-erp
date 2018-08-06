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
import org.springframework.data.redis.core.RedisConnectionUtils;
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

        Map<String,Object> maps = new HashMap<>();

        //业务工作台
        if (CommonConstant.COMMON_ZERO.equals(workbenchQueryParam.getWorkbenchName())){

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

            redisManager.add("SALES_WORKBENCH_"+userSupport.getCurrentUserId().toString(),workbench,180L);
        }

        //商务工作台
        if (CommonConstant.COMMON_ONE.equals(workbenchQueryParam.getWorkbenchName())){

            Workbench workbenchRedis = redisManager.get("BUSINESS_WORKBENCH_" + userSupport.getCurrentUserId().toString(), Workbench.class);
            if (workbenchRedis != null){
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(workbenchRedis);
                return serviceResult;
            }

            Map<String, Object> paramMap = new HashMap<>();
            //只有审核人数据
            if (!userSupport.isSuperUser()) {
                paramMap.put("verifyUserId", userSupport.getCurrentUserId().toString());
            }
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

            //----------------------商务待审核工作数量---------------------------
            Map<String,Object> workflowLinkWaitVerifyMap = new HashMap();
            workflowLinkWaitVerifyMap.put("params","verifyStatus");
            workflowLinkWaitVerifyMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkWaitVerifyMap.put("workbenchType",WorkbenchType.WORK_FLOW_LINK_WAIT_VERIFY);  //审核中的工作流
//            workflowLinkWaitVerifyMap.put("count",workbenchDO.getWorkflowLinkWaitVerifyCount());
//            listMap.add(workflowLinkWaitVerifyMap);
            //----------------------商务待审核工作流数量---------------------------

            //----------------------待认领银行流水数量---------------------------
            Map<String,Object> bankSlipDetailWaitClaimMap = new HashMap();
            bankSlipDetailWaitClaimMap.put("params","bankSlipDetailStatus");
            bankSlipDetailWaitClaimMap.put("paramsValue",BankSlipDetailStatus.UN_CLAIMED);
            bankSlipDetailWaitClaimMap.put("workbenchType",WorkbenchType.BANK_SLIP_DETAIL_WAIT_CLAIM);  //待认领银行流水数量
//            bankSlipDetailWaitClaimMap.put("count",workbenchDO.getBankSlipDetailWaitClaimCount());
//            listMap.add(bankSlipDetailWaitClaimMap);
            //----------------------待认领银行流水数量---------------------------

            //----------------------未支付的结算单数量---------------------------
            Map<String,Object> statementOrderUnpaidMap = new HashMap();
            statementOrderUnpaidMap.put("params","statementOrderStatus");
            statementOrderUnpaidMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
            statementOrderUnpaidMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_INIT);
//            statementOrderUnpaidMap.put("count",workbenchDO.getStatementOrderUnpaidCount());
//            listMap.add(statementOrderUnpaidMap);
            //----------------------未支付的结算单数量---------------------------

            //----------------------部分支付的结算单数量---------------------------
            Map<String,Object> statementOrderStatusSettledPartMap = new HashMap();
            statementOrderStatusSettledPartMap.put("params","statementOrderStatus");
            statementOrderStatusSettledPartMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
            statementOrderStatusSettledPartMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_SETTLED_PART);
//            statementOrderStatusSettledPartMap.put("count",workbenchDO.getStatementOrderSettledPartCount());
//            listMap.add(statementOrderStatusSettledPartMap);
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

            redisManager.add("BUSINESS_WORKBENCH_"+userSupport.getCurrentUserId().toString(),workbench,180L);
        }

        if (CommonConstant.COMMON_TWO.equals(workbenchQueryParam.getWorkbenchName())){

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
            Map<String,Object> workflowLinkWaitVerifyMap = new HashMap();
            workflowLinkWaitVerifyMap.put("params","verifyStatus");
            workflowLinkWaitVerifyMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkWaitVerifyMap.put("workbenchType",WorkbenchType.WORK_FLOW_LINK_WAIT_VERIFY);  //审核中的工作流
//            workflowLinkWaitVerifyMap.put("count",workbenchDO.getWorkflowLinkWaitVerifyCount());
//            listMap.add(workflowLinkWaitVerifyMap);
            //----------------------商务待审核工作流数量---------------------------

            //----------------------待认领银行流水数量---------------------------
            Map<String,Object> bankSlipDetailWaitClaimMap = new HashMap();
            bankSlipDetailWaitClaimMap.put("params","bankSlipDetailStatus");
            bankSlipDetailWaitClaimMap.put("paramsValue",BankSlipDetailStatus.UN_CLAIMED);
            bankSlipDetailWaitClaimMap.put("workbenchType",WorkbenchType.BANK_SLIP_DETAIL_WAIT_CLAIM);  //待认领银行流水数量
//            bankSlipDetailWaitClaimMap.put("count",workbenchDO.getBankSlipDetailWaitClaimCount());
//            listMap.add(bankSlipDetailWaitClaimMap);
            //----------------------待认领银行流水数量---------------------------

            //----------------------未支付的结算单数量---------------------------
            Map<String,Object> statementOrderUnpaidMap = new HashMap();
            statementOrderUnpaidMap.put("params","statementOrderStatus");
            statementOrderUnpaidMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
            statementOrderUnpaidMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_INIT);
//            statementOrderUnpaidMap.put("count",workbenchDO.getStatementOrderUnpaidCount());
//            listMap.add(statementOrderUnpaidMap);
            //----------------------未支付的结算单数量---------------------------

            //----------------------部分支付的结算单数量---------------------------
            Map<String,Object> statementOrderStatusSettledPartMap = new HashMap();
            statementOrderStatusSettledPartMap.put("params","statementOrderStatus");
            statementOrderStatusSettledPartMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
            statementOrderStatusSettledPartMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_SETTLED_PART);
//            statementOrderStatusSettledPartMap.put("count",workbenchDO.getStatementOrderSettledPartCount());
//            listMap.add(statementOrderStatusSettledPartMap);
            //----------------------部分支付的结算单数量---------------------------

            workbench.setOrderListMap(orderListMap);
            workbench.setK3ReturnOrderListMap(k3ReturnOrderListMap);
            workbench.setCustomerListMap(customerListMap);
            workbench.setWorkflowListMap(workflowListMap);

            redisManager.add("BUSINESS_AND_SALES_WORKBENCH_"+userSupport.getCurrentUserId().toString(),workbench,180L);
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

