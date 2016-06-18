package com.jxs.security.cache;

import redis.clients.jedis.Jedis;

/**
 * 基于Redis实现的缓存。
 * @author jiangxingshang
 */
public class RedisSessionCache implements SessionCache {

    private Jedis jedis;

    public RedisSessionCache(String host, int port) {
        this.jedis = new Jedis(host, port);
    }

    @Override
    public void set(String key, String value) {
        jedis.set(key, value);
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public void del(String key) {
        jedis.del(key);
    }

    @Override
    public void expire(String key, int timeout) {
        jedis.expire(key, timeout);
    }
}
