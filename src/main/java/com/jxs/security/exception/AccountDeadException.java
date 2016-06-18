package com.jxs.security.exception;

import java.lang.*;

/**
 * 帐号永久被停用。
 * @author jiangxingshang
 *
 */
@SuppressWarnings("serial")
public class AccountDeadException extends java.lang.SecurityException {

	public AccountDeadException() {
		super("帐号已被永久停用!");
	}
}
