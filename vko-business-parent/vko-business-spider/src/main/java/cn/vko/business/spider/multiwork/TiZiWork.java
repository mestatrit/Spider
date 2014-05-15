package cn.vko.business.spider.multiwork;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import us.codecraft.webmagic.Site;
import cn.vko.business.spider.MultiOOSpider;
import cn.vko.business.spider.cookie.TZCookieQueue;
import cn.vko.business.spider.downloader.ProxyDownloader;
import cn.vko.business.spider.model.TiZi;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;
import cn.vko.core.common.util.DateTimeUtil;

public class TiZiWork extends MultiWork {

	public TiZiWork(int workThread, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline) {
		super("tizi.com", "tizi.com", workThread, 10, 0, scheduler, pipeline);
		this.setCookieQueue(new TZCookieQueue());
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected MultiOOSpider initSpider(HttpHost hh, CookieStore cs) {
		Site site = Site.me();
		if (cs != null) {
			List<Cookie> cookies = cs.getCookies();
			for (Cookie cookie : cookies) {
				site.addCookie(cookie.getName(), cookie.getValue());
			}
		}
		MultiOOSpider spider = MultiOOSpider
				.create(site
						.addHeader(Site.HeaderConst.REFERER,
								"http://www.tizi.com/teacher/paper/question")
						.addHeader("X-Requested-With", "XMLHttpRequest")
						.addHeader("Accept",
								"application/json, text/javascript, */*; q=0.01")
						.addHeader("Accept-Encoding", "gzip,deflate,sdch")
						.addHeader("Accept-Language", "zh-CN,zh;q=0.8")
						.setDomain(getDomain())
						.setRetryTimes(3)
						.setHttpProxy(hh)
						.setSleepTime(1000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1897.2 Safari/537.36"),
						getPipeline(), TiZi.class);
		spider.setScheduler(scheduler);
		spider.setUUID(uuid);
		spider.setDownloader(new ProxyDownloader());
		return spider;
	}

	@Override
	public void addDownloadUrl() {
		// 数学
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=3&qlevel=1&ver="
									+ DateTimeUtil.millis());
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=3&qlevel=2&ver="
									+ DateTimeUtil.millis());
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=3&qlevel=3&ver="
									+ DateTimeUtil.millis());
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=3&qlevel=4&ver="
									+ DateTimeUtil.millis());
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=3&qlevel=5&ver="
									+ DateTimeUtil.millis());
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=18&qlevel=1&ver="
									+ DateTimeUtil.millis());

			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=18&qlevel=2&ver="
									+ DateTimeUtil.millis());

			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=18&qlevel=3&ver="
									+ DateTimeUtil.millis());

			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=18&qlevel=4&ver="
									+ DateTimeUtil.millis());
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=18&qlevel=5&ver="
									+ DateTimeUtil.millis());

			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=20&qlevel=1&ver="
									+ DateTimeUtil.millis());

			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=20&qlevel=2&ver="
									+ DateTimeUtil.millis());
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=20&qlevel=3&ver="
									+ DateTimeUtil.millis());

			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=20&qlevel=4&ver="
									+ DateTimeUtil.millis());

			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1372&cselect=1372&sid=11&qtype=20&qlevel=5&ver="
									+ DateTimeUtil.millis());

		}
		// 英语
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1570&cselect=1570&sid=12&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 语文
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1316&cselect=1316&sid=10&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 物理
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=589&cselect=589&sid=13&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 化学
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1669&cselect=1669&sid=14&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 生物
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1767&cselect=1767&sid=15&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 政治
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1852&cselect=1852&sid=16&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 历史
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1962&cselect=1962&sid=17&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 地理
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=2145&cselect=2145&sid=18&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 信息
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=18834&cselect=18834&sid=25&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}

		// 初中语
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1&cselect=1&sid=1&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}

		// 数学
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=59&cselect=59&sid=2&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 英语
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=96&cselect=96&sid=3&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 物理
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=196&cselect=196&sid=4&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 化学
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=13717&cselect=13717&sid=5&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}

		// 生物
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=835&cselect=835&sid=6&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 政治
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=957&cselect=957&sid=7&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 历史
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1074&cselect=1074&sid=8&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 地理
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=1243&cselect=1243&sid=9&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 信息技术
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=18832&cselect=18832&sid=24&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}

		// 小学语文
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=18234&cselect=18234&sid=19&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 数学
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=13335&cselect=13335&sid=20&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 英语
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=13472&cselect=13472&sid=21&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
		// 信息
		for (int i = 1; i < 100; i++) {
			scheduler
					.push(uuid,
							"http://www.tizi.com/paper/question/get_question?page="
									+ i
									+ "&nselect=18830&cselect=18830&sid=26&qtype=0&qlevel=0&ver="
									+ DateTimeUtil.millis());
		}
	}
}
