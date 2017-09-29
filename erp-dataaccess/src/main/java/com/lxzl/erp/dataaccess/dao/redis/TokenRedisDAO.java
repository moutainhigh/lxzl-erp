package com.lxzl.erp.dataaccess.dao.redis;

import com.lxzl.se.dataaccess.redis.BaseRedisDAO;


/**
 * 
 * @author lxzl
 *
 */
public interface TokenRedisDAO extends BaseRedisDAO {
	
	boolean addToken(final String token, final long timeout);

	String getToken(final String tokenKey);

	boolean removeToken(final String tokenKey);

}
