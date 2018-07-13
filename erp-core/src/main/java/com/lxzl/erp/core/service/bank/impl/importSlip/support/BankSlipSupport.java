package com.lxzl.erp.core.service.bank.impl.importSlip.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.bank.impl.BankSlipServiceImpl;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipClaimMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipDetailOperationLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipClaimDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailOperationLogDO;
import com.lxzl.se.common.util.StringUtil;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/14
 * @Time : Created in 21:12
 */
@Component
public class BankSlipSupport {
    private static Logger logger = LoggerFactory.getLogger(BankSlipServiceImpl.class);
    @Autowired
    private BankSlipMapper bankSlipMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BankSlipClaimMapper bankSlipClaimMapper;
    @Autowired
    private BankSlipDetailOperationLogMapper bankSlipDetailOperationLogMapper;
    @Autowired
    private BankSlipDetailMapper bankSlipDetailMapper;
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BankSlipDO formatBankSlipDetail(BankSlipDO bankSlipDO, List<BankSlipDetailDO> bankSlipDetailDOList) {

        //查询出导入时间的所有本公司的所有导入数据
        List<BankSlipDO> dbBankSlipDOList = bankSlipMapper.findBySubCompanyIdAndBankType(bankSlipDO.getSubCompanyId(), bankSlipDO.getBankType());

        if (CollectionUtil.isEmpty(dbBankSlipDOList)) {
            bankSlipMapper.save(bankSlipDO);
            bankSlipDO.setBankSlipDetailDOList(bankSlipDetailDOList);
            return bankSlipDO;
        }
        Map<String, BankSlipDetailDO> allSlipDetailDOMap = new HashMap<>();
        for (BankSlipDO slipDO : dbBankSlipDOList) {
            List<BankSlipDetailDO> slipDetailDOList = slipDO.getBankSlipDetailDOList();
            Map<String, BankSlipDetailDO> slipDetailDOMap = ListUtil.listToMap(slipDetailDOList, "tradeSerialNo");
            allSlipDetailDOMap.putAll(slipDetailDOMap);
        }

        //剩余要导入或者跟新数据
        int loanSignTypeAmount = 0;
        List<BankSlipDetailDO> newBankSlipDetailDOList = new ArrayList<>();
        for (BankSlipDetailDO bankSlipDetailDO : bankSlipDetailDOList) {
            if (!allSlipDetailDOMap.containsKey(bankSlipDetailDO.getTradeSerialNo())) {
                if (LoanSignType.INCOME.equals(bankSlipDetailDO.getLoanSign())) {
                    loanSignTypeAmount = loanSignTypeAmount + 1;
                }
                newBankSlipDetailDOList.add(bankSlipDetailDO);
            }
        }
        //判断导入的数据是不是今天的  是就跟新 不是就新增
        BankSlipDO dbBankSlipDO = bankSlipMapper.findBySubCompanyIdAndDayAndBankType(bankSlipDO.getSubCompanyId(), bankSlipDO.getBankType(), bankSlipDO.getSlipDay());
        if (dbBankSlipDO != null) {
            //跟新状态
            if (loanSignTypeAmount > 0 && SlipStatus.ALL_CLAIM.equals(dbBankSlipDO.getSlipStatus())) {
                dbBankSlipDO.setSlipStatus(SlipStatus.ALREADY_PUSH_DOWN);
            }
            //跟新
            dbBankSlipDO.setInCount(dbBankSlipDO.getInCount() + loanSignTypeAmount);
            dbBankSlipDO.setNeedClaimCount(dbBankSlipDO.getNeedClaimCount() + loanSignTypeAmount);
            bankSlipMapper.update(dbBankSlipDO);
            dbBankSlipDO.setBankSlipDetailDOList(newBankSlipDetailDOList);
            return dbBankSlipDO;
        }
        if (loanSignTypeAmount == 0) {
            return null;
        }

        bankSlipDO.setInCount(loanSignTypeAmount);
        bankSlipDO.setNeedClaimCount(loanSignTypeAmount);
        bankSlipMapper.save(bankSlipDO);
        bankSlipDO.setBankSlipDetailDOList(newBankSlipDetailDOList);
        return bankSlipDO;

    }

