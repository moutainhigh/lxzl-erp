package com.lxzl.erp.core.service.bank.impl.importSlip;

import com.lxzl.erp.common.constant.BankSlipDetailStatus;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.LoanSignType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/25
 * @Time : Created in 18:57
 */
@Repository
public class ImportStockCash {

    //存库存现金数据

    public ServiceResult<String, List<BankSlipDetailDO>> getStockCashData(Sheet sheet, Row row, Cell cell, BankSlipDO bankSlipDO, Date now) throws Exception {

        ServiceResult<String, List<BankSlipDetailDO>> serviceResult = new ServiceResult<>();

        BankSlipDetailDO bankSlipDetailDO = null;

        String selectAccount = null; //查询账号[ Inquirer account number ]
        int inCount = 0; //进款笔数

        int payerNameNo = 0; //交易对象
        int payTimeNo = 0; //时间
        int payMoneyNo = 0; //收入
        int creditSumNo = 0; //支出
        int payPostscriptNo = 0; //摘要
        int accountTypeNo = 0; //账户类型
        int payAccountNo = 0; //账户号码
        List<BankSlipDetailDO> bankSlipDetailDOList = new ArrayList<BankSlipDetailDO>();
        boolean bankSlipDetailDOListIsEmpty = true;

        int next = Integer.MAX_VALUE;
        bbb:
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null) {
                continue bbb;
            }
            if ((row.getCell(0) == null ? "" : BankSlipSupport.getValue(row.getCell(0))).contains("借方交易笔数:")) {
                break bbb;
            }
            boolean tradeAmountFlag = false;
            if (row != null) {
                //遍历所有的列
                ccc:
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    if (cell == null) {
                        continue bbb;
                    }
                    String value = BankSlipSupport.getValue(cell);

                    value = value == null ? "" : value;
                    value = value.trim();

                    if (("时间".equals(value)) ||
                            ("账户类型".equals(value)) ||
                            ("摘要".equals(value)) ||
                            ("账户号码".equals(value)) ||
                            ("交易对象".equals(value)) ||
                            ("收入".equals(value)) ||
                            ("支出".equals(value))) {
                        if ("时间".equals(value)) {
                            payTimeNo = y;
                            continue ccc;
                        }
                        if ("账户类型".equals(value)) {
                            accountTypeNo = y;
                            continue ccc;
                        }
                        if ("摘要".equals(value)) {
                            payPostscriptNo = y;
                            continue ccc;
                        }
                        if ("交易对象".equals(value)) {
                            payerNameNo = y;
                            continue ccc;
                        }
                        if ("收入".equals(value)) {
                            payMoneyNo = y;
                            continue ccc;
                        }
                        if ("账户号码".equals(value)) {
                            payAccountNo = y;
                            continue ccc;
                        }
                        if ("支出".equals(value)) {
                            next = j;
                            creditSumNo = y;
                            continue ccc;
                        }
                    }
                }
                // todo 以下可以直接存数据
                String payerName = null;  //交易对象
                String tradeTime = null;  //时间
                String tradeAmount = null;  //收入
                String tradeMessage = null;  //摘要
                String creditSum = null;  //支出
                String accountType = null;  //账户类型
                String otherSideAccountNo = null;  //账户号码


