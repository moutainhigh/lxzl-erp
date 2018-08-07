package com.lxzl.erp.core.service.bank.impl.importSlip;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailOperationLogMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailOperationLogDO;
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
 * @Description: 京东流水导入
 * @Date: Created in 2018\7\31 0031 18:25
 */
@Repository
public class ImportJingDong {
    public ServiceResult<String,List<BankSlipDetailDO>> getJingDongData(Sheet sheet, Row row, Cell cell, BankSlipDO bankSlipDO, Date now) {
        ServiceResult<String, List<BankSlipDetailDO>> serviceResult = new ServiceResult<>();

        BankSlipDetailDO bankSlipDetailDO = null;
        int inCount = 0; //进款笔数

        int payTimeNo = 0; //时间
        int customerOrderNo = 0; //商户订单号
        int tradeAmountNo = 0; //总金额
        int payerNameNo = 0; //客户
        int subCompanyNameNo = 0; //属地化公司
        int payPostscriptNo = 0; //交易附言
        List<BankSlipDetailDO> bankSlipDetailDOList = new ArrayList<BankSlipDetailDO>();
        boolean bankSlipDetailDOListIsEmpty = true;

        int next = Integer.MAX_VALUE;
        bbb:
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null) {
                continue bbb;
            }
            if (row.getCell(0) == null || row.getCell(0).toString() == "" ) {
                break bbb;
            }
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
                            ("商户订单号".equals(value)) ||
                            ("总金额".equals(value)) ||
                            ("客户".equals(value)) ||
                            ("属地化公司".equals(value)) ||
                            ("交易附言".equals(value))) {
                        if ("时间".equals(value)) {
                            payTimeNo = y;
                            continue ccc;
                        }
                        if ("商户订单号".equals(value)) {
                            customerOrderNo = y;
                            continue ccc;
                        }
                        if ("总金额".equals(value)) {
                            tradeAmountNo = y;
                            continue ccc;
                        }
                        if ("客户".equals(value)) {
                            payerNameNo = y;
                            continue ccc;
                        }
                        if ("属地化公司".equals(value)) {
                            subCompanyNameNo = y;
                            continue ccc;
                        }
                        if ("交易附言".equals(value)) {
                            next = j;
                            payPostscriptNo = y;
                            continue ccc;
                        }
                    }
                }
                // todo 以下可以直接存数据
                String tradeTime = null;  //时间
                String merchantOrderNo = null;  //商户订单号
                String tradeAmount = null;  //总金额
                String payerName = null;  //客户
                String subCompanyName = null; //属地化公司
                String tradeMessage = null;  //交易附言

                if (j > next) {

                    if (payTimeNo != 0 || customerOrderNo != 1 || tradeAmountNo != 2 || payerNameNo != 3 || subCompanyNameNo != 4 || payPostscriptNo != 5) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IMPORT_FAIL);
                        return serviceResult;
                    }
                    bankSlipDetailDOListIsEmpty = false;
                    Cell payPostscriptCell = row.getCell(payPostscriptNo);
                    //时间
                    tradeTime = (row.getCell(payTimeNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payTimeNo)).replaceAll("\\s+", ""));
                    //商户订单号
                    merchantOrderNo = (row.getCell(customerOrderNo) == null ? "" : BankSlipSupport.getValue(row.getCell(customerOrderNo)).replaceAll("\\s+", ""));
                    //总金额
                    tradeAmount = (row.getCell(tradeAmountNo) == null ? "" : BankSlipSupport.getValue(row.getCell(tradeAmountNo)).replaceAll("\\s+", ""));
                    //客户
                    payerName = (row.getCell(payerNameNo) == null ? "" : BankSlipSupport.getValue(row.getCell(payerNameNo)).replaceAll("\\s+", ""));
                    //属地化公司
                    subCompanyName = (row.getCell(subCompanyNameNo) == null ? "" : BankSlipSupport.getValue(row.getCell(subCompanyNameNo)).replaceAll("\\s+", ""));
                    //交易附言
                    if (payPostscriptCell != null) {
                        tradeMessage = (payPostscriptCell == null ? "" : BankSlipSupport.getValue(payPostscriptCell).replaceAll("\\s+", ""));
                    }
                    if ("".equals(tradeTime) &&
                            "".equals(merchantOrderNo) &&
                            "".equals(tradeAmount) &&
                            "".equals(payerName) &&
                            "".equals(subCompanyName) &&
                            "".equals(tradeMessage) ) {
                        continue bbb;
                    }
                    bankSlipDetailDO = new BankSlipDetailDO();
                    //属地化公司
                    switch(subCompanyName)
                    {
                        case "总公司" :
                            bankSlipDetailDO.setSubCompanyId(1);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "深圳" :
                            bankSlipDetailDO.setSubCompanyId(2);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "上海" :
                            bankSlipDetailDO.setSubCompanyId(3);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "北京" :
                            bankSlipDetailDO.setSubCompanyId(4);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "广州" :
                            bankSlipDetailDO.setSubCompanyId(5);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "南京" :
                            bankSlipDetailDO.setSubCompanyId(6);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "厦门" :
                            bankSlipDetailDO.setSubCompanyId(7);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "武汉" :
                            bankSlipDetailDO.setSubCompanyId(8);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "成都" :
                            bankSlipDetailDO.setSubCompanyId(9);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "电销" :
                            bankSlipDetailDO.setSubCompanyId(10);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                        case "渠道" :
                            bankSlipDetailDO.setSubCompanyId(11);
                            bankSlipDetailDO.setIsLocalization(1);
                            break;
                    }
                    //总金额
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
                    //时间
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
                    //客户
                    bankSlipDetailDO.setPayerName(payerName);
                    //交易附言
                    bankSlipDetailDO.setTradeMessage(tradeMessage);
                    //进款比数
                    inCount = inCount + 1;
                    //未认领
                    bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
                    //商户订单号
                    bankSlipDetailDO.setMerchantOrderNo(merchantOrderNo);
                    //借贷标志(京东全是1-贷（收入）)
                    bankSlipDetailDO.setLoanSign(1);
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

    @Autowired
    private BankSlipDetailOperationLogMapper bankSlipDetailOperationLogMapper;
}
