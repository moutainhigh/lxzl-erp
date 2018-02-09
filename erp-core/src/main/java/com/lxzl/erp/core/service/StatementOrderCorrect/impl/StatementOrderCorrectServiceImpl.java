package com.lxzl.erp.core.service.StatementOrderCorrect.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectQueryParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.StatementOrderCorrect.StatementOrderCorrectService;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.statementOrderCorrect.StatementOrderCorrectDO;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/30
 * @Time : Created in 11:08
 */
@Service
public class StatementOrderCorrectServiceImpl implements StatementOrderCorrectService {

    /**
     * 创建冲正单
     *
     * @param : statementOrderCorrect
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 11:14
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> createStatementOrderCorrect(StatementOrderCorrect statementOrderCorrect) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        //查看结算单详情,结算单是否存在,校验冲正金额是否超出结算金额
        ServiceResult<String, String> verifyServiceResult = verify(statementOrderCorrect);
        if (!verifyServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            return verifyServiceResult;
        }

        //创建结算冲正单
        Date now = new Date();
        StatementOrderCorrectDO statementOrderCorrectDO = ConverterUtil.convert(statementOrderCorrect, StatementOrderCorrectDO.class);
        statementOrderCorrectDO.setStatementCorrectNo(generateNoSupport.generateStatementOrderCorrect(statementOrderCorrectDO.getStatementOrderId()));
        statementOrderCorrectDO.setStatementCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementOrderCorrectDO.getStatementCorrectRentAmount(),
                statementOrderCorrectDO.getStatementCorrectRentDepositAmount()), statementOrderCorrectDO.getStatementCorrectDepositAmount()),
                statementOrderCorrectDO.getStatementCorrectOtherAmount()), statementOrderCorrectDO.getStatementCorrectOverdueAmount()));
        statementOrderCorrectDO.setStatementOrderCorrectStatus(StatementOrderCorrectStatus.VERIFY_STATUS_PENDING);
        statementOrderCorrectDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        statementOrderCorrectDO.setCreateTime(now);
        statementOrderCorrectDO.setCreateUser(userSupport.getCurrentUserId().toString());
        statementOrderCorrectDO.setUpdateTime(now);
        statementOrderCorrectDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        statementOrderCorrectMapper.save(statementOrderCorrectDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statementOrderCorrectDO.getStatementCorrectNo());
        return serviceResult;
    }

    /**
     * 提交冲正单
     *
     * @param : statementOrderCorrect
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 11:14
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> commitStatementOrderCorrect(StatementOrderCorrectParam statementOrderCorrectParam) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //查看结算冲正单是否存在
        StatementOrderCorrectDO dbStatementOrderCorrectDO = statementOrderCorrectMapper.findByNo(statementOrderCorrectParam.getStatementCorrectNo());
        if (dbStatementOrderCorrectDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_NOT_EXISTS);
            return serviceResult;
        }

        if (!StatementOrderCorrectStatus.VERIFY_STATUS_PENDING.equals(dbStatementOrderCorrectDO.getStatementOrderCorrectStatus())) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_STATUS_NOT_PENDING);
            return serviceResult;
        }

        StatementOrderDO statementOrderDO = statementOrderMapper.findById(dbStatementOrderCorrectDO.getStatementOrderId());
        if (statementOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus()) || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDO.getStatementStatus())) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        //判断是否超出结算金额
        serviceResult = commitVerify(ConverterUtil.convert(dbStatementOrderCorrectDO, StatementOrderCorrect.class));
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            return serviceResult;
        }
        //修改状态
        Date now = new Date();
        dbStatementOrderCorrectDO.setStatementOrderCorrectStatus(StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT);
        dbStatementOrderCorrectDO.setCreateTime(now);
        dbStatementOrderCorrectDO.setCreateUser(userSupport.getCurrentUserId().toString());
        dbStatementOrderCorrectDO.setUpdateTime(now);
        dbStatementOrderCorrectDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        statementOrderCorrectMapper.update(dbStatementOrderCorrectDO);

        ServiceResult<String, Boolean> needVerifyResult = workflowService.isNeedVerify(WorkflowType.WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT);
        if (!ErrorCode.SUCCESS.equals(needVerifyResult.getErrorCode())) {
            serviceResult.setErrorCode(needVerifyResult.getErrorCode());
            return serviceResult;
        } else {
//            if (needVerifyResult.getResult()) {
//            //走工作流
//            statementOrderCorrectParam.setVerifyMatters("结算冲正单");
//            ServiceResult<String, String> workflowServiceResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT, statementOrderCorrectParam.getStatementCorrectNo(), statementOrderCorrectParam.getVerifyUserId(), statementOrderCorrectParam.getVerifyMatters(), statementOrderCorrectParam.getRemark());
//            if (!ErrorCode.SUCCESS.equals(workflowServiceResult.getErrorCode())) {
//                serviceResult.setErrorCode(workflowServiceResult.getErrorCode());
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
//                return serviceResult;
//            }
//        } else {
            boolean result = receiveVerifyResult(true, statementOrderCorrectParam.getStatementCorrectNo());
            if (!result) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_FAIL);
                return serviceResult;
            }
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statementOrderCorrectParam.getStatementCorrectNo());
        return serviceResult;
    }

    /**
     * 修改冲正单
     *
     * @param : statementOrderCorrect
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 15:47
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateStatementOrderCorrect(StatementOrderCorrect statementOrderCorrect) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //查看结算冲正单是否存在
        StatementOrderCorrectDO dbStatementOrderCorrectDO = statementOrderCorrectMapper.findByNo(statementOrderCorrect.getStatementCorrectNo());
        if (dbStatementOrderCorrectDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_NOT_EXISTS);
            return serviceResult;
        }
        if (!StatementOrderCorrectStatus.VERIFY_STATUS_PENDING.equals(dbStatementOrderCorrectDO.getStatementOrderCorrectStatus())) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_STATUS_NOT_PENDING);
            return serviceResult;
        }
        //校验结算单和冲正金额是否超出结算金额
        ServiceResult<String, String> verifyServiceResult = verify(ConverterUtil.convert(dbStatementOrderCorrectDO,StatementOrderCorrect.class));
        if (!ErrorCode.SUCCESS.equals(verifyServiceResult.getErrorCode())) {
            return verifyServiceResult;
        }
        //跟新
        Date now = new Date();
        StatementOrderCorrectDO statementOrderCorrectDO = ConverterUtil.convert(statementOrderCorrect, StatementOrderCorrectDO.class);
        statementOrderCorrectDO.setStatementCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementOrderCorrectDO.getStatementCorrectRentAmount(), statementOrderCorrectDO.getStatementCorrectRentDepositAmount()), statementOrderCorrectDO.getStatementCorrectDepositAmount()), statementOrderCorrectDO.getStatementCorrectOtherAmount()), statementOrderCorrectDO.getStatementCorrectOverdueAmount()));
        statementOrderCorrectDO.setId(dbStatementOrderCorrectDO.getId());
        statementOrderCorrectDO.setUpdateTime(now);
        statementOrderCorrectDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        statementOrderCorrectMapper.update(statementOrderCorrectDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statementOrderCorrect.getStatementCorrectNo());
        return serviceResult;
    }

    /**
     * 取消冲正单
     *
     * @param : statementOrderCorrect
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 17:42
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> cancelStatementOrderCorrect(String statementOrderCorrectNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //查看结算冲正单是否存在
        StatementOrderCorrectDO dbStatementOrderCorrectDO = statementOrderCorrectMapper.findByNo(statementOrderCorrectNo);
        if (dbStatementOrderCorrectDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_NOT_EXISTS);
            return serviceResult;
        }
        //校验状态
        if (!StatementOrderCorrectStatus.VERIFY_STATUS_PENDING.equals(dbStatementOrderCorrectDO.getStatementOrderCorrectStatus()) && !StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT.equals(dbStatementOrderCorrectDO.getStatementOrderCorrectStatus())) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_STATUS_NOT_PENDING_OR_COMMIT);
            return serviceResult;
        }
        //提交状态取消增加关闭工作流
        if (StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT.equals(dbStatementOrderCorrectDO.getStatementOrderCorrectStatus())) {
            //关闭工作流
            ServiceResult<String, String> cancelWorkFlowServiceResult = workflowService.cancelWorkFlow(WorkflowType.WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT, dbStatementOrderCorrectDO.getStatementCorrectNo());
            if (!cancelWorkFlowServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return cancelWorkFlowServiceResult;
            }
        }

        //跟新
        Date now = new Date();
        dbStatementOrderCorrectDO.setStatementOrderCorrectStatus(StatementOrderCorrectStatus.CORRECT_CANCEL);
        dbStatementOrderCorrectDO.setUpdateTime(now);
        dbStatementOrderCorrectDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        statementOrderCorrectMapper.update(dbStatementOrderCorrectDO);

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(dbStatementOrderCorrectDO.getStatementCorrectNo());
        return serviceResult;
    }

    /**
     * 查询冲正单详情
     *
     * @param : statementOrderCorrectNo
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 19:02
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect>
     */
    @Override
    public ServiceResult<String, StatementOrderCorrect> queryStatementOrderCorrectDetailByNo(String statementOrderCorrectNo) {
        ServiceResult<String, StatementOrderCorrect> serviceResult = new ServiceResult<>();
        StatementOrderCorrectDO statementOrderCorrectDO = statementOrderCorrectMapper.findByNo(statementOrderCorrectNo);
        if (statementOrderCorrectDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_NOT_EXISTS);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(ConverterUtil.convert(statementOrderCorrectDO, StatementOrderCorrect.class));
        return serviceResult;
    }

