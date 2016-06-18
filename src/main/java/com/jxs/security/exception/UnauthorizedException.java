package com.jxs.security.exception;


/**
 * 帐号权限不足。
 * @author jiangxingshang
 */
@SuppressWarnings("serial")
public class UnauthorizedException extends SecurityException {

	public UnauthorizedException() {
		super("权限不足!");
	}
}
