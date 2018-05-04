package com.lxzl.erp.web.dao;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;
import com.lxzl.erp.dataaccess.dao.mysql.statistics.StatisticsMapper;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/2 19:20
 * @Description:
 */
public class StatisticsTest extends ERPUnTransactionalTest {

	@Autowired
	private StatisticsMapper statisticsMapper;

	@Test
	public void testQuerySalesmanDetailTwo() throws Exception {
		StatisticsSalesmanPageParam statisticsSalesmanPageParam = new StatisticsSalesmanPageParam();
		statisticsSalesmanPageParam.setPageNo(1);
		statisticsSalesmanPageParam.setPageSize(10);
		SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
		Date start = sdf.parse(" 2018-05-01 19:20:00 ");
		Date end = sdf.parse(" 2018-05-31 19:20:00 ");
		statisticsSalesmanPageParam.setStartTime(start);
		statisticsSalesmanPageParam.setEndTime(end);
		statisticsSalesmanPageParam.setSalesmanName("刘君诚");
		PageQuery pageQuery = new PageQuery(statisticsSalesmanPageParam.getPageNo(), statisticsSalesmanPageParam.getPageSize());
		Map<String, Object> maps = new HashMap<>();
		maps.put("start", pageQuery.getStart());
		maps.put("pageSize", pageQuery.getPageSize());
		maps.put("salesmanQueryParam", statisticsSalesmanPageParam);
		statisticsMapper.querySalesmanDetailTwo(maps);
	}

	@Test
	public void testQuerySalesmanDetailTwoExtend() throws Exception {
		StatisticsSalesmanPageParam statisticsSalesmanPageParam = new StatisticsSalesmanPageParam();
		statisticsSalesmanPageParam.setPageNo(1);
		statisticsSalesmanPageParam.setPageSize(10);
		SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
		Date start = sdf.parse(" 2008-07-10 19:20:00 ");
		Date end = sdf.parse(" 2028-07-10 19:20:00 ");
		statisticsSalesmanPageParam.setStartTime(start);
		PageQuery pageQuery = new PageQuery(statisticsSalesmanPageParam.getPageNo(), statisticsSalesmanPageParam.getPageSize());
		Map<String, Object> maps = new HashMap<>();
		maps.put("start", pageQuery.getStart());
		maps.put("pageSize", pageQuery.getPageSize());
		maps.put("salesmanQueryParam", statisticsSalesmanPageParam);
		statisticsMapper.querySalesmanDetailTwoExtend(maps);
	}
}
