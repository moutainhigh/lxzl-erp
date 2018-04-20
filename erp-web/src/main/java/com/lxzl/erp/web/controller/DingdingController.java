package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProduct;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.domain.ResultCode;
import com.lxzl.se.dataaccess.mysql.ddl.DdlConfig;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author daiqi
 * @create 2018-04-20 15:34
 */
@Controller
@ControllerLog
@RequestMapping("/dingding")
public class DingdingController extends BaseController {
    @Autowired
    private DingdingService dingdingService;
    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 增加组合商品
     */
    @RequestMapping(value = "bindDingDingUsers")
    public Result bindDingDingUsers() {
        ServiceResult<String, Object> serviceResult = dingdingService.bindDingDingUsers();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
