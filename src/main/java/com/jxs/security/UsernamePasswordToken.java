package com.jxs.security;

/**
 * @author jiangxingshang
 */
public class UsernamePasswordToken implements SecurityToken {

    private String username;
    private String password;

    public UsernamePasswordToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
