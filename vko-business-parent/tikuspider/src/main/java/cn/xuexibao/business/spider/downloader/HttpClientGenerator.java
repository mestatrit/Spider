package cn.xuexibao.business.spider.downloader;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import us.codecraft.webmagic.Site;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.4.0
 */
public class HttpClientGenerator {

	private PoolingHttpClientConnectionManager connectionManager;

	public HttpClientGenerator() {
		Registry<ConnectionSocketFactory> reg = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https",
						SSLConnectionSocketFactory.getSocketFactory()).build();
		connectionManager = new PoolingHttpClientConnectionManager(reg);
		connectionManager.setDefaultMaxPerRoute(200);
	}

	public HttpClientGenerator setPoolSize(int poolSize) {
		connectionManager.setMaxTotal(poolSize);
		return this;
	}

	public CloseableHttpClient getClient(boolean isKeepAlive, Site site) {
		return generateClient(isKeepAlive, site);
	}

	private CloseableHttpClient generateClient(boolean isKeepAlive, Site site) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager);
		if (isKeepAlive) {
			httpClientBuilder.setKeepAliveStrategy(myStrategy);
		}
		if (site != null && site.getUserAgent() != null) {
			httpClientBuilder.setUserAgent(site.getUserAgent());
		} else {
			httpClientBuilder.setUserAgent("");
		}
		if (site == null || site.isUseGzip()) {
			httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

				public void process(final HttpRequest request,
						final HttpContext context) throws HttpException,
						IOException {
					if (!request.containsHeader("Accept-Encoding")) {
						request.addHeader("Accept-Encoding", "gzip");
					}

				}
			});
		}
		SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true)
				.setTcpNoDelay(true).build();
		httpClientBuilder.setDefaultSocketConfig(socketConfig);
		if (site != null) {
			httpClientBuilder
					.setRetryHandler(new DefaultHttpRequestRetryHandler(site
							.getRetryTimes(), true));
		}
		generateCookie(httpClientBuilder, site);
		return httpClientBuilder.build();
	}

	private void generateCookie(HttpClientBuilder httpClientBuilder, Site site) {
		CookieStore cookieStore = new BasicCookieStore();
		for (Map.Entry<String, String> cookieEntry : site.getCookies()
				.entrySet()) {
			BasicClientCookie cookie = new BasicClientCookie(
					cookieEntry.getKey(), cookieEntry.getValue());
			cookie.setDomain(site.getDomain());
			cookieStore.addCookie(cookie);
		}
		for (Map.Entry<String, Map<String, String>> domainEntry : site
				.getAllCookies().entrySet()) {
			for (Map.Entry<String, String> cookieEntry : domainEntry.getValue()
					.entrySet()) {
				BasicClientCookie cookie = new BasicClientCookie(
						cookieEntry.getKey(), cookieEntry.getValue());
				cookie.setDomain(domainEntry.getKey());
				cookieStore.addCookie(cookie);
			}
		}
		httpClientBuilder.setDefaultCookieStore(cookieStore);
	}

	ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {

		public long getKeepAliveDuration(HttpResponse response,
				HttpContext context) {
			// Honor 'keep-alive' header
			HeaderElementIterator it = new BasicHeaderElementIterator(
					response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement he = it.nextElement();
				String param = he.getName();
				String value = he.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					try {
						return Long.parseLong(value) * 1000;
					} catch (NumberFormatException ignore) {
					}
				}
			}
			HttpHost target = (HttpHost) context
					.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
			if ("www.jyeoo.com".equalsIgnoreCase(target.getHostName())) {
				// Keep alive for 5 seconds only
				return 5 * 1000;
			} else {
				// otherwise keep alive for 30 seconds
				return 30 * 1000;
			}
		}

	};
}
