package cn.vko.business.spider.pipeline;

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
public class MysqlPageModelSinglePipeline implements IMysqlPipeline<IExam> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MysqlPageModelSinglePipeline.class);
	private IDbDao dao;

	@Override
	public synchronized void process(IExam t, Task task) {
		if (logger.isInfoEnabled()) {
			logger.info("save url " + t.getUrl()); //$NON-NLS-1$
		}
		try {
			SpiderExam se = new SpiderExam();
			BeanUtil.copyProperties(t, se);
			se.setDomain(task.getUUID());
			se.setCrTime(DateTimeUtil.nowDateTime());
			if (se.getContent().length() > 65535
					|| se.getSolution().length() > 65535
					|| se.getRemark().length() > 65535) {
				// se.setContent(null);
				// se.setSolution(null);
				se.setRemark(null);
			}
			dao.insert(se);
		} catch (Exception e) {
			logger.error("save url ", e); //$NON-NLS-1$
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
			// Sql sql1 = Sqls.create("select url from spider_exam_tran_jym");
			Sql sql1 = Sqls.create("select url from spider_exam_jym_424");
			// sql.params().set("domain", uuid);
			s = DbSqlUtil.queryStringList(dao, sql1, p);
			scheduler.addUrl(uuid, s);
			p.setPageNumber(p.getPageNumber() + 1);
			if (logger.isInfoEnabled()) {
				logger.info("addUrl p=" + p.getPageNumber()); //$NON-NLS-1$
			}

		} while (s.size() == p.getPageSize());
	}
}
