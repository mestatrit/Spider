package cn.xuexibao.business.spider.model;

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
import cn.xuexibao.business.spider.login.HttpClientLogin;

@Test
public class HttpClintLoginTest {

	@Test
	public void testZuoYeBao() throws IOException, URISyntaxException {
		String loginUrl = "http://www.zuoyebao.com/du/login";
		Map<String, String> map = new HashMap<String, String>();
		map.put("uEmail", "no1yxls@163.com");
		map.put("uPasswd", "zyb123");
		// cookie 中针对同一个账号登陆的下面两个值不变
		// zyb_uid=121963
		// zyb_ukey=6hmh0tjv704c8ckw80so840o0g44
		BasicCookieStore cookieStore = HttpClientLogin.getCookie(loginUrl, map);
		List<Cookie> cookies = cookieStore.getCookies();
		Site site = Site.me();
		site.setDomain("zuoyebao.com");
		for (Cookie cookie : cookies) {
			site.addCookie(cookie.getName(), cookie.getValue());
		}
		OOSpider.create(
				site.setRetryTimes(3)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
				new ConsolePageModelPipeline(), ZuoYeBao.class)
				.setUUID("www.zuoyebao.com/")
				.test("http://www.zuoyebao.com/s?p=2",
						"http://www.zuoyebao.com/q/1860789");
	}

	@Test
	public void testJyeoo() throws IOException, URISyntaxException {
		String loginUrl = "http://www.jyeoo.com/account/loginform";
		Map<String, String> map = new HashMap<String, String>();
		map.put("Email", "no1yxls@163.com");
		map.put("Password", "zyb123");
		map.put("Captcha", "10");
		map.put("Remember", "true");
		map.put("Ver", "true");
		map.put("AnonID", "e89d95aa-dbfa-450b-94d4-b07d562f5650");
		BasicCookieStore cookieStore = HttpClientLogin.getCookie(loginUrl, map);
		List<Cookie> cookies = cookieStore.getCookies();
		Site site = Site.me();
		for (Cookie cookie : cookies) {
			site.addCookie(cookie.getName(), cookie.getValue());
		}
		OOSpider.create(
				site.setDomain("jyeoo.com")
						.setRetryTimes(3)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36"),
				new ConsolePageModelPipeline(), Jyeoo.class)
				.setUUID("www.jyeoo.com/")
				.test("http://www.jyeoo.com/math3/training/openexam?a=e06f4e1d-55f4-4b00-912a-0254de3f07a3&r=0.06268329941667616");
	}

	@Test
	public void testMoFangGe() {
		OOSpider.create(
				Site.me().setDomain("www.mofangge.com").setRetryTimes(3),
				new ConsolePageModelPipeline(), MoFangGe.class)
				.setUUID("www.mofangge.com")
				.test("http://www.mofangge.com/qlist/zhengzhi/95/",
						"http://www.mofangge.com/html/qDetail/08/g3/201210/7dg4g308164799.html");
	}
}
