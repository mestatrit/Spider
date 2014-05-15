package cn.vko.business.spider.downloader;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.business.spider.util.HttpUtil;
import cn.vko.business.spider.util.ImgUrlUtil;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;

public class ImgDownloader {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ImgDownloader.class);

	private IDbDao dao;
	private String refer;

	public ImgDownloader(IDbDao dbDao) {
		this.dao = dbDao;
	}

	public ImgDownloader() {
	}

	public void download(String folder, String select)
			throws URISyntaxException {
		if (logger.isDebugEnabled()) {
			logger.debug("download(String, String) - start"); //$NON-NLS-1$
		}

		Pager p = new Pager();
		p.setPageSize(1000);
		List<String> s = CollectionUtil.list();
		CloseableHttpClient httpclient = HttpClients.custom().build();
		do {
			Sql sql = Sqls.create(select);
			s = DbSqlUtil.queryStringList(dao, sql, p);
			if (logger.isInfoEnabled()) {
				logger.info("down img url p=" + p.getPageNumber()); //$NON-NLS-1$
			}
			p.setPageNumber(p.getPageNumber() + 1);
			List<String> imgs = getImgUrl(s);
			try {
				HttpUtil.saveUrl(httpclient, folder, refer, null,
						CollectionUtil.collection2array(imgs));
			} catch (Exception e) {
				logger.info("imgs is " + imgs);
			}
		} while (s.size() == p.getPageSize());

		if (logger.isDebugEnabled()) {
			logger.debug("download(String, String) - end"); //$NON-NLS-1$
		}
	}

	public static List<String> getImgUrl(List<String> contents) {
		if (logger.isDebugEnabled()) {
			logger.debug("getImgUrl(List<String>) - start"); //$NON-NLS-1$
		}

		List<String> imgs = CollectionUtil.list();
		if (Util.isEmpty(contents)) {
			if (logger.isDebugEnabled()) {
				logger.debug("getImgUrl(List<String>) - end"); //$NON-NLS-1$
			}
			return imgs;
		}
		for (String content : contents) {
			List<String> imgList = ImgUrlUtil.getJyeooImgUrl(content);
			imgs.addAll(imgList);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getImgUrl(List<String>) - end"); //$NON-NLS-1$
		}
		return imgs;
	}
}
