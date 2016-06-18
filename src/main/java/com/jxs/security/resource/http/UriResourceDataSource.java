package com.jxs.security.resource.http;

import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 负责从数据库中获取URI类型的保护资源并缓存在内存中，根据请求uri可得到对应的资源数据。
 * @author jiangxingshang
 */
public class UriResourceDataSource {

    private AntPathMatcher matcher = new AntPathMatcher();
    private List<String> keys = new ArrayList<>();
    private Map<String, UriResource> resources = new HashMap<>();

    /**
     * 设置资源数据源。
     * @param resources
     */
    public void setResources(List<? extends UriResource> resources) {
        for(UriResource r : resources) {
            keys.add(r.getId());
            this.resources.put(r.getId(), r);
        }
    }

    /**
     * @param request
     * @return
     */
    public UriResource match(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod().toLowerCase();
        for(String key : keys) {
            if(matcher.match(resources.get(key).getUri(), uri)) {
                UriResource sr = resources.get(key);
                if(sr.getMethod() == null || sr.getMethod().length() == 0) {
                    return sr;
                } else if(method.equals(sr.getMethod())) {
                    return sr;
                }
            }
        }
        return null;
    }
}
