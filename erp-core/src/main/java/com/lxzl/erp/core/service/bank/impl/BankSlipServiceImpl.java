package com.lxzl.erp.core.service.bank.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ConstantConfig;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailOperationLogQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.ClaimParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetailOperationLog;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.bank.impl.importSlip.*;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipClaimMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailOperationLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailOperationLogDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: pengbinjie
 * @Description：
 * @Date: Created in 19:49 2018/3/20
 * @Modified By:
 */
@Service
public class BankSlipServiceImpl implements BankSlipService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
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
    private ImportHanKouBank importHanKouBank;

    @Autowired
    private BankSlipMapper bankSlipMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private BankSlipDetailMapper bankSlipDetailMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BankSlipClaimMapper bankSlipClaimMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;

    @Autowired
    private BankSlipDetailOperationLogMapper bankSlipDetailOperationLogMapper;

    @Override
    public ServiceResult<String, Page<BankSlip>> pageBankSlip(BankSlipQueryParam bankSlipQueryParam) {
        ServiceResult<String, Page<BankSlip>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());
        if (bankSlipQueryParam.getSlipDayStart() != null) {
            bankSlipQueryParam.setSlipDayStart(DateUtil.getDayByOffset(bankSlipQueryParam.getSlipDayStart(), CommonConstant.COMMON_ZERO));
        }
        if (bankSlipQueryParam.getSlipDayEnd() != null) {
            bankSlipQueryParam.setSlipDayEnd(DateUtil.getDayByOffset(bankSlipQueryParam.getSlipDayEnd(), CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD));
        }
        Integer departmentType = 0;
        if (userSupport.isFinancePerson() || userSupport.isSuperUser()) {
//            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isBusinessPerson()) {
//            //商务和业务员类型设置为2
            departmentType = 2;
        }
        //当前用户所属分公司

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipQueryParam", bankSlipQueryParam);
        maps.put("departmentType", departmentType);
        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
        Integer totalCount = bankSlipMapper.findBankSlipCountByParams(maps);
        List<BankSlipDO> bankSlipDOList = bankSlipMapper.findBankSlipByParams(maps);

        List<BankSlip> bankSlipList = ConverterUtil.convertList(bankSlipDOList, BankSlip.class);
        Page<BankSlip> page = new Page<>(bankSlipList, totalCount, bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> saveBankSlip(BankSlip bankSlip) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //判断当前用户是否是本分公司的(总公司除外)
        if(userSupport.isElectric() || userSupport.isChannelSubCompany()){
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        if (!(userSupport.getCurrentUserCompanyId().equals(bankSlip.getSubCompanyId()) && (userSupport.isFinancePerson() || userSupport.isSuperUser()))) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }

        //校验上传流水月份是否超过当天
        if (bankSlip.getSlipDay().getTime() - DateUtil.getDayByCurrentOffset(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD).getTime() > 0) {
            serviceResult.setErrorCode(ErrorCode.OVERSTEP_CURRENT_DAY);
            return serviceResult;
        }
        Integer bankType = bankSlip.getBankType();
        bankSlip.setSlipDay(DateUtil.getDayByOffset(bankSlip.getSlipDay(), CommonConstant.COMMON_ZERO));
        if (!BankType.BOC_BANK.equals(bankType) &&
                !BankType.TRAFFIC_BANK.equals(bankType) &&
                !BankType.NAN_JING_BANK.equals(bankType) &&
                !BankType.AGRICULTURE_BANK.equals(bankType) &&
                !BankType.ICBC_BANK.equals(bankType) &&
                !BankType.CCB_BANK.equals(bankType) &&
                !BankType.PING_AN_BANK.equals(bankType) &&
                !BankType.CMBC_BANK.equals(bankType) &&
                !BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType) &&
                !BankType.ALIPAY.equals(bankType) &&
                !BankType.HAN_KOU_BANK.equals(bankType)) {
            serviceResult.setErrorCode(ErrorCode.BANK_TYPE_IS_FAIL);
            return serviceResult;
        }

        String excelUrl = bankSlip.getExcelUrl();
        excelUrl = ConstantConfig.imageDomain + excelUrl;
        InputStream inputStream = FileUtil.getFileInputStream(excelUrl);

        if (inputStream == null) {
            serviceResult.setErrorCode(ErrorCode.EXCEL_SHEET_IS_NULL);
            return serviceResult;
        }
        ServiceResult<String, BankSlipDO> result = null;
        if (BankType.BOC_BANK.equals(bankType)) {
            result = importChinaBank.saveChinaBank(bankSlip, inputStream);
        } else if (BankType.TRAFFIC_BANK.equals(bankType)) {
            result = importTrafficBank.saveTrafficBank(bankSlip, inputStream);
        } else if (BankType.NAN_JING_BANK.equals(bankType)) {
            result = importNanJingBank.saveNanJingBank(bankSlip, inputStream);
        } else if (BankType.AGRICULTURE_BANK.equals(bankType)) {
            result = importAgricultureBank.saveAgricultureBank(bankSlip, inputStream);
        } else if (BankType.ICBC_BANK.equals(bankType)) {
            result = importICBCBank.saveICBCBank(bankSlip, inputStream);
        } else if (BankType.CCB_BANK.equals(bankType)) {
            result = importCCBBank.saveCCBBank(bankSlip, inputStream);
        } else if (BankType.PING_AN_BANK.equals(bankType)) {
            result = importPingAnBank.savePingAnBank(bankSlip, inputStream);
        } else if (BankType.CMBC_BANK.equals(bankType)) {
            result = importCMBCBank.saveCMBCBank(bankSlip, inputStream);
        } else if (BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType)) {
            result = importShanghaiPudongDevelopmentBank.saveShanghaiPudongDevelopmentBank(bankSlip, inputStream);
        } else if (BankType.ALIPAY.equals(bankType)) {
            result = importAlipay.saveAlipay(bankSlip, inputStream);
        } else if (BankType.HAN_KOU_BANK.equals(bankType)) {
            result = importHanKouBank.saveHanKouBank(bankSlip, inputStream);
        }
        if (ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            //跟新 需要认领数据
            BankSlipDO bankSlipDO = result.getResult();
            List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDO.getBankSlipDetailDOList();
            //查一边是否是 流入金额
            List<BankSlipDetailDO> newBankSlipDetailDOList = new ArrayList<>();

            // 查询出导入时间的所有本公司的所有导入数据
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                if (LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
                    newBankSlipDetailDOList.add(bankSlipDetailDO);
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

            //没有认领的数据
            List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
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
                    newBankSlipClaimDO.setClaimAmount(bankSlipClaimDO.getClaimAmount());
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
                    bankSlipDetailOperationLogDO.setOperationContent("自动认领(已有的认领数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + getBankTypeName(bankSlipDetailDO.getBankSlipBankType()) + "）--银行对公流水明细id：" + id + ",认领人：" + userSupport.getCurrentUser().getUserName().toString() + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
                    bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    bankSlipDetailOperationLogDO.setCreateTime(now);
                    bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
                } else {
                    lastBankSlipDetailDOList.add(bankSlipDetailDO);
                }
            }


            //自动认领付款人名称和已有的公司简单名称相同的数据

            if (CollectionUtil.isNotEmpty(lastBankSlipDetailDOList)) {
                List<CustomerCompanyDO> customerCompanyDOList = new ArrayList<>();
                for (BankSlipDetailDO bankSlipDetailDO : lastBankSlipDetailDOList) {
                    String simpleName = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
                    CustomerCompanyDO customerCompanyDO = new CustomerCompanyDO();
                    customerCompanyDO.setSimpleCompanyName(simpleName);
                    customerCompanyDOList.add(customerCompanyDO);
                }
                List<CustomerCompanyDO> dbCustomerCompanyDOList = customerCompanyMapper.findCustomerCompanyByName(customerCompanyDOList);
                if (CollectionUtil.isNotEmpty(dbCustomerCompanyDOList)) {
                    Map<String, CustomerCompanyDO> customerCompanyDOMap = ListUtil.listToMap(dbCustomerCompanyDOList, "simpleCompanyName");

                    Iterator<BankSlipDetailDO> iter = lastBankSlipDetailDOList.iterator();
                    aaa:
                    while (iter.hasNext()) {
                        BankSlipDetailDO bankSlipDetailDO = iter.next();
                        String simple = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
                        if (simple == null || "".equals(simple)) {
                            iter.remove();
                            continue aaa;
                        }
                        if (customerCompanyDOMap.containsKey(simple)) {
                            CustomerCompanyDO customerCompanyDO = customerCompanyDOMap.get(simple);

                            BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
                            newBankSlipClaimDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                            newBankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
                            newBankSlipClaimDO.setCustomerNo(customerCompanyDO.getCustomerNo());
                            newBankSlipClaimDO.setCustomerName(customerCompanyDO.getSimpleCompanyName());
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
                            bankSlipDetailOperationLogDO.setOperationContent("自动认领(付款人和已有的客户相同数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",认领人：" + userSupport.getCurrentUser().getUserName().toString() + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户编号：" + customerCompanyDO.getCustomerNo() + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
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

            //如果是总公司才操作
            if (CommonConstant.HEADER_COMPANY_ID.equals(bankSlipDO.getSubCompanyId())) {
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
                        bankSlipDetailOperationLogDO.setOperationContent("自动属地化(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + userSupport.getCurrentUser().getUserName().toString() + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",属地化到公司：" + dbBankSlipDetailDO.getSubCompanyName());
                        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        bankSlipDetailOperationLogDO.setCreateTime(now);
                        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                        bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
                    }
                }
                if (CollectionUtil.isNotEmpty(headerBankSlipDetailDOList)) {
                    bankSlipDetailMapper.updateSubCompanyAndIsLocalization(headerBankSlipDetailDOList);
                }
                bankSlipDO.setLocalizationCount(localizationCount);
                bankSlipMapper.update(bankSlipDO);

            }

            // 保存日志list
            if (CollectionUtil.isNotEmpty(bankSlipDetailOperationLogDOList)) {
                bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);
            }
        }
        serviceResult.setErrorCode(result.getErrorCode());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<BankSlipDetail>> pageBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam) {
        ServiceResult<String, Page<BankSlipDetail>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());

        if (bankSlipDetailQueryParam.getSlipDayStart() != null) {
            bankSlipDetailQueryParam.setSlipDayStart(DateUtil.getDayByOffset(bankSlipDetailQueryParam.getSlipDayStart(), CommonConstant.COMMON_ZERO));
        }
        if (bankSlipDetailQueryParam.getSlipDayEnd() != null) {
            bankSlipDetailQueryParam.setSlipDayEnd(DateUtil.getDayByOffset(bankSlipDetailQueryParam.getSlipDayEnd(), CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD));

        }
        Integer departmentType = 0;
        if (userSupport.isFinancePerson() || userSupport.isSuperUser()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isElectric() || userSupport.isChannelSubCompany()) {
            //商务类型设置为2
            departmentType = 2;
        } else if (userSupport.isBusinessPerson()) {
            //业务员类型设置为3
            departmentType = 3;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipDetailQueryParam", bankSlipDetailQueryParam);
        maps.put("departmentType", departmentType);
        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
        maps.put("currentUser", userSupport.getCurrentUserId().toString());

        Integer totalCount = bankSlipDetailMapper.findBankSlipDetailDOCountByParams(maps);
        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDetailMapper.findBankSlipDetailDOByParams(maps);


        List<BankSlipDetail> bankSlipDetailList = ConverterUtil.convertList(bankSlipDetailDOList, BankSlipDetail.class);
        Page<BankSlipDetail> page = new Page<>(bankSlipDetailList, totalCount, bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> pushDownBankSlip(BankSlip bankSlip) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //是否有权下推
        if (!userSupport.isFinancePerson() && !userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //银行对公流水是否存在
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlip.getBankSlipId());
        if (bankSlipDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_NOT_EXISTS);
            return serviceResult;
        }
        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }

        //判断状态是否是初始化
        if (!SlipStatus.INITIALIZE.equals(bankSlipDO.getSlipStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_STATUS_NOT_INITIALIZE);
            return serviceResult;
        }
        if (bankSlipDO.getNeedClaimCount() == 0 && bankSlipDO.getClaimCount() == 0) {
            bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
        } else {
            bankSlipDO.setSlipStatus(SlipStatus.ALREADY_PUSH_DOWN);
        }
        bankSlipDO.setUpdateTime(now);
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipMapper.update(bankSlipDO);
        // 添加操作日志
        if (CollectionUtil.isNotEmpty(bankSlipDO.getBankSlipDetailDOList())) {
            List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDO.getBankSlipDetailDOList()) {
                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.PUSH_DOWN);
                bankSlipDetailOperationLogDO.setOperationContent("下推操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",下推人：" + userSupport.getCurrentUser().getUserName().toString() + "，下推时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                bankSlipDetailOperationLogDO.setCreateTime(now);
                bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
                bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
            }

            // 保存日志list
            bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);
        }


        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlip.getBankSlipId());
        return serviceResult;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> claimBankSlipDetail(BankSlipClaim bankSlipClaim) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //判断是否有银行对公流水项
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipClaim.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }
        //校验流水总表状态是否下推，如果未下推，则商务和业务员不可以操作
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());

        if (SlipStatus.INITIALIZE.equals(bankSlipDO.getSlipStatus())) {
            if (!userSupport.isFinancePerson() && !userSupport.isSuperUser() && !userSupport.isElectric() && !userSupport.isChannelSubCompany()) {
                serviceResult.setErrorCode(ErrorCode.CURRENT_ROLES_NOT_PERMISSION);
                return serviceResult;
            }
        }
        if (!(LocalizationType.UNKNOWN.equals(bankSlipDetailDO.getIsLocalization()) && CommonConstant.HEADER_COMPANY_ID.equals(bankSlipDetailDO.getOwnerSubCompanyId()))) {
            //当前用户如不是总公司,看是否有权先操作
            String verifyPermission = verifyPermission(bankSlipDO, bankSlipDetailDO);
            if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
                serviceResult.setErrorCode(verifyPermission);
                return serviceResult;
            }
        }

        //判断银行对公流水项状态(已确认)
        if (BankSlipDetailStatus.CONFIRMED.equals(bankSlipDetailDO.getDetailStatus()) || BankSlipDetailStatus.HIDE.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_IS_CONFIRMED_OR_HIDE);
            return serviceResult;
        }

        //判断是否是收入状态
        if (!LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_INCOME);
            return serviceResult;
        }
        List<ClaimParam> claimParamList = bankSlipClaim.getClaimParam();
        //判断客户是否相等

        //判断如果有已支付金额就不允许修改
        List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
        // 数据库已认领金额
        BigDecimal dbAllClaimAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
            for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
                if (!RechargeStatus.INITIALIZE.equals(bankSlipClaimDO.getRechargeStatus()) &&
                        !RechargeStatus.PAY_FAIL.equals(bankSlipClaimDO.getRechargeStatus())) {
                    serviceResult.setErrorCode(ErrorCode.BANK_SLIP_CLAIM_PAY_STATUS_ERROR);
                    return serviceResult;
                }
                BigDecimalUtil.add(dbAllClaimAmount, bankSlipClaimDO.getClaimAmount());
            }
        }

        ServiceResult<String, BigDecimal> result = verifyClaimAmount(claimParamList);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            serviceResult.setErrorCode(result.getErrorCode());
            return serviceResult;
        }
        BigDecimal allClaimAmount = result.getResult();

        // 如果是总公司
        if (CommonConstant.HEADER_COMPANY_ID.equals(bankSlipDetailDO.getOwnerSubCompanyId())) {
            // 判断是否都认领完成 和是否是未知数据
            if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus()) && LocalizationType.UNKNOWN.equals(bankSlipDetailDO.getIsLocalization())) {
                // 删除所有数据
                bankSlipClaimMapper.deleteByBankSlipDetailId(bankSlipDetailDO.getId(), userSupport.getCurrentUserId().toString(), now);
                // 改变详情和总表状态
                bankSlipDetailMapper.deleteBankSlipDetail(bankSlipDetailDO.getId(), userSupport.getCurrentUserId().toString(), now);

                //判断总共金额是否相等
                if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) != 0 && BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) < 0) {
                    // 只是保存认领记录其他的不改变  和记录日志
                    long currentTimeMillis = System.currentTimeMillis();
                    saveBankSlipClaimDOAndBankSlipDetailOperationLog(bankSlipDO, bankSlipClaim, claimParamList, bankSlipDetailDO, currentTimeMillis, now);
                } else if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) == 0) {
                    //  调用下面方法去改变状态  和记录日志
                    //这里是保存银行对公流水认领表 数据
                    serviceResult = updateClaim(bankSlipDO, bankSlipDetailDO, bankSlipClaim, claimParamList, now);
                    return serviceResult;
                } else if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) > 0) {
                    serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_LESS_THAN_CURRENT_AGGREGATE_AMOUNT);
                    return serviceResult;
                }
            } else if (BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus()) && LocalizationType.UNKNOWN.equals(bankSlipDetailDO.getIsLocalization())) {
                // 判断未认领完成 和是否是未知数据
                // 现在的数据加上以前的数据开是否认领完成
                if (BigDecimalUtil.compare(BigDecimalUtil.add(dbAllClaimAmount, allClaimAmount), bankSlipDetailDO.getTradeAmount()) < 0) {
                    // 如果未认领完成  只是保存
                    long currentTimeMillis = System.currentTimeMillis();
                    saveBankSlipClaimDOAndBankSlipDetailOperationLog(bankSlipDO, bankSlipClaim, claimParamList, bankSlipDetailDO, currentTimeMillis, now);
                } else if (BigDecimalUtil.compare(BigDecimalUtil.add(dbAllClaimAmount, allClaimAmount), bankSlipDetailDO.getTradeAmount()) == 0) {
                    // 如果认领完成 调用下面的接口
                    serviceResult = updateClaim(bankSlipDO, bankSlipDetailDO, bankSlipClaim, claimParamList, now);
                }
                if (BigDecimalUtil.compare(BigDecimalUtil.add(dbAllClaimAmount, allClaimAmount), bankSlipDetailDO.getTradeAmount()) > 0) {
                    serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_LESS_THAN_CURRENT_AGGREGATE_AMOUNT);
                    return serviceResult;
                }
            }
        }

        //判断总共金额是否相等
        if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) != 0) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_UNEQUAL_CURRENT_AGGREGATE_AMOUNT);
            return serviceResult;
        }

        //这里是保存银行对公流水认领表 数据
        serviceResult = updateClaim(bankSlipDO, bankSlipDetailDO, bankSlipClaim, claimParamList, now);

        return serviceResult;
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateClaim(BankSlipDO bankSlipDO, BankSlipDetailDO bankSlipDetailDO, BankSlipClaim bankSlipClaim, List<ClaimParam> claimParamList, Date now) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //如果状态为未认领状态
        if (BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            //跟新银行对公流水已认领笔数
            bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() - 1);
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + 1);
        } else if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            //状态为已认领状态,以前的全部删除
            bankSlipClaimMapper.deleteByBankSlipDetailId(bankSlipDetailDO.getId(), userSupport.getCurrentUserId().toString(), now);
        }
        bankSlipDO.setUpdateTime(now);
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipMapper.update(bankSlipDO);
        long currentTimeMillis = System.currentTimeMillis();

        saveBankSlipClaimDOAndBankSlipDetailOperationLog(bankSlipDO, bankSlipClaim, claimParamList, bankSlipDetailDO, currentTimeMillis, now);
        //跟新银行对公流水项
        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailMapper.update(bankSlipDetailDO);


        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDetailDO.getId());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> confirmBankSlip(BankSlip bankSlip) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlip.getBankSlipId());
        Date now = new Date();
        if (bankSlipDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_NOT_EXISTS);
            return serviceResult;
        }

        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }

        //是否为商务
        if (!userSupport.isBusinessAffairsPerson() && !userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.IS_NOT_BUSINESS_AFFAIRS_PERSON);
            return serviceResult;
        }
        //是否为已经下推
        if (!SlipStatus.ALREADY_PUSH_DOWN.equals(bankSlipDO.getSlipStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_STATUS_NOT_ALREADY_PUSH_DOWN);
            return serviceResult;
        }
        //查询 银行对公流水明细表对应银行对公流水认领表数据分别加款导客户账户
        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDetailMapper.findClaimedByBankSlipId(bankSlip.getBankSlipId());

        if (CollectionUtil.isEmpty(bankSlipDetailDOList)) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_HAVE_CLAIMED);
            return serviceResult;
        }
        int amount = 0;

        List<BankSlipClaimDO> newDankSlipClaimDOList = new ArrayList<>();

        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
            List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
            if (CollectionUtil.isEmpty(bankSlipClaimDOList)) {
                continue;
            }
            BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
            boolean paySuccessFlag = true;
            StringBuffer stringBuffer = new StringBuffer("");
            for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
                //充值成功不需要再冲
                if (!RechargeStatus.PAY_SUCCESS.equals(bankSlipClaimDO.getRechargeStatus())) {
                    //加款到客户账号
                    ManualChargeParam manualChargeParam = new ManualChargeParam();
                    manualChargeParam.setBusinessCustomerNo(bankSlipClaimDO.getCustomerNo());
                    manualChargeParam.setChargeAmount(bankSlipClaimDO.getClaimAmount());
                    manualChargeParam.setChargeRemark(bankSlipClaimDO.getRemark());
                    try {
                        ServiceResult<String, Boolean> result = paymentService.manualCharge(manualChargeParam);
                        if (ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                            //银行对公流水认领表 成功 改变状态
                            bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_SUCCESS);
                            stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUser().getUserName().toString() + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",充值时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",充值：" + bankSlipClaimDO.getClaimAmount() + "元，成功！！！");
                        } else {
                            //银行对公流水认领表 失败 改变状态
                            bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
                            paySuccessFlag = false;
                            stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUser().getUserName().toString() + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",充值时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",充值：" + bankSlipClaimDO.getClaimAmount() + "元，失败！！！");
                        }
                    } catch (Exception e) {
                        logger.error("------------------充值出错----------------------", e);
                        //银行对公流水认领表 失败 改变状态
                        bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
                        paySuccessFlag = false;
                        stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUser().getUserName().toString() + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",充值时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",充值：" + bankSlipClaimDO.getClaimAmount() + "元，失败！！！");
                    }
                    bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    bankSlipClaimDO.setUpdateTime(now);
                    newDankSlipClaimDOList.add(bankSlipClaimDO);
                } else {
                    // 充值成功的充值也要记录
                    stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUser().getUserName().toString() + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",充值时间：" + bankSlipClaimDO.getClaimAmount() + "元，" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipClaimDO.getUpdateTime()) + "已充值无需充值！！！");
                }
            }
            // 添加操作日志
            bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
            bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.RECHARGE);
            bankSlipDetailOperationLogDO.setOperationContent(String.valueOf(stringBuffer));
            bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bankSlipDetailOperationLogDO.setCreateTime(now);
            bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
            //充值成功改为确认 ,失败只是跟改明细跟新动作
            if (paySuccessFlag) {
                //银行对公流水明细记录变为已确定
                bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CONFIRMED);
                amount = amount + 1;
            }
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailDO.setUpdateTime(now);
        }
        if (CollectionUtil.isEmpty(newDankSlipClaimDOList)) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_NEED_CONFIRMED);
            return serviceResult;
        }
        // 保存日志list
        if (CollectionUtil.isNotEmpty(bankSlipDetailOperationLogDOList)) {
            bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);
        }

        bankSlipClaimMapper.updateBankSlipClaimDO(newDankSlipClaimDOList);
        //以下是批量跟新操作 跟新对公流水项  和  认领表
        if (CollectionUtil.isNotEmpty(bankSlipDetailDOList)) {
            bankSlipDetailMapper.updateConfirmBankDetailDO(bankSlipDetailDOList);
            //改变已经确认个数  再判断认领个数
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() - amount);
            if (bankSlipDO.getNeedClaimCount() == 0 && bankSlipDO.getClaimCount() == 0) {
                bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
            }
            bankSlipDO.setConfirmCount(bankSlipDO.getConfirmCount() + amount);
            bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDO.setUpdateTime(now);
            bankSlipMapper.update(bankSlipDO);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDO.getId());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, BankSlipDetail> queryBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, BankSlipDetail> serviceResult = new ServiceResult<>();
        Integer departmentType = 0;
        if (userSupport.isFinancePerson() || userSupport.isSuperUser()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isElectric() || userSupport.isChannelSubCompany()) {
            //商务类型设置为2
            departmentType = 2;
        } else if (userSupport.isBusinessPerson()) {
            //业务员类型设置为3
            departmentType = 3;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("bankSlipDetailId", bankSlipDetail.getBankSlipDetailId());
        maps.put("departmentType", departmentType);
        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
        maps.put("currentUser", userSupport.getCurrentUserId().toString());

        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findBankSlipDetail(maps);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ConverterUtil.convert(bankSlipDetailDO, BankSlipDetail.class));
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> deleteBankSlip(BankSlip bankSlip) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        Integer bankSlipId = bankSlip.getBankSlipId();
        BankSlipDO bankSlipDO = bankSlipMapper.findDetailById(bankSlipId);

        if (bankSlipDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_NOT_EXISTS);
            return serviceResult;
        }
        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }
        if (!SlipStatus.INITIALIZE.equals(bankSlipDO.getSlipStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_STATUS_NOT_INITIALIZE);
            return serviceResult;
        }
        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDO.getBankSlipDetailDOList();
        if (CollectionUtil.isEmpty(bankSlipDetailDOList)) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }

        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
            bankSlipDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            bankSlipDetailDO.setUpdateTime(now);
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());

            // 添加操作日志
            BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
            bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
            bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.DELETE);
            bankSlipDetailOperationLogDO.setOperationContent("删除银行流水操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",删除人：" + userSupport.getCurrentUser().getUserName().toString() + ",删除时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
            bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bankSlipDetailOperationLogDO.setCreateTime(now);
            bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
        }

        //删除所有项
        bankSlipDetailMapper.deleteByBankSlipId(bankSlipDetailDOList);
        //删除总表
        bankSlipDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        bankSlipDO.setUpdateTime(now);
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipMapper.update(bankSlipDO);

        // 保存日志list
        bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> hideBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //是否有权隐藏
        if (!userSupport.isFinancePerson() && !userSupport.isSuperUser() && !userSupport.isElectric() && !userSupport.isChannelSubCompany()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }

        //加状态隐藏 跟新是否隐藏
        Integer bankSlipDetailId = bankSlipDetail.getBankSlipDetailId();
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetailId);

        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }

        if (!BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_DISPLAY);
            return serviceResult;
        }
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO, bankSlipDetailDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }


        if (SlipStatus.ALL_CLAIM.equals(bankSlipDO.getSlipStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IS_ALL_CLAIM);
            return serviceResult;
        }
        if (CommonConstant.HEADER_COMPANY_ID.equals(bankSlipDO.getSubCompanyId()) && !bankSlipDO.getSubCompanyId().equals(bankSlipDetailDO.getSubCompanyId())) {
            if (CommonConstant.COMMON_CONSTANT_YES.equals(bankSlipDetailDO.getIsLocalization())) {
                //调用取消属地化逻辑
                ServiceResult<String, BankSlipDetailDO> cancelLocalizationServiceResult = cancelLocalization(ConverterUtil.convert(bankSlipDetailDO, BankSlipDetail.class));
                if (!ErrorCode.SUCCESS.equals(cancelLocalizationServiceResult.getErrorCode())) {
                    serviceResult.setErrorCode(cancelLocalizationServiceResult.getErrorCode());
                    return serviceResult;
                }
                bankSlipDetailDO = cancelLocalizationServiceResult.getResult();
            }
        }
        //如果是未认领状态总数需要 -1
        if (BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() - 1);
            if (bankSlipDO.getNeedClaimCount() == 0 && bankSlipDO.getClaimCount() == 0) {
                if (SlipStatus.ALREADY_PUSH_DOWN.equals(bankSlipDO.getSlipStatus())) {
                    bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
                }
            }
        }

        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.HIDE);
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailMapper.update(bankSlipDetailDO);

        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDO.setUpdateTime(now);
        bankSlipMapper.update(bankSlipDO);

        //添加操作日志
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.HIDE);
        bankSlipDetailOperationLogDO.setOperationContent("影藏操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",隐藏人：" + userSupport.getCurrentUser().getUserName().toString() + ",隐藏时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> displayBankSlipDetail(BankSlipDetail bankSlipDetail) {
        //校验权限可显示
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //是否有权下推
        if (!userSupport.isFinancePerson() && !userSupport.isSuperUser() && !userSupport.isElectric() && !userSupport.isChannelSubCompany()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }

        //加状态隐藏 跟新是否隐藏
        Integer bankSlipDetailId = bankSlipDetail.getBankSlipDetailId();
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetailId);

        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }

        if (!BankSlipDetailStatus.HIDE.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_HIDE);
            return serviceResult;
        }
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());

        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO, bankSlipDetailDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }
        //跟新为未认领
        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailMapper.update(bankSlipDetailDO);

        //如果是未认领状态总数需要 +1
        bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() + 1);
        bankSlipDO.setSlipStatus(SlipStatus.ALREADY_PUSH_DOWN);
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDO.setUpdateTime(now);
        bankSlipMapper.update(bankSlipDO);

        // 添加操作日志
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.DISPLAY);
        bankSlipDetailOperationLogDO.setOperationContent("显示操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",显示人：" + userSupport.getCurrentUser().getUserName().toString() + ",显示时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> localizationBankSlipDetail(BankSlip bankSlip) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //是否总公司和超级管理员

        //判断公司是否存在
        SubCompanyDO subCompanyDO = subCompanyMapper.findById(bankSlip.getLocalizationSubCompanyId());
        if (subCompanyDO == null) {
            serviceResult.setErrorCode(ErrorCode.SUB_COMPANY_NOT_EXISTS);
            return serviceResult;
        }

        List<BankSlipDetailDO> updateBankSlipDetailDOList = new ArrayList<>();
        Map<Integer, BankSlipDO> bankSlipDOMap = new HashMap<>();

        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
        for (BankSlipDetail bankSlipDetail : bankSlip.getBankSlipDetailList()) {
            BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
            if (bankSlipDetailDO == null) {
                serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
                return serviceResult;
            }

            if (!bankSlipDOMap.containsKey(bankSlipDetailDO.getBankSlipId())) {
                BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
                bankSlipDOMap.put(bankSlipDO.getId(), bankSlipDO);
            }
            BankSlipDO bankSlipDO = bankSlipDOMap.get(bankSlipDetailDO.getBankSlipId());

            //判断所属公司是否是总公司
            String permission = verifyPermission(bankSlipDO, bankSlipDetailDO);
            if(!ErrorCode.SUCCESS.equals(permission)){
                serviceResult.setErrorCode(permission);
                return serviceResult;
            }

            //判断是否确认
            if (BankSlipDetailStatus.CONFIRMED.equals(bankSlipDetailDO.getDetailStatus())) {
                serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_IS_CONFIRMED);
                return serviceResult;
            }

            //需判断这个单子是否下推,未推只有财务管理员操作 ,下推了只有业务员和商务可以操作
            String localizationPermission = verifyLocalizationPermission(bankSlipDO);
            if (!ErrorCode.SUCCESS.equals(localizationPermission)) {
                serviceResult.setErrorCode(localizationPermission);
                return serviceResult;
            }
            boolean isLocalizationFlag = false;
            if (LocalizationType.NOT_LOCALIZATION.equals(bankSlipDetailDO.getIsLocalization()) || bankSlipDetailDO.getIsLocalization() == null || "".equals(bankSlipDetailDO.getIsLocalization())) {
                //总公司的数量和状态的改变
                if (!userSupport.getCurrentUserCompanyId().equals(bankSlip.getLocalizationSubCompanyId())) {
                    bankSlipDO.setLocalizationCount(bankSlipDO.getLocalizationCount() == null ? 1 : bankSlipDO.getLocalizationCount() + 1);
                } else {
                    isLocalizationFlag = true;
                }
            } else {
                //如果总表为总公司和指派的公司为总公司和原来归属公司不为总公司改变数量(只有属地化过才有这种情况)
                if (!userSupport.getCurrentUserCompanyId().equals(bankSlipDetailDO.getSubCompanyId()) && userSupport.getCurrentUserCompanyId().equals(bankSlip.getLocalizationSubCompanyId())) {
                    bankSlipDO.setLocalizationCount(bankSlipDO.getLocalizationCount() - 1);
                    isLocalizationFlag = true;
                }
            }

            //删除认领信息
            if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus()) && LocalizationType.IS_LOCALIZATION.equals(bankSlipDetailDO.getIsLocalization())) {
                List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
                if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
                    //判断是否有充值成功记录,不允许
                    Map<Integer, BankSlipClaimDO> bankSlipClaimDOMap = ListUtil.listToMap(bankSlipClaimDOList, "rechargeStatus");
                    if (bankSlipClaimDOMap.containsKey(RechargeStatus.PAY_SUCCESS) || bankSlipClaimDOMap.containsKey(RechargeStatus.PAYING)) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_CLAIM_PAY_STATUS_ERROR);
                        return serviceResult;
                    }
                    bankSlipClaimMapper.deleteBankSlipClaimDO(userSupport.getCurrentUserId().toString(), now, bankSlipClaimDOList);
                    bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
                    bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() - 1);
                    bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() + 1);
                    if (bankSlipDO.getNeedClaimCount() == 0 && bankSlipDO.getClaimCount() == 0) {
                        bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
                    }
                }
            }

            bankSlipDO.setUpdateTime(now);
            bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            //属地化公司id  属地化状态的改变
            bankSlipDetailDO.setSubCompanyId(bankSlip.getLocalizationSubCompanyId());
            bankSlipDetailDO.setIsLocalization(LocalizationType.IS_LOCALIZATION);
            if (isLocalizationFlag) {
                bankSlipDetailDO.setIsLocalization(LocalizationType.NOT_LOCALIZATION);
            }
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailDO.setUpdateTime(now);
            updateBankSlipDetailDOList.add(bankSlipDetailDO);

            // 添加操作日志
            BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
            bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
            bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.LOCALIZATION);
            bankSlipDetailOperationLogDO.setOperationContent("属地化(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + userSupport.getCurrentUser().getUserName().toString() + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",属地化到公司：" + bankSlipDetailDO.getSubCompanyName());
            bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bankSlipDetailOperationLogDO.setCreateTime(now);
            bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
        }

        //跟新对公流水项
        if (CollectionUtil.isEmpty(updateBankSlipDetailDOList)) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_ASSIGN_IS_NULL);
            return serviceResult;
        }
        bankSlipDetailMapper.updateBankSlipDetailDO(updateBankSlipDetailDOList);
        //跟新总表数据
        List<BankSlipDO> bankSlipDOList = ListUtil.mapToList(bankSlipDOMap);
        bankSlipMapper.updateBankSlipDO(bankSlipDOList);

        // 保存日志list
        bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, BankSlipDetailDO> cancelLocalizationBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, BankSlipDetailDO> serviceResult = new ServiceResult<>();
        if (!userSupport.isHeadUser() && !userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        serviceResult = cancelLocalization(bankSlipDetail);
        return serviceResult;

    }

    private ServiceResult<String, BankSlipDetailDO> cancelLocalization(BankSlipDetail bankSlipDetail) {
        Date now = new Date();
        ServiceResult<String, BankSlipDetailDO> serviceResult = new ServiceResult<>();
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }
        //根据总表
        BankSlipDO headquartersBankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());

        //判断是否是本公司数据和属地化数据
        if(!userSupport.getCurrentUserCompanyId().equals(bankSlipDetailDO.getOwnerSubCompanyId()) && !userSupport.getCurrentUserCompanyId().equals(bankSlipDetailDO.getSubCompanyId())){
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //需判断这个单子是否下推,未推只有财务管理员电销操作 ,下推了只有业务员和商务电销可以操作
        String localizationPermission = verifyLocalizationPermission(headquartersBankSlipDO);
        if (!ErrorCode.SUCCESS.equals(localizationPermission)) {
            serviceResult.setErrorCode(localizationPermission);
            return serviceResult;
        }
        //判断是否是属地化
        if (!LocalizationType.IS_LOCALIZATION.equals(bankSlipDetailDO.getIsLocalization())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_LOCALIZATION);
            return serviceResult;
        }
        //判断是否确认
        if (BankSlipDetailStatus.CONFIRMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_IS_CONFIRMED);
            return serviceResult;
        }

        //总公司表数量 +1,跟新时间和操作人
        headquartersBankSlipDO.setLocalizationCount(headquartersBankSlipDO.getLocalizationCount() - 1);
        if (headquartersBankSlipDO.getLocalizationCount() == 0 && headquartersBankSlipDO.getNeedClaimCount() == 0) {
            headquartersBankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
        }


        //删除认领信息
        if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus()) && LocalizationType.IS_LOCALIZATION.equals(bankSlipDetailDO.getIsLocalization())) {
            List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
            if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
                //判断是否有充值成功记录,不允许
                Map<Integer, BankSlipClaimDO> bankSlipClaimDOMap = ListUtil.listToMap(bankSlipClaimDOList, "rechargeStatus");
                if (bankSlipClaimDOMap.containsKey(RechargeStatus.PAY_SUCCESS) || bankSlipClaimDOMap.containsKey(RechargeStatus.PAYING)) {
                    serviceResult.setErrorCode(ErrorCode.BANK_SLIP_CLAIM_PAY_STATUS_ERROR);
                    return serviceResult;
                }
                bankSlipClaimMapper.deleteBankSlipClaimDO(userSupport.getCurrentUserId().toString(), now, bankSlipClaimDOList);
                bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
                headquartersBankSlipDO.setClaimCount(headquartersBankSlipDO.getClaimCount() - 1);
                headquartersBankSlipDO.setNeedClaimCount(headquartersBankSlipDO.getNeedClaimCount() + 1);
                if (headquartersBankSlipDO.getNeedClaimCount() == 0 && headquartersBankSlipDO.getClaimCount() == 0) {
                    headquartersBankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
                }

            }
        }

        headquartersBankSlipDO.setUpdateTime(now);
        headquartersBankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        //跟改流水项是否属地化状态和分公司id,跟新时间和操作人

        bankSlipDetailDO.setSubCompanyId(CommonConstant.HEADER_COMPANY_ID);
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailDO.setIsLocalization(LocalizationType.NOT_LOCALIZATION);

        bankSlipDetailMapper.update(bankSlipDetailDO);
        bankSlipMapper.update(headquartersBankSlipDO);

        // 添加操作日志
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.CANCEL_LOCALIZATION);
        bankSlipDetailOperationLogDO.setOperationContent("属地化(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(headquartersBankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + userSupport.getCurrentUser().getUserName().toString() + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",属地化到公司：" + bankSlipDetailDO.getSubCompanyName());
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDetailDO);
        return serviceResult;
    }


    @Override
    public ServiceResult<String, BankSlipDetail> queryBankSlipClaim(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, BankSlipDetail> serviceResult = new ServiceResult<>();
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }

        HashMap<String, Object> maps = new HashMap<>();
        BankSlipDetailQueryParam bankSlipDetailQueryParam = new BankSlipDetailQueryParam();
        bankSlipDetailQueryParam.setPayerName(bankSlipDetailDO.getPayerName());
        bankSlipDetailQueryParam.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
        maps.put("bankSlipDetailQueryParam", bankSlipDetailQueryParam);
        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
        List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailMapper.findByPayerNameAndOtherSideAccountNo(maps);
        List<BankSlipClaim> bankSlipClaimList = ConverterUtil.convertList(bankSlipClaimDOList, BankSlipClaim.class);
        bankSlipDetail = new BankSlipDetail();
        bankSlipDetail.setBankSlipClaimList(bankSlipClaimList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDetail);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<BankSlipDetailOperationLog>> pageBankSlipDetailOperationLog(BankSlipDetailOperationLogQueryParam bankSlipDetailOperationLogQueryParam) {
        ServiceResult<String, Page<BankSlipDetailOperationLog>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipDetailOperationLogQueryParam.getPageNo(), bankSlipDetailOperationLogQueryParam.getPageSize());
        Integer departmentType = 0;
        if (userSupport.isFinancePerson() || userSupport.isSuperUser()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isElectric() || userSupport.isChannelSubCompany()) {
            //商务类型设置为2
            departmentType = 2;
        } else if (userSupport.isBusinessPerson()) {
            //业务员类型设置为3
            departmentType = 3;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipDetailOperationLogQueryParam", bankSlipDetailOperationLogQueryParam);
        maps.put("departmentType", departmentType);
        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
        maps.put("currentUser", userSupport.getCurrentUserId().toString());
        Integer totalCount = bankSlipDetailOperationLogMapper.findBankSlipDetailOperationLogCountByParams(maps);
        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = bankSlipDetailOperationLogMapper.findBankSlipDetailOperationLogByParams(maps);

        List<BankSlipDetailOperationLog> bankSlipDetailOperationLogList = ConverterUtil.convertList(bankSlipDetailOperationLogDOList, BankSlipDetailOperationLog.class);
        Page<BankSlipDetailOperationLog> page = new Page<>(bankSlipDetailOperationLogList, totalCount, bankSlipDetailOperationLogQueryParam.getPageNo(), bankSlipDetailOperationLogQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    @Override
    public ServiceResult<String, String> queryUnknownBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        Integer departmentType = 0;
        if (userSupport.isFinancePerson() || userSupport.isSuperUser()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isElectric() || userSupport.isChannelSubCompany()) {
            //商务类型设置为2
            departmentType = 2;
        } else if (userSupport.isBusinessPerson()) {
            //业务员类型设置为3
            departmentType = 3;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("departmentType", departmentType);
        maps.put("bankSlipDetailId", bankSlipDetail.getBankSlipDetailId());
        maps.put("currentUser", userSupport.getCurrentUserId().toString());

        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findUnknownBankSlipDetailById(maps);

        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }
        // 判断是否是总公司人员
        if (!userSupport.isHeadUser()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
        String permission = verifyLocalizationPermission(bankSlipDO);
        if (!ErrorCode.SUCCESS.equals(permission)) {
            serviceResult.setErrorCode(permission);
            return serviceResult;
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    @Override
    public ServiceResult<String, String> unknownBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //todo 指派未知数据
        //todo 判断是否是总公司人员
        if (!userSupport.isHeadUser()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //todo 判断是否是总公司数据
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }
        if (!CommonConstant.HEADER_COMPANY_ID.equals(bankSlipDetailDO.getOwnerSubCompanyId())) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
        //todo 判断是未认领状态
        if (!BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_NOT_UN_CLAIMED);
            return serviceResult;
        }
        //todo 判断是否是没有属地化
        if (!LocalizationType.NOT_LOCALIZATION.equals(bankSlipDetailDO.getIsLocalization())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_IS_LOCALIZATION_OR_UNKNOWN);
            return serviceResult;
        }
        //todo 跟新
        bankSlipDetailDO.setIsLocalization(LocalizationType.UNKNOWN);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailMapper.update(bankSlipDetailDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    @Override
    public ServiceResult<String, Page<BankSlipDetail>> pageUnknownBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam) {
        ServiceResult<String, Page<BankSlipDetail>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());
        Integer departmentType = 0;
        if (userSupport.isFinancePerson() || userSupport.isSuperUser()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isElectric() || userSupport.isChannelSubCompany()) {
            //商务类型设置为2
            departmentType = 2;
        } else if (userSupport.isBusinessPerson()) {
            //业务员类型设置为3
            departmentType = 3;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipDetailQueryParam", bankSlipDetailQueryParam);
        maps.put("departmentType", departmentType);
        maps.put("currentUser", userSupport.getCurrentUserId().toString());
        Integer totalCount = bankSlipDetailMapper.findUnknownBankSlipDetailDOCountByParams(maps);
        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDetailMapper.findUnknownBankSlipDetailDOByParams(maps);

        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
            List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
            BigDecimal lastClaimAmount = bankSlipDetailDO.getTradeAmount();
            for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
                lastClaimAmount = BigDecimalUtil.sub(lastClaimAmount, bankSlipClaimDO.getClaimAmount());
            }
            bankSlipDetailDO.setTradeAmount(lastClaimAmount);
        }

        List<BankSlipDetail> bankSlipDetailList = ConverterUtil.convertList(bankSlipDetailDOList, BankSlipDetail.class);
        Page<BankSlipDetail> page = new Page<>(bankSlipDetailList, totalCount, bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


    private String getBankTypeName(Integer bankType) {
        String bankName = "";
        switch (bankType) {
            case 1:
                bankName = "支付宝";
                break;
            case 2:
                bankName = "中国银行";
                break;
            case 3:
                bankName = "交通银行";
                break;
            case 4:
                bankName = "南京银行";
                break;
            case 5:
                bankName = "农业银行";
                break;
            case 6:
                bankName = "工商银行";
                break;
            case 7:
                bankName = "建设银行";
                break;
            case 8:
                bankName = "平安银行";
                break;
            case 9:
                bankName = "招商银行";
                break;
            case 10:
                bankName = "浦发银行";
                break;
            case 11:
                bankName = "汉口银行";
                break;
        }
        return bankName;
    }

    private String verifyPermission(BankSlipDO bankSlipDO, BankSlipDetailDO bankSlipDetailDO) {

        //当前用户如不是总公司,看是否有权先操作
        if (!userSupport.isHeadUser()) {
            if (!userSupport.getCurrentUserCompanyId().equals(bankSlipDO.getSubCompanyId()) && !userSupport.getCurrentUserCompanyId().equals(bankSlipDetailDO.getSubCompanyId())) {
                return ErrorCode.DATA_HAVE_NO_PERMISSION;
            }
        }
        return ErrorCode.SUCCESS;
    }

    private String verifyPermission(BankSlipDO bankSlipDO) {

        //当前用户如不是总公司,看是否有权先操作
        if (!userSupport.isHeadUser()) {
            if (!userSupport.getCurrentUserCompanyId().equals(bankSlipDO.getSubCompanyId())) {
                return ErrorCode.DATA_HAVE_NO_PERMISSION;
            }
        }
        return ErrorCode.SUCCESS;
    }

    private String verifyLocalizationPermission(BankSlipDO headquartersBankSlipDO) {

        if (SlipStatus.ALREADY_PUSH_DOWN.equals(headquartersBankSlipDO.getSlipStatus())) {
            //是否是财务,业务员,商务
            if (!userSupport.isSuperUser()) {
                if (!userSupport.isBusinessPerson() && !userSupport.isBusinessAffairsPerson() && !userSupport.isFinancePerson() && !userSupport.isElectric() && !userSupport.isChannelSubCompany()) {
                    return ErrorCode.DATA_HAVE_NO_PERMISSION;
                }
            }
        } else if (SlipStatus.INITIALIZE.equals(headquartersBankSlipDO.getSlipStatus())) {
            if (!userSupport.isSuperUser()) {
                if (!userSupport.isFinancePerson() && !userSupport.isElectric() && !userSupport.isChannelSubCompany()) {
                    return ErrorCode.DATA_HAVE_NO_PERMISSION;
                }
            }
        }
        return ErrorCode.SUCCESS;
    }

    private ServiceResult<String, BigDecimal> verifyClaimAmount(List<ClaimParam> claimParamList) {

        ServiceResult<String, BigDecimal> serviceResult = new ServiceResult<>();

        Set<String> customerNoSet = new HashSet<>();
        //认领总金额
        BigDecimal allClaimAmount = BigDecimal.ZERO;
        for (ClaimParam claimParam : claimParamList) {
            CustomerDO customerDO = customerMapper.findByNo(claimParam.getCustomerNo());
            if (customerDO == null) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                return serviceResult;
            }
            customerNoSet.add(claimParam.getCustomerNo());
            BigDecimal claimAmount = claimParam.getClaimAmount();
            allClaimAmount = BigDecimalUtil.add(allClaimAmount, claimParam.getClaimAmount());
            //校验是否保留两位小数
            Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
            Matcher match = pattern.matcher(claimAmount.toString());
            if (!match.matches() || BigDecimalUtil.compare(claimAmount, BigDecimal.ZERO) < 0) {
                serviceResult.setErrorCode(ErrorCode.BANK_SLIP_CLAIM_AMOUNT_IS_FAIL);
                return serviceResult;
            }
        }
        if (customerNoSet.size() != claimParamList.size()) {
            serviceResult.setErrorCode(ErrorCode.CUSTOMER_NAME_REPETITION);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(allClaimAmount);
        return serviceResult;
    }

    private void saveBankSlipClaimDOAndBankSlipDetailOperationLog(BankSlipDO bankSlipDO, BankSlipClaim bankSlipClaim, List<ClaimParam> claimParamList, BankSlipDetailDO bankSlipDetailDO, Long currentTimeMillis, Date now) {

        StringBuffer stringBuffer = new StringBuffer("");
        for (ClaimParam claimParam : claimParamList) {
            BankSlipClaimDO bankSlipClaimDO = ConverterUtil.convert(claimParam, BankSlipClaimDO.class);
            bankSlipClaimDO.setBankSlipDetailId(bankSlipClaim.getBankSlipDetailId());
            bankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
            bankSlipClaimDO.setClaimSerialNo(currentTimeMillis);
            bankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
            bankSlipClaimDO.setRemark(bankSlipClaimDO.getRemark());
            bankSlipClaimDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
            bankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipClaimDO.setCreateTime(now);
            bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipClaimDO.setUpdateTime(now);
            bankSlipClaimMapper.save(bankSlipClaimDO);
            stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "手动认领(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",(认领人：" + userSupport.getCurrentUser().getUserName().toString() + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",认领：" + bankSlipClaimDO.getClaimAmount() + "元)");
        }
        // 添加操作日志
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.CLAIM);
        bankSlipDetailOperationLogDO.setOperationContent(String.valueOf(stringBuffer));
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        // 保存日志list
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);
    }

}
