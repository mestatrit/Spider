package cn.vko.business.spider.work;

import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;

public abstract class YiTiKuWork extends BaseBlockWork {

	public YiTiKuWork(String uuid, String domain, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline, boolean useProxy) {
		super(uuid, domain, scheduler, pipeline, useProxy, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected OOSpider createSpider() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
