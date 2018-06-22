package com.lxzl.erp.core.service.statement.impl.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerPersonMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderStatementDateChangeLogMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderStatementDateChangeLogDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-06 10:06
 */
@Component
public class StatementOrderSupport {

    @Autowired
    private StatementOrderMapper statementOrderMapper;

    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private CustomerPersonMapper customerPersonMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    public List<StatementOrderDO> getOverdueStatementOrderList(Integer customerId) {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        param.setIsOverdue(CommonConstant.COMMON_CONSTANT_YES);
        param.setStatementOrderCustomerId(customerId);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderQueryParam", param);
        return statementOrderMapper.listPage(maps);
    }

    public BigDecimal getShortRentReceivable(Integer customerId) {
        StatementOrderDetailQueryParam param = new StatementOrderDetailQueryParam();
        param.setCustomerId(customerId);
        param.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderDetailQueryParam", param);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listPage(maps);
        BigDecimal totalShortRentReceivable = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                totalShortRentReceivable = BigDecimalUtil.add(totalShortRentReceivable, statementOrderDetailDO.getStatementDetailAmount());
            }
        }
        return totalShortRentReceivable;
    }


    public BigDecimal getSubCompanyShortRentReceivable(Integer subCompanyId) {
        StatementOrderDetailQueryParam param = new StatementOrderDetailQueryParam();
        param.setSubCompanyId(subCompanyId);
        param.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        param.setIsNeedToPay(CommonConstant.COMMON_CONSTANT_YES);
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("statementOrderDetailQueryParam", param);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listPage(maps);
        BigDecimal totalShortRentReceivable = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                totalShortRentReceivable = BigDecimalUtil.add(totalShortRentReceivable, statementOrderDetailDO.getStatementDetailAmount());
            }
        }
        return totalShortRentReceivable;
    }

    /**
     * 计算结算时间
     *
     * @param statementDate
     * @param rentStartTime
     * @return
     */
    public Integer getCustomerStatementDate(Integer statementDate,Date rentStartTime) {
        if (statementDate == null){
            DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
            statementDate = dataDictionaryDO==null?StatementMode.STATEMENT_MONTH_END:Integer.parseInt(dataDictionaryDO.getDataName());
        }
        if (StatementMode.STATEMENT_MONTH_NATURAL.equals(statementDate)) {
            // 如果结算日为按月结算，那么就要自然日来结算
            Calendar rentStartTimeCalendar = Calendar.getInstance();
            rentStartTimeCalendar.setTime(rentStartTime);
            //如果是当月第一天
            if(DateUtil.isSameDay(rentStartTimeCalendar.getTime(),DateUtil.getStartMonthDate(rentStartTimeCalendar.getTime()))){
                statementDate = StatementMode.STATEMENT_MONTH_END;
            }else{
                rentStartTimeCalendar.add(Calendar.DAY_OF_MONTH, -1);
                statementDate = rentStartTimeCalendar.get(Calendar.DAY_OF_MONTH);
            }
        }
        return statementDate;
    }

    /**
     * 恢复结算单
     * @param currentTime
     * @param statementOrderDetailDOList
     */
    public void reStatement( Date currentTime , Map<Integer, StatementOrderDO> statementCache ,List<StatementOrderDetailDO> statementOrderDetailDOList){
        //处理结算单
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                StatementOrderDO statementOrderDO = statementCache.get(statementOrderDetailDO.getStatementOrderId());
                //处理结算单总金额
                statementOrderDO.setStatementAmount(BigDecimalUtil.sub(BigDecimalUtil.round(statementOrderDO.getStatementAmount(), BigDecimalUtil.STANDARD_SCALE), BigDecimalUtil.round(statementOrderDetailDO.getStatementDetailAmount(), BigDecimalUtil.STANDARD_SCALE)));
                //处理结算租金押金金额
                statementOrderDO.setStatementRentDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositAmount(), statementOrderDetailDO.getStatementDetailRentDepositAmount()));
                //处理结算押金金额
                statementOrderDO.setStatementDepositAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositAmount(), statementOrderDetailDO.getStatementDetailDepositAmount()));
                //处理结算租金金额
                statementOrderDO.setStatementRentAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentAmount(), statementOrderDetailDO.getStatementDetailRentAmount()));
                //处理结算单逾期金额
                statementOrderDO.setStatementOverdueAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOverdueAmount(), statementOrderDetailDO.getStatementDetailOverdueAmount()));
                //处理其他费用
                statementOrderDO.setStatementOtherAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOtherAmount(), statementOrderDetailDO.getStatementDetailOtherAmount()));
                //处理冲正金额
                statementOrderDO.setStatementCorrectAmount(BigDecimalUtil.sub(statementOrderDO.getStatementCorrectAmount(), statementOrderDetailDO.getStatementDetailCorrectAmount()));
                statementOrderDetailDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                statementOrderDetailDO.setUpdateTime(currentTime);
                statementOrderDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                statementOrderDetailMapper.update(statementOrderDetailDO);
            }
            for (Integer key : statementCache.keySet()) {
                StatementOrderDO statementOrderDO = statementCache.get(key);
                if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), BigDecimal.ZERO) == 0) {
                    statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                }else  if (BigDecimalUtil.compare(statementOrderDO.getStatementAmount(), statementOrderDO.getStatementPaidAmount()) <= 0){
                    statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
                }
                statementOrderDO.setUpdateTime(currentTime);
                statementOrderDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                statementOrderMapper.update(statementOrderDO);
            }
        }
    }
    /**
     * 恢复结算单已支付金额
     */
    public void reStatementPaid(Map<Integer, StatementOrderDO> statementOrderDOMap , List<StatementOrderDetailDO> statementOrderDetailDOList){
        //处理结算单
        //缓存查询到的结算单
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                StatementOrderDO statementOrderDO = statementOrderDOMap.get(statementOrderDetailDO.getStatementOrderId());
                BigDecimal statementDetailOtherPaidAmount = statementOrderDetailDO.getStatementDetailOtherPaidAmount();
                BigDecimal statementDetailRentDepositPaidAmount = statementOrderDetailDO.getStatementDetailRentDepositPaidAmount();
                BigDecimal statementDetailDepositPaidAmount = statementOrderDetailDO.getStatementDetailDepositPaidAmount();
                BigDecimal statementDetailRentPaidAmount = statementOrderDetailDO.getStatementDetailRentPaidAmount();
                BigDecimal statementDetailOverduePaidAmount = statementOrderDetailDO.getStatementDetailOverduePaidAmount();

                //处理结算单已支付租金押金金额
                statementOrderDO.setStatementRentDepositPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentDepositPaidAmount(), statementDetailRentDepositPaidAmount));
                //处理结算单已支付押金金额
                statementOrderDO.setStatementDepositPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementDepositPaidAmount(), statementDetailDepositPaidAmount));
                //处理结算单已支付租金金额
                statementOrderDO.setStatementRentPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementRentPaidAmount(), statementDetailRentPaidAmount));
                //处理结算单已支付逾期金额
                statementOrderDO.setStatementOverduePaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOverduePaidAmount(), statementDetailOverduePaidAmount));
                //处理结算单已支付其他费用
                statementOrderDO.setStatementOtherPaidAmount(BigDecimalUtil.sub(statementOrderDO.getStatementOtherPaidAmount(), statementDetailOtherPaidAmount));
            }
            for (Integer key : statementOrderDOMap.keySet()) {
                StatementOrderDO statementOrderDO = statementOrderDOMap.get(key);
               // 金额为零结算单删除
                if(BigDecimalUtil.compare(statementOrderDO.getStatementAmount(),BigDecimal.ZERO)==0){
                    statementOrderDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                }else if(BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(),statementOrderDO.getStatementAmount())==0){
                    statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED);
                }else if(BigDecimalUtil.compare(statementOrderDO.getStatementPaidAmount(),BigDecimal.ZERO)>0){
                    statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);
                }else{
                    statementOrderDO.setStatementStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);
                }
                statementOrderMapper.update(statementOrderDO);
            }
        }
    }
    public Map<Integer,StatementOrderDO> getStatementOrderByDetails(List<StatementOrderDetailDO> statementOrderDetailDOList) {
        //缓存查询到的结算单
        Map<Integer, StatementOrderDO> statementCache = new HashMap<>();
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                StatementOrderDO statementOrderDO = statementCache.get(statementOrderDetailDO.getStatementOrderId());
                if (statementOrderDO == null) {
                    statementOrderDO = statementOrderMapper.findById(statementOrderDetailDO.getStatementOrderId());
                    statementCache.put(statementOrderDO.getId(), statementOrderDO);
                }
            }
        }
        return statementCache;
    }

    public void recordStatementDateLog(String orderNo, Integer statementDate) {
        if (statementDate == null){
            DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findDataByOnlyOneType(DataDictionaryType.DATA_DICTIONARY_TYPE_STATEMENT_DATE);
            statementDate = dataDictionaryDO==null?StatementMode.STATEMENT_MONTH_END:Integer.parseInt(dataDictionaryDO.getDataName());
        }
        Date now = new Date();
        OrderStatementDateChangeLogDO orderStatementDateChangeLogDO = new OrderStatementDateChangeLogDO();
        orderStatementDateChangeLogDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        orderStatementDateChangeLogDO.setStatementDate(statementDate);
        orderStatementDateChangeLogDO.setOrderNo(orderNo);
        orderStatementDateChangeLogDO.setCreateTime(now);
        orderStatementDateChangeLogDO.setCreateUser(userSupport.getCurrentUserId().toString());
        orderStatementDateChangeLogDO.setUpdateTime(now);
        orderStatementDateChangeLogDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        orderStatementDateChangeLogMapper.save(orderStatementDateChangeLogDO);
    }

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private OrderStatementDateChangeLogMapper orderStatementDateChangeLogMapper;


}
