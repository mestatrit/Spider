package cn.xuexibao.business.spider.success;

import us.codecraft.webmagic.Request;

public class ZYBChecker implements ISuccessChecker {

	public boolean check(Request req) {
		if (req == null) {
			return false;
		}
		if (req.getUrl().indexOf("http://www.zuoyebao.com/q/") > -1) {
			return true;
		}
		return false;
	}

}
