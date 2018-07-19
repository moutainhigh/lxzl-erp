package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.workbench.WorkbenchService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: Pengbinjie
 * @Description：
 * @Date: Created in 14:43 2018/7/16
 * @Modified By:
 */
@RequestMapping("workbench")
@Controller
@ControllerLog
public class WorkbenchController {
    /**
     * 查询审核中,到期未处理,待发货的订单
     *
     * @param
     * @return Result
     */
    @RequestMapping(value = "queryOrder", method = RequestMethod.POST)
    public Result queryOrder(@RequestBody OrderQueryParam orderQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = workbenchService.queryVerifingOrder(orderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询可续租的订单
     *
     * @param
     * @return Result
     */

    /**
     * 查询审核中，被驳回，处理中，未提交退货单
     * @param k3ReturnOrderQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryReturnOrder", method = RequestMethod.POST)
    public Result queryReturnOrder(@RequestBody K3ReturnOrderQueryParam k3ReturnOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = workbenchService.queryReturnOrder(k3ReturnOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询审核中，被驳回的客户
     * @param customerCompanyQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryCompanyCustomer", method = RequestMethod.POST)
    public Result queryCompanyCustomer(@RequestBody CustomerCompanyQueryParam customerCompanyQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = workbenchService.queryCompanyCustomer(customerCompanyQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询审核中，被驳回的个人客户
     * @param customerPersonQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryPersonCustomer", method = RequestMethod.POST)
    public Result queryPersonCustomer(@RequestBody CustomerPersonQueryParam customerPersonQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = workbenchService.queryPersonCustomer(customerPersonQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     *查询审核中，被驳回的工作流
     *
     */
    @RequestMapping(value = "queryWorkflow", method = RequestMethod.POST)
    public Result queryWorkflow(@RequestBody WorkflowLinkQueryParam workflowLinkQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = workbenchService.queryWorkflow(workflowLinkQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }





    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private WorkbenchService workbenchService;

}
