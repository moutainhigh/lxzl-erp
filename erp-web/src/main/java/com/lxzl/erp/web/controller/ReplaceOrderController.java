package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.replace.ReplaceOrderCommitParam;
import com.lxzl.erp.common.domain.replace.ReplaceOrderConfirmChangeParam;
import com.lxzl.erp.common.domain.replace.ReplaceOrderQueryParam;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.CancelGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.replace.ReplaceOrderService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\5 0005 9:56
 */
@RequestMapping("/replaceOrder")
@Controller
@ControllerLog
public class ReplaceOrderController {
    @Autowired
    private ResultGenerator resultGenerator;
    @Autowired
    private ReplaceOrderService replaceOrderService;

    /**
     * 创建换货单
     *
     * @param replaceOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated(AddGroup.class) ReplaceOrder replaceOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = replaceOrderService.add(replaceOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 修改换货单
     *
     * @param replaceOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated(AddGroup.class) ReplaceOrder replaceOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = replaceOrderService.update(replaceOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 换货取消接口
     *
     * @param replaceOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody @Validated(CancelGroup.class) ReplaceOrder replaceOrder, BindingResult validResult) {
        return resultGenerator.generate(replaceOrderService.cancel(replaceOrder));
    }

    /**
     * 分页查询换货单
     *
     * @param param
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryAllReplaceOrder", method = RequestMethod.POST)
    public Result queryAllReplaceOrder(@RequestBody ReplaceOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<ReplaceOrder>> serviceResult = replaceOrderService.queryAllReplaceOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 通过换货单编号查询换货单详情
     *
     * @param replaceOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryReplaceOrderByNo", method = RequestMethod.POST)
    public Result queryReplaceOrderByNo(@RequestBody ReplaceOrder replaceOrder, BindingResult validResult) {
        ServiceResult<String, ReplaceOrder> serviceResult = replaceOrderService.queryReplaceOrderByNo(replaceOrder.getReplaceOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 确认换货
     * @Author : sunzhipeng
     * @param replaceOrderConfirmChangeParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "confirmReplaceOrder", method = RequestMethod.POST)
    public Result confirmReplaceOrder(@RequestBody ReplaceOrderConfirmChangeParam replaceOrderConfirmChangeParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = replaceOrderService.confirmReplaceOrder(replaceOrderConfirmChangeParam);
        return resultGenerator.generate(serviceResult);
    }

//    /**
//     * 换货单发货回调
//     * @Author : sunzhipeng
//     * @param replaceOrder
//     * @param validResult
//     * @return
//     */
//    @RequestMapping(value = "replaceOrderDeliveryCallBack", method = RequestMethod.POST)
//    public Result replaceOrderDeliveryCallBack(@RequestBody  ReplaceOrder replaceOrder, BindingResult validResult) {
//        ServiceResult<String, String> serviceResult = replaceOrderService.replaceOrderDeliveryCallBack(replaceOrder);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }

    /**
     * 提交换货单
     * @Author : sunzhipeng
     * @param replaceOrderCommitParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "commitReplaceOrder", method = RequestMethod.POST)
    public Result commitReplaceOrder(@RequestBody ReplaceOrderCommitParam replaceOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = replaceOrderService.commitReplaceOrder(replaceOrderCommitParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 发送换货单到K3
     * @Author : sunzhipeng
     * @param replaceOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "sendToK3", method = RequestMethod.POST)
    public Result sendToK3(@RequestBody ReplaceOrder replaceOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = replaceOrderService.sendReplaceOrderToK3(replaceOrder.getReplaceOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 发送换货单到K3
     * @Author : sunzhipeng
     * @param replaceOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "sendReplaceOrderToK3", method = RequestMethod.POST)
    public Result sendReplaceOrderInfoToK3(@RequestBody ReplaceOrder replaceOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = replaceOrderService.sendReplaceOrderInfoToK3(replaceOrder.getReplaceOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 通过订单编号查询换货单列表
     * @param replaceOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryReplaceOrderListForOrderNo", method = RequestMethod.POST)
    public Result queryReplaceOrderListForOrderNo(@RequestBody ReplaceOrder replaceOrder, BindingResult validResult) {
        ServiceResult<String, List<ReplaceOrder>> serviceResult = replaceOrderService.queryReplaceOrderListForOrderNo(replaceOrder.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
