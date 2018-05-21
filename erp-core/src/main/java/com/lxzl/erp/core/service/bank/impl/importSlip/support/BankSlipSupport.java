package com.lxzl.erp.core.service.bank.impl.importSlip.support;

import com.lxzl.erp.common.constant.LoanSignType;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/14
 * @Time : Created in 21:12
 */
@Component
public class BankSlipSupport {
    @Autowired
    private BankSlipMapper bankSlipMapper;

    public BankSlipDO formatBankSlipDetail(BankSlipDO bankSlipDO, List<BankSlipDetailDO> bankSlipDetailDOList) {

        //查询出导入时间的所有本公司的所有导入数据
        List<BankSlipDO> dbBankSlipDOList = bankSlipMapper.findBySubCompanyIdAndBankType(bankSlipDO.getSubCompanyId(), bankSlipDO.getBankType());

        if(CollectionUtil.isEmpty(dbBankSlipDOList)){
            bankSlipMapper.save(bankSlipDO);
            bankSlipDO.setBankSlipDetailDOList(bankSlipDetailDOList);
            return bankSlipDO;
        }
        Map<String, BankSlipDetailDO> allSlipDetailDOMap = new HashMap<>();
        for (BankSlipDO slipDO : dbBankSlipDOList) {
            List<BankSlipDetailDO> slipDetailDOList = slipDO.getBankSlipDetailDOList();
            Map<String, BankSlipDetailDO> slipDetailDOMap = ListUtil.listToMap(slipDetailDOList, "tradeSerialNo");
            allSlipDetailDOMap.putAll(slipDetailDOMap);
        }

        //剩余要导入或者跟新数据
        int loanSignTypeAmount = 0;
        List<BankSlipDetailDO> newBankSlipDetailDOList = new ArrayList<>();
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                if (!allSlipDetailDOMap.containsKey(bankSlipDetailDO.getTradeSerialNo())) {
                    if (LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
                        loanSignTypeAmount = loanSignTypeAmount + 1;
                    }
                    newBankSlipDetailDOList.add(bankSlipDetailDO);
                }
            }
        //判断导入的数据是不是今天的  是就跟新 不是就新增
        BankSlipDO dbBankSlipDO = bankSlipMapper.findBySubCompanyIdAndDayAndBankType(bankSlipDO.getSubCompanyId(), bankSlipDO.getBankType(), bankSlipDO.getSlipDay());
        if (dbBankSlipDO != null) {
            //跟新
            dbBankSlipDO.setInCount(dbBankSlipDO.getInCount() + loanSignTypeAmount);
            dbBankSlipDO.setNeedClaimCount(dbBankSlipDO.getNeedClaimCount() + loanSignTypeAmount);
            bankSlipMapper.update(dbBankSlipDO);
            dbBankSlipDO.setBankSlipDetailDOList(newBankSlipDetailDOList);
            return dbBankSlipDO;
        }
        if(loanSignTypeAmount == 0){
            return null;
        }

        bankSlipDO.setInCount(loanSignTypeAmount);
        bankSlipDO.setNeedClaimCount(loanSignTypeAmount);
        bankSlipMapper.save(bankSlipDO);
        bankSlipDO.setBankSlipDetailDOList(newBankSlipDetailDOList);
        return bankSlipDO;

    }
}
