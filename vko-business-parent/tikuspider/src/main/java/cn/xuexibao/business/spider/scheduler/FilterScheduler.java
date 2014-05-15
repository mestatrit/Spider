package cn.xuexibao.business.spider.scheduler;

import java.util.List;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;
import cn.xuexibao.business.spider.urlfilter.IRequestFilter;
import cn.xuexibao.business.spider.urlfilter.impl.SetFilter;

@Data
public abstract class FilterScheduler implements Scheduler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(FilterScheduler.class);

	protected IRequestFilter filter = new SetFilter();

	public void push(Request request, Task task) {
		logger.debug("push to queue " + request.getUrl());
		if (filter.canPush(request, task)) {
			pushWhenNoDuplicate(request, task);
		}
	}

	public abstract void pushWhenNoDuplicate(Request request, Task task);

	public abstract void clear(String uuid);

	public abstract boolean isScheduled(String uuid);

	public abstract boolean isFilted(String uuid);

	public abstract void addUrl(String uuid, List<String> url);

	public abstract void addSchedulerUrl(String uuid);

	public abstract void addDownloadUrl(String uuid, List<String> url);

	public abstract void push(final String uuid, String... url);
}
