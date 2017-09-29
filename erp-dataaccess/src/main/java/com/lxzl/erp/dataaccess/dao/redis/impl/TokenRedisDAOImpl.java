package com.lxzl.erp.dataaccess.dao.redis.impl;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Repository;

import com.lxzl.erp.dataaccess.dao.redis.TokenRedisDAO;
import com.lxzl.se.dataaccess.redis.callback.RedisCallback;
import com.lxzl.se.dataaccess.redis.impl.BaseRedisDAOImpl;

/**
 * 
 * @author lxzl
 *
 */
@Repository("tokenRedisDAO")
public class TokenRedisDAOImpl extends BaseRedisDAOImpl implements TokenRedisDAO {
	
	public static String PREFIX_TOKEN = "token:";

	@Override
	public boolean addToken(final String token, final long timeout) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public String getKey() {
				return PREFIX_TOKEN + token;
			}

			@Override
			public Boolean doInRedis(RedisConnection connection, byte[] key) {
				connection.setEx(key, timeout, serialize(token));
				return true;
			}
		});

		return result;
	}

	@Override
	public String getToken(final String tokenKey) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String getKey() {
				return PREFIX_TOKEN + tokenKey;
			}

			@Override
			public String doInRedis(RedisConnection connection, byte[] key) {
				byte[] bytes = connection.get(key);
				return deserialize(String.class, bytes);
			}
		});

		return result;
	}

	@Override
	public boolean removeToken(final String tokenKey) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public String getKey() {
				return PREFIX_TOKEN + tokenKey;
			}

			@Override
			public Boolean doInRedis(RedisConnection connection, byte[] key) {
				connection.del(key);
				return true;
			}
		});

		return result;
	}

}
