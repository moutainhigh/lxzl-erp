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
 * @Date : Created in 2018/3/20
 * @Time : Created in 19:24
 */
@Repository
public class ImportChinaBank {

    //存中国银行数据

    public ServiceResult<String, List<BankSlipDetailDO>> getChinaBankData(Sheet sheet, Row row, Cell cell, BankSlipDO bankSlipDO, Date now) throws Exception {

        ServiceResult<String, List<BankSlipDetailDO>> serviceResult = new ServiceResult<>();

        BankSlipDetailDO bankSlipDetailDO = null;

        String selectAccount = null; //查询账号[ Inquirer account number ]
        int inCount = 0; //进款笔数
        int claimCount = 0; //需认领笔数
        int payerNameNo = 0; //付款人名称[ Payer's Name ]
        int payTimeNo = 0; //交易日期[ Transaction Date ]
        int payMoneyNo = 0; //交易金额[ Trade Amount ]
        int paySerialNumberNo = 0; //交易流水号[ Transaction reference number ]
        int payPostscriptNo = 0; //交易附言[ Remark ]
        int payAccountNo = 0; //付款人账号[ Debit Account No. ]
        List<BankSlipDetailDO> bankSlipDetailDOList = new ArrayList<BankSlipDetailDO>();
        boolean bankSlipDetailDOListIsEmpty = true;

        int next = Integer.MAX_VALUE;
        bbb:
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null) {
                continue bbb;
            }

            boolean tradeAmountFlag = false;
            if (row != null) {
                //遍历所有的列
                ccc:
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    if (cell == null) {
                        continue ccc;
                    }
                    String value = BankSlipSupport.getValue(cell);

                    value = value == null ? "" : value;
                    value = value.trim();

                    if (("交易金额[ Trade Amount ]".equals(value)) ||
                            ("查询账号[ Inquirer account number ]".equals(value)) ||
                            ("交易流水号[ Transaction reference number ]".equals(value)) ||
                            ("付款人账号[ Debit Account No. ]".equals(value)) ||
                            ("交易附言[ Remark ]".equals(value)) ||
                            ("交易日期[ Transaction Date ]".equals(value)) ||
                            ("付款人名称[ Payer's Name ]".equals(value))) {
                        if ("查询账号[ Inquirer account number ]".equals(value)) {

                            Cell accountCell = (row.getCell(y + 1));
                            if (accountCell == null) {
                                continue ccc;
                            }
                            value = BankSlipSupport.getValue(accountCell);

                            selectAccount = value;
                            continue ccc;
                        }
                        if ("付款人名称[ Payer's Name ]".equals(value)) {
                            next = j;
                            payerNameNo = y;
                            continue ccc;
                        }
                        if ("交易日期[ Transaction Date ]".equals(value)) {
                            payTimeNo = y;
                            continue ccc;
                        }
                        if ("交易金额[ Trade Amount ]".equals(value)) {
                            payMoneyNo = y;
                            continue ccc;
                        }
                        if ("交易流水号[ Transaction reference number ]".equals(value)) {
                            paySerialNumberNo = y;
                            continue ccc;
                        }
                        if ("付款人账号[ Debit Account No. ]".equals(value)) {
                            payAccountNo = y;
                            continue ccc;
                        }
                        if ("交易附言[ Remark ]".equals(value)) {
                            payPostscriptNo = y;
                            //下一行开始存数据
                            continue ccc;
                        }
                    }
                }
                // todo 以下可以直接存数据
                String payerName = null;  //付款人名称[ Payer's Name ]
                String tradeTime = null;  //交易日期[ Transaction Date ]
                String tradeAmount = null;  //交易金额[ Trade Amount ]
                String tradeSerialNo = null;  //交易流水号[ Transaction reference number ]
                String otherSideAccountNo = null;  //付款人账号[ Debit Account No. ]
                String tradeMessage = null;  //交易附言[ Remark ]

                if (j > next) {

                    if (payerNameNo != 5 || payTimeNo != 10 || payMoneyNo != 13 || paySerialNumberNo != 17 || payPostscriptNo != 25 || payAccountNo != 4) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IMPORT_FAIL);
                        return serviceResult;
                    }
                    bankSlipDetailDOListIsEmpty = false;
                    Cell payPostscriptCell = row.getCell(payPostscriptNo);
                    if (payPostscriptCell != null) {
                        tradeMessage = (payPostscriptCell == null ? "" : BankSlipSupport.getValue(payPostscriptCell).replaceAll("\\s+", ""));  //交易附言[ Remark ]
                    }
                    payerName = (row.getCell(payerNameNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payerNameNo)).replaceAll("\\s+", ""));  //付款人名称[ Payer's Name ]
                    tradeTime = (row.getCell(payTimeNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payTimeNo)).replaceAll("\\s+", ""));  //交易日期[ Transaction Date ]
                    tradeAmount = (row.getCell(payMoneyNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payMoneyNo)).replaceAll("\\s+", ""));  //交易金额[ Trade Amount ]
                    tradeSerialNo = (row.getCell(paySerialNumberNo) == null ? "" : BankSlipSupport.getValue(row.getCell(paySerialNumberNo)).replaceAll("\\s+", ""));  //交易流水号[ Transaction reference number ]
                    otherSideAccountNo = (row.getCell(paySerialNumberNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payAccountNo)).replaceAll("\\s+", ""));  //付款人账号[ Debit Account No. ]

                    if ("".equals(payerName) &&
                            "".equals(tradeTime) &&
                            "".equals(tradeAmount) &&
                            "".equals(tradeSerialNo) &&
                            "".equals(otherSideAccountNo)) {
                        continue bbb;
                    }

                    bankSlipDetailDO = new BankSlipDetailDO();
                    try {
                        if (tradeAmount.contains(",")) {
                            tradeAmount = tradeAmount.replaceAll(",", "");
                        }
                        if (tradeAmount.contains("-")) {
                            tradeAmount = tradeAmount.replaceAll("-", "");
                            tradeAmountFlag = true;
                        }
                        bankSlipDetailDO.setTradeAmount(new BigDecimal(tradeAmount));
                    } catch (Exception e) {
                        logger.error("-----------------金额转换出错------------------------", e);
                        serviceResult.setErrorCode(ErrorCode.MONEY_TRANSITION_IS_FAIL);
                        return serviceResult;
                    }
                    try {
                        bankSlipDetailDO.setTradeTime(new SimpleDateFormat("yyyyMMdd").parse(tradeTime));
                    } catch (Exception e) {
                        logger.error("-----------------交易日期[ Transaction Date ]转换出错------------------------", e);
                        serviceResult.setErrorCode(ErrorCode.DATE_TRANSITION_IS_FAIL);
                        return serviceResult;
                    }
                    bankSlipDetailDO.setOtherSideAccountNo(otherSideAccountNo);
                    bankSlipDetailDO.setTradeSerialNo(tradeSerialNo);
                    bankSlipDetailDO.setPayerName(payerName);
                    bankSlipDetailDO.setTradeMessage(tradeMessage);
                    if (tradeAmountFlag) {
                        bankSlipDetailDO.setLoanSign(LoanSignType.EXPENDITURE);
                    } else {
                        bankSlipDetailDO.setLoanSign(LoanSignType.INCOME);
                        //进款比数
                        inCount = inCount + 1;
                    }
                    //保存明细状态
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
        bankSlipDO.setNeedClaimCount(inCount - claimCount);
        bankSlipDO.setClaimCount(claimCount);
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
