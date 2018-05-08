package com.lxzl.erp.web.service;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.pojo.OrderSplitDetail;
import com.lxzl.erp.core.service.order.OrderSplitDetailService;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/2 20:25
 * @Description:
 */
public class OrderSplitDetailServiceTest extends BaseUnTransactionalTest {

	@Autowired
	private OrderSplitDetailService orderSplitDetailService;

	@Test
	public void testAddOrderSplitDetail() {
		OrderSplitDetail orderSplitDetail = new OrderSplitDetail();
		orderSplitDetail.setOrderItemType(1);
		orderSplitDetail.setOrderItemReferId(26);
		orderSplitDetail.setIsPeer(0);
		orderSplitDetail.setDeliverySubCompanyId(3);
		orderSplitDetail.setSplitCount(3);
		orderSplitDetailService.addOrderSplitDetail(orderSplitDetail);
	}

	@Test
	public void testQueryOrderSplitDetailByOrderItemTypeAndOrderItemReferId() {
		ServiceResult<String, List<OrderSplitDetail>> serviceResult = orderSplitDetailService.queryOrderSplitDetailByOrderItemTypeAndOrderItemReferId(Integer.valueOf(1), Integer.valueOf(26));
		System.out.println(serviceResult.getResult().size());
	}
}
