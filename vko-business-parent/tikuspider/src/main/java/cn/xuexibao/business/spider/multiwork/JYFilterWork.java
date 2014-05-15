package cn.xuexibao.business.spider.multiwork;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Site;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;
import cn.xuexibao.business.spider.MultiOOSpider;
import cn.xuexibao.business.spider.downloader.ProxyDownloader;
import cn.xuexibao.business.spider.model.JyeooFilter;
import cn.xuexibao.business.spider.pipeline.IMysqlPipeline;
import cn.xuexibao.business.spider.proxy.ProxyQueue;
import cn.xuexibao.business.spider.scheduler.FilterScheduler;

public class JYFilterWork extends MultiWork {
	private static final Logger logger = LoggerFactory
			.getLogger(JYFilterWork.class);
	private IDbDao dao;

	public JYFilterWork(int workThread, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline, IDbDao dao) {
		super("fit.jyeoo.com", "jyeoo.com", workThread, 10, 0, scheduler,
				pipeline);
		this.dao = dao;
		ProxyQueue pq = new ProxyQueue();
		pq.setFetchNum(3);
		pq.setCheckUrl("http://www.jyeoo.com");
		this.setProxyQueue(pq);
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
								"http://www.jyeoo.com")
						.setDomain(getDomain())
						.setRetryTimes(3)
						.setHttpProxy(hh)
						.setSleepTime(2000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), JyeooFilter.class);
		spider.setScheduler(scheduler);
		spider.setUUID(uuid);
		spider.setDownloader(new ProxyDownloader());
		return spider;
	}

	public void addDownloadUrl() {
		this.addDownloadUrlOnce();
	}

	public void addDownloadUrlOnce() {
		Pager p = new Pager();
		p.setPageSize(1000);
		List<String> s = CollectionUtil.list();
		do {
			Sql sql = Sqls
					.create("select url from spider_exam where domain=@domain and solution like '%登录菁优网%' and subject like'%数学%' and url not in (select  url from  spider_exam_415 )");
			sql.params().set("domain", "fit.jyeoo.com");
			s = DbSqlUtil.queryStringList(dao, sql, p);
			scheduler.addDownloadUrl(uuid, s);
			p.setPageNumber(p.getPageNumber() + 1);
			if (logger.isInfoEnabled()) {
				logger.info("addDownloadUrl p=" + p.getPageNumber()); //$NON-NLS-1$
			}

		} while (s.size() == p.getPageSize());
	}
}
