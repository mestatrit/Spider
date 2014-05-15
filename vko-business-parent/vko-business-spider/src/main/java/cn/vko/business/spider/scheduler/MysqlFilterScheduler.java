package cn.vko.business.spider.scheduler;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;

@Data
@EqualsAndHashCode(callSuper = true)
public class MysqlFilterScheduler extends FilterScheduler {
	private IDbDao dao;
	private List<Record> works;
	private static final int LEFT = "http://www.zuoyebao.com/q/".length();
	private static Sql sql = Sqls
			.create("select id,url from spider_exam where domain='zuoyebao.com' and status=0 and solution like '%登录菁优网%' ");
	private static Sql sqlCount = Sqls
			.create("select count(1) from spider_exam where domain='zuoyebao.com' and status=0 and solution like '%登录菁优网%' ");

	@Override
	public synchronized Request poll(Task task) {
		if (Util.isEmpty(works)) {
			works = DbSqlUtil.query(dao, sql, sqlCount,
					new Pager().setPageSize(1000));
		}
		if (Util.isEmpty(works)) {
			return null;
		}
		Record one = works.remove(0);
		String url = one.getString("url");
		String qId = url.substring(LEFT);
		Request req = new Request(qId);
		req.setExtras(one);
		return req;
	}

	@Override
	public void pushWhenNoDuplicate(Request request, Task task) {
		return;
	}

	@Override
	public void clear(String uuid) {

	}

	@Override
	public boolean isScheduled(String uuid) {
		return true;
	}

	@Override
	public boolean isFilted(String uuid) {
		return false;
	}

	@Override
	public void addUrl(String uuid, List<String> url) {

	}

	@Override
	public void addSchedulerUrl(String uuid) {

	}

	@Override
	public void addDownloadUrl(String uuid, List<String> url) {

	}

	@Override
	public void push(String uuid, String... url) {

	}

}
