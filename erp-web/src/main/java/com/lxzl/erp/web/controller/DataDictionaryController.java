package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.DataDictionary;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.system.DataDictionaryService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@ControllerLog
@RequestMapping("/data")
public class DataDictionaryController extends BaseController {


    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @RequestMapping(value = "findDataByType", method = RequestMethod.POST)
    public Result findDataByType(@RequestBody DataDictionary dataDictionary, HttpServletRequest request) {
        ServiceResult<String, List<DataDictionary>> serviceResult = dataDictionaryService.findDataByType(dataDictionary.getDataType());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "addDictionary", method = RequestMethod.POST)
    public Result addDictionary(@RequestBody DataDictionary dataDictionary, HttpServletRequest request) {
        ServiceResult<String, Integer> serviceResult = dataDictionaryService.addDictionary(dataDictionary);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateDictionary", method = RequestMethod.POST)
    public Result updateDictionary(@RequestBody DataDictionary dataDictionary, HttpServletRequest request) {
        ServiceResult<String, Integer> serviceResult = dataDictionaryService.updateDictionary(dataDictionary);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
