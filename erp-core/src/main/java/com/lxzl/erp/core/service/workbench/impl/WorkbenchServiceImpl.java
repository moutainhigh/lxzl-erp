package com.lxzl.erp.core.service.workbench.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PermissionType;
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
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.k3.K3ReturnOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workbench.WorkbenchService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowVerifyUserGroupMapper;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
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
    public ServiceResult<String, Page<WorkflowLink>> queryWaitVerifyWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Page<WorkflowLink>> serviceResult = new ServiceResult<>();

        PageQuery pageQuery = new PageQuery(workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowQueryParam", workflowLinkQueryParam);
        //只有审核人数据
        if (!userSupport.isSuperUser()) {
            paramMap.put("verifyUserId", userSupport.getCurrentUserId().toString());
        }

        Integer dataCount = workflowLinkMapper.workbenchListCount(paramMap);
        List<WorkflowLinkDO> workflowLinkDOList = workflowLinkMapper.workbenchListPage(paramMap);

        Page<WorkflowLink> page = new Page<>(ConverterUtil.convertList(workflowLinkDOList, WorkflowLink.class), dataCount, workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(page);
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
    public ServiceResult<String, List<Map<String, Integer>>> queryStatementOrderCount(List<StatementOrderQueryParam>  statementOrderQueryParamList) {
        ServiceResult<String, List<Map<String, Integer>>> result = new ServiceResult<>();

        List<Map<String, Integer>> list = new ArrayList<>();
        if(CollectionUtil.isEmpty(statementOrderQueryParamList)){
            result.setErrorCode(ErrorCode.STATEMENT_ORDER_STATUS_IS_NULL);
            return result;
        }
        //判断是否都传了状态
        for (StatementOrderQueryParam statementOrderQueryParam : statementOrderQueryParamList) {
            if(statementOrderQueryParam.getStatementOrderStatus() == null){
                result.setErrorCode(ErrorCode.STATEMENT_ORDER_STATUS_IS_NULL);
                return result;
            }
        }

        for (StatementOrderQueryParam statementOrderQueryParam : statementOrderQueryParamList) {
            Map<String, Integer> map = new HashMap<>();
            //未分支付查询
            statementOrderQueryParam.setStatementExpectPayStartTime(DateUtil.getDayByOffset(new Date(), -7));

            Map<String, Object> maps = new HashMap<>();
            maps.put("statementOrderQueryParam", statementOrderQueryParam);
            maps.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));

            Integer totalCount = statementOrderMapper.listSaleCount(maps);

            map.put("statementOrderStatus",statementOrderQueryParam.getStatementOrderStatus());
            map.put("totalCount",totalCount);

            list.add(map);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(list);
        return result;
    }

    @Override
    public ServiceResult<String, Page<StatementOrder>> queryStatementOrderPage(StatementOrderQueryParam statementOrderQueryParam) {
        statementOrderQueryParam.setStatementExpectPayStartTime(DateUtil.getDayByOffset(new Date(), -7));
        return statementService.queryStatementOrder(statementOrderQueryParam);
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

    @Autowired
    private BankSlipDetailMapper bankSlipDetailMapper;

    @Autowired
    private BankSlipSupport bankSlipSupport;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementService statementService;

    @Autowired
    private PermissionSupport permissionSupport;
}

