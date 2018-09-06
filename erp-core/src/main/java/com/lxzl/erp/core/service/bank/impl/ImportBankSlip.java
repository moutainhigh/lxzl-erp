package com.lxzl.erp.core.service.bank.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetailManualClaimAndLocalizationConfig;
import com.lxzl.erp.common.domain.bank.pojo.dto.BankSipAutomaticClaimDTO;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.common.util.StrReplaceUtil;
import com.lxzl.erp.core.service.bank.impl.importSlip.*;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.*;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.domain.bank.*;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.se.common.util.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/26
 * @Time : Created in 10:51
 */
@Component
public class ImportBankSlip {

    @Autowired
    private ImportTrafficBank importTrafficBank;

    @Autowired
    private ImportChinaBank importChinaBank;

    @Autowired
    private ImportNanJingBank importNanJingBank;

    @Autowired
    private ImportAgricultureBank importAgricultureBank;

    @Autowired
    private ImportICBCBank importICBCBank;

    @Autowired
    private ImportCCBBank importCCBBank;

    @Autowired
    private ImportPingAnBank importPingAnBank;

    @Autowired
    private ImportShangHaiPuDongDevelopmentBank importShanghaiPudongDevelopmentBank;

    @Autowired
    private ImportAlipay importAlipay;

    @Autowired
    private ImportCMBCBank importCMBCBank;

    @Autowired
    private ImportStockCash importStockCash;

    @Autowired
    private ImportHanKouBank importHanKouBank;

    @Autowired
    private BankSlipClaimMapper bankSlipClaimMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;

    @Autowired
    private BankSlipDetailMapper bankSlipDetailMapper;

    @Autowired
    private BankSlipDetailOperationLogMapper bankSlipDetailOperationLogMapper;

    @Autowired
    private BankSlipMapper bankSlipMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private BankSlipSupport bankSlipSupport;

    @Autowired
    private ImportJingDong importJingDong;

    @Autowired
    private ImportChinaUnionPay importChinaUnionPay;

    @Autowired
    private BankSlipDetailManualClaimAndLocalizationConfigMapper bankSlipDetailManualClaimAndLocalizationConfigMapper;

    @Autowired
    private CustomerMapper customerMapper;

    private static Logger logger = LoggerFactory.getLogger(ImportBankSlip.class);

