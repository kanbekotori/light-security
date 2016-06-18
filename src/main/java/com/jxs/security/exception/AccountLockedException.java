package com.jxs.security.exception;


import java.lang.*;

/**
 * 帐号被锁定。
 * @author jiangxingshang
 *
 */
@SuppressWarnings("serial")
public class AccountLockedException extends java.lang.SecurityException {

	public AccountLockedException() {
		super("帐号已被禁用!");
	}
}
