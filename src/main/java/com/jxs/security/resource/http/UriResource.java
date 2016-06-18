package com.jxs.security.resource.http;


import com.jxs.security.resource.SecurityResource;

/**
 * URI保护资源，主要用于验证请求uri，支持rest，支持get，post等标准动词。
 * @author jiangxingshang
 */
public interface UriResource extends SecurityResource {

    /**
     * 获取uri的动词，如果没有则返回null，返回结果都为小写字符。
     * @return
     */
    String getMethod();

    /**
     * 本资源是否需要验证，true表示需要，false表示不需要，不需要验证应该直接放行。
     * @return
     */
    boolean isVerify();

    String getUri();
}
