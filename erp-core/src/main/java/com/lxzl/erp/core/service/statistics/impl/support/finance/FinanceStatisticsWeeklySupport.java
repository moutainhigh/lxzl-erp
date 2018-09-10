package com.lxzl.erp.core.service.statistics.impl.support.finance;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.statistics.FinanceStatisticsParam;
import com.lxzl.erp.common.domain.statistics.pojo.FinanceStatisticsDataWeeklyExcel;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.statistics.impl.StatisticsServiceImpl;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statistics.FinanceStatisticsDataWeeklyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statistics.StatisticsMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.statistics.*;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/7/23
 * Time: 10:28
 */
@Component
public class FinanceStatisticsWeeklySupport {

    public Integer countFinanceStatisticsDataMetaPage(FinanceStatisticsParam param) {
        if (param == null) {
            return null;
        }
        Map<String, Object> maps = financeStatisticsWeeklyParamToMap(param);
        return financeStatisticsDataWeeklyMapper.countFinanceStatisticsDataMetaPage(maps);
    }

    public List<FinanceStatisticsDataMeta> listFinanceStatisticsDataMetaPage(FinanceStatisticsParam param) {
        if (param == null) {
            return null;
        }
        Map<String, Object> maps = financeStatisticsWeeklyParamToMap(param);
        return financeStatisticsDataWeeklyMapper.listFinanceStatisticsDataMetaPage(maps);
    }

    public List<FinanceStatisticsRentProductDetail> statisticsRentProductDetail(FinanceStatisticsParam paramVo) {
        if (paramVo == null) {
            return null;
        }
        Map<String, Object> maps = financeStatisticsWeeklyParamToMap(paramVo);
        return statisticsMapper.statisticsRentProductDetail(maps);
    }

    public List<FinanceStatisticsReturnProductDetail> statisticsReturnProductDetail(FinanceStatisticsParam paramVo) {
        if (paramVo == null) {
            return null;
        }
        Map<String, Object> maps = financeStatisticsWeeklyParamToMap(paramVo);
        return statisticsMapper.statisticsReturnProductDetail(maps);
    }

    /**
     * 统计成交客户数量 { 订单所属公司为八个分公司（KA）的订单按订单所属分公司分组统计，订单所属公司为电销或者大客户渠道的订单则按执行分公司分组统计}
     * @param param
     * @return
     */
    private List<FinanceStatisticsDealsCountBySubCompany> statisticsCustomerCountWeekly(FinanceStatisticsParam param) {
        if (param == null) {
            return null;
        }
        Map<String, Object> maps = financeStatisticsWeeklyParamToMap(param);
        return statisticsMapper.statisticsCustomerCountWeekly(maps);
    }

