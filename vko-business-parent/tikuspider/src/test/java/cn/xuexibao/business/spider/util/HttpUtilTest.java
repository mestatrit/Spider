package cn.xuexibao.business.spider.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.annotations.Test;

public class HttpUtilTest {

	@Test
	public void testSaveUrl() throws Exception {
		CloseableHttpClient httpclient = HttpClients.custom().build();
		HttpUtil.saveUrl(httpclient, "d:/test", "http://www.mofangge.com",
				null,
				"http://pic2.mofangge.com/upload/papers//20130926/201309260016506683071.png");
	}

}
