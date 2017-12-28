package com.lxzl.erp.core.service;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.cache.CommonCache;
import com.lxzl.erp.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.se.core.service.StartupCallback;

import java.util.List;

@Service("startupCallback")
public class InitStartService implements StartupCallback {

	@Override
	public void businessHandle() {
		cacheUser();
	}

	public void cacheUser(){
		System.out.println("-------------------------------------缓存用户列表-------------------------------------");
		UserQueryParam userQueryParam = new UserQueryParam();
		try {
			ServiceResult<String, List<User>> getUserResult = userService.getUserListByParam(userQueryParam);
			if (ErrorCode.SUCCESS.equals(getUserResult.getErrorCode())) {
				List<User> userList = getUserResult.getResult();
				for (User user : userList) {
					CommonCache.userMap.put(user.getUserId(), user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private UserService userService;
}
