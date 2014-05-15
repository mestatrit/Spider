package cn.xuexibao.business.spider.downloader;

import java.io.IOException;
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
import us.codecraft.webmagic.utils.UrlUtils;
import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.common.util.Util;
import cn.xuexibao.business.spider.uucode.ZuoYeBaoRegister;

import com.google.common.collect.Sets;

@ThreadSafe
public class ZybDownloader extends ProxyDownloader {
	private static final String URL_FMT = "http://www.zuoyebao.com/q/detail?qId={0}&captcha={1}";
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ZybDownloader.class);

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
		RequestBuilder requestBuilder = RequestBuilder.get();
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
		CloseableHttpClient client = getHttpClient(site);
		try {
			String code = ZuoYeBaoRegister.getCode(client);
			if (code == null) {
				return handleError(
						"get verfiy code error \t" + request.getUrl(), request,
						site, null, proxy);
			}
			requestBuilder.setUri("");
			httpResponse = client.execute(requestBuilder.build());
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

	@Override
	protected CloseableHttpClient getHttpClient(Site site) {
		if (site == null) {
			return httpClientGenerator.getClient(false, null);
		}
		return httpClientGenerator.getClient(false, site);
	}
}
