package cn.vko.business.spider.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpUrlToFileTest {

	@Test
	public void testGetFolder() throws Exception {
		HttpUrlToFile hu = new HttpUrlToFile();
		Assert.assertEquals(hu.getFile("http://www.vko.cn/a/b/c.html"),
				"a/b/c.html");
	}

}
