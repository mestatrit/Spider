package cn.xuexibao.business.spider.downloader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.common.util.RandomUtil;
import cn.vko.core.common.util.Util;

public class ProxyPool {
	private final static String FETCH_URL = "http://api.iphai.com/getapi.ashx?ddh=575508240831742&yys=1&am=0&guolv=y&mt=6&fm=text&num=";
	public static String CHECK_URL = "http://www.jyeoo.com";
	private final static Pattern IP = Pattern.compile("([0-9|\\.]*:[0-9]*)");
	private final static Map<HttpHost, Integer> PROXY = Collections
			.synchronizedMap(new HashMap<HttpHost, Integer>(10));
	public static int POOLSIZE = 1;
	private final static List<Integer> ACCEPT_STATUSCODE = CollectionUtil
			.list(200);
	private final static int MAX_ERROR = 5;
	private static int NO_PROXY_ERROR = 0;
	private static final Executor EX = Executors
			.newFixedThreadPool(POOLSIZE / 5 + 1);
	static {
		// Timer timer = new Timer();
		// timer.schedule(new TimerTask() {
		// @Override
		// public void run() {
		// checkPool();
		// }
		// }, 30000);
	}
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ProxyPool.class);

	public synchronized static void fetchProxy() {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchProxy() - start"); //$NON-NLS-1$
		}
		if (PROXY.size() >= POOLSIZE) {
			return;
		}
		String responseString = downloadProxy();
		Matcher m = IP.matcher(responseString);
		while (m.find()) {
			String one = m.group(0);
			String[] spilts = one.split(":");
			final HttpHost hh = new HttpHost(spilts[0],
					ConvertUtil.obj2int(spilts[1]));
			EX.execute(new Runnable() {
				public void run() {
					if (isCanUse(hh)) {
						PROXY.put(hh, 0);
					}
				}
			});
		}
		if (logger.isDebugEnabled()) {
			logger.debug("fetchProxy() - end"); //$NON-NLS-1$
		}
		logger.info("fetch proxy one time! now size" + PROXY.size());
	}

	public synchronized static void fetchProxyBlock() {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchProxy() - start"); //$NON-NLS-1$
		}
		if (PROXY.size() >= POOLSIZE) {
			return;
		}
		String responseString = downloadProxy();
		Matcher m = IP.matcher(responseString);
		while (m.find()) {
			String one = m.group(0);
			String[] spilts = one.split(":");
			final HttpHost hh = new HttpHost(spilts[0],
					ConvertUtil.obj2int(spilts[1]));
			if (isCanUse(hh)) {
				PROXY.put(hh, 0);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("fetchProxy() - end"); //$NON-NLS-1$
		}
		logger.info("fetch proxy one time! now size" + PROXY.size());
	}

	private static String downloadProxy() {
		int num = (POOLSIZE - PROXY.size()) * 2;
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpGet get = new HttpGet(FETCH_URL + num);
		String responseString = null;
		try {
			Builder b = RequestConfig.custom();
			b.setConnectTimeout(3000);
			b.setSocketTimeout(3000);
			b.setConnectionRequestTimeout(3000);
			get.setConfig(b.build());
			response = client.execute(get);
			HttpEntity entity = response.getEntity(); // 返回服务器响应

			if (entity == null) {
				throw ExceptionUtil.bEx("proxy fetch response null error ");
			}
			responseString = EntityUtils.toString(entity); // 返回服务器响应的HTML代码
			if (Util.isEmpty(responseString) || responseString.indexOf(":") < 0) {
				throw ExceptionUtil.bEx("proxy fetch response error "
						+ responseString);
			}

			logger.info("fetch proxy \r\n" + responseString); //$NON-NLS-1$

		} catch (ClientProtocolException e) {
			throw ExceptionUtil.bEx("proxy fetch url error", e);
		} catch (IOException e) {
			get.abort();
			throw ExceptionUtil.bEx("proxy fetch io error", e);
		} finally {
			if (response != null)
				EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
			Util.safeClose(client);
		}
		return responseString;
	}

	public static void repeatFetchProxy() {
		while (PROXY.size() < POOLSIZE) {
			fetchProxy();
		}
	}

	public static void checkPool() {
		if (logger.isDebugEnabled()) {
			logger.debug("checkPool() - start"); //$NON-NLS-1$
		}

		if (Util.isEmpty(PROXY)) {
			repeatFetchProxy();

			if (logger.isDebugEnabled()) {
				logger.debug("checkPool() - end"); //$NON-NLS-1$
			}
			return;
		}
		Iterator<Entry<HttpHost, Integer>> it = PROXY.entrySet().iterator();
		while (it.hasNext()) {
			Entry<HttpHost, Integer> en = it.next();
			if (en.getValue() >= MAX_ERROR) {
				it.remove();
				continue;
			}
			if (isCanUse(en.getKey())) {
				continue;
			}
			en.setValue(en.getValue() + 1);
			if (en.getValue() >= MAX_ERROR) {
				it.remove();
				continue;
			}
		}
		repeatFetchProxy();

		if (logger.isDebugEnabled()) {
			logger.debug("checkPool() - end"); //$NON-NLS-1$
		}
	}

	public static boolean isCanUse(HttpHost hh) {
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpGet get = new HttpGet(CHECK_URL);
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

	public static HttpHost getHost() {
		if (NO_PROXY_ERROR < MAX_ERROR) {
			return null;
		}
		if (Util.isEmpty(PROXY)) {
			repeatFetchProxy();
		}
		return RandomUtil.random(PROXY.keySet());
	}

	public static void hostError(HttpHost host) {
		if (host == null) {
			logger.info("local ip is error");
			NO_PROXY_ERROR++;
			return;
		}
		logger.info("proxy ip is error" + host.toHostString());
		if (PROXY.containsKey(host)) {
			PROXY.put(host, PROXY.get(host) + 1);
			if (PROXY.get(host) >= MAX_ERROR) {
				PROXY.remove(host);
			}
		} else {
			PROXY.put(host, 1);
		}
	}

	public static synchronized HttpHost popHost() {
		if (Util.isEmpty(PROXY)) {
			fetchProxyBlock();
		}
		if (Util.isEmpty(PROXY)) {
			logger.info("proxy ip is poped local ");
			return null;
		}
		HttpHost one = PROXY.keySet().iterator().next();
		PROXY.remove(one);
		logger.info("proxy ip is poped " + one.toHostString());
		return one;
	}
}
