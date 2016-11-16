package com.framework.utils;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * HTTP 请求工具类
 *
 * @author chenhaipeng
 * @version 2.0
 * @date 2016/10/19 12:05
 */
public class HttpUtil {
	public static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	//20秒钟超时
	private static final int MAX_TIMEOUT = 30000;

	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		configBuilder.setStaleConnectionCheckEnabled(true);
		requestConfig = configBuilder.build();
	}

	/**
	 * 发送 GET 请求（HTTP），不带输入数据
	 *
	 * @param url
	 * @return
	 */
	public static String doGet(String url) throws Exception {
		return doGet(url, new HashMap<String, String>());
	}

	/**
	 * 发送 GET 请求（HTTP），K-V形式
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGet(String url, Map<String, String> params) throws Exception {
		String param = doGetParam(params);
		url = url.indexOf("?") > -1 ? url + "&" + param : url + "?" + param;
		String httpStr = null;
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);
			response = httpclient.execute(httpGet);
			httpStr = getResponse(url, httpStr, response);
		} catch (IOException e) {
			logger.error("请求失败, url = {}", new Object[]{url}, e);
			throw new RuntimeException("请求失败！url=[" + url + "],请求参数:params:"+JsonUtils.object2String(param), e);
		} finally {
			closeResponseIO(url, response);
		}
		return httpStr;
	}

	private static void closeResponseIO(String url, CloseableHttpResponse response) {
		if (response != null) {
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				logger.error("关闭Http IO 异常", new Object[]{url}, e);
				throw new RuntimeException("关闭Http IO 异常");
			}
		}
	}


	/**
	 * map 转Get 参数
	 *
	 * @param bean
	 * @return
	 */
	private static String doGetParam(Map<String, String> bean) {
		StringBuilder result = new StringBuilder();
		Iterator<String> it = bean.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (key != null && bean.get(key) != null) {
				result.append(key);
				result.append("=");
				result.append(bean.get(key));
				result.append("&");
			}
		}
		String rl = result.toString();
		logger.info("参数:" + rl);
		return rl.endsWith("&") ? rl.substring(0, rl.length() - 1) : rl;
	}

	/**
	 * 发送 POST 请求（HTTP），不带输入数据
	 *
	 * @param apiUrl
	 * @return
	 */
	public static String doPost(String apiUrl) throws Exception {
		return doPost(apiUrl, new HashMap<String, Object>());
	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 *
	 * @param url API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static <K, V> String doPost(String url, Map<K, V> params) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String httpStr = null;
		HttpPost httpPost = new HttpPost();
		CloseableHttpResponse response = null;

		try {
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<K, V> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair((String) entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
			response = httpClient.execute(httpPost);
			httpStr = getResponse(url, httpStr, response);

		} catch (IOException e) {
			logger.error("请求失败, url = {}", new Object[]{url}, e);
			throw new RuntimeException("请求失败！url=[" + url + "],请求参数:params:"+JsonUtils.object2String(params), e);
		} finally {
			closeResponseIO(url, response);
		}
		return httpStr;
	}

	private static String getResponse(String apiUrl, String httpStr, CloseableHttpResponse response) throws IOException {
		if (response != null) {
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					httpStr = EntityUtils.toString(resEntity, "utf-8");
				}
			} else {
				throw new RuntimeException("请求失败！url=[" + apiUrl + "]，请求状态码code=" + response == null ?
						null:response.getStatusLine().getStatusCode()+"");
			}
		}
		return httpStr;
	}

	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 *
	 * @param apiUrl
	 * @param json   json对象
	 * @return
	 */
	public static String doPost(String apiUrl, String json) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;

		httpStr = getPostConfig(apiUrl, json, httpClient, httpStr, httpPost, response);
		return httpStr;
	}


	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 *
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doPost(String apiUrl, Map<String, String> params, File file) throws Exception {
		String result = "";

		// 对请求的表单域进行填充
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("file", new FileBody(file));
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue() != null) {
				reqEntity.addPart(entry.getKey(), new StringBody(entry.getValue()));
			}
		}

		HttpClient httpclient = HttpClients.createDefault();
		// 请求处理页面
		HttpPost httppost = new HttpPost(apiUrl);
		httppost.setConfig(requestConfig);
		// 设置请求
		httppost.setEntity(reqEntity);
		// 执行
		HttpResponse response = httpclient.execute(httppost);

		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
			HttpEntity entity = response.getEntity();
			// 显示内容
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		}

		return result;
	}

	private static String getPostConfig(String url, String json, CloseableHttpClient httpClient, String httpStr, HttpPost httpPost, CloseableHttpResponse response) {
		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			httpStr = getResponse(url, httpStr, response);
		} catch (IOException e) {
			logger.error("请求失败, url = {}", new Object[]{url}, e);
			throw new RuntimeException("请求失败！url=[" + url + "],请求参数:params:"+JsonUtils.object2String(json), e);
		} finally {
			closeResponseIO(url, response);
		}
		return httpStr;
	}

	/**
	 * 发送 SSL POST 请求（HTTPS），K-V形式
	 *
	 * @param url API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doPostSSL(String url, Map<String, String> params) {
		CloseableHttpClient httpClient = createSSLHttpClient();
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;
		String httpStr = null;

		try {
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, String> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
			response = httpClient.execute(httpPost);
			httpStr = getResponse(url, httpStr, response);
		} catch (Exception e) {
			logger.error("请求失败, url = {}", new Object[]{url}, e);
			throw new RuntimeException("请求失败！url=[" + url + "],请求参数:params:"+JsonUtils.object2String(params), e);
		} finally {
			closeResponseIO(url, response);
		}
		return httpStr;
	}

	/**
	 * 发送 SSL POST 请求（HTTPS），K-V形式
	 *
	 * @param url    API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doGetSSL(String url, Map<String, String> params) throws Exception {
		CloseableHttpClient httpClient = createSSLHttpClient();
		String apiUrl = url;
		String param = doGetParam(params);
		apiUrl = url.indexOf("?") > -1 ? url + "&" + param : url + "?" + param;
		HttpGet httpGet = new HttpGet(apiUrl);
		CloseableHttpResponse response = null;
		String httpStr = null;
		try {
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);
			httpStr = getResponse(apiUrl, httpStr, response);
		} catch (Exception e) {
			logger.error("请求失败, url = {}", new Object[]{apiUrl}, e);
			throw new RuntimeException("请求失败,url=[" + url + "],请求参数:params:"+JsonUtils.object2String(params), e);
		} finally {
			closeResponseIO(apiUrl, response);
		}
		return httpStr;
	}

	/**
	 * 发送 SSL POST 请求（HTTPS），JSON形式
	 *
	 * @param apiUrl API接口URL
	 * @param json   JSON对象
	 * @return
	 */
	public static String doPostSSL(String apiUrl, String
			json) throws Exception {
		CloseableHttpClient httpClient = createSSLHttpClient();
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		String httpStr = null;

		httpStr = getPostConfig(apiUrl, json, httpClient, httpStr, httpPost, response);
		return httpStr;
	}

	private static CloseableHttpClient createSSLHttpClient() {
		CloseableHttpClient httpClient = null;
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("SSL");

			// Implementation of a trust manager for X509 certificates
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {

				}

				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			ctx.init(null, new TrustManager[]{tm}, new java.security.SecureRandom());
			SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", ssf).build();

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			httpClient = HttpClients.custom().setConnectionManager(connectionManager)
					.setDefaultRequestConfig(requestConfig).build();

		} catch (Exception e) {
			logger.error("createSSLHttpClient 调用失败", e);
			throw new RuntimeException("请求失败, createSSLHttpClient 调用失败", e);
		}

		return httpClient;
	}

	public static Header[] createHeaders(Map<String, String> map) {
		List<Header> headerList = new ArrayList<Header>(map.size());
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				headerList.add(new BasicHeader(entry.getKey(), entry.getValue()));
			}
		}
		return headerList.toArray(new BasicHeader[headerList.size()]);

	}
}