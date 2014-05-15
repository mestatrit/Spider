package cn.vko.business.spider.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.testng.annotations.Test;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.login.HttpClientLogin;
import cn.vko.business.spider.model.yitiku.YiTiKuChina;

public class YiTiKuTest {
	@Test
	public void testYiTiKu() throws IOException, URISyntaxException {
		String loginUrl = "http://www.yitiku.cn/Tiku/User/login";
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", "no1yxl@163.com");
		map.put("password", "123321");
		BasicCookieStore cookieStore = HttpClientLogin.getCookie(loginUrl, map);
		List<Cookie> cookies = cookieStore.getCookies();
		Site site = Site.me();
		for (Cookie cookie : cookies) {
			site.addCookie(cookie.getName(), cookie.getValue());
		}
		OOSpider.create(
				site.setDomain("yw.yitiku.cn")
						.setRetryTimes(3)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
				new ConsolePageModelPipeline(), YiTiKuChina.class).test(
				"http://www.yitiku.cn/shiti/769444.html");

	}
}
