package com.jxs.security;


/**
 * 登录细节处理器。
 * @author jiangxingshang
 */
public interface LoginHandler {

    /**
     * 用户登录。
     * @param token
     * @return
     * @throws SecurityException
     */
    Data login(SecurityToken token) throws SecurityException;
}
