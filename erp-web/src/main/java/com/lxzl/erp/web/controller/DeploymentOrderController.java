package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.deploymentOrder.CommitDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.DeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.deploymentOrder.ProcessDeploymentOrderParam;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.deploymentOrder.DeploymentOrderService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: 调拨单控制器
 *
 * @author gaochao
 * @date 2017-11-29 13:31
 */
@Controller
@ControllerLog
@RequestMapping("/deploymentOrder")
public class DeploymentOrderController extends BaseController {

    @Autowired
    private DeploymentOrderService deploymentOrderService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(@RequestBody DeploymentOrder deploymentOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = deploymentOrderService.createDeploymentOrder(deploymentOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody DeploymentOrder deploymentOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = deploymentOrderService.updateDeploymentOrder(deploymentOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public Result commit(@RequestBody CommitDeploymentOrderParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = deploymentOrderService.commitDeploymentOrder(param.getDeploymentOrderNo(), param.getVerifyUser(), param.getCommitRemark());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "process", method = RequestMethod.POST)
    public Result process(@RequestBody ProcessDeploymentOrderParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = deploymentOrderService.processDeploymentOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delivery", method = RequestMethod.POST)
    public Result delivery(@RequestBody ProcessDeploymentOrderParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = deploymentOrderService.deliveryDeploymentOrder(param.getDeploymentOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody ProcessDeploymentOrderParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = deploymentOrderService.cancelDeploymentOrder(param.getDeploymentOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    public Result confirm(@RequestBody ProcessDeploymentOrderParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = deploymentOrderService.confirmDeploymentOrder(param.getDeploymentOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryPage", method = RequestMethod.POST)
    public Result queryPage(@RequestBody DeploymentOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<DeploymentOrder>> serviceResult = deploymentOrderService.queryDeploymentOrderPage(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryDetail", method = RequestMethod.POST)
    public Result queryDetail(@RequestBody DeploymentOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, DeploymentOrder> serviceResult = deploymentOrderService.queryDeploymentOrderDetail(param.getDeploymentOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
