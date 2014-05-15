package cn.vko.business.spider.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.nutz.lang.Streams;

import cn.vko.core.common.util.EncodeUtil;
import cn.vko.core.common.util.FileUtil;
import cn.vko.core.common.util.Util;

public class HttpUtil {
	public static HttpUriRequest getUri(String url,
			final Map<String, String> param, boolean post)
			throws URISyntaxException {
		RequestBuilder requestBuilder = null;
		if (post) {
			requestBuilder = RequestBuilder.post();
		} else {
			requestBuilder = RequestBuilder.get();
		}
		StringBuilder sb = new StringBuilder(url);

		if (!Util.isEmpty(param)) {
			if (!post) {
				sb.append("?");
				for (Entry<String, String> entry : param.entrySet()) {
					sb.append(entry.getKey()).append("=")
							.append(EncodeUtil.urlEncode(entry.getValue()))
							.append("&");
				}
			} else {
				for (Entry<String, String> entry : param.entrySet()) {
					requestBuilder.addParameter(entry.getKey(),
							entry.getValue());
				}
			}
		}
		requestBuilder.setUri(new URI(sb.toString()));
		Builder b = RequestConfig.custom();
		b.setConnectTimeout(50000);
		b.setSocketTimeout(50000);
		b.setConnectionRequestTimeout(50000);
		requestBuilder.setConfig(b.build());

		HttpUriRequest uri = requestBuilder.build();
		uri.setHeader(
				"User-Agent",
				"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; KB974488)");
		uri.setHeader("Accept", "text/html, application/xhtml+xml, */*");
		uri.setHeader("Accept-Encoding", "gzip, deflate");
		uri.setHeader("Accept-Language", "zh-CN");
		return uri;
	}

	public static void getByte(CloseableHttpClient httpClient,
			HttpUriRequest uri, OutputStream output) {
		InputStream input = null;
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(uri);
			input = response.getEntity().getContent();
			byte b[] = new byte[1024];
			int j = 0;
			while ((j = input.read(b)) != -1) {
				output.write(b, 0, j);
			}
			output.flush();
		} catch (Exception e) {
		} finally {
			Streams.safeClose(input);
			Streams.safeClose(output);
			if (response != null) {
				EntityUtils.consumeQuietly(response.getEntity());
				Util.safeClose(response);
			}
		}
	}

	public static void saveUrl(CloseableHttpClient httpClient, String folder,
			String refer, IUrlToFile urlToFile, String... urls)
			throws URISyntaxException {
		if (urlToFile == null) {
			urlToFile = new HttpUrlToFile();
		}
		for (String url : urls) {
			// if (url.indexOf("upload") > -1 || url.indexOf("img") > -1) {
			HttpUriRequest uri = getUri(url, null, false);
			FileOutputStream fo = null;
			try {
				File save = new File(folder + File.separator
						+ urlToFile.getFile(url));
				if (!FileUtil.isExist(save.getPath())) {
					FileUtil.createNewFile(save);
					fo = new FileOutputStream(save);
					getByte(httpClient, uri, fo);
					fo.flush();
				}
			} catch (Exception e) {

			} finally {
				Util.safeClose(fo);
			}
			// }
		}
	}
}
