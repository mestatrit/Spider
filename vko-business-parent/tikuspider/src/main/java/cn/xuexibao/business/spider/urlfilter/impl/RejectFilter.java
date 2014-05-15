package cn.xuexibao.business.spider.urlfilter.impl;

import java.util.List;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import cn.xuexibao.business.spider.urlfilter.IRequestFilter;

public class RejectFilter implements IRequestFilter {

	public boolean canPush(Request request, Task task) {
		if (request.getExtra(Request.CYCLE_TRIED_TIMES) != null) {
			return true;
		}
		return false;
	}

	public void clear(String uuid) {

	}

	public void addUrl(String uuid, List<String> url) {

	}

	public boolean isFilted(String uuid) {
		return true;
	}

	@Override
	public boolean existUrl(String url, String url2) {
		// TODO Auto-generated method stub
		return false;
	}

}
