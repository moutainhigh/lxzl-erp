package com.lxzl.erp.core.service.StatementOrderCorrect.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectQueryParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.StatementOrderCorrect.StatementOrderCorrectService;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statementOrderCorrect.StatementOrderCorrectMapper;
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
    public ServiceResult<String, String> commitStatementOrderCorrect(StatementOrderCorrect statementOrderCorrect) {
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

        StatementOrderDO statementOrderDO = statementOrderMapper.findById(dbStatementOrderCorrectDO.getStatementOrderId());
        if (statementOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        if (StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus()) || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDO.getStatementStatus())) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_STATUS_ERROR);
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
        } else if (needVerifyResult.getResult()) {
            //走工作流
            statementOrderCorrect.setVerifyMatters("结算冲正单");
            ServiceResult<String, String> workflowServiceResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT, statementOrderCorrect.getStatementCorrectNo(), statementOrderCorrect.getVerifyUserId(), statementOrderCorrect.getVerifyMatters(), statementOrderCorrect.getRemark());
            if (!ErrorCode.SUCCESS.equals(workflowServiceResult.getErrorCode())) {
                serviceResult.setErrorCode(workflowServiceResult.getErrorCode());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return serviceResult;
            }
        } else {
            receiveVerifyResult(true, statementOrderCorrect.getStatementCorrectNo());
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statementOrderCorrect.getStatementCorrectNo());
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
        ServiceResult<String, String> verifyServiceResult = verify(statementOrderCorrect);
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
        if (!StatementOrderCorrectStatus.VERIFY_STATUS_PENDING.equals(dbStatementOrderCorrectDO.getStatementOrderCorrectStatus()) || !StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT.equals(dbStatementOrderCorrectDO.getStatementOrderCorrectStatus())) {
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
        StatementOrderCorrectDO statementOrderCorrectDO = statementOrderCorrectMapper.findByNo(businessNo);
        if (statementOrderCorrectDO == null || !StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT.equals(statementOrderCorrectDO.getStatementOrderCorrectStatus())) {
            return false;
        }
        //校验结算单是否存在记录和是否是已结算状态
        StatementOrderDO statementOrderDO = statementOrderMapper.findById(statementOrderCorrectDO.getStatementOrderId());
        if (statementOrderDO == null || StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDO.getStatementStatus()) || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDO.getStatementStatus())) {
            return false;
        }
        // TODO 待办事项 成功后的逻辑
        if (verifyResult) {
            return true;
        } else {
            statementOrderCorrectDO.setStatementOrderCorrectStatus(StatementOrderCorrectStatus.VERIFY_STATUS_PENDING);
            statementOrderCorrectMapper.update(statementOrderCorrectDO);
            return true;
        }

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
        }

        for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDO.getStatementOrderDetailDOList()) {
            if (StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                continue;
            }
            itemTypeAmountMap.put(rentTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(rentTypeAmountKey), statementOrderDetailDO.getStatementDetailRentAmount()));
            itemTypeAmountMap.put(rentDepositTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(rentDepositTypeAmountKey), statementOrderDetailDO.getStatementDetailRentDepositAmount()));
            itemTypeAmountMap.put(depositTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(depositTypeAmountKey), statementOrderDetailDO.getStatementDetailDepositAmount()));
            itemTypeAmountMap.put(otherTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(otherTypeAmountKey), statementOrderDetailDO.getStatementDetailOtherAmount()));
            itemTypeAmountMap.put(overdueTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(overdueTypeAmountKey), statementOrderDetailDO.getStatementDetailOverdueAmount()));
        }

        if (BigDecimalUtil.compare(itemTypeAmountMap.get(rentTypeAmountKey), BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(itemTypeAmountMap.get(rentDepositTypeAmountKey), BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(itemTypeAmountMap.get(depositTypeAmountKey), BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(itemTypeAmountMap.get(otherTypeAmountKey), BigDecimal.ZERO) < 0
                || BigDecimalUtil.compare(itemTypeAmountMap.get(overdueTypeAmountKey), BigDecimal.ZERO) < 0) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_GREATER_THAN_REALITY_AMOUNT);
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Autowired
    private StatementOrderCorrectMapper statementOrderCorrectMapper;

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private GenerateNoSupport generateNoSupport;

}
