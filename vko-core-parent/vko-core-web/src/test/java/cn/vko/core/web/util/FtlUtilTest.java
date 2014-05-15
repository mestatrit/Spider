package cn.vko.core.web.util;

import org.testng.annotations.Test;

import freemarker.template.Configuration;

public class FtlUtilTest {

	@Test
	public void testInitConfiguration() throws Exception {
		Configuration cfg = new Configuration();
		FtlUtil.initConfiguration(cfg, "webwww/config/", "",
				"webwww/config/ftl.properties");
		System.out.println(FtlUtil.build(cfg, "ftl/1.ftl", null));
	}
}
