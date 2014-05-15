package cn.vko.business.spider.urlfilter.impl;

import java.util.List;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import cn.vko.business.spider.urlfilter.IRequestFilter;

public class RejectFilter implements IRequestFilter {

	@Override
	public boolean canPush(Request request, Task task) {
		if (request.getExtra(Request.CYCLE_TRIED_TIMES) != null) {
			return true;
		}
		return false;
	}

	@Override
	public void clear(String uuid) {

	}

	@Override
	public void addUrl(String uuid, List<String> url) {

	}

	@Override
	public boolean isFilted(String uuid) {
		return true;
	}

}
