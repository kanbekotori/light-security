package com.jxs.security;



import com.jxs.security.resource.http.UriResource;

import java.util.Set;

/**
 * @author jiangxingshang
 */
public class Session {

    private String id;
    private String token;
    private Set<String> resources;
    private SessionManager userManager;

    private boolean login;

    protected Session(SessionManager userManager) {
        this.userManager = userManager;
        this.login = false;
    }

    protected Session(SessionManager userManager, String id, String token) {
        this.userManager = userManager;
        this.id = id;
        this.token = token;
        login = true;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Set<String> getResources() {
        return resources;
    }

    protected void setResources(Set<String> resources) {
        this.resources = resources;
    }

    public boolean hasResource(UriResource res) {
        return resources.contains(res.getId());
    }

    public void logout() {
        if(token != null && token.length() > 0) {
            userManager.del(token);
        }
    }

    public boolean isLogin() {
        return login;
    }
}
