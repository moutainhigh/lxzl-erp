package com.lxzl.erp.core.service.bank.impl.importSlip.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.LoanSignType;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        BankSlipDO dbBankSlipDO = bankSlipMapper.findBySubCompanyIdAndDayAndBankType(bankSlipDO.getSubCompanyId(), bankSlipDO.getSlipDay(), bankSlipDO.getBankType());
        List<BankSlipDetailDO> newBankSlipDetailDOList = new ArrayList<>();
        if(dbBankSlipDO != null){
            List<BankSlipDetailDO> dbBankSlipDetailDOList = dbBankSlipDO.getBankSlipDetailDOList();
            int loanSignTypeAmount = 0;
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                if (CollectionUtil.isNotEmpty(dbBankSlipDetailDOList)) {
                    boolean flag = true;
                    aaa:
                    for (BankSlipDetailDO dbBankSlipDetailDO : dbBankSlipDetailDOList) {
                        if (dbBankSlipDetailDO.equals(bankSlipDetailDO)) {
                            flag = false;
                            break aaa;
                        }
                    }
                    if (flag) {
                        if(LoanSignType.EXPENDITURE.equals(bankSlipDetailDO.getLoanSign())){
                            loanSignTypeAmount = loanSignTypeAmount+1;
                        }
                        newBankSlipDetailDOList.add(bankSlipDetailDO);
                    }
                } else {
                    newBankSlipDetailDOList.add(bankSlipDetailDO);
                }
            }
            dbBankSlipDO.setInCount(dbBankSlipDO.getInCount()+newBankSlipDetailDOList.size()-loanSignTypeAmount);
            dbBankSlipDO.setNeedClaimCount(dbBankSlipDO.getNeedClaimCount()+newBankSlipDetailDOList.size()-loanSignTypeAmount);
            bankSlipMapper.update(dbBankSlipDO);
            dbBankSlipDO.setBankSlipDetailDOList(newBankSlipDetailDOList);
            return dbBankSlipDO;
        }else {
            bankSlipMapper.save(bankSlipDO);
            bankSlipDO.setBankSlipDetailDOList(bankSlipDetailDOList);
            return bankSlipDO;
        }
    }

}
