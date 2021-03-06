package com.crawler.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.crawler.entity.Page;

/**
 * HttpClient 池工具类
 * 
 */
public class HttpClientUtil {

	// 超时
	static final int timeOut = 10 * 1000;

	private static CloseableHttpClient httpClient = null;

	private final static Object syncLock = new Object();

	static Logger log = Logger.getLogger(HttpClientUtil.class);

	private static void config(HttpRequestBase httpRequestBase, Map<String, String> headers) {
		// 设置Header等
		if (headers != null) {
			for (String key : headers.keySet()) {
				httpRequestBase.setHeader(key, headers.get(key));
			}
		}

		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
				.setConnectTimeout(timeOut).build();
		httpRequestBase.setConfig(requestConfig);
	}

	/**
	 * 获取HttpClient对象
	 * 
	 * @return
	 */
	public static CloseableHttpClient getHttpClient(String url) {
		String hostname = url.split("/")[2];
		int port = 80;
		if (hostname.contains(":")) {
			String[] arr = hostname.split(":");
			hostname = arr[0];
			port = Integer.parseInt(arr[1]);
		}
		if (httpClient == null) {
			synchronized (syncLock) {
				if (httpClient == null) {
					httpClient = createHttpClient(200, 40, 100, hostname, port);
				}
			}
		}
		return httpClient;
	}

	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 */
	public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname,
			int port) {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", plainsf).register("https", sslsf).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// 将每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(maxPerRoute);
		HttpHost httpHost = new HttpHost(hostname, port);
		// 将目标主机的最大连接数增加
		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 5) {// 如果已经重试了5次，就放弃
					return false;
				}
				if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
					// return false;
					return true;
				}
				if (exception instanceof InterruptedIOException) {// 超时
					// return false;
					return true;
				}
				if (exception instanceof UnknownHostException) {// 目标服务器不可达
					// return false;
					return true;
				}
				if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
					// return false;
					return true;
				}
				if (exception instanceof SSLException) {// SSL握手异常
					// return false;
					return true;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setRetryHandler(httpRequestRetryHandler).build();

		return httpClient;
	}

	/***
	 * 
	 * @Description GET请求内容
	 * @author lixin
	 * @date 2017年8月30日
	 * @param 请求路径
	 * @param 请求头,可以为空
	 * @return 请求网站后相应结果
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> headers) throws Exception {
		HttpGet httpget = new HttpGet(url);
		config(httpget, headers);
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient(url).execute(httpget, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				log.error("get请求关闭对象失败", e);
			}
		}
	}

	/***
	 * 
	 * @Description GET请求内容
	 * @author lixin
	 * @date 2017年8月30日
	 * @param 请求路径
	 * @param 请求头,可以为空
	 * @return Page对象
	 * @throws Exception
	 */
	public static Page getPage(String url, Map<String, String> headers) throws Exception {
		Page page = null;
		HttpGet httpget = new HttpGet(url);
		config(httpget, headers);
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient(url).execute(httpget, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String html = EntityUtils.toString(entity, "utf-8");
			page = new Page(html, response.getAllHeaders(), response.getStatusLine().getStatusCode());
			EntityUtils.consume(entity);
			return page;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				log.error("getPage请求关闭对象失败", e);
			}
		}
	}

	/***
	 * 
	 * @Description POST请求表单内容
	 * @author lixin
	 * @date 2017年8月30日
	 * @param 请求路径
	 * @param 请求头,可以为空
	 * @param 请求表单参数,可以为空
	 * @return 请求网站后相应结果
	 * @throws Exception
	 */
	public static String postFromData(String url, Map<String, String> headers, List<BasicNameValuePair> formParams)
			throws Exception {
		HttpPost httpPost = new HttpPost(url);

		if (formParams != null) {
			HttpEntity postParam = new UrlEncodedFormEntity(formParams, "utf-8");
			httpPost.setEntity(postParam);
		}

		config(httpPost, headers);
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				log.error("postFromData请求关闭对象失败", e);
			}
		}
	}

	/***
	 * 
	 * @Description POST 请求JSON 参数格式内容
	 * @author lixin
	 * @date 2017年8月30日
	 * @param 请求路径
	 * @param 请求头,可以为空
	 * @param 请求JSON参数,可以为空
	 * @return 请求网站后相应结果
	 * @throws Exception
	 */
	public static String postJson(String url, Map<String, String> headers, String params) throws Exception {
		HttpPost httpPost = new HttpPost(url);

		if (params != null) {
			HttpEntity postParam = new StringEntity(params, "utf-8");
			httpPost.setEntity(postParam);
		}

		config(httpPost, headers);
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				log.error("postJson请求关闭对象失败", e);
			}
		}
	}

}