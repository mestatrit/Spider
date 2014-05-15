package cn.vko.business.spider.cookie;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

import cn.vko.business.spider.login.HttpClientLogin;

public class YiCookieQueue implements ICookieQueue {

	@Override
	public CookieStore popCookie(HttpHost proxy) {
		String loginUrl = "http://www.yitiku.cn/Tiku/User/login";
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", "flowers10@126.com");
		map.put("password", "1988zhangch");
		try {
			BasicCookieStore cookieStore = HttpClientLogin.getCookie(loginUrl,
					map, proxy);
			return cookieStore;
		} catch (Exception e) {
			return null;
		}
	}

}
