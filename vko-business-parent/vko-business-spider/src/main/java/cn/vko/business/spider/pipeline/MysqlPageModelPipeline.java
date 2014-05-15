package cn.vko.business.spider.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Task;
import cn.vko.business.spider.entity.SpiderExam;
import cn.vko.business.spider.model.IExam;
import cn.vko.business.spider.scheduler.FilterScheduler;
import cn.vko.core.common.util.BeanUtil;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.DateTimeUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;

@Data
public class MysqlPageModelPipeline implements IMysqlPipeline<IExam> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MysqlPageModelPipeline.class);

	private List<SpiderExam> exams = Collections
			.synchronizedList(new ArrayList<SpiderExam>(100));
	private IDbDao dao;

	@Override
	public synchronized void process(IExam t, Task task) {
		SpiderExam se = new SpiderExam();
		BeanUtil.copyProperties(t, se);
		se.setDomain(task.getUUID());
		se.setCrTime(DateTimeUtil.nowDateTime());
		exams.add(se);
		if (exams.size() > 100) {
			dao.insert(exams);
			exams.clear();
		}
	}

	@Override
	public void clear(String uuid) {
		Sql sql = Sqls.create("delete from spider_exam where domain=@uuid");
		sql.params().set("uuid", uuid);
		dao.excute(sql);
	}

	@Override
	public void addUrl(String uuid, FilterScheduler scheduler) {
		Pager p = new Pager();
		p.setPageSize(1000);
		List<String> s = CollectionUtil.list();

		do {
			Sql sql = Sqls
					.create("select url from spider_exam where domain=@domain");
			sql.params().set("domain", uuid);
			s = DbSqlUtil.queryStringList(dao, sql, p);
			scheduler.addUrl(uuid, s);
			p.setPageNumber(p.getPageNumber() + 1);
			if (logger.isInfoEnabled()) {
				logger.info("addUrl p=" + p.getPageNumber()); //$NON-NLS-1$
			}

		} while (s.size() == p.getPageSize());
	}
}
