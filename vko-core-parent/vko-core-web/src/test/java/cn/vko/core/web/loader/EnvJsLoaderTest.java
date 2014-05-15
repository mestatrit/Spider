package cn.vko.core.web.loader;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EnvJsLoaderTest {

	@Test
	public void testEnvJsLoader() throws Exception {
		EnvJsLoader en = new EnvJsLoader("webwww/config");
		Assert.assertTrue(en.has("env"));
	}

}
