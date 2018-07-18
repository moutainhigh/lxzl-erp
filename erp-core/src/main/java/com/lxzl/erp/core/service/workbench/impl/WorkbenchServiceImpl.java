package com.lxzl.erp.core.service.workbench.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PermissionType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.k3.K3ReturnOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workbench.WorkbenchService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowVerifyUserGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:08 2018/7/16
 * @Modified By:
 */
@Service("workbenchService")
public class WorkbenchServiceImpl implements WorkbenchService{

    @Override
    public ServiceResult<String, Integer> queryVerifingOrder(OrderQueryParam orderQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        ServiceResult<String, Page<Order>> orderResult = orderService.queryAllOrder(orderQueryParam);
        serviceResult.setResult(orderResult.getResult().getTotalCount());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(serviceResult.getResult());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> queryReturnOrder(K3ReturnOrderQueryParam k3ReturnOrderQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        ServiceResult<String, Page<K3ReturnOrder>> k3ReturnOrderResult = k3ReturnOrderService.queryReturnOrder(k3ReturnOrderQueryParam);
        serviceResult.setResult(k3ReturnOrderResult.getResult().getTotalCount());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(serviceResult.getResult());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> queryCompanyCustomer(CustomerCompanyQueryParam customerCompanyQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        ServiceResult<String, Page<Customer>> customerCompanyResult = customerService.pageCustomerCompany(customerCompanyQueryParam);
        serviceResult.setResult(customerCompanyResult.getResult().getTotalCount());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(serviceResult.getResult());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> queryPersonCustomer(CustomerPersonQueryParam customerPersonQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        ServiceResult<String, Page<Customer>> customerPersonResult = customerService.pageCustomerPerson(customerPersonQueryParam);
        serviceResult.setResult(customerPersonResult.getResult().getTotalCount());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(serviceResult.getResult());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> queryWorkflow(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        ServiceResult<String, Page<WorkflowLink>> workflowLinkResult = workflowService.getWorkflowLinkPage(workflowLinkQueryParam);
        serviceResult.setResult(workflowLinkResult.getResult().getTotalCount());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(serviceResult.getResult());
        return serviceResult;
    }

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private K3ReturnOrderService k3ReturnOrderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private K3ReturnOrderMapper k3ReturnOrderMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;

    @Autowired
    private WorkflowVerifyUserGroupMapper workflowVerifyUserGroupMapper;
}