                if (j > next) {

                    if (payerNameNo != 4 || payTimeNo != 0 || payMoneyNo != 5 || payPostscriptNo != 3 || creditSumNo != 6 || accountTypeNo != 1) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IMPORT_FAIL);
                        return serviceResult;
                    }
                    bankSlipDetailDOListIsEmpty = false;
                    Cell payPostscriptCell = row.getCell(payPostscriptNo);
                    if (payPostscriptCell != null) {
                        tradeMessage = (payPostscriptCell == null ? "" : BankSlipSupport.getValue(payPostscriptCell).replaceAll("\\s+", ""));  //摘要
                    }
                    payerName = (row.getCell(payerNameNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payerNameNo)).replaceAll("\\s+", "")); //交易对象
                    tradeTime = (row.getCell(payTimeNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payTimeNo)).replaceAll("\\s+", "")); //时间
                    tradeAmount = (row.getCell(payMoneyNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payMoneyNo)).replaceAll("\\s+", "")); //收入
                    creditSum = (row.getCell(creditSumNo) == null ? "" : BankSlipSupport.getValue(row.getCell(creditSumNo)).replaceAll("\\s+", "")); //支出
                    accountType = (row.getCell(accountTypeNo) == null ? "" : BankSlipSupport.getValue(row.getCell(accountTypeNo)).replaceAll("\\s+", "")); //账户类型
                    otherSideAccountNo = (row.getCell(payAccountNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payAccountNo)).replaceAll("\\s+", "")); //账户号码

                    if ("".equals(payerName) &&
                            "".equals(tradeTime) &&
                            "".equals(tradeAmount) &&
                            "".equals(creditSum) &&
                            "".equals(otherSideAccountNo) &&
                            "".equals(accountType) ) {
                        continue bbb;
                    }
                    if(!"库存现金".equals(accountType)){
                        serviceResult.setErrorCode(ErrorCode.BANK_TYPE_IS_FAIL);
                        return serviceResult;
                    }
                    bankSlipDetailDO = new BankSlipDetailDO();
                    try {
                        if (tradeAmount.contains(",")) {
                            tradeAmount = tradeAmount.replaceAll(",", "");
                        }
                        if (creditSum.contains(",")) {
                            creditSum = creditSum.replaceAll(",", "");
                        }
                        if (creditSum != null && !("".equals(creditSum))) {
                            bankSlipDetailDO.setTradeAmount(new BigDecimal(creditSum));
                            tradeAmountFlag = true;
                        } else {
                            bankSlipDetailDO.setTradeAmount(new BigDecimal(tradeAmount));
                        }
                    } catch (Exception e) {
                        logger.error("-----------------金额转换出错------------------------", e);
                        serviceResult.setErrorCode(ErrorCode.MONEY_TRANSITION_IS_FAIL);
                        return serviceResult;
                    }
                    try {
                        bankSlipDetailDO.setTradeTime(new SimpleDateFormat("yyyy/MM/dd").parse(tradeTime));
                    } catch (Exception e) {
                        try {
                            bankSlipDetailDO.setTradeTime(new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(tradeTime));
                        } catch (Exception e1) {
                            logger.error("-----------------入账时间转换出错------------------------", e1);
                            serviceResult.setErrorCode(ErrorCode.DATE_TRANSITION_IS_FAIL);
                            return serviceResult;
                        }
                    }

                    bankSlipDetailDO.setPayerName(payerName);
                    bankSlipDetailDO.setTradeMessage(tradeMessage);
                    if (tradeAmountFlag) {
                        bankSlipDetailDO.setLoanSign(LoanSignType.EXPENDITURE);
                    } else {
                        bankSlipDetailDO.setLoanSign(LoanSignType.INCOME);
                        //进款比数
                        inCount = inCount + 1;
                    }
                    bankSlipDetailDO.setOtherSideAccountNo(otherSideAccountNo);
                    bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
                    bankSlipDetailDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
                    bankSlipDetailDO.setCreateTime(now);
                    bankSlipDetailDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    bankSlipDetailDO.setUpdateTime(now);
                    bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                }
            }

            if (bankSlipDetailDO != null) {
                bankSlipDetailDOList.add(bankSlipDetailDO);
            }

        }
        bankSlipDO.setAccountNo(selectAccount); //保存查询账号
        bankSlipDO.setInCount(inCount);
        bankSlipDO.setNeedClaimCount(inCount);
        if (bankSlipDetailDOListIsEmpty && CollectionUtil.isEmpty(bankSlipDetailDOList)) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IMPORT_FAIL);
            return serviceResult;
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDetailDOList);
        return serviceResult;
    }

    
        private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

        @Autowired
        private UserSupport userSupport;
}
