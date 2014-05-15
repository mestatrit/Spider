package cn.vko.business.spider.downloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.utils.UrlUtils;
import cn.vko.business.spider.IpFibbidenException;
import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.common.util.Util;

import com.google.common.collect.Sets;

@ThreadSafe
public class ProxyDownloader extends HttpClientDownloader {
	/**
	 * Logger for this class
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger(ProxyDownloader.class);
	protected final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();
	protected HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

	public ProxyDownloader() {
	}

	@Override
	public Page download(Request request, Task task) {
		Site site = null;
		if (task != null) {
			site = task.getSite();
		}
		Set<Integer> acceptStatCode;
		String charset = null;

		Map<String, String> headers = null;
		if (site != null) {
			acceptStatCode = site.getAcceptStatCode();
			charset = site.getCharset();
			headers = site.getHeaders();
		} else {
			acceptStatCode = Sets.newHashSet(200);
		}
		if (logger.isInfoEnabled()) {
			logger.info("downloading page " + request.getUrl()); //$NON-NLS-1$
		}
		RequestBuilder requestBuilder = RequestBuilder.get().setUri(
				request.getUrl());
		if (headers != null) {
			for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
				requestBuilder.addHeader(headerEntry.getKey(),
						headerEntry.getValue());
			}
		}
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
				.setConnectionRequestTimeout(site.getTimeOut())
				.setSocketTimeout(site.getTimeOut())
				.setConnectTimeout(site.getTimeOut())
				.setCookieSpec(CookieSpecs.BEST_MATCH);
		HttpHost proxy = null;
		if (site != null && site.getHttpProxy() != null) {
			proxy = site.getHttpProxy();
			requestConfigBuilder.setProxy(proxy);
		}

		requestBuilder.setConfig(requestConfigBuilder.build());
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = getHttpClient(site).execute(requestBuilder.build());
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (acceptStatCode.contains(statusCode)) {
				// charset
				if (charset == null) {
					String value = httpResponse.getEntity().getContentType()
							.getValue();
					charset = UrlUtils.getCharset(value);
				}
				return handleResponse(request, charset, httpResponse, task);
			} else {
				if (statusCode == 403 || statusCode == 500) {
					return handleIpError("code error " + statusCode + "\t"
							+ request.getUrl(), request, site, null, proxy);
				} else {
					return handleError("code error " + statusCode + "\t"
							+ request.getUrl(), request, site, null, proxy);
				}
			}
		} catch (ConnectionPoolTimeoutException e) {
			addToCycleRetry(request, site, true);
			throw ExceptionUtil.bEx("download err", e);
		} catch (IOException e) {
			return handleError("download page " + request.getUrl() + " error",
					request, site, e, proxy);
		} finally {
			if (httpResponse != null) {
				// ensure the connection is released back to pool
				EntityUtils.consumeQuietly(httpResponse.getEntity());
			}
			Util.safeClose(httpResponse);
		}
	}

	protected Page handleError(String errorMsg, Request request, Site site,
			Exception e, HttpHost proxy) {
		addToCycleRetry(request, site, false);
		throw ExceptionUtil.bEx("download err", e);
	}

	protected Page handleIpError(String errorMsg, Request request, Site site,
			Exception e, HttpHost proxy) {
		addToCycleRetry(request, site, false);
		throw new IpFibbidenException("download err", e);
	}

	protected Page addToCycleRetry(Request request, Site site, boolean notLimit) {
		if (site.getCycleRetryTimes() <= 0) {
			return null;
		}
		Page page = new Page();
		Object cycleTriedTimesObject = request
				.getExtra(Request.CYCLE_TRIED_TIMES);

		if (cycleTriedTimesObject == null) {
			cycleTriedTimesObject = Integer.valueOf(0);
		}
		int cycleTriedTimes = (Integer) cycleTriedTimesObject;
		if (notLimit) {
			page.addTargetRequest(request.setPriority(0).putExtra(
					Request.CYCLE_TRIED_TIMES, cycleTriedTimes));

			return page;
		}
		cycleTriedTimes++;
		if (cycleTriedTimes >= site.getCycleRetryTimes()) {
			return null;
		}
		page.addTargetRequest(request.setPriority(0).putExtra(
				Request.CYCLE_TRIED_TIMES, 1));

		return page;
	}

	protected CloseableHttpClient getHttpClient(Site site) {
		if (site == null) {
			return httpClientGenerator.getClient(null);
		}
		String domain = site.getDomain();
		CloseableHttpClient httpClient = httpClients.get(domain);
		if (httpClient == null) {
			synchronized (this) {
				httpClient = httpClients.get(domain);
				if (httpClient == null) {
					httpClient = httpClientGenerator.getClient(site);
					httpClients.put(domain, httpClient);
				}
			}
		}
		return httpClient;
	}

	@Override
	public void setThread(int thread) {
		httpClientGenerator.setPoolSize(thread);
	}
}
