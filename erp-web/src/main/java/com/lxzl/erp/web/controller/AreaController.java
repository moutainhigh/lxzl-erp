package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.area.AreaProvince;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.area.AreaService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/area")
@Controller
@ControllerLog
public class AreaController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "getAreaList", method = RequestMethod.POST)
    public Result getAreaList() {
        ServiceResult<String, List<AreaProvince>> serviceResult = areaService.getAreaList();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
