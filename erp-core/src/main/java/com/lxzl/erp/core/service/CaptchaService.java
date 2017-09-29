package com.lxzl.erp.core.service;

import com.lxzl.se.core.service.BaseService;

/**
 * 
 * @author lxzl
 *
 */
public interface CaptchaService extends BaseService {

	String generateCaptcha(String username, int timeout);

	boolean validCaptcha(String username, String inputCaptcha);
}
