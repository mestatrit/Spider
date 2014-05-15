package cn.xuexibao.business.spider.uucode;

import org.testng.annotations.Test;

import us.codecraft.webmagic.selector.thread.CountableThreadPool;
import cn.xuexibao.business.spider.uucode.UUCodeUtil.DM;

public class UUCodeUtilTest {

	@Test
	public void testGetImageCode() throws Exception {
		CountableThreadPool threadPool = new CountableThreadPool(10);
		while (true) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					UUCodeUtil.isLinux();
					DM.INSTANCE.uu_loginA("aa", "bb");
				}
			});
		}
	}
}
