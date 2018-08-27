package com.lxzl.erp.core.service.bank.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ConstantConfig;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.*;
import com.lxzl.erp.common.domain.bank.pojo.*;
import com.lxzl.erp.common.domain.bank.pojo.dto.BankSipAutomaticClaimDTO;
import com.lxzl.erp.common.domain.payment.*;
import com.lxzl.erp.common.domain.payment.account.pojo.ChargeRecord;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.bank.impl.importSlip.support.BankSlipSupport;
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
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
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
    private static Logger logger = LoggerFactory.getLogger(BankSlipServiceImpl.class);


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
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private ImportBankSlip importBankSlip;

    @Autowired
    private BankSlipDetailOperationLogMapper bankSlipDetailOperationLogMapper;

    @Autowired
    private BankSlipSupport bankSlipSupport;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;

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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> saveBankSlip(BankSlip bankSlip) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        //判断当前用户是否是本分公司的(总公司除外)
        if (userSupport.isElectric() || userSupport.isChannelSubCompany()) {
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
                !BankType.HAN_KOU_BANK.equals(bankType) &&
                !BankType.STOCK_CASH.equals(bankType) &&
                !BankType.JING_DONG.equals(bankType) &&
                !BankType.CHINA_UNION_PAY_TYPE.equals(bankType)) {
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
        ServiceResult<String, BankSlipDO> result = importBankSlip.saveBankSlip(bankSlip, inputStream, bankType);
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
        Integer departmentType = bankSlipSupport.departmentType();
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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
                bankSlipDetailOperationLogDO.setOperationContent("下推操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",下推人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + "，下推时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> claimBankSlipDetail(BankSlipClaim bankSlipClaim) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //判断是否有银行对公流水项
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipClaim.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }

        //判断是否是公海状态，是的话不能认领
        if(CommonConstant.COMMON_TWO.equals(bankSlipDetailDO.getIsLocalization())){
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_IS_UNKNOWN_CANNOT_OPERATION);
            return serviceResult;
        }

        List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());

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

        // 数据库已认领金额
        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
            for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
                if (!RechargeStatus.INITIALIZE.equals(bankSlipClaimDO.getRechargeStatus()) &&
                        !RechargeStatus.PAY_FAIL.equals(bankSlipClaimDO.getRechargeStatus())) {
                    serviceResult.setErrorCode(ErrorCode.BANK_SLIP_CLAIM_PAY_STATUS_ERROR);
                    return serviceResult;
                }
            }
        }

        List<ClaimParam> claimParamList = bankSlipClaim.getClaimParam();
        ServiceResult<String, BigDecimal> result = verifyClaimAmount(claimParamList);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            serviceResult.setErrorCode(result.getErrorCode());
            return serviceResult;
        }
        //获取到需要认领数据总和
        BigDecimal allClaimAmount = result.getResult();

        if (CollectionUtil.isNotEmpty(claimParamList)) {
//            if (LocalizationType.UNKNOWN.equals(bankSlipDetailDO.getIsLocalization())) {
//                //-----------以下是未知的情况---------------
//                // 如果是总公司
//                if (CommonConstant.HEADER_COMPANY_ID.equals(bankSlipDetailDO.getOwnerSubCompanyId())) {
//                    // 判断是否都认领完成 和是否是未知数据
//                    return saveOrUpdateUnknownBankSlipDetail(bankSlipDetailDO, claimParamList, bankSlipClaim, bankSlipDO, allClaimAmount, now);
//                } else {
//                    serviceResult.setErrorCode(ErrorCode.DATA_STATUS_ERROR);
//                    return serviceResult;
//                }
//                //-----------以上是未知的情况---------------
//            } else {
                if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
                    //如果不是未知状态判断当前用户是否是认领创建人
                    if (!userSupport.getCurrentUserId().toString().equals(bankSlipClaimDOList.get(0).getCreateUser())) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_DETAIL_STATUS_IS_CLAIMED);
                        return serviceResult;
                    }
                }
                //判断总共金额是否相等
                if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) != 0) {
                    serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_UNEQUAL_CURRENT_AGGREGATE_AMOUNT);
                    return serviceResult;
                }
                //这里是保存银行对公流水认领表 数据
                serviceResult = updateClaim(bankSlipDO, bankSlipDetailDO, bankSlipClaim, claimParamList, now);
//            }
        } else {
//            if (LocalizationType.UNKNOWN.equals(bankSlipDetailDO.getIsLocalization())) {
//                //传过参数为空时需要删除认领数据
//                return deleteUnknownBankSlipClaim(now, bankSlipClaimDOList, bankSlipDetailDO, bankSlipDO);
//            } else {
                if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
                    //如果不是未知状态判断当前用户是否是认领创建人或者是商务
                    if (!userSupport.getCurrentUserId().toString().equals(bankSlipClaimDOList.get(0).getCreateUser()) && !userSupport.isBusinessAffairsPerson()) {
                        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_DETAIL_STATUS_IS_CLAIMED);
                        return serviceResult;
                    }
                }
                //传过参数为空时需要删除认领数据
                return deleteBankSlipClaim(now, bankSlipClaimDOList, bankSlipDetailDO, bankSlipDO);
            }
