package cn.xuexibao.business.spider.transfer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.selector.thread.CountableThreadPool;
import cn.vko.core.common.util.BeanUtil;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.impl.DbDao;
import cn.xuexibao.business.spider.transfer.model.BaseSpiderExam;
import cn.xuexibao.business.spider.transfer.model.BaseSpiderExamTran;
import cn.xuexibao.business.spider.transfer.model.SpiderExamJym;
import cn.xuexibao.business.spider.transfer.model.SpiderExamTranJym;

public class JyeooTransfer {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(JyeooTransfer.class);

	protected final AtomicInteger threadAlive = new AtomicInteger(0);
	protected final AtomicInteger pageCount = new AtomicInteger(0);
	protected final AtomicInteger status = new AtomicInteger(0);
	private ReentrantLock newUrlLock = new ReentrantLock();

	private Condition newUrlCondition = newUrlLock.newCondition();
	private DbDao dbDao;

	public JyeooTransfer(DbDao dbDao) {
		this.dbDao = dbDao;
	}

	public void transferData() throws Exception {
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
			logger.info("now tranfer page is " + pageCount.get());
		}
	}

	private synchronized <T extends BaseSpiderExam> List<T> getExams(
			Class<T> exam) {
		if (logger.isDebugEnabled()) {
			logger.debug("getExams(Class<T>) - start"); //$NON-NLS-1$
		}

		Pager p = new Pager();
		p.setPageSize(1000);
		List<T> exams;
		exams = dbDao.query(exam, Cnd.where("latexStatus", "=", 0), p);
		List<Long> ids = CollectionUtil.list();
		for (BaseSpiderExam s : exams) {
			ids.add(s.getId());
		}
		dbDao.update(exam, Chain.make("latexStatus", "1"),
				Cnd.where("id", "in", ids));

		if (logger.isDebugEnabled()) {
			logger.debug("getExams(Class<T>) - end"); //$NON-NLS-1$
		}
		return exams;
	}

	private <T extends BaseSpiderExam, V extends BaseSpiderExamTran> void tran(
			final List<T> exams, Class<T> examClass, Class<V> tranClass) {
		if (logger.isDebugEnabled()) {
			logger.debug("tran(List<T>, Class<T>, Class<V>) - start"); //$NON-NLS-1$
		}

		List<BaseSpiderExamTran> sls = CollectionUtil.list();
		List<Long> ids = CollectionUtil.list();
		for (T exam : exams) {
			V sl = null;
			try {
				sl = tranClass.newInstance();
			} catch (InstantiationException e) {
				logger.error("tran(List<T>, Class<T>, Class<V>)", e); //$NON-NLS-1$

				e.printStackTrace();
			} catch (IllegalAccessException e) {
				logger.error("tran(List<T>, Class<T>, Class<V>)", e); //$NON-NLS-1$

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
			dbDao.update(examClass, Chain.make("latexStatus", "2"),
					Cnd.where("id", "in", ids));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("tran(List<T>, Class<T>, Class<V>) - end"); //$NON-NLS-1$
		}
	}

	private void waitNewUrl() {
		if (logger.isDebugEnabled()) {
			logger.debug("waitNewUrl() - start"); //$NON-NLS-1$
		}

		try {
			newUrlLock.lock();
			// double check
			if (threadAlive.get() == 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("waitNewUrl() - end"); //$NON-NLS-1$
				}
				return;
			}
			try {
				newUrlCondition.await();
			} catch (InterruptedException e) {
				logger.warn("waitNewUrl() - exception ignored", e); //$NON-NLS-1$
			}
		} finally {
			newUrlLock.unlock();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("waitNewUrl() - end"); //$NON-NLS-1$
		}
	}
}
