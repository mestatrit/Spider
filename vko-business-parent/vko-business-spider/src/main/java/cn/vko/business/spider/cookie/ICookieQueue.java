package cn.vko.business.spider.cookie;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;

public interface ICookieQueue {
	public CookieStore popCookie(HttpHost proxy);
}
