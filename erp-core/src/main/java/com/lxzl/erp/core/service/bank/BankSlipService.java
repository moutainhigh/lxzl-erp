package com.lxzl.erp.core.service.bank;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:49 2018/3/20
 * @Modified By:
 */
public interface BankSlipService {

    ServiceResult<String, Page<BankSlip>> pageBankSlip(BankSlipQueryParam bankSlipQueryParam);

    ServiceResult<String, Page<BankSlipDetail>> pageBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam);

    /**
     * 保存对公流水
     *
     * @param : multipartFile
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/19 16:12
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String, String> saveBankSlip(BankSlip bankSlip) throws Exception;

    /**
     * 下推流水
     *
     * @param : bankSlip
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/22 15:09
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String, Integer> pushDownBankSlip(BankSlip bankSlip);

    /**
     * 忽略流水项
     *
     * @param : bankSlipDetail
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/22 16:13
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    ServiceResult<String, Integer> ignoreBankSlipDetail(BankSlipDetail bankSlipDetail);

    /**
     * 认领流水项
     *
     * @param : bankSlipDetail
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/22 15:09
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String, Integer> claimBankSlipDetail(BankSlipClaim bankSlipClaim);

    /**
     * 确认银行对公流水
     *
     * @param : bankSlip
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/23 10:58
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    ServiceResult<String, Integer> confirmBankSlip(BankSlip bankSlip);
    /**
    * 查询银行流水项
    * @Author : XiaoLuYu
    * @Date : Created in 2018/3/31 10:55
    * @param : bankSlipDetail
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
    */
    ServiceResult<String, BankSlipDetail> queryBankSlipDetail(BankSlipDetail bankSlipDetail);
}
