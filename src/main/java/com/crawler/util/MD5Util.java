package com.crawler.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/***
 * 
 * @Description MD5加密
 * @author lixin
 * @date 2017年9月1日
 */
public class MD5Util {

	/***
	 * 
	 * @Description 加密成MD5
	 * @author lixin
	 * @date 2017年9月1日
	 * @param src
	 * @return
	 */
	public static final String MD5(String src) {
		byte[] input = src.getBytes();

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// 官方说法更新摘要
			// 个人理解:就是找到要加密的字符串,放置到update()中等待digest()方法进行产出
			// 就像是在机器的两头:一头的将原材料放进去，一头是产品的输出
			// 获取输入
			digest.update(input);
			// 获得产出
			input = digest.digest();

			// 下面就是进行十六进制的转换
			int length = input.length;
			StringBuffer strBuff = new StringBuffer();
			for (int i = 0; i < length; i++) {
				// 将字符转变成对应的ASSIC值
				int val = ((int) input[i]) & 0xff;
				// 转变成对应的值后小于4位
				if (val < 16) {
					strBuff.append("0");
				}
				strBuff.append(Integer.toHexString(val));
				// strBuff.append(hexDigits[val%16]);
			}

			return strBuff.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return src;
	}
}
