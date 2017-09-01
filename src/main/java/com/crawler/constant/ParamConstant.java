package com.crawler.constant;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

public class ParamConstant {
	private static List<BasicNameValuePair> formParams;

	static {
		formParams = new ArrayList<BasicNameValuePair>();
		formParams.add(new BasicNameValuePair("studentType", "1"));
		formParams.add(new BasicNameValuePair("chkAll", "all"));
		formParams.add(new BasicNameValuePair("recordStatus", "1"));
		formParams.add(new BasicNameValuePair("recordStatus", "11"));
		formParams.add(new BasicNameValuePair("recordStatus", "2"));
		formParams.add(new BasicNameValuePair("recordStatus", "3"));
		formParams.add(new BasicNameValuePair("recordStatus", "4"));
		formParams.add(new BasicNameValuePair("__multiselect_recordStatus", ""));
		formParams.add(new BasicNameValuePair("__checkbox_conditions['isTransfered']", "true"));
		formParams.add(new BasicNameValuePair("classId", ""));
		formParams.add(new BasicNameValuePair("conditions['createDateBegin']", ""));
		formParams.add(new BasicNameValuePair("conditions['createDateEnd']", ""));
		formParams.add(new BasicNameValuePair("conditions['userName']", ""));
		formParams.add(new BasicNameValuePair("conditions['passport']", ""));
		formParams.add(new BasicNameValuePair("conditions['updateDateBegin']", ""));
		formParams.add(new BasicNameValuePair("conditions['updateDateEnd']", ""));
		formParams.add(new BasicNameValuePair("conditions['studentNumber']", ""));
		formParams.add(new BasicNameValuePair("conditions['idCard']", ""));
	}

	public static void pageSize(Integer pageSize) {
		formParams.add(new BasicNameValuePair("pageSize", pageSize.toString()));

	}

	public static void page(Integer page) {
		formParams.add(new BasicNameValuePair("pageNo", page.toString()));
	}

	public static List<BasicNameValuePair> getParam(Integer page) {
		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
		formParams.add(new BasicNameValuePair("studentType", "1"));
		formParams.add(new BasicNameValuePair("chkAll", "all"));
		formParams.add(new BasicNameValuePair("recordStatus", "1"));
		formParams.add(new BasicNameValuePair("recordStatus", "11"));
		formParams.add(new BasicNameValuePair("recordStatus", "2"));
		formParams.add(new BasicNameValuePair("recordStatus", "3"));
		formParams.add(new BasicNameValuePair("recordStatus", "4"));
		formParams.add(new BasicNameValuePair("__multiselect_recordStatus", ""));
		formParams.add(new BasicNameValuePair("__checkbox_conditions['isTransfered']", "true"));
		formParams.add(new BasicNameValuePair("classId", ""));
		formParams.add(new BasicNameValuePair("conditions['createDateBegin']", ""));
		formParams.add(new BasicNameValuePair("conditions['createDateEnd']", ""));
		formParams.add(new BasicNameValuePair("conditions['userName']", ""));
		formParams.add(new BasicNameValuePair("conditions['passport']", ""));
		formParams.add(new BasicNameValuePair("conditions['updateDateBegin']", ""));
		formParams.add(new BasicNameValuePair("conditions['updateDateEnd']", ""));
		formParams.add(new BasicNameValuePair("conditions['studentNumber']", ""));
		formParams.add(new BasicNameValuePair("conditions['idCard']", ""));
		formParams.add(new BasicNameValuePair("pageSize", "20"));
		formParams.add(new BasicNameValuePair("pageNo", page.toString()));
		return formParams;
	}

	public static List<BasicNameValuePair> getFormParams() {
		return formParams;
	}

}
