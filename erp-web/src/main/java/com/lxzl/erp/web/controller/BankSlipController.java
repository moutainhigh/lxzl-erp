package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.exclt.ImportBankSlipService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/3/19
 * @Time : Created in 15:45
 */
@Controller
@ControllerLog
@RequestMapping("/bankSlip")
public class BankSlipController {
    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private ImportBankSlipService importToThePublicWaterService;
    @RequestMapping(value = "importExcel", method = RequestMethod.POST)
    public Result importBankSlip(@RequestBody @Validated(AddGroup.class) BankSlip bankSlip, BindingResult validated) throws Exception {

        ServiceResult<String, String> serviceResult = importToThePublicWaterService.saveBankSlip(bankSlip);
        return resultGenerator.generate(serviceResult.getErrorCode());

    }


}
