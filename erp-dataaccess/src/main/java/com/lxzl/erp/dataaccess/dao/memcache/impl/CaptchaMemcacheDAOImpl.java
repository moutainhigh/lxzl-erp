package com.lxzl.erp.dataaccess.dao.memcache.impl;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.springframework.stereotype.Repository;

import com.lxzl.erp.dataaccess.dao.memcache.CaptchaMemcacheDAO;
import com.lxzl.se.dataaccess.memcache.callback.MemcacheCallback;
import com.lxzl.se.dataaccess.memcache.client.XMemcachedClientProxy;
import com.lxzl.se.dataaccess.memcache.impl.BaseMemcacheDAOImpl;

/**
 * 
 * @author lxzl
 *
 */
@Repository("captchaMemcacheDAO")
public class CaptchaMemcacheDAOImpl extends BaseMemcacheDAOImpl implements CaptchaMemcacheDAO {
	
	public static String PREFIX_TOKEN = "captcha:";

	@Override
	public boolean addCaptcha(final String username, final String captcha, final int timeout) {
		boolean result = memcacheTemplate.execute(new MemcacheCallback<Boolean>() {
			@Override
			public Boolean doInMemcache(XMemcachedClientProxy memcachedClient) throws TimeoutException, InterruptedException, MemcachedException {
				String key = PREFIX_TOKEN + username + "_" + captcha;
				String value = captcha;
				memcachedClient.set(key, timeout, value);
				return true;
			}
		});
		
		return result;
	}

	@Override
	public String getCaptcha(final String username, final String captchaKey) {
		String result = memcacheTemplate.execute(new MemcacheCallback<String>() {
			@Override
			public String doInMemcache(XMemcachedClientProxy memcachedClient) throws TimeoutException, InterruptedException, MemcachedException {
				String key = PREFIX_TOKEN + username + "_" + captchaKey;
				String value = memcachedClient.get(key);
				return value;
			}
		});

		return result;
	}

	@Override
	public boolean removeCaptcha(final String username, final String captchaKey) {
		boolean result = memcacheTemplate.execute(new MemcacheCallback<Boolean>() {
			@Override
			public Boolean doInMemcache(XMemcachedClientProxy memcachedClient) throws TimeoutException, InterruptedException, MemcachedException {
				String key = PREFIX_TOKEN + username + "_" + captchaKey;
				memcachedClient.delete(key);
				return true;
			}
		});
		
		return result;
	}

}