    private Map<String, Object> financeStatisticsWeeklyParamToMap(FinanceStatisticsParam param) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("statisticsInterval", param.getStatisticsInterval());
        maps.put("year", param.getYear());
        maps.put("month", param.getMonth());
        maps.put("weekOfMonth", param.getWeekOfMonth());
        maps.put("orderOrigin", param.getOrderOrigin());
        maps.put("rentLengthType", param.getRentLengthType());
        maps.put("statisticsStartTime", param.getStatisticsStartTime());
        maps.put("statisticsEndTime", param.getStatisticsEndTime());
        if (StringUtil.isNotBlank(param.getNewCustomerKey())) {
            maps.put("newCustomerKey", param.getNewCustomerKey());
        }
        maps.put("start", param.getStart());
        maps.put("pageSize", param.getPageSize());
        return maps;
    }

    /**
     * 统计租赁出库商品数量 { 订单所属公司为八个分公司（KA）的订单按订单所属分公司分组统计，订单所属公司为电销或者大客户渠道的订单则按执行分公司分组统计}
     * @param param
     * @return
     */
    private List<FinanceStatisticsDealsCountBySubCompany> statisticsRentProductCountWeekly(FinanceStatisticsParam param) {
        if (param == null) {
            return null;
        }
        Map<String, Object> maps = financeStatisticsWeeklyParamToMap(param);
        return statisticsMapper.statisticsRentProductCountWeekly(maps);
    }

    /**
     * 统计退货商品数量 { 订单所属公司为八个分公司（KA）的订单按订单所属分公司分组统计，订单所属公司为电销或者大客户渠道的订单则按执行分公司分组统计}
     * @param param
     * @return
     */
    private List<FinanceStatisticsDealsCountBySubCompany> statisticsReturnProductCountWeekly(FinanceStatisticsParam param) {
        if (param == null) {
            return null;
        }
        Map<String, Object> maps = financeStatisticsWeeklyParamToMap(param);
        return statisticsMapper.statisticsReturnProductCountWeekly(maps);
    }

    // 获取此日期所在周的第一天
     public Date getFirstDayOfCurrentWeek(Date date) {
        /*Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            //cal.setFirstDayOfWeek(Calendar.MONDAY);  //设置星期一为当周第一天
            cal.set(Calendar.DAY_OF_WEEK, 1);
            //cal.add(Calendar.DAY_OF_WEEK, -6); //并且设置为当周第一天
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal.getTime();*/
        return getFirstDayOfCurrentWeek(date, SETTING_FIRST_DAY_OF_WEEK);
    }

    public Date getFirstDayOfCurrentWeek(Date date, int settingFirstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.setFirstDayOfWeek(settingFirstDayOfWeek);//设置星期五为当周第一天
            cal.setMinimalDaysInFirstWeek(1); //第一周的最小天数
            cal.set(Calendar.DAY_OF_WEEK, settingFirstDayOfWeek);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal.getTime();
    }

    public Date getLastDayOfCurrentWeek(Date date, int settingFirstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.setFirstDayOfWeek(settingFirstDayOfWeek);//设置星期五为当周第一天
            cal.setMinimalDaysInFirstWeek(1); //第一周的最小天数
            cal.set(Calendar.DAY_OF_WEEK, settingFirstDayOfWeek - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal.getTime();
    }

    public int getMaxWeekCountOfYearAndMonth(int year, int month) {
        Calendar currentCalendar = getCalendarInstance(SETTING_FIRST_DAY_OF_WEEK)/*Calendar.getInstance()*/;
        currentCalendar.set(Calendar.YEAR, year);
        currentCalendar.set(Calendar.MONTH, month - 1);
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return currentCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    public List<FinanceStatisticsDataWeeklyDO> reStatisticsFinanceDataWeekly(FinanceStatisticsParam paramVo) {
        int year = paramVo.getYear();
        int month = paramVo.getMonth();
        int weekOfMonth = paramVo.getWeekOfMonth();
        boolean isHistoryData = !isCurrentWeek(year, month, weekOfMonth);  //如果不是当前周,则都认为是历史统计数据,后面则从数据库里面取数据
        List<FinanceStatisticsDataWeeklyDO> financeAllStatisticsDataWeekly = new ArrayList<>();
        //统计KA
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_KA);
        List<FinanceStatisticsDataWeeklyDO> financeKAStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData, true);
        if (CollectionUtil.isNotEmpty(financeKAStatisticsDataWeekly)) {
            financeAllStatisticsDataWeekly.addAll(financeKAStatisticsDataWeekly);
        }
        //统计电销
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_TMK);
        List<FinanceStatisticsDataWeeklyDO> financeTMKStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData, true);
        if (CollectionUtil.isNotEmpty(financeTMKStatisticsDataWeekly)) {
            financeAllStatisticsDataWeekly.addAll(financeTMKStatisticsDataWeekly);
        }
        // 统计大客户渠道
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_BCC);
        List<FinanceStatisticsDataWeeklyDO> financeBCCStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData, true);
        if (CollectionUtil.isNotEmpty(financeBCCStatisticsDataWeekly)) {
            financeAllStatisticsDataWeekly.addAll(financeBCCStatisticsDataWeekly);
        }
        //将此次重新统计的数据保存到数据库
        freshStatisticsDataToDB(year, month, weekOfMonth, financeAllStatisticsDataWeekly);
        return financeAllStatisticsDataWeekly;
    }

    public List<FinanceStatisticsDataWeeklyDO> statisticsFinanceDataMonthly(FinanceStatisticsParam paramVo) {
        return statisticsFinanceDataMonthly(paramVo, false);
    }

    public List<FinanceStatisticsDataWeeklyDO> reStatisticsFinanceDataMonthly(FinanceStatisticsParam paramVo) {
        return statisticsFinanceDataMonthly(paramVo, true);
    }

    public List<FinanceStatisticsDataWeeklyDO> statisticsFinanceDataMonthly(FinanceStatisticsParam paramVo, boolean needReStatistics) {
        int year = paramVo.getYear();
        int month = paramVo.getMonth();
        int weekOfMonth = paramVo.getWeekOfMonth();
        boolean isHistoryData = !isCurrentWeek(year, month, weekOfMonth);
        List<FinanceStatisticsDataWeeklyDO> financeAllStatisticsDataWeekly = new ArrayList<>();
        //统计KA
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_KA);
        List<FinanceStatisticsDataWeeklyDO> financeKAStatisticsDataWeekly = null;
        if (!needReStatistics) {
            financeKAStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData);
        }
        fillFinanceStatisticsData(paramVo, isHistoryData, financeAllStatisticsDataWeekly, financeKAStatisticsDataWeekly);
        //统计电销
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_TMK);
        List<FinanceStatisticsDataWeeklyDO> financeTMKStatisticsDataWeekly = null;
        if (!needReStatistics) {
            financeTMKStatisticsDataWeekly =  statisticsFinanceDataWeekly(paramVo, isHistoryData);
        }
        fillFinanceStatisticsData(paramVo, isHistoryData, financeAllStatisticsDataWeekly, financeTMKStatisticsDataWeekly);
        // 统计大客户渠道
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_BCC);
        List<FinanceStatisticsDataWeeklyDO> financeBCCStatisticsDataWeekly = null;
        if (!needReStatistics) {
            financeBCCStatisticsDataWeekly =  statisticsFinanceDataWeekly(paramVo, isHistoryData);
        }
        fillFinanceStatisticsData(paramVo, isHistoryData, financeAllStatisticsDataWeekly, financeBCCStatisticsDataWeekly);
        //将此次重新统计的数据保存到数据库
        if (needReStatistics && CollectionUtil.isNotEmpty(financeAllStatisticsDataWeekly)) {
            freshStatisticsDataToDB(year, month, weekOfMonth, financeAllStatisticsDataWeekly);
        }
        return financeAllStatisticsDataWeekly;
    }

    private void fillFinanceStatisticsData(FinanceStatisticsParam paramVo, boolean isHistoryData, List<FinanceStatisticsDataWeeklyDO> financeAllStatisticsDataWeekly, List<FinanceStatisticsDataWeeklyDO> financeKAStatisticsDataWeekly) {
        if (CollectionUtil.isEmpty(financeKAStatisticsDataWeekly)) {
            financeKAStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData, true);
            if (CollectionUtil.isNotEmpty(financeKAStatisticsDataWeekly)) {
                financeAllStatisticsDataWeekly.addAll(financeKAStatisticsDataWeekly);
            }
        } else {
            financeAllStatisticsDataWeekly.addAll(financeKAStatisticsDataWeekly);
        }
    }


    private void freshStatisticsDataToDB(int year, int month, int weekOfMonth, List<FinanceStatisticsDataWeeklyDO> financeAllStatisticsDataWeekly) {
        Map<String,Object> deleteParamMap = new HashMap<>();
        deleteParamMap.put("year", year);
        deleteParamMap.put("month", month);
        deleteParamMap.put("weekOfMonth", weekOfMonth);
        // 防止本周已经保存过统计数据，可以先删除本周的统计数据
        financeStatisticsDataWeeklyMapper.deleteWhenCause(deleteParamMap);
        // 重新保存到数据库
        financeStatisticsDataWeeklyMapper.saveList(financeAllStatisticsDataWeekly);
    }

    /**
     * 被统计的差异数据
     * @param paramVo
     * @return
     */
    public List<FinanceStatisticsDataWeeklyDO> statisticsDiffFinanceDataWeekly(FinanceStatisticsParam paramVo){
        int year = paramVo.getYear();
        int month = paramVo.getMonth();
        int weekOfMonth = paramVo.getWeekOfMonth();
        boolean isHistoryData = !isCurrentWeek(year, month, weekOfMonth);  //如果不是当前周,则都认为是历史统计数据,后面则从数据库里面取数据
        List<FinanceStatisticsDataWeeklyDO> financeAllStatisticsDataWeekly = new ArrayList<>();
        List<FinanceStatisticsDataWeeklyDO> diffFinanceAllStatisticsDataWeekly = new ArrayList<>();
        //统计KA
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_KA);
        List<FinanceStatisticsDataWeeklyDO> financeKAStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData);
        if (CollectionUtil.isNotEmpty(financeKAStatisticsDataWeekly)) {
            diffFinanceAllStatisticsDataWeekly.addAll(getDiffStatisticsFinanceDataWeekly(financeKAStatisticsDataWeekly, paramVo, isHistoryData));
            financeAllStatisticsDataWeekly.addAll(financeKAStatisticsDataWeekly);
        }
        //统计电销
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_TMK);
        List<FinanceStatisticsDataWeeklyDO> financeTMKStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData);
        if (CollectionUtil.isNotEmpty(financeTMKStatisticsDataWeekly)) {
            diffFinanceAllStatisticsDataWeekly.addAll(getDiffStatisticsFinanceDataWeekly(financeTMKStatisticsDataWeekly, paramVo, isHistoryData));
            financeAllStatisticsDataWeekly.addAll(financeTMKStatisticsDataWeekly);
        }
        // 统计大客户渠道
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_BCC);
        List<FinanceStatisticsDataWeeklyDO> financeBCCStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData);
        if (CollectionUtil.isNotEmpty(financeBCCStatisticsDataWeekly)) {
            diffFinanceAllStatisticsDataWeekly.addAll(getDiffStatisticsFinanceDataWeekly(financeBCCStatisticsDataWeekly, paramVo, isHistoryData));
            financeAllStatisticsDataWeekly.addAll(financeBCCStatisticsDataWeekly);
        }

        //如果是当前周,则将此次统计数据保存到数据库
        if (!isHistoryData && CollectionUtil.isNotEmpty(financeAllStatisticsDataWeekly)) {
            freshStatisticsDataToDB(year, month, weekOfMonth, financeAllStatisticsDataWeekly);
        }
     /*   for(FinanceStatisticsDataWeeklyDO financeStatisticsDataWeeklyDO: financeAllStatisticsDataWeekly) {
            System.out.println(financeStatisticsDataWeeklyDO.toString());
        }
        System.out.println("-------------------------------------------------------------------------------------");
        for(FinanceStatisticsDataWeeklyDO financeStatisticsDataWeeklyDO: diffFinanceAllStatisticsDataWeekly) {
            System.out.println(financeStatisticsDataWeeklyDO.toString());
        }
        //return financeAllStatisticsDataWeekly;
        System.out.println("-------------------------------------------------------------------------------------");*/
        return diffFinanceAllStatisticsDataWeekly;
    }

    /**
     * 被统计的累计数据
     * @param paramVo
     * @return
     */
    public List<FinanceStatisticsDataWeeklyDO> statisticsTotalFinanceDataWeekly(FinanceStatisticsParam paramVo){
        int year = paramVo.getYear();
        int month = paramVo.getMonth();
        int weekOfMonth = paramVo.getWeekOfMonth();
        boolean isHistoryData = !isCurrentWeek(year, month, weekOfMonth);  //如果不是当前周,则都认为是历史统计数据,后面则从数据库里面取数据
        List<FinanceStatisticsDataWeeklyDO> financeAllStatisticsDataWeekly = new ArrayList<>();
        //统计KA
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_KA);
        List<FinanceStatisticsDataWeeklyDO> financeKAStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData);
        if (CollectionUtil.isNotEmpty(financeKAStatisticsDataWeekly)) {
            financeAllStatisticsDataWeekly.addAll(financeKAStatisticsDataWeekly);
        }
        //统计电销
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_TMK);
        List<FinanceStatisticsDataWeeklyDO> financeTMKStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData);
        if (CollectionUtil.isNotEmpty(financeTMKStatisticsDataWeekly)) {
            financeAllStatisticsDataWeekly.addAll(financeTMKStatisticsDataWeekly);
        }
        // 统计大客户渠道
        paramVo.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_BCC);
        List<FinanceStatisticsDataWeeklyDO> financeBCCStatisticsDataWeekly = statisticsFinanceDataWeekly(paramVo, isHistoryData);
        if (CollectionUtil.isNotEmpty(financeBCCStatisticsDataWeekly)) {
            financeAllStatisticsDataWeekly.addAll(financeBCCStatisticsDataWeekly);
        }

        //如果是当前周,则将此次统计数据保存到数据库
        if (!isHistoryData && CollectionUtil.isNotEmpty(financeAllStatisticsDataWeekly)) {
            freshStatisticsDataToDB(year, month, weekOfMonth, financeAllStatisticsDataWeekly);
        }
        return financeAllStatisticsDataWeekly;
    }

    /**
     * 转换成导出excel时需要的数据
     * @param list
     * @return
     */
    public List<FinanceStatisticsDataWeeklyExcel> convertToExcelData(List<FinanceStatisticsDataWeeklyDO> list) {
        List<FinanceStatisticsDataWeeklyExcel> excelDataList = new ArrayList<>();
        Map<String, FinanceStatisticsDataWeeklyExcel> excelMapCache = new LinkedHashMap<>();
        Map<String, FinanceStatisticsDataWeeklyExcel> excelSumMapCache = new LinkedHashMap<>();
        if (CollectionUtil.isNotEmpty(list)) {
            for (FinanceStatisticsDataWeeklyDO financeStatisticsDataWeeklyDO: list) {
                // 实现行列转置
                convertBaseData(excelMapCache, financeStatisticsDataWeeklyDO);
                // 进行合计计算
                sumBaseData(excelSumMapCache, financeStatisticsDataWeeklyDO);
            }
            if (excelMapCache.size()>0) {
                List<FinanceStatisticsDataWeeklyExcel> excelBaseDataList = ListUtil.mapToList(excelMapCache);
                excelDataList.addAll(excelBaseDataList);
            }
            if (excelSumMapCache.size()>0) {
                List<FinanceStatisticsDataWeeklyExcel> excelSumDataList = ListUtil.mapToList(excelSumMapCache);
                excelDataList.addAll(excelSumDataList);
            }
        }
        return excelDataList;
    }

    private void convertBaseData(Map<String, FinanceStatisticsDataWeeklyExcel> excelMapCache, FinanceStatisticsDataWeeklyDO financeStatisticsDataWeeklyDO) {
        // 客户成交数量
        String customerKey = financeStatisticsDataWeeklyDO.getOrderOrigin() + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_CUSTOMER;
        FinanceStatisticsDataWeeklyExcel customerStatisticsDataWeeklyExcel = excelMapCache.get(customerKey);
        if (customerStatisticsDataWeeklyExcel == null) {
            customerStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, customerStatisticsDataWeeklyExcel);
            excelMapCache.put(customerKey, customerStatisticsDataWeeklyExcel);
        }
        customerStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_CUSTOMER);
        customerStatisticsDataWeeklyExcel.fillCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getCustomerDealsCount());
        // 新客户成交数量
        String newCustomerKey = financeStatisticsDataWeeklyDO.getOrderOrigin() + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_NEW_CUSTOMER;
        FinanceStatisticsDataWeeklyExcel newCustomerStatisticsDataWeeklyExcel = excelMapCache.get(newCustomerKey);
        if (newCustomerStatisticsDataWeeklyExcel == null) {
            newCustomerStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, newCustomerStatisticsDataWeeklyExcel);
            excelMapCache.put(newCustomerKey, newCustomerStatisticsDataWeeklyExcel);
        }
        newCustomerStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_NEW_CUSTOMER);
        newCustomerStatisticsDataWeeklyExcel.fillCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getNewCustomerDealsCount());
        // 租赁商品成交数量
        String rentProductKey = financeStatisticsDataWeeklyDO.getOrderOrigin() + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_RENT_PRODUCT;
        FinanceStatisticsDataWeeklyExcel rentProductStatisticsDataWeeklyExcel = excelMapCache.get(rentProductKey);
        if (rentProductStatisticsDataWeeklyExcel == null) {
            rentProductStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, rentProductStatisticsDataWeeklyExcel);
            excelMapCache.put(rentProductKey, rentProductStatisticsDataWeeklyExcel);
        }
        rentProductStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_RENT_PRODUCT);
        rentProductStatisticsDataWeeklyExcel.fillCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getRentProductDealsCount());
        // 退货成交数量
        String returnProductKey = financeStatisticsDataWeeklyDO.getOrderOrigin() + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_RETURN_PRODUCT;
        FinanceStatisticsDataWeeklyExcel returnProductStatisticsDataWeeklyExcel = excelMapCache.get(returnProductKey);
        if (returnProductStatisticsDataWeeklyExcel == null) {
            returnProductStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, returnProductStatisticsDataWeeklyExcel);
            excelMapCache.put(returnProductKey, returnProductStatisticsDataWeeklyExcel);
        }
        returnProductStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_RETURN_PRODUCT);
        returnProductStatisticsDataWeeklyExcel.fillCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getReturnProductDealsCount());
        // 净增商品成交数量
        String increaseProductKey = financeStatisticsDataWeeklyDO.getOrderOrigin() + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_INCREASE_PRODUCT;
        FinanceStatisticsDataWeeklyExcel increaseProductStatisticsDataWeeklyExcel = excelMapCache.get(increaseProductKey);
        if (increaseProductStatisticsDataWeeklyExcel == null) {
            increaseProductStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, increaseProductStatisticsDataWeeklyExcel);
            excelMapCache.put(increaseProductKey, increaseProductStatisticsDataWeeklyExcel);
        }
        increaseProductStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_INCREASE_PRODUCT);
        increaseProductStatisticsDataWeeklyExcel.fillCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getIncreaseProductDealsCount());
    }

    /**
     * 合计各统计数量
     * @param excelMapCache
     * @param financeStatisticsDataWeeklyDO
     */
    private void sumBaseData(Map<String, FinanceStatisticsDataWeeklyExcel> excelMapCache, FinanceStatisticsDataWeeklyDO financeStatisticsDataWeeklyDO) {
        // 合计客户成交数量
        String customerKey =  StatisticsDealsCountType.DEALS_COUNT_TYPE_ALL + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_CUSTOMER;
        FinanceStatisticsDataWeeklyExcel customerStatisticsDataWeeklyExcel = excelMapCache.get(customerKey);
        if (customerStatisticsDataWeeklyExcel == null) {
            customerStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, customerStatisticsDataWeeklyExcel);
            excelMapCache.put(customerKey, customerStatisticsDataWeeklyExcel);
        }
        customerStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_CUSTOMER);
        customerStatisticsDataWeeklyExcel.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_ALL);
        customerStatisticsDataWeeklyExcel.sumCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getCustomerDealsCount());
        // 合计新客户成交数量
        String newCustomerKey = StatisticsDealsCountType.DEALS_COUNT_TYPE_ALL + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_NEW_CUSTOMER;
        FinanceStatisticsDataWeeklyExcel newCustomerStatisticsDataWeeklyExcel = excelMapCache.get(newCustomerKey);
        if (newCustomerStatisticsDataWeeklyExcel == null) {
            newCustomerStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, newCustomerStatisticsDataWeeklyExcel);
            excelMapCache.put(newCustomerKey, newCustomerStatisticsDataWeeklyExcel);
        }
        newCustomerStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_NEW_CUSTOMER);
        newCustomerStatisticsDataWeeklyExcel.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_ALL);
        newCustomerStatisticsDataWeeklyExcel.sumCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getNewCustomerDealsCount());
        // 合计租赁商品成交数量
        String rentProductKey = StatisticsDealsCountType.DEALS_COUNT_TYPE_ALL + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_RENT_PRODUCT;
        FinanceStatisticsDataWeeklyExcel rentProductStatisticsDataWeeklyExcel = excelMapCache.get(rentProductKey);
        if (rentProductStatisticsDataWeeklyExcel == null) {
            rentProductStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, rentProductStatisticsDataWeeklyExcel);
            excelMapCache.put(rentProductKey, rentProductStatisticsDataWeeklyExcel);
        }
        rentProductStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_RENT_PRODUCT);
        rentProductStatisticsDataWeeklyExcel.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_ALL);
        rentProductStatisticsDataWeeklyExcel.sumCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getRentProductDealsCount());
        // 合计退货成交数量
        String returnProductKey = StatisticsDealsCountType.DEALS_COUNT_TYPE_ALL + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" + StatisticsDealsCountType.DEALS_COUNT_TYPE_RETURN_PRODUCT;
        FinanceStatisticsDataWeeklyExcel returnProductStatisticsDataWeeklyExcel = excelMapCache.get(returnProductKey);
        if (returnProductStatisticsDataWeeklyExcel == null) {
            returnProductStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, returnProductStatisticsDataWeeklyExcel);
            excelMapCache.put(returnProductKey, returnProductStatisticsDataWeeklyExcel);
        }
        returnProductStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_RETURN_PRODUCT);
        returnProductStatisticsDataWeeklyExcel.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_ALL);
        returnProductStatisticsDataWeeklyExcel.sumCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getReturnProductDealsCount());
        // 合计净增商品成交数量
        String increaseProductKey = StatisticsDealsCountType.DEALS_COUNT_TYPE_ALL + "-" + financeStatisticsDataWeeklyDO.getRentLengthType() + "-" +  StatisticsDealsCountType.DEALS_COUNT_TYPE_INCREASE_PRODUCT;
        FinanceStatisticsDataWeeklyExcel increaseProductStatisticsDataWeeklyExcel = excelMapCache.get(increaseProductKey);
        if (increaseProductStatisticsDataWeeklyExcel == null) {
            increaseProductStatisticsDataWeeklyExcel = new FinanceStatisticsDataWeeklyExcel();
            BeanUtils.copyProperties(financeStatisticsDataWeeklyDO, increaseProductStatisticsDataWeeklyExcel);
            excelMapCache.put(increaseProductKey, increaseProductStatisticsDataWeeklyExcel);
        }
        increaseProductStatisticsDataWeeklyExcel.setDealsCountType(StatisticsDealsCountType.DEALS_COUNT_TYPE_INCREASE_PRODUCT);
        increaseProductStatisticsDataWeeklyExcel.setOrderOrigin(StatisticsOrderOriginType.ORDER_ORIGIN_TYPE_ALL);
        increaseProductStatisticsDataWeeklyExcel.sumCountBySubCompanyId(financeStatisticsDataWeeklyDO.getSubCompanyId(),financeStatisticsDataWeeklyDO.getIncreaseProductDealsCount());
    }

    // 是否是现在这一周(考虑跨月的情况)
    private boolean isCurrentWeek(int year, int month, int weekOfMonth) {
        //获取现在的时间
        Date now = new Date();
        Calendar currentCalendar = getCalendarInstance(SETTING_FIRST_DAY_OF_WEEK)/*Calendar.getInstance()*/;
        // 测试使用(修改当前时间)
        /**
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            now = sdf.parse("2018-07-01");//"2018-03-06" "2018-03-03"
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */
        currentCalendar.setTime(now);
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH) + 1; // 因为日历获取的月份比实际月份小1
        int currentWeekOfMonth = currentCalendar.get(Calendar.WEEK_OF_MONTH);
        Date firstDayOfThisWeek = getFirstDayOfCurrentWeek(now);
        if (firstDayOfThisWeek.compareTo(DateUtil.getStartMonthDate(now)) < 0) { //如果这周第一天的日期比当月第一天的时间还小，说明这周已经跨月份
            Calendar firstDayOfThisWeekCalendar = getCalendarInstance(SETTING_FIRST_DAY_OF_WEEK)/*Calendar.getInstance()*/;
            firstDayOfThisWeekCalendar.setTime(firstDayOfThisWeek);
            int firstDayOfThisWeekInYear = firstDayOfThisWeekCalendar.get(Calendar.YEAR);
            int firstDayOfThisWeekInMonth = firstDayOfThisWeekCalendar.get(Calendar.MONTH) + 1;  // 因为日历获取的月份比实际月份小1
            int firstDayOfThisWeekInWeekOfMonth = firstDayOfThisWeekCalendar.get(Calendar.WEEK_OF_MONTH);
            if (firstDayOfThisWeekInYear == year && firstDayOfThisWeekInMonth == month && firstDayOfThisWeekInWeekOfMonth == weekOfMonth) {
                return true;
            }
        }
        if (currentYear == year && currentMonth == month && currentWeekOfMonth == weekOfMonth) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前周和上一周的差异变化数据((即: 当前周统计时的当月累计数据减去上一周统计时的当月累计数据)
     * @param currentWeekStatisticsDataWeeklyDOList
     * @param paramVo
     * @param isHistoryData
     * @return
     */
    private List<FinanceStatisticsDataWeeklyDO> getDiffStatisticsFinanceDataWeekly(List<FinanceStatisticsDataWeeklyDO> currentWeekStatisticsDataWeeklyDOList, FinanceStatisticsParam paramVo, boolean isHistoryData) {
        if (CollectionUtil.isEmpty(currentWeekStatisticsDataWeeklyDOList)) {
            currentWeekStatisticsDataWeeklyDOList = statisticsFinanceDataWeekly(paramVo, isHistoryData);
        }
        List<FinanceStatisticsDataWeeklyDO> diffStatisticsDataWeeklyDOList = new ArrayList<>();
        // 从数据库中查找最近统计的本月累计数据(每周统计一次)
        List<FinanceStatisticsDataWeeklyDO> lastWeekStatisticsDataWeeklyDOList = getLastWeekStatisticsFinanceDataWeekly(paramVo); //按租赁方式和分公司排序
        if (CollectionUtil.isNotEmpty(currentWeekStatisticsDataWeeklyDOList) && CollectionUtil.isNotEmpty(lastWeekStatisticsDataWeeklyDOList) && lastWeekStatisticsDataWeeklyDOList.size()==currentWeekStatisticsDataWeeklyDOList.size()) {
            for (int i = 0, length = currentWeekStatisticsDataWeeklyDOList.size(); i < length; i++) {
                FinanceStatisticsDataWeeklyDO currentFinanceWeekStatisticsDataWeekly = currentWeekStatisticsDataWeeklyDOList.get(i);
                FinanceStatisticsDataWeeklyDO lastFinanceWeekStatisticsDataWeekly = lastWeekStatisticsDataWeeklyDOList.get(i);
                FinanceStatisticsDataWeeklyDO diffFinanceStatisticsDataWeekly = currentFinanceWeekStatisticsDataWeekly.diff(lastFinanceWeekStatisticsDataWeekly);
                diffStatisticsDataWeeklyDOList.add(diffFinanceStatisticsDataWeekly);
            }
        } else if (CollectionUtil.isEmpty(lastWeekStatisticsDataWeeklyDOList)){ // 如果本月的上周统计数据不存在,则本周差值变化数据则为本周累计数据
            diffStatisticsDataWeeklyDOList.addAll(currentWeekStatisticsDataWeeklyDOList);
        }
        return diffStatisticsDataWeeklyDOList;
    }

    /**
     * 上一周统计时的当月累计数据
     * @param paramVo
     * @return
     */
    private List<FinanceStatisticsDataWeeklyDO> getLastWeekStatisticsFinanceDataWeekly(FinanceStatisticsParam paramVo) {
        FinanceStatisticsParam lastWeekParamVo = new FinanceStatisticsParam();
        int lastWeek = paramVo.getWeekOfMonth() - 1;
        if (lastWeek <= 0) {
            return null;
        }
        BeanUtils.copyProperties(paramVo, lastWeekParamVo);
        lastWeekParamVo.setWeekOfMonth(lastWeek);
        return statisticsFinanceDataWeekly(lastWeekParamVo, true);
    }

    private List<FinanceStatisticsDataWeeklyDO> statisticsFinanceDataWeekly(FinanceStatisticsParam paramVo, boolean isHistoryData){
        return statisticsFinanceDataWeekly(paramVo, isHistoryData, false);
    }

    /**
     * 指定某一周统计时的当月累计数据(每周统计一次)
     * @param paramVo
     * @param isHistoryData  是否是历史周数据
     * @param needReStatistics  是否需要重新统计（供重新统计历史周数据时使用）
     * @return
     */
    private List<FinanceStatisticsDataWeeklyDO> statisticsFinanceDataWeekly(FinanceStatisticsParam paramVo, boolean isHistoryData, boolean needReStatistics) {
       int year = paramVo.getYear();
       int month = paramVo.getMonth();
       int weekOfMonth = paramVo.getWeekOfMonth();
       int statisticsOrderOriginType = paramVo.getOrderOrigin();

        List<FinanceStatisticsDataWeeklyDO> financeStatisticsDataWeeklyDOList = new ArrayList<>();
        if (statisticsOrderOriginType <= 0) {
            return financeStatisticsDataWeeklyDOList;
        }

        Calendar currentCalendar = getCalendarInstance(SETTING_FIRST_DAY_OF_WEEK)/*Calendar.getInstance()*/;
        currentCalendar.set(Calendar.YEAR, year);
        currentCalendar.set(Calendar.MONTH, (month -1));  //因为日历的月份是从0开始到11
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Date currentMonthDate = currentCalendar.getTime();
        int maxWeek = currentCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        if ( !isHistoryData || needReStatistics) {
            // 统计当月累计数据
            FinanceStatisticsParam param = new FinanceStatisticsParam();
            param.setOrderOrigin(statisticsOrderOriginType);
            StatisticsInterval statisticsInverval = createStatisticsInterval(year, month, weekOfMonth);
            if (paramVo.getStatisticsStartTime() == null) {
                //param.setStatisticsStartTime(DateUtil.getStartMonthDate(currentMonthDate));
                param.setStatisticsStartTime(statisticsInverval.getStatisticsStartTime());
            } else {
                param.setStatisticsStartTime(paramVo.getStatisticsStartTime());
            }
            if (paramVo.getStatisticsEndTime() == null) {
                //param.setStatisticsEndTime(DateUtil.getEndMonthDate(currentMonthDate));
                param.setStatisticsEndTime(statisticsInverval.getStatisticsEndTime());
            } else {
                param.setStatisticsEndTime(paramVo.getStatisticsEndTime());
            }

            Map<Integer, FinanceStatisticsDealsCountBySubCompany> customerDealsCountMap = null;
            Map<Integer, FinanceStatisticsDealsCountBySubCompany> rentProductDealsCountMap = null;
            Map<Integer, FinanceStatisticsDealsCountBySubCompany> returnProductDealsCountMap = null;
            Map<Integer, FinanceStatisticsDealsCountBySubCompany> newCustomerDealsCountMap = null;
            List<SubCompanyDO> subCompanyDOList = queryAllSubCompany();
            for (int i = 0, length = StatisticsRentLengthType.ALL_TYPES_ARRAY.length; i< length; i++) {
                param.setNewCustomerKey(null);
                int rentLengthType = StatisticsRentLengthType.ALL_TYPES_ARRAY[i];
                param.setRentLengthType(rentLengthType);
                List<FinanceStatisticsDealsCountBySubCompany> customerDealsCountBySubCompany =  statisticsCustomerCountWeekly(param);
                customerDealsCountMap = ListUtil.listToMap(customerDealsCountBySubCompany, "subCompanyId");
                List<FinanceStatisticsDealsCountBySubCompany> rentProductDealsCountBySubCompany =  statisticsRentProductCountWeekly(param);
                rentProductDealsCountMap = ListUtil.listToMap(rentProductDealsCountBySubCompany, "subCompanyId");
                List<FinanceStatisticsDealsCountBySubCompany> returnProductDealsCountBySubCompany =  statisticsReturnProductCountWeekly(param);
                returnProductDealsCountMap = ListUtil.listToMap(returnProductDealsCountBySubCompany, "subCompanyId");
                param.setNewCustomerKey("" + year);
                List<FinanceStatisticsDealsCountBySubCompany> newCustomerDealsCountBySubCompany =  statisticsCustomerCountWeekly(param);
                newCustomerDealsCountMap = ListUtil.listToMap(newCustomerDealsCountBySubCompany, "subCompanyId");
                // 遍历各个执行分公司
                if (CollectionUtil.isNotEmpty(subCompanyDOList)) {
                    for (SubCompanyDO subCompanyDO: subCompanyDOList) {
                        if (subCompanyDO.getId() == 1 || subCompanyDO.getId() >= 10) {   //执行分公司ID[2:9] 八个分公司
                            continue;
                        }
                        int customerDealsCount = getDealsCountFromMap(customerDealsCountMap, subCompanyDO.getId(), 0);
                        int newCustomerDealsCount = getDealsCountFromMap(newCustomerDealsCountMap, subCompanyDO.getId(), 0);
                        int rentProductDealsCount = getDealsCountFromMap(rentProductDealsCountMap, subCompanyDO.getId(), 0);
                        int returnProductDealsCount = getDealsCountFromMap(returnProductDealsCountMap, subCompanyDO.getId(), 0);
                        int increaseProductDealsCount = rentProductDealsCount - returnProductDealsCount;
                        FinanceStatisticsDataWeeklyDO financeStatisticsDataWeeklyDO = new FinanceStatisticsDataWeeklyDO();
                        financeStatisticsDataWeeklyDO.setOrderOrigin(statisticsOrderOriginType);
                        financeStatisticsDataWeeklyDO.setRentLengthType(rentLengthType);
                        financeStatisticsDataWeeklyDO.setSubCompanyId(subCompanyDO.getId());
                        financeStatisticsDataWeeklyDO.setCustomerDealsCount(customerDealsCount);
                        financeStatisticsDataWeeklyDO.setNewCustomerDealsCount(newCustomerDealsCount);
                        financeStatisticsDataWeeklyDO.setRentProductDealsCount(rentProductDealsCount);
                        financeStatisticsDataWeeklyDO.setReturnProductDealsCount(returnProductDealsCount);
                        financeStatisticsDataWeeklyDO.setIncreaseProductDealsCount(increaseProductDealsCount);
                        financeStatisticsDataWeeklyDO.setYear(year);
                        financeStatisticsDataWeeklyDO.setMonth(month);
                        financeStatisticsDataWeeklyDO.setWeekOfMonth(weekOfMonth);
                        financeStatisticsDataWeeklyDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                        Date now = new Date();
                        financeStatisticsDataWeeklyDO.setCreateTime(now);
                        financeStatisticsDataWeeklyDO.setUpdateTime(now);
                        financeStatisticsDataWeeklyDOList.add(financeStatisticsDataWeeklyDO);
                    }
                }
            }
        } else {
            // TODO 从数据库里查询历史数据
            Map<String,Object> queryParamMap = new HashMap<>();
            queryParamMap.put("orderOrigin", statisticsOrderOriginType);
            queryParamMap.put("year", year);
            queryParamMap.put("month", month);
            queryParamMap.put("weekOfMonth", weekOfMonth);
            financeStatisticsDataWeeklyDOList = financeStatisticsDataWeeklyMapper.findByWhenCause(queryParamMap);
            if (CollectionUtil.isEmpty(financeStatisticsDataWeeklyDOList) && maxWeek == weekOfMonth) { //如果是当前月份的最大周,则可以重新统计当月数据
                // TODO 重新统计当月数据
            }
        }
        return financeStatisticsDataWeeklyDOList;
    }

    private List<SubCompanyDO> queryAllSubCompany() {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        return subCompanyMapper.listPage(paramMap);
    }

    private int getDealsCountFromMap(Map<Integer, FinanceStatisticsDealsCountBySubCompany> map, Integer subCompanyId, int defaultValue) {
        if (map == null || map.size() == 0 || subCompanyId == null) {
            return defaultValue;
        }
        FinanceStatisticsDealsCountBySubCompany financeStatisticsDealsCountBySubCompany = map.get(subCompanyId);
        if (financeStatisticsDealsCountBySubCompany == null) {
            return defaultValue;
        }
        return financeStatisticsDealsCountBySubCompany.getDealsCount();
    }

    public class StatisticsInterval {
        Date statisticsStartTime = null;
        Date statisticsEndTime = null;

        public Date getStatisticsStartTime() {
            return statisticsStartTime;
        }

        public void setStatisticsStartTime(Date statisticsStartTime) {
            this.statisticsStartTime = statisticsStartTime;
        }

        public Date getStatisticsEndTime() {
            return statisticsEndTime;
        }

        public void setStatisticsEndTime(Date statisticsEndTime) {
            this.statisticsEndTime = statisticsEndTime;
        }
    }

    public StatisticsInterval createStatisticsInterval(int year, int month, int weekOfMonth) {
        Calendar calendar = getCalendarInstance(SETTING_FIRST_DAY_OF_WEEK);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);  // 因为日历获取的月份比实际月份小1
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayInCurrentMonth = calendar.getTime();
        Date statisticsStartTime = DateUtil.getStartMonthDate(firstDayInCurrentMonth); // 统计开始时间
        Date statisticsEndTime = null;  //统计结束时间
        Date endTimeInCurrentMonth = DateUtil.getEndMonthDate(firstDayInCurrentMonth);
        calendar.set(Calendar.WEEK_OF_MONTH, weekOfMonth);
        //calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.DAY_OF_WEEK, SETTING_FIRST_DAY_OF_WEEK - 1);
        calendar.set(Calendar.HOUR_OF_DAY ,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date lastDayInWeek = calendar.getTime();
        int monthOfLastDayInWeek = DateUtil.getMounth(lastDayInWeek);//周末所在月份
        if (monthOfLastDayInWeek > month) {  // 如果周末所在月份大于当前统计的月份
            statisticsEndTime = endTimeInCurrentMonth;  //月末为统计结束时间
        } else {
            statisticsEndTime = calendar.getTime();    //周末为统计结束时间
        }
        StatisticsInterval statisticsInterval = new StatisticsInterval();
        statisticsInterval.setStatisticsStartTime(statisticsStartTime);
        statisticsInterval.setStatisticsEndTime(statisticsEndTime);
        return statisticsInterval;
    }

    public Calendar getCalendarInstance(int settingFirstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(settingFirstDayOfWeek);
        calendar.setMinimalDaysInFirstWeek(1);
        return calendar;
    }

    public StatisticsInterval createStatisticsInterval(int statisticsInterval, int year, int month, int weekOfMonth) {
        if (StatisticsIntervalType.STATISTICS_INTERVAL_WEEKLY == statisticsInterval) {
            return createStatisticsInterval(year, month, weekOfMonth);
        } else if (StatisticsIntervalType.STATISTICS_INTERVAL_MONTHLY == statisticsInterval) {
            int maxWeekOfMonth = getMaxWeekCountOfYearAndMonth(year, month);
            return createStatisticsInterval(year, month, maxWeekOfMonth);
        }
        return createStatisticsInterval(year, month, weekOfMonth);
    }

    @Autowired
    private FinanceStatisticsDataWeeklyMapper financeStatisticsDataWeeklyMapper;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    public static int SETTING_FIRST_DAY_OF_WEEK = Calendar.FRIDAY;

}
