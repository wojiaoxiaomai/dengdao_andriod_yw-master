package com.choucheng.dengdao2.tools;

import com.choucheng.dengdao2.base64.BASE64Decoder;
import com.choucheng.dengdao2.base64.BASE64Encoder;
import com.choucheng.dengdao2.common.FinalVarible;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AESInfo {

	private static final String sKey = FinalVarible.SEED;

	/*
	 * 解密
	 */
	public static String decrypt(String sSrc) throws Exception {

		// 判断Key是否正确
		if (sKey == null) {
			return null;
		}
		byte[] enCodeFormat = sKey.getBytes("UTF-8");
		SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// hex2byte(sSrc);
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original, "UTF-8");
		return originalString;
	}

	/*
	 * 加密
	 */
	public static String encrypt(String sSrc) throws Exception {
		if (sKey == null) {
			return null;
		}
		byte[] enCodeFormat = sKey.getBytes("UTF-8");
		SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
		BASE64Encoder base = new BASE64Encoder();
		return base.encode(encrypted);
	}
}
