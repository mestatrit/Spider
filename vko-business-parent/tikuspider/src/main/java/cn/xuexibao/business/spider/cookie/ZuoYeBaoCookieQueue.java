package cn.xuexibao.business.spider.cookie;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;

import cn.vko.core.db.dao.IDbDao;
import cn.xuexibao.business.spider.uucode.ZuoYeBaoRegister;

public class ZuoYeBaoCookieQueue implements ICookieQueue {
	private ZuoYeBaoRegister register;

	public ZuoYeBaoCookieQueue(IDbDao dbDao) {
		register = new ZuoYeBaoRegister();
		register.setDbDao(dbDao);
		register.init();
	}

	public CookieStore popCookie(HttpHost proxy) {
		return register.registerOneRepeat();
	}
}
