package cn.xuexibao.business.spider.proxy;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.common.util.Util;

public class ProxyUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ProxyUtil.class);
	private final static List<Integer> ACCEPT_STATUSCODE = CollectionUtil
			.list(200);
	private final static String FETCH_URL = "http://api.iphai.com/getapi.ashx?ddh=575508240831742&yys=1&am=0&guolv=y&mt=6&fm=text&num=";
	// private final static String FETCH_URL =
	// "http://www.56pu.com/api?orderId=613603760511213&line=all&region=&regionEx=&beginWith=&ports=&vport=&speed=&anonymity=&scheme=&duplicate=3&quantity=";
	private final static Pattern IP = Pattern.compile("([0-9|\\.]*:[0-9]*)");

	public static List<HttpHost> downloadProxy(int num) {
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpGet get = new HttpGet(FETCH_URL + num);
		String responseString = null;
		List<HttpHost> result = CollectionUtil.list();
		try {
			Builder b = RequestConfig.custom();
			b.setConnectTimeout(3000);
			b.setSocketTimeout(3000);
			b.setConnectionRequestTimeout(3000);
			get.setConfig(b.build());
			response = client.execute(get);
			HttpEntity entity = response.getEntity(); // 返回服务器响应
			if (entity == null) {
				logger.error("proxy fetch response error response is null!");
				return result;
			}
			responseString = EntityUtils.toString(entity); // 返回服务器响应的HTML代码
			if (Util.isEmpty(responseString) || responseString.indexOf(":") < 0) {
				logger.error("proxy fetch response error " + responseString);
				return result;
			}
			logger.info("fetch proxy \r\n" + responseString); //$NON-NLS-1$
			Matcher m = IP.matcher(responseString);
			while (m.find()) {
				String one = m.group(0);
				String[] spilts = one.split(":");
				final HttpHost hh = new HttpHost(spilts[0],
						ConvertUtil.obj2int(spilts[1]));
				result.add(hh);
			}
		} catch (ClientProtocolException e) {
			logger.error("proxy fetch url error:" + FETCH_URL);
		} catch (IOException e) {
			get.abort();
			logger.error("proxy fetch io error:" + FETCH_URL);
		} finally {
			if (response != null)
				EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
			Util.safeClose(client);
		}
		return result;
	}

	public static boolean isCanUse(HttpHost hh, String checkUrl) {
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpGet get = new HttpGet(checkUrl);
		try {
			get.setHeader(
					"User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; KB974488)");
			get.setHeader("Accept", "text/html, application/xhtml+xml, */*");
			get.setHeader("Accept-Encoding", "gzip, deflate");
			get.setHeader("Accept-Language", "zh-CN");
			get.setHeader("Connection", "close");
			Builder b = RequestConfig.custom();
			b.setProxy(hh);
			b.setConnectTimeout(3000);
			b.setSocketTimeout(3000);
			b.setConnectionRequestTimeout(3000);
			get.setConfig(b.build());
			response = client.execute(get);
			if (ACCEPT_STATUSCODE.contains(response.getStatusLine()
					.getStatusCode())) {
				logger.info("find one ok proxy" + hh.toHostString());
				return true;
			}
			logger.info("find one fail proxy" + hh.toHostString());

			return false;
		} catch (ClientProtocolException e) {
			logger.error("is canuse Protocol error" + hh.toHostString());
			return false;
		} catch (IOException e) {
			logger.error("is canuse io error" + hh.toHostString());
			get.abort();
			return false;
		} finally {
			if (response != null)
				EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
			Util.safeClose(client);
		}
	}
}
