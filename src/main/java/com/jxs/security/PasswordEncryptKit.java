package com.jxs.security;


import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具。
 * @author jiangxingshang
 */
public class PasswordEncryptKit {

    static char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    /**
     * 加密密码。
     * @param pwd
     * @param salt 一个盐值，加大MD5破解的困难度，在你的应用系统中，你应该总是使用相同的盐值。
     * @return
     * @throws IllegalArgumentException
     */
	public static String encrypt(String pwd, String salt) throws IllegalArgumentException {
        if(StringUtils.isBlank(pwd)) throw new IllegalArgumentException("password must not be empty.");
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            byte[] md = mdInst.digest((pwd + salt).getBytes());
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
	}

}
