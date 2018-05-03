package com.lxzl.erp.core.service.statistics.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.statistics.*;
import com.lxzl.erp.common.domain.statistics.pojo.*;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.product.impl.support.ProductSupport;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statistics.StatisticsMapper;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
        Map<Integer, StatisticsIncomeDetail> statisticsIncomeDetailMap = ListUtil.listToMap(statisticsIncomeDetailList, "id");
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listAllForStatistics(maps);
        BigDecimal totalRent = BigDecimal.ZERO;    //总租金
        BigDecimal totalPrepayRent = BigDecimal.ZERO;    //总预付租金
        BigDecimal totalOtherPaid = BigDecimal.ZERO;    //总其他收入
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                Integer key = statementOrderDetailDO.getId();
                //计算查询区间内租金费用
                BigDecimal rentAmount = calculateRentAmount(statisticsIncomePageParam.getStartTime(), statisticsIncomePageParam.getEndTime(), statementOrderDetailDO);
                //计算查询区间内预付租金费用
                BigDecimal prepayRentAmount = calculatePrepayRentAmount(statisticsIncomePageParam.getEndTime(), statementOrderDetailDO);
                //统计总租金及总预付租金
                totalRent = BigDecimalUtil.add(totalRent, rentAmount);
                totalPrepayRent = BigDecimalUtil.add(totalPrepayRent, prepayRentAmount);
                totalOtherPaid = BigDecimalUtil.add(totalOtherPaid, statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                //如果在返回列表中有数据，则处理租金及预付租金字段
                BigDecimal inCome = BigDecimalUtil.addAll(statementOrderDetailDO.getStatementDetailOtherPaidAmount(),statementOrderDetailDO.getStatementDetailRentPaidAmount(),statementOrderDetailDO.getStatementDetailDepositPaidAmount(),
                        statementOrderDetailDO.getStatementDetailRentDepositPaidAmount(),statementOrderDetailDO.getStatementDetailOverduePaidAmount());
                inCome = BigDecimalUtil.subAll(inCome,statementOrderDetailDO.getStatementDetailDepositReturnAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount(), statementOrderDetailDO.getStatementDetailCorrectAmount());
                if (statisticsIncomeDetailMap.containsKey(key)) {
                    StatisticsIncomeDetail statisticsIncomeDetail = statisticsIncomeDetailMap.get(key);

                    statisticsIncomeDetail.setRentAmount(rentAmount);
                    statisticsIncomeDetail.setPrepayRentAmount(prepayRentAmount);
                    statisticsIncomeDetail.setOtherPaidAmount(statementOrderDetailDO.getStatementDetailOtherPaidAmount());

                    statisticsIncomeDetail.setIncomeAmount(inCome);
                }
            }
        }
        statisticsIncome.setTotalRent(totalRent);
        statisticsIncome.setTotalPrepayRent(totalPrepayRent);
        statisticsIncome.setTotalOtherPaid(totalOtherPaid);

        BigDecimal totalIncome = BigDecimalUtil.addAll(statisticsIncome.getTotalPrepayRent(),statisticsIncome.getTotalOtherPaid(),statisticsIncome.getTotalRent(),statisticsIncome.getTotalDeposit(),statisticsIncome.getTotalRentDeposit(),statisticsIncome.getTotalOverdueAmount());
        totalIncome = BigDecimalUtil.subAll(totalIncome,statisticsIncome.getTotalCorrectAmount(),statisticsIncome.getTotalReturnDeposit(),statisticsIncome.getTotalReturnRentDeposit());
        statisticsIncome.setTotalIncome(totalIncome);
        Page<StatisticsIncomeDetail> page = new Page<>(statisticsIncomeDetailList, statisticsIncome.getTotalCount(), statisticsIncomePageParam.getPageNo(), statisticsIncomePageParam.getPageSize());
        statisticsIncome.setStatisticsIncomeDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsIncome);
        return result;
    }

    @Override
    public ServiceResult<String, UnReceivable> queryUnReceivable(UnReceivablePageParam unReceivablePageParam) {
        ServiceResult<String, UnReceivable> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(unReceivablePageParam.getPageNo(), unReceivablePageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("unReceivableQueryParam", unReceivablePageParam);

        UnReceivable unReceivable = statisticsMapper.queryUnReceivableCount(maps);
        List<UnReceivableDetail> statisticsIncomeDetailList = statisticsMapper.queryUnReceivable(maps);
        Page<UnReceivableDetail> page = new Page<>(statisticsIncomeDetailList, unReceivable.getTotalCount(), unReceivablePageParam.getPageNo(), unReceivablePageParam.getPageSize());
        unReceivable.setUnReceivableDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(unReceivable);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsUnReceivable> queryStatisticsUnReceivable(StatisticsUnReceivablePageParam statisticsUnReceivablePageParam) {
        ServiceResult<String, StatisticsUnReceivable> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(statisticsUnReceivablePageParam.getPageNo(), statisticsUnReceivablePageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("statisticsUnReceivablePageParam", statisticsUnReceivablePageParam);

        StatisticsUnReceivable statisticsUnReceivable = statisticsMapper.queryStatisticsUnReceivableCount(maps);
        statisticsUnReceivable.setTotalCustomerCount(statisticsUnReceivable.getTotalRentedCustomerCountShort()+statisticsUnReceivable.getTotalRentingCustomerCountLong());
        statisticsUnReceivable.setTotalUnReceivablePercentage(getPercentage(statisticsUnReceivable.getTotalUnReceivable(),statisticsUnReceivable.getTotalLastMonthRent()));
        List<StatisticsUnReceivableDetail> statisticsUnReceivableDetailList = statisticsMapper.queryStatisticsUnReceivable(maps);
        if(CollectionUtil.isNotEmpty(statisticsUnReceivableDetailList)){
            for(StatisticsUnReceivableDetail statisticsUnReceivableDetail : statisticsUnReceivableDetailList){
                statisticsUnReceivableDetail.setCustomerCount(statisticsUnReceivableDetail.getRentedCustomerCountShort()+statisticsUnReceivableDetail.getRentingCustomerCountLong());
                statisticsUnReceivableDetail.setUnReceivablePercentage(getPercentage(statisticsUnReceivableDetail.getUnReceivable(),statisticsUnReceivableDetail.getLastMonthRent()));
            }
        }
        Page<StatisticsUnReceivableDetail> page = new Page<>(statisticsUnReceivableDetailList, statisticsUnReceivable.getTotalCount(), statisticsUnReceivablePageParam.getPageNo(), statisticsUnReceivablePageParam.getPageSize());
        statisticsUnReceivable.setStatisticsUnReceivableDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsUnReceivable);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsUnReceivableForSubCompany> queryStatisticsUnReceivableForSubCompany() {
        ServiceResult<String, StatisticsUnReceivableForSubCompany> result = new ServiceResult<>();
        StatisticsUnReceivableForSubCompany statisticsUnReceivableForSubCompany = new StatisticsUnReceivableForSubCompany();

        BigDecimal totalLastMonthRent = BigDecimal.ZERO;  //总上月租金
        BigDecimal totalUnReceivableLong= BigDecimal.ZERO;  //总长租未收
        BigDecimal totalUnReceivableShort= BigDecimal.ZERO;  //总短租未收
        BigDecimal totalUnReceivable= BigDecimal.ZERO;  //总合计未收
        Integer totalCustomerCount = 0 ;  //总客户数
        Integer totalUnReceivableCustomerCountShort = 0 ;  //短租未收客户数
        Integer totalUnReceivableCustomerCountLong = 0 ;  //长租未收客户数
        Integer totalRentedCustomerCountShort = 0 ;  //短租合作客户数
        Integer totalRentingCustomerCountLong = 0 ;  //长租在租客户数
        List<StatisticsUnReceivableDetailForSubCompany> statisticsUnReceivableDetailForSubCompanyList = statisticsMapper.querySubCompanyInfo();
        if(CollectionUtil.isNotEmpty(statisticsUnReceivableDetailForSubCompanyList)){
            for(StatisticsUnReceivableDetailForSubCompany statisticsUnReceivableDetailForSubCompany : statisticsUnReceivableDetailForSubCompanyList ){
                totalLastMonthRent = BigDecimalUtil.add(totalLastMonthRent,statisticsUnReceivableDetailForSubCompany.getLastMonthRent());
                totalUnReceivableLong = BigDecimalUtil.add(totalUnReceivableLong,statisticsUnReceivableDetailForSubCompany.getUnReceivableLong());
                totalUnReceivableShort = BigDecimalUtil.add(totalUnReceivableShort,statisticsUnReceivableDetailForSubCompany.getUnReceivableShort());
                totalUnReceivable = BigDecimalUtil.add(totalUnReceivable,statisticsUnReceivableDetailForSubCompany.getUnReceivable());
                Integer customerCount = statisticsUnReceivableDetailForSubCompany.getRentedCustomerCountShort()+statisticsUnReceivableDetailForSubCompany.getRentingCustomerCountLong();
                statisticsUnReceivableDetailForSubCompany.setCustomerCount(customerCount);
                totalCustomerCount = totalCustomerCount + customerCount;
                totalUnReceivableCustomerCountShort = totalUnReceivableCustomerCountShort + statisticsUnReceivableDetailForSubCompany.getUnReceivableCustomerCountShort();
                totalUnReceivableCustomerCountLong = totalUnReceivableCustomerCountLong + statisticsUnReceivableDetailForSubCompany.getUnReceivableCustomerCountLong();
                totalRentedCustomerCountShort = totalRentedCustomerCountShort + statisticsUnReceivableDetailForSubCompany.getRentedCustomerCountShort();
                totalRentingCustomerCountLong = totalRentingCustomerCountLong + statisticsUnReceivableDetailForSubCompany.getRentingCustomerCountLong();
                statisticsUnReceivableDetailForSubCompany.setUnReceivablePercentage(getPercentage(statisticsUnReceivableDetailForSubCompany.getUnReceivable(),statisticsUnReceivableDetailForSubCompany.getLastMonthRent()));
            }
        }
        statisticsUnReceivableForSubCompany.setTotalLastMonthRent(totalLastMonthRent );
        statisticsUnReceivableForSubCompany.setTotalUnReceivableLong(totalUnReceivableLong);
        statisticsUnReceivableForSubCompany.setTotalUnReceivableShort(totalUnReceivableShort);
        statisticsUnReceivableForSubCompany.setTotalUnReceivable(totalUnReceivable);
        statisticsUnReceivableForSubCompany.setTotalCustomerCount(totalCustomerCount );
        statisticsUnReceivableForSubCompany.setTotalUnReceivableCustomerCountShort(totalUnReceivableCustomerCountShort );
        statisticsUnReceivableForSubCompany.setTotalUnReceivableCustomerCountLong(totalUnReceivableCustomerCountLong );
        statisticsUnReceivableForSubCompany.setTotalRentedCustomerCountShort(totalRentedCustomerCountShort );
        statisticsUnReceivableForSubCompany.setTotalRentingCustomerCountLong(totalRentingCustomerCountLong );
        statisticsUnReceivableForSubCompany.setTotalUnReceivablePercentage(getPercentage(statisticsUnReceivableForSubCompany.getTotalUnReceivable(),statisticsUnReceivableForSubCompany.getTotalLastMonthRent()));

        statisticsUnReceivableForSubCompany.setStatisticsUnReceivableDetailForSubCompanyList(statisticsUnReceivableDetailForSubCompanyList);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsUnReceivableForSubCompany);
        return result;
    }

    /**
     *
     * @param d1 被除数
     * @param d2 除数
     * @return
     */
    private Double getPercentage(BigDecimal d1 , BigDecimal d2){
        d2=d2==null?BigDecimal.ZERO:d2;
        if(BigDecimal.ZERO.compareTo(d2)==0){
            return 0d;
        }else{
            BigDecimal b1 = BigDecimalUtil.mul(new BigDecimal(100),d1);
            BigDecimal per = BigDecimalUtil.div(b1,d2,2);
            return per.doubleValue();
        }
    }
    @Override
    public ServiceResult<String, StatisticsHomeByRentLengthType> queryLongRent(HomeRentParam homeRentParam) {
        ServiceResult<String, StatisticsHomeByRentLengthType> serviceResult = new ServiceResult<>();
        StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = getLongRent(homeRentParam.getStartTime(),homeRentParam.getEndTime());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statisticsHomeByRentLengthType);
        return serviceResult;
    }
    @Override
    public ServiceResult<String, StatisticsHomeByRentLengthType> queryShortRent(HomeRentParam homeRentParam) {
        ServiceResult<String, StatisticsHomeByRentLengthType> serviceResult = new ServiceResult<>();
        StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = getShortRent(homeRentParam.getStartTime(),homeRentParam.getEndTime());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statisticsHomeByRentLengthType);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<StatisticsHomeByRentLengthType>> queryLongRentByTime(HomeRentByTimeParam homeRentByTimeParam) {
        ServiceResult<String, List<StatisticsHomeByRentLengthType>> serviceResult = new ServiceResult<>();
        List<StatisticsHomeByRentLengthType> statisticsHomeByRentLengthTypeList = new ArrayList<>();
        List<TimePairs> timePairsList = getTimePairs(homeRentByTimeParam.getTimeDimensionType());
        if(CollectionUtil.isNotEmpty(timePairsList)){
            for(TimePairs timePairs : timePairsList){
                HomeRentParam homeRentParam = new HomeRentParam();
                Map<String, Object> maps = new HashMap<>();
                maps.put("homeRentParam", homeRentParam);
                StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = getLongRent(timePairs.startTime,timePairs.endTime);
                statisticsHomeByRentLengthTypeList.add(statisticsHomeByRentLengthType);
            }
        }
        addNoPassed(homeRentByTimeParam.getTimeDimensionType(),statisticsHomeByRentLengthTypeList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statisticsHomeByRentLengthTypeList);
        return serviceResult;
    }
    private void addNoPassed(Integer timeDimensionType, List<StatisticsHomeByRentLengthType> statisticsHomeByRentLengthTypeList){
        List<Date> noPassedDateList = null;
        if(timeDimensionType.equals(TimeDimensionType.TIME_DIMENSION_TYPE_MONTH)){
            noPassedDateList = DateUtil.getCurrentMonthNoPassedDay();
        }else if(timeDimensionType.equals(TimeDimensionType.TIME_DIMENSION_TYPE_YEAR)){
            noPassedDateList = DateUtil.getCurrentYearNoPassedMonth();
        }
        if(CollectionUtil.isNotEmpty(noPassedDateList)){
            for(Date date : noPassedDateList){
                StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = new StatisticsHomeByRentLengthType();
                statisticsHomeByRentLengthType.setTimeNode(date);
                statisticsHomeByRentLengthTypeList.add(statisticsHomeByRentLengthType);
            }
        }
    }
    private class TimePairs{
        Date startTime;
        Date endTime;
        public TimePairs(Date startTime,Date endTime){
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
    private List<TimePairs> getTimePairs(Integer timeDimensionType){
        List<TimePairs> timePairsList = new ArrayList<>();
        if(timeDimensionType.equals(TimeDimensionType.TIME_DIMENSION_TYPE_YEAR)){
            List<Date> dateList = DateUtil.getCurrentYearPassedMonth();
            for(Date date : dateList){
                timePairsList.add(new TimePairs(date,DateUtil.getMonthByOffset(date,1)));
            }
        }else if(timeDimensionType.equals(TimeDimensionType.TIME_DIMENSION_TYPE_MONTH)){
            List<Date> dateList = DateUtil.getCurrentMonthPassedDay();
            for(Date date : dateList){
                timePairsList.add(new TimePairs(date,DateUtil.getDayByOffset(date,1)));
            }
        }
        return timePairsList;
    }
    private StatisticsHomeByRentLengthType getLongRent(Date startTime ,Date endTime){
        HomeRentParam homeRentParam = new HomeRentParam();
        Map<String, Object> maps = new HashMap<>();
        homeRentParam.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_LONG);
        homeRentParam.setStartTime(startTime);
        homeRentParam.setEndTime(endTime);
        maps.put("homeRentParam", homeRentParam);
        StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = statisticsMapper.queryHomeByRentLengthType(maps);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listAllForHome(maps);
        BigDecimal rentDeposit = BigDecimal.ZERO;//租金押金
        BigDecimal deposit = BigDecimal.ZERO;//设备押金
        BigDecimal returnRentDeposit = BigDecimal.ZERO;//退租金押金
        BigDecimal returnDeposit = BigDecimal.ZERO;//退设备押金
        BigDecimal rent = BigDecimal.ZERO;//租金
        BigDecimal prepayRent = BigDecimal.ZERO;//预付租金
        BigDecimal rentIncome = BigDecimal.ZERO;//租金收入
        BigDecimal otherAmount = BigDecimal.ZERO;//其他费用
        if(CollectionUtil.isNotEmpty(statementOrderDetailDOList)){
            for(StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList){
                rentDeposit = BigDecimalUtil.add(rentDeposit,statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                deposit = BigDecimalUtil.add(deposit,statementOrderDetailDO.getStatementDetailDepositPaidAmount());
                //计算查询区间内租金费用
                BigDecimal rentAmount = calculateRentAmount(startTime, endTime, statementOrderDetailDO);
                //计算查询区间内预付租金费用
                BigDecimal prepayRentAmount = calculatePrepayRentAmount(endTime, statementOrderDetailDO);

                returnRentDeposit = BigDecimalUtil.add(returnRentDeposit,statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());
                returnDeposit = BigDecimalUtil.add(returnDeposit,statementOrderDetailDO.getStatementDetailDepositReturnAmount());
                rent = BigDecimalUtil.add(rent,rentAmount);
                prepayRent = BigDecimalUtil.add(prepayRent,prepayRentAmount);
                otherAmount = BigDecimalUtil.add(otherAmount,statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                rentIncome = BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.add(rentIncome,statementOrderDetailDO.getStatementDetailAmount()),statementOrderDetailDO.getStatementDetailDepositReturnAmount()),statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());
            }
        }
        statisticsHomeByRentLengthType.setRentDeposit(rentDeposit);
        statisticsHomeByRentLengthType.setReturnRentDeposit(returnRentDeposit);
        statisticsHomeByRentLengthType.setReturnDeposit(returnDeposit);
        statisticsHomeByRentLengthType.setDeposit(deposit);
        statisticsHomeByRentLengthType.setRent(rent);
        statisticsHomeByRentLengthType.setPrepayRent(prepayRent);
        statisticsHomeByRentLengthType.setOtherAmount(otherAmount);
        statisticsHomeByRentLengthType.setRentIncome(rentIncome);
        //净增台数(新增减退租)
        statisticsHomeByRentLengthType.setIncreaseProductCount(statisticsHomeByRentLengthType.getProductCountByNewCustomer()+statisticsHomeByRentLengthType.getProductCountByOldCustomer()-statisticsHomeByRentLengthType.getReturnProductCount());
        statisticsHomeByRentLengthType.setTotalOrderCount(statisticsHomeByRentLengthType.getOrderCountByNewCustomer()+statisticsHomeByRentLengthType.getOrderCountByOldCustomer());
        statisticsHomeByRentLengthType.setTotalProductCount(statisticsHomeByRentLengthType.getProductCountByNewCustomer()+statisticsHomeByRentLengthType.getProductCountByOldCustomer());
        statisticsHomeByRentLengthType.setTimeNode(startTime);
        return statisticsHomeByRentLengthType;
    }

    private StatisticsHomeByRentLengthType getShortRent(Date startTime ,Date endTime){
        HomeRentParam homeRentParam = new HomeRentParam();
        Map<String, Object> maps = new HashMap<>();
        homeRentParam.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        homeRentParam.setStartTime(startTime);
        homeRentParam.setEndTime(endTime);
        maps.put("homeRentParam", homeRentParam);
        StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = statisticsMapper.queryHomeByRentLengthType(maps);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listAllForHome(maps);
        BigDecimal rentIncome = BigDecimal.ZERO;//租金收入
        if(CollectionUtil.isNotEmpty(statementOrderDetailDOList)){
            for(StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList){
                if(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
                    rentIncome = BigDecimalUtil.add(rentIncome,statementOrderDetailDO.getStatementDetailAmount());
                }
            }
        }
        statisticsHomeByRentLengthType.setRentIncome(rentIncome);
        statisticsHomeByRentLengthType.setTotalOrderCount(statisticsHomeByRentLengthType.getOrderCountByNewCustomer()+statisticsHomeByRentLengthType.getOrderCountByOldCustomer());
        statisticsHomeByRentLengthType.setTotalProductCount(statisticsHomeByRentLengthType.getProductCountByNewCustomer()+statisticsHomeByRentLengthType.getProductCountByOldCustomer());
        statisticsHomeByRentLengthType.setTimeNode(startTime);
        return statisticsHomeByRentLengthType;
    }
    @Override
    public ServiceResult<String, List<StatisticsHomeByRentLengthType>> queryShortRentByTime(HomeRentByTimeParam homeRentByTimeParam) {
        ServiceResult<String, List<StatisticsHomeByRentLengthType>> serviceResult = new ServiceResult<>();
        List<StatisticsHomeByRentLengthType> statisticsHomeByRentLengthTypeList = new ArrayList<>();
        List<TimePairs> timePairsList = getTimePairs(homeRentByTimeParam.getTimeDimensionType());
        if(CollectionUtil.isNotEmpty(timePairsList)){
            for(TimePairs timePairs : timePairsList){
                HomeRentParam homeRentParam = new HomeRentParam();
                Map<String, Object> maps = new HashMap<>();
                maps.put("homeRentParam", homeRentParam);
                StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = getShortRent(timePairs.startTime,timePairs.endTime);
                statisticsHomeByRentLengthTypeList.add(statisticsHomeByRentLengthType);
            }
        }
        addNoPassed(homeRentByTimeParam.getTimeDimensionType(),statisticsHomeByRentLengthTypeList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statisticsHomeByRentLengthTypeList);
        return serviceResult;
    }

    @Override
    public ServiceResult queryAwaitReceivable(AwaitReceivablePageParam awaitReceivablePageParam) {
        ServiceResult<Object, Object> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(awaitReceivablePageParam.getPageNo(), awaitReceivablePageParam.getPageSize());
        Map<Object, Object> map = new HashMap<>();
        map.put("start",pageQuery.getStart());
        map.put("pageSize",pageQuery.getPageSize());
        map.put("awaitReceivableQueryParam",awaitReceivablePageParam);

        AwaitReceivable awaitReceivable = statisticsMapper.queryAwaitReceivableCount(map);
        List<AwaitReceivableDetail> awaitReceivableDetailList = statisticsMapper.queryAwaitReceivable(map);
        Page<AwaitReceivableDetail> page = new Page<>(awaitReceivableDetailList,awaitReceivable.getTotalCount(),awaitReceivablePageParam.getPageNo(),awaitReceivablePageParam.getPageSize());
        awaitReceivable.setAwaitReceivableDetailPage(page);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(awaitReceivable);
        return serviceResult;
    }

    @Override
    public ServiceResult queryStatisticsAwaitReceivable(StatisticsAwaitReceivablePageParam statisticsAwaitReceivablePageParam) {
        ServiceResult<String, StatisticsAwaitReceivable> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(statisticsAwaitReceivablePageParam.getPageNo(), statisticsAwaitReceivablePageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("statisticsAwaitReceivablePageParam", statisticsAwaitReceivablePageParam);

        StatisticsAwaitReceivable statisticsAwaitReceivable = statisticsMapper.queryStatisticsAwaitReceivableCount(maps);
        statisticsAwaitReceivable.setTotalCustomerCount(statisticsAwaitReceivable.getTotalRentedCustomerCountShort()+statisticsAwaitReceivable.getTotalRentingCustomerCountLong());
        statisticsAwaitReceivable.setTotalAwaitReceivablePercentage(getPercentage(statisticsAwaitReceivable.getTotalAwaitReceivable(),statisticsAwaitReceivable.getTotalLastMonthRent()));
        List<StatisticsAwaitReceivableDetail> statisticsAwaitReceivableDetailList = statisticsMapper.queryStatisticsAwaitReceivable(maps);
        if(CollectionUtil.isNotEmpty(statisticsAwaitReceivableDetailList)){
            for(StatisticsAwaitReceivableDetail statisticsAwaitReceivableDetail : statisticsAwaitReceivableDetailList){
                statisticsAwaitReceivableDetail.setCustomerCount(statisticsAwaitReceivableDetail.getRentedCustomerCountShort()+statisticsAwaitReceivableDetail.getRentingCustomerCountLong());
                statisticsAwaitReceivableDetail.setAwaitReceivablePercentage(getPercentage(statisticsAwaitReceivableDetail.getAwaitReceivable(),statisticsAwaitReceivableDetail.getLastMonthRent()));
            }
        }
        Page<StatisticsAwaitReceivableDetail> page = new Page<>(statisticsAwaitReceivableDetailList, statisticsAwaitReceivable.getTotalCount(), statisticsAwaitReceivablePageParam.getPageNo(), statisticsAwaitReceivablePageParam.getPageSize());
        statisticsAwaitReceivable.setStatisticsAwaitReceivableDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsAwaitReceivable);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsSalesman> querySalesman(StatisticsSalesmanPageParam statisticsSalesmanPageParam) {
        ServiceResult<String, StatisticsSalesman> result = new ServiceResult<>();
        String orderBy = statisticsSalesmanPageParam.getOrderBy();
        if (StringUtil.isNotBlank(orderBy) && !StatisticsSalesmanOrderBy.isValid(orderBy)) {
            result.setErrorCode(ErrorCode.PARAM_IS_ERROR);
            return result;
        }

        // 转换为数据库排序字段, orderType在pageQuery对象中已验证
        statisticsSalesmanPageParam.setOrderBy(StatisticsSalesmanOrderBy.getDataFiled(orderBy));

        // 格式化查询时间
        Date startTime = statisticsSalesmanPageParam.getStartTime();
        Calendar ca = Calendar.getInstance();
        ca.setTime(startTime);// someDate 为你要获取的那个月的时间
        ca.set(Calendar.DAY_OF_MONTH, 1);
        Date start = ca.getTime(); // 当月第一天
        ca.add(Calendar.MONTH, 1);
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date end = ca.getTime(); // 当月最后一天
        statisticsSalesmanPageParam.setStartTime(start);
        statisticsSalesmanPageParam.setEndTime(end);

        PageQuery pageQuery = new PageQuery(statisticsSalesmanPageParam.getPageNo(), statisticsSalesmanPageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("salesmanQueryParam", statisticsSalesmanPageParam);

        StatisticsSalesman statisticsSalesman = statisticsMapper.querySalesmanCount(maps);
        StatisticsSalesmanIncome statisticsSalesmanIncome = statisticsMapper.querySalesmanIncomeCount(maps);
        // 计算总应收
        if (statisticsSalesmanIncome != null) {
            statisticsSalesman.setTotalIncome(statisticsSalesmanIncome.getTotalIncome());
            statisticsSalesman.setTotalReceive(statisticsSalesmanIncome.getTotalAwaitReceivable().add(statisticsSalesmanIncome.getTotalIncome()));
        }

        // 查询以业务员，分公司分组的初步数据(主数据)
        List<StatisticsSalesmanDetailTwo> statisticsSalesmanDetailTwoList = statisticsMapper.querySalesmanDetailTwo(maps);
        // 装换为salesmanId-subCompnayId为key的map
        Map<String, StatisticsSalesmanDetailTwo> statisticsSalesmanDetailTwoMap = ListUtil.listToMap(statisticsSalesmanDetailTwoList, "salesmanId", "subCompanyId", "rentLengthType");

        // 计算应收 = 待收 + 实收
        List<StatisticsSalesmanDetailIncome> statisticsSalesmanDetailIncomeList = statisticsMapper.querySalesmanDetailIncome(maps);
        for (StatisticsSalesmanDetailIncome statisticsSalesmanDetailIncome : statisticsSalesmanDetailIncomeList) {
            String key = statisticsSalesmanDetailIncome.getSalesmanId() + "-" + statisticsSalesmanDetailIncome.getSubCompanyId() + "-" + statisticsSalesmanDetailIncome.getRentLengthType();
            StatisticsSalesmanDetailTwo statisticsSalesmanDetailTwo = statisticsSalesmanDetailTwoMap.get(key);
            if (statisticsSalesmanDetailTwo != null) {
                statisticsSalesmanDetailTwo.setIncome(statisticsSalesmanDetailTwo.getIncome().add(statisticsSalesmanDetailIncome.getIncome()));
                statisticsSalesmanDetailTwo.setReceive(statisticsSalesmanDetailTwo.getReceive().add(statisticsSalesmanDetailIncome.getAwaitReceivable()).add(statisticsSalesmanDetailIncome.getIncome()));
            }
        }


        for (StatisticsSalesmanDetailTwo statisticsSalesmanDetailTwo : statisticsSalesmanDetailTwoList) {
            statisticsSalesmanDetailTwo.setReceive(statisticsSalesmanDetailTwo.getAwaitReceivable().add(statisticsSalesmanDetailTwo.getIncome()));
            statisticsSalesmanDetailTwo.setPureIncrease(BigDecimal.valueOf(0));
        }

        // 查询扩展数据来计算净增台数
        List<StatisticsSalesmanDetailTwoExtend> statisticsSalesmanDetailTwoExtendList = statisticsMapper.querySalesmanDetailTwoExtend(maps);
        List<StatisticsSalesmanReturnOrder> statisticsSalesmanReturnOrderList = statisticsMapper.querySalesmanReturnOrder(maps);

        // 遍历计算每一订单项净增台数累加到相应的StatisticsSalesmanDetailTwo实体中，只算长租
        for (StatisticsSalesmanDetailTwoExtend statisticsSalesmanDetailTwoExtend : statisticsSalesmanDetailTwoExtendList) {
            if (RentLengthType.RENT_LENGTH_TYPE_LONG == statisticsSalesmanDetailTwoExtend.getRentLengthType()) {
                String key = statisticsSalesmanDetailTwoExtend.getSalesmanId() + "-" + statisticsSalesmanDetailTwoExtend.getSubCompanyId() + "-" + statisticsSalesmanDetailTwoExtend.getRentLengthType();
                StatisticsSalesmanDetailTwo statisticsSalesmanDetailTwo = statisticsSalesmanDetailTwoMap.get(key);
                if (statisticsSalesmanDetailTwo != null) {
                    BigDecimal increaseProduct = calcPureIncrease(statisticsSalesmanDetailTwoExtend);
                    statisticsSalesmanDetailTwo.setPureIncrease(statisticsSalesmanDetailTwo.getPureIncrease().add(increaseProduct));
                }
            }
        }

        // 遍历计算每一订单项净增台数累加到相应的StatisticsSalesmanDetailTwo实体中，只算长租
        for (StatisticsSalesmanReturnOrder statisticsSalesmanReturnOrder : statisticsSalesmanReturnOrderList) {
            if (RentLengthType.RENT_LENGTH_TYPE_LONG == statisticsSalesmanReturnOrder.getRentLengthType()) {
                String key = statisticsSalesmanReturnOrder.getEuId() + "-" + statisticsSalesmanReturnOrder.getEscId() + "-" + statisticsSalesmanReturnOrder.getRentLengthType();
                StatisticsSalesmanDetailTwo statisticsSalesmanDetailTwo = statisticsSalesmanDetailTwoMap.get(key);
                if (statisticsSalesmanDetailTwo != null) {
                    BigDecimal returnProduct = calcPureReturn(statisticsSalesmanReturnOrder);
                    statisticsSalesmanDetailTwo.setPureIncrease(statisticsSalesmanDetailTwo.getPureIncrease().subtract(returnProduct));
                }
            }
        }

        Page<StatisticsSalesmanDetailTwo> page = new Page<>(statisticsSalesmanDetailTwoList, statisticsSalesman.getTotalCount(), statisticsSalesmanPageParam.getPageNo(), statisticsSalesmanPageParam.getPageSize());
        statisticsSalesman.setStatisticsSalesmanDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsSalesman);
        return result;
    }

    // 计算每一订单项的净增台数
    private BigDecimal calcPureIncrease(StatisticsSalesmanDetailTwoExtend statisticsSalesmanDetailTwoExtend) {
        // 1. 按商品类型折算为实际台数
        Double productCount = statisticsSalesmanDetailTwoExtend.getProductCount() * statisticsSalesmanDetailTwoExtend.getProductCountFactor();
        // 2. 计算折扣系数，并算法订单净增数
        BigDecimal dis = statisticsSalesmanDetailTwoExtend.getRentPrice().divide(statisticsSalesmanDetailTwoExtend.getProductUnitAmount(), 5);
        // 折扣系数最大为1
        if (dis.compareTo(BigDecimal.valueOf(1)) > 0) {
            dis = BigDecimal.valueOf(1);
        }
        BigDecimal orderIncreaseProduce = dis.multiply(BigDecimal.valueOf(productCount));
        // 3. 计算属地化
        BigDecimal performance;
        Date localizationTime = statisticsSalesmanDetailTwoExtend.getLocalizationTime();
        Date createTime = statisticsSalesmanDetailTwoExtend.getCreateTime();
        if (localizationTime == null || createTime == null) {
            performance = BigDecimal.valueOf(1);
        } else if (DateUtil.getMonthSpace(localizationTime, createTime) <=3 ) {
            performance = BigDecimal.valueOf(0.3);
        } else {
            performance = BigDecimal.valueOf(0.7);
        }

        // 4. 计算订单净增和属地化
        BigDecimal result = performance.multiply(orderIncreaseProduce);
        return result;
    }

    // 计算每一退货订单项的台数
    private BigDecimal calcPureReturn(StatisticsSalesmanReturnOrder statisticsSalesmanReturnOrder) {
        // 不计算配件
        if (productSupport.isMaterial(statisticsSalesmanReturnOrder.getProductNo())) {
            return BigDecimal.valueOf(0);
        }

        // 1. 计算属地化
        BigDecimal performance;
        Date localizationTime = statisticsSalesmanReturnOrder.getLocalizationTime();
        Date createTime = statisticsSalesmanReturnOrder.getCreateTime();
        if (localizationTime == null || createTime == null) {
            performance = BigDecimal.valueOf(1);
        } else if (DateUtil.getMonthSpace(localizationTime, createTime) <=3 ) {
            performance = BigDecimal.valueOf(0.3);
        } else {
            performance = BigDecimal.valueOf(0.7);
        }

        BigDecimal returnProduct = BigDecimal.valueOf(0);

        // 4.根据eopId找到对应的退货单，并计算
        if (statisticsSalesmanReturnOrder.getProductCount() != null
                && statisticsSalesmanReturnOrder.getReturnTime() != null
                && OrderRentType.RENT_TYPE_MONTH.equals(statisticsSalesmanReturnOrder.getReturnType())) {
            // 计算已租月数
            Integer months = DateUtil.getMonthSpace(statisticsSalesmanReturnOrder.getRentStartTime(), statisticsSalesmanReturnOrder.getReturnTime());
            Integer rentTimeLength = statisticsSalesmanReturnOrder.getRentTimeLength();
            if (rentTimeLength != null && rentTimeLength > months) {
                returnProduct = BigDecimal.valueOf(rentTimeLength - months).divide(BigDecimal.valueOf(rentTimeLength), 5);
                returnProduct = returnProduct.multiply(BigDecimal.valueOf(statisticsSalesmanReturnOrder.getProductCountFactor()));
            }
        }

        // 4. 计算订单净增和属地化
        BigDecimal result = performance.multiply(returnProduct);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsRentInfo> queryRentInfo(StatisticsRentInfoPageParam statisticsRentInfoPageParam) {
        ServiceResult<String, StatisticsRentInfo> serviceResult = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", statisticsRentInfoPageParam.getStart());
        maps.put("pageSize", statisticsRentInfoPageParam.getPageSize());
        maps.put("rentInfoQueryParam", statisticsRentInfoPageParam);
        StatisticsRentInfo statisticsRentInfo = statisticsMapper.queryRentInfoCount(maps);
        List<StatisticsRentInfoDetail> statisticsRentInfoDetailList = statisticsMapper.queryRentInfoDetail(maps);
        // 转换成key为salesmanId-subCompanyId-rentLengthType的map
        Map<String, StatisticsRentInfoDetail> statisticsRentInfoDetailMap = ListUtil.listToMap(statisticsRentInfoDetailList, "salesmanId", "subCompanyId", "rentLengthType");
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listAllForRentInfo(maps);
        Date startTime = statisticsRentInfoPageParam.getStartTime();
        Date endTime = statisticsRentInfoPageParam.getEndTime();
        BigDecimal rentDeposit = BigDecimal.ZERO;//租金押金
        BigDecimal deposit = BigDecimal.ZERO;//设备押金
        BigDecimal returnRentDeposit = BigDecimal.ZERO;//退租金押金
        BigDecimal returnDeposit = BigDecimal.ZERO;//退设备押金
        BigDecimal rent = BigDecimal.ZERO;//租金
        BigDecimal prepayRent = BigDecimal.ZERO;//预付租金
        BigDecimal rentIncome = BigDecimal.ZERO;//租金收入
        BigDecimal otherAmount = BigDecimal.ZERO;//其他费用
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) { // 将每一个结算单计算到总计上和长短租详情列表的对应项上
                // 1. 添加到总计上
                rentDeposit = BigDecimalUtil.add(rentDeposit,statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                deposit = BigDecimalUtil.add(deposit,statementOrderDetailDO.getStatementDetailDepositPaidAmount());

                //计算查询区间内租金费用
                BigDecimal rentAmount = calculateRentAmount(startTime, endTime, statementOrderDetailDO);
                //计算查询区间内预付租金费用
                BigDecimal prepayRentAmount = calculatePrepayRentAmount(endTime, statementOrderDetailDO);

                returnRentDeposit = BigDecimalUtil.add(returnRentDeposit,statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());
                returnDeposit = BigDecimalUtil.add(returnDeposit,statementOrderDetailDO.getStatementDetailDepositReturnAmount());
                rent = BigDecimalUtil.add(rent,rentAmount);
                prepayRent = BigDecimalUtil.add(prepayRent,prepayRentAmount);
                otherAmount = BigDecimalUtil.add(otherAmount,statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                rentIncome = BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.add(rentIncome,statementOrderDetailDO.getStatementDetailAmount()),statementOrderDetailDO.getStatementDetailDepositReturnAmount()),statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());

                // 2. 添加到长短租详情列表的对应项上，因为是分页查询，可能有些结算单详情项没有在这一页的数据上，为null
                String mapKey = getStatementOrderDetailKeyForRentInfo(statementOrderDetailDO);
                StatisticsRentInfoDetail statisticsRentInfoDetail = statisticsRentInfoDetailMap.get(mapKey);
                if (statisticsRentInfoDetail != null) {
                    BigDecimalUtil.add(statisticsRentInfoDetail.getRentDeposit(),statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                    BigDecimalUtil.add(statisticsRentInfoDetail.getDeposit(),statementOrderDetailDO.getStatementDetailDepositPaidAmount());
                    BigDecimalUtil.add(statisticsRentInfoDetail.getReturnRentDeposit(),statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());
                    BigDecimalUtil.add(statisticsRentInfoDetail.getRentDeposit(),statementOrderDetailDO.getStatementDetailDepositReturnAmount());
                    BigDecimalUtil.add(statisticsRentInfoDetail.getRent(),rentAmount);
                    BigDecimalUtil.add(statisticsRentInfoDetail.getPrepayRent(),prepayRentAmount);
                    BigDecimalUtil.add(statisticsRentInfoDetail.getOtherAmount(),statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                    BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.add(statisticsRentInfoDetail.getRentIncome(),statementOrderDetailDO.getStatementDetailAmount()),statementOrderDetailDO.getStatementDetailDepositReturnAmount()),statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());

                    //净增台数(新增减退租)
                    statisticsRentInfoDetail.setIncreaseProductCount(statisticsRentInfoDetail.getProductCountByNewCustomer()+statisticsRentInfoDetail.getProductCountByOldCustomer()-statisticsRentInfoDetail.getReturnProductCount());
                    statisticsRentInfoDetail.setTotalOrderCount(statisticsRentInfoDetail.getOrderCountByNewCustomer()+statisticsRentInfoDetail.getOrderCountByOldCustomer());
                    statisticsRentInfoDetail.setTotalProductCount(statisticsRentInfoDetail.getProductCountByNewCustomer()+statisticsRentInfoDetail.getProductCountByOldCustomer());
                }
            }
        }
        statisticsRentInfo.setRentDeposit(rentDeposit);
        statisticsRentInfo.setReturnRentDeposit(returnRentDeposit);
        statisticsRentInfo.setReturnDeposit(returnDeposit);
        statisticsRentInfo.setDeposit(deposit);
        statisticsRentInfo.setRent(rent);
        statisticsRentInfo.setPrepayRent(prepayRent);
        statisticsRentInfo.setOtherAmount(otherAmount);
        statisticsRentInfo.setRentIncome(rentIncome);

        //净增台数(新增减退租)
        statisticsRentInfo.setIncreaseProductCount(statisticsRentInfo.getProductCountByNewCustomer()+statisticsRentInfo.getProductCountByOldCustomer()-statisticsRentInfo.getReturnProductCount());
        statisticsRentInfo.setTotalOrderCount(statisticsRentInfo.getOrderCountByNewCustomer()+statisticsRentInfo.getOrderCountByOldCustomer());
        statisticsRentInfo.setTotalProductCount(statisticsRentInfo.getProductCountByNewCustomer()+statisticsRentInfo.getProductCountByOldCustomer());

        Page<StatisticsRentInfoDetail> page = new Page<>(statisticsRentInfoDetailList, statisticsRentInfo.getTotalCount(), statisticsRentInfoPageParam.getPageNo(), statisticsRentInfoPageParam.getPageSize());
        statisticsRentInfo.setStatisticsRentInfoDetailPage(page);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statisticsRentInfo);
        return serviceResult;
    }

    /*
    获取key为salesmanId-subCompanyId-rentLengthType
     */
    private String getStatementOrderDetailKeyForRentInfo(StatementOrderDetailDO statementOrderDetailDO) {
        if (statementOrderDetailDO.getSubCompanyId() != null && statementOrderDetailDO.getSalesmanId() != null && statementOrderDetailDO.getRentLengthType() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(statementOrderDetailDO.getSalesmanId());
            sb.append("-");
            sb.append(statementOrderDetailDO.getSubCompanyId());
            sb.append("-");
            sb.append(statementOrderDetailDO.getRentLengthType());
            return sb.toString();
        }
        return StringUtil.EMPTY;
    }

    private BigDecimal calculateRentAmount(Date startTime, Date endTime, StatementOrderDetailDO statementOrderDetailDO) {
        //比较日期大小确定统计起始时间，结束时间
        //起始时间为MAX[统计起始时间，结算开始时间]
        //结束时间为MIN[统计结束时间，结算结束时间]
        if(!StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
            return BigDecimal.ZERO;
        }
        Date start = startTime.compareTo(statementOrderDetailDO.getStatementStartTime()) > 0 ? startTime : statementOrderDetailDO.getStatementStartTime();
        Date end = endTime.compareTo(statementOrderDetailDO.getStatementEndTime()) < 0 ? endTime : statementOrderDetailDO.getStatementEndTime();

        if (OrderRentType.RENT_TYPE_MONTH.equals(statementOrderDetailDO.getRentType())) {
            return BigDecimalUtil.mul(amountSupport.calculateRentAmount(start, end, statementOrderDetailDO.getGoodsUnitAmount()), new BigDecimal(statementOrderDetailDO.getGoodsCount()));
        } else if (OrderRentType.RENT_TYPE_DAY.equals(statementOrderDetailDO.getRentType())) {
            //计算两日期时间差
            Integer dayCount = DateUtil.daysBetween(start, end);

            // 结算单开始和结束同一天也算一天，都要加上一天
            dayCount = dayCount + 1;
            return BigDecimalUtil.mul(BigDecimalUtil.mul(statementOrderDetailDO.getGoodsUnitAmount(), new BigDecimal(dayCount)), new BigDecimal(statementOrderDetailDO.getGoodsCount()));
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculatePrepayRentAmount(Date endTime, StatementOrderDetailDO statementOrderDetailDO) {
        //如果结算单结束时间小于等于统计结束时间，则预付租金为0
        if (DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(), endTime) >= 0) {
            return BigDecimal.ZERO;
        }
        if(!StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
            return BigDecimal.ZERO;
        }
        BigDecimal amount = BigDecimalUtil.mul(amountSupport.calculateRentAmount(statementOrderDetailDO.getStatementStartTime(), endTime, statementOrderDetailDO.getGoodsUnitAmount()), new BigDecimal(statementOrderDetailDO.getGoodsCount()));
//        System.out.println("结算金额已交的为"+statementOrderDetailDO.getStatementDetailRentPaidAmount()+"元");
//        System.out.println("结算日起，至统计结束时间为止，需交金额为"+amount+"元");
//        System.out.println("预付"+BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentPaidAmount(),amount)+"元");
        BigDecimal prepayRentAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentPaidAmount(), amount);
        return BigDecimalUtil.compare(prepayRentAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : prepayRentAmount;
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
    @Autowired
    private SubCompanyMapper subCompanyMapper;
    @Autowired
    private ProductSupport productSupport;
}
