package cn.xuexibao.business.spider.downloader;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;
import cn.xuexibao.business.spider.util.HttpUtil;
import cn.xuexibao.business.spider.util.ImgUrlUtil;

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

	public void download(String folder, String select)
			throws URISyntaxException {
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
			HttpUtil.saveUrl(httpclient, folder, refer, null,
					CollectionUtil.collection2array(imgs));
		} while (s.size() == p.getPageSize());
	}

	public static List<String> getImgUrl(List<String> contents) {
		List<String> imgs = CollectionUtil.list();
		if (Util.isEmpty(contents)) {
			return imgs;
		}
		for (String content : contents) {
			List<String> imgList = ImgUrlUtil.getJyeooImgUrl(content);
			imgs.addAll(imgList);
		}
		return imgs;
	}
}
