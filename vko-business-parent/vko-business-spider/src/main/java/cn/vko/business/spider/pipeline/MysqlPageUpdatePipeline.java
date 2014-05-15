package cn.vko.business.spider.pipeline;

import lombok.Data;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Task;
import cn.vko.business.spider.entity.SpiderExam;
import cn.vko.business.spider.model.IExam;
import cn.vko.business.spider.scheduler.FilterScheduler;
import cn.vko.core.common.util.BeanUtil;
import cn.vko.core.db.dao.IDbDao;

@Data
public class MysqlPageUpdatePipeline implements IMysqlPipeline<IExam> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MysqlPageUpdatePipeline.class);
	private IDbDao dao;
	private static final String UPDATE = "update spider_exam set solution=@s,remark=@r where url=@url";

	@Override
	public synchronized void process(IExam t, Task task) {
		SpiderExam se = new SpiderExam();
		BeanUtil.copyProperties(t, se);
		if (se.getSolution().contains("登录菁优网")) {
			logger.info("update url " + se.getUrl() + " fail,is real vip?");
			return;
		}
		Sql up = Sqls.create(UPDATE);
		up.params().set("s", se.getSolution());
		up.params().set("r", se.getRemark());
		up.params().set("url", se.getUrl());
		dao.excute(up);
		logger.info("update url " + se.getUrl() + " success");
	}

	@Override
	public void clear(String uuid) {
	}

	@Override
	public void addUrl(String uuid, FilterScheduler scheduler) {
	}
}
