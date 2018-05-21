package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailOperationLogQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetailOperationLog;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.bank.AssignGroup;
import com.lxzl.erp.common.domain.validGroup.bank.ClaimBankSlipDetailGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
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

    @RequestMapping(value = "claimBankSlipDetail", method = RequestMethod.POST)
    public Result claimBankSlipDetail(@RequestBody @Validated(ClaimBankSlipDetailGroup.class) BankSlipClaim bankSlipClaim, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.claimBankSlipDetail(bankSlipClaim);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "confirmBankSlip", method = RequestMethod.POST)
    public Result verifyBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlip bankSlip, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.confirmBankSlip(bankSlip);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }


    @RequestMapping(value = "queryBankSlipDetail", method = RequestMethod.POST)
    public Result queryBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlipDetail bankSlipDetail, BindingResult validated) throws Exception {
        ServiceResult<String, BankSlipDetail> serviceResult = bankSlipService.queryBankSlipDetail(bankSlipDetail);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "deleteBankSlip", method = RequestMethod.POST)
    public Result deleteBankSlip(@RequestBody @Validated(IdGroup.class) BankSlip bankSlip, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.deleteBankSlip(bankSlip);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "hideBankSlipDetail", method = RequestMethod.POST)
    public Result hideBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlipDetail bankSlipDetail, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.hideBankSlipDetail(bankSlipDetail);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "displayBankSlipDetail", method = RequestMethod.POST)
    public Result displayBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlipDetail bankSlipDetail, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.displayBankSlipDetail(bankSlipDetail);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "localizationBankSlipDetail", method = RequestMethod.POST)
    public Result localizationBankSlipDetail(@RequestBody @Validated(AssignGroup.class) BankSlip bankSlip, BindingResult validated) throws Exception {
        ServiceResult<String, Integer> serviceResult = bankSlipService.localizationBankSlipDetail(bankSlip);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "cancelLocalizationBankSlipDetail", method = RequestMethod.POST)
    public Result cancelLocalizationBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlipDetail bankSlipDetail, BindingResult validated) throws Exception {
        ServiceResult<String, BankSlipDetailDO> serviceResult = bankSlipService.cancelLocalizationBankSlipDetail(bankSlipDetail);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "queryBankSlipClaim", method = RequestMethod.POST)
    public Result queryBankSlipClaim(@RequestBody @Validated(IdGroup.class) BankSlipDetail bankSlipDetail, BindingResult validated) throws Exception {
        ServiceResult<String, BankSlipDetail> serviceResult = bankSlipService.queryBankSlipClaim(bankSlipDetail);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "queryUnknownBankSlipDetail", method = RequestMethod.POST)
    public Result queryUnknownBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlipDetail bankSlipDetail, BindingResult validated) throws Exception {
        ServiceResult<String, BankSlipDetail> serviceResult = bankSlipService.queryUnknownBankSlipDetail(bankSlipDetail);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "pageBankSlipDetailOperationLog", method = RequestMethod.POST)
    public Result pageBankSlipDetailOperationLog(@RequestBody @Validated(QueryGroup.class) BankSlipDetailOperationLogQueryParam bankSlipDetailOperationLogQueryParam, BindingResult validated) throws Exception {
        ServiceResult<String, Page<BankSlipDetailOperationLog>> serviceResult = bankSlipService.pageBankSlipDetailOperationLog(bankSlipDetailOperationLogQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "pageUnknownBankSlipDetail", method = RequestMethod.POST)
    public Result pageUnknownBankSlipDetail(@RequestBody BankSlipDetailQueryParam bankSlipDetailQueryParam, BindingResult validated) throws Exception {
        ServiceResult<String, Page<BankSlipDetail>> serviceResult = bankSlipService.pageUnknownBankSlipDetail(bankSlipDetailQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

    @RequestMapping(value = "unknownBankSlipDetail", method = RequestMethod.POST)
    public Result unknownBankSlipDetail(@RequestBody @Validated(IdGroup.class) BankSlipDetail bankSlipDetail, BindingResult validated) throws Exception {
        ServiceResult<String, String> serviceResult = bankSlipService.unknownBankSlipDetail(bankSlipDetail);
        return resultGenerator.generate(serviceResult.getErrorCode(),serviceResult.getResult());
    }

}
