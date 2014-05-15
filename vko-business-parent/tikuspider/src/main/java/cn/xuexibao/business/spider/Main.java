package cn.xuexibao.business.spider;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.db.dao.impl.DbDao;
import cn.xuexibao.business.spider.multiwork.JYFilterWork;
import cn.xuexibao.business.spider.multiwork.JYWork;
import cn.xuexibao.business.spider.multiwork.MfgWork;
import cn.xuexibao.business.spider.multiwork.MultiWork;
import cn.xuexibao.business.spider.multiwork.TiZiWork;
import cn.xuexibao.business.spider.multiwork.WuYouWork;
import cn.xuexibao.business.spider.multiwork.XKWork;
import cn.xuexibao.business.spider.multiwork.ZuoYBWork;
import cn.xuexibao.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.xuexibao.business.spider.pipeline.MysqlPageUpdatePipeline;
import cn.xuexibao.business.spider.scheduler.RedisFilterScheduler;

public class Main {
	public static final void main(String[] args) {
		if (args.length == 0) {
			System.out
					.println("usage is nohup java -jar spider.jar [j,m,z,ym,yc] 1000 [c,cd] &  ");
			System.out.println(" [j,m,z,y] please choose one  ");
			System.out.println(" 1000 is thread num, default is 100 ");
			System.out.println(" c is clear cache, cd is clear cache+db ");
			return;
		}
		Ioc ioc = new NutIoc(new JsonLoader("/usr/tikuspider/config"));
		DbDao dao = ioc.get(DbDao.class, "dao");
		MysqlPageModelSinglePipeline p = ioc.get(
				MysqlPageModelSinglePipeline.class, "pipeline");
		RedisFilterScheduler s = ioc.get(RedisFilterScheduler.class,
				"scheduler");
		MysqlPageUpdatePipeline up = ioc.get(MysqlPageUpdatePipeline.class,
				"updatepl");
		RedisFilterScheduler rs = ioc.get(RedisFilterScheduler.class,
				"rejectsc");

		String type = "j";
		int thread = 100;
		if (args.length == 1) {
			type = args[0];
		} else if (args.length == 2) {
			type = args[0];
			thread = ConvertUtil.obj2int(args[1]);
			if (thread == -1) {
				thread = 100;
			}
		}
		// String select = "select solution from spider_exam_jym_424";
		// ImgDownloader imgDownloader = new ImgDownloader(dao);
		// try {
		// imgDownloader.download("/usr/tikuspider/imgs424", select);
		// } catch (URISyntaxException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		MultiWork work = null;
		if ("j".equals(type)) {
			work = new JYWork(thread, s, p);
			// work.setExitWhenComplete(false);
		} else if ("jf".equals(type)) {
			// 注意这个是特殊的 scheduler和pipeline
			work = new JYFilterWork(thread, rs, p, dao);
			work.setExitWhenComplete(false);
		} else if ("z".equals(type)) {
			work = new ZuoYBWork(thread, s, p, dao);
			work.setExitWhenComplete(false);
		} else if ("w".equals(type)) {
			work = new WuYouWork(thread, s, p);
			work.setExitWhenComplete(false);
		} else if ("m".equals(type)) {
			work = new MfgWork(thread, s, p);
		} else if ("x".equals(type)) {
			work = new XKWork(thread, s, p);
			work.setExitWhenComplete(false);
		} else if ("t".equals(type)) {
			work = new TiZiWork(thread, s, p);
			work.setExitWhenComplete(false);
		} else {
			System.out
					.println("usage is nohup java -jar spider.jar [j,m,z,ym,yc] 1000 &  ");
			System.out
					.println(" [j,m,z,ym,yc] please choose one ,please don't input "
							+ type);
			System.out.println(" 1000 is thread num, default is 100 ");
			return;
		}
		if (args.length == 3) {
			if ("c".equals(args[2])) {
				work.clearCache();
				System.out.println("clear cache over! ");
				return;
			} else if ("cd".equals(args[2])) {
				work.clearAll();
				System.out.println("clear cache+db over! ");
				return;
			}
		}
		work.run();
		/*
		 * // 并行数据清洗 final JyeooTransfer jyeooTransfer = new JyeooTransfer(dao);
		 * final ImgDownloader imgDownloader = new ImgDownloader(dao);
		 * ExecutorService ex = ThreadUtils.newFixedThreadPool(2); final
		 * AtomicInteger i = new AtomicInteger(0); ex.execute(new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * try { String selectsql = getSelectSql(i.get()); i.getAndIncrement();
		 * if (Util.isEmpty(selectsql)) { try { jyeooTransfer.transferData(); }
		 * catch (Exception e) { e.printStackTrace(); } } else {
		 * imgDownloader.download("d:\\dealImage/417imgs", selectsql); }
		 * 
		 * } catch (URISyntaxException e) { e.printStackTrace(); }
		 * 
		 * } });
		 */

	}

	/*
	 * private static String getSelectSql(int i) { if (i == 2) { return null; }
	 * String[] sqls = new String[] {
	 * "select solution from spider_exam_jym_420",
	 * "select content from spider_exam_jym_420" }; return sqls[i]; }
	 */
}
