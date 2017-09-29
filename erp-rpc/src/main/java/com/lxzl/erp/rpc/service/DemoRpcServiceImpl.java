package com.lxzl.erp.rpc.service;

import org.springframework.stereotype.Service;

import com.lxzl.erp.rpc.api.service.DemoRpcService;

/**
 * 
 * @author lxzl
 *
 */
@Service("demoRpcServiceImpl")
public class DemoRpcServiceImpl implements DemoRpcService {

	@Override
	public String sayHello() {
		System.out.println("erp lxzl rpc hello");
		return "erp lxzl rpc hello";
	}

	@Override
	public String say(String s) {
		System.out.println(s);
		return s;
	}

	@Override
	public String doIt() {
		System.out.println("do it");
		return "do it";
	}
}
