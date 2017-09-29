package com.lxzl.erp.rpc.service;

import org.springframework.stereotype.Service;

import com.lxzl.erp.rpc.api.service.TestRpcService;

@Service("testRpcServiceImpl")
public class TestRpcServiceImpl implements TestRpcService {

	@Override
	public String test() {
		System.out.println("lxzl test");
		try {
			Thread.sleep(20000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lxzl test";
	}

}