    public void constantlyPaymentClaim(BankSlipClaimDO bankSlipClaimDO, BankSlipDO bankSlipDO, BankSlipDetailDO bankSlipDetailDO, BankSlipDetailOperationLogDO bankSlipDetailOperationLogDO, Date now) {
        Boolean paySuccessFlag = true;
        StringBuffer stringBuffer = new StringBuffer("");
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
                stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值时时导入(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",充值时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",充值：" + bankSlipClaimDO.getClaimAmount() + "元，成功！！！");
            } else {
                //银行对公流水认领表 失败 改变状态
                bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
                paySuccessFlag = false;
                stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值时时导入(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",充值时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",充值：" + bankSlipClaimDO.getClaimAmount() + "元，失败！！！");
            }
        } catch (Exception e) {
            logger.error("------------------充值出错----------------------", e);
            //银行对公流水认领表 失败 改变状态
            bankSlipClaimDO.setRechargeStatus(RechargeStatus.PAY_FAIL);
            paySuccessFlag = false;
            stringBuffer.append(("".equals(String.valueOf(stringBuffer)) ? "" : ",") + "客户充值时时导入异常出错(导入时间：" + new SimpleDateFormat("yyyy-MM-dd").format(bankSlipDO.getSlipDay()) + "）--银行对公流水明细id：" + bankSlipDetailDO.getId() + ",充值人：" + userSupport.getCurrentUserCompany().getSubCompanyName() + "  " + userSupport.getCurrentUser().getRoleList().get(0).getDepartmentName() + "  " + userSupport.getCurrentUser().getRealName() + ",客户编号：" + bankSlipClaimDO.getCustomerNo() + ",充值时间：" + new SimpleDateFormat("yyyy-MM-dd").format(now) + ",充值：" + bankSlipClaimDO.getClaimAmount() + "元，失败！！！");
        }
        if (!paySuccessFlag) {
            bankSlipClaimMapper.update(bankSlipClaimDO);
        }
        // 添加操作日志
        bankSlipDetailOperationLogDO.setBankSlipDetailId(bankSlipDetailDO.getId());
        bankSlipDetailOperationLogDO.setOperationType(BankSlipDetailOperationType.RECHARGE);
        bankSlipDetailOperationLogDO.setOperationContent(String.valueOf(stringBuffer));
        bankSlipDetailOperationLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        bankSlipDetailOperationLogDO.setCreateTime(now);
        bankSlipDetailOperationLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        bankSlipDetailOperationLogMapper.save(bankSlipDetailOperationLogDO);

        //充值失败
        if (!paySuccessFlag) {
            bankSlipDetailDO.setDetailStatus(BankSlipDetailStatus.CLAIMED);
            bankSlipDetailMapper.update(bankSlipDetailDO);
            //总表确认减一
            bankSlipDO.setConfirmCount(bankSlipDO.getConfirmCount() - 1);
            bankSlipDO.setClaimCount(bankSlipDO.getClaimCount() + 1);
            bankSlipDO.setSlipStatus(SlipStatus.ALREADY_PUSH_DOWN);
            bankSlipMapper.update(bankSlipDO);
        }
    }

    //toString重写
    public static String getValue(Cell cell) {
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

    public static  String verifyTradeTime(String tradeTime,Integer... stringLength){
        tradeTime = StringUtil.trim(tradeTime);
        boolean flag = false;
        for (Integer integer : stringLength) {
            if(integer.equals(tradeTime.length())){
                flag = true;
            }
        }
        if(flag){
            return ErrorCode.SUCCESS;
        }else {
            return  ErrorCode.DATE_TRANSITION_IS_FAIL;
        }

    }

    /**
     * 获取银行名称
     *
     * @param : bankType
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/26 14:34
     * @Return : java.lang.String
     */
    public static String getBankTypeName(Integer bankType) {
        String bankName;
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
            case 12:
                bankName = "快付通";
                break;
            case 13:
                bankName = "库存现金";
                break;
            case 14:
                bankName = "威富通";
                break;
            default:
                bankName = null;
                 break;
        }
        return bankName;
    }

}
