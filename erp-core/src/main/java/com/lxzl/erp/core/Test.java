package com.lxzl.erp.core;

import com.lxzl.se.common.util.http.HttpClientUtil;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class Test {

	public static void main(String[] args) {
		String url = "http://127.0.0.1:8085/example/testRedis?format=json";
		String r = HttpClientUtil.get(url);
		System.out.println(r);
	}
}
