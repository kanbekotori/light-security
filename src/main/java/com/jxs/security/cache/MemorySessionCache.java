package com.jxs.security.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于Map实现的缓存，适用与开发阶段。
 * @author jiangxingshang
 */
public class MemorySessionCache implements SessionCache {

    private Map<String, String> map = new HashMap<>();

    @Override
    public void del(String key) {
        map.remove(key);
    }

    @Override
    public void set(String key, String value) {
        map.put(key, value);
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public void expire(String key, int timeout) {
        //nothing to do
    }
}
