package com.lxzl.erp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.erp.core.service.RpcTestService;
import com.lxzl.erp.rpc.api.domain.User;
import com.lxzl.erp.rpc.api.service.TestRpcService;
import com.lxzl.erp.rpc.api.service.UserRpcService;
import com.lxzl.se.core.service.impl.BaseServiceImpl;
import com.lxzl.se.rpc.dubbo.client.DubboClientFactory;

@Service("rpcTestService")
public class RpcTestServiceImpl extends BaseServiceImpl implements RpcTestService {
	
	@Autowired(required = false)
	private DubboClientFactory dubboClientFactory;

	@Override
	public String testRpc() {
		//DemoRpcService demoRpcService = dubboClientFactory.getDubboClient("demoRpcService");
		TestRpcService testRpcService = dubboClientFactory.getDubboClient("testRpcService");
		UserRpcService userRpcService = dubboClientFactory.getDubboClient("userRpcService");
		//String m = demoRpcService.sayHello();
		//demoRpcService.doIt();
		User user = userRpcService.findUser("zhangsan");
		System.out.println(user);
		String m = testRpcService.test();
		return m;
	}

}
