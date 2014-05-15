package cn.vko.business.spider.model;

import org.testng.annotations.Test;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;

public class MoFangGeTest {
	@Test
	public void testMoFangGe() {
		OOSpider.create(
				Site.me().setDomain("www.mofangge.com").setRetryTimes(3),
				new ConsolePageModelPipeline(), MoFangGe.class)
				.setUUID("www.mofangge.com")
				.test("http://www.mofangge.com/qlist/zhengzhi/95/",
						"http://www.mofangge.com/html/qDetail/08/g3/201210/7dg4g308164799.html");
	}

	@Test
	public void testZuoYeBao() {
		OOSpider.create(
				Site.me()
						.setDomain("zuoyebao.com")
						.setRetryTimes(3)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36")
						.addCookie("zyb_uid", "112757")
						.addCookie("zyb_ukey", "jaqjfam0bfw4kwcco08cogso08oc")
						.addCookie("Hm_lvt_46967348db34a19ca410b12aaff57687",
								"1394370843,1394700828,1394700870,1394771005")
						.addCookie("Hm_lpvt_46967348db34a19ca410b12aaff57687",
								"1394771009")
						.addCookie("PHPSESSID", "jmifn96s71m7u6tnt1ecvdq146"),
				new ConsolePageModelPipeline(), ZuoYeBao.class)
				.setUUID("www.zuoyebao.com/")
				.test("http://www.zuoyebao.com/s?p=2",
						"http://www.zuoyebao.com/q/1860789");
	}

	@Test
	public void testJyeoo() {
		OOSpider.create(
				Site.me()
						.setDomain("www.jyeoo.com")
						.setRetryTimes(3)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
				new ConsolePageModelPipeline(), Jyeoo.class)
				.setUUID("www.jyeoo.com/")
				.test("http://www.jyeoo.com/math/ques/detail/0330cd84-f596-48c0-8cf7-7c5bf14ead57");
	}
}
