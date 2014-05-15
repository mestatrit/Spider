package cn.vko.business.spider.cookie;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import us.codecraft.webmagic.Site;
import cn.vko.business.spider.login.HttpClientLogin;
import cn.vko.core.common.util.DateTimeUtil;
import cn.vko.core.common.util.Util;

public class TZCookieQueue implements ICookieQueue {

	public CookieStore popCookie(HttpHost proxy) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		Map<String, String> mapValue = null;
		try {
			mapValue = init(httpclient);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", "wenjie20001@sohu.com");
		map.put("password", "4b0c87cd32e7bbf39433556d16637853");
		map.put("token", mapValue.get("token"));
		map.put("page_name", mapValue.get("pname"));
		map.put("ver", String.valueOf(DateTimeUtil.millis()));
		map.put("callback_name", "callback");
		try {
			HttpClientLogin.getAjaxCookie(
					"http://www.tizi.com/login/submit?callback=jQuery18204988396840635687_"
							+ DateTimeUtil.millis(), map,
					"http://www.tizi.com/", httpclient);
			loadFirst(httpclient);
			// loadJs(httpclient);
		} catch (Exception e) {
			return null;
		} finally {
			Util.safeClose(httpclient);
		}
		return cookieStore;
	}

	private Map<String, String> init(CloseableHttpClient httpclient)
			throws URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder.get().setUri(
				new URI("http://www.tizi.com"));
		HttpUriRequest register = requestBuilder.build();
		register.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		register.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		register.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		register.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1897.2 Safari/537.36");
		CloseableHttpResponse response = null;
		Map<String, String> map = null;
		try {
			response = httpclient.execute(register);
			map = getToken2Pname(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
		}
		return map;
	}

	private Map<String, String> getToken2Pname(CloseableHttpResponse response)
			throws IOException, UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		String html = IOUtils.toString(response.getEntity().getContent(),
				"utf-8");

		Pattern pattern = Pattern
				.compile("<span class=\"token\" id=(.*)></span>[\\s\\S]*<span class=\"pname\" id=(.*)></span>");
		Matcher mathcer = pattern.matcher(html);
		if (mathcer.find()) {
			String[] value = mathcer.group(0).split("\"");
			String token = value[3];
			String pname = value[7];
			map.put("token", token);
			map.put("pname", pname);
		}
		return map;
	}

	private void loadFirst(CloseableHttpClient httpclient)
			throws URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder.get().setUri(
				new URI("http://www.tizi.com/teacher/paper/question"));
		HttpUriRequest register = requestBuilder.build();
		register.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		register.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		register.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		register.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1897.2 Safari/537.36");
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(register);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} finally {
			EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
		}
	}

	public void loadJs(CloseableHttpClient httpclient)
			throws URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder
				.get()
				.setUri(new URI(
						"http://www.tizi.com/paper/question/get_question?page=2&nselect=1372&cselect=1372&sid=11&qtype=0&qlevel=0&ver="
								+ DateTimeUtil.millis()));
		HttpUriRequest register = requestBuilder.build();
		register.setHeader(Site.HeaderConst.REFERER,
				"http://www.tizi.com/teacher/paper/question");
		register.setHeader("X-Requested-With", "XMLHttpRequest");
		register.setHeader("Accept",
				"application/json, text/javascript, */*; q=0.01");
		register.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		register.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		register.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1897.2 Safari/537.36");
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(register);
			String html = IOUtils.toString(response.getEntity().getContent(),
					"utf-8");
			System.out.println(html);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} finally {
			EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
		}
	}
}
