package cn.vko.business.spider.work.yitiku;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.BlockOOSpider;
import cn.vko.business.spider.login.HttpClientLogin;
import cn.vko.business.spider.model.yitiku.YiTiKuEnglish;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;
import cn.vko.business.spider.work.YiTiKuWork;

public class YiTiKuEnglishWork extends YiTiKuWork {

	public YiTiKuEnglishWork(FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline, boolean useProxy) {
		super("en.yitiku.cn", "yitiku.cn", scheduler, pipeline, useProxy);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void addUrl(OOSpider spider) {
		spider.addUrl("http://www.yitiku.cn/tiku/yingyu");
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected OOSpider createSpider(CookieStore cookieStore) throws Exception {
		Site site = Site.me();
		if (cookieStore != null) {
			List<Cookie> cookies = cookieStore.getCookies();
			for (Cookie cookie : cookies) {
				site.addCookie(cookie.getName(), cookie.getValue());
			}
		}
		OOSpider spider = BlockOOSpider
				.create(site
						.addHeader("Connection", "close")
						.addHeader(Site.HeaderConst.REFERER,
								"http://www.yitiku.cn")
						.setDomain(getDomain())
						.setRetryTimes(5)
						.setSleepTime(1000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), YiTiKuEnglish.class);

		return spider;
	}

	@Override
	protected CookieStore getCookie(HttpHost proxy) {
		String loginUrl = "http://www.yitiku.cn/Tiku/User/login";
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", "894204994@qq.com");
		map.put("password", "894204994");
		try {
			BasicCookieStore cookieStore = HttpClientLogin.getCookie(loginUrl,
					map, proxy);
			return cookieStore;
		} catch (Exception e) {
			return null;
		}
	}
}
