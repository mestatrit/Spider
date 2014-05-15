package cn.xuexibao.business.spider.downloader;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;

public class ImgDownloaderTest {
	private IDbDao dbDao;
	private NutDao nutDao;
	private Ioc ioc;
	private static final Logger logger = LoggerFactory
			.getLogger(ImgDownloader.class);

	@Test
	public void testDownload() throws Exception {
		ioc = new NutIoc(new JsonLoader("spider/config/pipeline.js"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		String select = "select solution from spider_exam_jym_420";
		ImgDownloader imgDownloader = new ImgDownloader(dbDao);
		imgDownloader.download("d:\\dealImage/420imgs", select);
	}
}
