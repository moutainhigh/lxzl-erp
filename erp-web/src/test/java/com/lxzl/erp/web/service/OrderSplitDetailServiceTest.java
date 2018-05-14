package com.lxzl.erp.web.service;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.pojo.OrderSplit;
import com.lxzl.erp.common.domain.order.pojo.OrderSplitDetail;
import com.lxzl.erp.core.service.order.OrderSplitDetailService;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
		OrderSplit orderSplit = new OrderSplit();
		orderSplit.setOrderItemReferId(26);
		orderSplit.setOrderItemType(1);

		List<OrderSplitDetail> orderSplitDetailList = new ArrayList<>();
		OrderSplitDetail orderSplitDetail = new OrderSplitDetail();
		orderSplitDetail.setIsPeer(0);
		orderSplitDetail.setDeliverySubCompanyId(3);
		orderSplitDetail.setSplitCount(3);
		orderSplitDetailList.add(orderSplitDetail);

		orderSplitDetail = new OrderSplitDetail();
		orderSplitDetail.setIsPeer(1);
		orderSplitDetail.setSplitCount(4);
		orderSplitDetailList.add(orderSplitDetail);

		orderSplit.setSplitDetailList(orderSplitDetailList);
		orderSplitDetailService.addOrderSplitDetail(orderSplit);
	}

	@Test
	public void testQueryOrderSplitDetailByOrderItemTypeAndOrderItemReferId() {
		ServiceResult<String, List<OrderSplitDetail>> serviceResult = orderSplitDetailService.queryOrderSplitDetailByOrderItemTypeAndOrderItemReferId(Integer.valueOf(1), Integer.valueOf(26));
		System.out.println(serviceResult.getResult().size());
	}

	@Test
	public void testDelete() {
		orderSplitDetailService.deleteOrderSplit(1, 26);
	}

	@Test
	public void testUpdate() {
		OrderSplit orderSplit = new OrderSplit();
		orderSplit.setOrderItemReferId(26);
		orderSplit.setOrderItemType(1);

		List<OrderSplitDetail> orderSplitDetailList = new ArrayList<>();
		OrderSplitDetail orderSplitDetail = new OrderSplitDetail();
		orderSplitDetail.setOrderSplitDetailId(21);
		orderSplitDetail.setIsPeer(1);
		orderSplitDetail.setSplitCount(4);
		orderSplitDetailList.add(orderSplitDetail);

		orderSplitDetail = new OrderSplitDetail();
		orderSplitDetail.setIsPeer(0);
		orderSplitDetail.setDeliverySubCompanyId(2);
		orderSplitDetail.setSplitCount(10);
		orderSplitDetailList.add(orderSplitDetail);

		orderSplit.setSplitDetailList(orderSplitDetailList);
		orderSplitDetailService.updateOrderSplit(orderSplit);
	}
}
