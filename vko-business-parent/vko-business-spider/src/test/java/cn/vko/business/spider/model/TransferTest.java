package cn.vko.business.spider.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import us.codecraft.webmagic.selector.thread.CountableThreadPool;
import cn.vko.business.spider.transfer.Transfer;
import cn.vko.core.common.util.BeanUtil;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.JsonUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;
import cn.vko.core.web.util.FtlUtil;
import freemarker.template.Configuration;

public class TransferTest {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TransferTest.class);

	private IDbDao dbDao;
	private NutDao nutDao;
	private Ioc ioc;
	protected final AtomicInteger threadAlive = new AtomicInteger(0);
	protected final AtomicInteger pageCount = new AtomicInteger(0);
	protected final AtomicInteger status = new AtomicInteger(0);
	private ReentrantLock newUrlLock = new ReentrantLock();

	private Condition newUrlCondition = newUrlLock.newCondition();

	@Test
	public void testTrans() throws Exception {

		Element e = Jsoup.parse(
				this.getClass().getResourceAsStream("/example.html"), "UTF-8",
				"xx");
		List<Element> tables = e.select("table");
		for (Element t : tables) {
			System.out.println(t.child(0).children().size());
			if (3 == t.child(0).children().size()) {
				System.out.println(t);
			}
		}

	}

	@Test
	public void testTrans2() throws Exception {

		Element e = Jsoup.parse(
				this.getClass().getResourceAsStream("/example.html"), "UTF-8",
				"xx");
		List<Element> tables = e.select(".MathJye");
		for (Element t : tables) {
			String s = Transfer.transfer(t);
			System.out.println(s);
			System.out.println("-------------------------------------------");
			t.replaceWith(new TextNode(s, ""));
		}
		System.out.println(e);
	}

	@Test
	public void testTrans3() throws Exception {
		ioc = new NutIoc(new JsonLoader("spider/config/pipeline.js"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		// nutDao.create(SpiderExamTran.class, false);
		CountableThreadPool threadPool = new CountableThreadPool(4);
		while (true) {
			final List<SpiderExamJym> exams = getExams(SpiderExamJym.class);
			if (Util.isEmpty(exams)) {
				if (threadAlive.get() == 0) {
					break;
				}
				waitNewUrl();
				continue;
			}
			threadAlive.incrementAndGet();
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						tran(exams, SpiderExamJym.class,
								SpiderExamTranJym.class);
					} finally {
						threadAlive.decrementAndGet();
						pageCount.incrementAndGet();
					}
				}

			});
			System.out.println(pageCount.get());
		}
	}

	@Test
	public void testTrans4() throws Exception {
		ioc = new NutIoc(new JsonLoader("spider/config/pipeline.js"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		nutDao.create(SpiderExamTranMfe.class, false);
		CountableThreadPool threadPool = new CountableThreadPool(10);
		final Configuration cfg = new Configuration();
		FtlUtil.initConfiguration(cfg, "ftl/", "", "ftl/ftl.properties");
		while (true) {
			final List<SpiderExamMfe> exams = getExams(SpiderExamMfe.class);
			if (Util.isEmpty(exams)) {
				if (threadAlive.get() == 0) {
					break;
				}
				waitNewUrl();
				continue;
			}
			threadAlive.incrementAndGet();
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						tranMfe(exams, SpiderExamMfe.class,
								SpiderExamTranMfe.class, cfg);
					} finally {
						threadAlive.decrementAndGet();
						pageCount.incrementAndGet();
					}
				}

			});
			System.out.println(pageCount.get());
		}
	}

	private synchronized <T extends BaseSpiderExam> List<T> getExams(
			Class<T> exam) {
		Pager p = new Pager();
		p.setPageSize(1000);
		List<T> exams;
		exams = dbDao.query(exam, Cnd.where("status", "=", 0), p);
		List<Long> ids = CollectionUtil.list();
		for (BaseSpiderExam s : exams) {
			ids.add(s.getId());
		}
		dbDao.update(exam, Chain.make("status", "1"),
				Cnd.where("id", "in", ids));
		return exams;
	}

	private synchronized <T extends BaseSpiderExamTran> List<T> getTranExams(
			Class<T> exam) {
		Pager p = new Pager();
		p.setPageSize(1000);
		List<T> exams;
		exams = dbDao.query(exam, Cnd.where("status", "=", 0), p);
		List<Long> ids = CollectionUtil.list();
		for (BaseSpiderExamTran s : exams) {
			ids.add(s.getId());
		}
		dbDao.update(exam, Chain.make("status", "1"),
				Cnd.where("id", "in", ids));
		return exams;
	}

	private <T extends BaseSpiderExam, V extends BaseSpiderExamTran> void tran(
			final List<T> exams, Class<T> examClass, Class<V> tranClass) {
		List<BaseSpiderExamTran> sls = CollectionUtil.list();
		List<Long> ids = CollectionUtil.list();
		for (T exam : exams) {
			V sl = null;
			try {
				sl = tranClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			BeanUtil.copyProperties(exam, sl);
			sl.setContent(Transfer.rmEnter(sl.getContent()));
			sl.setLatex(Transfer.replaceMathSign(Transfer.transfer(exam
					.getContent())));
			sl.setAnswer(Transfer.getJyeooRightAnswer(exam.getContent()));
			sl.setKnowledge(Transfer.getJyeooKnowledge(exam.getRemark()));
			sl.setSolution(Transfer.rmEnter(sl.getSolution()));
			sls.add(sl);
			ids.add(exam.getId());
		}
		if (sls.size() > 0) {
			dbDao.insert(sls);
		}
		if (ids.size() > 0) {
			dbDao.update(examClass, Chain.make("status", "2"),
					Cnd.where("id", "in", ids));
		}
	}

	private void tranD2(List<SpiderExamTranJym> exams,
			Class<SpiderExamTranJym> class1) {
		List<Long> ids = CollectionUtil.list();
		for (SpiderExamTranJym exam : exams) {
			exam.setLatex(Transfer.replaceMathSign(Transfer.transfer(exam
					.getContent())));
			ids.add(exam.getId());
			dbDao.update(exam, "latex");
		}
		if (ids.size() > 0) {
			dbDao.update(class1, Chain.make("status", "2"),
					Cnd.where("id", "in", ids));
		}
	}

	private <T extends BaseSpiderExam, V extends BaseSpiderExamTran> void tranMfe(
			final List<T> exams, Class<T> examClass, Class<V> tranClass,
			Configuration cfg) {
		List<BaseSpiderExamTran> sls = CollectionUtil.list();
		List<Long> ids = CollectionUtil.list();
		for (T exam : exams) {
			V sl = null;
			try {
				sl = tranClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			BeanUtil.copyProperties(exam, sl);
			sl.setContent(Transfer.transStyle(sl.getContent(), sl.getType(),
					cfg));
			sl.setLatex(Transfer.getMangFGText(sl.getContent()));
			sl.setAnswer(Transfer.getMangFGAnswer(sl.getAnswer()));
			sl.setKnowledge(Transfer.getMangFGText(sl.getKnowledge()));
			sl.setDiff(Transfer.getMangFGDiff(sl.getDiff()));
			sl.setSource(Transfer.getMangFGText(sl.getSource()));
			sl.setType(Transfer.getMangFGType(sl.getType()));
			sls.add(sl);
			ids.add(exam.getId());
		}
		if (sls.size() > 0) {
			dbDao.insert(sls);
		}
		if (ids.size() > 0) {
			dbDao.update(examClass, Chain.make("status", "2"),
					Cnd.where("id", "in", ids));
		}
	}

	private void waitNewUrl() {
		try {
			newUrlLock.lock();
			// double check
			if (threadAlive.get() == 0) {
				return;
			}
			try {
				newUrlCondition.await();
			} catch (InterruptedException e) {
			}
		} finally {
			newUrlLock.unlock();
		}
	}

	/**
	 * 统计知识点分布
	 * 
	 * @throws Exception
	 */
	@Test
	public void tongji() throws Exception {
		ioc = new NutIoc(new JsonLoader("spider/config/pipeline.js"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		CountableThreadPool threadPool = new CountableThreadPool(10);
		final ConcurrentMap<String, ConcurrentHashMap<String, AtomicLong>> concurrentHashMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicLong>>();
		while (true) {
			final List<Record> records = getTranExams();
			if (Util.isEmpty(records)) {
				if (threadAlive.get() == 0) {
					break;
				}
				waitNewUrl();
				continue;
			}
			threadAlive.incrementAndGet();
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						getData(records, concurrentHashMap);
					} finally {
						threadAlive.decrementAndGet();
						pageCount.incrementAndGet();
					}
				}
			});
			System.out.println(pageCount.get());
		}
		System.out.println(JsonUtil.toJson(concurrentHashMap));
	}

	private synchronized void getData(
			List<Record> records,
			ConcurrentMap<String, ConcurrentHashMap<String, AtomicLong>> concurrentHashMap) {
		for (Record record : records) {
			String subject = Transfer.getMangFGText((String) record
					.get("subject"));
			String knowlege = (String) record.get("knowledge");
			ConcurrentHashMap<String, AtomicLong> map = new ConcurrentHashMap<String, AtomicLong>();
			if (Util.isEmpty(knowlege)) {
				return;
			}
			if (!concurrentHashMap.containsKey(subject)) {
				addKnowNoSub(concurrentHashMap, subject, knowlege, map);
			} else {
				addKnowHSub(concurrentHashMap, subject, knowlege);
			}
		}
	}

	private void addKnowHSub(
			ConcurrentMap<String, ConcurrentHashMap<String, AtomicLong>> concurrentHashMap,
			String subject, String knowlege) {
		ConcurrentHashMap<String, AtomicLong> eMap = concurrentHashMap
				.get(subject);
		if (knowlege.indexOf(",") > -1) {
			String[] knowleges = knowlege.split(",");
			for (int i = 0; i < knowleges.length; i++) {
				addKnow(knowleges[i], eMap);
			}
		} else {
			addKnow(knowlege, eMap);
		}
	}

	private void addKnowNoSub(
			ConcurrentMap<String, ConcurrentHashMap<String, AtomicLong>> concurrentHashMap,
			String subject, String knowlege,
			ConcurrentHashMap<String, AtomicLong> map) {
		if (knowlege.indexOf(",") > -1) {
			String[] knowleges = knowlege.split(",");
			for (int i = 0; i < knowleges.length; i++) {
				map.putIfAbsent(knowleges[i], new AtomicLong(1));
			}
		} else {
			map.put(knowlege, new AtomicLong(1));
		}

		concurrentHashMap.putIfAbsent(subject, map);
	}

	private void addKnow(String knowlege, ConcurrentMap<String, AtomicLong> eMap) {
		if (eMap.containsKey(knowlege)) {
			long now = eMap.get(knowlege).getAndIncrement();
			eMap.putIfAbsent(knowlege, new AtomicLong(now));
		} else {
			eMap.putIfAbsent(knowlege, new AtomicLong(1));
		}
	}

	private List<Record> getTranExams() {
		Pager p = new Pager();
		p.setPageSize(1);
		List<Record> records;
		Sql sql = Sqls
				.create("select id, subject,knowledge from spider_exam_tran_jym_bak  where status=0  ");
		records = dbDao.query(sql, null, p);

		List<Long> ids = CollectionUtil.list();
		for (Record s : records) {
			ids.add((Long) s.get("id"));
		}
		dbDao.update(SpiderExamTranJym.class, Chain.make("status", "1"),
				Cnd.where("id", "in", ids));
		return records;
	}

	@Test
	public void readTxt() throws Exception {
		FileReader reader = null;
		BufferedReader br = null;
		try {
			// read file content from file
			StringBuffer sb = new StringBuffer("");

			reader = new FileReader("d://know.txt");
			br = new BufferedReader(reader);

			String str = null;

			while ((str = br.readLine()) != null) {
				String s = str.split(" :")[0].replaceAll("\"", "");
				// String s = str.split(" :")[1].substring(0,
				// str.split(" :")[1].length() - 1);
				sb.append(s + "/n");
				System.out.println(s);
			}
		} finally {
			br.close();
			reader.close();
		}
	}

	@Test
	public void test() throws Exception {
		String s = " <!--BA--><div class=\"quizPutTag\">  -3 </div><!--EA--><div class=\"sanwser\">  -3 </div>";
		System.out.println(Transfer.transfer(s));
	}

	@Test
	public void testTransBug() throws Exception {
		ioc = new NutIoc(new JsonLoader("spider/config/pipeline.js"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		// nutDao.create(SpiderExamTran.class, false);
		CountableThreadPool threadPool = new CountableThreadPool(10);
		while (true) {
			final List<SpiderExamTranJym> exams = getTranExams(SpiderExamTranJym.class);
			if (Util.isEmpty(exams)) {
				if (threadAlive.get() == 0) {
					break;
				}
				waitNewUrl();
				continue;
			}
			threadAlive.incrementAndGet();
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						tranD2(exams, SpiderExamTranJym.class);
					} finally {
						threadAlive.decrementAndGet();
						pageCount.incrementAndGet();
					}
				}

			});
			System.out.println(pageCount.get());
		}
	}
}
