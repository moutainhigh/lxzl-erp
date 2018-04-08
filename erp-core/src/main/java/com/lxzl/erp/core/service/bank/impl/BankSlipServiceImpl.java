package com.lxzl.erp.core.service.bank.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ConstantConfig;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.ClaimParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.bank.impl.importSlip.*;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipClaimMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
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

    @Override
    public ServiceResult<String, Page<BankSlip>> pageBankSlip(BankSlipQueryParam bankSlipQueryParam) {
        ServiceResult<String, Page<BankSlip>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

        Integer departmentType = 0;
        if (userSupport.isFinancePerson() || userSupport.isSuperUser()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isBusinessPerson()) {
            //商务和业务员类型设置为2
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
        //校验上传流水月份是否超过当月
        if (bankSlip.getSlipMonth().getTime() - DateUtil.getMonthByCurrentOffset(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD).getTime() > 0) {
            serviceResult.setErrorCode(ErrorCode.OVERSTEP_CURRENT_MONTH);
            return serviceResult;
        }
        Integer bankType = bankSlip.getBankType();
        bankSlip.setSlipMonth(DateUtil.getMonthByOffset(bankSlip.getSlipMonth(), CommonConstant.COMMON_ZERO));
        if (!BankType.BOC_BANK.equals(bankType) &&
                !BankType.TRAFFIC_BANK.equals(bankType) &&
                !BankType.NAN_JING_BANK.equals(bankType) &&
                !BankType.AGRICULTURE_BANK.equals(bankType) &&
                !BankType.ICBC_BANK.equals(bankType) &&
                !BankType.CCB_BANK.equals(bankType) &&
                !BankType.PING_AN_BANK.equals(bankType) &&
                !BankType.CMBC_BANK.equals(bankType) &&
                !BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType) &&
                !BankType.ALIPAY.equals(bankType)) {
            serviceResult.setErrorCode(ErrorCode.BANK_TYPE_IS_FAIL);
            return serviceResult;
        }
        //分公司一个月不通银行只能导入一次

        BankSlipDO bankSlipDO = bankSlipMapper.findBySubCompanyIdAndMonthAndBankType(bankSlip.getSubCompanyId(), bankSlip.getSlipMonth(), bankType);
        if (bankSlipDO != null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_EXISTS);
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
        }
        if (ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            //跟新 需要认领数据
            bankSlipDO = result.getResult();
            List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDO.getBankSlipDetailDOList();
            //查一边是否是 流入金额
            ArrayList<BankSlipDetailDO> newBankSlipDetailDOList = new ArrayList<>();
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
            Map<String, BankSlipDetailDO> newBankSlipDetailDOMap = ListUtil.listToMap(newBankSlipDetailDOList, "otherSideAccountNo");

            //认领数据批量跟新
            List<BankSlipClaimDO> bankSlipClaimDOList = new ArrayList<>();
            //对公流水项批量跟新
            int claimCount = 0;
            for (String otherSideAccountNo : newBankSlipDetailDOMap.keySet()) {
                //如果是就创建一条认领数据
                if (bankSlipClaimDOMap.containsKey(otherSideAccountNo)) {
                    BankSlipClaimDO bankSlipClaimDO = bankSlipClaimDOMap.get(otherSideAccountNo);
                    bankSlipClaimDO.setId(null);
                    bankSlipClaimDO.setBankSlipDetailId(newBankSlipDetailDOMap.get(otherSideAccountNo).getId());
                    bankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
                    bankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
                    bankSlipClaimDO.setCreateTime(now);
                    bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    bankSlipClaimDO.setUpdateTime(now);
                    bankSlipClaimDOList.add(bankSlipClaimDO);
                    //改变流水项状态
                    BankSlipDetailDO newBankSlipDetailDO = newBankSlipDetailDOMap.get(otherSideAccountNo);
                    newBankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
                    //已认领数量
                    claimCount = claimCount + 1;
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
                bankSlipDetailMapper.updateConfirmBankDetailDO(newBankSlipDetailDOList);
                bankSlipMapper.update(bankSlipDO);
            }
        }
        serviceResult.setErrorCode(result.getErrorCode());
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<BankSlipDetail>> pageBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam) {
        ServiceResult<String, Page<BankSlipDetail>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());

        Integer departmentType = 0;
        if (userSupport.isFinancePerson() || userSupport.isSuperUser()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isBusinessPerson()) {
            //商务和业务员类型设置为2
            departmentType = 2;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipDetailQueryParam", bankSlipDetailQueryParam);
        maps.put("departmentType", departmentType);
        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());

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
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlip.getBankSlipId());
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> ignoreBankSlipDetail(BankSlipDetail bankSlipDetail) {
        Date now = new Date();
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //银行对公流水项是否存在
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }
        //校验流水总表状态是否下推，如果未下推，则商务和业务员不可以操作
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
        if (SlipStatus.INITIALIZE.equals(bankSlipDO.getSlipStatus())) {
            if (!userSupport.isFinancePerson() && !userSupport.isSuperUser()) {
                serviceResult.setErrorCode(ErrorCode.CURRENT_ROLES_NOT_PERMISSION);
                return serviceResult;
            }
        }

        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }

        //判断是否是收入状态
        if (!LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_INCOME);
            return serviceResult;
        }

        //状态是否为未认领
        if (!BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_NOT_UN_CLAIMED);
            return serviceResult;
        }
        //明细状态改变为忽略
        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.IGNORE);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailMapper.update(bankSlipDetailDO);

        //总表需认领数量-1
        int newNeedClaimCount = bankSlipDO.getNeedClaimCount() - 1;

        bankSlipDO.setNeedClaimCount(newNeedClaimCount);
        bankSlipMapper.update(bankSlipDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(bankSlipDetail.getBankSlipDetailId());
        return serviceResult;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> claimBankSlipDetail(BankSlipClaim bankSlipClaim) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        List<ClaimParam> claimParamList = bankSlipClaim.getClaimParam();
        if (CollectionUtil.isEmpty(claimParamList)) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_NEED_CLAIMED);
            return serviceResult;
        }
        for (ClaimParam claimParam : claimParamList) {
            BigDecimal claimAmount = claimParam.getClaimAmount();
            Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
            Matcher match = pattern.matcher(claimAmount.toString());
            if (!match.matches() || BigDecimalUtil.compare(claimAmount, BigDecimal.ZERO) < 0) {
                serviceResult.setErrorCode(ErrorCode.BANK_SLIP_CLAIM_AMOUNT_IS_FAIL);
                return serviceResult;
            }
        }


        //判断是否有银行对公流水项
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipClaim.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }
        //校验流水总表状态是否下推，如果未下推，则商务和业务员不可以操作
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());

        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }

        if (SlipStatus.INITIALIZE.equals(bankSlipDO.getSlipStatus())) {
            if (!userSupport.isFinancePerson() && !userSupport.isSuperUser()) {
                serviceResult.setErrorCode(ErrorCode.CURRENT_ROLES_NOT_PERMISSION);
                return serviceResult;
            }
        }
        //判断银行对公流水项状态(已确认)
        if (BankSlipDetailStatus.CONFIRMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_IS_CONFIRMED);
            return serviceResult;
        }

        //判断是否是收入状态
        if (!LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_INCOME);
            return serviceResult;
        }

        //判断客户是否存在
        BigDecimal allClaimAmount = new BigDecimal(0);

        //判断客户是否相等
        if (claimParamList.size() > 1) {
            if (ListUtil.listToMap(claimParamList, "customerNo").size() != claimParamList.size()) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_NAME_REPETITION);
                return serviceResult;
            }
        }

        //判断如果有已支付金额就不允许修改
        int payFail = 0;
        int paySuccess = 0;
        List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
        for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
            if (RechargeStatus.PAY_FAIL.equals(bankSlipClaimDO.getRechargeStatus())) {
                payFail = payFail + 1;
            }
            if (RechargeStatus.PAY_SUCCESS.equals(bankSlipClaimDO.getRechargeStatus())) {
                paySuccess = paySuccess + 1;
            }
        }

        if (payFail != 0 && paySuccess != 0) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_CLAIM_NEED_PAY_SUCCESS);
            return serviceResult;
        }

        for (ClaimParam claimParam : claimParamList) {
            CustomerDO customerDO = customerMapper.findByNo(claimParam.getCustomerNo());
            if (customerDO == null) {
                serviceResult.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                return serviceResult;
            }
            allClaimAmount = BigDecimalUtil.add(allClaimAmount, claimParam.getClaimAmount());
        }

        //判断总共金额是否相等
        if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) != 0) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_UNEQUAL_CURRENT_AGGREGATE_AMOUNT);
            return serviceResult;
        }

        //这里是保存银行对公流水认领表 数据
        serviceResult = updateClaim(bankSlipDetailDO, bankSlipClaim, claimParamList, now);
        return serviceResult;
    }

    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateClaim(BankSlipDetailDO bankSlipDetailDO, BankSlipClaim bankSlipClaim, List<ClaimParam> claimParamList, Date now) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //如果状态为未认领状态
        BankSlipClaimDO bankSlipClaimDO = ConverterUtil.convert(bankSlipClaim, BankSlipClaimDO.class);

        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
        if (BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            //跟新银行对公流水已认领笔数
            bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() - 1);
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + 1);

        } else if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            //状态为已认领状态
            //以前的全部删除 添加新的
            bankSlipClaimMapper.updateDataStatusByBankSlipDetailId(bankSlipDetailDO.getId(), userSupport.getCurrentUserId().toString(), now);
        } else if (BankSlipDetailStatus.IGNORE.equals(bankSlipDetailDO.getDetailStatus())) {
            //状态为忽略状态,已认领笔数+1
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + 1);
        }
        bankSlipDO.setUpdateTime(now);
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipMapper.update(bankSlipDO);
        long currentTimeMillis = System.currentTimeMillis();
        for (ClaimParam claimParam : claimParamList) {
            bankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
            bankSlipClaimDO.setClaimAmount(claimParam.getClaimAmount());
            bankSlipClaimDO.setCustomerNo(claimParam.getCustomerNo());
            bankSlipClaimDO.setClaimSerialNo(currentTimeMillis);
            bankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
            bankSlipClaimDO.setDataStatus(CommonConstant.COMMON_CONSTANT_YES);
            bankSlipClaimDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipClaimDO.setCreateTime(now);
            bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipClaimDO.setUpdateTime(now);
            bankSlipClaimMapper.save(bankSlipClaimDO);
        }
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
        //是否为已经下推 或者为 部分确认
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

        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
            List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
            if (CollectionUtil.isEmpty(bankSlipClaimDOList)) {
                continue;
            }
            boolean paySuccessFlag = true;
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
                        } else {
                            //银行对公流水认领表 失败 改变状态
                            bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
                            paySuccessFlag = false;
                        }
                    } catch (Exception e) {
                        logger.error("------------------充值出错----------------------", e);
                        //银行对公流水认领表 失败 改变状态
                        bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
                        paySuccessFlag = false;
                    }
                    bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    bankSlipClaimDO.setUpdateTime(now);
                    newDankSlipClaimDOList.add(bankSlipClaimDO);
                }
            }
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
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }
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
        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
            bankSlipDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            bankSlipDetailDO.setUpdateTime(now);
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        }

        //删除所有项
        bankSlipDetailMapper.deleteByBankSlipId(bankSlipDetailDOList);
        //删除总表
        bankSlipDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        bankSlipDO.setUpdateTime(now);
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipMapper.update(bankSlipDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> hideBankSlipDetail(BankSlipDetail bankSlipDetail) {
        //校验权限可隐藏
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //是否有权下推
        if (!userSupport.isFinancePerson() && !userSupport.isSuperUser()) {
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

        if (!BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus()) && !BankSlipDetailStatus.IGNORE.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_DISPLAY);
            return serviceResult;
        }
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }

        if (SlipStatus.ALL_CLAIM.equals(bankSlipDO.getSlipStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_IS_ALL_CLAIM);
            return serviceResult;
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
        if (!userSupport.isFinancePerson() && !userSupport.isSuperUser()) {
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
        String verifyPermission = verifyPermission(bankSlipDO);
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

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
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

}
