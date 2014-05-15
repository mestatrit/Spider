package cn.vko.business.spider.work;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.login.HttpClientLogin;
import cn.vko.business.spider.model.ZuoYeBao;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;

public class ZuoYeBaoWork extends BaseWork {

	public ZuoYeBaoWork(FilterScheduler scheduler, IMysqlPipeline<?> pipeline,
			boolean useProxy) {
		super("zuoyebao.com", scheduler, pipeline, useProxy);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected OOSpider createSpider() throws Exception {
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
		for (Cookie cookie : cookies) {
			site.addCookie(cookie.getName(), cookie.getValue());
		}
		OOSpider spider = OOSpider
				.create(site
						.addHeader("Connection", "close")
						.addHeader(Site.HeaderConst.REFERER,
								"http://www.zuoyebao.com")
						.setDomain(getDomain())
						.setRetryTimes(3)
						.setSleepTime(3000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), ZuoYeBao.class);

		return spider;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void addUrl(OOSpider spider) {
		spider.addUrl("http://www.zuoyebao.com/s");
	}
}
