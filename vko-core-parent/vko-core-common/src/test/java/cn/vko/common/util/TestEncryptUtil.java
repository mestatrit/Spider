package cn.vko.common.util;

import static cn.vko.core.common.util.EncryptUtil.*;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * @author 赵立伟
 * @Date 2013-2-1
 * @version 5.0.2
 */
public class TestEncryptUtil {
	@Test
	public void testEncryptAndDecrypt() {
		assertEquals("vDu6PnI/N1Yl9TvsxcqTOQ==", encrypt("斯蒂芬"));
		assertEquals("斯蒂芬", decrypt("vDu6PnI/N1Yl9TvsxcqTOQ=="));
	}

	// @Test
	public void testMd5() throws Exception {
		System.out.println(md5("lwz15933038651++"));
	}

	@Test(enabled = false)
	public void testDecrypt() throws Exception {
		System.out
				.println(encrypt("999999", "26BEA6081D37E8CEB83C3102113F5A82"));
	}
}
