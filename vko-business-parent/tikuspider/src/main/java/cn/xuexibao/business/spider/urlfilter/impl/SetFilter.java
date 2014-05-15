package cn.xuexibao.business.spider.urlfilter.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import cn.xuexibao.business.spider.urlfilter.IRequestFilter;

public class SetFilter implements IRequestFilter {
	private Set<String> urls = Collections
			.synchronizedSet(new HashSet<String>());

	public boolean canPush(Request request, Task task) {
		if (request.getExtra(Request.CYCLE_TRIED_TIMES) != null) {
			return true;
		}
		if (urls.add(request.getUrl())) {
			return true;
		}
		return false;
	}

	public void clear(String uuid) {
		urls.clear();
	}

	public void addUrl(String uuid, List<String> urlList) {
		urls.addAll(urlList);
	}

	public boolean isFilted(String uuid) {
		return urls.size() > 0;
	}

	@Override
	public boolean existUrl(String url, String url2) {
		// TODO Auto-generated method stub
		return false;
	}

}
