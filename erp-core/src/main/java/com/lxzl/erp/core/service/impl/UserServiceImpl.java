package com.lxzl.erp.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.DemoResultCode;
import com.lxzl.erp.core.service.UserService;
import com.lxzl.erp.dataaccess.dao.mysql.UserMysqlDAO;
import com.lxzl.erp.dataaccess.domain.UserDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.poi.ExcelCallback;
import com.lxzl.se.common.util.poi.ExcelResult;
import com.lxzl.se.common.util.poi.ExcelUtil;
import com.lxzl.se.common.util.poi.ExcelUtil.ExcelType;
import com.lxzl.se.common.util.secret.MD5Util;
import com.lxzl.se.common.util.validate.ValidateUtil;
import com.lxzl.se.core.service.impl.BaseServiceImpl;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Autowired
	private UserMysqlDAO userMysqlDAO;

	@Override
	public UserDO findByUsername(String username) {
		ValidateUtil.isNotBlank(username, DemoResultCode.A_BUSINESS_EXCEPTION);
		UserDO user = userMysqlDAO.findByUsername(username);
		return user;
	}

	@Transactional
	@Override
	public Integer addUser(final String username, String password, String nick, Integer sex, Integer age, Date birthday, String address, String tel,
			String email, String md5Key) {
		ValidateUtil.isNotBlank(username, DemoResultCode.A_BUSINESS_EXCEPTION);
		ValidateUtil.isNotBlank(password, "用户密码不能为空");

		// 根据用户名查询是否已经存在
		UserDO oldUser = userMysqlDAO.findByUsername(username);

		if (oldUser != null) {
			throw new BusinessException("该用户已经存在");
		}

		String encryptPword = generateMD5Password(username, password, md5Key);
		UserDO user = new UserDO();
		user.setUsername(username);
		user.setPassword(encryptPword);
		user.setNick(StringUtils.isBlank(nick) ? username : nick);
		user.setSex(sex);
		user.setAge(age);
		user.setBirthday(birthday);
		user.setAddress(address);
		user.setTel(tel);
		user.setEmail(email);
		user.setCreateUser(username);
		user.setUpdateUser(username);
		user.setStatus(UserDO.STATUS_NORMAL);
		userMysqlDAO.save(user);
		Integer userId = user.getId();

		if (userId == null || userId.intValue() <= 0) {
			throw new BusinessException("创建用户失败");
		}

		return userId;
	}

	@Override
	public ExcelResult importUser(String file, InputStream inputStream) throws InstantiationException, IllegalAccessException, IOException {
		ExcelType excelType = ExcelUtil.getExcelType(file);
		ExcelResult excelResult = ExcelUtil.importExcelWithHeader(excelType, 1, inputStream, UserDO.class, new ExcelCallback<UserDO>() {
			@Override
			public boolean handleImportData(UserDO user, int rowNum) {
				boolean flag = true;

				try {
					String username = user.getUsername();
					String password = user.getPassword();
					
					ValidateUtil.isNotBlank(username, DemoResultCode.A_BUSINESS_EXCEPTION);
					ValidateUtil.isNotBlank(password, "用户密码不能为空");

					// 根据用户名查询是否已经存在
					UserDO oldUser = userMysqlDAO.findByUsername(username);

					if (oldUser != null) {
						throw new BusinessException("该用户已经存在");
					}

					String encryptPword = generateMD5Password(username, password, ApplicationConfig.authKey);
					user.setPassword(encryptPword);
					userMysqlDAO.save(user);
					Integer userId = user.getId();

					if (userId == null || userId.intValue() <= 0) {
						throw new BusinessException("创建用户失败");
					}
				} catch (Exception e) {
					flag = false;
					LOG.error("ImportUser error, user=" + user, e);
				}

				return flag;
			}
		});

		return excelResult;
	}

	/**
	 * 生成MD5密码
	 * 
	 * @param username
	 * @param password
	 * @param createTime
	 * @param md5Key
	 * @return
	 */
	private String generateMD5Password(String username, String password, String md5Key) {
		return MD5Util.encryptWithKey(username + password, md5Key);
	}
}
