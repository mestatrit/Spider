package cn.xuexibao.business.spider.model;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

public class JsoupTest {
	@Test
	public void getUrl() throws Exception {
		Document doc1 = Jsoup
				.connect(
						"http://www.jyeoo.com/math2/report/search?z=1&y=2013&s=1&o=2&d=1&p=4")
				.data("query", "Java")// 请求参数
				.userAgent("Mozilla")// 设置urer-agent
				.cookie("auth", "token")// 设置cookie
				.timeout(50000)// 设置连接超时
				.post();// 或者改为get ;

		List<Element> urls = doc1.body().select(".R01 a");
		for (Element url : urls) {
			System.out.println(url.attr("href"));
		}
	}
}
