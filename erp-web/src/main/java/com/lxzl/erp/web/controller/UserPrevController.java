package com.lxzl.erp.web.controller;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.UserSysDataPrivilege;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.CancelGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.user.UserPrevService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/userPrev")
@Controller
@ControllerLog
public class UserPrevController extends BaseController {

    @Autowired
    private UserPrevService userPrevService;

    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 单个授权
     * @param userSysDataPrivilege
     * @param validResult
     * @return
     */

    @RequestMapping(value = "AddPrev", method = RequestMethod.POST)
    public Result AddPrev(@RequestBody @Validated({AddGroup.class}) UserSysDataPrivilege userSysDataPrivilege, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userPrevService.AddPrev(userSysDataPrivilege);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 批量授权
     */

    @RequestMapping(value = "BatchPrev", method = RequestMethod.POST)
    public Result BatchPrev(@RequestBody @Validated({AddGroup.class}) List<UserSysDataPrivilege> userSysDataPrivilege, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userPrevService.BatchPrev(userSysDataPrivilege);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 取消授权
     */
    @RequestMapping(value = "deleteUserPrev", method = RequestMethod.POST)
    public Result deleteUserPrev(@RequestBody @Validated({CancelGroup.class}) UserSysDataPrivilege userSysDataPrivilege, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = userPrevService.deleteUserPrev(userSysDataPrivilege);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
