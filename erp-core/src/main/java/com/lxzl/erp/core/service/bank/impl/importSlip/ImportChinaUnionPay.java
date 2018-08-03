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
 * @Author: Sunzhipeng
 * @Description:银联流水导入
 * @Date: Created in 2018\7\31 0031 18:27
 */
@Repository
public class ImportChinaUnionPay {
    public ServiceResult<String,List<BankSlipDetailDO>> getChinaUnionPayData(Sheet sheet, Row row, Cell cell, BankSlipDO bankSlipDO, Date now) {
        ServiceResult<String, List<BankSlipDetailDO>> serviceResult = new ServiceResult<>();

        BankSlipDetailDO bankSlipDetailDO = null;

        int inCount = 0; //进款笔数

        int paySerialNumberNo = 0; //交易流水号
        int customerOrderNo = 0; //订单号
        int payTimeNo = 0; //交易时间
        int payMoneyNo = 0; //交易金额
        int payAccountNo = 0; //账号
        List<BankSlipDetailDO> bankSlipDetailDOList = new ArrayList<BankSlipDetailDO>();
        boolean bankSlipDetailDOListIsEmpty = true;

        int next = Integer.MAX_VALUE;
        bbb:
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null) {
                continue bbb;
            }
            if ((row.getCell(0) == null ? "" : BankSlipSupport.getValue(row.getCell(0))).contains("总笔数")) {
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

                    if (("交易流水号".equals(value)) ||
                            ("订单号".equals(value)) ||
                            ("交易时间".equals(value)) ||
                            ("交易金额".equals(value)) ||
                            ("账号".equals(value))) {
                        if ("交易流水号".equals(value)) {
                            paySerialNumberNo = y;
                            continue ccc;
                        }
                        if ("订单号".equals(value)) {
                            customerOrderNo = y;
                            continue ccc;
                        }
                        if ("交易时间".equals(value)) {
                            payTimeNo = y;
                            continue ccc;
                        }
                        if ("交易金额".equals(value)) {
                            payMoneyNo = y;
                            continue ccc;
                        }
                        if ("账号".equals(value)) {
                            next = j;
                            payAccountNo = y;
                            continue ccc;
                        }
                    }
                }
                // todo 以下可以直接存数据
                String tradeSerialNo = null;  //交易流水号
                String merchantOrderNo = null;  //订单号
                String tradeTime = null;  //交易时间
                String tradeAmount = null;  //交易金额
                String otherSideAccountNo = null;  //账号

                if (j > next) {

                    if (paySerialNumberNo != 0 || customerOrderNo != 1 || payTimeNo != 2 || payMoneyNo != 5 || payAccountNo != 14 ) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IMPORT_FAIL);
                        return serviceResult;
                    }
                    bankSlipDetailDOListIsEmpty = false;
                    //交易流水号
                    tradeSerialNo = (row.getCell(paySerialNumberNo) == null ? "" : BankSlipSupport.getValue(row.getCell(paySerialNumberNo)).replaceAll("\\s+", ""));
                    //订单号
                    merchantOrderNo = (row.getCell(customerOrderNo) == null ? "" : BankSlipSupport.getValue(row.getCell(customerOrderNo)).replaceAll("\\s+", ""));
                    //交易时间
                    tradeTime = (row.getCell(payTimeNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payTimeNo)).replaceAll("\\s+", ""));
                    //交易金额
                    tradeAmount = (row.getCell(payMoneyNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payMoneyNo)).replaceAll("\\s+", ""));
                    //账号
                    otherSideAccountNo = (row.getCell(payAccountNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payAccountNo)).replaceAll("\\s+", ""));

                    if ("".equals(tradeSerialNo) &&
                            "".equals(merchantOrderNo) &&
                            "".equals(tradeTime) &&
                            "".equals(tradeAmount) &&
                            "".equals(otherSideAccountNo)) {
                        continue bbb;
                    }
                    bankSlipDetailDO = new BankSlipDetailDO();
                    //交易金额
                    try {
                        if (tradeAmount.contains(",")) {
                            tradeAmount = tradeAmount.replaceAll(",", "");
                        }
                        if (tradeAmount.contains("-")) {
                            tradeAmount = tradeAmount.replaceAll("-", "");
                        }

                        bankSlipDetailDO.setTradeAmount(new BigDecimal(tradeAmount));
                    } catch (Exception e) {
                        logger.error("-----------------金额转换出错------------------------", e);
                        serviceResult.setErrorCode(ErrorCode.MONEY_TRANSITION_IS_FAIL);
                        return serviceResult;
                    }
                    //交易时间
                    try {
                        bankSlipDetailDO.setTradeTime(new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(tradeTime));
                    } catch (Exception e) {
                        try {
                            bankSlipDetailDO.setTradeTime(new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(tradeTime));
                        } catch (Exception e1) {
                            logger.error("-----------------入账时间转换出错------------------------", e1);
                            serviceResult.setErrorCode(ErrorCode.DATE_TRANSITION_IS_FAIL);
                            return serviceResult;
                        }
                    }
                    //交易流水号
                    bankSlipDetailDO.setTradeSerialNo(tradeSerialNo);
                    //订单号
                    bankSlipDetailDO.setMerchantOrderNo(merchantOrderNo);
                    //账号
                    bankSlipDetailDO.setOtherSideAccountNo(otherSideAccountNo);
                    //进款比数
                    inCount = inCount + 1;
                    //借贷标志(银联全是1-贷（收入）)
                    bankSlipDetailDO.setLoanSign(1);
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
