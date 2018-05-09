package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.printLog.pojo.PrintLog;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.printLog.PrintLogService;
import com.lxzl.erp.dataaccess.domain.printLog.PrintLogDO;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "updatePrintLog",method = RequestMethod.POST)
    public Result updatePrintLog(@RequestBody @Validated(UpdateGroup.class) PrintLog printLog, BindingResult validResult){
        ServiceResult<String,Integer> serviceResult = printLogService.updatePrintLog(printLog);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryPrintLog",method = RequestMethod.POST)
    public Result queryPrintLog(@RequestBody @Validated(QueryGroup.class) PrintLog printLog, BindingResult validResult){
        ServiceResult<String,PrintLogDO> serviceResult = printLogService.queryPrintLog(printLog);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
