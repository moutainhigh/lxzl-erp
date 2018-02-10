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
        ServiceResult<String, String> verifyServiceResult = verify(ConverterUtil.convert(dbStatementOrderCorrectDO, StatementOrderCorrect.class));
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

            //判断每次跟新是否超出
            ServiceResult<String, String> result = commitVerify(ConverterUtil.convert(statementOrderCorrectDO, StatementOrderCorrect.class));
            if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                return false;
            }
            String rentTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_RENT + "-" + statementOrderCorrectDO.getStatementOrderItemId();
            String rentDepositTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_RENT_DEPOSIT + "-" + statementOrderCorrectDO.getStatementOrderItemId();
            String depositTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_DEPOSIT + "-" + statementOrderCorrectDO.getStatementOrderItemId();
            String otherTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_OTHER + "-" + statementOrderCorrectDO.getStatementOrderItemId();
            String overdueTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_OVERDUE + "-" + statementOrderCorrectDO.getStatementOrderItemId();
            Map<String, BigDecimal> itemTypeAmountMap = getAllStatementOrderCorrectAmountMap(ConverterUtil.convert(statementOrderCorrectDO, StatementOrderCorrect.class));

            //需要冲正的数据
            BigDecimal statementCorrectRentDepositAmount = itemTypeAmountMap.get(rentDepositTypeAmountKey);
            BigDecimal statementCorrectDepositAmount = itemTypeAmountMap.get(depositTypeAmountKey);
            BigDecimal statementCorrectRentAmount = itemTypeAmountMap.get(rentTypeAmountKey);
            BigDecimal statementCorrectOtherAmount = itemTypeAmountMap.get(otherTypeAmountKey);
            BigDecimal statementCorrectOverdueAmount = itemTypeAmountMap.get(overdueTypeAmountKey);

            BigDecimal thisCorrectAllAmount = BigDecimal.ZERO;

            // 不同类型不同订单项的冲正金额
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                //校验支付状态
                if (statementOrderDetailDO == null || StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED.equals(statementOrderDetailDO.getStatementDetailStatus()) || StatementOrderStatus.STATEMENT_ORDER_STATUS_NO.equals(statementOrderDetailDO.getStatementDetailStatus())) {
                    continue;
                }
                //判断商品是否存在
                if (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(statementOrderDetailDO.getOrderItemType())) {
                    OrderProductDO orderProductDO = orderProductMapper.findById(statementOrderDetailDO.getOrderItemReferId());
                    if (orderProductDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                }
                //判断物料是否存在
                if (OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(statementOrderDetailDO.getOrderItemType())) {
                    OrderMaterialDO orderMaterialDO = orderMaterialMapper.findById(statementOrderDetailDO.getOrderItemReferId());
                    if (orderMaterialDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                }
                //需要付的逾期金额
                BigDecimal realStatementDetailOverdueAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOverdueAmount(), statementOrderDetailDO.getStatementDetailOverduePaidAmount());
                // 历史冲正单金额
                BigDecimal oldStatementDetailCorrectAmount = statementOrderDetailDO.getStatementDetailCorrectAmount();
                // 本次冲正业务金额
                BigDecimal thisCorrectBusinessDetailAmount = BigDecimal.ZERO;
                //结算其他费用
                if (StatementDetailType.STATEMENT_DETAIL_TYPE_OTHER.equals(statementOrderDetailDO.getStatementDetailType())) {
                    //需要付的其他费用（运费等）
                    BigDecimal realStatementDetailOtherAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailOtherAmount(), statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                    BigDecimal statementDetailOtherAmount = BigDecimalUtil.sub(realStatementDetailOtherAmount, statementCorrectOtherAmount);
                    if (BigDecimalUtil.compare(statementDetailOtherAmount, BigDecimal.ZERO) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    thisCorrectBusinessDetailAmount = statementCorrectOtherAmount;
                } else if (StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    //需要付的租金
                    BigDecimal realStatementDetailRentAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentAmount(), statementOrderDetailDO.getStatementDetailRentPaidAmount());
                    BigDecimal statementDetailRentAmount = BigDecimalUtil.sub(realStatementDetailRentAmount, statementCorrectRentAmount);
                    if (BigDecimalUtil.compare(statementDetailRentAmount, BigDecimal.ZERO) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                        return false;
                    }
                    thisCorrectBusinessDetailAmount = statementCorrectRentAmount;
                } else if (StatementDetailType.STATEMENT_DETAIL_TYPE_DEPOSIT.equals(statementOrderDetailDO.getStatementDetailType())) {
                    //需要付的租金押金
                    BigDecimal realStatementDetailRentDepositAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                    //需要付的押金
                    BigDecimal realStatementDetailDepositAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailDepositAmount(), statementOrderDetailDO.getStatementDetailDepositPaidAmount());
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
                    thisCorrectBusinessDetailAmount = BigDecimalUtil.add(statementCorrectRentDepositAmount, statementCorrectDepositAmount);
                }

                // 本次冲正的预期金额
                BigDecimal thisCorrectOverdueDetailAmount = BigDecimal.ZERO;
                if (BigDecimalUtil.compare(statementCorrectOverdueAmount, BigDecimal.ZERO) > 0) {
                    // 如果冲正金额大于逾期金额，那么就全冲
                    thisCorrectOverdueDetailAmount = BigDecimalUtil.compare(statementCorrectOverdueAmount, realStatementDetailOverdueAmount) >= 0 ? realStatementDetailOverdueAmount : statementCorrectOverdueAmount;
                    statementCorrectOverdueAmount = BigDecimalUtil.sub(statementCorrectOverdueAmount, thisCorrectOverdueDetailAmount);
                }

                // 本次冲正金额
                BigDecimal thisCorrectDetailAmount = BigDecimalUtil.add(thisCorrectOverdueDetailAmount, thisCorrectBusinessDetailAmount);
                statementOrderDetailDO.setStatementDetailCorrectAmount(thisCorrectDetailAmount);
                // 先把历史冲正的还原回来，然后再减去本次冲正的
                statementOrderDetailDO.setStatementDetailAmount(BigDecimalUtil.sub(BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailAmount(), oldStatementDetailCorrectAmount), thisCorrectDetailAmount));
                if (BigDecimalUtil.compare(statementOrderDetailDO.getStatementDetailAmount(), BigDecimal.ZERO) == 0) {
                    statementOrderDetailDO.setStatementDetailStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_CORRECTED);
                }
                statementOrderDetailMapper.update(statementOrderDetailDO);
                thisCorrectAllAmount = BigDecimalUtil.add(thisCorrectAllAmount, thisCorrectDetailAmount);

                // 逾期金额如果冲多了，就要回滚
                if (BigDecimalUtil.compare(statementCorrectOverdueAmount, BigDecimal.ZERO) < 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return false;
                }
            }

            BigDecimal oldStatementCorrectAmount = statementOrderDO.getStatementCorrectAmount();
            statementOrderDO.setStatementAmount(BigDecimalUtil.sub(BigDecimalUtil.add(statementOrderDO.getStatementAmount(), oldStatementCorrectAmount), thisCorrectAllAmount));
            //更新结算单
            if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) == 0) {
                statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_CORRECTED);
            }
            statementOrderDO.setStatementCorrectAmount(thisCorrectAllAmount);
            statementOrderMapper.update(statementOrderDO);

            //跟新冲正单
            statementOrderCorrectDO.setStatementOrderCorrectStatus(StatementOrderCorrectStatus.CORRECT_SUCCESS);
            statementOrderCorrectDO.setStatementCorrectSuccessTime(now);
            statementOrderCorrectMapper.update(statementOrderCorrectDO);
            return true;
        } else {
            statementOrderCorrectDO.setStatementOrderCorrectStatus(StatementOrderCorrectStatus.CORRECT_FAIL);
            statementOrderCorrectDO.setStatementCorrectSuccessTime(now);
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
        if ((statementOrderCorrect.getStatementCorrectRentAmount() != null && BigDecimalUtil.compare(statementOrderCorrect.getStatementCorrectRentAmount(), BigDecimal.ZERO) < 0)
                || (statementOrderCorrect.getStatementCorrectRentDepositAmount() != null && BigDecimalUtil.compare(statementOrderCorrect.getStatementCorrectRentDepositAmount(), BigDecimal.ZERO) < 0)
                || (statementOrderCorrect.getStatementCorrectDepositAmount() != null && BigDecimalUtil.compare(statementOrderCorrect.getStatementCorrectDepositAmount(), BigDecimal.ZERO) < 0)
                || (statementOrderCorrect.getStatementCorrectOtherAmount() != null && BigDecimalUtil.compare(statementOrderCorrect.getStatementCorrectOtherAmount(), BigDecimal.ZERO) < 0)
                || (statementOrderCorrect.getStatementCorrectOverdueAmount() != null && BigDecimalUtil.compare(statementOrderCorrect.getStatementCorrectOverdueAmount(), BigDecimal.ZERO) < 0)) {
            serviceResult.setErrorCode(ErrorCode.CORRECT_AMOUNT_MORE_THEN_ZERO);
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
        String rentTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_RENT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String rentDepositTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_RENT_DEPOSIT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String depositTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_DEPOSIT + "-" + statementOrderCorrect.getStatementOrderItemId();
        String otherTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_OTHER + "-" + statementOrderCorrect.getStatementOrderItemId();
        String overdueTypeAmountKey = StatementCorrectAmountType.AMOUNT_TYPE_OVERDUE + "-" + statementOrderCorrect.getStatementOrderItemId();

        // 不同类型不同订单项的冲正金额
        Map<String, BigDecimal> itemTypeAmountMap = getAllStatementOrderCorrectAmountMap(statementOrderCorrect);

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
        return verify(statementOrderCorrect);
    }

    private Map<String, BigDecimal> getAllStatementOrderCorrectAmountMap(StatementOrderCorrect statementOrderCorrect) {

        List<StatementOrderCorrectDO> statementOrderCorrectDOList = statementOrderCorrectMapper.findStatementOrderIdAndItemId(statementOrderCorrect.getStatementOrderId(), statementOrderCorrect.getStatementOrderItemId());

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

        // 注释，不能删除，以后开发修改的时候可以直接放开
        /*if (statementOrderCorrect.getStatementCorrectNo() != null) {
            // 如果为修改，把历史的减掉
            StatementOrderCorrectDO dbStatementOrderCorrectDO = statementOrderCorrectMapper.findByNo(statementOrderCorrect.getStatementCorrectNo());
            itemTypeAmountMap.put(rentTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(rentTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectRentAmount()));
            itemTypeAmountMap.put(rentDepositTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(rentDepositTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectRentDepositAmount()));
            itemTypeAmountMap.put(depositTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(depositTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectDepositAmount()));
            itemTypeAmountMap.put(otherTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(otherTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectOtherAmount()));
            itemTypeAmountMap.put(overdueTypeAmountKey, BigDecimalUtil.sub(itemTypeAmountMap.get(overdueTypeAmountKey), dbStatementOrderCorrectDO.getStatementCorrectOverdueAmount()));
        }*/

        return itemTypeAmountMap;
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
