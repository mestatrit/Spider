package cn.xuexibao.business.spider.cookie;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.testng.annotations.Test;

public class TZCookieQueueTest {

	@Test
	public void testPopCookie() throws Exception {
		TZCookieQueue tzCookieQueue = new TZCookieQueue();
		CookieStore cookie = tzCookieQueue.popCookie(null);
		for (Cookie c : cookie.getCookies()) {
			System.out.println("----" + c.getName() + "---" + c.getValue());
		}

	}
}
