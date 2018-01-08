package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.assembleOder.AssembleOrderQueryParam;
import com.lxzl.erp.common.domain.assembleOder.pojo.AssembleOrder;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.assembleOrder.AssembleOrderService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@RequestMapping(value = "assemble")
@Controller
@ControllerLog
public class AssembleOrderController {
    /**
     * 添加组装单
     * */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addAssembleOrder(@RequestBody @Validated(AddGroup.class) AssembleOrder assembleOrder, BindingResult validResult) {
        ServiceResult<String, Integer> result = assembleOrderService.addAssembleOrder(assembleOrder);
        return resultGenerator.generate(result);
    }
    /**
     * 查询组装单
     * */
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public Result queryAssembleOrderByAssembleOrderId(@RequestBody @Validated(IdGroup.class) AssembleOrder assembleOrder, BindingResult validResult) {
        ServiceResult<String, AssembleOrder> result = assembleOrderService.queryAssembleOrderByAssembleOrderId(assembleOrder.getAssembleOrderId());
        return resultGenerator.generate(result);
    }
    /**
     * 分页查询组装单
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result pageAssembleOrder(@RequestBody AssembleOrderQueryParam assembleOrderQueryParam) {
        ServiceResult<String, Page<AssembleOrder>> result = assembleOrderService.pageAssembleOrder(assembleOrderQueryParam);
        return resultGenerator.generate(result);
    }
    @Autowired
    private ResultGenerator resultGenerator;
    @Autowired
    private AssembleOrderService assembleOrderService;
}
