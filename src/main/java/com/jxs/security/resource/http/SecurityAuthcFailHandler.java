package com.jxs.security.resource.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求验证失败处理器。
 * @author jiangxingshang
 */
public interface SecurityAuthcFailHandler {

    int NOT_LOGIN = 0; //未登录
    int UNAUTHORIZATION = 1; //权限不足

    /**
     *
     * @param request
     * @param response
     * @param status 参考本接口的常量
     */
    void onFail(HttpServletRequest request, HttpServletResponse response, int status);
}
