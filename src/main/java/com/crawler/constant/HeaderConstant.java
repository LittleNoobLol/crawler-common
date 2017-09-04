package com.crawler.constant;

import java.util.HashMap;
import java.util.Map;

public class HeaderConstant {

	public static Map<String, String> header;

	static {
		header = new HashMap<String, String>();
		header.put("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		header.put("Cache-Control", "max-age=0");
		header.put("Connection", "keep-alive");
		header.put("Upgrade-Insecure-Requests", "1");
	}

	public static void initCookie(String cookies) {
		header.put("Cookie", cookies);
	}

}
