package com.jxs.security.resource.http;

import com.jxs.security.Session;
import com.jxs.security.SessionManager;
import org.springframework.beans.factory.FactoryBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * URI资源验证器。
 * @author jiangxingshang
 */
public class SecurityFilterFactoryBean implements FactoryBean<Filter> {

    private UriResourceDataSource dataSource;
    private SecurityAuthcFailHandler handler;
    private SessionManager sessionManager;
    private SecurityFilter instance;
    private List<String> staticResources;

    public SecurityFilterFactoryBean(SessionManager sessionManager, UriResourceDataSource dataSource, SecurityAuthcFailHandler handler) {
        this.sessionManager = sessionManager;
        this.dataSource = dataSource;
        this.handler = handler;
    }

    @Override
    public Filter getObject() throws Exception {
        if(instance == null) {
            synchronized (this) {
                instance = new SecurityFilter();
            }
        }
        return instance;
    }

    @Override
    public Class<?> getObjectType() {
        return SecurityFilter.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 添加静态资源路径，这些路径会被忽略验证。建议：你应该使用nginx等类似的前置服务器来服务静态资源。
     * 这些路径会使用{@link String#startsWith(String)}的方式来验证。
     * @param res 静态资源URL
     * @return
     */
    public SecurityFilterFactoryBean addStaticResource(String res) {
        if(this.staticResources == null) {
            this.staticResources = new LinkedList<>();
        }
        this.staticResources.add(res);
        return this;
    }

    private final class SecurityFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse)response;

            if(staticResources != null && staticResources.size() > 0) {
                String path = req.getRequestURI();
                for(String r : staticResources) {
                    if(path.startsWith(r)) {
                        chain.doFilter(req, resp);
                        return;
                    }
                }
            }
            Session s = sessionManager.getSession();
            UriResource uri = dataSource.match(req);
            if(uri != null) {
                if(uri.isVerify()) {
                    if(s.isLogin()) {
                        if(s.hasResource(uri)) {
                            //用户拥有这个资源权限，更新token过期时间
                            sessionManager.expire(s.getToken());
                            chain.doFilter(req, resp);
                            return;
                        } else {
                            handler.onFail(req, resp, SecurityAuthcFailHandler.UNAUTHORIZATION);
                        }
                    } else {
                        //未登录
                        handler.onFail(req, resp, SecurityAuthcFailHandler.NOT_LOGIN);
                        return;
                    }
                } else {
                    //无需权限验证的资源
                    chain.doFilter(req, resp);
                    return;
                }
            }

            chain.doFilter(req, resp);
        }

        @Override
        public void destroy() {

        }
    }
}
