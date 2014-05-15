package cn.vko.business.spider.cookie;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;

import cn.vko.business.spider.uucode.ZuoYeBaoRegister;
import cn.vko.core.db.dao.IDbDao;

public class ZuoYeBaoCookieQueue implements ICookieQueue {
	private ZuoYeBaoRegister register;

	public ZuoYeBaoCookieQueue(IDbDao dbDao) {
		register = new ZuoYeBaoRegister();
		register.setDbDao(dbDao);
		register.init();
	}

	@Override
	public CookieStore popCookie(HttpHost proxy) {
		return register.registerOneRepeat();
	}
}
