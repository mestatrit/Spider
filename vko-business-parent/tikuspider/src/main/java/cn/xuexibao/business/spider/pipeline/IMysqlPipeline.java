package cn.xuexibao.business.spider.pipeline;

import us.codecraft.webmagic.pipeline.PageModelPipeline;
import cn.vko.core.db.dao.IDbDao;
import cn.xuexibao.business.spider.scheduler.FilterScheduler;

public interface IMysqlPipeline<T> extends PageModelPipeline<T> {
	public void clear(String uuid);

	public void addUrl(String uuid, FilterScheduler scheduler);

	public IDbDao getDao();
}
