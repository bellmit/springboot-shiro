package com.sq.transportmanage.gateway.service.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

public final class PasswordUtil {
//	/**生成MD5摘要**/
//	public static String md5( String text ){
//		try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			return new String(Hex.encodeHex(md.digest(text.getBytes()))).toLowerCase() ;
//		}catch(Exception ex) {
//			return null;
//		}
//	}
	
	/**生成MD5摘要**/
	public static String md5( String password , String salt ){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return new String(Hex.encodeHex(md.digest( (password+"{"+salt+"}").getBytes()))) .toLowerCase() ;
		}catch(Exception ex) {
			return null;
		}
	}

	public static void main(String[] args) {
//		System.out.println( PasswordUtil.md5("123456") );
		System.out.println( PasswordUtil.md5("13579","huiyiyongche05") );
		System.out.println( PasswordUtil.md5("24680","huiyiyongche04") );
		System.out.println( PasswordUtil.md5("66666","huiyiyongche08") );
		System.out.println( PasswordUtil.md5("123789","huiyiyongche06") );
		System.out.println( PasswordUtil.md5("222111","huiyiyongche03") );
		System.out.println( PasswordUtil.md5("666777","huiyiyongche01") );
		System.out.println( PasswordUtil.md5("789789","huiyiyongche07") );
		System.out.println( PasswordUtil.md5("999000","huiyiyongche02") );
		//System.out.println( "15101067308".substring(7) );
	}
}