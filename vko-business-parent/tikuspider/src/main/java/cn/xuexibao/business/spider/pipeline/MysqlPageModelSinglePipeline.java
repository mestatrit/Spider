package cn.xuexibao.business.spider.pipeline;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import org.apache.http.impl.client.CloseableHttpClient;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import cn.vko.core.common.util.BeanUtil;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.DateTimeUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;
import cn.xuexibao.business.spider.downloader.HttpClientGenerator;
import cn.xuexibao.business.spider.entity.SpiderExamTran;
import cn.xuexibao.business.spider.entity.SpiderExamVip;
import cn.xuexibao.business.spider.model.IExam;
import cn.xuexibao.business.spider.scheduler.FilterScheduler;
import cn.xuexibao.business.spider.transfer.Transfer;
import cn.xuexibao.business.spider.util.HttpUtil;
import cn.xuexibao.business.spider.util.ImgUrlUtil;

@Data
public class MysqlPageModelSinglePipeline implements IMysqlPipeline<IExam> {
	protected final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();
	protected HttpClientGenerator httpClientGenerator = new HttpClientGenerator();
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MysqlPageModelSinglePipeline.class);
	private IDbDao dao;

	public synchronized void process(IExam t, Task task) {
		if (logger.isInfoEnabled()) {
			logger.info("save url " + t.getUrl()); //$NON-NLS-1$
		}
		try {
			SpiderExamTran se = new SpiderExamTran();
			BeanUtil.copyProperties(t, se);
			se.setDomain(task.getUUID());
			se.setCrTime(DateTimeUtil.nowDateTime());
			if (se.getSolution().indexOf("登录") > -1) {
				SpiderExamVip vip = new SpiderExamVip();
				BeanUtil.copyProperties(t, vip);
				vip.setDomain(task.getUUID());
				vip.setCrTime(DateTimeUtil.nowDateTime());
				dao.insert(vip);
			} else {
				// 下载图片
				List<String> imgList1 = ImgUrlUtil.getJyeooImgUrl(se
						.getContent());
				List<String> imgList2 = ImgUrlUtil.getJyeooImgUrl(se
						.getSolution());
				imgList1.addAll(imgList2);
				Site site = task.getSite();
				CloseableHttpClient httpclient = getHttpClient(site);
				String subject = se.getUrl().split("/")[3];
				if (Character.isDigit(subject.charAt(subject.length() - 1))) {
					subject = subject.substring(0, subject.length() - 1);
				}
				String path = "/data/images" + File.separator + subject;
				// String path = "/data/images";
				if (!Util.isEmpty(imgList1)) {
					HttpUtil.saveUrl(httpclient, path, "", null,
							CollectionUtil.collection2array(imgList1));
				}
				// content
				se.setContent(Transfer.rmEnter(se.getContent()));
				String imgUrl = "img.91xuexibao.com/" + subject;
				// String imgUrl = "img.91xuexibao.com";
				se.setContent(se.getContent().replaceAll("img.jyeoo.net",
						imgUrl));
				se.setContent(se.getContent().replaceAll("菁优网", "学习宝"));
				se.setContent(se.getContent().replaceAll("MathJye", "MathXxb"));
				se.setLatex(Transfer.replaceMathSign(Transfer.transfer(se
						.getContent())));
				se.setAnswer(Transfer.getJyeooRightAnswer(se.getContent()));
				se.setKnowledge(Transfer.getJyeooKnowledge(se.getRemark()));
				// solution
				se.setSolution(Transfer.rmEnter(se.getSolution()));
				se.setSolution(se.getSolution().replaceAll("img.jyeoo.net",
						imgUrl));
				se.setSolution(se.getSolution().replaceAll("菁优网", "学习宝"));
				se.setSolution(se.getSolution()
						.replaceAll("MathJye", "MathXxb"));
				dao.insert(se);
				logger.info("insert success " + t.getUrl());
			}
		} catch (Exception e) {
			logger.error("save url ", e); //$NON-NLS-1$
		}
	}

	public void clear(String uuid) {
		Sql sql = Sqls.create("delete from spider_exam where domain=@uuid");
		sql.params().set("uuid", uuid);
		dao.excute(sql);
	}

	public void addUrl(String uuid, FilterScheduler scheduler) {
		Pager p = new Pager();
		p.setPageSize(1000);
		List<String> s = CollectionUtil.list();

		do {
			Sql sql = Sqls
					.create("select url from  spider_exam_tran_chemistry ");
			// sql.params().set("domain", uuid);
			s = DbSqlUtil.queryStringList(dao, sql, p);
			scheduler.addUrl(uuid, s);
			p.setPageNumber(p.getPageNumber() + 1);
			if (logger.isInfoEnabled()) {
				logger.info("addUrl p=" + p.getPageNumber()); //$NON-NLS-1$
			}

		} while (s.size() == p.getPageSize());
	}

	protected CloseableHttpClient getHttpClient(Site site) {
		if (site == null) {
			return httpClientGenerator.getClient(true, null);
		}
		String domain = site.getDomain();
		CloseableHttpClient httpClient = httpClients.get(domain);
		if (httpClient == null) {
			synchronized (this) {
				httpClient = httpClients.get(domain);
				if (httpClient == null) {
					httpClient = httpClientGenerator.getClient(true, site);
					httpClients.put(domain, httpClient);
				}
			}
		}
		return httpClient;
	}
}
