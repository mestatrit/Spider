package cn.xuexibao.business.spider.login;

import static cn.vko.core.common.util.Util.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import cn.vko.core.common.util.Util;

public class HttpClientLogin {

	/**
	 * 
	 * @param loginUrl
	 *            登陆url
	 * @param map
	 *            登陆填充信息
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static BasicCookieStore getCookie(String loginUrl,
			Map<String, String> map) throws IOException, URISyntaxException {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		RequestBuilder requestBuilder = RequestBuilder.post().setUri(
				new URI(loginUrl));
		for (Entry<String, String> entry : map.entrySet()) {
			requestBuilder.addParameter(entry.getKey(), entry.getValue());
		}
		HttpUriRequest login = requestBuilder.build();
		try {
			httpclient.execute(login);
		} finally {
			httpclient.close();
		}
		if (isEmpty(cookieStore)) {
			return null;
		}
		return cookieStore;
	}

	/**
	 * 
	 * @param loginUrl
	 *            登陆url
	 * @param map
	 *            登陆填充信息
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static BasicCookieStore getCookie(String loginUrl,
			Map<String, String> map, final HttpHost proxy) throws IOException,
			URISyntaxException {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		RequestBuilder requestBuilder = RequestBuilder.post().setUri(
				new URI(loginUrl));
		for (Entry<String, String> entry : map.entrySet()) {
			requestBuilder.addParameter(entry.getKey(), entry.getValue());
		}
		Builder b = RequestConfig.custom();
		b.setProxy(proxy);
		b.setConnectTimeout(20000);
		b.setSocketTimeout(20000);
		b.setConnectionRequestTimeout(20000);
		requestBuilder.setConfig(b.build());
		HttpUriRequest login = requestBuilder.build();
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(login);
		} finally {
			Util.safeClose(response);
			Util.safeClose(httpclient);
		}
		if (isEmpty(cookieStore)) {
			return null;
		}
		return cookieStore;
	}

	public static void getAjaxCookie(String loginUrl, Map<String, String> map,
			String referer, CloseableHttpClient httpclient) throws IOException,
			URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder.post().setUri(
				new URI(loginUrl));
		for (Entry<String, String> entry : map.entrySet()) {
			requestBuilder.addParameter(entry.getKey(), entry.getValue());
		}
		HttpUriRequest login = requestBuilder.build();
		login.setHeader(
				"Accept",
				" text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
		login.setHeader("X-Requested-With", "XMLHttpRequest");
		login.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		login.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		login.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		login.setHeader("Referer", referer);
		login.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1897.2 Safari/537.36");
		try {
			httpclient.execute(login);
		} finally {

		}
	}
}
