package com.sq.transportmanage.gateway.service.util;

import java.security.SecureRandom;
import java.util.Random;

public final class NumberUtil {
	private static final String SEED_CHARS = "0123456789";
	private static final Random RND = new  SecureRandom();
	
	public static String genRandomCode( int length ) {
		StringBuffer sb = new StringBuffer( length  );
		for(int i=0;i<length;i++) {
			int index = RND.nextInt( SEED_CHARS.length()  );
			char cha = SEED_CHARS.charAt(index);
			sb.append(cha);
		}
		return sb.toString();
	}

	public static String genRandomCode( int length, String seed ) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<length;i++) {
			int index = RND.nextInt( seed.length()  );
			char cha = seed.charAt(index);
			builder.append(cha);
		}
		return builder.toString();
	}
	

}