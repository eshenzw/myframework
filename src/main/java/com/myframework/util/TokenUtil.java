package com.myframework.util;

public class TokenUtil
{
	public static final String RANDOM_ID = "c334414b-2298-42fc-9869-f6c860e29666";

	/**
	 * 创建简单令牌
	 * 
	 * @param data
	 * @return
	 */
	public static String createSimpleToken(String data)
	{
		String hash = StringUtil.hashKey(data + RANDOM_ID, StringUtil.HASH_TYPE_MD5);
		return hash;
	}

	/**
	 * 检验简单令牌
	 */
	public static boolean checkSimpleToken(String data, String token)
	{
		String token0 = createSimpleToken(data);
		return token0.equals(token);
	}
}
