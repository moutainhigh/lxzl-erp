package com.lxzl.erp.rpc.api.service;

import com.lxzl.erp.rpc.api.domain.User;

public interface UserRpcService {

	User findUser(String username);
	
}