    /**
     * 分页查询
     *
     * @param : statementOrderCorrectQueryParam
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 19:26
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,com.lxzl.erp.common.domain.Page<com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect>>
     */
    @Override
    public ServiceResult<String, Page<StatementOrderCorrect>> pageStatementOrderCorrect(StatementOrderCorrectQueryParam statementOrderCorrectQueryParam) {
        ServiceResult<String, Page<StatementOrderCorrect>> serviceResult = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        PageQuery pageQuery = new PageQuery(statementOrderCorrectQueryParam.getPageNo(), statementOrderCorrectQueryParam.getPageSize());
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("statementOrderCorrectQueryParam", statementOrderCorrectQueryParam);
        Integer statementOrderCorrectCount = statementOrderCorrectMapper.listCount(maps);
        List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderCorrectAndStatementOrderByQueryParam(maps);
        List<StatementOrderCorrect> statementOrderCorrectList = ConverterUtil.convertList(statementOrderCorrectDOList, StatementOrderCorrect.class);
        Page<StatementOrderCorrect> statementOrderCorrectPage = new Page<>(statementOrderCorrectList, statementOrderCorrectCount, statementOrderCorrectQueryParam.getPageNo(), statementOrderCorrectQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statementOrderCorrectPage);
        return serviceResult;
    }

