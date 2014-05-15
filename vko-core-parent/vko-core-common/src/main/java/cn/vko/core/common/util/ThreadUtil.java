package cn.vko.core.common.util;

public class ThreadUtil {
	public static void sleep(long mills) {
		if (mills <= 0) {
			return;
		}
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
		}
	}
}
