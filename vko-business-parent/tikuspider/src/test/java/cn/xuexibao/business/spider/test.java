package cn.xuexibao.business.spider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class test {
	public static void main(String[] args) throws Exception {
		ExecutorService ex = Executors.newFixedThreadPool(3);
		final AtomicInteger i = new AtomicInteger(0);

		ex.execute(new Runnable() {
			@Override
			public void run() {
				String selectsql = getSelectSql(i.get());
				i.getAndIncrement();
				if (selectsql == null) {
					try {
						System.out.println("method tanrfer----");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("method tanrfer" + selectsql);
				}
				System.out.println("Thread i----" + i.get());
			}

		});
		System.out.println("i----" + i.get());

	}

	private synchronized static String getSelectSql(int i) {
		if (i == 2) {
			return null;
		}
		String[] sqls = new String[] {
				"select solution from spider_exam_jym_420",
				"select content from spider_exam_jym_420" };
		return sqls[i];
	}
}
