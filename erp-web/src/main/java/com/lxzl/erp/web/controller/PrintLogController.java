package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.printLog.PrintLogPageParam;
import com.lxzl.erp.common.domain.printLog.pojo.PrintLog;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.printLog.PrintLogService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/4
 * @Time : Created in 11:13
 */
@RequestMapping("/print")
@ControllerLog
@Controller
public class PrintLogController {

    @Autowired
    private PrintLogService printLogService;
    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "savePrintLog",method = RequestMethod.POST)
    public Result savePrintLog(@RequestBody @Validated(AddGroup.class) PrintLog printLog, BindingResult validResult){
        ServiceResult<String,Integer> serviceResult = printLogService.savePrintLog(printLog);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryPrintLogCount",method = RequestMethod.POST)
    public Result queryPrintLogCount(@RequestBody @Validated(QueryGroup.class) PrintLogPageParam printLogPageParam, BindingResult validResult){
        ServiceResult<String,Map<String,Integer >> serviceResult = printLogService.queryPrintLogCount(printLogPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pagePrintLog",method = RequestMethod.POST)
    public Result pagePrintLog(@RequestBody @Validated(QueryGroup.class) PrintLogPageParam printLogPageParam, BindingResult validResult){
        ServiceResult<String, Page<PrintLog>> serviceResult = printLogService.pagePrintLog(printLogPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
