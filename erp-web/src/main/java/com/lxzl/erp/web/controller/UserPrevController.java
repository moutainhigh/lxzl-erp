package com.lxzl.erp.web.controller;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.user.UserPrevService;
import com.lxzl.erp.dataaccess.domain.user.UserPrevDO;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/userPrev")
@Controller
@ControllerLog
public class UserPrevController extends BaseController {

    @Autowired
    private UserPrevService userPrevService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "insetUserPrev", method = RequestMethod.POST)
    public Result insetUserPrev(@RequestBody @Validated({ExtendGroup.class}) UserPrevDO userPrevDO, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userPrevService.insetUserPrev(userPrevDO);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
