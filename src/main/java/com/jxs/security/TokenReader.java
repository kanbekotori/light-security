package com.jxs.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 从http请求中获取token，首先会检查header信息里是否有X-User-Token，如果没有再检查查询参数的"_token"是否有值。
 * @author jiangxingshang
 */
public final class TokenReader {

    public static String readToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("X-User-Token");
        if(StringUtils.isBlank(token)) {
            token = request.getParameter("_token");
        }
        return token;
    }
}
