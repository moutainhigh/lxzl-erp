package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrder;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * 测试生成编号工具类
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/15
 * @Time : Created in 17:57
 */

@RequestMapping("generate")
@Controller
@ControllerLog
public class TestNoController {



    @RequestMapping(value = "createCustomerNo", method = RequestMethod.POST)
    public Result createTransferOrderInto(@RequestBody @Validated(AddGroup.class) TransferOrder transferOrder, BindingResult validResult) {
        String customerNo = generateNoSupport.generateCustomerNo(new Date(),1);
        return resultGenerator.generate(ErrorCode.SUCCESS, customerNo);
    }


    @Autowired
    ResultGenerator resultGenerator;
    @Autowired
    UserSupport userSupport;
    @Autowired
    GenerateNoSupport generateNoSupport;
}
