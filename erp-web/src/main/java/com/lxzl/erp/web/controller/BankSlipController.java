package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.bank.ClaimBankSlipDetailGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: bengbinjie
 * @Description：
 * @Date: Created in 19:51 2018/3/20
 * @Modified By:Xiaoluyu
 */
@RequestMapping(value = "bankSlip")
@Controller
@ControllerLog
public class BankSlipController {

    @Autowired
    private BankSlipService bankSlipService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "pageBankSlip", method = RequestMethod.POST)
    public Result pageBankSlip(@RequestBody BankSlipQueryParam bankSlipQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<BankSlip>> serviceResult = bankSlipService.pageBankSlip(bankSlipQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageBankSlipDetail", method = RequestMethod.POST)
    public Result pageBankSlipDetail(@RequestBody BankSlipDetailQueryParam bankSlipDetailQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<BankSlipDetail>> serviceResult = bankSlipService.pageBankSlipDetail(bankSlipDetailQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "importExcel", method = RequestMethod.POST)
    public Result importBankSlip(@RequestBody @Validated(AddGroup.class) BankSlip bankSlip, BindingResult validated) throws Exception {
        ServiceResult<String, String> serviceResult = bankSlipService.saveBankSlip(bankSlip);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

    @RequestMapping(value = "pushDownBankSlip", method = RequestMethod.POST)
    public Result pushDownBankSlip(@RequestBody @Validated(IdGroup.class) BankSlip bankSlip, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.pushDownBankSlip(bankSlip);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }


    @RequestMapping(value = "ignoreBankSlipDetail", method = RequestMethod.POST)
    public Result ignoreBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlipDetail bankSlipDetail, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.ignoreBankSlipDetail(bankSlipDetail);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "claimBankSlipDetail", method = RequestMethod.POST)
    public Result claimBankSlipDetail(@RequestBody @Validated(ClaimBankSlipDetailGroup.class) BankSlipClaim bankSlipClaim, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.claimBankSlipDetail(bankSlipClaim);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "verifyBankSlipDetail", method = RequestMethod.POST)
    public Result verifyBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlip bankSlip, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.verifyBankSlipDetail(bankSlip);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }



}
