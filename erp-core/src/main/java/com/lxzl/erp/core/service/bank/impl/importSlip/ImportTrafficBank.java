package com.lxzl.erp.core.service.bank.impl.importSlip;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.ByteArrayInputStream;
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
 * @Date : Created in 2018/3/20
 * @Time : Created in 19:31
 */
@Repository
public class ImportTrafficBank {
    /**
     * 成都交通银行数据导入
     *
     * @param : bankSlip
     * @param : inputStream
     * @Author : XiaoLuYu
     * @Date : Created in 2018/3/20 19:21
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>
     */

    public ServiceResult<String, BankSlipDO> saveTrafficBank(BankSlip bankSlip, InputStream inputStream) throws Exception {
        ServiceResult<String, BankSlipDO> serviceResult = new ServiceResult<>();
        BankSlipDO bankSlipDO = null;
        String excelUrl = bankSlip.getExcelUrl();
        try {
            Workbook work = WorkbookFactory.create(inputStream);

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
            ServiceResult<String, List<BankSlipDetailDO>> data = getTrafficBankData(sheet, row, cell, bankSlipDO, now);
            if (!ErrorCode.SUCCESS.equals(data.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回
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

            bankSlipDO = bankSlipSupport.formatBankSlipDetail(bankSlipDO, bankSlipDetailDOList);
            if (bankSlipDO == null) {
                serviceResult.setErrorCode(ErrorCode.IMPORT_BANK_SLIP_DETAILS_IS_EXIST);
                return serviceResult;
            }
            bankSlipDetailDOList = bankSlipDO.getBankSlipDetailDOList();
            //查看是否为空
            if (CollectionUtil.isEmpty(bankSlipDetailDOList)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(ErrorCode.EXCEL_SHEET_IS_NULL);
                return serviceResult;
            }
            //保存  银行对公流水明细表
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                bankSlipDetailDO.setBankSlipId(bankSlipDO.getId());
                bankSlipDetailDO.setIsLocalization(CommonConstant.COMMON_CONSTANT_NO);
                bankSlipDetailDO.setSubCompanyId(bankSlipDO.getSubCompanyId());
            }
            bankSlipDetailMapper.saveBankSlipDetailDOList(bankSlipDetailDOList);
            bankSlipDO.setBankSlipDetailDOList(bankSlipDetailDOList);
        } catch (IOException e) {
            logger.error("导入转换发生异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(ErrorCode.INPUT_STREAM_READER_IS_FAIL);
            return serviceResult;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                logger.error("关闭转换发生异常", e);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDO);
        return serviceResult;

    }


    //存中国银行数据

    public ServiceResult<String, List<BankSlipDetailDO>> getTrafficBankData(Sheet sheet, Row row, Cell cell, BankSlipDO bankSlipDO, Date now) throws Exception {

        ServiceResult<String, List<BankSlipDetailDO>> serviceResult = new ServiceResult<>();

        BankSlipDetailDO bankSlipDetailDO = null;

        String selectAccount = null; //查询账号[ Inquirer account number ]
        int inCount = 0; //进款笔数

        int payerNameNo = 0; //对方户名
        int payTimeNo = 0; //交易时间
        int payMoneyNo = 0; //发生额
        int paySerialNumberNo = 0; //核心流水号
        int payPostscriptNo = 0; //摘要
        int payAccountNo = 0; //对方账号
        int borrowingMarksNo = 0; //借贷标志
        List<BankSlipDetailDO> bankSlipDetailDOList = new ArrayList<BankSlipDetailDO>();
        boolean bankSlipDetailDOListIsEmpty = true;

        int next = Integer.MAX_VALUE;
        bbb:
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null) {
                continue bbb;
            }
            if ((row.getCell(0) == null ? "" : getValue(row.getCell(0))).contains("借方交易笔数:")) {
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
                    String value = getValue(cell);

                    value = value == null ? "" : value;
                    value = value.trim();

                    if (("发生额".equals(value)) ||
                            ("查询账号:".equals(value)) ||
                            ("核心流水号".equals(value)) ||
                            ("对方账号".equals(value)) ||
                            ("摘要".equals(value)) ||
                            ("交易时间".equals(value)) ||
                            ("对方户名".equals(value)) ||
                            ("借贷标志".equals(value))) {
                        if ("查询账号:".equals(value)) {

                            Cell accountCell = (row.getCell(y + 1));
                            if (accountCell == null) {
                                continue ccc;
                            }
                            value = getValue(accountCell);

                            selectAccount = value;
                            continue ccc;
                        }
                        if ("借贷标志".equals(value)) {
                            borrowingMarksNo = y;
                            continue ccc;
                        }
                        if ("对方户名".equals(value)) {
                            next = j;
                            payerNameNo = y;
                            continue ccc;
                        }
                        if ("交易时间".equals(value)) {
                            payTimeNo = y;
                            continue ccc;
                        }
                        if ("发生额".equals(value)) {
                            payMoneyNo = y;
                            continue ccc;
                        }
                        if ("摘要".equals(value)) {
                            payPostscriptNo = y;
                            continue ccc;
                        }
                        if ("对方账号".equals(value)) {
                            payAccountNo = y;
                            continue ccc;
                        }
                        if ("核心流水号".equals(value)) {//核心流水号
                            paySerialNumberNo = y;
                            //下一行开始存数据
                            continue ccc;
                        }
                    }
                }
                // todo 以下可以直接存数据
                String payerName = null;  //对方户名
                String tradeTime = null;  //交易时间
                String tradeAmount = null;  //发生额
                String tradeSerialNo = null;  //核心流水号
                String otherSideAccountNo = null;  //付款人账号[ Debit Account No. ]
                String tradeMessage = null;  //摘要

                if (j > next) {

                    if (payerNameNo != 9 || payTimeNo != 0 || payMoneyNo != 5 || paySerialNumberNo != 13 || payPostscriptNo != 1 || payAccountNo != 8 || borrowingMarksNo != 11) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IMPORT_FAIL);
                        return serviceResult;
                    }
                    bankSlipDetailDOListIsEmpty = false;
                    Cell payPostscriptCell = row.getCell(payPostscriptNo);
                    if (payPostscriptCell != null) {
                        tradeMessage = (payPostscriptCell == null ? "" : getValue(payPostscriptCell).replaceAll("\\s+", ""));  //摘要
                    }
                    payerName = (row.getCell(payerNameNo) == null ? "" : getValue(row.getCell(payerNameNo)).replaceAll("\\s+", "")); //对方户名
                    tradeTime = (row.getCell(payTimeNo) == null ? "" : getValue(row.getCell(payTimeNo)).replaceAll("\\s+", "")); //交易时间
                    tradeAmount = (row.getCell(payMoneyNo) == null ? "" : getValue(row.getCell(payMoneyNo)).replaceAll("\\s+", "")); //发生额
                    tradeSerialNo = (row.getCell(paySerialNumberNo) == null ? "" : getValue(row.getCell(paySerialNumberNo)).replaceAll("\\s+", "")); //核心流水号
                    otherSideAccountNo = (row.getCell(payAccountNo) == null ? "" : getValue(row.getCell(payAccountNo)).replaceAll("\\s+", "")); //付款人账号[ Debit Account No. ]

                    bankSlipDetailDO = new BankSlipDetailDO();
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
                    try {
                        bankSlipDetailDO.setTradeTime(new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse(tradeTime));
                    } catch (Exception e) {
                        try {
                            bankSlipDetailDO.setTradeTime(new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(tradeTime));
                        } catch (Exception e1) {
                            logger.error("-----------------入账时间转换出错------------------------", e1);
                            serviceResult.setErrorCode(ErrorCode.DATE_TRANSITION_IS_FAIL);
                            return serviceResult;
                        }
                    }

                    bankSlipDetailDO.setOtherSideAccountNo(otherSideAccountNo);
                    bankSlipDetailDO.setTradeSerialNo(tradeSerialNo);
                    bankSlipDetailDO.setPayerName(payerName);
                    bankSlipDetailDO.setTradeMessage(tradeMessage);
                    if ("贷".equals(getValue(row.getCell(borrowingMarksNo)).replaceAll("\\s+", ""))) {
                        bankSlipDetailDO.setLoanSign(LoanSignType.INCOME);
                        //进款比数
                        inCount = inCount + 1;
                    } else if ("借".equals(getValue(row.getCell(borrowingMarksNo)).replaceAll("\\s+", ""))) {
                        bankSlipDetailDO.setLoanSign(LoanSignType.EXPENDITURE);
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
                if (value > 1000000000) {
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
    @Autowired
    private BankSlipSupport bankSlipSupport;
}
