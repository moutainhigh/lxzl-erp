package com.lxzl.erp.core.service.statistics.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.statistics.StatisticsIncomePageParam;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsIncome;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsIncomeDetail;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsIndexInfo;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statistics.StatisticsMapper;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.ReturnType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-24 16:25
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
    @Override
    public ServiceResult<String, StatisticsIndexInfo> queryIndexInfo() {
        ServiceResult<String, StatisticsIndexInfo> result = new ServiceResult<>();
        StatisticsIndexInfo statisticsIndexInfo = new StatisticsIndexInfo();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("productEquipmentQueryParam", new ProductEquipmentQueryParam());
        Integer totalProductEquipmentCount = productEquipmentMapper.listCount(paramMap);
        statisticsIndexInfo.setTotalProductEquipmentCount(totalProductEquipmentCount);

        paramMap.put("customerQueryParam", new CustomerQueryParam());
        Integer totalCustomerCount = customerMapper.listCount(paramMap);
        statisticsIndexInfo.setTotalCustomerCount(totalCustomerCount);

        BigDecimal totalRentAmount = orderMapper.findPaidOrderAmount();
        statisticsIndexInfo.setTotalRentAmount(totalRentAmount);

        // 空参数
        paramMap.clear();
        paramMap.put("orderQueryParam", new OrderQueryParam());
        Integer totalOrderCount = orderMapper.findOrderCountByParams(paramMap);
        statisticsIndexInfo.setTotalOrderCount(totalOrderCount);

        List<Map<String, Object>> subCompanyRentAmountList = orderMapper.querySubCompanyOrderAmount(paramMap);
        Map<String, BigDecimal> subCompanyRentAmount = new HashMap<>();
        for (Map<String, Object> subCompanyRentAmountMap : subCompanyRentAmountList) {
            if (subCompanyRentAmountMap.get("total_order_amount") != null && subCompanyRentAmountMap.get("total_order_amount") instanceof BigDecimal) {
                subCompanyRentAmount.put(subCompanyRentAmountMap.get("sub_company_name").toString(), (BigDecimal) subCompanyRentAmountMap.get("total_order_amount"));
            } else {
                subCompanyRentAmount.put(subCompanyRentAmountMap.get("sub_company_name").toString(), BigDecimal.ZERO);
            }
        }
        statisticsIndexInfo.setSubCompanyRentAmount(subCompanyRentAmount);

        Map<Integer, BigDecimal> monthRentAmount = new HashMap<>();
        statisticsIndexInfo.setMonthRentAmount(monthRentAmount);

        result.setResult(statisticsIndexInfo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsIncome> queryIncome(StatisticsIncomePageParam statisticsIncomePageParam) {
        ServiceResult<String, StatisticsIncome> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(statisticsIncomePageParam.getPageNo(), statisticsIncomePageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("incomeQueryParam", statisticsIncomePageParam);

        StatisticsIncome statisticsIncome = statisticsMapper.queryIncomeCount(maps);
        List<StatisticsIncomeDetail> statisticsIncomeDetailList = statisticsMapper.queryIncome(maps);
        Map<String,StatisticsIncomeDetail> statisticsIncomeDetailMap = ListUtil.listToMap(statisticsIncomeDetailList,"orderItemReferId","orderItemType");
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listAllForStatistics(maps);
        BigDecimal totalRent = BigDecimal.ZERO;    //总租金
        BigDecimal totalPrepayRent = BigDecimal.ZERO;    //总预付租金
        if(CollectionUtil.isNotEmpty(statementOrderDetailDOList)){
            for(StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList){
                String key = statementOrderDetailDO.getOrderItemReferId()+"-"+statementOrderDetailDO.getOrderItemType();
                //如果在返回列表中有数据，则处理租金及预付租金字段
                if(statisticsIncomeDetailMap.containsKey(key)){
                    StatisticsIncomeDetail statisticsIncomeDetail = statisticsIncomeDetailMap.get(key);
                    //计算查询区间内租金费用
                    BigDecimal rentAmount = calculateRentAmount(statisticsIncomePageParam.getStartTime(),statisticsIncomePageParam.getEndTime(),statementOrderDetailDO);
                    //计算查询区间内预付租金费用
                    BigDecimal prepayRentAmount =  calculatePrepayRentAmount(statisticsIncomePageParam.getEndTime(),statementOrderDetailDO);

                    statisticsIncomeDetail.setRentAmount(BigDecimalUtil.add(statisticsIncomeDetail.getRentAmount(),rentAmount));
                    statisticsIncomeDetail.setPrepayRentAmount(BigDecimalUtil.add(statisticsIncomeDetail.getPrepayRentAmount(),prepayRentAmount));
                    //统计总租金及总预付租金
                    totalRent = BigDecimalUtil.add(totalRent,rentAmount);
                    totalPrepayRent = BigDecimalUtil.add(totalPrepayRent,prepayRentAmount);
                }
            }
        }
        statisticsIncome.setTotalRent(totalRent);
        statisticsIncome.setTotalPrepayRent(totalPrepayRent);
        BigDecimal in = BigDecimalUtil.add(statisticsIncome.getTotalPrepayRent(),BigDecimalUtil.add(statisticsIncome.getTotalRent(),BigDecimalUtil.add(statisticsIncome.getTotalDeposit(),statisticsIncome.getTotalRentDeposit())));
        BigDecimal out = BigDecimalUtil.add(statisticsIncome.getTotalReturnDeposit(),statisticsIncome.getTotalReturnRentDeposit());
        statisticsIncome.setTotalIncome(BigDecimalUtil.sub(in,out));
        Page<StatisticsIncomeDetail> page = new Page<>(statisticsIncomeDetailList, statisticsIncome.getTotalCount(), statisticsIncomePageParam.getPageNo(), statisticsIncomePageParam.getPageSize());
        statisticsIncome.setStatisticsIncomeDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsIncome);
        return result;
    }

    private BigDecimal calculateRentAmount(Date startTime,Date endTime,StatementOrderDetailDO statementOrderDetailDO){
        //比较日期大小确定统计起始时间，结束时间
        //起始时间为MAX[统计起始时间，结算开始时间]
        //结束时间为MIN[统计结束时间，结算结束时间]
        Date start = DateUtil.daysBetween(startTime,statementOrderDetailDO.getStatementStartTime())>0?startTime:statementOrderDetailDO.getStatementStartTime();
        Date end = DateUtil.daysBetween(endTime,statementOrderDetailDO.getStatementEndTime())>0?statementOrderDetailDO.getStatementEndTime():endTime;
        if(OrderRentType.RENT_TYPE_MONTH.equals(statementOrderDetailDO.getRentType())){
            return BigDecimalUtil.mul(amountSupport.calculateRentAmount(start,end,statementOrderDetailDO.getGoodsUnitAmount()),new BigDecimal(statementOrderDetailDO.getGoodsCount()));
        }else if(OrderRentType.RENT_TYPE_DAY.equals(statementOrderDetailDO.getRentType())){
            //计算两日期时间差
            Integer dayCount = DateUtil.daysBetween(start,end);
            return BigDecimalUtil.mul(statementOrderDetailDO.getGoodsUnitAmount(),new BigDecimal(dayCount));
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculatePrepayRentAmount(Date endTime,StatementOrderDetailDO statementOrderDetailDO){
        //如果结算单结束时间小于等于统计结束时间，则预付租金为0
        if(DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(),endTime)>=0){
            return BigDecimal.ZERO;
        }
        BigDecimal amount = BigDecimalUtil.mul(amountSupport.calculateRentAmount(statementOrderDetailDO.getStatementStartTime(),endTime,statementOrderDetailDO.getGoodsUnitAmount()),new BigDecimal(statementOrderDetailDO.getGoodsCount()));
//        System.out.println("结算金额已交的为"+statementOrderDetailDO.getStatementDetailRentPaidAmount()+"元");
//        System.out.println("结算日起，至统计结束时间为止，需交金额为"+amount+"元");
//        System.out.println("预付"+BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentPaidAmount(),amount)+"元");
        return BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentPaidAmount(),amount);
    }
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;
    @Autowired
    private AmountSupport amountSupport;
}
