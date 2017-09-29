package com.lxzl.erp.dataaccess.dao.memcache;

import com.lxzl.se.dataaccess.memcache.BaseMemcacheDAO;


/**
 * 
 * @author lxzl
 *
 */
public interface CaptchaMemcacheDAO extends BaseMemcacheDAO {
	
	boolean addCaptcha(final String username, final String captcha, final int timeout);

	String getCaptcha(final String username, final String captchaKey);

	boolean removeCaptcha(final String username, final String captchaKey);

}
