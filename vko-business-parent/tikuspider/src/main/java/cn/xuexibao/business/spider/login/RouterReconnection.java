package cn.xuexibao.business.spider.login;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.common.util.DateTimeUtil;
import cn.vko.core.common.util.ThreadUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;
import cn.xuexibao.business.spider.entity.SpiderReRouter;
import cn.xuexibao.business.spider.uucode.ZuoYeBaoRegister;

public class RouterReconnection {
	private static final Logger logger = LoggerFactory
			.getLogger(ZuoYeBaoRegister.class);
	private static final Sql getLastTime = Sqls
			.create("select max(crTime) from spider_rerouter");

	public synchronized static void reconnection(IDbDao dao, String domain) {

		CloseableHttpClient httpclient = HttpClients.custom().build();
		CloseableHttpResponse response = null;
		Timestamp last = ConvertUtil.cast(
				DbSqlUtil.fetchString(dao, getLastTime), Timestamp.class);
		if (last != null && !DateTimeUtil.past(last, 2)) {
			return;
		}
		try {

			HttpUriRequest register = getUri("http://192.168.1.1/router/wan_status_set.cgi?wanid=WAN1&statu=0");

			response = httpclient.execute(register);
			logger.info(response.getEntity().toString());
			SpiderReRouter sr = new SpiderReRouter();
			sr.setCrTime(DateTimeUtil.nowDateTime());
			sr.setDomain(domain);
			dao.insert(sr);
			ThreadUtil.sleep(2000);
		} catch (Exception e) {
			logger.error("to reconnection error", e);
			return;
		} finally {
			EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
			Util.safeClose(httpclient);
		}

	}

	private static HttpUriRequest getUri(String url) throws URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder.get();
		requestBuilder.setUri(new URI(url));
		HttpUriRequest register = requestBuilder.build();
		register.setHeader(
				"User-Agent",
				"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; KB974488)");
		register.setHeader("Accept", "text/html, application/xhtml+xml, */*");
		register.setHeader("Accept-Encoding", "gzip, deflate");
		register.setHeader("Accept-Language", "zh-CN");
		register.setHeader("Authorization", "Basic YWRtaW46NTIxMTExMjU=");
		return register;
	}
}
