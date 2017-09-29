package com.lxzl.erp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.erp.core.service.CaptchaService;
import com.lxzl.erp.dataaccess.dao.memcache.CaptchaMemcacheDAO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.captcha.CaptchaUtil;
import com.lxzl.se.common.util.validate.ValidateUtil;
import com.lxzl.se.core.service.impl.BaseServiceImpl;

/**
 * 
 * @author lxzl
 * 
 */
@Service("captchaService")
public class CaptchaServiceImpl extends BaseServiceImpl implements CaptchaService {

	@Autowired
	private CaptchaMemcacheDAO captchaMemcacheDAO;

	@Override
	public String generateCaptcha(String username, int timeout) {
		ValidateUtil.isNotBlank(username, "用户名不允许为空");
		ValidateUtil.notNull(timeout, "验证码超时时间timeout不允许为空");
		int captchaNum = CaptchaUtil.getCaptchaNum();
		String captcha = String.valueOf(captchaNum);
		captchaMemcacheDAO.addCaptcha(username, captcha, timeout);
		return captcha;
	}

	@Override
	public boolean validCaptcha(String username, String inputCaptcha) {
		ValidateUtil.isNotBlank(username, "用户名不允许为空");
		ValidateUtil.isNotBlank(inputCaptcha, "inputCaptcha不允许为空");

		String captchaKey = inputCaptcha;
		String captcha = captchaMemcacheDAO.getCaptcha(username, captchaKey);

		if (!inputCaptcha.equals(captcha)) {
			throw new BusinessException("captcha两次输入不一致");
		}

		captchaMemcacheDAO.removeCaptcha(username, captchaKey);
		return true;
	}

}
