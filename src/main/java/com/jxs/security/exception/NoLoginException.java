package com.jxs.security.exception;



/**
 * 用户未登录或登录超时。
 * @author jiangxingshang
 *
 */
@SuppressWarnings("serial")
public class NoLoginException extends SecurityException {

	public NoLoginException() {
		super("您还未登录或登录已超时!");
	}
}
