package com.jxs.security;

import java.util.Set;

/**
 * 登录成功后返回的数据包装类。
 * @author jiangxingshang
 */
public class Data {

    private String id;

    private Set<String> resources;

    private String accessToken;

    private Object obj;

    /**
     *
     * @param id 用户唯一标识
     * @param resources 用户持有的资源id
     */
    public Data(String id, Set<String> resources) {
        this.id = id;
        this.resources = resources;
    }

    /**
     *
     * @param id 用户唯一标识
     * @param resources 用户持有的资源id
     * @param obj 用户自己的数据
     */
    public Data(String id, Set<String> resources, Object obj) {
        this.id = id;
        this.resources = resources;
        this.obj = obj;
    }

    public String getId() {
        return id;
    }

    public Set<String> getResources() {
        return resources;
    }

    protected void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
