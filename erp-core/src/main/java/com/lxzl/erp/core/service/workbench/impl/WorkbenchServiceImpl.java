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
    public ServiceResult<String, Map<String,Integer>> queryVerifingOrder(WorkbenchOrderQueryParam workbenchOrderQueryParam) {
        ServiceResult<String, Map<String,Integer>> serviceResult = new ServiceResult<>();
        Map<String,Integer> resultMap = new HashMap();

        List<OrderQueryParam> orderQueryParamList = workbenchOrderQueryParam.getOrderQueryParamList();
        if (CollectionUtil.isNotEmpty(orderQueryParamList)){
            for (OrderQueryParam orderQueryParam: orderQueryParamList){
                ServiceResult<String, Page<Order>> orderResult = orderService.queryAllOrder(orderQueryParam);
                //审核中的订单
                if (OrderStatus.ORDER_STATUS_VERIFYING.equals(orderQueryParam.getOrderStatus())){
                    resultMap.put("verifyingOrder",orderResult.getResult().getTotalCount());
                }
                //待发货的订单
                if (OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderQueryParam.getOrderStatus())){
                    resultMap.put("waitDeliveryOrder_",orderResult.getResult().getTotalCount());
                }
                //到期未处理的订单
                if (CommonConstant.COMMON_CONSTANT_YES.equals(orderQueryParam.getIsReturnOverDue())){
                    resultMap.put("returnOverDueOrder",orderResult.getResult().getTotalCount());
                }
                //未支付的订单
                if (PayStatus.PAY_STATUS_INIT.equals(orderQueryParam.getPayStatus())){
                    resultMap.put("notPayOrder",orderResult.getResult().getTotalCount());
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(resultMap);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> queryCanReletOrder(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        Map<String,Object> maps = new HashMap<>();
        maps.put("orderQueryParam",orderQueryParam);
        maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_SERVICE, PermissionType.PERMISSION_TYPE_SUB_COMPANY_FOR_BUSINESS, PermissionType.PERMISSION_TYPE_USER));

        List<OrderDO> orderDOList = orderMapper.findOrderByOrderStatus(maps);
        List<Order> canReletOrderList = new ArrayList<>();
        for (OrderDO orderDO :orderDOList){
            Order order = ConverterUtil.convert(orderDO, Order.class);
            Integer canReletOrder = orderSupport.isOrderCanRelet(order);
            order.setCanReletOrder(canReletOrder);
            if (CommonConstant.COMMON_CONSTANT_YES.equals(order.getCanReletOrder())){
                canReletOrderList.add(order);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(canReletOrderList.size());
        return serviceResult;
    }

//    @Override
//    public ServiceResult<String, Page<Order>> queryCanReletOrderPage(OrderQueryParam orderQueryParam) {
//        ServiceResult<String, Page<Order>> serviceResult = new ServiceResult<>();
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
//        List<Order> pageOrderList = new ArrayList<>();
//        Integer startPage = orderQueryParam.getPageSize() * orderQueryParam.getPageNo() - 15 <= 0 ? 0:orderQueryParam.getPageSize() * orderQueryParam.getPageNo() - 15;
//        if (CollectionUtil.isNotEmpty(canReletOrderList)){
//            for (int i = startPage; i< orderQueryParam.getPageSize() * orderQueryParam.getPageNo() - 1; i++){
//                if (i <= canReletOrderList.size() - 1){
//                    if(canReletOrderList.get(i) != null){
//                        pageOrderList.add(canReletOrderList.get(i));
//                    }
//                }
//            }
//        }
//
//        Page<Order> page = new Page<>(pageOrderList, canReletOrderList.size(), orderQueryParam.getPageNo(), orderQueryParam.getPageSize());
//        serviceResult.setErrorCode(ErrorCode.SUCCESS);
//        serviceResult.setResult(page);
//        return serviceResult;
//    }

    @Override
    public ServiceResult<String, Map<String,Integer>> queryReturnOrder(WorkbenchReturnOrderQueryParam workbenchReturnOrderQueryParam) {
        ServiceResult<String, Map<String,Integer>> serviceResult = new ServiceResult<>();
        Map<String,Integer> resultMap = new HashMap();

        List<K3ReturnOrderQueryParam> k3ReturnOrderQueryParamList = workbenchReturnOrderQueryParam.getK3ReturnOrderQueryParamList();
        if (CollectionUtil.isNotEmpty(k3ReturnOrderQueryParamList)) {
            for (K3ReturnOrderQueryParam k3ReturnOrderQueryParam : k3ReturnOrderQueryParamList) {
                ServiceResult<String, Page<K3ReturnOrder>> k3ReturnOrderResult = k3ReturnOrderService.queryReturnOrder(k3ReturnOrderQueryParam);
                //未提交的退货单
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT.equals(k3ReturnOrderQueryParam.getReturnOrderStatus())) {
                    resultMap.put("waitCommitReturnOrder", k3ReturnOrderResult.getResult().getTotalCount());
                }
                //审核中的退货单
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING.equals(k3ReturnOrderQueryParam.getReturnOrderStatus())) {
                    resultMap.put("verifyingReturnOrder", k3ReturnOrderResult.getResult().getTotalCount());
                }
                //处理中的退货单
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING.equals(k3ReturnOrderQueryParam.getReturnOrderStatus())) {
                    resultMap.put("processingReturnOrder", k3ReturnOrderResult.getResult().getTotalCount());
                }
                //被驳回的退货单
                if (ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED.equals(k3ReturnOrderQueryParam.getReturnOrderStatus())) {
                    resultMap.put("backedReturnOrder", k3ReturnOrderResult.getResult().getTotalCount());
                }

            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(resultMap);
        return serviceResult;
    }

    @Override
    public ServiceResult<String,  Map<String,Integer>> queryCompanyCustomer(WorkbenchCompanyCustomerQueryParam workbenchCompanyCustomerQueryParam) {
        ServiceResult<String,  Map<String,Integer>> serviceResult = new ServiceResult<>();
        Map<String,Integer> resultMap = new HashMap();

        List<CustomerCompanyQueryParam> customerCompanyQueryParamList = workbenchCompanyCustomerQueryParam.getCustomerCompanyQueryParamList();
        if (CollectionUtil.isNotEmpty(customerCompanyQueryParamList)){
            for (CustomerCompanyQueryParam customerCompanyQueryParam : customerCompanyQueryParamList){
                ServiceResult<String, Page<Customer>> customerCompanyResult = customerService.pageCustomerCompany(customerCompanyQueryParam);
                if (CustomerStatus.STATUS_COMMIT.equals(customerCompanyQueryParam.getCustomerStatus())){
                    resultMap.put("verifyingCompanyCustomer",customerCompanyResult.getResult().getTotalCount());
                }
                if (CustomerStatus.STATUS_REJECT.equals(customerCompanyQueryParam.getCustomerStatus())){
                    resultMap.put("rejectCompanyCustomer",customerCompanyResult.getResult().getTotalCount());
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(resultMap);
        return serviceResult;
    }

    @Override
    public ServiceResult<String,  Map<String,Integer>> queryPersonCustomer(WorkbenchPersonCustomerQueryParam workbenchPersonCustomerQueryParam) {
        ServiceResult<String,  Map<String,Integer>> serviceResult = new ServiceResult<>();
        Map<String,Integer> resultMap = new HashMap();

        List<CustomerPersonQueryParam> customerPersonQueryParamList = workbenchPersonCustomerQueryParam.getCustomerPersonQueryParamList();
        if (CollectionUtil.isNotEmpty(customerPersonQueryParamList)){
            for (CustomerPersonQueryParam customerPersonQueryParam : customerPersonQueryParamList){
                ServiceResult<String, Page<Customer>> customerPersonResult = customerService.pageCustomerPerson(customerPersonQueryParam);
                if (CustomerStatus.STATUS_COMMIT.equals(customerPersonQueryParam.getCustomerStatus())){
                    resultMap.put("verifyingPersonCustomer",customerPersonResult.getResult().getTotalCount());
                }
                if (CustomerStatus.STATUS_REJECT.equals(customerPersonQueryParam.getCustomerStatus())){
                    resultMap.put("rejectPersonCustomer",customerPersonResult.getResult().getTotalCount());
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(resultMap);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Map<String,Integer>> queryWorkflow(WorkbenchWorkflowQueryParam workbenchWorkflowQueryParam) {
        ServiceResult<String, Map<String,Integer>> serviceResult = new ServiceResult<>();
        Map<String,Integer> resultMap = new HashMap();

        List<WorkflowLinkQueryParam> workflowLinkQueryParamList = workbenchWorkflowQueryParam.getWorkflowLinkQueryParamList();
        if (CollectionUtil.isNotEmpty(workflowLinkQueryParamList)){
            for(WorkflowLinkQueryParam workflowLinkQueryParam : workflowLinkQueryParamList) {
                ServiceResult<String, Page<WorkflowLink>> workflowLinkResult = workflowService.getWorkflowLinkPage(workflowLinkQueryParam);
                if (VerifyStatus.VERIFY_STATUS_COMMIT.equals(workflowLinkQueryParam.getVerifyStatus())) {
                    resultMap.put("verifingWorkflow", workflowLinkResult.getResult().getTotalCount());
                }
                if (VerifyStatus.VERIFY_STATUS_BACK.equals(workflowLinkQueryParam.getVerifyStatus())) {
                    resultMap.put("backedWorkflow", workflowLinkResult.getResult().getTotalCount());
                }
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(resultMap);
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
    public ServiceResult<String, Map<String, Integer>> queryStatementOrderCount(List<StatementOrderQueryParam>  statementOrderQueryParamList) {
        ServiceResult<String, Map<String, Integer>> result = new ServiceResult<>();

        Map<String, Integer> map = new HashMap<>();

        if(CollectionUtil.isNotEmpty(statementOrderQueryParamList)){
            for (StatementOrderQueryParam statementOrderQueryParam : statementOrderQueryParamList) {
                //未分支付查询
                statementOrderQueryParam.setStatementExpectPayStartTime(DateUtil.getDayByOffset(new Date(), -7));

                Map<String, Object> maps = new HashMap<>();
                maps.put("statementOrderQueryParam", statementOrderQueryParam);
                maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

                Integer totalCount = statementOrderMapper.listSaleCount(maps);

                if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementOrderQueryParam.getStatementOrderStatus())) {
                    map.put("unPaymentStatementOrder", totalCount);
                }else if(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART.equals(statementOrderQueryParam.getStatementOrderStatus())){
                    map.put("partialPaymentStatementOrder", totalCount);
                }
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(map);
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

    @Autowired
    private StatementService statementService;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

}

