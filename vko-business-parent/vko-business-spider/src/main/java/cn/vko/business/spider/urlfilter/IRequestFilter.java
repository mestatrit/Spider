package cn.vko.business.spider.urlfilter;

import java.util.List;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;

public interface IRequestFilter {
	public boolean canPush(Request request, Task task);

	public void clear(String uuid);

	public void addUrl(String uuid, List<String> url);

	public boolean isFilted(String uuid);
}