    /**
     * 保存银行流水
     *
     * @param : bankSlip
     * @param : inputStream
     * @param : bankType
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 14:33
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.dataaccess.domain.bank.BankSlipDO>
     */
    public ServiceResult<String, BankSlipDO> saveBankSlip(BankSlip bankSlip, InputStream inputStream, Integer bankType) throws Exception {
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

            //获取数据
            ServiceResult<String, List<BankSlipDetailDO>> data = getBankSlipData(sheet, row, cell, bankSlipDO, now, bankType);
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
            bankSlipDO = bankSlipSupport.filterBankSlipDetail(bankSlipDO, bankSlipDetailDOList);
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
                if (BankType.JING_DONG.equals(bankSlipDO.getBankType())) {
                    bankSlipDetailDO.setBankSlipId(bankSlipDO.getId());
                    bankSlipDetailDO.setOtherSideAccountNo("");
                } else {
                    bankSlipDetailDO.setBankSlipId(bankSlipDO.getId());
                    bankSlipDetailDO.setIsLocalization(CommonConstant.COMMON_CONSTANT_NO);
                    bankSlipDetailDO.setSubCompanyId(bankSlipDO.getSubCompanyId());
                }
            }
            bankSlipDetailMapper.saveBankSlipDetailDOList(bankSlipDetailDOList);
            bankSlipDO.setBankSlipDetailDOList(bankSlipDetailDOList);
        } catch (Exception e) {
            logger.error("导入转换发生异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            serviceResult.setErrorCode(ErrorCode.INPUT_STREAM_READER_IS_FAIL);
            return serviceResult;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                logger.error("关闭转换发生异常", e);
            }
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDO);

        //自动认领和自动归属化
        automaticClaimAndLocalization(serviceResult);

        return serviceResult;

    }

    /**
     * 获取解析的数据
     *
     * @param : sheet
     * @param : row
     * @param : cell
     * @param : bankSlipDO
     * @param : now
     * @param : bankType
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 14:33
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.util.List<com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO>>
     */
    public ServiceResult<String, List<BankSlipDetailDO>> getBankSlipData(Sheet sheet, Row row, Cell cell, BankSlipDO bankSlipDO, Date now, Integer bankType) throws Exception {

        ServiceResult<String, List<BankSlipDetailDO>> result = null;
        if (BankType.BOC_BANK.equals(bankType)) {
            result = importChinaBank.getChinaBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.TRAFFIC_BANK.equals(bankType)) {
            result = importTrafficBank.getTrafficBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.NAN_JING_BANK.equals(bankType)) {
            result = importNanJingBank.getNanJingBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.AGRICULTURE_BANK.equals(bankType)) {
            result = importAgricultureBank.getAgricultureBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.ICBC_BANK.equals(bankType)) {
            result = importICBCBank.getICBCBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.CCB_BANK.equals(bankType)) {
            result = importCCBBank.getCCBBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.PING_AN_BANK.equals(bankType)) {
            result = importPingAnBank.getPingAnBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.CMBC_BANK.equals(bankType)) {
            result = importCMBCBank.getCMBCBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType)) {
            result = importShanghaiPudongDevelopmentBank.getShanghaiPudongDevelopmentBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.ALIPAY.equals(bankType)) {
            result = importAlipay.getAlipayData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.HAN_KOU_BANK.equals(bankType)) {
            result = importHanKouBank.getHanKouBankData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.STOCK_CASH.equals(bankType)) {
            result = importStockCash.getStockCashData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.JING_DONG.equals(bankType)) {//京东
            result = importJingDong.getJingDongData(sheet, row, cell, bankSlipDO, now);
        } else if (BankType.CHINA_UNION_PAY_TYPE.equals(bankType)) {//银联
            result = importChinaUnionPay.getChinaUnionPayData(sheet, row, cell, bankSlipDO, now);
        }
        return result;
    }

    /**
     * 自动认领和自动归属化
     *
     * @param : result
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 11:31
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void automaticClaimAndLocalization(ServiceResult<String, BankSlipDO> result) {
        Date now = new Date();
        if (ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            //跟新 需要认领数据
            BankSlipDO bankSlipDO = result.getResult();
            List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDO.getBankSlipDetailDOList();
            //查一边是否是 流入金额
            List<BankSlipDetailDO> newBankSlipDetailDOList = new ArrayList<>();

            //需要手动认领的所有付款人名称和对方账号
            List<BankSlipDetailManualClaimAndLocalizationConfigDO> bankSlipDetailManualClaimAndLocalizationConfigDOList = bankSlipDetailManualClaimAndLocalizationConfigMapper.findAllConfig();

            // 查询出导入时间的所有本公司的所有导入数据
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                if (LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
                    boolean manualClaimAndLocalizationFlag = true;
                    if (CollectionUtil.isNotEmpty(bankSlipDetailManualClaimAndLocalizationConfigDOList)) {
                        for (BankSlipDetailManualClaimAndLocalizationConfigDO bankSlipDetailManualClaimAndLocalizationConfigDO : bankSlipDetailManualClaimAndLocalizationConfigDOList) {
                            //付款人对方账号是否有相同的
                            if (StringUtil.isNotBlank(bankSlipDetailManualClaimAndLocalizationConfigDO.getOtherSideAccountNo())) {
                                if (bankSlipDetailManualClaimAndLocalizationConfigDO.getOtherSideAccountNo().equals(bankSlipDetailDO.getOtherSideAccountNo())) {
                                    manualClaimAndLocalizationFlag = false;
                                }
                            }
                            //付款人名称是否有相同的
                            if (StringUtil.isNotBlank(bankSlipDetailManualClaimAndLocalizationConfigDO.getPayerName())) {
                                if (bankSlipDetailManualClaimAndLocalizationConfigDO.getPayerName().equals(bankSlipDetailDO.getPayerName())) {
                                    manualClaimAndLocalizationFlag = false;
                                }
                            }
                        }
                    }
                    if (manualClaimAndLocalizationFlag) {
                        newBankSlipDetailDOList.add(bankSlipDetailDO);
                    }
                }
            }


            //查询一边所有本银行认领 只有一条认领数据的认领数据
            List<BankSlipClaimDO> dbBankSlipClaimDOList = bankSlipClaimMapper.findBankSlipClaimPaySuccess();

            //数据库的当前银行所有的已确认的银行流水项
            Map<String, BankSlipClaimDO> bankSlipClaimDOMap = ListUtil.listToMap(dbBankSlipClaimDOList, "otherSideAccountNo");

            //上传的当前银行所有的已确认的银行流水项
            Map<Integer, BankSlipDetailDO> newBankSlipDetailDOMap = ListUtil.listToMap(newBankSlipDetailDOList, "id");

            //认领数据批量跟新
            List<BankSlipClaimDO> bankSlipClaimDOList = new ArrayList<>();

            //没有认领的数据
            List<BankSlipDetailDO> lastBankSlipDetailDOList = new ArrayList<>();

            //第二步没有认领的数据
            List<BankSlipDetailDO> lastTwoBankSlipDetailDOList = new ArrayList<>();

            //没有认领的数据
            List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();

            Set<String> customerNoSet = new HashSet();
            for (BankSlipClaimDO dbBankSlipClaimDO : dbBankSlipClaimDOList) {
                String customerNo = dbBankSlipClaimDO.getCustomerNo();
                customerNoSet.add(customerNo);
            }

            Map<String, CustomerDO> customerNoMap = getCustomerNoMap(customerNoSet);

            //对公流水项批量跟新
            int claimCount = 0;
            for (Integer id : newBankSlipDetailDOMap.keySet()) {
                //如果是就创建一条认领数据
                BankSlipDetailDO bankSlipDetailDO = newBankSlipDetailDOMap.get(id);
                String otherSideAccountNo = bankSlipDetailDO.getOtherSideAccountNo();
                if (bankSlipClaimDOMap.containsKey(otherSideAccountNo)) {
                    BankSlipClaimDO bankSlipClaimDO = bankSlipClaimDOMap.get(otherSideAccountNo);
                    BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
                    newBankSlipClaimDO.setBankSlipDetailId(id);
                    newBankSlipClaimDO.setOtherSideAccountNo(otherSideAccountNo);
                    newBankSlipClaimDO.setCustomerNo(bankSlipClaimDO.getCustomerNo());
                    newBankSlipClaimDO.setCustomerName(bankSlipClaimDO.getCustomerName());
                    newBankSlipClaimDO.setClaimAmount(bankSlipDetailDO.getTradeAmount());
                    newBankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
                    newBankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                    newBankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    newBankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    newBankSlipClaimDO.setCreateTime(now);
                    newBankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    newBankSlipClaimDO.setUpdateTime(now);
                    bankSlipClaimDOList.add(newBankSlipClaimDO);
                    //改变流水项状态
                    bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
                    //已认领数量
                    claimCount = claimCount + 1;
                    // 添加已有认领数据自动认领操作日志
                    BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                    bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                    bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_CLAIM);
                    bankSlipDetailOperationLogDO.setOperationContent("自动认领(已有的认领数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + BankSlipSupport.getBankTypeName(bankSlipDO.getBankType()) + "）--银行对公流水明细id：" + id + ",认领人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户名称：" + getCustomerName(customerNoMap,bankSlipClaimDO.getCustomerNo()) + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
                    bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    bankSlipDetailOperationLogDO.setCreateTime(now);
                    bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
                } else {
                    lastBankSlipDetailDOList.add(bankSlipDetailDO);
                }
            }

            //自动认领最新一次记录
            if (CollectionUtil.isNotEmpty(lastBankSlipDetailDOList)) {
                List<CustomerCompanyDO> customerCompanyDOList = new ArrayList<>();
                for (BankSlipDetailDO bankSlipDetailDO : lastBankSlipDetailDOList) {
                    if (!StringUtil.isEmpty(bankSlipDetailDO.getPayerName())) {
                        String simpleName = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
                        CustomerCompanyDO customerCompanyDO = new CustomerCompanyDO();
                        customerCompanyDO.setSimpleCompanyName(simpleName);
                        customerCompanyDOList.add(customerCompanyDO);
                    }
                }

                if (CollectionUtil.isNotEmpty(customerCompanyDOList)) {
                    //List<CustomerCompanyDO> dbCustomerCompanyDOList = customerCompanyMapper.findCustomerCompanyByName(customerCompanyDOList);
                    List<BankSipAutomaticClaimDTO> bankSipAutomaticClaimDTOList = bankSlipClaimMapper.findBankSlipClaimPaySuccessByName(customerCompanyDOList);
                    if (CollectionUtil.isNotEmpty(bankSipAutomaticClaimDTOList)) {
                        Map<String, BankSipAutomaticClaimDTO> bankSipAutomaticClaimDTOMap = this.bankSipAutomaticClaimToMap(bankSipAutomaticClaimDTOList);
                        Iterator<BankSlipDetailDO> iter = lastBankSlipDetailDOList.iterator();


                        Set<String> automaticClaimCustomerNoSet = new HashSet();
                        for (BankSipAutomaticClaimDTO bankSipAutomaticClaimDTO : bankSipAutomaticClaimDTOList) {

                            String customerNo = bankSipAutomaticClaimDTO.getCompanyNo();
                            automaticClaimCustomerNoSet.add(customerNo);
                        }
                        Map<String, CustomerDO> automaticClaimCustomerNoMap = getCustomerNoMap(automaticClaimCustomerNoSet);


                        nameIsNull:
                        while (iter.hasNext()) {
                            BankSlipDetailDO bankSlipDetailDO = iter.next();
                            String simple = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
                            if (StringUtil.isEmpty(simple)) {
                                iter.remove();
                                continue nameIsNull;
                            }
                            if (bankSipAutomaticClaimDTOMap.containsKey(simple)) {
                                BankSipAutomaticClaimDTO bankSipAutomaticClaimDTO = bankSipAutomaticClaimDTOMap.get(simple);

                                BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
                                newBankSlipClaimDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                                newBankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
                                newBankSlipClaimDO.setCustomerNo(bankSipAutomaticClaimDTO.getCompanyNo());
                                newBankSlipClaimDO.setCustomerName(bankSipAutomaticClaimDTO.getCompanyName());
                                newBankSlipClaimDO.setClaimAmount(bankSlipDetailDO.getTradeAmount());
                                newBankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
                                newBankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                                newBankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                                newBankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
                                newBankSlipClaimDO.setCreateTime(now);
                                newBankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                                newBankSlipClaimDO.setUpdateTime(now);
                                bankSlipClaimDOList.add(newBankSlipClaimDO);
                                //改变流水项状态
                                bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
                                //已认领数量
                                claimCount = claimCount + 1;
                                // 添加操作日志
                                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_CLAIM);
                                bankSlipDetailOperationLogDO.setOperationContent("自动认领(付款人和已有的客户相同数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",认领人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户名称：" + getCustomerName(automaticClaimCustomerNoMap,bankSipAutomaticClaimDTO.getCompanyNo()) + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
                                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                                bankSlipDetailOperationLogDO.setCreateTime(now);
                                bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                                bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
                            } else {
                                lastTwoBankSlipDetailDOList.add(bankSlipDetailDO);
                                iter.remove();
                            }
                        }
                    } else {
                        lastTwoBankSlipDetailDOList = lastBankSlipDetailDOList;
                    }
                } else {
                    lastTwoBankSlipDetailDOList = lastBankSlipDetailDOList;
                }
            }

            //自动认领付款人名称和已有的公司简单名称相同的数据
            if (CollectionUtil.isNotEmpty(lastTwoBankSlipDetailDOList)) {
                List<CustomerCompanyDO> customerCompanyDOList = new ArrayList<>();
                for (BankSlipDetailDO bankSlipDetailDO : lastTwoBankSlipDetailDOList) {
                    if (!StringUtil.isEmpty(bankSlipDetailDO.getPayerName())) {
                        String simpleName = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
                        CustomerCompanyDO customerCompanyDO = new CustomerCompanyDO();
                        customerCompanyDO.setSimpleCompanyName(simpleName);
                        customerCompanyDOList.add(customerCompanyDO);
                    }
                }
                if (CollectionUtil.isNotEmpty(customerCompanyDOList)) {
                    List<CustomerCompanyDO> dbCustomerCompanyDOList = customerCompanyMapper.findCustomerCompanyByName(customerCompanyDOList);
                    if (CollectionUtil.isNotEmpty(dbCustomerCompanyDOList)) {
                        Map<String, CustomerCompanyDO> customerCompanyDOMap = ListUtil.listToMap(dbCustomerCompanyDOList, "simpleCompanyName");
                        Iterator<BankSlipDetailDO> iter = lastTwoBankSlipDetailDOList.iterator();
                        nameIsNull:
                        while (iter.hasNext()) {
                            BankSlipDetailDO bankSlipDetailDO = iter.next();
                            String simple = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
                            if (StringUtil.isEmpty(simple)) {
                                iter.remove();
                                continue nameIsNull;
                            }
                            if (customerCompanyDOMap.containsKey(simple)) {
                                CustomerCompanyDO customerCompanyDO = customerCompanyDOMap.get(simple);

                                BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
                                newBankSlipClaimDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                                newBankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
                                newBankSlipClaimDO.setCustomerNo(customerCompanyDO.getCustomerNo());
                                newBankSlipClaimDO.setCustomerName(customerCompanyDO.getCompanyName());
                                newBankSlipClaimDO.setClaimAmount(bankSlipDetailDO.getTradeAmount());
                                newBankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
                                newBankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                                newBankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                                newBankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
                                newBankSlipClaimDO.setCreateTime(now);
                                newBankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                                newBankSlipClaimDO.setUpdateTime(now);
                                bankSlipClaimDOList.add(newBankSlipClaimDO);
                                //改变流水项状态
                                bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
                                //已认领数量
                                claimCount = claimCount + 1;
                                // 添加操作日志
                                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_CLAIM);
                                bankSlipDetailOperationLogDO.setOperationContent("自动认领(付款人和已有的客户相同数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",认领人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户名称：" + customerCompanyDO.getCompanyName() + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
                                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                                bankSlipDetailOperationLogDO.setCreateTime(now);
                                bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                                bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                }
            }

            //改变总表的已认领数量和未认领数量
            bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() - claimCount);
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + claimCount);

            //创建各个数据批量存储
            if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
                bankSlipClaimMapper.saveBankSlipClaimDO(bankSlipClaimDOList);
            }
            if (CollectionUtil.isNotEmpty(newBankSlipDetailDOList)) {
                bankSlipDetailMapper.updateBankSlipDetailDO(newBankSlipDetailDOList);
                bankSlipMapper.update(bankSlipDO);
            }
            //跟新付款人名称和已有的公司简单名称相同的数据 的流水详情表状态
            if (CollectionUtil.isNotEmpty(lastBankSlipDetailDOList)) {
                bankSlipDetailMapper.updateBankSlipDetailDO(lastBankSlipDetailDOList);
            }

            //自动归属化操作
            if (BankType.JING_DONG.equals(bankSlipDO.getBankType())) {
                bankSlipDO.setLocalizationCount(bankSlipDO.getInCount());
                bankSlipDetailMapper.updateBankSlipDetailDO(bankSlipDetailDOList);
            } else {
                //如果是总公司才操作
                List<BankSlipDetailDO> headerBankSlipDetailDOList = new ArrayList<>();
                //查询所有已归属的总公司数据
                List<BankSlipDetailDO> localizationBankSlipDetailDOList = bankSlipDetailMapper.findLocalizationBankSlipDetailDO();
                Map<String, BankSlipDetailDO> localizationBankSlipDetailDOMap = ListUtil.listToMap(localizationBankSlipDetailDOList, "otherSideAccountNo");
                int localizationCount = 0;
                for (Integer key : newBankSlipDetailDOMap.keySet()) {
                    BankSlipDetailDO bankSlipDetailDO = newBankSlipDetailDOMap.get(key);
                    String otherSideAccountNo = bankSlipDetailDO.getOtherSideAccountNo();
                    if (localizationBankSlipDetailDOMap.containsKey(otherSideAccountNo)) {
                        BankSlipDetailDO dbBankSlipDetailDO = localizationBankSlipDetailDOMap.get(otherSideAccountNo);
                        bankSlipDetailDO.setSubCompanyId(dbBankSlipDetailDO.getSubCompanyId());
                        bankSlipDetailDO.setIsLocalization(CommonConstant.COMMON_CONSTANT_YES);
                        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                        bankSlipDetailDO.setUpdateTime(now);
                        localizationCount++;
                        headerBankSlipDetailDOList.add(bankSlipDetailDO);
                        // 添加操作日志
                        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_LOCALIZATION);
                        bankSlipDetailOperationLogDO.setOperationContent("自动属地化(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",属地化到公司：" + dbBankSlipDetailDO.getSubCompanyName());
                        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        bankSlipDetailOperationLogDO.setCreateTime(now);
                        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                        bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
                    }
                }
                if (CollectionUtil.isNotEmpty(headerBankSlipDetailDOList)) {
                    bankSlipDetailMapper.updateSubCompanyAndIsLocalization(headerBankSlipDetailDOList);
                }
                bankSlipDO.setLocalizationCount((bankSlipDO.getLocalizationCount() == null ? 0 : bankSlipDO.getLocalizationCount()) + localizationCount);
            }

            bankSlipMapper.update(bankSlipDO);

            // 保存日志list
            if (CollectionUtil.isNotEmpty(bankSlipDetailOperationLogDOList)) {
                bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);
            }
        }
    }

    private Map<String, BankSipAutomaticClaimDTO> bankSipAutomaticClaimToMap(List<BankSipAutomaticClaimDTO> bankSipAutomaticClaimList) {
        Map<String, BankSipAutomaticClaimDTO> bankSipAutomaticClaimMap = new HashMap<>();
        for (BankSipAutomaticClaimDTO bankSipAutomaticClaimDTO : bankSipAutomaticClaimList) {
            if (null != bankSipAutomaticClaimDTO && !StringUtil.isEmpty(bankSipAutomaticClaimDTO.getPayerName())) {
                bankSipAutomaticClaimMap.put(bankSipAutomaticClaimDTO.getPayerName(), bankSipAutomaticClaimDTO);
            }
        }
        return bankSipAutomaticClaimMap;
    }
    public Map<String, CustomerDO> getCustomerNoMap(Set<String> customerNoSet) {
        Map<String, CustomerDO> customerCompanyNoMap = null;
        if(CollectionUtil.isNotEmpty(customerNoSet)){
            List<CustomerDO> customerNoList = customerMapper.findByCustomerNoList(new ArrayList<>(customerNoSet));
            if (CollectionUtil.isNotEmpty(customerNoList)) {
                customerCompanyNoMap = ListUtil.listToMap(customerNoList, "customerNo");
            }
        }
        return customerCompanyNoMap;
    }

    public String getCustomerName(Map<String, CustomerDO> customerNoMap,String customerNo){
        String customerName = "";
        if(customerNoMap != null){
            if(customerNoMap.get(customerNo) != null){
                customerName = customerNoMap.get(customerNo).getCustomerName();
            }
        }
        return customerName;
    }


}
