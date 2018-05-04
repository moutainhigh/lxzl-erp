package com.lxzl.erp.web.service;

import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/2 20:25
 * @Description:
 */
public class StatisticsServiceTest extends BaseUnTransactionalTest {

	@Autowired
	private StatisticsService statisticsService;

	@Test
	public void querySalesman() throws Exception {
		StatisticsSalesmanPageParam statisticsSalesmanPageParam = new StatisticsSalesmanPageParam();
		statisticsSalesmanPageParam.setPageNo(1);
		statisticsSalesmanPageParam.setPageSize(10);
		SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
		Date start = sdf.parse(" 2018-05-10 19:20:00 ");
		statisticsSalesmanPageParam.setStartTime(start);
		statisticsSalesmanPageParam.setSalesmanName("刘君诚");
		statisticsService.querySalesman(statisticsSalesmanPageParam);
	}
}
