package com.jxs.security.exception;

/**
 * 帐号或密码错误。
 * @author jiangxingshang
 */
@SuppressWarnings("serial")
public class UsernamePasswordIncorrectException extends SecurityException {

	public UsernamePasswordIncorrectException() {
		super("用户名或密码错误!");
	}
}
