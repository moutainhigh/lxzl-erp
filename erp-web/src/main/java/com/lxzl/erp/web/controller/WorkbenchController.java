package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.workbench.WorkbenchQueryParam;
import com.lxzl.erp.common.domain.workbench.pojo.Workbench;
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

    @RequestMapping(value = "queryWorkbenchCount", method = RequestMethod.POST)
    public Result queryWorkbenchCount(@RequestBody WorkbenchQueryParam workbenchQueryParam, BindingResult validResult) {
        ServiceResult<String, Workbench> serviceResult = workbenchService.queryWorkbenchCount(workbenchQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryWorkhouseWorkbenchCount", method = RequestMethod.POST)
    public Result queryWorkhouseWorkbenchCount(@RequestBody WorkbenchQueryParam workbenchQueryParam, BindingResult validResult) {
        ServiceResult<String, Workbench> serviceResult = workbenchService.queryWorkhouseWorkbenchCount(workbenchQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private WorkbenchService workbenchService;

}
