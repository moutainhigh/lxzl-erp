package com.lxzl.erp.core.service;

import com.lxzl.se.core.service.BaseService;


public interface TokenService extends BaseService {

	String generateToken(long timeout);
	
	boolean validToken(String inputToken);
}
