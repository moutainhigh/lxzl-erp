package com.lxzl.erp.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.lxzl.erp.dataaccess.domain.UserDO;
import com.lxzl.se.common.util.poi.ExcelResult;
import com.lxzl.se.core.service.BaseService;

public interface UserService extends BaseService {

	UserDO findByUsername(String username);

	Integer addUser(String username, String password, String nick, Integer sex, Integer age, Date birthday, String address, String tel, String email,
			String md5Key);

	ExcelResult importUser(String file, InputStream inputStream) throws InstantiationException, IllegalAccessException, IOException;
}
