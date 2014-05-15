package cn.vko.business.spider.urlfilter.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import cn.vko.business.spider.urlfilter.IRequestFilter;

public class SetFilter implements IRequestFilter {
	private Set<String> urls = Collections
			.synchronizedSet(new HashSet<String>());

	@Override
	public boolean canPush(Request request, Task task) {
		if (request.getExtra(Request.CYCLE_TRIED_TIMES) != null) {
			return true;
		}
		if (urls.add(request.getUrl())) {
			return true;
		}
		return false;
	}

	@Override
	public void clear(String uuid) {
		urls.clear();
	}

	@Override
	public void addUrl(String uuid, List<String> urlList) {
		urls.addAll(urlList);
	}

	@Override
	public boolean isFilted(String uuid) {
		return urls.size() > 0;
	}

}
