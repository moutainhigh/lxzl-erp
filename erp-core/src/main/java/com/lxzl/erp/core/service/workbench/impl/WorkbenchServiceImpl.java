package com.lxzl.erp.core.service.workbench.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.workbench.*;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.k3.K3ReturnOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.order.impl.support.OrderSupport;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workbench.WorkbenchService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
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

    @Override
    public ServiceResult<String, List<Map<String,Object>>> queryVerifingOrder(WorkbenchOrderQueryParam workbenchOrderQueryParam) {
        ServiceResult<String, List<Map<String,Object>>> serviceResult = new ServiceResult<>();
        List<Map<String,Object>> MapList = new ArrayList<>();

        List<OrderQueryParam> orderQueryParamList = workbenchOrderQueryParam.getOrderQueryParamList();
        if (CollectionUtil.isNotEmpty(orderQueryParamList)){
            for (OrderQueryParam orderQueryParam: orderQueryParamList){
                ServiceResult<String, Page<Order>> orderResult = orderService.queryAllOrder(orderQueryParam);
                //审核中的订单
                if (OrderStatus.ORDER_STATUS_VERIFYING.equals(orderQueryParam.getOrderStatus())){
                    Map<String,Object> verifyingMap = new HashMap();
                    verifyingMap.put("params","orderStatus");
                    verifyingMap.put("paramsValue",OrderStatus.ORDER_STATUS_VERIFYING);
                    verifyingMap.put("workbenchType",WorkbenchType.VERIFYING);
                    verifyingMap.put("count",orderResult.getResult().getTotalCount());
                    MapList.add(verifyingMap);
                }
                //待发货的订单
                if (OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderQueryParam.getOrderStatus())){
                    Map<String,Object> waitDeliveryMap = new HashMap();
                    waitDeliveryMap.put("params","orderStatus");
                    waitDeliveryMap.put("paramsValue",OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
                    waitDeliveryMap.put("workbenchType",WorkbenchType.WAIT_DELIVERY);
                    waitDeliveryMap.put("count",orderResult.getResult().getTotalCount());
                    MapList.add(waitDeliveryMap);
                }
                //到期未处理的订单
                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderQueryParam.getIsReturnOverDue())){
                    Map<String,Object> overDueMap= new HashMap();
                    overDueMap.put("params","isReturnOverDue");
                    overDueMap.put("paramsValue",CommonConstant.COMMON_CONSTANT_YES);
                    overDueMap.put("workbenchType",WorkbenchType.OVER_DUE);
                    overDueMap.put("count",orderResult.getResult().getTotalCount());
                    MapList.add(overDueMap);
                }
                //未支付的订单
                if (PayStatus.PAY_STATUS_INIT.equals(orderQueryParam.getPayStatus())){
                    Map<String,Object> notPayMap = new HashMap();
                    notPayMap.put("params","payStatus");
                    notPayMap.put("paramsValue",PayStatus.PAY_STATUS_INIT);
                    notPayMap.put("workbenchType",WorkbenchType.NOT_PAY);
                    notPayMap.put("count",orderResult.getResult().getTotalCount());
                    MapList.add(notPayMap);
                }
                //可续租的订单
                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderQueryParam.getIsCanReletOrder())){
                    Map<String,Object> notPayMap = new HashMap();
                    notPayMap.put("params","isCanReletOrder");
                    notPayMap.put("paramsValue",CommonConstant.COMMON_CONSTANT_YES);
                    notPayMap.put("workbenchType",WorkbenchType.CAN_RELET);
                    notPayMap.put("count",orderResult.getResult().getTotalCount());
                    MapList.add(notPayMap);
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(MapList);
        return serviceResult;
    }

