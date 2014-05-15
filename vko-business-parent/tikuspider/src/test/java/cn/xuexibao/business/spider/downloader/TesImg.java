package cn.xuexibao.business.spider.downloader;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;
import cn.vko.core.db.util.DbSqlUtil;
import cn.xuexibao.business.spider.util.HttpUtil;
import cn.xuexibao.business.spider.util.ImgUrlUtil;

public class TesImg {
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
		String select = "select content from spider_exam_tran_physics_auto where id ='224'";
		Sql sql = Sqls.create(select);
		List<String> imgs = DbSqlUtil.queryStringList(dbDao, sql, null);
		List<String> imgList1 = ImgUrlUtil.getJyeooImgUrl(imgs.get(0));
		List<String> imgList2 = new ArrayList<String>();
		CloseableHttpClient httpclient = HttpClients.custom().build();
		for (String url : imgList1) {
			imgList2.add(url.replaceAll("img.91xuexibao.com/physics",
					"img.jyeoo.net"));
		}
		if (!Util.isEmpty(imgList1)) {
			HttpUtil.saveUrl(httpclient, "d:\\data/test", "", null,
					CollectionUtil.collection2array(imgList2));
		}
	}
}
