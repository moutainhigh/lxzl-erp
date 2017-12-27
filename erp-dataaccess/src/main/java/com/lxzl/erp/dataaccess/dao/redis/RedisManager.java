package com.lxzl.erp.dataaccess.dao.redis;

import com.alibaba.fastjson.JSON;
import com.lxzl.se.dataaccess.redis.callback.RedisCallback;
import com.lxzl.se.dataaccess.redis.impl.BaseRedisDAOImpl;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Component;

@Component
public class RedisManager extends BaseRedisDAOImpl {
    private static long timeout_default = 60 * 60L;

    public boolean add(final String key, final Object value) {
        return this.add(key, value, null);
    }

    public boolean add(final String key, final Object value, final Long timeoutSecond) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Boolean doInRedis(RedisConnection connection, byte[] key) {
                long t = 0l;
                if (timeoutSecond == null) {
                    t = timeout_default;
                } else {
                    t = timeoutSecond;
                }
                connection.setEx(key, t, serialize(JSON.toJSONString(value)));
                return true;
            }
        });
        return result;
    }

    public String get(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String doInRedis(RedisConnection connection, byte[] key) {
                byte[] bytes = connection.get(key);
                return deserialize(String.class, bytes);
            }
        });

        return result;
    }

    public <T> T get(final String key, Class<T> clazz) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String doInRedis(RedisConnection connection, byte[] key) {
                byte[] bytes = connection.get(key);
                return deserialize(String.class, bytes);
            }
        });
        return JSON.parseObject(result, clazz);
    }

    public boolean remove(final String key) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public String getKey() {
                return key;
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
