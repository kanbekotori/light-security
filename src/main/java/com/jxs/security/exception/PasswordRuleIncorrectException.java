package com.jxs.security.exception;


/**
 * 密码规则不符合规范时抛出的异常
 * @author jiangxingshang
 *
 */
@SuppressWarnings("serial")
public class PasswordRuleIncorrectException extends SecurityException {

	public PasswordRuleIncorrectException(String msg) {
		super(msg);
	}
}
