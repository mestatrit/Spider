package cn.vko.business.spider.transfer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import us.codecraft.webmagic.selector.thread.CountableThreadPool;
import cn.vko.business.spider.model.BaseSpiderExam;
import cn.vko.business.spider.model.BaseSpiderExamTran;
import cn.vko.business.spider.model.SpiderExamJym;
import cn.vko.business.spider.model.SpiderExamTranJym;
import cn.vko.core.common.util.BeanUtil;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;

public class TransferPhysical {
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
	public void testTransPhysical() throws Exception {
		ioc = new NutIoc(new JsonLoader("spider/config/pipeline.js"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		// nutDao.create(SpiderExamTranJym.class, false);
		CountableThreadPool threadPool = new CountableThreadPool(10);
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

	public static void main(String[] args) {
		IDbDao dbDao;
		NutDao nutDao;
		Ioc ioc;
		ioc = new NutIoc(new JsonLoader("spider/config/pipeline.js"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		List<SpiderExamTranJym> spiderExamTranJyms = dbDao.query(
				SpiderExamTranJym.class, Cnd.wrap("latex like '%Ω%'"), null);
		int i = 0;
		for (SpiderExamTranJym spiderExamTranJym : spiderExamTranJyms) {
			spiderExamTranJym.setLatex(Transfer
					.replaceMathSign(spiderExamTranJym.getLatex()));
			dbDao.update(spiderExamTranJym, "latex");
			i++;
		}
		System.out.println(i);
		// 10022
	}
}
