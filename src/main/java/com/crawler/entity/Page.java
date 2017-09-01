package com.crawler.entity;

import org.apache.http.Header;

/**
 * 
 * @Description 请求相应类
 * @author lixin
 * @date 2017年8月31日
 */
public class Page {

	public Page() {
	}

	public Page(String html, Header[] header, int status) {
		this.html = html;
		this.header = header;
		this.status = status;
	}

	/**
	 * html信息
	 */
	private String html;

	/**
	 * 相应的header
	 */
	private Header[] header;

	/**
	 * 响应的状态码
	 */
	private int status;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Header[] getHeader() {
		return header;
	}

	public void setHeader(Header[] header) {
		this.header = header;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
