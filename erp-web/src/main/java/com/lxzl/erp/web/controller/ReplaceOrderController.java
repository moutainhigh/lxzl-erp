package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
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
    public Result add(@RequestBody @Validated ReplaceOrder replaceOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = replaceOrderService.add(replaceOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
