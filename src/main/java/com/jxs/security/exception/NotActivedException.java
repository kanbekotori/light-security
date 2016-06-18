package com.jxs.security.exception;

import java.lang.*;

/**
 * 账号未激活。
 * @author jiangxingshang
 *
 */
@SuppressWarnings("serial")
public class NotActivedException extends java.lang.SecurityException {

	public NotActivedException() {
		super("帐号还未激活!");
	}
}
