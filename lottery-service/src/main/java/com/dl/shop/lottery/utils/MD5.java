package com.dl.shop.lottery.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.alibaba.druid.util.StringUtils;

import java.util.Map.Entry;


public class MD5 {
    /**
     * Encodes a string
     * 
     * @param str String to encode
     * @return Encoded String
     * @throws Exception
     */
    public static String crypt(String str)
    {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
        
        StringBuffer hexString = new StringBuffer();
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();
            
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                }               
                else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }               
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return hexString.toString();
    }
    /**
	 * 获取签名
	 * 
	 * @param params
	 *            签名参数
	 * @return sign
	 */
    public static String getSign(String strSign) {
		if(StringUtils.isEmpty(strSign)) {
			return "";
		}
		byte[] bytes = null;
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(strSign.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 将MD5输出的二进制结果转换为小写的十六进制
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString();
	}

}

