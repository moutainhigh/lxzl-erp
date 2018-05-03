package com.lxzl.erp.web.service;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsSalesmanDetailTwo;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/2 20:25
 * @Description:
 */
public class StatisticsServiceTest extends BaseUnTransactionalTest {

	@Autowired
	private StatisticsService statisticsService;

	@Test
	public void querySalesmanTwo() throws Exception {
		StatisticsSalesmanPageParam statisticsSalesmanPageParam = new StatisticsSalesmanPageParam();
		statisticsSalesmanPageParam.setPageNo(1);
		statisticsSalesmanPageParam.setPageSize(10);
		SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
		Date start = sdf.parse(" 2018-04-10 19:20:00 ");
		Date end = sdf.parse(" 2028-07-10 19:20:00 ");
		statisticsSalesmanPageParam.setStartTime(start);
//		statisticsSalesmanPageParam.setSalesmanName("刘君诚");
		ServiceResult<String, List<StatisticsSalesmanDetailTwo>>  result = statisticsService.querySalesmanTwo(statisticsSalesmanPageParam);
		System.out.println(result.getResult());
	}
}
