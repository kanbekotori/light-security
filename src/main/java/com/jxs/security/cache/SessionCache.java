package com.jxs.security.cache;

/**
 * @author jiangxingshang
 */
public interface SessionCache {

    void set(String key, String value);

    String get(String key);

    void del(String key);

    /**
     * 设置N个时间单位后过期。
     * @param key
     * @param timeout 毫秒
     */
    void expire(String key, int timeout);
}
