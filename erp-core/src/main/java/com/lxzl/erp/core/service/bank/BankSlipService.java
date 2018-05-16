package com.lxzl.erp.core.service.bank;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailOperationLogQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetailOperationLog;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;

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
    /**
     * 删除银行流水
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/2 16:15
     * @param : bankSlip
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    ServiceResult<String,Integer> deleteBankSlip(BankSlip bankSlip);
    /**
     * 隐藏银行流水项
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/2 17:45
     * @param : bankSlipDetail
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    ServiceResult<String,Integer> hideBankSlipDetail(BankSlipDetail bankSlipDetail);
    /**
     * 显示银行流水项
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/2 17:45
     * @param : bankSlipDetail
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    ServiceResult<String,Integer> displayBankSlipDetail(BankSlipDetail bankSlipDetail);
    /**
     * 批量指派银行流水明细
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/16 17:31
     * @param : bankSlipDetail
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    ServiceResult<String,Integer> localizationBankSlipDetail(BankSlip bankSlip);
    /**
     * 取消指派银行流水明细
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/16 21:17
     * @param : bankSlipDetailList
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    ServiceResult<String, BankSlipDetailDO> cancelLocalizationBankSlipDetail(BankSlipDetail bankSlipDetail);

    /**
     * 查询历史认领数据
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/16 21:17
     * @param : bankSlipDetailList
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    ServiceResult<String,BankSlipDetail> queryBankSlipClaim(BankSlipDetail bankSlipDetail);
    /**
     * 未知银行流水详情
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/12 14:42
     * @param : bankSlipDetail
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> queryUnknownBankSlipDetail(BankSlipDetail bankSlipDetail);
    /**
     * 查询操作日志
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/15 14:34
     * @param : bankSlipDetailOperationLogQueryParam
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.Page<com.lxzl.erp.common.domain.bank.pojo.BankSlip>>
     */
    ServiceResult<String,Page<BankSlipDetailOperationLog>> pageBankSlipDetailOperationLog(BankSlipDetailOperationLogQueryParam bankSlipDetailOperationLogQueryParam);
    /**
     * 未知流水分页
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/15 21:37
     * @param : bankSlipDetailOperationLogQueryParam
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.Page<com.lxzl.erp.common.domain.bank.pojo.BankSlipDetailOperationLog>>
     */
    ServiceResult<String, Page<BankSlipDetail>> pageUnknownBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam);
    /**
     * 指派未知数据
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/15 23:08
     * @param : bankSlipDetail
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> unknownBankSlipDetail(BankSlipDetail bankSlipDetail);
}
