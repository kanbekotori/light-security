package com.jxs.security;

import com.jxs.security.cache.SessionCache;
import com.jxs.security.exception.NoLoginException;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 负责处理用户登录和登出，缓存用户的可访问资源数据。
 * 缓存资源信息主要将资源的id缓存在内存中，所以id长度应尽可能的短，这样可以节省空间，用户量越多节省空间越多。
 * 缓存服务器采用Redis。
 * @author jiangxingshang
 */
public class SessionManager {

    private SessionCache sessionCache;
    private LoginHandler realm;
    private int timeout = 3600;
    private String prefix;

    public SessionManager(SessionCache sessionCache) throws IOException {
        this.sessionCache = sessionCache;
    }

    public SessionManager(String sessionPrefix, SessionCache sessionCache) throws IOException {
        this.sessionCache = sessionCache;
        this.prefix = sessionPrefix;
    }

    /**
     * 设置会话超时，单位秒。
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setRealm(LoginHandler realm) {
        this.realm = realm;
    }

    private void set(String token, Data data) {
        sessionCache.set(prefix + token, data.getId() + "," + StringUtils.join(data.getResources(), ","));
    }

    public Data get(String token) throws NoLoginException {
        String data = sessionCache.get(prefix + token);
        if(data == null) throw new NoLoginException();
        Set<String> sets = new HashSet<>();
        String id = null;
        for(String s : StringUtils.split(data, ",")) {
            if(id == null) {
                id = s;
            } else {
                sets.add(s);
            }
        }
        return new Data(id, sets);
    }

    public void del(String token) {
        sessionCache.del(prefix + token);
    }

    public void expire(String token) {
        sessionCache.expire(prefix + token, timeout);
    }

    /**
     * 登录，登录成功后返回一个凭证，以后的请求都必须带上这个凭证，token的传递方式可以参考{@link TokenReader}。
     * @param token
     * @return
     * @throws com.jxs.security.exception.SecurityException
     * @see TokenReader
     */
    public Data login(SecurityToken token) throws com.jxs.security.exception.SecurityException {
        String t = UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
        Data data = realm.login(token);
        data.setAccessToken(t);
        set(t, data);
        expire(t);
        return data;
    }

    /**
     * 返回一个用户会话，返回值一定不为null，你可以使用会话来查看用户登录状态，请求中必须带上登录后获得的token。
     * @return
     * @see TokenReader
     */
    public Session getSession() {
        String token = TokenReader.readToken();
        Data data = null;
        try {
            data = get(token);
        } catch(NoLoginException e) {
        }
        Session s;
        if(data != null) {
            s = new Session(this, data.getId(), token);
            s.setResources(data.getResources());
        } else {
            s = new Session(this);
        }
        return s;
    }
}
