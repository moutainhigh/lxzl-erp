package com.lxzl.erp.core.service.StatementOrderCorrect.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.StatementOrderCorrectStatus;
import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectQueryParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.StatementOrderCorrect.StatementOrderCorrectService;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> createStatementOrderCorrect(StatementOrderCorrect statementOrderCorrect) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();

        //查看结算单详情,结算单是否存在,校验冲正金额是否超出结算金额
        ServiceResult<String, String> verifyServiceResult = verify(statementOrderCorrect, statementOrderCorrect.getStatementOrderDetailId(), statementOrderCorrect.getStatementCorrectAmount());
        if (!verifyServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            return verifyServiceResult;
        }

        //创建结算冲正单
        Date now = new Date();
        StatementOrderCorrectDO statementOrderCorrectDO = ConverterUtil.convert(statementOrderCorrect, StatementOrderCorrectDO.class);
        statementOrderCorrectDO.setStatementCorrectNo(generateNoSupport.generateStatementOrderCorrect(statementOrderCorrectDO.getStatementOrderId()));
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> commitStatementOrderCorrect(String statementOrderCorrectNo, String remark) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //查看结算冲正单是否存在
        List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findByNo(statementOrderCorrectNo);
        if (CollectionUtil.isEmpty(statementOrderCorrectDOList)) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_NOT_EXISTS);
            return serviceResult;
        }
        //判断状态
        StatementOrderCorrectDO dbStatementOrderCorrectDO = statementOrderCorrectDOList.get(0);
        if (dbStatementOrderCorrectDO.getStatementOrderCorrectStatus() != StatementOrderCorrectStatus.VERIFY_STATUS_PENDING) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_STATUS_NOT_PENDING);
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

        //走工作流
        ServiceResult<String, String> workflowServiceResult = workflowService.commitWorkFlow(WorkflowType.WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT, statementOrderCorrectNo, userSupport.getCurrentUserId(), "结算冲正单", remark);
        if (!workflowServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            serviceResult.setErrorCode(workflowServiceResult.getErrorCode());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return serviceResult;
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statementOrderCorrectNo);
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> updateStatementOrderCorrect(StatementOrderCorrect statementOrderCorrect) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //查看结算冲正单是否存在
        List<StatementOrderCorrectDO> dbStatementOrderCorrectDOList = statementOrderCorrectMapper.findByNo(statementOrderCorrect.getStatementCorrectNo());
        if (CollectionUtil.isEmpty(dbStatementOrderCorrectDOList)) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_NOT_EXISTS);
            return serviceResult;
        }
        //判断状态
        StatementOrderCorrectDO dbStatementOrderCorrectDO = dbStatementOrderCorrectDOList.get(0);
        if (dbStatementOrderCorrectDO.getStatementOrderCorrectStatus() != StatementOrderCorrectStatus.VERIFY_STATUS_PENDING) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_STATUS_NOT_PENDING);
            return serviceResult;
        }
        //校验结算单和冲正金额是否超出结算金额
        ServiceResult<String, String> verifyServiceResult = verify(statementOrderCorrect, statementOrderCorrect.getStatementOrderDetailId(), statementOrderCorrect.getStatementCorrectAmount());
        if (!verifyServiceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            return verifyServiceResult;
        }
        //跟新
        Date now = new Date();
        StatementOrderCorrectDO statementOrderCorrectDO = ConverterUtil.convert(statementOrderCorrect, StatementOrderCorrectDO.class);
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
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, String> cancelStatementOrderCorrect(String statementOrderCorrectNo) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //查看结算冲正单是否存在
        List<StatementOrderCorrectDO> dbStatementOrderCorrectDOList = statementOrderCorrectMapper.findByNo(statementOrderCorrectNo);
        if (CollectionUtil.isEmpty(dbStatementOrderCorrectDOList)) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_NOT_EXISTS);
            return serviceResult;
        }
        //校验状态
        StatementOrderCorrectDO dbStatementOrderCorrectDO = dbStatementOrderCorrectDOList.get(0);
        if (!dbStatementOrderCorrectDO.getStatementOrderCorrectStatus().equals(StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT) || !dbStatementOrderCorrectDO.getStatementOrderCorrectStatus().equals(StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT)) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_CORRECT_STATUS_NOT_PENDING_OR_COMMIT);
            return serviceResult;
        }
        //提交状态取消增加关闭工作流
        if (dbStatementOrderCorrectDO.getStatementOrderCorrectStatus().equals(StatementOrderCorrectStatus.VERIFY_STATUS_COMMIT)) {
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
        Map<String, Object> maps = new HashMap<>();
        StatementOrderCorrectQueryParam statementOrderCorrectQueryParam = new StatementOrderCorrectQueryParam();
        statementOrderCorrectQueryParam.setStatementCorrectNo(statementOrderCorrectNo);
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderCorrectQueryParam", statementOrderCorrectQueryParam);
        List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderCorrectAndStatementOrderByQueryParam(maps);
        List<StatementOrderCorrect> statementOrderCorrectList = null;
        if (CollectionUtil.isNotEmpty(statementOrderCorrectDOList)) {
            statementOrderCorrectList = ConverterUtil.convertList(statementOrderCorrectDOList, StatementOrderCorrect.class);
        }
        StatementOrderCorrect statementOrderCorrect = statementOrderCorrectList.get(0);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statementOrderCorrect);
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
     * 校验结算单,结算单明细和冲正金额是否超出结算金额
     *
     * @param : statementOrderId
     * @param : statementCorrectAmount
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/30 17:24
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    private ServiceResult<String, String> verify(StatementOrderCorrect statementOrderCorrect, Integer statementOrderDetailId, BigDecimal statementCorrectAmount) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        //查看结算单明细是否存在
        StatementOrderDetailDO statementOrderDetailDO = statementOrderDetailMapper.findNoSettlementById(statementOrderDetailId);
        if (statementOrderDetailDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_DETAIL_NOT_EXISTS);
            return serviceResult;
        }

        //判断结算单是否存在
        StatementOrderDO statementOrderDO = statementOrderMapper.findNoSettlementById(statementOrderDetailDO.getStatementOrderId());
        if (statementOrderDO == null) {
            serviceResult.setErrorCode(ErrorCode.STATEMENT_ORDER_NOT_EXISTS);
            return serviceResult;
        }
        //保存结算单id
        statementOrderCorrect.setStatementOrderId(statementOrderDetailDO.getStatementOrderId());
        BigDecimal statementPaidAmount = statementOrderDetailDO.getStatementDetailAmount();
        //校验冲正金额是否超出结算金额

        if (statementCorrectAmount.compareTo(statementPaidAmount) > 0) {
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

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;
}
