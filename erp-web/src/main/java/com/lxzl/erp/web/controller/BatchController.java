package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.BatchCreateK3ReturnOrderStatementParam;
import com.lxzl.erp.common.domain.statement.BatchReturnDepositParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.batch.BatchService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/batch")
@Controller
@ControllerLog
public class BatchController {

    @Autowired
    private BatchService batchService;
    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "batchCreateK3ReturnOrderStatement", method = RequestMethod.POST)
    public Result batchCreateK3ReturnOrderStatement(@RequestBody BatchCreateK3ReturnOrderStatementParam batchCreateK3ReturnOrderStatementParam) {
        return resultGenerator.generate(batchService.batchCreateK3ReturnOrderStatement(batchCreateK3ReturnOrderStatementParam.getReturnOrderNoList()));
    }

    @RequestMapping(value = "batchReturnDeposit", method = RequestMethod.POST)
    public Result batchReturnDeposit(@RequestBody BatchReturnDepositParam param) {
        ServiceResult<String, String> serviceResult = batchService.batchReturnDeposit(param.getOrderNoList());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
