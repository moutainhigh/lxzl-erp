package com.lxzl.erp.web.dao;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/2 19:20
 * @Description:
 */
public class OrderTest extends ERPUnTransactionalTest {

	@Autowired
	private OrderMapper orderMapper;

	@Test
	public void testFindByOrderItemTypeAndOrderItemReferId() {
		OrderDO orderDO = orderMapper.findByOrderItemTypeAndOrderItemReferId(1, 270000000);
		System.out.println(orderDO);
	}
}