//        }
        return serviceResult;
    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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
        Integer amount = 0;

        List<BankSlipClaimDO> newDankSlipClaimDOList = new ArrayList<>();

        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
        Map<BankSlipDetailDO, List<BankSlipClaimDO>> payMap = new HashMap<>();

        PublicTransferPlusChargeParam param = new PublicTransferPlusChargeParam();
        List<ChargeRequestParam> chargeRequests = new ArrayList<>();
        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
            //校验认领金额总和是否等于充值金额
            if (!verifyClaimAmount(bankSlipDetailDO)) {
                serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_UNEQUAL_CURRENT_AGGREGATE_AMOUNT);
                return serviceResult;
            }
            if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
                List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
                if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
                    for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
                        //可以充值
                        if (!RechargeStatus.PAY_SUCCESS.equals(bankSlipClaimDO.getRechargeStatus())) {
                            //封装加款到客户账号的参数
                            ChargeRequestParam chargeRequestParam = new ChargeRequestParam();
                            chargeRequestParam.setBusinessCustomerNo(bankSlipClaimDO.getCustomerNo());
                            chargeRequestParam.setChargeAmount(bankSlipClaimDO.getClaimAmount());
                            chargeRequestParam.setChargeRemark(bankSlipClaimDO.getRemark());
                            chargeRequests.add(chargeRequestParam);
                            //保存加款的资金流水详情和认领列表
                            if (!payMap.containsKey(bankSlipDetailDO)) {
                                List<BankSlipClaimDO> list = new ArrayList<>();
                                list.add(bankSlipClaimDO);
                                payMap.put(bankSlipDetailDO, list);
                            } else {
                                payMap.get(bankSlipDetailDO).add(bankSlipClaimDO);
                            }
                        }
                    }
                }
            }
        }
        param.setChargeRequests(chargeRequests);
        if (CollectionUtil.isNotEmpty(param.getChargeRequests())) {
            try {
                //调用批量加款接口
                ServiceResult<String, Boolean> result = paymentService.publicTransferPlusCharge(param);
                if (ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    amount = successMethod(bankSlipDO, now, amount, newDankSlipClaimDOList, bankSlipDetailOperationLogDOList, payMap);
                } else {
                    notSuccessMethod(bankSlipDO, now, newDankSlipClaimDOList, bankSlipDetailOperationLogDOList, payMap);
                }
            } catch (Exception e) {
                logger.error("------------------充值出错----------------------", e);
                notSuccessMethod(bankSlipDO, now, newDankSlipClaimDOList, bankSlipDetailOperationLogDOList, payMap);
            }
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
        //以下是批量更新操作 更新对公流水项  和  认领表
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

    /**
     * 批量加款成功后更新资金流水明细、认领表，添加操作日志
     *
     * @param bankSlipDO
     * @param now
     * @param amount
     * @param newDankSlipClaimDOList
     * @param bankSlipDetailOperationLogDOList
     * @param payMap
     * @return
     */
    private Integer successMethod(BankSlipDO bankSlipDO, Date now, Integer amount, List<BankSlipClaimDO> newDankSlipClaimDOList, List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList, Map<BankSlipDetailDO, List<BankSlipClaimDO>> payMap) {
        for (BankSlipDetailDO bankSlipDetailDO : payMap.keySet()) {
            amount += 1;
            bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CONFIRMED);
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailDO.setUpdateTime(now);
            List<BankSlipClaimDO> successBankSlipClaimDOList = payMap.get(bankSlipDetailDO);

            Set<String> customerNoSet = new HashSet<>();
            for (BankSlipClaimDO bankSlipClaimDO : successBankSlipClaimDOList) {
                customerNoSet.add(bankSlipClaimDO.getCustomerNo());
            }

            Map<String, CustomerCompanyDO> customerCompanyNoMap = importBankSlip.getCustomerCompanyNoMap(customerNoSet);

            StringBuffer stringBuffer = new StringBuffer("");
            for (BankSlipClaimDO bankSlipClaimDO : successBankSlipClaimDOList) {
                //银行对公流水认领表 成功 改变状态
                bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_SUCCESS);
                bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setUpdateTime(now);
                newDankSlipClaimDOList.add(bankSlipClaimDO);
                stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",客户名称：" + (customerCompanyNoMap == null ? "":customerCompanyNoMap.get(bankSlipClaimDO.getCustomerNo()).getCompanyName()) + ",充值时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",充值：" + bankSlipClaimDO.getClaimAmount() + "元，成功！！！");
            }
            //添加日志
            BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
            bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
            bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.RECHARGE);
            bankSlipDetailOperationLogDO.setOperationContent(String.valueOf(stringBuffer));
            bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bankSlipDetailOperationLogDO.setCreateTime(now);
            bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
        }
        //返回确认的资金流水明细数量
        return amount;
    }

    /**
     * 批量加款失败或报错后更新资金流水明细、认领表，添加操作日志
     *
     * @param bankSlipDO
     * @param now
     * @param newDankSlipClaimDOList
     * @param bankSlipDetailOperationLogDOList
     * @param payMap
     */
    private void notSuccessMethod(BankSlipDO bankSlipDO, Date now, List<BankSlipClaimDO> newDankSlipClaimDOList, List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList, Map<BankSlipDetailDO, List<BankSlipClaimDO>> payMap) {
        for (BankSlipDetailDO bankSlipDetailDO : payMap.keySet()) {
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailDO.setUpdateTime(now);
            List<BankSlipClaimDO> successBankSlipClaimDOList = payMap.get(bankSlipDetailDO);

            Set<String> customerNoSet = new HashSet<>();
            for (BankSlipClaimDO bankSlipClaimDO : successBankSlipClaimDOList) {
                customerNoSet.add(bankSlipClaimDO.getCustomerNo());
            }

            Map<String, CustomerCompanyDO> customerCompanyNoMap = importBankSlip.getCustomerCompanyNoMap(customerNoSet);

            StringBuffer stringBuffer = new StringBuffer("");
            for (BankSlipClaimDO bankSlipClaimDO : successBankSlipClaimDOList) {
                //银行对公流水认领表 失败 改变状态
                bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
                bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setUpdateTime(now);
                newDankSlipClaimDOList.add(bankSlipClaimDO);
                stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",客户编号："  + (customerCompanyNoMap == null ? "":customerCompanyNoMap.get(bankSlipClaimDO.getCustomerNo()).getCompanyName()) + ",充值时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",充值：" + bankSlipClaimDO.getClaimAmount() + "元，失败！！！");
            }
            //添加日志
            BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
            bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
            bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.RECHARGE);
            bankSlipDetailOperationLogDO.setOperationContent(String.valueOf(stringBuffer));
            bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bankSlipDetailOperationLogDO.setCreateTime(now);
            bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
        }
    }

    @Override
    public ServiceResult<String, BankSlipDetail> queryBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, BankSlipDetail> serviceResult = new ServiceResult<>();
        Integer departmentType = bankSlipSupport.departmentType();
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> deleteBankSlip(BankSlip bankSlip) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        Integer bankSlipId = bankSlip.getBankSlipId();
        BankSlipDO bankSlipDO = bankSlipMapper.findDetailById(bankSlipId);
        if (!(String.valueOf(userSupport.getCurrentUserId())).equals(bankSlipDO.getCreateUser()) && !userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.DATA_HAVE_NO_PERMISSION);
            return serviceResult;
        }
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
            bankSlipDetailOperationLogDO.setOperationContent("删除银行流水操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",删除人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",删除时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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
        //判断是否是未知状态
        if(CommonConstant.COMMON_TWO.equals(bankSlipDetailDO.getIsLocalization())){
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_IS_UNKNOWN_CANNOT_OPERATION);
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
        //总公司影藏时为属地化的话取消属地化
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
        bankSlipDetailOperationLogDO.setOperationContent("影藏操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",隐藏人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",隐藏时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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

        //判断是否是未知状态
        if(CommonConstant.COMMON_TWO.equals(bankSlipDetailDO.getIsLocalization())){
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_IS_UNKNOWN_CANNOT_OPERATION);
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
        if (!SlipStatus.INITIALIZE.equals(bankSlipDO.getSlipStatus())) {
            bankSlipDO.setSlipStatus(SlipStatus.ALREADY_PUSH_DOWN);
        }
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDO.setUpdateTime(now);
        bankSlipMapper.update(bankSlipDO);

        // 添加操作日志
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.DISPLAY);
        bankSlipDetailOperationLogDO.setOperationContent("显示操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",显示人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",显示时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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
            if(!CommonConstant.COMMON_TWO.equals(bankSlipDetailDO.getIsLocalization())){
                String permission = verifyPermission(bankSlipDO, bankSlipDetailDO);
                if (!ErrorCode.SUCCESS.equals(permission)) {
                    serviceResult.setErrorCode(permission);
                    return serviceResult;
                }
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
            if(!LocalizationType.UNKNOWN.equals(bankSlipDetailDO.getIsLocalization()) ){
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
            }else {
                //如果属地化到数据归属公司
                if (!bankSlipDetailDO.getSubCompanyId().equals(bankSlip.getLocalizationSubCompanyId())) {
                    bankSlipDO.setLocalizationCount(bankSlipDO.getLocalizationCount() + 1);
                }else {
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
                    if (!SlipStatus.INITIALIZE.equals(bankSlipDO.getSlipStatus())) {
                        if (bankSlipDO.getNeedClaimCount() == 0 && bankSlipDO.getClaimCount() == 0) {
                            bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
                        }
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
            bankSlipDetailOperationLogDO.setOperationContent("属地化(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",属地化到公司：" + subCompanyDO.getSubCompanyName());
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, BankSlipDetailDO> cancelLocalizationBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, BankSlipDetailDO> serviceResult = cancelLocalization(bankSlipDetail);
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
        if (!userSupport.getCurrentUserCompanyId().equals(bankSlipDetailDO.getOwnerSubCompanyId()) && !userSupport.getCurrentUserCompanyId().equals(bankSlipDetailDO.getSubCompanyId())) {
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
                if (!SlipStatus.INITIALIZE.equals(headquartersBankSlipDO.getSlipStatus())) {
                    if (headquartersBankSlipDO.getNeedClaimCount() == 0 && headquartersBankSlipDO.getClaimCount() == 0) {
                        headquartersBankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
                    }
                }
            }
        }

        headquartersBankSlipDO.setUpdateTime(now);
        headquartersBankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        //跟改流水项是否属地化状态和分公司id,跟新时间和操作人

        bankSlipDetailDO.setSubCompanyId(bankSlipDetailDO.getOwnerSubCompanyId());
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailDO.setIsLocalization(LocalizationType.NOT_LOCALIZATION);

        bankSlipDetailMapper.update(bankSlipDetailDO);
        bankSlipMapper.update(headquartersBankSlipDO);

        // 添加操作日志
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.CANCEL_LOCALIZATION);
        bankSlipDetailOperationLogDO.setOperationContent("属地化(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(headquartersBankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",属地化到公司：" + bankSlipDetailDO.getSubCompanyName());
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
        Integer departmentType = bankSlipSupport.departmentType();
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipDetailOperationLogQueryParam", bankSlipDetailOperationLogQueryParam);
        maps.put("departmentType", departmentType);
        maps.put("currentUser", userSupport.getCurrentUserId().toString());
        Integer totalCount = bankSlipDetailOperationLogMapper.findBankSlipDetailOperationLogCountByParams(maps);
        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = bankSlipDetailOperationLogMapper.findBankSlipDetailOperationLogByParams(maps);

        List<BankSlipDetailOperationLog> bankSlipDetailOperationLogList = ConverterUtil.convertList(bankSlipDetailOperationLogDOList, BankSlipDetailOperationLog.class);
        Page<BankSlipDetailOperationLog> page = new Page<>(bankSlipDetailOperationLogList, totalCount, bankSlipDetailOperationLogQueryParam.getPageNo(), bankSlipDetailOperationLogQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }


//    @Override
//    public ServiceResult<String, BankSlipDetail> queryUnknownBankSlipDetail(BankSlipDetail bankSlipDetail) {
//        ServiceResult<String, BankSlipDetail> serviceResult = new ServiceResult<>();
//
//        Integer departmentType = bankSlipSupport.departmentType();
//        Map<String, Object> maps = new HashMap<>();
//        maps.put("departmentType", departmentType);
//        maps.put("bankSlipDetailId", bankSlipDetail.getBankSlipDetailId());
//        maps.put("currentUser", userSupport.getCurrentUserId().toString());
//        maps.put("subCompanyId", userSupport.getCurrentUserCompanyId());
//        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findUnknownBankSlipDetailById(maps);
//
//        if (bankSlipDetailDO == null) {
//            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
//            return serviceResult;
//        }
//
//        List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
//        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
//            BigDecimal claimAmount = new BigDecimal(0);
//            for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
//                claimAmount = BigDecimalUtil.add(claimAmount, bankSlipClaimDO.getClaimAmount());
//            }
//            bankSlipDetailDO.setTradeAmount(BigDecimalUtil.sub(bankSlipDetailDO.getTradeAmount(), claimAmount));
//        }
//
//        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
//        String permission = verifyLocalizationPermission(bankSlipDO);
//        if (!ErrorCode.SUCCESS.equals(permission)) {
//            serviceResult.setErrorCode(permission);
//            return serviceResult;
//        }
//
//        serviceResult.setErrorCode(ErrorCode.SUCCESS);
//        serviceResult.setResult(ConverterUtil.convert(bankSlipDetailDO, BankSlipDetail.class));
//        return serviceResult;
//    }


    @Override
    public ServiceResult<String, String> unknownBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();

        //数据是否存在
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }

        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());

        String localizationPermission = verifyLocalizationPermission(bankSlipDO);
        if(!localizationPermission.equals(ErrorCode.SUCCESS)){
            serviceResult.setErrorCode(localizationPermission);
            return serviceResult;
        }

        //判断是否是未知
        if (LocalizationType.UNKNOWN.equals(bankSlipDetailDO.getIsLocalization())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_IS_UNKNOWN);
            return serviceResult;
        }

        //影藏
        if (BankSlipDetailStatus.HIDE.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.UNKNOWN_BANK_SLIP_DETAIL_NOT_DISPLAY);
            return serviceResult;
        }

        //判断是否是否是认领
        if (!BankSlipDetailStatus.UN_CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.UNKNOWN_BANK_SLIP_DETAIL_NOT_UNCLAIM);
            return serviceResult;
        }

        //判断是否是否是认领和已认领状态
        if (!CommonConstant.COMMON_CONSTANT_NO.equals(bankSlipDetailDO.getIsLocalization())) {
            serviceResult.setErrorCode(ErrorCode.UNKNOWN_BANK_SLIP_DETAIL_NOT_LOCALIZATION);
            return serviceResult;
        }

        //跟新
        bankSlipDetailDO.setIsLocalization(LocalizationType.UNKNOWN);
        bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailMapper.update(bankSlipDetailDO);

        //保存操作记录
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.UNKNOWN);
        bankSlipDetailOperationLogDO.setOperationContent("属地化到流水公海--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

//    @Override
//    public ServiceResult<String, Page<BankSlipDetail>> pageUnknownBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam) {
//        ServiceResult<String, Page<BankSlipDetail>> result = new ServiceResult<>();
//        PageQuery pageQuery = new PageQuery(bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());
//        Integer departmentType = bankSlipSupport.departmentType();
//        Map<String, Object> maps = new HashMap<>();
//        maps.put("start", pageQuery.getStart());
//        maps.put("pageSize", pageQuery.getPageSize());
//        maps.put("bankSlipDetailQueryParam", bankSlipDetailQueryParam);
//        maps.put("departmentType", departmentType);
//        maps.put("currentUser", userSupport.getCurrentUserId().toString());
//        Integer totalCount = bankSlipDetailMapper.findUnknownBankSlipDetailDOCountByParams(maps);
//        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDetailMapper.findUnknownBankSlipDetailDOByParams(maps);
//        if (CollectionUtil.isNotEmpty(bankSlipDetailDOList)) {
//            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
//                List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
//                BigDecimal lastClaimAmount = bankSlipDetailDO.getTradeAmount();
//                for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
//                    lastClaimAmount = BigDecimalUtil.sub(lastClaimAmount, bankSlipClaimDO.getClaimAmount());
//                }
//                bankSlipDetailDO.setTradeAmount(lastClaimAmount);
//            }
//        }
//
//        List<BankSlipDetail> bankSlipDetailList = ConverterUtil.convertList(bankSlipDetailDOList, BankSlipDetail.class);
//        Page<BankSlipDetail> page = new Page<>(bankSlipDetailList, totalCount, bankSlipDetailQueryParam.getPageNo(), bankSlipDetailQueryParam.getPageSize());
//
//        result.setErrorCode(ErrorCode.SUCCESS);
//        result.setResult(page);
//        return result;
//    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> confirmBankSlipDetail(BankSlipDetail bankSlipDetail) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        Date now = new Date();
        BankSlipDetailDO bankSlipDetailDO = bankSlipDetailMapper.findById(bankSlipDetail.getBankSlipDetailId());
        if (bankSlipDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_NOT_EXISTS);
            return serviceResult;
        }

        //资金流水是否是已认领状态
        if (!BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_STATUS_CAN_NOT_CONFIRM);
            return serviceResult;
        }
        BankSlipDO bankSlipDO = bankSlipMapper.findById(bankSlipDetailDO.getBankSlipId());
        //是否为已经下推
        if (!SlipStatus.ALREADY_PUSH_DOWN.equals(bankSlipDO.getSlipStatus())) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_STATUS_NOT_ALREADY_PUSH_DOWN);
            return serviceResult;
        }
        //当前用户如不是总公司,看是否有权先操作
        String verifyPermission = verifyPermission(bankSlipDetailDO);
        if (!ErrorCode.SUCCESS.equals(verifyPermission)) {
            serviceResult.setErrorCode(verifyPermission);
            return serviceResult;
        }
        //是否为商务
        if (!userSupport.isBusinessAffairsPerson() && !userSupport.isSuperUser() && !userSupport.isElectric() && !userSupport.isChannelSubCompany()) {
            serviceResult.setErrorCode(ErrorCode.IS_NOT_BUSINESS_AFFAIRS_PERSON);
            return serviceResult;
        }

        List<BankSlipClaimDO> newBankSlipClaimDOList = new ArrayList<>();
        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
        List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
        //校验认领金额总和是否等于充值金额
        if (!verifyClaimAmount(bankSlipDetailDO)) {
            serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_UNEQUAL_CURRENT_AGGREGATE_AMOUNT);
            return serviceResult;
        }
