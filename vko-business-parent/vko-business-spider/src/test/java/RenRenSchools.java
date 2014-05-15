import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import cn.vko.core.common.util.Util;

public class RenRenSchools {

	@Test
	public void init() throws URISyntaxException {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		try {
			getCookie(httpclient);
			getCookieFocus(httpclient);
			getCookieClick(httpclient);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (Cookie cookie : cookieStore.getCookies()) {
			System.out.println(cookie.getName() + "=====" + cookie.getValue());
		}
		RequestBuilder requestBuilder = RequestBuilder
				.get()
				.setUri(new URI(
						"http://reg.renren.com/fill-basic-info?type=submit?sex=1&birthyear=2008&birthmonth=5&birthday=6&stage=mid"));
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
			String html = IOUtils.toString(response.getEntity().getContent(),
					"utf-8");
			System.out.println(html);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
		}
	}

	private void getCookieClick(CloseableHttpClient httpclient)
			throws URISyntaxException, ClientProtocolException, IOException {
		RequestBuilder requestBuilder = RequestBuilder
				.post()
				.setUri(new URI(
						"http://dj.renren.com/focus?J={\"ID\":null,\"R\":\"http://reg.renren.com/fill-basic-info?type=submit\",\"X\":795,\"Y\":336,\"T\":\"input\"}&t=0.626538563054055"));
		HttpUriRequest login = requestBuilder.build();
		try {
			httpclient.execute(login);
		} finally {
		}

	}

	private void getCookieFocus(CloseableHttpClient httpclient)
			throws URISyntaxException, ClientProtocolException, IOException {
		RequestBuilder requestBuilder = RequestBuilder
				.post()
				.setUri(new URI(
						"http://dj.renren.com/click?J={\"ID\":null,\"R\":\"http://reg.renren.com/fill-basic-info?type=submit\",\"X\":778,\"Y\":381,\"T\":\"div\"}&t=0.626538563054055"));
		HttpUriRequest login = requestBuilder.build();
		try {
			httpclient.execute(login);
		} finally {
		}
	}

	private void getCookie(CloseableHttpClient httpclient)
			throws URISyntaxException, IOException {
		RequestBuilder requestBuilder = RequestBuilder.post().setUri(
				new URI("http://reg.renren.com/fill-basic-info?type=submit"));
		HttpUriRequest login = requestBuilder.build();
		try {
			httpclient.execute(login);
		} finally {
		}
	}
}
