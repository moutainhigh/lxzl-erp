package com.lxzl.erp.core.service.bank.impl.importSlip;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/3/21
 * @Time : Created in 10:18
 */
@Repository
public class ImportPingAnBank {
    /**
     * 保存平安银行
     *
     * @param : runningWater
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/19 17:50
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    public ServiceResult<String, BankSlipDO> savePingAnBank(BankSlip bankSlip, InputStream inputStream) throws Exception {
        ServiceResult<String, BankSlipDO> serviceResult = new ServiceResult<>();
        BankSlipDO bankSlipDO = null;
        String excelUrl = bankSlip.getExcelUrl();
        try {
            Workbook work = null;
            String fileType = excelUrl.substring(excelUrl.lastIndexOf("."));
            if (".xlsx".equals(fileType)) {
                work = new XSSFWorkbook(inputStream);
            } else if (".xls".equals(fileType)) {
                work = new HSSFWorkbook(inputStream);
            }

            if (null == work) {
                serviceResult.setErrorCode(ErrorCode.EXCEL_SHEET_IS_NULL);
                return serviceResult;
            }

            Sheet sheet = null;
            Row row = null;
            Cell cell = null;
            Date now = new Date();

            //遍历Excel中所有的sheet
            sheet = work.getSheetAt(0);
            if (sheet == null) {
                serviceResult.setErrorCode(ErrorCode.EXCEL_SHEET_IS_NULL);
                return serviceResult;
            }
            //遍历当前sheet中的所有行

            SubCompanyDO subCompanyDO = subCompanyMapper.findById(bankSlip.getSubCompanyId());

            bankSlipDO = ConverterUtil.convert(bankSlip, BankSlipDO.class);

            //todo 存储
            ServiceResult<String, List<BankSlipDetailDO>> data = getPingAnBankData(sheet, row, cell, bankSlipDO, now);
            if (!ErrorCode.SUCCESS.equals(data.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(data.getErrorCode());
                return serviceResult;
            }
            List<BankSlipDetailDO> bankSlipDetailDOList = data.getResult();

            //保存  银行对公流水表
            bankSlipDO.setSubCompanyName(subCompanyDO.getSubCompanyName());

            bankSlipDO.setSlipStatus(SlipStatus.INITIALIZE);
            bankSlipDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
            bankSlipDO.setClaimCount(CommonConstant.COMMON_ZERO);
            bankSlipDO.setConfirmCount(CommonConstant.COMMON_ZERO);
            bankSlipDO.setCreateTime(now);
            bankSlipDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipDO.setUpdateTime(now);
            bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipMapper.save(bankSlipDO);
            //查看是否为空
            if (CollectionUtil.isEmpty(bankSlipDetailDOList)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(ErrorCode.EXCEL_SHEET_IS_NULL);
                return serviceResult;
            }
            //保存  银行对公流水明细表
            Integer isLocalization = null;
            if (CommonConstant.HEADER_COMPANY_ID.equals(bankSlipDO.getSubCompanyId())) {
                isLocalization = CommonConstant.COMMON_CONSTANT_NO;
            }
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                bankSlipDetailDO.setBankSlipId(bankSlipDO.getId());
                if (CommonConstant.HEADER_COMPANY_ID.equals(bankSlipDO.getSubCompanyId())) {
                    bankSlipDetailDO.setIsLocalization(isLocalization);
                }
                bankSlipDetailDO.setSubCompanyId(bankSlipDO.getSubCompanyId());
            }
            bankSlipDetailMapper.saveBankSlipDetailDOList(bankSlipDetailDOList);
            bankSlipDO.setBankSlipDetailDOList(bankSlipDetailDOList);
        } catch (IOException e) {
            logger.error("导入excel的IO流转换发生异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(ErrorCode.INPUT_STREAM_READER_IS_FAIL);
            return serviceResult;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                logger.error("关闭excel的IO流转换发生异常", e);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDO);
        return serviceResult;

    }

    //存平安银行数据

    public ServiceResult<String, List<BankSlipDetailDO>> getPingAnBankData(Sheet sheet, Row row, Cell cell, BankSlipDO bankSlipDO, Date now) throws Exception {

        ServiceResult<String, List<BankSlipDetailDO>> serviceResult = new ServiceResult<>();

        BankSlipDetailDO bankSlipDetailDO = null;

        String selectAccount = null; //查询账号[ Inquirer account number ]
        int inCount = 0; //进款笔数

        int payerNameNo = 0; //对方账户名称
        int payTimeNo = 0; //交易日期
        int payMoneyNo = 0; //贷
        int paySerialNumberNo = 0; //交易流水号
        int payPostscriptNo = 0; //用途
        int payAccountNo = 0; //付款人账号[ Debit Account No. ]
        int creditSumNo = 0; //借
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
                    String value = getValue(cell);

                    value = value == null ? "" : value;
                    value = value.trim();

                    if (("贷".equals(value)) ||
                            ("借".equals(value)) ||
                            ("查询账号[ Inquirer account number ]".equals(value)) ||
                            ("交易流水号".equals(value)) ||
                            ("对方账户".equals(value)) ||
                            ("用途".equals(value)) ||
                            ("交易日期".equals(value)) ||
                            ("对方账户名称".equals(value))) {
                        if ("查询账号[ Inquirer account number ]".equals(value)) {

                            Cell accountCell = (row.getCell(y + 1));
                            if (accountCell == null) {
                                continue ccc;
                            }
                            value = getValue(accountCell);

                            selectAccount = value;
                            continue ccc;
                        }
                        if ("对方账户名称".equals(value)) {
                            next = j;
                            payerNameNo = y;
                            continue ccc;
                        }
                        if ("借".equals(value)) {
                            creditSumNo = y;
                            continue ccc;
                        }
                        if ("交易日期".equals(value)) {
                            payTimeNo = y;
                            continue ccc;
                        }
                        if ("贷".equals(value)) {
                            payMoneyNo = y;
                            continue ccc;
                        }
                        if ("交易流水号".equals(value)) {
                            paySerialNumberNo = y;
                            continue ccc;
                        }
                        if ("对方账户".equals(value)) {
                            payAccountNo = y;
                            continue ccc;
                        }
                        if ("用途".equals(value)) {
                            payPostscriptNo = y;
                            //下一行开始存数据
                            continue ccc;
                        }
                    }
                }
                // 以下可以直接存数据
                String payerName = null;  //对方账户名称
                String tradeTime = null;  //交易日期
                String tradeAmount = null;  //贷
                String tradeSerialNo = null;  //交易流水号
                String otherSideAccountNo = null;  //付款人账号[ Debit Account No. ]
                String tradeMessage = null;  //用途
                String tradeAmount1 = null;  //借

                if (j > next) {

                    if (payerNameNo != 6 || payTimeNo != 0 || payMoneyNo != 3 || paySerialNumberNo != 7 || payPostscriptNo != 10 || payAccountNo != 5 || creditSumNo != 2) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IMPORT_FAIL);
                        return serviceResult;
                    }
                    bankSlipDetailDOListIsEmpty = false;
                    Cell payPostscriptCell = row.getCell(payPostscriptNo);
                    if (payPostscriptCell != null) {
                        tradeMessage = (payPostscriptCell == null ? "" : getValue(payPostscriptCell).replaceAll("\\s+", ""));  //用途
                    }
                    payerName = (row.getCell(payerNameNo) == null ? "" : getValue(row.getCell(payerNameNo)).replaceAll("\\s+", ""));  //对方账户名称
                    tradeTime = (row.getCell(payTimeNo) == null ? "" : getValue(row.getCell(payTimeNo)).replaceAll("\\s+", ""));  //交易日期
                    tradeAmount = (row.getCell(payMoneyNo) == null ? "" : getValue(row.getCell(payMoneyNo)).replaceAll("\\s+", ""));  //贷
                    tradeAmount1 = (row.getCell(creditSumNo) == null ? "" : getValue(row.getCell(creditSumNo)).replaceAll("\\s+", ""));  //贷方发生额
                    tradeSerialNo = (row.getCell(paySerialNumberNo) == null ? "" : getValue(row.getCell(paySerialNumberNo)).replaceAll("\\s+", ""));  //交易流水号
                    otherSideAccountNo = (row.getCell(payAccountNo) == null ? "" : getValue(row.getCell(payAccountNo)).replaceAll("\\s+", ""));  //对方账号

                    if ("".equals(payerName) &&
                            "".equals(tradeTime) &&
                            "".equals(tradeAmount) &&
                            "".equals(tradeAmount1) &&
                            "".equals(tradeSerialNo) &&
                            "".equals(otherSideAccountNo)) {
                        continue bbb;
                    }
                    bankSlipDetailDO = new BankSlipDetailDO();
                    try {
                        if (tradeAmount.contains(",")) {
                            tradeAmount = tradeAmount.replaceAll(",", "");
                        }
                        if (tradeAmount1.contains(",")) {
                            tradeAmount1 = tradeAmount1.replaceAll(",", "");
                        }
                        if (tradeAmount1 != null && !("".equals(tradeAmount1))) {
                            bankSlipDetailDO.setTradeAmount(new BigDecimal(tradeAmount1));
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
                        bankSlipDetailDO.setTradeTime(new SimpleDateFormat("yyyyMMdd").parse(tradeTime));
                    } catch (Exception e) {
                        logger.error("-----------------交易日期转换出错------------------------", e);
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

    //toString重写
    private String getValue(Cell cell) {
        if (cell == null) {

        }
        switch (cell.getCellType()) {
            case 0:
                if (DateUtil.isCellDateFormatted(cell)) {
                    DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    return sdf.format(cell.getDateCellValue());
                }
                double value = cell.getNumericCellValue();
                if (value > 10000000) {
                    DecimalFormat decimalFormat = new DecimalFormat("##0");//格式化设置
                    return decimalFormat.format(value);
                } else {
                    return value + "";
                }


            case 1:
                return cell.getRichStringCellValue().toString();
            case 2:
                return cell.getCellFormula();
            case 3:
                return "";
            case 4:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case 5:
                return ErrorEval.getText(cell.getErrorCellValue());
            default:
                return "Unknown Cell Type: " + cell.getCellType();
        }
    }

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private BankSlipDetailMapper bankSlipDetailMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private BankSlipMapper bankSlipMapper;
}
