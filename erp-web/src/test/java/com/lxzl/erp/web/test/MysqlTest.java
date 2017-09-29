package com.lxzl.erp.web.test;

import java.sql.SQLException;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lxzl.erp.core.service.UserService;
import com.lxzl.erp.dataaccess.domain.UserDO;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;

public class MysqlTest extends BaseUnTransactionalTest {
	
	@Autowired
	private UserService userService;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testMysql() throws SQLException {
		String username = "lisi";
		String password = "123456";
		String nick = "李四";
		Integer sex = null;
		Integer age = null;
		Date birthday = null;
		String address = null;
		String tel = null;
		String email = null;
		String md5Key = "2d1287777f4f45a881040167db74f276";

		try {
			userService.addUser(username, password, nick, sex, age, birthday, address, tel, email, md5Key);
		} catch (Exception e) {
		}
		
		UserDO user = userService.findByUsername(username);
		Assert.assertNotNull(user);
	}
}