    /**
     * 获取处理审核结果
     *
     * @param : verifyResult
     * @param : businessNo
     * @Author : XiaoLuYu
     * @Date : Created in 2018/2/1 10:24
     * @Return : boolean
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {
        //校验结算冲正单是否存在记录和是否是已结算状态
        Date now = new Date();
        StatementOrderCorrectDO statementOrderCorrectDO = statementOrderCorrectMapper.findByNo(businessNo);
        ServiceResult<String, String> serviceResult = commitVerify(ConverterUtil.convert(statementOrderCorrectDO, StatementOrderCorrect.class));
        if (!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())) {
            return false;
        }
        if (statementOrderCorrectDO == null || !StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT.equals(statementOrderCorrectDO.getStatementOrderCorrectStatus())) {
            return false;
        }
        //校验结算单是否存在记录和是否是已结算状态
        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderCorrectDO.getStatementOrderId());
        if (statementOrderDO == null || StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus()) || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDO.getStatementStatus())) {
            return false;
        }
        //校验结算单项是否存在
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.findByStatementOrderIdAndItemReferId(statementOrderCorrectDO.getStatementOrderItemId(), statementOrderDO.getId());
        if (CollectionUtil.isEmpty(statementOrderDetailDOList)) {
            return false;
        }
        if (verifyResult) {
            BigDecimal statementCorrectAmount = statementOrderCorrectDO.getStatementCorrectAmount();
            if (BigDecimalUtil.compare(statementCorrectAmount, BigDecimal.ZERO) < 0) {
                return false;
            }
            //需要冲正的数据
            BigDecimal statementCorrectRentDepositAmount = statementOrderCorrectDO.getStatementCorrectRentDepositAmount();
            BigDecimal statementCorrectDepositAmount = statementOrderCorrectDO.getStatementCorrectDepositAmount();
            BigDecimal statementCorrectRentAmount = statementOrderCorrectDO.getStatementCorrectRentAmount();
            BigDecimal statementCorrectOverdueAmount = statementOrderCorrectDO.getStatementCorrectOverdueAmount();
            BigDecimal statementCorrectOtherAmount = statementOrderCorrectDO.getStatementCorrectOtherAmount();
            //多余的逾期金额
            BigDecimal lastStatementDetailOverdueAmount = new BigDecimal(0);
            // 不同类型不同订单项的冲正金额
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                //判断每次跟新是否超出
                ServiceResult<String, String> result = commitVerify(ConverterUtil.convert(statementOrderCorrectDO, StatementOrderCorrect.class));
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    return false;
                }
                //校验支付状态
                if (statementOrderDetailDO == null || StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus()) || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return false;
                }
                //判断商品是否存在
                if (statementOrderDetailDO.getOrderItemType() == 1) {
                    OrderProductDO orderProductDO = orderProductMapper.findById(statementOrderDetailDO.getOrderItemReferId());
                    if (orderProductDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                }
                //判断物料是否存在
                if (statementOrderDetailDO.getOrderItemType() == 2) {
                    OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(statementOrderDetailDO.getOrderItemReferId());
                    if (orderMaterialDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                }
                //逾期金额
                BigDecimal dbStatementDetailOverdueAmount = statementOrderDetailDO.getStatementDetailOverdueAmount();
                BigDecimal dbStatementDetailOverduePaidAmount = statementOrderDetailDO.getStatementDetailOverduePaidAmount();
                //需要付的逾期金额
                BigDecimal realStatementDetailOverdueAmount = BigDecimalUtil.sub(dbStatementDetailOverdueAmount, dbStatementDetailOverduePaidAmount);
                //冲正单金额
                BigDecimal statementDetailCorrectAmount = statementOrderDetailDO.getStatementDetailCorrectAmount();
                //结算单总金额
                BigDecimal statementDetailAmount = statementOrderDetailDO.getStatementDetailAmount();
                //结算其他费用
                if (StatementDetailType.STATEMENT_DETAIL_TYPE_OTHER.equals(statementOrderDetailDO.getStatementDetailType())) {
                    //判断冲正单传值是否正确
                    if (BigDecimalUtil.compare(statementCorrectOtherAmount, BigDecimal.ZERO) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    //其他费用（运费等）
                    BigDecimal dbStatementDetailOtherAmount = statementOrderDetailDO.getStatementDetailOtherAmount();
                    BigDecimal dbStatementDetailOtherPaidAmount = statementOrderDetailDO.getStatementDetailOtherPaidAmount();
                    //需要付的其他费用（运费等）
                    BigDecimal realStatementDetailOtherAmount = BigDecimalUtil.sub(dbStatementDetailOtherAmount, dbStatementDetailOtherPaidAmount);
                    BigDecimal statementDetailOtherAmount = BigDecimalUtil.sub(realStatementDetailOtherAmount, statementCorrectOtherAmount);
                    if (BigDecimalUtil.compare(statementDetailOtherAmount, BigDecimal.ZERO) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    if (lastStatementDetailOverdueAmount != null) {
                        lastStatementDetailOverdueAmount = BigDecimalUtil.add(lastStatementDetailOverdueAmount, realStatementDetailOverdueAmount);
                    }
                    if (lastStatementDetailOverdueAmount != null && BigDecimalUtil.compare(BigDecimalUtil.sub(lastStatementDetailOverdueAmount, statementCorrectOverdueAmount), BigDecimal.ZERO) <= 0) {
                        statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailCorrectAmount, realStatementDetailOverdueAmount), statementCorrectOtherAmount));
                        BigDecimal newStatementDetailCorrectAmount = BigDecimalUtil.sub(BigDecimalUtil.sub(statementDetailAmount, realStatementDetailOverdueAmount), statementCorrectOtherAmount);
                        //校验总金额是否合格和写入状态
                        if (!verifyAmountAndSetStatus(statementOrderDetailDO, newStatementDetailCorrectAmount)) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                            return false;
                        }
                        statementOrderDetailDO.setStatementDetailAmount(newStatementDetailCorrectAmount);
                    } else {
                        BigDecimal newStatementDetailCorrectAmount = null;
                        //当前要减去的逾期金额
                        if (lastStatementDetailOverdueAmount == null) {
                            statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(statementDetailCorrectAmount, statementCorrectOtherAmount));
                            newStatementDetailCorrectAmount = BigDecimalUtil.sub(statementDetailAmount, statementCorrectOtherAmount);
                        } else {
                            BigDecimal statementDetailOverdueAmount = BigDecimalUtil.sub(realStatementDetailOverdueAmount, BigDecimalUtil.sub(lastStatementDetailOverdueAmount, statementCorrectOverdueAmount));
                            statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailCorrectAmount, statementDetailOverdueAmount), statementCorrectOtherAmount));
                            newStatementDetailCorrectAmount = BigDecimalUtil.sub(BigDecimalUtil.sub(statementDetailAmount, statementDetailOverdueAmount), statementCorrectOtherAmount);
                        }
                        //校验总金额是否合格和写入状态
                        if (!verifyAmountAndSetStatus(statementOrderDetailDO, newStatementDetailCorrectAmount)) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                            return false;
                        }
                        statementOrderDetailDO.setStatementDetailAmount(newStatementDetailCorrectAmount);
                        lastStatementDetailOverdueAmount = null;
                    }
                    statementOrderDetailMapper.update(statementOrderDetailDO);
                    //结算租金费用
                } else if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    //判断冲正单传值是否正确
                    if (BigDecimalUtil.compare(statementCorrectRentAmount, BigDecimal.ZERO) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    BigDecimal dbStatementDetailRentAmount = statementOrderDetailDO.getStatementDetailRentAmount();
                    BigDecimal dbStatementDetailRentPaidAmount = statementOrderDetailDO.getStatementDetailRentPaidAmount();
                    //需要付的租金
                    BigDecimal realStatementDetailRentAmount = BigDecimalUtil.sub(dbStatementDetailRentAmount, dbStatementDetailRentPaidAmount);
                    BigDecimal statementDetailRentAmount = BigDecimalUtil.sub(realStatementDetailRentAmount, statementCorrectRentAmount);
                    if (BigDecimalUtil.compare(statementDetailRentAmount, BigDecimal.ZERO) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    if (lastStatementDetailOverdueAmount != null) {
                        lastStatementDetailOverdueAmount = BigDecimalUtil.add(lastStatementDetailOverdueAmount, realStatementDetailOverdueAmount);
                    }
                    if (lastStatementDetailOverdueAmount != null && BigDecimalUtil.compare(BigDecimalUtil.sub(lastStatementDetailOverdueAmount, statementCorrectOverdueAmount), BigDecimal.ZERO) <= 0) {
                        statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailCorrectAmount, realStatementDetailOverdueAmount), statementCorrectRentAmount));
                        BigDecimal newStatementDetailCorrectAmount = BigDecimalUtil.sub(BigDecimalUtil.sub(statementDetailAmount, realStatementDetailOverdueAmount), statementCorrectRentAmount);
                        //校验总金额是否合格和写入状态
                        if (!verifyAmountAndSetStatus(statementOrderDetailDO, newStatementDetailCorrectAmount)) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                            return false;
                        }
                        statementOrderDetailDO.setStatementDetailAmount(newStatementDetailCorrectAmount);
                    } else {
                        BigDecimal newStatementDetailCorrectAmount = null;
                        //当前要减去的逾期金额
                        if (lastStatementDetailOverdueAmount == null) {
                            statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(statementDetailCorrectAmount, statementCorrectRentAmount));
                            newStatementDetailCorrectAmount = BigDecimalUtil.sub(statementDetailAmount, statementCorrectRentAmount);
                        } else {
                            BigDecimal statementDetailOverdueAmount = BigDecimalUtil.sub(realStatementDetailOverdueAmount, BigDecimalUtil.sub(lastStatementDetailOverdueAmount, statementCorrectOverdueAmount));
                            statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailCorrectAmount, statementDetailOverdueAmount), statementCorrectRentAmount));
                            newStatementDetailCorrectAmount = BigDecimalUtil.sub(BigDecimalUtil.sub(statementDetailAmount, statementDetailOverdueAmount), statementCorrectRentAmount);
                        }
                        //校验总金额是否合格和写入状态
                        if (!verifyAmountAndSetStatus(statementOrderDetailDO, newStatementDetailCorrectAmount)) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                            return false;
                        }
                        statementOrderDetailDO.setStatementDetailAmount(newStatementDetailCorrectAmount);
                        lastStatementDetailOverdueAmount = null;
                    }
                    statementOrderDetailMapper.update(statementOrderDetailDO);
                    //结算押金费用
                } else if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    //判断冲正单传值是否正确
                    if ((BigDecimalUtil.compare(statementCorrectDepositAmount, BigDecimal.ZERO) < 0 || BigDecimalUtil.compare(statementCorrectRentDepositAmount, BigDecimal.ZERO) < 0)) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    //租金押金
                    BigDecimal dbStatementDetailRentDepositAmount = statementOrderDetailDO.getStatementDetailRentDepositAmount();
                    BigDecimal dbStatementDetailRentDepositPaidAmount = statementOrderDetailDO.getStatementDetailRentDepositPaidAmount();
                    //需要付的租金押金
                    BigDecimal realStatementDetailRentDepositAmount = BigDecimalUtil.sub(dbStatementDetailRentDepositAmount, dbStatementDetailRentDepositPaidAmount);
                    //押金
                    BigDecimal dbStatementDetailDepositAmount = statementOrderDetailDO.getStatementDetailDepositAmount();
                    BigDecimal dbStatementDetailDepositPaidAmount = statementOrderDetailDO.getStatementDetailDepositPaidAmount();
                    //需要付的押金
                    BigDecimal realStatementDetailDepositAmount = BigDecimalUtil.sub(dbStatementDetailDepositAmount, dbStatementDetailDepositPaidAmount);
                    //冲正剩余的租金押金金额
                    BigDecimal statementDetailRentDepositAmount = BigDecimalUtil.sub(realStatementDetailRentDepositAmount, statementCorrectRentDepositAmount);
                    //冲正剩余的押金
                    BigDecimal statementDetailDepositAmount = BigDecimalUtil.sub(realStatementDetailDepositAmount, statementCorrectDepositAmount);
                    if (BigDecimalUtil.compare(statementDetailDepositAmount, BigDecimal.ZERO) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    if (BigDecimalUtil.compare(statementDetailRentDepositAmount, BigDecimal.ZERO) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    if (lastStatementDetailOverdueAmount != null) {
                        lastStatementDetailOverdueAmount = BigDecimalUtil.add(lastStatementDetailOverdueAmount, realStatementDetailOverdueAmount);
                    }
                    if (lastStatementDetailOverdueAmount != null && BigDecimalUtil.compare(BigDecimalUtil.sub(lastStatementDetailOverdueAmount, statementCorrectOverdueAmount), BigDecimal.ZERO) <= 0) {
                        statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailCorrectAmount, realStatementDetailOverdueAmount), statementCorrectRentDepositAmount), statementCorrectDepositAmount));
                        BigDecimal newStatementDetailCorrectAmount = BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.sub(statementDetailAmount, realStatementDetailOverdueAmount), statementCorrectRentDepositAmount), statementCorrectDepositAmount);
                        //校验总金额是否合格和写入状态
                        if (!verifyAmountAndSetStatus(statementOrderDetailDO, newStatementDetailCorrectAmount)) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                            return false;
                        }
                        statementOrderDetailDO.setStatementDetailAmount(newStatementDetailCorrectAmount);
                    } else {

                        BigDecimal newStatementDetailCorrectAmount = null;
                        //当前要减去的逾期金额
                        if (lastStatementDetailOverdueAmount == null) {
                            statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailCorrectAmount, statementCorrectRentDepositAmount), statementCorrectDepositAmount));
                            newStatementDetailCorrectAmount = BigDecimalUtil.sub(BigDecimalUtil.sub(statementDetailAmount, statementCorrectRentDepositAmount), statementCorrectDepositAmount);
                        } else {
                            BigDecimal statementDetailOverdueAmount = BigDecimalUtil.sub(realStatementDetailOverdueAmount, BigDecimalUtil.sub(lastStatementDetailOverdueAmount, statementCorrectOverdueAmount));
                            statementOrderDetailDO.setStatementDetailCorrectAmount(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailCorrectAmount, statementDetailOverdueAmount), statementCorrectRentDepositAmount), statementCorrectDepositAmount));
                            newStatementDetailCorrectAmount = BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.sub(statementDetailAmount, statementDetailOverdueAmount), statementCorrectRentDepositAmount), statementCorrectDepositAmount);
                        }
                        //校验总金额是否合格和写入状态
                        if (!verifyAmountAndSetStatus(statementOrderDetailDO, newStatementDetailCorrectAmount)) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                            return false;
                        }
                        statementOrderDetailDO.setStatementDetailAmount(newStatementDetailCorrectAmount);
                        lastStatementDetailOverdueAmount = null;
                    }
                    statementOrderDetailMapper.update(statementOrderDetailDO);

                }
            }


            //以下是校验金额是否超出以及跟新结算单
            BigDecimal statementRentDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDO.getStatementRentDepositPaidAmount());
            BigDecimal statementDepositAmount = BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDO.getStatementDepositPaidAmount());
            BigDecimal statementRentAmount = BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDO.getStatementRentPaidAmount());
            BigDecimal statementOverdueAmount = BigDecimalUtil.sub(statementOrderDO.getStatementOverdueAmount(), statementOrderDO.getStatementOverduePaidAmount());
            BigDecimal statementOtherAmount = BigDecimalUtil.sub(statementOrderDO.getStatementOtherAmount(), statementOrderDO.getStatementOtherPaidAmount());
            //已冲正 待付租金押金余额
            BigDecimal realStatementDetailRentDepositAmount = BigDecimalUtil.sub(statementRentDepositAmount, statementCorrectRentDepositAmount);
            //已冲正 待付押金余额
            BigDecimal realStatementDetailDepositAmount = BigDecimalUtil.sub(statementDepositAmount, statementCorrectDepositAmount);
            //已冲正 待付租金余额
            BigDecimal realStatementDetailRentAmount = BigDecimalUtil.sub(statementRentAmount, statementCorrectRentAmount);
            //已冲正 待付逾期金额余额
            BigDecimal realStatementDetailOverdueAmount = BigDecimalUtil.sub(statementOverdueAmount, statementCorrectOverdueAmount);
            //已冲正 待付其他费用（运费等）余额
            BigDecimal realStatementDetailOtherAmount = BigDecimalUtil.sub(statementOtherAmount, statementCorrectOtherAmount);
            //数据库结算单总额
            BigDecimal dbStatementAmount = statementOrderDO.getStatementAmount();
            //结算单总额
            BigDecimal newStatementAmount = BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.sub(dbStatementAmount, statementCorrectRentDepositAmount), statementCorrectDepositAmount), statementCorrectRentAmount), statementCorrectOverdueAmount), statementCorrectOtherAmount);
            if (BigDecimalUtil.compare(realStatementDetailRentDepositAmount, BigDecimal.ZERO) < 0
                    || BigDecimalUtil.compare(realStatementDetailDepositAmount, BigDecimal.ZERO) < 0
                    || BigDecimalUtil.compare(realStatementDetailRentAmount, BigDecimal.ZERO) < 0
                    || BigDecimalUtil.compare(realStatementDetailOverdueAmount, BigDecimal.ZERO) < 0
                    || BigDecimalUtil.compare(realStatementDetailOtherAmount, BigDecimal.ZERO) < 0
                    || BigDecimalUtil.compare(newStatementAmount, BigDecimal.ZERO) < 0
                    ) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return false;
            }
            //数据库结算单总额
            BigDecimal dbStatementCorrectAmount = statementOrderDO.getStatementCorrectAmount();
            //冲正单总额
            BigDecimal newStatementCorrectAmount = BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(dbStatementCorrectAmount, statementCorrectRentDepositAmount), statementCorrectDepositAmount), statementCorrectRentAmount), statementCorrectOverdueAmount), statementCorrectOtherAmount);

            //跟新结算单
            if (BigDecimalUtil.compare(newStatementAmount, BigDecimal.ZERO) == 0) {
                statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
            }
            statementOrderDO.setStatementAmount(newStatementAmount);
            statementOrderDO.setStatementCorrectAmount(newStatementCorrectAmount);
            statementOrderMapper.update(statementOrderDO);

            //跟新冲正单
            statementOrderCorrectDO.setStatementOrderCorrectStatus(StatementOrderCorrectStatus.CORRECT_SUCCESS);
            statementOrderCorrectDO.setStatementCorrectSuccessTime(now);
            statementOrderCorrectMapper.update(statementOrderCorrectDO);
        }
        return true;

    }

    /**
     * 校验结算单,结算单明细和冲正金额是否超出结算金额
     *
     * @param : statementOrderId
     * @param : statementCorrectAmount
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 17:24
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    private ServiceResult<String, String> verify(StatementOrderCorrect statementOrderCorrect) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        if (statementOrderCorrect == null || statementOrderCorrect.getStatementOrderId() == null || statementOrderCorrect.getStatementOrderItemId() == null) {
            serviceResult.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return serviceResult;
        }

        //判断结算单是否存在
        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderCorrect.getStatementOrderId());
        if (statementOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus()) || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDO.getStatementStatus())) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderIdAndItemId(statementOrderDO.getId(), statementOrderCorrect.getStatementOrderItemId());

        String rentTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_RENT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String rentDepositTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_RENT_DEPOSIT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String depositTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_DEPOSIT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String otherTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_OTHER + "-" + statementOrderCorrect.getStatementOrderItemId();
        String overdueTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_OVERDUE + "-" + statementOrderCorrect.getStatementOrderItemId();

        // 不同类型不同订单项的冲正金额
        Map<String, BigDecimal> itemTypeAmountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(statementOrderCorrectDOList)) {
            for (StatementOrderCorrectDO statementOrderCorrectDO : statementOrderCorrectDOList) {
                // 不同订单项的冲正金额
                itemTypeAmountMap.put(rentTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(rentTypeAmountKey), statementOrderCorrectDO.getStatementCorrectRentAmount()));
                itemTypeAmountMap.put(rentDepositTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(rentDepositTypeAmountKey), statementOrderCorrectDO.getStatementCorrectRentDepositAmount()));
                itemTypeAmountMap.put(depositTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(depositTypeAmountKey), statementOrderCorrectDO.getStatementCorrectDepositAmount()));
                itemTypeAmountMap.put(otherTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(otherTypeAmountKey), statementOrderCorrectDO.getStatementCorrectOtherAmount()));
                itemTypeAmountMap.put(overdueTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(overdueTypeAmountKey), statementOrderCorrectDO.getStatementCorrectOverdueAmount()));
            }
        }
        // 加上本次不同
        itemTypeAmountMap.put(rentTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(rentTypeAmountKey), statementOrderCorrect.getStatementCorrectRentAmount()));
        itemTypeAmountMap.put(rentDepositTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(rentDepositTypeAmountKey), statementOrderCorrect.getStatementCorrectRentDepositAmount()));
        itemTypeAmountMap.put(depositTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(depositTypeAmountKey), statementOrderCorrect.getStatementCorrectDepositAmount()));
        itemTypeAmountMap.put(otherTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(otherTypeAmountKey), statementOrderCorrect.getStatementCorrectOtherAmount()));
        itemTypeAmountMap.put(overdueTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(overdueTypeAmountKey), statementOrderCorrect.getStatementCorrectOverdueAmount()));

        if (statementOrderCorrect.getStatementCorrectNo() != null) {
            // 如果为修改，把历史的减掉
            StatementOrderCorrectDO dbStatementOrderCorrectDO = statementOrderCorrectMapper.findByNo(statementOrderCorrect.getStatementCorrectNo());
            itemTypeAmountMap.put(rentTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(rentTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectRentAmount()));
            itemTypeAmountMap.put(rentDepositTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(rentDepositTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectRentDepositAmount()));
            itemTypeAmountMap.put(depositTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(depositTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectDepositAmount()));
            itemTypeAmountMap.put(otherTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(otherTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectOtherAmount()));
            itemTypeAmountMap.put(overdueTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(overdueTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectOverdueAmount()));
        }

        BigDecimal statementDetailRentAmount = new BigDecimal(0);
        BigDecimal statementDetailRentPaidAmount = new BigDecimal(0);
        BigDecimal statementDetailRentDepositAmount = new BigDecimal(0);
        BigDecimal statementDetailRentDepositPaidAmount = new BigDecimal(0);
        BigDecimal statementDetailDepositAmount = new BigDecimal(0);
        BigDecimal statementDetailDepositPaidAmount = new BigDecimal(0);
        BigDecimal statementDetailOtherAmount = new BigDecimal(0);
        BigDecimal statementDetailOtherPaidAmount = new BigDecimal(0);
        BigDecimal statementDetailOverdueAmount = new BigDecimal(0);
        BigDecimal statementDetailOverduePaidAmount = new BigDecimal(0);
        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
            if (!statementOrderDetailDO.getStatementOrderId().equals(statementOrderCorrect.getStatementOrderId()) || !statementOrderDetailDO.getOrderItemReferId().equals(statementOrderCorrect.getStatementOrderItemId())) {
                continue;
            }
            statementDetailRentAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentAmount(), statementDetailRentAmount);
            statementDetailRentPaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), statementDetailRentPaidAmount);
            statementDetailRentDepositAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementDetailRentDepositAmount);
            statementDetailRentDepositPaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), statementDetailRentDepositPaidAmount);
            statementDetailDepositAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositAmount(), statementDetailDepositAmount);
            statementDetailDepositPaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementDetailDepositPaidAmount);
            statementDetailOtherAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherAmount(), statementDetailOtherAmount);
            statementDetailOtherPaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), statementDetailOtherPaidAmount);
            statementDetailOverdueAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOverdueAmount(), statementDetailOverdueAmount);
            statementDetailOverduePaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOverduePaidAmount(), statementDetailOverduePaidAmount);
        }
        statementDetailRentAmount = BigDecimalUtil.sub(statementDetailRentAmount, statementDetailRentPaidAmount);
        statementDetailRentDepositAmount = BigDecimalUtil.sub(statementDetailRentDepositAmount, statementDetailRentDepositPaidAmount);
        statementDetailDepositAmount = BigDecimalUtil.sub(statementDetailDepositAmount, statementDetailDepositPaidAmount);
        statementDetailOtherAmount = BigDecimalUtil.sub(statementDetailOtherAmount, statementDetailOtherPaidAmount);
        statementDetailOverdueAmount = BigDecimalUtil.sub(statementDetailOverdueAmount, statementDetailOverduePaidAmount);

        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailRentAmount, itemTypeAmountMap.get(rentTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailRentDepositAmount, itemTypeAmountMap.get(rentDepositTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailDepositAmount, itemTypeAmountMap.get(depositTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailOtherAmount, itemTypeAmountMap.get(otherTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailOverdueAmount, itemTypeAmountMap.get(overdueTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 校验结算单,结算单明细和冲正金额是否超出结算金额
     *
     * @param : statementOrderId
     * @param : statementCorrectAmount
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 17:24
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    private ServiceResult<String, String> commitVerify(StatementOrderCorrect statementOrderCorrect) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //判断结算单是否存在
        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderCorrect.getStatementOrderId());
        if (statementOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus()) || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDO.getStatementStatus())) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_STATUS_ERROR);
            return serviceResult;
        }

        List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderIdAndItemId(statementOrderDO.getId(), statementOrderCorrect.getStatementOrderItemId());

        String rentTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_RENT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String rentDepositTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_RENT_DEPOSIT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String depositTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_DEPOSIT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String otherTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_OTHER + "-" + statementOrderCorrect.getStatementOrderItemId();
        String overdueTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_OVERDUE + "-" + statementOrderCorrect.getStatementOrderItemId();

        // 不同类型不同订单项的冲正金额
        Map<String, BigDecimal> itemTypeAmountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(statementOrderCorrectDOList)) {
            for (StatementOrderCorrectDO statementOrderCorrectDO : statementOrderCorrectDOList) {
                // 不同订单项的冲正金额
                itemTypeAmountMap.put(rentTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(rentTypeAmountKey), statementOrderCorrectDO.getStatementCorrectRentAmount()));
                itemTypeAmountMap.put(rentDepositTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(rentDepositTypeAmountKey), statementOrderCorrectDO.getStatementCorrectRentDepositAmount()));
                itemTypeAmountMap.put(depositTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(depositTypeAmountKey), statementOrderCorrectDO.getStatementCorrectDepositAmount()));
                itemTypeAmountMap.put(otherTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(otherTypeAmountKey), statementOrderCorrectDO.getStatementCorrectOtherAmount()));
                itemTypeAmountMap.put(overdueTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(overdueTypeAmountKey), statementOrderCorrectDO.getStatementCorrectOverdueAmount()));
            }
        }
        // 加上本次不同
        itemTypeAmountMap.put(rentTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(rentTypeAmountKey), statementOrderCorrect.getStatementCorrectRentAmount()));
        itemTypeAmountMap.put(rentDepositTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(rentDepositTypeAmountKey), statementOrderCorrect.getStatementCorrectRentDepositAmount()));
        itemTypeAmountMap.put(depositTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(depositTypeAmountKey), statementOrderCorrect.getStatementCorrectDepositAmount()));
        itemTypeAmountMap.put(otherTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(otherTypeAmountKey), statementOrderCorrect.getStatementCorrectOtherAmount()));
        itemTypeAmountMap.put(overdueTypeAmountKey, BigDecimalUtil.add(itemTypeAmountMap.get(overdueTypeAmountKey), statementOrderCorrect.getStatementCorrectOverdueAmount()));

        BigDecimal statementDetailAmount = new BigDecimal(0);
        BigDecimal statementDetailCorrectAmount = new BigDecimal(0);
        BigDecimal statementDetailRentAmount = new BigDecimal(0);
        BigDecimal statementDetailRentPaidAmount = new BigDecimal(0);
        BigDecimal statementDetailRentDepositAmount = new BigDecimal(0);
        BigDecimal statementDetailRentDepositPaidAmount = new BigDecimal(0);
        BigDecimal statementDetailDepositAmount = new BigDecimal(0);
        BigDecimal statementDetailDepositPaidAmount = new BigDecimal(0);
        BigDecimal statementDetailOtherAmount = new BigDecimal(0);
        BigDecimal statementDetailOtherPaidAmount = new BigDecimal(0);
        BigDecimal statementDetailOverdueAmount = new BigDecimal(0);
        BigDecimal statementDetailOverduePaidAmount = new BigDecimal(0);
        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
            if (!statementOrderDetailDO.getStatementOrderId().equals(statementOrderCorrect.getStatementOrderId()) || !statementOrderDetailDO.getOrderItemReferId().equals(statementOrderCorrect.getStatementOrderItemId())) {
                continue;
            }
            statementDetailAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailAmount(), statementDetailAmount);
            statementDetailCorrectAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailCorrectAmount(), statementDetailCorrectAmount);
            statementDetailRentAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentAmount(), statementDetailRentAmount);
            statementDetailRentPaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(), statementDetailRentPaidAmount);
            statementDetailRentDepositAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementDetailRentDepositAmount);
            statementDetailRentDepositPaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(), statementDetailRentDepositPaidAmount);
            statementDetailDepositAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositAmount(), statementDetailDepositAmount);
            statementDetailDepositPaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(), statementDetailDepositPaidAmount);
            statementDetailOtherAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherAmount(), statementDetailOtherAmount);
            statementDetailOtherPaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherPaidAmount(), statementDetailOtherPaidAmount);
            statementDetailOverdueAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOverdueAmount(), statementDetailOverdueAmount);
            statementDetailOverduePaidAmount = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOverduePaidAmount(), statementDetailOverduePaidAmount);
        }
        statementDetailRentAmount = BigDecimalUtil.sub(statementDetailRentAmount, statementDetailRentPaidAmount);
        statementDetailRentDepositAmount = BigDecimalUtil.sub(statementDetailRentDepositAmount, statementDetailRentDepositPaidAmount);
        statementDetailDepositAmount = BigDecimalUtil.sub(statementDetailDepositAmount, statementDetailDepositPaidAmount);
        statementDetailOtherAmount = BigDecimalUtil.sub(statementDetailOtherAmount, statementDetailOtherPaidAmount);
        statementDetailOverdueAmount = BigDecimalUtil.sub(statementDetailOverdueAmount, statementDetailOverduePaidAmount);

        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailRentAmount, itemTypeAmountMap.get(rentTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailRentDepositAmount, itemTypeAmountMap.get(rentDepositTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailDepositAmount, itemTypeAmountMap.get(depositTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailOtherAmount, itemTypeAmountMap.get(otherTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        if (BigDecimalUtil.compare(BigDecimalUtil.sub(statementDetailOverdueAmount, itemTypeAmountMap.get(overdueTypeAmountKey)), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    /**
     * 校验总金额是否合格和写入状态
     *
     * @param : commonStatementOrderDetailDO
     * @param : newStatementDetailCorrectAmount
     * @Author : XiaoLuYu
     * @Date : Created in 2018/2/7 17:16
     * @Return : boolean
     */
    public boolean verifyAmountAndSetStatus(StatementOrderDetailDO commonStatementOrderDetailDO, BigDecimal newStatementDetailCorrectAmount) {
        if (BigDecimalUtil.compare(newStatementDetailCorrectAmount, BigDecimal.ZERO) < 0) {
            return false;
        }
        if (BigDecimalUtil.compare(newStatementDetailCorrectAmount, BigDecimal.ZERO) == 0) {
            commonStatementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_NO);
            return true;
        }
        return true;
    }

    @Autowired
    private StatementOrderCorrectMapper statementOrderCorrectMapper;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private OrderMaterialMapper orderMaterialMapper;

}
