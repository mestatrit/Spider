package cn.vko.business.spider.success;

import us.codecraft.webmagic.Request;

public interface ISuccessChecker {
	public boolean check(Request req);
}
