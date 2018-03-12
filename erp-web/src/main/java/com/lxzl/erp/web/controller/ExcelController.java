package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.excel.ImportProductService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-03-13 1:15
 */
@Controller
@ControllerLog
@RequestMapping("/excel")
public class ExcelController extends BaseController {

    @Autowired
    private ImportProductService importProductService;

    @Autowired
    private ResultGenerator resultGenerator;


    @RequestMapping(value = "importAssemblyProduct", method = RequestMethod.POST)
    public Result importAssemblyProduct() throws Exception {
        String serviceResult = importProductService.importAssemblyProduct("E:\\数据20180312-22.xlsx");
        return resultGenerator.generate(serviceResult, serviceResult);
    }


    @RequestMapping(value = "importNodeProduct", method = RequestMethod.POST)
    public Result importNodeProduct() throws Exception {
        String serviceResult = importProductService.importNodeProduct("E:\\数据20180312-22.xlsx");
        return resultGenerator.generate(serviceResult, serviceResult);
    }


    @RequestMapping(value = "importAIOProduct", method = RequestMethod.POST)
    public Result importAIOProduct() throws Exception {
        String serviceResult = importProductService.importAIOProduct("E:\\数据20180312-22.xlsx");
        return resultGenerator.generate(serviceResult, serviceResult);
    }


    @RequestMapping(value = "importTVProduct", method = RequestMethod.POST)
    public Result importTVProduct() throws Exception {
        String serviceResult = importProductService.importTVProduct("E:\\数据20180312-22.xlsx");
        return resultGenerator.generate(serviceResult, serviceResult);
    }


    @RequestMapping(value = "importMonitorProduct", method = RequestMethod.POST)
    public Result importMonitorProduct() throws Exception {
        String serviceResult = importProductService.importMonitorProduct("E:\\数据20180312-22.xlsx");
        return resultGenerator.generate(serviceResult, serviceResult);
    }
}