//    @Override
//    public ServiceResult<String, List<Map<String,Object>>> queryCanReletOrder(OrderQueryParam orderQueryParam) {
//        ServiceResult<String, List<Map<String,Object>>> serviceResult = new ServiceResult<>();
//        List<Map<String,Object>> MapList = new ArrayList<>();
//
//        Map<String,Object> maps = new HashMap<>();
//        maps.put("orderQueryParam",orderQueryParam);
//        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));
//
//        List<OrderDO> orderDOList = orderMapper.findOrderByOrderStatus(maps);
//        List<Order> canReletOrderList = new ArrayList<>();
//        for (OrderDO orderDO :orderDOList){
//            Order order = ConverterUtil.convert(orderDO, Order.class);
//            Integer canReletOrder = orderSupport.isOrderCanRelet(order);
//            order.setCanReletOrder(canReletOrder);
//            if (CommonConstant.COMMON_CONSTANT_YES.equals(order.getCanReletOrder())){
//                canReletOrderList.add(order);
//            }
//        }
//
//        Map<String,Object> resultMap = new HashMap();
//        resultMap.put("params","isCanReletOrder");
//        resultMap.put("paramsValue",CommonConstant.COMMON_CONSTANT_YES);
//        resultMap.put("workbenchType",WorkbenchType.CAN_RELET);
//        resultMap.put("count",canReletOrderList.size());
//
//        MapList.add(resultMap);
//
//        serviceResult.setErrorCode(ErrorCode.SUCCESS);
//        serviceResult.setResult(MapList);
//        return serviceResult;
//    }

    @Override
    public ServiceResult<String,List<Map<String,Object>>> queryReturnOrder(WorkbenchReturnOrderQueryParam workbenchReturnOrderQueryParam) {
        ServiceResult<String, List<Map<String,Object>>> serviceResult = new ServiceResult<>();
        List<Map<String,Object>> MapList = new ArrayList<>();

        List<Integer> returnOrderStatusList = workbenchReturnOrderQueryParam.getReturnOrderStatusList();
        if (CollectionUtil.isNotEmpty(returnOrderStatusList)) {
            for (Integer returnOrderStatus : returnOrderStatusList) {
                K3ReturnOrderQueryParam k3ReturnOrderQueryParam = new K3ReturnOrderQueryParam();
                k3ReturnOrderQueryParam.setReturnOrderStatus(returnOrderStatus);
                ServiceResult<String, Page<K3ReturnOrder>> k3ReturnOrderResult = k3ReturnOrderService.queryReturnOrder(k3ReturnOrderQueryParam);
                //未提交的退货单
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(k3ReturnOrderQueryParam.getReturnOrderStatus())) {
                    Map<String,Object> waitCommitMap = new HashMap();
                    waitCommitMap.put("params","returnOrderStatus");
                    waitCommitMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
                    waitCommitMap.put("workbenchType",WorkbenchType.WAIT_COMMIT);
                    waitCommitMap.put("count",k3ReturnOrderResult.getResult().getTotalCount());
                    MapList.add(waitCommitMap);
                }
                //审核中的退货单
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(k3ReturnOrderQueryParam.getReturnOrderStatus())) {
                    Map<String,Object> verifyingMap = new HashMap();
                    verifyingMap.put("params","returnOrderStatus");
                    verifyingMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING);
                    verifyingMap.put("workbenchType",WorkbenchType.VERIFYING);
                    verifyingMap.put("count",k3ReturnOrderResult.getResult().getTotalCount());
                    MapList.add(verifyingMap);
                }
                //处理中的退货单
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(k3ReturnOrderQueryParam.getReturnOrderStatus())) {
                    Map<String,Object> processingMap = new HashMap();
                    processingMap.put("params","returnOrderStatus");
                    processingMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
                    processingMap.put("workbenchType",WorkbenchType.PROCESSING);
                    processingMap.put("count",k3ReturnOrderResult.getResult().getTotalCount());
                    MapList.add(processingMap);
                }
                //被驳回的退货单
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED.equals(k3ReturnOrderQueryParam.getReturnOrderStatus())) {
                    Map<String,Object> backedMap = new HashMap();
                    backedMap.put("params","returnOrderStatus");
                    backedMap.put("paramsValue",ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED);
                    backedMap.put("workbenchType",WorkbenchType.REJECT);
                    backedMap.put("count",k3ReturnOrderResult.getResult().getTotalCount());
                    MapList.add(backedMap);
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(MapList);
        return serviceResult;
    }

    @Override
    public ServiceResult<String,  List<Map<String,Object>>> queryCompanyCustomer(WorkbenchCompanyCustomerQueryParam workbenchCompanyCustomerQueryParam) {
        ServiceResult<String,  List<Map<String,Object>>> serviceResult = new ServiceResult<>();
        List<Map<String,Object>> MapList = new ArrayList<>();

        List<Integer> customerCompanyStatusList = workbenchCompanyCustomerQueryParam.getCustomerCompanyStatusList();
        if (CollectionUtil.isNotEmpty(customerCompanyStatusList)){
            for (Integer customerCompanyStatus : customerCompanyStatusList){
                CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
                customerCompanyQueryParam.setCustomerStatus(customerCompanyStatus);
                customerCompanyQueryParam.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO); //客户查询默认为禁用
                ServiceResult<String, Page<Customer>> customerCompanyResult = customerService.pageCustomerCompany(customerCompanyQueryParam);
                if (CustomerStatus.STATUS_COMMIT.equals(customerCompanyQueryParam.getCustomerStatus())){
                    Map<String,Object> commitMap = new HashMap();
                    commitMap.put("params","customerStatus");
                    commitMap.put("paramsValue",CustomerStatus.STATUS_COMMIT);
                    commitMap.put("workbenchType",WorkbenchType.VERIFYING);
                    commitMap.put("count",customerCompanyResult.getResult().getTotalCount());
                    MapList.add(commitMap);
                }
                if (CustomerStatus.STATUS_REJECT.equals(customerCompanyQueryParam.getCustomerStatus())){
                    Map<String,Object> rejectMap = new HashMap();
                    rejectMap.put("params","customerStatus");
                    rejectMap.put("paramsValue",CustomerStatus.STATUS_REJECT);
                    rejectMap.put("workbenchType",WorkbenchType.REJECT);
                    rejectMap.put("count",customerCompanyResult.getResult().getTotalCount());
                    MapList.add(rejectMap);
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(MapList);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<Map<String,Object>>> queryPersonCustomer(WorkbenchPersonCustomerQueryParam workbenchPersonCustomerQueryParam) {
        ServiceResult<String,  List<Map<String,Object>>> serviceResult = new ServiceResult<>();
        List<Map<String,Object>> MapList = new ArrayList<>();

        List<Integer> customerPersonStatusList = workbenchPersonCustomerQueryParam.getCustomerPersonStatusList();
        if (CollectionUtil.isNotEmpty(customerPersonStatusList)){
            for (Integer customerPersonStatus : customerPersonStatusList){
                CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
                customerPersonQueryParam.setCustomerStatus(customerPersonStatus);
                customerPersonQueryParam.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO); //客户查询默认为禁用
                ServiceResult<String, Page<Customer>> customerPersonResult = customerService.pageCustomerPerson(customerPersonQueryParam);
                if (CustomerStatus.STATUS_COMMIT.equals(customerPersonQueryParam.getCustomerStatus())){
                    Map<String,Object> commitMap = new HashMap();
                    commitMap.put("params","customerStatus");
                    commitMap.put("paramsValue",CustomerStatus.STATUS_COMMIT);
                    commitMap.put("workbenchType",WorkbenchType.VERIFYING);
                    commitMap.put("count",customerPersonResult.getResult().getTotalCount());
                    MapList.add(commitMap);
                }
                if (CustomerStatus.STATUS_REJECT.equals(customerPersonQueryParam.getCustomerStatus())){
                    Map<String,Object> rejectMap = new HashMap();
                    rejectMap.put("params","customerStatus");
                    rejectMap.put("paramsValue",CustomerStatus.STATUS_REJECT);
                    rejectMap.put("workbenchType",WorkbenchType.REJECT);
                    rejectMap.put("count",customerPersonResult.getResult().getTotalCount());
                    MapList.add(rejectMap);
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(MapList);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<Map<String,Object>>> queryWorkflow(WorkbenchWorkflowQueryParam workbenchWorkflowQueryParam) {
        ServiceResult<String, List<Map<String,Object>>> serviceResult = new ServiceResult<>();
        List<Map<String,Object>> MapList = new ArrayList<>();

        List<Integer> workflowLinkStatusList = workbenchWorkflowQueryParam.getWorkflowLinkStatusList();
        if (CollectionUtil.isNotEmpty(workflowLinkStatusList)){
            for(Integer workflowLinkStatus : workflowLinkStatusList) {
                WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
                workflowLinkQueryParam.setVerifyStatus(workflowLinkStatus);
                ServiceResult<String, Page<WorkflowLink>> workflowLinkResult = workflowService.getWorkflowLinkPage(workflowLinkQueryParam);
                if (VerifyStatus.VERIFY_STATUS_COMMIT.equals(workflowLinkQueryParam.getVerifyStatus())) {
                    Map<String,Object> rejectMap = new HashMap();
                    rejectMap.put("params","verifyStatus");
                    rejectMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_COMMIT);
                    rejectMap.put("workbenchType",WorkbenchType.VERIFYING);
                    rejectMap.put("count",workflowLinkResult.getResult().getTotalCount());
                    MapList.add(rejectMap);
                }
                if (VerifyStatus.VERIFY_STATUS_BACK.equals(workflowLinkQueryParam.getVerifyStatus())) {
                    Map<String,Object> rejectMap = new HashMap();
                    rejectMap.put("params","verifyStatus");
                    rejectMap.put("paramsValue",VerifyStatus.VERIFY_STATUS_BACK);
                    rejectMap.put("workbenchType",WorkbenchType.REJECT);
                    rejectMap.put("count",workflowLinkResult.getResult().getTotalCount());
                    MapList.add(rejectMap);
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(MapList);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> queryWaitVerifyWorkflowLinkCount(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("workflowQueryParam", workflowLinkQueryParam);
        //只有审核人数据
        if (!userSupport.isSuperUser()) {
            paramMap.put("verifyUserId", userSupport.getCurrentUserId().toString());
        }

        Integer dataCount = workflowLinkMapper.workbenchListCount(paramMap);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(dataCount);
        return serviceResult;
    }


    @Override
    public ServiceResult<String, Integer> queryBankSlipDetailCount(BankSlipDetailQueryParam bankSlipDetailQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        Map<String, Object> maps = new HashMap<>();
        Integer departmentType = bankSlipSupport.departmentType();
        maps.put("bankSlipDetailQueryParam", bankSlipDetailQueryParam);
        maps.put("departmentType", departmentType);
        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
        maps.put("currentUser", userSupport.getCurrentUserId().toString());

        Integer totalCount = bankSlipDetailMapper.findBankSlipDetailDOCountByParams(maps);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(totalCount);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<Map<String, Object>>> queryStatementOrderCount( WorkbenchStatementOrderQueryParam  workbenchStatementOrderQueryParam) {
        ServiceResult<String, List<Map<String, Object>>> result = new ServiceResult<>();

        List<Map<String, Object>> list =  new ArrayList<>();
        if(CollectionUtil.isNotEmpty(workbenchStatementOrderQueryParam.getStatementOrderQueryParamList())){
            for (StatementOrderQueryParam statementOrderQueryParam : workbenchStatementOrderQueryParam.getStatementOrderQueryParamList()) {

                if(statementOrderQueryParam.getStatementOrderStatus() == null){
                    continue;
                }

                //预计支付时间提前七天查询
                statementOrderQueryParam.setStatementExpectPayStartTime(DateUtil.getDayByOffset(new Date(), -7));

                Map<String, Object> maps = new HashMap<>();
                maps.put("statementOrderQueryParam", statementOrderQueryParam);
                maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

                Integer totalCount = statementOrderMapper.listSaleCount(maps);

                if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementOrderQueryParam.getStatementOrderStatus())) {
                    Map<String,Object> rejectMap = new HashMap();
                    rejectMap.put("params","statementOrderStatus");
                    rejectMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
                    rejectMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_INIT);
                    rejectMap.put("count",totalCount);
                    list.add(rejectMap);
                }

                if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART.equals(statementOrderQueryParam.getStatementOrderStatus())) {
                    Map<String,Object> rejectMap = new HashMap();
                    rejectMap.put("params","statementOrderStatus");
                    rejectMap.put("paramsValue",StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
                    rejectMap.put("workbenchType",WorkbenchType.STATEMENT_ORDER_STATUS_SETTLED_PART);
                    rejectMap.put("count",totalCount);
                    list.add(rejectMap);
                }


            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(list);
        return result;
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


}

