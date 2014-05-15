package cn.vko.business.spider.pipeline;

import us.codecraft.webmagic.pipeline.PageModelPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;
import cn.vko.core.db.dao.IDbDao;

public interface IMysqlPipeline<T> extends PageModelPipeline<T> {
	public void clear(String uuid);

	public void addUrl(String uuid, FilterScheduler scheduler);

	public IDbDao getDao();
}