//        boolean paySuccessFlag = bankSlipSupport.paymentClaim(bankSlipClaimDOList, newDankSlipClaimDOList, bankSlipDetailOperationLogDOList, bankSlipDO, bankSlipDetailDO, now);
        StringBuffer stringBuffer = new StringBuffer("确认操作:");

        Set<String> customerNoSet = new HashSet<>();
        for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
            customerNoSet.add(bankSlipClaimDO.getCustomerNo());
        }

        Map<String, CustomerCompanyDO> customerCompanyNoMap = importBankSlip.getCustomerCompanyNoMap(customerNoSet);

        for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
            //充值成功不需要再冲
            if (!RechargeStatus.PAY_SUCCESS.equals(bankSlipClaimDO.getRechargeStatus())) {
                //加款到客户账号
                ManualChargeParam manualChargeParam = new ManualChargeParam();
                manualChargeParam.setBusinessCustomerNo(bankSlipClaimDO.getCustomerNo());
                manualChargeParam.setChargeAmount(bankSlipClaimDO.getClaimAmount());
                //todo 这里的备注可能换成其他字段
                manualChargeParam.setChargeRemark(bankSlipClaimDO.getRemark());
                try {
                    ServiceResult<String, Boolean> result = paymentService.manualCharge(manualChargeParam);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        //银行对公流水认领表 失败 改变状态
                        bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
                        stringBuffer.append("客户名称[" + (customerCompanyNoMap == null ? "":customerCompanyNoMap.get(bankSlipClaimDO.getCustomerNo()).getCompanyName()) + "],充值：" + bankSlipClaimDO.getClaimAmount() + "元，充值失败。\n");
                    } else {
                        bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_SUCCESS);
                        stringBuffer.append("客户名称[" + (customerCompanyNoMap == null ? "":customerCompanyNoMap.get(bankSlipClaimDO.getCustomerNo()).getCompanyName()) + "],充值：" + bankSlipClaimDO.getClaimAmount() + "元，充值成功。\n");
                    }
                } catch (Exception e) {
                    //银行对公流水认领表 失败 改变状态
                    bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
                    stringBuffer.append("客户名称[" + (customerCompanyNoMap == null ? "":customerCompanyNoMap.get(bankSlipClaimDO.getCustomerNo()).getCompanyName()) + "],充值：" + bankSlipClaimDO.getClaimAmount() + "元，支付系统异常" + e.getMessage() + "，充值失败。\n");
                }
                bankSlipClaimDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bankSlipClaimDO.setUpdateTime(now);
                newBankSlipClaimDOList.add(bankSlipClaimDO);
            }
            // 添加操作日志
            BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
            bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
            bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.RECHARGE);
            bankSlipDetailOperationLogDO.setOperationContent(String.valueOf(stringBuffer));
            bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bankSlipDetailOperationLogDO.setCreateTime(now);
            bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
        }

        //银行对公流水明细记录变为已确定
        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CONFIRMED);

        // 保存日志list
        if (CollectionUtil.isNotEmpty(bankSlipDetailOperationLogDOList)) {
            bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);
        }
        bankSlipClaimMapper.updateBankSlipClaimDO(newBankSlipClaimDOList);
        bankSlipDetailMapper.update(bankSlipDetailDO);
        //改变已经确认个数  再判断认领个数
        bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() - 1);
        if (bankSlipDO.getNeedClaimCount() == 0 && bankSlipDO.getClaimCount() == 0) {
            bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
        }
        bankSlipDO.setConfirmCount(bankSlipDO.getConfirmCount() + 1);
        bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        bankSlipDO.setUpdateTime(now);
        bankSlipMapper.update(bankSlipDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addOnlineHistoryBankSlip(AddOnlineBankSlipQueryParam addOnlineBankSlipQueryParam) throws ParseException {
        //传入查询参数
        ChargeRecordPageParam param = new ChargeRecordPageParam();
        param.setChannelNo(addOnlineBankSlipQueryParam.getChannelNo());
        param.setPageNo(CommonConstant.COMMON_ONE);
        param.setPageSize(Integer.MAX_VALUE);
        param.setChargeStatus(ChargeStatus.PAY_SUCCESS);
        param.setQueryStartTime(addOnlineBankSlipQueryParam.getStartTime());
        param.setQueryEndTime(addOnlineBankSlipQueryParam.getEndTime());
        param.setSubCompanyId(addOnlineBankSlipQueryParam.getSubCompanyId());
        param.setBusinessCustomerNo(addOnlineBankSlipQueryParam.getCustomerNo());
        param.setChargeOrderNo(addOnlineBankSlipQueryParam.getChargeOrderNo());

        //调接口查询结果
        ServiceResult<String, Page<ChargeRecord>> result = paymentService.queryChargeRecordParamPage(param);
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        if (result.getResult() == null) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_NULL);
            return serviceResult;
        }
        List<ChargeRecord> itemList = result.getResult().getItemList();

        if (CollectionUtil.isEmpty(itemList)) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_NULL);
            return serviceResult;
        }
        //过滤已有的数据
        Map<String, ChargeRecord> chargeRecordMap = new HashMap<>();
        for (ChargeRecord chargeRecord : itemList) {
            if (!ChargeType.MANUAL_CHARGE.equals(chargeRecord.getChargeType()) && !ChargeType.BANK_SLIP_CHARGE.equals(chargeRecord.getChargeType()) && !ChargeType.BALANCE_PAID.equals(chargeRecord.getChargeType())) {
                chargeRecordMap.put(chargeRecord.getThirdPartyPayOrderId(), chargeRecord);
            }
        }

        List<BankSlipDetailDO> bankSlipDetailDOList;
        if (chargeRecordMap.size() > 0) {
            bankSlipDetailDOList = bankSlipDetailMapper.findBankSlipDetailByTradeSerialNoList(new ArrayList<>(chargeRecordMap.keySet()));
        } else {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_NULL);
            return serviceResult;
        }

        if (CollectionUtil.isNotEmpty(bankSlipDetailDOList)) {
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                if (chargeRecordMap.containsKey(bankSlipDetailDO.getTradeSerialNo())) {
                    chargeRecordMap.remove(bankSlipDetailDO.getTradeSerialNo());
                }
            }
        }

        if (chargeRecordMap.size() <= 0) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_NULL);
            return serviceResult;
        }
        itemList = ListUtil.mapToList(chargeRecordMap);

        //保存过滤完成的数据
        for (ChargeRecord chargeRecord : itemList) {
            saveChargeRecordToBankSlip(chargeRecord);
        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 流水认领明细列表
     *
     * @Author : sunzhipeng
     */
    @Override
    public ServiceResult<String, BankSlipClaimPage> pageBankSlipClaimDetail(BankSlipClaimDetailQueryParam bankSlipClaimDetailQueryParam) {
        ServiceResult<String, BankSlipClaimPage> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipClaimDetailQueryParam.getPageNo(), bankSlipClaimDetailQueryParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        //查询已认领，但是确认时间和确认人条件不为空时给回空集合；查询不是已认领，确认时间和确认人条件不为空时将查询条件设置为查询已确认
        if (BankSlipDetailStatus.CLAIMED.equals(bankSlipClaimDetailQueryParam.getDetailStatus())
                && (bankSlipClaimDetailQueryParam.getStartClaimUpdateTime() != null
                || bankSlipClaimDetailQueryParam.getEndClaimUpdateTime() != null
                || StringUtil.isNotEmpty(bankSlipClaimDetailQueryParam.getClaimUpdateUserName()))) {
            BankSlipClaimPage bankSlipClaimPage = new BankSlipClaimPage();
            List<BankSlipClaimDetail> bankSlipClaimDetailList = new ArrayList<>();
            Page<BankSlipClaimDetail> page = new Page<>(bankSlipClaimDetailList, 0, bankSlipClaimDetailQueryParam.getPageNo(), bankSlipClaimDetailQueryParam.getPageSize());
            bankSlipClaimPage.setBankSlipClaimDetailPage(page);
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(bankSlipClaimPage);
            return result;
        } else if (bankSlipClaimDetailQueryParam.getStartClaimUpdateTime() != null
                || bankSlipClaimDetailQueryParam.getEndClaimUpdateTime() != null
                || StringUtil.isNotEmpty(bankSlipClaimDetailQueryParam.getClaimUpdateUserName())) {
            bankSlipClaimDetailQueryParam.setDetailStatus(BankSlipDetailStatus.CONFIRMED);
        }


        maps.put("bankSlipClaimDetailQueryParam", bankSlipClaimDetailQueryParam);
        Integer userSubCompanyId = null;
        if (!userSupport.isHeadUser()) {
            userSubCompanyId = userSupport.getCurrentUserCompanyId();
        }
        maps.put("userSubCompanyId", userSubCompanyId);
        BankSlipClaimPage bankSlipClaimPage = bankSlipClaimMapper.findBankSlipClaimPageCountAndAmountByParams(maps);
        List<BankSlipClaimDetail> bankSlipClaimDetailList = bankSlipClaimMapper.findBankSlipClaimDetailByParams(maps);
        for (BankSlipClaimDetail bankSlipClaimDetail : bankSlipClaimDetailList) {
            if (!BankSlipDetailStatus.CONFIRMED.equals(bankSlipClaimDetail.getDetailStatus())) {
//                bankSlipClaimDetail.setSlipDetailUpdateUserName("");
//                bankSlipClaimDetail.setSlipDetailUpdateUser("");
//                bankSlipClaimDetail.setSlipDetailUpdateTime(null);
                bankSlipClaimDetail.setClaimUpdateUserName("");
                bankSlipClaimDetail.setClaimUpdateUser("");
                bankSlipClaimDetail.setClaimUpdateTime(null);

            }
        }

        Page<BankSlipClaimDetail> page = new Page<>(bankSlipClaimDetailList, bankSlipClaimPage.getClaimCount(), bankSlipClaimDetailQueryParam.getPageNo(), bankSlipClaimDetailQueryParam.getPageSize());
        bankSlipClaimPage.setBankSlipClaimDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(bankSlipClaimPage);
        return result;
    }

    /**
     * @param
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     * @描述
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void automaticUnknownBankSlipDetail() {
        //查询总公司所有已当前时间起三天未认领数据
        Date now = new Date();
        Date threeDaysAgo = DateUtil.getDayByCurrentOffset(-3);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm;ss");
        String format = simpleDateFormat.format(threeDaysAgo);
        System.out.println(format);

        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDetailMapper.findByThreeDaysAgo(threeDaysAgo);
        String userId = CommonConstant.SUPER_USER_ID.toString();
        if (CollectionUtil.isNotEmpty(bankSlipDetailDOList)) {
            List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
            for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
                bankSlipDetailDO.setIsLocalization(CommonConstant.COMMON_TWO);
                bankSlipDetailDO.setUpdateUser(userId);
                bankSlipDetailDO.setUpdateTime(now);

                //保存操作记录
                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.UNKNOWN);
                bankSlipDetailOperationLogDO.setOperationContent("属地化到流水公海--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：系统  ，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                bankSlipDetailOperationLogDO.setCreateTime(now);
                bankSlipDetailOperationLogDO.setCreateUser(userId);
                bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);

            }
            //保存操作日志
            bankSlipDetailMapper.updateUnknownBankSlipDetailById(bankSlipDetailDOList);
            //保存操作日志
            bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> addOnlineBankSlip(AddOnlineBankSlipQueryParam addOnlineBankSlipQueryParam) throws ParseException {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        ChargeRecordPageParam param = new ChargeRecordPageParam();
        param.setPageNo(CommonConstant.COMMON_ONE);
        param.setPageSize(Integer.MAX_VALUE);
        param.setChargeStatus(ChargeStatus.PAY_SUCCESS);
        param.setChargeOrderNo(addOnlineBankSlipQueryParam.getChargeOrderNo());

        ServiceResult<String, Page<ChargeRecord>> result = paymentService.queryChargeRecordParamPage(param);
        if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
            serviceResult.setErrorCode(result.getErrorCode());
            return serviceResult;
        }

        if (CollectionUtil.isEmpty(result.getResult().getItemList())) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_NOT_EXISTS);
            return serviceResult;
        }

        if (result.getResult().getItemList().size() != 1) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_DATA_FAIL);
            return serviceResult;
        }

        return saveChargeRecordToBankSlip(result.getResult().getItemList().get(0));

    }

    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    ServiceResult<String, String> saveChargeRecordToBankSlip(ChargeRecord chargeRecord) throws ParseException {

        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Date now = new Date();
        String userId = CommonConstant.SUPER_USER_ID.toString();

        //过滤手动充值,余额充值,对公加款
        if (ChargeType.MANUAL_CHARGE.equals(chargeRecord.getChargeType())) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_TYPE_IS_MANUAL_CHARGE);
            return serviceResult;
        }
        if (ChargeType.BALANCE_PAID.equals(chargeRecord.getChargeType())) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_TYPE_IS_BALANCE_PAID);
            return serviceResult;
        }
        if (ChargeType.BANK_SLIP_CHARGE.equals(chargeRecord.getChargeType())) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_TYPE_IS_BANK_SLIP_CHARGE);
            return serviceResult;
        }

        //过滤绑定过的客户(已经打款无需再次认领打款)
        CustomerDO customerDO = customerMapper.findByNo(chargeRecord.getBusinessCustomerNo());
        if (customerDO != null) {
            serviceResult.setErrorCode(ErrorCode.BINDING_CUSTOMER_NO);
            return serviceResult;
        }

        //判断是否有重复数据
        BankSlipDetailDO dbBankSlipDetailDO = bankSlipDetailMapper.findBankSlipDetailByTradeSerialNo(chargeRecord.getThirdPartyPayOrderId());
        if (dbBankSlipDetailDO != null) {
            serviceResult.setErrorCode(ErrorCode.CHARGE_RECORD_IS_EXIST);
            return serviceResult;
        }

        //导入时间统一
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(chargeRecord.getChargeTime() == null ? new Date() : chargeRecord.getChargeTime());
        Date chargeTime = simpleDateFormat.parse(format);

        Integer channelType;
        if (ChannelType.LYCHEE_PAY.equals(chargeRecord.getChannelType())) {
            channelType = BankType.LYCHEE_PAY;
        } else if (ChannelType.SWIFT_PASS.equals(chargeRecord.getChannelType())) {
            channelType = BankType.SWIFT_PASS;
        } else {
            channelType = BankType.UNKNOWN_CHANNEL_PAY_TYPE;
        }

        //保存流水总纪录
        BankSlipDO bankSlipDO = bankSlipMapper.findBySubCompanyIdAndDayAndBankType(chargeRecord.getSubCompanyId(), channelType, chargeTime);
        if (bankSlipDO != null) {
            bankSlipDO.setInCount(bankSlipDO.getInCount() + 1);   //进款笔数
            bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() + 1);   //需认领笔数
            if (SlipStatus.ALL_CLAIM.equals(bankSlipDO.getSlipStatus())) {
                bankSlipDO.setSlipStatus(SlipStatus.ALREADY_PUSH_DOWN);
            }
            bankSlipDO.setUpdateUser(userId);
            bankSlipDO.setUpdateTime(now);
            //跟新数据
            bankSlipMapper.update(bankSlipDO);
        } else {
            //保存银行流水
            bankSlipDO = new BankSlipDO();
            bankSlipDO.setSubCompanyId(chargeRecord.getSubCompanyId());   //分公司ID
            bankSlipDO.setSubCompanyName(chargeRecord.getSubCompanyName());   //分公司名称
            bankSlipDO.setBankType(channelType);   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行,11-汉口银行,12-快付通,13-库存现金,14-威富通
            bankSlipDO.setSlipDay(chargeTime);   //导入日期
            bankSlipDO.setInCount(CommonConstant.COMMON_ONE);   //进款笔数
            bankSlipDO.setNeedClaimCount(CommonConstant.COMMON_ONE);
            bankSlipDO.setClaimCount(CommonConstant.COMMON_ZERO);
            bankSlipDO.setConfirmCount(CommonConstant.COMMON_ZERO);   //已确认笔数
            bankSlipDO.setSlipStatus(SlipStatus.INITIALIZE);   //单据状态：0-初始化，1-已下推，2-部分认领，3-全部认领
            bankSlipDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);   //状态：0不可用；1可用；2删除
            bankSlipDO.setCreateTime(now);   //添加时间
            bankSlipDO.setCreateUser(userId);   //添加人
            bankSlipDO.setUpdateTime(now);   //修改时间
            bankSlipDO.setUpdateUser(userId);   //修改人
            bankSlipDO.setLocalizationCount(CommonConstant.COMMON_ZERO);  //属地化数量
            bankSlipMapper.save(bankSlipDO);
        }

        //保存流水项记录
        BankSlipDetailDO bankSlipDetailDO = new BankSlipDetailDO();
        bankSlipDetailDO.setPayerName(chargeRecord.getCustomerName());       //付款人名称
        bankSlipDetailDO.setTradeAmount(chargeRecord.getChargeAmountReal());       //交易金额
        bankSlipDetailDO.setTradeSerialNo(chargeRecord.getThirdPartyPayOrderId());       //交易流水号
        bankSlipDetailDO.setTradeTime(chargeTime);       //交易日期
        bankSlipDetailDO.setTradeMessage(chargeRecord.getRemark());       //交易附言
        bankSlipDetailDO.setOtherSideAccountNo(StringUtil.isEmpty(chargeRecord.getBusinessCustomerNo()) ? "-1" : chargeRecord.getBusinessCustomerNo());       //对方账号
        bankSlipDetailDO.setMerchantOrderNo(null);       //商户订单号
        bankSlipDetailDO.setLoanSign(LoanSignType.INCOME);       //借贷标志,1-贷（收入），2-借（支出）
        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);       //明细状态，1-未认领，2-已认领，3-已确定，4-忽略
        bankSlipDetailDO.setDetailJson(null);       //明细json数据
        bankSlipDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);       //状态：0不可用；1可用；2删除
        bankSlipDetailDO.setRemark(chargeRecord.getRemark());       //备注
        bankSlipDetailDO.setBankSlipId(bankSlipDO.getId());       //银行对公流水表id
        bankSlipDetailDO.setSubCompanyId(chargeRecord.getSubCompanyId() == null ? 1 : chargeRecord.getSubCompanyId());      //属地化分公司ID
        bankSlipDetailDO.setIsLocalization(CommonConstant.NO);       //是否已属地化,0-否，1-是[总公司时有值]
        bankSlipDetailDO.setCreateUser(userId);
        bankSlipDetailDO.setCreateTime(now);
        bankSlipDetailDO.setUpdateUser(userId);
        bankSlipDetailDO.setUpdateTime(now);
        bankSlipDetailMapper.save(bankSlipDetailDO);

        List<BankSlipDetailDO> bankSlipDetailDOList = new ArrayList<>();
        bankSlipDetailDOList.add(bankSlipDetailDO);
        bankSlipDO.setBankSlipDetailDOList(bankSlipDetailDOList);
        //自动认领和自动属地化
        ServiceResult<String, BankSlipDO> result = new ServiceResult<>();
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(bankSlipDO);
        importBankSlip.automaticClaimAndLocalization(result);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

//    /**
//     * 自动认领和自动归属化
//     *
//     * @param : result
//     * @Author : XiaoLuYu
//     * @Date : Created in 2018/5/26 11:31
//     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
//     */
//    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
//    void automaticClaimAndLocalization(BankSlipDO bankSlipDO) {
//        Date now = new Date();
//        String userId = CommonConstant.SUPER_USER_ID.toString();
//
//        //跟新 需要认领数据
//        List<BankSlipDetailDO> bankSlipDetailDOList = bankSlipDO.getBankSlipDetailDOList();
//        //查一边是否是 流入金额
//        List<BankSlipDetailDO> newBankSlipDetailDOList = new ArrayList<>();
//
//        // 查询出导入时间的所有本公司的所有导入数据
//        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
//            if (LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
//                newBankSlipDetailDOList.add(bankSlipDetailDO);
//            }
//        }
//
//        //查询一边所有本银行认领 只有一条认领数据的认领数据
//        List<BankSlipClaimDO> dbBankSlipClaimDOList = bankSlipClaimMapper.findBankSlipClaimPaySuccess();
//
//        //数据库的当前银行所有的已确认的银行流水项
//        Map<String, BankSlipClaimDO> bankSlipClaimDOMap = ListUtil.listToMap(dbBankSlipClaimDOList, "otherSideAccountNo");
//
//        //上传的当前银行所有的已确认的银行流水项
//        Map<Integer, BankSlipDetailDO> newBankSlipDetailDOMap = ListUtil.listToMap(newBankSlipDetailDOList, "id");
//
//        //认领数据批量跟新
//        List<BankSlipClaimDO> bankSlipClaimDOList = new ArrayList<>();
//
//        //没有认领的数据
//        List<BankSlipDetailDO> lastBankSlipDetailDOList = new ArrayList<>();
//
//        //第二步没有认领的数据
//        List<BankSlipDetailDO> lastTwoBankSlipDetailDOList = new ArrayList<>();
//
//        //没有认领的数据
//        List<BankSlipDetailOperationLogDO> bankSlipDetailOperationLogDOList = new ArrayList<>();
//        //对公流水项批量跟新
//        int claimCount = 0;
//        for (Integer id : newBankSlipDetailDOMap.keySet()) {
//            //如果是就创建一条认领数据
//            BankSlipDetailDO bankSlipDetailDO = newBankSlipDetailDOMap.get(id);
//            String otherSideAccountNo = bankSlipDetailDO.getOtherSideAccountNo();
//            if (bankSlipClaimDOMap.containsKey(otherSideAccountNo)) {
//                BankSlipClaimDO bankSlipClaimDO = bankSlipClaimDOMap.get(otherSideAccountNo);
//                BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
//                newBankSlipClaimDO.setBankSlipDetailId(id);
//                newBankSlipClaimDO.setOtherSideAccountNo(otherSideAccountNo);
//                newBankSlipClaimDO.setCustomerNo(bankSlipClaimDO.getCustomerNo());
//                newBankSlipClaimDO.setCustomerName(bankSlipClaimDO.getCustomerName());
//                newBankSlipClaimDO.setClaimAmount(bankSlipDetailDO.getTradeAmount());
//                newBankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
//                newBankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
//                newBankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//                newBankSlipClaimDO.setCreateUser(userId);
//                newBankSlipClaimDO.setCreateTime(now);
//                newBankSlipClaimDO.setUpdateUser(userId);
//                newBankSlipClaimDO.setUpdateTime(now);
//                bankSlipClaimDOList.add(newBankSlipClaimDO);
//                //改变流水项状态
//                bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
//                //已认领数量
//                claimCount = claimCount + 1;
//                // 添加已有认领数据自动认领操作日志
//                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
//                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
//                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_CLAIM);
//                bankSlipDetailOperationLogDO.setOperationContent("自动认领(已有的认领数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + BankSlipSupport.getBankTypeName(bankSlipDO.getBankType()) + "）--银行对公流水明细id：" + id + ",认领人：" + "系统" + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户名称：" + bankSlipClaimDO.getCustomerNo() + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
//                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//                bankSlipDetailOperationLogDO.setCreateTime(now);
//                bankSlipDetailOperationLogDO.setCreateUser(userId);
//                bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
//            } else {
//                lastBankSlipDetailDOList.add(bankSlipDetailDO);
//            }
//        }
//
//
//        //自动认领最新一次记录
//        if (CollectionUtil.isNotEmpty(lastBankSlipDetailDOList)) {
//            List<CustomerCompanyDO> customerCompanyDOList = new ArrayList<>();
//            for (BankSlipDetailDO bankSlipDetailDO : lastBankSlipDetailDOList) {
//                if (!StringUtil.isEmpty(bankSlipDetailDO.getPayerName())) {
//                    String simpleName = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
//                    CustomerCompanyDO customerCompanyDO = new CustomerCompanyDO();
//                    customerCompanyDO.setSimpleCompanyName(simpleName);
//                    customerCompanyDOList.add(customerCompanyDO);
//                }
//            }
//            if (CollectionUtil.isNotEmpty(customerCompanyDOList)) {
//                //List<CustomerCompanyDO> dbCustomerCompanyDOList = customerCompanyMapper.findCustomerCompanyByName(customerCompanyDOList);
//                List<BankSipAutomaticClaimDTO> bankSipAutomaticClaimDTOList = bankSlipClaimMapper.findBankSlipClaimPaySuccessByName(customerCompanyDOList);
//                if (CollectionUtil.isNotEmpty(bankSipAutomaticClaimDTOList)) {
//                    Map<String, BankSipAutomaticClaimDTO> bankSipAutomaticClaimDTOMap = this.bankSipAutomaticClaimToMap(bankSipAutomaticClaimDTOList);
//                    Iterator<BankSlipDetailDO> iter = lastBankSlipDetailDOList.iterator();
//                    nameIsNull:
//                    while (iter.hasNext()) {
//                        BankSlipDetailDO bankSlipDetailDO = iter.next();
//                        String simple = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
//                        if (StringUtil.isEmpty(simple)) {
//                            iter.remove();
//                            continue nameIsNull;
//                        }
//                        if (bankSipAutomaticClaimDTOMap.containsKey(simple)) {
//                            BankSipAutomaticClaimDTO bankSipAutomaticClaimDTO = bankSipAutomaticClaimDTOMap.get(simple);
//
//                            BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
//                            newBankSlipClaimDO.setBankSlipDetailId(bankSlipDetailDO.getId());
//                            newBankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
//                            newBankSlipClaimDO.setCustomerNo(bankSipAutomaticClaimDTO.getCompanyNo());
//                            newBankSlipClaimDO.setCustomerName(bankSipAutomaticClaimDTO.getCompanyName());
//                            newBankSlipClaimDO.setClaimAmount(bankSlipDetailDO.getTradeAmount());
//                            newBankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
//                            newBankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
//                            newBankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//                            newBankSlipClaimDO.setCreateUser(userId);
//                            newBankSlipClaimDO.setCreateTime(now);
//                            newBankSlipClaimDO.setUpdateUser(userId);
//                            newBankSlipClaimDO.setUpdateTime(now);
//                            bankSlipClaimDOList.add(newBankSlipClaimDO);
//                            //改变流水项状态
//                            bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
//                            //已认领数量
//                            claimCount = claimCount + 1;
//                            // 添加操作日志
//                            BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
//                            bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
//                            bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_CLAIM);
//                            bankSlipDetailOperationLogDO.setOperationContent("自动认领(付款人和已有的客户相同数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + BankSlipSupport.getBankTypeName(bankSlipDO.getBankType()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",认领人：" + "系统" + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户编号：" + bankSipAutomaticClaimDTO.getCompanyNo() + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
//                            bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//                            bankSlipDetailOperationLogDO.setCreateTime(now);
//                            bankSlipDetailOperationLogDO.setCreateUser(userId);
//                            bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
//                        } else {
//                            lastTwoBankSlipDetailDOList.add(bankSlipDetailDO);
//                            iter.remove();
//                        }
//                    }
//                } else {
//                    lastTwoBankSlipDetailDOList = lastBankSlipDetailDOList;
//                }
//            } else {
//                lastTwoBankSlipDetailDOList = lastBankSlipDetailDOList;
//            }
//        }
//
//        //自动认领付款人名称和已有的公司简单名称相同的数据
//        if (CollectionUtil.isNotEmpty(lastTwoBankSlipDetailDOList)) {
//            List<CustomerCompanyDO> customerCompanyDOList = new ArrayList<>();
//            for (BankSlipDetailDO bankSlipDetailDO : lastTwoBankSlipDetailDOList) {
//                String simpleName = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
//                CustomerCompanyDO customerCompanyDO = new CustomerCompanyDO();
//                customerCompanyDO.setSimpleCompanyName(simpleName);
//                customerCompanyDOList.add(customerCompanyDO);
//            }
//            List<CustomerCompanyDO> dbCustomerCompanyDOList = customerCompanyMapper.findCustomerCompanyByName(customerCompanyDOList);
//            if (CollectionUtil.isNotEmpty(dbCustomerCompanyDOList)) {
//                Map<String, CustomerCompanyDO> dbCustomerCompanyDOMap = ListUtil.listToMap(dbCustomerCompanyDOList, "simpleCompanyName");
//                Iterator<BankSlipDetailDO> iter = lastTwoBankSlipDetailDOList.iterator();
//                isNameNull:
//                while (iter.hasNext()) {
//                    BankSlipDetailDO bankSlipDetailDO = iter.next();
//                    String simple = StrReplaceUtil.nameToSimple(bankSlipDetailDO.getPayerName());
//                    if (StringUtil.isEmpty(simple)) {
//                        iter.remove();
//                        continue isNameNull;
//                    }
//                    if (dbCustomerCompanyDOMap.containsKey(simple)) {
//                        CustomerCompanyDO customerCompanyDO = dbCustomerCompanyDOMap.get(simple);
//                        BankSlipClaimDO newBankSlipClaimDO = new BankSlipClaimDO();
//                        newBankSlipClaimDO.setBankSlipDetailId(bankSlipDetailDO.getId());
//                        newBankSlipClaimDO.setOtherSideAccountNo(bankSlipDetailDO.getOtherSideAccountNo());
//                        newBankSlipClaimDO.setCustomerNo(customerCompanyDO.getCustomerNo());
//                        newBankSlipClaimDO.setCustomerName(customerCompanyDO.getCompanyName());
//                        newBankSlipClaimDO.setClaimAmount(bankSlipDetailDO.getTradeAmount());
//                        newBankSlipClaimDO.setClaimSerialNo(System.currentTimeMillis());
//                        newBankSlipClaimDO.setRechargeStatus(RechargeStatus.INITIALIZE);
//                        newBankSlipClaimDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//                        newBankSlipClaimDO.setCreateUser(userId);
//                        newBankSlipClaimDO.setCreateTime(now);
//                        newBankSlipClaimDO.setUpdateUser(userId);
//                        newBankSlipClaimDO.setUpdateTime(now);
//                        bankSlipClaimDOList.add(newBankSlipClaimDO);
//                        //改变流水项状态
//                        bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
//                        //已认领数量
//                        claimCount = claimCount + 1;
//                        // 添加操作日志
//                        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
//                        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
//                        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_CLAIM);
//                        bankSlipDetailOperationLogDO.setOperationContent("自动认领(付款人和已有的客户相同数据过滤)(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + BankSlipSupport.getBankTypeName(bankSlipDO.getBankType()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",认领人：" + "系统" + "，认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",客户编号：" + customerCompanyDO.getCustomerNo() + ",认领：" + newBankSlipClaimDO.getClaimAmount() + "元");
//                        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//                        bankSlipDetailOperationLogDO.setCreateTime(now);
//                        bankSlipDetailOperationLogDO.setCreateUser(userId);
//                        bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
//                    } else {
//                        lastTwoBankSlipDetailDOList.add(bankSlipDetailDO);
//                        iter.remove();
//                    }
//                }
//            }
//        }
//
//        //改变总表的已认领数量和未认领数量
//        bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() - claimCount);
//        bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + claimCount);
//
//        //创建各个数据批量存储
//        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
//            bankSlipClaimMapper.saveBankSlipClaimDO(bankSlipClaimDOList);
//        }
//        if (CollectionUtil.isNotEmpty(newBankSlipDetailDOList)) {
//            bankSlipDetailMapper.updateBankSlipDetailDO(newBankSlipDetailDOList);
//            bankSlipMapper.update(bankSlipDO);
//        }
//        //跟新付款人名称和已有的公司简单名称相同的数据 的流水详情表状态
//        if (CollectionUtil.isNotEmpty(lastBankSlipDetailDOList)) {
//            bankSlipDetailMapper.updateBankSlipDetailDO(lastBankSlipDetailDOList);
//        }
//
//        //如果是总公司才操作
//        List<BankSlipDetailDO> headerBankSlipDetailDOList = new ArrayList<>();
//        //查询所有已归属的总公司数据
//        List<BankSlipDetailDO> localizationBankSlipDetailDOList = bankSlipDetailMapper.findLocalizationBankSlipDetailDO();
//        Map<String, BankSlipDetailDO> localizationBankSlipDetailDOMap = ListUtil.listToMap(localizationBankSlipDetailDOList, "otherSideAccountNo");
//        int localizationCount = 0;
//        for (Integer key : newBankSlipDetailDOMap.keySet()) {
//            BankSlipDetailDO bankSlipDetailDO = newBankSlipDetailDOMap.get(key);
//            String otherSideAccountNo = bankSlipDetailDO.getOtherSideAccountNo();
//            if (localizationBankSlipDetailDOMap.containsKey(otherSideAccountNo)) {
//                BankSlipDetailDO dbBankSlipDetailDO = localizationBankSlipDetailDOMap.get(otherSideAccountNo);
//                bankSlipDetailDO.setSubCompanyId(dbBankSlipDetailDO.getSubCompanyId());
//                bankSlipDetailDO.setIsLocalization(CommonConstant.COMMON_CONSTANT_YES);
//                bankSlipDetailDO.setUpdateUser(userId);
//                bankSlipDetailDO.setUpdateTime(now);
//                localizationCount++;
//                headerBankSlipDetailDOList.add(bankSlipDetailDO);
//                // 添加操作日志
//                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
//                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
//                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.MOTION_LOCALIZATION);
//                bankSlipDetailOperationLogDO.setOperationContent("自动属地化(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + ",银行：" + BankSlipSupport.getBankTypeName(bankSlipDO.getBankType()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",属地化人：" + "系统" + "，属地化时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",属地化到公司：" + dbBankSlipDetailDO.getSubCompanyName());
//                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
//
//                bankSlipDetailOperationLogDO.setCreateTime(now);
//                bankSlipDetailOperationLogDO.setCreateUser(userId);
//                bankSlipDetailOperationLogDOList.add(bankSlipDetailOperationLogDO);
//            }
//        }
//        if (CollectionUtil.isNotEmpty(headerBankSlipDetailDOList)) {
//            bankSlipDetailMapper.updateSubCompanyAndIsLocalization(headerBankSlipDetailDOList);
//        }
//        bankSlipDO.setLocalizationCount((bankSlipDO.getLocalizationCount() == null ? 0 : bankSlipDO.getLocalizationCount()) + localizationCount);
//        bankSlipMapper.update(bankSlipDO);
//
//        // 保存日志list
//        if (CollectionUtil.isNotEmpty(bankSlipDetailOperationLogDOList)) {
//            bankSlipDetailOperationLogMapper.saveBankSlipDetailOperationLogDOList(bankSlipDetailOperationLogDOList);
//        }
//    }

    /**
     * 当前用户如不是总公司,看是否有权先操作 （总表数据和属地化数据）
     *
     * @param : bankSlipDO
     * @param : bankSlipDetailDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:49
     * @Return : java.lang.String
     */
    private String verifyPermission(BankSlipDO bankSlipDO, BankSlipDetailDO bankSlipDetailDO) {

        //当前用户如不是总公司,看是否有权先操作
        if (!userSupport.isHeadUser()) {
            if (!userSupport.getCurrentUserCompanyId().equals(bankSlipDO.getSubCompanyId()) && !userSupport.getCurrentUserCompanyId().equals(bankSlipDetailDO.getSubCompanyId())) {
                return ErrorCode.DATA_HAVE_NO_PERMISSION;
            }
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 当前用户如不是总公司,看是否有权先操作  （总表数据）
     *
     * @param : bankSlipDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:48
     * @Return : java.lang.String
     */
    private String verifyPermission(BankSlipDO bankSlipDO) {

        //当前用户如不是总公司,看是否有权先操作
        if (!userSupport.isHeadUser()) {
            if (!userSupport.getCurrentUserCompanyId().equals(bankSlipDO.getSubCompanyId())) {
                return ErrorCode.DATA_HAVE_NO_PERMISSION;
            }
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 当前用户如不是总公司,看是否有权先操作  （总表数据）
     *
     * @param : bankSlipDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:48
     * @Return : java.lang.String
     */
    private String verifyPermission(BankSlipDetailDO bankSlipDetailDO) {

        //当前用户如不是总公司,看是否有权先操作
        if (!userSupport.isHeadUser()) {
            if (!userSupport.getCurrentUserCompanyId().equals(bankSlipDetailDO.getSubCompanyId())) {
                return ErrorCode.DATA_HAVE_NO_PERMISSION;
            }
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 需判断这个单子是否下推,未推只有财务管理员操作 ,下推了只有业务员和商务可以操作
     *
     * @param : headquartersBankSlipDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:48
     * @Return : java.lang.String
     */
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

    /**
     * 校验认领数据
     *
     * @param : claimParamList
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:04
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.math.BigDecimal>
     */
    private ServiceResult<String, BigDecimal> verifyClaimAmount(List<ClaimParam> claimParamList) {

        ServiceResult<String, BigDecimal> serviceResult = new ServiceResult<>();
        if (CollectionUtil.isEmpty(claimParamList)) {
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(BigDecimal.ZERO);
            return serviceResult;
        }
        Set<String> customerNoSet = new HashSet<>();
        //认领总金额
        BigDecimal allClaimAmount = BigDecimal.ZERO;
        for (ClaimParam claimParam : claimParamList) {
            if (BigDecimalUtil.compare(BigDecimal.ZERO, claimParam.getClaimAmount()) == 0) {
                serviceResult.setErrorCode(ErrorCode.AMOUNT_MAST_MORE_THEN_ZERO);
                return serviceResult;
            }
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

    /**
     * 保存认领信息和保存操作记录
     *
     * @param : bankSlipDO
     * @param : bankSlipClaim
     * @param : claimParamList
     * @param : bankSlipDetailDO
     * @param : currentTimeMillis
     * @param : now
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:03
     * @Return : void
     */
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    void saveBankSlipClaimDOAndBankSlipDetailOperationLog(BankSlipDO bankSlipDO, BankSlipClaim bankSlipClaim, List<ClaimParam> claimParamList, BankSlipDetailDO bankSlipDetailDO, Long currentTimeMillis, Date now) {

        StringBuffer stringBuffer = new StringBuffer("");
        Set<String> hashSet = new HashSet<>();
        for (ClaimParam claimParam : claimParamList) {
            hashSet.add(claimParam.getCustomerNo());
        }

        Map<String, CustomerCompanyDO> customerCompanyNoMap = null;
        if(CollectionUtil.isNotEmpty(hashSet)){
            List<CustomerCompanyDO> customerCompanyNoList = customerCompanyMapper.findByCustomerNoList(new ArrayList<>(hashSet));
            if (CollectionUtil.isNotEmpty(customerCompanyNoList)) {
                customerCompanyNoMap = ListUtil.listToMap(customerCompanyNoList, "customerNo");
            }
        }

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
            stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "手动认领(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",(认领人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",客户名称：" + (customerCompanyNoMap == null ? "":customerCompanyNoMap.get(bankSlipClaimDO.getCustomerNo()).getCompanyName()) + ",认领时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",认领：" + bankSlipClaimDO.getClaimAmount() + "元)");
        }
        // 添加操作日志
        BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.CLAIM);
        bankSlipDetailOperationLogDO.setOperationContent(String.valueOf(stringBuffer));
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailOperationLogDO.setUpdateTime(now);
        bankSlipDetailOperationLogDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        // 保存日志list
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);
    }

    /**
     * 删除认领数据
     *
     * @param : now
     * @param : bankSlipClaimDOList
     * @param : bankSlipDetailDO
     * @param : bankSlipDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:02
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    private ServiceResult<String, Integer> deleteBankSlipClaim(Date now, List<BankSlipClaimDO> bankSlipClaimDOList, BankSlipDetailDO bankSlipDetailDO, BankSlipDO bankSlipDO) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
            //删除所有
            bankSlipClaimMapper.deleteBankSlipClaimDO(userSupport.getCurrentUserId().toString(), now, bankSlipClaimDOList);

            //操作记录
            BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
            bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
            bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.DELETE);
            bankSlipDetailOperationLogDO.setOperationContent("删除银行流水认领操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",删除人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",删除时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
            bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            bankSlipDetailOperationLogDO.setCreateTime(now);
            bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());

            bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);

            bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
            bankSlipDetailDO.setUpdateTime(now);
            bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDetailMapper.update(bankSlipDetailDO);

            bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() + 1);
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() - 1);
            if (bankSlipDO.getNeedClaimCount() == 0 && bankSlipDO.getClaimCount() == 0) {
                bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
            }
            bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
            bankSlipDO.setUpdateTime(now);
            bankSlipMapper.update(bankSlipDO);
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 删除未知认领数据
     *
     * @param : now
     * @param : bankSlipClaimDOList
     * @param : bankSlipDetailDO
     * @param : bankSlipDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:02
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    private ServiceResult<String, Integer> deleteUnknownBankSlipClaim(Date now, List<BankSlipClaimDO> bankSlipClaimDOList, BankSlipDetailDO bankSlipDetailDO, BankSlipDO bankSlipDO) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
            //判断是否是商务
            if (userSupport.isBusinessAffairsPerson()) {
                //判断是否是总公司商务，总公司商务删除该条资金流水详情的所有认领信息，分公司商务只能删除该分公司业务认领的数据
                if (userSupport.isHeadUser()) {
                    bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
                } else {
                    //查询该商务所属分公司
                    Integer subCompanyId = userSupport.getCurrentUserCompanyId();
                    //获取该公司的所有认领记录
                    List<BankSlipClaimDO> deleteBankSlipClaimDOList = new ArrayList<>();
                    for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
                        if (subCompanyId.equals(userSupport.getCompanyIdByUser(Integer.parseInt(bankSlipClaimDO.getUpdateUser())))) {
                            deleteBankSlipClaimDOList.add(bankSlipClaimDO);
                        }
                    }
                    bankSlipClaimDOList = deleteBankSlipClaimDOList;
                }
            } else {
                //找出所有当前用户的认领数据
                bankSlipClaimDOList = bankSlipClaimMapper.findAmountByBankSlipDetailIdAndCreateUser(bankSlipDetailDO.getId(), userSupport.getCurrentUserId().toString());
            }

            if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
                //删除所有
                bankSlipClaimMapper.deleteBankSlipClaimDO(userSupport.getCurrentUserId().toString(), now, bankSlipClaimDOList);

                //操作记录
                BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO = new BankSlipDetailOperationLogDO();
                bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
                bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.DELETE);
                bankSlipDetailOperationLogDO.setOperationContent("删除银行流水认领操作(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",删除人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",删除时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now));
                bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                bankSlipDetailOperationLogDO.setCreateTime(now);
                bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());

                bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);
                boolean flag = false;
                if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
                    flag = true;
                    bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
                }
                bankSlipDetailDO.setUpdateTime(now);
                bankSlipDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                bankSlipDetailMapper.update(bankSlipDetailDO);

                if (flag) {
                    bankSlipDO.setNeedClaimCount(bankSlipDO.getNeedClaimCount() + 1);
                    bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() - 1);
                    if (bankSlipDO.getNeedClaimCount() == 0 && bankSlipDO.getClaimCount() == 0) {
                        bankSlipDO.setSlipStatus(SlipStatus.ALL_CLAIM);
                    }
                    bankSlipDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    bankSlipDO.setUpdateTime(now);
                    bankSlipMapper.update(bankSlipDO);
                }
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 保存或者跟新未知银行流水数据
     *
     * @param : bankSlipDetailDO
     * @param : claimParamList
     * @param : bankSlipClaim
     * @param : bankSlipDO
     * @param : allClaimAmount
     * @param : now
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 15:16
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    private ServiceResult<String, Integer> saveOrUpdateUnknownBankSlipDetail(BankSlipDetailDO bankSlipDetailDO, List<ClaimParam> claimParamList, BankSlipClaim bankSlipClaim, BankSlipDO bankSlipDO, BigDecimal allClaimAmount, Date now) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();


        List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipClaimMapper.findAmountByBankSlipDetailIdAndCreateUser(bankSlipDetailDO.getId(), userSupport.getCurrentUserId().toString());
        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
            //删除所有
            bankSlipClaimMapper.deleteBankSlipClaimDO(userSupport.getCurrentUserId().toString(), now, bankSlipClaimDOList);
            //删除所有认领记录
            bankSlipDetailOperationLogMapper.deleteByBankSlipDetailId(userSupport.getCurrentUserId().toString(), now, bankSlipDetailDO.getId());
            if (BankSlipDetailStatus.CLAIMED.equals(bankSlipDetailDO.getDetailStatus())) {
                // 改变详情当前用户科操作的数据和总表状态
                bankSlipDetailMapper.deleteBankSlipDetail(bankSlipDetailDO.getId(), userSupport.getCurrentUserId().toString(), now);
            }
        }

        Integer amount = bankSlipClaimMapper.findAmountByBankSlipDetailId(bankSlipDetailDO.getId());
        BigDecimal dbAmount = new BigDecimal(amount == null ? 0 : amount);
        dbAmount.setScale(2, BigDecimal.ROUND_UP);
        allClaimAmount = BigDecimalUtil.add(allClaimAmount, dbAmount);
        //判断总共金额是否相等
        if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) < 0) {
            // 只是保存认领记录其他的不改变  和记录日志
            long currentTimeMillis = System.currentTimeMillis();
            saveBankSlipClaimDOAndBankSlipDetailOperationLog(bankSlipDO, bankSlipClaim, claimParamList, bankSlipDetailDO, currentTimeMillis, now);
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            return serviceResult;
        } else if (BigDecimalUtil.compare(allClaimAmount, bankSlipDetailDO.getTradeAmount()) == 0) {
            //  调用下面方法去改变状态  和记录日志
            //这里是保存银行对公流水认领表 数据
            serviceResult = updateClaim(bankSlipDO, bankSlipDetailDO, bankSlipClaim, claimParamList, now);
            if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            }
            return serviceResult;
        }
        //以下是加上要加的总金额大于要充值的总金额的情况
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
        serviceResult.setErrorCode(ErrorCode.BANK_SLIP_DETAIL_TRADE_AMOUNT_LESS_THAN_CURRENT_AGGREGATE_AMOUNT);
        return serviceResult;

    }

    /**
     * 校验认领金额总和是否等于充值金额
     *
     * @param : bankSlipClaimDOList
     * @param : bankSlipDetailDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 17:40
     * @Return : java.lang.String
     */
    private boolean verifyClaimAmount(BankSlipDetailDO bankSlipDetailDO) {
        List<BankSlipClaimDO> bankSlipClaimDOList = bankSlipDetailDO.getBankSlipClaimDOList();
        //判断需确认金额和总金额额是否相等
        BigDecimal claimAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(bankSlipClaimDOList)) {
            for (BankSlipClaimDO bankSlipClaimDO : bankSlipClaimDOList) {
                claimAmount = BigDecimalUtil.add(claimAmount, bankSlipClaimDO.getClaimAmount());
            }
        }
        if (BigDecimalUtil.compare(bankSlipDetailDO.getTradeAmount(), claimAmount) == 0) {
            return true;
        }
        return false;
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
}
