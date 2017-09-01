package com.crawler.constant;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

public class ParamConstant {
	public static List<BasicNameValuePair> formParams;

	static {
		formParams = new ArrayList<BasicNameValuePair>();
	}

}
